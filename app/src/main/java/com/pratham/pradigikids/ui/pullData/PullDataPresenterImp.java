package com.pratham.pradigikids.ui.pullData;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.async.PD_ApiRequest;
import com.pratham.pradigikids.interfaces.ApiResult;
import com.pratham.pradigikids.models.ModalProgram;
import com.pratham.pradigikids.models.Modal_ContentDetail;
import com.pratham.pradigikids.models.Modal_Crl;
import com.pratham.pradigikids.models.Modal_Groups;
import com.pratham.pradigikids.models.Modal_Student;
import com.pratham.pradigikids.models.Modal_Village;
import com.pratham.pradigikids.models.RaspCrl;
import com.pratham.pradigikids.models.RaspGroup;
import com.pratham.pradigikids.models.RaspProgram;
import com.pratham.pradigikids.models.RaspStudent;
import com.pratham.pradigikids.models.RaspVillage;
import com.pratham.pradigikids.models.Village;
import com.pratham.pradigikids.util.APIs;
import com.pratham.pradigikids.util.PD_Constant;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import static com.pratham.pradigikids.PrathamApplication.crLdao;
import static com.pratham.pradigikids.PrathamApplication.groupDao;
import static com.pratham.pradigikids.PrathamApplication.statusDao;
import static com.pratham.pradigikids.PrathamApplication.studentDao;
import static com.pratham.pradigikids.PrathamApplication.villageDao;

/**
 * Created by PEF on 20/11/2018.
 */
@EBean
public class PullDataPresenterImp implements PullDataContract.PullDataPresenter, ApiResult {
    private final Context context;
    private final List<Modal_Student> studentList = new ArrayList();
    private final List<Modal_Groups> groupList = new ArrayList();
    private final List<String> villageIDList = new ArrayList();
    @Bean(PD_ApiRequest.class)
    PD_ApiRequest pd_apiRequest;
    private PullDataContract.PullDataView pullDataView;
    private String selectedBlock;
    private String selectedProgram;
    private int count = 0;
    private int groupCount = 0;
    private ArrayList<Modal_Village> vilageList = new ArrayList<>();
    private List<Modal_Crl> crlList = new ArrayList<>();
    private List<ModalProgram> prgrmList = new ArrayList<>();

    public PullDataPresenterImp(Context context) {
        this.context = context;
    }

    @Override
    public void setView(PullDataFragment pullDataFragment) {
        this.pullDataView = pullDataFragment;
        pd_apiRequest.setApiResult(PullDataPresenterImp.this);
    }

    @Override
    public void loadSpinner() {
        String[] states = context.getResources().getStringArray(R.array.india_states);
        pullDataView.showStatesSpinner(states);
    }

    @Override
    public void proccessVillageData(String block) {
        ArrayList<Village> villageName = new ArrayList();
        for (Modal_Village vill : vilageList) {
            if (block.equalsIgnoreCase(vill.getBlock().trim()))
                villageName.add(new Village(vill.getVillageId(), vill.getVillageName()));
        }
        if (!villageName.isEmpty()) {
            pullDataView.showVillageDialog(villageName);
        }
    }

    @Override
    public void loadBloackSpinner(int pos, String selectedProgram) {
        pullDataView.showProgressDialog("loading Blocks");
        getUrlAndPull(pos, selectedProgram);
    }

    @Background
    public void getUrlAndPull(int pos, String selectedProgram) {
        String[] statesCodes = context.getResources().getStringArray(R.array.india_states_shortcode);
        selectedBlock = statesCodes[pos];
        this.selectedProgram = selectedProgram;
        String url;
        if (PrathamApplication.wiseF.isDeviceConnectedToSSID(PD_Constant.PRATHAM_KOLIBRI_HOTSPOT)) {
            url = APIs.pullVillagesKolibriURL + selectedProgram + APIs.KOLIBRI_STATE + selectedBlock;
            pd_apiRequest.pullFromKolibri(PD_Constant.KOLIBRI_BLOCK, url);
        } else if (PrathamApplication.wiseF.isDeviceConnectedToWifiNetwork() || PrathamApplication.wiseF.isDeviceConnectedToMobileNetwork()) {
            url = APIs.pullVillagesServerURL + selectedProgram + APIs.SERVER_STATE + selectedBlock;
            pd_apiRequest.pullFromInternet(PD_Constant.SERVER_BLOCK, url);
        }
    }

    @Override
    public void downloadStudentAndGroup(ArrayList<String> villageIDList1) {
        //download Student groups and KOLIBRI_CRL
        pullDataView.showProgressDialog("Please wait...");
        getStudentUrlAndFetch(villageIDList1);
    }

    @Background
    public void getStudentUrlAndFetch(ArrayList<String> villageIDList1) {
        villageIDList.clear();
        villageIDList.addAll(villageIDList1);
        studentList.clear();
        count = 0;
        String url;
        if (PrathamApplication.wiseF.isDeviceConnectedToSSID(PD_Constant.PRATHAM_KOLIBRI_HOTSPOT)) {
            for (String id : villageIDList) {
                url = APIs.pullStudentsKolibriURL + selectedProgram + APIs.KOLIBRI_VILLAGE + id;
                pd_apiRequest.pullFromKolibri(PD_Constant.KOLIBRI_STU, url);
            }
        } else if (PrathamApplication.wiseF.isDeviceConnectedToWifiNetwork() || PrathamApplication.wiseF.isDeviceConnectedToMobileNetwork()) {
            for (String id : villageIDList) {
                url = APIs.pullStudentsServerURL + selectedProgram + APIs.SERVER_VILLAGE + id;
                pd_apiRequest.pullFromInternet(PD_Constant.SERVER_STU, url);
            }
        }
    }

    private void loadGroups() {
        if (count >= villageIDList.size()) {
            groupCount = 0;
            groupList.clear();
            String urlgroup;
            if (PrathamApplication.wiseF.isDeviceConnectedToSSID(PD_Constant.PRATHAM_KOLIBRI_HOTSPOT)) {
                for (String id : villageIDList) {
                    urlgroup = APIs.pullGroupsKolibriURL + selectedProgram + APIs.KOLIBRI_VILLAGE + id;
                    pd_apiRequest.pullFromKolibri(PD_Constant.KOLIBRI_GRP, urlgroup);
                }
            } else if (PrathamApplication.wiseF.isDeviceConnectedToWifiNetwork() || PrathamApplication.wiseF.isDeviceConnectedToMobileNetwork()) {
                for (String id : villageIDList) {
                    urlgroup = APIs.pullGroupsServerURL + selectedProgram + APIs.SERVER_VILLAGE + id;
                    pd_apiRequest.pullFromInternet(PD_Constant.SERVER_GRP, urlgroup);
                }
            }
        }
    }

    private void loadCRL() {
        if (groupCount >= villageIDList.size()) {
            String crlURL;
            if (PrathamApplication.wiseF.isDeviceConnectedToSSID(PD_Constant.PRATHAM_KOLIBRI_HOTSPOT)) {
                crlURL = APIs.pullCrlsKolibriURL + selectedProgram + APIs.KOLIBRI_STATE + selectedBlock;
                pd_apiRequest.pullFromKolibri(PD_Constant.KOLIBRI_CRL, crlURL);
            } else if (PrathamApplication.wiseF.isDeviceConnectedToWifiNetwork() || PrathamApplication.wiseF.isDeviceConnectedToMobileNetwork()) {
                crlURL = APIs.pullCrlsServerURL + selectedProgram + APIs.SERVER_STATECODE + selectedBlock;
                pd_apiRequest.pullFromInternet(PD_Constant.SERVER_CRL, crlURL);
            }
        }
    }

    @Background
    @Override
    public void saveData() {
        crLdao.insertAllCRL(crlList);
        //To safely remove from a collection while iterating over it, Iterator should be used.
        Iterator<Modal_Student> i = studentList.iterator();
        while (i.hasNext()) {
            Modal_Student stu = i.next(); // must be called before you can call i.remove()
            if (stu.getGender().equalsIgnoreCase("deleted"))
                i.remove();
        }
        studentDao.insertAllStudents(studentList);
        Iterator<Modal_Groups> gi = groupList.iterator();
        while (gi.hasNext()) {
            Modal_Groups stu = gi.next(); // must be called before you can call i.remove()
            if (stu.getDeviceId().equalsIgnoreCase("deleted"))
                gi.remove();
        }
        groupDao.insertAllGroups(groupList);
        saveDownloadedVillages();
        statusDao.updateValue("programId", selectedProgram);
        pullDataView.openLoginActivity();
    }

    private void saveDownloadedVillages() {
        for (Modal_Village vill : vilageList) {
            if (villageIDList.contains(String.valueOf(vill.getVillageId())))
                villageDao.insertVillage(vill);
        }
    }

    //    @Background
    @Override
    public void clearLists() {
        if (crlList != null) {
            crlList.clear();
        }
        studentList.clear();
        groupList.clear();
        if (vilageList != null) {
            vilageList.clear();
        }
        villageIDList.clear();
        pullDataView.clearStateSpinner();
        pullDataView.clearBlockSpinner();
        pullDataView.disableSaveButton();
    }

    @Override
    public void onSaveClick() {
        pullDataView.showConfirmationDialog(crlList.size(), studentList.size(), groupList.size(), vilageList.size());
    }

    @Override
    public void recievedContent(String header, String response, ArrayList<Modal_ContentDetail> contentList) {
        Gson gson = new Gson();
        if (header.equalsIgnoreCase(PD_Constant.KOLIBRI_CRL)) {
            Type listType = new TypeToken<List<RaspCrl>>() {
            }.getType();
            ArrayList<RaspCrl> crlListTemp = gson.fromJson(response, listType);
            crlList.clear();
            for (RaspCrl raspCrl : crlListTemp) {
                crlList.addAll(raspCrl.getData());
            }
            pullDataView.closeProgressDialog();
            pullDataView.enableSaveButton();
        } else if (header.equalsIgnoreCase(PD_Constant.SERVER_CRL)) {
            Type listType = new TypeToken<List<Modal_Crl>>() {
            }.getType();
            List<Modal_Crl> temp = gson.fromJson(response, listType);
            crlList.addAll(temp);
            LinkedHashSet hs = new LinkedHashSet(crlList);
            crlList.clear();
            crlList.addAll(hs);
            pullDataView.closeProgressDialog();
            pullDataView.enableSaveButton();
        } else if (header.equalsIgnoreCase(PD_Constant.KOLIBRI_GRP)) {
            groupCount++;
            String json = response;
            Type listType = new TypeToken<List<RaspGroup>>() {
            }.getType();
            List<RaspGroup> groupListTemp = gson.fromJson(json, listType);
            for (RaspGroup raspGroup : groupListTemp) {
                groupList.addAll(raspGroup.getData());
            }
            loadCRL();
        } else if (header.equalsIgnoreCase(PD_Constant.SERVER_GRP)) {
            groupCount++;
            String json = response;
            Type listType = new TypeToken<List<Modal_Groups>>() {
            }.getType();
            List<Modal_Groups> temp = gson.fromJson(json, listType);
            groupList.addAll(temp);
            LinkedHashSet hs = new LinkedHashSet(groupList);
            groupList.clear();
            groupList.addAll(hs);
            loadCRL();
        } else if (header.equalsIgnoreCase(PD_Constant.KOLIBRI_STU)) {
            count++;
            String json = response;
            Type listType = new TypeToken<List<RaspStudent>>() {
            }.getType();
            List<RaspStudent> studentListTemp = gson.fromJson(json, listType);
            for (RaspStudent raspStudent : studentListTemp) {
                studentList.addAll(raspStudent.getData());
            }
            loadGroups();
        } else if (header.equalsIgnoreCase(PD_Constant.SERVER_STU)) {
            count++;
            String json = response;
            Type listType = new TypeToken<List<Modal_Student>>() {
            }.getType();
            List<Modal_Student> temp = gson.fromJson(json, listType);
            studentList.addAll(temp);
            LinkedHashSet hs = new LinkedHashSet(studentList);
            studentList.clear();
            studentList.addAll(hs);
            loadGroups();
        } else if (header.equalsIgnoreCase(PD_Constant.KOLIBRI_BLOCK)) {
            List<String> blockList = new ArrayList<>();
            Type listType = new TypeToken<List<RaspVillage>>() {
            }.getType();
            List<RaspVillage> raspvilageList = gson.fromJson(response, listType);
            if (raspvilageList != null) {
                if (raspvilageList.isEmpty()) {
                    blockList.add("NO BLOCKS");
                } else {
                    blockList.add("Select block");
                    for (RaspVillage raspVillage : raspvilageList) {
                        //                                    for (Modal_Village village : raspVillage.getData()) {
                        vilageList.add(raspVillage.getData());
                        blockList.add(raspVillage.getData().getBlock());
                        //                                    }
                    }
                }
                LinkedHashSet hs = new LinkedHashSet(blockList);
                blockList.clear();
                blockList.addAll(hs);
                LinkedHashSet hs1 = new LinkedHashSet(vilageList);
                vilageList.clear();
                vilageList.addAll(hs1);
                pullDataView.showBlocksSpinner(blockList);
            }
            pullDataView.closeProgressDialog();
        } else if (header.equalsIgnoreCase(PD_Constant.SERVER_BLOCK)) {
            List<String> blockList = new ArrayList<>();
            Type listType = new TypeToken<List<Modal_Village>>() {
            }.getType();
            vilageList = gson.fromJson(response, listType);
            if (vilageList != null) {
                if (vilageList.isEmpty()) {
                    blockList.add("NO BLOCKS");
                } else {
                    blockList.add("Select block");
                    for (Modal_Village vill : vilageList) {
                        blockList.add(vill.getBlock());
                    }
                }
                LinkedHashSet hs = new LinkedHashSet(blockList);
                blockList.clear();
                blockList.addAll(hs);
                pullDataView.showBlocksSpinner(blockList);
            }
            pullDataView.closeProgressDialog();
        } else if (header.equalsIgnoreCase(PD_Constant.KOLIBRI_PROGRAM)) {
            prgrmList.clear();
            Type listType = new TypeToken<List<RaspProgram>>() {
            }.getType();
            List<RaspProgram> prgm = gson.fromJson(response, listType);
            if (prgm != null) {
                for (RaspProgram prg : prgm) {
                    ModalProgram mp = new ModalProgram();
                    mp.setProgramId(prg.getData().getKolibriProgramId());
                    mp.setProgramName(prg.getData().getKolibriProgramName());
                    prgrmList.add(mp);
                }
                ModalProgram mp = new ModalProgram();
                mp.setProgramId("-1");
                mp.setProgramName("Select Program");
                LinkedHashSet hs = new LinkedHashSet(prgrmList);//to remove redundant values
                prgrmList.clear();
                prgrmList.addAll(hs);
                prgrmList.add(0, mp);
                pullDataView.showProgram(prgrmList);
            }
        } else if (header.equalsIgnoreCase(PD_Constant.SERVER_PROGRAM)) {
            prgrmList.clear();
            Type listType = new TypeToken<List<ModalProgram>>() {
            }.getType();
            prgrmList = gson.fromJson(response, listType);
            if (prgrmList != null) {
                ModalProgram modalProgram = new ModalProgram();
                modalProgram.setProgramId("-1");
                modalProgram.setProgramName("Select Program");
                LinkedHashSet hs = new LinkedHashSet(prgrmList);//to remove redundant values
                prgrmList.clear();
                prgrmList.addAll(hs);
                prgrmList.add(0, modalProgram);
                pullDataView.showProgram(prgrmList);
            }
        }
    }

    @Override
    public void recievedError(String header, ArrayList<Modal_ContentDetail> contentList) {
        if (header.equalsIgnoreCase(PD_Constant.SERVER_BLOCK) || header.equalsIgnoreCase(PD_Constant.KOLIBRI_BLOCK)) {
            pullDataView.closeProgressDialog();
            pullDataView.clearBlockSpinner();
            pullDataView.showErrorToast();
        } else if (header.equalsIgnoreCase(PD_Constant.SERVER_STU) || header.equalsIgnoreCase(PD_Constant.KOLIBRI_STU)
                || header.equalsIgnoreCase(PD_Constant.SERVER_GRP) || header.equalsIgnoreCase(PD_Constant.KOLIBRI_GRP)) {
            studentList.clear();
            pullDataView.closeProgressDialog();
            pullDataView.showErrorToast();
        } else if (header.equalsIgnoreCase(PD_Constant.SERVER_CRL) || header.equalsIgnoreCase(PD_Constant.KOLIBRI_CRL)) {
            pullDataView.closeProgressDialog();
            pullDataView.showErrorToast();
        }
    }

    //    @Background
    @Override
    public void clearData() {
        clearData_();
        pullDataView.onDataClearToast();
    }

    @Background
    public void clearData_() {
        villageDao.deleteAllVillages();
        groupDao.deleteAllGroups();
        studentDao.deleteAllStudents();
        crLdao.deleteAllCRLs();
    }

    @Override
    public void loadProgrammes() {
        if (PrathamApplication.wiseF.isDeviceConnectedToSSID(PD_Constant.PRATHAM_KOLIBRI_HOTSPOT))
            pd_apiRequest
                    .pullFromKolibri(PD_Constant.KOLIBRI_PROGRAM, PD_Constant.URL.DATASTORE_RASPBERY_PROGRAM_STATE_URL.toString());
        else if (PrathamApplication.wiseF.isDeviceConnectedToWifiNetwork() || PrathamApplication.wiseF.isDeviceConnectedToMobileNetwork())
            pd_apiRequest
                    .pullFromInternet(PD_Constant.SERVER_PROGRAM, PD_Constant.URL.PULL_PROGRAMS.toString());
    }
}
