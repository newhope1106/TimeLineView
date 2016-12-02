package cn.appleye.timelineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author appleye<liuliaopu>
 * @date 2016-12-02
 * 时间轴控件
 */

public class TimeLineView extends View {

    private Drawable mMarker;
    private Drawable mBottomMarker;
    private Drawable mStartLine;
    private Drawable mEndLine;
    private int mMarkerSize;
    private int mLineSize;
    private int mMarkTop;//时间球位于顶部的距离

    private Rect mBounds;
    private Context mContext;

    private boolean mIsFistView = false;
    private boolean mIsLastView = false;

    public TimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);

        setBackgroundColor(0xffb200);
        setWillNotDraw(false);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.timeline_style);
        mMarker = typedArray.getDrawable(R.styleable.timeline_style_marker);
        mBottomMarker = typedArray.getDrawable(R.styleable.timeline_style_marker);
        mStartLine = typedArray.getDrawable(R.styleable.timeline_style_line);
        mEndLine = typedArray.getDrawable(R.styleable.timeline_style_line);
        mMarkerSize = typedArray.getDimensionPixelSize(R.styleable.timeline_style_marker_size, 25);
        mLineSize = typedArray.getDimensionPixelSize(R.styleable.timeline_style_line_size, 2);
        mMarkTop = typedArray.getDimensionPixelSize(R.styleable.timeline_style_marker_top, 0);
        typedArray.recycle();

        if(mMarker == null) {
            mMarker = mContext.getResources().getDrawable(R.drawable.marker);
        }

        if(mBottomMarker == null) {
            mBottomMarker = mContext.getResources().getDrawable(R.drawable.marker);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Width measurements of the width and height and the inside view of child controls
        int w = mMarkerSize + getPaddingLeft() + getPaddingRight();
        int h = mMarkerSize + getPaddingTop() + getPaddingBottom();

        // Width and height to determine the final view through a systematic approach to decision-making
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        setMeasuredDimension(widthSize, heightSize);
        initDrawable();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // When the view is displayed when the callback
        // Positioning Drawable coordinates, then draw
        initDrawable();
    }

    private void initDrawable() {
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();// Width of current custom view
        int height = getHeight();

        int cWidth = width - pLeft - pRight;// Circle width
        int cHeight = height - pTop - pBottom;

        int markSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));

        if(mMarker != null) {
            mMarker.setBounds(pLeft, pTop + mMarkTop, pLeft + markSize, pTop + markSize + mMarkTop);
            mBounds = mMarker.getBounds();
        }

        int centerX = mBounds.centerX();
        int lineLeft = centerX - (mLineSize >> 1);

        if(!mIsFistView && mStartLine!=null) {
            mStartLine.setBounds(lineLeft, 0, mLineSize + lineLeft, mBounds.top);
        }

        if(mEndLine != null) {
            mEndLine.setBounds(lineLeft, mBounds.bottom, mLineSize + lineLeft, height);
        }

        if(mIsLastView && mBottomMarker != null){
            mBottomMarker.setBounds(pLeft,height - pBottom - markSize,pLeft + markSize,pTop + height - pBottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mMarker != null) {
            mMarker.draw(canvas);
        }

        if(!mIsFistView && mStartLine != null) {
            mStartLine.draw(canvas);
        }

        if(mEndLine != null) {
            mEndLine.draw(canvas);
        }

        if(mIsLastView && mBottomMarker != null) {
            mBottomMarker.draw(canvas);
        }
    }

    public void setMarker(Drawable marker) {
        mMarker = marker;
        initDrawable();
    }

    /**
     * 时间轴圆圈大小
     * */
    public void setMarkerSize(int markerSize) {
        mMarkerSize = markerSize;
    }

    /**
     * 时间轴圆圈中心位于顶部的距离
     * */
    public void setMarkTop(int markTop) {
        mMarkTop = markTop;
    }

    /**
     * 时间轴宽度
     * */
    public void setLineSize(int lineSize) {
        mLineSize = lineSize;
    }

    /**
     * 时间轴第一个圆圈是否可见
     * */
    public void isFirstView(boolean isFirst) {
        mIsFistView = isFirst;
    }

    /**
     * 时间轴第二个(底部)圆圈是否可见
     * */
    public void isLastView(boolean isLast) {
        mIsLastView = isLast;
    }

    public void invalidateIfNeed() {
        initDrawable();
        invalidate();
    }
}
