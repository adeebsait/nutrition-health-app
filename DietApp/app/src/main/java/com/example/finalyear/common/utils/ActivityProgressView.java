package com.example.finalyear.common.utils;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.example.finalyear.R;

public class ActivityProgressView extends AppCompatTextView {


    private Paint bgLine;
    private Paint fgLine;

    private float arcSwipeAngle = 1f;
    private ValueAnimator swipeAngleAnimator = null;

    private float fgLineWidth;

    private float left;
    private float top;
    private float right;
    private float bottom;
    private float middle;

    private TextPaint txtPaint;
    private boolean isRunning = false;
    private ActivityProgressViewClick onClickListener;

    private float max = 100;

    private SpannableString prefixSpannable;

    private SpannableString suffixSpannable;

    private String mainText;

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

    public ActivityProgressView(@NonNull Context context) {
        this(context, null, 0);
    }

    public ActivityProgressView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActivityProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {


        TypedArray attributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ActivityProgressView);

        int bgLineColor = attributes.getColor(R.styleable.ActivityProgressView_background_line,
                ContextCompat.getColor(getContext(), android.R.color.transparent));

        int fgLineColor = attributes.getColor(R.styleable.ActivityProgressView_foreground_line,
                ContextCompat.getColor(getContext(), android.R.color.transparent));

        float bgLineWidth = attributes.getDimension(R.styleable.ActivityProgressView_background_line_width, 0F);
        fgLineWidth = attributes.getDimension(R.styleable.ActivityProgressView_foreground_line_width, 20F);

        int fgProgress = attributes.getInt(R.styleable.ActivityProgressView_foreground_progress, 0);

        arcSwipeAngle = 300f / max * fgProgress;

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

        setPadding((int) fgLineWidth * 4, (int) (fgLineWidth * 4),
                (int) (fgLineWidth * 4), (int) (fgLineWidth * 4));

        txtPaint = new TextPaint();
        txtPaint.setAntiAlias(true);
        txtPaint.setTextSize(getTextSize());
        txtPaint.setTextAlign(Paint.Align.CENTER);

        attributes.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        left = fgLineWidth * 2f;
        top = fgLineWidth * 2f;
        right = getWidth() - fgLineWidth * 2f;
        bottom = getHeight() - fgLineWidth * 2f;

        middle = getWidth() / 2f;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        mainText = text == null || text.length() < 1 ? "-" : text.toString();

        SpannableString mainTextSpannable = new SpannableString(mainText);
        mainTextSpannable.setSpan(new RelativeSizeSpan(2.0f), 0, mainText.length(), 0);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (prefixSpannable != null) builder.append(prefixSpannable);
        builder.append(mainTextSpannable);
        if (suffixSpannable != null) builder.append(suffixSpannable);
        super.setText(builder, type);

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.drawArc(left, top, right, bottom, -240f, 300f, false, bgLine);
        canvas.drawArc(left, top, right, bottom, -240f, arcSwipeAngle, false, fgLine);
        if (isRunning) {
            canvas.drawText("Running", middle, bottom, txtPaint);
        }
        super.onDraw(canvas);
    }

    public void setPrefix(String prefix) {
        SpannableString string = new SpannableString(prefix + "\n");
        string.setSpan(new RelativeSizeSpan(0.8f), 0, string.length(), 0);
        prefixSpannable = string;
        setText(mainText);
    }

    public void setSuffix(String suffix) {
        SpannableString string = new SpannableString("\n" + suffix);
        string.setSpan(new RelativeSizeSpan(0.8f), 0, string.length(), 0);
        suffixSpannable = string;
        setText(mainText);
    }

    public void setProgressValue(int progress, boolean animate) {
        if (progress == 0) progress = 1;
        setAnimate(300f / max * progress, animate);
    }

    public void setProgressValue(int progress) {
        if (progress == 0) progress = 1;
        setAnimate(300f / max * progress, false);
    }

    public void setMax(int maxValue) {
        if (maxValue < 1) return;
        this.max = maxValue;
        postInvalidate();
    }

    public void playPr() {
        if (isRunning) return;
        isRunning = true;
        postInvalidate();
    }

    public void pausePr() {
        if (!isRunning) return;
        isRunning = false;
        postInvalidate();
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    public void setOnClickListener(ActivityProgressViewClick l) {
        super.setOnClickListener(l);
        this.onClickListener = l;
    }

    @Override
    public boolean performClick() {
        onClickListener.onClick(isRunning);
        if (isRunning) pausePr();
        else playPr();
        return super.performClick();
    }

    public interface ActivityProgressViewClick extends View.OnClickListener {
        void onClick(boolean isRunning);

        @Override
        default void onClick(View v) {
        }

        ;
    }

    private Integer dpToPixel(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, Resources.getSystem().getDisplayMetrics()));
    }


}
