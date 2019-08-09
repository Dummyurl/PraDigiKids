package com.pratham.pradigikids.ui.fragment_select_group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.BlurPopupDialog.BlurPopupWindow;
import com.pratham.pradigikids.custom.CircularRevelLayout;
import com.pratham.pradigikids.models.Modal_Groups;
import com.pratham.pradigikids.models.Modal_Student;
import com.pratham.pradigikids.ui.fragment_child_attendance.FragmentChildAttendance;
import com.pratham.pradigikids.ui.fragment_child_attendance.FragmentChildAttendance_;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.pratham.pradigikids.PrathamApplication.groupDao;
import static com.pratham.pradigikids.PrathamApplication.statusDao;
import static com.pratham.pradigikids.PrathamApplication.studentDao;

@EFragment(R.layout.fragment_select_group)
public class FragmentSelectGroup extends Fragment implements ContractGroup, CircularRevelLayout.CallBacks {

    @ViewById(R.id.rv_group)
    RecyclerView rv_group;
    @ViewById(R.id.circular_group_reveal)
    CircularRevelLayout circular_group_reveal;
    @ViewById(R.id.btn_group_next)
    Button btn_group_next;

    private GroupAdapter groupAdapter;
    private int revealX;
    private int revealY;
    private BlurPopupWindow noGrpDialog;

    @AfterViews
    public void initialize() {
        circular_group_reveal.setListener(this);
        if (getArguments() != null) {
            revealX = getArguments().getInt(PD_Constant.REVEALX, 0);
            revealY = getArguments().getInt(PD_Constant.REVEALY, 0);
            circular_group_reveal.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    circular_group_reveal.getViewTreeObserver().removeOnPreDrawListener(this);
                    circular_group_reveal.revealFrom(revealX, revealY, 0);
                    return true;
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initiaiteParametersInGroup();
    }

    @Background
    public void initiaiteParametersInGroup() {
        ArrayList<String> present_groups = new ArrayList<>();
        String groupId1 = statusDao.getValue(PD_Constant.GROUPID1);
        if (groupId1 != null && !groupId1.equalsIgnoreCase("0")) present_groups.add(groupId1);
        String groupId2 = statusDao.getValue(PD_Constant.GROUPID2);
        if (groupId2 != null && !groupId2.equalsIgnoreCase("0")) present_groups.add(groupId2);
        String groupId3 = statusDao.getValue(PD_Constant.GROUPID3);
        if (groupId3 != null && !groupId3.equalsIgnoreCase("0")) present_groups.add(groupId3);
        String groupId4 = statusDao.getValue(PD_Constant.GROUPID4);
        if (groupId4 != null && !groupId4.equalsIgnoreCase("0")) present_groups.add(groupId4);
        String groupId5 = statusDao.getValue(PD_Constant.GROUPID5);
        if (groupId5 != null && !groupId5.equalsIgnoreCase("0")) present_groups.add(groupId5);
        List<Modal_Groups> groups;
        if (Objects.requireNonNull(getArguments()).getBoolean(PD_Constant.GROUP_AGE_BELOW_7)) {
            groups = get3to6Groups(present_groups);
        } else {
            groups = get8to14Groups(present_groups);
        }
        if (groups != null && groups.size() > 0) {
            setGroups(groups);
        } else {
            showNoGroupsDialog();
        }
    }

    @UiThread
    public void showNoGroupsDialog() {
        noGrpDialog = new BlurPopupWindow.Builder(getActivity())
                .setContentView(R.layout.no_grp_dialog)
                .bindClickListener(v -> {
                    noGrpDialog.dismiss();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                }, R.id.dialog_no_grp_btn_exit)
                .setGravity(Gravity.CENTER)
                .setDismissOnTouchBackground(false)
                .setDismissOnClickBack(false)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();
        noGrpDialog.show();
    }

    private List<Modal_Groups> get3to6Groups(ArrayList<String> allGroups) {
        List<Modal_Groups> groups = new ArrayList<>();
        Modal_Groups group;
        boolean grpFound;
        for (String grID : allGroups) {
            grpFound = false;
            ArrayList<Modal_Student> students = (ArrayList<Modal_Student>) studentDao.getGroupwiseStudents(grID);
            for (Modal_Student stu : students) {
                if (Integer.parseInt(stu.getAge()) < 7) {
                    grpFound = true;
                }
            }
            if (grpFound) {
                group = groupDao.getGroupByGrpID(grID);
                groups.add(group);
            }
        }
        return groups;
    }

    private List<Modal_Groups> get8to14Groups(ArrayList<String> allGroups) {
        List<Modal_Groups> groups = new ArrayList<>();
        Modal_Groups group;
        boolean grpFound;
        for (String grID : allGroups) {
            grpFound = false;
            ArrayList<Modal_Student> students = (ArrayList<Modal_Student>) studentDao.getGroupwiseStudents(grID);
            for (Modal_Student stu : students) {
                if (Integer.parseInt(stu.getAge()) >= 7) {
                    grpFound = true;
                }
            }
            if (grpFound) {
                group = groupDao.getGroupByGrpID(grID);
                groups.add(group);
            }
        }
        return groups;
    }

    @UiThread
    public void setGroups(List<Modal_Groups> groups) {
        groupAdapter = new GroupAdapter(getActivity()/*, groups*/, FragmentSelectGroup.this);
        rv_group.setHasFixedSize(true);
        rv_group.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_group.setAdapter(groupAdapter);
        groupAdapter.submitList(groups);
    }

    @Click(R.id.btn_group_next)
    public void setSelectedGroup() {
        Modal_Groups tempGrp = new Modal_Groups();
        for (Modal_Groups grp : groupAdapter.getList()) {
            if (grp.isSelected()) {
                tempGrp = grp;
                break;
            } else tempGrp = null;
        }
        if (tempGrp != null)
            setNext(btn_group_next, tempGrp);
        else Toast.makeText(getActivity(), "Please select a Group.", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.img_att_back)
    public void setAttBack() {
        try {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void setNext(View v, Modal_Groups modal_groups) {
        PrathamApplication.bubble_mp.start();
        ArrayList<Modal_Student> students = new ArrayList<>(studentDao.getGroupwiseStudents(modal_groups.getGroupId()));
        int[] outLocation = new int[2];
        v.getLocationOnScreen(outLocation);
        outLocation[0] += v.getWidth() / 2;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PD_Constant.STUDENT_LIST, students);
        bundle.putString(PD_Constant.GROUP_NAME, modal_groups.getGroupName());
        bundle.putString(PD_Constant.GROUPID, modal_groups.getGroupId());
        bundle.putInt(PD_Constant.REVEALX, outLocation[0]);
        bundle.putInt(PD_Constant.REVEALY, outLocation[1]);
        PD_Utility.addFragment(getActivity(), new FragmentChildAttendance_(), R.id.frame_attendance,
                bundle, FragmentChildAttendance.class.getSimpleName());
    }

    @Override
    public void groupItemClicked(View v, Modal_Groups modalGroup, int position) {
        PrathamApplication.bubble_mp.start();
        for (Modal_Groups grp : groupAdapter.getList()) {
            if (grp.getGroupId().equalsIgnoreCase(modalGroup.getGroupId()))
                grp.setSelected(true);
            else
                grp.setSelected(false);
        }
//        temp.set(position, modalGroup);
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRevealed() {

    }

    @Override
    public void onUnRevealed() {
    }
}
