package com.mobit;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mobit.DbHelper;
import com.mobit.FieldInfo;
import com.mobit.IDb;
import com.mobit.IndexInfo;

public class DbHelper implements Closeable {

	private PreparedStatement ins = null;
	private PreparedStatement upd = null;
	private PreparedStatement pk = null;
	private Connection conn;

	public DbHelper(Connection conn) {
		this.conn = conn;
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	@Override
	public void close() {
		if (ins != null) {
			try {

				ins.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ins = null;
		}
		if (upd != null) {
			try {
				upd.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			upd = null;
		}
		if (pk != null) {
			try {
				pk.close();
			} catch (SQLException e) {

			}
			pk = null;
		}
	}

	public Connection getConnection() {
		return conn;
	}

	public PreparedStatement getInsStatment(IDb obj) throws Exception {

		if (ins != null) {
			ins.clearParameters();
			return ins;
		}
		try {

			Class<?> c = obj.getClass();
			Field f = utility.getField(c, "insertString");
			ins = conn.prepareStatement((String) f.get(null));
			return ins;

		} catch (NoSuchFieldException e) {

		} catch (SecurityException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}

		throw new Exception("insertString alanı bulunamadı!");

	}

	public PreparedStatement getUpdStatment(IDb obj) throws Exception {
		if (upd != null) {

			upd.clearParameters();
			return upd;
		}

		try {
			Class<?> c = obj.getClass();
			Field f = utility.getField(c, "updateString");
			upd = conn.prepareStatement((String) f.get(null));
			return upd;
		}

		catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		throw new Exception("updateString alanı bulunamadı!");
	}

	// -------------------------------------------------------------------------

	// public static Connection conn = null;

	public static List<String> CreateTables(Class<?>[] tables) throws Exception {

		ArrayList<String> list = new ArrayList<String>(tables.length + 30);
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < tables.length; i++) {

			String tableName = (String) utility.getField(tables[i], "tableName").get(null);
			FieldInfo[] fields = (FieldInfo[]) utility.getField(tables[i], "info").get(null);

			sb.setLength(0);
			sb.append(String.format("CREATE TABLE IF NOT EXISTS %s (", tableName));

			for (int j = 0; j < fields.length; j++) {

				FieldInfo field = fields[j];
				String primaryKey = (field.PrimaryKey) ? "PRIMARY KEY" : "";

				switch (field.Type) {
				case CHARACTER:
				case VARCHAR:
				case VARYING_CHARACTER:
				case NCHAR:
				case NATIVE_CHARACTER:
				case NVARCHAR:
					sb.append(String.format("%s %s(%d) %s", field.Name, field.Type.toString(), field.Size, primaryKey));
					break;
				default:
					sb.append(String.format("%s %s %s", field.Name, field.Type.toString(), primaryKey));
				}
				if (j < fields.length - 1)
					sb.append(",");
			}
			sb.append(");\r\n");
			list.add(sb.toString());

			/*
			try {

				utility.getField(tables[i], "insertString").set(null, PrepareInsertString(tables[i]));
				utility.getField(tables[i], "updateString").set(null, PrepareUpdateString(tables[i]));
			} catch (Exception e) {

			}*/
			list.addAll(CreateIndices(tables[i]));

		}
		return list;
	}

	public static List<String> CreateIndices(Class<?> table) throws Exception {

		ArrayList<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();

		try {
			String tableName = (String) utility.getField(table, "tableName").get(null);

			IndexInfo[] indices = (IndexInfo[]) utility.getField(table, "indices").get(null);
			for (int j = 0; j < indices.length; j++) {
				IndexInfo index = indices[j];
				sb.setLength(0);
				sb.append(index.Unique ? "CREATE UNIQUE INDEX " : "CREATE INDEX ");
				sb.append(String.format("IF NOT EXISTS %s ON %s (", index.Name, tableName));
				for (int k = 0; k < index.fieldNames.length; k++) {
					sb.append(index.fieldNames[k]);
					if (k < index.fieldNames.length - 1)
						sb.append(",");
				}
				sb.append(");\r\n");
				list.add(sb.toString());
			}
		} catch (NoSuchFieldException e) {

		}

		return list;
	}

	public static List<String> DropIndices(Class<?> table) throws Exception {

		ArrayList<String> list = new ArrayList<String>();

		try {
			String tableName = (String) utility.getField(table, "tableName").get(null);
			IndexInfo[] indices = (IndexInfo[]) utility.getField(table, "indices").get(null);
			for (int i = 0; i < indices.length; i++) {
				IndexInfo index = indices[i];
				list.add(String.format("DROP INDEX IF EXISTS %s;\r\n", index.Name));
			}
		} catch (NoSuchFieldException e) {

		}

		return list;
	}
	
	public static boolean isColumnExists(Connection conn, String tableName, String columnName) throws SQLException {
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(String.format("PRAGMA table_info(%s);", tableName));
			while(rs.next()){
				String name = rs.getString("name");
				if(name.equalsIgnoreCase(columnName)){
					return true;
				}
			}
		}
		finally {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
		}
		return false;
        
    }

	
	public static String PrepareInsertString(Class<?> c) {

		String tableName;
		FieldInfo[] fields;

		try {
			tableName = (String) utility.getField(c, "tableName").get(null);
			fields = (FieldInfo[]) utility.getField(c, "info").get(null);
		} catch (Exception e) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("INSERT OR IGNORE INTO %s (", tableName));
		int cnt = 0;
		for (int i = 0; i < fields.length; i++) {

			FieldInfo field = fields[i];
			// if(field.Name.equalsIgnoreCase("ID")) continue;
			sb.append(field.Name);
			if (i < fields.length - 1)
				sb.append(",");
			cnt++;
		}
		sb.append(") VALUES (");
		for (int i = 0; i < cnt; i++) {
			sb.append('?');
			if (i < cnt - 1)
				sb.append(',');
		}
		sb.append(");");

		return sb.toString();
	}

	public static String PrepareUpdateString(Class<?> c) {

		return PrepareUpdateString(c, "ID");
	}

	public static String PrepareUpdateString(Class<?> c, String... keys) {

		String tableName;
		FieldInfo[] fields;

		try {
			tableName = (String) utility.getField(c, "tableName").get(null);
			fields = (FieldInfo[]) utility.getField(c, "info").get(null);
			// indices = (IndexInfo [])utility.getField(c, "indices").get(null);

		} catch (Exception e) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("UPDATE %s SET ", tableName));
		int i = 0;
		for (i = 0; i < fields.length; i++) {

			FieldInfo field = fields[i];
			if (field.PrimaryKey)
				continue;

			sb.append(String.format("%s=?", field.Name));
			if (i < fields.length - 1)
				sb.append(",");
		}

		for (i = 0; i < keys.length; i++) {
			if (i == 0)
				sb.append(" WHERE ");
			sb.append(String.format("%s=?", keys[i]));
			sb.append((i < keys.length - 1) ? " and " : ";");
		}

		return sb.toString();
	}

	public Integer GetId() throws SQLException {
		ResultSet rs = null;
		PreparedStatement pk = null;
		try {

			pk = conn.prepareStatement("select last_insert_rowid();");
			rs = pk.executeQuery();

			if (rs.next())
				return rs.getInt(1);

			throw new SQLException("ID alınamadı");

		} finally {
			if (rs != null)
				rs.close();
			if (pk != null)
				pk.close();
		}
	}

	public static int getVersion(Connection conn) throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery("pragma user_version");

			if (rs.next())
				return rs.getInt(1);

			throw new SQLException("Veritabanı versiyonu alınamadı");

		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
	}

	public static void setVersion(Connection conn, int dbVer) throws SQLException {
		Statement stmt = null;
		try {

			stmt = conn.createStatement();
			stmt.execute(String.format("pragma user_version=%d;", dbVer));

		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	public static <T> List<T> Select(ResultSet rs, Class<?> c) throws Exception {
		ArrayList<T> list = new ArrayList<T>();
		// Method m = c.getMethod("get", ResultSet.class);

		while (rs.next()) {
			@SuppressWarnings("unchecked")
			T obj = (T) c.newInstance();
			((IDb) obj).get(rs);
			list.add(obj);
		}
		return list;
	}

	public static <T> List<T> Select(Connection conn, Class<?> c, int id) throws Exception {

		Field f = utility.getField(c, "tableName");
		String s = String.format("select * from %s where ID = ?", f.get(null));
		List<T> list = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(s);
			stmt.setInt(1, id);
			rs = stmt.executeQuery(s);
			list = Select(rs, c);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}

	public static <T> List<T> SelectAll(Connection conn, Class<?> c) throws Exception {

		Field f = utility.getField(c, "tableName");
		String s = String.format("select * from %s", f.get(null));
		List<T> list = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(s);
			list = Select(rs, c);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}

	public static <T> void Save(Connection conn, List<T> list) throws Exception {

		if (list == null || list.size() == 0)
			return;

		DbHelper h = new DbHelper(conn);
		boolean tran = false;

		try {

			if (conn.getAutoCommit() == true) {
				conn.setAutoCommit(false);
				tran = true;
			}

			for (int i = 0; i < list.size(); i++) {
				T t = list.get(i);
				if (((IDb) t).put(h) == 0)
					throw new MobitException("Veritabanına kaydedilemedi!");
			}
			// if(tran) conn.commit();

		} catch (Exception e) {
			if (tran)
				conn.rollback();
			throw e;
		} finally {

			if (tran)
				conn.setAutoCommit(true);
			if (h != null)
				h.close();
		}
	}

	public static void Save(Connection conn, Object t) throws Exception {

		DbHelper h = new DbHelper(conn);
		boolean tran = false;
		try {

			if (conn.getAutoCommit() == true) {
				conn.setAutoCommit(false);
				tran = true;
			}

			IDb db = (IDb) t;
			if (db.put(h) == 0)
				throw new MobitException("Kaydedilemedi!");

			// if(tran)conn.commit();

		} catch (Exception e) {
			if (tran)
				conn.rollback();
			throw e;
		} finally {
			if (tran)
				conn.setAutoCommit(true);
			if (h != null)
				h.close();
		}
	}
	/*
		String.format("delete from %s where %s = ?",
			tableName, field.ISEMRI_DURUMU);
	 */

	public static void DeleteAtanmis(Connection conn, Class<?> c,int ISEMRI_DURUMU) throws Exception {

		String tableName = (String) utility.getField(c, "tableName").get(null);
		//String query1 = String.format("delete from %s where ISEMRI_DURUMU= %s", tableName, ISEMRI_DURUMU);
		Statement stmt = null;
		boolean tran = false;
		try {

			stmt = conn.createStatement();

			if (conn.getAutoCommit() == true) {
				conn.setAutoCommit(false);
				tran = true;
			}

			stmt.execute(String.format("delete from %s where ISEMRI_DURUMU=%s",
					tableName, ISEMRI_DURUMU));

			// if(tran) conn.commit();

		} catch (Exception e) {
			if (tran)
				conn.rollback();
			throw e;
		} finally {

			if (tran)
				conn.setAutoCommit(true);
			if (stmt != null)
				stmt.close();
		}
	}
	public static void DeleteAll(Connection conn, Class<?> c) throws Exception {

		String tableName = (String) utility.getField(c, "tableName").get(null);

		Statement stmt = null;
		boolean tran = false;
		try {

			stmt = conn.createStatement();

			if (conn.getAutoCommit() == true) {
				conn.setAutoCommit(false);
				tran = true;
			}

			stmt.execute(String.format("delete from %s;", tableName));

			// if(tran) conn.commit();

		} catch (Exception e) {
			if (tran)
				conn.rollback();
			throw e;
		} finally {

			if (tran)
				conn.setAutoCommit(true);
			if (stmt != null)
				stmt.close();
		}
	}

	public static Integer getFieldIndex(FieldInfo[] info, String fieldName) {
		for (int i = 0; i < info.length; i++) {
			if (info[i].Name.equalsIgnoreCase(fieldName))
				return i + 1;
		}
		return null;
	}

	public static void DeleteKarne(Connection conn, int KarneNo) throws Exception {
		//Onur Veritabanından istenen karneyi silmek için eklendi
		String tableName = "isemri";
		Statement stmt = null;
		boolean tran = false;
		try {

			stmt = conn.createStatement();

			if (conn.getAutoCommit() == true) {
				conn.setAutoCommit(false);
				tran = true;
			}

			stmt.execute(String.format("delete from %s where KARNE_NO=%s",
					tableName, KarneNo));

			// if(tran) conn.commit();

		} catch (Exception e) {
			if (tran)
				conn.rollback();
			throw e;
		} finally {

			if (tran)
				conn.setAutoCommit(true);
			if (stmt != null)
				stmt.close();
		}

	}

}
