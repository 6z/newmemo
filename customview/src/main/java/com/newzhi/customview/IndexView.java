package com.newzhi.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 索引列表自定义视图.
 */
public class IndexView extends View {
    private int mNormalTextColor = Color.BLACK;
    private float mNormalTextDimension = 0;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private String[] mIndexStr;

    private TextView mTextViewDialog;

    private String mCurrentIndex;

    private OnIndexChangeListener mChangeListener;

    public IndexView(Context context) {
        super(context);
        init(null, 0);
    }

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.IndexView, defStyle, 0);

        mNormalTextColor = a.getColor(
                R.styleable.IndexView_textNormalColor,
                mNormalTextColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mNormalTextDimension = a.getDimension(
                R.styleable.IndexView_textNormalDimension,
                mNormalTextDimension);

        int arrayResId = -1;
        if (a.hasValue(R.styleable.IndexView_indexArraySrc)) {
            arrayResId = a.getResourceId(R.styleable.IndexView_indexArraySrc, arrayResId);
        } else {
            arrayResId = R.array.indexString;
        }
        mIndexStr = getResources().getStringArray(arrayResId);

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mNormalTextDimension);
        mTextPaint.setColor(mNormalTextColor);
        for (String item : mIndexStr) {
            float width = mTextPaint.measureText(item);
            if (width > mTextWidth) {
                mTextWidth = width;
            }
        }
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        int indexSize = mIndexStr.length;
        if (indexSize <= 0) {
            return;
        }
        float lineHeight = mTextHeight;
        if (mTextHeight * indexSize < contentHeight) {
            lineHeight = contentHeight / indexSize;
        }
        for (int index = 0; index < indexSize; index++) {
            float y = (index + 1) * lineHeight + paddingTop;
            canvas.drawText(mIndexStr[index], paddingLeft, y, mTextPaint);
        }

        // Draw the text.
        /*canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);*/

        // Draw the example drawable on top of the text.
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            float textWidth = mTextWidth;
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            float textHeight = mTextHeight * mIndexStr.length;
            height = (int) (getPaddingTop() + textHeight + getPaddingBottom());
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int indexSize = mIndexStr.length;
        if (indexSize <= 0) {
            return true;
        }
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float lineHeight = mTextHeight;
        if (mTextHeight * indexSize < contentHeight) {
            lineHeight = contentHeight / indexSize;
        }
        float y = event.getY();
        int currentPosition = (int) Math.floor((y - getPaddingTop()) / lineHeight);
        if (currentPosition < 0 || currentPosition >= indexSize) {
            return true;
        }
        //int currentPosition = (int) ((y - getPaddingTop()) / lineHeight - 1);
        mCurrentIndex = mIndexStr[currentPosition];
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                if (mTextViewDialog != null) {
                    mTextViewDialog.setVisibility(View.GONE);
                }
                break;
            default:
                setBackgroundColor(Color.parseColor("#cccccc"));
                if (currentPosition > -1 && currentPosition < mIndexStr.length) {
                    if (mTextViewDialog != null) {
                        mTextViewDialog.setVisibility(View.VISIBLE);
                        mTextViewDialog.setText(mCurrentIndex);
                    }
                    if (mChangeListener != null) {
                        mChangeListener.onChange(mCurrentIndex);
                    }
                }
                break;
        }
        invalidate();
        return true;
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getNormalTextColor() {
        return mNormalTextColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setNormalTextColor(int exampleColor) {
        mNormalTextColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getNormalTextDimension() {
        return mNormalTextDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setNormalTextDimension(float exampleDimension) {
        mNormalTextDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    public TextView getTextViewDialog() {
        return mTextViewDialog;
    }

    public void setTextViewDialog(TextView view) {
        mTextViewDialog = view;
    }

    public void setOnIndexChangeListener(OnIndexChangeListener listener) {
        mChangeListener = listener;
    }

    public String[] getIndexStringArray() {
        return mIndexStr;
    }

    public interface OnIndexChangeListener {
        void onChange(String indexString);
    }
}
