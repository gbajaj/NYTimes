package codepath.gauravbajaj.com.nytimes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.gauravbajaj.com.nytimes.R;
import codepath.gauravbajaj.com.nytimes.models.Article;

public class ArticleActivity extends AppCompatActivity {
    @BindView(R.id.article_activity_wv)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Article article = Parcels.unwrap(getIntent().getParcelableExtra("article"));
        webView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
        webView.loadUrl(article.getWeburl());

    }

}
