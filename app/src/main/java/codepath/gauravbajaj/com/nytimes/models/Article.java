package codepath.gauravbajaj.com.nytimes.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gauravb on 3/6/17.
 */
@org.parceler.Parcel
public class Article {
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
    String thumbNail = "";

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

    @Override
    public String toString() {
        return "Article{" +
                "weburl='" + weburl + '\'' +
                ", mainHeadLine='" + mainHeadLine + '\'' +
                ", thumbNail='" + thumbNail + '\'' +
                '}';
    }
}