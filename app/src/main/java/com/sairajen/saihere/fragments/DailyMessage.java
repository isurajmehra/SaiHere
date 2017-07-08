package com.sairajen.saihere.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sairajen.saihere.R;
import com.sairajen.saihere.helper.Funcs;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Gmonetix
 */
public class DailyMessage extends Fragment {

    private View view;
    private TextView tvDate;
    private ImageView imageView;
    private Button share;
    private RelativeLayout cApps;

    public DailyMessage() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daily_message, container, false);
        init();

        Calendar c = Calendar.getInstance();
        String month = new SimpleDateFormat("MMM").format(c.getTime());
        String weekDay = new SimpleDateFormat("EEEE", Locale.US).format(c.getTime());
        int year = c.get(Calendar.YEAR);
        int monthi = c.get(Calendar.MONTH)+1;
        int dayi = c.get(Calendar.DAY_OF_MONTH);
        String monthS = String.valueOf(monthi);
        String dayS = String.valueOf(dayi);

        if (monthS.length() == 1){
            monthS = "0"+monthS;
        }
        if (dayS.length() == 1){
            dayS = "0"+dayS;
        }

        tvDate.setText(dayS+"-"+month+"-"+String.valueOf(year)+" "+weekDay);

        final String url = "http://www.saihere.com/sai-daily-message/uploads/today/m"+monthS+"/"+dayS+".jpg";

        Picasso.with(getActivity()).load(url).into(imageView);

        final String finalMonthS = monthS;
        final String finalDayS = dayS;
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funcs.shareLink(getActivity(),"http://www.saihere.com/sai-daily-message/"+ finalMonthS +"/"+ finalDayS);
            }
        });

        cApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funcs.openLink(getActivity(),"http://www.saihere.com/more-app.html");
            }
        });

        return view;
    }

    private void init() {
        cApps = (RelativeLayout) view.findViewById(R.id.cAppsDailyMessage);
        tvDate = (TextView) view.findViewById(R.id.daily_message_date);
        imageView = (ImageView) view.findViewById(R.id.daily_message_image);
        share = (Button) view.findViewById(R.id.daily_message_share);
    }

}
