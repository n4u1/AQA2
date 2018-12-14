package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class DeviceInitFailDialog extends DialogFragment {
    public DeviceInitFailDialog() {
    }



    public static DeviceInitFailDialog newInstance(String time) {
        DeviceInitFailDialog deviceInitFailDialog =  new DeviceInitFailDialog();
        Bundle bundle = new Bundle(1);
        bundle.putString("time", time);
        deviceInitFailDialog.setArguments(bundle);
        return deviceInitFailDialog;
    }

    public interface DeviceInitFailDialogListener {
        public void DeviceInitFailDialogCallback(String string);
    }

    DeviceInitFailDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DeviceInitFailDialog.DeviceInitFailDialogListener) getActivity();
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
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String time = getArguments().getString("time");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("기기 초기화는 하루에 한번만 가능합니다.\n\n" + time + "후에 다시 시도해 주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.DeviceInitFailDialogCallback("확인");

            }
        });




        return builder.create();
    }
}
