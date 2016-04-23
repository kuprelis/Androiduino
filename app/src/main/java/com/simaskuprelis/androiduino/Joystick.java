package com.simaskuprelis.androiduino;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Joystick extends View {
    private static final String TAG = "Joystick";

    private onMoveListener mMoveListener;

    private PointF mFinger;
    private PointF mMiddle;
    private Paint mPtrPaint;
    private int mPtrRadius = 75;

    public Joystick(Context context) {
        this(context, null);
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPtrPaint = new Paint();
        mPtrPaint.setColor(getContext().getResources().getColor(R.color.colorAccent));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setPos(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                setPos(mMiddle.x, mMiddle.y);
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMiddle = new PointF(w / 2, h / 2);
        mFinger = mMiddle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mFinger.x, mFinger.y, mPtrRadius, mPtrPaint);
    }

    private void setPos(float x, float y) {
        if (x < 0) x = 0;
        else if (x > getWidth()) x = getWidth();
        if (y < 0) y = 0;
        else if (y > getHeight()) y = getHeight();
        mFinger = new PointF(x, y);
        int xp = Math.round((x - mMiddle.x) / mMiddle.x * 100);
        int yp = Math.round((mMiddle.y - y) / mMiddle.y * 100);
        mMoveListener.onMove(xp, yp);
    }

    public void setOnMoveListener(onMoveListener oml) {
        mMoveListener = oml;
    }

    public interface onMoveListener {
        void onMove(int x, int y);
    }
}
