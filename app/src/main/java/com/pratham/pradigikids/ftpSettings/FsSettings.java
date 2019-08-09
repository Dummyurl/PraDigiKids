/*
Copyright 2011-2013 Pieter Pareit
Copyright 2009 David Revell

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.pratham.pradigikids.ftpSettings;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.ftpSettings.server.FtpUser;
import com.pratham.pradigikids.util.PD_Constant;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FsSettings {

    private final static String TAG = FsSettings.class.getSimpleName();

    public static List<FtpUser> getUsers() {
//        final SharedPreferences sp = getSharedPreferences();
        if (FastSave.getInstance().isKeyExists("users")/*.contains("users")*/) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<FtpUser>>() {
            }.getType();
            return gson.fromJson(FastSave.getInstance().getString("users", null), listType);
        } else if (FastSave.getInstance().isKeyExists("username")/*.contains("username")*/) {
            // on ftp server version < 2.19 we had username/password preference
            String username = FastSave.getInstance().getString("username", "ftp");
            String password = FastSave.getInstance().getString("password", "ftp");
            String chroot = FastSave.getInstance().getString("chrootDir", "");
            return new ArrayList<>(Arrays.asList(new FtpUser(username, password, chroot)));
        } else {
            FtpUser defaultUser = new FtpUser("ftp", "ftp", "\\");
            return new ArrayList<>(Arrays.asList(defaultUser));
        }
    }

    public static FtpUser getUser(String username) {
        // on java 8 (and android support) we can use getUsers().stream.filter(...)
        for (FtpUser user : getUsers()) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public static void addUser(FtpUser user) {
        if (getUser(user.getUsername()) != null) {
            throw new IllegalArgumentException("User already exists");
        }
//        SharedPreferences sp = getSharedPreferences();
        Gson gson = new Gson();
        List<FtpUser> userList = getUsers();
        userList.add(user);
        FastSave.getInstance().saveString("users", gson.toJson(userList));
//        sp.edit().putString("users", gson.toJson(userList)).apply();
    }

    public static void removeUser(String username) {
        // on java 8 (and android support) we can use getUsers().removeIf(...)
//        SharedPreferences sp = getSharedPreferences();
        Gson gson = new Gson();
        List<FtpUser> users = getUsers();
        List<FtpUser> found = new ArrayList<FtpUser>();
        for (FtpUser user : users) {
            if (user.getUsername().equals(username)) {
                found.add(user);
            }
        }
        users.removeAll(found);
        FastSave.getInstance().saveString("users", gson.toJson(users));
//        sp.edit().putString("users", gson.toJson(users)).apply();
    }

    public static void modifyUser(String username, FtpUser newUser) {
        removeUser(username);
        addUser(newUser);
    }

    public static boolean allowAnoymous() {
//        final SharedPreferences sp = getSharedPreferences();
        return /*sp.getBoolean("allow_anonymous", false)*/true;
    }

    public static File getDefaultChrootDir() {
        File chrootDir;
        if (PrathamApplication.contentExistOnSD) {
            chrootDir = new File(PrathamApplication.contentSDPath);
        } else {
            chrootDir = new File(PrathamApplication.pradigiPath);
        }
        if (!chrootDir.isDirectory()) {
            Log.e(TAG, "getChrootDir: not a directory");
            // if this happens, we are screwed
            // we give it the application directory
            // but this will probably not be what the user wants
            return new File(PrathamApplication.pradigiPath);
        }
        return chrootDir;
    }

    public static int getPortNumber() {
//        final SharedPreferences sp = getSharedPreferences();
        // port is always an number, so store this accordingly
//        String portString = sp.getString("portNum", "2121");
//        int port = Integer.valueOf(portString);
//        Log.v(TAG, "Using port: " + port);
        return 8080;
    }

    public static boolean shouldTakeFullWakeLock() {
//        final SharedPreferences sp = getSharedPreferences();
        return /*sp.getBoolean("stayAwake", false)*/true;
    }

//    public static Set<String> getAutoConnectList() {
//        SharedPreferences sp = getSharedPreferences();
//        return sp.getStringSet("autoconnect_preference", new TreeSet<>());
//    }

//    public static void removeFromAutoConnectList(final String ssid) {
//        Set<String> autoConnectList = getAutoConnectList();
//        autoConnectList.remove(ssid);
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        editor.remove("autoconnect_preference").apply(); // work around bug in android
//        editor.putStringSet("autoconnect_preference", autoConnectList).apply();
//    }

    public static int getTheme() {
//        SharedPreferences sp = getSharedPreferences();

//        switch (sp.getString("theme", "0")) {
//            case "0":
//                return R.style.AppTheme;
////            case "1":
////                return R.style.AppThemeLight;
////            case "2":
////                return R.style.AppThemeLight_DarkActionBar;
//        }

        return R.style.AppTheme;
    }

    public static boolean showNotificationIcon() {
//        SharedPreferences sp = getSharedPreferences();
        return /*sp.getBoolean("show_notification_icon_preference", true)*/true;
    }

    /**
     * @return the SharedPreferences for this application
     */
//    private static SharedPreferences getSharedPreferences() {
//        final Context context = PrathamApplication.getInstance();
//        return PreferenceManager.getDefaultSharedPreferences(context);
//    }

    // cleaning up after his
    protected static int inputBufferSize = 256;
    protected static boolean allowOverwrite = false;
    protected static int dataChunkSize = 8192; // do file I/O in 8k chunks
    protected static int sessionMonitorScrollBack = 10;
    protected static int serverLogScrollBack = 10;

    public static int getInputBufferSize() {
        return inputBufferSize;
    }

    public static void setInputBufferSize(int inputBufferSize) {
        FsSettings.inputBufferSize = inputBufferSize;
    }

    public static boolean isAllowOverwrite() {
        return allowOverwrite;
    }

    public static void setAllowOverwrite(boolean allowOverwrite) {
        FsSettings.allowOverwrite = allowOverwrite;
    }

    public static int getDataChunkSize() {
        return dataChunkSize;
    }

    public static void setDataChunkSize(int dataChunkSize) {
        FsSettings.dataChunkSize = dataChunkSize;
    }

    public static int getSessionMonitorScrollBack() {
        return sessionMonitorScrollBack;
    }

    public static void setSessionMonitorScrollBack(int sessionMonitorScrollBack) {
        FsSettings.sessionMonitorScrollBack = sessionMonitorScrollBack;
    }

    public static int getServerLogScrollBack() {
        return serverLogScrollBack;
    }

    public static void setLogScrollBack(int serverLogScrollBack) {
        FsSettings.serverLogScrollBack = serverLogScrollBack;
    }

    public static String getExternalStorageUri() {
//        final SharedPreferences sp = getSharedPreferences();
        return /*sp.getString("externalStorageUri", null)*/FastSave.getInstance().getString(PD_Constant.SDCARD_URI, null);
    }

    public static void setExternalStorageUri(String externalStorageUri) {
//        final SharedPreferences sp = getSharedPreferences();
//        sp.edit().putString("externalStorageUri", externalStorageUri).apply();
    }

}
