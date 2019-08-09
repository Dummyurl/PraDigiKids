package com.pratham.pradigikids.dbclasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.pradigikids.models.Modal_Score;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Modal_Score score);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Modal_Score> score);

    @Query("UPDATE Score SET sentFlag = 1 where SessionID = :s_id")
    int updateFlag(String s_id);

    @Delete
    void delete(Modal_Score score);

    @Delete
    void deleteAll(Modal_Score... scores);

    @Query("select * from Score where sentFlag = 0 AND SessionID=:s_id")
    List<Modal_Score> getAllNewScores(String s_id);

    @Query("select * from Score where sentFlag = 0")
    List<Modal_Score> getAllNewScores();

    @Query("DELETE FROM Score")
    void deleteAllScores();
}
