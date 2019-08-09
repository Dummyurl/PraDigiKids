package com.pratham.pradigikids.ui.fragment_language;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.pratham.pradigikids.models.Modal_Language;

import java.util.ArrayList;

public class LanguageDiffCallback extends DiffUtil.Callback {
    private final ArrayList<Modal_Language> oldLanguageList;
    private final ArrayList<Modal_Language> newLanguageList;

    public LanguageDiffCallback(ArrayList<Modal_Language> oldLanguageList, ArrayList<Modal_Language> newLanguageList) {
        this.oldLanguageList = oldLanguageList;
        this.newLanguageList = newLanguageList;
    }

    @Override
    public int getOldListSize() {
        return oldLanguageList != null ? oldLanguageList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newLanguageList != null ? newLanguageList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newLanguageList.get(newItemPosition).compareTo(oldLanguageList.get(oldItemPosition));
        return result == 0;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return newLanguageList.get(newItemPosition);
    }
}
