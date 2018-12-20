package com.n4u1.AQA.AQA.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.n4u1.AQA.AQA.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n4u1.AQA.AQA.views.PollRankingActivity;
import com.n4u1.AQA.AQA.views.PollSingleActivity;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PollResultRankingDialog extends DialogFragment {
    public PollResultRankingDialog() {
    }

    private DatabaseReference mDatabaseReference;

    int contentHit;
    int imageN;
    int currentPick;
    String contentKey;
    String statisticsCode;
    String selectedDivide = "전 체 (선택)";
    //    String ageRange = "전 체";
    List<String> divideList = new ArrayList<>();
    TextView pollResultDialog_close;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_pollresult, container);
        HorizontalBarChart pollActivity_horizontalBarChart_result = view.findViewById(R.id.pollActivity_horizontalBarChart_result);
//        pollResultDialog_spinner_age = view.findViewById(R.id.pollResultDialog_spinner_age);
        AppCompatSpinner pollResultDialog_spinner_divide = view.findViewById(R.id.pollResultDialog_spinner_divide);
        pollResultDialog_close = view.findViewById(R.id.pollResultDialog_close);

        divideList.add("전 체 (선택)");
        divideList.add("여 자");
        divideList.add("남 자");
        divideList.add("10대 전체 (10 ~ 19)");
        divideList.add("ㄴ 10대 초반 (10 ~ 12)");
        divideList.add("ㄴ 10대 중반 (13 ~ 16)");
        divideList.add("ㄴ 10대 후반 (17 ~ 19)");
        divideList.add("20대 전체 (20 ~ 29)");
        divideList.add("ㄴ 20대 초반 (20 ~ 22)");
        divideList.add("ㄴ 20대 중반 (23 ~ 26)");
        divideList.add("ㄴ 20대 후반 (27 ~ 20)");
        divideList.add("30대 전체 (30 ~ 39)");
        divideList.add("ㄴ 30대 초반 (30 ~ 32)");
        divideList.add("ㄴ 30대 중반 (33 ~ 36)");
        divideList.add("ㄴ 30대 후반 (37 ~ 39)");
        divideList.add("40대 전체 (40 ~ 49)");
        divideList.add("ㄴ 40대 초반 (40 ~ 42)");
        divideList.add("ㄴ 40대 중반 (43 ~ 46)");
        divideList.add("ㄴ 40대 후반 (47 ~ 40)");
        divideList.add("50대 전체 (50 ~ 59)");
        divideList.add("ㄴ 50대 초반 (50 ~ 52)");
        divideList.add("ㄴ 50대 중반 (53 ~ 56)");
        divideList.add("ㄴ 50대 후반 (57 ~ 59)");
        divideList.add("60대 전체 (60 ~ 69)");
        divideList.add("ㄴ 60대 초반 (60 ~ 62)");
        divideList.add("ㄴ 60대 중반 (63 ~ 66)");
        divideList.add("ㄴ 60대 후반 (67 ~ 69)");
        divideList.add("70대 전체 (70 ~ 79)");
        divideList.add("ㄴ 70대 초반 (70 ~ 72)");
        divideList.add("ㄴ 70대 중반 (73 ~ 76)");
        divideList.add("ㄴ 70대 후반 (77 ~ 79)");
        divideList.add("80대 전체 (80 ~ 89)");
        divideList.add("ㄴ 80대 초반 (80 ~ 82)");
        divideList.add("ㄴ 80대 중반 (83 ~ 86)");
        divideList.add("ㄴ 80대 후반 (87 ~ 89)");
        divideList.add("90대 전체 (80 ~ 99)");
        divideList.add("ㄴ 90대 초반 (90 ~ 92)");
        divideList.add("ㄴ 90대 중반 (93 ~ 96)");
        divideList.add("ㄴ 90대 후반 (97 ~ 99)");

//        ArrayAdapter ageRangeAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, ageRangeList);
        ArrayAdapter selectedDivideAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, divideList);
//        ageRangeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        selectedDivideAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        pollResultDialog_spinner_age.setAdapter(ageRangeAdapter);
        pollResultDialog_spinner_divide.setAdapter(selectedDivideAdapter);


        //스피너클릭해서 차트재설정
        pollResultDialog_spinner_divide.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDivide = divideList.get(position);
//                Log.d("lkj in gd_", ageRange);
                parsingData(selectedDivide);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //차트클릭시 다이얼로그 닫기
        pollActivity_horizontalBarChart_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PollRankingActivity pollRankingActivity = (PollRankingActivity)getActivity();
                pollRankingActivity.refreshActivity();
                dismiss();
            }
        });
        pollResultDialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PollRankingActivity pollRankingActivity = (PollRankingActivity)getActivity();
                pollRankingActivity.refreshActivity();
                dismiss();
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            contentKey = bundle.getString("currentContent", null);
            statisticsCode = bundle.getString("statisticsCode");
            imageN = bundle.getInt("imageN");
            currentPick = bundle.getInt("imagePick");
            contentHit = bundle.getInt("contentHits");
            Log.d("lkj contentKey", contentKey);
            Log.d("lkj imageN", String.valueOf(imageN));
            Log.d("lkj currentPick", String.valueOf(currentPick));
            Log.d("lkj currentHits", String.valueOf(contentHit));
            Log.d("lkj statisticsCodeeee", statisticsCode);

//            getPicker(contentKey);            //textView Test

        }
        return view;
    }


    /**
     * onCreateView()     onCreateView()     onCreateView()     onCreateView()     onCreateView()     onCreateView()
     */

    //투표한 사용자 uid parsing
    private void getPicker(String k) {
        DatabaseReference mDatabaseReference_;
        mDatabaseReference_ = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference_.child("user_contents").child(k).child("contentPicker").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue().toString();
                int contentPickerCount = (int) dataSnapshot.getChildrenCount();

                ArrayList<String> stringArrayList = new ArrayList<>();
                String[] stringArray;
                stringArray = string.split("=");
                stringArray[0] = stringArray[0].replace("{", "");

                for (int i = 1; i < contentPickerCount; i++) {
                    int idx = stringArray[i].indexOf(" ");
                    int idx_ = stringArray[i].length();
                    stringArray[i] = stringArray[i].substring(idx + 1, idx_);
                }
                stringArray[contentPickerCount] = null;
                Collections.addAll(stringArrayList, stringArray);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //차트 초기 세팅 (전체 데이터)
    private void setChartFullData(final int contentN, String key, final View v) {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("user_contents").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> contentDTO = (Map<String, Object>) dataSnapshot.getValue();

                ArrayList<String> labels = new ArrayList<>();
                ArrayList<BarEntry> yValue = new ArrayList<>();
                ArrayList<Integer> tmp = new ArrayList<>();

                for (int i = 0; i < contentN + 1; i++) {
                    int j = contentN - i + 1;
                    labels.add("`" + String.valueOf(j));
                }

                Object object0 = contentDTO.get("candidateScore_0");
                Object object1 = contentDTO.get("candidateScore_1");
                Object object2 = contentDTO.get("candidateScore_2");
                Object object3 = contentDTO.get("candidateScore_3");
                Object object4 = contentDTO.get("candidateScore_4");
                Object object5 = contentDTO.get("candidateScore_5");
                Object object6 = contentDTO.get("candidateScore_6");
                Object object7 = contentDTO.get("candidateScore_7");
                Object object8 = contentDTO.get("candidateScore_8");
                Object object9 = contentDTO.get("candidateScore_9");

                tmp.add(Integer.parseInt(object0.toString()));
                tmp.add(Integer.parseInt(object1.toString()));
                tmp.add(Integer.parseInt(object2.toString()));
                tmp.add(Integer.parseInt(object3.toString()));
                tmp.add(Integer.parseInt(object4.toString()));
                tmp.add(Integer.parseInt(object5.toString()));
                tmp.add(Integer.parseInt(object6.toString()));
                tmp.add(Integer.parseInt(object7.toString()));
                tmp.add(Integer.parseInt(object8.toString()));
                tmp.add(Integer.parseInt(object9.toString()));

                HorizontalBarChart pollActivity_horizontalBarChart_result = v.findViewById(R.id.pollActivity_horizontalBarChart_result);
//                HorizontalBarChart pollActivity_horizontalBarChart_result1 = v.findViewById(R.id.pollActivity_horizontalBarChart_result1);

                CategoryBarChartXaxisFormatter xAxisFormatter = new CategoryBarChartXaxisFormatter(labels);
                XAxis xAxis = pollActivity_horizontalBarChart_result.getXAxis();
                xAxis.setValueFormatter(xAxisFormatter);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setXOffset(10); // 후보 1,2,3,4,5....위치
                xAxis.setTextSize(20f); // 후보 1,2,3,4,5... 크기

                YAxis yAxis = pollActivity_horizontalBarChart_result.getAxisLeft();
                yAxis.setAxisMinimum(0);
                yAxis.setMinWidth(0);
                yAxis.setMaxWidth(3);
                yAxis.setDrawZeroLine(true);
                yAxis.setDrawTopYLabelEntry(true);

                yAxis.setCenterAxisLabels(true);
                yAxis.setEnabled(true);

//                pollActivity_horizontalBarChart_result.getDescription().setEnabled(false);
                pollActivity_horizontalBarChart_result.setTouchEnabled(true);
                pollActivity_horizontalBarChart_result.setDragEnabled(false);
                pollActivity_horizontalBarChart_result.setDoubleTapToZoomEnabled(false);
                pollActivity_horizontalBarChart_result.setPinchZoom(false);
                pollActivity_horizontalBarChart_result.setDescription(null);
                pollActivity_horizontalBarChart_result.animateY(1200);
                pollActivity_horizontalBarChart_result.setFitBars(true);
                pollActivity_horizontalBarChart_result.setDrawBarShadow(false);
                pollActivity_horizontalBarChart_result.getAxisLeft().setEnabled(false);
                pollActivity_horizontalBarChart_result.getAxisRight().setEnabled(false);
                pollActivity_horizontalBarChart_result.getXAxis().setEnabled(true);
                pollActivity_horizontalBarChart_result.setDrawValueAboveBar(true);
                pollActivity_horizontalBarChart_result.setDrawGridBackground(false);
                pollActivity_horizontalBarChart_result.getLegend().setEnabled(false);


                for (int i = 0; i < contentN; i++) {
                    yValue.add(new BarEntry((float) contentN - i, tmp.get(i)));
                }

                BarDataSet set1 = new BarDataSet(yValue, null);
//                set1.setColor(Color.GRAY);
//                set1.setColors(ColorTemplate.LIBERTY_COLORS);
                set1.setColors(0xff4485c9);

                BarData data1 = new BarData(set1);
                data1.setBarWidth(0.5f); //바 크기
                data1.setValueTextSize(15f); //결과값 크기
                data1.setValueTextColor(Color.GRAY);



                ResultValueFormatter resultValueFormatter = new ResultValueFormatter();
                data1.setValueFormatter(resultValueFormatter);

                pollActivity_horizontalBarChart_result.setData(data1);
                pollActivity_horizontalBarChart_result.invalidate(); //refresh


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("lkj setChartData", "setChartData ERR");

            }
        });

    }



    private void setChartPartData(final int contentN, String key, final View v, final int[] integerArrayList) {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("user_contents").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> contentDTO = (Map<String, Object>) dataSnapshot.getValue();

                ArrayList<String> labels = new ArrayList<>();
                ArrayList<BarEntry> yValue = new ArrayList<>();
                ArrayList<Integer> tmp = new ArrayList<>();

                for (int i = 0; i < contentN + 1; i++) {
                    int j = contentN - i + 1;
                    labels.add("`" + String.valueOf(j));
                }

                for (int i = 0; i < contentN; i++) {
                    tmp.add(integerArrayList[i]);
                }

                HorizontalBarChart pollActivity_horizontalBarChart_result = v.findViewById(R.id.pollActivity_horizontalBarChart_result);
//                HorizontalBarChart pollActivity_horizontalBarChart_result1 = v.findViewById(R.id.pollActivity_horizontalBarChart_result1);

                CategoryBarChartXaxisFormatter xAxisFormatter = new CategoryBarChartXaxisFormatter(labels);
                XAxis xAxis = pollActivity_horizontalBarChart_result.getXAxis();
                xAxis.setValueFormatter(xAxisFormatter);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setXOffset(10); // 후보 1,2,3,4,5....위치
                xAxis.setTextSize(20f); // 후보 1,2,3,4,5... 크기


                YAxis yAxis = pollActivity_horizontalBarChart_result.getAxisLeft();
                yAxis.setAxisMinimum(0);
                yAxis.setMinWidth(0);
                yAxis.setMaxWidth(3);
                yAxis.setDrawZeroLine(true);
                yAxis.setDrawTopYLabelEntry(true);


                yAxis.setCenterAxisLabels(true);
                yAxis.setEnabled(true);

//                pollActivity_horizontalBarChart_result.getDescription().setEnabled(false);
                pollActivity_horizontalBarChart_result.setTouchEnabled(true);
                pollActivity_horizontalBarChart_result.setDragEnabled(false);
                pollActivity_horizontalBarChart_result.setDoubleTapToZoomEnabled(false);
                pollActivity_horizontalBarChart_result.setPinchZoom(false);
                pollActivity_horizontalBarChart_result.setDescription(null);
                pollActivity_horizontalBarChart_result.animateY(1200);
                pollActivity_horizontalBarChart_result.setFitBars(true);
                pollActivity_horizontalBarChart_result.setDrawBarShadow(false);
                pollActivity_horizontalBarChart_result.getAxisLeft().setEnabled(false);
                pollActivity_horizontalBarChart_result.getAxisRight().setEnabled(false);
                pollActivity_horizontalBarChart_result.getXAxis().setEnabled(true);
                pollActivity_horizontalBarChart_result.setDrawValueAboveBar(true);
                pollActivity_horizontalBarChart_result.setDrawGridBackground(false);
                pollActivity_horizontalBarChart_result.getLegend().setEnabled(false);


                for (int i = 0; i < contentN; i++) {
                    yValue.add(new BarEntry((float) contentN - i, tmp.get(i)));
                }

                BarDataSet set1 = new BarDataSet(yValue, null);
//                set1.setColor(Color.GRAY);
//                set1.setColors(ColorTemplate.LIBERTY_COLORS);
                set1.setColors(0xff4485c9);

                BarData data1 = new BarData(set1);
                data1.setBarWidth(0.5f); //바 크기
                data1.setValueTextSize(15f); //결과값 크기
                data1.setValueTextColor(Color.GRAY);


                ResultValueFormatter resultValueFormatter = new ResultValueFormatter();
                data1.setValueFormatter(resultValueFormatter);

                pollActivity_horizontalBarChart_result.setData(data1);
                pollActivity_horizontalBarChart_result.invalidate(); //refresh

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("lkj setChartData", "setChartData ERR");

            }
        });
    }


    private void parsingData(String gR) {
        String[] stringArray = null;
        stringArray = statisticsCode.split(":");
        int[] tmpStatistics = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            tmpStatistics[i] = Integer.parseInt(stringArray[i]);
        }

        if (gR.equals("전 체 (선택)")) {
            setChartFullData(imageN, contentKey, getView());
            Log.d("lkj parsingData", "초기상태"); //처음 onCreate에서 차트 만들지 말고 여기서 한번만 만들면 될듯
            //10대전체, 20대전체 만들고 선택시 초중후 합치는 로직으로?
        } else {
            int[] tmpStatistics_divide = new int[30];
            switch (gR) {
                case "여 자":
                    System.arraycopy(tmpStatistics, 0, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "남 자":
                    System.arraycopy(tmpStatistics, 10, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "10대 전체 (10 ~ 19)":
                    System.arraycopy(tmpStatistics, 20, tmpStatistics_divide, 0, 30);
                    setChartPartData(imageN, contentKey, getView(), ageRangePlus(tmpStatistics_divide));
                    break;
                case "10대 초반 (10 ~ 12)":
                    System.arraycopy(tmpStatistics, 20, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "10대 중반 (13 ~ 16)":
                    System.arraycopy(tmpStatistics, 30, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "10대 후반 (17 ~ 19)":
                    System.arraycopy(tmpStatistics, 40, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "20대 전체 (20 ~ 29)":
                    System.arraycopy(tmpStatistics, 50, tmpStatistics_divide, 0, 30);
                    setChartPartData(imageN, contentKey, getView(), ageRangePlus(tmpStatistics_divide));
                    break;
                case "20대 초반 (20 ~ 22)":
                    System.arraycopy(tmpStatistics, 50, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "20대 중반 (23 ~ 26)":
                    System.arraycopy(tmpStatistics, 60, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "20대 후반 (27 ~ 20)":
                    System.arraycopy(tmpStatistics, 70, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "30대 전체 (30 ~ 39)":
                    System.arraycopy(tmpStatistics, 80, tmpStatistics_divide, 0, 30);
                    setChartPartData(imageN, contentKey, getView(), ageRangePlus(tmpStatistics_divide));
                    break;
                case "30대 초반 (30 ~ 32)":
                    System.arraycopy(tmpStatistics, 80, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "30대 중반 (33 ~ 36)":
                    System.arraycopy(tmpStatistics, 90, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "30대 후반 (37 ~ 39)":
                    System.arraycopy(tmpStatistics, 100, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "40대 전체 (40 ~ 49)":
                    System.arraycopy(tmpStatistics, 110, tmpStatistics_divide, 0, 30);
                    setChartPartData(imageN, contentKey, getView(), ageRangePlus(tmpStatistics_divide));
                    break;
                case "40대 초반 (40 ~ 42)":
                    System.arraycopy(tmpStatistics, 110, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "40대 중반 (43 ~ 46)":
                    System.arraycopy(tmpStatistics, 120, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "40대 후반 (47 ~ 40)":
                    System.arraycopy(tmpStatistics, 130, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "50대 전체 (50 ~ 59)":
                    System.arraycopy(tmpStatistics, 140, tmpStatistics_divide, 0, 30);
                    setChartPartData(imageN, contentKey, getView(), ageRangePlus(tmpStatistics_divide));
                    break;
                case "50대 초반 (50 ~ 52)":
                    System.arraycopy(tmpStatistics, 140, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "50대 중반 (53 ~ 56)":
                    System.arraycopy(tmpStatistics, 150, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "50대 후반 (57 ~ 59)":
                    System.arraycopy(tmpStatistics, 160, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "60대 전체 (60 ~ 69)":
                    System.arraycopy(tmpStatistics, 170, tmpStatistics_divide, 0, 30);
                    setChartPartData(imageN, contentKey, getView(), ageRangePlus(tmpStatistics_divide));
                    break;
                case "60대 초반 (60 ~ 62)":
                    System.arraycopy(tmpStatistics, 170, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "60대 중반 (63 ~ 66)":
                    System.arraycopy(tmpStatistics, 180, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "60대 후반 (67 ~ 69)":
                    System.arraycopy(tmpStatistics, 190, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "70대 전체 (70 ~ 79)":
                    System.arraycopy(tmpStatistics, 200, tmpStatistics_divide, 0, 30);
                    setChartPartData(imageN, contentKey, getView(), ageRangePlus(tmpStatistics_divide));
                    break;
                case "70대 초반 (70 ~ 72)":
                    System.arraycopy(tmpStatistics, 200, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "70대 중반 (73 ~ 76)":
                    System.arraycopy(tmpStatistics, 210, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "70대 후반 (77 ~ 79)":
                    System.arraycopy(tmpStatistics, 220, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "80대 전체 (80 ~ 89)":
                    System.arraycopy(tmpStatistics, 230, tmpStatistics_divide, 0, 30);
                    setChartPartData(imageN, contentKey, getView(), ageRangePlus(tmpStatistics_divide));
                    break;
                case "80대 초반 (80 ~ 82)":
                    System.arraycopy(tmpStatistics, 230, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "80대 중반 (83 ~ 86)":
                    System.arraycopy(tmpStatistics, 240, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "80대 후반 (87 ~ 89)":
                    System.arraycopy(tmpStatistics, 250, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "90대 전체 (80 ~ 99)":
                    System.arraycopy(tmpStatistics, 260, tmpStatistics_divide, 0, 30);
                    setChartPartData(imageN, contentKey, getView(), ageRangePlus(tmpStatistics_divide));
                    break;
                case "90대 초반 (90 ~ 92)":
                    System.arraycopy(tmpStatistics, 260, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "90대 중반 (93 ~ 96)":
                    System.arraycopy(tmpStatistics, 270, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;
                case "90대 후반 (97 ~ 99)":
                    System.arraycopy(tmpStatistics, 280, tmpStatistics_divide, 0, 10);
                    setChartPartData(imageN, contentKey, getView(), tmpStatistics_divide);
                    break;


            }

        }
    }

    private int[] ageRangePlus(int[] tmpStatistics_divide) {
        int result[] = new int[10];
        for (int i = 0; i < 10; i++) {
            result[i] = tmpStatistics_divide[i] + tmpStatistics_divide[i+10] + tmpStatistics_divide[i+20];
        }
        return result;
    }




    public class ResultValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        public ResultValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");


        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }





    public class CategoryBarChartXaxisFormatter implements IAxisValueFormatter {
        ArrayList<String> mValues;

        public CategoryBarChartXaxisFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            int val = (int) value;
            String label = "";
            if (val >= 0 && val < mValues.size()) {
                label = mValues.get(val);
            } else {
                label = "";
            }

            return label;
        }
    }


}
