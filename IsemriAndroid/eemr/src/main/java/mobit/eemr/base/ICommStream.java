package mobit.eemr.base;

import java.io.IOException;

public interface ICommStream {
	
	void Close();

	boolean IsAborted();
	void SetAborted(boolean aborted) throws IOException;

	Object GetHandle();
	void SetReadTimeout(long timeout) throws IOException;

	void Read(byte [] pBuffer, int off, int bytesToRead, int [] pBytesRead) throws Exception;
	void Read(byte [] pBuffer, int bytesToRead, int [] pBytesRead) throws Exception;
	void Write(byte [] pBuffer, int off, int bytesToWrite, int [] pBytesWritten) throws Exception;
	void Write(byte [] pBuffer, int bytesToWrite, int [] pBytesWritten) throws Exception;

	void FlushWrite() throws Exception;
	void ClearIoBuffers(boolean clearInput, boolean clearOutput) throws InterruptedException, IOException;
}
