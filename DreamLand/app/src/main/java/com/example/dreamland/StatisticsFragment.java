package com.example.dreamland;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreamland.database.AppDatabase;
import com.example.dreamland.database.Sleep;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StatisticsFragment extends Fragment {

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");

    private AppDatabase db;
    LineChart lineChart; // 취침 시간
    LineChart lineChart2; // 기상 시간
    LineChart lineChart3; // 잠들기까지 걸린 시간
    LineChart lineChart4; // 수면 시간
    LineChart lineChart5; // 코골이, 무호흡 시간
    BarChart barChart; // 자세 교정
    BarChart barChart2; // 수면 만족도
    BarChart barChart3; // 산소 포화도

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getDatabase(getContext());

        // 취침시간 차트
        lineChart = view.findViewById(R.id.lineChart);
        final XAxis xAxis = lineChart.getXAxis(); // X축
        final YAxis yAxis = lineChart.getAxisLeft(); // Y축
        setLineChartOptions(lineChart, xAxis, yAxis);

        final ArrayList<String> xLabels = new ArrayList<>();
        final String[] yLabels = {
                "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00",
                "22:30", "23:00", "23:30", "00:00", "00:30", "01:00", "01:30", "02:00", "02:30",
                "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00", "06:30", "07:00",
                "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00",
                "16:30", "17:00", "17:30"
        };
        final ArrayList<Entry> entries = new ArrayList<>();

        // 기상시간 차트
        lineChart2 = view.findViewById(R.id.lineChart2);
        final XAxis xAxis2 = lineChart2.getXAxis(); // X축
        final YAxis yAxis2 = lineChart2.getAxisLeft(); // Y축
        setLineChartOptions(lineChart2, xAxis2, yAxis2);

        final String[] yLabels2 = {
                "00:00", "00:30", "01:00", "01:30", "02:00", "02:30",
                "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00", "06:30", "07:00",
                "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00",
                "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30",
                "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"
        };
        final ArrayList<Entry> entries2 = new ArrayList<>();

        // 잠들끼까지 걸린 시간 차트
        lineChart3 = view.findViewById(R.id.lineChart3);
        final XAxis xAxis3 = lineChart3.getXAxis(); // X축
        final YAxis yAxis3 = lineChart3.getAxisLeft(); // Y축
        setLineChartOptions(lineChart3, xAxis3, yAxis3);

        final String[] yLabels3 = {
                "0분", "5분", "10분", "15분", "20분", "25분", "30분", "35분", "40분", "45분", "50분", "55분",
                "1시간", "1시간 5분", "1시간 10분", "1시간 15분", "1시간 20분", "1시간 25분", "1시간 30분",
                "1시간 35분", "1시간 40분", "1시간 45분", "1시간 50분", "2시간"
        };
        final ArrayList<Entry> entries3 = new ArrayList<>();

        // 수면 시간 차트
        lineChart4 = view.findViewById(R.id.lineChart4);
        final XAxis xAxis4 = lineChart4.getXAxis(); // X축
        final YAxis yAxis4 = lineChart4.getAxisLeft(); // Y축
        setLineChartOptions(lineChart4, xAxis4, yAxis4);

        final String[] yLabels4 = {
                "0분", "30분", "1시간", "1시간 30분", "2시간", "2시간 30분", "3시간", "3시간 30분",
                "4시간", "4시간 30분", "5시간", "5시간 30분", "6시간", "6시간 30분", "7시간", "7시간 30분",
                "8시간", "8시간 30분", "9시간", "9시간 30분", "10시간", "10시간 30분", "11시간", "11시간 30분",
                "12시간", "12시간 30분", "13시간", "13시간 30분", "14시간", "14시간 30분", "15시간"
        };
        final ArrayList<Entry> entries4 = new ArrayList<>();

        // 코골이, 무호흡 시간 차트
        lineChart5 = view.findViewById(R.id.lineChart5);
        final XAxis xAxis5 = lineChart5.getXAxis(); // X축
        final YAxis yAxis5 = lineChart5.getAxisLeft(); // Y축
        setLineChartOptions(lineChart5, xAxis5, yAxis5);
        final ArrayList<Entry> entries5 = new ArrayList<>();

        // 자세 교정 차트
        barChart = view.findViewById(R.id.barChart);
        final XAxis xAxis6 = barChart.getXAxis(); // X축
        final YAxis yAxis6 = barChart.getAxisLeft(); // Y축
        setBarChartOptions(barChart, xAxis6, yAxis6);
        final ArrayList<BarEntry> entries6 = new ArrayList<>();

        // 수면 데이터 변경시
        db.sleepDao().getRecentSleeps().observe(this, new Observer<List<Sleep>>() {
            @Override
            public void onChanged(List<Sleep> sleeps) {
                entries.clear();
                entries2.clear();
                entries3.clear();
                entries4.clear();
                entries5.clear();
                entries6.clear();
                if (!sleeps.isEmpty()) {
                    for (int i = 0; i < sleeps.size(); i++) {
                        int index = (sleeps.size() - 1) - i;
                        Sleep sleep = sleeps.get(index);
                        try {
                            Date date = format2.parse(sleep.getSleepDate());

                            xLabels.add(sdf2.format(date));


                            entries.add(new Entry(i, timeToFloat(
                                    sleep.getWhenSleep(), 18)));
                            entries2.add(new Entry(i, timeToFloat(
                                    sleep.getWhenWake(), 0)));

                            // 잠들기까지 시간
                            long diffTime = sdf.parse(sleep.getWhenSleep()).getTime()
                                    - sdf.parse(sleep.getWhenStart()).getTime();

                            entries3.add(new Entry(i, timeToFloatForMinute(
                                    sdf.format(diffTime), 5)));
                            entries4.add(new Entry(i, timeToFloatForMinute(
                                    sleep.getSleepTime(), 30)));
                            entries5.add(new Entry(i, timeToFloatForMinute(
                                    sleep.getConTime(), 5)));
                            entries6.add(new BarEntry(i, sleep.getAdjCount()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // x, y 라벨 값 반환
                xAxis.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return xLabels.get((int) value);
                    }
                });

                yAxis.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return yLabels[(int) value];
                    }
                });

                xAxis2.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return xLabels.get((int) value);
                    }
                });

                yAxis2.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return yLabels2[(int) value];
                    }
                });

                xAxis3.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return xLabels.get((int) value);
                    }
                });

                yAxis3.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return yLabels3[(int) value];
                    }
                });

                xAxis4.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return xLabels.get((int) value);
                    }
                });

                yAxis4.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return yLabels4[(int) value];
                    }
                });

                xAxis5.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return xLabels.get((int) value);
                    }
                });

                yAxis5.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return yLabels3[(int) value];
                    }
                });

                xAxis6.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return xLabels.get((int) value);
                    }
                });

                yAxis6.setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return (int) value + "번";
                    }
                });

                // 데이터 셋 설정
                LineDataSet dataSet = new LineDataSet(entries, "Label");
                setLineDataSetOptions(dataSet);
                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                lineChart.invalidate(); // refresh

                LineDataSet dataSet2 = new LineDataSet(entries2, "Label");
                setLineDataSetOptions(dataSet2);
                LineData lineData2 = new LineData(dataSet2);
                lineChart2.setData(lineData2);
                lineChart2.invalidate(); // refresh

                LineDataSet dataSet3 = new LineDataSet(entries3, "Label");
                setLineDataSetOptions(dataSet3);
                LineData lineData3 = new LineData(dataSet3);
                lineChart3.setData(lineData3);
                lineChart3.invalidate(); // refresh

                LineDataSet dataSet4 = new LineDataSet(entries4, "Label");
                setLineDataSetOptions(dataSet4);
                LineData lineData4 = new LineData(dataSet4);
                lineChart4.setData(lineData4);
                lineChart4.invalidate(); // refresh

                LineDataSet dataSet5 = new LineDataSet(entries5, "Label");
                setLineDataSetOptions(dataSet5);
                LineData lineData5 = new LineData(dataSet5);
                lineChart5.setData(lineData5);
                lineChart5.invalidate(); // refresh

                BarDataSet barDataSet = new BarDataSet(entries6, "Label");
                setBarDataSetOptions(barDataSet);
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                barChart.invalidate();
            }
        });
    }

    // 라인 차트의 기본 옵션 세팅
    private void setLineChartOptions(LineChart lineChart, XAxis xAxis, YAxis yAxis) {
        lineChart.getAxisRight().setEnabled(false); // 오른쪽 라인 사용 안함
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 아래로
        xAxis.setTextColor(Color.GRAY);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextSize(12.0f);
        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);

        yAxis.setTextColor(Color.GRAY);
        yAxis.setGranularity(1f);
        yAxis.setTextSize(12.0f);
        yAxis.setXOffset(15.0f);
    }

    // 바 차트 기본 옵션 세팅
    private void setBarChartOptions(BarChart barChart, XAxis xAxis, YAxis yAxis) {
        barChart.getAxisRight().setEnabled(false); // 오른쪽 라인 사용 안함
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setPinchZoom(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setHighlightPerDragEnabled(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 아래로
        xAxis.setTextColor(Color.GRAY);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextSize(12.0f);
        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);

        yAxis.setTextColor(Color.GRAY);
        yAxis.setGranularity(1f);
        yAxis.setTextSize(12.0f);
        yAxis.setXOffset(15.0f);
        yAxis.setAxisMinimum(0.0f);
    }

    // line data set 옵션 세팅
    private void setLineDataSetOptions(LineDataSet dataSet) {
        dataSet.setDrawFilled(true); // 아래쪽 채우기
        dataSet.setLineWidth(4.0f);
        dataSet.setCircleRadius(5.0f);
        dataSet.setCircleHoleRadius(3.0f);
        dataSet.setValueTextSize(0.0f);
    }

    // bar data set 옵션 세팅
    private void setBarDataSetOptions(BarDataSet dataSet) {
        dataSet.setValueTextSize(0.0f);
        dataSet.setBarBorderWidth(2f);
    }

    // 입력된 시간을 차트의 y좌표 값으로 변환
    private float timeToFloat(String time, int startHour) {
        try {
            Date date = sdf.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            long millis = cal.getTimeInMillis(); // 입력된 날짜의 millis

            // 시간을 당일 18시로 설정
            cal.set(Calendar.HOUR_OF_DAY, startHour);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long diff = millis - cal.getTimeInMillis(); // 입력된 날과 시작시의 차

            if (diff < 0) { // 음수이면 하루를 더함
                diff += (1000 * 60 * 60 * 24);
            }
            return (float) diff / (1000 * 60 * 30); // 30분 간격으로 나눈 값
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0f;
    }

    // 시간을 y좌표로 변환
    private float timeToFloatForMinute(String time, int minute) {
        try {
            Date date = sdf.parse(time);

            long millis = date.getTime();
            if (millis < 0) {
                millis += (1000 * 60 * 60 * 9);
            }
            return (float) millis / (1000 * 60 * minute); // 입력시간 간격으로 나눈 값
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0f;
    }


}
