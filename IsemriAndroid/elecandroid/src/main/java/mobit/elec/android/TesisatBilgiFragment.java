package mobit.elec.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mobit.IDetail;
import com.mobit.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mobit.elec.Globals;
import mobit.elec.IElecApplication;
import mobit.elec.IIsemri;
import mobit.elec.INavigate;
import mobit.elec.ISayacBilgi;
import mobit.elec.ITesisatBilgi;
import mobit.elec.enums.IIslemTipi;
import mobit.elec.enums.IslemTipi;
import mobit.elec.enums.SayacKodu;

public class TesisatBilgiFragment extends Fragment {

	IElecApplication app;
	private TextView textIslemTipi;
	private TextView textUnvan;
	public TextView textKarneNo;
	private TextView textAdres;
	private TextView textAciklama;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(!(Globals.app instanceof IElecApplication)){
			return null;
		}
		app = (IElecApplication)Globals.app;

		return inflater.inflate(R.layout.fragment_tesisat_bilgi, container, false);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		textIslemTipi = (TextView) view.findViewById(R.id.textIslemTipi);
		textUnvan = (TextView) view.findViewById(R.id.textUnvan);
		textKarneNo = (TextView) view.findViewById(R.id.textKarneNo);
		textAdres = (TextView) view.findViewById(R.id.textAdres);
		textAciklama = (TextView) view.findViewById(R.id.textAciklama);


	}

	public void show(IIsemri isemri,String Aciklama) {

		if (Aciklama==null)
			Aciklama=" ";

		clear();
		if (!(isemri instanceof ITesisatBilgi)) {
			// Desteklenmiyor
			return;
		}
		int color = isemri.getISEMRI_DURUMU().getColor();

		// Sayaç okuma ekranı için
		if (isemri.getISLEM_TIPI().equals(IslemTipi.SayacOkuma)) {
			IIslemTipi islemTipi = isemri.getISLEM_TIPI();
			textIslemTipi.setText(String.format("%s", islemTipi));
			textIslemTipi.setBackgroundColor(app.getIslemRenk(islemTipi));
			ISayacBilgi sayac = isemri.getSAYACLAR().getSayac(SayacKodu.Aktif);
			String sayacBilgi = "";
			if(sayac != null) sayacBilgi = String.format("S:%d/ %s", sayac.getSAYAC_NO(), sayac.getMARKA());
			textUnvan.setText(String.format("Tesisat No: %d\n%s\n%s", isemri.getTESISAT_NO(), sayacBilgi, isemri.getUNVAN()));
			textUnvan.setBackgroundColor(color);
			String pattern = "dd/MM/yyyy";
			DateFormat df = new SimpleDateFormat(pattern);
			String todayAsString = df.format(isemri.getSON_OKUMA_TARIHI());
			textKarneNo.setText(String.format("Karne: %d\nSıra:%d/%d Ç:%.0f\nT:"+isemri.getTARIFE_KODU()+"-So:"+todayAsString, isemri.getKARNE_NO(), isemri.getSIRA_NO(), isemri.getSIRA_EK(),isemri.getCARPAN()));
			textKarneNo.setBackgroundColor(color);
			textAdres.setText(String.format("%s %s", isemri.getADRES(),isemri.getADRES_TARIF()));
			textAciklama.setVisibility(View.VISIBLE);
			textAciklama.setText(Aciklama);
		} else {

			IIslemTipi islemTipi = isemri.getISLEM_TIPI();
			String s = isemri instanceof IDetail ? ((IDetail) isemri).getAciklama() : islemTipi.toString();
			textIslemTipi.setText(s);
			textIslemTipi.setBackgroundColor(app.getIslemRenk(islemTipi));

			s = String.format(Globals.locale, "Tesisat No: %d\n%s", isemri.getTESISAT_NO(), isemri.getUNVAN());
			s = s + String.format(Globals.locale, "\n %d - %d - %s", isemri.getALT_EMIR_TURU(),
					isemri.getTARIFE_KODU(), Globals.dateFmt.format(isemri.getSON_OKUMA_TARIHI()));

			textUnvan.setText(s);
			textUnvan.setBackgroundColor(color);
			textKarneNo.setText(String.format("Karne: %d\nSıra:%d/%d", isemri.getKARNE_NO(), isemri.getSIRA_NO(), isemri.getSIRA_EK()));
			textKarneNo.setBackgroundColor(color);
			textAdres.setText(String.format("%s", isemri.getADRES()));
			textAciklama.setVisibility(View.VISIBLE);
			textAciklama.setText(Aciklama);
		}
		if (isemri.getKESME_DUR().equals("SK") || isemri.getKESME_DUR().equals("TTK") || isemri.getKESME_DUR().equals("BTK") || isemri.getKESME_DUR().equals("ACK")
				||isemri.getKESME_DUR().equals("TCK")|| isemri.getKESME_DUR().equals("ASY")|| isemri.getKESME_DUR().equals("TNK")|| isemri.getKESME_DUR().equals("MYK")
				||isemri.getKESME_DUR().equals("SBK")|| isemri.getKESME_DUR().equals("KCK")|| isemri.getKESME_DUR().equals("FBK")){ //Onur Kesik Tesisatlarda Uyarı için eklendi
			AlertDialog.Builder alert = new AlertDialog.Builder(textAciklama.getContext());
			alert.setMessage("İşlem Yapacağınız Tesisat Kesik Konumdadır..!");
			alert.setTitle("Dikkat...");
			alert.setIcon(R.drawable.eleclogonot);
			alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			});

			alert.show();
		}
	}

	public void show(IIsemri isemri) {



		clear();
		if (!(isemri instanceof ITesisatBilgi)) {
			// Desteklenmiyor
			return;
		}
		int color = isemri.getISEMRI_DURUMU().getColor();

		// Sayaç okuma ekranı için
		if (isemri.getISLEM_TIPI().equals(IslemTipi.SayacOkuma)) {
			IIslemTipi islemTipi = isemri.getISLEM_TIPI();
			textIslemTipi.setText(String.format("%s", islemTipi));
			textIslemTipi.setBackgroundColor(app.getIslemRenk(islemTipi));
			ISayacBilgi sayac = isemri.getSAYACLAR().getSayac(SayacKodu.Aktif);
			String sayacBilgi = "";
			if(sayac != null) sayacBilgi = String.format("S:%d/ %s", sayac.getSAYAC_NO(), sayac.getMARKA());
			textUnvan.setText(String.format("Tesisat No: %d\n%s\n%s", isemri.getTESISAT_NO(), sayacBilgi, isemri.getUNVAN()));
			textUnvan.setBackgroundColor(color);
			String pattern = "dd/MM/yyyy";
			DateFormat df = new SimpleDateFormat(pattern);
			String todayAsString = df.format(isemri.getSON_OKUMA_TARIHI());
			textKarneNo.setText(String.format("Karne: %d\nSıra:%d/%d Ç:%.0f\nT:"+isemri.getTARIFE_KODU()+"-So:"+todayAsString, isemri.getKARNE_NO(), isemri.getSIRA_NO(), isemri.getSIRA_EK(),isemri.getCARPAN()));
			textKarneNo.setBackgroundColor(color);
			textAdres.setText(String.format("%s %s", isemri.getADRES(),isemri.getADRES_TARIF()));
			textAciklama.setVisibility(View.GONE);
		} else {

			IIslemTipi islemTipi = isemri.getISLEM_TIPI();
			String s = isemri instanceof IDetail ? ((IDetail) isemri).getAciklama() : islemTipi.toString();
			textIslemTipi.setText(s);
			textIslemTipi.setBackgroundColor(app.getIslemRenk(islemTipi));

			s = String.format(Globals.locale, "Tesisat No: %d\n%s", isemri.getTESISAT_NO(), isemri.getUNVAN());
			s = s + String.format(Globals.locale, "\n %d - %d - %s", isemri.getALT_EMIR_TURU(),
					isemri.getTARIFE_KODU(), Globals.dateFmt.format(isemri.getSON_OKUMA_TARIHI()));

			textUnvan.setText(s);
			textUnvan.setBackgroundColor(color);
			textKarneNo.setText(String.format("Karne: %d\nSıra:%d/%d", isemri.getKARNE_NO(), isemri.getSIRA_NO(), isemri.getSIRA_EK()));
			textKarneNo.setBackgroundColor(color);
			textAdres.setText(String.format("%s", isemri.getADRES()));
			textAciklama.setVisibility(View.GONE);
		}
	}
	private void clear() {
		textUnvan.setText("");
		textKarneNo.setText("");
		textAdres.setText("");
		textAciklama.setText("");
	}
}
