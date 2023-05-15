package com.mobit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//import com.sun.jndi.toolkit.url.Uri;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.graphics.ZebraImageI;
import com.zebra.sdk.graphics.internal.CompressedBitmapOutputStreamZpl;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.lang.Object;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;

import mobit.eemr.OlcuDevreForm;


public abstract class IPrinterFormat implements IForm {

    protected IApplication app = null;
    protected PageFormatInfo pfi;
    protected int xdpi;
    protected int ydpi;
    protected int xoffset;
    protected int yoffset;
    protected int pageSize;
    protected int pageWidth;
    protected int quantity;
    protected IConverter converter;
    protected Printer printerInfo;
    protected String unit;
    protected boolean blackMarker = false;

    public abstract String getModeleId();

    public static final String x10mmUnit = "x10mm";

    public void init(IApplication app, Printer printerInfo) throws Exception {
        this.app = app;
        this.printerInfo = printerInfo;
    }

    public abstract void close() throws Exception;


    //sami
    public void prepare(PageData page, PageFormatInfo pfi, IIslem islem, boolean suret) throws Exception {

        this.pfi = pfi;
        unit = pfi.getUnit();
        if (unit.equalsIgnoreCase(x10mmUnit))
            converter = new X10mmToPixel();
        else
            converter = new X10mmToPixel();

        xdpi = pfi.getxdpi();
        ydpi = pfi.getydpi();
        pageWidth = converter.toPixel(pfi.getPageWidth(), xdpi);
        pageSize = converter.toPixel(pfi.getPageSize(), ydpi);
        xoffset = converter.toPixel(pfi.getxoffset(), xdpi);
        yoffset = converter.toPixel(pfi.getyoffset(), ydpi);
        blackMarker = pfi.getBlackMark() != 0;
        quantity = pfi.getquantity();

        PageData _page = new PageData(page);
//cpcl içindeki begin printe giriyor
        begin_print(_page, pfi);

        //pfi içinde xml parçalanmış datalar var
        //Yzdırmayı burada yapabiliriz

        Collection<INode> list = pfi.getItemList();
        for (INode item : list) {
            if (item instanceof Text) {

                if (item.getText().toString().isEmpty()) {
                    PrintItem pi = _page.getPrintItem(item.Name);
                    if (pi == null) {
                        Object obj = app.getIslemData(item.Name, page, islem);
                        //veri tabanından gelen verileri alıyor sırayla
                        if (obj != null) pi = new PrintItem(item.Name, obj.toString());
                    }
                    if (pi != null) {
                        TextOut((Text) item, pi);
                        _page.removeItem(pi);
                    }
                } else {
                    TextOut((Text) item, new PrintItem("", item.getText()));
                }
            } else if (item instanceof Barcode) {
                PrintItem pi = _page.getPrintItem(item.Name);
                if (pi == null) {
                    Object obj = app.getIslemData(item.Name, page, islem);
                    if (obj != null) pi = new PrintItem(item.Name, obj.toString());
                }
                if (pi != null) {
                    DrawBarcode((Barcode) item, pi);
                    _page.removeItem(pi);
                }
            } else if (item instanceof Line)
                DrawLine((Line) item);
            else if (item instanceof Rectangle)
                DrawRectangle((Rectangle) item);
            else if (item instanceof Image) {
                Image image = (Image) item;
                if ((suret == false && image.Suret == 0) || (suret == true && image.Suret != 0))
                    DrawImage(image);
            } else if (item instanceof RawText)
                RawTextOut((RawText) item, page, islem);
            else {
                continue;
            }

        }

        if (!_page.getlist().isEmpty()) {

            StringBuilder sb = new StringBuilder();
            sb.append("Yazdırma formunda aşağıdaki alanlar tanımlanmamış:\n");
            for (PrintItem pi : _page.getlist())
                sb.append(pi.Name + "\n");
            if (false && !Globals.isDeveloping())
                throw new MobitException(sb.toString());
        }

        end_print();

    }

    public String prepareZPLFormat(PageData page, PageFormatInfo pfi, IIslem islem, boolean suret) throws Exception {

        int PageHeight = pfi.getPageHeight();//   HÜSEYİN EMRE ÇEVİK sayfa yüksekliği 02.04.2021
        int PageWidth = pfi.getPageWidth();
        int PageSize = pfi.getPageSize();
        int FontSize = pfi.getFontSize();
        String pw = Integer.toString(PageWidth);
        String ph = Integer.toString(PageHeight);
        String ps = Integer.toString(PageSize);

        String zpl = "^XA^PR5,5^CI28^MMT^POI^MNM^LH0," + ph + "^LL" + ps + "^PW" + pw + "\n" +
                //"^CI28^FX\n" +  ZEBRA YÖNETİCİSİ KAPATILMASINI ÖNERDİ 10.11.2020
                "^CF0," + FontSize + "\n";
        PageData _page = new PageData(page);
        Collection<INode> list = pfi.getItemList();

        for (INode item : list) {
            try {
                if (item instanceof Text) {
                    String Left = item.getAttribute("Left");
                    String Top = item.getAttribute("Top");
                    if (item.getAttribute("Name") == null || item.getAttribute("Name").length() == 0) {
                        //sabit xml textleri

                        OlcuDevreForm odf = new OlcuDevreForm();
                        // sayac değiştirmeden geliyor formdaki guc trafosu ve tespitinin görünümünü kapatmak için eklendi.
                        if (!item.getAttribute("Value").equals("") && odf.get_sayac_degisimi_dur() == 1) {
                            continue;
                        }
                        // formdaki guc trafosu görünümünü kapatmak için eklendi.
                        if (!item.getAttribute("Value").equals("") && odf.getguc_trafosu_dur() != 1 && item.getAttribute("Value").equals("guc_trafosu")) {
                            continue;
                        }
                        // formdaki guc tespitinin görünümünü kapatmak için eklendi.
                        if (!item.getAttribute("Value").equals("") && odf.getguc_tespiti_dur() != 1 && item.getAttribute("Value").equals("guc_tespiti")) {
                            continue;
                        }

                        String text;
                        text = item.getText();

                        int fontId = Integer.parseInt(item.getAttribute("FontId"));
                        zpl += "^CF0," + pfi.getFaturaSize(fontId) + "\n";

                        zpl += "^FO" + Left + "," + Top + "^FH^FD" + text + "^FS\n";
                    }
                    if (item.getText().toString().isEmpty()) {

                        PrintItem pi = _page.getPrintItem(item.Name);//muhur_seri
                        if (pi == null) {
                            Object obj = app.getIslemData(item.Name, page, islem);
                            //veri tabanından gelen verileri alıyor sırayla

                            if (obj != null) pi = new PrintItem(item.Name, obj.toString());
                            if (item.Name.equals("PUANT_MESAJ"))
                                //Onur puant abonelerinin saat değişikliğini fatura üzerine basmak için açıldı
                                pi = new PrintItem(item.Name, "Sayaç saati sürekli yaz saati uygulamasına göre güncellenmemiş sayaçlar üzerinden puant tarife kullanan aboneler için zaman dilimi; Mart Ayı Son Pazar Günü ile Ekim Ayı Son Pazar Günü arasında Gündüz 06-17, Puant 17-22, Gece 22-06 şeklindedir.");
                        }


                        if (pi != null) {
                            String piName = pi.Name;
                            if (piName.equalsIgnoreCase("muhur_seri")) {
                                String piStr = pi.toString();
                                if (pi.toString().contains("000000")) {
                                    Object obj = app.getIslemData(item.Name, page, islem);
                                    if (obj != null) pi = new PrintItem(item.Name, obj.toString());
                                }
                            }


                            int fontId = Integer.parseInt(item.getAttribute("FontId"));
                            zpl += "^CF0," + pfi.getFaturaSize(fontId) + "\n";
                            //HÜSEYİN EMRE ÇEVİK MESAJLARIN KESİK GELMESİ DÜZELTİLDİ ("^FB600,7,," Sayesinde 600 satır 7 sütun)
                            if (item.Name.equals("mesaj_1") || item.Name.equals("mesaj_2") || item.Name.equals("mesaj_3") || item.Name.equals("ISEMRI_MESAJ") || item.Name.equals("PUANT_MESAJ")) {
                                zpl += "^FO" + Left + "," + Top + "^FB580,8,,^FD" + pi.toString().replace("#", "") + "^FS\n";
                            }else if (item.Name.equals("adres_1")){//Onur FATURADAKİ ADRESLERİN YARIM ÇIKMASI ÇÖZÜLDÜ
                                zpl += "^FO" + Left + "," + Top + "^FB390,2,,^FD" + pi.toString().replace("#", "") + "^FS\n";
                            }else {
                                zpl += "^FO" + Left + "," + Top + "^FD" + pi.toString() + "^FS\n";
                            }
                        }
                    } else {

                    }
                } else if (item instanceof Barcode) {
                	/*
                	printer.sendCommand("^XA\n" +
                        "^FO20,20\n" +
                        "^B3N,N,100,Y\n" +
                        "^FDTEST+$$M$J-$^FS\n" +
                        "^XZ");
                	 */
                    PrintItem pi = _page.getPrintItem(item.Name);

                    if (pi == null) {
                        //zpl += "^CF0,21\n";
                        //zpl +="^B3N,N,100,Y\n";
                        zpl += "^FO" + ((Barcode) item).Left + "," + ((Barcode) item).Top + "\n^B3N,N,30,Y\n" + "^FD01386981^FS\n";
                        double top = ((Barcode) item).Top + 80;
                        zpl += "^CF0,21\n";
                        //zpl += "^FO" + ((Barcode) item).Left + "," + top + "^FD01386981^FS\n";
                    }
                    if (pi != null) {


                        zpl += "^FO" + ((Barcode) item).Left + "," + ((Barcode) item).Top + "\n^B3N,N,30,Y\n" + "^FD" + pi.toString() + "^FS\n";

                        double top = ((Barcode) item).Top + 80;
                        zpl += "^CF0,21\n";
                        //zpl += "^FO" + ((Barcode) item).Left + "," + top + "^FD" + pi.toString() + "^FS\n";
                    }
                } else if (item instanceof Line)
                    DrawLine((Line) item);
                else if (item instanceof Rectangle)
                    DrawRectangle((Rectangle) item);
                else if (item instanceof Image) {
                    if (pfi.getFormType().equals("perakende_form") || pfi.getFormType().equals("perakende_kdm_form")) {
                        //zpl += "^FO20,20^XGR:PERAKENDE_IMZA2.GRF,1,1^FS\n";
                        String TopImage = ((Image) item).getImageAttribute("Top");
                        String LeftImage = ((Image) item).getImageAttribute("Left");

                        zpl += "^FO" + LeftImage + "," + TopImage;
                        Image image = (Image) item;
                        zpl += DrawImageZpl(image.file.getAbsolutePath());

                    } else if (pfi.getFormType().equals("dagitim_form") || pfi.getFormType().equals("aysonu_form") || pfi.getFormType().equals("aysonu_kdm_form")) {
                        //İmage için left ve top değerleri eklendi 03.05.2021 HÜSEYİN EMRE ÇEVİK
                        String TopImage = ((Image) item).getImageAttribute("Top");
                        String LeftImage = ((Image) item).getImageAttribute("Left");


                        zpl += "^FO" + LeftImage + "," + TopImage;
                        Image image = (Image) item;
                        zpl += DrawImageZpl(image.file.getAbsolutePath());

                    } else {
                        Image image = (Image) item;
                        zpl += DrawImageZpl(image.file.getAbsolutePath());
                    }
                } else if (item instanceof RawText)
                    RawTextOut((RawText) item, page, islem);
                else {
                    continue;
                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        zpl += "^XZ";
        return zpl;
    }

    // H. Elif ODF yazdırma
    public String prepareZPLFormatOdf(PageData page, PageFormatInfo pfi, OlcuDevreForm odf) throws Exception {
        int PageHeight = pfi.getPageHeight();
        int PageWidth = pfi.getPageWidth();
        int PageSize = pfi.getPageSize();
        int FontSize = pfi.getFontSize();
        String pw = Integer.toString(PageWidth);
        String ph = Integer.toString(PageHeight);
        String ps = Integer.toString(PageSize);


            String zpl = "^XA^PR5,5^CI28^MMT^POI^MNM^LH0," + ph + "^LL" + ps + "^PW" + pw + "\n" +
                    "^CF0," + FontSize + "\n";
            PageData _page = new PageData(page);
            Collection<INode> list = pfi.getItemList();

            for (INode item : list) {
                try {
                    if (item instanceof Text) {
                        String Left = item.getAttribute("Left");
                        String Top = item.getAttribute("Top");
                        if (item.getAttribute("Name") == null || item.getAttribute("Name").length() == 0) {

                            // sayac değiştirmeden geliyor formdaki guc trafosu ve tespitinin görünümünü kapatmak için eklendi.
                            if (!item.getAttribute("Value").equals("") && odf.get_sayac_degisimi_dur() == 1) {
                                continue;
                            }
                            // formdaki guc trafosu görünümünü kapatmak için eklendi.
                            if (!item.getAttribute("Value").equals("") && odf.getguc_trafosu_dur() != 1 && item.getAttribute("Value").contains("guc_trafosu")) {
                                continue;
                            }
                            // formdaki guc tespitinin görünümünü kapatmak için eklendi.
                            if (!item.getAttribute("Value").equals("") && odf.getguc_tespiti_dur() != 1 && item.getAttribute("Value").contains("guc_tespiti")) {
                                continue;
                            }

                            String text;
                            text = item.getText();

                            int fontId = Integer.parseInt(item.getAttribute("FontId"));
                            zpl += "^CF0," + pfi.getFaturaSize(fontId) + "\n";

                            zpl += "^FO" + Left + "," + Top + "^FH^FD" + text + "^FS\n";
                        } else {

                            if (!item.Name.equals("ihbar_control")) {
                                PrintItem pi = _page.getPrintItem(item.Name);
                                if (pi == null) {
                                    pi = new PrintItem(item.Name, String.valueOf(odf.getClass().getField(item.Name).get(odf)));
                                }
                                if (pi != null) {
                                    zpl += "^FB575,5,,";
                                    String result_string = pi.toString();

                                    if (result_string.equals("-1"))
                                        result_string = "";

                                    if (item.Name.equals("aciklama")) {
                                        String control_text = "Kontrol Neticesinde; " + result_string;
                                        String control_str = "";
                                        result_string = "";

                                        String[] control_diz = control_text.split(" ");

                                        for (int i = 0; i < control_diz.length; i++) {
                                            control_str = control_diz[i];
                                            String cntrl = result_string + " " + control_str;

                                            if (cntrl.length() > PageWidth) {
                                                result_string += "\\&";
                                            }
                                            result_string = result_string + " " + control_str;
                                        }
                                    }

                                    if (item.Name.equals("adres")) {
                                        zpl += "^FB475,5,,";
                                    }

                                    if (item.Name.equals("guc_tespit_cins") || item.Name.equals("guc_tespit_adet") || item.Name.equals("guc_tespit_guc")) {

                                        String[] tespit_list;
                                        tespit_list = result_string.split("[\\[\\]]");
                                        result_string = tespit_list[1].replace(",", "\\&");
                                    }

                                    if (item.Name.equals("sokulen_muhur_list")) {
                                        String[] tespit_list;
                                        tespit_list = result_string.split("[\\[\\]]");
                                        result_string = tespit_list[1];
                                    }


                                    int fontId = Integer.parseInt(item.getAttribute("FontId"));
                                    zpl += "^CF0," + pfi.getFaturaSize(fontId) + "\n";
                                    zpl += "^FO" + Left + "," + Top + "^FD" + result_string + "^FS\n";

                                }
                            } else {
                                if (odf.get_ihbar_dur() == 1) {
                                    int fontId = Integer.parseInt(item.getAttribute("FontId"));
                                    zpl += "^CF0," + pfi.getFaturaSize(fontId) + "\n";
                                    zpl += "^FB575,5,,";
                                    zpl += "^FO" + Left + "," + Top + "^FD" + "Yukarıda belirtilen eksikliklerin iç tesisat yönetmeliği hükümlerine \\& uygun şekilde en geç 15 gün içerisinde tamamlanmaması halinde \\& enerjinizi kesmek zorunda kalacağımızı bildiririz. \\& Gerekli hassasiyetin gösterilmesini rica ederiz. \\& Saygılarımızla." + "^FS\n";
                                }
                            }
                        }


                    } else if (item instanceof Rectangle)
                        DrawRectangle((Rectangle) item);
                    else if (item instanceof Image) {
                        Image image = (Image) item;
                        zpl += DrawImageZpl(image.file.getAbsolutePath());
                    } else {
                        continue;
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

            zpl += "^XZ";
            return zpl;

    }


    public String DrawImageZpl(String imageFilePath) throws Exception {
        String Resim = "";
        try {
            File imgFile = new File(imageFilePath);
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFilePath);
            ZPLImageConverter zp = new ZPLImageConverter();
            Resim = zp.convertFromImage(myBitmap, false);

        } catch (Exception e) {

        }
        return Resim;
    }

    public static InputStream bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }


    public abstract byte[] getPrintStream() throws IOException;

    protected abstract void begin_print(PageData page, PageFormatInfo pfi) throws Exception;

    protected abstract void end_print() throws Exception;

    protected abstract void TextOut(Text text, PrintItem item) throws Exception;

    protected abstract void DrawLine(Line line) throws Exception;

    protected abstract void DrawRectangle(Rectangle rect) throws Exception;

    protected abstract void DrawBarcode(Barcode bc, PrintItem item) throws Exception;

    protected abstract void DrawImage(Image image) throws Exception;

    protected abstract void RawTextOut(RawText text, PageData pd, IIslem islem) throws Exception;

	/*
	public static int mm2pixel(float mm, int dpi) {
		int pixel = (int) (dpi * mm / 25.4);
		return (pixel > 0) ? pixel : 1;
	}

	public static int x10mm2pixel(float x10mm, int dpi) {
		int pixel = (int) (dpi * x10mm / 254);
		return (pixel > 0) ? pixel : 1;
	}

	public static float pixel2x10mm(int pixel, int dpi) {
		return pixel * 254 / dpi;
	}
	*/
}
