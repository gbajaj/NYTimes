package codepath.gauravbajaj.com.nytimes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.gauravbajaj.com.nytimes.NYTimesApp;
import codepath.gauravbajaj.com.nytimes.R;
import codepath.gauravbajaj.com.nytimes.activities.ArticleActivity;
import codepath.gauravbajaj.com.nytimes.models.Article;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by gauravb on 3/15/17.
 */

public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {
    private static final String TAG = ArticleArrayAdapter.class.getSimpleName();
    Picasso picasso = NYTimesApp.instance().picasso;
    ArrayList<Article> articleArrayList;
    Context context;

    public ArticleArrayAdapter(Context context, ArrayList<Article> articleArrayList, Picasso picasso) {
        this.picasso = picasso;
        this.articleArrayList = articleArrayList;
        this.context = context;
        if (articleArrayList == null) {
            throw new NullPointerException("Article ArrayList Can't Be Null or Empty");
        }
    }


    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.article_adapter_item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Get the data iterm from the current position
        final Article article = articleArrayList.get(position);
        holder.articleImage.setImageResource(0);
        String headLine = "";
        holder.articleText.setText(article.getMainHeadLine());
        String thumbNail = article.getThumbNail();
        if (TextUtils.isEmpty(thumbNail) == false) {
            picasso.load(thumbNail)
//                .placeholder(placeHolder)
                    .transform(new RoundedCornersTransformation(2, 2
                    )).into(holder.articleImage, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @BindView(R.id.item_article_image)
        public ImageView articleImage;
        @BindView(R.id.item_article_image_text)
        public TextView articleText;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        Article article = articleArrayList.get(position);
                        // We can access the data within the views
                        Intent i = new Intent(context, ArticleActivity.class);
                        i.putExtra("article", Parcels.wrap(article));
                        context.startActivity(i);
                        Log.d(TAG, "Message " + article + " clicked");
                    }
                }
            });
        }
    }
}
