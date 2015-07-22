package jim.gibx.gibco.guevent.com.gibxadmindailyreport;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Report","Admin"};
    int Numboftabs =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.DeepSkyBlue);
            }
        });
        tabs.setViewPager(pager);
    }

    private boolean defined = false;

    public void refreshClicked(View view){
        TextView lblUpdate = (TextView) findViewById(R.id.lblUpdate);
        lblUpdate.setText("Updating, please wait...");
        CharSequence d = android.text.format.DateFormat.format("MM-dd-yyyy", new Date().getTime());
        setTab1Views();
        requestDate(d.toString(), view, false); //request today, 'false' being not the running.json
        requestDate("running", view, true);     //request running.json, set to 'true'
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private TextView txtCorpoToday1, txtYWCToday1, txtMLMFToday1, txtMLMIToday1;
    private TextView txtCorpoRunning1, txtYWCRunning1, txtMLMFRunning1, txtMLMIRunning1;

    private void setTab1Views(){
        if(defined) return;
        defined = true;
        txtCorpoToday1 = (TextView) findViewById(R.id.txtCorpoToday1);
        txtYWCToday1 = (TextView) findViewById(R.id.txtYWCToday1);
        txtMLMFToday1 = (TextView) findViewById(R.id.txtMLMFToday1);
        txtMLMIToday1 = (TextView) findViewById(R.id.txtMLMIToday1);

        txtCorpoRunning1 = (TextView) findViewById(R.id.txtCorpoRunning1);
        txtYWCRunning1 = (TextView) findViewById(R.id.txtYWCRunning1);
        txtMLMFRunning1 = (TextView) findViewById(R.id.txtMLMFRunning1);
        txtMLMIRunning1 = (TextView) findViewById(R.id.txtMLMIRunning1);
    }

    private void requestDate(String date, View v, boolean running){
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpURLConnection httpCon;
        //String url = "http://yeswecare.16mb.com/upload/";
        String url = "http://10.0.2.2/getSale?date=" + date;
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
            CharSequence text;
            if(builder.toString().equals("error_date")){
                text = "Failed. Sir Roman might not yet updated today. This is bad!";
                Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
                t.show();
                return;
            }

            SaleToday sale = new Gson().fromJson(builder.toString(), SaleToday.class);
            text = "Successfully retrieved report ;)";
            Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
            t.show();

            if(!running) {
                txtCorpoToday1.setText(sale.getCorpo());
                txtYWCToday1.setText(sale.getYwc());
                txtMLMFToday1.setText(sale.getMlmF());
                txtMLMIToday1.setText(sale.getMlmI());
            }else{
                txtCorpoRunning1.setText(sale.getCorpo());
                txtYWCRunning1.setText(sale.getYwc());
                txtMLMFRunning1.setText(sale.getMlmF());
                txtMLMIRunning1.setText(sale.getMlmI());
            }

            TextView lblUpdate = (TextView) findViewById(R.id.lblUpdate);
            lblUpdate.setText("Press the refresh button to update");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
