package codepath.gauravbajaj.com.nytimes.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Date;

import codepath.gauravbajaj.com.nytimes.NYTimesApp;
import codepath.gauravbajaj.com.nytimes.R;

/**
 * Created by gauravb on 3/17/17.
 */

public class UserPreferences {

    SharedPreferences sharedPreferences = NYTimesApp.instance().getDefaultSharedPreferences();
    SharedPreferences.Editor editor = sharedPreferences.edit();
    Context context = NYTimesApp.instance();

    public String getSortOrder() {
        return sharedPreferences.getString(context.getString(R.string.pref_key_search_news_sort_order), "oldest");
    }

    public void setSortOrder(String value) {
        editor.putString(context.getString(R.string.pref_key_search_news_sort_order), value);
    }

    public void setArtNewsEnable(boolean value) {
        editor.putBoolean(context.getString(R.string.pref_key_search_news_desk_value_art), value);
    }

    public Boolean isArtNewsEnabled() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_key_search_news_desk_value_art), false);
    }

    public void setFashionNewsEnable(boolean value) {
        editor.putBoolean(context.getString(R.string.pref_key_search_news_desk_value_fashion), value);
    }

    public Boolean isFashionNewsEnabled() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_key_search_news_desk_value_fashion), false);
    }

    public void setSportsNewsEnable(boolean value) {
        editor.putBoolean(context.getString(R.string.pref_key_search_news_desk_value_sports), value);
    }

    public Boolean isSportsNewsEnabled() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_key_search_news_desk_value_sports), false);
    }

    public Long getSearchBeginDate() {
        return sharedPreferences.getLong(context.getString(R.string.pref_key_search_news_begin_date), -1);
    }

    public void setBeginDate(long value) {
        editor.putLong(context.getString(R.string.pref_key_search_news_begin_date), value);
    }

    public void commit() {
        editor.commit();
    }

    private Date defaultBeginDate() {
        Long val = getSearchBeginDate();
        if (val == -1) {
            final Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, 2017);
            c.set(Calendar.MONTH, 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            return c.getTime();
        }
        return new Date(val);
    }

    public String searchBeginValue() {
        Calendar c = Calendar.getInstance();
        c.setTime(defaultBeginDate());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        return "" + year + String.format("%02d", month) + String.format("%02d", date);
    }

    public String getNewsDeskValues() {
        StringBuilder newsDesk = new StringBuilder();
        StringBuilder selection = new StringBuilder();
        newsDesk.append("news_desk:(\"");

        if (isFashionNewsEnabled()) {
            selection.append("Fashion & Style");
        }
        if (isArtNewsEnabled()) {
            selection.append("Arts");
        }
        if (isSportsNewsEnabled()) {
            selection.append("Sports");
        }
        if (selection.length() == 0) {
            selection.append("none");
        }
        newsDesk.append(selection.toString());
        newsDesk.append("\")");
        return newsDesk.toString();
    }
}
