package com.example.todoapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskListViewAdapter extends BaseAdapter {
    final ArrayList<Task> taskArrayList;
    ImageButton btnDelete;
    Intent intent;
    CountDownTimer timeLeft;
    CreateTaskActivity cr = new CreateTaskActivity();

    TaskListViewAdapter(ArrayList<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    @Override
    public int getCount() {
        return taskArrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        return taskArrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View getView(final int pos, final View view, ViewGroup viewGroup) {
        final View viewTask;
        Date start = null, end = null;
        if (view == null) {
            viewTask = View.inflate(viewGroup.getContext(), R.layout.task_view, null);
        } else viewTask = view;
        final Task task = (Task) getItem(pos);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            start = format.parse(task.startDay + " " + task.startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            end = format.parse(task.endDay + " " + task.endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        btnDelete = (ImageButton) viewTask.findViewById(R.id.btnDelete);
        task.btnEdit = (ImageButton) viewTask.findViewById(R.id.btnEdit);
        task.checkBox = (CheckBox) viewTask.findViewById(R.id.checkbox);
        final long a = end.getTime() - start.getTime();
        long hour = a / (1000 * 60 * 60);
        long minute = a / (1000 * 60) - hour * 60;
        ((TextView) viewTask.findViewById(R.id.name)).setText(task.toString());
//        ((TextView) viewTask.findViewById(R.id.startTime1)).setText("Start: " + String.format(task.getStartTime()) + " " + String.format(task.getStartDay()));
//        ((TextView) viewTask.findViewById(R.id.endTime1)).setText("End: " + String.format(task.getEndTime()) + " " + String.format(task.getEndDay()));
        ((TextView) viewTask.findViewById(R.id.timeToDo)).setText("Time to do: " + String.valueOf(hour) + " : " + String.valueOf(minute));
        final TextView txtView = ((TextView) viewTask.findViewById(R.id.timeleft));
        txtView.setText(String.valueOf(a));
        if (task.checkBox.isChecked()) {
            task.setStatus("Completed");
            ((TextView) viewTask.findViewById(R.id.status)).setTextColor(Color.GREEN);
        } else {
            task.setStatus("Doing");
            ((TextView) viewTask.findViewById(R.id.status)).setTextColor(Color.BLACK);
            timeLeft = new CountDownTimer(a, 1000) {
                long t = a / 1000;

                @Override
                public void onTick(long l) {
                    long second = (l / 1000 - (l / (1000 * 60) * 60));
                    ((TextView) viewTask.findViewById(R.id.timeToDo)).setText("Time to do: " + String.valueOf(l / (1000 * 60 * 60)) + " : " + String.valueOf(l / (1000 * 60) - l / (1000 * 60 * 60) * 60) + " : " + String.valueOf(second));
                    if (l / 1000 <= t / 5) {
                        cr.addNotification(txtView.getContext(), task);

                    }
                }

                @Override
                public void onFinish() {
                    task.setStatus("Expired");
                    ((TextView) viewTask.findViewById(R.id.timeToDo)).setText("Time to do: 0 : 0 : 0");
                    ((TextView) viewTask.findViewById(R.id.status)).setText(task.getStatus());
                    ((TextView) viewTask.findViewById(R.id.status)).setTextColor(Color.RED);
                }
            }.start();
        }
        ((TextView) viewTask.findViewById(R.id.status)).setText(task.getStatus());
        ((TextView) viewTask.findViewById(R.id.status)).setTextSize(26);

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(viewTask.getContext());
                builder.setMessage("Do you want to delete this task?");
                builder.setTitle("Delete task");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int a) {
                        timeLeft.cancel();
                        if (!task.checkBox.isChecked()) {
                            for (int i = 1; i < taskArrayList.size() - pos; i++) {
                                if (taskArrayList.get(pos + i).checkBox.isChecked()) {
                                    taskArrayList.get(pos + i).checkBox.setChecked(false);
                                    taskArrayList.get(pos + i).checkBox.setVisibility(View.VISIBLE);
                                    taskArrayList.get(pos + i).btnEdit.setVisibility(View.VISIBLE);
                                    taskArrayList.get(pos + i - 1).checkBox.setChecked(true);
                                    taskArrayList.get(pos + i - 1).checkBox.setVisibility(View.INVISIBLE);
                                    taskArrayList.get(pos + i - 1).btnEdit.setVisibility(View.GONE);
                                }
                            }
                        }
                        taskArrayList.remove(pos);
                        getData(taskArrayList);
                        cr.saveData();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int a) {
                        return;
                    }
                });
                builder.create().show();
            }
        });
        task.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(viewTask.getContext());
                builder.setMessage("Do you finish this task?");
                builder.setTitle("Complete task");
                builder.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int a) {
                        if (b) {
                            task.checkBox.setChecked(true);
                            task.checkBox.setVisibility(View.INVISIBLE);
                            task.btnEdit.setVisibility(View.GONE);
                            timeLeft.cancel();
//                            notifyDataSetChanged();
                            notifyDataSetChanged();
                            cr.saveData();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int a) {
                        task.checkBox.setChecked(false);
                        return;
                    }
                });
                builder.create().show();
            }
        });
        task.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), CreateTaskActivity.class);
                intent.putExtra("editTask", task);
                intent.putExtra("id", pos);
                view.getContext().startActivity(intent);
            }
        });

        return viewTask;
    }

    void getData(ArrayList<Task> taskArrayList) {
//        newList = taskArrayList;
        ArrayList<Task> newList = new ArrayList<>();
        for (int i = 0; i < taskArrayList.size(); i++) {
            newList.add(taskArrayList.get(i));
        }
        taskArrayList.clear();
        taskArrayList.addAll(newList);
        notifyDataSetChanged();
    }
}
