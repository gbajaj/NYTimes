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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.gauravbajaj.com.nytimes.R;
import codepath.gauravbajaj.com.nytimes.activities.ArticleActivity;
import codepath.gauravbajaj.com.nytimes.models.Article;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

//import com.squareup.picasso.Picasso;

/**
 * Created by gauravb on 3/15/17.
 */

public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ArticleArrayAdapter.class.getSimpleName();
    ArrayList<Article> articleArrayList;
    Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public ArticleArrayAdapter(Context context, ArrayList<Article> articleArrayList) {
        this.articleArrayList = articleArrayList;
        this.context = context;
        if (articleArrayList == null) {
            throw new NullPointerException("Article ArrayList Can't Be Null or Empty");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.article_adapter_item_article_result, parent, false);

            // Return a new holder instance
            viewHolder = new ViewHolder(contactView);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            viewHolder = new ProgressViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //Get the data iterm from the current position
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            final Article article = articleArrayList.get(position);
            holder.articleImage.setImageResource(0);
            String headLine = "";
            holder.articleText.setText(article.getMainHeadLine());
            String thumbNail = article.getThumbNail();
            if (TextUtils.isEmpty(thumbNail) == false) {
                Glide.with(context).load(thumbNail).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d(TAG, "Glide onException: ");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d(TAG, "Glide downloaded : " + model);
                        return false;
                    }
                }).bitmapTransform(
                        new RoundedCornersTransformation(context, 2, 2)).into(holder.articleImage);
            }
            String newsDesk = article.getNewdesk();
            holder.newsDeskTv.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(newsDesk) == false && "None".equals(newsDesk) == false) {
                holder.newsDeskTv.setText(newsDesk);
            } else {
                holder.newsDeskTv.setVisibility(View.GONE);
            }
            String snippet = article.getSnippet();
            if (TextUtils.isEmpty(snippet) == false) {
                holder.snippetTv.setText(snippet);

            }
        } else {
            ((ProgressViewHolder)viewHolder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return articleArrayList.get(position) != null? VIEW_ITEM: VIEW_PROG;
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
        @BindView(R.id.item_article_headline)
        public TextView articleText;
        @BindView(R.id.item_article_news_desk)
        public TextView newsDeskTv;
        @BindView(R.id.item_article_snippet)
        public TextView snippetTv;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    Article article = articleArrayList.get(position);
                    // We can access the data within the views
                    Intent i = new Intent(context, ArticleActivity.class);
                    i.putExtra("article", Parcels.wrap(article));
                    context.startActivity(i);
                    Log.d(TAG, "Message " + article + " clicked");
                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}
