package com.example.dbvideomarker.callbacks;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.MainActivity;
import com.example.dbvideomarker.adapter.MarkAdapter;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.database.entitiy.Mark;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.ui.home.HomeFragment;
import com.example.dbvideomarker.ui.mark.MarkFragment;

import java.util.List;

public class Toolbar_ActionMode implements ActionMode.Callback {
    private final VideoAdapter videoAdapter;
    private final MarkAdapter markAdapter;
    private final boolean isHomeFragment;

    public Toolbar_ActionMode(Context context, VideoAdapter videoAdapter, MarkAdapter markAdapter, List<Video> videoList, List<Mark> markList, boolean isHomeFragment) {
        this.videoAdapter = videoAdapter;
        this.markAdapter = markAdapter;
        this.isHomeFragment = isHomeFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_action_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }


    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
       return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (isHomeFragment) {
            videoAdapter.removeSelection(true);  // remove selection
            Fragment HomerecyclerFragment = new MainActivity().getFragment(0);//Get list fragment
            if (HomerecyclerFragment != null)
                ((HomeFragment) HomerecyclerFragment).setNullToActionMode();//Set action mode null
        } else {
            markAdapter.removeSelection(true);  // remove selection
            Fragment MarkrecyclerFragment = new MainActivity().getFragment(1);//Get recycler fragment
            if (MarkrecyclerFragment != null)
                ((MarkFragment) MarkrecyclerFragment).setNullToActionMode();//Set action mode null
        }
    }

}
