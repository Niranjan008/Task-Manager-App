package com.example.taskmanager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    String title,id;
    String description;
    String dateandtime;
    String assignerphone;
    String employeephone;
    String alarmset,viewed;
    public Task(){

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public String getAssignerphone() {
        return assignerphone;
    }

    public String getEmployeephone() {
        return employeephone;
    }

    public String getAlarmset() {
        return alarmset;
    }

    public String getViewed() {
        return viewed;
    }

    public String getId() {
        return id;
    }

    public  Task(String id, String title, String description, String dateandtime, String assignerphone,
                 String employeephone, String alarmset, String viewed){
        this.id =id;
        this.title = title;
        this.description = description;
        this.dateandtime = dateandtime;
        this.assignerphone = assignerphone;
        this.employeephone = employeephone;
        this.alarmset = alarmset;
        this.viewed = viewed;
    }
}
