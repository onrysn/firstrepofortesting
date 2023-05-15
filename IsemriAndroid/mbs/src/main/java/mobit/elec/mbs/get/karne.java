package mobit.elec.mbs.get;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mobit.DbType;
import com.mobit.FieldInfo;
import com.mobit.IEnum;
import com.mobit.IndexInfo;
import mobit.elec.IKarne;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.utility;

public class karne implements IKarne, IDeserialize {

	private int id = 0;
	
	private int ZAMAN_KODU; /* CHAR(8) Saniye cinsinden zaman kodu hex 32-bit */
	private IEnum ISLEM_TIPI;
	private int KARNE_NO; /* NUM 9(7). */
	private int ADET; /* NUM 9(8) */

	@Override
	public int deserialize(ICommand cmd, String row) throws Exception   {
		// TODO Auto-generated method stub

		String[] list = utility.parser(row, info);
		if (list.length > 4) throw new ParseException(row, list.length);

		setZAMAN_KODU(Integer.parseInt(list[0], 16));
		setISLEM_TIPI(IslemTipi.fromInteger(Integer.parseInt(list[1])));
		setKARNE_NO(utility.MBS_FORMAT.parse(list[2]).intValue());
		setADET(utility.MBS_FORMAT.parse(list[3]).intValue());
		
		return 0;
	}

	//-------------------------------------------------------------------------
	
	
	public static final String tableName = "karne";
	
	public static final FieldInfo [] info = new FieldInfo[]{
			
			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),
			
			new FieldInfo(field.ZAMAN_KODU, DbType.INTEGER, field.s_ZAMAN_KODU, true),
			new FieldInfo(field.ISLEM_TIPI, DbType.INTEGER, field.s_ISLEM_TIPI, true),
			new FieldInfo(field.KARNE_NO, DbType.INTEGER, field.s_KARNE_NO, true),
			new FieldInfo(field.ADET, DbType.INTEGER, field.s_ADET, true)
	};
	
	public static final IndexInfo [] indices = new IndexInfo[] {
			new IndexInfo(tableName+"_karne_no", true, field.KARNE_NO +","+field.ISLEM_TIPI),
	};
	
	//-------------------------------------------------------------------------
	@Override
	public String toString() {
		return String.format("%d %s (%d)", KARNE_NO, ISLEM_TIPI, ADET);
	}

	public int getZAMAN_KODU() {
		return ZAMAN_KODU;
	}

	public IEnum getISLEM_TIPI() {
		return ISLEM_TIPI;
	}

	public int getKARNE_NO() {
		return KARNE_NO;
	}

	public int getADET() {
		return ADET;
	}

	public void setZAMAN_KODU(int zAMAN_KODU) {
		ZAMAN_KODU = zAMAN_KODU;
	}

	public void setISLEM_TIPI(IEnum iSLEM_TIPI) {
		ISLEM_TIPI = iSLEM_TIPI;
	}

	public void setKARNE_NO(int kARNE_NO) {
		com.mobit.utility.check(kARNE_NO, field.s_KARNE_NO);
		KARNE_NO = kARNE_NO;
	}

	public void setADET(int aDET) {
		com.mobit.utility.check(aDET, field.s_ADET);
		ADET = aDET;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void get(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		id = rs.getInt(field.ID);
		
		setZAMAN_KODU(rs.getInt(field.ZAMAN_KODU));
		setISLEM_TIPI(IslemTipi.fromInteger(rs.getInt(field.ISLEM_TIPI)));
		setKARNE_NO(rs.getInt(field.KARNE_NO));
		setADET(rs.getInt(field.ADET));
		
	}
	

}
