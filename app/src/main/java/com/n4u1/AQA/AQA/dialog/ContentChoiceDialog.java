package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.n4u1.AQA.AQA.R;

import java.util.ArrayList;

public class ContentChoiceDialog extends DialogFragment {

    public ContentChoiceDialog() {}


    public interface ContentChoiceDialogListener {
        public void onDialogPositiveClick(ArrayList arrayList);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    ContentChoiceDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ContentChoiceDialogListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("※ 5개 선택 가능")
                .setMultiChoiceItems(R.array.contents, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            mSelectedItems.add(i);
                        } else if (mSelectedItems.contains(i)) {
                            mSelectedItems.remove(Integer.valueOf(i));
                        }
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        ArrayList mSelectedItemsResult = new ArrayList();
                        for (int i = 0; i < 12; i++) {
                            if (mSelectedItems.contains(i)) {
                                switch (i) {
                                    case 0 : mSelectedItemsResult.add("아침"); break;
                                    case 1 : mSelectedItemsResult.add("점심"); break;
                                    case 2 : mSelectedItemsResult.add("저녁"); break;
                                    case 3 : mSelectedItemsResult.add("소개팅"); break;
                                    case 4 : mSelectedItemsResult.add("정치"); break;
                                    case 5 : mSelectedItemsResult.add("사회"); break;
                                    case 6 : mSelectedItemsResult.add("경제"); break;
                                    case 7 : mSelectedItemsResult.add("아무거나"); break;
                                    case 8 : mSelectedItemsResult.add("패션"); break;
                                    case 9 : mSelectedItemsResult.add("축구"); break;
                                    case 10 : mSelectedItemsResult.add("농구"); break;
                                    case 11 : mSelectedItemsResult.add("야구"); break;
                                }
                            }
                        }
                        mListener.onDialogPositiveClick(mSelectedItemsResult);

                    }

                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();
    }
}
