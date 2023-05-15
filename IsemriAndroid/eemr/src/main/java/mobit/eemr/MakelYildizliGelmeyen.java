package mobit.eemr;
import mobit.eemr.MbtMeterInformation;
public class MakelYildizliGelmeyen {
    public byte S= (byte) 83;
    public byte Y= (byte) 89;
    public byte M= (byte) 77;
    public byte BIR= (byte) 49;
    public byte SIFIR= (byte) 48;
    public byte IKI= (byte) 50;
    public byte T= (byte) 84;
    public byte D= (byte) 68;
    public byte C= (byte) 67;
    public byte BES= (byte) 53;
    public byte ALTI= (byte) 54;
    public byte DORT= (byte) 52;
    public byte SEKIZ= (byte) 56;
    public byte NOKTA = (byte) 46;

    public int checkMsyModel(byte[] buffer,int read){
        int msy_check=0;

        for (int i=0;i<read;i++){

            if (i<read-3 && msy_check ==0) {
                //MSY5
                if (buffer[i]==M && buffer[i+1]==S && buffer[i+2]==Y){
                    msy_check = 1;
                }
            }
            if (i<read-8 && msy_check ==1){
                if ((buffer[i]==BES || buffer[i]==ALTI) &&
                        (buffer[i+1]==SIFIR || buffer[i+1]==BIR || buffer[i+1]==BES || buffer[i+1]==ALTI) &&
                        (buffer[i+1]==SIFIR) &&
                        (buffer[i+2]==NOKTA) &&
                        (buffer[i+3]==IKI) &&
                        (buffer[i+4]==IKI) &&
                        (buffer[i+5]==BES) &&
                        (buffer[i+6]==BIR)){
                    return 1;
                }
            }
        }
        return 0;

    }
}
