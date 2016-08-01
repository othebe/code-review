package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import gifapp.jsm.myapp.app.interview.jsmtech.gifapp.FullGifView;
import gifapp.jsm.myapp.app.interview.jsmtech.gifapp.R;
import model.GIFDetails;

/**
 * Created by Sukriti on 7/26/16.
 */
public class GIFAdapter extends RecyclerView.Adapter<GIFAdapter.RV_ViewHolder> {

    private List<GIFDetails> listOfGifUrls;
    private Context mContext;
    View itemView;

    int[] a = {R.color.colorAccent, R.color.green, R.color.orange, R.color.yellow,
            R.color.lightBlue, R.color.purple};
    int random = 0;

    public GIFAdapter(Context context, ArrayList<GIFDetails> listOfGifUrls) {
        this.mContext = context;
        this.listOfGifUrls = listOfGifUrls;
    }

    @Override
    public RV_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gif_view, parent, false);
        return new RV_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RV_ViewHolder holder, int position) {
        final GIFDetails gif = listOfGifUrls.get(holder.getAdapterPosition());

        // Randomizing the default image
       random = a[(int) (Math.random()*a.length)];

        // loading the gifs on the views
        Glide.with(mContext)
                .load(gif.getGifUrl())
                .skipMemoryCache(true)
                .centerCrop()
                .placeholder(random)
                .into(new GlideDrawableImageViewTarget(holder.mGifView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        //check isRefreshing
                    }
                });


        // going to full view on click
        holder.mGifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FullGifView.class);
                intent.putExtra("url", gif.getGifUrl());
                mContext.startActivity(intent);
                //animation
                ((Activity) mContext).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                // ((Activity) mContext).overridePendingTransition(android.R.anim.slide_in_left, 0);
            }
        });


        // For better performance, can load only images by default and show gifs only in full view
//        Picasso.with(mContext)
//                .load(gif.getGifUrl())
//                .fit()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_default_image)
//                .error(R.mipmap.R.color.purple)
//                .into(holder.mGifView);
                // .setIndicatorsEnabled(true);



    }

    @Override
    public int getItemCount() {
        return listOfGifUrls.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class RV_ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mGifView;

        public RV_ViewHolder(View itemView) {
            super(itemView);
            mGifView = (ImageView) itemView.findViewById(R.id.gif_image);
        }
    }
}


