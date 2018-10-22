package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.models.User;

import java.util.Map;

public class ShareDialog extends DialogFragment {

    public ShareDialog() {}


    public static final ShareDialog newInstance(String id) {
        ShareDialog shareDialog =  new ShareDialog();
        Bundle bundle = new Bundle(1);
        bundle.putString("id", id);
        shareDialog.setArguments(bundle);
        return shareDialog;
    }


    public interface ShareDialogListener {
        public void ShareDialogCallback(String string);
    }

    ShareDialogListener mListener;


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
            mListener = (ShareDialog.ShareDialogListener) getActivity();
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
        String userId = getArguments().getString("id");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(userId + "님!");
        builder.setMessage("SNS에 공유하고 인증 해주세요!\n하루 한분께 스벅 아아를 쏩니다!");
        builder.setPositiveButton("공유하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.ShareDialogCallback("공유하기");

            }
        });
        builder.setNegativeButton("인증하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.ShareDialogCallback("인증하기");
            }
        });



        return builder.create();
    }



}
