package codepath.gauravbajaj.com.nytimes.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

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

        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.addDefaultShareMenuItem();
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, article.getWeburl());

        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_white_24px);
        intentBuilder.setActionButton(icon, "Share Link", pendingIntent, true);

        CustomTabsIntent customTabsIntent = intentBuilder.build();

        customTabsIntent.launchUrl(this, Uri.parse(article.getWeburl()));
    }

}
