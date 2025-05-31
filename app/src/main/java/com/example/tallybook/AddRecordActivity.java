package com.example.tallybook;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tallybook.db.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddRecordActivity extends AppCompatActivity {
    private TextInputEditText etAmount, etDate, etRemark;
    private AutoCompleteTextView spinnerType, spinnerCategory;
    private DatabaseHelper dbHelper;
    private Calendar calendar;

    private static final String[] TYPES = new String[]{"支出", "收入"};
    private static final String[] CATEGORIES = new String[]{
        "餐饮", "交通", "购物", "娱乐", "医疗", "教育", "住房", "工资", "投资", "其他"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        dbHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        initViews();
        setupSpinners();
        setListeners();
    }

    private void initViews() {
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etRemark = findViewById(R.id.etRemark);
        spinnerType = findViewById(R.id.spinnerType);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        // 设置当前日期
        updateDateDisplay();
    }

    private void setupSpinners() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_dropdown_item_1line, TYPES);
        spinnerType.setAdapter(typeAdapter);
        spinnerType.setText(TYPES[0], false);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_dropdown_item_1line, CATEGORIES);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setText(CATEGORIES[0], false);
    }

    private void setListeners() {
        etDate.setOnClickListener(v -> showDatePicker());

        findViewById(R.id.btnSave).setOnClickListener(v -> saveRecord());
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateDisplay();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
           calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void saveRecord() {
        String amountStr = etAmount.getText().toString();
        String type = spinnerType.getText().toString();
        String category = spinnerCategory.getText().toString();
        String date = etDate.getText().toString();
        String remark = etRemark.getText().toString();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_AMOUNT, amount);
            values.put(DatabaseHelper.COLUMN_TYPE, type);
            values.put(DatabaseHelper.COLUMN_CATEGORY, category);
            values.put(DatabaseHelper.COLUMN_DATE, date);
            values.put(DatabaseHelper.COLUMN_REMARK, remark);

            long newRowId = db.insert(DatabaseHelper.TABLE_RECORD, null, values);

            if (newRowId != -1) {
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的金额", Toast.LENGTH_SHORT).show();
        }
    }
} 