package codepath.gauravbajaj.com.nytimes.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gauravb on 3/17/17.
 */

public class Headline {
    public String getMainHeadLine() {
        return mainHeadLine;
    }

    @SerializedName("main")
    @Expose
    String mainHeadLine;

}
