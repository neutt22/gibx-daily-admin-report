package jim.gibx.gibco.guevent.com.gibxadmindailyreport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Tab2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);

        EditText corpo = (EditText) v.findViewById(R.id.txtCorpoToday);
        EditText ywc = (EditText) v.findViewById(R.id.txtYWCToday);
        EditText mlmF = (EditText) v.findViewById(R.id.txtMLMFToday);
        EditText mlmI = (EditText) v.findViewById(R.id.txtMLMIToday);

        Button b = (Button) v.findViewById(R.id.btnPush);
        b.setOnClickListener(new ButtonHandler(corpo, ywc, mlmF, mlmI));

        return v;
    }
}