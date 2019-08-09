package com.pratham.pradigikids.util;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.models.StorageInfo;
import com.pratham.pradigikids.ui.attendance_activity.AttendanceActivity;
import com.pratham.pradigikids.ui.dashboard.ActivityMain;
import com.pratham.pradigikids.ui.video_player.Activity_VPlayer;

import org.greenrobot.eventbus.EventBus;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static android.content.Context.ACTIVITY_SERVICE;

public class PD_Utility {


    static int Display_Year = 0;
    static int Dont_Disclose = 0;
    private static String TAG = "Utility";
    static Dialog mDateTimeDialog = null;
    private static Dialog dialog;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_WIFI = 1;
    public static final int NETWORK_STATUS_MOBILE = 2;

    public static final Pattern otp_pattern = Pattern.compile("(|^)\\d{4}");
    private static List<Integer> colors;
    private static int[] boy_avatars = new int[]{
            R.drawable.ic_boy_one, R.drawable.ic_boy_two, R.drawable.ic_boy_three, R.drawable.ic_boy_four
            , R.drawable.ic_boy_five, R.drawable.ic_boy_six, R.drawable.ic_boy_seven, R.drawable.ic_boy_eight
            , R.drawable.ic_boy_nine
    };
    private static int[] girl_avatars = new int[]{
            R.drawable.ic_girl_one, R.drawable.ic_girl_two, R.drawable.ic_girl_three, R.drawable.ic_girl_four
            , R.drawable.ic_girl_five, R.drawable.ic_girl_six, R.drawable.ic_girl_seven, R.drawable.ic_girl_eight
            , R.drawable.ic_girl_nine
    };

    public PD_Utility(Context context) {
        colors = getAllMaterialColors();
    }

    public static int dp2px(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    public static String getLocalHostName() {
        String str1 = Build.MODEL;
//        String str2 = getRandomNumStr(3);
        return str1 /*+ "_" + str2*/;
    }

    public static String getPhoneModel() {
        String str1 = Build.BRAND;
        String str2 = Build.MODEL;
        str2 = str1 + "_" + str2;
        return str2;
    }

    public static String getRandomNumStr(int NumLen) {
        Random random = new Random(System.currentTimeMillis());
        StringBuffer str = new StringBuffer();
        int i, num;
        for (i = 0; i < NumLen; i++) {
            num = random.nextInt(10); // 0-10的随机数
            str.append(num);
        }
        return str.toString();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / (context.getResources().getDimension(R.dimen._160sdp)));
        return noOfColumns;
    }

    /**
     * Method to Hide Soft Input Keyboard
     *
     * @param act
     */
    public static void HideInputKeypad(Activity act) {

        InputMethodManager inputManager = (InputMethodManager) act
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (act.getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(act.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public static String getVersion() {
        Context context = PrathamApplication.getInstance();
        String packageName = context.getPackageName();
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Unable to find", "the name " + packageName + " in the package");
            return null;
        }
    }

    /**
     * Function to show Fragment
     *
     * @param mActivity
     * @param mFragment
     * @param mBundle
     * @param TAG
     */
    public static void showFragment(Activity mActivity, Fragment mFragment, int frame,
                                    Bundle mBundle, String TAG) {

        if (mBundle != null)
            mFragment.setArguments(mBundle);

        if (mActivity instanceof ActivityMain) {
            ((ActivityMain) mActivity).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(frame, mFragment, TAG)
                    .addToBackStack(TAG)
                    .commit();
        } else if (mActivity instanceof AttendanceActivity) {
            ((AttendanceActivity) mActivity).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(frame, mFragment, TAG)
                    .addToBackStack(TAG)
                    .commit();
        } else if (mActivity instanceof Activity_VPlayer) {
            ((Activity_VPlayer) mActivity).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(frame, mFragment, TAG)
                    .addToBackStack(TAG)
                    .commit();
        }
    }

    public static void addFragment(Activity mActivity, Fragment mFragment, int frame,
                                   Bundle mBundle, String TAG) {
        if (mBundle != null)
            mFragment.setArguments(mBundle);
        if (mActivity instanceof ActivityMain) {
            ((ActivityMain) mActivity).getSupportFragmentManager()
                    .beginTransaction()
                    .add(frame, mFragment, TAG)
                    .addToBackStack(TAG)
                    .commit();
        } else if (mActivity instanceof AttendanceActivity) {
            ((AttendanceActivity) mActivity).getSupportFragmentManager()
                    .beginTransaction()
                    .add(frame, mFragment, TAG)
                    .addToBackStack(TAG)
                    .commit();
        } else if (mActivity instanceof Activity_VPlayer) {
            ((Activity_VPlayer) mActivity).getSupportFragmentManager()
                    .beginTransaction()
                    .add(frame, mFragment, TAG)
                    .addToBackStack(TAG)
                    .commit();
        }
    }

    public static void setLocale(Context context, String lang) {

        if (lang.equalsIgnoreCase("English"))
            lang = "en";
        if (lang.equalsIgnoreCase("Hindi"))
            lang = "hi";
        if (lang.equalsIgnoreCase("Marathi"))
            lang = "mr";
        if (lang.equalsIgnoreCase("Kannada"))
            lang = "kn";
        if (lang.equalsIgnoreCase("Telugu"))
            lang = "te";
        if (lang.equalsIgnoreCase("Bengali"))
            lang = "bn";
        if (lang.equalsIgnoreCase("Gujarati"))
            lang = "gu";
        if (lang.equalsIgnoreCase("Punjabi"))
            lang = "pa";
        if (lang.equalsIgnoreCase("Tamil"))
            lang = "ta";
        if (lang.equalsIgnoreCase("Odiya"))
            lang = "or";
        if (lang.equalsIgnoreCase("Malayalam"))
            lang = "ml";
        if (lang.equalsIgnoreCase("Assamese"))
            lang = "as";

        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
/*
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
*/
        Configuration conf = context.getResources().getConfiguration();
        conf.setLocale(myLocale);
        context.createConfigurationContext(conf);
        context.getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
    }

    /**
     * Method to Show Log
     *
     * @param Type
     * @param TAG
     * @param Message
     */
    public static void DEBUG_LOG(int Type, String TAG, String Message) {
        switch (Type) {
            case 0:
                Log.i(TAG, Message);
                break;
            case 1:
                Log.d(TAG, Message);
                break;
            case 2:
                Log.e(TAG, Message);
                break;
            case 3:
                Log.v(TAG, Message);
                break;
            case 4:
                Log.w(TAG, Message);
                break;
            default:
                break;
        }

    }


    /**
     * Function to show toast
     *
     * @param mContext
     * @param str_Message
     */
    public static void ShowToast(Context mContext, String str_Message) {

        Toast.makeText(mContext, str_Message, Toast.LENGTH_SHORT).show();

    }

    public static String getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        return dateFormat.format(cal.getTime());
    }

    public static long getTimeDifference(String start, String end) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            Date date1 = simpleDateFormat.parse(start);
            Date date2 = simpleDateFormat.parse(end);

            long difference = date2.getTime() - date1.getTime();
//        days = (int) (difference / (1000 * 60 * 60 * 24));
//        hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
//        min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
//        hours = (hours < 0 ? -hours : hours);
            Log.i("time_diff", " :: " + difference);
            return difference;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int convertDpToPixels(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int convertDpToSp(float dp, Context context) {
        int sp = (int) (convertDpToPixels(dp, context) / (float) convertSpToPixels(dp, context));
        return sp;
    }


    /*public static void setFont(Context context, TextView view, DatabaseHandler db) {
        Typeface font = null;
        if (db.GetUserLanguage().equalsIgnoreCase("Odiya")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/lohit_oriya.ttf");
        } else if (db.GetUserLanguage().equalsIgnoreCase("Assamese")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/geetl_assamese.ttf");
        } else if (db.GetUserLanguage().equalsIgnoreCase("Gujarati")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/muktavaani_gujarati.ttf");
        } else if (db.GetUserLanguage().equalsIgnoreCase("Punjabi")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/raavi_punjabi.ttf");
        }
        if (font != null) {
            view.setTypeface(font, Typeface.NORMAL);
        }
    }*/

    /*public static Typeface getFont(Context context, DatabaseHandler db) {
        Typeface font = null;
        if (db.GetUserLanguage().equalsIgnoreCase("Odiya")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/lohit_oriya.ttf");
        } else if (db.GetUserLanguage().equalsIgnoreCase("Assamese")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/geetl_assamese.ttf");
        } else if (db.GetUserLanguage().equalsIgnoreCase("Gujarati")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/muktavaani_gujarati.ttf");
        } else if (db.GetUserLanguage().equalsIgnoreCase("Punjabi")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/raavi_punjabi.ttf");
        }
        return font;
    }*/
//    private void startAnim() {
//        int mainViewHeight = logo_pathview.getRootView().getHeight();
//        ObjectAnimator mainViewScaleXAnim = ObjectAnimator.ofFloat(logo_pathview, "scaleX", 1.0f, 0.8f);
//        ObjectAnimator mainViewScaleYAnim = ObjectAnimator.ofFloat(logo_pathview, "scaleY", 1.0f, 0.9f);
//        ObjectAnimator mainViewAlphaAnim = ObjectAnimator.ofFloat(logo_pathview, "alpha", 1.0f, 0.5f);
//        ObjectAnimator mainViewRotationXAnim = ObjectAnimator.ofFloat(logo_pathview, "rotationX", 0f, 8f);
//        ObjectAnimator mainViewRotationXAnimResume = ObjectAnimator.ofFloat(logo_pathview, "rotationX", 8f, 0f);
//        mainViewRotationXAnimResume.setStartDelay(200);
//        ObjectAnimator mainViewTranslationYAnim = ObjectAnimator.ofFloat(logo_pathview, "translationY", 0, -(mainViewHeight / 20));
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(mainViewScaleXAnim, mainViewScaleYAnim, mainViewAlphaAnim, mainViewRotationXAnim, mainViewRotationXAnimResume, mainViewTranslationYAnim);
//        animatorSet.setDuration(400);
//        animatorSet.start();
//    }

//    private void resumeAnim() {
//        int mainViewHeight = logo_pathview.getRootView().getHeight();
//        ObjectAnimator mainViewScaleXAnim = ObjectAnimator.ofFloat(logo_pathview, "scaleX", 0.8f, 1.0f);
//        ObjectAnimator mainViewScaleYAnim = ObjectAnimator.ofFloat(logo_pathview, "scaleY", 0.9f, 1.0f);
//        ObjectAnimator mainViewAlphaAnim = ObjectAnimator.ofFloat(logo_pathview, "alpha", 0.5f, 1.0f);
//        ObjectAnimator mainViewRotationXAnim = ObjectAnimator.ofFloat(logo_pathview, "rotationX", 0f, 8f);
//        ObjectAnimator mainViewRotationXAnimResume = ObjectAnimator.ofFloat(logo_pathview, "rotationX", 8f, 0f);
//        mainViewRotationXAnimResume.setStartDelay(200);
//        ObjectAnimator mainViewTranslationYAnim = ObjectAnimator.ofFloat(logo_pathview, "translationY", -(mainViewHeight / 20), 0);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(mainViewScaleXAnim, mainViewScaleYAnim, mainViewAlphaAnim, mainViewRotationXAnim, mainViewRotationXAnimResume, mainViewTranslationYAnim);
//        animatorSet.setDuration(400);
//        animatorSet.start();
//    }
//    /**
//     * Function to show alert
//     *
//     * @param mContext
//     * @param messgae
//     */
//    public static void ShowAlert(final Dialog mDialog, final Context mContext,
//                                 final String messgae) {
//
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        mDialog.setContentView(R.layout.cw_error_rmessage_popup);
//
//        DEBUG_LOG(1, TAG, CW_Constant.DEVICE_WIDTH + "");
//
//        Spannable wordtoSpan = null;
//        if (messgae.equalsIgnoreCase(mContext.getString(R.string.cw_no_gps))) {
//
//            int i = messgae.indexOf("Coffee Wink");
//            wordtoSpan = new SpannableString(messgae);
//            wordtoSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
//                    i, i + 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            mDialog.setCancelable(false);
//        } else
//            mDialog.setCancelable(true);
//
//        TextView txt_heading = (TextView) mDialog
//                .findViewById(R.id.txt_heading);
//        LinearLayout.LayoutParams mParams;
//
//        if (CW_Constant.DEVICE_WIDTH <= 480) {
//            mParams = new LinearLayout.LayoutParams(400,
//                    LayoutParams.WRAP_CONTENT);
//            mParams.gravity = Gravity.CENTER_HORIZONTAL;
//
//        } else {
//
//            int width = (int) (CW_Constant.DEVICE_WIDTH * .80);
//            if (mContext.getResources().getBoolean(R.bool.isTablet))
//                width = (int) (CW_Constant.DEVICE_WIDTH * .60);
//
//            mParams = new LinearLayout.LayoutParams(
//                    width, LayoutParams.WRAP_CONTENT);
//            mParams.gravity = Gravity.CENTER_HORIZONTAL;
//
//        }
//        txt_heading.setLayoutParams(mParams);
//
//        TextView txt_error_message = (TextView) mDialog
//                .findViewById(R.id.txt_error_message);
//
//        if (mContext.getResources().getBoolean(R.bool.isTablet))
//            mParams.setMargins(0, 0, 0, 0);
//        else
//            mParams.setMargins(15, 0, 15, 0);
//
//        txt_error_message.setLayoutParams(mParams);
//
//        if (wordtoSpan != null) {
//            txt_error_message.setText(wordtoSpan);
//        } else
//            txt_error_message.setText(messgae);
//
//        mDialog.findViewById(R.id.btn_action_left).setVisibility(View.GONE);
//
//        Button btn_okh = (Button) mDialog.findViewById(R.id.btn_action_right);
//
//        btn_okh.setText(R.string.cw_ok);
//
//        btn_okh.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (messgae.equalsIgnoreCase(mContext
//                        .getString(R.string.cw_no_gps))) {
//
//                    if (GPSTracker.getInstance(mContext).canGetLocation()) {
//                        mDialog.dismiss();
//                    }
//
//                } else {
//                    mDialog.dismiss();
//                }
//            }
//        });
//        mDialog.show();
//
//    }


//    /**
//     * Function to date picker with two additional options
//     *
//     * @param mDialog
//     * @param mContext
//     * @param mDateSetListener
//     * @param DOB_Show_Other
//     */
//    public static void ShowDatePicker(final Dialog mDialog,
//                                      final Context mContext, final onDateSetListener mDateSetListener, int DOB_Show_Other) {
//
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        mDialog.setContentView(R.layout.cw_date_picker);
//        mDialog.setCancelable(true);
//
//        LinearLayout ll_Main = (LinearLayout) mDialog
//                .findViewById(R.id.ll_main);
//
//        FrameLayout.LayoutParams mParams;
//
//        if (CW_Constant.DEVICE_WIDTH <= 480) {
//            mParams = new FrameLayout.LayoutParams(400,
//                    LayoutParams.WRAP_CONTENT);
//            mParams.gravity = Gravity.CENTER_HORIZONTAL;
//
//        } else {
//
//            int width = (int) (CW_Constant.DEVICE_WIDTH * .80);
//            if (mContext.getResources().getBoolean(R.bool.isTablet))
//                width = (int) (CW_Constant.DEVICE_WIDTH * .60);
//
//            mParams = new FrameLayout.LayoutParams(
//                    width, LayoutParams.WRAP_CONTENT);
//            mParams.gravity = Gravity.CENTER_HORIZONTAL;
//        }
//        ll_Main.setLayoutParams(mParams);
//
//        Calendar mCalendar = Calendar.getInstance();
//        mCalendar.add(Calendar.YEAR, -18);
//        final DatePicker mDatePicker = (DatePicker) mDialog
//                .findViewById(R.id.dp_dob);
//        mDatePicker.setMaxDate(mCalendar.getTimeInMillis());
//
//        final ImageView imgbtn_Display_Year_Only = (ImageView) mDialog
//                .findViewById(R.id.img_display_dob_in_year);
//
//        final ImageView imgbtn_Dont_Disclose = (ImageView) mDialog
//                .findViewById(R.id.img_dont_disclose);
//
//
//        imgbtn_Display_Year_Only.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (Display_Year == 0) {
//                    Display_Year = 1;
//                    imgbtn_Display_Year_Only
//                            .setImageResource(R.drawable.cw_date_picker_checked);
//
//
//                    Dont_Disclose = 0;
//                    imgbtn_Dont_Disclose
//                            .setImageResource(R.drawable.cw_date_picker_unchecked);
//
//                } else {
//                    Display_Year = 0;
//                    imgbtn_Display_Year_Only
//                            .setImageResource(R.drawable.cw_date_picker_unchecked);
//
//                }
//
//            }
//        });
//
//
//        imgbtn_Dont_Disclose.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (Dont_Disclose == 0) {
//                    Dont_Disclose = 1;
//                    imgbtn_Dont_Disclose
//                            .setImageResource(R.drawable.cw_date_picker_checked);
//
//                    Display_Year = 0;
//                    imgbtn_Display_Year_Only
//                            .setImageResource(R.drawable.cw_date_picker_unchecked);
//                } else {
//                    Dont_Disclose = 0;
//                    imgbtn_Dont_Disclose
//                            .setImageResource(R.drawable.cw_date_picker_unchecked);
//                }
//
//            }
//        });
//
//        switch (DOB_Show_Other) {
//            case 0:
//                break;
//
//            case 1:
//                Display_Year = 1;
//                imgbtn_Display_Year_Only
//                        .setImageResource(R.drawable.cw_date_picker_checked);
//
//
//                Dont_Disclose = 0;
//                imgbtn_Dont_Disclose
//                        .setImageResource(R.drawable.cw_date_picker_unchecked);
//
//                break;
//
//            case 2:
//                Dont_Disclose = 1;
//                imgbtn_Dont_Disclose
//                        .setImageResource(R.drawable.cw_date_picker_checked);
//
//                Display_Year = 0;
//                imgbtn_Display_Year_Only
//                        .setImageResource(R.drawable.cw_date_picker_unchecked);
//                break;
//
//
//        }
//
//
//        Button btn_Clear = (Button) mDialog.findViewById(R.id.btn_clear);
//        btn_Clear.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//
//            }
//        });
//
//        Button btn_Done = (Button) mDialog.findViewById(R.id.btn_done);
//        btn_Done.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                CalculateAge(mDatePicker.getDayOfMonth() + "-"
//                        + mDatePicker.getMonth() + "-" + mDatePicker.getYear());
//
//                mDateSetListener.onDoneClickListener(
//                        mDatePicker.getDayOfMonth() + "-"
//                                + (mDatePicker.getMonth() + 1) + "-"
//                                + mDatePicker.getYear(),
//                        CalculateAge(mDatePicker.getDayOfMonth() + "-"
//                                + (mDatePicker.getMonth() + 1) + "-"
//                                + mDatePicker.getYear()), Display_Year,
//                        Dont_Disclose);
//                mDialog.dismiss();
//            }
//        });
//
//        mDialog.show();
//
//    }

    /**
     * Function to get scalled bitmap
     *
     * @param mBitmap
     * @return
     */
    public static Bitmap GetScalledImage(Bitmap mBitmap) {

        Bitmap resultBitmap;
        if (mBitmap.getWidth() >= mBitmap.getHeight()) {

            resultBitmap = Bitmap.createBitmap(mBitmap, mBitmap.getWidth() / 2
                            - mBitmap.getHeight() / 2, 0, mBitmap.getHeight(),
                    mBitmap.getHeight());

        } else {

            resultBitmap = Bitmap.createBitmap(mBitmap, 0, mBitmap.getHeight()
                            / 2 - mBitmap.getWidth() / 2, mBitmap.getWidth(),
                    mBitmap.getWidth());
        }

        return resultBitmap;

    }

    /**
     * Function to calculate user age
     *
     * @param DATE
     * @return
     */
    public static int CalculateAge(String DATE) {

        try {

            SimpleDateFormat DOB_SDF = new SimpleDateFormat("dd-MM-yyyy",
                    Locale.US);

            Date mDOB = DOB_SDF.parse(DATE);

            Calendar dob = Calendar.getInstance();
            dob.setTime(mDOB);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
                age--;
            } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < dob
                    .get(Calendar.DAY_OF_MONTH)) {
                age--;
            }

            return age;

            // return Time_SDF.format(_24HourDt).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Method to generate HMAC-SHA256 signature.
     *
     * @param msg       - Message
     * @param keyString - Key according to which signature will be generated.
     * @return - HMAC - SHA256 signature
     */

    public static String getHMACSignature(String msg, String keyString) {

        DEBUG_LOG(3, TAG, "Msg is--->" + msg);
        DEBUG_LOG(3, TAG, "Key is--->" + keyString);

        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec(
                    (keyString).getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return digest;
    }

    /**
     * Method to convert Input stream into string.
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Method to check Internet availability, returns true if Device has an
     * Internet connection.
     *
     * @param mContext
     * @return
     */

    public static boolean isInternetAvailable(Context mContext) {

        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;

    }

    public static Bitmap getThumbnail(Uri uri, Context mContext)
            throws FileNotFoundException, IOException {
        InputStream input = mContext.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;// optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1)
                || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
                : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 160) ? (originalSize / 160) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;// optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        input = mContext.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0)
            return 1;
        else
            return k;
    }

    /**
     * Function to get byte array from file
     *
     * @param mFile
     * @return
     */
    public static byte[] getByteArrayfromFile(File mFile) {
        // File file = new File(Path);
        byte[] b = new byte[(int) mFile.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(mFile);
            fileInputStream.read(b);
            for (int i = 0; i < b.length; i++) {
                System.out.print((char) b[i]);
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
        return b;
    }

    /**
     * Function to get file from byte array
     *
     * @param args
     */
    public static void getFilefromByteArray(String[] args) {

        String strFilePath = "Your path";
        try {
            FileOutputStream fos = new FileOutputStream(strFilePath);
            String strContent = "Write File using Java ";

            fos.write(strContent.getBytes());
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException : " + ex);
        } catch (IOException ioe) {
            System.out.println("IOException : " + ioe);
        }
    }

    /**
     * Function to rotate image according to angle
     *
     * @param angle
     * @param mBitmap
     * @return
     */
    public static Bitmap RotateImage(int angle, Bitmap mBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), matrix, true);
        return mBitmap;
    }

    public static String getImagePath(Uri uri, Context mContext) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null,
                null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ",
                new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor
                .getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    /**
     * Function to get Hash Map
     *
     * @param mContext
     */
    public static void GetHashMap(Context mContext) {

        // Add code to print out the key hash
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    "com.coffeewink", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * Function to check position is even or odd
     *
     * @param position
     * @return
     */
    public static boolean isOdd(int position) {
        if ((position % 2) == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Function to get real path of image
     *
     * @param contentURI
     * @param mContext
     * @return
     */
    public static String getRealPathFromURI(Uri contentURI, Context mContext) {
        String result;
        Cursor cursor = mContext.getContentResolver().query(contentURI, null,
                null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * Function to check google play services are installed or not
     *
     * @param mContext
     * @return
     */
//    public static boolean isGooglePlayServicesAvailable(Context mContext) {
//        // Check that Google Play services is available
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(mContext);
//        // If Google Play services is available
//        if (ConnectionResult.SUCCESS == resultCode) {
//            // In debug mode, log the status
//            Log.d("Location Updates", "Google Play services is available.");
//            return true;
//        } else {
//
//            return false;
//        }
//    }

    /**
     * Function to check whether the service is running or not
     *
     * @param serviceClass
     * @param mContext
     * @return
     */
    public static boolean isServiceRunning(Class<?> serviceClass,
                                           Context mContext) {
        ActivityManager manager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                DEBUG_LOG(1, TAG, "Service Running");
                return true;
            }
        }

        DEBUG_LOG(1, TAG, "Service Not Running");
        return false;
    }

    public static int getCurrentDPI(Context mContext) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;
        switch (densityDpi) {
            case DisplayMetrics.DENSITY_XXXHIGH:

                break;
            case DisplayMetrics.DENSITY_XXHIGH:

                break;
            case DisplayMetrics.DENSITY_XHIGH:

                break;
            case DisplayMetrics.DENSITY_HIGH:

                break;
            case DisplayMetrics.DENSITY_MEDIUM:

                break;
            case DisplayMetrics.DENSITY_LOW:

                break;

            default:
                break;
        }

        return 0;

    }

    public static String getApplicationName(Context context) {
        String appname = "";
        CharSequence c = "";
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
        try {
            c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
            appname = c.toString();
            Log.w("LABEL", c.toString());
            return appname;
        } catch (Exception e) {
            e.printStackTrace();
            return "PraDigi";
        }
    }

    /**
     * calculates age from birth date
     *
     * @param DOB
     */
    public boolean isAdult(String DOB) {

        try {
            SimpleDateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.US);
            Date dateOfBirth = mSDF.parse(DOB);
            Calendar dob = Calendar.getInstance();
            dob.setTime(dateOfBirth);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
                age--;
            } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < dob
                    .get(Calendar.DAY_OF_MONTH)) {
                age--;
            }

            if (age < 18) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

//
//    /**
//     * Function to update Header (Heading) of active activity
//     *
//     * @param activity
//     * @param str_Heading
//     */
//    public static void SetHeading(FragmentActivity activity, String str_Heading) {
//        TextView txt_Heading = (TextView) activity.findViewById(R.id.txt_heading);
//        txt_Heading.setText(str_Heading.toUpperCase());
//    }


    /**
     * Function to get Byte array fom bitmap
     *
     * @param mBitmap
     * @return
     */
    public static byte[] getByteArray(Bitmap mBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] mArray = stream.toByteArray();
        return mArray;
    }


    /**
     * Function to get formatted meeting date "EEE dd, yyyy"
     *
     * @param str_Date
     * @return
     */
    public static String getFormattedDate(String str_Date) {

        SimpleDateFormat Meeting_Date_SDF = new SimpleDateFormat("dd-MM-yyyy",
                Locale.US);
        SimpleDateFormat Meeting_Date_SDF_Formatted = new SimpleDateFormat("MMM dd, yyyy",
                Locale.US);
        try {
            Date mDate = Meeting_Date_SDF.parse(str_Date);
            return Meeting_Date_SDF_Formatted.format(mDate).toString();

        } catch (ParseException e) {
            e.printStackTrace();
            return str_Date;
        }

    }

    /**
     * Function to get formatted meeting date "EEE dd, yyyy"
     *
     * @param str_Time
     * @return
     */
    public static String getFormattedTime(String str_Time) {

        SimpleDateFormat Meeting_Time_SDF = new SimpleDateFormat("HH:mm",
                Locale.US);
        SimpleDateFormat Meeting_Time_SDF_Formatted = new SimpleDateFormat("hh:mm a",
                Locale.US);
        try {
            Date mDate = Meeting_Time_SDF.parse(str_Time);
            return Meeting_Time_SDF_Formatted.format(mDate).toString();

        } catch (ParseException e) {
            e.printStackTrace();
            return str_Time;
        }

    }

    /**
     * Function to get full address from lat and long coordinates
     *
     * @param lat,lng
     * @return
     */
    public static String getAddressFromLocation(Context context, double lat, double lng) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            String result = null;
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                result = sb.toString();
                return result;
            } else {
                return "Getting Location....";
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);
            return "Location not detected";
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void showTryAgainDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setTitle("Please check your network connectivity and try again.");
        dialog.show();
    }
//    /**
//     * Function to get name and email address from contacts
//     *
//     * @param mContext
//     * @return
//     */
//    public static ArrayList<Contact> getContacts(Context mContext) {
//
//
//        try {
//            ContentResolver cr = mContext.getContentResolver(); // Activity/Application
//            // android.content.Context
//            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
//                    null, null, null);
//            ArrayList<Contact> al_Contacts = new ArrayList<Contact>();
//            if (cursor.moveToFirst()) {
//                do {
//                    String id = cursor.getString(cursor
//                            .getColumnIndex(ContactsContract.Contacts._ID));
//
//                    if (Integer
//                            .parseInt(cursor.getString(cursor
//                                    .getColumnIndex(ContactsContract.Contacts._ID))) > 0) {
//                        Cursor pCur = cr.query(
//                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                                null,
//                                ContactsContract.CommonDataKinds.Email.CONTACT_ID
//                                        + " = ?", new String[]{id}, null);
//                        while (pCur.moveToNext()) {
//                            String contactEmail = pCur
//                                    .getString(pCur
//                                            .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                            String contactName = pCur
//                                    .getString(pCur
//                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//
//                            Contact mContact = new Contact();
//                            mContact.setContact_name(contactName);
//                            mContact.setContact_email(contactEmail);
//                            al_Contacts.add(mContact);
//                            CW_Utility.DEBUG_LOG(1, TAG, "Email :" + contactEmail);
//                            break;
//                        }
//                        pCur.close();
//                    }
//                } while (cursor.moveToNext());
//            }
//            return al_Contacts;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public static Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    public static Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    public static Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

    /*public static AlertDialog showLoader(Context context) {
        final View dialogView = View.inflate(context, R.layout.sunbaby_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }*/

//    private void revealShow(View rootView, boolean reveal, final AlertDialog dialog) {
//        final View view = rootView.findViewById(R.id.filter_popup);
//        int w = view.getRight();
//        int h = view.getTop();
//        float maxRadius = (float) Math.sqrt(w * w + h * h);
//        if (reveal) {
//            Animator revealAnimator = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                revealAnimator = ViewAnimationUtils.createCircularReveal(view,
//                        w, h, 0, maxRadius);
//                revealAnimator.setDuration(500);
//            }
//            view.setVisibility(View.VISIBLE);
//            if (revealAnimator != null) {
//                revealAnimator.start();
//            }
//        } else {
//            Animator anim = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                anim = ViewAnimationUtils.createCircularReveal(view, w, h, maxRadius, 0);
//                anim.setDuration(500);
//            }
//            if (anim != null) {
//                anim.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        dialog.dismiss();
//                        view.setVisibility(View.INVISIBLE);
//                    }
//                });
//                anim.start();
//            } else {
//                dialog.dismiss();
//                view.setVisibility(View.INVISIBLE);
//            }
//        }
//    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static final int ACCELERATE_DECELERATE_INTERPOLATOR = 0;
    public static final int ACCELERATE_INTERPOLATOR = 1;
    public static final int ANTICIPATE_INTERPOLATOR = 2;
    public static final int ANTICIPATE_OVERSHOOT_INTERPOLATOR = 3;
    public static final int BOUNCE_INTERPOLATOR = 4;
    public static final int DECELERATE_INTERPOLATOR = 5;
    public static final int FAST_OUT_LINEAR_IN_INTERPOLATOR = 6;
    public static final int FAST_OUT_SLOW_IN_INTERPOLATOR = 7;
    public static final int LINEAR_INTERPOLATOR = 8;
    public static final int LINEAR_OUT_SLOW_IN_INTERPOLATOR = 9;
    public static final int OVERSHOOT_INTERPOLATOR = 10;

    public static TimeInterpolator createInterpolator(@IntRange(from = 0, to = 10) final int interpolatorType) {
        switch (interpolatorType) {
            case ACCELERATE_DECELERATE_INTERPOLATOR:
                return new AccelerateDecelerateInterpolator();
            case ACCELERATE_INTERPOLATOR:
                return new AccelerateInterpolator();
            case ANTICIPATE_INTERPOLATOR:
                return new AnticipateInterpolator();
            case ANTICIPATE_OVERSHOOT_INTERPOLATOR:
                return new AnticipateOvershootInterpolator();
            case BOUNCE_INTERPOLATOR:
                return new BounceInterpolator();
            case DECELERATE_INTERPOLATOR:
                return new DecelerateInterpolator();
            case FAST_OUT_LINEAR_IN_INTERPOLATOR:
                return new FastOutLinearInInterpolator();
            case FAST_OUT_SLOW_IN_INTERPOLATOR:
                return new FastOutSlowInInterpolator();
            case LINEAR_INTERPOLATOR:
                return new LinearInterpolator();
            case LINEAR_OUT_SLOW_IN_INTERPOLATOR:
                return new LinearOutSlowInInterpolator();
            case OVERSHOOT_INTERPOLATOR:
                return new OvershootInterpolator();
            default:
                return new LinearInterpolator();
        }
    }

    /*This is some working code for downloading a given URL to a given File object.
    The File object (outputFile) has just been created using new File(path),
    I haven't called createNewFile or anything.*/
    private static void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    public static void toggleHideyBar(AppCompatActivity activity) {

        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
            Log.i(TAG, "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Modal_Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    public static String getYouTubeID(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return "nothing";
        }
    }

    public static String getDisplayMetrics(AppCompatActivity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (metrics.densityDpi == DisplayMetrics.DENSITY_LOW) {
            return "ldpi";
        } else if (metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
            return "mdpi";
        } else if (metrics.densityDpi == DisplayMetrics.DENSITY_HIGH) {
            return "hdpi";
        } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
            return "xhdpi";
        } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XXHIGH) {
            return "xxhdpi";
        } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XXXHIGH) {
            return "xxxhdpi";
        } else {
            return "mdpi";
        }
    }

    public static String getCurrentVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;
        return currentVersion;
    }

    public static boolean checkWhetherConnectedToRaspberry(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            if (ssid.contains("prathamkolibri"))//RPiHotspotN
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    public static boolean checkWhetherConnectedToSelectedNetwork(Context context, String networkname) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            if (ssid.contains(networkname))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    public static boolean checkWhetherConnectedToFTP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            if (ssid.contains("PrathamHotspot_"))//ftp HotspotN
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    public static String getInternalPath(Context context) {
        File[] intDir = context.getExternalFilesDirs("");
        if (intDir.length > 1) {
            try {
                File file = new File(intDir[1].getAbsolutePath(), "hello.txt");
                if (!file.exists())
                    file.createNewFile();
                file.delete();
                PD_Constant.STORING_IN = "Internal Storage";
                return intDir[1].getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                PD_Constant.STORING_IN = "Internal Storage";
                return intDir[0].getAbsolutePath();
            }
        } else {
            PD_Constant.STORING_IN = "Internal Storage";
            return intDir[0].getAbsolutePath();
        }
    }

    public static String getObbPath(Context context) {
        File[] intDir = context.getObbDirs();
        if (intDir.length > 1) {
            return intDir[1].getAbsolutePath();
        } else {
            return intDir[0].getAbsolutePath();
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static Point getCenterPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0] + view.getWidth() / 2;
        int y = location[1] + view.getHeight() / 2;
        return new Point(x, y);
    }

    public static String getLanguageKeyword(String langcode) {
        if (langcode.equalsIgnoreCase("en"))
            langcode = PD_Constant.ENGLISH;
        else if (langcode.equalsIgnoreCase("hi"))
            langcode = PD_Constant.HINDI;
        else if (langcode.equalsIgnoreCase("mr"))
            langcode = PD_Constant.MARATHI;
        else if (langcode.equalsIgnoreCase("kn"))
            langcode = PD_Constant.KANNADA;
        else if (langcode.equalsIgnoreCase("te"))
            langcode = PD_Constant.TELUGU;
        else if (langcode.equalsIgnoreCase("bn"))
            langcode = PD_Constant.BENGALI;
        else if (langcode.equalsIgnoreCase("gu"))
            langcode = PD_Constant.GUJARATI;
        else if (langcode.equalsIgnoreCase("pnb"))
            langcode = PD_Constant.PUNJABI;
        else if (langcode.equalsIgnoreCase("ta"))
            langcode = PD_Constant.TAMIL;
        else if (langcode.equalsIgnoreCase("or"))
            langcode = PD_Constant.ODIYA;
        else if (langcode.equalsIgnoreCase("ml"))
            langcode = PD_Constant.MALAYALAM;
        else if (langcode.equalsIgnoreCase("as"))
            langcode = PD_Constant.ASSAMESE;
        else if (langcode.equalsIgnoreCase("ur"))
            langcode = PD_Constant.URDU;
        return langcode;
    }

    public static <T> T jsonToBean(String jsonResult, Class<T> clz) {
        Gson gson = new Gson();
        T t = gson.fromJson(jsonResult, clz);
        return t;
    }

    public static <T> String beanToJson(T clz) {
        Gson gson = new Gson();
        return gson.toJson(clz);
    }

    public static List<File> getCurrentFileList(String path) {
        if (path == null) {
            return null;
        }
        List<File> list = new ArrayList<>();
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            for (File f : file.listFiles()) {
                File wf = new File(f.getAbsolutePath());
                list.add(wf);
            }
        } else {
            return null;
        }
        Collections.sort(list);
        return list;
    }

    public static UUID getUUID() {
        return UUID.randomUUID();
    }

    public static String formatTime(final int timeMs) {
        if (timeMs < 1000) {
            return "00:00";
        }
        String result = "";
        final int totalSec = timeMs / 1000;
        final int hours = totalSec / 3600;
        final int minutes = (totalSec / 60) % 60;
        final int seconds = totalSec % 60;

        if (hours > 0) {
            result += hours + ":";
        }
        if (minutes >= 10) {
            result += minutes + ":";
        } else {
            result += "0" + minutes + ":";
        }
        if (seconds >= 10) {
            result += seconds;
        } else {
            result += "0" + seconds;
        }
        return result;
    }

    public static StateListDrawable createStateListDrawable(Drawable drawable, @ColorInt int drawableColor) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        int[] defaultStateSet = {-android.R.attr.state_pressed, -android.R.attr.state_focused, android.R.attr.state_enabled};
        stateListDrawable.addState(defaultStateSet, drawable);

        int[] focusedStateSet = {-android.R.attr.state_pressed, android.R.attr.state_focused};
        Drawable focusedDrawable = darkenDrawable(drawable, drawableColor, 0.7f);
        stateListDrawable.addState(focusedStateSet, focusedDrawable);

        int[] pressedStateSet = {android.R.attr.state_pressed};
        Drawable pressedDrawable = darkenDrawable(drawable, drawableColor, 0.6f);
        stateListDrawable.addState(pressedStateSet, pressedDrawable);

        int[] disableStateSet = {-android.R.attr.state_enabled};
        Drawable disableDrawable = darkenDrawable(drawable, drawableColor, 0.4f);
        stateListDrawable.addState(disableStateSet, disableDrawable);

        return stateListDrawable;
    }

    private static Drawable darkenDrawable(Drawable drawable, @ColorInt int drawableColor, float factor) {
        int color = darkenColor(drawableColor, factor);
        Drawable d = drawable.getConstantState().newDrawable().mutate();
        d.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return d;
    }

    @ColorInt
    private static int darkenColor(@ColorInt int color, float factor) {
        if (factor < 0 || factor > 1) {
            return color;
        }
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha,
                Math.max((int) (red * factor), 0),
                Math.max((int) (green * factor), 0),
                Math.max((int) (blue * factor), 0));
    }

    public static String getDeviceSerialID() {
        return Build.SERIAL;
    }

    public static String getDeviceID() {
        return Settings.Secure.getString(PrathamApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceWifiMACID() {
        if (!PrathamApplication.wiseF.isWifiEnabled())
            PrathamApplication.wiseF.enableWifi();
        WifiManager wifiManager = (WifiManager) PrathamApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        return macAddress;
    }

    public static int getRandomAvatar(String gender) {
        if (gender.equalsIgnoreCase("male"))
            return boy_avatars[new Random().nextInt(boy_avatars.length)];
        else
            return girl_avatars[new Random().nextInt(girl_avatars.length)];
    }

    public static int getRandomColorGradient() {
        if (colors != null) {
            int randomIndex = new Random().nextInt(colors.size());
            int randomColor = colors.get(randomIndex);
            return randomColor;
        } else {
            return 0;
        }
    }

    private static List<Integer> getAllMaterialColors() {
        try {
            XmlResourceParser xrp = PrathamApplication.getInstance().getResources().getXml(R.xml.android_material_design_colours);
            List<Integer> allColors = new ArrayList<>();
            int nextEvent;
            while ((nextEvent = xrp.next()) != XmlResourceParser.END_DOCUMENT) {
                String s = xrp.getName();
                if ("color".equals(s)) {
                    String color = xrp.nextText();
                    allColors.add(Color.parseColor(color));
                }
            }
            return allColors;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showDialog(Activity activity) {
        if (dialog == null) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.cat_loading_dialog);
        }
        dialog.show();
    }

    public static void dismissDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    public static long getAvailableSpaceInGB(Context context, String path) {
        final long SIZE_KB = 1024L;
        final long SIZE_GB = SIZE_KB * SIZE_KB * SIZE_KB;
        long availableSpace = -1L;
        StatFs stat = new StatFs(path);
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        Log.d(TAG, "getAvailableSpaceInGB:" + Formatter.formatFileSize(context, availableSpace));
        return availableSpace / SIZE_GB;
    }

    public static String formatSize(long size) {
        String suffix = null;
        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
            }
        }
        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    @SuppressLint("MissingPermission")
    public static void getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        EventMessage message = new EventMessage();
        message.setMessage(PD_Constant.CONNECTION_STATUS);
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                message.setConnection_resource(context.getResources().getDrawable(R.drawable.ic_dialog_connect_wifi_item));
                message.setConnection_name(PrathamApplication.wiseF.getCurrentNetwork().getSSID());
                EventBus.getDefault().post(message);
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String carrierName = manager.getNetworkOperatorName();
                message.setConnection_resource(context.getResources().getDrawable(R.drawable.ic_4g_network));
                message.setConnection_name(carrierName);
                EventBus.getDefault().post(message);
            }
        } else {
            message.setConnection_resource(context.getResources().getDrawable(R.drawable.ic_no_wifi));
            message.setConnection_name(PD_Constant.NO_CONNECTION);
        }
        EventBus.getDefault().post(message);
    }

    public static long folderSize(File directory) {
        long length = 0;
        File[] f = directory.listFiles();
        for (File file : f) {
            if (file != null) {
                if (file.isFile())
                    length += file.length();
                else
                    length += folderSize(file);
            }
        }
        return length;
    }

    public static String getMonthname() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        return month_name;
    }

    public static Drawable getDrawableAccordingToMonth(Context context) {
        String month_name = getMonthname();
        switch (month_name.toLowerCase()) {
            case "january":
                return context.getResources().getDrawable(R.drawable.january);
            case "february":
                return context.getResources().getDrawable(R.drawable.february);
            case "march":
                return context.getResources().getDrawable(R.drawable.march);
            case "april":
                return context.getResources().getDrawable(R.drawable.april);
            case "may":
                return context.getResources().getDrawable(R.drawable.may);
            case "june":
                return context.getResources().getDrawable(R.drawable.june);
            case "july":
                return context.getResources().getDrawable(R.drawable.july);
            case "august":
                return context.getResources().getDrawable(R.drawable.august);
            case "september":
                return context.getResources().getDrawable(R.drawable.september);
            case "october":
                return context.getResources().getDrawable(R.drawable.october);
            case "november":
                return context.getResources().getDrawable(R.drawable.november);
            case "december":
                return context.getResources().getDrawable(R.drawable.december);
            default:
                return null;
        }
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        fileOrDirectory.delete();
    }

    public static boolean isUsages(String name) {
        switch (name) {
            case "sessions.json":
                return true;
            case "logs.json":
                return true;
            case "attendance.json":
                return true;
            case "status.json":
                return true;
            case "scores.json":
                return true;
            default:
                return false;
        }
    }

    public static boolean isProfile(String name) {
        switch (name) {
            case "villages.json":
                return true;
            case "groups.json":
                return true;
            case "crls.json":
                return true;
            case "students.json":
                return true;
            default:
                return false;
        }
    }

    public static boolean checkIfPermissionGranted(Context context, String permission) {
        PackageManager pm = context.getPackageManager();
        int hasPerm = pm.checkPermission(permission, context.getPackageName());
        if (hasPerm != PackageManager.PERMISSION_GRANTED) return false;
        else return true;
    }


    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String readJSONFile(String filePath) {
        try {
            FileInputStream is = new FileInputStream(filePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
            return mResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static List<StorageInfo> getStorageList() {
        List<StorageInfo> list = new ArrayList<StorageInfo>();
        String def_path = Environment.getExternalStorageDirectory().getPath();
        boolean def_path_removable = Environment.isExternalStorageRemovable();
        String def_path_state = Environment.getExternalStorageState();
        boolean def_path_available = def_path_state.equals(Environment.MEDIA_MOUNTED)
                || def_path_state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean def_path_readonly = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);

        HashSet<String> paths = new HashSet<String>();
        int cur_removable_number = 1;

        if (def_path_available) {
            paths.add(def_path);
            list.add(0, new StorageInfo(def_path, def_path_readonly, def_path_removable, def_path_removable ? cur_removable_number++ : -1));
        }

        BufferedReader buf_reader = null;
        try {
            buf_reader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            Log.d(TAG, "/proc/mounts");
            while ((line = buf_reader.readLine()) != null) {
                Log.d(TAG, line);
                if (line.contains("vfat") || line.contains("/mnt")) {
                    StringTokenizer tokens = new StringTokenizer(line, " ");
                    String unused = tokens.nextToken(); //device
                    String mount_point = tokens.nextToken(); //mount point
                    if (paths.contains(mount_point)) {
                        continue;
                    }
                    unused = tokens.nextToken(); //file system
                    List<String> flags = Arrays.asList(tokens.nextToken().split(",")); //flags
                    boolean readonly = flags.contains("ro");

                    if (line.contains("/dev/block/vold")) {
                        if (!line.contains("/mnt/secure")
                                && !line.contains("/mnt/asec")
                                && !line.contains("/mnt/obb")
                                && !line.contains("/dev/mapper")
                                && !line.contains("tmpfs")) {
                            paths.add(mount_point);
                            list.add(new StorageInfo(mount_point, readonly, true, cur_removable_number++));
                        }
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (buf_reader != null) {
                try {
                    buf_reader.close();
                } catch (IOException ex) {
                }
            }
        }
        return list;
    }
}
