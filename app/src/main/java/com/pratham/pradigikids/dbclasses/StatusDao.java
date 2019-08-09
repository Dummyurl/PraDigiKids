package com.pratham.pradigikids.dbclasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pratham.pradigikids.models.Modal_Status;

import java.util.List;

@Dao
public interface StatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Modal_Status status);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Modal_Status> statuses);

    @Update
    int update(Modal_Status status);

    @Delete
    void delete(Modal_Status status);

    @Delete
    void deleteAll(Modal_Status... statuses);

    @Query("select * from Status")
    List<Modal_Status> getAllStatuses();

    @Query("Select statusKey from Status where statusKey = :key")
    String getKey(String key);

    @Query("Select value from Status where statusKey = :key")
    String getValue(String key);

    @Query("UPDATE Status set value =:value where statusKey =:key")
    void updateValue(String key, String value);

}