package com.pratham.pradigikids.async;

import android.os.AsyncTask;

import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.interfaces.DownloadedContents;
import com.pratham.pradigikids.util.PD_Constant;

import static com.pratham.pradigikids.PrathamApplication.modalContentDao;

public class GetDownloadedContent extends AsyncTask {
    private final String parentId;
    private final DownloadedContents downloadedContents;

    public GetDownloadedContent(DownloadedContents downloadedContents, String parentId) {
        this.parentId = parentId;
        this.downloadedContents = downloadedContents;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String lang = FastSave.getInstance().getString(PD_Constant.LANGUAGE, PD_Constant.HINDI);
        if (parentId != null && !parentId.equalsIgnoreCase("0") && !parentId.isEmpty())
            return modalContentDao.getChildsOfParent(parentId, lang);
        else
            return modalContentDao.getParentsHeaders(lang);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (downloadedContents != null)
            downloadedContents.downloadedContents(o, parentId);
    }
}
