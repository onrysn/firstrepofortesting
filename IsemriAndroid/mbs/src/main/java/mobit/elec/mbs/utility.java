package mobit.elec.mbs;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.mobit.FieldInfo;
import com.mobit.IEnum;
import com.mobit.MobitException;
import mobit.elec.IEndeks;
import mobit.elec.enums.EndeksTipi;
import mobit.elec.mbs.get.field;

public class utility {
	
	
	public static int type = 1;
	
	public static String [] parser(String s, int [] fields)
	{
		String [] arr;
		if(type == 1){
		
			ArrayList<String> list = new ArrayList<String>(fields.length);
			int index = 0;
			for(int i = 0; i < fields.length; i++){
				if((index + fields[i]) > s.length()) 
					break;
				list.add(s.substring(index, index += fields[i]));
			}
			if(index < s.length()){
				//int l = s.length();
				list.add(s.substring(index, s.length()));	
			}
			arr = new String[list.size()];
			list.toArray(arr);
		}
		else {
			arr = s.split("\t");
		}
		return arr;
	}
	
	public static String [] parser(String s, FieldInfo [] fields) throws MobitException
	{
		String [] arr;
		if(type == 1){
		
			ArrayList<String> list = new ArrayList<String>(fields.length);
			int index = 0;
			int boyy = s.length()-4;
			for(int i = 0; i < fields.length; i++){
				if(!fields[i].Parse) continue;
				int size = fields[i].Size;
				if((index + size) > s.length()){
					size = s.length() - index;
					// Mbs bazı kayıtlardaki son alanı dökümandaki uzunluğunda göndermiyor
					//throw new MobitException(String.format("Mbs'den gelen veride hata var.\n%s", s));
				}
				//PARSERRR
				if (fields[i].Name.equals("IMAL_YILI_1") || fields[i].Name.equals("IMAL_YILI_2") || fields[i].Name.equals("IMAL_YILI_3")
						||fields[i].Name.equals("IMAL_YILI_K1")|| fields[i].Name.equals("IMAL_YILI_K2") || fields[i].equals("IMAL_YILI_K3")){
					try {
						if (s.substring(index, index + size).split("\\s+").length > 1) {
							index += 2;
							list.add(s.substring(index, index += size).trim());
						}
						else {
							list.add(s.substring(index, index += size).trim());
						}

					}catch (Exception exxx){
						index=boyy;
						list.add(s.substring(boyy, boyy += size).trim());
					}

				}
				else {
					list.add(s.substring(index, index += size).trim());
				}


			}
			if(index < s.length()){
				//int l = s.length();
				list.set(list.size()-1, list.get(list.size()-1) + s.substring(index, s.length()));	
			}
			arr = new String[list.size()];
			list.toArray(arr);
		}
		else {
			arr = s.split("\t");
		}
		return arr;
	}
		
	public static final Locale MBS_LOCALE = Locale.ENGLISH;
	public static final NumberFormat MBS_FORMAT = NumberFormat.getInstance(MBS_LOCALE);
	
	public static final String endeksDblFmt = "%" + field.s_ENDEKS + "." + field.s_ENDEKS_PREC + "f";
	public static final String endeksStrFmt = "%" + field.s_ENDEKS + "s";
	
	public static final String demandDblFmt = "%" + field.s_DEMAND_ENDEKS + "." + field.s_DEMAND_PREC + "f";
	public static final String demandStrFmt = "%" + field.s_DEMAND_ENDEKS + "s";
	
	public static void toSerialize(StringBuilder sb, IEndeks endeks) throws Exception
	{	
		String str = "";
		if(!endeks.isEmpty()){
			IEnum type = endeks.getType();
			if(type.equals(EndeksTipi.Demand))
				str = String.format(demandDblFmt, endeks.getValue());
			else 
				str = String.format(endeksDblFmt, endeks.getValue());			
		}
		else {
			IEnum type = endeks.getType();
			if(type.equals(EndeksTipi.Demand))
				str = String.format(demandStrFmt, "");
			else 
				str = String.format(endeksStrFmt, "");
		}
		
		sb.append(str);
	}
		
}
