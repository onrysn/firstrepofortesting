package mobit.elec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.IEndeksTipi;
import mobit.elec.enums.ISayacHaneSayisi;

public class Endeksler implements IEndeksler {

	private List<IEndeks> endeksler = new ArrayList<IEndeks>();

	@Override
	public void add(IEndeks endeks)
	{
		int index = -1;
		for(int i = 0; i < endeksler.size(); i++){
			IEndeks end = endeksler.get(i);
			if(end.getType().equals(endeks.getType())){
				index = i;
				break;
			}
		}
		
		if(index < 0)
			endeksler.add(endeks);
		else
			endeksler.set(index, endeks);
	}
	
	public void add(IEndeksTipi endeksTipi, ISayacHaneSayisi haneSayisi, String endeks) throws Exception
	{
		add(new Endeks(endeksTipi, haneSayisi, endeks));
	}
	
	public void add(IEndeksTipi endeksTipi, ISayacHaneSayisi haneSayisi, String tam, String kusurat) throws Exception
	{
		add(new Endeks(endeksTipi, haneSayisi, tam, kusurat));
	}
	
	@Override
	public void remove(IEndeksTipi endeksTipi)
	{
		for (Iterator<IEndeks> iterator = endeksler.iterator(); iterator.hasNext();) {
			IEndeks endeks = iterator.next();
		    if (endeks.getType().equals(endeksTipi)) {
		        iterator.remove();
		    }
		}
	}
	
	@Override
	public void remove(IEndeks endeks)
	{
		endeksler.remove(endeks);
		
	}
	
	@Override
	public List<IEndeks> getEndeksler() {
		
		List<IEndeks> list = new ArrayList<IEndeks>();
		for(IEndeks endeks : endeksler) list.add(endeks);
		return list;
	}
	
	@Override
	public IEndeks getEndeks(IEndeksTipi endeksTipi) {
		for (IEndeks end : endeksler)
			if (end != null)
				if (endeksTipi.equals(end.getType()))
					return end;
		return new Endeks(EndeksTipi.Tanimsiz);
	}

	@Override
	public String getAciklama() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String[]> getDetay() {
		// TODO Auto-generated method stub
		List<String[]> list = new ArrayList<String[]>();
		for(IEndeks endeks : endeksler) list.addAll(endeks.getDetay());
		return list;
	}

}
