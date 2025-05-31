package com.example.tallybook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tallybook.db.DatabaseHelper;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView tvIncome, tvExpense;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTodayStats();
    }

    private void initViews() {
        tvIncome = findViewById(R.id.tvIncome);
        tvExpense = findViewById(R.id.tvExpense);
        MaterialButton btnAddRecord = findViewById(R.id.btnAddRecord);
        MaterialButton btnViewRecords = findViewById(R.id.btnViewRecords);
        MaterialButton btnStatistics = findViewById(R.id.btnStatistics);
    }

    private void setListeners() {
        findViewById(R.id.btnAddRecord).setOnClickListener(v -> 
            startActivity(new Intent(this, AddRecordActivity.class)));

        findViewById(R.id.btnViewRecords).setOnClickListener(v -> 
            startActivity(new Intent(this, RecordListActivity.class)));

        findViewById(R.id.btnStatistics).setOnClickListener(v -> 
            startActivity(new Intent(this, StatisticsActivity.class)));
    }

    private void updateTodayStats() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 计算今日收入
        Cursor incomeCursor = db.rawQuery(
            "SELECT SUM(" + DatabaseHelper.COLUMN_AMOUNT + ") FROM " + DatabaseHelper.TABLE_RECORD +
            " WHERE " + DatabaseHelper.COLUMN_TYPE + "=? AND " + DatabaseHelper.COLUMN_DATE + " LIKE ?",
            new String[]{"收入", today + "%"}
        );

        // 计算今日支出
        Cursor expenseCursor = db.rawQuery(
            "SELECT SUM(" + DatabaseHelper.COLUMN_AMOUNT + ") FROM " + DatabaseHelper.TABLE_RECORD +
            " WHERE " + DatabaseHelper.COLUMN_TYPE + "=? AND " + DatabaseHelper.COLUMN_DATE + " LIKE ?",
            new String[]{"支出", today + "%"}
        );

        if (incomeCursor.moveToFirst()) {
            double income = incomeCursor.getDouble(0);
            tvIncome.setText(String.format(Locale.getDefault(), "¥%.2f", income));
        }

        if (expenseCursor.moveToFirst()) {
            double expense = expenseCursor.getDouble(0);
            tvExpense.setText(String.format(Locale.getDefault(), "¥%.2f", expense));
        }

        incomeCursor.close();
        expenseCursor.close();
    }
}