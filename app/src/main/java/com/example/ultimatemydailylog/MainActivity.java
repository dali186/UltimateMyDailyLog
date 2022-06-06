package com.example.ultimatemydailylog;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ultimatemydailylog.DBHelper;
import com.example.ultimatemydailylog.R;
import com.example.ultimatemydailylog.DBContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<HashMap<String, String>> listData;
    SimpleAdapter simpleAdapter;
    int iSelectedItem = -1; //listview에서의 index값

    int iSelectedID = 0;    //record에 id값
    int iMaxID = 0;         //iMaxID + 1

    DBHelper dbHelper;
    SQLiteDatabase database;


    ActivityResultContract<Intent, ActivityResult> contract = new ActivityResultContracts.StartActivityForResult();
    ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                int iItem = intent.getIntExtra("item", -1);
                int iID = intent.getIntExtra("id", 0);
                HashMap<String, String> hitem = new HashMap<>();
                hitem.put("schedule", intent.getStringExtra("schedule"));
                hitem.put("date", intent.getStringExtra("date"));
                hitem.put("place", intent.getStringExtra("place"));
                hitem.put("detail", intent.getStringExtra("detail"));
                ContentValues values = new ContentValues();
                values.put("schedule", intent.getStringExtra("schedule"));
                values.put("date", intent.getStringExtra("date"));
                values.put(DBContract.COL_WHERE, intent.getStringExtra("place"));
                values.put(DBContract.COL_DETAIL, intent.getStringExtra("detail"));

                if (iItem == -1) {//add
                    iMaxID++;
                    hitem.put("id", String.valueOf(iMaxID));
                    values.put("id", String.valueOf(iMaxID));
                    listData.add(hitem);
                    database = dbHelper.getWritableDatabase();
                    database.insert(DBContract.TABLE_NAME, null, values);
                } else {
                    hitem.put("id", String.valueOf(iID));
                    listData.set(iItem, hitem);
                    database = dbHelper.getWritableDatabase();
                    database.update(DBContract.TABLE_NAME, values, "id=?",
                            new String[]{String.valueOf(iID)});
                }
                simpleAdapter.notifyDataSetChanged();
            } else
                Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
        }
    };
    ActivityResultLauncher<Intent> launcher;

    private void loadTable() {
        listData.clear();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(DBContract.SQL_LOAD, null);
        while (cursor.moveToNext()) {
            int nID = cursor.getInt(0);
            HashMap<String, String> hitem = new HashMap<>();
            hitem.put("schedule", cursor.getString(1));
            hitem.put("date", cursor.getString(2));
            hitem.put("place", cursor.getString(3));
            hitem.put("detail", cursor.getString(4));
            hitem.put("id", String.valueOf(nID));
            listData.add(hitem);
            iMaxID = Math.max(iMaxID, nID);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        listData = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(this, listData, R.layout.simple_list_item_activated_3,
                new String[]{"schedule", "date", "place"}, new int[]{R.id.text1, R.id.text2, R.id.text3});
        listView.setAdapter(simpleAdapter);

        dbHelper = new DBHelper(this);
        loadTable();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iSelectedItem = position;
                HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
                String sID = hitem.get("id");
                Toast.makeText(getApplicationContext(), sID, Toast.LENGTH_SHORT).show();
                iSelectedID = Integer.parseInt(hitem.get("id"));
            }
        });

        Button btnInfo = findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iSelectedItem == -1) {
                    Toast.makeText(getApplicationContext(), "선택한 항목이 없습니다.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
                    //dialog
                    String sDetail = hitem.get("detail");
                    Toast.makeText(getApplicationContext(), sDetail, Toast.LENGTH_SHORT).show();
                }
            }
        });

        launcher = registerForActivityResult(contract, callback);
    }//oncreate

    public void onClickAdd(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("item", -1);
        intent.putExtra("id", 0);
        launcher.launch(intent);
    }

    public void onClickEdit(View view) {
        if (iSelectedItem == -1) {
            Toast.makeText(this, "선택한 항목이 없습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("item", iSelectedItem);
        intent.putExtra("id", iSelectedID);
        HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
        intent.putExtra("schedule", hitem.get("schedule"));
        intent.putExtra("date", hitem.get("date"));
        intent.putExtra("place", hitem.get("place"));
        intent.putExtra("detail", hitem.get("detail"));
        launcher.launch(intent);
    }

    public void onClickdelete(View view) {
        if (iSelectedItem == -1) {
            Toast.makeText(this, "선택한 항목이 없습니다.", Toast.LENGTH_LONG).show();
            return;
        }else {
            database = dbHelper.getReadableDatabase();
            String SQL_DELETE = "DELETE FROM SCHEDULE_A WHERE ID=" + iSelectedID;
            database.execSQL(SQL_DELETE);
            database.close();

            listData.remove(iSelectedItem);
            simpleAdapter.notifyDataSetChanged();

        }
    }
}