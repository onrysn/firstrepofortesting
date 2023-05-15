package mobit.eemr.base;

public class hdlc {

	public static final int AARQ = 0x60;
	public static final int AARE = 0x61;

	/* http://www.acacia-net.com/wwwcla/protocol/iso_4335.htm#I%20format */
	public static int HDLC_INFORMATION_FRAME_CONTROL(int ns, int poll, int nr) {
		return (((nr & 7) << 5) | (poll << 4) | ((ns & 7) << 1) | 0);
	}

	/* http://www.acacia-net.com/wwwcla/protocol/iso_4335.htm#U%20format */
	public static int HDLC_UNNUMBERED_FRAME_CONTROL(int mm, int poll, int mmmm) {
		return (((mmmm & 7) << 5) | (poll << 4) | ((mm & 3) << 2) | 3);
	}

	public static final int HDLC_SNRM_REQUEST = HDLC_UNNUMBERED_FRAME_CONTROL(0, 1, 4);
	public static final int HDLC_DISCONNECT_REQUEST = HDLC_UNNUMBERED_FRAME_CONTROL(0, 1, 2);

	public static void SendHdlcFrame(ICommStream pStream, byte[] pDestAddr, int destAddrLen, byte[] pSrcAddr,
			int srcAddrLen, byte control, byte[] pData, int dataSize) throws Exception {
		int bufPos;
		byte[] pFrameBuffer;
		int[] bytesWritten = new int[1];
		int frameFormat, fcs16;
		int segmentation, frameLength;

		bufPos = 0;
		segmentation = 0;
		pFrameBuffer = new byte[6 + destAddrLen + srcAddrLen];

		/*
		 * The value of the frame length subfield is the count of octets in the
		 * frame excluding the opening and closing frame flag sequences
		 */
		frameLength = 2 + destAddrLen + srcAddrLen + 1 + 2;
		if (dataSize > 0)
			frameLength += dataSize + 2;

		frameFormat = ((0x0a << 12) | (segmentation & 0x01) << 11) | (frameLength & 0x7ff);

		// Flag field
		pFrameBuffer[bufPos++] = 0x7e;

		// Frame format field
		pFrameBuffer[bufPos++] = (byte) ((frameFormat >> 8) & 0xff);
		pFrameBuffer[bufPos++] = (byte) (frameFormat & 0xff);

		// Destination address field
		// memcpy(pFrameBuffer + bufPos, pDestAddr, destAddrLen);
		System.arraycopy(pDestAddr, 0, pFrameBuffer, bufPos, destAddrLen);
		bufPos += destAddrLen;

		// Source address field
		System.arraycopy(pSrcAddr, 0, pFrameBuffer, bufPos, srcAddrLen);
		// memcpy(pFrameBuffer + bufPos, pSrcAddr, srcAddrLen);
		bufPos += srcAddrLen;

		// Control field
		pFrameBuffer[bufPos++] = control;

		// Header check sequence field

		fcs16 = mobit.eemr.base.fcs16.pppfcs16(mobit.eemr.base.fcs16.PPPINITFCS16, pFrameBuffer, 1, bufPos - 1);
		pFrameBuffer[bufPos++] = (byte) ((fcs16 ^ 0xffff) & 0xff);
		pFrameBuffer[bufPos++] = (byte) (((fcs16 ^ 0xffff) >> 8) & 0xff);

		fcs16 = mobit.eemr.base.fcs16.pppfcs16(fcs16, pFrameBuffer, bufPos - 2, 2);
		pStream.Write(pFrameBuffer, bufPos, bytesWritten);
		if (bytesWritten[0] != bufPos)
			throw new Exception("Başlık kontrol dizesi yazılamadı!");

		bufPos = 0;
		if (dataSize > 0) {
			// Information field
			pStream.Write(pData, dataSize, bytesWritten);
			if (bytesWritten[0] != dataSize)
				throw new Exception("Bilgi alanı yazılamadı!");

			// Frame check sequence field
			fcs16 = mobit.eemr.base.fcs16.pppfcs16(fcs16, pData, 0, dataSize);
			pFrameBuffer[bufPos++] = (byte)((fcs16 ^ 0xffff) & 0xff);
			pFrameBuffer[bufPos++] = (byte)(((fcs16 ^ 0xffff) >> 8) & 0xff);
		}

		// Flag field
		pFrameBuffer[bufPos++] = 0x7e;
		pStream.Write(pFrameBuffer, bufPos, bytesWritten);
		if (bytesWritten[0] != bufPos)
			throw new Exception("Bayrak alanı yazılamadı!");
		
		
		pStream.FlushWrite();
	}

	public static int ReadHdlcFrame(ICommStream pStream, byte[] pData, int maxDataSize) throws Exception {
		byte[] chr1 = new byte[1];
		byte[] chr2 = new byte[1];
		int wordValue, fcs16;
		int frameLength, dataSize;

		// Flag field
		if (!utils.ReadByte(pStream, chr1) || chr1[0] != 0x7e)
			return (-1);

		// Frame format field
		if (!utils.ReadByte(pStream, chr1) || !utils.ReadByte(pStream, chr2))
			return (-1);

		fcs16 = mobit.eemr.base.fcs16.pppfcs16(mobit.eemr.base.fcs16.PPPINITFCS16, chr1, 0, 1);
		fcs16 = mobit.eemr.base.fcs16.pppfcs16(fcs16, chr2, 0, 1);

		wordValue = (chr1[0] << 8) | chr2[0];
		if (wordValue >> 12 != 0x0a)
			return (-1);

		// Segmentation not supported
		if (((wordValue >> 11) & 0x01) != 0)
			return (-1);

		frameLength = wordValue & 0x7ff;
		dataSize = frameLength - 2 - 1 - 1 - 1 - 2;
		if (dataSize > 0)
			dataSize -= 2;

		if (dataSize > maxDataSize)
			return (-1);

		// Destination address field
		if (!utils.ReadByte(pStream, chr1))
			return (-1);
		fcs16 = mobit.eemr.base.fcs16.pppfcs16(fcs16, chr1, 0, 1);

		// Source address field
		if (!utils.ReadByte(pStream, chr1))
			return (-1);
		fcs16 = mobit.eemr.base.fcs16.pppfcs16(fcs16, chr1, 0, 1);

		// Control field
		if (!utils.ReadByte(pStream, chr1))
			return (-1);
		fcs16 = mobit.eemr.base.fcs16.pppfcs16(fcs16, chr1, 0, 1);

		// Header check sequence field
		if (!utils.ReadByte(pStream, chr1) || !utils.ReadByte(pStream, chr2))
			return (-1);

		wordValue = (chr2[0] << 8) | chr1[0];
		if (wordValue != (fcs16 ^ 0xffff))
			return (-1);

		fcs16 = mobit.eemr.base.fcs16.pppfcs16(fcs16, chr1, 0, 1);
		fcs16 = mobit.eemr.base.fcs16.pppfcs16(fcs16, chr2, 0, 1);
		if (dataSize > 0) {
			int index;

			for (index = 0; index < dataSize; index++) {
				if (!utils.ReadByte(pStream, pData, index))
					return (-1);

				fcs16 = mobit.eemr.base.fcs16.pppfcs16(fcs16, pData, index, 1);
			}

			// Frame check sequence field
			if (!utils.ReadByte(pStream, chr1) || !utils.ReadByte(pStream, chr2))
				return (-1);

			wordValue = (chr2[0] << 8) | chr1[0];
			if (wordValue != (fcs16 ^ 0xffff))
				return (-1);
		}

		if (!utils.ReadByte(pStream, chr1) || chr1[0] != 0x7e)
			return (-1);

		return (dataSize);
	}

}
