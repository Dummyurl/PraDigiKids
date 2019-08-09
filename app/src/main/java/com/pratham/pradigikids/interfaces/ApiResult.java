package com.pratham.pradigikids.interfaces;

import com.pratham.pradigikids.models.Modal_ContentDetail;

import java.util.ArrayList;

public interface ApiResult {
    void recievedContent(String header, String response, ArrayList<Modal_ContentDetail> contentList);

    void recievedError(String header, ArrayList<Modal_ContentDetail> contentList);
}
