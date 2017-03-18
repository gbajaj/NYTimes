package codepath.gauravbajaj.com.nytimes.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.gauravbajaj.com.nytimes.NYTimesApp;
import codepath.gauravbajaj.com.nytimes.R;
import codepath.gauravbajaj.com.nytimes.adapters.ArticleAdaptersEndlessScrollListener;
import codepath.gauravbajaj.com.nytimes.adapters.ArticleArrayAdapter;
import codepath.gauravbajaj.com.nytimes.data.UserPreferences;
import codepath.gauravbajaj.com.nytimes.fragments.DatePickerFragment;
import codepath.gauravbajaj.com.nytimes.fragments.SettingsDialogFragment;
import codepath.gauravbajaj.com.nytimes.models.Article;
import codepath.gauravbajaj.com.nytimes.network.NYResponse;
import codepath.gauravbajaj.com.nytimes.network.Observables;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final String SettingsFragment = "Sample Fragment";

    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.etQuery)
    EditText etQuery;
    @BindView(R.id.search_activity_rvArticles)
    RecyclerView rvArticles;

    ArrayList<Article> articleArrayList = new ArrayList<>();
    ArticleArrayAdapter articleArrayAdapter;
    Context context = NYTimesApp.instance();
    UserPreferences userPreferences = new UserPreferences();
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        articleArrayAdapter = new ArticleArrayAdapter(this, articleArrayList, NYTimesApp.instance().picasso);
        rvArticles.setAdapter(articleArrayAdapter);
        // Set layout manager to position the items
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(staggeredGridLayoutManager);

        rvArticles.setLayoutManager(staggeredGridLayoutManager);

        //Create endless scroll listener
        final ArticleAdaptersEndlessScrollListener articleAdaptersEndlessScrollListener =
                new ArticleAdaptersEndlessScrollListener(staggeredGridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {
                        loadNextDataFromApi(page);
                    }
                };
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String query = etQuery.getText().toString();
                getIntent().putExtra("Query", query);
                if (TextUtils.isEmpty(query) == false) {
                    fetchResults(query, 0).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    nyResponse -> {
                                        final List<Article> articles = nyResponse.getResponse().getArticles();
                                        articleArrayList.clear();
                                        articleArrayList.addAll(articles);
                                        articleAdaptersEndlessScrollListener.resetState();
                                        articleArrayAdapter.notifyDataSetChanged();
                                    },
                                    throwable -> Log.d(TAG, "Message "),
                                    () -> Log.d(TAG, "onComplete()"));
                }
                Toast.makeText(SearchActivity.this, "searching for " + query, Toast.LENGTH_SHORT).show();
            }
        });

        rvArticles.addOnScrollListener(articleAdaptersEndlessScrollListener);
    }


    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        Log.d(TAG, "loadNextDataFromApi " + offset);
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        String query = getIntent().getStringExtra("Query");
        fetchResults(query, offset).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(
                nyResponse -> {
                    final List<Article> articles = nyResponse.getResponse().getArticles();
                    articleArrayList.addAll(articles);
                    articleArrayAdapter.notifyDataSetChanged();
                },

                throwable -> Toast.makeText(context, "Error Loading results", Toast.LENGTH_SHORT).show(),

                () -> Log.d(TAG, "onCompleted"));
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

    private Observable<NYResponse> fetchResults(String query, int page) {
        Observables observables = new Observables();
        String searchBeginDate = userPreferences.searchBeginValue();
        String sortOrder = userPreferences.getSortOrder();
        String newsDesk = userPreferences.getNewsDeskValues();
        return observables.searchArticles(query, page, sortOrder, searchBeginDate, newsDesk);
    }
}
