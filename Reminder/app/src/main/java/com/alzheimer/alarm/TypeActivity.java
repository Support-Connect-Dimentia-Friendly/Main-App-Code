package com.alzheimer.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        ButterKnife.bind(this);
    }

    //返回图标点击事件
    @OnClick(R.id.icon_fanhui)
    public void icon_fanhui(View v) {
        Intent intent = new Intent(this,Note_Home.class);
        startActivity(intent);
    }


    @OnClick({R.id.typeline, R.id.outline})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.typeline:
                Intent intent = new Intent(TypeActivity.this,EventActivity.class);
                startActivity(intent);
                break;
            case R.id.outline:
                Intent intent1 = new Intent(TypeActivity.this,OutEventActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
