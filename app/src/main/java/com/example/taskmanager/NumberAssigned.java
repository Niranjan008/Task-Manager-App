package com.example.taskmanager;

public class NumberAssigned {
    String phonenum;
    int count;
    NumberAssigned(){

    }
    public NumberAssigned(String phonenum, int count) {
        this.phonenum = phonenum;
        this.count = count;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public int getCount() {
        return count;
    }
}
