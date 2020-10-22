/*
package com.example.dbvideomarker.callbacks;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.activity.MainActivity;
import com.example.dbvideomarker.adapter.VideoAdapter;
import com.example.dbvideomarker.database.entitiy.Video;
import com.example.dbvideomarker.ui.home.HomeFragment;

public class Toolbar_ActionMode implements ActionMode.Callback {
    private Context context;
    private VideoAdapter videoAdapter;

    public Toolbar_ActionMode(Context context, VideoAdapter videoAdapter) {
        this.context = context;
        this.videoAdapter = videoAdapter;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_action_toolbar, menu);//Inflate the menu over action mode
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
        videoAdapter.removeSelection(true);  // remove selection
        Fragment recyclerFragment = new MainActivity().getFragment(0);//Get recycler fragment
        if (recyclerFragment != null) {
            ((HomeFragment) recyclerFragment).setNullToActionMode();//Set action mode null
        }
    }
}
*/
