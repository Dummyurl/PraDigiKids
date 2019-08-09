package com.pratham.pradigikids.ui.video_player;

import android.os.Bundle;

import com.pratham.pradigikids.BaseActivity;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.greenrobot.eventbus.EventBus;

@EActivity(R.layout.activity_vplayer)
public class Activity_VPlayer extends BaseActivity {

    @AfterViews
    public void initialize() {
        Bundle bundle = new Bundle();
        bundle.putString("videoPath", getIntent().getStringExtra("videoPath"));
        bundle.putString("resId", getIntent().getStringExtra("resId"));
        bundle.putBoolean("hint", getIntent().getBooleanExtra("hint", false));
        PD_Utility.showFragment(this, new Fragment_VideoPlayer_(), R.id.vp_frame,
                bundle, Fragment_VideoPlayer.class.getSimpleName());
    }

    @Click(R.id.close_video)
    public void setClose_video() {
        EventMessage message = new EventMessage();
        message.setMessage(PD_Constant.VIDEO_PLAYER_BACK_PRESS);
        EventBus.getDefault().post(message);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Runtime rs = Runtime.getRuntime();
        rs.freeMemory();
        rs.gc();
        rs.freeMemory();
        this.finish();
        overridePendingTransition(R.anim.nothing, R.anim.pop_out);
    }
}
