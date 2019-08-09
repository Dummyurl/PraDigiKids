package com.pratham.pradigikids.util;

public class APIs {

    public static final String village = "village";
    public static final String CRL = "KOLIBRI_CRL";
    public static final String Group = "Groups";
    public static final String Student = "Student";

    public static final String KOLIBRI_STATE = ",state:";
    public static final String SERVER_STATE = "&state=";
    public static final String KOLIBRI_VILLAGE = ",villageid:";
    public static final String SERVER_VILLAGE = "&villageId=";
    public static final String SERVER_STATECODE = "&statecode=";
    public static final String pullVillagesKolibriURL = "http://192.168.4.1:8080/pratham/datastore/?table_name=village&filter_name=programid:";
    public static final String pullVillagesServerURL = "http://www.hlearning.openiscool.org/api/village/get?programId=";
    public static final String pullGroupsKolibriURL = "http://192.168.4.1:8080/pratham/datastore/?table_name=group&filter_name=programid:";
    public static final String pullGroupsServerURL = "http://www.devtab.openiscool.org/api/Group?programid=";
    public static final String pullStudentsKolibriURL = "http://192.168.4.1:8080/pratham/datastore/?table_name=student&filter_name=programid:";
    public static final String pullStudentsServerURL = "http://www.devtab.openiscool.org/api/student?programid=";
    public static final String pullCrlsKolibriURL = "http://192.168.4.1:8080/pratham/datastore/?table_name=Crl&filter_name=programid:";
    public static final String pullCrlsServerURL = "http://www.swap.prathamcms.org/api/UserList?programId=";
}
