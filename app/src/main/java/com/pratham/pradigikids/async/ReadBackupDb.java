package com.pratham.pradigikids.async;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;

import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.dbclasses.PrathamDatabase;
import com.pratham.pradigikids.models.Attendance;
import com.pratham.pradigikids.models.Modal_Score;
import com.pratham.pradigikids.models.Modal_Session;
import com.pratham.pradigikids.models.Modal_Status;
import com.pratham.pradigikids.util.PD_Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pratham.pradigikids.PrathamApplication.attendanceDao;
import static com.pratham.pradigikids.PrathamApplication.scoreDao;
import static com.pratham.pradigikids.PrathamApplication.sessionDao;
import static com.pratham.pradigikids.PrathamApplication.statusDao;

public class ReadBackupDb extends AsyncTask<String, String, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            File db_file = new File(Environment.getExternalStorageDirectory() + "/" + PrathamDatabase.DB_NAME);
            if (db_file.exists()) {
                SQLiteDatabase db = SQLiteDatabase.openDatabase(db_file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
                if (db == null) return false;
                try {
                    Cursor att_cursor = db.rawQuery("SELECT * FROM Attendance where sentFlag = 0", null);
                    List<Attendance> attendances = new ArrayList<>();
                    if (att_cursor.moveToFirst()) {
                        while (!att_cursor.isAfterLast()) {
                            Attendance att = new Attendance();
                            att.setVillageID(att_cursor.getString(att_cursor.getColumnIndex("VillageID")));
                            att.setGroupID(att_cursor.getString(att_cursor.getColumnIndex("GroupID")));
                            att.setSessionID(att_cursor.getString(att_cursor.getColumnIndex("SessionID")));
                            att.setStudentID(att_cursor.getString(att_cursor.getColumnIndex("StudentID")));
                            att.setDate(att_cursor.getString(att_cursor.getColumnIndex("Date")));
                            att.setPresent(att_cursor.getInt(att_cursor.getColumnIndex("Present")));
                            att.setSentFlag(0);
                            attendances.add(att);
                            att_cursor.moveToNext();
                        }
                    }
                    attendanceDao.insertAttendance(attendances);
                    att_cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Cursor score_cursor = db.rawQuery("SELECT * FROM Score where sentFlag = 0", null);
                    List<Modal_Score> scores = new ArrayList<>();
                    if (score_cursor.moveToFirst()) {
                        while (!score_cursor.isAfterLast()) {
                            Modal_Score modal_score = new Modal_Score();
                            modal_score.setSessionID(score_cursor.getString(score_cursor.getColumnIndex("SessionID")));
                            modal_score.setStudentID(score_cursor.getString(score_cursor.getColumnIndex("StudentID")));
                            modal_score.setGroupID(score_cursor.getString(score_cursor.getColumnIndex("GroupID")));
                            modal_score.setDeviceID(score_cursor.getString(score_cursor.getColumnIndex("DeviceID")));
                            modal_score.setResourceID(score_cursor.getString(score_cursor.getColumnIndex("ResourceID")));
                            modal_score.setQuestionId(score_cursor.getInt(score_cursor.getColumnIndex("QuestionId")));
                            modal_score.setScoredMarks(score_cursor.getInt(score_cursor.getColumnIndex("ScoredMarks")));
                            modal_score.setTotalMarks(score_cursor.getInt(score_cursor.getColumnIndex("TotalMarks")));
                            modal_score.setStartDateTime(score_cursor.getString(score_cursor.getColumnIndex("StartDateTime")));
                            modal_score.setEndDateTime(score_cursor.getString(score_cursor.getColumnIndex("EndDateTime")));
                            modal_score.setLevel(score_cursor.getInt(score_cursor.getColumnIndex("Level")));
                            modal_score.setLabel(score_cursor.getString(score_cursor.getColumnIndex("Label")));
                            modal_score.setSentFlag(0);
                            scores.add(modal_score);
                            score_cursor.moveToNext();
                        }
                    }
                    scoreDao.insertAll(scores);
                    score_cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Cursor session_cursor = db.rawQuery("SELECT * FROM Session where sentFlag = 0", null);
                    List<Modal_Session> sessions = new ArrayList<>();
                    if (session_cursor.moveToFirst()) {
                        while (!session_cursor.isAfterLast()) {
                            Modal_Session session = new Modal_Session();
                            session.setSessionID(session_cursor.getString(session_cursor.getColumnIndex("SessionID")));
                            session.setFromDate(session_cursor.getString(session_cursor.getColumnIndex("fromDate")));
                            session.setToDate(session_cursor.getString(session_cursor.getColumnIndex("toDate")));
                            session.setSentFlag(0);
                            sessions.add(session);
                            session_cursor.moveToNext();
                        }
                    }
                    sessionDao.insertAll(sessions);
                    session_cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Cursor status_cursor = db.rawQuery("SELECT * FROM Status", null);
                    List<Modal_Status> stat = new ArrayList<>();
                    if (status_cursor.moveToFirst()) {
                        while (!status_cursor.isAfterLast()) {
                            Modal_Status modal_status = new Modal_Status();
                            modal_status.setStatusKey(status_cursor.getString(status_cursor.getColumnIndex("statusKey")));
                            modal_status.setValue(status_cursor.getString(status_cursor.getColumnIndex("value")));
                            modal_status.setDescription(status_cursor.getString(status_cursor.getColumnIndex("description")));
                            stat.add(modal_status);
                            status_cursor.moveToNext();
                        }
                    }
                    statusDao.insertAll(stat);
                    status_cursor.close();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        FastSave.getInstance().saveBoolean(PD_Constant.BACKUP_DB_COPIED, result);
    }
}
