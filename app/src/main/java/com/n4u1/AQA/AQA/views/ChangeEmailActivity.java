package com.n4u1.AQA.AQA.views;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.ChangeEmailDoneDialog;
import com.n4u1.AQA.AQA.dialog.LiveEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotInputDialog;
import com.n4u1.AQA.AQA.splash.LoadingDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeEmailActivity extends AppCompatActivity implements ChangeEmailDoneDialog.ChangeEmailDoneDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_change_email);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setSubtitle("이메일 변경");
        }

        final LoadingDialog loadingDialog = new LoadingDialog(this);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final EditText changeEmail_editText_email = findViewById(R.id.changeEmail_editText_email);
        final EditText changeEmail_editText_password = findViewById(R.id.changeEmail_editText_password);
        final Button changeEmail_button_done = findViewById(R.id.changeEmail_button_done);
        final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        changeEmail_button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputEmail = changeEmail_editText_email.getText().toString();
                final String inputPassword = changeEmail_editText_password.getText().toString();
                //빈칸이 있으면
                if (inputEmail.length() == 0 | inputPassword.length() == 0) {
                    NotInputDialog notInputDialog = new NotInputDialog();
                    notInputDialog.show(getSupportFragmentManager(), "notInputDialog");
                    //이메일 형식이 아니면
                } else if (!checkEmail(inputEmail)) {
                    NotEmailDialog notEmailDialog = new NotEmailDialog();
                    notEmailDialog.show(getSupportFragmentManager(), "notEmailDialog");
                    //정상이면 비밀번호 확인후 이메일 변경
                } else {
                    loadingDialog.show();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), inputPassword);
                    user.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    user.updateEmail(inputEmail)
                                            //변경 완료
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        mDatabaseReference.child("users").child(user.getUid()).child("email").setValue(inputEmail);
                                                        loadingDialog.dismiss();
                                                        ChangeEmailDoneDialog changeEmailDoneDialog = new ChangeEmailDoneDialog();
                                                        changeEmailDoneDialog.show(getSupportFragmentManager(), "changeEmailDoneDialog");
                                                    }
                                                }
                                            })
                                            //변경 실패
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    if (e.toString().contains("The email address is already in use by another account.")) {
                                                        loadingDialog.dismiss();
                                                        LiveEmailDialog liveEmailDialog = new LiveEmailDialog();
                                                        liveEmailDialog.show(getSupportFragmentManager(), "liveEmailDialog");
                                                    } else {
                                                        loadingDialog.dismiss();
                                                        Toast.makeText(ChangeEmailActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadingDialog.dismiss();
                                    Toast toast = Toast.makeText(ChangeEmailActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.show();
                                }
                            });
                }
            }
        });

    }
    /*
    onCreate()
    onCreate()
     */

    private boolean checkEmail(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void ChangeEmailDoneDialogCallback(String string) {
        if (string.equals("확인")) {
            finish();
        }
    }
}
