package codepath.gauravbajaj.com.nytimes.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import codepath.gauravbajaj.com.nytimes.network.Headline;
import codepath.gauravbajaj.com.nytimes.network.Multimedia;

/**
 * Created by gauravb on 3/6/17.
 */
public class Article implements Parcelable {
    @SerializedName("web_url")
    @Expose
    String weburl;

    public String getWeburl() {
        return weburl;
    }

    @SerializedName("headline")
    @Expose
    Headline headLine;
    @SerializedName("multimedia")
    @Expose
    List<Multimedia> multimediaList;

    String mainHeadLine;

    public String getMainHeadLine() {
        if (TextUtils.isEmpty(thumbNail) && headLine != null) {
            mainHeadLine = headLine.getMainHeadLine();
        }
        return mainHeadLine;
    }

    public String getThumbNail() {
        if (TextUtils.isEmpty(thumbNail) && multimediaList != null && multimediaList.isEmpty() == false) {
            thumbNail = "http://www.nytimes.com/" + multimediaList.get(0).getUrl();
        }
        return thumbNail;
    }

    String thumbNail = "";

    public Article(JSONObject jsonObject) throws JSONException {
        weburl = jsonObject.getString("web_url");
        mainHeadLine = jsonObject.getJSONObject("headline").getString("main");
        JSONArray multimedia = jsonObject.getJSONArray("multimedia");
        if (multimedia.length() > 0) {
            JSONObject multiMediaJson = multimedia.getJSONObject(0);
            this.thumbNail = "http://www.nytimes.com/" + multiMediaJson.getString("url");
        }

    }

    public static ArrayList<Article> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Article> results = new ArrayList<>();
        for (int x = 0; x < jsonArray.length(); x++) {
            try {
                results.add(new Article(jsonArray.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


    protected Article(Parcel in) {
        weburl = in.readString();
        mainHeadLine = in.readString();
        thumbNail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weburl);
        dest.writeString(mainHeadLine);
        dest.writeString(thumbNail);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public String toString() {
        return "Article{" +
                "weburl='" + weburl + '\'' +
                ", mainHeadLine='" + mainHeadLine + '\'' +
                ", thumbNail='" + thumbNail + '\'' +
                '}';
    }
}