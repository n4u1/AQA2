package com.n4u1.AQA.AQA.views;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
//

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

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {
    private static final int GALLEY_CODE = 10;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mEmailDatabaseReference;
    private ValueEventListener mEmailListener;
    private ChildEventListener mChildEventListener;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView imageView_createUser = findViewById(R.id.imageView_createUser);
        ImageView imageView_loginUser = findViewById(R.id.imageView_loginUser);
//        Button button_Login = findViewById(R.id.button_login);
        Button button_aLogin = findViewById(R.id.button_aLogin);
        Button button_bLogin = findViewById(R.id.button_bLogin);
        Button button_cLogin = findViewById(R.id.button_cLogin);
        Button button_dLogin = findViewById(R.id.button_dLogin);
        Button button_eLogin = findViewById(R.id.button_eLogin);
        Button button_fLogin = findViewById(R.id.button_fLogin);
        Button button_gLogin = findViewById(R.id.button_gLogin);
        Button button_hLogin = findViewById(R.id.button_hLogin);
//        Button button_uploadTest = findViewById(R.id.button_uploadTest);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mEmailDatabaseReference = mDatabase.getReference("users");
        storage = FirebaseStorage.getInstance();


        imageView_createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }
        });

        imageView_loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId;
                EditText editTextEmail = findViewById(R.id.editText_email);
                EditText editTextPassword = findViewById(R.id.editText_password);
                userId = editTextEmail.getText().toString();
//                userId = editTextEmail.getText().toString() + "@aqa.com";
                if (editTextEmail.getText().toString().equals("") || editTextPassword.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "빈 칸이 있어요 ㅠ_ㅠ", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
//                    Snackbar.make(v, "빈 칸이 있습니다ㅠ_ㅠ", Snackbar.LENGTH_SHORT).show();
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

    private void loginUser(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Toast.makeText(getApplicationContext(), "User Login Success", Toast.LENGTH_LONG).show();//
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "User Login Fail", Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }


    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "User Create Success", Toast.LENGTH_LONG).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "User Create Fail : " + task.getException(), Toast.LENGTH_LONG).show();
                            Log.d("getException", "createUserWithEmail:failure", task.getException());


                        }

                    }
                });
    }
}
