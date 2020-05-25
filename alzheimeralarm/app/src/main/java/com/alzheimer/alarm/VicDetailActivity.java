package com.alzheimer.alarm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VicDetailActivity extends AppCompatActivity {

    @BindView(R.id.icon_fanhui)
    ImageView iconFanhui;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.vic_des)
    TextView des;
    @BindView(R.id.vic_img)
    ImageView vicImg;


    @BindView(R.id.vic_tours1)
    TextView vicTours;
    @BindView(R.id.vic_phone)
    TextView vicPhone;

    private int id;
    private Vic vic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vic_detail);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("id", 0);
        Data_loading();
    }

    public Bitmap returnBitMap(String s) {
        {
            Bitmap bitmap = null;
            try {
                URL url = new URL(s);
                bitmap = BitmapFactory.decodeStream(url.openStream());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    // Handler running in the main thread: use looper in Android's default UI thread
    public Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String strData = (String) msg.obj;
                    vic = JSONObject.parseObject(strData, Vic.class);
                    des.setText(vic.getDescription());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = Message.obtain();
                            message.what = 2;
                            message.obj = returnBitMap(vic.getImg());
                            handlerUI.sendMessage(message);
//                            vicImg.setImageBitmap(returnBitMap(vic.getImg()));
                        }
                    }).start();
                    String[] tours = vic.getTours1().split("#");
                    vicTours.setText(tours[0]);
                    vicPhone.setText(tours[1]);
                    break;
                case 2:
                    vicImg.setImageBitmap((Bitmap) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    // Load data from database
    private void Data_loading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://39.107.109.210:13522/alzheimer/vic/" + id)// Request interface
                            .build();//Create request object
                    Response response = client.newCall(request).execute();// Get response object
                    if (response.isSuccessful()) {
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = response.body().string();
                        handlerUI.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @OnClick({R.id.icon_fanhui, R.id.home})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.icon_fanhui:
                intent = new Intent(this, VICActivity.class);
                startActivity(intent);
                break;
            case R.id.home:
                intent = new Intent(this, Note_Home.class);
                startActivity(intent);
                break;
        }
    }

}
