package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.n4u1.AQA.AQA.R;

import java.util.ArrayList;

public class ContentKindsChoiceDialog extends DialogFragment {

    OnMyDialogResult mDialogResult;


    public ContentKindsChoiceDialog() {}


    public interface ContentKindsChoiceDialogListener {
        public void onDialogPositiveClick(ArrayList arrayList);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void myCallback(Bundle bundle);
    }

    ContentKindsChoiceDialogListener mListener;
    ContentKindsChoiceDialogListener myListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ContentKindsChoiceDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            myListener = (ContentKindsChoiceDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        myListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final ArrayList mSelectedItem = new ArrayList();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("추가할 자료의 종류를 선택해주세요.")
                .setSingleChoiceItems(R.array.contentskinds, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            mSelectedItem.clear();
                            mSelectedItem.add(i);
                        } else if (i == 1) {
                            mSelectedItem.clear();
                            mSelectedItem.add(i);
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            if (mSelectedItem.contains(0)) {
                                mSelectedItem.clear();
                                mSelectedItem.add("사진");
                                } else {
                                mSelectedItem.clear();
                                mSelectedItem.add("동영상");
                            }
                        mDialogResult.finish(mSelectedItem.toString());
//                            Bundle args = new Bundle();
//                            args.putString("test", String.valueOf(mSelectedItem));
//                            myListener.myCallback(args);


//                        Toast.makeText(getContext(), "arrayList : " + arrayList, Toast.LENGTH_SHORT).show();

//                        myListener.myCallback(savedInstanceState);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    //다이얼로그 프래그먼트에서 프래그먼트로 데이터를 보내려면 이거 써야함
    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }



    //다이얼로그 프래그먼트에서 프래그먼트로 데이터를 보내려면 이거 써야함
    public interface OnMyDialogResult{
        void finish(String result);
    }




}
