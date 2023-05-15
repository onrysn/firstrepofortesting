package mobit.eemr;

public class YkpOkuma {
    //genel
    public static int YkpOkumaDurum=0;
    public static int YkpCount=1;
    public static byte[] YkpBuffer=null;

    //VIKO 96.1.8.0
    public static byte[] YkpVEMMBuffer=null;
    //VIKO 96.A.8.0
    public static byte[] YkpVEMCBuffer=null;
    //VIKO P.01
    public static byte[] YkpM005Buffer=null;
    //AEL
    public static byte[][] YkpAELBuffer=null;
    public static int ObisCount=2047;
    //GENEL
    public static String SayacMarka=null;
    public static String TarihAralık=null;
    public static String Periyot=null;
    public static String YkpResult=null;
    public static int IsemriNo=0;
    public static int TesisatNo=0;

    public String getYkpResult(){return YkpResult;}

    public void setYkpOkumaDurum(int ykpOkumaDurum){YkpOkumaDurum=ykpOkumaDurum;}
    public void setYkpBuffer(byte[] ykpBuffer){YkpBuffer=ykpBuffer;}
    public void setSayacMarka(String sayacMarka){SayacMarka=sayacMarka;}
    public void setTarihAralık(String tarihAralık){TarihAralık=tarihAralık;}
    public void setPeriyot(String periyot){Periyot=periyot;}
    public void setYkpCount(int ykpCount){YkpCount=ykpCount;}
    public void setYkpResult(String ykpResult){YkpResult=ykpResult;}
    public void setIsemriNo(int isemriNo){IsemriNo=isemriNo;}
    public void setTesisatNo(int tesisatNo){TesisatNo=tesisatNo;}
    public void setYkpVEMMBuffer(byte[] ykpVEMMBuffer){YkpVEMMBuffer=ykpVEMMBuffer;}
    public void setYkpVEMCBuffer(byte[] ykpVEMCBuffer){YkpVEMCBuffer=ykpVEMCBuffer;}
    public void setYkpM005Buffer(byte[] ykpM005Buffer){YkpM005Buffer=ykpM005Buffer;}
    public void setYkpAELBuffer(byte[][] ykpAELBuffer){YkpAELBuffer=ykpAELBuffer;}
    public void setObisCount(int obisCount){
        if (ObisCount<=1000)
        {
            ObisCount=obisCount;
        }
        else {
            ObisCount = 1000;
        }
    }
    //Model Alfabesi
    public byte V= (byte) 86;
    public byte E= (byte) 69;
    public byte M= (byte) 77;
    public byte BIR= (byte) 49;
    public byte SIFIR= (byte) 48;
    public byte IKI= (byte) 50;
    public byte T= (byte) 84;
    public byte D= (byte) 68;
    public byte C= (byte) 67;
    public byte BES= (byte) 53;
    public byte DORT= (byte) 52;
    public byte SEKIZ= (byte) 56;
    public void checkVIKOModel(byte[] buffer,int read){
        for (int i=0;i<read;i++){

            if (i<read-3) {
                //1.GRUP M GRUBU
                //VEMM
                if (buffer[i] == V && buffer[i + 1] == E && buffer[i + 2] == M && buffer[i + 3] == M) {
                    setYkpBuffer(YkpVEMMBuffer);
                }
                //M1E0
                if (buffer[i] == M && buffer[i + 1] == BIR && buffer[i + 2] == E && buffer[i + 3] == SIFIR) {
                    setYkpBuffer(YkpVEMMBuffer);
                }
                //M2E2
                if (buffer[i] == M && buffer[i + 1] == IKI && buffer[i + 2] == E && buffer[i + 3] == IKI) {
                    setYkpBuffer(YkpVEMMBuffer);
                }
                //M1M2
                if (buffer[i] == M && buffer[i + 1] == BIR && buffer[i + 2] == M && buffer[i + 3] == IKI) {
                    setYkpBuffer(YkpVEMMBuffer);
                }
                //VEMT
                if (buffer[i] == V && buffer[i + 1] == E && buffer[i + 2] == M && buffer[i + 3] == T) {
                    setYkpBuffer(YkpVEMMBuffer);
                }
                //T1E0
                if (buffer[i] == T && buffer[i + 1] == BIR && buffer[i + 2] == E && buffer[i + 3] == SIFIR) {
                    setYkpBuffer(YkpVEMMBuffer);
                }
                //T2E2
                if (buffer[i] == T && buffer[i + 1] == IKI && buffer[i + 2] == E && buffer[i + 3] == IKI) {
                    setYkpBuffer(YkpVEMMBuffer);
                }
                //T1M2
                if (buffer[i] == T && buffer[i + 1] == BIR && buffer[i + 2] == M && buffer[i + 3] == IKI) {
                    setYkpBuffer(YkpVEMMBuffer);
                }
                //2.GRUP C-D GRUBU
                //VEMD
                if (buffer[i] == V && buffer[i + 1] == E && buffer[i + 2] == M && buffer[i + 3] == D) {
                    setYkpBuffer(YkpVEMCBuffer);
                }
                //D1E0
                if (buffer[i] == D && buffer[i + 1] == BIR && buffer[i + 2] == E && buffer[i + 3] == SIFIR) {
                    setYkpBuffer(YkpVEMCBuffer);
                }
                //D2E2
                if (buffer[i] == D && buffer[i + 1] == IKI && buffer[i + 2] == E && buffer[i + 3] == IKI) {
                    setYkpBuffer(YkpVEMCBuffer);
                }
                //D1M1
                if (buffer[i] == D && buffer[i + 1] == BIR && buffer[i + 2] == M && buffer[i + 3] == BIR) {
                    setYkpBuffer(YkpVEMCBuffer);
                }
                //VEMC
                if (buffer[i] == V && buffer[i + 1] == E && buffer[i + 2] == M && buffer[i + 3] == C) {
                    setYkpBuffer(YkpVEMCBuffer);
                }
                //C1E0
                if (buffer[i] == C && buffer[i + 1] == BIR && buffer[i + 2] == E && buffer[i + 3] == SIFIR) {
                    setYkpBuffer(YkpVEMCBuffer);
                }
                //C1E2
                if (buffer[i] == C && buffer[i + 1] == BIR && buffer[i + 2] == E && buffer[i + 3] == IKI) {
                    setYkpBuffer(YkpVEMCBuffer);
                }
                //C1M1
                if (buffer[i] == C && buffer[i + 1] == BIR && buffer[i + 2] == M && buffer[i + 3] == BIR) {
                    setYkpBuffer(YkpVEMCBuffer);
                }
                //C1M2
                if (buffer[i] == C && buffer[i + 1] == BIR && buffer[i + 2] == M && buffer[i + 3] == IKI) {
                    setYkpBuffer(YkpVEMCBuffer);
                }
                //3.GRUP M005 GRUBU
                //M005
                if (buffer[i] == M && buffer[i + 1] == SIFIR && buffer[i + 2] == SIFIR && buffer[i + 3] == BES) {
                    setYkpBuffer(YkpM005Buffer);
                }
                //T104
                if (buffer[i] == T && buffer[i + 1] == BIR && buffer[i + 2] == SIFIR && buffer[i + 3] == DORT) {
                    setYkpBuffer(YkpM005Buffer);
                }
                //V108
                if (buffer[i] == V && buffer[i + 1] == BIR && buffer[i + 2] == SIFIR && buffer[i + 3] == SEKIZ) {
                    setYkpBuffer(YkpM005Buffer);
                }
            }
        }

    }
}
