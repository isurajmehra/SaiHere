package com.sairajen.saihere.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sairajen.saihere.R;
import com.sairajen.saihere.helper.Funcs;

/**
 * @author Gmonetix
 */
public class OnlinePrayers extends Fragment {

    private View view;
    private Button shareBtn;
    private Button sendBtn;
    private LinearLayout onlinePrayerll1, onlinePrayerll2;

    public OnlinePrayers() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online_prayers, container, false);

        init();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlinePrayerll1.setVisibility(View.GONE);
                onlinePrayerll2.setVisibility(View.VISIBLE);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funcs.shareLink(getActivity(),"http://www.saihere.com/prayers");
            }
        });

        return view;
    }

    private void init() {
        shareBtn = (Button) view.findViewById(R.id.online_prayer_share_btn);
        sendBtn = (Button) view.findViewById(R.id.online_prayer_send);
        onlinePrayerll1 = (LinearLayout) view.findViewById(R.id.online_prayer_ll_1);
        onlinePrayerll2 = (LinearLayout) view.findViewById(R.id.online_prayer_ll_2);
    }

}
