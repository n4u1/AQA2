package com.n4u1.AQA.AQA.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.n4u1.AQA.AQA.util.GlideApp;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ImageFragment extends Fragment {

    private int GALLEY_CODE = 1000;


    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private String imgPath;
    private int contentCount;

    String[] fileString = {"", "", "", "", "", "", "", "", "", ""};

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


    private OnFragmentInteractionListener mListener;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);


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

        LinearLayout imageView_addImage = view.findViewById(R.id.imageView_addImage);
        imageView_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLEY_CODE);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String[] strings) {
        if (mListener != null) {
            mListener.onFragmentInteraction(strings);
        }
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int checkCount = imageViewCheck();
        if (requestCode == GALLEY_CODE) {
            if (data == null) {
                return;
            } else {
                if (checkCount == 0) {
                    linearLayout_userAddContent_1.setVisibility(View.VISIBLE);
                    textView_userAddContent_1.setVisibility(View.VISIBLE);
                    imageView_userAddContent_1.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_1);
                    fileString[0] = imgPath;
                }
                if (checkCount == 1) {
                    linearLayout_userAddContent_2.setVisibility(View.VISIBLE);
                    textView_userAddContent_2.setVisibility(View.VISIBLE);
                    imageView_userAddContent_2.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_2);
                    fileString[1] = imgPath;
                }
                if (checkCount == 2) {
                    linearLayout_userAddContent_3.setVisibility(View.VISIBLE);
                    textView_userAddContent_3.setVisibility(View.VISIBLE);
                    imageView_userAddContent_3.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_3);
                    fileString[2] = imgPath;
                }
                if (checkCount == 3) {
                    linearLayout_userAddContent_4.setVisibility(View.VISIBLE);
                    textView_userAddContent_4.setVisibility(View.VISIBLE);
                    imageView_userAddContent_4.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_4);
                    fileString[3] = imgPath;
                }
                if (checkCount == 4) {
                    linearLayout_userAddContent_5.setVisibility(View.VISIBLE);
                    textView_userAddContent_5.setVisibility(View.VISIBLE);
                    imageView_userAddContent_5.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_5);
                    fileString[4] = imgPath;

                }
                if (checkCount == 5) {
                    linearLayout_userAddContent_6.setVisibility(View.VISIBLE);
                    textView_userAddContent_6.setVisibility(View.VISIBLE);
                    imageView_userAddContent_6.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_6);
                    fileString[5] = imgPath;
                }
                if (checkCount == 6) {
                    linearLayout_userAddContent_7.setVisibility(View.VISIBLE);
                    textView_userAddContent_7.setVisibility(View.VISIBLE);
                    imageView_userAddContent_7.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_7);
                    fileString[6] = imgPath;
                }
                if (checkCount == 7) {
                    linearLayout_userAddContent_8.setVisibility(View.VISIBLE);
                    textView_userAddContent_8.setVisibility(View.VISIBLE);
                    imageView_userAddContent_8.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_8);
                    fileString[7] = imgPath;

                }
                if (checkCount == 8) {
                    linearLayout_userAddContent_9.setVisibility(View.VISIBLE);
                    textView_userAddContent_9.setVisibility(View.VISIBLE);
                    imageView_userAddContent_9.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_9);
                    fileString[8] = imgPath;
                }
                if (checkCount == 9) {
                    linearLayout_userAddContent_10.setVisibility(View.VISIBLE);
                    textView_userAddContent_10.setVisibility(View.VISIBLE);
                    imageView_userAddContent_10.setVisibility(View.VISIBLE);
                    imgPath = getPath(data.getData());
                    GlideApp.with(getContext()).load(imgPath).centerCrop().into(imageView_userAddContent_10);
                    fileString[9] = imgPath;

                }
            }
        } else {
            return;
        }
        mListener.onFragmentInteraction(fileString);
        contentCount = checkCount;
        super.onActivityResult(requestCode, resultCode, data);
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

    //디바이스 경로 가져오기
    private String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getContext(), uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }


    @Override
    public void onDestroy() {
        if (imageView_userAddContent_1 != null) {
            imageView_userAddContent_1 = null;
        }
        if (imageView_userAddContent_2 != null) {
            imageView_userAddContent_2 = null;
        }
        if (imageView_userAddContent_3 != null) {
            imageView_userAddContent_3 = null;
        }
        if (imageView_userAddContent_4 != null) {
            imageView_userAddContent_4 = null;
        }
        if (imageView_userAddContent_5 != null) {
            imageView_userAddContent_5 = null;
        }
        if (imageView_userAddContent_6 != null) {
            imageView_userAddContent_6 = null;
        }
        if (imageView_userAddContent_7 != null) {
            imageView_userAddContent_7 = null;
        }
        if (imageView_userAddContent_8 != null) {
            imageView_userAddContent_8 = null;
        }
        if (imageView_userAddContent_9 != null) {
            imageView_userAddContent_9 = null;
        }
        if (imageView_userAddContent_10 != null) {
            imageView_userAddContent_10 = null;
        }


        Log.d("lkjFragment onDestroy", "onDestroy");
        super.onDestroy();
    }

//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d("lkj4", String.valueOf(contentCount));
//        int curId = item.getItemId();
//        switch (curId) {
//            case R.id.menu_next:
//                upload(imgPath);
//                Intent intent = new Intent(getContext(), FileChoiceActivity.class);
//                startActivity(intent);
//                break;
//
//        }
//        onBackPressed();
//        return super.onOptionsItemSelected(item);
}


