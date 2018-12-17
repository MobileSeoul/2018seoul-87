package com.hippo.jun.weandseoul;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by JUN on 2018-08-17.
 */
public class LinedEditText extends AppCompatEditText {
    private Rect mRect;
    private Paint mPaint;

    public LinedEditText(Context context) {
        super(context);
        initPaint();
    }

    public LinedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    // we need this constructor for LayoutInflater
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }
    private void initPaint() {
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x80000000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        int count = getLineCount();
        int count = (getHeight()-getPaddingTop()-getPaddingBottom()) / getLineHeight();
        Rect r = mRect;
        Paint paint = mPaint;
        getLineBounds(0, r);

        for (int i = 0; i < count; i++) {
            int baseline2 = getLineHeight() * (i+1) + getPaddingTop();
            Log.d("","onDraw: Baseline  "+baseline2+"  "+r.left+"  "+r.right+" "+count);
            canvas.drawLine(r.left, baseline2 + 1, r.right, baseline2 + 1, paint);
        }
        super.onDraw(canvas);

    }
}