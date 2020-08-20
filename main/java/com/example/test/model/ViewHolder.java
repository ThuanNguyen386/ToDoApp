package com.example.test.model;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.test.R;
import com.example.test.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewHolder {
    public TextView name;

    public TextView getTimeToDo() {
        return timeToDo;
    }

    public TextView timeToDo;
    public CheckBox checkBox;
    public TextView status;
    Task mTask;

    public void setData(Task item) {
        mTask = item;
        name.setText(item.getName());
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setChecked(false);
        timeToDo.setText("Start: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(mTask.getStart())
                + " (Time to do: "
                + mTask.toString() + ")");
        if (item.getStatus().equals(MainActivity.getStatus()[1])) {
            status.setBackgroundColor(Color.GREEN);
            name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            checkBox.setVisibility(View.INVISIBLE);
        } else if (item.getStart().getTime() <= new Date(System.currentTimeMillis()).getTime())
            updateTimeRemaining(System.currentTimeMillis());
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public void updateTimeRemaining(long currentTime) {
        if (!mTask.getStatus().equals(MainActivity.getStatus()[1])) {
            if (currentTime - mTask.getStart().getTime() >= 0) {
                long timeDiff = mTask.getFinish().getTime() - currentTime;
                mTask.setTimeLeft(timeDiff);
                if (timeDiff > 0) {
                    int seconds = (int) (timeDiff / 1000) % 60;
                    int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                    int hours = (int) ((timeDiff / (1000 * 60 * 60)));
                    timeToDo.setText(hours + " hrs " + minutes + " mins " + seconds + " sec");
                } else {
                    mTask.setStatus(MainActivity.getStatus()[2]);
                    timeToDo.setText(R.string.expired);
                    status.setBackgroundColor(Color.RED);
                    name.setPaintFlags(0);
                    checkBox.setVisibility(View.INVISIBLE);
                    MainActivity.updateTask();
                }
            } else {
                timeToDo.setText("Start: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(mTask.getStart())
                        + " (Time to do: "
                        + mTask.toString() + ")");
            }
        }
    }

    public TextView getStatus() {
        return status;
    }
}
