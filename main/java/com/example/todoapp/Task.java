package com.example.todoapp;

import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class Task implements Parcelable {
    String name;
    String startDay;
    String startTime;
    String endDay;
    String endTime;
    String status;
    CheckBox checkBox;
    ImageButton btnEdit;


    public CheckBox getCheckBox() {
        return checkBox;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Task(String name, String startDay, String startTime, String endDay, String endTime) {
        this.name = name;
        this.startDay = startDay;
        this.startTime = startTime;
        this.endDay = endDay;
        this.endTime = endTime;
        this.status = "Doing";

    }


    protected Task(Parcel in) {
        name = in.readString();
        startDay = in.readString();
        startTime = in.readString();
        endDay = in.readString();
        endTime = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return   name + '\n' +
                " From " + startTime + " - " + startDay +
                " to " + endTime + " - " + endDay;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(startDay);
        parcel.writeString(startTime);
        parcel.writeString(endDay);
        parcel.writeString(endTime);
    }
}