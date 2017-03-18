package codepath.gauravbajaj.com.nytimes.network;

import java.io.IOException;

import codepath.gauravbajaj.com.nytimes.models.NYResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by gauravb on 3/17/17.
 */

public class RetrofitRequest {

    public static interface NYTimesArticleSearch {
        @GET("articlesearch.json")
        Observable<NYResponse> getArticles(@Query("q") String user, @Query("page") String page, @Query("api-key") String apiKey,
                                           @Query("begin_date") String beginDate,
                                           @Query("sort") String sort,
                                           @Query("fq") String newsDesk);
    }
}
