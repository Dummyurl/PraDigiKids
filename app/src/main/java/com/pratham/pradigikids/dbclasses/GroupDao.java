package com.pratham.pradigikids.dbclasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.pradigikids.models.Modal_Groups;

import java.util.List;

@Dao
public interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllGroups(List<Modal_Groups> groupsList);

    @Query("DELETE FROM Groups")
    void deleteAllGroups();

    @Query("SELECT * FROM Groups ")
    List<Modal_Groups> getAllGroups();

    @Query("SELECT * FROM Groups WHERE VillageID=:vID ORDER BY GroupName ASC")
    List<Modal_Groups> GetGroups(int vID);

    @Query("DELETE FROM Groups WHERE GroupID=:grpID")
    void deleteGroupByGrpID(String grpID);

    @Query("SELECT * FROM Groups WHERE GroupID=:grpID")
    Modal_Groups getGroupByGrpID(String grpID);

    @Query("select * from Groups WHERE DeviceID = 'deleted'")
    List<Modal_Groups> GetAllDeletedGroups();

}