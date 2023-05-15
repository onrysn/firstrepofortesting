package mobit.elec;

import java.util.ArrayList;
import java.util.List;

public class Yetki implements IYetki {

	private List<IIslemYetki> list = new ArrayList<IIslemYetki>();
	@Override
	public List<IIslemYetki> getIslemYetki() {
		// TODO Auto-generated method stub
		return list;
	}

}
