package com.app.popularmovies.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.app.popularmovies.AppController;
import com.app.popularmovies.R;
import com.app.popularmovies.adapter.MovieListAdapter;
import com.app.popularmovies.model.MoviesResponseBean;
import com.app.popularmovies.utils.AppConstants;
import com.app.popularmovies.utils.EndlessScrollListener;
import com.app.popularmovies.utils.Lg;
import com.app.popularmovies.utils.SnackBarBuilder;
import com.app.popularmovies.utils.Utility;
import com.app.popularmovies.webServices.ServerInteractor;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * This class contains all the list of movies . Also provide filter for most popular and highest rated movies.
 */
public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar mProgressBar;
    private GridView mGridView;
    private ArrayList<MoviesResponseBean.MoviesResult> moviesResultsList = new ArrayList<>();
    private int mPagination = 1;

    private MovieListAdapter mAdapter;
    private String mSortByParam = AppConstants.POPULARITY_DESC;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isSortApplied;

    @Override
    public int getLayoutById() {
        return R.layout.activity_main;
    }

    @Override
    public void initUi() {
        // set header title and title's color
        setToolBarTitle(getResources().getString(R.string.title_home));

        mGridView = (GridView) findViewById(R.id.popular_movies_gridview);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra(AppConstants.EXTRA_INTENT_PARCEL, moviesResultsList.get(position));
                startActivity(intent);
                Utility.nextActivityAnimation(MainActivity.this);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mProgressBar = Utility.getProgressBarInstance(this, R.id.circular_progress_bar);
        getMoviesList(View.VISIBLE);
    }

    private void getMoviesList(int progressBarVisibility) {
        if (AppController.getApplicationInstance().isNetworkConnected()) {
            mProgressBar.setVisibility(progressBarVisibility);

            HashMap<String, String> stringHashMap = new HashMap<>();
            stringHashMap.put(AppConstants.PARAM_SORT_BY, mSortByParam);
            stringHashMap.put(AppConstants.PARAM_API_KEY, AppConstants.API_KEY);
            stringHashMap.put(AppConstants.PARAM_PAGE, mPagination + "");

            Call<MoviesResponseBean> beanCall = ServerInteractor.getInstance().getApiServices().apiMoviesList(stringHashMap);
            beanCall.enqueue(new Callback<MoviesResponseBean>() {
                @Override
                public void onResponse(Response<MoviesResponseBean> response, Retrofit retrofit) {
                    mProgressBar.setVisibility(View.GONE);
                    MoviesResponseBean responseBean = response.body();
                    if(responseBean!=null){
                        moviesResultsList.addAll(responseBean.getResults());

                        if (mPagination == 1) {
                            mGridView.setOnScrollListener(mEndlessScrollListener);
                        }

                        if (mAdapter == null) {
                            mAdapter = new MovieListAdapter(MainActivity.this, moviesResultsList);
                            mGridView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                        if (responseBean.getResults().size() == 0) {
                            mGridView.setOnScrollListener(null);
                        }

                        if (responseBean.getResults().isEmpty())
                            Lg.i("Retro", response.toString());
                    }else {
                        mSnackBar = SnackBarBuilder.make(getWindow().getDecorView(), getString(R.string.api_key_alert)).build();
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Lg.i("Retro", t.toString());
                }
            });
        } else {
            mSnackBar = SnackBarBuilder.make(getWindow().getDecorView(), getString(R.string.no_internet_connction))
                    .setActionText(getString(R.string.retry))
                    .onSnackBarClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMoviesList(View.VISIBLE);
                        }
                    })
                    .build();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                isSortApplied = true;
                mSortByParam = AppConstants.POPULARITY_DESC;
                onRefresh();
                return true;
            case R.id.highest_rated:
                mSortByParam = AppConstants.HIGHEST_RATED;
                isSortApplied = true;
                onRefresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    EndlessScrollListener mEndlessScrollListener = new EndlessScrollListener() {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            if (AppController.getApplicationInstance().isNetworkConnected()) {
                mPagination = mPagination + 1;
                getMoviesList(View.GONE);
            } else
                mSnackBar = SnackBarBuilder.make(getWindow().getDecorView(), getString(R.string.no_internet_connction)).build();

        }
    };

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        if (AppController.getApplicationInstance().isNetworkConnected()) {
            mPagination = 1;
            mGridView.setOnScrollListener(null);
            moviesResultsList.clear();
            try {
                mAdapter.notifyDataSetChanged();
                mAdapter = null;
            } catch (Exception ignored) {
            }

            if (isSortApplied) {
                isSortApplied = false;
            } else {
                mSortByParam = AppConstants.POPULARITY_DESC;
            }
            getMoviesList(View.VISIBLE);
        } else {
            mSnackBar = SnackBarBuilder.make(getWindow().getDecorView(), getString(R.string.no_internet_connction)).build();
        }
    }
}
