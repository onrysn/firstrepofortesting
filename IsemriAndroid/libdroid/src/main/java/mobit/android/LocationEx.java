package mobit.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.TimeZone;
import java.util.logging.Logger;

import android.location.Location;
import com.mobit.Globals;
import com.mobit.IDetail;
import com.mobit.ILocation;
import com.mobit.utility;

import mobit.eemr.Lun_Control;

public class LocationEx extends Location implements ILocation, IDetail {

	 
	public LocationEx(Location l) {
		super(l);
		// TODO Auto-generated constructor stub
		
	}

	public LocationEx() {
		this(new Location(""));
	}

	@Override
	public float distanceTo(ILocation location) {
		// TODO Auto-generated method stub
		return super.distanceTo((Location) location);

	}

	@Override
	public String getAciklama() {
		// TODO Auto-generated method stub
		return "Konum Bilgisi";
	}
	
	@Override
	public boolean isValid()
	{
		return getLatitude() != 0;
	}

	@Override
	public List<String[]> getDetay() {
		// TODO Auto-generated method stub

		List<String[]> list = new ArrayList<String[]>();
		if (getLatitude() != 0) {

			SimpleDateFormat utc = (SimpleDateFormat)Globals.dateTimeFmt.clone();
			utc.setTimeZone(Globals.utcTimeZone);


			list.add(new String[] { "Hassasiyet:", getAccuracy() != 0 ? String.format(Globals.locale, "%.1f (0-5 iyi)", getAccuracy()) : "" });
			list.add(new String[] { "Enlem:", getLatitude() != 0 ? String.format(Globals.locale, "%.7f°", getLatitude()) : "" });
			list.add(new String[] { "Boylam:", getLongitude() != 0 ? String.format(Globals.locale, "%.7f°", getLongitude()) : "" });
			list.add(new String[] { "Yükseklik:", getAltitude() != 0 ? String.format(Globals.locale, "%.1f m", getAltitude()) : "" });
			list.add(new String[] { "Hız:", getSpeed() != 0 ? String.format(Globals.locale, "%.1f m/s", getSpeed()) : "" });
			list.add(new String[] { "Zaman:", utc.format(Globals.getTime()) });
			Date dt = new Date(getTime());
			list.add(new String[] { "Zaman(UTC):", getTime() != 0 ? utc.format(dt) : "" });
			Lun_Control Gps = new Lun_Control();
			list.add(new String[] { "#SON SAĞLIKLI ","KOORDİNAT#"});
			list.add(new String[] { "Kayıtlı Hassasiyet:",String.format(Globals.locale, "%.1f (0-5 iyi)", Gps.Accuracy)  });
			list.add(new String[] { "Kayıtlı Enlem:",String.format(Globals.locale, "%.7f", Gps.Latitude)  });
			list.add(new String[] { "Kayıtlı Boylam:",String.format(Globals.locale, "%.7f", Gps.Longitude)  });
			list.add(new String[] { "Kayıtlı Zaman(+3 saat):",Gps.Time });
		} else {
			list.add(new String[] { "", "Konum alınamadı!" });
			Lun_Control Gps = new Lun_Control();
			list.add(new String[] { "#SON SAĞLIKLI ","KOORDİNAT#" });
			list.add(new String[] { "Enlem:", String.format(Globals.locale, "%.7f°", getLatitude())});
			list.add(new String[] { "Kayıtlı Hassasiyet:",String.format(Globals.locale, "%.1f (0-5 iyi)", Gps.Accuracy)  });
			list.add(new String[] { "Kayıtlı Enlem:",String.format(Globals.locale, "%.7f", Gps.Latitude)  });
			list.add(new String[] { "Kayıtlı Boylam:",String.format(Globals.locale, "%.7f", Gps.Longitude)  });
			list.add(new String[] { "Kayıtlı Zaman(+3 saat):",Gps.Time });
		}
		
		return list;
	}

}
