package com.n4u1.AQA.AQA.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
import com.n4u1.AQA.AQA.views.MyUploadActivity;
import com.n4u1.AQA.AQA.views.PollRankingActivity;
import com.n4u1.AQA.AQA.views.PollSingleActivity;
import com.n4u1.AQA.AQA.views.SplashActivity;
import com.n4u1.AQA.AQA.views.TestActivity;
import com.n4u1.AQA.AQA.views.WhenNotiClickActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.media.RingtoneManager.getDefaultUri;

public class NotificationJobService extends JobService {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("lkj noti ready", "noti ready");
                if (mUser != null) {
                    mDatabase.child("users").child(mUser.getUid()).child("uploadContent").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //내가 게시한 투표 리스트 가져오기
                            Map<String, Object> uploadContents = (Map<String, Object>) dataSnapshot.getValue();
                            final ArrayList<String> contentList = new ArrayList<>();
                            try {
                                for (Map.Entry<String, Object> entry : uploadContents.entrySet()) {
                                    if (entry.getValue().equals("true")) {
                                        contentList.add(entry.getKey());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            //가져온 리스트와 전체 리스트 비교하기
                            mDatabase.child("user_contents").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterator<DataSnapshot> listIterator = dataSnapshot.getChildren().iterator();
                                    while (listIterator.hasNext()) {
                                        final ContentDTO contentDTO = listIterator.next().getValue(ContentDTO.class);
                                        for (int i = 0; i < contentList.size(); i++) {
                                            if (contentDTO.contentKey.equals(contentList.get(i))) {

                                                //가져온 리스트와 전체 리스트 비교해서 알람에 설정된 카운트 가져오기
                                                //continueCount는 n번째마다 알람
                                                //oneCount는 n번
                                                mDatabase.child("user_contents").child(contentList.get(i)).child("alarm").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        try {
                                                            String tmp = dataSnapshot.toString();
                                                            int index = tmp.indexOf("C");//ContinueCount
                                                            String temp = tmp.substring(index, tmp.length() - 2);
                                                            int index_ = temp.indexOf("O");//OneCount
//                                                        int continueCount = Integer.parseInt(temp.substring(1, index_));
                                                            int oneCount = Integer.parseInt(temp.substring(index_ + 1, temp.length()));
//
                                                            if (oneCount != 0) {
                                                                if (contentDTO.contentHit >= oneCount) {
                                                                    notificationShow(contentDTO.title, contentDTO.contentHit, contentDTO.contentKey, contentDTO.pollMode, contentDTO.itemViewType);
                                                                    mDatabase.child("user_contents").child(contentDTO.contentKey).child("alarm").setValue("C0O0");
                                                                    Log.d("lkjJobTest1", "JOBTESTTTTT???????????TT");
                                                                    Log.d("lkjJobTest2", String.valueOf(contentDTO.contentHit));
                                                                    Log.d("lkjJobTest3", String.valueOf(oneCount));
                                                                }
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Log.d("lkjJobTest", "JOBTESTTTTTTTTTTTTTTTT");
                }

            }
        }).start();

        return true; // Answers the question: "Is there still work going on?"
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("lkjJobTest", "JOBTES_endendend");
        return true; // Answers the question: "Should this job be retried?"
    }


    public void notificationShow(String title, int hitCount, String contentKey, String mode, int itemViewType) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_q_custom);
        builder.setContentTitle(title);
        builder.setContentText(hitCount + "분께서 투표하셨어요!");
        PendingIntent pendingIntent;

        int requestID = (int) System.currentTimeMillis();

        Log.d("lkj hitCount", String.valueOf(hitCount));
        Log.d("lkj title", title);
        Log.d("lkj contentKey", contentKey);
        Log.d("lkj mode", mode);
        Log.d("lkj itemViewType", String.valueOf(itemViewType));

        if (mode.equals("순위 투표")) {
            Intent intentRanking = new Intent(getApplicationContext(), PollRankingActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(HomeActivity.class);
            stackBuilder.addNextIntent(intentRanking);
            Bundle bundle = new Bundle();
            bundle.putString("contentKey", contentKey);
            bundle.putInt("itemViewType", itemViewType);
            bundle.putInt("contentHit", hitCount);
            intentRanking.putExtras(bundle);
            pendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {
            Intent intentSingle = new Intent(getApplicationContext(), PollSingleActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(HomeActivity.class);
            stackBuilder.addNextIntent(intentSingle);
            Bundle bundle = new Bundle();
            bundle.putString("contentKey", contentKey);
            bundle.putInt("itemViewType", itemViewType);
            bundle.putInt("contentHit", hitCount);
            intentSingle.putExtras(bundle);
            pendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT);
        }




//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
////        stackBuilder.addParentStack(SplashActivity.class);
//        stackBuilder.addNextIntent(intent);
////        Bundle bundle = new Bundle();
////        bundle.putString("notify_contentKey", contentKey);
////        bundle.putString("notify_mode", mode);
////        bundle.putInt("notify_itemViewType", itemViewType);
////        bundle.putInt("notify_contentHit", hitCount);
////        intent.putExtras(bundle);
//        pendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT);





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
//
//    private void setNotification(String notificationMessage) {
//
////**add this line**
//        int requestID = (int) System.currentTimeMillis();
//
//        Uri alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        mNotificationManager  = getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent notificationIntent = new Intent(getApplicationContext(), NotificationActivity2.class);
//
////**add this line**
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
////**edit this line to put requestID as requestCode**
//        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle("My Notification")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(notificationMessage))
//                .setContentText(notificationMessage).setAutoCancel(true);
//        mBuilder.setSound(alarmSound);
//        mBuilder.setContentIntent(contentIntent);
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//
//    }

}
