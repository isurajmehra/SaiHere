package com.sairajen.saihere.fragments;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sairajen.saihere.R;
import com.sairajen.saihere.helper.Funcs;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import dmax.dialog.SpotsDialog;

/**
 * @author Gmonetix
 */
public class AnswerYourQuestion extends Fragment {

    private View view;
    private LinearLayout ll1, ll2;
    private Button btnSend, btnShare, btnBack;
    private ImageView imageView;
    private TextView textView;
    private EditText editText;

    private AlertDialog progressBar;

    public AnswerYourQuestion() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_answer_your_question, container, false);

        init();

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funcs.shareLink(getActivity(),"http://www.saihere.com/sai-answer/index.php");
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    progressBar = new SpotsDialog(getActivity(),R.style.ProgressDialog);
                    progressBar.show();

                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.VISIBLE);

                    Random random = new Random();
                    int num = random.nextInt(20);
                    if (num == 0)
                        num = 14;

                    Picasso.with(getActivity()).load("http://www.saihere.com/sai-answer/uploads/"+String.valueOf(num)+".jpg").into(imageView);

                    StringRequest request = new StringRequest(Request.Method.POST, "http://www.saihere.com/sai-answer/post_random_for_app.php",
                            new Response.Listener<String>() {
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
                                        Toast.makeText(getActivity(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    } finally {
                                        progressBar.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressBar.dismiss();
                            volleyError.printStackTrace();
                            Toast.makeText(getActivity(),""+volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("data",editText.getText().toString().trim());
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    request.setShouldCache(false);
                    queue.add(request);
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll2.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
            }
        });
        
        return view;
    }

    private boolean validate() {
        boolean res = true;

        if (editText.getText().toString() == null || editText.getText().toString().equals("") || Integer.parseInt(editText.getText().toString().trim()) < 1 || Integer.parseInt(editText.getText().toString().trim()) > 720 ) {
            editText.setError("Enter value of 1-720");
            res  = false;
        } else {
            editText.setError(null);
            res = true;
        }

        return res;
    }

    private void init() {
        ll1 = (LinearLayout) view.findViewById(R.id.answer_question_ll_1);
        ll2 = (LinearLayout) view.findViewById(R.id.answer_question_ll_2);
        btnSend = (Button) view.findViewById(R.id.answer_question_send_btn);
        btnShare = (Button) view.findViewById(R.id.answer_question_share_btn);
        btnBack = (Button) view.findViewById(R.id.answer_question_back_btn);
        imageView = (ImageView) view.findViewById(R.id.answer_question_imageView);
        textView = (TextView) view.findViewById(R.id.answer_question_text);
        editText = (EditText) view.findViewById(R.id.answer_question_edit_text);
    }

}
