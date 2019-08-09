package com.pratham.pradigikids.ftpSettings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.pratham.pradigikids.interfaces.FTPConnected;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class ConnectToFTP extends AsyncTask<Void, Void, Boolean> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private FTPClient client1;
    private FTPConnected ftpConnected;
    private String scanned_qr_ip;

    public ConnectToFTP(Context context, FTPConnected ftpConnected, String ip) {
        this.context = context;
        this.ftpConnected = ftpConnected;
        this.scanned_qr_ip = ip;
//        this.ftpConnectInterface = ftpConnectInterface;
//        this.ipaddress = ipaddress.replace("ftp://", "");
//        this.port = port;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        pd = new ProgressDialog(context);
//        pd.setMessage("Connecting ... Please wait !!!");
//        pd.setCanceledOnTouchOutside(false);
//        pd.setCancelable(false);
//        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        client1 = new FTPClient();
        try {
//            ipaddress = ipaddress.replace("ftp://", "");
//            ipaddress = ipaddress.replace(":8080", "");
            Thread.sleep(2500);
            client1.setControlEncoding("UTF-8");
            client1.setAutodetectUTF8(true);
            /*The Apache Commons Net library believes that the 220 response from the server does not conform to RFC 959 (probably rightfully).
            If you want to allow the library to talk to the server, call FTP.setStrictReplyParsing*/
            client1.setStrictReplyParsing(false);
            client1.connect(scanned_qr_ip, 8080);
            client1.login("ftp", "ftp");
//            client1.changeWorkingDirectory("/storage/sdcard1");
            client1.enterLocalPassiveMode();
            client1.setFileType(FTP.BINARY_FILE_TYPE);
            FTPFile[] files = client1.listFiles();
            String wor = client1.printWorkingDirectory();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean connected) {
        super.onPostExecute(connected);
        if (connected) {
            ftpConnected.onFTPConnected(true, client1);
        } else {
            Toast.makeText(context, "Connecting... Please wait!", Toast.LENGTH_SHORT).show();
            ftpConnected.onFTPConnected(connected, null);
        }
    }
}
