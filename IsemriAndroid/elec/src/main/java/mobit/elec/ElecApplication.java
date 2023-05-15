package mobit.elec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.mobit.Application;
import com.mobit.Globals;
import com.mobit.ICbs;
import com.mobit.IChecked;
import com.mobit.IEleman;
import com.mobit.IIslem;
import com.mobit.ILocation;
import com.mobit.IPlatform;
import com.mobit.Operation;
import mobit.eemr.ICallback;
import mobit.eemr.IMeterReader;
import mobit.eemr.IReadResult;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemTipi;

public abstract class ElecApplication extends Application implements IElecApplication {

	// IIsemri isemri = null;
	// IIslem islem = null;

	IReadResult result = null;
	private IMeterReader mr = null;

	public ElecApplication() throws Exception {
		super();
		Class.forName(mobit.elec.MsgInfo.class.getName());

	}

	public void init(IPlatform platform, Object obj) throws Exception {
		super.init(platform, obj);
		platform.addMessage("Optik port yükleniyor...");
		mr = platform.createMeterReader();
		mr.setSilent(false);
		mr.ConnectAsync(getPortMac(), getPortPIN(), "");
		platform.addMessage("Optik port yüklendi.");

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		Globals.app = null;
		if (mr != null) {
			mr.Disconnect();
			mr = null;
		}

		super.close();
	}

	@Override
	public IIsemri getActiveIsemri() {
		return (IIsemri) getObject(IDef.OBJ_AKTIF_ISEMRI);
	}

	@Override
	public void setActiveIsemri(IIsemri isemri) {
		setObject(IDef.OBJ_AKTIF_ISEMRI, isemri);
	}

	@Override
	public IIslem getActiveIslem() {
		return (IIslem) getObject(IDef.OBJ_AKTIF_ISLEM);
	}

	@Override
	public void setActiveIslem(IIslem islem) {
		setObject(IDef.OBJ_AKTIF_ISLEM, islem);
	}


	@Override
	public String getPortMac() {
		return platform.getSettingValue(PORT_MAC);

	}

	@Override
	public void setPortMac(String PortMac) {
		platform.setSettingValue(PORT_MAC, PortMac);
	}

	@Override
	public String getPortPIN() {
		return platform.getSettingValue(PORT_PIN);

	}

	@Override
	public void setPortPIN(String Pin) {
		platform.setSettingValue(PORT_PIN, Pin);
	}

	static final String PORT_MAC = "PortMac";
	static final String PORT_PIN = "PortPin";

	@Override
	public int getIslemRenk(IIslemTipi tip) {

		return (tip).getIslemRenk();
	}

	@Override
	public IMeterReader getMeterReader() {
		return mr;
	}

	@Override
	public void setPortCallback(ICallback clb) {
		if(mr != null) mr.setCallback(clb);
	}

	@Override
	public void setOptikResult(IReadResult result) {
		this.result = result;
	}

	@Override
	public IReadResult getOptikResult() {
		return result;
	}

	@Override
	public List<String> getAboneDurumAciklama(IIsemriIslem islem, IAdurum durum) throws Exception {
		return new ArrayList<String>();
	}

	@Override
	public void Sirala(List<IIsemri> list, ILocation location) throws InterruptedException {

		List<IIsemri> tempList = new ArrayList<IIsemri>(list);
		final InterruptedException ex = new InterruptedException("İşlem iptal edildi");
		synchronized (list) {
			list.clear();
			while (!tempList.isEmpty()) {

				if(Thread.interrupted()) throw ex;

				for (IIsemri isemri : tempList) {
					ICbs cbs = isemri.getCBS();

					if(Thread.interrupted()) throw ex;

					if (cbs != null && !cbs.isEmpty()) {
						try {
							isemri.setDistance(location.distanceTo(cbs.toLocation()));
						} catch (Exception e) {
							cbs = null;
						}
					}
				}

				Collections.sort(tempList, new Comparator<IIsemri>() {
					public int compare(IIsemri obj1, IIsemri obj2) {

						if (obj1.getDistance() < obj2.getDistance())
							return -1;
						if (obj1.getDistance() > obj2.getDistance())
							return 1;
						return 0;
					}
				});

				list.add(tempList.get(0));
				tempList.remove(0);

			}
		}

	}

	@Override
	public AltEmirTuru[] getAltEmirTuru(IIslemTipi islemTipi) {
		return new AltEmirTuru[0];
	}

	@Override
	public boolean Kontrol(int mode, IIsemriIslem islem) throws Exception {
		return true;
	}

	@Override
	public Operation[] getOperationList(IIslemTipi islemTipi) {

		if (islemTipi.equals(IslemTipi.Acma))
			return IDef.acmaIsl;
		else if (islemTipi.equals(IslemTipi.Bilgi))
			return IDef.genel;
		else if (islemTipi.equals(IslemTipi.Ihbar))
			return IDef.genel;
		else if (islemTipi.equals(IslemTipi.Kesme))
			return IDef.kesmeIsl;
		else if (islemTipi.equals(IslemTipi.Kontrol))
			return IDef.genel;
		else if (islemTipi.equals(IslemTipi.SayacDegistir))
			return IDef.sayacDegistir;
		else if (islemTipi.equals(IslemTipi.SayacOkuma))
			return IDef.okuma;
		else if (islemTipi.equals(IslemTipi.SayacTakma))
			return IDef.sayacTak;
		else if (islemTipi.equals(IslemTipi.Tespit))
			return IDef.tespit;

		return IDef.genel;

	}

	@Override
	public boolean checkFilter(IsemriFilterParam param) {


		if (param != null) {

			boolean exists;
			
			if (param.isemri.getSAHA_ISEMRI_NO() == 0)
				return false;

			// ---------------------------------------------------------------------
			
			if (param.islemTipleri != null) {
				exists = false;
				for(IChecked islemTipi : param.islemTipleri){
					if(!islemTipi.getCheck()) continue;
					if (islemTipi.equals(IslemTipi.Tumu) || param.isemri.getISLEM_TIPI().equals(islemTipi)){
						exists = true;
						break;
					}
					
				}
				if(!exists) return false;
				
			} 
			
			// ---------------------------------------------------------------------

			if(param.altEmirTurleri != null) {
				exists = false;
				for (IChecked obj : param.altEmirTurleri) {
					if (!obj.getCheck()) continue;
					AltEmirTuru altEmirTuru = (AltEmirTuru) obj;
					if (altEmirTuru.altEmirTuru == -1 || altEmirTuru.altEmirTuru == param.isemri.getALT_EMIR_TURU()) {
						exists = true;
						break;
					}
				}
				if (!exists) return false;
			}

			// ---------------------------------------------------------------------

			if (param.tesisat_no > 0) {
				if (param.isemri.getTESISAT_NO() != param.tesisat_no) {
					return false;
				}
			}
		}
		
		// ---------------------------------------------------------------------
		// Sunucudan gelen yetkilere göre
		IEleman eleman = getEleman();
		if (eleman != null) {
			IYetki yetki = (IYetki) eleman.getYetki();
			if (yetki != null) {
				List<IIslemYetki> list = yetki.getIslemYetki();
				if (!list.isEmpty()) {
					boolean ok = false;
					IIslemTipi islemTipi = param.isemri.getISLEM_TIPI();
					int altEmirTuru = param.isemri.getALT_EMIR_TURU();
					for (IIslemYetki y : list) {
						if (islemTipi.equals(y.getISLEM_TIPI()) && altEmirTuru == y.getALT_EMIR_TURU()) {
							ok = true;
							break;
						}
					}
					return ok;
				}
			}
		}

		// ---------------------------------------------------------------------

		return true;

	}

	@Override
	public void Filter(List<IIsemri> list, IsemriFilterParam filter) {
		
		synchronized (list) {
			Iterator<IIsemri> iterator = list.iterator();
			while (iterator.hasNext()) {
				filter.isemri = iterator.next();
				if (!checkFilter(filter))
					iterator.remove();
			}
		}
	}
	
	
}
