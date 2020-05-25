package com.alzheimer.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TypeActivity extends AppCompatActivity {


    @BindView(R.id.typeline)
    LinearLayout typeline;
    @BindView(R.id.outline)
    LinearLayout outline;
    @BindView(R.id.home)
    ImageView home;
    @BindView(R.id.icon_fanhui)
    ImageView iconFanhui;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.typeline, R.id.outline, R.id.home, R.id.icon_fanhui})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.typeline:
                intent = new Intent(TypeActivity.this, EventActivity.class);
                startActivity(intent);
                break;
            case R.id.outline:
                intent = new Intent(TypeActivity.this, OutEventActivity.class);
                startActivity(intent);
                break;
            case R.id.home:
            case R.id.icon_fanhui:
                intent = new Intent(this, Note_Home.class);
                startActivity(intent);
                break;

        }
    }

}
