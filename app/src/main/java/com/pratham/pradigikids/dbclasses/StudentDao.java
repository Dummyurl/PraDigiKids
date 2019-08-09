package com.pratham.pradigikids.dbclasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pratham.pradigikids.models.Modal_Student;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllStudents(List<Modal_Student> studentsList);

    @Query("DELETE FROM Students")
    void deleteAllStudents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudent(Modal_Student studentsList);

    @Update
    int updateAllStudent(List<Modal_Student> studList);

    @Query("DELETE FROM Students WHERE Gender='Deleted'")
    void deleteDeletedStdRecords();

    @Query("DELETE FROM Students WHERE GroupID=:grpID")
    void deleteDeletedGrpsStdRecords(String grpID);

    @Query("SELECT * FROM Students")
    List<Modal_Student> getAllStudents();

    @Query("select * from Students where sentFlag = 0")
    List<Modal_Student> getAllNewStudents();

    @Query("update Students set sentFlag=1 where StudentId=:s_id")
    void updateSentStudentFlags(String s_id);

    @Query("select FullName from Students where StudentID = :studentID")
    String getStudentName(String studentID);

    @Query("SELECT * FROM Students WHERE GroupId=:gID")
    List<Modal_Student> getGroupwiseStudents(String gID);

    @Query("DELETE FROM Students Where StudentId=:stdID")
    void deleteStudentByID(String stdID);

 //   void deleteAllStudents();
}