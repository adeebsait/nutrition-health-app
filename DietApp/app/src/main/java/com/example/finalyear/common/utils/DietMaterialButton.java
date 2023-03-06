package com.example.finalyear.common.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

public class DietMaterialButton extends MaterialButton {

    private boolean isLoading = false;

    private float cx = 0F;
    private float transLateableSize = 0F;
    private float staticX = 0F;
    private float cy = 0F;
    private Paint circlePaint;
    private ValueAnimator valueAnimator;

    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener;

    private int initTextColor;

    private void setLoading(boolean aBoolean) {
        isLoading = aBoolean;
        if (isLoading) {
            animateProgress();
        } else {
            valueAnimator.cancel();
        }
        setTextColor(isLoading ? Color.TRANSPARENT : initTextColor);
        setEnabled(!aBoolean);
//        setText(isLoading ? "" : btnText);
        postInvalidate();
    }

    private void setCx(float animateFloat) {
        cx = staticX + transLateableSize * animateFloat;
        postInvalidate();
    }

    private void animateProgress() {
        valueAnimator.start();
    }

    public DietMaterialButton(@NonNull Context context) {
        this(context, null, 0);
    }

    public DietMaterialButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DietMaterialButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(getCurrentTextColor());
        circlePaint.setAntiAlias(true);

        animatorUpdateListener = animation -> {
            setCx((Float) animation.getAnimatedValue());
        };

        valueAnimator = ValueAnimator.ofFloat(-0.5F, 0.5f).setDuration(600);
        DecelerateInterpolator interpolator = new DecelerateInterpolator(1.1f);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(animatorUpdateListener);

        initTextColor = getCurrentTextColor();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cx = getWidth() / 2f;
        cy = getHeight() / 2f;

        staticX = getWidth() / 2f;
        transLateableSize = staticX / 1.5F;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isLoading) {
            canvas.drawCircle(cx, cy, 10f, circlePaint);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            setLoading(false);
        }
    }

    public void startLoading() {
        setLoading(true);
    }

    public void stopLoading() {
        setLoading(false);
    }

    public void isLoading(boolean loading) {
        if (loading) startLoading();
        else stopLoading();

    }

    public Boolean isLoading() {
        return isLoading;
    }
}
