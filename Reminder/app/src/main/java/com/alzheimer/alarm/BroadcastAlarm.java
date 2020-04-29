package com.alzheimer.alarm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 */

public class BroadcastAlarm extends BroadcastReceiver{

    private int alarmId;

    @Override
    public void onReceive(Context context, Intent intent) {
        //showMemo(context);
        Log.d("Alarm rings","rings");
        alarmId=intent.getIntExtra("alarmId",0);
        showNotice(context);
    }

    private void showNotice(Context context) {
        int num=alarmId;
        Log.d("Note_Home","alarmNoticeId "+num);

        Intent intent=new Intent(context,Note_Taskdetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.putExtra("alarmId",alarmId);
//        Note_table note_table= DataSupport.find(Note_table.class,num);
//        deleteTheAlarm(num);//or num
//        transportInformationToEdit(intent,note_table);
        context.startActivity(intent);
//        showMyStyle(context);
//        PendingIntent pi=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationManager manager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//        Notification notification = null;
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            NotificationChannel mChannel = new NotificationChannel("my_channel_01", "渠道名字", NotificationManager.IMPORTANCE_LOW);
//            Toast.makeText(context, mChannel.toString(), Toast.LENGTH_SHORT).show();
//            manager.createNotificationChannel(mChannel);
//            notification = new Notification.Builder(context)
//                    .setChannelId("my_channel_01")
//                    .setContentTitle("5 new messages")
//                    .setContentText("hahaha")
//                    .setSmallIcon(R.mipmap.ic_launcher).build();
//
//        }else{
//            notification=new NotificationCompat.Builder(context)
//                    .setContentTitle("您的备忘任务开始了～")
//                    .setContentText(note_table.getContent())
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentIntent(pi)
//                    .setAutoCancel(true)
//                    //.setStyle(new NotificationCompat.BigTextStyle().bigText(record.getMainText()))
//                    .setLights(Color.GREEN,1000,1000)
//                    .build();
//        }
//
//        manager.notify(num,notification);
    }


    /**
     * 原生自定义 dialog
     */
    private void showMyStyle(final Context context) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.layout_test, null);
        final TextView etUsername = view.findViewById(R.id.contents);
        final TextView etPassword = view.findViewById(R.id.contents1);

        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(view).setTitle("自定义dialog——登录").setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "账号： " + etUsername.getText().toString() + "  密码： " + etPassword.getText().toString()
                                , Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }

    public void permission(Context context,AlertDialog mFloatView){
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                context.startActivity(intent);
                return;
            } else {
                //Android6.0以上
                if (mFloatView!=null) {
                    mFloatView.show();
                }
            }
        } else {
            //Android6.0以下，不用动态声明权限
            if (mFloatView!=null) {
                mFloatView.show();
            }
        }

}

    private void deleteTheAlarm(int num) {
        ContentValues temp = new ContentValues();
        temp.put("alarm_key", "");
        String where = String.valueOf(num);
//        DataSupport.updateAll(Note_table.class, temp, "id = ?", where);
    }

    private void transportInformationToEdit(Intent it,Note_table note_table) {

        it.putExtra("content",note_table.getDescription());
        it.putExtra("color_key",note_table.getColorkey());
        it.putExtra("time",note_table.getTime());

    }

}
