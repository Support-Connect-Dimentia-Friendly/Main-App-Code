package com.alzheimer.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.shotline)
    LinearLayout shotline;
    @BindView(R.id.bedline)
    LinearLayout bedline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.shotline, R.id.bedline,R.id.cleanline,R.id.shoppingline,R.id.rubbishline,R.id.bathline,R.id.hairline})
    public void onViewClicked(View view) {
        String eventname = "";
        switch (view.getId()) {
            case R.id.shotline:
                eventname = "Insulin shot";
                break;
            case R.id.bedline:
                eventname = "make the bed";
                break;
            case R.id.cleanline:
                eventname = "clean dishes";
                break;
            case R.id.shoppingline:
                eventname = "grocery shopping";
                break;
            case R.id.rubbishline:
                eventname = "Take the rubbish";
                break;
            case R.id.bathline:
                eventname = "take a bath";
                break;
            case R.id.hairline:
                eventname = "hair";
                break;
        }
        Intent intent = new Intent(EventActivity.this,Note_Edit.class);
        intent.putExtra("eventname",eventname);
        intent.putExtra("typename","at home");
        startActivity(intent);
    }
    @OnClick(R.id.icon_fanhui)
    public void icon_fanhui(View view){
        Intent intent = new Intent(this,Note_Home.class);
        startActivity(intent);
    }
}
