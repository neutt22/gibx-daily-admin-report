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

        GIBXConnection todayConnection = new GIBXConnection();
        boolean hasToday = todayConnection.requestDate(d.toString(), view, "today"); //request today, 'false' being not the running.json
        if(hasToday){
            SaleToday saleToday = todayConnection.getSale();
            renderToday(saleToday);
        }

        GIBXConnection runningConnection = new GIBXConnection();
        boolean hasRunning = runningConnection.requestDate("running", view, "running"); //request running.json, set to 'true'
        if(hasRunning){
            SaleToday saleRunning = runningConnection.getSale();
            renderRunning(saleRunning);
        }

        if(hasToday && hasRunning){
            renderTotal();
        }

        //ToDo: Update the 'Last Admin Update:' label on Tab 1, so we will know how long he is not updating
        //ToDo: App is crushing when there's no right {date}.json file found lol

        lblUpdate.setText("Press the refresh button to update");
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
    private TextView txtCorpoTotal1, txtYWCTotal1, txtMLMFTotal1, txtMLMITotal1;

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

        txtCorpoTotal1 = (TextView) findViewById(R.id.txtCorpoTotal1);
        txtYWCTotal1 = (TextView) findViewById(R.id.txtYWCTotal1);
        txtMLMFTotal1 = (TextView) findViewById(R.id.txtMLMFTotal1);
        txtMLMITotal1 = (TextView) findViewById(R.id.txtMLMITotal1);
    }

    private boolean hasToday = false;
    private void renderToday(SaleToday today){
        txtCorpoToday1.setText(today.getCorpo());
        txtYWCToday1.setText(today.getYwc());
        txtMLMFToday1.setText(today.getMlmF());
        txtMLMIToday1.setText(today.getMlmI());
    }

    private void renderTotal(){
        int corpoToday = Integer.parseInt(txtCorpoToday1.getText().toString());
        int corpoRunning = Integer.parseInt(txtCorpoRunning1.getText().toString());
        int corpoTotal = corpoRunning + corpoToday;
        txtCorpoTotal1.setText(corpoTotal + "");

        int ywcToday = Integer.parseInt(txtYWCToday1.getText().toString());
        int ywcRunning = Integer.parseInt(txtYWCRunning1.getText().toString());
        int ywcTotal = ywcRunning + ywcToday;
        txtYWCTotal1.setText(ywcTotal + "");

        int mlmFToday = Integer.parseInt(txtMLMFToday1.getText().toString());
        int mlmFRunning = Integer.parseInt(txtMLMFRunning1.getText().toString());
        int mlmFTotal = mlmFRunning + mlmFToday;
        txtMLMFTotal1.setText(mlmFTotal + "");

        int mlmIToday = Integer.parseInt(txtMLMIToday1.getText().toString());
        int mlmIRunning = Integer.parseInt(txtMLMIRunning1.getText().toString());
        int mlmITotal = mlmIRunning + mlmIToday;
        txtMLMITotal1.setText(mlmITotal + "");
    }

    //formula: Running = Running - Today
    private void renderRunning(SaleToday sale){
        int corpoRunning = Integer.parseInt(sale.getCorpo()); // - Integer.parseInt(txtCorpoToday1.getText().toString());
        txtCorpoRunning1.setText(corpoRunning + "");

        int ywcRunning = Integer.parseInt(sale.getYwc()); // - Integer.parseInt(txtYWCToday1.getText().toString());
        txtYWCRunning1.setText(ywcRunning + "");

        int mlmFRunning = Integer.parseInt(sale.getMlmF()); // - Integer.parseInt(txtMLMFToday1.getText().toString());
        txtMLMFRunning1.setText(mlmFRunning + "");

        int mlmIRunning = Integer.parseInt(sale.getMlmI()); // - Integer.parseInt(txtMLMIToday1.getText().toString());
        txtMLMIRunning1.setText(mlmIRunning + "");
    }
}
