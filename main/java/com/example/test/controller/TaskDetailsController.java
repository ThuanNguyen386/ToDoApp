package com.example.test.controller;

import com.example.test.model.Task;
import com.example.test.view.MainActivity;
import com.example.test.view.TaskViewAdapter;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

public class TaskDetailsController {
    ArrayList<Task> taskArrayList = MainActivity.getTaskArrayList();
    TaskViewAdapter taskListArrayAdapter = MainActivity.getTaskArrayAdapter();

    public boolean checkValidateDate(Date date1, Date date2) {
        if (date2.getTime() - new Date(System.currentTimeMillis()).getTime() <= 0) return false;
        return date2.getTime() - date1.getTime() > 0;
    }

    public ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    public void saveTask(int pos, Task task) {
        taskArrayList.set(pos, task);
        taskListArrayAdapter.changeSaved(pos);
        MainActivity.updateTask();
    }

    private String convertTaskToJson(Task task) {
        Gson json = new Gson();
        return json.toJson(task);
    }

    public void addTask(Task task) {
        taskArrayList.add(task);
        taskListArrayAdapter.notifyDataSetChanged();
        MainActivity.updateTask();
    }

    public void saveData() {
        try {
            File file = new File(MainActivity.file, "Saved");
            FileOutputStream fileOutput = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutput));
            for (int i = 0; i < taskArrayList.size(); i++) {
                String line = convertTaskToJson(taskArrayList.get(i));
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            fileOutput.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}
