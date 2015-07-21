package jim.gibx.gibco.guevent.com.gibxadmindailyreport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;


public class Tab1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_1,container,false);

        TextView lblDate = (TextView) v.findViewById(R.id.lblDate);
        CharSequence date = android.text.format.DateFormat.format("MM-dd-yyyy", new Date().getTime());
        lblDate.setText(date);
        return v;
    }
}