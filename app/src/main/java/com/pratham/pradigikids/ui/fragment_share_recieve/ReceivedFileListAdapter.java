package com.pratham.pradigikids.ui.fragment_share_recieve;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.Modal_ReceivingFilesThroughFTP;
import com.pratham.pradigikids.util.PD_Constant;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceivedFileListAdapter extends RecyclerView.Adapter<ReceivedFileListAdapter.FileViewHolder> {
    private final Context context;
    private final AsyncListDiffer<Modal_ReceivingFilesThroughFTP> mDiffer;

    public ReceivedFileListAdapter(Context context) {
        DiffUtil.ItemCallback<Modal_ReceivingFilesThroughFTP> diffcallback = new DiffUtil.ItemCallback<Modal_ReceivingFilesThroughFTP>() {
            @Override
            public boolean areItemsTheSame(@NonNull Modal_ReceivingFilesThroughFTP detail, @NonNull Modal_ReceivingFilesThroughFTP t1) {
                return Objects.equals(detail.getGameName(), t1.getGameName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Modal_ReceivingFilesThroughFTP detail, @NonNull Modal_ReceivingFilesThroughFTP t1) {
                int result = detail.compareTo(t1);
                return result == 0;
            }
        };
        mDiffer = new AsyncListDiffer<>(this, diffcallback);
        this.context = context;
    }

    public void submitList(List<Modal_ReceivingFilesThroughFTP> data) {
        mDiffer.submitList(data);
    }

    public List<Modal_ReceivingFilesThroughFTP> getList() {
        return mDiffer.getCurrentList();
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater header = LayoutInflater.from(parent.getContext());
        View v = header.inflate(R.layout.receiving_row_file_item, parent, false);
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int i) {
        Modal_ReceivingFilesThroughFTP files = mDiffer.getCurrentList().get(holder.getAdapterPosition());
        holder.setFiles(files);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Modal_ReceivingFilesThroughFTP file = (Modal_ReceivingFilesThroughFTP) payloads.get(0);
            holder.updateFileItem(file);
        }
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.receive_content_parts)
        TextView receive_content_parts;
        @BindView(R.id.receive_content_title)
        TextView receive_content_title;
        @BindView(R.id.receive_lottie_view)
        LottieAnimationView receive_lottie_view;
        @BindView(R.id.img_recieve_file)
        ImageView img_recieve_file;
        Modal_ReceivingFilesThroughFTP files;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setFiles(Modal_ReceivingFilesThroughFTP files) {
            this.files = files;
            switch (files.getGameType()) {
                case PD_Constant.GAME:
                    img_recieve_file.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_joystick));
//                    receive_lottie_view.setAnimation("gaming_pad.json");
                    break;
                case PD_Constant.VIDEO:
                    img_recieve_file.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video));
//                    receive_lottie_view.setAnimation("play_button.json");
                    break;
                case PD_Constant.PDF:
                    img_recieve_file.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_book));
//                    receive_lottie_view.setAnimation("book.json");
                    break;
                case PD_Constant.APK:
                    img_recieve_file.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_joystick));
//                    receive_lottie_view.setAnimation("gaming_pad.json");
                    break;
            }
            receive_content_title.setText(files.getGameName());
            receive_content_parts.setText(files.getGamePart());
            if (files.isReceived()) {
                receive_lottie_view.setVisibility(View.VISIBLE);
                receive_lottie_view.playAnimation();
            } else
                receive_lottie_view.setVisibility(View.GONE);
        }

        void updateFileItem(Modal_ReceivingFilesThroughFTP files) {
            receive_content_parts.setText(files.getGamePart());
            if (files.isReceived()) {
                receive_lottie_view.setVisibility(View.VISIBLE);
                receive_lottie_view.playAnimation();
            } else
                receive_lottie_view.setVisibility(View.GONE);
        }
    }
}
