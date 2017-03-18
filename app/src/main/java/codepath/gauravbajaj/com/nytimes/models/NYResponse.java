package codepath.gauravbajaj.com.nytimes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gauravb on 3/17/17.
 */

public class NYResponse {
    public SearchResponse getResponse() {
        return response;
    }

    @SerializedName("response")
    @Expose
    SearchResponse response = null;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("copyright")
    @Expose
    String copyright;

}
