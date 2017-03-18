package codepath.gauravbajaj.com.nytimes.network;

import android.util.Log;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by gauravb on 3/18/17.
 */

public class Observables {
    private static String TAG = Observables.class.getSimpleName();
    RetrofitClient retrofitClient = new RetrofitClient();

    Retrofit retrofitSearchClient = retrofitClient.NYSearchClient();

    public Observable<NYResponse> searchArticles(String query, int page, String sortOrder, String searchBeginDate, String newsDesk) {
        Log.d(TAG, "searchArticles()" + " " + query + " " + page + " " + sortOrder + " " + searchBeginDate + " " + newsDesk);
        RetrofitRequest.NYTimesArticleSearch apiService =
                retrofitSearchClient.create(RetrofitRequest.NYTimesArticleSearch.class);
        return apiService.getArticles(
                query, "" + page, "5a45d2a34a0f4225a786bcb7086ad327", searchBeginDate,
                sortOrder, newsDesk);
    }
}
