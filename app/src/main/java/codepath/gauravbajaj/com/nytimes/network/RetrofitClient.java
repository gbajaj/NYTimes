package codepath.gauravbajaj.com.nytimes.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gauravb on 3/17/17.
 */

public class RetrofitClient {

    public Retrofit NYSearchClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/svc/search/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
