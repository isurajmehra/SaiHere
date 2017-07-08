package com.sairajen.saihere.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sairajen.saihere.R;
import com.sairajen.saihere.fragments.DailyMessage;
import com.sairajen.saihere.fragments.MenuHome;
import com.sairajen.saihere.helper.Funcs;
import com.sairajen.saihere.helper.NotificationReceiver;
import com.sairajen.saihere.helper.SharedPref;
import com.sairajen.saihere.model.Category;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * @author Gmonetix
 */
public class Home extends AppCompatActivity {

    private Toolbar toolbar;

    private AdView adView;

    public static final String EXTRA_OBJC = "EXTRA_OBJC";

    private MenuHome menuHome;
    public static List<Category> categoryList;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_home);
        initHeader();

        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.banner_ad_unit_id));
        adView = (AdView) findViewById(R.id.adHome);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        sharedPref = new SharedPref(Home.this);
        if (sharedPref.isFirstTime()) {
            sharedPref.setFirstTime(false);
            setNotification();
        }

        menuHome = new MenuHome();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,menuHome).commit();

        if (getIntent().hasExtra("IS_ACCESSED_FROM_NOTIFICATION")) {
            boolean res = false;
            res = getIntent().getExtras().getBoolean("IS_ACCESSED_FROM_NOTIFICATION");
            if (res) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new DailyMessage()).commit();
            }
        }

        categoryList = new ArrayList<>();
        categoryList = (List<Category>) getIntent().getSerializableExtra(EXTRA_OBJC);

    }

    private void initHeader() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        Home.this.setTitle("www.saihere.com");
    }

    private void setNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,7);
        calendar.set(Calendar.MINUTE,30);

        Intent intent = new Intent(getApplicationContext(),NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,menuHome).commit();
                break;
            case R.id.toolbar_share:
                Funcs.shareApp(Home.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.container) != menuHome) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,menuHome).commit();
        } else {
            super.onBackPressed();
        }
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

//    public static boolean active = false;

}
