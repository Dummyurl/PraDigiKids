package com.pratham.pradigikids.ui.fragment_aaj_ka_sawal;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.design.card.MaterialCardView;
import android.support.v4.app.Fragment;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.CircularRevelLayout;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.custom.view_animator.ViewAnimator;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.models.Modal_AajKaSawal;
import com.pratham.pradigikids.models.Modal_ContentDetail;
import com.pratham.pradigikids.models.Modal_Score;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

import static com.pratham.pradigikids.PrathamApplication.scoreDao;

@EFragment(R.layout.frag_aaj_ka_sawal)
public class Fragment_AAJ_KA_SAWAL extends Fragment {
    private static final int HIGHLIGHT_SELECTED_ANSWER = 3;
    @ViewById(R.id.aks_reveal)
    CircularRevelLayout aks_reveal;
    @ViewById(R.id.aaj_txt_question)
    TextView aaj_txt_question;
    @ViewById(R.id.card_option_one)
    MaterialCardView card_option_one;
    @ViewById(R.id.card_option_two)
    MaterialCardView card_option_two;
    @ViewById(R.id.card_option_three)
    MaterialCardView card_option_three;
    @ViewById(R.id.card_option_four)
    MaterialCardView card_option_four;
    @ViewById(R.id.txt_option_one)
    TextView txt_option_one;
    @ViewById(R.id.txt_option_two)
    TextView txt_option_two;
    @ViewById(R.id.txt_option_three)
    TextView txt_option_three;
    @ViewById(R.id.txt_option_four)
    TextView txt_option_four;
    @ViewById(R.id.aaj_play_video)
    ImageView aaj_play_video;

    private String selectedAnswer = "";
    private Modal_AajKaSawal selectedAajKaSawal;
    private String startTime;
    private Modal_ContentDetail vidContent = null;
    private final MediaPlayer.OnCompletionListener applauseCompletionListener = mp -> Objects.requireNonNull(getActivity()).onBackPressed();
    private MediaPlayer wrong_mp;
    private MediaPlayer correct_mp;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HIGHLIGHT_SELECTED_ANSWER:
                    wrong_mp.start();
                    if (txt_option_one.getText().toString().equalsIgnoreCase(selectedAajKaSawal.getAnswer()))
                        ViewAnimator.animate(card_option_one)
                                .flash()
                                .duration(350)
                                .start();
                    else if (txt_option_two.getText().toString().equalsIgnoreCase(selectedAajKaSawal.getAnswer()))
                        ViewAnimator.animate(card_option_two)
                                .flash()
                                .duration(350)
                                .start();
                    else if (txt_option_three.getText().toString().equalsIgnoreCase(selectedAajKaSawal.getAnswer()))
                        ViewAnimator.animate(card_option_three)
                                .flash()
                                .duration(350)
                                .start();
                    else if (txt_option_four.getText().toString().equalsIgnoreCase(selectedAajKaSawal.getAnswer()))
                        ViewAnimator.animate(card_option_four)
                                .flash()
                                .duration(350)
                                .start();
                    break;
            }
        }
    };

    @AfterViews
    public void init() {
        wrong_mp = MediaPlayer.create(getActivity(), R.raw.wrong_buzzer);
        correct_mp = MediaPlayer.create(getActivity(), R.raw.applause);
        correct_mp.setOnCompletionListener(applauseCompletionListener);
        wrong_mp.setOnCompletionListener(applauseCompletionListener);
        startTime = PD_Utility.getCurrentDateTime();
        if (getArguments() != null)
            showQuestion(getArguments().getParcelable(PD_Constant.AKS_QUESTION));
        else Objects.requireNonNull(getActivity()).onBackPressed();
        aks_reveal.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int[] outLocation = new int[2];
                aaj_play_video.getLocationOnScreen(outLocation);
                outLocation[0] += aaj_play_video.getWidth() / 2;
                aks_reveal.getViewTreeObserver().removeOnPreDrawListener(this);
                aks_reveal.revealFrom(outLocation[0], outLocation[1], 0);
                return true;
            }
        });
    }

    @Background
    public void parseAksJSON(Modal_AajKaSawal sawal) {
    }

    @UiThread
    public void showQuestion(Modal_AajKaSawal aajKaSawal) {
        selectedAajKaSawal = aajKaSawal;
        aaj_txt_question.setText(aajKaSawal.getQuestion());
        txt_option_one.setText(aajKaSawal.getOption1());
        txt_option_two.setText(aajKaSawal.getOption2());
        txt_option_three.setText(aajKaSawal.getOption3());
        txt_option_four.setText(aajKaSawal.getOption4());
    }

    @Click(R.id.aaj_btn_skip)
    public void setskip() {
        PrathamApplication.bubble_mp.start();
        addScoreToDB(0, true, false);
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    @Click(R.id.aaj_okay)
    public void setOkay() {
        PrathamApplication.bubble_mp.start();
        if (selectedAnswer.equalsIgnoreCase(selectedAajKaSawal.getAnswer())) {
            correct_mp.start();
            addScoreToDB(10, false, false);
        } else {
            addScoreToDB(0, false, false);
            mHandler.sendEmptyMessage(HIGHLIGHT_SELECTED_ANSWER);
        }
    }

    @Click(R.id.aaj_play_video)
    public void setPlayVideo() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
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

    @Subscribe
    public void messageReceived(EventMessage message) {
        if (message != null) {
            if (message.getMessage().equalsIgnoreCase(PD_Constant.VIDEO_PLAYER_BACK_PRESS)) {
                addScoreToDB(0, true, true);
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        }
    }

    @Click(R.id.card_option_one)
    public void setOption1() {
        card_option_one.setCardBackgroundColor(getResources().getColor(R.color.aks_selected));
        card_option_two.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        card_option_three.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        card_option_four.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        selectedAnswer = txt_option_one.getText().toString();
    }

    @Click(R.id.card_option_two)
    public void setOption2() {
        card_option_one.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        card_option_two.setCardBackgroundColor(getResources().getColor(R.color.aks_selected));
        card_option_three.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        card_option_four.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        selectedAnswer = txt_option_two.getText().toString();
    }

    @Click(R.id.card_option_three)
    public void setOption3() {
        card_option_one.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        card_option_two.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        card_option_three.setCardBackgroundColor(getResources().getColor(R.color.aks_selected));
        card_option_four.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        selectedAnswer = txt_option_three.getText().toString();
    }

    @Click(R.id.card_option_four)
    public void setOption4() {
        card_option_one.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        card_option_two.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        card_option_three.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
        card_option_four.setCardBackgroundColor(getResources().getColor(R.color.aks_selected));
        selectedAnswer = txt_option_four.getText().toString();
    }

    @Background
    public void addScoreToDB(int ttlScore, boolean isSkipped, boolean isClosed) {
        String endTime = PD_Utility.getCurrentDateTime();
        Modal_Score modalScore = new Modal_Score();
        modalScore.setSessionID(FastSave.getInstance().getString(PD_Constant.SESSIONID, ""));
        if (PrathamApplication.isTablet)
            modalScore.setGroupID(FastSave.getInstance().getString(PD_Constant.GROUPID, "no_group"));
        else
            modalScore.setStudentID(FastSave.getInstance().getString(PD_Constant.STUDENTID, "no_student"));
        modalScore.setDeviceID(PD_Utility.getDeviceID());
        modalScore.setResourceID(selectedAajKaSawal.getResourceId());
        modalScore.setQuestionId(Integer.parseInt(selectedAajKaSawal.getQueId()));
        modalScore.setScoredMarks(ttlScore);
        modalScore.setTotalMarks(10);
        modalScore.setStartDateTime(startTime);
        modalScore.setEndDateTime(endTime);
        boolean hintVideoViewed = false;
        modalScore.setLevel(990);
        if (isClosed)
            modalScore.setLabel("Video Closed. Sawal was not attempted");
        else
            modalScore.setLabel(isSkipped ? "Skipped" : "Video not Viewed");
        modalScore.setSentFlag(0);
        scoreDao.insert(modalScore);
    }
}
