package com.pratham.pradigikids.ui.fragment_admin_panel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.BlurPopupDialog.BlurPopupWindow;
import com.pratham.pradigikids.custom.CircularRevelLayout;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.ui.assign.Activity_AssignGroups_;
import com.pratham.pradigikids.ui.pullData.PullDataFragment;
import com.pratham.pradigikids.ui.pullData.PullDataFragment_;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

@EFragment(R.layout.admin_panel_login)
public class AdminPanelFragment extends Fragment implements AdminPanelContract.AdminPanelView {
    @ViewById(R.id.circular_admin_reveal)
    CircularRevelLayout circular_admin_reveal;
    @ViewById(R.id.userName)
    EditText userNameET;
    @ViewById(R.id.password)
    EditText passwordET;

    @Bean(AdminPanelPresenter.class)
    AdminPanelContract.AdminPanelPresenter adminPanelPresenter;
    LottieAnimationView push_lottie;
    TextView txt_push_dialog_msg;
    TextView txt_push_error;
    BlurPopupWindow pushDialog;

    @AfterViews
    public void setViews() {
        adminPanelPresenter.setView(AdminPanelFragment.this);
        if (getArguments() != null) {
            int revealX = getArguments().getInt(PD_Constant.REVEALX, 0);
            int revealY = getArguments().getInt(PD_Constant.REVEALY, 0);
            circular_admin_reveal.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    circular_admin_reveal.getViewTreeObserver().removeOnPreDrawListener(this);
                    circular_admin_reveal.revealFrom(revealX, revealY, 0);
                    return true;
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Click(R.id.btn_login)
    public void loginCheck() {
        adminPanelPresenter.checkLogin(getUserName(), getPassword());
        Objects.requireNonNull(userNameET.getText()).clear();
        Objects.requireNonNull(passwordET.getText()).clear();
    }

    @Click(R.id.btn_push_data)
    public void pushData() {
        adminPanelPresenter.pushData();
    }

    private String getUserName() {
        String userName = Objects.requireNonNull(userNameET.getText()).toString();
        return userName.trim();
    }

    private String getPassword() {
        String password = Objects.requireNonNull(passwordET.getText()).toString();
        return password.trim();
    }

    @UiThread
    @Override
    public void openPullDataFragment() {
        PD_Utility.showFragment(getActivity(), new PullDataFragment_(), R.id.frame_attendance,
                null, PullDataFragment.class.getSimpleName());
    }

    @UiThread
    @Override
    public void onLoginFail() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Invalid Credentials");
        alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
        alertDialog.setButton("OK", (dialog, which) -> {
            userNameET.setText("");
            passwordET.setText("");
            userNameET.requestFocus();
        });
        alertDialog.show();
    }

    @UiThread
    @Override
    public void onLoginSuccess() {

        Intent intent = new Intent(getActivity(), Activity_AssignGroups_.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void DataPushedSuccessfully(EventMessage msg) {
        if (msg != null) {
            if (msg.getMessage().equalsIgnoreCase(PD_Constant.SUCCESSFULLYPUSHED)) {
                push_lottie.setAnimation("success.json");
                push_lottie.playAnimation();
                txt_push_dialog_msg.setText("Data Pushed Successfully!!");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pushDialog.dismiss();
                    }
                }, 1500);
            } else if (msg.getMessage().equalsIgnoreCase(PD_Constant.PUSHFAILED)) {
                push_lottie.setAnimation("error_cross.json");
                push_lottie.playAnimation();
                txt_push_dialog_msg.setText("Data Pushing Failed!!");
                txt_push_error.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pushDialog.dismiss();
                    }
                }, 1500);
            }
        }
    }

    @Click(R.id.img_admin_back)
    public void setAdminBack() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showPushingDialog() {
        pushDialog = new BlurPopupWindow.Builder(getContext())
                .setContentView(R.layout.app_success_dialog)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .setDismissOnClickBack(true)
                .setDismissOnTouchBackground(true)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();
        push_lottie = pushDialog.findViewById(R.id.push_lottie);
        txt_push_dialog_msg = pushDialog.findViewById(R.id.txt_push_dialog_msg);
        txt_push_error = pushDialog.findViewById(R.id.txt_push_error);
        pushDialog.show();
    }
}
