package mobit.elec.mbs.medas;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

//Muhammed Gökkaya 30.01.2020
public class SoruSenaryo {
    public static String Senaryo;
    public void SetSoruSenaryosu(String senaryo){
        Senaryo=senaryo;

    }
    public JsonArray GetSoruSenaryosu(){
        String[] Parse,Data;
        Parse=Senaryo.split("\\*");
        JsonArray jsA = new JsonArray();
        for (int i=0;i<Parse.length;i++)
        {
            Data=Parse[i].split("#");
            JsonObject jsO = new JsonObject();

            jsO.addProperty("emir_turu",Data[0]);
            jsO.addProperty("alt_emir_turu",Data[1]);
            jsO.addProperty("soru_no",Data[2]);
            if (Data.length>3)
            {
                jsO.addProperty("kosul_soru",Data[3]);
            }
            if (Data.length>4){
                jsO.addProperty("kosul_cevap",Data[4]);
            }
            if(Data.length>5)
            {
                jsO.addProperty("cevap",Data[5]);
            }

            jsA.add(jsO);

        }
        return  jsA;
    }
    public JsonArray GetSoruSenaryosuDetay(String emir_turu,String alt_emir_turu){
        String[] Parse,Data;
        Parse=Senaryo.split("\\*");
        JsonArray jsA = new JsonArray();
        for (int i=1;i<Parse.length;i++)
        {

            Data=Parse[i].split("#");
            JsonObject jsO = new JsonObject();
            if(emir_turu.equals(Data[0]) && alt_emir_turu.equals(Data[1]))
            {
                jsO.addProperty("emir_turu",Data[0]);
                jsO.addProperty("alt_emir_turu",Data[1]);
                //String SoruNo=String.valueOf(Integer.parseInt(Data[2])+1);
                jsO.addProperty("soru_no",Integer.parseInt(Data[2]));
                if (Data.length>3)
                {
                    //String KosulSoru=String.valueOf(Integer.parseInt(Data[3])+1);
                    jsO.addProperty("kosul_soru",Integer.parseInt(Data[3]));
                }
                else{
                    jsO.addProperty("kosul_soru",-1);
                }
                if (Data.length>4){
                    jsO.addProperty("kosul_cevap",Data[4]);
                }
                else{
                    jsO.addProperty("kosul_cevap",-1);
                }
                if(Data.length>5)
                {
                    jsO.addProperty("cevap",Data[5]);
                }
                else {
                    jsO.addProperty("cevap",-1);
                }

                jsA.add(jsO);
            }



        }
        return  jsA;
    }
    public boolean CheckSoruEmirTuru(String emir_turu,String alt_emir_turu){
        String[] Parse,Data;
        Parse=Senaryo.split("\\*");
        boolean control=false;
        for (int i=0;i<Parse.length;i++)
        {

            Data=Parse[i].split("#");
            if(emir_turu.equals(Data[0]) && alt_emir_turu.equals(Data[1]))
            {
                control=true;
            }
        }
        return  control;
    }
    public String[] GetSoruSatır(int id){
        String[] Parse;
        Parse=Senaryo.split("\\*");
        return  Parse[id].split("#");
    }
    public JsonArray GetSoruDetay(int SoruNo,String emir_turu,String alt_emir_turu) {
        String[] Parse, Data;
        Parse = Senaryo.split("\\*");
        JsonArray jsA = new JsonArray();
        for (int i = 1; i < Parse.length; i++) {

            Data = Parse[i].split("#");
            JsonObject jsO = new JsonObject();
            if (SoruNo == Integer.parseInt(Data[2]) && emir_turu.equals(Data[0]) && alt_emir_turu.equals(Data[1])) {
                jsO.addProperty("emir_turu", Data[0]);
                jsO.addProperty("alt_emir_turu", Data[1]);
                //String SoruNo=String.valueOf(Integer.parseInt(Data[2])+1);
                jsO.addProperty("soru_no", Integer.parseInt(Data[2]));
                if (Data.length > 3) {
                    //String KosulSoru=String.valueOf(Integer.parseInt(Data[3])+1);
                    jsO.addProperty("kosul_soru", Integer.parseInt(Data[3]));
                } else {
                    jsO.addProperty("kosul_soru", -1);
                }
                if (Data.length > 4) {
                    jsO.addProperty("kosul_cevap", Data[4]);
                } else {
                    jsO.addProperty("kosul_cevap", -1);
                }
                if (Data.length > 5) {
                    jsO.addProperty("cevap", Data[5]);
                } else {
                    jsO.addProperty("cevap", -1);
                }

                jsA.add(jsO);
            }
        }
        return  jsA;
    }
    public  JsonObject GetKosulDetay(int KosulSoru){
        String[] Parse, Data;
        Parse = Senaryo.split("\\*");
        JsonArray jsA = new JsonArray();
        for (int i = 1; i < Parse.length; i++) {

            Data = Parse[i].split("#");
            JsonObject jsO = new JsonObject();
            if (KosulSoru == Integer.parseInt(Data[3]) ) {
                jsO.addProperty("emir_turu", Data[0]);
                jsO.addProperty("alt_emir_turu", Data[1]);
                //String SoruNo=String.valueOf(Integer.parseInt(Data[2])+1);
                jsO.addProperty("soru_no", Integer.parseInt(Data[2]) );
                if (Data.length > 3) {
                    //String KosulSoru=String.valueOf(Integer.parseInt(Data[3])+1);
                    jsO.addProperty("kosul_soru", Integer.parseInt(Data[3]));
                } else {
                    jsO.addProperty("kosul_soru", -1);
                }
                if (Data.length > 4) {
                    jsO.addProperty("kosul_cevap", Data[4]);
                } else {
                    jsO.addProperty("kosul_cevap", -1);
                }
                if (Data.length > 5) {
                    jsO.addProperty("cevap", Data[5]);
                } else {
                    jsO.addProperty("cevap", -1);
                }

                return  (jsO);
            }
        }
        return  new JsonObject();
    }
    public int GetSoruCount(int soru_count,String emir_turu,String alt_emir_turu,int KosulSoruNo,String CevapNo){
        String[] Parse, Data;
        Parse = Senaryo.split("\\*");
        JsonArray jsA = new JsonArray();
        int cevap_check=0;
        for (int i = soru_count; i < Parse.length; i++) {

            Data = Parse[i].split("#");
            if (Data.length>=5) {
                if (emir_turu.equals(Data[0]) && alt_emir_turu.equals(Data[1]) && Data[3].equals(String.valueOf(KosulSoruNo)) && Data[4].equals(CevapNo)) {
                    if (Data.length>5) {
                        if (Data[5].split(";").length>1) {
                            soru_count = Integer.valueOf(Data[2]);
                            cevap_check=1;
                        }
                        if (emir_turu.equals("6") && alt_emir_turu.equals("24") && soru_count==30) soru_count++;

                    }

                }
            }

        }
        if (cevap_check==0) {
            for (int i = soru_count; i < Parse.length; i++) {

                Data = Parse[i].split("#");
                if (emir_turu.equals(Data[0]) && alt_emir_turu.equals(Data[1]) ) {
                    if (Data.length<4){
                        soru_count=Integer.valueOf(Data[2]);
                    }

                }

            }
        }
        return soru_count;
    }

}
