package com.n4u1.AQA.AQA.views;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.GoHomeDialog;
import com.n4u1.AQA.AQA.dialog.UploadLoadingActivity;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.util.GlideApp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UserSuggestUploadActivity extends AppCompatActivity implements GoHomeDialog.GoHomeDialogListener {

    private StorageReference storageRef;
    private FirebaseStorage mStorage;
    private StorageReference riversRef;
    private DatabaseReference mdatabaseRef;

    int loadingSec = 0;
    private int GALLEY_CODE = 1100;
    String[] fileStrings = {"", ""};
    LinearLayout suggestActivity_linearLayout_addImage, suggestActivity_linearLayout_Image1, suggestActivity_linearLayout_Image2;
    EditText suggestActivity_editText_title, suggestActivity_editText_description;
    ImageView suggestActivity_imageView_Image1, suggestActivity_imageView_Image2;

    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_suggest_upload);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
        }


        auth = FirebaseAuth.getInstance();
        mdatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        suggestActivity_linearLayout_addImage = findViewById(R.id.suggestActivity_linearLayout_addImage);
        suggestActivity_linearLayout_Image1 = findViewById(R.id.suggestActivity_linearLayout_Image1);
        suggestActivity_linearLayout_Image2 = findViewById(R.id.suggestActivity_linearLayout_Image2);
        suggestActivity_editText_title = findViewById(R.id.suggestActivity_editText_title);
        suggestActivity_editText_description = findViewById(R.id.suggestActivity_editText_description);
        suggestActivity_imageView_Image1 = findViewById(R.id.suggestActivity_imageView_Image1);
        suggestActivity_imageView_Image2 = findViewById(R.id.suggestActivity_imageView_Image2);


        //이미지 추가하기
        suggestActivity_linearLayout_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLEY_CODE);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int checkCount = imageViewCheck();
        String imgPath;
        if (requestCode == GALLEY_CODE) {
            if (data == null) {
                return;
            } else {
                if (checkCount == 0) {
                    suggestActivity_linearLayout_Image1.setVisibility(View.VISIBLE);
                    suggestActivity_imageView_Image1.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getApplicationContext()).load(imgPath).centerCrop().into(suggestActivity_imageView_Image1);
                    fileStrings[0] = imgPath;
                }
                if (checkCount == 1) {
                    suggestActivity_linearLayout_Image2.setVisibility(View.VISIBLE);
                    suggestActivity_imageView_Image2.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getApplicationContext()).load(imgPath).centerCrop().into(suggestActivity_imageView_Image2);
                    fileStrings[1] = imgPath;
                    suggestActivity_linearLayout_addImage.setVisibility(View.GONE);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.suggest_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_confirm:
//                loadingSec = getLoadingSec(fileStrings);//1000 = 1M
                upload(fileStrings);
                break;


            case android.R.id.home:
                GoHomeDialog goHomeDialog = new GoHomeDialog();
                goHomeDialog.show(getSupportFragmentManager(), "goHomeDialog");
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    public void upload(final String[] uri) {

        UploadTask uploadTask;
        DatabaseReference mDatabaseRef;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        final String key = mDatabaseRef.child("suggest").push().getKey();

        SuggestDTO suggestDTO = new SuggestDTO();
        suggestDTO.title = suggestActivity_editText_title.getText().toString();
        suggestDTO.description = suggestActivity_editText_description.getText().toString();
        suggestDTO.uploadDate = getDate();
        suggestDTO.uid = auth.getCurrentUser().getUid();
        suggestDTO.suggestKey = key;
        suggestDTO.userID = getIntent().getStringExtra("suggestUserId");
        suggestDTO.userEmail= auth.getCurrentUser().getEmail();
        mDatabaseRef.child("suggest").child(key).setValue(suggestDTO);
        mDatabaseRef.child("users").child(auth.getCurrentUser().getUid()).child("suggest").child(key).setValue("true");


        if (uri[0].length() != 0) {
            Uri file_0 = Uri.fromFile(new File(uri[0]));
            storageRef = mStorage.getReferenceFromUrl("gs://test130-1068f.appspot.com");
            riversRef = storageRef.child("images/" + file_0.getLastPathSegment());
            uploadTask = riversRef.putFile(file_0);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mdatabaseRef.child("suggest").child(key).child("imageUrl_1").setValue(uri.toString());
                        }
                    });
                }
            });
        }

        if (uri[1].length() != 0) {
            Uri file_1 = Uri.fromFile(new File(uri[1]));
            StorageReference storageRef = mStorage.getReferenceFromUrl("gs://test130-1068f.appspot.com");
            final StorageReference riversRef = storageRef.child("videos/" + file_1.getLastPathSegment());
            UploadTask uploadTask_ = riversRef.putFile(file_1);
            uploadTask_.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mdatabaseRef.child("suggest").child(key).child("imageUrl_2").setValue(uri.toString());
                        }
                    });
                }
            });
        }


        Intent progressIntent = new Intent(UserSuggestUploadActivity.this, UploadLoadingActivity.class);
        progressIntent.putExtra("sec", 2000);
        Intent intent = new Intent(UserSuggestUploadActivity.this, SuggestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        progressIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        startActivity(progressIntent);
//
    }



    public String getDate() {
        TimeZone timeZone;
        timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd(E)HH:mm:ssSS", Locale.KOREAN);
        df.setTimeZone(timeZone);
        String currentDate = df.format(date);
        return currentDate;
    }

//    private int getLoadingSec(String[] imgStrings) {
//        int sec = 0;
//        int file_size = 0;
//        for (int i = 0; i < 2; i++) {
//            if (imgStrings[i] != null) {
//                imgStrings[i] = imgStrings[i];
//                File file = new File(imgStrings[i]);
//                file_size = Integer.parseInt(String.valueOf(file.length()/1024));
//            }
//            sec = sec + file_size;
//        }
//        //1000 = 1M
//        return sec;
//    }

    private int imageViewCheck() {
        int count = 0;
        if (suggestActivity_imageView_Image1.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (suggestActivity_imageView_Image2.getVisibility() == View.VISIBLE) {
            count++;
        }
        return count;
    }

    //디바이스 경로 가져오기
    private String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }



    @Override
    public void GoHomeDialogCallback(String string) {
        if (string.equals("확인")) {
            onBackPressed();
        }

    }
}



