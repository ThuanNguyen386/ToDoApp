package com.example.test.model;

import com.example.test.view.MainActivity;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    String name;
    Date start;
    Date finish;
    String status;
    boolean completed;
    long timeLeft;


    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public Date getFinish() {
        return finish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Task(String name, Date start, Date finish) {
        this.name = name;
        this.start = start;
        this.finish = finish;
        this.status = MainActivity.getStatus()[0];
        this.completed = false;
        this.timeLeft = finish.getTime() - start.getTime();
    }

    @Override
    public String toString() {

        long t = (finish.getTime() - start.getTime()) / 1000;
        long hour = t / (60 * 60);
        long minute = t / 60 - hour * 60;
        if (minute < 10)
            return hour + ":0" + minute;
        else
            return hour + ":" + minute;
    }
}
