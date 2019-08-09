package com.pratham.pradigikids.ui.avatar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.CircularRevelLayout;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.models.Attendance;
import com.pratham.pradigikids.models.Modal_Session;
import com.pratham.pradigikids.models.Modal_Student;
import com.pratham.pradigikids.services.AppKillService;
import com.pratham.pradigikids.ui.dashboard.ActivityMain_;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.pratham.pradigikids.PrathamApplication.attendanceDao;
import static com.pratham.pradigikids.PrathamApplication.sessionDao;
import static com.pratham.pradigikids.PrathamApplication.studentDao;

@EFragment(R.layout.select_avatar)
public class Fragment_SelectAvatar extends Fragment implements AvatarContract.avatarView, CircularRevelLayout.CallBacks {

    private static final String TAG = Fragment_SelectAvatar.class.getSimpleName();
    @ViewById(R.id.avatar_circular_reveal)
    CircularRevelLayout avatar_circular_reveal;
    @ViewById(R.id.et_child_name)
    EditText et_child_name;
    @ViewById(R.id.avatar_rv)
    DiscreteScrollView avatar_rv;
    @ViewById(R.id.btn_avatar_next)
    Button btn_avatar_next;
    @ViewById(R.id.spinner_class)
    Spinner spinner_class;
    @ViewById(R.id.spinner_age)
    Spinner spinner_age;
    @ViewById(R.id.img_add_child_back)
    ImageView img_add_child_back;

    private final ArrayList<String> avatarList = new ArrayList<>();
    private Context context;
    private String avatar_selected = "";
    private final DiscreteScrollView.OnItemChangedListener onItemChangedListener = new DiscreteScrollView.OnItemChangedListener() {
        @Override
        public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
            if (viewHolder != null) {
                ((LottieAnimationView) (Objects.requireNonNull(viewHolder.itemView))).playAnimation();
                ((LottieAnimationView) (Objects.requireNonNull(viewHolder.itemView))).loop(true);
                avatar_selected = avatarList.get(avatar_rv.getCurrentItem());
            }
        }
    };
    private int revealX;
    private int revealY;

    @Background
    public void initializeAvatars() {
        String[] avatars = getResources().getStringArray(R.array.avatars);
        avatarList.addAll(Arrays.asList(avatars));
        initializeAdapter();
    }

    @AfterViews
    public void initialize() {
        avatar_circular_reveal.setListener(this);
        if (getArguments() != null && getArguments().getBoolean(PD_Constant.SHOW_BACK))
            img_add_child_back.setVisibility(View.VISIBLE);
        else img_add_child_back.setVisibility(View.GONE);
        if (getArguments() != null) {
            revealX = getArguments().getInt(PD_Constant.REVEALX, 0);
            revealY = getArguments().getInt(PD_Constant.REVEALY, 0);
            avatar_circular_reveal.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    avatar_circular_reveal.getViewTreeObserver().removeOnPreDrawListener(this);
                    avatar_circular_reveal.revealFrom(revealX, revealY, 0);
                    return true;
                }
            });
        }
        initializeAvatars();
    }

    @UiThread
    public void initializeAdapter() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.student_class, R.layout.simple_spinner_item);
        spinner_class.setAdapter(adapter);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.age, R.layout.simple_spinner_item);
        spinner_age.setAdapter(adapter2);
        avatar_rv.setOrientation(DSVOrientation.VERTICAL);
        avatar_rv.addOnItemChangedListener(onItemChangedListener);
        avatar_rv.setAdapter(new AvatarAdapter(context, avatarList));
        avatar_rv.setItemTransitionTimeMillis(150);
        avatar_rv.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.9f)
                .setMaxScale(1.05f)
                .build());
    }

    @Click(R.id.btn_avatar_next)
    public void setNext() {
        PrathamApplication.bubble_mp.start();
        if (!et_child_name.getText().toString().isEmpty()) {
            insertStudentAndMarkAttendance();
        } else
            Toast.makeText(getActivity(), "Please enter the name", Toast.LENGTH_SHORT).show();
    }

    @Background
    public void insertStudentAndMarkAttendance() {
        FastSave.getInstance().saveString(PD_Constant.AVATAR, avatar_selected);
        FastSave.getInstance().saveString(PD_Constant.PROFILE_NAME, et_child_name.getText().toString());
        Modal_Student modal_student = new Modal_Student();
        modal_student.setStudentId(PD_Utility.getUUID().toString());
        modal_student.setFullName(et_child_name.getText().toString());
        modal_student.setGroupId("SmartPhone");
        modal_student.setGroupName("SmartPhone");
        modal_student.setFirstName(et_child_name.getText().toString());
        modal_student.setMiddleName(et_child_name.getText().toString());
        modal_student.setLastName(et_child_name.getText().toString());
        modal_student.setStud_Class(spinner_class.getSelectedItem().toString());
        modal_student.setAge(spinner_age.getSelectedItem().toString());
        modal_student.setGender("M");
        modal_student.setSentFlag(0);
        modal_student.setAvatarName(avatar_selected);
        studentDao.insertStudent(modal_student);
        FastSave.getInstance().saveString(PD_Constant.STUDENTID, modal_student.getStudentId());
        markAttendance(modal_student);
        presentActivity();
    }

    @UiThread
    public void presentActivity() {
        Objects.requireNonNull(getActivity()).startService(new Intent(getActivity(), AppKillService.class));
        FastSave.getInstance().saveBoolean(PD_Constant.STORAGE_ASKED, false);
        Intent mActivityIntent = new Intent(getActivity(), ActivityMain_.class);
        if (getArguments() != null && getArguments().getBoolean(PD_Constant.DEEP_LINK, false)) {
            mActivityIntent.putExtra(PD_Constant.DEEP_LINK, true);
            mActivityIntent.putExtra(PD_Constant.DEEP_LINK_CONTENT, getArguments().getString(PD_Constant.DEEP_LINK_CONTENT));
        }
        startActivity(mActivityIntent);
        getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        getActivity().finishAfterTransition();
    }

    private void markAttendance(Modal_Student stud) {
        FastSave.getInstance().saveString(PD_Constant.SESSIONID, PD_Utility.getUUID().toString());
        ArrayList<Attendance> attendances = new ArrayList<>();
        Attendance attendance = new Attendance();
        attendance.SessionID = FastSave.getInstance().getString(PD_Constant.SESSIONID, "");
        attendance.StudentID = stud.getStudentId();
        attendance.Date = PD_Utility.getCurrentDateTime();
        attendance.GroupID = "SmartPhone";
        attendance.sentFlag = 0;
        FastSave.getInstance().saveString(PD_Constant.GROUPID, "SmartPhone");
        attendances.add(attendance);
        attendanceDao.insertAttendance(attendances);
        Modal_Session s = new Modal_Session();
        s.setSessionID(FastSave.getInstance().getString(PD_Constant.SESSIONID, ""));
        s.setFromDate(PD_Utility.getCurrentDateTime());
        s.setToDate("NA");
        sessionDao.insert(s);
    }

    @Click(R.id.img_add_child_back)
    public void setAttBack() {
        try {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openDashboard() {
    }

    @Override
    public void onRevealed() {
    }

    @Override
    public void onUnRevealed() {
    }

}
