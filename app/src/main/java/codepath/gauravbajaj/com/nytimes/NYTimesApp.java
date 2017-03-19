package codepath.gauravbajaj.com.nytimes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;


/**
 * Created by gauravb on 3/15/17.
 */

public class NYTimesApp extends Application {
    public Glide glide;
    private static NYTimesApp instance;
    private static final String SHARED_PREF_DEFAULT = "SharedPreferencesDefault";

    public NYTimesApp() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        glide  = Glide.get(this);
    }

    public static NYTimesApp instance() {
        return instance;
    }

    public SharedPreferences getDefaultSharedPreferences() {
        return this.getSharedPreferences(SHARED_PREF_DEFAULT, Context.MODE_PRIVATE);
    }
}
