package com.example.finalyear.common.utils;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.example.finalyear.R;

public class DietProgressView extends AppCompatTextView {


    private Paint bgLine;
    private Paint fgLine;

    private Float arcSwipeAngle = 0f;
    private ValueAnimator swipeAngleAnimator = null;

    private float fgLineWidth;

    private float left;
    private float top;
    private float right;
    private float bottom;

    private void setAnimate(float angle, boolean animate) {
        if (!animate) {
            arcSwipeAngle = angle;
            postInvalidate();
            return;
        }
        if (swipeAngleAnimator != null && swipeAngleAnimator.isRunning()) {
            swipeAngleAnimator.cancel();
            swipeAngleAnimator = null;
        }
        swipeAngleAnimator = ValueAnimator.ofFloat(0, 1);
        swipeAngleAnimator.setDuration(1000);
        swipeAngleAnimator.addUpdateListener(animation -> {
            arcSwipeAngle = angle * (float) animation.getAnimatedValue();
            postInvalidate();
        });
        swipeAngleAnimator.start();
    }

    public DietProgressView(@NonNull Context context) {
        this(context, null, 0);
    }

    public DietProgressView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DietProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {

        TypedArray attributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.DietProgressView);

        int bgLineColor = attributes.getColor(R.styleable.DietProgressView_background_line,
                ContextCompat.getColor(getContext(), android.R.color.transparent));

        int fgLineColor = attributes.getColor(R.styleable.DietProgressView_foreground_line,
                ContextCompat.getColor(getContext(), android.R.color.transparent));

        float bgLineWidth = attributes.getDimension(R.styleable.DietProgressView_background_line_width, 0F);
        fgLineWidth = attributes.getDimension(R.styleable.DietProgressView_foreground_line_width, 20F);

        int fgProgress = attributes.getInt(R.styleable.DietProgressView_foreground_progress, 0);

        arcSwipeAngle = 3.6f * fgProgress;

        setMinHeight(dpToPixel(60));
        setMinWidth(dpToPixel(60));

        bgLine = new Paint();
        bgLine.setStyle(Paint.Style.STROKE);
        bgLine.setAntiAlias(true);
        bgLine.setStrokeWidth(dpToPixel(bgLineWidth));
        bgLine.setColor(bgLineColor);

        fgLine = new Paint();
        fgLine.setStyle(Paint.Style.STROKE);
        fgLine.setAntiAlias(true);
        fgLine.setStrokeCap(Paint.Cap.ROUND);
        fgLine.setStrokeWidth(dpToPixel(fgLineWidth));
        fgLine.setColor(fgLineColor);

        setGravity(Gravity.CENTER);

        setPadding((int) fgLineWidth*4, (int) (fgLineWidth*4),
                (int) (fgLineWidth*4), (int) (fgLineWidth*4));

        attributes.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        left = fgLineWidth*2f;
        top = fgLineWidth*2f;
        right = getWidth() - fgLineWidth*2f;
        bottom = getHeight() - fgLineWidth*2f;

    }



    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.drawArc(left, top, right, bottom, 0f, 360f, false, bgLine);
        canvas.drawArc(left, top, right, bottom, -90f, arcSwipeAngle, false, fgLine);
        super.onDraw(canvas);
    }

    public void setProgress(int progress, boolean animate) {
        setAnimate(3.6f * progress, animate);
    }

    public void setProgress(int progress) {
        setAnimate(3.6f * progress, false);
    }

    private Integer dpToPixel(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, Resources.getSystem().getDisplayMetrics()));
    }


}
