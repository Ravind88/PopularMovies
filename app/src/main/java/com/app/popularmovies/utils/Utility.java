package com.app.popularmovies.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.popularmovies.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;

/**
 * Created by ravind maurya on 8/2/16.
 */
public class Utility {

    /**
     * Static method to get an instance of material styled progress bar
     *
     * @param mContext Context of the calling class
     * @param resId    Resource Id of the progress bar
     * @return An instance of MaterialProgressBar
     */
    public static ProgressBar getProgressBarInstance(Context mContext, int resId) {

        ProgressBar nonBlockingProgressBar = (ProgressBar) ((Activity) mContext).getWindow().findViewById(resId);
        if (nonBlockingProgressBar != null)
            nonBlockingProgressBar.setIndeterminateDrawable(new IndeterminateProgressDrawable(mContext));
        return nonBlockingProgressBar;
    }

    /**
     * Static method to get an instance of material styled progress dialog
     *
     * @param mContext Context of the calling class
     * @return An instance of MaterialProgressDialog
     */
    public static MaterialProgressDialog getProgressDialogInstance(Context mContext) {
        MaterialProgressDialog mProgressDialog = new MaterialProgressDialog(mContext,
                mContext.getString(R.string.please_wait));
        mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }

    /**
     * Method to parse date from server
     *
     * @param date         Date from the server
     * @param sourceFormat Format of the date from server
     * @param targetFormat Target format in which to return the date
     * @return Formatted date
     */
    public static String parseDateTime(String date, String sourceFormat, String targetFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(sourceFormat);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date strDate = sdf.parse(date);

            SimpleDateFormat sdf2 = new SimpleDateFormat(targetFormat);
            sdf2.setTimeZone(TimeZone.getDefault());
            return sdf2.format(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * Static method to check network availability
     *
     * @param context Context of the calling class
     */

    public static boolean getNetworkState(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Method to set text to a textview
     *
     * @param mTextView Textview in which to set text
     * @param text      The text to set in the widget
     */
    public static void setText(TextView mTextView, String text) {
        try {
            if (text != null)
                mTextView.setText(text.trim());
        } catch (Exception ignored) {
        }

    }

    /**
     * Method to set back animation from left to right
     *
     * @param activity Activity  in which back button is called
     */
    public static void backPressActivityAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_out_from_left,
                R.anim.slide_in_to_left);
        activity.finish();
    }
    /**
     * Method to set back animation from left to right
     *
     * @param activity Activity  in which start the another activity
     */
    public static void nextActivityAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
    }




}