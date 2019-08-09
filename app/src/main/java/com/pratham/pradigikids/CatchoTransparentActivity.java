package com.pratham.pradigikids;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.models.Modal_Log;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import net.alhazmy13.catcho.library.Catcho;
import net.alhazmy13.catcho.library.error.CatchoError;

import static com.pratham.pradigikids.PrathamApplication.logDao;

public class CatchoTransparentActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CatchoError error = (CatchoError) getIntent().getSerializableExtra(Catcho.ERROR);
        Modal_Log log = new Modal_Log();
        log.setCurrentDateTime(PD_Utility.getCurrentDateTime());
        log.setErrorType("ERROR");
        log.setExceptionMessage(error.toString());
        log.setExceptionStackTrace(error.getError());
        log.setMethodName("NO_METHOD");
        log.setSessionId(FastSave.getInstance().getString(PD_Constant.SESSIONID, "no_session"));
        log.setDeviceId(PD_Utility.getDeviceSerialID());
        logDao.insertLog(log);
        finish();
    }
}
