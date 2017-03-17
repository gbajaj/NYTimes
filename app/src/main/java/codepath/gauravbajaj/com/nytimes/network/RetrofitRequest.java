package codepath.gauravbajaj.com.nytimes.network;

import java.io.IOException;
import java.util.List;

import codepath.gauravbajaj.com.nytimes.models.Article;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gauravb on 3/17/17.
 */

public class RetrofitRequest {

    public static interface NYTimesArticleSearch {
        @GET("articlesearch.json")
        Call<NYResponse> getArticles(@Query("q") String user, @Query("page") String page, @Query("api-key") String apiKey);
    }

    Retrofit build() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/svc/search/v2/")
                .build();
        return retrofit;
    }

    public String searchArticles(String searchTerm) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.nytimes.com/svc/search/v2/articlesearch.json").newBuilder();
        urlBuilder.addQueryParameter("api-key", "5a45d2a34a0f4225a786bcb7086ad32    7");
        urlBuilder.addQueryParameter("page", "0");
        urlBuilder.addQueryParameter("q", searchTerm);

        String url = urlBuilder.build().toString();

        final OkHttpClient client = new OkHttpClient();

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }
}
