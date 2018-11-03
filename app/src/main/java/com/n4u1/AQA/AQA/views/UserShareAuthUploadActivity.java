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
import com.n4u1.AQA.AQA.models.ShareAuthDTO;
import com.n4u1.AQA.AQA.models.SuggestDTO;
import com.n4u1.AQA.AQA.util.GlideApp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UserShareAuthUploadActivity extends AppCompatActivity implements GoHomeDialog.GoHomeDialogListener {


    private StorageReference storageRef;
    private FirebaseStorage mStorage;
    private StorageReference riversRef;
    private DatabaseReference mdatabaseRef;

    private int GALLEY_CODE = 1100;
    String[] fileStrings = {""};
    LinearLayout shareAuthUploadActivity_linearLayout_addImage, shareAuthUploadActivity_linearLayout_Image1;
    EditText shareAuthUploadActivity_editText_title;
    ImageView shareAuthUploadActivity_imageView_Image1;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_share_auth_upload);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("공유 인증 하기");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
        }


        auth = FirebaseAuth.getInstance();
        mdatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        shareAuthUploadActivity_linearLayout_addImage = findViewById(R.id.shareAuthUploadActivity_linearLayout_addImage);
        shareAuthUploadActivity_linearLayout_Image1 = findViewById(R.id.shareAuthUploadActivity_linearLayout_Image1);        
        shareAuthUploadActivity_editText_title = findViewById(R.id.shareAuthUploadActivity_editText_title);        
        shareAuthUploadActivity_imageView_Image1 = findViewById(R.id.shareAuthUploadActivity_imageView_Image1);


        //이미지 추가하기
        shareAuthUploadActivity_linearLayout_addImage.setOnClickListener(new View.OnClickListener() {
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
                    shareAuthUploadActivity_linearLayout_Image1.setVisibility(View.VISIBLE);
                    shareAuthUploadActivity_imageView_Image1.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getApplicationContext()).load(imgPath).centerCrop().into(shareAuthUploadActivity_imageView_Image1);
                    fileStrings[0] = imgPath;
                    shareAuthUploadActivity_linearLayout_addImage.setVisibility(View.GONE);
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.suggest_write_menu, menu);
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
        final String key = mDatabaseRef.child("shareAuth").push().getKey();
        String currentDate = getDate();

        ShareAuthDTO shareAuthDTO = new ShareAuthDTO();
        shareAuthDTO.title = shareAuthUploadActivity_editText_title.getText().toString();
        shareAuthDTO.uploadDate = currentDate;
        shareAuthDTO.uid = auth.getCurrentUser().getUid();
        shareAuthDTO.contentId = getContentId(currentDate);
        shareAuthDTO.shareAuthKey = key;
        shareAuthDTO.userID = getIntent().getStringExtra("shareAuthUserId");
        shareAuthDTO.userEmail= auth.getCurrentUser().getEmail();
        mDatabaseRef.child("shareAuth").child(key).setValue(shareAuthDTO);
        mDatabaseRef.child("users").child(auth.getCurrentUser().getUid()).child("shareAuth").child(key).setValue("true");


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
                            mdatabaseRef.child("shareAuth").child(key).child("imageUrl").setValue(uri.toString());
                        }
                    });
                }
            });
        }



        Intent progressIntent = new Intent(UserShareAuthUploadActivity.this, ShareAuthActivity.class);
        progressIntent.putExtra("sec", 2000);
        Intent intent = new Intent(UserShareAuthUploadActivity.this, ShareAuthActivity.class);
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd(E)HH:mm:ss", Locale.KOREAN);
        df.setTimeZone(timeZone);
        String currentDate = df.format(date);
        return currentDate;
    }



    public String getContentId(String date) {
        return date.replaceAll("[^0-9]", "");
    }


    private int imageViewCheck() {
        int count = 0;
        if (shareAuthUploadActivity_imageView_Image1.getVisibility() == View.VISIBLE) {
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
