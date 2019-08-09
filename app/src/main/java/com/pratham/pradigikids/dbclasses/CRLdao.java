package com.pratham.pradigikids.dbclasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.pradigikids.models.Modal_Crl;

import java.util.List;

@Dao
public interface CRLdao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCRL(List<Modal_Crl> crlList);

    @Query("DELETE FROM CRL")
    void deleteAllCRLs();

    @Query("SELECT * FROM CRL WHERE UserName=:user AND Password=:pass")
    Modal_Crl checkUserValidation(String user, String pass);

    @Query("SELECT * FROM CRL")
    List<Modal_Crl> getAllCRLs();

   /*  @Query("SELECT count(*) FROM KOLIBRI_CRL")
    public int getCRLsCount();

    @Query("SELECT RoleId FROM KOLIBRI_CRL where CRLId=:id")
    public String getCRLsRoleById(String id);

    @Query("SELECT DISTINCT ProgramName FROM KOLIBRI_CRL")
    public List<String> getDistinctCRLsdProgram();

    @Query("SELECT DISTINCT  RoleName FROM KOLIBRI_CRL")
    public List<String> getDistinctCRLsRoleId();

    @Query("SELECT DISTINCT UserName,CRLId,FirstName FROM KOLIBRI_CRL WHERE RoleName=:roleName and ProgramName=:programName")
    public List<Modal_Crl> getDistinctCRLsUserName(String roleName, String programName);*/
}