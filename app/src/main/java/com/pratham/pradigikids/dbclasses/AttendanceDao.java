package com.pratham.pradigikids.dbclasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.pradigikids.models.Attendance;

import java.util.List;

@Dao
public interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttendance(List<Attendance> attendancesList);

    @Query("DELETE FROM Attendance")
    void deleteAllAttendances();

    @Query("SELECT * FROM Attendance")
    List<Attendance> getAllAttendances();

    @Query("UPDATE Attendance SET sentFlag=1 WHERE SessionID=:s_id")
    void updateSentFlag(String s_id);

    @Query("SELECT * FROM Attendance WHERE sentFlag=0 AND SessionID=:s_id")
    List<Attendance> getNewAttendances(String s_id);

    @Query("SELECT * FROM Attendance WHERE sentFlag=0")
    List<Attendance> getNewAttendances();

    @Query("UPDATE Attendance SET sentFlag=:pushStatus")
    void updateAllSentFlag(int pushStatus);

    @Query("SELECT DISTINCT SessionID FROM Attendance")
    List<String> getAllDistinctSessions();

    @Query("select GroupID from Attendance where SessionID=:SessID")
    String GetGrpIDBySessionID(String SessID);

    @Query("select Present from Attendance where SessionID=:SessID")
    List<Integer> GetAllPresentStdBySessionId(String SessID);

}
