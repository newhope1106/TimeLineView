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

    /**顶部时间球*/
    private Drawable mTopTimeBall;
    /**底部时间球*/
    private Drawable mBottomTimeBall;
    /**顶部时间球到view的支线*/
    private Drawable mStartLine;
    /**顶部时间球到底部的直线*/
    private Drawable mEndLine;
    /**时间球大小*/
    private int mBallSize;
    /**直线宽度*/
    private int mLineWidth;
    /**顶部时间球中心距离顶部的距离*/
    private int mBallCenterMarginTop;

    private Rect mBounds;
    private Context mContext;

    /**顶部直线是否可见*/
    private boolean mIsTopLineVisible = false;
    /**底部时间球是否可见*/
    private boolean mIsBottomBallVisible = false;

    public TimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        /*获取xml中设置的属性值*/
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.timeline_style);
        mTopTimeBall = typedArray.getDrawable(R.styleable.timeline_style_ball);
        mBottomTimeBall = typedArray.getDrawable(R.styleable.timeline_style_ball);
        mStartLine = typedArray.getDrawable(R.styleable.timeline_style_line);
        mEndLine = typedArray.getDrawable(R.styleable.timeline_style_line);
        mBallSize = typedArray.getDimensionPixelSize(R.styleable.timeline_style_ballSize, 25);
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.timeline_style_lineWidth, 2);
        mBallCenterMarginTop = typedArray.getDimensionPixelSize(R.styleable.timeline_style_ballCenterMargin, 0);
        typedArray.recycle();

        /*没有设置drawable属性值，则采用缺省值*/
        if(mTopTimeBall == null) {
            mTopTimeBall = mContext.getResources().getDrawable(R.drawable.ball);
        }

        if(mBottomTimeBall == null) {
            mBottomTimeBall = mContext.getResources().getDrawable(R.drawable.ball);
        }

        if(mStartLine == null) {
            mStartLine = mContext.getResources().getDrawable(R.drawable.time_line);
        }

        if(mEndLine == null) {
            mEndLine = mContext.getResources().getDrawable(R.drawable.time_line);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = mBallSize + getPaddingLeft() + getPaddingRight();
        int h = mBallSize + getPaddingTop() + getPaddingBottom();

        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        setMeasuredDimension(widthSize, heightSize);
        initTimeLineView();
    }

    /**
     * 初始化时间轴控件的位置大小等
     * */
    private void initTimeLineView() {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth();// Width of current custom view
        int height = getHeight();

        /*有效的宽度*/
        int effectWidth = width - paddingLeft - paddingRight;
        /*有效高度*/
        int effectHeight = height - paddingTop - paddingBottom;

        /*时间球实际大小不能大于有效宽高度*/
        int ballSize = Math.min(mBallSize, Math.min(effectWidth, effectHeight));

        /*顶部时间球位置*/
        if(mTopTimeBall != null) {
            mTopTimeBall.setBounds(paddingLeft, paddingTop + mBallCenterMarginTop,
                    paddingLeft + ballSize, paddingTop + ballSize + mBallCenterMarginTop);
            mBounds = mTopTimeBall.getBounds();
        } else {
            mBounds = new Rect(paddingLeft, 0, paddingLeft + ballSize, 0);
        }

        int centerX = mBounds.centerX();
        int lineLeft = centerX - (mLineWidth >> 1);

        if(mIsTopLineVisible && mStartLine!=null) {
            mStartLine.setBounds(lineLeft, 0, mLineWidth + lineLeft, mBounds.top);
        }

        if(mEndLine != null) {
            mEndLine.setBounds(lineLeft, mBounds.bottom, mLineWidth + lineLeft, height);
        }

        if(mIsBottomBallVisible && mBottomTimeBall != null){
            mBottomTimeBall.setBounds(paddingLeft,height - paddingBottom - ballSize,
                    paddingLeft + ballSize, height - paddingBottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mTopTimeBall != null) {
            mTopTimeBall.draw(canvas);
        }

        if(mIsTopLineVisible && mStartLine != null) {
            mStartLine.draw(canvas);
        }

        if(mEndLine != null) {
            mEndLine.draw(canvas);
        }

        if(mIsBottomBallVisible && mBottomTimeBall != null) {
            mBottomTimeBall.draw(canvas);
        }
    }

    /**
     * 设置顶部时间球
     * */
    public void setTopTimeBallDrawable(Drawable ball) {
        mTopTimeBall = ball;
        initTimeLineView();
    }

    /**
     * 设置底部时间球
     * */
    public void setBottomTimeDrawable(Drawable ball) {
        mBottomTimeBall = ball;

        initTimeLineView();
    }

    /**
     * 时间球大小
     * */
    public void setTimeBallSize(int ballSize) {
        mBallSize = ballSize;

        initTimeLineView();
    }

    /**
     * 时间球中心位于顶部的距离
     * */
    public void setBallMarginTop(int top) {
        mBallCenterMarginTop = top;

        initTimeLineView();
    }

    /**
     * 时间轴宽度
     * */
    public void setLineWidth(int width) {
        mLineWidth = width;

        initTimeLineView();
    }

    /**
     * 顶部时间球是否可见
     * */
    public void setTopLineVisible(boolean visible) {
        mIsTopLineVisible = visible;
    }

    /**
     * 底部时间球是否可见
     * */
    public void setBottomBallVisible(boolean visible) {
        mIsBottomBallVisible = visible;
    }
}
