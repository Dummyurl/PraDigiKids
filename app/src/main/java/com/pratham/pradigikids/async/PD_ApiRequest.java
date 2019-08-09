package com.pratham.pradigikids.async;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.dbclasses.BackupDatabase;
import com.pratham.pradigikids.interfaces.ApiResult;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.models.Modal_ContentDetail;
import com.pratham.pradigikids.models.Modal_Download;
import com.pratham.pradigikids.models.Modal_FileDownloading;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.SpeedMonitor;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;

import static com.pratham.pradigikids.PrathamApplication.logDao;
import static com.pratham.pradigikids.PrathamApplication.modalContentDao;

/**
 * Created by HP on 30-12-2016.
 */

@EBean
public class PD_ApiRequest {
    private final Context mContext;
    private ApiResult apiResult = null;
//    OkHttpClient okHttpClient;

    public PD_ApiRequest(Context mContext) {
        this.mContext = mContext;
    }

    public void setApiResult(ApiResult result) {
        this.apiResult = result;
    }

    public void getContentFromRaspberry(final String requestType, String url, ArrayList<Modal_ContentDetail> contentList) {
        try {
            AndroidNetworking.get(url)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader())
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            if (apiResult != null)
                                apiResult.recievedContent(requestType, response, contentList);
                        }

                        @Override
                        public void onError(ANError anError) {
                            if (apiResult != null)
                                apiResult.recievedError(requestType, contentList);
                            Log.d("Error::", anError.getErrorDetail());
                            Log.d("Error::", anError.getResponse().toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getContentFromInternet(final String requestType, String url, ArrayList<Modal_ContentDetail> contentList) {
        try {
            AndroidNetworking.get(url)
                    .addHeaders("Content-Type", "application/json")
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            if (apiResult != null)
                                apiResult.recievedContent(requestType, response, contentList);
                        }

                        @Override
                        public void onError(ANError anError) {
                            if (apiResult != null)
                                apiResult.recievedError(requestType, contentList);
                            Log.d("Error:", anError.getErrorDetail());
                            Log.d("Error::", anError.getResponse().toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pushDataToRaspberry(/*final String requestType, */String url, String data,
                                                                  String filter_name, String table_name) {
        AndroidNetworking.post(url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", getAuthHeader())
                .addBodyParameter("filter_name", filter_name)
                .addBodyParameter("table_name", table_name)
                .addBodyParameter("facility", FastSave.getInstance().getString(PD_Constant.FACILITY_ID, ""))
                .addBodyParameter("data", data)
                .setExecutor(Executors.newSingleThreadExecutor())
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
//                        apiResult.notifySuccess(requestType, "success");
                        logDao.deleteLogs();
                        BackupDatabase.backup(PrathamApplication.getInstance());
                        EventMessage msg = new EventMessage();
                        msg.setMessage(PD_Constant.SUCCESSFULLYPUSHED);
                        msg.setPushData(data);
                        EventBus.getDefault().post(msg);
                    }

                    @Override
                    public void onError(ANError anError) {
                        EventMessage msg = new EventMessage();
                        msg.setMessage(PD_Constant.PUSHFAILED);
                        EventBus.getDefault().post(msg);
                        Log.d("Error::", anError.getErrorDetail());
                        Log.d("Error::", anError.getMessage());
                        Log.d("Error::", anError.getResponse().toString());
                    }
                });
    }

    public void pushDataToInternet(/*final String requestType, */String url, JSONObject data) {
        AndroidNetworking.post(url)
//                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(data)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        logDao.deleteLogs();
                        BackupDatabase.backup(PrathamApplication.getInstance());
                        EventMessage msg = new EventMessage();
                        msg.setMessage(PD_Constant.SUCCESSFULLYPUSHED);
                        msg.setPushData(data.toString());
                        EventBus.getDefault().post(msg);
                    }

                    @Override
                    public void onError(ANError anError) {
                        EventMessage msg = new EventMessage();
                        msg.setMessage(PD_Constant.PUSHFAILED);
                        EventBus.getDefault().post(msg);
                        Log.d("Error::", anError.getErrorDetail());
                        Log.d("Error::", anError.getMessage());
                        Log.d("Error::", anError.getResponse().toString());
                    }
                });
    }

    public void getacilityIdfromRaspberry(final String requestType, String url, JSONObject data) {
        AndroidNetworking.post(url)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(data)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        apiResult.recievedContent(requestType, response, null);
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (apiResult != null)
//                            apiResult.notifyError(requestType/*, null*/);
                            Log.d("Error::", anError.getErrorDetail());
                        Log.d("Error::", anError.getMessage());
                        Log.d("Error::", anError.getResponse().toString());
                    }
                });
    }

    private String getAuthHeader() {
        String encoded = Base64.encodeToString(("pratham" + ":" + "pratham").getBytes(), Base64.NO_WRAP);
        return "Basic " + encoded;
    }

    public static void downloadImage(String url, String filename) {
        File dir = new File(PrathamApplication.pradigiPath + "/PrathamImages"); //Creating an internal dir;
        if (!dir.exists()) dir.mkdirs();
        AndroidNetworking.download(url, dir.getAbsolutePath(), filename)
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.d("image::", "DownloadComplete");
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("image::", "Not Downloaded");
                    }
                });
    }

    public void pullFromKolibri(String header, String url) {
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", getAuthHeader())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (apiResult != null)
                            apiResult.recievedContent(header, response.toString(), null);
                    }

                    @Override
                    public void onError(ANError error) {
                        if (apiResult != null)
                            apiResult.recievedError(header, null);
                    }
                });
    }

    public void pullFromInternet(String header, String url) {
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
//                .addHeaders("Authorization", getAuthHeader("pratham", "pratham"))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (apiResult != null)
                            apiResult.recievedContent(header, response.toString(), null);
                    }

                    @Override
                    public void onError(ANError error) {
                        if (apiResult != null)
                            apiResult.recievedError(header, null);
                    }
                });
    }

    public static void downloadAajKaSawal(String url, String filename) {
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            File aksFile = new File(PrathamApplication.pradigiPath + "/" + filename); //Creating an internal dir;
                            if (!aksFile.exists()) aksFile.createNewFile();
                            Writer output = new BufferedWriter(new FileWriter(aksFile));
                            output.write(response.toString());
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void downloadContentFromServer(Modal_Download modal_download) {
        notifyAdapter(modal_download);
        dowloadImages(modal_download, modal_download.getLevelContents());
        AndroidNetworking.download(modal_download.getUrl(), modal_download.getDir_path(), modal_download.getF_name())
                .setTag(modal_download.getContent().getNodeid())
                .setPriority(Priority.MEDIUM)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .setDownloadProgressListener((bytesDownloaded, totalBytes) -> {
                    if (totalBytes < 0)
                        totalBytes = (modal_download.getContent().getLevel() > 0) ? modal_download.getContent().getLevel() : 1;
                    updateProgress(modal_download, totalBytes, bytesDownloaded);
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        notifyDownloadSuccess(modal_download);
                    }

                    @Override
                    public void onError(ANError anError) {
                        EventMessage msg = new EventMessage();
                        msg.setMessage(PD_Constant.FAST_DOWNLOAD_ERROR);
                        msg.setDownloadId(modal_download.getContent().getNodeid());
                        EventBus.getDefault().post(msg);
                    }
                });
    }

    @Background
    public void notifyDownloadSuccess(Modal_Download modal_download) {
        if (Objects.requireNonNull(modal_download).getFolder_name().equalsIgnoreCase(PD_Constant.GAME))
            unzipFile(modal_download.getDir_path() + "/" + modal_download.getF_name(), modal_download.getDir_path());
        modal_download.getContent().setContentType("file");
        ArrayList<Modal_ContentDetail> temp = new ArrayList<>(modal_download.getLevelContents());
        temp.add(modal_download.getContent());
        for (Modal_ContentDetail d : temp) {
            if (d.getNodeimage() != null) {
                String img_name = d.getNodeimage().substring(d.getNodeimage().lastIndexOf('/') + 1);
                d.setNodeimage(img_name);
            }
            d.setContent_language(FastSave.getInstance().getString(PD_Constant.LANGUAGE, PD_Constant.HINDI));
            d.setDownloaded(true);
            d.setOnSDCard(false);
        }
        modalContentDao.addContentList(temp);
        EventMessage message = new EventMessage();
        message.setMessage(PD_Constant.FAST_DOWNLOAD_COMPLETE);
        message.setDownloadId(modal_download.getContent().getNodeid());
        message.setContentDetail(modal_download.getContent());
        EventBus.getDefault().post(message);
    }

    @Background
    public void updateProgress(Modal_Download modal_download, long totalBytes, long bytesDownloaded) {
        Modal_FileDownloading modal_fileDownloading = new Modal_FileDownloading();
        modal_fileDownloading.setDownloadId(Objects.requireNonNull(modal_download).getContent().getNodeid());
        modal_fileDownloading.setFilename(modal_download.getContent().getNodetitle());
        modal_fileDownloading.setProgress((int) ((100 * bytesDownloaded) / totalBytes));
        modal_fileDownloading.setContentDetail(modal_download.getContent());
        modal_fileDownloading.setRemaining_time(SpeedMonitor.compute((int) (totalBytes - bytesDownloaded)));
        EventMessage eventMessage = new EventMessage();
        eventMessage.setMessage(PD_Constant.FAST_DOWNLOAD_UPDATE);
        eventMessage.setDownloadId(modal_download.getContent().getNodeid());
        eventMessage.setModal_fileDownloading(modal_fileDownloading);
        EventBus.getDefault().post(eventMessage);
    }

    @Background
    public void notifyAdapter(Modal_Download modal_download) {
        Modal_FileDownloading modal_fileDownloading = new Modal_FileDownloading();
        modal_fileDownloading.setDownloadId(modal_download.getContent().getNodeid());
        modal_fileDownloading.setFilename(modal_download.getContent().getNodetitle());
        modal_fileDownloading.setProgress(0);
        modal_fileDownloading.setContentDetail(modal_download.getContent());
        EventMessage msg = new EventMessage();
        msg.setMessage(PD_Constant.FAST_DOWNLOAD_STARTED);
        msg.setDownloadId(modal_download.getContent().getNodeid());
        msg.setModal_fileDownloading(modal_fileDownloading);
        EventBus.getDefault().post(msg);
    }

    @Background
    public void dowloadImages(Modal_Download modal_download, ArrayList<Modal_ContentDetail> levelContents) {
        for (Modal_ContentDetail detail : levelContents) {
            if (detail.getNodeserverimage() != null) {
                String f_name = detail.getNodeserverimage()
                        .substring(detail.getNodeserverimage().lastIndexOf('/') + 1);
                downloadImage(detail.getNodeserverimage(), f_name);
            }
        }
        if (modal_download.getContent().getNodeserverimage() != null) {
            String f_name = modal_download.getContent().getNodeserverimage()
                    .substring(modal_download.getContent().getNodeserverimage().lastIndexOf('/') + 1);
            downloadImage(modal_download.getContent().getNodeserverimage(), f_name);
        }
    }

    private void unzipFile(String source, String destination) {
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
            new File(source).delete();
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
