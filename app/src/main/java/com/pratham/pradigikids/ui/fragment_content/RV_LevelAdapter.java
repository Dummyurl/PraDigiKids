package com.pratham.pradigikids.ui.fragment_content;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.Modal_ContentDetail;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 01-08-2017.
 */

public class RV_LevelAdapter extends RecyclerView.Adapter {

    private static final int LAST_ITEM = 1;
    private static final int NORMAL_ITEM = 2;
    private final LevelContract levelContract;
    private final AsyncListDiffer<Modal_ContentDetail> mDiffer;

    public RV_LevelAdapter(Context context, LevelContract levelContract) {
        DiffUtil.ItemCallback<Modal_ContentDetail> diffcallback = new DiffUtil.ItemCallback<Modal_ContentDetail>() {
            @Override
            public boolean areItemsTheSame(@NonNull Modal_ContentDetail detail, @NonNull Modal_ContentDetail t1) {
                return Objects.equals(detail.getNodeid(), t1.getNodeid());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Modal_ContentDetail detail, @NonNull Modal_ContentDetail t1) {
                int result = detail.compareTo(t1);
                return result == 0;
            }
        };
        mDiffer = new AsyncListDiffer<>(this, diffcallback);
        Context context1 = context;
        this.levelContract = levelContract;
    }

    public void submitList(List<Modal_ContentDetail> data) {
        mDiffer.submitList(data);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == (mDiffer.getCurrentList().size() - 1))
            return LAST_ITEM;
        else return NORMAL_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView;
        switch (viewType) {
            case LAST_ITEM:
                LayoutInflater last = LayoutInflater.from(viewGroup.getContext());
                itemView = last.inflate(R.layout.item_level_last_state, viewGroup, false);
                return new LastItemViewHolder(itemView);
            case NORMAL_ITEM:
                LayoutInflater normal = LayoutInflater.from(viewGroup.getContext());
                itemView = normal.inflate(R.layout.item_level_normal_state, viewGroup, false);
                return new NormalItemViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//        Modal_ContentDetail detail = levels.get(viewHolder.getAdapterPosition());
        Modal_ContentDetail detail = mDiffer.getCurrentList().get(viewHolder.getAdapterPosition());
        switch (viewHolder.getItemViewType()) {
            case NORMAL_ITEM:
                NormalItemViewHolder holder = (NormalItemViewHolder) viewHolder;
                holder.l_name.setText(detail.getNodetitle());
                break;
            case LAST_ITEM:
                LastItemViewHolder last = (LastItemViewHolder) viewHolder;
                last.last_level_name.setText(detail.getNodetitle());
                break;
        }
        viewHolder.itemView.setOnClickListener(v -> levelContract.levelClicked(detail));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Modal_ContentDetail contentDetail = (Modal_ContentDetail) payloads.get(0);
            switch (holder.getItemViewType()) {
                case NORMAL_ITEM:
                    NormalItemViewHolder normalItemViewHolder = (NormalItemViewHolder) holder;
                    normalItemViewHolder.l_name.setText(contentDetail.getNodetitle());
                    break;
                case LAST_ITEM:
                    LastItemViewHolder last = (LastItemViewHolder) holder;
                    last.last_level_name.setText(contentDetail.getNodetitle());
                    break;
            }
            holder.itemView.setOnClickListener(v -> levelContract.levelClicked(contentDetail));
        }
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public class NormalItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.level_name)
        TextView l_name;

        NormalItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class LastItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.last_level_name)
        TextView last_level_name;

        LastItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
