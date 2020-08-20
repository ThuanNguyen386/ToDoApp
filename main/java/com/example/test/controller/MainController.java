package com.example.test.controller;

import com.example.test.model.Task;
import com.example.test.view.MainActivity;
import com.example.test.view.TaskViewAdapter;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainController {
    ArrayList<Task> taskArrayList = MainActivity.getTaskArrayList();
    TaskViewAdapter taskListArrayAdapter;

    public MainController(TaskViewAdapter taskListArrayAdapter) {
        this.taskListArrayAdapter = taskListArrayAdapter;
    }

    public ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    public long taskExpired(ArrayList<Task> taskArrayList) {
        long t = 0;
        for (Task task : taskArrayList) {
            if (task.getStatus().equals("expired")) t++;
        }
        return t;
    }

    public long taskCompleted(ArrayList<Task> taskArrayList) {
        long t = 0;
        for (Task task : taskArrayList) {
            if (task.getStatus().equals("completed")) t++;
        }
        return t;
    }

    public long taskToDo(ArrayList<Task> taskArrayList) {
        long t = 0;
        for (Task task : taskArrayList) {
            if (task.getStatus().equals("doing")) t++;
        }
        return t;
    }

    private Task convertJsonToTask(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Task.class);
    }

    public void readData() {
        File file = new File(MainActivity.file, "Saved");
        if (!file.exists()) {
            return;
        }
        try {
            FileInputStream fileInput = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInput));
            String line = bufferedReader.readLine();
            while (line != null) {
                Task task = convertJsonToTask(line);
                taskArrayList.add(task);
                taskListArrayAdapter.notifyDataSetChanged();
                line = bufferedReader.readLine();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

}
