package com.pratham.pradigikids.ui.video_player;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.media_controller.PlayerControlView;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.models.Modal_AajKaSawal;
import com.pratham.pradigikids.models.Modal_Score;
import com.pratham.pradigikids.ui.fragment_aaj_ka_sawal.Fragment_AAJ_KA_SAWAL;
import com.pratham.pradigikids.ui.fragment_aaj_ka_sawal.Fragment_AAJ_KA_SAWAL_;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Objects;

import static com.pratham.pradigikids.PrathamApplication.scoreDao;

@EFragment(R.layout.fragment_generic_vplayer)
public class Fragment_VideoPlayer extends Fragment {

    private static final int AAJ_KA_SAWAL_FOR_THIS_VIDEO = 1;
    private static final int SHOW_SAWAL = 2;
    @ViewById(R.id.videoView)
    VideoView videoView;
    @ViewById(R.id.player_control_view)
    PlayerControlView player_control_view;

    private String videoPath;
    private String startTime = "no_resource";
    private String resId;
    private long videoDuration = 0;
    private boolean isHint = false;
    private Modal_AajKaSawal videoSawal = null;
    @SuppressLint("HandlerLeak")
    private final
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AAJ_KA_SAWAL_FOR_THIS_VIDEO:
                    //aaj_ka_sawal is downloaded in main activity.
                    try {
                        String filename = "AajKaSawal_" + FastSave.getInstance().getString(PD_Constant.LANGUAGE, PD_Constant.HINDI) + ".json";
                        File aksFile = new File(PrathamApplication.pradigiPath + "/" + filename);
                        if (aksFile.exists()) {
                            String aks = PD_Utility.readJSONFile(aksFile.getAbsolutePath());
                            Modal_AajKaSawal rootAajKaSawal = new Gson().fromJson(aks, Modal_AajKaSawal.class);
                            for (Modal_AajKaSawal subjectSawal : rootAajKaSawal.getNodelist()) {
                                boolean found = false;
                                for (Modal_AajKaSawal aajKaSawal : subjectSawal.getNodelist()) {
                                    if (aajKaSawal.getResourceId().equalsIgnoreCase(resId)) {
                                        found = true;
                                        videoSawal = aajKaSawal;
                                        break;
                                    }
                                }
                                if (found) break;
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    break;
                case SHOW_SAWAL:
                    Bundle aksbundle = new Bundle();
                    aksbundle.putParcelable(PD_Constant.AKS_QUESTION, videoSawal);
                    PD_Utility.addFragment(getActivity(), new Fragment_AAJ_KA_SAWAL_(), R.id.vp_frame,
                            aksbundle, Fragment_AAJ_KA_SAWAL.class.getSimpleName());
                    break;
            }
        }
    };

    @AfterViews
    public void initialize() {
        videoPath = Objects.requireNonNull(getArguments()).getString("videoPath");
        resId = getArguments().getString("resId");
        isHint = getArguments().getBoolean("hint", false);
        mHandler.sendEmptyMessage(AAJ_KA_SAWAL_FOR_THIS_VIDEO);
        initializePlayer(videoPath);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initializePlayer(String videoPath) {
        videoView.setVideoPath(videoPath);
        videoView.setMediaController(player_control_view.getMediaControllerWrapper());
        videoView.start();
        videoView.setOnPreparedListener(mp -> {
            startTime = PD_Utility.getCurrentDateTime();
            player_control_view.show();
            videoDuration = videoView.getDuration();
        });
        videoView.setOnCompletionListener(mp -> {
            if (!isHint) addScoreToDB();
            if (videoSawal == null)
                Objects.requireNonNull(getActivity()).onBackPressed();
            else
                mHandler.sendEmptyMessage(SHOW_SAWAL);
        });
    }

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

    @Subscribe
    public void messageReceived(EventMessage message) {
        if (message != null) {
            if (message.getMessage().equalsIgnoreCase(PD_Constant.VIDEO_PLAYER_BACK_PRESS)) {
                if (!isHint) addScoreToDB();
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        }
    }

    @Background
    public void addScoreToDB() {
        String endTime = PD_Utility.getCurrentDateTime();
        Modal_Score modalScore = new Modal_Score();
        modalScore.setSessionID(FastSave.getInstance().getString(PD_Constant.SESSIONID, ""));
        if (PrathamApplication.isTablet)
            modalScore.setGroupID(FastSave.getInstance().getString(PD_Constant.GROUPID, "no_group"));
        else
            modalScore.setStudentID(FastSave.getInstance().getString(PD_Constant.STUDENTID, "no_student"));
        modalScore.setDeviceID(PD_Utility.getDeviceID());
        modalScore.setResourceID(resId);
        modalScore.setQuestionId(0);
        modalScore.setScoredMarks((int) PD_Utility.getTimeDifference(startTime, endTime));
        modalScore.setTotalMarks((int) videoDuration);
        modalScore.setStartDateTime(startTime);
        modalScore.setEndDateTime(endTime);
        modalScore.setLevel(0);
        modalScore.setLabel("_");
        modalScore.setSentFlag(0);
        scoreDao.insert(modalScore);
    }
}
