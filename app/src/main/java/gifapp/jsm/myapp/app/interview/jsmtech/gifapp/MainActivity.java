package gifapp.jsm.myapp.app.interview.jsmtech.gifapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.GIFAdapter;
import helper.EndlessRecyleOnScrollListenerStaggered;
import helper.SingleTon;
import helper.Utility;
import model.GIFDetails;

/**
 * Created by Sukriti on 7/25/16.
 */

public class MainActivity extends AppCompatActivity {

    private EditText mSearch;
    private RecyclerView mRecycleView;
    private ProgressBar mProgressBar;
    private ArrayList<GIFDetails> listOfGifs;
    private GIFAdapter gifAdapter;
    private Integer offset = 0;
    private String searchUrl;
    private int calledMethod = 0;  // 0 -> Trending || 1 -> Search // othebe: Should use var names for this.
    private Utility utility = new Utility();
    String API_KEY = "dc6zaTOxFJmzC"; // Public beta key for the API // othebe: Should be final.
                                                                        // othebe: Would have liked this to be in a config class.
    private Integer trendingOffset = 0;
    String trendingUrl = "http://api.giphy.com/v1/gifs/trending?api_key=" + API_KEY + "&limit=25" + "&offset=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // linking to xml file
        setContentView(R.layout.activity_main);

        // linking individual UI components
        mSearch = (EditText) findViewById(R.id.search_text);
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // after search if the user starts scrolling the keyboard goes away automatically
        // othebe: use separate function for listeners/handlers.
        // othebe: could have hidden keyboard on scroll too.
        mRecycleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utility.hideKeyboard(MainActivity.this);
                return false;
            }
        });

        // setting the recycle view to a grid layout
        final StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(sglm);
        mRecycleView.setHasFixedSize(true);

        // setting the list to the adapter
        listOfGifs = new ArrayList<>();
        gifAdapter = new GIFAdapter(MainActivity.this, listOfGifs);

        // othebe: Small typos here and there.
        // setting the adapter to the recyle view
        mRecycleView.setAdapter(gifAdapter);

        // calling the volley method to load trending gifs by default
        makeGifJSONRequest(trendingUrl + trendingOffset);


        // enabling loading additional gifs
        mRecycleView.setOnScrollListener(new EndlessRecyleOnScrollListenerStaggered(sglm) {
            @Override
            public void onLoadMore(int current_page) {
                // Check if URL is equal to trending url;
                if (calledMethod == 1) { // for searching
                    // othebe: offset logic should be encapsulated in a funtion.
                    offset += 25;
                    makeGifJSONRequest(searchUrl + offset);
                } else {
                    trendingOffset += 25; // for trending

                    // othebe: No need to pass variables since they are part of object.
                    makeGifJSONRequest(trendingUrl + trendingOffset);
                }
            }
        });

        // enabling search in real time
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != 0) { // something is entered in the EditText
                    offset = 0; // every time something new is entered, the search reinitializes
                    calledMethod = 1; // search method
                    searchGifs(s.toString()); // calling the search method
                } else {
                    // shows trending GIFs by default
                    trendingOffset = 0;
                    calledMethod = 0;
                    makeGifJSONRequest(trendingUrl + trendingOffset);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    // search method which in turn calls the volley method
    public void searchGifs(String s) {
        String searchString = s.trim().replaceAll("\\s+", "+");
        searchUrl = "http://api.giphy.com/v1/gifs/search?q=" + searchString + "&api_key=" + API_KEY + "&offset=";
        calledMethod = 1;
        makeGifJSONRequest(searchUrl + 0);
    }

    // volley request for fetching the JSON data and adding it to the list of gifs
    public void makeGifJSONRequest(String url) {
        mProgressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray j = response.getJSONArray("data");
                    // delete all data when its a new call
                    if ((offset == 0 && calledMethod == 1) || (trendingOffset == 0 && calledMethod == 0)) {
                        listOfGifs.clear();
                        gifAdapter.notifyDataSetChanged();
                        mRecycleView.smoothScrollToPosition(0);
                    }
                    // fetching JSON response
                    for (int i = 0; i < j.length(); i++) {
                        // getting the GIF url for all the trending gifs
                        String url = j.getJSONObject(i).getJSONObject("images").getJSONObject("fixed_height").getString("url");
                        // adding to the list
                        GIFDetails gifDetails = new GIFDetails();
                        gifDetails.setGifUrl(url);
                        listOfGifs.add(gifDetails);
                    }
                    mProgressBar.setVisibility(View.GONE);
                    gifAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    utility.showShortText(MainActivity.this, "Error loading data");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                utility.showShortText(MainActivity.this, "Error " + error.getMessage());
            }
        });

        SingleTon.getInstance(getApplicationContext()).addToRequestQueue(request, "json_object");
    }


}
