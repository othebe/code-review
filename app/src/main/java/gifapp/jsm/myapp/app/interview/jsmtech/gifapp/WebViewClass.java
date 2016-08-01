package gifapp.jsm.myapp.app.interview.jsmtech.gifapp;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.webkit.WebView;

import helper.Gestures;

/**
 * Created by Sukriti on 7/26/16.
 */
public class WebViewClass extends AppCompatActivity {

    String gifUrl = "";
    private WebView mWebview;

    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        mWebview = (WebView) findViewById(R.id.web_view);
        gestureDetectorCompat = new GestureDetectorCompat(this, new Gestures(WebViewClass.this));
        // fetching url from the previous activity of the gif clicked
        Bundle data = getIntent().getExtras();
        if (data != null) {
            gifUrl = data.getString("url");
            mWebview.loadUrl(gifUrl);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        // for animation on back pressed
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


}
