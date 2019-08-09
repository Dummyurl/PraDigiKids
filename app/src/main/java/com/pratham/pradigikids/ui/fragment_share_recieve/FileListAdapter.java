package com.pratham.pradigikids.ui.fragment_share_recieve;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.File_Model;
import com.pratham.pradigikids.util.PD_Constant;

import java.io.File;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {
    private final ContractShare.shareView shareView;
    private final AsyncListDiffer<File_Model> mDiffer;

    public FileListAdapter(Context context, ContractShare.shareView shareView) {
        DiffUtil.ItemCallback<File_Model> diffcallback = new DiffUtil.ItemCallback<File_Model>() {
            @Override
            public boolean areItemsTheSame(@NonNull File_Model detail, @NonNull File_Model t1) {
                return Objects.equals(detail.getDetail().getNodeid(), t1.getDetail().getNodeid());
            }

            @Override
            public boolean areContentsTheSame(@NonNull File_Model detail, @NonNull File_Model t1) {
                int result = detail.compareTo(t1);
                return result == 0;
            }
        };
        mDiffer = new AsyncListDiffer<>(this, diffcallback);
        Context context1 = context;
        this.shareView = shareView;
    }

    public void submitList(List<File_Model> data) {
        mDiffer.submitList(data);
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater header = LayoutInflater.from(parent.getContext());
        View v = header.inflate(R.layout.share_file_item, parent, false);
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int i) {
        File_Model file_model = mDiffer.getCurrentList().get(holder.getAdapterPosition());
        holder.setFile_model(file_model, holder.getAdapterPosition());
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            File_Model file_model = (File_Model) payloads.get(0);
            holder.updateFile_model(file_model, holder.getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.share_content_image)
        SimpleDraweeView share_content_image;
        @BindView(R.id.share_title)
        TextView share_title;

        File_Model file_model;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setFile_model(File_Model file_model, int pos) {
            this.file_model = file_model;
            if (file_model.getDetail().isOnSDCard())
                share_content_image.setImageURI(Uri.fromFile(new File(PrathamApplication.contentSDPath
                        + "/PrathamImages/" + file_model.getDetail().getNodeimage())));
            else
                share_content_image.setImageURI(Uri.fromFile(new File(PrathamApplication.pradigiPath
                        + "/PrathamImages/" + file_model.getDetail().getNodeimage())));
            if (file_model.getDetail().getContentType().equalsIgnoreCase(PD_Constant.FOLDER)) {
                share_title.setText(file_model.getDetail().getNodetitle());
                share_content_image.setOnClickListener(v -> shareView.fileItemClicked(file_model.getDetail(), pos));
            } else {
                share_title.setText("Share");
                share_content_image.setOnClickListener(v -> shareView.sendItemChecked(file_model, pos));
            }
        }

        void updateFile_model(File_Model file_model, int pos) {
            this.file_model = file_model;
            if (file_model.getDetail().isOnSDCard())
                share_content_image.setImageURI(Uri.fromFile(new File(PrathamApplication.contentSDPath
                        + "/PrathamImages/" + file_model.getDetail().getNodeimage())));
            else
                share_content_image.setImageURI(Uri.fromFile(new File(PrathamApplication.pradigiPath +
                        "/PrathamImages/" + file_model.getDetail().getNodeimage())));
            if (file_model.getDetail().getContentType().equalsIgnoreCase(PD_Constant.FOLDER)) {
                share_title.setText(file_model.getDetail().getNodetitle());
                share_content_image.setOnClickListener(v -> shareView.fileItemClicked(file_model.getDetail(), pos));
            } else {
                share_content_image.setOnClickListener(v -> shareView.sendItemChecked(file_model, getAdapterPosition()));
            }
        }
    }
}
