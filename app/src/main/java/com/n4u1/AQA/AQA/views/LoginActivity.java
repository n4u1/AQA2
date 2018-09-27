package com.n4u1.AQA.AQA.views;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(" ");
        }
//        getSupportActionBar().setIcon(R.drawable.q);
        getSupportActionBar().setIcon(R.mipmap.ic_q_custom);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        //파일업로드용 기기 저장소 접근 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

        Button button_CreateUser = findViewById(R.id.button_createUser);
        Button button_Login = findViewById(R.id.button_login);
        Button button_aLogin = findViewById(R.id.button_aLogin);
        Button button_bLogin = findViewById(R.id.button_bLogin);
        Button button_cLogin = findViewById(R.id.button_cLogin);
        Button button_dLogin = findViewById(R.id.button_dLogin);
        Button button_eLogin = findViewById(R.id.button_eLogin);
        Button button_fLogin = findViewById(R.id.button_fLogin);
        Button button_gLogin = findViewById(R.id.button_gLogin);
        Button button_hLogin = findViewById(R.id.button_hLogin);
        Button button_uploadTest = findViewById(R.id.button_uploadTest);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mEmailDatabaseReference = mDatabase.getReference("users");
        storage = FirebaseStorage.getInstance();



        button_CreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
//
//                EditText editTextEmail = findViewById(R.id.editText_email);
//                EditText editTextPassword = findViewById(R.id.editText_password);
//                final String inputUserEmail = editTextEmail.getText().toString();
//                final String inputUserPassword = editTextPassword.getText().toString();
//
//                if (inputUserEmail.isEmpty() || inputUserPassword.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "빈칸이 있어요!", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                if (!checkEmail(inputUserEmail)) {
//                    Toast.makeText(getApplicationContext(), "이메일 형식으로 입력해주세요!!!!", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                if (inputUserPassword.length() < 6) {
//                    int test = inputUserPassword.length();
//                    Toast.makeText(getApplicationContext(), "비밀번호가 너무 짧아요ㅠ_ㅠ (6자 이상)", Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), "현재 몇글자??? >>> " + test, Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                //email 계정 생성
//                createUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());

            }
        });

        button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextEmail = findViewById(R.id.editText_email);
                EditText editTextPassword = findViewById(R.id.editText_password);
                loginUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());

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

        button_uploadTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLEY_CODE);

            }
        });

        findViewById(R.id.button_uploadTestActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLEY_CODE) {

            StorageReference storageRef = storage.getReferenceFromUrl("gs://test130-1068f.appspot.com");

            Uri file = Uri.fromFile(new File(getPath(data.getData())));
            StorageReference riversRef = storageRef.child("imagessss/"+file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });

        }
    }

    private String getPath(Uri uri) {
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    //정규표현식으로 이메일 체킹
    private boolean checkEmail(String inputUserEmail) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputUserEmail);
        boolean isNormal = m.matches();
        return isNormal;
    }

    private void loginUser(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(getApplicationContext(), "User Login Success", Toast.LENGTH_LONG).show();
//
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            finish();
                            startActivity(intent);
//
//                            mEmailDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    Iterable<DataSnapshot> emailIterable = dataSnapshot.getChildren();
//                                    Iterator<DataSnapshot> emailIterator = emailIterable.iterator();
//
//                                    int userCount = (int)dataSnapshot.getChildrenCount();
//                                    int loopCount = 0;
//
//                                    while (emailIterator.hasNext()) {
//                                        User user = emailIterator.next().getValue(User.class);
//                                        if (!user.getEmail().equals(email)) {
//                                            loopCount++;
//                                        }
//                                    }
//                                    if ( loopCount == userCount) {
//                                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                                        finish();
//                                        startActivity(intent);
//                                    } else {
//                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                        finish();
//                                        startActivity(intent);
//                                    }
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });


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
