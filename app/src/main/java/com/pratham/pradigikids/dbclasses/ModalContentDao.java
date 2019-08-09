package com.pratham.pradigikids.dbclasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.pradigikids.models.Modal_ContentDetail;

import java.util.List;

@Dao
public interface ModalContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addContentList(List<Modal_ContentDetail> contentList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addContent(Modal_ContentDetail content);

    @Query("Delete from TableContent WHERE nodeid=:nodeId")
    void deleteContent(String nodeId);

    @Query("SELECT COUNT(*) from TableContent WHERE parentid=:nodeId and content_language=:language")
    int getChildCountOfParent(String nodeId, String language);

    @Query("SELECT * FROM TableContent WHERE (parentid ISNULL or parentid = 0 or parentid='' " +
            "or LTRIM(RTRIM([parentid])) = '') and content_language=:language")
    List<Modal_ContentDetail> getParentsHeaders(String language);

    @Query("SELECT * FROM TableContent WHERE parentid=:id and content_language=:language")
    List<Modal_ContentDetail> getChildsOfParent(String id, String language);

    @Query("SELECT * FROM TableContent WHERE nodeid=:id and content_language=:language")
    Modal_ContentDetail getContent(String id, String language);

    @Query("SELECT * FROM TableContent WHERE altnodeid=:id and content_language=:language")
    Modal_ContentDetail getContentFromAltNodeId(String id, String language);
}

