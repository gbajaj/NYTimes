package codepath.gauravbajaj.com.nytimes.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by gauravb on 3/10/17.
 */

public class Request {
    public String searchArticles(String searchTerm) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.nytimes.com/svc/search/v2/articlesearch.json").newBuilder();
        urlBuilder.addQueryParameter("api-key", "5a45d2a34a0f4225a786bcb7086ad327");
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
