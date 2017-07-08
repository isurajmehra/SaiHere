package com.sairajen.saihere.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * @author Gmonetix
 */

public class Funcs {

    public static void shareApp(Context context){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sai Here");
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.sairajen.saihere\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            context.startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            Log.e("ERROR",""+e.getMessage());
        }
    }

    public static void shareLink(Context context, String text){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sai Here");
            i.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            Log.e("ERROR",""+e.getMessage());
        }
    }

    public static void openLink(Context context, String url){
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            Log.e("ERROR",""+e.getMessage());
        }
    }

}
