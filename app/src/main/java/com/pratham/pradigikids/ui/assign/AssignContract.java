package com.pratham.pradigikids.ui.assign;

import com.pratham.pradigikids.models.Modal_Groups;
import com.pratham.pradigikids.models.Modal_Village;

import java.util.List;

public interface AssignContract {
    interface assignPresenter {
        void getStates();

        void getProgramId(String header);

        void getBlocks(String selectedState);

        void getVillages(String selectedBlock);

        void populateGroups(int vilID);

        void assignGroups(String group1, String group2, String group3, String group4, String group5, int vilID);

        void removeDeletedGroups();

        void clearData();
    }

    interface assignView {
        void setStates(List<String> states);

        void setProgramId(String header, String programID);

        void populateBlock(List<String> blocks);

        void populateVillage(List<Modal_Village> villages);

        void populateGroups(List<Modal_Groups> dbgroupList);

        void showDialog();

        void groupsAssignedSuccessfully();

        void onDataCleared();
    }
}
