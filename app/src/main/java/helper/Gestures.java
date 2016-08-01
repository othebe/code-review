package helper;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Sukriti on 7/27/16.
 */
public class Gestures extends GestureDetector.SimpleOnGestureListener {
    //handle 'swipe right' gesture
    private Context context;

    public Gestures(Context context){
        this.context = context;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {

        if(event2.getX() > event1.getX()) {
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        return true;
    }
}