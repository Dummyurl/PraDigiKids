package com.pratham.pradigikids.ui.fragment_language;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.Modal_Language;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private final ArrayList<Modal_Language> datalist;
    private final ContractLanguage contractLanguage;

    public LanguageAdapter(Context context, ArrayList<Modal_Language> datalist, ContractLanguage contractLanguage) {
        this.datalist = datalist;
        Context context1 = context;
        this.contractLanguage = contractLanguage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_language, parent, false);
        return new LanguageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        pos = viewHolder.getAdapterPosition();
        viewHolder.tv_language.setText(datalist.get(pos).getLanguage());
        if (datalist.get(pos).isIsselected())
            viewHolder.tv_language.setSelected(true);
        else
            viewHolder.tv_language.setSelected(false);
        viewHolder.tv_language.setOnClickListener(v -> contractLanguage.languageSelected(viewHolder.getAdapterPosition()));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Modal_Language language = (Modal_Language) payloads.get(0);
            holder.tv_language.setText(language.getLanguage());
            if (language.isIsselected())
                holder.tv_language.setSelected(true);
            else
                holder.tv_language.setSelected(false);
        }
    }

    public void updateLanguageItems(final ArrayList<Modal_Language> languages) {
        final LanguageDiffCallback diffCallback = new LanguageDiffCallback(this.datalist, languages);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.datalist.clear();
        this.datalist.addAll(languages);
//        this.datalist = languages;
        diffResult.dispatchUpdatesTo(this);
    }

    public Modal_Language getitem(int pos) {
        return datalist.get(pos);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_language)
        TextView tv_language;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
