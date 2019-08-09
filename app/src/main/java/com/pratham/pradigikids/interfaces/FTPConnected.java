package com.pratham.pradigikids.interfaces;

//import org.apache.commons.net.ftp.FTPFile;

import org.apache.commons.net.ftp.FTPClient;

/**
 * Created by pefpr on 31/01/2018.
 */

public interface FTPConnected {
    void onFTPConnected(boolean connected, FTPClient client);
}
