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
import mobit.eemr.OlcuDevreForm;
import mobit.elec.Globals;
import mobit.elec.IElecApplication;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.medas.ws.SayacZimmetBilgi;

public class IslemTamamla implements Runnable {

    protected IElecApplication app;
    protected IMedasApplication app2;
    protected IForm form;
    protected Callback clb;
    protected IIslemGrup islemGrup;
    SayacZimmetBilgi medasServer;

    public IslemTamamla(IForm form, Callback clb) {
        super();
        this.form = form;
        this.clb = clb;
        try {
            if (form == null)
                form = (IForm) AndroidPlatform.getActivity();
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        if (!(Globals.app instanceof IElecApplication)) {
            clb.run(new MobitException(MsgInfo.DESTEKLENMIYOR));
            return;
        }

        app = (IElecApplication) Globals.app;

        if (!(app.getActiveIslem() instanceof IIslemGrup)) {
            clb.run(new MobitException(MsgInfo.DESTEKLENMIYOR));
            return;
        }
        islemGrup = (IIslemGrup) app.getActiveIslem();

        try {
            ILocation location = app.getLocation();
            if (location == null || !location.isValid()) {
                app.ShowMessage(form, "Konum alınamadı. Devam etmek istiyor musunuz?", "", DialogMode.YesNo, 0,
                        0, new IDialogCallback() {

                            @Override
                            public void DialogResponse(int msg_id, DialogResult result) {
                                // TODO Auto-generated method stub
                                if (result == DialogResult.Yes) {
                                    run2();
                                }
                            }

                        });
                return;
            }

            run2();

        } catch (Exception e) {
            app.ShowException(form, e);
            clb.run(e);
        }

    }

    static class Param {
        public IIslem islem;
        public Object obj;

        public Param(IIslem islem, Object obj) {
            this.islem = islem;
            this.obj = obj;
        }
    }

    private void run2() {
        try {

            final IIslem islem = app.saveIslem(islemGrup);
            app.sendIslem(form, islem, new Callback() {

                @Override
                public Object run(final Object obj) {
                    // TODO Auto-generated method stub

                    Object res = clb.run(new Param(islem, obj));

                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                app2 = (IMedasApplication) Globals.app;
                                medasServer = (mobit.elec.medas.ws.SayacZimmetBilgi) app2.getServer(1);

                                if (app.getActiveIsemri().getISLEM_TIPI().toString().equals("Kesme") && app.getActiveIsemri().getALT_EMIR_TURU() == 10) {
                                    String res = medasServer.EndeksorKapatmaService(app.getActiveIsemri().getTESISAT_NO(), app.getActiveIsemri().getSAHA_ISEMRI_NO());

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    thread.interrupt();

                    return res;
                }
            }, 15000);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            app.ShowException(form, e);
            clb.run(e);
        }
    }

}
