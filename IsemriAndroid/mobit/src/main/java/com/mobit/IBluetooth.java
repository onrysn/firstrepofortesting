package com.mobit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public abstract class IBluetooth implements AutoCloseable {

	protected InputStream m_input;
	protected OutputStream m_output;
	
	protected void finalize()
	{
		close();
	}
	
	@Override
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
	
	public InputStream get_InputStream() throws Exception
	{
		if(!isConnected()) throw new Exception("Bluetooth bağlantısı kesilmiş!");
		return m_input;
	}
	
	public OutputStream get_OutputStream() throws Exception
	{
		if(!isConnected()) throw new Exception("Bluetooth bağlantısı kesilmiş!");
		return m_output;
	}
	
	public int read(byte [] buffer) throws IOException 
	{
		int read = 0;
		try {
			
			 read = m_input.read(buffer);
			//return (m_input != null) ? m_input.read(buffer) : 0;	
		}
		catch(Exception e){
		}
		return read;
		//return readInputStreamWithTimeout(m_input, buffer, 2000);
		
	}
	
	public void write(byte [] buffer) throws IOException 
	{
		try {
			m_output.write(buffer);
		}
		catch(IOException e){
			
		}
		catch(Exception e){
			
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
	
	public abstract void Open(UUID sdpid, String BthAddr, String Pin, Runnable r) throws Exception;
	public abstract boolean isConnected();
	
	public static final UUID SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
}
