package com.example.dbvideomarker.activity.setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dbvideomarker.R;

public class InquiryActivity extends AppCompatActivity {

    public String getDeviceModel(){
        return Build.MODEL;
    }
    public String getOsVersion(){
        return Build.VERSION.RELEASE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inquiry);
        setTitle("1:1 고객 문의");
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            TextView email = findViewById(R.id.btnemail);
            email.setOnClickListener(view -> {
                Intent email1 = new Intent(Intent.ACTION_SEND);
                email1.setType("plain/text");
                String[] address = {"developer@email.com"};
                email1.putExtra(Intent.EXTRA_EMAIL, address);
                email1.putExtra(Intent.EXTRA_SUBJECT, "VideoMarker 문의");
                email1.putExtra(Intent.EXTRA_TEXT, "앱 버전 (AppVersion):" + getString(R.string.appVersion) +
                        "\n기기명 (Device):"+(getDeviceModel())+"\n안드로이드 OS (Android OS):" +
                        (getOsVersion())+"\n내용 (Content):\n");
                startActivity(email1);
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
            //뒤로가기
        }
        return super.onOptionsItemSelected(item);
    }
}