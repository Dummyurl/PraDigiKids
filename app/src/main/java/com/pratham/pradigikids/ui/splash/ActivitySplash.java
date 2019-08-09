package com.pratham.pradigikids.ui.splash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pratham.pradigikids.BaseActivity;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.BlurPopupDialog.BlurPopupWindow;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.services.PrathamSmartSync;
import com.pratham.pradigikids.ui.attendance_activity.AttendanceActivity_;
import com.pratham.pradigikids.util.PD_Constant;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;

@EActivity(R.layout.splash_activity)
public class ActivitySplash extends BaseActivity implements SplashContract.splashview {

    private static final int GOOGLE_SIGN_IN = 1;
    private static final String TAG = ActivitySplash.class.getSimpleName();
    private static final int LIGHT_ANIMATION = 2;
    private static final int UPDATE_DIALOG = 3;
    private static final int REDIRECT_TO_DASHBOARD = 4;
    private static final int REDIRECT_TO_AVATAR = 5;
    private static final int REDIRECT_TO_ATTENDANCE = 6;
    private static final int ENTER_CODE_QR_DIALOG = 7;
    //    @ViewById(R.id.img_splash_light)
//    ImageView img_splash_light;
    @ViewById(R.id.splash_video)
    VideoView splash_video;
    @ViewById(R.id.avatar_view)
    LottieAnimationView pingpong_view;
//    @ViewById(R.id.rich)
//    RichPathView rich;

    @Bean(SplashPresenterImpl.class)
    SplashContract.splashPresenter splashPresenter;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private BlurPopupWindow dialog_code;
    private EditText et_qr_code;
    private final TextWatcher codeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (et_qr_code.getText().toString().length() > 0) {
                et_qr_code.setError(null);
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private final Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LIGHT_ANIMATION:
//                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(img_splash_light, "rotation", 0f, 360f);
//                    objectAnimator.setInterpolator(new LinearInterpolator());
//                    objectAnimator.setDuration(7600);
//                    objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
//                    img_splash_light.setLayerType(View.LAYER_TYPE_NONE, null);
//                    objectAnimator.start();
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pratham);
                    splash_video.setVideoURI(video);
                    splash_video.setOnCompletionListener(mp -> {
                        splashPresenter.checkPrathamCode();
//                            new Handler().postDelayed(() -> splashPresenter.checkPrathamCode(), 2200);
                    });
//                    splash_video.setOnPreparedListener(mp -> mp.setLooping(true));
//                    splash_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        public void onCompletion(MediaPlayer mp) {
//                            startNextActivity();
//                        }
//                    });
                    splash_video.start();
//                    rich.setVisibility(View.VISIBLE);
//                    final RichPath[] allPaths = rich.findAllRichPaths();
//                    RichPathAnimator.animate(allPaths).trimPathEnd(0, 1).interpolator(new AccelerateDecelerateInterpolator()).duration(1800).start();
//                    pingpong_view.setVisibility(View.VISIBLE);
                    break;
                case UPDATE_DIALOG:
                    new BlurPopupWindow.Builder(mContext)
                            .setContentView(R.layout.app_update_dialog)
                            .bindClickListener(v -> {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.pratham.pradigikids"));
                                startActivity(intent);
                            }, R.id.btn_update)
                            .setGravity(Gravity.CENTER)
                            .setDismissOnTouchBackground(false)
                            .setDismissOnClickBack(false)
                            .setScaleRatio(0.2f)
                            .setBlurRadius(10)
                            .setTintColor(0x30000000)
                            .build()
                            .show();
                    break;
                case ENTER_CODE_QR_DIALOG:
                    dialog_code = new BlurPopupWindow.Builder(mContext)
                            .setContentView(R.layout.dialog_enter_tab_qr_code)
                            .bindClickListener(v -> {
                                if (!et_qr_code.getText().toString().isEmpty()) {
                                    splashPresenter.savePrathamCode(et_qr_code.getText().toString().trim());
                                    dialog_code.dismiss();
                                    new Handler().postDelayed(() -> loadSplash(), 1000);
                                } else {
                                    et_qr_code.setError("Please enter Tablet QR Code here");
                                }
                            }, R.id.dialog_qr_submit)
                            .setGravity(Gravity.CENTER)
                            .setDismissOnTouchBackground(false)
                            .setDismissOnClickBack(false)
                            .setScaleRatio(0.2f)
                            .setBlurRadius(10)
                            .setTintColor(0x30000000)
                            .build();
                    et_qr_code = dialog_code.findViewById(R.id.et_tab_qr_code);
                    et_qr_code.addTextChangedListener(codeTextWatcher);
                    dialog_code.show();
                    break;
                case REDIRECT_TO_DASHBOARD:
                    Intent intent = new Intent(ActivitySplash.this, AttendanceActivity_.class);
                    if (getIntent().getBooleanExtra(PD_Constant.DEEP_LINK, false)) {
                        intent.putExtra(PD_Constant.DEEP_LINK, true);
                        intent.putExtra(PD_Constant.DEEP_LINK_CONTENT, getIntent().getStringExtra(PD_Constant.DEEP_LINK_CONTENT));
                    }
                    intent.putExtra(PD_Constant.STUDENT_ADDED, true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                    finishAfterTransition();
                    break;
                case REDIRECT_TO_AVATAR:
                    Intent intent2 = new Intent(ActivitySplash.this, AttendanceActivity_.class);
                    if (getIntent().getBooleanExtra(PD_Constant.DEEP_LINK, false)) {
                        intent2.putExtra(PD_Constant.DEEP_LINK, true);
                        intent2.putExtra(PD_Constant.DEEP_LINK_CONTENT, getIntent().getStringExtra(PD_Constant.DEEP_LINK_CONTENT));
                    }
                    intent2.putExtra(PD_Constant.STUDENT_ADDED, false);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                    finishAfterTransition();
                    break;
                case REDIRECT_TO_ATTENDANCE:
                    Intent intent3 = new Intent(ActivitySplash.this, AttendanceActivity_.class);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                    finishAfterTransition();
                    break;
            }
        }
    };

    @AfterViews
    public void initializeViews() {
        mContext = this;
        splashPresenter.clearPreviousBuildData();
        splashPresenter.populateDefaultDB();
        mhandler.sendEmptyMessage(LIGHT_ANIMATION);
//        new Handler().postDelayed(() -> splashPresenter.checkPrathamCode(), 2200);
    }

    @Override
    public void showEnterPrathamCodeDialog() {
        new Handler().postDelayed(() -> mhandler.sendEmptyMessage(ENTER_CODE_QR_DIALOG), 1200);
    }

    @Override
    public void loadSplash() {
        if (PrathamApplication.useSatelliteGPS)
//                                signInUsingGoogle();
            splashPresenter.startGpsTimer();
////                            else
        else
            splashPresenter.checkIfContentinSDCard();
        PrathamSmartSync.pushUsageToServer(false);
    }

    @UiThread
    @Override
    public void showAppUpdateDialog() {
        mhandler.sendEmptyMessage(UPDATE_DIALOG);
    }

    @Override
    public void signInUsingGoogle() {
        if (!FastSave.getInstance().getBoolean(PD_Constant.IS_GOOGLE_SIGNED_IN, false)) {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = splashPresenter.configureSignIn();
            }
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        } else {
            splashPresenter.checkIfContentinSDCard();
        }
    }

    @UiThread
    @Override
    public void redirectToDashboard() {
        mhandler.sendEmptyMessage(REDIRECT_TO_DASHBOARD);
    }

    @UiThread
    @Override
    public void redirectToAvatar() {
        mhandler.sendEmptyMessage(REDIRECT_TO_AVATAR);
    }

    @UiThread
    @Override
    public void redirectToAttendance() {
        mhandler.sendEmptyMessage(REDIRECT_TO_ATTENDANCE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
//            if (resultCode == RESULT_OK)
            splashPresenter.validateSignIn(data);
        } else {
            // Google Sign In failed, update UI appropriately
            Log.d(TAG, "Login Unsuccessful.");
            splashPresenter.checkIfContentinSDCard();
        }
    }

    @UiThread
    @Override
    public void googleSignInFailed() {
        Toast.makeText(mContext, "Error connecting to Google. Please check your Internet connection or try with different ID", Toast.LENGTH_SHORT).show();
        signInUsingGoogle();
    }

    @Subscribe
    public void onMessageReceived(EventMessage message) {
        if (message != null) {
            if (message.getMessage().equalsIgnoreCase(PD_Constant.LOCATION_CHANGED))
                splashPresenter.onLocationChanged(message.getLocation());
        }
    }
}