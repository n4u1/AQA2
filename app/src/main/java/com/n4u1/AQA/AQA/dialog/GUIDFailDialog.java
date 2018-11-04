package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class GUIDFailDialog extends DialogFragment {
    public GUIDFailDialog() {
    }


    public interface GUIDFailDialogListener {
        public void GUIDFailDialogCallback(String string);
    }

    GUIDFailDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("사용해주셔서 감사합니다.");
        builder.setMessage("AQA는 공정성을 위해 하나의 기기에서 하나의 ID만 생성 가능합니다.\n\n혹시 모바일 기기를 변경하셨다면 초기화를 진행해주시기 바랍니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.GUIDFailDialogCallback("확인");
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xff4485c9);
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xff4485c9);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (GoHomeDialog.GoHomeDialogListener) getActivity();
            mListener = (GUIDFailDialog.GUIDFailDialogListener) getActivity();
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



}
