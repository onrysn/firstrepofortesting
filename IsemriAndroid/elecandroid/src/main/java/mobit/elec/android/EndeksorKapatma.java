package mobit.elec.android;

import com.mobit.Callback;
import com.mobit.DialogMode;
import com.mobit.DialogResult;
import com.mobit.IDialogCallback;
import com.mobit.IForm;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.ILocation;
import com.mobit.MobitException;
import com.mobit.MsgInfo;

import mobit.android.AndroidPlatform;
import mobit.elec.Globals;
import mobit.elec.IElecApplication;
import mobit.elec.medas.ws.SayacZimmetBilgi;

public class EndeksorKapatma {

    protected IElecApplication app;
    protected Callback clb;
    protected IIslemGrup islemGrup;
    SayacZimmetBilgi medasServer;

    public EndeksorKapatma() {
        if (!(Globals.app instanceof IElecApplication)) {
            clb.run(new MobitException(MsgInfo.DESTEKLENMIYOR));
            return;
        }

        app = (IElecApplication) Globals.app;

        if (!(app.getActiveIslem() instanceof IIslemGrup)) {
            clb.run(new MobitException(MsgInfo.DESTEKLENMIYOR));
            return;
        }

        try {
            EndeksorKapatma();
        } catch (Exception e) {

        }
    }

    void EndeksorKapatma(){ // H.Elif
        if (app.getActiveIsemri().getISLEM_TIPI().toString().equals("Kesme") && app.getActiveIsemri().getALT_EMIR_TURU() == 10) {
            medasServer =(mobit.elec.medas.ws.SayacZimmetBilgi) app.getServer(1);
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String res = medasServer.EndeksorKapatmaService(app.getActiveIsemri().getTESISAT_NO(), app.getActiveIsemri().getSAHA_ISEMRI_NO());
                    } catch (Exception e) {
                    }
                }
            });
            thread.start();
            thread.interrupt();
        }
    }

}
