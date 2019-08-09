package com.pratham.pradigikids.ui.QRLogin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.pratham.pradigikids.BaseActivity;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.models.Attendance;
import com.pratham.pradigikids.models.Modal_Session;
import com.pratham.pradigikids.models.Modal_Student;
import com.pratham.pradigikids.services.AppKillService;
import com.pratham.pradigikids.ui.dashboard.ActivityMain_;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.pratham.pradigikids.PrathamApplication.attendanceDao;
import static com.pratham.pradigikids.PrathamApplication.sessionDao;

@EActivity(R.layout.activity_qrlogin)
public class QRLogin extends BaseActivity implements ZXingScannerView.ResultHandler {

    @ViewById(R.id.content_frame)
    ViewGroup content_frame;
    @ViewById(R.id.tv_stud_one)
    TextView tv_stud_one;
    @ViewById(R.id.tv_stud_two)
    TextView tv_stud_two;
    @ViewById(R.id.tv_stud_three)
    TextView tv_stud_three;
    @ViewById(R.id.tv_stud_four)
    TextView tv_stud_four;
    @ViewById(R.id.tv_stud_five)
    TextView tv_stud_five;
    @ViewById(R.id.btn_Start)
    Button btn_Start;
    @ViewById(R.id.btn_Reset)
    Button btn_Reset;

    private ZXingScannerView startCameraScan;
    private ArrayList<Modal_Student> stdList;
    private Dialog dialog;
    private int totalStudents = 0;
    private Boolean setStud = false;

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrlogin);
        ButterKnife.bind(QRLogin.this);

        hideAllStudents();
        stdList = new ArrayList<Modal_Student>();
        if (ContextCompat.checkSelfPermission(QRLogin.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(QRLogin.this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QRLogin.this);
                builder.setMessage("App requires camera permission to scan QR code");
                builder.setCancelable(false);
                builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(QRLogin.this, new String[]{Manifest.permission.CAMERA}, 1);
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(QRLogin.this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        } else {
            permission = true;
        initCamera();
        }

    }// onCreate*/

    @AfterViews
    public void initialize() {
        hideAllStudents();
        stdList = new ArrayList<>();
        initCamera();
    }

    @Click(R.id.btn_Reset)
    public void resetButton() {
        content_frame.setVisibility(View.VISIBLE);
        stdList.clear();
        totalStudents = 0;
        clearStudents();
        hideAllStudents();
        scanNextQRCode();
    }

    private void clearStudents() {
        tv_stud_one.setText("");
        tv_stud_two.setText("");
        tv_stud_three.setText("");
        tv_stud_four.setText("");
        tv_stud_five.setText("");
    }

    private void hideAllStudents() {
        tv_stud_one.setVisibility(View.GONE);
        tv_stud_two.setVisibility(View.GONE);
        tv_stud_three.setVisibility(View.GONE);
        tv_stud_four.setVisibility(View.GONE);
        tv_stud_five.setVisibility(View.GONE);
    }

    @Click(R.id.btn_Start)
    public void startButton(View view) {
        if (stdList.size() > 0)
            setValues(view);
        else
            Toast.makeText(QRLogin.this, "Please Add Student !!!", Toast.LENGTH_SHORT).show();
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                Toast.makeText(QRLogin.this, "You Need Camera permission", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                initCamera();
//                permission = true;
//            }
//        }
//
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        startCameraScan.resumeCameraPreview(QRLogin.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraScan.resumeCameraPreview(QRLogin.this);
    }

    @Override
    public void onDestroy() {
        if (startCameraScan != null) startCameraScan.stopCamera();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startCameraScan.stopCamera();
    }

    @Background
    public void setValues(View view) {
        List<Attendance> attendances = new ArrayList<>();
        try {
            if (stdList != null && stdList.size() > 0) {
                FastSave.getInstance().saveString(PD_Constant.SESSIONID, PD_Utility.getUUID().toString());
                for (int i = 0; i < stdList.size(); i++) {
                    Attendance attendance = new Attendance();
                    attendance.setSessionID(FastSave.getInstance().getString(PD_Constant.SESSIONID, "defaultSession"));
                    attendance.setStudentID(stdList.get(i).getStudentId());
                    attendance.setDate(PD_Utility.getCurrentDateTime());
                    attendance.setGroupID("QR");
                    attendance.setSentFlag(0);
                    attendances.add(attendance);
                }
                FastSave.getInstance().saveString(PD_Constant.GROUPID, "QR");
                attendanceDao.insertAttendance(attendances);

                Modal_Session s = new Modal_Session();
                s.setSessionID(FastSave.getInstance().getString(PD_Constant.SESSIONID, "defaultSession"));
                s.setFromDate(PD_Utility.getCurrentDateTime());
                s.setToDate("NA");
                sessionDao.insert(s);
                if (startCameraScan != null)
                    startCameraScan.stopCamera();
                FastSave.getInstance().saveBoolean(PD_Constant.STORAGE_ASKED, false);
                startService(new Intent(this, AppKillService.class));
                openDashboard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void openDashboard() {
        Intent main = new Intent(QRLogin.this, ActivityMain_.class);
        startActivity(main);
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        finishAfterTransition();
    }

    private void scanNextQRCode() {
        if (startCameraScan != null) {
            startCameraScan.stopCamera();
        }
        Objects.requireNonNull(startCameraScan).startCamera();
        startCameraScan.resumeCameraPreview(QRLogin.this);
    }

    @Override
    public void handleResult(Result result) {
        try {
            boolean dulicateQR = false;
            startCameraScan.stopCamera();
            Log.d("RawResult:::", "****" + result.getText());
            if (result.getText().contains("{")) {
                // New QRCode Jsons
                // Json Parsing
                JSONObject jsonobject = new JSONObject(result.getText());
                String id = jsonobject.getString("stuId");
//                String name = jsonobject.getString("name");
                if (stdList.size() <= 0)
                    qrEntryProcess(result);
                else {
                    for (Modal_Student student : stdList) {
                        String[] currentIdArr = {id};
                        String currId = currentIdArr[0];
                        if (student.getStudentId().equalsIgnoreCase("" + currId)) {
                            showQrDialog(", This QR Was Already Scaned");
                            setStud = false;
                            dulicateQR = true;
                            break;
                        }
                    }
                    if (!dulicateQR) {
                        qrEntryProcess(result);
                    }
                }
            } else {
                // For Old QR Json Format
                try {
                    Log.d("RawResult:::", "****" + result.getText());
                    Pattern pattern = Pattern.compile("[A-Za-z0-9]+-[A-Za-z._]{2,50}");
                    Matcher mat = pattern.matcher(result.getText());
                    if (mat.matches()) {
                        if (stdList.size() <= 0)
                            qrEntryProcess(result);
                        else {
                            for (Modal_Student student : stdList) {
                                String[] currentIdArr = decodeStudentId(result.getText(), "-");
                                String currId = currentIdArr[0];
                                if (student.getStudentId().equalsIgnoreCase("" + currId)) {
                                    showQrDialog(", This QR Was Already Scaned");
                                    setStud = false;
                                    dulicateQR = true;
                                    break;
                                }
                            }
                            if (!dulicateQR) {
                                qrEntryProcess(result);
                            }
                        }
                    } else {
                        startCameraScan.startCamera();
                        startCameraScan.resumeCameraPreview(QRLogin.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Toast.makeText(QRLogin.this, "Invalid QR Code !!!", Toast.LENGTH_SHORT).show();
            btn_Reset.performClick();
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showQrDialog(String studentName) {
        dialog = new Dialog(QRLogin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_for_qrscan);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = dialog.findViewById(R.id.dialog_tv_student_name);
        ImageView iv_close = dialog.findViewById(R.id.dialog_iv_close);
        Button scanNextQR = dialog.findViewById(R.id.dialog_btn_scan_qr);
        text.setText("Hi " + studentName);
        iv_close.setOnClickListener(view -> {
            dialog.dismiss();
            if (totalStudents == 5) {
                content_frame.setVisibility(View.GONE);
                showStudentName(totalStudents);
            } else {
                if (setStud) {
                    setStud = false;
                    showStudentName(totalStudents);
                }
                scanNextQRCode();
            }
        });
        scanNextQR.setOnClickListener(view -> {
            dialog.dismiss();
            if (totalStudents == 5) {
                content_frame.setVisibility(View.GONE);
                showStudentName(totalStudents);
            } else {
                if (setStud) {
                    setStud = false;
                    showStudentName(totalStudents);
                }
                scanNextQRCode();
            }
        });
        dialog.show();
    }

    private void qrEntryProcess(Result result) {
        if (result.getText().contains("{")) {
            totalStudents++;
            String sid = "", sname = "";
            Modal_Student std = new Modal_Student(sid, sname, "QRGroupID");
            if (totalStudents < 6) {
                try {
                    JSONObject jsonobject = new JSONObject(result.getText());
                    String resultID = jsonobject.getString("stuId");
                    String resultName = jsonobject.getString("name");
                    //Valid pattern
                    String[] id = {resultID};
                    String stdId = id[0];
                    //String stdFirstName = id[1];
                    String[] name = {resultName};
                    String stdFirstName = name[0];
                    std.setStudentId(stdId);
                    std.setFirstName(stdFirstName);
                    stdList.add(std);
                    setStud = true;
                    showQrDialog(stdFirstName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            totalStudents++;
            String sid = "", sname = "";
            Modal_Student std = new Modal_Student(sid, sname, "QRGroupID");
            if (totalStudents < 6) {
                //Valid pattern
                String[] id = decodeStudentId(result.getText(), "-");
                String stdId = id[0];
                String[] name = decodeStudentId(id[1], "_");
                String stdFirstName = name[0];
                std.setStudentId(stdId);
                std.setFirstName(stdFirstName);
                stdList.add(std);
                setStud = true;
                showQrDialog(stdFirstName);
            }
        }
    }

    private String[] decodeStudentId(String text, String s) {
        return text.split(s);
    }

    @SuppressLint("SetTextI18n")
    private void showStudentName(int totalStudents) {

        switch (totalStudents) {
            case 1:
                tv_stud_one.setVisibility(View.VISIBLE);
                tv_stud_one.setText("" + stdList.get(0).getFirstName());
                break;
            case 2:
                tv_stud_two.setVisibility(View.VISIBLE);
                tv_stud_two.setText("" + stdList.get(1).getFirstName());
                break;
            case 3:
                tv_stud_three.setVisibility(View.VISIBLE);
                tv_stud_three.setText("" + stdList.get(2).getFirstName());
                break;
            case 4:
                tv_stud_four.setVisibility(View.VISIBLE);
                tv_stud_four.setText("" + stdList.get(3).getFirstName());
                break;
            case 5:
                tv_stud_five.setVisibility(View.VISIBLE);
                tv_stud_five.setText("" + stdList.get(4).getFirstName());
                break;
        }
    }

    private void initCamera() {
        try {
            startCameraScan = new ZXingScannerView(QRLogin.this);
            startCameraScan.setResultHandler(QRLogin.this);
            content_frame.addView((startCameraScan));
            startCameraScan.startCamera();
            startCameraScan.resumeCameraPreview(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
