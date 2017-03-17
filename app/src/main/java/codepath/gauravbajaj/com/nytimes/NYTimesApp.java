package codepath.gauravbajaj.com.nytimes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 * Created by gauravb on 3/15/17.
 */

public class NYTimesApp extends Application {
    public OkHttpClient client;
    public Picasso picasso;
    private static NYTimesApp instance;
    private static final String SHARED_PREF_DEFAULT = "SharedPreferencesDefault";

    public NYTimesApp() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        client = new OkHttpClient();
        picasso = new Picasso.Builder(this).downloader(new OkHttp3Downloader(client)).build();
    }

    public static NYTimesApp instance() {
        return instance;
    }

    public SharedPreferences getDefaultSharedPreferences() {
        return this.getSharedPreferences(SHARED_PREF_DEFAULT, Context.MODE_PRIVATE);
    }
}
