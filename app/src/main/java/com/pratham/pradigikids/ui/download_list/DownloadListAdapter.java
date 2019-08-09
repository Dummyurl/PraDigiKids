package com.pratham.pradigikids.ui.download_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.number_progressbar.NumberProgressBar;
import com.pratham.pradigikids.models.Modal_FileDownloading;
import com.pratham.pradigikids.util.PD_Constant;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.DownloadViewHolder> {
    private final DowloadContract dowloadContract;
    private final AsyncListDiffer<Modal_FileDownloading> mDiffer;

    public DownloadListAdapter(Context context, DowloadContract dowloadContract) {
        DiffUtil.ItemCallback<Modal_FileDownloading> diffcallback = new DiffUtil.ItemCallback<Modal_FileDownloading>() {
            @Override
            public boolean areItemsTheSame(@NonNull Modal_FileDownloading detail, @NonNull Modal_FileDownloading t1) {
                return Objects.equals(detail.getDownloadId(), t1.getDownloadId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Modal_FileDownloading detail, @NonNull Modal_FileDownloading t1) {
                int result = detail.compareTo(t1);
                return result == 0;
            }
        };
        mDiffer = new AsyncListDiffer<>(this, diffcallback);
        Context context1 = context;
        this.dowloadContract = dowloadContract;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater header = LayoutInflater.from(parent.getContext());
        View v = header.inflate(R.layout.download_list_item, parent, false);
        return new DownloadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int i) {
        if (mDiffer.getCurrentList().get(holder.getAdapterPosition()).getContentDetail().getResourcetype().toLowerCase()
                .equalsIgnoreCase(PD_Constant.GAME))
            Objects.requireNonNull(holder.download_file_view).setImageResource(R.drawable.ic_joystick);
        else if (mDiffer.getCurrentList().get(holder.getAdapterPosition()).getContentDetail().getResourcetype().toLowerCase()
                .equalsIgnoreCase(PD_Constant.VIDEO))
            Objects.requireNonNull(holder.download_file_view).setImageResource(R.drawable.ic_video);
        else if (mDiffer.getCurrentList().get(holder.getAdapterPosition()).getContentDetail().getResourcetype().toLowerCase()
                .equalsIgnoreCase(PD_Constant.PDF))
            Objects.requireNonNull(holder.download_file_view).setImageResource(R.drawable.ic_book);
        else
            Objects.requireNonNull(holder.download_file_view).setImageResource(R.drawable.ic_joystick);
        Objects.requireNonNull(holder.download_remaining_time).setText(mDiffer.getCurrentList().get(holder.getAdapterPosition()).getRemaining_time());
        Objects.requireNonNull(holder.content_title).setText(mDiffer.getCurrentList().get(holder.getAdapterPosition()).getFilename());
        Objects.requireNonNull(holder.number_progress).setProgress(mDiffer.getCurrentList().get(holder.getAdapterPosition()).getProgress());
        Objects.requireNonNull(holder.download_delete).setOnClickListener(v -> dowloadContract.deleteDownload(holder.getAdapterPosition(),
                mDiffer.getCurrentList().get(holder.getAdapterPosition()).getDownloadId()));
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public void submitList(List<Modal_FileDownloading> data) {
        mDiffer.submitList(data);
    }

    public List<Modal_FileDownloading> getModelList() {
        return mDiffer.getCurrentList();
    }

    class DownloadViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.number_progress)
        NumberProgressBar number_progress;
        @Nullable
        @BindView(R.id.download_content_title)
        TextView content_title;
        @Nullable
        @BindView(R.id.download_remaining_time)
        TextView download_remaining_time;
        @Nullable
        @BindView(R.id.download_file_view)
        ImageView download_file_view;
        @Nullable
        @BindView(R.id.download_delete)
        ImageView download_delete;

        DownloadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
