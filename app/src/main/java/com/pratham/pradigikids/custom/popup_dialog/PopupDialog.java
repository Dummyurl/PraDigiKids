package com.pratham.pradigikids.custom.popup_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pratham.pradigikids.R;

public class PopupDialog extends Dialog {
    private static final int DEFAULT_CONTENT_LAYOUT = R.layout.layout_dialog_default;
    private int mContentLayoutId = -1;
    private View mContentLayout;
    private int mGravity = Gravity.BOTTOM;
    private boolean mUseRadius = true;
    private int mWindowWidth = -1;
    private int mWindowHeight = -1;

    public PopupDialog(@NonNull Context context) {
        super(context);
    }

    public PopupDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PopupDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private int getContentLayoutId() {
        if (mContentLayoutId <= 0) {
            return DEFAULT_CONTENT_LAYOUT;
        }
        return mContentLayoutId;
    }

    protected void setContentLayout(@LayoutRes int contentLayoutId) {
        mContentLayoutId = contentLayoutId;
    }

    protected void setContentLayout(View contentLayout) {
        this.mContentLayout = contentLayout;
    }

    protected void setWindowGravity(int gravity) {
        this.mGravity = gravity;
    }

    protected void setUseRadius(boolean useRadius) {
        this.mUseRadius = useRadius;
    }

    protected void setWindowWidth(int width) {
        this.mWindowWidth = width;
    }

    protected void setWindowHeight(int height) {
        this.mWindowHeight = height;
    }

    private void configWindow() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = mGravity;
            configWindowBackground(window);
            configWindowLayoutParams(window, params);
            configWindowAnimations(window);
        }
    }

    private void configWindowLayoutParams(Window window, WindowManager.LayoutParams params) {
        params.gravity = mGravity;
        if (mGravity == Gravity.LEFT || mGravity == Gravity.RIGHT) {
            params.width = getWidthParams(WindowManager.LayoutParams.WRAP_CONTENT);
            params.height = getHeightParams(WindowManager.LayoutParams.MATCH_PARENT);
        } else if (mGravity == Gravity.TOP || mGravity == Gravity.BOTTOM) {
            params.width = getWidthParams(WindowManager.LayoutParams.MATCH_PARENT);
            params.height = getHeightParams(WindowManager.LayoutParams.WRAP_CONTENT);
        } else {
            params.width = getWidthParams(WindowManager.LayoutParams.WRAP_CONTENT);
            params.height = getHeightParams(WindowManager.LayoutParams.WRAP_CONTENT);
        }
        window.setAttributes(params);
    }

    private int getWidthParams(int defaultParams) {
        if (mWindowWidth >= 0) {
            return mWindowWidth;
        }
        return defaultParams;
    }

    private int getHeightParams(int defaultParams) {
        if (mWindowHeight >= 0) {//此时高度已被赋值
            return mWindowHeight;
        }
        return defaultParams;
    }

    private void configWindowAnimations(Window window) {
        switch (mGravity) {
            case Gravity.LEFT:
                window.setWindowAnimations(R.style.LeftDialogAnimation);
                break;
            case Gravity.RIGHT:
                window.setWindowAnimations(R.style.RightDialogAnimation);
                break;
            case Gravity.CENTER:
                //从中心弹出使用默认动画，不作额外处理
                break;
            case Gravity.TOP:
                window.setWindowAnimations(R.style.TopDialogAnimation);
                break;
            case Gravity.BOTTOM:
            default:
                window.setWindowAnimations(R.style.BottomDialogAnimation);
                break;
        }
    }

    private void configWindowBackground(Window window) {
        if (!mUseRadius) {
            window.setBackgroundDrawableResource(R.drawable.popup_dialog_bkgd);
            return;
        }
        switch (mGravity) {
            case Gravity.LEFT:
                window.setBackgroundDrawableResource(R.drawable.popup_dialog_bkgd);
                break;
            case Gravity.RIGHT:
                window.setBackgroundDrawableResource(R.drawable.popup_dialog_bkgd);
                break;
            case Gravity.CENTER:
                window.setBackgroundDrawableResource(R.drawable.popup_dialog_bkgd);
                break;
            case Gravity.TOP:
                window.setBackgroundDrawableResource(R.drawable.popup_dialog_bkgd);
                break;
            case Gravity.BOTTOM:
            default:
                window.setBackgroundDrawableResource(R.drawable.popup_dialog_bkgd);
                break;
        }
    }

    private void configContentView() {
        if (mContentLayout != null) {
            setContentView(mContentLayout);
        } else {
            setContentView(getContentLayoutId());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configContentView();
        configWindow();
    }
}
