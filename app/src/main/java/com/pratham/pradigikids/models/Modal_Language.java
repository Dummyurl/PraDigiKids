package com.pratham.pradigikids.models;

import android.support.annotation.NonNull;

public class Modal_Language implements Comparable {
    private String language;
    private String main_language;
    private boolean isselected;

    public String getMain_language() {
        return main_language;
    }

    public void setMain_language(String main_language) {
        this.main_language = main_language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Modal_Language compare = (Modal_Language) o;
        if (compare.isIsselected() == this.isIsselected())
            return 0;
        else return 1;
    }
}
