package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.n4u1.AQA.AQA.R;

public class AlarmDoneDialog extends DialogFragment {
    public AlarmDoneDialog() {
    }



    public static AlarmDoneDialog newInstance(String count) {
        AlarmDoneDialog alarmDoneDialog =  new AlarmDoneDialog();
        Bundle bundle = new Bundle(1);
        bundle.putString("count", count);
        alarmDoneDialog.setArguments(bundle);
        return alarmDoneDialog;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xff4485c9);
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xff4485c9);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String count = getArguments().getString("count");
//        String[] item = {"이제 " + count + "명이 투표하면 알람이 발생합니다!"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("알람 설정 완료");
        builder.setMessage(count + "명이 투표하면 알려 드릴게요!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });




        return builder.create();
    }
}
