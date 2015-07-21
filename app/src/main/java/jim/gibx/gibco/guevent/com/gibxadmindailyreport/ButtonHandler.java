package jim.gibx.gibco.guevent.com.gibxadmindailyreport;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

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

        Log.i("JSON: " + gson.toJson(today), "onclick");
    }

}
