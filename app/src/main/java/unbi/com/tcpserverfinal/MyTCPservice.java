package unbi.com.tcpserverfinal;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyTCPservice extends Service implements MyTCPinterface {
    MyTCPinterface myTCPinterface;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onStart(Intent intent, int startId) {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTCPinterface = this;
        if (intent != null) {


            MySendmessage mySendmessage = new MySendmessage(intent);
            mydoNetwork stuff = new mydoNetwork();
            mySendmessage.sendMsg(stuff);
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ServerSocket socServer = new ServerSocket(UserPrefernece.getInstance().getIpPort().getPort());
                    Socket socClient = null;
                    while (true) {
                        socClient = socServer.accept();
                        ServerAsyncTask serverAsyncTask = new ServerAsyncTask(myTCPinterface);
                        serverAsyncTask.execute(new Socket[]{socClient});
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        showNotification();
        return START_STICKY;
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, MyTCPservice.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);
        //todo crash is here

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForegroundForOreoandPei();
        } else {
            int ONGOING_NOTIFICATION_ID = 001;
            Notification notification =
                    new Notification.Builder(this)
                            .setContentTitle("TCP server")
                            .setContentText("Running....")
                            .setSmallIcon(com.unbi.tcpserverfinal.R.drawable.ic_stat_router)
                            .setContentIntent(pendingIntent)
                            .build();

            startForeground(ONGOING_NOTIFICATION_ID, notification);
        }
    }

    private void startMyOwnForegroundForOreoandPei() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "com.unbi.tcpserverfinal";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(com.unbi.tcpserverfinal.R.drawable.ic_stat_router)
                    .setContentTitle("TCP server")
                    .setContentText("Running.....")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }
    }

    @Override
    public void onDestroy() {
        NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.cancel(001);

    }


    @Override
    public void sendBroadCast(Intent intent) {
        sendBroadcast(intent);
    }

    @Override
    public void Toast(String msg) {
        if (UserPrefernece.getInstance().isBooltoast()) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

    }
}



