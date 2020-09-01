package com.example.medicinereminder;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class PatientList extends AppCompatActivity {

    Button buttonBack;
    ListView patientList;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> patientListArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        buttonBack = findViewById(R.id.buttonBack);
        patientList = findViewById(R.id.patientList);


        patientListArray = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patientListArray);


        SharedPreferences preferences;
        preferences = getSharedPreferences("pref", MODE_PRIVATE);


        int size = preferences.getInt("size", 0);

        for (int j = 0; j < size; j++) {
            patientListArray.add(preferences.getString("value" + j, null));
        }


        String date = preferences.getString("Date", "");
        String name = preferences.getString("Name", "");
        String edit1 = preferences.getString("edit1", "");
        String edit2 = preferences.getString("edit2", "");
        String edit3 = preferences.getString("edit3", "");


        arrayAdapter.add(date + "\n" + name + "\n" + edit1 + " - " + edit2 + " - " + edit3);
        Collections.sort(patientListArray);
        patientList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);

                startActivity(i);
            }
        });


    }


}
