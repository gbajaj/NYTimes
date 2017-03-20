package codepath.gauravbajaj.com.nytimes.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import codepath.gauravbajaj.com.nytimes.models.NYResponse;
import codepath.gauravbajaj.com.nytimes.network.Observables;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final String SettingsFragment = "Sample Fragment";

    @BindView(R.id.search_activity_rvArticles)
    RecyclerView rvArticles;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<Article> articleArrayList = new ArrayList<>();
    ArticleArrayAdapter articleArrayAdapter;
    Context context = NYTimesApp.instance();
    UserPreferences userPreferences = new UserPreferences();
    private StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL);
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        articleArrayAdapter = new ArticleArrayAdapter(this, articleArrayList);

        rvArticles.setAdapter(articleArrayAdapter);
        // Set layout manager to position the items
        rvArticles.setLayoutManager(staggeredGridLayoutManager);
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

                throwable -> {
                },

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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                getIntent().putExtra("Query", query);
                if (TextUtils.isEmpty(query) == false) {
                    fetchResults(query, 0)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    nyResponse -> {
                                        final List<Article> articles = nyResponse.getResponse().getArticles();
                                        articleArrayList.clear();
                                        articleArrayList.addAll(articles);
                                        articleAdaptersEndlessScrollListener.resetState();
                                        articleArrayAdapter.notifyDataSetChanged();
                                    },
                                    throwable -> {
                                        Log.d(TAG, "Message ");
                                        if (throwable instanceof HttpException) {
                                            HttpException exception = (HttpException) throwable;
                                            if (exception.code() == 429) {
                                                Toast.makeText(context, "429", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        throwable.printStackTrace();

                                    },
                                    () -> Log.d(TAG, "onComplete()"));
                }

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(v -> {
            Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        });
        searchView.setOnCloseListener(() -> {
            Toast.makeText(this, "close", Toast.LENGTH_SHORT).show();
            return false;
        });
        // Customize searchview text and hint colors
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(0x80ffffff);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.miCompose:
                onFilterAction(item);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
//        .
//                retryWhen(errors -> errors.flatMap(error -> {
//            // For IOExceptions, we  retry
//            if (error instanceof HttpException) {
//                return Observable.just(null);
//            }
//
//            // For anything else, don't retry
//            return Observable.error(error);
//        }))
//                ;
    }

    //Create endless scroll listener
    final ArticleAdaptersEndlessScrollListener articleAdaptersEndlessScrollListener =
            new ArticleAdaptersEndlessScrollListener(staggeredGridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {
                    loadNextDataFromApi(page);
                }
            };

}
