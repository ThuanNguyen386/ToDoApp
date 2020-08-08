package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static TaskListViewAdapter taskListViewAdapter;

    static ArrayList<Task> taskArrayList;

    public static ArrayList<Task> taskArrayList() {
        return taskArrayList;
    }

    public static TaskListViewAdapter TaskListViewAdapter(ArrayList<Task> taskArrayList) {
        return taskListViewAdapter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskArrayList = new ArrayList<Task>();
        taskListViewAdapter = new TaskListViewAdapter(taskArrayList);
       ListView taskList = (ListView) findViewById(R.id.listTask);

        readData();
        taskListViewAdapter.getData(taskArrayList);
        taskList.setAdapter(taskListViewAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnAdd) {
            Intent intent = new Intent(this, CreateTaskActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void readData() {
        File file = new File(this.getFilesDir(), "Saved");
        if (!file.exists()) {
            return;
        }
        try {
            FileInputStream fileInput = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInput));
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] a = line.split(" ");
                Task task = new Task(a[0], a[2], a[1], a[4], a[3]);
                taskArrayList.add(task);
                taskListViewAdapter.notifyDataSetChanged();
                line = bufferedReader.readLine();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
//    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==1){
//            String name = data.getExtras().getString("key_1");
//            Task task=new Task(name);
//        }
//    }
}