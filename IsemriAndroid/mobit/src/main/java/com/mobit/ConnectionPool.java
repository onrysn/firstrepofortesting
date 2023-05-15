package com.mobit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool implements AutoCloseable {

	private class ConnItem {
		private int refCount;
		private Connection conn;
		private Thread thread;
		
		public ConnItem(Connection conn)
		{
			refCount = 0;
			this.conn = conn;
			thread = null;
		}
		
		public Connection reserve()
		{
			if(refCount > 0) return null;
			refCount++;
			thread = Thread.currentThread();
			return conn;
		}
		public boolean release(Connection conn) throws Exception
		{
			if(this.conn != conn) return false;
			
			if(refCount == 1){
				thread = null;
				refCount--;
				return true;
			}
			throw new Exception("Havuz veritabanı bağlantı referans hatası!");
			
		}
	}
	
	private Object sync = new Object();
	private List<ConnItem> list = null;

	public ConnectionPool(int maxConn, String driverName, String prefix, String connectionString) throws Exception {

		Class.forName(driverName);
		final String jdbcURL = String.format("%s%s", prefix, connectionString);

		list = new ArrayList<ConnItem>(maxConn);

		for (int i = 0; i < maxConn; i++)
			list.set(i, new ConnItem(DriverManager.getConnection(jdbcURL)));
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			close();
		}
		finally {
			super.finalize();
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		synchronized (sync) {
			if (list != null) {
				for (ConnItem item : list) {
					try {
						item.conn.setAutoCommit(true);
						item.conn.close();
						list.remove(item);
					} catch (Exception e) {
					}
				}
			}
		}
	}
	
	Connection reserve() throws Exception
	{
		Connection conn = null;
		synchronized (sync) {
			if (list != null) {
				for (ConnItem item : list) {
					conn = item.reserve();
					if(conn != null) return conn;
				}	
			}
		}
		throw new Exception("Havuzdan veritabanı bağlantı nesnesi alınamadı!");
	}
	void release(Connection conn) throws Exception
	{
		synchronized (sync) {
			if (list != null) {
				for (ConnItem item : list) {
					if(item.release(conn)){
						return;
					}
				}			
			}
		}
		
		throw new Exception("Veritabanı bağlantı nesnesi havuzda bulunamadı!");
	}

}
