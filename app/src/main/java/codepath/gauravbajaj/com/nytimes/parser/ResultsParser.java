package codepath.gauravbajaj.com.nytimes.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codepath.gauravbajaj.com.nytimes.models.Article;


/**
 * Created by gauravb on 3/10/17.
 */

public class ResultsParser {
    public ArrayList<Article> nowPlaying(String responseData) throws JSONException {
        JSONObject jsonResponse = new JSONObject(responseData);
        JSONArray docsResults = null;
        docsResults = jsonResponse.getJSONObject("response").getJSONArray("docs");
        Log.d("ResultsParser", docsResults.toString());
        return Article.fromJSONArray(docsResults);
    }
}
