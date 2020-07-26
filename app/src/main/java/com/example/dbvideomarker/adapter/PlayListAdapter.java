package com.example.dbvideomarker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.listener.OnItemClickListener;
import com.example.dbvideomarker.database.entitiy.PlayList;

import org.w3c.dom.Text;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PViewHolder> {

    private OnItemClickListener onItemClickListener;

    private List<PlayList> playListList;
    private LayoutInflater nInflater;

    public PlayListAdapter(Context context, OnItemClickListener onItemClickListener) {
        nInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public PViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = nInflater.from(parent.getContext()).inflate(R.layout.playlist_main_item, parent, false);
        return new PViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PViewHolder holder, int position) {
        if (playListList != null) {
            PlayList current = playListList.get(position);
            holder.pId.setText(String.valueOf(current.getPid()));
            holder.pName.setText(current.getpName());
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pid = current.getPid();
                    onItemClickListener.clickLongItem(pid);
                    return false;
                }
            });

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pid = current.getPid();
                    String pname = current.getpName();
                    onItemClickListener.clickItem(pid, pname);
                }
            });
        }
    }

    public void setPlayLists(List<PlayList> playList) {
        playListList = playList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (playListList != null)
            return playListList.size();
        else return 0;
    }

    public class PViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView pId;
        private TextView pName;

        public PViewHolder(View view) {
            super(view);
            this.view = view;
            pId = view.findViewById(R.id.pId);
            pName = view.findViewById(R.id.pName);
        }
    }
}
