package com.example.pranav.sharkfeed;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.pranav.sharkfeed.R;
import com.example.pranav.sharkfeed.SharkPhotoActivity;
import com.example.pranav.sharkfeed.SharkGalleryItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class SharkGalleryAdapter extends RecyclerView.Adapter<SharkGalleryAdapter.ViewHolder> {

    private Context mContext;
    private List<SharkGalleryItem> mList;

    public SharkGalleryAdapter(Context mContext, List<SharkGalleryItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            //mImageView = (ImageView) itemView.findViewById(R.id.gallery_item); old code
            mImageView = (ImageView) itemView.findViewById(R.id.gallery_item);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharkgallery_adapter,
                // View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharkgallery_item,
                parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SharkGalleryItem item = mList.get(position);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SharkPhotoActivity.class);
                intent.putExtra("item", item);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext)
                .load(item.getUrl())
                .thumbnail(0.5f)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<SharkGalleryItem> newList) {
        mList.addAll(newList);
    }

    public void clear() {
        mList.clear();
    }
}

