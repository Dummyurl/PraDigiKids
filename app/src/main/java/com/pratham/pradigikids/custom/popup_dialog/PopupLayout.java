package com.pratham.pradigikids.custom.popup_dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

public class PopupLayout {
    private static final String TAG = "PopupLayout";
    private PopupDialog mPopupDialog;
    private DismissListener mDismissListener;

    public static int POSITION_LEFT = Gravity.LEFT;
    public static int POSITION_RIGHT = Gravity.RIGHT;
    public static int POSITION_CENTER = Gravity.CENTER;
    public static int POSITION_TOP = Gravity.TOP;
    public static int POSITION_BOTTOM = Gravity.BOTTOM;

    private PopupLayout() {
    }

    public static PopupLayout init(Context context, @LayoutRes int contentLayoutId) {
        PopupLayout popupLayout = new PopupLayout();
        popupLayout.mPopupDialog = new PopupDialog(context);
        popupLayout.mPopupDialog.setContentLayout(contentLayoutId);
        popupLayout.initListener();
        return popupLayout;
    }

    public static PopupLayout init(Context context, View contentView) {
        PopupLayout popupLayout = new PopupLayout();
        popupLayout.mPopupDialog = new PopupDialog(context);
        popupLayout.mPopupDialog.setContentLayout(contentView);
        popupLayout.initListener();
        return popupLayout;
    }

    private void initListener() {
        mPopupDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mDismissListener != null) {
                    mDismissListener.onDismiss();
                }
            }
        });
    }

    public void setUseRadius(boolean useRadius) {
        if (mPopupDialog != null) {
            mPopupDialog.setUseRadius(useRadius);
        }
    }

    public void show() {
        show(POSITION_BOTTOM);
    }

    public void show(int position) {
        if (mPopupDialog == null) {
            Log.e(TAG, "Dialog init error,it's null");
            return;
        }
        mPopupDialog.setWindowGravity(position);
        mPopupDialog.show();
    }

    public void setHeight(int height, boolean dpMode) {
        if (dpMode) {
            mPopupDialog.setWindowHeight(dp2Px(mPopupDialog.getContext(), height));
        } else {
            mPopupDialog.setWindowHeight(height);
        }
    }

    public void setWidth(int width, boolean dpMode) {
        if (dpMode) {
            mPopupDialog.setWindowWidth(dp2Px(mPopupDialog.getContext(), width));
        } else {
            mPopupDialog.setWindowWidth(width);
        }
    }

    public void hide() {
        if (mPopupDialog != null) {
            mPopupDialog.hide();
        }

    }

    public void dismiss() {
        if (mPopupDialog != null) {
            mPopupDialog.dismiss();
        }

    }

    public void setDismissListener(DismissListener dismissListener) {
        this.mDismissListener = dismissListener;
    }

    public interface DismissListener {
        void onDismiss();
    }

    private static int dp2Px(Context context, int dp) {
        if (context == null) {
            return 0;
        }
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
