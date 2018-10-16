package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.n4u1.AQA.AQA.R;

public class CreateUserGenderDialog extends DialogFragment {

    public CreateUserGenderDialog() {}

    public interface CreateUserGenderDialogListener {
        public void choiceItemCallbackGender(String string);
    }

    CreateUserGenderDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CreateUserGenderDialogListener) getActivity();
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("※ 성별을 선택해주세요.")
                .setItems(R.array.createUserGender, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = "";
                        switch (which) {
                            case 0 : result ="남"; break;
                            case 1 : result ="여"; break;
                        }
                        mListener.choiceItemCallbackGender(result);
                    }
                });
        return builder.create();
    }
}
