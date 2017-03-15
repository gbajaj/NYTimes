package codepath.gauravbajaj.com.nytimes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.gauravbajaj.com.nytimes.R;
import codepath.gauravbajaj.com.nytimes.adapters.ArticleArrayAdapter;
import codepath.gauravbajaj.com.nytimes.models.Article;
import codepath.gauravbajaj.com.nytimes.network.Request;
import codepath.gauravbajaj.com.nytimes.parser.ResultsParser;

public class SearchActivity extends AppCompatActivity {
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

                            Request request = new Request();

                            try {
                                String responseString = request.searchArticles(query);
                                final ArrayList<Article> results = new ResultsParser().nowPlaying(responseString);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        articleArrayAdapter.addAll(results);
                                        Log.d("Test", articles.toString());
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }
                Toast.makeText(SearchActivity.this, "searching for " + query, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onFilterAction(MenuItem mi) {
        Intent  i = new Intent(this, SettingsActivity.class);

        startActivity(i);
        Toast.makeText(this, "ToastText", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//

}
