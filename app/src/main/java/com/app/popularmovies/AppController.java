package com.app.popularmovies;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.app.popularmovies.utils.Utility;


/**
 * Created by Ravind maurya on 25/8/15.
 */
public class AppController extends Application implements Application.ActivityLifecycleCallbacks {
    private boolean mIsNetworkConnected;
    private static AppController mApplicationInstance;

    public static AppController getApplicationInstance() {
        if (mApplicationInstance == null)
            mApplicationInstance = new AppController();
        return mApplicationInstance;
    }

    /**
     * Method to tell the state of internet connectivity
     *
     * @return State of internet
     */
    public boolean isNetworkConnected() {
        return mIsNetworkConnected;
    }

    public void setIsNetworkConnected(boolean mIsNetworkConnected) {
        this.mIsNetworkConnected = mIsNetworkConnected;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        mApplicationInstance = this;
        mIsNetworkConnected = Utility.getNetworkState(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
