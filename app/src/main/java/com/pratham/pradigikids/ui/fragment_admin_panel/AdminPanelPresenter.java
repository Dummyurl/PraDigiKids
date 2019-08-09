package com.pratham.pradigikids.ui.fragment_admin_panel;

import android.content.Context;
import android.os.Handler;

import com.pratham.pradigikids.models.Modal_Crl;
import com.pratham.pradigikids.services.PrathamSmartSync;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import static com.pratham.pradigikids.PrathamApplication.crLdao;
import static com.pratham.pradigikids.PrathamApplication.statusDao;

/**
 * Created by PEF on 19/11/2018.
 */
@EBean
public class AdminPanelPresenter implements AdminPanelContract.AdminPanelPresenter {
    private AdminPanelContract.AdminPanelView adminPanelView;

    public AdminPanelPresenter(Context context) {
        Context context1 = context;
    }

    @Override
    public void setView(AdminPanelFragment adminPanelFragment) {
        this.adminPanelView = adminPanelFragment;
    }

    @Override
    public void checkLogin(String userName, String password) {
        checkLogin_(userName, password);
    }

    @Background
    public void checkLogin_(String userName, String password) {
        // if user name and password are admin then navigate to Download activity otherWise admin activity
        if (userName.equals("pratham") && password.equals("pratham")) {
            adminPanelView.openPullDataFragment();
        } else {
            // assign push logic
            Modal_Crl loggedCrl = crLdao.checkUserValidation(userName, password);
            if (loggedCrl != null) {
                statusDao.updateValue("CRLID", loggedCrl.getCRLId());
                adminPanelView.onLoginSuccess();
            } else {
                //userNAme and password may be wrong
                adminPanelView.onLoginFail();
            }
        }
    }

    @Override
    public void pushData() {
        adminPanelView.showPushingDialog();
        //Necessary to add some delay, as the ui will change very frequent to notice
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PrathamSmartSync.pushUsageToServer(true);
            }
        }, 1500);
    }

}
