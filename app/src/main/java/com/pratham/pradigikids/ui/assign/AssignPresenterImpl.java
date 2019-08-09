package com.pratham.pradigikids.ui.assign;

import android.content.Context;

import com.pratham.pradigikids.models.Modal_Groups;
import com.pratham.pradigikids.models.Modal_Village;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.List;

import static com.pratham.pradigikids.PrathamApplication.crLdao;
import static com.pratham.pradigikids.PrathamApplication.groupDao;
import static com.pratham.pradigikids.PrathamApplication.statusDao;
import static com.pratham.pradigikids.PrathamApplication.studentDao;
import static com.pratham.pradigikids.PrathamApplication.villageDao;

@EBean
public class AssignPresenterImpl implements AssignContract.assignPresenter {
    private final AssignContract.assignView assignView;

    public AssignPresenterImpl(Context context) {
        Context context1 = context;
        this.assignView = (AssignContract.assignView) context;
    }

    @Background
    @Override
    public void getStates() {
        List<String> states;
//        States.add("Select State");
        states = villageDao.getAllStates();
        assignView.setStates(states);
    }

    @Background
    @Override
    public void getProgramId(String header) {
        String programID = statusDao.getValue("programId");
        assignView.setProgramId(header, programID);
    }

    @Background
    @Override
    public void getBlocks(String selectedState) {
        List<String> blocks;
        blocks = villageDao.GetStatewiseBlock(selectedState);
        assignView.populateBlock(blocks);
    }

    @Background
    @Override
    public void getVillages(String selectedBlock) {
        String programID = statusDao.getValue("programId");
        List<Modal_Village> BlocksVillages;
        if (programID.equals("1") || programID.equals("3") || programID.equals("10")) { // H Learning
            BlocksVillages = villageDao.GetVillages(selectedBlock);
            assignView.populateVillage(BlocksVillages);
        } else if (programID.equals("2")) {// RI
            int vilID = villageDao.GetVillageIDByBlock(selectedBlock);
            List<Modal_Groups> dbgroupList = groupDao.GetGroups(vilID);
            assignView.populateGroups(dbgroupList);
        } else {
            BlocksVillages = villageDao.GetVillages(selectedBlock);
            assignView.populateVillage(BlocksVillages);
        }
    }

    @Override
    public void populateGroups(int vilID) {
        String programID = statusDao.getValue("programId");
        List<Modal_Groups> dbgroupList = groupDao.GetGroups(vilID);
        assignView.populateGroups(dbgroupList);
    }

    @Background
    @Override
    public void assignGroups(String group1, String group2, String group3, String group4, String group5, int vilID) {
        assignView.showDialog();
        // Delete Groups where Device ID is deleted & also delete associated students & update status table
        deleteGroupsWithStudents();
        // delete deleted Students
        studentDao.deleteDeletedStdRecords();
        // Update Groups in Status
        statusDao.updateValue(PD_Constant.GROUPID1, group1);
        statusDao.updateValue(PD_Constant.GROUPID2, group2);
        statusDao.updateValue(PD_Constant.GROUPID3, group3);
        statusDao.updateValue(PD_Constant.GROUPID4, group4);
        statusDao.updateValue(PD_Constant.GROUPID5, group5);
        statusDao.updateValue("village", Integer.toString(vilID));
        statusDao.updateValue("DeviceId", PD_Utility.getDeviceID());
        statusDao.updateValue("ActivatedDate", PD_Utility.getCurrentDateTime());
        statusDao.updateValue("ActivatedForGroups", group1 + "," + group2 + "," + group3 + "," + group4 + "," + group5);
        assignView.groupsAssignedSuccessfully();
    }

    // Delete Groups with Students
    @Background
    public void deleteGroupsWithStudents() {
        // Delete Records of Deleted Students
        List<Modal_Groups> deletedGroupsList = groupDao.GetAllDeletedGroups();
        // Delete students for all deleted groups
        for (Modal_Groups grps : deletedGroupsList) {
            //Delete Group
            groupDao.deleteGroupByGrpID(grps.getGroupId());
            // Delete Student
            studentDao.deleteDeletedGrpsStdRecords(grps.getGroupId());
        }
    }

    @Override
    public void removeDeletedGroups() {
        // Delete Groups where Device ID is deleted & also delete associated students & update status table
        deleteGroupsWithStudents();
    }

    @Override
    public void clearData() {
        clearData_();
        assignView.onDataCleared();
    }

    @Background
    public void clearData_() {
        villageDao.deleteAllVillages();
        groupDao.deleteAllGroups();
        studentDao.deleteAllStudents();
        crLdao.deleteAllCRLs();
    }
}
