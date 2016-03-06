package com.app.popularmovies.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.app.popularmovies.R;


/**
 * Custom logger class to show logs for this app
 */
public class Lg {
    /**
     * This flag is to toggle showing logs in the monitor.
     */
    public static boolean DEBUG = true;

    public static void i(String tag, String msg) {
        if (DEBUG)
            if (msg != null)
                Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            if (msg != null)
                Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            if (msg != null)
                Log.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            if (msg != null)
                Log.w(tag, msg);
    }

    /**
     * Method to show toast in development phase.
     * Throws NullPointerException if message is null
     *
     * @param context Context of the calling class
     * @param message Message to show
     */
    public static Toast showToast(Context context, String message) {
        Toast toast;
        if (DEBUG && !TextUtils.isEmpty(message)) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
        } else
            throw new NullPointerException(context.getString(R.string.message_context_cannot_null));
        return toast;
    }
}
