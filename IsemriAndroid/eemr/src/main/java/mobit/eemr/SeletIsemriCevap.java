package mobit.eemr;
import java.sql.Connection;
public class SeletIsemriCevap {
    public static Connection conn=null;
    public static int TesisatNo=0;
    public static int IsmeriNo=0;

    public static Connection getdbConn(){ return conn;}
    public static int getTesisatNo(){return TesisatNo;}
    public static int getIsmeriNo() {return IsmeriNo;}

}
