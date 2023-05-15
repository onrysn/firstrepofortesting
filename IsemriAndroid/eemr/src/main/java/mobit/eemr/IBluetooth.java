package mobit.eemr;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.rmi.runtime.Log;

public abstract class IBluetooth {

	protected InputStream m_input;
	protected OutputStream m_output;

	@Override
	protected void finalize() throws Throwable
	{
		close();
		super.finalize();
	}

	public void close()
	{
		if(m_output != null){
			try {
				m_output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_output = null;
		}
		if(m_input != null){
			try {
				m_input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_input = null;
		}
	}

	public InputStream get_InputStream()
	{
		return m_input;
	}

	public OutputStream get_OutputStream()
	{
		return m_output;
	}
	//muhammed gökkaya

	public byte L=76;
	public byte U=85;
	public byte N=78;
	//public int LUNa_CONTROL=0;
	Lun_Control Tesisat=new Lun_Control();
    YkpOkuma ykp=new YkpOkuma();
	MakelYildizliGelmeyen makel = new MakelYildizliGelmeyen();

	public int read(byte [] buffer) throws IOException
	{
		int read = 0;
		try {

			read = m_input.read(buffer);
			//Logger.getLogger("Raw").info(new String(buffer,0,read, StandardCharsets.US_ASCII));
			/*
			int i=0;

			if (read>3 & Tesisat.Luna_Control==0){
				for (i=0;i<read;i++){
					if (i<read-2){
						if (buffer[i]==L & buffer[i+1]==U & buffer[i+2]==N ){
							Tesisat.setLuna_Control(1);
							break;
						}
					}
				}

			}
			if (read > 14 && Tesisat.Luna_Control==0){
				if (makel.checkMsyModel(buffer,read)== 1 ){
					Tesisat.setLuna_Control(1);
				}
			}

			if (read>3 && ykp.SayacMarka.equals("VI-KO")){
			    ykp.checkVIKOModel(buffer,read);
            }
			 */
			//Thread.sleep(200);

		}
		catch(Exception e)
		{
			String fg  =e.getLocalizedMessage();
		}
		return read;
		//return readInputStreamWithTimeout(m_input, buffer, 2000);

	}
	public void write(byte [] buffer) throws IOException
	{
		//buffer= new byte[]{47,63,33,13,10};
        //1 82 50 2 48 46 48 46 48 40 41 3 80
		try {
			//muhammed gökkaya luna
			if (ykp.YkpOkumaDurum==1){
			    if (!ykp.SayacMarka.equals("KOHLER (AEL)")) {

                    if (ykp.YkpCount == 2) {
                        byte[] bitir = new byte[]{1, 66,48,3,115};
                        m_output.write(bitir);
                        ykp.YkpCount = 3;
                    }


                    if (buffer.length > 10 && ykp.YkpCount == 1) {
                        //byte[] bufferviko=new byte[]{1,82,50,2,57,54,46,49,46,56,46,48,40,59,41,3,67};
                        //byte[] bufferviko=new byte[]{1,82,50,2,57,54,46,49,46,56,46,48,40,49,57,45,48,49,45,48,49,44,49,48,58,48,48,59,49,57,45,48,49,45,48,50,44,49,48,58,48,48,41,3,64};
                        //[SOH] R2 [STX] 128.12.0001() [ETX]Y
                        //[SOH]R2[STX]P.04(2019-07-04,00:00;2019-07-05,15:27)[ETX]!
                        //byte[] bufferkhl = new byte[]{1, 82, 50, 2, 49, 50, 56, 46, 49, 50, 46, 50, 48, 52, 56, 40, 41, 3, 86};
						//byte[] buffermak= new byte[]{1,82,50,2,80,46,48,49,40,59,41,3,36};
						//m_output.write(buffermak);
                        m_output.write(ykp.YkpBuffer);
                        ykp.YkpCount = 2;
                    }
                    if (buffer.length < 11) {
                        if (buffer.length > 5) {
                            if (buffer[0] == 6 & buffer[1] == 48 & buffer[2] == 53 & buffer[3] == 48 & buffer[4] == 13 & buffer[5] == 10) {
                                buffer[3] = 49;
                                m_output.write(buffer);
                                //m_output.write(ykp.YkpBuffer);
                            } else {
                                m_output.write(buffer);
                            }
                        } else {
                            m_output.write(buffer);
                        }
                    } else {
                        //pass
                    }
                }
			    else {
			        //AEL KOMUTLARI BURAYA
					if (buffer.length > 10 ) {
						int index=ykp.ObisCount;
						m_output.write(ykp.YkpAELBuffer[index]);
						ykp.YkpCount = 2;
						ykp.setObisCount(index-1);
					}
					if (buffer.length < 11) {
						if (buffer.length > 5) {
							if (buffer[0] == 6 & buffer[1] == 48 & buffer[2] == 53 & buffer[3] == 48 & buffer[4] == 13 & buffer[5] == 10) {
								buffer[3] = 49;
								m_output.write(buffer);
							} else {
								m_output.write(buffer);
							}
						} else {
							m_output.write(buffer);
						}
					} else {
						//pass
					}
                }
			}
			else {

				Logger LOGGER = Logger.getLogger("lunatest");
				LOGGER.info(String.valueOf(buffer.length));
				Logger LOGGER3 = Logger.getLogger("Buffer");
				LOGGER3.info(String.valueOf(buffer.toString()));
				Logger LOGGER1 = Logger.getLogger("lunatestcontrol");
				LOGGER1.info(String.valueOf(Tesisat.Luna_Control));
				Logger LOGGER2 = Logger.getLogger("lunatestIcerik");
				for (int x = 0; x< buffer.length; x++) {
					LOGGER2.info(String.valueOf(buffer[x]));
					LOGGER2.info("-");
				}
				int deg = 1;
				if (buffer.length > 3 & Tesisat.Luna_Control == deg) {
					if (buffer[0] == 47 & buffer[1] == 63 & buffer[2] == 33 & buffer[3] == 10) {
						buffer[0] = 47;
						buffer[1] = 63;
						buffer[2] = 33;
						buffer[3] = 13;
						buffer[4] = 10;
						m_output.write(buffer);
					} else if (buffer.length > 5 & Tesisat.Luna_Control == deg) {
						if (buffer[0] == 6 & buffer[1] == 48 & buffer[2] == 53 & buffer[3] == 48 & buffer[4] == 13 & buffer[5] == 10) {
						/*
						Muhammed Gökkaya
						*/
							buffer[0] = 6;
							buffer[1] = 48;
							buffer[2] = 53;
							buffer[3] = 55;
							buffer[4] = 13;
							buffer[5] = 10;
							m_output.write(buffer);
							buffer = new byte[]{6, 48, 53, 48, 13, 10};
							buffer[0] = 6;
							buffer[1] = 48;
							buffer[2] = 53;
							buffer[3] = 48;
							buffer[4] = 13;
							buffer[5] = 10;
							m_output.write(buffer);
						} else {
							m_output.write(buffer);
						}
					} else {
						m_output.write(buffer);
					}
				} else {
					if (buffer.length>=5) {
						if (buffer[0] == 6 & buffer[1] == 48 & buffer[2] == 53 & buffer[3] == 48 & buffer[4] == 13) {
							//buffer[2] = 51;
							//buffer[3] = 55;
							m_output.write(buffer);
							/*
							buffer[0] = 6;
							buffer[1] = 48;
							buffer[2] = 53;
							buffer[3] = 48;
							buffer[4] = 13;
							buffer[5] = 10;
							m_output.write(buffer);
							 */
						}
						else {
							m_output.write(buffer);
						}
					}
					else {
						m_output.write(buffer);
					}
				}
			}

		}
		catch(IOException e){

		}
		catch(Exception e){
            e=null;
		}
		FlushWrite();
	}

	public void FlushWrite() throws IOException
	{
		try {
			m_output.flush();
		}
		catch(IOException e){
		}
		catch(Exception e){
		}
	}

	public abstract void Open(UUID sdpid, String BthAddr, String Pin) throws Exception;
	public abstract boolean isConnected();

	public static final UUID SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

}
