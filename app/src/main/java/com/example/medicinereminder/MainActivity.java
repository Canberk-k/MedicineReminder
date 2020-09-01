package com.example.medicinereminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    ListView patientList;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> patientListArray;
    EditText nameString;
    EditText edit1;
    EditText edit2;
    EditText edit3;
    DatePicker datePicker;
    Calendar calendar;
    Button buttonAdd;
    Button buttonShow;
    String date;

    TimePickerDialog timePickerDialog;
    final static int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        datePicker = findViewById(R.id.datePicker);

        calendar = Calendar.getInstance();

        nameString = findViewById(R.id.nameString);
        edit1 = findViewById(R.id.edit1);
        edit2 = findViewById(R.id.edit2);
        edit3 = findViewById(R.id.edit3);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonShow = findViewById(R.id.buttonShow);


        patientList = findViewById(R.id.patientList);

        patientListArray = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, patientListArray);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.YEAR, datePicker.getYear());


                int day = datePicker.getDayOfMonth();
                int month = (datePicker.getMonth() + 1);
                int year = datePicker.getYear();

                String dayString = String.valueOf(day);
                String monthString = String.valueOf(month);
                String yearString = String.valueOf(year);

                String nameString1 = String.valueOf(nameString.getText().toString().trim());
                String edit1String = String.valueOf(edit1.getText().toString().trim());
                String edit2String = String.valueOf(edit2.getText().toString().trim());
                String edit3String = String.valueOf(edit3.getText().toString().trim());

                date = (dayString + "/" + monthString + "/" + yearString);


                arrayAdapter.add(date + "\n" + nameString1 + "\n" + edit1String + " - " + edit2String + " - " + edit3String);


                patientList.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();


                SharedPreferences preferences;
                SharedPreferences.Editor editor;
                preferences = getSharedPreferences("pref", MODE_PRIVATE);


                editor = preferences.edit();

                int k = patientListArray.size();

                for (int i = 0; i < k; i++) {
                    editor.putString("value" + i, patientListArray.get(i));
                }
                editor.putInt("size", patientListArray.size());
                editor.apply();


                editor.putString("Date", date);
                editor.putString("Name", nameString1);
                editor.putString("edit1", edit1String);
                editor.putString("edit2", edit2String);
                editor.putString("edit3", edit3String);

                editor.apply();

                openPickerDialog(false);


            }
        });

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, PatientList.class);

                startActivity(i);

            }
        });


    }

    private void openPickerDialog(boolean is24hour) {


        timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                onTimeSetListener,

                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24hour);
        timePickerDialog.setTitle("Hasta Listesi");

        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {

                calSet.add(Calendar.DATE, 1);
            }

            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar alarmCalender) {


        Toast.makeText(getApplicationContext(), "Alarm Ayarlandı!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), pendingIntent);

    }


}