package mobit.elec.mbs.get;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mobit.DbHelper;
import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.ICbs;
import com.mobit.IndexInfo;
import mobit.elec.IKoordinat;
import mobit.elec.mbs.utility;

public class koordinat implements IKoordinat, IDeserialize {

	private int id = 0;
	
	private int TESISAT_NO;
	private ICbs CBS;
	
	@Override
	public int deserialize(ICommand cmd, String row) throws Exception {
		// TODO Auto-generated method stub
		String[] list = utility.parser(row, info);

		if(list.length != 3) throw new ParseException(row, list.length);
		
		setTESISAT_NO(utility.MBS_FORMAT.parse(list[0]).intValue());
		setCBS((ICbs)new Cbs(list[1], list[2]));
		
		return 0;
	}

	//-------------------------------------------------------------------------
	
	
	public static final String tableName = "koordinat";
	
	public static final FieldInfo [] info = new FieldInfo[]{
			
			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			
			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, true),
			new FieldInfo(field.CBS_X, DbType.REAL, field.s_CBS_X, true),
			new FieldInfo(field.CBS_Y, DbType.REAL, field.s_CBS_Y, true)
	};
	
	public static final IndexInfo [] indices = new IndexInfo[] {
			new IndexInfo("ix_tesisat_no", true, field.TESISAT_NO),
	};
	
	public static final String insertString = DbHelper.PrepareInsertString(koordinat.class);
	public static final String updateString = DbHelper.PrepareUpdateString(koordinat.class);
	
	//-------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return String.format("%d %s", TESISAT_NO, CBS.toString());
	}

	public int getTESISAT_NO() {
		return TESISAT_NO;
	}

	public ICbs getCBS() {
		return CBS;
	}
	
	public void setTESISAT_NO(int tESISAT_NO) {
		com.mobit.utility.check(tESISAT_NO, field.s_TESISAT_NO);
		TESISAT_NO = tESISAT_NO;
	}

	public void setCBS(ICbs cBS) {
		CBS = cBS;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void get(ResultSet rs) throws SQLException, ParseException {
		// TODO Auto-generated method stub
		
		id = rs.getInt(field.ID);
		
		setTESISAT_NO(rs.getInt(field.TESISAT_NO));
		setCBS(Cbs.fromResultSet2(rs));
		
	}

	
}
