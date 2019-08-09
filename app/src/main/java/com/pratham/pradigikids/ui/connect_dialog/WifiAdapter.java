package com.pratham.pradigikids.ui.connect_dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratham.pradigikids.R;
import com.pratham.pradigikids.util.PD_Constant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    private final ArrayList<String> datalist;
    private final Context context;
    private final ConnectInterface connectInterface;

    public WifiAdapter(Context context, ArrayList<String> datalist, ConnectInterface connectInterface) {
        this.datalist = datalist;
        this.context = context;
        this.connectInterface = connectInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.connect_dialog_item, parent, false);
        return new WifiAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        if (datalist.get(viewHolder.getAdapterPosition()).equalsIgnoreCase(PD_Constant.PRATHAM_KOLIBRI_HOTSPOT))
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_green));
        else
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
        viewHolder.txt_wifi_name.setText(datalist.get(viewHolder.getAdapterPosition()));
        viewHolder.itemView.setOnClickListener(v -> connectInterface.wifiClicked(datalist.get(viewHolder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_wifi_name)
        TextView txt_wifi_name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
