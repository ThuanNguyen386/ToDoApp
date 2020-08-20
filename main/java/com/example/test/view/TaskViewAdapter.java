package com.example.test.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.test.R;
import com.example.test.controller.AdapterController;
import com.example.test.model.Task;
import com.example.test.model.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TaskViewAdapter extends ArrayAdapter<Task> {
    Context context;
    ArrayList<Task> taskArrayList;
    final ArrayList<ViewHolder> listHolder = new ArrayList<>();
    int layoutResource;
    TextView name;
    TextView status, timeToDo;
    ImageButton btnDelete;
    AdapterController adapterController = new AdapterController();
    CheckBox checkBox;
    private Handler mHandler = new Handler();
    Runnable mRunnable;
    Timer tmr;

    public TaskViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Task> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.taskArrayList = objects;
        startUpdateTimer();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.timeToDo = (TextView) convertView.findViewById(R.id.timeToDo);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
            synchronized (listHolder) {
                listHolder.add(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setData(taskArrayList.get(position));
        mRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (listHolder) {
                    long currentTime = new Date(System.currentTimeMillis()).getTime();
                    for (ViewHolder holder1 : listHolder) {
                        holder1.updateTimeRemaining(currentTime);
                    }
                }
            }
        };
        status = convertView.findViewById(R.id.status);
        timeToDo = holder.getTimeToDo();
        btnDelete = convertView.findViewById(R.id.btnDelete);
        checkBox = holder.getCheckBox();
        name = holder.getName();
        status = holder.getStatus();
        updateTaskDisplay(taskArrayList.get(position));
        MainActivity.updateTask();
        final View finalConvertView = convertView;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                if (b) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getContext());
                    builder.setMessage(R.string.done_confirm);
                    builder.setTitle(R.string.done);
                    builder.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int a) {
                            taskArrayList.get(position).setCompleted(true);
                            taskArrayList.get(position).setStatus(MainActivity.getStatus()[1]);
                            stopTimer();
                            notifyDataSetChanged();
                            updateTaskDisplay(taskArrayList.get(position));
                            adapterController.saveData();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int a) {
                            checkBox.setChecked(false);
                        }
                    });
                    builder.create().show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                alertDialog.setTitle(R.string.delete);
                alertDialog.setMessage(R.string.delete_confirm);
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        taskArrayList.remove(position);
                        notifyDataSetChanged();
                        MainActivity.updateTask();
                        for (i = 0; i < taskArrayList.size(); i++) {
                            updateTaskDisplay(taskArrayList.get(i));
                        }
                        adapterController.saveData();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                alertDialog.create().show();
            }
        });
        if (taskArrayList.get(position).getStatus().equals(MainActivity.getStatus()[0])) {
            if (taskArrayList.get(position).getFinish().getTime() - System.currentTimeMillis() <= (taskArrayList.get(position).getFinish().getTime() - taskArrayList.get(position).getStart().getTime()) / 5) {
                @SuppressLint("ResourceType") NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle((CharSequence) convertView.findViewById(R.string.title_notification))
                        .setContentText("Task " + taskArrayList.get(position).getName() + " going time out. Complete it quickly!")
                        .setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE);
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManager notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notiManager.notify(0, builder.build());
            }
        }
        return convertView;
    }

    public void updateTaskDisplay(Task task) {
        switch (task.getStatus()) {
            case "completed":
                status.setBackgroundColor(Color.GREEN);
                name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                checkBox.setVisibility(View.INVISIBLE);
                break;
            case "expired":
                status.setBackgroundColor(Color.RED);
                name.setPaintFlags(0);
                checkBox.setVisibility(View.INVISIBLE);
                break;
            case "doing":
                status.setBackgroundColor(Color.BLUE);
                name.setPaintFlags(0);
                checkBox.setChecked(false);
                break;
            default:
                break;
        }
    }

    public void changeSaved(int position) {
        listHolder.get(position - 1).name.setText(taskArrayList.get(position).getName());
        startUpdateTimer();
        notifyDataSetChanged();
    }

    private void startUpdateTimer() {
        tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(mRunnable);
            }
        }, 1000, 1000);
    }

    private void stopTimer() {
        tmr.cancel();
        tmr.purge();
    }
}