package gifapp.jsm.myapp.app.interview.jsmtech.gifapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import helper.Gestures;

/**
 * Created by Sukriti on 7/26/16.
 */
public class FullGifView extends AppCompatActivity {

    String gifUrl = "";
    private ImageView mGifView;
    private TextView mGifUrl;
    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_gif_view);

        mGifView = (ImageView) findViewById(R.id.gif_image);
        mGifUrl = (TextView) findViewById(R.id.url_gif);

        gestureDetectorCompat = new GestureDetectorCompat(this, new Gestures(FullGifView.this));

        // fetching data from previous activity
        Bundle data = getIntent().getExtras();
        if (data != null) {
        // using the Glide Library to display the gifs from the url
            gifUrl = data.getString("url");
            mGifUrl.setText(gifUrl);
            Glide.with(FullGifView.this)
                    .load(gifUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .fitCenter()
                    .placeholder(R.color.purple)
                    .into(new GlideDrawableImageViewTarget(mGifView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                        }
                    });

            // clicking on the url will take the user to a web view - didn't want to a have a link that does nothing
            mGifUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FullGifView.this, WebViewClass.class);
                    intent.putExtra("url", gifUrl);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });
        }
    }
    // for adding some animation on back pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

