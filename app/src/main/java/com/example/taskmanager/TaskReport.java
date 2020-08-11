package com.example.taskmanager;

public class TaskReport {
    String id,title,description,taskName,taskRemarks,acknowledge,assignerPhone,completerphone;
    TaskReport(){

    }

    public TaskReport(String id, String title, String description, String taskName, String taskRemarks, String acknowledge, String assignerPhone,String completerphone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskName = taskName;
        this.taskRemarks = taskRemarks;
        this.acknowledge = acknowledge;
        this.assignerPhone = assignerPhone;
        this.completerphone = completerphone;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskRemarks() {
        return taskRemarks;
    }

    public String getAcknowledge() {
        return acknowledge;
    }

    public String getAssignerPhone() {
        return assignerPhone;
    }

    public String getCompleterphone() {
        return completerphone;
    }
}
