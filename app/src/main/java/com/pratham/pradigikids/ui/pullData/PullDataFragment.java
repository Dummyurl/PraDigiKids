package com.pratham.pradigikids.ui.pullData;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.ModalProgram;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by PEF on 19/11/2018.
 */

@EFragment(R.layout.pull_data_fragment)
public class PullDataFragment extends Fragment implements PullDataContract.PullDataView, VillageSelectListener {
    private static final String TAG = PullDataFragment.class.getSimpleName();
//    @ViewById(R.id.rg_programs)
//    RadioGroup radioGroupPrograms;

    @ViewById(R.id.programSpinner)
    Spinner programSpinner;
    @ViewById(R.id.stateSpinner)
    Spinner stateSpinner;
    @ViewById(R.id.blockSpinner)
    Spinner blockSpinner;
    @ViewById(R.id.save_button)
    Button save_button;

    @Bean(PullDataPresenterImp.class)
    PullDataContract.PullDataPresenter pullDataPresenter;

    private ProgressDialog progressDialog;
    private String selectedProgram = "";
    List<ModalProgram> prgrmList;

    @AfterViews
    public void initialize() {
        pullDataPresenter.setView(PullDataFragment.this);
        pullDataPresenter.loadProgrammes();
    }

    @Override
    public void showProgram(List<ModalProgram> prgrmList) {
        this.prgrmList = prgrmList;
        List<String> prgrms = new ArrayList<>();
        for (ModalProgram mp : prgrmList) {
            prgrms.add(mp.getProgramName());
        }
        ArrayAdapter arrayStateAdapter = new ArrayAdapter(Objects.requireNonNull(Objects.requireNonNull(getActivity())), android.R.layout.simple_spinner_item, prgrms);
        arrayStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        programSpinner.setAdapter(arrayStateAdapter);
        programSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                disableSaveButton();
                if (position <= 0) {
                    pullDataPresenter.clearLists();
                } else {
                    selectedProgram = prgrmList.get(position).getProgramId();
                    pullDataPresenter.loadSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @UiThread
    @Override
    public void showStatesSpinner(String[] states) {
        ArrayAdapter arrayStateAdapter = new ArrayAdapter(Objects.requireNonNull(Objects.requireNonNull(getActivity())), android.R.layout.simple_spinner_item, states);
        arrayStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(arrayStateAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                disableSaveButton();
                if (pos <= 0) {
                    clearBlockSpinner();
                } else {
//                    int selectedRadioButtonId = radioGroupPrograms.getCheckedRadioButtonId();
//                    if (selectedRadioButtonId == -1) {
//                        Toast.makeText(getContext(), "Please Select Program", Toast.LENGTH_SHORT).show();
//                    } else {
//                    RadioButton radioButton = radioGroupPrograms.findViewById(selectedRadioButtonId);
//                    String selectedProgram = radioButton.getText().toString();
                    pullDataPresenter.loadBloackSpinner(pos, selectedProgram);
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG, "onNothingSelected:");
            }
        });
    }

    @UiThread
    @Override
    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    @UiThread
    @Override
    public void showConfirmationDialog(int crlListCnt, int studentListcnt, int groupListCnt, int villageListCnt) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog);
        dialogBuilder.setCancelable(false);
        if (studentListcnt > 0) {
            dialogBuilder.setTitle("Data Preview");
            dialogBuilder.setMessage("CRLList : " + crlListCnt + "\nstudentList : " + studentListcnt + "\ngroupsList : " + groupListCnt + "\nvillageList : " + villageListCnt);
            dialogBuilder.setPositiveButton("Confirm", (dialog, whichButton) -> pullDataPresenter.saveData());
            dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> pullDataPresenter.clearLists());
            dialogBuilder.show();
        } else {
            dialogBuilder.setTitle("   No students to save..         ");
            dialogBuilder.setNegativeButton("OK", (dialog, whichButton) -> pullDataPresenter.clearLists());
            dialogBuilder.show();
        }
    }

    @UiThread
    @Override
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @UiThread
    @Override
    public void clearBlockSpinner() {
        blockSpinner.setSelection(0);
        blockSpinner.setEnabled(false);
    }

    @UiThread
    @Override
    public void clearStateSpinner() {
        stateSpinner.setSelection(0);
    }

    @UiThread
    @Override
    public void showBlocksSpinner(List blocks) {
        blockSpinner.setEnabled(true);
        ArrayAdapter arrayBlockAdapter = new ArrayAdapter(Objects.requireNonNull(Objects.requireNonNull(getActivity())), android.R.layout.simple_spinner_item, blocks);
        arrayBlockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockSpinner.setAdapter(arrayBlockAdapter);
        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                disableSaveButton();
                if (pos > 0) {
                    //open Village Dialog
                    String block = adapterView.getSelectedItem().toString();
                    pullDataPresenter.proccessVillageData(block);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //
    @UiThread
    @Override
    public void showVillageDialog(List villageList) {
        Dialog villageDialog = new SelectVillageDialog(Objects.requireNonNull(getActivity()), this, villageList);
        villageDialog.show();
    }

    @UiThread
    @Override
    public void disableSaveButton() {
        save_button.setEnabled(false);
    }

    @UiThread
    @Override
    public void enableSaveButton() {
        save_button.setEnabled(true);
    }

    @UiThread
    @Override
    public void showErrorToast() {
        Toast.makeText(getActivity(), "Please check connection", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    @Override
    public void openLoginActivity() {
        Toast.makeText(getActivity(), "Data Pulled Successful !", Toast.LENGTH_SHORT).show();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    @UiThread
    @Override
    public void getSelectedItems(ArrayList<String> villageIDList) {
        pullDataPresenter.downloadStudentAndGroup(villageIDList);
    }

    @Click(R.id.save_button)
    public void saveData() {
        pullDataPresenter.onSaveClick();
    }

    @Click(R.id.btn_clearData)
    public void clearData() {
        AlertDialog clearDataDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Clear Data")
                .setMessage("Are you sure you want to clear everything ?")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    pullDataPresenter.clearData();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create();
        clearDataDialog.show();
        clearDataDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    @UiThread
    @Override
    public void onDataClearToast() {
        Toast.makeText(getActivity(), "Data cleared Successfully", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.img_pull_back)
    public void setBack() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }
}
