package jim.gibx.gibco.guevent.com.gibxadmindailyreport;


import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GIBXConnection {

    private SaleToday sale;

    public SaleToday getSale() { return sale; }

    public GIBXConnection(){
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public boolean requestDate(String date, View v, String msg){
        HttpURLConnection httpCon;
        String url = "http://yeswecare.16mb.com/getSale?" + date;
        //String url = "http://10.0.2.2/getSale?" + date;
        try{
            httpCon = (HttpURLConnection) ((new URL(url).openConnection()));
            httpCon.setDoOutput(false);
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            httpCon.setRequestProperty("Accept", "*/*");
            httpCon.setRequestMethod("GET");
            httpCon.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();
            Log.d("ERR: " + builder.toString(), "builder of " + msg);
            Context context = v.getContext();
            CharSequence text;
            if(builder.toString().equals("error_date")) {
                text = "Failed retrieving report. Confirm with sir Roman. This is bad!";
                Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
                t.show();
                return false;
            }

            if(builder.toString().equals("error_today")) {
                text = "Failed pushing updates. Contact Jim. This is bad!";
                Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
                t.show();
                return false;
            }

            sale = new Gson().fromJson(builder.toString(), SaleToday.class);
            text = "Successfully retrieved " + msg + " report ;)";
            Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
            t.show();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
