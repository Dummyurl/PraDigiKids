package com.pratham.pradigikids.ui.connect_dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isupatches.wisefy.callbacks.AddNetworkCallbacks;
import com.isupatches.wisefy.callbacks.ConnectToNetworkCallbacks;
import com.isupatches.wisefy.callbacks.GetNearbyAccessPointsCallbacks;
import com.isupatches.wisefy.callbacks.GetSavedNetworkCallbacks;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.BlurPopupDialog.BlurPopupWindow;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pratham.pradigikids.PrathamApplication.wiseF;

public class ConnectDialog extends BlurPopupWindow implements ConnectInterface {

    private static final String TAG = ConnectDialog.class.getSimpleName();
    private Context context;
    @BindView(R.id.wifi_list)
    RecyclerView wifi_list;
    //    @BindView(R.id.refresh)
//    LottieAnimationView refresh;
    @BindView(R.id.dialog_txt)
    TextView dialog_txt;
    @BindView(R.id.rl_enter_password)
    RelativeLayout rl_enter_password;
    @BindView(R.id.rl_connect_option)
    RelativeLayout rl_connect_option;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.et_wifi_pass)
    EditText et_wifi_pass;
    @BindView(R.id.wifi_back)
    ImageView wifi_back;

    private String ssid;
    private String password;
    private ArrayList<String> wifi_result;

    public ConnectDialog(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("HandlerLeak")
    private
    Handler mHandler = new Handler() {
        @SuppressLint({"MissingPermission", "SetTextI18n"})
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    WifiAdapter adapter = new WifiAdapter(getContext(), wifi_result, ConnectDialog.this);
                    wifi_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    wifi_list.setHasFixedSize(true);
                    wifi_list.setAdapter(adapter);
                    break;
                case 1:
                    rl_progress.setVisibility(GONE);
                    dialog_txt.setText("Failed");
                    break;
                case 2:
                    if (PrathamApplication.wiseF.isDeviceConnectedToMobileNetwork()) {
                        wifi_list.setVisibility(GONE);
                        rl_connect_option.setVisibility(VISIBLE);
                    } else if (PrathamApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
                        wifi_list.setVisibility(VISIBLE);
                        rl_connect_option.setVisibility(GONE);
                        PrathamApplication.wiseF.getNearbyAccessPoints(true, nearbyAccessPointsCallbacks);
                    } else {
                        wifi_list.setVisibility(VISIBLE);
                        rl_connect_option.setVisibility(GONE);
                        if (!PrathamApplication.wiseF.isWifiEnabled())
                            PrathamApplication.wiseF.enableWifi();
                        PrathamApplication.wiseF.getNearbyAccessPoints(true, nearbyAccessPointsCallbacks);
                    }
                    break;
                case 3:
                    try {
                        @SuppressLint("WrongConstant")
                        Object sbservice = getContext().getSystemService("statusbar");
                        @SuppressLint("PrivateApi") Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                        Method showsb = statusbarManager.getMethod("expand");
                        showsb.invoke(sbservice);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    rl_progress.setVisibility(VISIBLE);
                    break;
            }
        }
    };
    private final GetNearbyAccessPointsCallbacks nearbyAccessPointsCallbacks = new GetNearbyAccessPointsCallbacks() {
        @Override
        public void retrievedNearbyAccessPoints(@NotNull List<ScanResult> scanResults) {
            wifi_result = new ArrayList<>();
            for (ScanResult sc : scanResults)
                wifi_result.add(sc.SSID);
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void wisefyFailure(int i) {
            Log.d("response:::", "not found nearby networks");
        }
    };
    private final GetSavedNetworkCallbacks savedNetworkCallbacks = new GetSavedNetworkCallbacks() {
        @Override
        public void savedNetworkNotFound() {
            if (!password.isEmpty())
                addNetwork(ssid, password);
            else
                addOpenNetwork(ssid);
        }

        @Override
        public void retrievedSavedNetwork(@NotNull WifiConfiguration wifiConfiguration) {
            connectToNetwork(ssid);
        }

        @Override
        public void wisefyFailure(int i) {
            Log.d(TAG, "wisefyFailure:");
            mHandler.sendEmptyMessage(1);
        }
    };

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    protected View createContentView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_connecction, parent, false);
        ButterKnife.bind(this, view);
        mHandler = new Handler();
        this.isDismissOnTouchBackground();
        this.isDismissOnClickBack();
        new Handler().postDelayed(() -> mHandler.sendEmptyMessage(2), 300);
        return view;
    }

    private void addOpenNetwork(String ssid) {
        PrathamApplication.wiseF.addOpenNetwork(ssid, new AddNetworkCallbacks() {
            @Override
            public void failureAddingNetwork(int i) {
                Log.d(TAG, "failureAddingNetwork: ");
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void networkAdded(int i, @NotNull WifiConfiguration wifiConfiguration) {
                connectToNetwork(ssid);
            }

            @Override
            public void wisefyFailure(int i) {
                Log.d(TAG, "wisefyFailure: ");
                mHandler.sendEmptyMessage(1);
            }
        });
    }

    private void addNetwork(String ssid, String pass) {
        PrathamApplication.wiseF.addWPA2Network(ssid, pass, new AddNetworkCallbacks() {
            @Override
            public void failureAddingNetwork(int i) {
                Log.d(TAG, "failureAddingNetwork: ");
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void networkAdded(int i, @NotNull WifiConfiguration wifiConfiguration) {
                connectToNetwork(ssid);
            }

            @Override
            public void wisefyFailure(int i) {
                Log.d(TAG, "wisefyFailure: ");
                mHandler.sendEmptyMessage(1);
            }
        });
    }

    private void connectToNetwork(String ssid) {
        PrathamApplication.wiseF.connectToNetwork(ssid, 10000, new ConnectToNetworkCallbacks() {
            @Override
            public void connectedToNetwork() {
                dismiss();
            }

            @Override
            public void failureConnectingToNetwork() {
                mHandler.sendEmptyMessage(1);
                Log.d(TAG, "failureConnectingToNetwork: ");
            }

            @Override
            public void networkNotFoundToConnectTo() {
                mHandler.sendEmptyMessage(1);
                Log.d(TAG, "networkNotFoundToConnectTo: ");
            }

            @Override
            public void wisefyFailure(int i) {
                mHandler.sendEmptyMessage(1);
                Log.d(TAG, "wisefyFailure: ");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void wifiClicked(String wifi_name) {
        PrathamApplication.bubble_mp.start();
        ssid = wifi_name;
        if (ssid.equalsIgnoreCase(PD_Constant.PRATHAM_KOLIBRI_HOTSPOT)) {
            mHandler.sendEmptyMessage(4);
            password = PD_Constant.WIFI_AP_PASSWORD;
            dialog_txt.setText("Connecting...");
            if (!PD_Utility.checkWhetherConnectedToSelectedNetwork(PrathamApplication.getInstance(), ssid)) {
                wiseF.disconnectFromCurrentNetwork();
                wiseF.getSavedNetwork(ssid, savedNetworkCallbacks);
            } else {
                dismiss();
            }
        } else {
            rl_enter_password.setVisibility(VISIBLE);
            wifi_back.setVisibility(VISIBLE);
        }
    }

    @OnClick(R.id.btn_connect_wifi)
    public void setBtnConnectWifi() {
        PrathamApplication.bubble_mp.start();
        if (!PD_Utility.checkWhetherConnectedToSelectedNetwork(PrathamApplication.getInstance(), ssid)) {
            mHandler.sendEmptyMessage(4);
            password = et_wifi_pass.getText().toString();
            wiseF.disconnectFromCurrentNetwork();
            wiseF.getSavedNetwork(ssid, savedNetworkCallbacks);
        }
    }

    @OnClick(R.id.btn_try_again)
    public void setTryButton() {
        PrathamApplication.bubble_mp.start();
        if (PrathamApplication.wiseF.isDeviceConnectedToMobileNetwork())
            mHandler.sendEmptyMessage(3);
        else {
            rl_connect_option.setVisibility(GONE);
            wifi_list.setVisibility(VISIBLE);
        }
    }

    @OnClick(R.id.wifi_refresh)
    public void setRefresh() {
        PrathamApplication.bubble_mp.start();
        mHandler.sendEmptyMessage(2);
    }

    @OnClick(R.id.wifi_back)
    public void setWifiBack() {
        rl_connect_option.setVisibility(GONE);
        rl_enter_password.setVisibility(GONE);
        wifi_list.setVisibility(VISIBLE);
        wifi_back.setVisibility(GONE);
    }

    public static class Builder extends BlurPopupWindow.Builder<ConnectDialog> {
        public Builder(Context context) {
            super(context);
            this.setScaleRatio(0.25f).setBlurRadius(8).setTintColor(0x30000000);
        }

        @Override
        protected ConnectDialog createPopupWindow() {
            return new ConnectDialog(mContext);
        }
    }
}
