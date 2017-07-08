package com.sairajen.saihere.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sairajen.saihere.R;
import com.sairajen.saihere.connection.API;
import com.sairajen.saihere.connection.RestAdapter;
import com.sairajen.saihere.connection.callbacks.CallbackDetailsPost;
import com.sairajen.saihere.helper.Const;
import com.sairajen.saihere.helper.Funcs;
import com.sairajen.saihere.helper.NetworkCheck;
import com.sairajen.saihere.helper.SharedPref;
import com.sairajen.saihere.helper.Tools;
import com.sairajen.saihere.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimplePostDetails extends AppCompatActivity {

    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";
    public static final String EXTRA_NOTIF = "key.EXTRA_NOTIF";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionView, Post obj) {
        Intent intent = new Intent(activity, SimplePostDetails.class);
        intent.putExtra(EXTRA_OBJC, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionView, EXTRA_OBJC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Toolbar toolbar;
    private View parent_view;
    private Button share;
    // extra obj
    private Post post;
    private boolean from_notif;

    private AdView adView;

    private SharedPref sharedPref;
    private boolean flag_read_later;
    private Call<CallbackDetailsPost> callbackCall = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_post_details);

        parent_view = findViewById(android.R.id.content);
        share = (Button) findViewById(R.id.share_stories);
        sharedPref = new SharedPref(this);

        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.banner_ad_unit_id));
        adView = (AdView) findViewById(R.id.adSimplePost);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // get extra object
        post = (Post) getIntent().getSerializableExtra(EXTRA_OBJC);
        from_notif = getIntent().getBooleanExtra(EXTRA_NOTIF, false);
        initToolbar();

        displayPostData();

        if (post.isDraft()) requestAction();

        // get enabled controllers
        Tools.requestInfoApi(this);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funcs.shareLink(SimplePostDetails.this,post.url);
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SimplePostDetails.this.setTitle("www.saihere.com");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SimplePostDetails.this.finish();
    }

    private void requestDetailsPostApi() {
        API api = RestAdapter.createAPI();
        callbackCall = api.getPostDetialsById(post.id);
        callbackCall.enqueue(new Callback<CallbackDetailsPost>() {
            @Override
            public void onResponse(Call<CallbackDetailsPost> call, Response<CallbackDetailsPost> response) {
                CallbackDetailsPost resp = response.body();
                if (resp != null && resp.status.equals("ok")) {
                    post = resp.post;
                    displayPostData();
                } else {
                    onFailRequest();
                }
            }

            @Override
            public void onFailure(Call<CallbackDetailsPost> call, Throwable t) {
                if (!call.isCanceled()) onFailRequest();
            }

        });
    }

    private void requestAction() {
        showFailedView(false, "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestDetailsPostApi();
            }
        }, Const.DELAY_TIME_MEDIUM);
    }

    private void onFailRequest() {
        if (NetworkCheck.isConnect(this)) {
            showFailedView(true, getString(R.string.failed_text));
        } else {
            showFailedView(true, getString(R.string.no_internet_text));
        }
    }

    private void displayPostData() {

        ((TextView)findViewById(R.id.title)).setText(Html.fromHtml(post.title));

        WebView webview = (WebView) findViewById(R.id.content);
        String html_data = "<style>img{max-width:100%;height:auto;} iframe{width:100%;}</style> ";
        html_data += post.content;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings();
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setBackgroundColor(Color.TRANSPARENT);
        webview.setWebViewClient(new Browser());
        webview.setWebChromeClient(new MyWebClient());
  //      webview.setWebChromeClient(new WebChromeClient());
        webview.loadData(html_data, "text/html; charset=UTF-8", null);
        // disable scroll on touch
        webview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        ((TextView) findViewById(R.id.date)).setText(Tools.getFormatedDate(post.date));
        ((TextView) findViewById(R.id.author)).setText(Html.fromHtml(post.author.name.toUpperCase()));

    }

    private void showFailedView(boolean show, String message) {
        View lyt_failed = (View) findViewById(R.id.lyt_failed);
        View lyt_main_content = (View) findViewById(R.id.lyt_main_content);

        ((TextView) findViewById(R.id.failed_message)).setText(message);
        if (show) {
            lyt_main_content.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            lyt_main_content.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        ((Button) findViewById(R.id.failed_retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class Browser extends WebViewClient
    {
        Browser() {}

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
        {
            paramWebView.loadUrl(paramString);
            return true;
        }
    }

    public class MyWebClient
            extends WebChromeClient
    {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public MyWebClient() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (SimplePostDetails.this == null) {
                return null;
            }
            return BitmapFactory.decodeResource(SimplePostDetails.this.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)SimplePostDetails.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            SimplePostDetails.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            SimplePostDetails.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = SimplePostDetails.this.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = SimplePostDetails.this.getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)SimplePostDetails.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            SimplePostDetails.this.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu:
                Intent intent = new Intent(SimplePostDetails.this,Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.toolbar_share:
                Funcs.shareApp(SimplePostDetails.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}
