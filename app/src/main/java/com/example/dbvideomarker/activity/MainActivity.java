package com.example.dbvideomarker.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.dbvideomarker.R;
import com.example.dbvideomarker.adapter.ViewPagerAdapter;
import com.example.dbvideomarker.ui.home.HomeFragment;
import com.example.dbvideomarker.ui.mark.MarkFragment;
import com.example.dbvideomarker.ui.playlist.PlaylistFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    ViewPagerAdapter adapter;
    MarkFragment markFragment;
    HomeFragment homeFragment;
    PlaylistFragment playlistFragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);//Set up View Pager

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#D5355F"));
        tabLayout.setTabTextColors(Color.parseColor("#73D5355F"), Color.parseColor("#D5355F"));
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        markFragment = new MarkFragment();
        homeFragment = new HomeFragment();
        playlistFragment = new PlaylistFragment();
        adapter.addFragment(homeFragment, "VIDEO");
        adapter.addFragment(markFragment, "BOOKMARK");
        adapter.addFragment(playlistFragment, "MY");
        viewPager.setAdapter(adapter);
    }

    public Fragment getFragment(int pos) {
        return adapter.getItem(pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
        }
    }
}

