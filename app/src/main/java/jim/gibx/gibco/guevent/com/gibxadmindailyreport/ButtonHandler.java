package jim.gibx.gibco.guevent.com.gibxadmindailyreport;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        sendJson(gson.toJson(today), v);
        Log.i("JSON: " + gson.toJson(today), "onclick");
        //ToDo: recompute running.json, add today's data.
    }

    private void sendJson(String json, View v){
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpURLConnection httpCon;
        String url = "http://yeswecare.16mb.com/addSale?today=" + json;
        //String url = "http://10.0.2.2/addSale?today=" + json;
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
            String line = null;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();

            Context context = v.getContext();
            CharSequence text = "Nice. Sales Report pushed to server! ;)";
            Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
            t.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
