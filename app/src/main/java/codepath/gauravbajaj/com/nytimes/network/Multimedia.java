package codepath.gauravbajaj.com.nytimes.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by gauravb on 3/17/17.
 */
@Parcel
public class Multimedia {
    @SerializedName("url")
    @Expose
    String url;

    public String getUrl() {
        return url;
    }
}
