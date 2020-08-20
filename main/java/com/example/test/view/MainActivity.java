package com.example.test.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
import com.example.test.controller.MainController;
import com.example.test.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static MainController mainController;
    private static TaskViewAdapter taskArrayAdapter;
    static ArrayList<Task> taskArrayList;
    static TextView todo, expired, completed;
    ListView listTask;
    public static File file;
    FloatingActionButton fab;
    public static String[] status;

    public static String[] getStatus() {
        return status;
    }

    public static TaskViewAdapter getTaskArrayAdapter() {
        return taskArrayAdapter;
    }


    public static void updateTask() {
        todo.setText(mainController.taskToDo(mainController.getTaskArrayList()) + " Tasks todo");
        expired.setText(mainController.taskExpired(mainController.getTaskArrayList()) + " Tasks expired");
        completed.setText(mainController.taskCompleted(mainController.getTaskArrayList()) + " Tasks done");
    }

    public static ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        init();
    }

    void init() {
        taskArrayList = new ArrayList<>();
        taskArrayAdapter = new TaskViewAdapter(this, android.R.layout.simple_list_item_1, taskArrayList);
        mainController = new MainController(taskArrayAdapter);
        file = this.getFilesDir();
        mainController.readData();
        status = getResources().getStringArray(R.array.status);
        listTask = findViewById(R.id.listTask);
        listTask.setAdapter(taskArrayAdapter);
        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(MainActivity.this, TaskDetailsActivity.class);
                intent1.putExtra("id", i);
                intent1.putExtra("task", taskArrayList.get(i));
                startActivity(intent1);
            }
        });

        todo = findViewById(R.id.taskDoing);
        expired = findViewById(R.id.taskExpired);
        completed = findViewById(R.id.taskDone);
        updateTask();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        taskArrayAdapter.unregister();
    }

    @Override
    public void onStop() {
        try {
//            taskArrayAdapter.unregister();
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

}