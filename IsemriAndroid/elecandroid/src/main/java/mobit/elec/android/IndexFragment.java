package mobit.elec.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.mobit.Globals;
import com.mobit.IForm;
import com.mobit.IIslem;
import mobit.eemr.IReadResult;
import mobit.elec.ElecApplication;
import mobit.elec.IEndeksler;
import mobit.elec.IIsemri;
import mobit.elec.IIsemriIslem;
import mobit.elec.ISayacBilgi;
import mobit.elec.ISayaclar;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.enums.SayacCinsi;
import mobit.elec.enums.SayacKodu;
import mobit.elec.mbs.get.field;

public class IndexFragment extends Fragment implements OnClickListener, OnFocusChangeListener {

	ElecApplication app = (ElecApplication) Globals.app;
	//IIsemri isemri;
	IIsemriIslem isemriIslem;
	ISayacBilgi sayac;
	ISayaclar sayaclar;

	IReadResult result = null;

	boolean readOnly = false;

	TextView textToplam;
	TextView textGunduz;
	TextView textPuant;
	TextView textGece;
	TextView textEnduktif;
	TextView textKapasitif;
	TextView textDemand;

	EditText editToplam;
	EditText editGunduz;
	EditText editPuant;
	EditText editGece;
	EditText editEnduktif;
	EditText editKapasitif;
	EditText editDemand;

	char decimalSep;

	public IndexFragment() {
		decimalSep = Globals.getDecimalFormatSymbols().getDecimalSeparator();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_index, container, false);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		textToplam = (TextView) view.findViewById(R.id.textToplam);
		textGunduz = (TextView) view.findViewById(R.id.textGunduz);
		textPuant = (TextView) view.findViewById(R.id.textPuant);
		textGece = (TextView) view.findViewById(R.id.textGece);
		textEnduktif = (TextView) view.findViewById(R.id.textEnduktif);
		textKapasitif = (TextView) view.findViewById(R.id.textKapasitif);
		textDemand = (TextView) view.findViewById(R.id.textDemand);

		editToplam = (EditText) view.findViewById(R.id.editToplam);
		editGunduz = (EditText) view.findViewById(R.id.editGunduz);
		editPuant = (EditText) view.findViewById(R.id.editPuant);
		editGece = (EditText) view.findViewById(R.id.editGece);
		editEnduktif = (EditText) view.findViewById(R.id.editEnduktif);
		editKapasitif = (EditText) view.findViewById(R.id.editKapasitif);
		editDemand = (EditText) view.findViewById(R.id.editDemand);

		editToplam.setOnFocusChangeListener(this);
		editGunduz.setOnFocusChangeListener(this);
		editPuant.setOnFocusChangeListener(this);
		editGece.setOnFocusChangeListener(this);
		editEnduktif.setOnFocusChangeListener(this);
		editKapasitif.setOnFocusChangeListener(this);
		editDemand.setOnFocusChangeListener(this);

		editToplam.setSelectAllOnFocus(true);
		editGunduz.setSelectAllOnFocus(true);
		editPuant.setSelectAllOnFocus(true);
		editGece.setSelectAllOnFocus(true);
		editEnduktif.setSelectAllOnFocus(true);
		editKapasitif.setSelectAllOnFocus(true);
		editDemand.setSelectAllOnFocus(true);


		hide();

	}

	public void clear() {

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				editToplam.setText("");
				editGunduz.setText("");
				editPuant.setText("");
				editGece.setText("");
				editEnduktif.setText("");
				editKapasitif.setText("");
				editDemand.setText("");
			}
		});
	}

	public void hide() {

		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				textToplam.setVisibility(View.INVISIBLE);
				textGunduz.setVisibility(View.INVISIBLE);
				textPuant.setVisibility(View.INVISIBLE);
				textGece.setVisibility(View.INVISIBLE);
				textEnduktif.setVisibility(View.INVISIBLE);
				textKapasitif.setVisibility(View.INVISIBLE);
				textDemand.setVisibility(View.INVISIBLE);

				editToplam.setVisibility(View.INVISIBLE);
				editGunduz.setVisibility(View.INVISIBLE);
				editPuant.setVisibility(View.INVISIBLE);
				editGece.setVisibility(View.INVISIBLE);
				editEnduktif.setVisibility(View.INVISIBLE);
				editKapasitif.setVisibility(View.INVISIBLE);
				editDemand.setVisibility(View.INVISIBLE);

				clear();
			}

		});

	}

	public void save() throws Exception {
		if (sayaclar != null) {
			ISayacBilgi sb = sayaclar.getSayac(SayacKodu.Aktif);
			if(sb != null){
				if(sb.getSAYAC_CINSI() == SayacCinsi.Mekanik){
					sayaclar.addEndeks(EndeksTipi.Gunduz, editGunduz.getText().toString());
				}
				else {
					//if(!editToplam.getText().toString().isEmpty()) sayaclar.addEndeks(EndeksTipi.Toplam, editToplam.getText().toString());
					sayaclar.addEndeks(EndeksTipi.Gunduz, editGunduz.getText().toString());
					sayaclar.addEndeks(EndeksTipi.Puant, editPuant.getText().toString());
					sayaclar.addEndeks(EndeksTipi.Gece, editGece.getText().toString());
					sayaclar.addEndeks(EndeksTipi.Demand, editDemand.getText().toString());
				}
			}
			sb = sayaclar.getSayac(SayacKodu.Reaktif);
			if(sb != null) sayaclar.addEndeks(EndeksTipi.Enduktif, editEnduktif.getText().toString());
			sb = sayaclar.getSayac(SayacKodu.Kapasitif);
			if(sb != null) sayaclar.addEndeks(EndeksTipi.Kapasitif, editKapasitif.getText().toString());
			
		}
	}
	
	public void show(IIslem iislem, IReadResult result) {

		/*
		hide();

		if (!(iislem instanceof IIsemriIslem)) {
			// Desteklenmiyor
			return;
		}

		this.isemriIslem = (IIsemriIslem) iislem;
		isemri = isemriIslem.getIsemri();
		ISayaclar sayaclar = isemriIslem.getSAYACLAR();

		ISayacBilgi sb;
		EndeksFilter[] fs;

		sb = sayaclar.getSayac(SayacKodu.Aktif);
		if (sb != null) {
			fs = new EndeksFilter[] { new EndeksFilter(sb.getHANE_SAYISI().getValue(), field.s_ENDEKS_PREC) };
			editGunduz.setFilters(fs);
			// editGunduz.addTextChangedListener( new TextWatcher() {
			textGunduz.setVisibility(View.VISIBLE);
			editGunduz.setVisibility(View.VISIBLE);
			if (sb.getSAYAC_CINSI().equals(SayacCinsi.Elektronik) || sb.getSAYAC_CINSI().equals(SayacCinsi.Kombi)) {
				editPuant.setFilters(fs);
				editGece.setFilters(fs);
				textPuant.setVisibility(View.VISIBLE);
				textGece.setVisibility(View.VISIBLE);
				editPuant.setVisibility(View.VISIBLE);
				editGece.setVisibility(View.VISIBLE);
				textGunduz.setText("T1");

			} else {
				textGunduz.setText("T");
			}
		}

		sb = sayaclar.getSayac(SayacKodu.Reaktif);
		if (sb != null) {
			fs = new EndeksFilter[] { new EndeksFilter(sb.getHANE_SAYISI().getValue(), field.s_ENDEKS_PREC) };
			editEnduktif.setFilters(fs);
			textEnduktif.setVisibility(View.VISIBLE);
			editEnduktif.setVisibility(View.VISIBLE);
		}
		sb = sayaclar.getSayac(SayacKodu.Kapasitif);
		if (sb != null) {
			fs = new EndeksFilter[] { new EndeksFilter(sb.getHANE_SAYISI().getValue(), field.s_ENDEKS_PREC) };
			editKapasitif.setFilters(fs);
			textKapasitif.setVisibility(View.VISIBLE);
			editKapasitif.setVisibility(View.VISIBLE);
		}
		if (isemri.getCIFT_TERIM_DUR()) {
			fs = new EndeksFilter[] {
					new EndeksFilter(field.s_DEMAND_ENDEKS - field.s_DEMAND_PREC - 1, field.s_DEMAND_PREC) };
			editDemand.setFilters(fs);
			textDemand.setVisibility(View.VISIBLE);
			editDemand.setVisibility(View.VISIBLE);
		}

		IEndeksler endeksler = islem.getENDEKSLER();
		editGunduz.setText(endeksler.getEndeks(EndeksTipi.Gunduz).toString());
		editPuant.setText(endeksler.getEndeks(EndeksTipi.Puant).toString());
		editGece.setText(endeksler.getEndeks(EndeksTipi.Gece).toString());
		editEnduktif.setText(endeksler.getEndeks(EndeksTipi.Enduktif).toString());
		editKapasitif.setText(endeksler.getEndeks(EndeksTipi.Kapasitif).toString());
		editDemand.setText(endeksler.getEndeks(EndeksTipi.Demand).toString());

		if (result != null) {
			if (Globals.isDeveloping() || sayaclar.getSayac(Integer.parseInt(result.get_sayac_no())) != null) {
				optikEndeksDoldur(result);
			}
		}

		editGunduz.requestFocus();
		*/
	}
	
	public void show(IIsemriIslem isemriIslem, ISayaclar sayaclar, IReadResult result) {

		hide();

		EndeksFilter[] fs;
		ISayacBilgi sayac;
		IEndeksler endeksler;
		IIsemri isemri = isemriIslem.getIsemri();

		sayac = sayaclar.getSayac(SayacKodu.Aktif);
		if (sayac != null) {

			endeksler = sayac.getENDEKSLER();
			fs = new EndeksFilter[] { new EndeksFilter(sayac.getHANE_SAYISI().getValue(), field.s_ENDEKS_PREC) };
			editGunduz.setFilters(fs);
			// editGunduz.addTextChangedListener( new TextWatcher() {
			textGunduz.setVisibility(View.VISIBLE);
			editGunduz.setVisibility(View.VISIBLE);
			if (!sayac.getSAYAC_CINSI().equals(SayacCinsi.Mekanik)) {
				editPuant.setFilters(fs);
				editGece.setFilters(fs);
				textPuant.setVisibility(View.VISIBLE);
				textGece.setVisibility(View.VISIBLE);
				editPuant.setVisibility(View.VISIBLE);
				editGece.setVisibility(View.VISIBLE);
				textGunduz.setText("T1");

				editGunduz.setText(endeksler.getEndeks(EndeksTipi.Gunduz).toString());
				editPuant.setText(endeksler.getEndeks(EndeksTipi.Puant).toString());
				editGece.setText(endeksler.getEndeks(EndeksTipi.Gece).toString());

			} else {
				textGunduz.setText("T");
				editGunduz.setText(endeksler.getEndeks(EndeksTipi.Gunduz).toString());
			}
		}

		sayac = sayaclar.getSayac(SayacKodu.Reaktif);
		if (sayac != null) {
			endeksler = sayac.getENDEKSLER();
			fs = new EndeksFilter[] { new EndeksFilter(sayac.getHANE_SAYISI().getValue(), field.s_ENDEKS_PREC) };
			editEnduktif.setFilters(fs);
			textEnduktif.setVisibility(View.VISIBLE);
			editEnduktif.setVisibility(View.VISIBLE);
			editEnduktif.setText(endeksler.getEndeks(EndeksTipi.Enduktif).toString());
		}

		sayac = sayaclar.getSayac(SayacKodu.Kapasitif);
		if (sayac != null) {
			endeksler = sayac.getENDEKSLER();
			fs = new EndeksFilter[] { new EndeksFilter(sayac.getHANE_SAYISI().getValue(), field.s_ENDEKS_PREC) };
			editKapasitif.setFilters(fs);
			textKapasitif.setVisibility(View.VISIBLE);
			editKapasitif.setVisibility(View.VISIBLE);
			editKapasitif.setText(endeksler.getEndeks(EndeksTipi.Kapasitif).toString());
		}
		if (isemri.getCIFT_TERIM_DUR()) {
			endeksler = sayac.getENDEKSLER();
			fs = new EndeksFilter[] {
					new EndeksFilter(field.s_DEMAND_ENDEKS - field.s_DEMAND_PREC - 1, field.s_DEMAND_PREC) };
			editDemand.setFilters(fs);
			textDemand.setVisibility(View.VISIBLE);
			editDemand.setVisibility(View.VISIBLE);
			editDemand.setText(endeksler.getEndeks(EndeksTipi.Demand).toString());
		}

		this.isemriIslem = isemriIslem;
		this.result = result;
		this.sayaclar = sayaclar;

		if (result != null) {
			if (Globals.isDeveloping()
					|| sayaclar.getSayac(Integer.parseInt(result.get_sayac_no())) != null) {
				optikEndeksDoldur(result);
			}
		}
		else {
			setReadOnly(false);
			app.showSoftKeyboard((IForm)getActivity(), true);
		}

		editGunduz.requestFocus();

	}

	private void optikEndeksDoldur(final IReadResult result) {

		try {

			setReadOnly(true);

			String gunduz = result.get_gunduz_end();
			String puant = result.get_puant_end();
			String gece = result.get_gece_end();
			String enduktif = result.get_enduktif_end();
			String kapasitif = result.get_kapasitif_end();
			String demand = result.get_demand_end();

			if (decimalSep != '.') {
				if(gunduz != null) gunduz = gunduz.replace('.', decimalSep);
				if(puant != null) puant = puant.replace('.', decimalSep);
				if(gece != null) gece = gece.replace('.', decimalSep);
				if(enduktif != null) enduktif = enduktif.replace('.', decimalSep);
				if(kapasitif != null) kapasitif = kapasitif.replace('.', decimalSep);
				if(demand != null) demand = demand.replace('.', decimalSep);

			}
			if(gunduz != null) editGunduz.setText(gunduz);
			if(puant != null) editPuant.setText(puant);
			if(gece != null) editGece.setText(gece);
			if(enduktif != null) editEnduktif.setText(enduktif);
			if(kapasitif != null) editKapasitif.setText(kapasitif);
			if(demand != null) editDemand.setText(demand);


		} catch (Exception e) {
			app.ShowException((IForm) getActivity(), e);
		}

	}

	void setReadOnly(boolean readonly) {

		boolean focusable = !readonly;

		editGunduz.setFocusableInTouchMode(focusable);
		editGunduz.setFocusable(focusable);

		editPuant.setFocusableInTouchMode(focusable);
		editPuant.setFocusable(focusable);

		editGece.setFocusableInTouchMode(focusable);
		editGece.setFocusable(focusable);

		editEnduktif.setFocusableInTouchMode(focusable);
		editEnduktif.setFocusable(focusable);

		editKapasitif.setFocusableInTouchMode(focusable);
		editKapasitif.setFocusable(focusable);

		editDemand.setFocusableInTouchMode(focusable);
		editDemand.setFocusable(focusable);

		readOnly = readonly;

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if (!hasFocus) {

			try {
				if (id == R.id.editGunduz && editGunduz.getVisibility() == View.VISIBLE) {
					sayaclar.addEndeks(EndeksTipi.Gunduz, editGunduz.getText().toString());
				} else if (id == R.id.editPuant && editPuant.getVisibility() == View.VISIBLE) {
					sayaclar.addEndeks(EndeksTipi.Puant, editPuant.getText().toString());
				} else if (id == R.id.editGece && editGece.getVisibility() == View.VISIBLE) {
					sayaclar.addEndeks(EndeksTipi.Gece, editGece.getText().toString());
				} else if (id == R.id.editEnduktif && editEnduktif.getVisibility() == View.VISIBLE) {
					sayaclar.addEndeks(EndeksTipi.Enduktif, editEnduktif.getText().toString());
				} else if (id == R.id.editKapasitif && editKapasitif.getVisibility() == View.VISIBLE) {
					sayaclar.addEndeks(EndeksTipi.Kapasitif, editKapasitif.getText().toString());
				} else if (id == R.id.editDemand && editDemand.getVisibility() == View.VISIBLE) {
					sayaclar.addEndeks(EndeksTipi.Demand, editDemand.getText().toString());
				}

			} catch (Exception e) {
				app.ShowException((IForm) getActivity(), e);
				return;
			}
		}
	}

	

}
