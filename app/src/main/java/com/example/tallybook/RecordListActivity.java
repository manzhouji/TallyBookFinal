package com.example.tallybook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallybook.db.DatabaseHelper;
import com.example.tallybook.model.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AutoCompleteTextView spinnerFilter;
    private DatabaseHelper dbHelper;
    private RecordAdapter adapter;
    private List<Record> records;

    private static final String[] FILTER_OPTIONS = new String[]{"全部", "收入", "支出"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        dbHelper = new DatabaseHelper(this);
        records = new ArrayList<>();

        initViews();
        setupFilter();
        loadRecords("全部");
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordAdapter(records);
        recyclerView.setAdapter(adapter);
    }

    private void setupFilter() {
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_dropdown_item_1line, FILTER_OPTIONS);
        spinnerFilter.setAdapter(filterAdapter);
        spinnerFilter.setText(FILTER_OPTIONS[0], false);

        spinnerFilter.setOnItemClickListener((parent, view, position, id) -> {
            String filter = FILTER_OPTIONS[position];
            loadRecords(filter);
        });
    }

    private void loadRecords(String filter) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = null;
        String[] selectionArgs = null;

        if (!filter.equals("全部")) {
            selection = DatabaseHelper.COLUMN_TYPE + " = ?";
            selectionArgs = new String[]{filter};
        }

        Cursor cursor = db.query(
            DatabaseHelper.TABLE_RECORD,
            null,
            selection,
            selectionArgs,
            null,
            null,
            DatabaseHelper.COLUMN_DATE + " DESC"
        );

        records.clear();
        while (cursor.moveToNext()) {
            Record record = new Record();
            record.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
            record.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT)));
            record.setType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE)));
            record.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY)));
            record.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)));
            record.setRemark(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REMARK)));
            records.add(record);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private static class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
        private final List<Record> records;

        RecordAdapter(List<Record> records) {
            this.records = records;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Record record = records.get(position);
            holder.tvCategory.setText(record.getCategory());
            holder.tvDate.setText(record.getDate());
            holder.tvRemark.setText(record.getRemark());
            
            String amountText = String.format("¥%.2f", record.getAmount());
            if (record.getType().equals("支出")) {
                amountText = "-" + amountText;
                holder.tvAmount.setTextColor(holder.itemView.getContext()
                    .getColor(android.R.color.holo_red_dark));
            } else {
                amountText = "+" + amountText;
                holder.tvAmount.setTextColor(holder.itemView.getContext()
                    .getColor(android.R.color.holo_green_dark));
            }
            holder.tvAmount.setText(amountText);
        }

        @Override
        public int getItemCount() {
            return records.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvCategory, tvDate, tvRemark, tvAmount;

            ViewHolder(View view) {
                super(view);
                tvCategory = view.findViewById(R.id.tvCategory);
                tvDate = view.findViewById(R.id.tvDate);
                tvRemark = view.findViewById(R.id.tvRemark);
                tvAmount = view.findViewById(R.id.tvAmount);
            }
        }
    }
} 