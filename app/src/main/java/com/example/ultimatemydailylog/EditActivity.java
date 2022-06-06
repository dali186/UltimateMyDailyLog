package com.example.ultimatemydailylog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ultimatemydailylog.DBHelper;
import com.example.ultimatemydailylog.R;
import com.example.ultimatemydailylog.DBContract;

public class EditActivity extends AppCompatActivity {
    EditText editTitle, editDate, editDiet, editStrength, editMental, editMsg;
    int iItem = -1;
    int iID = 0;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.et_title);
        editDate = findViewById(R.id.et_date);
        editDiet = findViewById(R.id.et_diet);
        editStrength = findViewById(R.id.et_strength);
        editMental = findViewById(R.id.et_mental);
        editMsg = findViewById(R.id.et_msg);

        dbHelper = new DBHelper(this);

        Intent intentR = getIntent();
        if(intentR != null){
            iItem = intentR.getIntExtra("item", -1);
            iID = intentR.getIntExtra("id",0);
            if(iItem != -1){
                editTitle.setText(intentR.getStringExtra("diary"));
                editDate.setText(intentR.getStringExtra("date"));
                editDiet.setText(intentR.getStringExtra("diet"));
                editStrength.setText(intentR.getStringExtra("strength"));
                editMental.setText(intentR.getStringExtra("mental"));
                editMsg.setText(intentR.getStringExtra("msg"));

            }
        }
        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTitle = editTitle.getText().toString().trim();
                String sDate = editDate.getText().toString().trim();
                String sDiet = editDiet.getText().toString().trim();
                String sStrength = editStrength.getText().toString().trim();
                String sMental = editMental.getText().toString().trim();
                String sMsg = editMsg.getText().toString().trim();

                if(sTitle.isEmpty() || sDate.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Title과 Date를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery(DBContract.SQL_SELECT_ID, new String[] {sTitle, sDate});
                if(cursor.getCount() != 0){
                    cursor.moveToNext();
                    if(iItem == -1) {
                        Toast.makeText(getApplicationContext(), "중복되었습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }else if(iID != cursor.getInt(0)){
                        Toast.makeText(getApplicationContext(), "중복되었습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("diary", sTitle);
                intent.putExtra("date", sDate);
                intent.putExtra("diet", sDiet);
                intent.putExtra("strength", sStrength);
                intent.putExtra("mental", sMental);
                intent.putExtra("msg", sMsg);
                intent.putExtra("item", iItem);
                intent.putExtra("id",iID);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }//oncreate
}