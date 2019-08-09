package com.pratham.pradigikids;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.androidnetworking.AndroidNetworking;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.isupatches.wisefy.WiseFy;
import com.pratham.pradigikids.async.ReadBackupDb;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.dbclasses.AttendanceDao;
import com.pratham.pradigikids.dbclasses.CRLdao;
import com.pratham.pradigikids.dbclasses.GroupDao;
import com.pratham.pradigikids.dbclasses.LogDao;
import com.pratham.pradigikids.dbclasses.ModalContentDao;
import com.pratham.pradigikids.dbclasses.PrathamDatabase;
import com.pratham.pradigikids.dbclasses.ScoreDao;
import com.pratham.pradigikids.dbclasses.SessionDao;
import com.pratham.pradigikids.dbclasses.StatusDao;
import com.pratham.pradigikids.dbclasses.StudentDao;
import com.pratham.pradigikids.dbclasses.VillageDao;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by HP on 09-08-2017.
 */
public class PrathamApplication extends Application {
    private static PrathamApplication mInstance;
    public static WiseFy wiseF;
    public static String pradigiPath = "";
    public static MediaPlayer bubble_mp;
    /*Before building signed apk
     * Check Todo
     * Check Catcho in BaseActivity
     * Check baseUrl in PDConstant
     * increase version before generating signed apk
     */
    public static boolean isTablet = false;
    public static boolean useSatelliteGPS = false;
    public static boolean contentExistOnSD = false;
    public static String contentSDPath = "";
    public static AttendanceDao attendanceDao;
    public static CRLdao crLdao;
    public static GroupDao groupDao;
    public static ModalContentDao modalContentDao;
    public static ScoreDao scoreDao;
    public static SessionDao sessionDao;
    public static StatusDao statusDao;
    public static StudentDao studentDao;
    public static VillageDao villageDao;
    public static LogDao logDao;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mInstance == null) {
            mInstance = this;
        }
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//        isTablet = PD_Utility.isTablet(this);
        Fresco.initialize(this);
        FastSave.init(getApplicationContext());
        initializeDatabaseDaos();
//       copyBackupDb();
        bubble_mp = MediaPlayer.create(this, R.raw.bubble_pop);
        setPradigiPath();
        wiseF = new WiseFy.Brains(getApplicationContext()).logging(true).getSmarts();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
    }

    /*private void copyBackupDb() {
        try {
            File db_file = new File(Environment.getExternalStorageDirectory(), PrathamDatabase.DB_NAME);
            if (db_file.exists()) {
                InputStream myInput = new FileInputStream(db_file);
                String outFileName = "/data/data/"
                        + getInstance().getPackageName()
                        + "/databases/" + PrathamDatabase.DB_NAME;
                OutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized PrathamApplication getInstance() {
        return mInstance;
    }

    public void setPradigiPath() {
        pradigiPath = PD_Utility.getInternalPath(this) + "/" + FastSave.getInstance().getString(PD_Constant.LANGUAGE, PD_Constant.HINDI);
        File f = new File(pradigiPath);
        if (!f.exists()) f.mkdirs();
    }

    public void setExistingSDContentPath(String path) {
        contentExistOnSD = true;
        contentSDPath = path;
    }

    private void initializeDatabaseDaos() {
        PrathamDatabase db = PrathamDatabase.getDatabaseInstance(this);
        attendanceDao = db.getAttendanceDao();
        crLdao = db.getCrLdao();
        groupDao = db.getGroupDao();
        modalContentDao = db.getModalContentDao();
        scoreDao = db.getScoreDao();
        sessionDao = db.getSessionDao();
        statusDao = db.getStatusDao();
        studentDao = db.getStudentDao();
        villageDao = db.getVillageDao();
        logDao = db.getLogDao();
        if (!FastSave.getInstance().getBoolean(PD_Constant.BACKUP_DB_COPIED, false))
            new ReadBackupDb().execute();
    }
}

