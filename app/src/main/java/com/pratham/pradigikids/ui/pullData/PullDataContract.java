package com.pratham.pradigikids.ui.pullData;

import com.pratham.pradigikids.models.ModalProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PEF on 19/11/2018.
 */

public interface PullDataContract {
    interface PullDataView {
        void showStatesSpinner(String[] states);

        void showProgressDialog(String msg);

        void showConfirmationDialog(int crlListCnt, int studentListcnt, int groupListCnt, int villageIDListCnt);

        void closeProgressDialog();

        void clearBlockSpinner();

        void clearStateSpinner();

        void showBlocksSpinner(List blocks);

        void showVillageDialog(List villageList);

        void disableSaveButton();

        void enableSaveButton();

        void showErrorToast();

        void openLoginActivity();

        void onDataClearToast();

        void showProgram(List<ModalProgram> prgrmList);
    }

    interface PullDataPresenter {
        void loadSpinner();

        void proccessVillageData(String respnce);

        void loadBloackSpinner(int pos, String selectedprogram);

        void downloadStudentAndGroup(ArrayList<String> villageIDList);

        void saveData();

        void clearLists();

        void onSaveClick();

        void setView(PullDataFragment pullDataFragment);

        void clearData();

        void loadProgrammes();
    }
}
