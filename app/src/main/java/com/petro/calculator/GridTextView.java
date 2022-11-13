package com.petro.calculator;

import android.content.Context;
import android.util.AttributeSet;

public class GridTextView extends androidx.appcompat.widget.AppCompatTextView {

    public GridTextView(Context context) {
        super(context);
    }

    public GridTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}