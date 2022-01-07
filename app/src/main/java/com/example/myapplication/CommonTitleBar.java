package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.databinding.CommonTitleBarLayoutBinding;


/**
 * @author muyf
 * @brief CommonTitleBar
 * @date 2021-09-26
 */
public class CommonTitleBar extends FrameLayout {

    private CommonTitleBarLayoutBinding mBinding;
    private OnActionListener mOnActionListener;

    private String mTitle;

    public CommonTitleBar(@NonNull Context context) {
        this(context, null);
    }

    public CommonTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommonTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.common_title_bar_layout, this, true);
        mBinding.tvTitle.getPaint().setFakeBoldText(true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        String title = array.getString(R.styleable.TitleBar_title);
        int backIcon = array.getResourceId(R.styleable.TitleBar_backIcon, 0);
        boolean rightIconVisible = array.getBoolean(R.styleable.TitleBar_rightIconVisible, false);
        int rightIcon = array.getResourceId(R.styleable.TitleBar_rightIcon, 0);
        array.recycle();

        setTitle(title);
        setBackIcon(backIcon);
        setRightIconVisible(rightIconVisible);
        setRightIcon(rightIcon);
        initEvent();
    }

    /**
     *
     */
    private void initEvent() {

        mBinding.ivBack.setOnClickListener(view -> {
            if (mOnActionListener != null) {
                mOnActionListener.onBack();
            }
        });

        mBinding.ivRight.setOnClickListener(view -> {
            if (mOnActionListener != null) {
                mOnActionListener.onActionClick();
            }
        });
    }

    /**
     * 关闭Activity
     *
     * @param activity
     * @return
     */
    public CommonTitleBar setBackFinish(final Activity activity) {
        if (activity != null) {
            mBinding.ivBack.setOnClickListener(view -> {
                activity.finish();
            });
        }

        return this;
    }


    /**
     * @param title
     */
    public void setTitle(String title) {
        mBinding.tvTitle.setText(title);
    }

    public String getTitle() {
        return mTitle;
    }

    private void setBackIcon(int resId) {
        mBinding.ivBack.setImageResource(resId);
    }

    private void setRightIcon(int resId) {
        mBinding.ivRight.setImageResource(resId);
    }

    /**
     * 右边图片是否显示
     *
     * @param rightIconVisible
     */
    public void setRightIconVisible(boolean rightIconVisible) {
        mBinding.setVariable(rightIconVisible ? VISIBLE : INVISIBLE, false);
    }

    public void setEditVisible(boolean editVisible) {
        mBinding.tvTitle.setVisibility(View.GONE);
        mBinding.etSearch.setVisibility(editVisible ? VISIBLE : GONE);
    }

    public EditText getEditText() {
        return mBinding.etSearch;
    }

    public interface OnActionListener {
        /**
         * 返回按钮点击事件
         */
        void onBack();

        /**
         * 右侧按钮点击事件
         */
        void onActionClick();

    }

    public void setOnActionListener(OnActionListener listener) {
        mOnActionListener = listener;
    }
}
