package com.app.popularmovies.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Custom class to handle snackbar properties
 * Created by ravind maurya on 4/10/15.
 * Example :-
 * <pre>
 * {@code
 * SnackBarBuilder.make(findViewById(R.id.parent), "Hello World")
 *      .setActionTextColor(getResources().getColor(R.color.blue1))
 *      .setActionText("Confirm")
 *      .onSnackBarClicked(new View.OnClickListener() {
 *          @Override public void onClick(View v) {
 *              SnackBarBuilder.make(findViewById(R.id.parent), "Clicked").build();
 *          }
 *      })
 *      .build();
 * }
 * </pre>
 */
public class SnackBarBuilder {

    private final String message;
    private final View parent;
    private String actionText;
    private View.OnClickListener onClickListener;
    private int actionTextColor;

    private SnackBarBuilder(View parent, String message) {
        if (parent == null || message == null) {
            throw new NullPointerException("Please specify a parent and a message");
        }
        this.parent = parent;
        this.message = message;
    }

    /**
     * Method to make a Snackbar builder
     *
     * @param parent  Parent object of activity view
     * @param message Message to show in snackbar
     * @return An instance of SnackBarBuilder
     */
    public static SnackBarBuilder make(View parent, String message) {
        return new SnackBarBuilder(parent, message);
    }

    /**
     * Method to set action text in Snackbar
     *
     * @param actionText Action Text
     * @return An instance of SnackBarBuilder
     */
    public SnackBarBuilder setActionText(String actionText) {
        if (actionText == null || actionText.isEmpty()) {
            throw new NullPointerException("Action text cannot be null or empty");
        }
        this.actionText = actionText;
        return this;
    }

    /**
     * Method to set action text color in Snackbar
     *
     * @param actionTextColor Action Text Color
     * @return An instance of SnackBarBuilder
     */
    public SnackBarBuilder setActionTextColor(int actionTextColor) {
        this.actionTextColor = actionTextColor;
        return this;
    }

    /**
     * Method to provide a click on action text
     *
     * @param onClickListener Action Text Color
     * @return An instance of SnackBarBuilder
     */
    public SnackBarBuilder onSnackBarClicked(View.OnClickListener onClickListener) {
        if (onClickListener == null) {
            throw new NullPointerException("Click listener cannot be null");
        }
        this.onClickListener = onClickListener;
        return this;
    }

    /**
     * Method to build the class into a snackbar
     */
    public Snackbar build() {
        int length = Snackbar.LENGTH_LONG;
        Snackbar snackbar = Snackbar.make(parent, message, length);
        if (actionText != null && !actionText.isEmpty() && onClickListener != null) {
            snackbar.setAction(actionText, onClickListener);
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        }
        if (actionTextColor != 0) {
            snackbar.setActionTextColor(actionTextColor);
        }
        snackbar.show();
        return snackbar;
    }

}
