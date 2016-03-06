package com.app.popularmovies.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.app.popularmovies.R;

import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;

/**
 * Created by ravind maurya on 2/2/16.
 */
public class MaterialProgressDialog extends ProgressDialog {
    private String message;
    private Context context;

    public MaterialProgressDialog(Context context, String message) {
        super(context);
        this.context = context;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_material_progress);
        ProgressBar mCircularProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mCircularProgressBar.setIndeterminateDrawable(new IndeterminateProgressDrawable(context));
        TextView mTextView = (TextView) findViewById(R.id.progress_dialog_text);

        if (message != null && !message.isEmpty()) {
            mTextView.setText(message);
        }
    }

    @Override
    public void setMessage(CharSequence message) {
        super.setMessage(message);
    }
}
