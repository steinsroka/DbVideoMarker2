package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.util.MyItemView;
import com.example.dbvideomarker.adapter.util.ViewCase;
import com.example.dbvideomarker.adapter.viewholder.ItemViewHolderMedia;
import com.example.dbvideomarker.database.entitiy.Media;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MyItemView> {

    private List<Media> mediaList;
    private LayoutInflater mInflater;
    private ViewCase sel_type;

    public MediaAdapter(Context context, ViewCase sel_type, List<Media> mediaList) {
        mInflater = LayoutInflater.from(context);
        this.sel_type = sel_type;
        this.mediaList = mediaList;

    }

    @NonNull
    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(sel_type == ViewCase.MEDIA) {
            View view = mInflater.from(parent.getContext()).inflate(R.layout.home_item_media, parent, false);
            return new ItemViewHolderMedia(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemView holder, int position) {
        if(holder instanceof ItemViewHolderMedia) {
            ItemViewHolderMedia viewHolderMedia = (ItemViewHolderMedia)holder;
            if(mediaList != null) {
                Media current = mediaList.get(position);
                viewHolderMedia.Name.setText(current.getName());
                viewHolderMedia.ContentUri.setText(current.getContentUri());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }
}
