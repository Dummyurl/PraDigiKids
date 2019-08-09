package com.pratham.pradigikids.async;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.models.Modal_ContentDetail;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.apache.commons.net.ftp.FTPClient;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FTPSingleFileUploadTask extends AsyncTask {
    private final FTPClient client;
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final String localFilePath;
    private final boolean isImage;
    private final ArrayList<Modal_ContentDetail> levels;
    private long lenghtOfFile;
    private boolean isProfileSharing = false;
    private long total = 0;

    public FTPSingleFileUploadTask(Context context, FTPClient client, String localFilePath, boolean isImage, ArrayList<Modal_ContentDetail> levels) {
        this.context = context;
        this.client = client;
        this.localFilePath = localFilePath;
        this.isImage = isImage;
        this.levels = levels;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            client.enterLocalPassiveMode();
            String remoteFilePath;
            if (isImage) {
                client.makeDirectory("PrathamImages");
                for (Modal_ContentDetail detail : levels) {
                    String imgDirPath = localFilePath + "/PrathamImages/" + detail.getNodeimage();
                    String remoteFPath = "/PrathamImages/" + new File(imgDirPath).getName();
                    uploadSingleFile(client, imgDirPath, remoteFPath, false);
                }
            } else {
                if (localFilePath.endsWith(".json")) {
                    //means contents are being send i.e video/pdf/games
                    remoteFilePath = "/" + new File(localFilePath).getName();
                    uploadSingleFile(client, localFilePath, remoteFilePath, false);
                } else {
                    //profiles or usages are being sent
                    File file = new File(localFilePath);
                    lenghtOfFile = PD_Utility.folderSize(file);
                    isProfileSharing = true;
                    if (file.isDirectory())
                        for (File f : file.listFiles()) {
                            remoteFilePath = "/" + f.getName();
                            uploadSingleFile(client, f.getAbsolutePath(), remoteFilePath, true);
                        }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void uploadSingleFile(FTPClient ftpClient, String localFilePath, String remoteFilePath, boolean isProfileSharing) {
        try {
            File localFile = new File(localFilePath);
            total += localFile.length();
            FileInputStream in = new FileInputStream(localFile);
            boolean result = ftpClient.storeFile(remoteFilePath, in);
            Log.v("upload_result:::", "" + result);
//            if (result && !isImage) localFile.delete();
            if (isProfileSharing)
                publishProgress((total * 100) / lenghtOfFile, localFile.getName());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        EventMessage msg = new EventMessage();
        msg.setMessage(PD_Constant.FILE_SHARE_PROGRESS);
        msg.setProgress((Long) values[0]);
        msg.setFile_name((String) values[1]);
        EventBus.getDefault().post(msg);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        File path = context.getDir(PD_Constant.PRATHAM_TEMP_FILES, Context.MODE_PRIVATE);
        if (path.exists())
            PD_Utility.deleteRecursive(path);
        if (isProfileSharing) {
            EventMessage msg = new EventMessage();
            msg.setMessage(PD_Constant.FILE_SHARE_COMPLETE);
            EventBus.getDefault().post(msg);
        }
    }
}
