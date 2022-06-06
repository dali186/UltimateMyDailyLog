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
    EditText editTitle, editDate, editPlace, editDetail;
    int iItem = -1;
    int iID = 0;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editTitle = findViewById(R.id.editTextName);
        editDate = findViewById(R.id.editTextDate);
        editPlace = findViewById(R.id.editTextPlace);
        editDetail = findViewById(R.id.editTextDetail);

        dbHelper = new DBHelper(this);

        Intent intentR = getIntent();
        if(intentR != null){
            iItem = intentR.getIntExtra("item", -1);
            iID = intentR.getIntExtra("id",0);
            if(iItem != -1){
                editTitle.setText(intentR.getStringExtra("schedule"));
                editDate.setText(intentR.getStringExtra("date"));
                editPlace.setText(intentR.getStringExtra("place"));
                editDetail.setText(intentR.getStringExtra("detail"));

            }
        }
        Button btnCancel = findViewById(R.id.buttonCancel);
        Button btnSave = findViewById(R.id.buttonSave);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sName = editTitle.getText().toString().trim();
                String sDate = editDate.getText().toString().trim();
                String sPlace = editPlace.getText().toString().trim();
                String sDetail = editDetail.getText().toString().trim();

                if(sName.isEmpty() || sDate.isEmpty() || sPlace.isEmpty()){
                    Toast.makeText(getApplicationContext(), "항목을 모두 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                //static final String SQL_SELECT_ID = "SELECT ID FROM "  + TABLE_NAME + " WHERE " + COL_TITLE + "=? and " + COL_WHEN + "=?";
                Cursor cursor = db.rawQuery(DBContract.SQL_SELECT_ID, new String[] {sName, sDate});
                if(cursor.getCount() != 0){
                    cursor.moveToNext();
                    if(iItem == -1) {    //추가하기 위해 이 액티비티로 이돟한 경우
                        Toast.makeText(getApplicationContext(), "중복되었습니다.", Toast.LENGTH_LONG).show();
                        return;
                        //수정하기 위해 record로 보낸 ID값과 반환된 ID값이 같으면 중복 X 같지않으면 중복
                    }else if(iID != cursor.getInt(0)){  //수정하기 위해 이동한 경우
                        Toast.makeText(getApplicationContext(), "중복되었습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("schedule", sName);
                intent.putExtra("date", sDate);
                intent.putExtra("place", sPlace);
                intent.putExtra("detail", sDetail);
                intent.putExtra("item", iItem);
                intent.putExtra("id",iID);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }//oncreate
}