package com.sairajen.saihere;

import android.app.Application;
import com.onesignal.OneSignal;
import com.sairajen.saihere.helper.Tools;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Gmonetix
 */

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // init realm database
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("wordpress.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        // Call syncHashedEmail anywhere in your app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        // OneSignal.syncHashedEmail(userEmail);

        // get enabled controllers
        Tools.requestInfoApi(this);

    }

    public static synchronized App getInstance() {
        return instance;
    }

}
