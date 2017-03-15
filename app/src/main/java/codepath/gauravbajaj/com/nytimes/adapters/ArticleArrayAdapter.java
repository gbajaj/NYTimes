package codepath.gauravbajaj.com.nytimes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import codepath.gauravbajaj.com.nytimes.NYTimesApp;
import codepath.gauravbajaj.com.nytimes.R;
import codepath.gauravbajaj.com.nytimes.activities.ArticleActivity;
import codepath.gauravbajaj.com.nytimes.models.Article;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by gauravb on 3/15/17.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    Picasso picasso = NYTimesApp.instance().picasso;

    public ArticleArrayAdapter(@NonNull Context context, @NonNull List<Article> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get the data iterm from the current position
        final Article article = getItem(position);

        //Check if the existing view is being used
        if (convertView == null) {
            // not using a recycled view -> inflate the layout
            convertView =
                    LayoutInflater.from(getContext()).inflate(R.layout.article_adapter_item_article_result, parent, false);

        } else {

        }
        //find the image view inside it
        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_article_image);
        //clear out the recycle image from the convertview from the last time
        imageView.setImageResource(0);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.item_article_image_text);
        tvTitle.setText(article.getMainHeadLine());
        String thumbNail = article.getThumbNail();
        if (TextUtils.isEmpty(thumbNail) == false) {
            picasso.load(thumbNail)
//                .placeholder(placeHolder)
                    .transform(new RoundedCornersTransformation(2, 2
                    )).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {

                }
            });
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                article.
                Intent i = new Intent(getContext(), ArticleActivity.class);
                i.putExtra("article", article);
                getContext().startActivity(i);
            }
        });

        return convertView;

    }
}
