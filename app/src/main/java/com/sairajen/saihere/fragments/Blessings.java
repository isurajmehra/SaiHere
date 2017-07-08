package com.sairajen.saihere.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sairajen.saihere.R;
import com.sairajen.saihere.helper.Funcs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import dmax.dialog.SpotsDialog;

/**
 * @author Gmonetix
 */
public class Blessings extends Fragment {

    private View view;
    private ImageView imageView;
    private TextView textView;
    private Button btnShare;

    private AlertDialog progressBar;

    public Blessings() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blessings, container, false);

        init();

        progressBar = new SpotsDialog(getActivity(),R.style.ProgressDialog);
        progressBar.show();

        Random random = new Random();
        int num = random.nextInt(20);
        if (num == 0)
            num = 14;

        Picasso.with(getActivity()).load("http://www.saihere.com/sai-blessing/uploads/"+String.valueOf(num)+".jpg").into(imageView);

        StringRequest request = new StringRequest(Request.Method.GET, "http://www.saihere.com/sai-blessing/post_android.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONArray array = new JSONArray(s);
                            JSONObject jsonObject = array.getJSONObject(0);
                            String s1 = jsonObject.getString("message");
                            JSONObject object = new JSONObject(s1);
                            String msg = object.getString("posts_content");
                            textView.setText(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Error occurred", Toast.LENGTH_SHORT).show();
                        } finally {
                            progressBar.dismiss();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.dismiss();
                volleyError.printStackTrace();
                Toast.makeText(getActivity(),"Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setShouldCache(false);
        queue.add(request);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funcs.shareLink(getActivity(),"http://www.saihere.com/sai-blessing/index.php");
            }
        });

        return view;
    }

    private void init() {
        btnShare = (Button) view.findViewById(R.id.blessings_share);
        textView = (TextView) view.findViewById(R.id.blessings_text);
        imageView = (ImageView) view.findViewById(R.id.blessings_image);
    }

}
