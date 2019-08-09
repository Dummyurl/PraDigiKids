package com.pratham.pradigikids.view_holders;

import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.Modal_ContentDetail;
import com.pratham.pradigikids.ui.fragment_content.ContentContract;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    @BindView(R.id.rl_reveal)
    public RelativeLayout rl_reveal;
    @Nullable
    @BindView(R.id.file_content_image)
    SimpleDraweeView file_content_image;
    @Nullable
    @BindView(R.id.file_item_lottieview)
    LottieAnimationView file_item_lottieview;
    @Nullable
    @BindView(R.id.img_download_content)
    ImageView img_download_content;
    @Nullable
    @BindView(R.id.item_file_delete)
    ImageView item_file_delete;
    @Nullable
    @BindView(R.id.rl_download)
    RelativeLayout rl_download;
    @Nullable
    @BindView(R.id.file_content_desc)
    TextView file_content_desc;
    @Nullable
    @BindView(R.id.content_card_file)
    RelativeLayout content_card_file;
    @Nullable
    @BindView(R.id.rl_delete_reveal)
    LinearLayout rl_delete_reveal;
    @Nullable
    @BindView(R.id.file_del_yes)
    Button file_del_yes;
    @Nullable
    @BindView(R.id.file_del_no)
    Button file_del_no;
    private final ContentContract.contentClick contentClick;

    public FileViewHolder(View view, final ContentContract.contentClick contentClick) {
        super(view);
        ButterKnife.bind(this, view);
        this.contentClick = contentClick;
    }

    public void setContentItem(Modal_ContentDetail contentItem, int pos) {
        Objects.requireNonNull(file_content_desc).setText(contentItem.getNodetitle());
        rl_delete_reveal.setVisibility(View.GONE);
        if (contentItem.isDownloaded()) {
            item_file_delete.setVisibility(View.VISIBLE);
            rl_reveal.setVisibility(View.GONE);
            file_item_lottieview.setVisibility(View.VISIBLE);
            img_download_content.setVisibility(View.GONE);
            if (contentItem.getNodeserverimage() != null && !contentItem.getNodeserverimage().isEmpty()) {
                Uri imgUri;
                if (contentItem.isOnSDCard()) {
                    imgUri = Uri.fromFile(new File(
                            PrathamApplication.contentSDPath + "/PrathamImages/" + contentItem.getNodeimage()));
                    Objects.requireNonNull(file_content_image).setImageURI(imgUri);
                } else {
                    imgUri = Uri.fromFile(new File(
                            PrathamApplication.pradigiPath + "/PrathamImages/" + contentItem.getNodeimage()));
                    Objects.requireNonNull(file_content_image).setImageURI(imgUri);
                }
            }
            if (contentItem.getResourcetype().toLowerCase().equalsIgnoreCase(PD_Constant.GAME))
                Objects.requireNonNull(file_item_lottieview).setAnimation("gaming_pad.json");
            else if (contentItem.getResourcetype().toLowerCase().equalsIgnoreCase(PD_Constant.VIDEO))
                Objects.requireNonNull(file_item_lottieview).setAnimation("play_button.json");
            else if (contentItem.getResourcetype().toLowerCase().equalsIgnoreCase(PD_Constant.PDF))
                Objects.requireNonNull(file_item_lottieview).setAnimation("book.json");
            Objects.requireNonNull(content_card_file).setOnClickListener(v -> contentClick.openContent(pos, contentItem));
            Objects.requireNonNull(rl_download).setOnClickListener(v -> contentClick.openContent(pos, contentItem));
            Objects.requireNonNull(item_file_delete).setOnClickListener(v -> {
                rl_delete_reveal.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(() -> reveal(rl_delete_reveal, item_file_delete), 200);
            });
            Objects.requireNonNull(file_del_yes).setOnClickListener(v -> contentClick.deleteContent(pos, contentItem));
            Objects.requireNonNull(file_del_no).setOnClickListener(v -> unreveal(rl_delete_reveal, item_file_delete));
        } else {
            rl_delete_reveal.setVisibility(View.GONE);
            item_file_delete.setVisibility(View.GONE);
            file_item_lottieview.setVisibility(View.GONE);
            img_download_content.setVisibility(View.VISIBLE);
            ImageRequest request = null;
            if (contentItem.getKolibriNodeImageUrl() != null && !contentItem.getKolibriNodeImageUrl().isEmpty()) {
                request = ImageRequestBuilder
                        .newBuilderWithSource(Uri.parse(contentItem.getKolibriNodeImageUrl()))
                        .setResizeOptions(new ResizeOptions(300, 200))
                        .setLocalThumbnailPreviewsEnabled(true).build();
            } else if (contentItem.getNodeserverimage() != null && !contentItem.getNodeserverimage().isEmpty()) {
                request = ImageRequestBuilder
                        .newBuilderWithSource(Uri.parse(contentItem.getNodeserverimage()))
                        .setResizeOptions(new ResizeOptions(300, 200))
                        .setLocalThumbnailPreviewsEnabled(true).build();
            }
            if (request != null) {
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(Objects.requireNonNull(file_content_image).getController())
                        .build();
                file_content_image.setController(controller);
            }
            Drawable background = rl_download.getBackground();
            if (background instanceof GradientDrawable) {
                int color = PD_Utility.getRandomColorGradient();
                ((GradientDrawable) background).setColor(color);
                rl_reveal.setBackgroundColor(color);
            }
            rl_download.setOnClickListener(v -> {
                rl_reveal.setVisibility(View.INVISIBLE);
                contentClick.onDownloadClicked(pos, contentItem, rl_reveal, rl_download);
            });
            Objects.requireNonNull(content_card_file).setOnClickListener(null);
        }
        if (Objects.requireNonNull(rl_reveal).getVisibility() == View.VISIBLE)
            unreveal(rl_reveal, rl_download);
        if (Objects.requireNonNull(rl_delete_reveal).getVisibility() == View.VISIBLE)
            unreveal(rl_delete_reveal, item_file_delete);
    }

    private void unreveal(View view, View end) {
        // previously visible view
        try {
            int centerX = view.getWidth();
            int centerY = view.getHeight();
            int startRadius = 0;
            int endRadius = Math.max(centerX, centerY);
            Animator anim = ViewAnimationUtils.createCircularReveal(view, (int) end.getX(), (int) end.getY(), endRadius, startRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(300);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            anim.start();
        } catch (Exception e) {
            e.printStackTrace();
            view.setVisibility(View.GONE);
        }
    }

    public void reveal(View view, View startView) {
        // previously invisible view
        try {
            int centerX = view.getWidth();
            int centerY = view.getHeight();
            int startRadius = 0;
            int endRadius = (int) Math.hypot(centerX, centerY);
            Animator anim = ViewAnimationUtils.createCircularReveal(view, (int) startView.getX(), (int) startView.getY(), startRadius, endRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(300);
            view.setVisibility(View.VISIBLE);
            anim.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}