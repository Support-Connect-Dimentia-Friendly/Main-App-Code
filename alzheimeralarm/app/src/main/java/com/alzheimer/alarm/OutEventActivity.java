package com.alzheimer.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OutEventActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_event);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.gpline, R.id.dentistline,R.id.parkline,R.id.friendline,R.id.dogline,R.id.dinnerline,R.id.cookingline})
    public void onViewClicked(View view) {
        String eventname = "";
        switch (view.getId()) {
            case R.id.gpline:
                eventname = "GP appointment";
                break;
            case R.id.dentistline:
                eventname = "Dentist Appointment";
                break;
            case R.id.parkline:
                eventname = "Going to the park";
                break;
            case R.id.friendline:
                eventname = "Visiting Friends";
                break;
            case R.id.dogline:
                eventname = "Walking a dog";
                break;
            case R.id.dinnerline:
                eventname = "Going out for dinner";
                break;
            case R.id.cookingline:
                eventname = "Participate in community cooking competition";
                break;
        }
        Intent intent = new Intent(OutEventActivity.this,Note_Edit.class);
        intent.putExtra("eventname",eventname);
        intent.putExtra("typename","outdoor");
        startActivity(intent);
    }
    @OnClick(R.id.icon_fanhui)
    public void icon_fanhui(View view){
        Intent intent = new Intent(this,Note_Home.class);
        startActivity(intent);
    }
}
