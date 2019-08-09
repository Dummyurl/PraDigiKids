package com.pratham.pradigikids.ui.pdf_viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pratham.pradigikids.R;

import java.util.ArrayList;

public class PDFPagerAdapter extends PagerAdapter {
    private final ArrayList<Bitmap> bitmaps;
    private final LayoutInflater mLayoutInflater;

    public PDFPagerAdapter(Context context, ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        Context context1 = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pdf_pager_item, container, false);
        ImageView imageView = itemView.findViewById(R.id.pdf_page);
        imageView.setImageBitmap(bitmaps.get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
