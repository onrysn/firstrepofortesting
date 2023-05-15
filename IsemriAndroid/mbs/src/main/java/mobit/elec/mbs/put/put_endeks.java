package mobit.elec.mbs.put;


import com.mobit.DbType;
import com.mobit.FieldInfo;
import mobit.elec.IElecApplication;
import mobit.elec.IEndeks;
import mobit.elec.IIsemri2;
import mobit.elec.ISerialize;
import mobit.elec.mbs.utility;
import mobit.elec.mbs.get.Aciklama;
import mobit.elec.mbs.get.field;

public class put_endeks extends put_isemri {

	
	private static final String format = "PUT ENDEKS " + "%0" + field.s_TESISAT_NO + "d";

	public put_endeks()
	{
		super();
	}
	
	public put_endeks(IIsemri2 isemri) throws Exception
	{
		super(isemri);
		
	}
	
	@Override
	public void toSerialize(StringBuilder b) throws Exception {
		// TODO Auto-generated method stub

		if (CBS != null)
			((ISerialize) CBS).toSerialize(b);

		b.append(String.format(utility.MBS_LOCALE, format, TESISAT_NO));
		((ISerialize) SINYAL_SEVIYESI).toSerialize(b);

		for (int i = 0; i < GELEN_DURUM.length; i++)
			((ISerialize) GELEN_DURUM[i]).toSerialize(b);

		IEndeks[] SON_ENDEKS = endeksSirala(getSAYACLAR().getEndeksler());
		
		for (int i = 0; i < SON_ENDEKS.length; i++) {
			((ISerialize) SON_ENDEKS[i]).toSerialize(b);
			if(i < GELEN_DURUM_KODU.length) 
				((ISerialize) GELEN_DURUM_KODU[i]).toSerialize(b);
		}

		if (OKUMA_ACIKLAMA != null){
			ISerialize ser = (OKUMA_ACIKLAMA instanceof ISerialize) ? (ISerialize)OKUMA_ACIKLAMA : 
				new Aciklama(OKUMA_ACIKLAMA.getSTR());
			ser.toSerialize(b);
		}
		b.append('\n');

	}
	
	@Override
	public void IslemKontrol(IElecApplication app) throws Exception
	{
		if(app.Kontrol(1, this))
			if(getSAYACLAR().getSayaclar().size() != 0) { // H.Elif 11.05.2022
				getSAYACLAR().getSayaclar().get(0).EndeksKontrol(); // H.Elif 26.11.2021
				//getSAYACLAR().EndeksKontrol();
			}
	}

	// -------------------------------------------------------------------------

	public static final FieldInfo[] info = new FieldInfo[] {

			new FieldInfo(field.ID, true, DbType.INTEGER, 0, false),

			new FieldInfo(field.TESISAT_NO, DbType.INTEGER, field.s_TESISAT_NO, true),
			new FieldInfo(field.SINYAL_SEVIYESI, DbType.VARCHAR, field.s_SINYAL_SEVIYESI, true),
			new FieldInfo(field.GELEN_DURUM_1, DbType.INTEGER, field.s_GELEN_DURUM_1, true),
			new FieldInfo(field.GELEN_DURUM_2, DbType.INTEGER, field.s_GELEN_DURUM_2, true),
			new FieldInfo(field.GELEN_DURUM_3, DbType.INTEGER, field.s_GELEN_DURUM_3, true),
			new FieldInfo(field.SON_ENDEKS_1, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.SON_ENDEKS_2, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.SON_ENDEKS_3, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.SON_ENDEKS_4, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.SON_ENDEKS_5, DbType.REAL, field.s_SON_ENDEKS, true),
			new FieldInfo(field.GELEN_DURUM_KODU_1, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.GELEN_DURUM_KODU_2, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.GELEN_DURUM_KODU_3, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.GELEN_DURUM_KODU_4, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.GELEN_DURUM_KODU_5, DbType.INTEGER, field.s_GELEN_DURUM_KODU, true),
			new FieldInfo(field.DEMAND_ENDEKS, DbType.REAL, field.s_DEMAND_ENDEKS, true),
			new FieldInfo(field.OKUMA_ACIKLAMA, DbType.VARCHAR, field.s_OKUMA_ACIKLAMA, true),
			new FieldInfo(field.CBS_X, DbType.REAL, field.s_CBS_X, true),
			new FieldInfo(field.CBS_Y, DbType.REAL, field.s_CBS_Y, true)
			
		};
	

}
