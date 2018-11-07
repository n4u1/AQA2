package com.n4u1.AQA.AQA.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.n4u1.AQA.AQA.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.n4u1.AQA.AQA.dialog.AgainPasswordDialog;
import com.n4u1.AQA.AQA.dialog.BaseLoadingActivity;
import com.n4u1.AQA.AQA.dialog.GUIDFailDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotInputDialog;
import com.n4u1.AQA.AQA.dialog.PreviewDialog;
import com.n4u1.AQA.AQA.dialog.PrivacyPolicyActivity;
import com.n4u1.AQA.AQA.util.Common;
import com.n4u1.AQA.AQA.util.ImageSaver;


import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.security.AccessController.getContext;


public class LoginActivity extends AppCompatActivity implements PreviewDialog.PreviewDialogListener,
        GUIDFailDialog.GUIDFailDialogListener {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private long backPressedTime = 0;
    EditText editTextEmail;
    EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textView_hidden = findViewById(R.id.textView_hidden);
        TextView textView_privacy_policy = findViewById(R.id.textView_privacy_policy);

        LinearLayout linearLayout_createUser = findViewById(R.id.linearLayout_createUser);
        LinearLayout linearLayout_loginUser = findViewById(R.id.linearLayout_loginUser);
        LinearLayout linearLayout_findUser = findViewById(R.id.linearLayout_findUser);
        TextView textView_preview = findViewById(R.id.textView_preview);
//        String htmlString = "<u>둘러보기</u>";


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);


        //둘러보기
//        textView_preview.setText(Html.fromHtml(htmlString));
        textView_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewDialog previewDialog = new PreviewDialog();
                previewDialog.show(getSupportFragmentManager(), "previewDialog");
            }
        });


        //개인정보 처리방침
        textView_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        //히든
        textView_hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Hidden Quest Complete!\n오늘 좋은일이 생길거에요!", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "히든 퀘스트!!\n잘생긴 경준이 오빠라고 하면 커피 쏩니다ㅋㅋㅋㅋㅋㅋㅋㅋㅋ선착순1명ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", Toast.LENGTH_LONG).show();

            }
        });


        //계정 만들기
        linearLayout_createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateUserEmailActivity.class);
                startActivity(intent);
            }
        });


        //계정 찾기
        linearLayout_findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findIntent = new Intent(LoginActivity.this, FindUserActivity.class);
                startActivity(findIntent);
            }
        });


        //로그인하기
        linearLayout_loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId;
                userId = editTextEmail.getText().toString();
                if (editTextEmail.getText().toString().equals("") || editTextPassword.getText().toString().equals("")) {
                    NotInputDialog notInputDialog = new NotInputDialog();
                    notInputDialog.show(getSupportFragmentManager(), "notInputDialog");
                } else if (!checkEmail(editTextEmail.getText().toString())) {
                    NotEmailDialog notEmailDialog = new NotEmailDialog();
                    notEmailDialog.show(getSupportFragmentManager(), "notEmailDialog");
                } else if (editTextPassword.getText().toString().length() < 6) {
                    AgainPasswordDialog againPasswordDialog = new AgainPasswordDialog();
                    againPasswordDialog.show(getSupportFragmentManager(), "againPasswordDialog");
                } else {
                    loginUser(userId, editTextPassword.getText().toString());
                }


            }
        });

    }


    private void loginUser(final String email, final String password) {
        Intent loadingIntent = new Intent(LoginActivity.this, SplashLoadingActivity.class);
        loadingIntent.putExtra("id", email);
        loadingIntent.putExtra("pw", password);
        startActivity(loadingIntent);

//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            try {
//
//                                Log.d("lkj current uid", mAuth.getCurrentUser().getUid());
//                                GUIDAsyncTask guidAsyncTask = new GUIDAsyncTask();
//                                guidAsyncTask.execute();
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
////                            Intent intent = new Intent(LoginActivity.this, SplashLoadingActivity.class);
////                            startActivity(intent);
////                            finish();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Handler hd = new Handler();
//                            hd.postDelayed(new splashhandlerLogin(), 3500);
//                            Toast.makeText(getApplicationContext(), "User Login Fail", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
    }


    private boolean checkEmail(String inputUserEmail) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputUserEmail);
        boolean isNormal = m.matches();
        return isNormal;
    }


    @Override
    public void PreviewDialogCallback(String string) {
        if (string.equals("확인")) {
            Toast toast = Toast.makeText(this, "준비중 입니다.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }



    private class GUIDAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String advertId = null;
            try {
                advertId = idInfo.getId();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            Log.d("lkj advertId1", advertId);
            return advertId;
        }

        @Override
        protected void onPostExecute(final String advertId) {
            Log.d("lkj advertId2", advertId);
            if (advertId != null && advertId.trim().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "구글 광고ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                DatabaseReference mDatabaseRef;
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                mDatabaseRef.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                        if (user.get("guid").toString().equals(advertId)) {
                            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("com.n4u1.AQA.fireBaseUserEmail", editTextEmail.getText().toString());
                            editor.putString("com.n4u1.AQA.fireBaseUserPassword", editTextPassword.getText().toString());
                            editor.commit();

                            Handler hd = new Handler();
                            hd.postDelayed(new splashhandlerHome(), 3500);
                        } else {
                            GUIDFailDialog guidFailDialog = new GUIDFailDialog();
                            guidFailDialog.show(getSupportFragmentManager(), "guidFailDialog");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }


    private class splashhandlerLogin implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            LoginActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }

    }


    private class splashhandlerHome implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), HomeActivity.class)); // 로딩이 끝난후 이동할 Activity
            LoginActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        long FINISH_INTERVAL_TIME = 2000;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
//            super.onBackPressed();
            Activity activity = this;
            activity.finish();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, " '뒤로' 버튼을 한번더 누르시면 종료 됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void GUIDFailDialogCallback(String string) {
    }

}
