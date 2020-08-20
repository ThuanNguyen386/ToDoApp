package com.example.test.controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.test.R;
import com.example.test.model.Task;
import com.example.test.view.MainActivity;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class AdapterController {
    ArrayList<Task> taskArrayList = MainActivity.getTaskArrayList();

//    public void addNotification(Context context, Task task) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification")
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle("Task going time out")
//                .setContentText("Task " + task.getName() + " going time out. Complete it quickly!")
//                .setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE);
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        NotificationManager notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notiManager.notify(0, builder.build());
//    }

    private String convertTaskToJson(Task task) {
        Gson json = new Gson();
        return json.toJson(task);
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
