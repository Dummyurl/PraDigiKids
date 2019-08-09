package com.pratham.pradigikids.ui.fragment_share_recieve;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.BlurPopupDialog.BlurPopupWindow;
import com.pratham.pradigikids.custom.CircularRevelLayout;
import com.pratham.pradigikids.custom.permissions.KotlinPermissions;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.services.LocationService;
import com.pratham.pradigikids.ui.fragment_receive.FragmentReceive;
import com.pratham.pradigikids.ui.fragment_receive.FragmentReceive_;
import com.pratham.pradigikids.ui.fragment_share.FragmentShare;
import com.pratham.pradigikids.ui.fragment_share.FragmentShare_;
import com.pratham.pradigikids.util.FileUtils;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;
import com.pratham.pradigikids.util.WifiUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EFragment(R.layout.fragment_share_receive)
public class FragmentShareRecieve extends Fragment implements CircularRevelLayout.CallBacks {

    private static final String TAG = FragmentShareRecieve.class.getSimpleName();
    private static final int SDCARD_LOCATION_CHOOSER = 100;
    private static final int SHOW_SD_CARD_DIALOG = 101;
    private static final int CREATE_HOTSPOT = 11;
    private static final int JOIN_HOTSPOT = 12;
    private static final int ANIMATE_RECIEVE_AND_INIT_CAMERA = 15;
    @ViewById(R.id.root_share)
    LinearLayout root_share;
    @ViewById(R.id.rl_receive_root)
    RelativeLayout rl_receive_root;
    @ViewById(R.id.rl_share_root)
    RelativeLayout rl_share_root;
    @ViewById(R.id.rl_share_block)
    RelativeLayout rl_share_block;
    @ViewById(R.id.rl_recieve_block)
    RelativeLayout rl_recieve_block;
    @ViewById(R.id.circular_share_reveal)
    CircularRevelLayout circular_share_reveal;

    private final List<String> mList = new ArrayList<>();
    private int revealX;
    private int revealY;
    private BlurPopupWindow sd_builder;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ANIMATE_RECIEVE_AND_INIT_CAMERA:
                    if (new LocationService(getActivity()).checkLocationEnabled()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Settings.System.canWrite(getActivity())) {
                                TransitionManager.beginDelayedTransition(root_share);
                                ViewGroup.LayoutParams params = rl_share_root.getLayoutParams();
                                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                rl_share_root.requestLayout();
                                rl_receive_root.setVisibility(View.GONE);
                                rl_share_root.setClickable(false);
                                PD_Utility.addFragment(getActivity(), new FragmentShare_(), R.id.main_frame,
                                        null, FragmentShare.class.getSimpleName());
                            } else {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                intent.setData(Uri.parse("package:" + Objects.requireNonNull(getActivity()).getPackageName()));
                                startActivityForResult(intent, JOIN_HOTSPOT);
                            }
                        } else {
                            TransitionManager.beginDelayedTransition(root_share);
                            ViewGroup.LayoutParams params = rl_share_root.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            rl_share_root.requestLayout();
                            rl_receive_root.setVisibility(View.GONE);
                            rl_share_root.setClickable(false);
                            PD_Utility.addFragment(getActivity(), new FragmentShare_(), R.id.main_frame,
                                    null, FragmentShare.class.getSimpleName());
                        }
                    } else new LocationService(getActivity()).checkLocation();
                    break;
                case PD_Constant.ApScanResult:
                    for (ScanResult wifi : WifiUtils.getScanResults()) {
                        String ssid = wifi.SSID;
                        if (ssid.startsWith(/*PD_Constant.WIFI_AP_HEADER*/"pratham")) {
                            if (!mList.contains(ssid)) {
                                mList.add(wifi.SSID);
//                                searchView.addApView(wifi);
                            }
                        }
                    }
                    break;
                case SHOW_SD_CARD_DIALOG:
                    sd_builder = new BlurPopupWindow.Builder(getContext())
                            .setContentView(R.layout.dialog_alert_sd_card)
                            .setGravity(Gravity.CENTER)
                            .setScaleRatio(0.2f)
                            .bindClickListener(v -> {
                                new Handler().postDelayed(() -> {
                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                    startActivityForResult(intent, SDCARD_LOCATION_CHOOSER);
                                }, 1500);
                                sd_builder.dismiss();
                            }, R.id.txt_choose_sd_card)
                            .setDismissOnClickBack(true)
                            .setDismissOnTouchBackground(true)
                            .setScaleRatio(0.2f)
                            .setBlurRadius(8)
                            .setTintColor(0x30000000)
                            .build();
                    sd_builder.show();
                    break;
            }
        }
    };

    @AfterViews
    public void initialize() {
        circular_share_reveal.setListener(this);
        if (getArguments() != null) {
            revealX = getArguments().getInt(PD_Constant.REVEALX, 0);
            revealY = getArguments().getInt(PD_Constant.REVEALY, 0);
            circular_share_reveal.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    circular_share_reveal.getViewTreeObserver().removeOnPreDrawListener(this);
                    circular_share_reveal.revealFrom(revealX, revealY, 0);
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

    @Click(R.id.rl_receive_root)
    public void setRl_share() {
        if (PrathamApplication.wiseF.isWifiEnabled())
            PrathamApplication.wiseF.disableWifi();
        KotlinPermissions.with(Objects.requireNonNull(getActivity()))
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .onAccepted(permissionResult -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.System.canWrite(getActivity())) {
                            ArrayList<String> sdPath = FileUtils.getExtSdCardPaths(getActivity());
                            if (sdPath.size() > 0) {
                                if (FastSave.getInstance().getString(PD_Constant.SDCARD_URI, null) == null)
                                    mHandler.sendEmptyMessage(SHOW_SD_CARD_DIALOG);
                                else animateHotspotCreation();
                            } else animateHotspotCreation();
                        } else {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + Objects.requireNonNull(getActivity()).getPackageName()));
                            startActivityForResult(intent, CREATE_HOTSPOT);
                        }
                    } else {
                        ArrayList<String> sdPath = FileUtils.getExtSdCardPaths(getActivity());
                        if (sdPath.size() > 0) {
                            if (FastSave.getInstance().getString(PD_Constant.SDCARD_URI, null) == null)
                                mHandler.sendEmptyMessage(SHOW_SD_CARD_DIALOG);
                            else animateHotspotCreation();
                        } else animateHotspotCreation();
                    }
                })
                .ask();
    }

    @UiThread
    public void animateHotspotCreation() {
        TransitionManager.beginDelayedTransition(root_share);
        ViewGroup.LayoutParams params = rl_receive_root.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rl_receive_root.requestLayout();
        rl_share_root.setVisibility(View.GONE);
        rl_receive_root.setClickable(false);
        rl_share_block.setVisibility(View.GONE);
        mHandler.postDelayed(() -> PD_Utility.addFragment(getActivity(), new FragmentReceive_(), R.id.main_frame
                , null, FragmentReceive.class.getSimpleName()), 800);
    }

    @Click(R.id.rl_share_root)
    public void setRl_recieve() {
        WifiUtils.closeWifiAp();
        if (!PrathamApplication.wiseF.isWifiEnabled()) PrathamApplication.wiseF.enableWifi();
        KotlinPermissions.with(Objects.requireNonNull(getActivity()))
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .onAccepted(permissionResult -> {
                    mHandler.sendEmptyMessage(ANIMATE_RECIEVE_AND_INIT_CAMERA);
                })
                .ask();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SDCARD_LOCATION_CHOOSER) {
            if (data != null && data.getData() != null) {
                Uri treeUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                PrathamApplication.getInstance().getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
                FastSave.getInstance().saveString(PD_Constant.SDCARD_URI, treeUri.toString());
            }
        }/*  else if (requestCode == CREATE_HOTSPOT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(getActivity()))
                    animateHotspotCreation();
                else
                    Toast.makeText(getActivity(), "Hotspot Permission Is required", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == JOIN_HOTSPOT) {
            setRl_recieve();
        }*/
    }

    @Subscribe
    public void messageRecievedInShare(EventMessage message) {
        if (message != null) {
            /*if (message.getMessage().equalsIgnoreCase(PD_Constant.CLOSE_FTP_SERVER))
                disconnectFTP();
            else */
            if (message.getMessage().equalsIgnoreCase(PD_Constant.FRAGMENT_SHARE_BACK)) {
                TransitionManager.beginDelayedTransition(root_share);
                ViewGroup.LayoutParams params = rl_share_root.getLayoutParams();
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.width = 0;
                rl_receive_root.setVisibility(View.VISIBLE);
                rl_share_root.requestLayout();
                rl_share_root.setClickable(true);
            }
        }
    }

    @UiThread
    public void closeFTPJoin() {
        onUnRevealed();
    }

    @Override
    public void onRevealed() {

    }

    @UiThread
    @Override
    public void onUnRevealed() {
    }
}
