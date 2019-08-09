package com.pratham.pradigikids.ui.fragment_age_group;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.permissions.KotlinPermissions;
import com.pratham.pradigikids.ui.QRLogin.QRLogin_;
import com.pratham.pradigikids.ui.connect_dialog.ConnectDialog;
import com.pratham.pradigikids.ui.fragment_admin_panel.AdminPanelFragment;
import com.pratham.pradigikids.ui.fragment_admin_panel.AdminPanelFragment_;
import com.pratham.pradigikids.ui.fragment_select_group.FragmentSelectGroup;
import com.pratham.pradigikids.ui.fragment_select_group.FragmentSelectGroup_;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

@EFragment(R.layout.fragment_select_age_group)
public class FragmentSelectAgeGroup extends Fragment {
    @ViewById(R.id.btn_admin_panel)//admin_panel
            LinearLayout admin_panel;

    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    @Click(R.id.btn_scan_qr)//scan_qr
    public void setScanQR() {
        KotlinPermissions.with(Objects.requireNonNull(getActivity()))
                .permissions(Manifest.permission.CAMERA)
                .onAccepted(permissionResult -> {
                    Intent intent = new Intent(getActivity(), QRLogin_.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                })
                .ask();
    }

    @Click(R.id.btn_3_6_yrs)//iv_age_3_to_6
    public void open3to6Groups(View view) {
        PrathamApplication.bubble_mp.start();
        int[] outLocation = new int[2];
        view.getLocationOnScreen(outLocation);
        outLocation[0] += view.getWidth() / 2;
        Bundle bundle = new Bundle();
        bundle.putBoolean(PD_Constant.GROUP_AGE_BELOW_7, true);
        bundle.putInt(PD_Constant.REVEALX, outLocation[0]);
        bundle.putInt(PD_Constant.REVEALY, outLocation[1]);
        PD_Utility.addFragment(getActivity(), new FragmentSelectGroup_(), R.id.frame_attendance,
                bundle, FragmentSelectGroup.class.getSimpleName());
    }

    @Click(R.id.btn_8_14_yrs)//iv_age_8_to_14
    public void open8to14Groups(View view) {
        PrathamApplication.bubble_mp.start();
        int[] outLocation = new int[2];
        view.getLocationOnScreen(outLocation);
        outLocation[0] += view.getWidth() / 2;
        Bundle bundle = new Bundle();
        bundle.putBoolean(PD_Constant.GROUP_AGE_BELOW_7, false);
        bundle.putInt(PD_Constant.REVEALX, outLocation[0]);
        bundle.putInt(PD_Constant.REVEALY, outLocation[1]);
        PD_Utility.addFragment(getActivity(), new FragmentSelectGroup_(), R.id.frame_attendance,
                bundle, FragmentSelectGroup.class.getSimpleName());
    }

    @Click(R.id.btn_admin_panel)//admin_panel
    public void openAdminPanel() {
        if (!PrathamApplication.wiseF.isWifiEnabled())
            PrathamApplication.wiseF.enableWifi();
        if (!PrathamApplication.wiseF.isDeviceConnectedToMobileNetwork() && !PrathamApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
            ConnectDialog connectDialog = new ConnectDialog.Builder(getActivity()).build();
            connectDialog.isDismissOnClickBack();
            connectDialog.isDismissOnTouchBackground();
            connectDialog.setOnDismissListener(popupWindow -> onActivityResult(3, Activity.RESULT_OK, null));
            connectDialog.show();
        } else {
            onActivityResult(3, Activity.RESULT_OK, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                int[] outLocation = new int[2];
                admin_panel.getLocationOnScreen(outLocation);
                outLocation[0] += admin_panel.getWidth() / 2;
                Bundle bundle = new Bundle();
                bundle.putInt(PD_Constant.REVEALX, outLocation[0]);
                bundle.putInt(PD_Constant.REVEALY, outLocation[1]);
                PD_Utility.addFragment(getActivity(), new AdminPanelFragment_(), R.id.frame_attendance,
                        bundle, AdminPanelFragment.class.getSimpleName());
            }
        }
    }

    /*@Click(R.id.btn_talk_grp)
    public void openConference() {
        Intent mActivityIntent = new Intent(getActivity(), CnferenceAct.class);
        startActivity(mActivityIntent);
    }*/
}