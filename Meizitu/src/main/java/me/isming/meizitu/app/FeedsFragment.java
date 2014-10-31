package me.isming.meizitu.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import me.isming.meizitu.App;
import me.isming.meizitu.adapter.CardsAnimationAdapter;
import me.isming.meizitu.adapter.FeedsAdapter;
import me.isming.meizitu.dao.FeedsDataHelper;
import me.isming.meizitu.data.GsonRequest;
import me.isming.meizitu.model.Feed;
import me.isming.meizitu.util.ActionBarUtils;
import me.isming.meizitu.util.CLog;
import me.isming.meizitu.util.ListViewUtils;
import me.isming.meizitu.util.TaskUtils;
import me.isming.meizitu.view.LoadingFooter;
import me.isming.meizitu.view.PageListView;

import java.util.ArrayList;


/**
 * Created by Sam on 14-3-25.
 */
public class FeedsFragment extends BaseFragment implements  LoaderManager.LoaderCallbacks<Cursor>,SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    SwipeRefreshLayout mSwipeLayout;

    PageListView mListView;

    private FeedsDataHelper mDataHelper;
    private FeedsAdapter mAdapter;
    private int mMaxId = 0;
    private int mSinceId = 0;
    private String mString = "http://www.ourhfuu.com/meizitu.php";

    public static FeedsFragment newInstance(int sectionNumber) {
        FeedsFragment fragment = new FeedsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_feed, container, false);

        mListView = (PageListView)contentView.findViewById(R.id.listView);
        mSwipeLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_container);

        mDataHelper = new FeedsDataHelper(App.getContext());
        getLoaderManager().initLoader(0, null, this);
        mAdapter = new FeedsAdapter(getActivity(), mListView);
        View header = new View(getActivity());
        mListView.addHeaderView(header);
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);
        mListView.setLoadNextListener(new PageListView.OnLoadNextListener() {
            @Override
            public void onLoadNext() {
                loadNextData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int actualPosition = position - mListView.getHeaderViewsCount();
                if(actualPosition<0) {
                    return;
                }
                Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                Feed feed = mAdapter.getItem(position-mListView.getHeaderViewsCount());
                intent.putExtra(ImageViewActivity.IMAGE_NAME, feed.getName());
                intent.putStringArrayListExtra(ImageViewActivity.IMAGE_URL, feed.getImgs());
                startActivity(intent);
            }
        });

        initActionBar();
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(R.color.holo_blue_light,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);

        return contentView;
    }

    private void initActionBar() {
        //View actionBarContainer = ActionBarUtils.findActionBarContainer(getActivity());
//        if(actionBarContainer == null) {
//            CLog.i("actionBarContainer为空，直接返回了");
//            return;
//        }
//
//        actionBarContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ListViewUtils.smoothScrollListViewToTop(mListView);
//            }
//        });
    }

    private String getRefreshUrl() {
        return mString + "?max_id=" + mMaxId;
    }

    private String getNextUrl() {
        return mString + "?since_id=" + mSinceId;
    }


    private void loadNextData() {
        if (!mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(true);
        }
        CLog.d("NExt"+getNextUrl());
        executeRequest(new GsonRequest(getNextUrl(), Feed.FeedRequestData.class, responseListener(), errorListener()));
    }

    private void refreshData() {
        if (!mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(true);
        }
        CLog.d("Refresh"+getRefreshUrl());
        executeRequest(new GsonRequest(getRefreshUrl(), Feed.FeedRequestData.class, responseListener(), errorListener()));
    }

    private Response.Listener<Feed.FeedRequestData> responseListener() {
        return new Response.Listener<Feed.FeedRequestData>() {
            @Override
            public void onResponse(final Feed.FeedRequestData response) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        ArrayList<Feed> feeds = response.data;
                        if(feeds != null && feeds.size()>0) {
                            mDataHelper.bulkInsert(feeds);
                            int num1 = feeds.get(0).getId();
                            int num2 = feeds.get(feeds.size()-1).getId();
                            if(num1>mMaxId) {
                                mMaxId = num1;
                            }
                            if(mSinceId == 0|| num1<mSinceId) {
                                mSinceId = num1;
                            }
                            if(num2>mMaxId) {
                                mMaxId = num2;
                            }
                            if(mSinceId == 0|| num2<mSinceId) {
                                mSinceId = num2;
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                            mSwipeLayout.setRefreshing(false);
                            mListView.setState(LoadingFooter.State.Idle, 3000);
                    }
                });
            }
        };
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(App.getContext(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                mSwipeLayout.setRefreshing(false);
                mListView.setState(LoadingFooter.State.Idle, 3000);
            }
        };
    }

    public void scrollTopAndRefresh() {
        if (mListView != null) {
            ListViewUtils.smoothScrollListViewToTop(mListView);
            refreshData();
        }
    }


    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        if (data != null && data.getCount() == 0) {
            refreshData();
        } else {
            int num1 = mAdapter.getItem(mAdapter.getCount() -1 ).getId();
            int num2 = mAdapter.getItem(0).getId();
            if(num1 > num2) {
                mMaxId = num1;
                mSinceId = num2;
            } else {
                mMaxId = num2;
                mSinceId = num1;
            }
            CLog.d(num1+""+num2);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((AppMainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
