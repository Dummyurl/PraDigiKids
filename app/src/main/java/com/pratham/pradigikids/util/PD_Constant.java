package com.pratham.pradigikids.util;

import android.support.annotation.NonNull;

public class PD_Constant {

    private static final String BASE_URL = "http://prodigi.openiscool.org/api/posv2/";
//        public static final String BASE_URL = "http://devprodigi.openiscool.org/api/posv2/";

    public static final String FTP_IP = "FTP_IP";
    public static final String FAST_DOWNLOAD_STARTED = "fast_download_started";
    public static final String FAST_DOWNLOAD_UPDATE = "fast_download_update";
    public static final String FAST_DOWNLOAD_COMPLETE = "fast_download_complete";
    public static final String FAST_DOWNLOAD_ERROR = "fast_download_error";
    public static final String MODAL_DOWNLOAD = "modal_download";
    public static final String MSG_HANDLER_KEY = "msg_handler_key";
    public static final String DOWNLOAD_PROGRESS = "download_progress";
    public static final String DOWNLOAD_SPEED_RATE = "download_speed_rate";
    public static final String RASP_IP = "http://192.168.4.1:8080";
    public static final String AVATAR = "avatar";
    public static final String INTERNET_HEADER = "INTERNET_HEADER";
    public static final String BROWSE_INTERNET = "BROWSE_INTERNET";
    public static final String INTERNET_DOWNLOAD = "INTERNET_DOWNLOAD";
    public static final String IS_GOOGLE_SIGNED_IN = "IS_GOOGLE_SIGNED_IN";
    public static final String GOOGLE_TOKEN = "GOOGLE_TOKEN";
    public static final String CONTENT = "CONTENT";
    public static final int LOCATION_GRANTED = 1;
    public static final String GAME = "game";
    public static final String VIDEO = "video";
    public static final String PDF = "pdf";
    public static final String SESSIONID = "sessionid";
    public static final String GROUPID = "groupid";
    public static final String STUDENTID = "studentid";
    public static final String CONTENT_BACK = "content_back";
    public static final String STUDENT_ADDED = "STUDENT_ADDED";
    public static final String STUDENT_LIST = "STUDENT_LIST";
    public static final String GROUP_AGE_BELOW_7 = "GROUP_AGE_BELOW_7";
    public static final String GROUPID1 = "group1";
    public static final String GROUPID2 = "group2";
    public static final String GROUPID3 = "group3";
    public static final String GROUPID4 = "group4";
    public static final String GROUPID5 = "group5";
    public static final String FACILITY_ID = "facility_id";
    public static final String WRITE_PERMISSION = "write_permission";
    public static final String DOWNLOAD_COMPLETE = "download_complete";
    public static final String REVEALX = "REVEALX";
    public static final String REVEALY = "REVEALY";
    public static final String LANGUAGE_BACK = "language_back";
    public static final String NO_CONNECTION = "no_connection";
    public static final String FTP_HOTSPOT_SSID = "ftp_hotspot_ssid";
    public static final String FTP_HOTSPOT_PASS = "ftp_hotspot_pass";
    public static final String GROUP_NAME = "group_name";
    public static final String PROFILE_NAME = "profile_name";
    public static final String SHOW_BACK = "show_back";
    public static final String PRADIGI_ICON = "pradigi_icon";
    public static final String CANCEL_DOWNLOAD = "cancel_download";
    public static final String DEEP_LINK = "deep_link";
    public static final String DEEP_LINK_CONTENT = "deep_link_content";
    public static final String AKS_QUESTION = "aks_file_path";
    public static final String VIDEO_PLAYER_BACK_PRESS = "video_player_back_press";
    public static final String GPS_SESSION = "GPS_SESSION_";
    public static final String GPS_MINIMUM_INTERVAL = "GPS_MINIMUM_INTERVAL";
    public static final String GPS_MINIMUM_DISTANCE = "GPS_MINIMUM_DISTANCE";
    public static final String GPS_MINIMUM_ACCURACY = "GPS_MINIMUM_ACCURACY";
    public static final String KEEP_GPS_ON_BETWEEN_FIXES = "KEEP_GPS_ON_BETWEEN_FIXES";
    public static final String GPS_LOGGING_RETRY_TIME = "GPS_LOGGING_RETRY_TIME";
    public static final String GPS_ALTITUDE_SUBTRACT_OFFSET = "GPS_ALTITUDE_SUBTRACT_OFFSET";
    public static final String GPS_ALTITUDE_SHOULD_ADJUST = "GPS_ALTITUDE_SHOULD_ADJUST";
    public static final String GPS_ACTIVITYRECOGNITION_DONTLOGIFSTILL = "GPS_ACTIVITYRECOGNITION_DONTLOGIFSTILL";
    public static final String GPS_LOCATION_LISTENERS = "GPS_LOCATION_LISTENERS";
    public static final String GPS_LATLONG_DISPLAY_FORMAT = "GPS_LATLONG_DISPLAY_FORMAT";
    public static final String GPS_HIDE_NOTIFICATION_FROM_STATUS_BAR = "GPS_HIDE_NOTIFICATION_FROM_STATUS_BAR";
    public static final String GPS_HIDE_NOTIFICATION_BUTTONS = "GPS_HIDE_NOTIFICATION_BUTTONS";
    public static final String GPS_ABSOLUTE_TIMEOUT = "GPS_ABSOLUTE_TIMEOUT";
    public static final String GPS_LOG_PASSIVE_LOCATIONS = "GPS_LOG_PASSIVE_LOCATIONS";
    public static final String GPS_HDOP = "GPS_HDOP";
    public static final String GPS_PDOP = "GPS_PDOP";
    public static final String GPS_VDOP = "GPS_VDOP";
    public static final String GPS_GEOIDHEIGHT = "GPS_GEOIDHEIGHT";
    public static final String GPS_AGEOFDGPSDATA = "GPS_AGEOFDGPSDATA";
    public static final String GPS_DGPSID = "GPS_DGPSID";
    public static final String GPS_PASSIVE = "GPS_PASSIVE";
    public static final String GPS_LISTENER = "GPS_LISTENER";
    public static final String GPS_SATELLITES_FIX = "GPS_SATELLITES_FIX";
    public static final String GPS_DETECTED_ACTIVITY = "GPS_DETECTED_ACTIVITY";
    public static final String GPS_LOGGER_IMMEDIATE_START = "GPS_LOGGER_IMMEDIATE_START";
    public static final String GPS_LOGGER_IMMEDIATE_STOP = "GPS_LOGGER_IMMEDIATE_STOP";
    public static final String GPS_LOGGER_GET_STATUS = "GPS_LOGGER_GET_STATUS";
    public static final String GPS_LOGGER_AUTOSEND_NOW = "GPS_LOGGER_AUTOSEND_NOW";
    public static final String GPS_LOGGER_GET_NEXT_POINT = "GPS_LOGGER_GET_NEXT_POINT";
    public static final String GPS_LOGGER_SET_DESCRIPTION = "GPS_LOGGER_SET_DESCRIPTION";
    public static final String GPS_LOGGER_PREFER_CELLTOWER = "GPS_LOGGER_PREFER_CELLTOWER";
    public static final String GPS_LOGGER_LOG_ONCE = "GPS_LOGGER_LOG_ONCE";
    public static final String GPS_LOGGER_ABSOLUTE_TIMEOUT = "GPS_LOGGER_ABSOLUTE_TIMEOUT";
    public static final String GPS_LOGGER_RETRY_TIME = "GPS_LOGGER_RETRY_TIME";
    public static final String GPS_LOGGER_GPS_ON_BETWEEN_FIX = "GPS_LOGGER_GPS_ON_BETWEEN_FIX";
    public static final String GPS_LOGGER_DISTANCE_BEFORE_LOGGING = "GPS_LOGGER_DISTANCE_BEFORE_LOGGING";
    public static final String GPS_LOGGER_TIME_BEFORE_LOGGING = "GPS_LOGGER_TIME_BEFORE_LOGGING";
    public static final String LOCATION_CHANGED = "LOCATION_CHANGED";
    public static final String KOLIBRI_PROGRAM = "KOLIBRI_PROGRAM";
    public static final String SERVER_PROGRAM = "SERVER_PROGRAM";
    public static final String FRAGMENT_SHARE_BACK = "fragment_share_back";
    public static final String BACKUP_DB_COPIED = "BACKUP_DB_COPIED";
    public static int FTP_HOTSPOT_KEYMGMT = -11;
    public static final String CONNECTION_STATUS = "CONNECTION_STATUS";
    public static final String STORAGE_ASKED = "storage_asked";
    public static final String FROMDATE = "fromDate";
    public static final String TODATE = "toDate";
    public static final String SCORE = "scores";
    public static final String ATTENDANCE = "attendances";
    public static final String SESSION = "session";
    public static final String LOGS = "logs";
    public static final String STUDENTS = "students";
    public static final String METADATA = "metadata";
    public static final String USAGEDATA = "USAGEDATA";
    public static final String DOWNLOAD_STARTED = "download_started";
    public static final String DOWNLOAD_UPDATE = "download_update";
    public static final String PRADIGI_FOLDER = ".PraDigi";
    public static final String FILE_DOWNLOAD_COMPLETE = "file_download_complete";
    public static final String SCORE_COUNT = "ScoreCount";
    public static final String FOLDER = "folder";
    public static final String FILE = "file";
    public static final String FILE_SHARE_PROGRESS = "file_share_progress";
    public static final String SHARE_BACK = "share_back";
    public static final String CLOSE_FTP_SERVER = "close_ftp_server";
    public static final String FTP_CLIENT_CONNECTED = "ftp_client_connected";
    public static final String APK = "apk";
    public static final String FILE_DOWNLOAD_STARTED = "file_download_started";
    public static final String KOLIBRI_CRL = "kolibri_crl";
    public static final String SERVER_CRL = "server_crl";
    public static final String KOLIBRI_GRP = "kolibri_grp";
    public static final String SERVER_GRP = "server_grp";
    public static final String KOLIBRI_STU = "kolibri_stu";
    public static final String SERVER_STU = "server_stu";
    public static final String KOLIBRI_BLOCK = "kolibri_block";
    public static final String SERVER_BLOCK = "server_block";
    public static final String DOWNLOAD_FAILED = "download_failed";
    public static final String FILE_DOWNLOAD_ERROR = "FILE_DOWNLOAD_ERROR";
    public static final String BROADCAST_DOWNLOADINGS = "broadcast_downloadings";
    public static final String SDCARD_URI = "sdcard_uri";
    public static final String SDCARD_PATH = "sdcard_path";
    public static final String APP_VERSION = "app_version";
    public static final String FILE_SHARE_COMPLETE = "file_share_complete";
    public static final String SHARE_PROFILE = "id_profile_";
    public static final String SHARE_USAGE = "id_usage_";
    public static final String PRATHAM_TEMP_FILES = "pratham_temp_files";
    public static final String FILE_RECEIVE_COMPLETE = "file_receive_complete";
    public static final String FTP_KEYMGMT = "ftp_keymgmt";
    public static String HOTSPOT_SSID = "pratham";
    public static String HOTSPOT_PASSWORD = "";
    public static String STORING_IN = "";
    public static final String SUCCESSFULLYPUSHED = "successfully_pushed";
    public static final String PUSHFAILED = "push_failed";
    public static final String PRATHAM_KOLIBRI_HOTSPOT = "prathamkolibri";
    public static final String BROWSE_RASPBERRY = "BROWSE_RASPBERRY";
    public static final String RASPBERRY_HEADER = "RASPBERRY_HEADER";
    public static final String COMPLETE_FOLDER_DOWNLOAD = "complete_folder_download";
    public static final String RASPBERRY_FOLDER_DOWNLOAD = "raspberry_folder_download";
    public static final String OPEN_DOWNLOAD_LIST = "open_download_list";
    //languages
    public static String HINDI = "Hindi";
    static String ENGLISH = "English";
    static String MARATHI = "Marathi";
    static String KANNADA = "Kannada";
    static String TELUGU = "Telugu";
    static String BENGALI = "Bengali";
    static String GUJARATI = "Gujarati";
    static String PUNJABI = "Punjabi";
    static String TAMIL = "Tamil";
    static String ODIYA = "Odiya";
    static String MALAYALAM = "Malayalam";
    static String ASSAMESE = "Assamese";
    static String URDU = "Urdu";

    //wifi constants
    public static final int ApScanResult = 201;
    public static final int WiFiConnectSuccess = 202;
    public static final int ApCreateApSuccess = 203;
    public static final String WIFI_AP_PASSWORD = "pratham123";

    public static final String LANGUAGE = "language";
    public static final String GOOGLE_ID = "google_id";

    public enum URL {

        BROWSE_BY_ID(BASE_URL + "get?id="),
        SEARCH_BY_KEYWORD(BASE_URL + "GetSearchList?"),
        POST_GOOGLE_DATA(BASE_URL + "PostGoogleSignIn"),
        GET_TOP_LEVEL_NODE(BASE_URL + "GetTopLevelNode?lang="),
        DATASTORE_RASPBERY_URL(RASP_IP + "/pratham/datastore/"),
        DATASTORE_RASPBERY_PROGRAM_STATE_URL(RASP_IP + "/pratham/datastore/?table_name=ProgramState"),
        BROWSE_RASPBERRY_URL(RASP_IP + "/api/contentnode?parent="),
        //        GET_RASPBERRY_HEADER(RASP_IP + "/api/contentnode?content_id=f9da12749d995fa197f8b4c0192e7b2c"),
        GET_RASPBERRY_HEADER(RASP_IP + "/api/channel/"),
        POST_SMART_INTERNET_URL("http://www.rpi.prathamskills.org/api/pushdatasmartphone/post/"),
        POST_TAB_INTERNET_URL("http://www.rpi.prathamskills.org/api/pushdatapradigi/post/"),
        AAJ_KA_SAWAL_URL("http://rpi.prathamskills.org/aajkasawaal/"),
        PULL_PROGRAMS("http://swap.prathamcms.org/api/program"),
        DOWNLOAD_RESOURCE(BASE_URL + "DownloadResource?resid=");

        private final String name;

        URL(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        @NonNull
        public String toString() {
            return name;
        }

    }
}
