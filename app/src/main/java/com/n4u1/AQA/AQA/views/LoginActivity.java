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
import android.os.Build;
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;
//

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
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotInputDialog;
import com.n4u1.AQA.AQA.dialog.PreviewDialog;
import com.n4u1.AQA.AQA.util.Common;
import com.n4u1.AQA.AQA.util.ImageSaver;


import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.security.AccessController.getContext;


public class LoginActivity extends AppCompatActivity implements PreviewDialog.PreviewDialogListener {
    private static final int GALLEY_CODE = 10;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mEmailDatabaseReference;
    private ValueEventListener mEmailListener;
    private ChildEventListener mChildEventListener;
    private FirebaseStorage storage;
//    private SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

    SplashActivity splashActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textView_hidden = findViewById(R.id.textView_hidden);


//        Bitmap shareBitmap;
        //테스트용


        ImageView imageView_createUser = findViewById(R.id.imageView_createUser);
        ImageView imageView_loginUser = findViewById(R.id.imageView_loginUser);
        ImageView imageView_findUser = findViewById(R.id.imageView_findUser);
        Button button_aLogin = findViewById(R.id.button_aLogin);
        Button button_bLogin = findViewById(R.id.button_bLogin);
        Button button_cLogin = findViewById(R.id.button_cLogin);
        Button button_dLogin = findViewById(R.id.button_dLogin);
        Button button_eLogin = findViewById(R.id.button_eLogin);
        Button button_fLogin = findViewById(R.id.button_fLogin);
        Button button_gLogin = findViewById(R.id.button_gLogin);
        Button button_hLogin = findViewById(R.id.button_hLogin);
        TextView textView_preview = findViewById(R.id.textView_preview);
        String htmlString="<u>둘러보기</u>";


        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mEmailDatabaseReference = mDatabase.getReference("users");
        storage = FirebaseStorage.getInstance();

        //둘러보기
        textView_preview.setText(Html.fromHtml(htmlString));
        textView_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewDialog previewDialog = new PreviewDialog();
                previewDialog.show(getSupportFragmentManager(), "previewDialog");
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
        imageView_createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }
        });


        //계정 찾기
        imageView_findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findIntent = new Intent(LoginActivity.this, FindUserActivity.class);
                startActivity(findIntent);
            }
        });


        //로그인하기
        imageView_loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId;
                EditText editTextEmail = findViewById(R.id.editText_email);
                EditText editTextPassword = findViewById(R.id.editText_password);
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

        button_aLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser("a@a.com", "aaaaaa");
            }
        });

        button_bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser("b@b.com", "bbbbbb");
            }
        });

        button_cLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser("c@c.com", "cccccc");
            }
        });

        button_dLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser("d@d.com", "dddddd");
            }
        });

        button_eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser("e@e.com", "eeeeee");
            }
        });

        button_fLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser("f@f.com", "ffffff");
            }
        });

        button_gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser("g@g.com", "gggggg");
            }
        });

        button_hLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser("h@h.com", "hhhhhh");
            }
        });
//
//        button_uploadTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                startActivityForResult(intent, GALLEY_CODE);
//
//            }
//        });
//
//        findViewById(R.id.button_uploadTestActivity).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, TestActivity.class);
//                startActivity(intent);
//
//            }
//        });

    }

//
//    //정규표현식으로 이메일 체킹
//    private boolean checkEmail(String inputUserEmail) {
//        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(inputUserEmail);
//        boolean isNormal = m.matches();
//        return isNormal;
//    }

    private void loginUser(final String email, final String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            SharedPreferences pref = getSharedPreferences("com.n4u1.AQA", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("com.n4u1.AQA.fireBaseUserEmail", email);
                            editor.putString("com.n4u1.AQA.fireBaseUserPassword", password);
                            editor.commit();



                            Intent intent = new Intent(LoginActivity.this, SplashLoadingActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "User Login Fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
}
