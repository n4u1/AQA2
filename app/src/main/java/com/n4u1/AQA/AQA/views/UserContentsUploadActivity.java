package com.n4u1.AQA.AQA.views;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.R;

import com.n4u1.AQA.AQA.dialog.ContentChoiceDialog;
import com.n4u1.AQA.AQA.dialog.ContentTypeDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.n4u1.AQA.AQA.dialog.GoHomeDialog;
import com.n4u1.AQA.AQA.models.User;

import java.util.ArrayList;
import java.util.Map;

public class UserContentsUploadActivity extends AppCompatActivity implements ContentChoiceDialog.ContentChoiceDialogListener
        , ContentTypeDialog.ContentTypeDialogListener, AddContentFragment.AddContentFragmentListener, GoHomeDialog.GoHomeDialogListener {


    private static final int GALLEY_CODE = 100;
    private static final int VIDEO_CODE = 200;

    ImageView imageView_userAddContent_1;
    ImageView imageView_userAddContent_2;
    ImageView imageView_userAddContent_3;

    TextView textView_next;

    EditText editText_title;
    EditText editText_description;
    EditText editText_pollMode;
    EditText editText_addCategory;

    String pollMode;


    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private FirebaseDatabase mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contents_upload);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_aqa_custom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }


        //파일업로드용 기기 저장소 접근 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
//            requestPermissions(new String[]{Manifest.permission.CAMERA},1);
        }

//            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);


        storage = FirebaseStorage.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        pollMode = "";
        editText_title = findViewById(R.id.editText_title);
        editText_description = findViewById(R.id.editText_description);


        //카테고리 선택
        editText_addCategory = findViewById(R.id.editText_addCategory);
        editText_addCategory.setFocusable(false);
        editText_addCategory.setClickable(false);
        editText_addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentChoiceDialog contentChoiceDialog = new ContentChoiceDialog();
                contentChoiceDialog.show(getSupportFragmentManager(), "contentChoiceDialog");
            }
        });

        //투표모드 선택
        editText_pollMode = findViewById(R.id.editText_pollMode);
        editText_pollMode.setFocusable(false);
        editText_pollMode.setClickable(false);
        editText_pollMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentTypeDialog contentTypeDialog = new ContentTypeDialog();
                contentTypeDialog.show(getSupportFragmentManager(), "pollModeDialog");
            }
        });


        textView_next = findViewById(R.id.textView_next);
        textView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_title.getText().toString().equals("") | editText_addCategory.getText().toString().equals("") | editText_pollMode.getText().toString().equals("") | editText_description.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "빈 칸이 있어요!", Toast.LENGTH_SHORT).show();
                } else {
                    mdatabase.getReference().child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                            ArrayList<String> userInputContents = new ArrayList<>();
                            userInputContents.add(editText_title.getText().toString());
                            userInputContents.add(editText_addCategory.getText().toString());
                            userInputContents.add(editText_pollMode.getText().toString());
                            userInputContents.add(editText_description.getText().toString());
                            userInputContents.add(user.get("userId").toString());
                            Intent intent = new Intent(UserContentsUploadActivity.this, FileChoiceActivity.class);
                            intent.putStringArrayListExtra("userInputContents", userInputContents);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });


    }


    // + 버튼클릭 > 사진 or 동영상 추가 팝업 > 사진 선택
    public void addUserContentImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLEY_CODE);
    }

    // + 버튼클릭 > 사진 or 동영상 추가 팝업 > 동영상 선택
    private void addUserContentVideo() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.usercontentupload_menu, menu);
        return true;
    }

    // 상단>버튼 클릭시 이미지or동영상 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_home:
                if (editText_title.getText().toString().length() + editText_pollMode.getText().toString().length() +
                        editText_description.getText().toString().length() + editText_addCategory.getText().toString().length() == 0) {
                    Intent intentAqa = new Intent(UserContentsUploadActivity.this, HomeActivity.class);
                    intentAqa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentAqa);
                } else {
                    GoHomeDialog goHomeDialog = new GoHomeDialog();
                    goHomeDialog.show(getSupportFragmentManager(), "goHomeDialog");
                }
                break;
            case R.id.menu_next:
                if (editText_title.getText().toString().equals("") | editText_addCategory.getText().toString().equals("") | editText_pollMode.getText().toString().equals("") | editText_description.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "빈 칸이 있어요!", Toast.LENGTH_SHORT).show();
                } else {
                    mdatabase.getReference().child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                            ArrayList<String> userInputContents = new ArrayList<>();
                            userInputContents.add(editText_title.getText().toString());
                            userInputContents.add(editText_addCategory.getText().toString());
                            userInputContents.add(editText_pollMode.getText().toString());
                            userInputContents.add(editText_description.getText().toString());
                            userInputContents.add(user.get("userId").toString());
                            Intent intent = new Intent(UserContentsUploadActivity.this, FileChoiceActivity.class);
                            intent.putStringArrayListExtra("userInputContents", userInputContents);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                break;
            case android.R.id.home:
                if (editText_title.getText().toString().length() + editText_pollMode.getText().toString().length() +
                        editText_description.getText().toString().length() + editText_addCategory.getText().toString().length() == 0) {
                    Intent intentAqa = new Intent(UserContentsUploadActivity.this, HomeActivity.class);
                    intentAqa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentAqa);
                } else {
                    GoHomeDialog goHomeDialog = new GoHomeDialog();
                    goHomeDialog.show(getSupportFragmentManager(), "goHomeDialog");
                }
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    //ContentChoiceDialog 확인
    @Override
    public void onDialogPositiveClick(ArrayList arrayList) {
        editText_addCategory = findViewById(R.id.editText_addCategory);
        String string = arrayList.toString();
        if (arrayList.size() < 6) {
            String resultString = string.replace("[", "").replace("]", "");
            editText_addCategory.setText(resultString);
        } else {
            editText_addCategory.setText("");
            Toast.makeText(getApplicationContext(), "5개 이하로 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    //ContentChoiceDialog 취소
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    //ContentTypeDialog choiceItemCallback
    @Override
    public void choiceItemCallback(String string) {
        pollMode = string;
        editText_pollMode.setText(pollMode);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //AddContentFragment 에서 받은 "[사진] or [동영상]" 값을 받아서 처리
    @Override
    public void callbackMethod(String string) {
        if (string.equals("[사진]")) {
            addUserContentImage();

        } else if (string.equals("[동영상]")) {
            addUserContentVideo();

        } else {
            Toast.makeText(getApplicationContext(), "No Image/No Video...What the...?", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void GoHomeDialogCallback(String string) {
        if (string.equals("확인")) {
            Intent intentAqa = new Intent(UserContentsUploadActivity.this, HomeActivity.class);
            intentAqa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentAqa);
        }
    }
}
