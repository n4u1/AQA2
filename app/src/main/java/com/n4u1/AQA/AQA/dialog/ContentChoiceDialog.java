package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.n4u1.AQA.AQA.R;

import java.util.ArrayList;

public class ContentChoiceDialog extends DialogFragment {

    public ContentChoiceDialog() {}


    public interface ContentChoiceDialogListener {
        public void onDialogPositiveClick(ArrayList arrayList);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    ContentChoiceDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ContentChoiceDialogListener) getActivity();
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
        mListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("※ 5개 선택 가능")
                .setMultiChoiceItems(R.array.contents, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            mSelectedItems.add(i);
                        } else if (mSelectedItems.contains(i)) {
                            mSelectedItems.remove(Integer.valueOf(i));
                        }
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        ArrayList mSelectedItemsResult = new ArrayList();
                        for (int i = 0; i < 69; i++) {
                            if (mSelectedItems.contains(i)) {
                                switch (i) {
                                    case 0 : mSelectedItemsResult.add("개그"); break;
                                    case 1 : mSelectedItemsResult.add("건강"); break;
                                    case 2 : mSelectedItemsResult.add("게임"); break;
                                    case 3 : mSelectedItemsResult.add("결혼"); break;
                                    case 4 : mSelectedItemsResult.add("경제"); break;
                                    case 5 : mSelectedItemsResult.add("고민"); break;
                                    case 6 : mSelectedItemsResult.add("공포"); break;
                                    case 7 : mSelectedItemsResult.add("과학"); break;
                                    case 8 : mSelectedItemsResult.add("군대"); break;
                                    case 9 : mSelectedItemsResult.add("꿈"); break;
                                    case 10 : mSelectedItemsResult.add("낚시"); break;
                                    case 11 : mSelectedItemsResult.add("노래"); break;
                                    case 12 : mSelectedItemsResult.add("농구"); break;
                                    case 13 : mSelectedItemsResult.add("다이어트"); break;
                                    case 14 : mSelectedItemsResult.add("동물"); break;
                                    case 15 : mSelectedItemsResult.add("드라마"); break;
                                    case 16 : mSelectedItemsResult.add("똥"); break;
                                    case 17 : mSelectedItemsResult.add("롤"); break;
                                    case 18 : mSelectedItemsResult.add("만화"); break;
                                    case 19 : mSelectedItemsResult.add("맛집"); break;
                                    case 20 : mSelectedItemsResult.add("모바일게임"); break;
                                    case 21 : mSelectedItemsResult.add("바둑"); break;
                                    case 22 : mSelectedItemsResult.add("배그"); break;
                                    case 23 : mSelectedItemsResult.add("법"); break;
                                    case 24 : mSelectedItemsResult.add("뷰티"); break;
                                    case 25 : mSelectedItemsResult.add("사건사고"); break;
                                    case 26 : mSelectedItemsResult.add("사진"); break;
                                    case 27 : mSelectedItemsResult.add("사회"); break;
                                    case 28 : mSelectedItemsResult.add("세월호"); break;
                                    case 29 : mSelectedItemsResult.add("소개팅"); break;
                                    case 30 : mSelectedItemsResult.add("술"); break;
                                    case 31 : mSelectedItemsResult.add("스포츠"); break;
                                    case 32 : mSelectedItemsResult.add("시사"); break;
                                    case 33 : mSelectedItemsResult.add("식물"); break;
                                    case 34 : mSelectedItemsResult.add("심리"); break;
                                    case 35 : mSelectedItemsResult.add("아무거나"); break;
                                    case 36 : mSelectedItemsResult.add("아이폰"); break;
                                    case 37 : mSelectedItemsResult.add("아침"); break;
                                    case 38 : mSelectedItemsResult.add("안드로이드"); break;
                                    case 39 : mSelectedItemsResult.add("안주"); break;
                                    case 40 : mSelectedItemsResult.add("애니메이션"); break;
                                    case 41 : mSelectedItemsResult.add("야구"); break;
                                    case 42 : mSelectedItemsResult.add("여행"); break;
                                    case 43 : mSelectedItemsResult.add("역사"); break;
                                    case 44 : mSelectedItemsResult.add("연애"); break;
                                    case 45 : mSelectedItemsResult.add("연예인"); break;
                                    case 46 : mSelectedItemsResult.add("영화"); break;
                                    case 47 : mSelectedItemsResult.add("와우"); break;
                                    case 48 : mSelectedItemsResult.add("요리"); break;
                                    case 49 : mSelectedItemsResult.add("웹툰"); break;
                                    case 50 : mSelectedItemsResult.add("유머"); break;
                                    case 51 : mSelectedItemsResult.add("육아"); break;
                                    case 52 : mSelectedItemsResult.add("음악"); break;
                                    case 53 : mSelectedItemsResult.add("의료"); break;
                                    case 54 : mSelectedItemsResult.add("인테리어"); break;
                                    case 55 : mSelectedItemsResult.add("자동차"); break;
                                    case 56 : mSelectedItemsResult.add("자전거"); break;
                                    case 57 : mSelectedItemsResult.add("저녁"); break;
                                    case 58 : mSelectedItemsResult.add("점심"); break;
                                    case 59 : mSelectedItemsResult.add("정치"); break;
                                    case 60 : mSelectedItemsResult.add("종교"); break;
                                    case 61 : mSelectedItemsResult.add("지식"); break;
                                    case 62 : mSelectedItemsResult.add("직업"); break;
                                    case 63 : mSelectedItemsResult.add("책"); break;
                                    case 64 : mSelectedItemsResult.add("축구"); break;
                                    case 65 : mSelectedItemsResult.add("취업"); break;
                                    case 66 : mSelectedItemsResult.add("컴퓨터"); break;
                                    case 67 : mSelectedItemsResult.add("패션"); break;
                                    case 68 : mSelectedItemsResult.add("휴가"); break;

                                }
                            }
                        }
                        mListener.onDialogPositiveClick(mSelectedItemsResult);

                    }

                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();
    }
}
