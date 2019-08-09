package com.pratham.pradigikids.ui.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.pratham.pradigikids.BaseActivity;
import com.pratham.pradigikids.BuildConfig;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.BlurPopupDialog.BlurPopupWindow;
import com.pratham.pradigikids.custom.NotificationBadge;
import com.pratham.pradigikids.custom.flexbox.FlexDirection;
import com.pratham.pradigikids.custom.flexbox.FlexboxLayoutManager;
import com.pratham.pradigikids.custom.flexbox.JustifyContent;
import com.pratham.pradigikids.custom.permissions.KotlinPermissions;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.custom.spotlight.SpotlightView;
import com.pratham.pradigikids.ftpSettings.FsService;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.models.Modal_NavigationMenu;
import com.pratham.pradigikids.ui.connect_dialog.ConnectDialog;
import com.pratham.pradigikids.ui.download_list.DownloadListFragment;
import com.pratham.pradigikids.ui.download_list.DownloadListFragment_;
import com.pratham.pradigikids.ui.fragment_content.ContentContract;
import com.pratham.pradigikids.ui.fragment_content.FragmentContent_;
import com.pratham.pradigikids.ui.fragment_language.FragmentLanguage;
import com.pratham.pradigikids.ui.fragment_language.FragmentLanguage_;
import com.pratham.pradigikids.ui.fragment_receive.FragmentReceive_;
import com.pratham.pradigikids.ui.fragment_share.FragmentShare_;
import com.pratham.pradigikids.ui.fragment_share_recieve.FragmentShareRecieve;
import com.pratham.pradigikids.ui.fragment_share_recieve.FragmentShareRecieve_;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import static com.pratham.pradigikids.async.PD_ApiRequest.downloadAajKaSawal;

@EActivity(R.layout.main_activity_two)
public class ActivityMain extends BaseActivity implements ContentContract.mainView, ContractMenu,
        SlidingPaneLayout.PanelSlideListener {

    private static final String TAG = ActivityMain.class.getSimpleName();
    private static final int INITILIZE_DRAWER = 1;
    private static final int MENU_LANGUAGE = 2;
    private static final int MENU_CONNECT_WIFI = 3;
    private static final int MENU_SHARE = 4;
    private static final int MENU_SHARE_APP = 5;
    private static final int MENU_EXIT = 6;
    private static final int MENU_HOME = 7;
    private static final int SHOW_MENU_WITH_DEEP_LINK = 8;
    private static final int CHECK_AAJ_KA_SAWAL = 11;
    @ViewById(R.id.download_notification)
    NotificationBadge download_notification;
    @ViewById(R.id.download_badge)
    RelativeLayout download_badge;
    @ViewById(R.id.outer_area)
    View outer_area;
    @ViewById(R.id.main_hamburger)
    ImageView main_hamburger;
    @ViewById(R.id.main_sliding_drawer)
    SlidingPaneLayout main_sliding_drawer;
    @ViewById(R.id.drawer_profile_lottie)
    LottieAnimationView drawer_profile_lottie;
    @ViewById(R.id.drawer_profile_name)
    TextView drawer_profile_name;
    @ViewById(R.id.rv_drawer)
    RecyclerView rv_drawer;
    @ViewById(R.id.main_nav)
    MaterialCardView main_nav;

    private boolean isChecked;
    private BlurPopupWindow exitDialog;
    private DownloadListFragment_ downloadListFragment_;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_MENU_WITH_DEEP_LINK:
                    Bundle bundle = new Bundle();
                    if (getIntent().getBooleanExtra(PD_Constant.DEEP_LINK, false)) {
                        bundle.putBoolean(PD_Constant.DEEP_LINK, true);
                        bundle.putString(PD_Constant.DEEP_LINK_CONTENT, getIntent().getStringExtra(PD_Constant.DEEP_LINK_CONTENT));
                    } else {
                        new Handler().postDelayed(() -> showIntro(), 2000);
                    }
                    bundle.putInt(PD_Constant.REVEALX, 0);
                    bundle.putInt(PD_Constant.REVEALY, 0);
                    PD_Utility.showFragment(ActivityMain.this, new FragmentContent_(), R.id.main_frame,
                            bundle, FragmentContent_.class.getSimpleName());
                    break;
                case INITILIZE_DRAWER:
                    initializeDrawer();
                    break;
                case MENU_HOME:
                    if (isChecked)
                        toggleToArrow();
                    Bundle homebundle = new Bundle();
                    homebundle.putInt(PD_Constant.REVEALX, 0);
                    homebundle.putInt(PD_Constant.REVEALY, 0);
                    PD_Utility.showFragment(ActivityMain.this, new FragmentContent_(), R.id.main_frame,
                            homebundle, FragmentContent_.class.getSimpleName());
                    break;
                case MENU_LANGUAGE:
                    if (isChecked)
                        toggleToArrow();
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt(PD_Constant.REVEALX, 0);
                    bundle2.putInt(PD_Constant.REVEALY, 0);
                    PD_Utility.showFragment(ActivityMain.this, new FragmentLanguage_(), R.id.main_frame,
                            bundle2, FragmentLanguage.class.getSimpleName());
                    break;
                case MENU_CONNECT_WIFI:
                    if (isChecked)
                        toggleToArrow();
                    ConnectDialog connectDialog = new ConnectDialog.Builder(ActivityMain.this).build();
                    connectDialog.isDismissOnTouchBackground();
                    connectDialog.isDismissOnClickBack();
                    connectDialog.setOnDismissListener(popupWindow -> {
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt(PD_Constant.REVEALX, 0);
                        bundle1.putInt(PD_Constant.REVEALY, 0);
                        PD_Utility.showFragment(ActivityMain.this, new FragmentContent_(), R.id.main_frame,
                                bundle1, FragmentContent_.class.getSimpleName());
                    });
                    connectDialog.show();
                    break;
                case MENU_SHARE:
                    if (isChecked)
                        toggleToArrow();
                    Bundle bundle3 = new Bundle();
                    bundle3.putInt(PD_Constant.REVEALX, 0);
                    bundle3.putInt(PD_Constant.REVEALY, 0);
                    PD_Utility.showFragment(ActivityMain.this, new FragmentShareRecieve_(), R.id.main_frame,
                            bundle3, FragmentShareRecieve.class.getSimpleName());
                    break;
                case MENU_SHARE_APP:
                    KotlinPermissions.with(ActivityMain.this)
                            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                            .onAccepted(permissionResult -> {
                                try {
                                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                                    PackageManager pm = PrathamApplication.getInstance().getPackageManager();
                                    ApplicationInfo ai = pm.getApplicationInfo(PrathamApplication.getInstance().getPackageName(), 0);
                                    File localFile = new File(ai.publicSourceDir);
                                    Uri uri = FileProvider.getUriForFile(ActivityMain.this,
                                            BuildConfig.APPLICATION_ID + ".provider", localFile);
                                    intentShareFile.setType("*/*");
                                    intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
                                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Please download apk from here...");
                                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.pratham.pradigikids");
                                    intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(Intent.createChooser(intentShareFile, "Share through"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            })
                            .ask();
                    break;
                case MENU_EXIT:
                    exitApp();
                    break;
                case CHECK_AAJ_KA_SAWAL:
                    String filename = "AajKaSawal_" + FastSave.getInstance().getString(PD_Constant.LANGUAGE, PD_Constant.HINDI) + ".json";
                    File aksFile = new File(PrathamApplication.pradigiPath + "/" + filename); //Creating an internal dir;
                    if (!aksFile.exists()) {
                        String aksUrl = PD_Constant.URL.AAJ_KA_SAWAL_URL.toString() + filename;
                        downloadAajKaSawal(aksUrl, filename);
                    }
                    break;
            }
        }
    };

    @AfterViews
    public void initialize() {
        mHandler.sendEmptyMessage(INITILIZE_DRAWER);
        mHandler.sendEmptyMessage(SHOW_MENU_WITH_DEEP_LINK);
        mHandler.sendEmptyMessage(CHECK_AAJ_KA_SAWAL);
    }

    private void initializeDrawer() {
        main_sliding_drawer.setPanelSlideListener(this);
        if (PrathamApplication.isTablet)
            drawer_profile_lottie.setAnimation("avatars/dino_dance.json");
        else
            drawer_profile_lottie.setAnimation(FastSave.getInstance().getString(PD_Constant.AVATAR,
                    "avatars/dino_dance.json"));
        drawer_profile_name.setText(FastSave.getInstance().getString(PD_Constant.PROFILE_NAME, "No Name"));
        initializeMenu();
    }

    @Click(R.id.download_badge)
    public void showDownloadList() {
        PrathamApplication.bubble_mp.start();
        downloadListFragment_ = new DownloadListFragment_();
        downloadListFragment_.show(getSupportFragmentManager(), DownloadListFragment.class.getSimpleName());
        EventMessage message = new EventMessage();
        message.setMessage(PD_Constant.BROADCAST_DOWNLOADINGS);
        EventBus.getDefault().post(message);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void showNotificationBadge(EventMessage message) {
        if (message != null) {
            if (message.getMessage().equalsIgnoreCase(PD_Constant.FILE_DOWNLOAD_STARTED)) {
                increaseNotificationCount(message);
            } else if (message.getMessage().equalsIgnoreCase(PD_Constant.FILE_DOWNLOAD_COMPLETE)) {
                decreaseNotificationCount(message);
            } else if (message.getMessage().equalsIgnoreCase(PD_Constant.FILE_DOWNLOAD_ERROR)) {
                decreaseNotificationCount(message);
            } else if (message.getMessage().equalsIgnoreCase(PD_Constant.OPEN_DOWNLOAD_LIST)) {
                showList();
            }
        }
    }

    @UiThread
    public void showList() {
        new Handler().postDelayed(this::showDownloadList, 500);
    }

    @UiThread
    public void increaseNotificationCount(EventMessage message) {
        download_notification.setNumber(message.getDownlaodContentSize());
        if (message.getDownlaodContentSize() == 1) {
            ScaleAnimation animation = new ScaleAnimation(0f, 1f, 0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setFillAfter(true);
            animation.setDuration(300);
            download_badge.setAnimation(animation);
            download_badge.setVisibility(View.VISIBLE);
            animation.start();
        }
    }

    @UiThread
    public void decreaseNotificationCount(EventMessage message) {
        download_notification.setNumber(message.getDownlaodContentSize());
        if (message.getDownlaodContentSize() == 0) {
            ScaleAnimation animation = new ScaleAnimation(1f, 0f, 1f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(300);
            animation.setFillAfter(false);
            download_badge.setAnimation(animation);
            animation.start();
            download_badge.setVisibility(View.GONE);
            if (downloadListFragment_ != null)
                downloadListFragment_.dismiss();
        }
    }

    private void initializeMenu() {
        ArrayList<Modal_NavigationMenu> navigationMenus = new ArrayList<>();
        String[] menus = getResources().getStringArray(R.array.navigation_menu);
        int[] menus_img = {R.drawable.ic_education, R.drawable.ic_abc_blocks, R.drawable.ic_wifi,
                R.drawable.ic_folder, R.drawable.ic_app_sharing, R.drawable.ic_backpacker};
        for (int i = 0; i < menus.length; i++) {
            Modal_NavigationMenu nav = new Modal_NavigationMenu();
            nav.setMenu_name(menus[i]);
            nav.setMenuImage(menus_img[i]);
            nav.setIsselected(false);
            navigationMenus.add(nav);
        }
        RV_MenuAdapter rv_menuAdapter = new RV_MenuAdapter(this, navigationMenus, this);
        rv_drawer.setHasFixedSize(true);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this, FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
        rv_drawer.setLayoutManager(flexboxLayoutManager);
        rv_drawer.setAdapter(rv_menuAdapter);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if (fragment instanceof FragmentContent_) {
            EventMessage message = new EventMessage();
            message.setMessage(PD_Constant.CONTENT_BACK);
            EventBus.getDefault().post(message);
        } else if (fragment instanceof FragmentLanguage_) {
            EventMessage message = new EventMessage();
            message.setMessage(PD_Constant.LANGUAGE_BACK);
            EventBus.getDefault().post(message);
        } else if (fragment instanceof FragmentShare_ || fragment instanceof FragmentReceive_) {
            EventMessage message = new EventMessage();
            message.setMessage(PD_Constant.SHARE_BACK);
            EventBus.getDefault().post(message);
        }
    }

    private void exitApp() {
        if (!FsService.isRunning()) {
            exitDialog = new BlurPopupWindow.Builder(ActivityMain.this)
                    .setContentView(R.layout.app_exit_dialog)
                    .bindClickListener(v -> finishAffinity(), R.id.dialog_btn_exit)
                    .bindClickListener(v -> exitDialog.dismiss(), R.id.btn_cancel)
                    .setGravity(Gravity.CENTER)
                    .setDismissOnTouchBackground(true)
                    .setDismissOnClickBack(true)
                    .setScaleRatio(0.2f)
                    .setBlurRadius(10)
                    .setTintColor(0x30000000)
                    .build();
            exitDialog.show();
        } else {
            EventMessage msg = new EventMessage();
            msg.setMessage(PD_Constant.CLOSE_FTP_SERVER);
            EventBus.getDefault().post(msg);
        }
    }

    @Click(R.id.outer_area)
    public void onRootTouch(View v) {
        if (main_sliding_drawer.isOpen())
            main_sliding_drawer.closePane();
    }

//    SpotlightListener spotlightListener = new SpotlightListener() {
//        @Override
//        public void onUserClicked(String spotlightViewId) {
//            if (top_scaling.getState() == State.EXPANDED)
//                SpotlightSequence.getInstance(ActivityMain.this, null)
//                        .addSpotlight(sheet_home, "DISPLAY CONTENT", "Click here to view contents", "sheet_home")
//                        .addSpotlight(sheet_language, "CHANGE LANGUAGE", "Click here to change Language", "sheet_language")
//                        .addSpotlight(sheet_connect, "CONNECT FTP_HOTSPOT_SSID", "Click here to connect wifi", "sheet_connect")
//                        .addSpotlight(sheet_connect, "CONNECT FTP_HOTSPOT_SSID", "Click here to connect wifi", "sheet_connect")
//                        .startSequence();
//        }
//    };

    private void showIntro() {
        new SpotlightView.Builder(ActivityMain.this)
                .introAnimationDuration(400)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(32)
                .headingTvText("Open Menu")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("Click here and Open Menu")
                .maskColor(Color.parseColor("#dc000000"))
                .target(main_nav)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#eb273f"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
//                .setListener(spotlightListener)
                .usageId(PD_Constant.PRADIGI_ICON) //UNIQUE ID
                .show();
    }

    @Override
    public void menuClicked(int position, Modal_NavigationMenu modal_navigationMenu) {
        PrathamApplication.bubble_mp.start();
        if (main_sliding_drawer.isOpen())
            main_sliding_drawer.closePane();
        if (modal_navigationMenu.getMenu_name().equalsIgnoreCase("Home"))
            mHandler.sendEmptyMessage(MENU_HOME);
        else if (modal_navigationMenu.getMenu_name().equalsIgnoreCase("Language"))
            mHandler.sendEmptyMessage(MENU_LANGUAGE);
        else if (modal_navigationMenu.getMenu_name().equalsIgnoreCase("Connect Wifi"))
            mHandler.sendEmptyMessage(MENU_CONNECT_WIFI);
        else if (modal_navigationMenu.getMenu_name().equalsIgnoreCase("Share OR Receive"))
            mHandler.sendEmptyMessage(MENU_SHARE);
        else if (modal_navigationMenu.getMenu_name().equalsIgnoreCase("Share App"))
            mHandler.sendEmptyMessage(MENU_SHARE_APP);
        else if (modal_navigationMenu.getMenu_name().equalsIgnoreCase("Exit"))
            mHandler.sendEmptyMessage(MENU_EXIT);
    }

    @Override
    public void toggleMenuIcon() {
        toggleToArrow();
    }

    @Click(R.id.main_nav)
    public void setMenuClicked() {
        if (!FsService.isRunning()) {
            if (!isChecked)
                if (main_sliding_drawer.isOpen()) main_sliding_drawer.closePane();
                else main_sliding_drawer.openPane();
            else
                onBackPressed();
        } else {
            EventMessage msg = new EventMessage();
            msg.setMessage(PD_Constant.CLOSE_FTP_SERVER);
            EventBus.getDefault().post(msg);
        }
    }

    @UiThread
    public void toggleToArrow() {
        isChecked = !isChecked;
        final int[] stateSet = {android.R.attr.state_checked * (isChecked ? 1 : -1)};
        main_hamburger.setImageState(stateSet, true);
    }

    @Override
    public void onPanelSlide(@NonNull View view, float v) {
        outer_area.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPanelOpened(@NonNull View view) {
        Log.d(TAG, "onPanelOpened: ");
    }

    @Override
    public void onPanelClosed(@NonNull View view) {
        outer_area.setVisibility(View.GONE);
    }
}