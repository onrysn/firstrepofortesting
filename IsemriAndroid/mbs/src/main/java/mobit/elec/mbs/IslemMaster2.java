package mobit.elec.mbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mobit.IApplication;
import com.mobit.ICbs;
import com.mobit.IIslem;
import com.mobit.IRecordStatus;
import com.mobit.IslemMaster;
import com.mobit.MobitException;
import mobit.elec.IAtarif;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriSoru;
import mobit.elec.IIsemriZabit;
import mobit.elec.IMuhurSokme;
import mobit.elec.ITakilanSayac;
import mobit.elec.ITesisatMuhur;
import mobit.elec.mbs.get.isemri_soru;
import mobit.elec.mbs.get.takilan_sayac;
import mobit.elec.mbs.put.put_atarif;
import mobit.elec.mbs.put.put_isemri;
import mobit.elec.mbs.put.put_isemri_zabit;
import mobit.elec.mbs.put.put_isemri_unvan;
import mobit.elec.mbs.put.put_muhur_sokme;
import mobit.elec.mbs.put.put_tesisat_muhur;

public class IslemMaster2 extends com.mobit.IslemMaster implements IIslemMaster {

	public IslemMaster2(IApplication app, IIslem iislem) throws MobitException {
		// TODO Auto-generated constructor stub
		super(app, iislem);
	}

	public IslemMaster2(IApplication app) throws MobitException {
		// TODO Auto-generated constructor stub
		super(app);
	}

	public IslemMaster2(IIslem islem) throws MobitException {
		// TODO Auto-generated constructor stub
		super(islem);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IIslem loadIslem() throws Exception {
		// TODO Auto-generated method stub
		islem = null;

		if (getTabloId() == isemri_soru.TABLOID) {
			List<IIsemriSoru> list = isemri_soru.selectIslem(app.getConnection(), getId());
			if (list.size() == 1)
				islem = list.get(0);
		} else if (getTabloId() == takilan_sayac.TABLOID) {
			List<ITakilanSayac> list = takilan_sayac.selectIslem(app.getConnection(), getId());
			if (list.size() == 1)
				islem = list.get(0);
		} else if (getTabloId() == put_isemri.TABLOID) {
			List<IIsemriIslem> list = put_isemri.selectIslem(app.getConnection(), getId());
			if (list.size() == 1)
				islem = list.get(0);

		} else if (getTabloId() == put_isemri_zabit.TABLOID) {
			List<IIsemriZabit> list = put_isemri_zabit.selectIslem(app.getConnection(), getId());
			if (list.size() == 1)
				islem = list.get(0);
		}
		else if (getTabloId() == put_atarif.TABLOID) {
			List<IAtarif> list = put_atarif.selectIslem(app.getConnection(), getId());
			if (list.size() == 1)
				islem = list.get(0);
		}
		else if (getTabloId() == put_muhur_sokme.TABLOID) {
			List<IMuhurSokme> list = put_muhur_sokme.selectIslem(app.getConnection(), getId());
			if (list.size() == 1)
				islem = list.get(0);
		}
		else if (getTabloId() == put_tesisat_muhur.TABLOID) {
			List<ITesisatMuhur> list = put_tesisat_muhur.selectIslem(app.getConnection(), getId());
			if (list.size() == 1)
				islem = list.get(0);
		}
		return islem;
	}

	protected static String query1 = String.format("select * from %s where %s = ? order by %s asc", tableName, sDURUM,
			sID);

	public static List<IIslemMaster> selectFromDurum(IApplication app, IRecordStatus durum) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IIslemMaster> list = new ArrayList<IIslemMaster>();
		Connection conn = app.getConnection();
		try {


			stmt = conn.prepareStatement(query1);
			stmt.setInt(1, durum.getValue());
			rs = stmt.executeQuery();
			while (rs.next()) {
				IIslemMaster m = (IIslemMaster)app.newIslemMaster();
				m.get(rs);
				list.add(m);
			}

		} finally {

			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}

	protected static String query2 = String.format("select * from %s where %s = ?", tableName, sID);

	public static IIslemMaster selectFromId(IApplication app, Integer id) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = app.getConnection();
		try {

			stmt = conn.prepareStatement(query2);
			stmt.setObject(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				IIslemMaster m = (IIslemMaster)app.newIslemMaster();
				m.get(rs);
				return m;
			}

		} finally {

			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return null;
	}

	protected static String query3 = String.format("select * from %s where %s = ?", tableName, sSAHA_ISEMRI_NO);

	public static List<IIslemMaster> selectFromIsemriNo(IApplication app, int isemriNo) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = app.getConnection();
		List<IIslemMaster> list = new ArrayList<IIslemMaster>();
		try {

			stmt = conn.prepareStatement(query3);
			stmt.setObject(1, isemriNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				IIslemMaster m = (IIslemMaster)app.newIslemMaster();
				m.get(rs);
				list.add(m);
			}

		} finally {

			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return list;
	}

	@Override
	public void setCBS(ICbs cbs){
		super.setCBS(cbs);
		//GPS DUZELTME
		IIslem islem = null;
		try {
			islem = getIslem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (islem instanceof IIsemriIslem)
			((IIsemriIslem) islem).setCBS(cbs);
	}

}
