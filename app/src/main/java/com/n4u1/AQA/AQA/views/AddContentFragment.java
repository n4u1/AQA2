package com.n4u1.AQA.AQA.views;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.dialog.ContentKindsChoiceDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContentFragment extends Fragment implements ContentKindsChoiceDialog.ContentKindsChoiceDialogListener{

    private AddContentFragmentListener mListener;

    public AddContentFragment() {}

    public interface AddContentFragmentListener {
        void callbackMethod(String string);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (AddContentFragmentListener) context;
    }

    private void sendValue(String string) {
        if (mListener != null) {
            mListener.callbackMethod(string);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_content, container, false);
        view.findViewById(R.id.imageView_addContent_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentKindsChoiceDialog contentKindsChoiceDialog = new ContentKindsChoiceDialog();
                contentKindsChoiceDialog.show(getFragmentManager(), "contentKindsChoiceDialog");
                contentKindsChoiceDialog.setDialogResult(new ContentKindsChoiceDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result) {
//                        Toast.makeText(getContext(), "result : " + result, Toast.LENGTH_SHORT).show();
                        sendValue(result);

                    }
                });
            }
        });

        return view;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDialogPositiveClick(ArrayList arrayList) {
        Toast.makeText(getContext(), "???????????" + arrayList, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }


    @Override
    public void myCallback(Bundle bundle) {
        String string = bundle.toString();
        Toast.makeText(getContext(), "bundle string?" + string, Toast.LENGTH_SHORT).show();

    }

}
