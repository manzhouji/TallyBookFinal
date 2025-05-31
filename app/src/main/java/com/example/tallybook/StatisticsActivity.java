package com.example.tallybook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallybook.db.DatabaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {
    private PieChart pieChart;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private StatisticsAdapter adapter;
    private List<StatisticsItem> statisticsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new DatabaseHelper(this);
        statisticsItems = new ArrayList<>();

        initViews();
        setupPieChart();
        setupTabLayout();
        loadDailyStatistics(); // 默认显示日统计
    }

    private void initViews() {
        pieChart = findViewById(R.id.pieChart);
        recyclerView = findViewById(R.id.recyclerView);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StatisticsAdapter(statisticsItems);
        recyclerView.setAdapter(adapter);
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
    }

    private void setupTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loadDailyStatistics();
                } else {
                    loadMonthlyStatistics();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadDailyStatistics() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().getTime());
        loadStatistics(today + "%");
    }

    private void loadMonthlyStatistics() {
        String month = new SimpleDateFormat("yyyy-MM", Locale.getDefault())
            .format(Calendar.getInstance().getTime());
        loadStatistics(month + "%");
    }

    private void loadStatistics(String datePattern) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, Double> categorySum = new HashMap<>();
        double totalExpense = 0;

        // 查询支出数据
        Cursor cursor = db.rawQuery(
            "SELECT " + DatabaseHelper.COLUMN_CATEGORY + ", SUM(" + DatabaseHelper.COLUMN_AMOUNT + ") " +
            "FROM " + DatabaseHelper.TABLE_RECORD + " WHERE " + DatabaseHelper.COLUMN_TYPE + "=? " +
            "AND " + DatabaseHelper.COLUMN_DATE + " LIKE ? GROUP BY " + DatabaseHelper.COLUMN_CATEGORY,
            new String[]{"支出", datePattern}
        );

        while (cursor.moveToNext()) {
            String category = cursor.getString(0);
            double amount = cursor.getDouble(1);
            categorySum.put(category, amount);
            totalExpense += amount;
        }
        cursor.close();

        // 更新饼图
        List<PieEntry> entries = new ArrayList<>();
        statisticsItems.clear();

        for (Map.Entry<String, Double> entry : categorySum.entrySet()) {
            float percentage = (float) (entry.getValue() / totalExpense * 100);
            entries.add(new PieEntry(percentage, entry.getKey()));
            
            StatisticsItem item = new StatisticsItem();
            item.category = entry.getKey();
            item.amount = entry.getValue();
            item.percentage = percentage;
            statisticsItems.add(item);
        }

        PieDataSet dataSet = new PieDataSet(entries, "支出类别");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(11f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setData(data);
        pieChart.invalidate();

        adapter.notifyDataSetChanged();
    }

    private static class StatisticsItem {
        String category;
        double amount;
        float percentage;
    }

    private static class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {
        private final List<StatisticsItem> items;

        StatisticsAdapter(List<StatisticsItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            StatisticsItem item = items.get(position);
            holder.text1.setText(String.format("%s: ¥%.2f", item.category, item.amount));
            holder.text2.setText(String.format("占比: %.1f%%", item.percentage));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;

            ViewHolder(View view) {
                super(view);
                text1 = view.findViewById(android.R.id.text1);
                text2 = view.findViewById(android.R.id.text2);
            }
        }
    }
} 