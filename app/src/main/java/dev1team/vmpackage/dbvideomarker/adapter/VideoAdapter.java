package dev1team.vmpackage.dbvideomarker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import dev1team.vmpackage.dbvideomarker.R;
import dev1team.vmpackage.dbvideomarker.adapter.util.Callback;
import dev1team.vmpackage.dbvideomarker.adapter.viewholder.VideoViewHolderDrag;
import dev1team.vmpackage.dbvideomarker.adapter.viewholder.VideoViewHolderRecent;
import dev1team.vmpackage.dbvideomarker.listener.OnItemClickListener;
import dev1team.vmpackage.dbvideomarker.listener.OnItemSelectedListener;
import dev1team.vmpackage.dbvideomarker.adapter.util.MyItemView;
import dev1team.vmpackage.dbvideomarker.adapter.util.ViewCase;
import dev1team.vmpackage.dbvideomarker.adapter.viewholder.VideoViewHolderNormal;
import dev1team.vmpackage.dbvideomarker.adapter.viewholder.VideoViewHolderSelect;
import dev1team.vmpackage.dbvideomarker.database.entitiy.Video;
import dev1team.vmpackage.dbvideomarker.util.MediaStoreLoader;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<MyItemView> implements Callback.OnItemMoveListener {

    public interface OnStartDragListener {
        void onStartDrag(VideoViewHolderDrag mHolder);
    }

    private final OnStartDragListener onStartDragListener;
    private List<Video> videoList;
    private ViewCase sel_type;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedItemIds = new SparseBooleanArray(0);
    private OnItemSelectedListener onItemSelectedListener;
    private OnItemClickListener onItemClickListener;
    private RequestManager mRequestManager;

    public VideoAdapter(Context context, ViewCase sel_type, OnItemSelectedListener onItemSelectedListener, OnItemClickListener onItemClickListener, OnStartDragListener onStartDragListener) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        mRequestManager = Glide.with(context);
        this.onStartDragListener = onStartDragListener;
        this.sel_type = sel_type;
        this.onItemSelectedListener = onItemSelectedListener;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (sel_type == ViewCase.NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new VideoViewHolderNormal(view);
        } else if (sel_type == ViewCase.SELECT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_select, parent, false);
            return new VideoViewHolderSelect(view);
        } else if (sel_type == ViewCase.RECENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_recent, parent, false);
            return new VideoViewHolderRecent(view);
        } else if (sel_type == ViewCase.DRAG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_playlist, parent, false);
            return new VideoViewHolderDrag(view);
        }
        return null;
    }


    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final MyItemView holder, int position) {
        MediaStoreLoader loader = new MediaStoreLoader();
        if (holder instanceof VideoViewHolderNormal) {
            VideoViewHolderNormal viewHolderNormal = (VideoViewHolderNormal) holder;
            if (videoList != null) {
                Video current = videoList.get(position);
                viewHolderNormal.vName.setText(String.valueOf(current.getVname()));
                viewHolderNormal.vDur.setText(loader.getReadableDuration(current.getVdur()));
                mRequestManager.asBitmap().load(Uri.fromFile(new File(current.getVpath()))).into(viewHolderNormal.vThumb);
                viewHolderNormal.view.setOnClickListener(view -> onItemClickListener.clickItem(current.getContentId(), current.getVpath()));
                viewHolderNormal.moreImage.setOnClickListener(v -> onItemClickListener.clickLongItem(v, current.getContentId(), current.getVpath()));
            } else {
                viewHolderNormal.vName.setText("No Data");
            }
        } else if (holder instanceof VideoViewHolderSelect) {
            //선택모드

            VideoViewHolderSelect viewHolderSelect = (VideoViewHolderSelect) holder;
            if (videoList != null) {
                Video current = videoList.get(position);
                viewHolderSelect.selectedName.setText(String.valueOf(current.getVname()));
                viewHolderSelect.selectedDur.setText(String.valueOf(loader.getReadableDuration(current.getVdur())));
                mRequestManager.asBitmap().load(Uri.fromFile(new File(current.getVpath()))).into(viewHolderSelect.selectedThumb);

                if (mSelectedItems.get(position, false)) {
                    viewHolderSelect.view.setBackgroundColor(Color.parseColor("#A6A6A6"));
                } else {
                    viewHolderSelect.view.setBackgroundColor(Color.parseColor("#373737"));
                }
                viewHolderSelect.view.setOnClickListener(view -> {
                    if (mSelectedItems.get(position, false)) {
                        mSelectedItems.delete(position);
                        mSelectedItemIds.delete(current.getContentId());
                        onItemClickListener.onClickListener(current, view, 0);
                        notifyItemChanged(position);

                    } else {
                        mSelectedItems.put(position, true);
                        mSelectedItemIds.put(current.getContentId(), true);
                        onItemClickListener.onClickListener(current, view, 1);
                        notifyItemChanged(position);
                    }
                    Log.d("test", "parsed" + mSelectedItemIds.size());

                    onItemSelectedListener.onItemSelected(view, mSelectedItemIds);
                });
            }
        } else if (holder instanceof VideoViewHolderRecent) {
            VideoViewHolderRecent videoViewHolderRecent = (VideoViewHolderRecent) holder;
            if (videoList != null) {
                Video current = videoList.get(position);
                videoViewHolderRecent.rName.setText(current.getVname());
                videoViewHolderRecent.rDur.setText(String.valueOf(loader.getReadableDuration(current.getVdur())));
                mRequestManager.asBitmap().load(Uri.fromFile(new File(current.getVpath()))).into(videoViewHolderRecent.rThumb);
                videoViewHolderRecent.view.setOnClickListener(view -> onItemClickListener.clickItem(current.getContentId(), current.getVpath()));
            }
        } else if (holder instanceof VideoViewHolderDrag) {
            VideoViewHolderDrag videoViewHolderDrag = (VideoViewHolderDrag) holder;
            if (videoList != null) {
                Video current = videoList.get(position);
                videoViewHolderDrag.vName.setText(current.getVname());
                videoViewHolderDrag.vDur.setText(String.valueOf(loader.getReadableDuration(current.getVdur())));
                mRequestManager.asBitmap().load(Uri.fromFile(new File(current.getVpath()))).into(videoViewHolderDrag.vThumb);
                videoViewHolderDrag.view.setOnClickListener(view -> onItemClickListener.clickItem(current.getContentId(), current.getVpath()));
                videoViewHolderDrag.iv_drag.setOnTouchListener((v, event) -> {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        onStartDragListener.onStartDrag(videoViewHolderDrag);
                    }
                    return false;
                });
            }
        }
    }

    public void setVideos(List<Video> videos) {
        videoList = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (videoList != null) {
            return videoList.size();
        } else return 0;
    }

    public void removeSelection(boolean isActionModeMenuClicked) {
        mSelectedItemIds = new SparseBooleanArray(0);
        if (!isActionModeMenuClicked) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(videoList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public int getSelectedCount() {
        return mSelectedItemIds.size();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemIds.put(position, value);
        else
            mSelectedItemIds.delete(position);

        notifyDataSetChanged();
    }
}
