package com.app.popularmovies.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.popularmovies.AppController;
import com.app.popularmovies.R;
import com.app.popularmovies.model.MoviesResponseBean;
import com.app.popularmovies.utils.AppConstants;
import com.app.popularmovies.utils.Lg;
import com.app.popularmovies.utils.SnackBarBuilder;
import com.app.popularmovies.utils.SquareImageView;
import com.app.popularmovies.utils.Utility;
import com.app.popularmovies.webServices.ServerInteractor;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Ravind maurya on 2/21/2016.
 * <p/>
 * This class provide full detail of the movies including rating, timing, description etc.
 * In this : using images thickness color for title, movie name and their background color.They render at run time on the basic
 * Poster's thickness of color
 */
public class MovieDetailActivity extends BaseActivity implements View.OnClickListener {
    private MoviesResponseBean.MoviesResult moviesResult;

    @Override
    public int getLayoutById() {
        return R.layout.activity_movies_detail;
    }

    @Override
    public void initUi() {
        setToolBarTitle(getResources().getString(R.string.title_movie_detail));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        moviesResult = getIntent().getParcelableExtra(AppConstants.EXTRA_INTENT_PARCEL);
        final TextView movieName = (TextView) findViewById(R.id.movie_name);
        Utility.setText(movieName, moviesResult.getTitle());

        Utility.setText((TextView) findViewById(R.id.movie_desc), moviesResult.getOverview());
        String formattedDate = Utility.parseDateTime(moviesResult.getReleaseDate(), AppConstants.DATE_FORMAT1, AppConstants.DATE_FORMAT2);
        Utility.setText((TextView) findViewById(R.id.movie_release_year), formattedDate);
        Utility.setText((TextView) findViewById(R.id.movie_rating), moviesResult.getVoteAverage() + " /10");
        final SquareImageView movieImageView = (SquareImageView) findViewById(R.id.movie_image);

        TextView markFavoriteTv = (TextView) findViewById(R.id.mark_favorite);
        markFavoriteTv.setOnClickListener(this);

        if (moviesResult.getPosterPath() != null && !moviesResult.getPosterPath().isEmpty())
            Picasso.with(this)
                    .load(AppConstants.BASE_THUMB_IMAGE_URL + moviesResult.getPosterPath())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            movieImageView.setImageBitmap(bitmap);

                            // Asynchronous
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette p) {
                                    // Use generated instance
                                    Palette.Swatch vibrantSwatch = p.getVibrantSwatch();
                                    if (vibrantSwatch != null) {
                                        movieName.setTextColor(vibrantSwatch.getTitleTextColor());
                                        ((LinearLayout) movieName.getParent()).setBackgroundColor(vibrantSwatch.getRgb());
                                    }

                                    int defaultColor = getResources().getColor(R.color.colorPrimary);
                                    setToolBarColor(p.getLightVibrantColor(defaultColor));


                                    Lg.i("Palette", p.toString());
                                }
                            });


                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        getMoviesDetail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.backPressActivityAnimation(MovieDetailActivity.this);
    }

    private void getMoviesDetail() {
        if (AppController.getApplicationInstance().isNetworkConnected()) {
            showProgressDialog(false);
            HashMap<String, String> stringHashMap = new HashMap<>();
            stringHashMap.put(AppConstants.PARAM_API_KEY, AppConstants.API_KEY);

            Call<MoviesResponseBean.MoviesResult> beanCall = ServerInteractor.getInstance().getApiServices().apiMoviesDetail(moviesResult.getId(), stringHashMap);
            beanCall.enqueue(new Callback<MoviesResponseBean.MoviesResult>() {
                @Override
                public void onResponse(Response<MoviesResponseBean.MoviesResult> response, Retrofit retrofit) {
                    showProgressDialog(false);
                    MoviesResponseBean.MoviesResult moviesResult = response.body();
                    if (moviesResult != null) {
                        Utility.setText((TextView) findViewById(R.id.movie_runtime), moviesResult.getRuntime() + " min");
                        Utility.setText((TextView) findViewById(R.id.movie_tagline), moviesResult.getTagline());
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    showProgressDialog(false);
                    Lg.i("Retro", t.toString());
                }
            });
        } else {
            mSnackBar = SnackBarBuilder.make(getWindow().getDecorView(), getString(R.string.no_internet_connction))
                    .setActionText(getString(R.string.retry))
                    .onSnackBarClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMoviesDetail();
                        }
                    })
                    .build();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mark_favorite:
                mSnackBar = SnackBarBuilder.make(mParent, getString(R.string.feature_in_stage_two)).build();
                break;
        }
    }
}
