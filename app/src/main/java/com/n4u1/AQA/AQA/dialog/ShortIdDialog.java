package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.n4u1.AQA.AQA.R;

public class ShortIdDialog extends DialogFragment {
    public ShortIdDialog() {
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("확인", null)
                .setMessage("아이디는 영소문자와 숫자, 3~10 글자 사이로 입력해주세요.");
        return builder.create();
    }
}
