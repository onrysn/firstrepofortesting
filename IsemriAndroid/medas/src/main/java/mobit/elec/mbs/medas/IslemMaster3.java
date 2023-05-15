package mobit.elec.mbs.medas;

import com.mobit.IApplication;
import com.mobit.IIslem;
import com.mobit.MobitException;

import java.util.List;

import mobit.elec.IIsemriSoru;
import mobit.elec.mbs.IslemMaster2;
import mobit.elec.mbs.get.isemri_soru;

/**
 * Created by Genel on 5.10.2018.
 */

public class IslemMaster3 extends IslemMaster2 {

    public IslemMaster3(IApplication app, IIslem iislem) throws MobitException {
        // TODO Auto-generated constructor stub
        super(app, iislem);
    }

    public IslemMaster3(IApplication app) throws MobitException {
        // TODO Auto-generated constructor stub
        super(app);
    }

    public IslemMaster3(IIslem islem) throws MobitException {
        // TODO Auto-generated constructor stub
        super(islem);
    }
    @Override
    protected IIslem loadIslem() throws Exception {
        // TODO Auto-generated method stub
        islem = super.loadIslem();
        if(islem != null) return islem;
        if (getTabloId() == kacak.TABLOID) {
            List<IKacak> list = kacak.selectIslem(app.getConnection(), getId());
            if (list.size() == 1)
                islem = list.get(0);
        }
        return islem;
    }
}
