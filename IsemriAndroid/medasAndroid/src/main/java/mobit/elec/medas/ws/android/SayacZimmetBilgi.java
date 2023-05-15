package mobit.elec.medas.ws.android;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.mobit.Callback;
import com.mobit.IApplication;
import com.mobit.IForm;
import mobit.elec.mbs.medas.IMedasApplication;
import mobit.elec.mbs.medas.IsemriDosya;
import mobit.elec.mbs.medas.IslemSendTask2;

public class SayacZimmetBilgi extends mobit.elec.medas.ws.SayacZimmetBilgi {

	IMedasApplication app;

	@Override
	public boolean isTest()
	{
		return false;
	}

	@Override
	public void init(IApplication app) throws Exception {
		// TODO Auto-generated method stub
		this.app = (IMedasApplication)app;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getUTCTime() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Tesisatsız Kaçakların İhbarı için
	@Override
	public void KacakBilgisiGondermeServisi(IForm form, final KacakBilgisi kacakBilgi, final Callback clb)
			throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					KacakBilgisiGondermeServisi(kacakBilgi);
				} catch (Exception e) {

					return e;
				}
				return null;
			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}

	// Sayaç değişiminde sayaç bilgileri ve zimmet kontrolü için
	@Override
	public void SayacBilgiService(final IForm form, final int okuyucuKodu, final int seriNo, final String sayacMarka,
			final int isEmriNo, final Callback clb) throws Exception {

		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			private ProgressDialog progressDialog = new ProgressDialog((Activity) form);

			@Override
			protected void finalize() throws Throwable {
				progressDialog.dismiss();
				super.finalize();
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.setMessage("Saya bilgisi alınıyor...");
				progressDialog.show();
			}

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return SayacBilgiService(okuyucuKodu, seriNo, sayacMarka, isEmriNo);
				} catch (Exception e) {

					return e;
				}
			}

			@Override
			protected void onPostExecute(Object result) {
				try {
					if (clb != null)
						clb.run(result);
				} finally {
					if (progressDialog != null)
						progressDialog.dismiss();
				}
			}
		};
		asyncTask.execute();
	}

	// Sayaç takma sırasında eski sayaç takılacak ise eski sayacın bilgileri ve
	// endeks bilgilerini almak için
	@Override
	public void SayacEndeksServisi(IForm form, final String tesisatNo, final String sayacNo, final Callback clb) {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return SayacEndeksServisi(tesisatNo, sayacNo);
				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}

	// Sayaç değişiminde eski bir sayaç takılıyorsa endekslerini almak için.
	@Override
	public void SayacSenkService(IForm form, final int okuyucuKodu, final int seriNo, final String sayacMarka, final Callback clb)
			 {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return SayacSenkService(okuyucuKodu, seriNo, sayacMarka);
					
				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}

	// Abone durum koduna karşılık gelen açıklama listesi
	@Override
	public void DurumKoduDataService(final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return DurumKoduDataService();
					
					
				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}

	@Override
	public void kacak_kayit(final int isemri_no, final Date kayit_tarihi, final String zabit_seri, final int zabit_no,
			final int memurbir_sicil_no, final int memuriki_sicil_no, final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return kacak_kayit(isemri_no, kayit_tarihi, zabit_seri, zabit_no, memurbir_sicil_no,
							memuriki_sicil_no);

				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	//Muhammed Gökkaya Resimekle servisi
	@Override
	public void Resimekle(final int isemri_no, final  String DosyaListesi,final String DosyaAdi, final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return Resimekle(isemri_no, DosyaListesi,DosyaAdi);

				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}

	//Soru Senaryo Servisi
	@Override
	public void SoruSenaryoService(final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return SoruSenaryoService();

				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}


	// H.Elif
	@Override
	public void OlcuKontrolService(final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {
					return OlcuKontrolService();
				} catch (Exception e) {
					return e;
				}
			}
			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}

	// H.Elif
	@Override
	public void OlcuIsemriKontrolService(final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {
					return OlcuIsemriKontrolService();
				} catch (Exception e) {
					return e;
				}
			}
			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}


	@Override
	public void EndeksorKapatmaService(final int tesisat_no, final int isemri_no, final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return EndeksorKapatmaService(tesisat_no, isemri_no);

				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}


	Exception ex;
	public void endeksor_kullanici(final int okuyucu_kodu, final Callback clb) throws Exception
	{
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				
				try {

					Object obj = endeksor_kullanici(okuyucu_kodu);
					return obj;

				} catch (Exception e) {

					ex = e;
					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	
	
	public void endeksor_muhur(final int okuyucu_kodu, final Callback clb) throws Exception
	{
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				
				try {

					Object obj = endeksor_muhur(okuyucu_kodu);
					return obj;

				} catch (Exception e) {

					ex = e;
					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}


	public void endeksor_zabit(final int okuyucu_kodu, final Callback clb) throws Exception
	{
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub

				try {

					Object obj = endeksor_zabit(okuyucu_kodu);
					return obj;

				} catch (Exception e) {

					ex = e;
					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	@Override
	public void YaziciKontrolService(final int tesisat_no,final int isemri_no,final int gonderme_dur,final int yazdirma_dur,final String hata_aciklama, final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return YaziciKontrolService(tesisat_no,isemri_no,gonderme_dur,yazdirma_dur,hata_aciklama);

				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	@Override
	public void YkpEndeksService(final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return YkpEndeksService();

				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	@Override
	public void GidilmeyenKarne(final int karne_no,final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {
					Object obj = GidilmeyenKarne(karne_no);
					return obj;

				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	@Override
	public void AddOrtakTrafo(final int tesisat_no,final int isemri_no,final String yenitesisat_no,final int teyit_dur,final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {
					return AddOrtakTrafo(tesisat_no,isemri_no,yenitesisat_no,teyit_dur);


				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	@Override
	public void OrtakTrafoKontrol(final int tesisat_no,final int isemri_no,final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {
					return OrtakTrafoKontrol(tesisat_no,isemri_no);


				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	@Override
	public void AddPhoto(final int tesisat_no,final String aciklama,final String dosyalistesi,final String dosyaadi,final int okumadurumu,final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {
					return AddPhoto(tesisat_no,aciklama,dosyalistesi,dosyaadi,okumadurumu);

				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	@Override
	public void YikikIhbarServisi(final int TESISAT_NO,final String SAYAC_NO,final String DIREK_NO,final String BOX_NO,final int REFERANS_TESISAT_NO,final int OKUYUCU_KODU,final String CBS_X,final String CBS_Y,final String KAYIT_TARIHI,final String ACIKLAMA,final String ADRES_TARIFI,final String T1,final String T2,final String T3,final String RI,final String RC,final Callback clb) throws Exception {
		AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				try {

					return YikikIhbarServisi(TESISAT_NO,SAYAC_NO,DIREK_NO,BOX_NO,REFERANS_TESISAT_NO,OKUYUCU_KODU,CBS_X,CBS_Y,KAYIT_TARIHI,ACIKLAMA,ADRES_TARIFI,T1,T2,T3,RI,RC);

				} catch (Exception e) {

					return e;
				}

			}

			@Override
			public void onPostExecute(Object result) {
				if (clb != null)
					clb.run(result);
			}
		};
		asyncTask.execute();
	}
	

	
}
