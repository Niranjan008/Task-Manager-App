package com.example.taskmanager;

public class Comment {
    String id,assignerphone,employeephone,title,description,oldmess,newmess;

    public Comment(String id,String assignerphone, String employeephone, String title, String description, String oldmess, String newmess) {
        this.id = id;
        this.assignerphone = assignerphone;
        this.employeephone = employeephone;
        this.title = title;
        this.description = description;
        this.oldmess = oldmess;
        this.newmess = newmess;
    }

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public String getAssignerphone() {
        return assignerphone;
    }

    public String getEmployeephone() {
        return employeephone;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOldmess() {
        return oldmess;
    }

    public String getNewmess() {
        return newmess;
    }
}
