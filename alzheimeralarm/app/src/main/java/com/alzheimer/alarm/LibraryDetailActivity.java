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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LibraryDetailActivity extends AppCompatActivity {

    @BindView(R.id.icon_fanhui)
    ImageView iconFanhui;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.des)
    TextView des;
    //    @BindView(R.id.library_img)
//    ImageView libraryImg;
    @BindView(R.id.library_address)
    TextView libraryAddress;
    @BindView(R.id.library_phone)
    TextView libraryPhone;
    @BindView(R.id.bmapView)
    MapView map;
    private int id;
    private Library library;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_detail);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("id", 0);
        Data_loading();
    }

    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // Handler running in the main thread: use looper in Android's default UI thread
    public Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String strData = (String) msg.obj;
                    library = JSONObject.parseObject(strData, Library.class);
                    des.setText(library.getInfo());
                    libraryAddress.setText(library.getAddress());
                    libraryPhone.setText(library.getPhone());
                    String latlngStr = library.getTag();
                    String[] point = latlngStr.split(",");
                    LatLng latLng = new LatLng(Double.parseDouble(point[0]), Double.parseDouble(point[1]));

                    MapStatusUpdate status2 = MapStatusUpdateFactory.newLatLng(latLng);
                    map.getMap().setMapStatus(status2);
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.mylocation);
                    OverlayOptions option = new MarkerOptions()
                            .position(latLng)
                            .icon(bitmap);
                    map.getMap().addOverlay(option);
                    break;

                default:
                    break;
            }
        }
    };

    //Load data from database
    private void Data_loading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://39.107.109.210:13522/alzheimer/library/" + id)//Request interface
                            .build();//Create request object
                    Response response = client.newCall(request).execute();//Get response object
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
                intent = new Intent(this, LibraryActivity.class);
                startActivity(intent);
                break;
            case R.id.home:
                intent = new Intent(this, Note_Home.class);
                startActivity(intent);
                break;
        }
    }
}
