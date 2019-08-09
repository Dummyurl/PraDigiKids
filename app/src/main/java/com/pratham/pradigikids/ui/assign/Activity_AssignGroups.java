package com.pratham.pradigikids.ui.assign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.pratham.pradigikids.BaseActivity;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.Modal_Groups;
import com.pratham.pradigikids.models.Modal_Village;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
@EActivity(R.layout.activity_assign_groups)
public class Activity_AssignGroups extends BaseActivity implements AssignContract.assignView {

    @ViewById(R.id.spinner_SelectState)
    Spinner spinner_SelectState;
    @ViewById(R.id.spinner_SelectBlock)
    Spinner spinner_SelectBlock;
    @ViewById(R.id.spinner_selectVillage)
    Spinner spinner_selectVillage;
    @ViewById(R.id.assignGroup1)
    LinearLayout assignGroup1;
    @ViewById(R.id.assignGroup2)
    LinearLayout assignGroup2;
    @ViewById(R.id.LinearLayoutGroups)
    LinearLayout LinearLayoutGroups;
    @ViewById(R.id.allocateGroups)
    Button allocateGroups;

    @Bean(AssignPresenterImpl.class)
    AssignContract.assignPresenter assignPresenter;
    private Boolean isAssigned = false;

    private int vilID;
    private String[] checkBoxIds;
    private ProgressDialog progress;

    private final String SPINNER = "spinner";

    @AfterViews
    public void initialize() {
//        assignPresenter.removeDeletedGroups();
        assignPresenter.getStates();
        assignPresenter.getProgramId(SPINNER);
    }

    // Show/Hide Spinners according to Program
    @UiThread
    @Override
    public void setProgramId(String header, String programID) {
        // Hide Village Spinner based on HLearning / RI
        if (header.equalsIgnoreCase(SPINNER)) {
            if (programID.equals("1") || programID.equals("3") || programID.equals("10"))
                spinner_selectVillage.setVisibility(View.VISIBLE);
            else if (programID.equals("2")) // RI
                spinner_selectVillage.setVisibility(View.GONE);
            else
                spinner_selectVillage.setVisibility(View.VISIBLE);
        }
    }

    @UiThread
    @Override
    public void setStates(List<String> states) {
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<String> StateAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner, states);
        // Hint for AllSpinners
        spinner_SelectState.setPrompt("Select State");
        spinner_SelectState.setAdapter(StateAdapter);
        spinner_SelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedState = spinner_SelectState.getSelectedItem().toString();
                assignPresenter.getBlocks(selectedState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Populate Blocks
    @UiThread
    @Override
    public void populateBlock(List<String> blocks) {
        //Get Villages Data for Blocks AllSpinners
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<String> BlockAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner, blocks);
        // Hint for AllSpinners
        spinner_SelectBlock.setPrompt("Select Block");
        spinner_SelectBlock.setAdapter(BlockAdapter);
        spinner_SelectBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBlock = spinner_SelectBlock.getSelectedItem().toString();
                assignPresenter.getVillages(selectedBlock);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @UiThread
    @Override
    public void populateVillage(List<Modal_Village> villages) {
        List<String> vill_list = new ArrayList<>();
        for (Modal_Village vill : villages)
            vill_list.add(vill.getVillageName());
        ArrayAdapter<String> VillagesAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner, vill_list);
        // Hint for AllSpinners
//        spinner_selectVillage.setPrompt("Select Village");
        spinner_selectVillage.setAdapter(VillagesAdapter);
        spinner_selectVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Modal_Village village = (Modal_Village) parent.getItemAtPosition(position);
                Modal_Village village = villages.get(position);
                vilID = village.getVillageId();
                try {
                    assignPresenter.populateGroups(vilID);  //Populate groups According to JSON & DB in Checklist instead of using spinner
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @UiThread
    @Override
    public void populateGroups(List<Modal_Groups> dbgroupList) {
        assignGroup1.removeAllViews();
        assignGroup2.removeAllViews();
        checkBoxIds = new String[dbgroupList.size()];
        int half = Math.round(dbgroupList.size() / 2);
        for (int i = 0; i < dbgroupList.size(); i++) {
            Modal_Groups grp = dbgroupList.get(i);
            String groupName = grp.getGroupName();
            String groupId = grp.getGroupId();
            TableRow row = new TableRow(Activity_AssignGroups.this);
            //row.setId(groupId);
            checkBoxIds[i] = groupId;
            //dynamically create checkboxes. i.e no. of students in group = no. of checkboxes
            row.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            CheckBox checkBox = new CheckBox(Activity_AssignGroups.this);
            try {
                checkBox.setId(i);
                checkBox.setTag(groupId);
                checkBox.setText(groupName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            checkBox.setTextSize(20);
            checkBox.setTextColor(Color.BLACK);
            checkBox.setBackgroundColor(Color.LTGRAY);
            row.addView(checkBox);
            if (i >= half)
                assignGroup2.addView(row);
            else
                assignGroup1.addView(row);
        }
        // Animation Effect on Groups populate
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        LinearLayoutGroups.startAnimation(animation1);
        allocateGroups.setVisibility(View.VISIBLE);
    }

    // Assign Groups
    @Click(R.id.allocateGroups)
    public void assignButtonClick() {
        try {
            String group1 = "0";
            String group2 = "0";
            String group3 = "0";
            String group4 = "0";
            String group5 = "0";
            int cnt = 0;
            for (int i = 0; i < checkBoxIds.length; i++) {
                CheckBox checkBox = findViewById(i);
                if (checkBox.isChecked() && group1.equals("0")) {
                    group1 = (String) checkBox.getTag();
                    cnt++;
                } else if (checkBox.isChecked() && group2.equals("0")) {
                    cnt++;
                    group2 = (String) checkBox.getTag();
                } else if (checkBox.isChecked() && group3.equals("0")) {
                    cnt++;
                    group3 = (String) checkBox.getTag();
                } else if (checkBox.isChecked() && group4.equals("0")) {
                    cnt++;
                    group4 = (String) checkBox.getTag();
                } else if (checkBox.isChecked() && group5.equals("0")) {
                    cnt++;
                    group5 = (String) checkBox.getTag();
                } else if (checkBox.isChecked()) {
                    cnt++;
                }
            }
            if (cnt < 1) {
                Toast.makeText(Activity_AssignGroups.this, "Please Select atleast one Group !!!", Toast.LENGTH_SHORT).show();
            } else if (cnt <= 5) {
                assignPresenter.assignGroups(group1, group2, group3, group4, group5, vilID);
            } else {
                Toast.makeText(Activity_AssignGroups.this, " You can select Maximum 5 Groups !!! ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    @Override
    public void showDialog() {
        progress = new ProgressDialog(Activity_AssignGroups.this);
        progress.setMessage("Please Wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    @UiThread
    @Override
    public void groupsAssignedSuccessfully() {
        isAssigned = true;
        Toast.makeText(Activity_AssignGroups.this, " Groups Assigned Successfully !!!", Toast.LENGTH_SHORT).show();
        progress.dismiss();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isAssigned) {
            Intent i = new Intent();
            setResult(Activity.RESULT_OK, i);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Click(R.id.assign_clear_data)
    public void clearData() {
        AlertDialog clearDataDialog = new AlertDialog.Builder(Activity_AssignGroups.this)
                .setTitle("Clear Data")
                .setMessage("Are you sure you want to clear everything ?")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    assignPresenter.clearData();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create();
        clearDataDialog.show();
        clearDataDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    @Override
    public void onDataCleared() {
        isAssigned = false;
        super.onBackPressed();
    }
}
