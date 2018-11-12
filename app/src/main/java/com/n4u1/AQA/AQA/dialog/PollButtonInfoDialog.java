package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class PollButtonInfoDialog extends DialogFragment {
    public PollButtonInfoDialog() {
    }

    public interface PollButtonInfoDialogListener {
        public void PollButtonInfoDialogCallback(String string);
    }

    PollButtonInfoDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (GoHomeDialog.GoHomeDialogListener) getActivity();
            mListener = (PollButtonInfoDialog.PollButtonInfoDialogListener) getActivity();
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

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("사진을 선택해서 투표 또는 순위투표를 해주세요\n그리고 아래 'Q' 버튼으로 투표 참여후 결과를 확인해보세요.");
        builder.setPositiveButton("확 인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.PollButtonInfoDialogCallback("확인");

            }
        });
        builder.setNeutralButton("다시보지않기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.PollButtonInfoDialogCallback("다시보지않기");
            }
        });
        return builder.create();
    }
}
