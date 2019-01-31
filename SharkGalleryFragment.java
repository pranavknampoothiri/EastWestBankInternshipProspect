package com.example.pranav.sharkfeed;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pranav.sharkfeed.R;
import com.example.pranav.sharkfeed.SharkGalleryItem;
import com.example.pranav.sharkfeed.UrlManager;
// import com.reginald.swiperefresh.CustomSwipeRefreshLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;








/**
 * A simple {@link Fragment} subclass.
 */
public class SharkGalleryFragment extends Fragment {

    private static final String TAG = SharkGalleryFragment.class.getSimpleName();
    private static final int COLUMN_NUM = 4;
    private static final int ITEM_PER_PAGE = 10;

    private RequestQueue mRq;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    //private CustomSwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SharkGalleryAdapter mAdapter;

    private boolean mLoading = false;
    private boolean mHasMore = true;

    private SearchView mSearchView;

    public SharkGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.sharkfragment_gallery, container, false);
        View view = inflater.inflate(R.layout.sharkfragment_gallery, container, false);
        mRq = Volley.newRequestQueue(getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), COLUMN_NUM);
        // This is the Recycle View path
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SharkGalleryAdapter(getActivity(), new ArrayList<SharkGalleryItem>());
        mRecyclerView.setAdapter(mAdapter);
        // All the objects are attached/stored to an adapter arraylist, which helps the recyclerview present them
        // as pictures.

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItem = mLayoutManager.getItemCount();
                int lastItemPos = mLayoutManager.findLastVisibleItemPosition();
                if (mHasMore && !mLoading && totalItem - 1 != lastItemPos) {
                    startLoading();
                }
            }
        });

        // mSwipeRefreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );

        startLoading();
        return view;
    }

    public void refresh() {
        mAdapter.clear();
        startLoading();
    }

    //The objects are loaded into
    private void startLoading() {

        Log.d(TAG, "startLoading");
        mLoading = true;
        int totalItem = mLayoutManager.getItemCount();
        final int page = totalItem / ITEM_PER_PAGE + 1;

        String query = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(UrlManager.PREF_SEARCH_QUERY, null);

        // String url = UrlManager.getInstance().getItemUrl(query, page);
        String url = UrlManager.getInstance().getItemUrl( page);

        // Here you are filing a query to say what kind of topic pictures you are interested in.
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse " + response);
                        List<SharkGalleryItem> result = new ArrayList<>();
                        try {
                            JSONObject photos = response.getJSONObject("photos");
                            if (photos.getInt("pages") == page) {
                                mHasMore = false;
                            }
                            JSONArray photoArr = photos.getJSONArray("photo");
                            for (int i = 0; i < photoArr.length(); i++) {
                                JSONObject itemObj = photoArr.getJSONObject(i);
                                SharkGalleryItem item = new SharkGalleryItem(
                                        itemObj.getString("id"),
                                        itemObj.getString("secret"),
                                        itemObj.getString("server"),
                                        itemObj.getString("farm")
                                );
                                result.add(item);
                            }
                        } catch (JSONException e) {

                        }

                        mAdapter.addAll(result);
                        mAdapter.notifyDataSetChanged();
                        mLoading = false;

                        //mSwipeRefreshLayout.refreshComplete(); old code
                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        request.setTag(TAG);
        mRq.add(request);
    }

    private void stopLoading() {
        if (mRq != null) {
            mRq.cancelAll(TAG);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLoading();
    }
}
