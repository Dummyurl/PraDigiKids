package com.pratham.pradigikids.ui.fragment_child_attendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.custom.shapes.ShapeOfView;
import com.pratham.pradigikids.models.Modal_Student;
import com.pratham.pradigikids.util.PD_Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildAdapter extends RecyclerView.Adapter {
    private static final int ADD_CHILD = 1;
    private static final int TAB_CHILD = 2;
    private static final int SMART_CHILD = 3;
    private final ArrayList<Modal_Student> datalist;
    private final Context context;
    private final ContractChildAttendance.attendanceView attendanceView;

    public ChildAdapter(Context context, ArrayList<Modal_Student> datalist,
                        ContractChildAttendance.attendanceView attendanceView) {
        this.context = context;
        this.datalist = datalist;
        this.attendanceView = attendanceView;
    }

    @Override
    public int getItemViewType(int position) {
        if (datalist.get(position).getStudentId().equalsIgnoreCase("Add Child"))
            return ADD_CHILD;
        else if (PrathamApplication.isTablet)
            return TAB_CHILD;
        else
            return SMART_CHILD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case ADD_CHILD:
                LayoutInflater header = LayoutInflater.from(parent.getContext());
                v = header.inflate(R.layout.item_add_child, parent, false);
                return new AddChildHolder(v);
            case TAB_CHILD:
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                v = inflater.inflate(R.layout.item_child_attendance, parent, false);
                return new ChildHolder(v);
            case SMART_CHILD:
                LayoutInflater inflater2 = LayoutInflater.from(parent.getContext());
                v = inflater2.inflate(R.layout.item_smart_child_attendance, parent, false);
                return new SmartChildHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        position = viewHolder.getAdapterPosition();
        switch (viewHolder.getItemViewType()) {
            case ADD_CHILD:
                AddChildHolder addChildHolder = (AddChildHolder) viewHolder;
                addChildHolder.itemView.setOnClickListener(v -> attendanceView.addChild(addChildHolder.itemView));
                break;
            case TAB_CHILD:
                ChildHolder childHolder = (ChildHolder) viewHolder;
                childHolder.child_name.setText(datalist.get(position).getFullName());
                int drawable = PD_Utility.getRandomAvatar(datalist.get(position).getGender());
                childHolder.child_shape.setDrawable(drawable);
                childHolder.child_avatar.setImageResource(drawable);
                if (datalist.get(position).isChecked())
                    childHolder.child_card_name.setCardBackgroundColor(context.getResources().getColor(R.color.mustord_yellow));
                else
                    childHolder.child_card_name.setCardBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                int finalPos = position;
                childHolder.itemView.setOnClickListener(v ->
                        attendanceView.childItemClicked(datalist.get(finalPos), finalPos));
                break;
            case SMART_CHILD:
                SmartChildHolder smartChildHolder = (SmartChildHolder) viewHolder;
                smartChildHolder.smart_child_name.setText(datalist.get(position).getFullName());
                smartChildHolder.smart_child_avatar.setAnimation(datalist.get(position).getAvatarName());
                int finalPos2 = position;
                smartChildHolder.smart_child_avatar.setOnClickListener(v ->
                        attendanceView.moveToDashboardOnChildClick(datalist.get(finalPos2), finalPos2, smartChildHolder.itemView));
                break;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            switch (holder.getItemViewType()) {
                case ADD_CHILD:
                    AddChildHolder addChildHolder = (AddChildHolder) holder;
                    addChildHolder.itemView.setOnClickListener(v -> attendanceView.addChild(addChildHolder.itemView));
                    break;
                case TAB_CHILD:
                    ChildHolder childHolder = (ChildHolder) holder;
                    Modal_Student student = (Modal_Student) payloads.get(0);
                    if (student.isChecked())
                        childHolder.child_card_name.setCardBackgroundColor(context.getResources().getColor(R.color.mustord_yellow));
                    else
                        childHolder.child_card_name.setCardBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            }
        }
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class ChildHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.child_card_name)
        MaterialCardView child_card_name;
        @BindView(R.id.child_shape)
        ShapeOfView child_shape;
        @BindView(R.id.child_name)
        TextView child_name;
        @BindView(R.id.child_avatar)
        ImageView child_avatar;

        ChildHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SmartChildHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.child_avatar)
        LottieAnimationView smart_child_avatar;
        @BindView(R.id.smart_child_name)
        TextView smart_child_name;

        SmartChildHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class AddChildHolder extends RecyclerView.ViewHolder {
        AddChildHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
