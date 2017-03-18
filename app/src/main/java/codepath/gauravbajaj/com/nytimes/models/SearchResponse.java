package codepath.gauravbajaj.com.nytimes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import codepath.gauravbajaj.com.nytimes.models.Article;

/**
 * Created by gauravb on 3/17/17.
 */

public class SearchResponse {
    public List<Article> getArticles() {
        return articles;
    }

    @SerializedName("docs")
    @Expose
    private List<Article> articles = null;

}
