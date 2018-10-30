package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class FindEmailDialog extends DialogFragment {
    public FindEmailDialog() {
    }

    public static FindEmailDialog newInstance(String count) {
        FindEmailDialog findEmailDialog = new FindEmailDialog();
        Bundle bundle = new Bundle(1);
        bundle.putString("userEmail", count);
        findEmailDialog.setArguments(bundle);
        return findEmailDialog;
    }


    public interface FindEmailDialogListener {
        public void FindEmailDialogCallback(String string);
    }

    FindEmailDialogListener mListener;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (FindEmailDialogListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xff4485c9);
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xff4485c9);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String email = getArguments().getString("userEmail");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("비밀번호 변경 안내");
        builder.setMessage(email + "으로 비밀번호 변경 안내 메일이 발송됩니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.FindEmailDialogCallback("확인");
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

}
