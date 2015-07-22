package jim.gibx.gibco.guevent.com.gibxadmindailyreport;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ButtonHandler implements View.OnClickListener {

    private EditText txtCorpoToday, txtYWCToday, txtMLMFToday, txtMLMIToday;

    public ButtonHandler(EditText corpo, EditText ywc, EditText mlmF, EditText mlmI){
        txtCorpoToday = corpo;
        txtYWCToday = ywc;
        txtMLMFToday = mlmF;
        txtMLMIToday = mlmI;
    }

    @Override
    public void onClick(View v){
        SaleToday today = new SaleToday(txtCorpoToday.getText().toString(),
                txtYWCToday.getText().toString(),
                txtMLMFToday.getText().toString(),
                txtMLMIToday.getText().toString() );
        Gson gson = new Gson();
        sendJson(gson.toJson(today));
        Log.i("JSON: " + gson.toJson(today), "onclick");
    }

    private void sendJson(String json){
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpURLConnection httpCon;
        String url = "http://yeswecare.16mb.com/upload/";
        try{
            httpCon = (HttpURLConnection) ((new URL(url).openConnection()));
           // httpCon.setDoOutput(true);
            //httpCon.setRequestProperty("Content-Type", "application/json");
           // httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            httpCon.setRequestProperty("Accept","*/*");
            httpCon.setRequestMethod("GET");
            httpCon.connect();

            OutputStream os = httpCon.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write("today=" + json);
            bw.close();

            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
            String line = null;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();
            Log.i("RESULT: " + builder.toString(), "res");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
