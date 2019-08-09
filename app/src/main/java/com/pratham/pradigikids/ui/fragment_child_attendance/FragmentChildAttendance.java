package com.pratham.pradigikids.ui.fragment_child_attendance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.CircularRevelLayout;
import com.pratham.pradigikids.custom.flexbox.FlexDirection;
import com.pratham.pradigikids.custom.flexbox.FlexboxLayoutManager;
import com.pratham.pradigikids.custom.flexbox.JustifyContent;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.models.Attendance;
import com.pratham.pradigikids.models.Modal_Session;
import com.pratham.pradigikids.models.Modal_Student;
import com.pratham.pradigikids.services.AppKillService;
import com.pratham.pradigikids.ui.avatar.Fragment_SelectAvatar_;
import com.pratham.pradigikids.ui.dashboard.ActivityMain_;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Objects;

import static com.pratham.pradigikids.PrathamApplication.attendanceDao;
import static com.pratham.pradigikids.PrathamApplication.sessionDao;

@EFragment(R.layout.fragment_child_attendance)
public class FragmentChildAttendance extends Fragment implements ContractChildAttendance.attendanceView,
        CircularRevelLayout.CallBacks {

    private static final String TAG = FragmentChildAttendance.class.getSimpleName();
    private static final int INITIALIZE_STUDENTS = 1;
    @ViewById(R.id.chid_attendance_reveal)
    CircularRevelLayout chid_attendance_reveal;
    @ViewById(R.id.rv_child)
    RecyclerView rv_child;
    @ViewById(R.id.btn_attendance_next)
    Button btn_attendance_next;
    @ViewById(R.id.img_child_back)
    ImageView img_child_back;
//    @ViewById(R.id.btn_talk)
//    Button btn_talk;

    private ChildAdapter childAdapter;
    private ArrayList<Modal_Student> students = new ArrayList<>();
    private int revealX;
    private int revealY;
    private String groupID = "";
    @SuppressLint("HandlerLeak")
    private final
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIALIZE_STUDENTS:
                    students = getArguments() != null ? getArguments().getParcelableArrayList(PD_Constant.STUDENT_LIST) : null;
                    if (PrathamApplication.isTablet) {
                        img_child_back.setVisibility(View.VISIBLE);
                        btn_attendance_next.setVisibility(View.VISIBLE);
                        groupID = getArguments().getString(PD_Constant.GROUPID);
                    } else {
                        img_child_back.setVisibility(View.GONE);
                        btn_attendance_next.setVisibility(View.GONE);
                        groupID = "SmartPhone";
                        //adding add child item
                        Modal_Student add_student = new Modal_Student();
                        add_student.setFullName("Add Child");
                        add_student.setStudentId("Add Child");
                        if (!students.contains(add_student)) students.add(add_student);
                    }
                    setChilds(students);
                    break;
            }
        }
    };

    @AfterViews
    public void initialize() {
        chid_attendance_reveal.setListener(this);
//        if (PrathamApplication.isTablet) btn_talk.setVisibility(View.GONE);
//        else btn_talk.setVisibility(View.VISIBLE);
        if (getArguments() != null) {
            revealX = getArguments().getInt(PD_Constant.REVEALX, 0);
            revealY = getArguments().getInt(PD_Constant.REVEALY, 0);
            chid_attendance_reveal.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    chid_attendance_reveal.getViewTreeObserver().removeOnPreDrawListener(this);
                    chid_attendance_reveal.revealFrom(revealX, revealY, 0);
                    return true;
                }
            });
        }
        mHandler.sendEmptyMessage(INITIALIZE_STUDENTS);
    }

    @UiThread
    public void setChilds(ArrayList<Modal_Student> childs) {
        childAdapter = new ChildAdapter(getActivity(), childs, FragmentChildAttendance.this);
        rv_child.setHasFixedSize(true);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getActivity(), FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
        rv_child.setLayoutManager(flexboxLayoutManager);
        rv_child.setAdapter(childAdapter);
        rv_child.scheduleLayoutAnimation();
    }

    @Override
    public void childItemClicked(Modal_Student stud, int position) {
        PrathamApplication.bubble_mp.start();
        for (Modal_Student stu : students) {
            if (stu.getStudentId().equalsIgnoreCase(stud.getStudentId())) {
                if (stu.isChecked()) stu.setChecked(false);
                else stu.setChecked(true);
                stud = stu;
                break;
            }
        }
        childAdapter.notifyItemChanged(position, stud);
    }

    @Override
    public void addChild(View view) {
        int[] outLocation = new int[2];
        view.getLocationOnScreen(outLocation);
        outLocation[0] += view.getWidth() / 2;
        Bundle bundle = new Bundle();
        bundle.putInt(PD_Constant.REVEALX, outLocation[0]);
        bundle.putInt(PD_Constant.REVEALY, outLocation[1]);
        bundle.putBoolean(PD_Constant.SHOW_BACK, true);
        PD_Utility.addFragment(getActivity(), new Fragment_SelectAvatar_(), R.id.frame_attendance,
                bundle, Fragment_SelectAvatar_.class.getSimpleName());
    }

    @Override
    public void moveToDashboardOnChildClick(Modal_Student student, int position, View v) {
        PrathamApplication.bubble_mp.start();
        FastSave.getInstance().saveString(PD_Constant.STUDENTID, student.getStudentId());
        FastSave.getInstance().saveString(PD_Constant.AVATAR, student.getAvatarName());
        if (student.getFullName() != null && !student.getFullName().isEmpty())
            FastSave.getInstance().saveString(PD_Constant.PROFILE_NAME, student.getFullName());
        else
            FastSave.getInstance().saveString(PD_Constant.PROFILE_NAME, student.getFirstName() + " " + student.getLastName());
        ArrayList<Modal_Student> s = new ArrayList<>();
        s.add(student);
        markAttendance(s);
        presentActivity(v);
    }

    @Touch(R.id.btn_attendance_next)
    public boolean setNextAvatar(View view, MotionEvent event) {
        revealX = (int) event.getRawX();
        revealY = (int) event.getY();
        return Objects.requireNonNull(getActivity()).onTouchEvent(event);
    }

    @Touch(R.id.rv_child)
    public boolean getRecyclerTouch(View view, MotionEvent event) {
        revealX = (int) event.getRawX();
        revealY = (int) event.getY();
        return Objects.requireNonNull(getActivity()).onTouchEvent(event);
    }

    @Click(R.id.btn_attendance_next)
    public void setNext(View v) {
        ArrayList<Modal_Student> checkedStds = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).isChecked())
                checkedStds.add(students.get(i));
        }
        if (checkedStds.size() > 0) {
            PrathamApplication.bubble_mp.start();
            FastSave.getInstance().saveString(PD_Constant.AVATAR, "avatars/dino_dance.json");
            FastSave.getInstance().saveString(PD_Constant.PROFILE_NAME, Objects.requireNonNull(getArguments()).getString(PD_Constant.GROUP_NAME));
            markAttendance(checkedStds);
            presentActivity(v);
        } else {
            Toast.makeText(getContext(), "Please Select Students !", Toast.LENGTH_SHORT).show();
        }
    }

    @Background
    public void markAttendance(ArrayList<Modal_Student> stud) {
        FastSave.getInstance().saveString(PD_Constant.SESSIONID, PD_Utility.getUUID().toString());
        ArrayList<Attendance> attendances = new ArrayList<>();
        for (Modal_Student stu : stud) {
            Attendance attendance = new Attendance();
            attendance.SessionID = FastSave.getInstance().getString(PD_Constant.SESSIONID, "");
            attendance.StudentID = stu.getStudentId();
            attendance.Date = PD_Utility.getCurrentDateTime();
            attendance.GroupID = groupID;
            attendance.sentFlag = 0;
            FastSave.getInstance().saveString(PD_Constant.GROUPID, groupID);
            attendances.add(attendance);
        }
        attendanceDao.insertAttendance(attendances);
        Modal_Session s = new Modal_Session();
        s.setSessionID(FastSave.getInstance().getString(PD_Constant.SESSIONID, ""));
        s.setFromDate(PD_Utility.getCurrentDateTime());
        s.setToDate("NA");
        sessionDao.insert(s);
    }

    @UiThread
    public void presentActivity(View view) {
        FastSave.getInstance().saveBoolean(PD_Constant.STORAGE_ASKED, false);
        Objects.requireNonNull(getActivity()).startService(new Intent(getActivity(), AppKillService.class));
        Intent mActivityIntent = new Intent(getActivity(), ActivityMain_.class);
        if (Objects.requireNonNull(getArguments()).getBoolean(PD_Constant.DEEP_LINK, false)) {
            mActivityIntent.putExtra(PD_Constant.DEEP_LINK, true);
            mActivityIntent.putExtra(PD_Constant.DEEP_LINK_CONTENT, getArguments().getString(PD_Constant.DEEP_LINK_CONTENT));
        }
        startActivity(mActivityIntent);
        getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        getActivity().finishAfterTransition();
    }

    @Click(R.id.img_child_back)
    public void setAttBack() {
        try {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRevealed() {
    }

    @Override
    public void onUnRevealed() {
    }

    /*@Click(R.id.btn_talk)
    public void openConference(){
        Intent mActivityIntent = new Intent(getActivity(), CnferenceAct.class);
        startActivity(mActivityIntent);
        getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        getActivity().finishAfterTransition();
    }*/
}
