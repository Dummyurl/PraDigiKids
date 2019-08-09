package com.pratham.pradigikids.custom.progress_layout;

public interface ProgressLayoutListener {
    void onProgressCompleted();

    void onProgressChanged(int seconds);
}
