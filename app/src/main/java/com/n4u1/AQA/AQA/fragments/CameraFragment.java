package com.n4u1.AQA.AQA.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.util.GlideApp;
import com.n4u1.AQA.AQA.util.PermissionRequester;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CameraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CameraFragment extends Fragment {

    private int REQUEST_IMAGE_CAPTURE = 10000;
    private OnFragmentInteractionListener mListener;
    String imagePath;
    private int contentCount;


    String[] fileString = {"", "", "", "", "", "", "", "", "", ""};

    LinearLayout imageView_addImage;
    ImageView imageView_userAddContent_1, imageView_userAddContent_2, imageView_userAddContent_3,
            imageView_userAddContent_4, imageView_userAddContent_5, imageView_userAddContent_6,
            imageView_userAddContent_7, imageView_userAddContent_8, imageView_userAddContent_9,
            imageView_userAddContent_10;
    LinearLayout linearLayout_userAddContent_1, linearLayout_userAddContent_2, linearLayout_userAddContent_3,
            linearLayout_userAddContent_4, linearLayout_userAddContent_5, linearLayout_userAddContent_6,
            linearLayout_userAddContent_7, linearLayout_userAddContent_8, linearLayout_userAddContent_9,
            linearLayout_userAddContent_10;
    TextView textView_userAddContent_1, textView_userAddContent_2, textView_userAddContent_3,
            textView_userAddContent_4, textView_userAddContent_5, textView_userAddContent_6,
            textView_userAddContent_7, textView_userAddContent_8, textView_userAddContent_9,
            textView_userAddContent_10;
    File picture;


    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        imageView_addImage = view.findViewById(R.id.imageView_addImage);
        linearLayout_userAddContent_1 = view.findViewById(R.id.linearLayout_userAddContent_1);
        linearLayout_userAddContent_2 = view.findViewById(R.id.linearLayout_userAddContent_2);
        linearLayout_userAddContent_3 = view.findViewById(R.id.linearLayout_userAddContent_3);
        linearLayout_userAddContent_4 = view.findViewById(R.id.linearLayout_userAddContent_4);
        linearLayout_userAddContent_5 = view.findViewById(R.id.linearLayout_userAddContent_5);
        linearLayout_userAddContent_6 = view.findViewById(R.id.linearLayout_userAddContent_6);
        linearLayout_userAddContent_7 = view.findViewById(R.id.linearLayout_userAddContent_7);
        linearLayout_userAddContent_8 = view.findViewById(R.id.linearLayout_userAddContent_8);
        linearLayout_userAddContent_9 = view.findViewById(R.id.linearLayout_userAddContent_9);
        linearLayout_userAddContent_10 = view.findViewById(R.id.linearLayout_userAddContent_10);
        textView_userAddContent_1 = view.findViewById(R.id.textView_userAddContent_1);
        textView_userAddContent_2 = view.findViewById(R.id.textView_userAddContent_2);
        textView_userAddContent_3 = view.findViewById(R.id.textView_userAddContent_3);
        textView_userAddContent_4 = view.findViewById(R.id.textView_userAddContent_4);
        textView_userAddContent_5 = view.findViewById(R.id.textView_userAddContent_5);
        textView_userAddContent_6 = view.findViewById(R.id.textView_userAddContent_6);
        textView_userAddContent_7 = view.findViewById(R.id.textView_userAddContent_7);
        textView_userAddContent_8 = view.findViewById(R.id.textView_userAddContent_8);
        textView_userAddContent_9 = view.findViewById(R.id.textView_userAddContent_9);
        textView_userAddContent_10 = view.findViewById(R.id.textView_userAddContent_10);
        imageView_userAddContent_1 = view.findViewById(R.id.imageView_userAddContent_1);
        imageView_userAddContent_2 = view.findViewById(R.id.imageView_userAddContent_2);
        imageView_userAddContent_3 = view.findViewById(R.id.imageView_userAddContent_3);
        imageView_userAddContent_4 = view.findViewById(R.id.imageView_userAddContent_4);
        imageView_userAddContent_5 = view.findViewById(R.id.imageView_userAddContent_5);
        imageView_userAddContent_6 = view.findViewById(R.id.imageView_userAddContent_6);
        imageView_userAddContent_7 = view.findViewById(R.id.imageView_userAddContent_7);
        imageView_userAddContent_8 = view.findViewById(R.id.imageView_userAddContent_8);
        imageView_userAddContent_9 = view.findViewById(R.id.imageView_userAddContent_9);
        imageView_userAddContent_10 = view.findViewById(R.id.imageView_userAddContent_10);


        imageView_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Camera Application이 있으면
                if (isExistCameraApplication()) {

                    // Camera Application을 실행한다.
                    Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // 찍은 사진을 보관할 파일 객체를 만들어서 보낸다.
                    picture = savePictureFile();

                    if (picture != null) {
//                        cameraApp.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                        cameraApp.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), "com.n4u1.AQA.AQA.fileprovider", picture));
                        startActivityForResult(cameraApp, 10000);
                    }

                } else {
                    Toast.makeText(getContext(), "카메라 앱을 설치하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int checkCount = imageViewCheck();

        if (requestCode == 10000 && resultCode == RESULT_OK) {

            if (checkCount == 0) {
                linearLayout_userAddContent_1.setVisibility(View.VISIBLE);
                textView_userAddContent_1.setVisibility(View.VISIBLE);
                imageView_userAddContent_1.setVisibility(View.VISIBLE);
                fileString[0] = imagePath;
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_1);

            }
            if (checkCount == 1) {
                fileString[1] = imagePath;
                linearLayout_userAddContent_2.setVisibility(View.VISIBLE);
                textView_userAddContent_2.setVisibility(View.VISIBLE);
                imageView_userAddContent_2.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_2);
            }

            if (checkCount == 2) {
                fileString[2] = imagePath;
                linearLayout_userAddContent_3.setVisibility(View.VISIBLE);
                textView_userAddContent_3.setVisibility(View.VISIBLE);
                imageView_userAddContent_3.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_3);
            }

            if (checkCount == 3) {
                fileString[3] = imagePath;
                linearLayout_userAddContent_4.setVisibility(View.VISIBLE);
                textView_userAddContent_4.setVisibility(View.VISIBLE);
                imageView_userAddContent_4.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_4);
            }

            if (checkCount == 4) {
                fileString[4] = imagePath;
                linearLayout_userAddContent_5.setVisibility(View.VISIBLE);
                textView_userAddContent_5.setVisibility(View.VISIBLE);
                imageView_userAddContent_5.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_5);
            }

            if (checkCount == 5) {
                fileString[5] = imagePath;
                linearLayout_userAddContent_6.setVisibility(View.VISIBLE);
                textView_userAddContent_6.setVisibility(View.VISIBLE);
                imageView_userAddContent_6.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_6);
            }

            if (checkCount == 6) {
                fileString[6] = imagePath;
                linearLayout_userAddContent_7.setVisibility(View.VISIBLE);
                textView_userAddContent_7.setVisibility(View.VISIBLE);
                imageView_userAddContent_7.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_7);
            }

            if (checkCount == 7) {
                fileString[7] = imagePath;
                linearLayout_userAddContent_8.setVisibility(View.VISIBLE);
                textView_userAddContent_8.setVisibility(View.VISIBLE);
                imageView_userAddContent_8.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_8);
            }

            if (checkCount == 8) {
                fileString[8] = imagePath;
                linearLayout_userAddContent_9.setVisibility(View.VISIBLE);
                textView_userAddContent_9.setVisibility(View.VISIBLE);
                imageView_userAddContent_9.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_9);
            }

            if (checkCount == 9) {
                fileString[9] = imagePath;
                linearLayout_userAddContent_10.setVisibility(View.VISIBLE);
                textView_userAddContent_10.setVisibility(View.VISIBLE);
                imageView_userAddContent_10.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).load(imagePath).centerCrop().into(imageView_userAddContent_10);
            }

        }
        mListener.onFragmentInteraction(fileString);
        contentCount = checkCount;
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        imageView_userAddContent_1 = null;
        imageView_userAddContent_2 = null;
        imageView_userAddContent_3 = null;
        imageView_userAddContent_4 = null;
        imageView_userAddContent_5 = null;
        imageView_userAddContent_6 = null;
        imageView_userAddContent_7 = null;
        imageView_userAddContent_8 = null;
        imageView_userAddContent_9 = null;
        imageView_userAddContent_10 = null;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String[] strings);
    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }


    private int imageViewCheck() {
        int count = 0;
        if (imageView_userAddContent_1.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (imageView_userAddContent_2.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (imageView_userAddContent_3.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (imageView_userAddContent_4.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (imageView_userAddContent_5.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (imageView_userAddContent_6.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (imageView_userAddContent_7.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (imageView_userAddContent_8.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (imageView_userAddContent_9.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (imageView_userAddContent_10.getVisibility() == View.VISIBLE) {
            count++;
        }
        return count;
    }


    /**
     * Android에 Camera Application이 설치되어있는지 확인한다.
     *
     * @return 카메라 앱이 있으면 true, 없으면 false
     */
    private boolean isExistCameraApplication() {

        // Android의 모든 Application을 얻어온다.
        PackageManager packageManager = getContext().getPackageManager();

        // Camera Application
        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // MediaStore.ACTION_IMAGE_CAPTURE을 처리할 수 있는 App 정보를 가져온다.
        List cameraApps = packageManager.queryIntentActivities(cameraApp, PackageManager.MATCH_DEFAULT_ONLY);

        // 카메라 App이 적어도 한개 이상 있는지 리턴
        return cameraApps.size() > 0;
    }


    /**
     * 카메라에서 찍은 사진을 외부 저장소에 저장한다.
     *
     * @return
     */
    private File savePictureFile() {

        // 외부 저장소 쓰기 권한을 얻어온다.
        PermissionRequester.Builder requester =
                new PermissionRequester.Builder(getActivity());

        int result = requester
                .create()
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        20000,
                        new PermissionRequester.OnClickDenyButtonListener() {
                            @Override
                            public void onClick(Activity activity) {

                            }
                        });

        // 사용자가 권한을 수락한 경우
        if (result == PermissionRequester.ALREADY_GRANTED
                || result == PermissionRequester.REQUEST_PERMISSION) {

            // 사진 파일의 이름을 만든다.
            // Date는 java.util 을 Import 한다.
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            String fileName = "IMG_" + timestamp;

            /**
             * 사진파일이 저장될 장소를 구한다.
             * 외장메모리에서 사진을 저장하는 폴더를 찾아서
             * 그곳에 MYAPP 이라는 폴더를 만든다.
             */
            File pictureStorage = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "AQA");

            // 만약 장소가 존재하지 않는다면 폴더를 새롭게 만든다.
            if (!pictureStorage.exists()) {

                /**
                 * mkdir은 폴더를 하나만 만들고,
                 * mkdirs는 경로상에 존재하는 모든 폴더를 만들어준다.
                 */
                pictureStorage.mkdirs();
            }

            try {
                File file = File.createTempFile(fileName, ".jpg", pictureStorage);

                // ImageView에 보여주기위해 사진파일의 절대 경로를 얻어온다.
                imagePath = file.getAbsolutePath();

                // 찍힌 사진을 "갤러리" 앱에 추가한다.
                Intent mediaScanIntent =
                        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(imagePath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);

                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 사용자가 권한을 거부한 경우
        else {

        }

        return null;
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }



}
