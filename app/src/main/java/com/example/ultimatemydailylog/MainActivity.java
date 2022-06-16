package com.example.ultimatemydailylog;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView tv_setWeight, tv_setMass, tv_setFat, tv_setGweight, tv_setGmass, tv_setGfat;
    //DAIRY
    ListView listView;
    ArrayList<HashMap<String, String>> listData;
    SimpleAdapter simpleAdapter;
    int iSelectedItem = -1;
    int iSelectedID = 0;
    int iMaxID = 0;
    DBHelper dbHelper;
    SQLiteDatabase database;
    //TOOLBAR
    Toolbar toolbar;

    //DIARY INTENT
    ActivityResultContract<Intent, ActivityResult> contract = new ActivityResultContracts.StartActivityForResult();
    ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                int iItem = intent.getIntExtra("item", -1);
                int iID = intent.getIntExtra("id", 0);
                HashMap<String, String> hitem = new HashMap<>();
                hitem.put("diary", intent.getStringExtra("diary"));
                hitem.put("date", intent.getStringExtra("date"));
                hitem.put("diet", intent.getStringExtra("diet"));
                hitem.put("strength", intent.getStringExtra("strength"));
                hitem.put("mental", intent.getStringExtra("mental"));
                hitem.put("msg", intent.getStringExtra("msg"));
                ContentValues values = new ContentValues();
                values.put("diary", intent.getStringExtra("diary"));
                values.put("date", intent.getStringExtra("date"));
                values.put("diet", intent.getStringExtra("diet"));
                values.put("strength", intent.getStringExtra("strength"));
                values.put("mental", intent.getStringExtra("mental"));
                values.put("msg", intent.getStringExtra("msg"));

                if (iItem == -1) {
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
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
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
            hitem.put("diary", cursor.getString(1));
            hitem.put("date", cursor.getString(2));
            hitem.put("diet", cursor.getString(3));
            hitem.put("strength", cursor.getString(4));
            hitem.put("mental", cursor.getString(5));
            hitem.put("msg", cursor.getString(6));
            hitem.put("id", String.valueOf(nID));
            listData.add(hitem);
            iMaxID = Math.max(iMaxID, nID);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    //TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings1:
                //사용자 추가
                userInfo();
                return true;
            case R.id.action_settings2:
                //달력 보기
                calendarInfo();
                return true;
            case R.id.action_settings3:
                //목표 확인
                Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //목표 설정 메소드
        //SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        setInfo();

        //user
        /*
        userData = new ArrayList<>();
        userHelper = new UserHelper(this);

        ContentValues values = new ContentValues();
        values.put("name", "kim");
        values.put("sex", "M");
        values.put("height", "186");
        values.put("weight", "80");
        values.put("goal", "bulk");
        values.put("sdate", "20192610");
        values.put("id", 1);

        userDatabase = userHelper.getWritableDatabase();
        userDatabase.insert(UserContract.TABLE_NAME, null, values);
         */

        //toobar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn_add = findViewById(R.id.btn_add);
        Button btn_delete = findViewById(R.id.btn_delete);
        Button btn_modify = findViewById(R.id.btn_modify);
        //dairy
        listView = findViewById(R.id.listView);
        listData = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(this, listData, R.layout.simple_list_item_activated_3,
                new String[]{"date", "diary"}, new int[]{R.id.text1, R.id.text2});
        listView.setAdapter(simpleAdapter);

        dbHelper = new DBHelper(this);
        loadTable();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iSelectedItem = position;
                HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
                String sTitle = hitem.get("diary");
                String sDate = hitem.get("date");
                Toast.makeText(getApplicationContext(), sDate + ", " + sTitle, Toast.LENGTH_SHORT).show();
                iSelectedID = Integer.parseInt(hitem.get("id"));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
                String dTitle = hitem.get("diary");
                String dDate = hitem.get("date");
                String dDiet = hitem.get("diet");
                String dStrength = hitem.get("strength");
                String dMental = hitem.get("mental");
                String dMsg = hitem.get("msg");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("기록 확인 " + dDate);
                builder.setMessage(" 운동 종류 : " + dTitle
                        + "\n 식단 유무 : " + dDiet
                        + "\n 운동 강도 : " + dStrength
                        + "\n 의지 정도 : " + dMental
                        + "\n 짧은 메모 : " + dMsg);
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
    /*
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
     */

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("item", -1);
                intent.putExtra("id", 0);
                launcher.launch(intent);
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iSelectedItem == -1) {
                    Toast.makeText(getApplicationContext(), "선택한 항목이 없습니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("item", iSelectedItem);
                intent.putExtra("id", iSelectedID);
                HashMap<String, String> hitem = (HashMap<String, String>) simpleAdapter.getItem(iSelectedItem);
                intent.putExtra("diary", hitem.get("diary"));
                intent.putExtra("date", hitem.get("date"));
                intent.putExtra("diet", hitem.get("diet"));
                intent.putExtra("strength", hitem.get("strength"));
                intent.putExtra("mental", hitem.get("mental"));
                intent.putExtra("msg", hitem.get("msg"));
                launcher.launch(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iSelectedItem == -1) {
                    Toast.makeText(getApplicationContext(), "선택한 항목이 없습니다.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    database = dbHelper.getReadableDatabase();
                    String SQL_DELETE = "DELETE FROM DIARY_D WHERE ID=" + iSelectedID;
                    database.execSQL(SQL_DELETE);
                    database.close();

                    listData.remove(iSelectedItem);
                    simpleAdapter.notifyDataSetChanged();

                }
            }
        });

        launcher = registerForActivityResult(contract, callback);
    }//oncreate

    public void setInfo(){

        tv_setWeight = findViewById(R.id.tv_setWeight);
        tv_setMass = findViewById(R.id.tv_setMass);
        tv_setFat = findViewById(R.id.tv_setFat);
        tv_setGweight = findViewById(R.id.tv_setGweight);
        tv_setGmass = findViewById(R.id.tv_setGmass);
        tv_setGfat = findViewById(R.id.tv_setGfat);

        SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        //String userName = sharedPreferences.getString("setName", null);
        String userMass = sharedPreferences.getString("setMass", null);
        String userWeight = sharedPreferences.getString("setWeight", null);
        String userFat = sharedPreferences.getString("setFat", null);
        String userGmass = sharedPreferences.getString("setGmass", null);
        String userGweight = sharedPreferences.getString("setGweight", null);
        String userGfat = sharedPreferences.getString("setGfat", null);

        tv_setWeight.setText(userMass + " Kg");
        tv_setMass.setText(userWeight + " Kg");
        tv_setFat.setText(userFat + " %");
        tv_setGweight.setText(userGmass + " Kg");
        tv_setGmass.setText(userGweight + " Kg");
        tv_setGfat.setText(userGfat + " %");

    }

    public void userInfo() {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);
    }

    public void calendarInfo() {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);
    }

}