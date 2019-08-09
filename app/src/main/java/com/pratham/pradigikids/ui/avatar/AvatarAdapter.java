package com.pratham.pradigikids.ui.avatar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.pratham.pradigikids.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {

    private final ArrayList<String> datalist;

    public AvatarAdapter(Context context, ArrayList<String> datalist) {
        Context context1 = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public AvatarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_avatar, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarAdapter.ViewHolder viewHolder, int i) {
        viewHolder.lottie_avatar.setAnimation(datalist.get(i));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lottie_avatar)
        LottieAnimationView lottie_avatar;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
