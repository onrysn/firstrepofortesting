package mobit.elec.mbs.medas;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketException;
import java.sql.Connection;
import java.util.ArrayList;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeoutException;

import com.mobit.Globals;
import com.mobit.IApplication;
import com.mobit.IIslem;
import com.mobit.IIslemGrup;
import com.mobit.IIslemMaster;
import com.mobit.IResult;
import com.mobit.MobitException;
import com.mobit.RecordStatus;
import com.mobit.Yazici;

import mobit.eemr.Lun_Control;
import mobit.eemr.SayacEslestir;
import mobit.eemr.YaziciHataBildirimi;
import mobit.elec.IAdurum;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.IIsemriSoru;
import mobit.elec.IIsemriZabit;
import mobit.elec.enums.IIslemDurum;
import mobit.elec.enums.IslemDurum;
import mobit.elec.mbs.MbsException;
import mobit.elec.mbs.get.ICommand;
import mobit.elec.mbs.get.isemri_guncelle;
import mobit.elec.mbs.server.Base64;
import mobit.elec.medas.ws.SayacZimmetBilgi;
import mobit.elec.medas.ws.SayacZimmetBilgi.KacakBilgisi;
import mobit.elec.osos.viko.OperationService;

public class IslemSendTask2 extends mobit.elec.mbs.IslemSendTask {

	protected SayacZimmetBilgi medasServer;
	protected OperationService ososServer;

	public IslemSendTask2(IApplication app, IIslem islem) {
		super(app, islem);
		medasServer = (SayacZimmetBilgi) app.getServer(1);
		ososServer = (OperationService) app.getServer(2);
	}

	public Exception call() {
		if (islem instanceof IIslemGrup)
			return islemGrup((IIslemGrup) islem);
		else if (islem instanceof IIslemMaster)
			return islemMaster((IIslemMaster) islem);
		return new MobitException("İşlem tanımlanmamış!");

	}

	protected Exception islemGrup(IIslemGrup islemGrup) {
//ALTERNATİFTEN DÖNEN MESAJA BAKABİLİRSİN
		Exception e = null;
		MobitException ex = null;
		for (IIslem islem : islemGrup.getIslem()) {
			e = Islem(islem);
			if (e instanceof SocketException || e instanceof TimeoutException)
				return e;

			if(e instanceof MobitException){
				MobitException ee = (MobitException)e;
				if(ee.getCode() == IDef.WRN_KUYRUGA_EKLENDI) return ee;
			}
			if(e != null){
				if(ex == null) ex = new MobitException();
				ex.add(e);
			}
		}


		return ex;
	}

	protected Exception Islem(IIslem islem) {

		if (islem instanceof IIslemMaster)
			return islemMaster((IIslemMaster) islem);
		return null;
	}

	protected Exception islemMaster(IIslemMaster master) {

		Exception ex = null;
		Connection conn = null;

		IIslem islem = null;

		try {

			conn = app.getConnection();

			islem = master.getIslem();

			//muhammed gökkaya
			//ResimEkle(conn,master,islem);
			OncekiIslemYaziciKontrol();
			//SoruSenaryoService();
			int sunucu = 1;
			if (islem instanceof IIsemriSoru && ((IIsemriSoru) islem).getSORU_NO() == 0)
				sunucu = 0;
			// Mbs zabtı değilse gönderme
			else if (islem instanceof IIsemriZabit && ((IIsemriZabit) islem).getZABIT_TIPI() != IDef.MBS_ZABIT)
				sunucu = 2;
			else if (islem instanceof IKacak)
				sunucu = 2;

			// Mbs
			if (sunucu == 1) {

				try {



					mbsServer.sendIslem(islem);
					ResimEkle(conn,master,islem);//01.05.2021 HÜSEYİN EMRE ÇEVİK İŞLEM TAMAMLANDIKTAN SONRA RESİM YÜKLE
					return  MbsBasariliDurumu(conn, master, islem);

				} catch (MbsException e) {
					return MbsHataDurumu(conn, master, islem, e);

				} catch (SocketException | TimeoutException e) {

					return SunucuUlasimHataDurumu(conn, master, islem, e);
				}



			}
			// Medaş Web Service
			else if (sunucu == 2) {
				ResimEkle(conn,master,islem);
				return MedasWebService(conn, master, islem);
			}

		} catch (Exception e) {

			ex = e;

		}

		return ex;

	}
	public void ResimEkle(Connection conn, IIslemMaster master, IIslem islem){

		/*
		<kadi>string</kadi>
        <sifre>string</sifre>
        <isemrino>string</isemrino>
        <DosyaListesi>
          <IsEmriDosya>
            <fileByte>base64Binary</fileByte>
            <fileName>string</fileName>
          </IsEmriDosya>
          <IsEmriDosya>
            <fileByte>base64Binary</fileByte>
            <fileName>string</fileName>
          </IsEmriDosya>
        </DosyaListesi>
		 */
		String path = Globals.platform.getExternalStorageDirectory();
		//List<Object>  IsEmriDosya = new List<Object>();

		File f = new File(path);
		File file[] = f.listFiles();
		int foto_control=0;
		String fileName="";

		for (int i=0; i < file.length; i++)
		{
			if (file[i].getName().equals("medas")){

			}
			else {

				if (master.getIsemriNo()==Integer.parseInt(file[i].getName().split("_")[0]) ){
					foto_control++;



					fileName= file[i].getName();

					File fi = new File(file[i].getPath());
					int size = (int) fi.length();
					byte[] bytes = new byte[size];
					try {
						BufferedInputStream buf = new BufferedInputStream(new FileInputStream(fi));
						buf.read(bytes, 0, bytes.length-1);
						buf.close();
						String base=Base64.encodeToString(bytes,Base64.DEFAULT);
						//base=file[i].getPath().getBytes();

						if(ResimEkleServis(conn,master,islem,base,fileName).equals("successSuccess")){
							//Hüseyin Emre Çevik 12.04.2021 ( Fotoyu sunucuya gönderdikten sonra silme )
							file[i].delete();
						}

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}



	}
	public String ResimEkleServis(Connection conn, IIslemMaster master, IIslem islem,String filelist,String filename){
		try {
			//endeksörde ismeri_no ve tesisat_no aynıdır
			if (Integer.parseInt(filename.split("_")[0])==Integer.parseInt(filename.split("_")[1])){
				//optik = 1
				//okunmayan = 2
				islem = master.getIslem();
				IIsemriIslem isemriIslem = (IIsemriIslem) islem;
				IAdurum[] adurum = isemriIslem.getGELEN_DURUM();
				int okuma_durum=2;
				if (adurum[0].getABONE_DURUM_KODU() == 1 ||
						adurum[0].getABONE_DURUM_KODU() == 2 ||
						adurum[0].getABONE_DURUM_KODU() == 16 ||
						adurum[0].getABONE_DURUM_KODU() == 39 ||
						adurum[0].getABONE_DURUM_KODU() == 72)
				{
					okuma_durum = 1;
				}
				String mesaj= medasServer.AddPhoto(Integer.parseInt(filename.split("_")[0]),"Medas isemri uygulaması",filelist,filename,okuma_durum);
				mesaj+= medasServer.Resimekle(master.getIsemriNo(), filelist, filename);
				return mesaj;
			}
			//mobil
			else {
				String mesaj = medasServer.Resimekle(master.getIsemriNo(), filelist, filename);
				return mesaj;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  "basarisiz";
		}
	}
	public void OncekiIslemYaziciKontrol(){
		try {
			YaziciHataBildirimi yazici=new YaziciHataBildirimi();
			if (yazici.getTesisat_no()!=0 && !yazici.getHata_aciklama().equals("")) {
				String result = medasServer.YaziciKontrolService(yazici.getTesisat_no(),yazici.getIsemri_no(),yazici.getGonderme_dur(),yazici.getYazdirma_dur(),yazici.getHata_aciklama());
				if (result.equals("OK")){
					yazici.setTesisat_no(0);
					yazici.setIsemri_no(0);
					yazici.setHata_aciklama("");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public String SoruSenaryoService() {
        String SoruSenaryosu="";
		try {
			SoruSenaryosu=medasServer.SoruSenaryoService();
			return SoruSenaryosu;
		} catch (Exception ex) {

		}
		return  "";
	}

	// -------------------------------------------------------------------------

	private Exception SunucuUlasimHataDurumu(Connection conn, IIslemMaster master, IIslem islem, Exception e) {

		// Sunucuya ulaşılamamış. İş emri kuyrukta olarak
		// işaretlenecek
		isemri_guncelle.setISEMRI_DURUM(conn, master.getIsemriNo(), IslemDurum.Kuyrukta);
		return new MobitException(IDef.WRN_KUYRUGA_EKLENDI, "Sunucuya ulaşılamadığından iş emri kuyruğa eklendi!", e);

	}

	// -------------------------------------------------------------------------

	private Exception MbsBasariliDurumu(Connection conn, IIslemMaster master, IIslem islem) {

		IResult r = null;
		if (islem instanceof ICommand) {
			ICommand cmd = (ICommand) islem;
			r = cmd.getResult();
		}

		if (islem instanceof IIsemriIslem) {

			isemri_guncelle.setISEMRI_DURUM(conn, master.getIsemriNo(), IslemDurum.Tamamlandi);
		}

		master.setDurum(RecordStatus.Sent);
		if (r != null) {
			if (r.getRESULT_TYPE().equals("FTL") && r.getRESULT_CODE()==496)
			{
				Lun_Control zz=new Lun_Control();
				if (zz.ZabitDur==1) zz.setKacakVarmi(1);
			}
			master.setRESULT_TYPE(r.getRESULT_TYPE());
			master.setRESULT_CODE(r.getRESULT_CODE());
			master.setRESULT(r.getRESULT());
		} else {
			master.setRESULT_TYPE(IDef.OK);
			master.setRESULT_CODE(0);
		}
		try {
			app.Save(master);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return e;
		}
		return null;
	}

	private Exception MbsHataDurumu(Connection conn, IIslemMaster master, IIslem islem, MbsException e) {
		// put_isemri'nde Mbs'den bir hata alınmış ise durum
		// Atanmis yada Yapilamadi olarak işaretlenmesi
		if (e.getErrType().equals("ERR"))
		{

		}
		else
		{

			if (e.getErrType().equals("FTL") && e.getErrCode()==496)
			{
				Lun_Control zz=new Lun_Control();
				if (zz.ZabitDur==1) zz.setKacakVarmi(1);
			}
			//Eğer hata durumu err değilse resimleri uçur
			String path = Globals.platform.getExternalStorageDirectory();

			File f = new File(path);
			File file[] = f.listFiles();


			for (int i=0; i < file.length; i++)
			{
				if (file[i].getName().equals("medas")){

				}
				else {
					if (master.getIsemriNo()==Integer.parseInt(file[i].getName().split("_")[0]) ){
						file[i].delete();
					}

				}
			}

		}
		if (islem instanceof IIsemriIslem) {
			// Uyarı alınmış ise atanmış olacak. Hata veriyorsa tamamlandı olacak
			IIslemDurum islemDurum = (e.getErrType().equals(IDef.WRN))  ? IslemDurum.Yapilamadi : IslemDurum.Atanmis;
			isemri_guncelle.setISEMRI_DURUM(conn, master.getIsemriNo(), islemDurum);
		}

		master.setRESULT_TYPE(e.getErrType());
		master.setRESULT_CODE(e.getErrCode());
		master.setRESULT(e.getMessage());
		master.setDurum(RecordStatus.Sent);

		try {
			app.Save(master);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			return e1;
		}
		return e;
	}

	// -------------------------------------------------------------------------

	private Exception MedasWebService(Connection conn, IIslemMaster master, IIslem islem) {

		try {


			if (islem instanceof IIsemriZabit) {
				IIsemriZabit zabit = (IIsemriZabit) islem;
				medasServer.kacak_kayit(zabit.getSAHA_ISEMRI_NO(), app.getTime(), zabit.getZABIT_SERI(),
						zabit.getZABIT_NO(), app.getEleman().getELEMAN_KODU(), zabit.getOKUYUCU2_KODU());
			} else if (islem instanceof IKacak) {
				IKacak k = (IKacak) islem;
				KacakBilgisi kb = new KacakBilgisi(master, k);
				medasServer.KacakBilgisiGondermeServisi(kb);

			}

			return MedasWebServiceBasariliDurumu(conn, master, islem);

		} catch (SayacZimmetBilgi.MedasWsException e) {

			return WebServiceHataDurumu(conn, master, islem, e);
		}

		catch (SocketException e) {
			// TODO Auto-generated catch block
			return SunucuUlasimHataDurumu(conn, master, islem, e);
		} catch (Exception e) {

			return e;
		}

	}

	private Exception MedasWebServiceBasariliDurumu(Connection conn, IIslemMaster master, IIslem islem) {

		try {
			
			master.setDurum(RecordStatus.Sent);
			app.Save(master);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return e;
		}
		return null;
	}

	private Exception WebServiceHataDurumu(Connection conn, IIslemMaster master, IIslem islem,
			SayacZimmetBilgi.MedasWsException e) {

		master.setRESULT_TYPE("WS");
		master.setRESULT_CODE(e.getCode());
		master.setRESULT(e.getMessage());
		master.setDurum(RecordStatus.Sent);
		try {
			app.Save(master);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			return e1;
		}
		return e;
	}

	// -------------------------------------------------------------------------

}
