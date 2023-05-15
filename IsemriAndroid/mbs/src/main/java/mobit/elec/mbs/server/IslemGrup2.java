package mobit.elec.mbs.server;

import com.mobit.IApplication;
import com.mobit.IIslemGrup;
import com.mobit.IIslemMaster;
import com.mobit.IRecordStatus;
import com.mobit.IslemGrup;
import com.mobit.IslemMaster;
import com.mobit.RecordStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mobit.elec.mbs.IslemMaster2;

import static com.mobit.IslemMaster.sSAHA_ISEMRI_NO;

/**
 * Created by Genel on 4.10.2018.
 */

public class IslemGrup2 extends IslemGrup {

    protected static final String query1 = String.format("select * from %s where %s = ? order by %s asc",
            IslemMaster.tableName, IslemMaster.sDURUM, IslemMaster.sID);

    public static List<IIslemGrup> select(IApplication app, IRecordStatus status) throws Exception {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<IIslemGrup> list = new ArrayList<IIslemGrup>();
        try {
            Connection conn = app.getConnection();
            stmt = conn.prepareStatement(query1);
            stmt.setInt(1, status.getValue());
            rs = stmt.executeQuery();
            IslemGrup grup = null;
            while (rs.next()) {
                long grupId = rs.getLong(IslemMaster.sGRUPID);
                if(grup == null || grup.getGrupId() != grupId){
                    grup = new IslemGrup();
                    grup.setGrupId(grupId);
                    list.add(grup);
                }

                IIslemMaster m = (IIslemMaster) app.newIslemMaster();
                m.get(rs);
                grup.add(m);
            }

        } finally {

            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }

        return list;
    }

    protected static final String query2 = String.format("select * from %s where %s = ? and %s = ? order by %s asc",
            IslemMaster.tableName, IslemMaster.sDURUM, sSAHA_ISEMRI_NO, IslemMaster.sID);

    public static List<IIslemGrup> select(IApplication app, IRecordStatus status, int isemriNo) throws Exception {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<IIslemGrup> list = new ArrayList<IIslemGrup>();
        try {
            Connection conn = app.getConnection();
            stmt = conn.prepareStatement(query2);
            stmt.setInt(1, status.getValue());
            stmt.setInt(2, isemriNo);
            rs = stmt.executeQuery();
            IslemGrup grup = null;
            while (rs.next()) {
                long grupId = rs.getLong(IslemMaster.sGRUPID);
                if(grup == null || grup.getGrupId() != grupId){
                    grup = new IslemGrup();
                    grup.setGrupId(grupId);
                    list.add(grup);
                }

                IIslemMaster m = (IIslemMaster) app.newIslemMaster();
                m.get(rs);
                grup.add(m);
            }

        } finally {

            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }

        return list;
    }

}
