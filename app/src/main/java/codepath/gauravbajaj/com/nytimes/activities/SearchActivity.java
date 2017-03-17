package codepath.gauravbajaj.com.nytimes.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.gauravbajaj.com.nytimes.R;
import codepath.gauravbajaj.com.nytimes.adapters.ArticleArrayAdapter;
import codepath.gauravbajaj.com.nytimes.fragments.DatePickerFragment;
import codepath.gauravbajaj.com.nytimes.fragments.SettingsDialogFragment;
import codepath.gauravbajaj.com.nytimes.models.Article;
import codepath.gauravbajaj.com.nytimes.network.NYResponse;
import codepath.gauravbajaj.com.nytimes.network.RetrofitClient;
import codepath.gauravbajaj.com.nytimes.network.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final String SettingsFragment = "Sample Fragment";

    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.etQuery)
    EditText etQuery;
    @BindView(R.id.gvResults)
    GridView gtResults;

    ArrayList<Article> articles = new ArrayList<>();
    ArticleArrayAdapter articleArrayAdapter;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        articleArrayAdapter = new ArticleArrayAdapter(this, articles);
        gtResults.setAdapter(articleArrayAdapter);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String query = etQuery.getText().toString();
                if (TextUtils.isEmpty(query) == false) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            RetrofitRequest.NYTimesArticleSearch apiService = new RetrofitClient().NYSearchClient()
                                    .create(RetrofitRequest.NYTimesArticleSearch.class);

                            Call<NYResponse> searchResponseCall = apiService.getArticles(query, "0", "5a45d2a34a0f4225a786bcb7086ad327");
                            searchResponseCall.enqueue(new Callback<NYResponse>() {
                                                           @Override
                                                           public void onResponse(Call<NYResponse> call, retrofit2.Response<NYResponse> response) {
                                                               try {
                                                                   final List<Article> articles = response.body().getResponse().getArticles();
                                                                   handler.post(new Runnable() {
                                                                       @Override
                                                                       public void run() {
                                                                           articleArrayAdapter.addAll(articles);
                                                                       }
                                                                   });

                                                                   Log.d(TAG, "Message " + articles);

                                                               } catch (Exception ex) {
                                                                   ex.printStackTrace();
                                                               }
                                                           }

                                                           @Override
                                                           public void onFailure(Call<NYResponse> call, Throwable t) {

                                                           }
                                                       }
                            );

                        }
                    });
                    t.start();
                }
                Toast.makeText(SearchActivity.this, "searching for " + query, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onFilterAction(MenuItem mi) {
        FragmentManager fm = getSupportFragmentManager();
        SettingsDialogFragment settingsDialogFragment = new SettingsDialogFragment();
        settingsDialogFragment.show(fm, SettingsFragment);
        Toast.makeText(this, "ToastText", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(fm, "Sample Fragment2");
        Toast.makeText(this, "onClick" +
                "", Toast.LENGTH_SHORT).show();
    }

}
