package com.n4u1.AQA.AQA.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.views.HomeActivity;

public class NotificationJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("lkjJobTest", "JOBTESTTTTTTTTTTTTTTTT");
            }
        }).start();
//        Log.d("lkjJobTest", "JOBTESTTTTTTTTTTTTTTTT");
//        notificationShow();

        return true; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("lkjJobTest", "JOBTES_endendend");
        return true; // Answers the question: "Should this job be retried?"
    }


    private void notificationShow() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_aqa_custom);
        builder.setContentTitle("100명이 투표했어요!");
        builder.setContentText("클릭해서 확인하기!");
        Intent intentHome = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentHome, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_aqa_custom);
        builder.setLargeIcon(largeIcon);

        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        manager.notify(1, builder.build());


    }


}
