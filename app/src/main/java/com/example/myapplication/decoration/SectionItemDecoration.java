package com.example.myapplication.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.TimeZoneBean;

import java.util.List;

public class SectionItemDecoration extends RecyclerView.ItemDecoration {
    private List<TimeZoneBean> mData;
    private Paint mBgPaint;
    private TextPaint mTextPaint;
    private Rect mBounds;

    private int mSectionHeight;
    private int mBgColor;
    private int mTextColor;
    private int mTextSize;

    private Context mContext;

    public SectionItemDecoration(Context context, List<TimeZoneBean> data) {
        this.mData = data;
        this.mContext = context;

        mBgColor = mContext.getResources().getColor(R.color.section_bg);
        mSectionHeight = (int) mContext.getResources().getDimension(R.dimen.section_height);
        mTextSize = mContext.getResources().getDimensionPixelSize(R.dimen.section_text_size);
        mTextColor = Color.BLACK;

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(mBgColor);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        mBounds = new Rect();
    }

    public void setData(List<TimeZoneBean> data) {
        this.mData = data;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (mData != null && !mData.isEmpty() && position <= mData.size() - 1 && position > -1) {
//                if (position == 0) {
//                    // ?????????section????????????????????????
//                    drawSection(c, left, right, child, params, position);
//                } else {
//                    // ????????????????????????????????????????????????????????????section??????????????????
//                    if (null != mData.get(position).getIndexTag() && !mData.get(position).getIndexTag().equals(mData.get(position - 1).getIndexTag())) {
//                        drawSection(c, left, right, child, params, position);
//                    }
//                }
            }
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param c
     * @param left
     * @param right
     * @param child
     * @param params
     * @param position
     */
    private void drawSection(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {
        // ??????????????????
        c.drawRect(left, child.getTop() - params.topMargin - mSectionHeight, right,
                child.getTop() - params.topMargin, mBgPaint);
        mTextPaint.getTextBounds(mData.get(position).getIndexTag(), 0, mData.get(position).getIndexTag().length(),
                mBounds);
        c.drawText(mData.get(position).getIndexTag(),
                child.getPaddingLeft(),// child.getPaddingLeft()??????????????????item??????????????????paddingLeft??????
                child.getTop() - params.topMargin - (mSectionHeight / 2 - mBounds.height() / 2),
                mTextPaint);
    }

    /**
     * ??????????????????
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (pos < 0)
            return;
        if (mData == null || mData.isEmpty())
            return;
        String section = mData.get(pos).getIndexTag();
        View child = parent.findViewHolderForLayoutPosition(pos).itemView;

        boolean flag = false;//????????????flag???Canvas????????????????????????
        if ((pos + 1) < mData.size()) {
            if (null != section && !section.equals(mData.get(pos + 1).getIndexTag())) {
                if (child.getHeight() + child.getTop() < mSectionHeight) {
                    c.save();//??????????????? ????????????Canvas??????
                    flag = true;
                    //???????????????canvas?????? ???y???????????? ,????????????canvas ????????????Rect???Text????????????????????????????????????????????????
                    c.translate(0, child.getHeight() + child.getTop() - mSectionHeight);
                }
            }
        }
//        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(),
//                parent.getPaddingTop() + mSectionHeight, mBgPaint);
//        mTextPaint.getTextBounds(section, 0, section.length(), mBounds);
//        c.drawText(section, child.getPaddingLeft(),
//                parent.getPaddingTop() + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2),
//                mTextPaint);
        if (flag) {
            c.restore();
        }

    }

    /**
     * ????????????????????? ?????????itemView??????padding?????????
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (mData != null && !mData.isEmpty() && position <= mData.size() - 1 && position > -1) {
//            if (position == 0) {
//                // ?????????????????????item??????????????????sectionHeight??????
//                outRect.set(0, mSectionHeight, 0, 0);
//            } else {
//                if (null != mData.get(position).getIndexTag()
//                        && !mData.get(position).getIndexTag().equals(mData.get(position - 1).getIndexTag())) {
//                    // ??????????????????????????????item??????????????????item???section??????????????????????????????sectionHeight??????
//                    outRect.set(0, mSectionHeight, 0, 0);
//                }
//            }
        }
    }
}
