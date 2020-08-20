package com.example.test.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
import com.example.test.controller.MainController;
import com.example.test.controller.TaskDetailsController;
import com.example.test.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TaskDetailsActivity extends AppCompatActivity {
    TextView startDate, startTime, endDate, endTime, issue;
    Button btnSave;
    ImageButton setStartDate;
    ImageButton setEndDate;
    EditText edtName;
    ArrayList<Task> taskArrayList;
    TaskDetailsController taskDetailsController = new TaskDetailsController();
    Intent intent;
    int pos;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_task_details);
        setTitle(R.string.title_add);
        startDate = (TextView) findViewById(R.id.setStartDate);
        startTime = (TextView) findViewById(R.id.setStartTime);
        endDate = (TextView) findViewById(R.id.setEndDate);
        endTime = (TextView) findViewById(R.id.setEndTime);
        issue = (TextView) findViewById(R.id.issue);
        setStartDate = (ImageButton) findViewById(R.id.startDate);
        setEndDate = (ImageButton) findViewById(R.id.endDate);
        btnSave = (Button) findViewById(R.id.btnSave);
        edtName = (EditText) findViewById(R.id.taskName);
        taskArrayList = taskDetailsController.getTaskArrayList();
        intent = getIntent();
        pos = intent.getIntExtra("id", 0);
        task = (Task) intent.getSerializableExtra("task");
        setStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showDatePickerDialog(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
//            }
        });
        setEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    showDatePickerDialog(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        if (task != null) {
            setTitle(R.string.title_edit);
            Task task = taskArrayList.get(pos);
            edtName.setText(task.getName());
            startDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(task.getStart()));
            startTime.setText(new SimpleDateFormat("HH:mm").format(task.getStart()));
            endDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(task.getFinish()));
            endTime.setText(new SimpleDateFormat("HH:mm").format(task.getFinish()));
            btnSave.setEnabled(true);
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
            setTitle(R.string.title_edit);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTimePickerDialog(final TextView txt) throws ParseException {
        final Calendar calendar;
        int hour, minute;
        calendar = Calendar.getInstance();
        if ((txt.getText().toString().isEmpty())) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = simpleDateFormat.parse(txt.getText().toString());
            hour = date.getHours();
            minute = date.getMinutes();
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(0, 0, 0, i, i1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                switch (txt.getId()) {
                    case R.id.setStartTime:
                        txt.setText(simpleDateFormat.format(calendar.getTime()));
                        break;
                    case R.id.setEndTime:
                        txt.setText(simpleDateFormat.format(calendar.getTime()));
                        break;
                    default:
                        break;
                }
            }

        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showDatePickerDialog(final TextView txt) throws ParseException {
        final Calendar calendar = Calendar.getInstance();
        int day, month, year;
        if (txt.getText().toString().isEmpty()) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = simpleDateFormat.parse(txt.getText().toString());
            calendar.setTime(date);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                switch (txt.getId()) {
                    case R.id.setStartDate:
                        txt.setText(simpleDateFormat.format(calendar.getTime()));
                        try {
                            showTimePickerDialog(startTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.setEndDate:
                        txt.setText(simpleDateFormat.format(calendar.getTime()));
                        TextView txt2 = findViewById(R.id.setStartDate);
                        if (!txt2.getText().toString().isEmpty()) {
                            btnSave.setEnabled(true);
                        }
                        try {
                            showTimePickerDialog(endTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private Date convertToDate(TextView date, TextView time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateTime = date.getText().toString() + " " + time.getText().toString();
        Date day = simpleDateFormat.parse(dateTime);
        return day;
    }

    void addTask() throws ParseException {
        if (!edtName.getText().toString().isEmpty()) {
            if (startTime.getText().toString().equals("")) {
                startTime.setText(R.string.start_of_day);
            }
            if (endTime.getText().toString().equals("")) {
                endTime.setText(R.string.start_of_day);
            }
            String name = String.valueOf(edtName.getText());
            Date startDay = convertToDate(startDate, startTime);
            Date endDay = convertToDate(endDate, endTime);
            if (!taskDetailsController.checkValidateDate(startDay, endDay)) {
                issue.setText(R.string.illegal_data);
                issue.setTextColor(Color.RED);
            } else {
                issue.setVisibility(View.INVISIBLE);
                Task task = new Task(name, startDay, endDay);
                taskDetailsController.addTask(task);
                taskDetailsController.saveData();
                onBackPressed();
            }

        } else {
            edtName.setHint(R.string.enter_task_name);
            edtName.setHintTextColor(Color.RED);
        }

    }

    void Save() throws ParseException {
        if (!edtName.getText().toString().isEmpty()) {
            {
                String name = String.valueOf(edtName.getText());
                Date startDay = convertToDate(startDate, startTime);
                Date endDay = convertToDate(endDate, endTime);
                if (!taskDetailsController.checkValidateDate(startDay, endDay)) {
                    issue.setText(R.string.illegal_data);
                    issue.setTextColor(Color.RED);
                } else {
                    issue.setVisibility(View.INVISIBLE);
                    Task task = new Task(name, startDay, endDay);
                    taskDetailsController.saveTask(pos, task);
                    taskDetailsController.saveData();
                    onBackPressed();
                }
            }
        } else {
            edtName.setHint(R.string.enter_task_name);
            edtName.setHintTextColor(Color.RED);
        }
    }
}