package helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Sukriti on 7/25/16.
 */

/**
from a stackOverflow response - code not written by me
 */

 public abstract class EndlessRecyleOnScrollListenerStaggered extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyleOnScrollListenerStaggered.class
            .getSimpleName();

    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    public EndlessRecyleOnScrollListenerStaggered(
            StaggeredGridLayoutManager staggeredGridLayoutManager) {
        this.mStaggeredGridLayoutManager = staggeredGridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = mStaggeredGridLayoutManager.getChildCount();
        totalItemCount = mStaggeredGridLayoutManager.getItemCount();
        int[] firstVisibleItems = null;
        firstVisibleItems = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
        if (firstVisibleItems != null && firstVisibleItems.length > 0) {
            pastVisibleItems = firstVisibleItems[0];
        }
        if (loading) {
            if ((visibleItemCount + pastVisibleItems) >= (totalItemCount)) {
                loading = false;
                // Log.d("tag", "LOAD NEXT ITEM");
                current_page++;
                onLoadMore(current_page);
                loading = true;
            }
        }
    }

    public abstract void onLoadMore(int current_page);
}




