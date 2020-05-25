package com.alzheimer.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FacilitiesActivity extends AppCompatActivity {

    @BindView(R.id.icon_fanhui)
    ImageView iconFanhui;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bus_name)
    TextView busName;
    @BindView(R.id.bus_img)
    ImageView busImg;
    @BindView(R.id.busline)
    LinearLayout busline;
    @BindView(R.id.library_name)
    TextView libraryName;
    @BindView(R.id.library_img)
    ImageView libraryImg;
    @BindView(R.id.libraryline)
    LinearLayout libraryline;
    @BindView(R.id.sport_name)
    TextView sportName;
    @BindView(R.id.sport_img)
    ImageView sportImg;
    @BindView(R.id.sportline)
    LinearLayout sportline;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);
        ButterKnife.bind(this);
        mContext = this;
    }


    @OnClick({R.id.busline, R.id.libraryline, R.id.sportline,R.id.icon_fanhui,R.id.home})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.busline:
                intent = new Intent(this,VICActivity.class);
                startActivity(intent);
                break;
            case R.id.libraryline:
                intent = new Intent(this,LibraryActivity.class);
                startActivity(intent);
                break;
            case R.id.sportline:
                intent = new Intent(this,SportActivity.class);
                startActivity(intent);
                break;
            case R.id.home:case R.id.icon_fanhui:
                intent = new Intent(this, Note_Home.class);
                startActivity(intent);
                break;

        }
    }
}
