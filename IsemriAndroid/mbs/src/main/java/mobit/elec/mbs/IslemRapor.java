package mobit.elec.mbs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IIslem;
import com.mobit.IResult;
import com.mobit.RecordStatus;

import mobit.elec.IIslemRapor;

public class IslemRapor implements IIslemRapor {

	private int SAHA_ISEMRI_NO;
	private IApplication app = null;
	List<IIslemMaster> islemList = null;

	public IslemRapor(IApplication app, int SAHA_ISEMRI_NO) throws Exception {
		this.app = app;
		this.SAHA_ISEMRI_NO = SAHA_ISEMRI_NO;
		islemList = IslemMaster2.selectFromIsemriNo(app, SAHA_ISEMRI_NO);

	}
	public IslemRapor(IApplication app, int SAHA_ISEMRI_NO, List<IIslemMaster> islemList) {
		this.app = app;
		this.SAHA_ISEMRI_NO = SAHA_ISEMRI_NO;
		this.islemList = islemList;
	}

	@Override
	public String getAciklama() {
		// TODO Auto-generated method stub
		return "İşlem Raporu";
	}

	static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@Override
	public List<String[]> getDetay() {
		// TODO Auto-generated method stub

		List<String[]> list = new ArrayList<String[]>();
		for (IIslemMaster islem : islemList) {
			if (islem instanceof IResult) {
				IResult r = (IResult) islem;
				
				String type = r.getRESULT_TYPE();
				String s1 = null;
				String s2 = null;
				IIslem iislem = null;

				try {
					iislem = islem.getIslem();
				}
				catch (Exception e){
					continue;
				}

				if (type != null) {
					// Hata veya uyarı durumu
					s1 = String.format("%s : %d\n%s", type, r.getRESULT_CODE(), Globals.dateTimeFmt.format(islem.getZaman()));
					s2 = iislem.toString();
					if(type.equals(IDef.ERR) || type.equals(IDef.FTL)  || type.equals(IDef.WRN)) {
						s2 = String.format("%s\n%s", s2, r.getRESULT());
					}
				}
				else if(islem.getDurum() == RecordStatus.Saved) {
					s1 = Globals.dateTimeFmt.format(islem.getZaman());
					s2 = String.format("%s\n%s", iislem.toString(), "Gönderilmeyi Bekliyor...");
				}

				if(s1 != null) list.add(new String[] { s1, s2 });
			}
		}
		return list;
	}

}
