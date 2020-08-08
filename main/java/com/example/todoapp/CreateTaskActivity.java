package com.example.todoapp;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class CreateTaskActivity extends AppCompatActivity {
    private EditText edtName;
    public ArrayList<Task> taskArrayList;
    public TaskListViewAdapter taskListViewAdapter;
    private Task task;
    Button btnSave;
    TimePicker startTime, endTime;
    DatePicker startDate, endDate;
    Intent intent;
    int id;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Task Details");
        setContentView(R.layout.activity_create_task);
        startTime = (TimePicker) findViewById(R.id.startHour);
        startTime.setIs24HourView(true);
        endTime = (TimePicker) findViewById(R.id.endHour);
        endTime.setIs24HourView(true);
        startDate = (DatePicker) findViewById(R.id.startDate);
        endDate = (DatePicker) findViewById(R.id.endDate);
        btnSave = (Button) findViewById(R.id.saveBtn);
        edtName = (EditText) findViewById(R.id.enterName);
        taskArrayList = MainActivity.taskArrayList();
        taskListViewAdapter = MainActivity.TaskListViewAdapter(taskArrayList);
        intent = getIntent();
        task = intent.getParcelableExtra("editTask");
        if (task != null) {
            edtName.setText(task.name);
            String[] start = task.startTime.split(":");
            String[] end = task.endTime.split(":");
            String[] startd = task.startDay.split("/");
            String[] endd = task.endDay.split("/");
            startTime.setCurrentHour(Integer.parseInt(start[0]));
            startTime.setCurrentMinute(Integer.parseInt(start[1]));
            endTime.setCurrentHour(Integer.parseInt(end[0]));
            endTime.setCurrentMinute(Integer.parseInt(end[1]));
            startDate.init(Integer.parseInt(startd[2]), Integer.parseInt(startd[1]), Integer.parseInt(startd[0]), null);
            endDate.init(Integer.parseInt(endd[2]), Integer.parseInt(endd[1]), Integer.parseInt(endd[0]), null);
        }
        if (edtName.getText().toString().isEmpty()) {
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        addTask();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Save();
                }
            });
        }
        id = intent.getIntExtra("id", 0);
    }

    public void Save() {
        String name = (String) edtName.getText().toString();
        String timeStart = String.valueOf(startTime.getCurrentHour()) + ":" + String.valueOf(startTime.getCurrentMinute());
        String timeEnd = String.valueOf(endTime.getCurrentHour()) + ":" + String.valueOf(endTime.getCurrentMinute());
        String dateStart = String.valueOf(startDate.getDayOfMonth()) + "/" + String.valueOf(startDate.getMonth() + 1) + "/" + String.valueOf(startDate.getYear());
        String dateEnd = String.valueOf(endDate.getDayOfMonth()) + "/" + String.valueOf(endDate.getMonth() + 1) + "/" + String.valueOf(endDate.getYear());
        task.setName(name);
        task.setStartDay(dateStart);
        task.setEndDay(dateEnd);
        task.setStartTime(timeStart);
        task.setEndTime(timeEnd);
        taskArrayList.set(id, task);
        taskListViewAdapter.notifyDataSetChanged();
        saveData();
        onBackPressed();
    }

    public void addTask() throws ParseException {
        String name = edtName.getText().toString();
        String timeStart = String.valueOf(startTime.getCurrentHour()) + ":" + String.valueOf(startTime.getCurrentMinute());
        String timeEnd = String.valueOf(endTime.getCurrentHour()) + ":" + String.valueOf(endTime.getCurrentMinute());
        String dateStart = String.valueOf(startDate.getDayOfMonth()) + "/" + String.valueOf(startDate.getMonth() + 1) + "/" + String.valueOf(startDate.getYear());
        String dateEnd = String.valueOf(endDate.getDayOfMonth()) + "/" + String.valueOf(endDate.getMonth() + 1) + "/" + String.valueOf(endDate.getYear());
        if (!name.isEmpty()) {
            Task task = new Task(name, dateStart, timeStart, dateEnd, timeEnd);
            if (!isValidate(task)) {
                Toast.makeText(this, "Data is not validate. Please try again", Toast.LENGTH_LONG).show();
            } else {
                taskArrayList.add(task);
                taskListViewAdapter.notifyDataSetChanged();
                saveData();
                onBackPressed();
            }
        } else {
            edtName.setHint("Please enter task name");
            edtName.setHintTextColor(getResources().getColor(R.color.colorWarn));
        }
    }

    public boolean isValidate(Task task) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date start = format.parse(task.getStartDay() + " " + task.getStartTime());
        Date end = format.parse(task.getEndDay() + " " + task.getEndTime());
        if (start.compareTo(end) > 0) return false;
        return true;
    }

    public void saveData() {
        try {
            File file = new File(this.getFilesDir(), "Saved");
            FileOutputStream fileOutput = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutput));
//            taskListViewAdapter.getData(taskArrayList);
            for (int i = 0; i < taskArrayList.size(); i++) {
                Task task = taskArrayList.get(i);
//                if (task.getCheckBox().isChecked()) {
//                    task.setStatus("Completed");
//                }
                bufferedWriter.write(task.getName() + " " + task.getStartTime() + " " + task.getStartDay() + " " + task.getEndTime() + " " + task.getEndDay() + " " + task.getStatus());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            fileOutput.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public void addNotification(Context context,Task task) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"Notification")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Task is running out")
                .setContentText("Task " + task.getName() + " going time out. Complete it quickly!");
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}