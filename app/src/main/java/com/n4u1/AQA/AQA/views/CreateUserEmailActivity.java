package com.n4u1.AQA.AQA.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.AgainPasswordDialog;
import com.n4u1.AQA.AQA.dialog.ConfirmPasswordFailDialog;
import com.n4u1.AQA.AQA.dialog.GuidCheckDialog;
import com.n4u1.AQA.AQA.dialog.LiveEmailDialog;
import com.n4u1.AQA.AQA.dialog.NotEmailDialog;
import com.n4u1.AQA.AQA.dialog.NullEmailDialog;
import com.n4u1.AQA.AQA.splash.LoadingDialog;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserEmailActivity extends AppCompatActivity implements GuidCheckDialog.GuidCheckDialogListener {


    private DatabaseReference mRef;
    EditText createUserEmail_editText_email, createUserEmail_editText_pw, createUserEmail_editText_pwComfirm;
    TextView createUserEmail_textView_next;
    ImageView createUserEmail_imageView_back;
    boolean GUID_CHECK_FLAG = true;
    boolean CURRENT_EMAIL_CHECK_FLAG = true;
    String guid;
    String TAG = "lkjlkj ";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_create_user_email);
        final LoadingDialog loadingDialog = new LoadingDialog(CreateUserEmailActivity.this);

        guid = getIntent().getStringExtra("guid");
        mRef = FirebaseDatabase.getInstance().getReference();
        createUserEmail_textView_next = findViewById(R.id.createUserEmail_textView_next);
        createUserEmail_editText_email = findViewById(R.id.createUserEmail_editText_email);
        createUserEmail_imageView_back = findViewById(R.id.createUserEmail_imageView_back);
        createUserEmail_editText_pw = findViewById(R.id.createUserEmail_editText_pw);
        createUserEmail_editText_pwComfirm = findViewById(R.id.createUserEmail_editText_pwComfirm);

        mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> usersIterator = dataSnapshot.getChildren().iterator();
                while (usersIterator.hasNext()) {
                    Map<String, Object> user = (Map<String, Object>) usersIterator.next().getValue();
                    String guid_ = String.valueOf(user.get("guid"));
                    if (guid_.equals(guid)) {
                        GUID_CHECK_FLAG = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //다음 클릭
        createUserEmail_textView_next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    createUserEmail_textView_next.setTextColor(0xFF88B6E7);
                }
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    createUserEmail_textView_next.setTextColor(0xff4485c9);
                    if (checkEmail()) {
                        loadingDialog.show();
                        final String email = createUserEmail_editText_email.getText().toString();
                        //이메일이 이미 존재하는지 여부
                        mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("lkjflag1", String.valueOf(CURRENT_EMAIL_CHECK_FLAG));
                                Iterator<DataSnapshot> usersIterator = dataSnapshot.getChildren().iterator();
                                while (usersIterator.hasNext()) {
                                    Map<String, Object> user = (Map<String, Object>) usersIterator.next().getValue();
                                    String emailTemp = String.valueOf(user.get("email"));
                                    if (emailTemp.equals(email)) {
                                        CURRENT_EMAIL_CHECK_FLAG = false;
                                        loadingDialog.dismiss();
                                        LiveEmailDialog liveEmailDialog = new LiveEmailDialog();
                                        liveEmailDialog.show(getSupportFragmentManager(), "liveEmailDialog");
                                    }
                                    Log.d("lkjflag2", String.valueOf(CURRENT_EMAIL_CHECK_FLAG));
                                }
                                //모두 이상없으면 다음으로 이동

                                loadingDialog.dismiss();
                                Log.d("lkjflag3", String.valueOf(CURRENT_EMAIL_CHECK_FLAG));
                                if (CURRENT_EMAIL_CHECK_FLAG) {
                                    loadingDialog.dismiss();
                                    Intent intent = new Intent(CreateUserEmailActivity.this, CreateUserIdActivity.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("pw", createUserEmail_editText_pw.getText().toString());
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
                return true;

            }
        });


        //뒤로 클릭
        createUserEmail_imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    /*
    onCreate();
     */

    //이메일 체크
    private boolean checkEmail() {
        if (!GUID_CHECK_FLAG) {
            GuidCheckDialog guidCheckDialog = new GuidCheckDialog();
            guidCheckDialog.show(getSupportFragmentManager(), "guidCheckDialog");
            return false;
        }
        if (createUserEmail_editText_email.getText().toString().isEmpty()) {
            NullEmailDialog nullEmailDialog = new NullEmailDialog();
            nullEmailDialog.show(getSupportFragmentManager(), "nullEmailDialog");
            return false;
        }
        if (!checkEmail(createUserEmail_editText_email.getText().toString())) {
            NotEmailDialog notEmailDialog = new NotEmailDialog();
            notEmailDialog.show(getSupportFragmentManager(), "notEmailDialog");
            return false;
        }
        if (createUserEmail_editText_pw.getText().toString().length() < 6) {
            AgainPasswordDialog againPasswordDialog = new AgainPasswordDialog();
            againPasswordDialog.show(getSupportFragmentManager(), "againPasswordDialog");
            return false;
        }
        if (!createUserEmail_editText_pwComfirm.getText().toString().equals(createUserEmail_editText_pw.getText().toString())) {
            ConfirmPasswordFailDialog confirmPasswordFailDialog = new ConfirmPasswordFailDialog();
            confirmPasswordFailDialog.show(getSupportFragmentManager(), "confirmPasswordFailDialog");
            return false;
        }

        CURRENT_EMAIL_CHECK_FLAG = true;
        return true;
    }


    //이메일 형식 체크
    private boolean checkEmail(String inputUserEmail) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputUserEmail);
        boolean isNormal = m.matches();
        return isNormal;
    }


    //광고ID 콜백
    @Override
    public void GuidCheckDialogCallback(String string) {
        if (string.equals("확인")) {
            onBackPressed();
        }
    }
}
