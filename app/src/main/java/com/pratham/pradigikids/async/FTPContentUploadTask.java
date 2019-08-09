package com.pratham.pradigikids.async;

import android.os.AsyncTask;
import android.util.Log;

import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import org.apache.commons.net.ftp.FTPClient;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FTPContentUploadTask extends AsyncTask {
    private final FTPClient client;
    private final long lenghtOfFile;
    //    private Context context;
    private final String localPath;
    private final String contentType;
    private final String downloadId;
    private String remoteDirPath;
    private long total = 0;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            // use local passive mode to pass firewall
            client.enterLocalPassiveMode();
            client.makeDirectory(contentType);
            if (contentType.equalsIgnoreCase("PrathamGame")) {
                remoteDirPath += new File(localPath).getName();
                client.makeDirectory(remoteDirPath);
                uploadDirectory(client, remoteDirPath, localPath, "");
            } else {
                String remoteFilePath = "/" + contentType + "/" + new File(localPath).getName();
                uploadSingleFile(client, localPath, remoteFilePath);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public FTPContentUploadTask(/*Context context, */FTPClient client, String localPath, String contentType, String downloadId) {
//        this.context = context;
        this.client = client;
        this.localPath = localPath;
        this.downloadId = downloadId;
        this.contentType = "Pratham" + contentType;
        File f = new File(localPath);
        lenghtOfFile = (f.isFile()) ? f.length() : PD_Utility.folderSize(f);
        if (contentType.equalsIgnoreCase(PD_Constant.GAME))
            remoteDirPath = "/PrathamGame/";
        else if (contentType.equalsIgnoreCase(PD_Constant.VIDEO))
            remoteDirPath = "/PrathamVideo/";
        else if (contentType.equalsIgnoreCase(PD_Constant.PDF))
            remoteDirPath = "/PrathamPDF/";
    }

    private void uploadDirectory(FTPClient ftpClient, String remoteDirPath, String localParentDir,
                                 String remoteParentDir) {
        try {
            File localDir = new File(localParentDir);
            for (File subFile : localDir.listFiles()) {
                String remoteFilePath = remoteDirPath + "/" + remoteParentDir + "/" + subFile.getName();
                if (remoteParentDir.equals("")) {
                    remoteFilePath = remoteDirPath + "/" + subFile.getName();
                }
                if (subFile.isFile()) {
                    // upload the file
                    String localFilePath = subFile.getAbsolutePath();
                    uploadSingleFile(ftpClient, localFilePath, remoteFilePath);
                } else {
                    ftpClient.makeDirectory(remoteFilePath);
                    String parent = remoteParentDir + "/" + subFile.getName();
                    if (remoteParentDir.equals("")) {
                        parent = subFile.getName();
                    }
                    localParentDir = subFile.getAbsolutePath();
                    uploadDirectory(ftpClient, remoteDirPath, localParentDir, parent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadSingleFile(FTPClient ftpClient, String localFilePath, String remoteFilePath) {
        try {
            File localFile = new File(localFilePath);
            total += localFile.length();
            FileInputStream in = new FileInputStream(localFile);
            boolean result = ftpClient.storeFile(remoteFilePath, in);
            publishProgress((total * 100) / lenghtOfFile, localFile.getName());
            Log.v("upload_result:::", "" + result);
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
        msg.setDownloadId(downloadId);
        msg.setProgress((Long) values[0]);
        msg.setFile_name((String) values[1]);
        EventBus.getDefault().post(msg);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        EventMessage msg = new EventMessage();
        msg.setMessage(PD_Constant.FILE_SHARE_COMPLETE);
        EventBus.getDefault().post(msg);
    }
}
