package mobit.elec.mbs;

import com.mobit.Globals;

import com.mobit.IRecordStatus;
import com.mobit.IslemMaster;
import com.mobit.MobitRuntimeException;
import com.mobit.RecordStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mobit.elec.IIsemri;
import mobit.elec.IOkumaRapor;
import mobit.elec.enums.IslemTipi;
import mobit.elec.mbs.get.field;
import mobit.elec.mbs.get.isemri_guncelle;

/**
 * Created by Genel on 5.12.2018.
 */

class OkumaRaporu implements IOkumaRapor {

    private List<IIsemri> okunanlar = new ArrayList<>();
    private List<IIsemri> okunmayanlar = new ArrayList<>();
    private List<IIsemri> kuyruktaOlanlar = new ArrayList<>();
    private List<IIsemri> uyariliOkunanlar = new ArrayList<>();
    private List<IIsemri> hataliOkunanlar = new ArrayList<>();

    @Override
    public List<IIsemri> getOkunanlar()
    {
        return okunanlar;
    }
    @Override
    public List<IIsemri> getOkunmayanlar()
    {
        return okunmayanlar;
    }
    @Override
    public List<IIsemri> getKuyruktaOlanlar()
    {
        return kuyruktaOlanlar;
    }
    @Override
    public List<IIsemri> getUyariliOkunanlar()
    {
        return uyariliOkunanlar;
    }
    @Override
    public List<IIsemri> getHataliOkunanlar()
    {
        return hataliOkunanlar;
    }

    public OkumaRaporu(Connection conn, int karne_no) throws Exception {

        List<Item> list = okumaRaporu(conn, karne_no);
        if(list.size() == 0) return;

        int i = 0;
        Item cur = list.get(i++);
        List<Item> list1 = new ArrayList<>();
        list1.add(cur);

        while(true) {

            boolean add = false;
            while(i < list.size()) {
                Item item = list.get(i++);
                if (cur.isemri.getSAHA_ISEMRI_NO() != item.isemri.getSAHA_ISEMRI_NO()){
                    cur = item;
                    add = true;
                    break;
                }
                list1.add(item);
            }
            decision(list1);
            list1.clear();
            if(add) list1.add(cur);

            if(i == list.size()){
                if(list1.size() > 0) decision(list1);
                break;
            }
        }
    }

    private void decision(List<Item> list){

        if(list.size() == 0) return;
        int i = 0;
        Item cur = list.get(i);
        do {
            Item item = list.get(i++);
            if(compare(cur, item)) cur = item;

        }while(i < list.size());

        add(cur);
    }

    private boolean compare(Item cur, Item item) {

        IRecordStatus itemDurum = item.islem.getDurum();
        IRecordStatus curDurum = cur.islem.getDurum();

        // Hiç okunmamış
        if(itemDurum == RecordStatus.None) {
            return (curDurum == RecordStatus.None);
        }
        // Kuyrukta bekliyor
        else if(itemDurum == RecordStatus.Saved){
            // curDurum gönderilmemiş ise değiştir
            return (curDurum != RecordStatus.Sent);
        }
        // Gönderilmiş
        else if(itemDurum == RecordStatus.Sent){
            // curDurum gönderilmemiş ise değiştirme
            if(curDurum != RecordStatus.Sent) return false;
        }

        String itemType = item.islem.getRESULT_TYPE();
        String curType = cur.islem.getRESULT_TYPE();

        if(itemType == null) return (curType == null);
        if(curType == null) return (itemType != null);

        if(IDef.OK.compareToIgnoreCase(itemType) == 0 || IDef.PRN.compareToIgnoreCase(itemType) == 0)
            return true;
        if(IDef.OK.compareToIgnoreCase(curType) == 0 || IDef.PRN.compareToIgnoreCase(curType) == 0)
            return false;
        if(IDef.WRN.compareToIgnoreCase(itemType) == 0)
            return true;
        if(IDef.WRN.compareToIgnoreCase(curType) == 0)
            return false;
        if(IDef.ERR.compareToIgnoreCase(itemType) == 0 || IDef.FTL.compareToIgnoreCase(itemType) == 0)
            return true;
        if(IDef.ERR.compareToIgnoreCase(curType) == 0 || IDef.FTL.compareToIgnoreCase(curType) == 0)
            return false;

        return true;

    }

    private void add(Item item) {

        IRecordStatus itemDurum = item.islem.getDurum();
        if(itemDurum == RecordStatus.None) {
            okunmayanlar.add(item.isemri);
            return;
        }
        else if(itemDurum == RecordStatus.Saved) {
            kuyruktaOlanlar.add(item.isemri);
            return;
        }
        String itemType = item.islem.getRESULT_TYPE();
        if(IDef.OK.compareToIgnoreCase(itemType) == 0 || IDef.PRN.compareToIgnoreCase(itemType) == 0) {
            okunanlar.add(item.isemri);
            return;
        }
        else if(IDef.WRN.compareToIgnoreCase(itemType) == 0) {
          uyariliOkunanlar.add(item.isemri);
          return;
        }
        else if(IDef.ERR.compareToIgnoreCase(itemType) == 0 || IDef.FTL.compareToIgnoreCase(itemType) == 0) {
            hataliOkunanlar.add(item.isemri);
            return;
        }

        throw new MobitRuntimeException("OkumaRaporu hatalı durum!");

    }

    // Dikkat! Aynı alan adına sahip tablolarda ikinci sıradaki tablonun alan değerini dikkate alıyor
    // Bu nedenle önce ii.* sonra i.* alındı. ii.SAHA_ISEMRI_NO boş gelebiliyor.
    private static final String query = String.format("select ii.*, i.* from %s i left join %s ii on i.%s = ii.%s and i.%s = ii.%s where %s = ? and i.%s = ? order by sira_no, sira_ek",
            isemri_guncelle.tableName, IslemMaster.tableName, field.SAHA_ISEMRI_NO, field.SAHA_ISEMRI_NO, field.ISLEM_TIPI, field.ISLEM_TIPI,
            field.KARNE_NO, field.ISLEM_TIPI);

    private static class Item {
        public IIsemri isemri;
        public IIslemMaster islem;

        public Item(IIsemri isemri, IIslemMaster islem){
            this.isemri = isemri;
            this.islem = islem;
        }
    }
    public static List<Item> okumaRaporu(Connection conn, int karne_no) throws Exception {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Item> list = new ArrayList<Item>();

        try {

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, karne_no);
            stmt.setInt(2, IslemTipi.SayacOkuma.getValue());
            rs = stmt.executeQuery();
            while (rs.next()) {
                isemri_guncelle t = new isemri_guncelle();
                IIslemMaster m = new IslemMaster2(Globals.app);
                t.get(rs);
                m.get(rs);
                list.add(new Item(t, m));
            }

        } catch (Exception e) {

            throw e;

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {

            }
        }
        return list;
    }
}
