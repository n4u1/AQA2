package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.n4u1.AQA.AQA.R;

public class ContentTypeDialog extends DialogFragment {
    public ContentTypeDialog() {
    }


    public interface ContentTypeDialogListener {
        public void choiceItemCallback(String string);
    }

    ContentTypeDialogListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ContentTypeDialogListener) getActivity();
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
        builder.setTitle("※ 투표방식을 선택해주세요")
                .setItems(R.array.contentType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = "";
                        switch (which) {
                            case 0 : result = "단일 투표"; break;
                            case 1 : result = "순위 투표"; break;
                        }
                        mListener.choiceItemCallback(result);
                    }
                });
        return builder.create();
    }
}
