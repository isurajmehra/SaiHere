package com.sairajen.saihere.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.sairajen.saihere.R;
import com.sairajen.saihere.connection.callbacks.CallbackInfo;
import com.google.gson.Gson;

public class SharedPref {

    private Context ctx;
    private SharedPreferences custom_prefence;

    public static final int MAX_OPEN_COUNTER = 10 ;

    public SharedPref(Context context) {
        this.ctx = context;
        custom_prefence = context.getSharedPreferences("MAIN_PREF", Context.MODE_PRIVATE);
    }

    /**
     * Preference for Fcm register
     */
    public void setFirstTime(boolean value){
        custom_prefence.edit().putBoolean("FIRST_TIME", value).apply();
    }

    public boolean isFirstTime(){
        return custom_prefence.getBoolean("FIRST_TIME", true);
    }

    public void setFcmRegId(String fcmRegId){
        custom_prefence.edit().putString("FCM_PREF_KEY", fcmRegId).apply();
    }

    public void setOpenAppCounter(int val){
        custom_prefence.edit().putInt("OPEN_COUNTER_KEY", val).apply();
    }

    public void setInfoObject(CallbackInfo object_info) {
        String str_info = new Gson().toJson(object_info, CallbackInfo.class);
        custom_prefence.edit().putString("key_info", str_info).apply();
    }

}
