package mobit.elec.mbs;

import java.util.ArrayList;
import java.util.List;

import mobit.elec.Globals;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemriRapor;
import mobit.elec.enums.IIslemDurum;
import mobit.elec.enums.IIslemTipi;

public class IsemriRapor implements IIsemriRapor {

	public static class Item {
		public IIslemTipi islemTipi;
		public IIslemDurum islemDurum;
		int miktar;
		
		public Item(IIslemTipi islemTipi, IIslemDurum islemDurum, int miktar)
		{
			this.islemTipi = islemTipi;
			this.islemDurum = islemDurum;
			this.miktar = miktar;
			
		}
	}
	
	IElecApplication app;
	public IsemriRapor()
	{
		app = (IElecApplication)Globals.app;
		
	}
	
	@Override
	public String getAciklama() {
		// TODO Auto-generated method stub
		return "İş Emri Raporu";
	}

	@Override
	public List<String[]> getDetay() {
		// TODO Auto-generated method stub
		List<String[]> detail = new ArrayList<String[]>();
		for(int i = 0; i < list.size(); i++){
			Item item = list.get(i);
			String [] s = new String[2];
			s[0] = item.islemTipi.toString();
			s[1] = String.format("  %-12s:%5d", item.islemDurum, item.miktar);
			detail.add(s);
		}
		return detail;
	}
	
	List<Item> list = new ArrayList<Item>();
	public void add(Item item)
	{
		list.add(item);
	}

}
