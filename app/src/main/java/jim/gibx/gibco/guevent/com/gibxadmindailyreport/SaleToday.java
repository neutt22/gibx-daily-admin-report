package jim.gibx.gibco.guevent.com.gibxadmindailyreport;


import java.util.Date;

public class SaleToday {

    private String corpo, ywc, mlmF, mlmI;
    private String date;

    public SaleToday(String c, String y, String f, String i){
        corpo = c;
        ywc = y; mlmF = f; mlmI = i;
        CharSequence d = android.text.format.DateFormat.format("MM-dd-yyyy", new Date().getTime());
        date = d.toString();
    }

    public String getCorpo() { return corpo; }
    public String getYwc() { return ywc; }
    public String getMlmF() { return mlmF; }
    public String getMlmI() { return mlmI; }
    public String getDate() { return date; }

}
