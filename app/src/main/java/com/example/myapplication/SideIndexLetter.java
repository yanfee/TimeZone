package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SideIndexLetter extends View {
    private static final String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private Paint mPaint, mTouchedPaint;
    private int mSideLetterTextSize = 16;
    private int mSideLetterNormalColor = Color.BLACK;
    private int mSideLetterTouchedColor = Color.GREEN;
    //TODO  https://blog.csdn.net/dmk877/article/details/51550031 解释getXXX()的含义
    private int mPaddingTop = getPaddingTop();
    private int mPaddingBottom = getPaddingBottom();
    private int mTopMargin =0;
    private int mWidth;
    private int mHeight;
    private int maxHeight;
    //每个字母的高度
    private int mItemHeight;
    //当前选中的字母索引值
    private int mLastIndex;
    //显示当前字母的控件
    private TextView mOverlayTextView;
    // 字母索引列表滑动监听
    private ISideIndexLetterTouchListener mListener;

    public SideIndexLetter setListener(ISideIndexLetterTouchListener listener) {
        this.mListener = listener;
        return this;
    }
    public SideIndexLetter setOverlayTextView(TextView overlayTextView){
        this.mOverlayTextView=overlayTextView;
        return this;
    }

    public SideIndexLetter(Context context) {
        this(context, null);
    }

    public SideIndexLetter(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideIndexLetter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SideIndexLetter);
        mSideLetterTextSize = array.getDimensionPixelSize(R.styleable.SideIndexLetter_sideLetterTextSize, (int) sp2px(mSideLetterTextSize));
        mSideLetterNormalColor = array.getColor(R.styleable.SideIndexLetter_sideLetterNormalColor, mSideLetterNormalColor);
        mSideLetterTouchedColor = array.getColor(R.styleable.SideIndexLetter_sideLetterTouchedColor, mSideLetterTouchedColor);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mSideLetterTextSize);
        mPaint.setColor(mSideLetterNormalColor);

        mTouchedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTouchedPaint.setTextSize(mSideLetterTextSize);
        mTouchedPaint.setColor(mSideLetterTouchedColor);

        array.recycle();
    }

    private float sp2px(int mSideLetterTextSize) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mSideLetterTextSize, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int textWidth = (int) mPaint.measureText("A");
        int width = getPaddingLeft() + getPaddingRight() + textWidth;
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mItemHeight = (mHeight - mPaddingTop - mPaddingBottom) / mLetters.length;
        for (int i = 0; i < mLetters.length; i++) {
            // 知道每个字母的中心位置 1 字母的高度一半 2 字母高度一半 + 前面字母的高度
            int letterCenterY = i * mItemHeight + mItemHeight / 2 + mPaddingTop+mTopMargin;
            Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
            //top表示基线到文字最上面的位置的距离 是个负值 bottom为基线到最下面的距离，为正值
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
            int baseLine = letterCenterY + dy;
            int singleTextWidth = (int) mPaint.measureText(mLetters[i]);
            int dx = mWidth/ 2 - singleTextWidth / 2;
            if(i==mLastIndex){
                canvas.drawText(mLetters[i], dx, baseLine, mTouchedPaint);
            }else {
                canvas.drawText(mLetters[i], dx, baseLine, mPaint);
            }

        }

    }
    /**
     * 使用EditText控件时，调起软键盘会将布局上顶，高度会发生变化，取前后的最大值为高度
     * <p>
     * bug:切换中英文时oldh和h均不为最大值
     * 这里用一个maxHeight来记录最大值
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        if (maxHeight == 0) {
            maxHeight = Math.max(h, oldh);
        }
        mHeight = Math.max(maxHeight, Math.max(h, oldh));
        mItemHeight = mHeight / mLetters.length;
        mTopMargin = (mHeight - mItemHeight * mLetters.length) / 2;
        Log.i("xht", "maxHeight==" + maxHeight + "  onSizeChanged()--w==" + w + " h==" + h + "  oldw==" + oldw + "  oldh==" + oldh
                + "  mWidth==" + mWidth + " mHeight==" + mHeight + "  mItemHeight==" + mItemHeight + "  mTopMargin==" + mTopMargin);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //TODO 如果不super.onTouchEvent(event); 设置setOnClickListener是无效的
        super.onTouchEvent(event);
        Log.e("TAG", "onTouchEvent:  execute ->"+event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int currentY = (int) event.getY();

                int mTouchedIndex = currentY / mItemHeight;
                if (mTouchedIndex < 0) {
                    mTouchedIndex = 0;
                }else if(mTouchedIndex>mLetters.length-1){
                    mTouchedIndex=mLetters.length-1;
                }
                if (mTouchedIndex != mLastIndex) {
                    mLastIndex = mTouchedIndex;
                }
                if(mListener!=null){
                    mListener.onTouchAction(mLetters[mTouchedIndex],mTouchedIndex);
                }
                if(mOverlayTextView!=null){
                    mOverlayTextView.setVisibility(View.VISIBLE);
                    mOverlayTextView.setText(mLetters[mTouchedIndex]);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(mOverlayTextView!=null){
                    mOverlayTextView.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    public interface ISideIndexLetterTouchListener{
        void  onTouchAction(String touchedLetterText,int position);
    }
}
