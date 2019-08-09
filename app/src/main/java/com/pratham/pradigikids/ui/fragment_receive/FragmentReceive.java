package com.pratham.pradigikids.ui.fragment_receive;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.CircularProgress;
import com.pratham.pradigikids.ftpSettings.FsService;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.models.File_Model;
import com.pratham.pradigikids.models.Modal_ContentDetail;
import com.pratham.pradigikids.models.Modal_ReceivingFilesThroughFTP;
import com.pratham.pradigikids.ui.fragment_content.FragmentContent_;
import com.pratham.pradigikids.ui.fragment_share_recieve.ContractShare;
import com.pratham.pradigikids.ui.fragment_share_recieve.ReceivedFileListAdapter;
import com.pratham.pradigikids.ui.fragment_share_recieve.SharePresenter;
import com.pratham.pradigikids.util.HotspotUtils;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;
import com.pratham.pradigikids.util.WifiUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EFragment(R.layout.fragment_receive)
public class FragmentReceive extends Fragment implements ContractShare.shareView {
    private static final int RECIEVED_FROM_TETHERING_ACTIVITY = 1;
    private static final int CREATE_HOTSPOT_ACCORDING_TO_ANDROID_VERSION = 3;
    private static final String TAG = FragmentReceive.class.getSimpleName();

    @ViewById(R.id.shareCircle)
    RelativeLayout shareCircle;
    @ViewById(R.id.circleProgress)
    CircularProgress circleProgress;
    @ViewById(R.id.status)
    EditText status;
    @ViewById(R.id.rl_hotspot_qr)
    LinearLayout rl_hotspot_qr;
    @ViewById(R.id.img_hotspot_qr)
    ImageView img_hotspot_qr;
    @ViewById(R.id.txt_scan_qr_)
    TextView txt_scan_qr_;
    @ViewById(R.id.rv_files)
    RecyclerView rv_files;
    @ViewById(R.id.ll_receive_files)
    LinearLayout ll_receive_files;

    @Bean(SharePresenter.class)
    ContractShare.sharePresenter sharePresenter;
    private HotspotUtils hotspotUtils;
    private boolean isHotspotEnabled = false;
    private ReceivedFileListAdapter receivedFileListAdapter;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CREATE_HOTSPOT_ACCORDING_TO_ANDROID_VERSION:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                            try {
                                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
                                intent.setComponent(cn);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                isHotspotEnabled = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else
                            animateHotspotCreation();
                    } else
                        animateHotspotCreation();
                    break;
                case PD_Constant.ApCreateApSuccess:
//                    String s = PD_Utility.getLocalHostName();
//                    SpannableString ss = new SpannableString(s);
//                    ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 4, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
//                    ss.setSpan(new RelativeSizeSpan(1.2f), 0, 4, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
//                    status.setText(ss);
                    circleProgress.finishAnim();
                    new Handler().postDelayed(() -> createHotspotQrCode(), 1500);
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @AfterViews
    public void initialize() {
        hotspotUtils = HotspotUtils.getInstance(getActivity(), mHandler);
        createHotspot();
    }

    private void createHotspot() {
        mHandler.sendEmptyMessage(CREATE_HOTSPOT_ACCORDING_TO_ANDROID_VERSION);
    }

    @Override
    public void onResume() {
        super.onResume();
        sharePresenter.setView(FragmentReceive.this);
        if (isHotspotEnabled) {
            isHotspotEnabled = false;
            onActivityResult(RECIEVED_FROM_TETHERING_ACTIVITY, 0, null);
        }
    }

    @Override
    public void hotspotStarted() {

    }

    @Override
    public void onWifiConnected(String ssid) {

    }

    @Override
    public void fileItemClicked(Modal_ContentDetail detail, int position) {

    }

    @Override
    public void sendItemChecked(File_Model detail, int position) {

    }

    @Override
    public void showFilesList(ArrayList<File_Model> contents, String parentId) {

    }

    @Override
    public void disconnectFTP() {
        new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("PraDigi")
                .setMessage("Do you want to Disconnect?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    hotspotUtils.disable();
                    Intent intent = new Intent(FsService.ACTION_STOP_FTPSERVER);
                    intent.setPackage(Objects.requireNonNull(getActivity()).getPackageName());
                    getActivity().sendBroadcast(intent);
                    closeFTPJoin();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @UiThread
    @Override
    public void closeFTPJoin() {
        Bundle bundle = new Bundle();
        bundle.putInt(PD_Constant.REVEALX, 0);
        bundle.putInt(PD_Constant.REVEALY, 0);
        PD_Utility.showFragment(getActivity(), new FragmentContent_(), R.id.main_frame,
                bundle, FragmentContent_.class.getSimpleName());
        Fragment fragment = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void ftpConnected_showFolders() {

    }

    @Override
    public void ftpConnectionFailed() {

    }

    @Override
    public void animateHamburger() {

    }

    @UiThread
    public void animateHotspotCreation() {
        startCreateAnim();
        mHandler.postDelayed(() -> hotspotUtils.enableConfigured("pratham", null), 2000);
    }

    private void startCreateAnim() {
        shareCircle.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).
                setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
        shareCircle.setVisibility(View.VISIBLE);
        circleProgress.startCircleAnim(1000);
        circleProgress.startAnim(2000);
    }

    private void createHotspotQrCode() {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                PrathamApplication.getInstance().registerFtpReceiver();
//            }
            startServer();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PD_Constant.FTP_HOTSPOT_SSID, PD_Constant.HOTSPOT_SSID);
            jsonObject.put(PD_Constant.FTP_HOTSPOT_PASS, PD_Constant.HOTSPOT_PASSWORD);
            jsonObject.put(PD_Constant.FTP_KEYMGMT, PD_Constant.FTP_HOTSPOT_KEYMGMT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                jsonObject.put(PD_Constant.FTP_IP, FsService.getLocalInetAddress().getHostAddress());
            else jsonObject.put(PD_Constant.FTP_IP, "192.168.43.1");
            QRCodeWriter formatWriter = new QRCodeWriter();
            BitMatrix bitMatrix = formatWriter.encode(jsonObject.toString(), BarcodeFormat.QR_CODE, 512, 512);
            Bitmap bitmap = createBitmap(bitMatrix);
            shareCircle.setVisibility(View.GONE);
            rl_hotspot_qr.setVisibility(View.VISIBLE);
            rl_hotspot_qr.setClickable(true);
            img_hotspot_qr.setImageBitmap(bitmap);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startServer() {
        Intent intent = new Intent(FsService.ACTION_START_FTPSERVER);
        intent.setPackage(Objects.requireNonNull(getActivity()).getPackageName());
        getActivity().sendBroadcast(intent);
    }

    private Bitmap createBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
//        int[] pixels = new int[width * height];
//        for (int y = 0; y < height; y++) {
//            int offset = y * width;
//            for (int x = 0; x < width; x++) {
//                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    @Subscribe
    public void messageRecievedInShare(EventMessage message) {
        if (message != null) {
            if (message.getMessage().equalsIgnoreCase(PD_Constant.FTP_CLIENT_CONNECTED)) {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    rl_hotspot_qr.setVisibility(View.GONE);
                    ll_receive_files.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Client Connected", Toast.LENGTH_SHORT).show();
                });
            } else if (message.getMessage().equalsIgnoreCase(PD_Constant.FILE_RECEIVE_COMPLETE)) {
                List<Modal_ReceivingFilesThroughFTP> list = receivedFileListAdapter.getList();
                Modal_ReceivingFilesThroughFTP ftpItem = null;
                int pos = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getGameName().contains(message.getFile_name())) {
                        list.get(i).setReceived(true);
                        ftpItem = list.get(i);
                        pos = i;
                    }
                }
                if (ftpItem != null)
                    receivedFileListAdapter.notifyItemChanged(pos, ftpItem);
            } else if (message.getMessage().equalsIgnoreCase(PD_Constant.CLOSE_FTP_SERVER) ||
                    message.getMessage().equalsIgnoreCase(PD_Constant.SHARE_BACK))
                disconnectFTP();
        }
    }

    @Subscribe
    public void filesCurrentlyRecieving(File recievedFile) {
        if (recievedFile != null) {
            sharePresenter.showFilesRecieving(recievedFile);
        }
    }

    @UiThread
    @Override
    public void showRecieving(ArrayList<Modal_ReceivingFilesThroughFTP> filesRecieving) {
        if (receivedFileListAdapter == null) {
            //initialize adapter
            receivedFileListAdapter = new ReceivedFileListAdapter(getActivity());
            rv_files.setHasFixedSize(true);
            rv_files.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rv_files.setAdapter(receivedFileListAdapter);
            receivedFileListAdapter.submitList(filesRecieving);
        } else {
            receivedFileListAdapter.submitList(filesRecieving);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharePresenter.viewDestroyed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharePresenter.viewDestroyed();
    }

}
