package com.example.studentdatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText rollno, name, branch, marks, percentage, resultRollNo;
    Button btnInsert, btnShow, btnShowAll;
    TextView resultTv;
    StringBuilder str;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInsert = findViewById(R.id.btnInsert);
        btnShow = findViewById(R.id.btnShow);
        btnShowAll = findViewById(R.id.btnShowAll);
        rollno = findViewById(R.id.rollNo);
        name = findViewById(R.id.name);
        branch = findViewById(R.id.branch);
        marks = findViewById(R.id.marks);
        percentage = findViewById(R.id.percentage);
        resultRollNo = findViewById(R.id.resultRollNo);
        resultTv = findViewById(R.id.resultTv);
        str = new StringBuilder();
        try {
            db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
            String createTable = "create table student ( rollno integer, name varchar, branch varchar, marks integer, percentage varchar );";
            db.execSQL(createTable);
        }
        catch(Exception e) {
            Toast.makeText(this, "Failed to Create", Toast.LENGTH_SHORT).show();
        }
        btnInsert.setOnClickListener(view -> {
            try {
                ContentValues values = new ContentValues();
                values.put("rollno", rollno.getText().toString());
                values.put("name", name.getText().toString());
                values.put("branch", branch.getText().toString());
                values.put("marks", marks.getText().toString());
                values.put("percentage", percentage.getText().toString());
                if(db.insert("student", null, values) != -1) {
                    Toast.makeText(this, "Inseted Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Failed to Inserted", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e) {
                Toast.makeText(this, "Failed to Insert", Toast.LENGTH_SHORT).show();
            }
        });
        btnShow.setOnClickListener(view -> {
            Cursor c = db.rawQuery("select * from student where rollno = " + Integer.parseInt(resultRollNo.getText().toString()), null);
            c.moveToFirst();
            while(!c.isAfterLast()) {
                resultTv.setText("Roll Number: " + c.getString(0) + "\nName: " + c.getString(1) + "\nBranch: " + c.getString(2) + "\nMarks: " + c.getString(3) + "\nPercentage: " + c.getString(4));
                c.moveToNext();
            }
            c.close();
        });
        btnShowAll.setOnClickListener(view -> {
            Cursor c = db.rawQuery("select * from student", null);
            c.moveToFirst();
            while(!c.isAfterLast()) {
                str.append("Roll Number: " + c.getString(0) + "\nName: " + c.getString(1) + "\nBranch: " + c.getString(2) + "\nMarks: " + c.getString(3) + "\nPercentage: " + c.getString(4) + "\n\n");
                c.moveToNext();
            }
            resultTv.setText(str.toString());
            c.close();
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        db.close();
    }
}