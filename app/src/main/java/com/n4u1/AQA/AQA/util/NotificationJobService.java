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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.models.ContentDTO;
import com.n4u1.AQA.AQA.models.User;
import com.n4u1.AQA.AQA.views.HomeActivity;
import com.n4u1.AQA.AQA.views.MineActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NotificationJobService extends JobService {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here

        new Thread(new Runnable() {
            @Override
            public void run() {

                mDatabase.child("users").child(mUser.getUid()).child("uploadContent").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> uploadContents = (Map<String, Object>) dataSnapshot.getValue();
//                        Set set = uploadContents.keySet();

                        ArrayList<String> contentList = new ArrayList<>();

                        for (Map.Entry<String, Object> entry : uploadContents.entrySet()) {
//                            System.out.println(entry.getKey() + " / " + entry.getValue());
                            if (entry.getValue().equals("true")) {
                                contentList.add(entry.getKey());
                            }
                        }

                        for(int i = 0; i < contentList.size(); i++) {
                            System.out.println(contentList.get(i));
                        }

                        Map<String, String> alarmList;
                        alarmList = getAlarmList(contentList);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Log.d("lkjJobTest", "JOBTESTTTTTTTTTTTTTTTT");
            }
        }).start();
//        Log.d("lkjJobTest", "JOBTESTTTTTTTTTTTTTTTT");
//        notificationShow();

        return true; // Answers the question: "Is there still work going on?"
    }

    private Map<String, String> getAlarmList(final ArrayList<String> contentList) {
//        ArrayList<String> alarmList = new ArrayList<>();
        final Map<String, String> list = null;
        mDatabase.child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                ArrayList<String> tmp = new ArrayList<>();
//
//                Iterator<DataSnapshot> contentDTOIterator = dataSnapshot.getChildren().iterator();
//
//                while (contentDTOIterator.hasNext()) {
//                    final ContentDTO contentDTO = contentDTOIterator.next().getValue(ContentDTO.class);
//                    tmp.add(contentDTO.title);
//                }

                Iterator<DataSnapshot> listIterator = dataSnapshot.getChildren().iterator();
                while (listIterator.hasNext()) {
                    ContentDTO contentDTO = listIterator.next().getValue(ContentDTO.class);
                    contentList.size();
                    for (int i = 0; i < contentList.size(); i++) {
                        if (contentDTO.contentKey.equals(contentList.get(i))) {
//                            list.put(contentDTO.uid, contentDTO.title);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return list;

    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("lkjJobTest", "JOBTES_endendend");
        return true; // Answers the question: "Should this job be retried?"
    }


    private void notificationShow() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_q_custom);
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

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        manager.notify(1, builder.build());


    }

}
