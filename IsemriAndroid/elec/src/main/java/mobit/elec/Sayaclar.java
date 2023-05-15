package mobit.elec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mobit.MobitException;
import mobit.eemr.IReadResult;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.IEndeksTipi;
import mobit.elec.enums.ISayacKodu;
import mobit.elec.enums.SayacKodu;

public class Sayaclar implements ISayaclar {

	private List<ISayacBilgi> sayaclar = new ArrayList<ISayacBilgi>();

	@Override
	public void add(ISayacBilgi sayac) throws Exception
	{
		
		for(ISayacBilgi syc : sayaclar){
			if(syc.getSAYAC_KODU().equals(sayac.getSAYAC_KODU()))
				throw new MobitException(String.format("%s tipte bir sayaç eklenmiş!", sayac.getSAYAC_KODU()));
		}
		sayaclar.add(sayac);
	}
	
	@Override
	public Object clone()
	{
		ISayaclar sayaclar = new Sayaclar();
		for(ISayacBilgi sayac : getSayaclar())
			try {
				sayaclar.add((ISayacBilgi)sayac.clone());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return sayaclar;
	}
	
	@Override
	public void addEndeks(IEndeksTipi endeksTipi, String endeks) throws Exception
	{
		ISayacBilgi sayac = getSayac(endeksTipi);
		if(sayac == null) throw new MobitException(String.format("%s endeks tipi destekleyen bir sayaç yok", endeksTipi));
		sayac.getENDEKSLER().add(endeksTipi,sayac.getHANE_SAYISI(), endeks);
	}
	
	@Override
	public void addEndeks(IEndeksTipi endeksTipi, String tam, String kusurat) throws Exception
	{
		ISayacBilgi sayac = getSayac(endeksTipi);
		if(sayac == null) throw new MobitException(String.format("%s endeks tipi destekleyen bir sayaç yok", endeksTipi));
		sayac.getENDEKSLER().add(endeksTipi,sayac.getHANE_SAYISI(), tam, kusurat);
	}
	
	@Override
	public void addEndeks(IEndeks endeks) throws Exception
	{
		ISayacBilgi sayac = getSayac(endeks.getType());
		if(sayac == null) throw new MobitException(String.format("%s endeks tipi destekleyen bir sayaç yok", endeks.getType()));
		sayac.getENDEKSLER().add(endeks);
	}
	
	@Override
	public void remove(ISayacKodu sayacKodu)
	{
		for (Iterator<ISayacBilgi> iterator = sayaclar.iterator(); iterator.hasNext();) {
			ISayacBilgi sayac = iterator.next();
		    if (sayac.getSAYAC_KODU().equals(sayacKodu)) {
		        iterator.remove();
		    }
		}
	}
	
	@Override
	public void remove(ISayacBilgi sayac)
	{
		for (Iterator<ISayacBilgi> iterator = sayaclar.iterator(); iterator.hasNext();) {
			if (iterator.next().equals(sayac)) {
		        iterator.remove();
		    }
		}
		
	}
	
	@Override
	public void remove(int sayac_no)
	{
		for (Iterator<ISayacBilgi> iterator = sayaclar.iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
			if (obj.equals(sayac_no)) {
		        iterator.remove();
		    }
		}
		
	}
	
	@Override
	public List<ISayacBilgi> getSayaclar() {
		
		//List<ISayacBilgi> list = new ArrayList<ISayacBilgi>();
		//for(ISayacBilgi sayac : sayaclar) list.add(sayac);
		return sayaclar;
	}
	
	@Override
	public IEndeksler getEndeksler() {
		
		IEndeksler endeksler = new Endeksler();
		for(ISayacBilgi sayac : sayaclar){
			IEndeksler endler = sayac.getENDEKSLER();
			for(IEndeks endeks : endler.getEndeksler()) endeksler.add(endeks);
		}
		return endeksler;
	}
	
	@Override
	public ISayacBilgi getSayac(ISayacKodu sayacKodu) {
		for (ISayacBilgi s : sayaclar) if(s.getSAYAC_KODU().is(sayacKodu)) return s;	
		return null;
	}
	
	@Override
	public ISayacBilgi getSayac(int sayac_no) {
		for (ISayacBilgi s : sayaclar)
			if (s != null)
				if (s.getSAYAC_NO() == sayac_no)
					return s;
		
		//throw new MobitException(String.format("%d nolu sayaç yok!", sayac_no));
		return null;
	}
	
	@Override
	public ISayacBilgi getSayac(IEndeksTipi endeksTipi){
		
		if(endeksTipi.equals(EndeksTipi.Toplam) || 
				endeksTipi.equals(EndeksTipi.Gunduz) ||
				endeksTipi.equals(EndeksTipi.Puant) ||
				endeksTipi.equals(EndeksTipi.Gece)||
				endeksTipi.equals(EndeksTipi.Demand)){
			
			for (ISayacBilgi s : sayaclar) if(s.getSAYAC_KODU().is(SayacKodu.Aktif)) return s; 
		}
		else if(endeksTipi.equals(EndeksTipi.Enduktif)){
			for (ISayacBilgi s : sayaclar) if(s.getSAYAC_KODU().is(SayacKodu.Reaktif)) return s;
		}
		else if(endeksTipi.equals(EndeksTipi.Kapasitif)){
			for (ISayacBilgi s : sayaclar) if(s.getSAYAC_KODU().is(SayacKodu.Kapasitif)) return s;
		}
		
		return null;
	}
	
	

	@Override
	public List<String[]> getDetay() {
		// TODO Auto-generated method stub
		List<String[]> list = new ArrayList<String[]>();
		if(sayaclar.size() > 0){
			list.add(new String[]{"SAYAÇLAR"});
			for(ISayacBilgi sayac : sayaclar) list.addAll(sayac.getDetay());
		}
		return list;
	}

	@Override
	public String getAciklama() {
		// TODO Auto-generated method stub
		return "Sayaçlar";
	}
	
	@Override
	public void Kontrol() throws Exception
	{
		List<ISayacBilgi> sayaclar = getSayaclar();
		if(sayaclar.isEmpty()) throw new MobitException("Sayaç yok");
		for(ISayacBilgi sayac : sayaclar)
			sayac.Kontrol();
	}
	
	@Override
	public void EndeksKontrol() throws Exception
	{
		List<ISayacBilgi> sayaclar = getSayaclar();
		if(sayaclar.isEmpty()) throw new MobitException("Sayaç yok");
		for(ISayacBilgi sayac : getSayaclar())
			sayac.EndeksKontrol();
	}

	@Override
	public List<IReadResult> getOptikResult() {
		// TODO Auto-generated method stub
		List<IReadResult> results = new ArrayList<IReadResult>();
		for(ISayacBilgi sayac : sayaclar){
			if(sayac.getOptikResult() != null)
				results.add(sayac.getOptikResult());
		}
		
		return results;
	}
	
	@Override
	public void setOptikResult(IReadResult result) throws Exception {
		// TODO Auto-generated method stub
		if(result == null){
			for(ISayacBilgi sayac : sayaclar) sayac.setOptikResult(result);
			return;
		}
		int sayac_no = Integer.parseInt(result.get_sayac_no());
		for(ISayacBilgi sayac : sayaclar){
			if(sayac.getSAYAC_NO() == sayac_no){
				sayac.setOptikResult(result);
				return;
			}
		}
		if(Globals.isDeveloping()){
			ISayacBilgi sayac = sayaclar.get(0);
			sayac.setOptikResult(result);
			return;
			
		}
		throw new MobitException("Farklı sayaca ait potik port verisi!");
	}
	
	

}
