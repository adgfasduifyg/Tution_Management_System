package Entity;

import java.time.LocalDate;

public class Class {
    private String ClassID;
    private String Class_Name;
    private String Class_Level;
    private String Class_Timetable;
    private LocalDate Class_Start_Date;
    private LocalDate Class_End_Date;
    private int Class_Capacity;
    private int Class_Fee;
    private String StaffID;

    public Class() {
    }

    public Class(String classID, String class_Name, String class_Level, String class_Timetable,
                 LocalDate class_Start_Date, LocalDate class_End_Date, int class_Capacity,
                 int class_Fee, String staffID) {
        this.ClassID = classID;
        this.Class_Name = class_Name;
        this.Class_Level = class_Level;
        this.Class_Timetable = class_Timetable;
        this.Class_Start_Date = class_Start_Date;
        this.Class_End_Date = class_End_Date;
        this.Class_Capacity = class_Capacity;
        this.Class_Fee = class_Fee;
        this.StaffID = staffID;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        this.ClassID = classID;
    }

    public String getClass_Name() {
        return Class_Name;
    }

    public void setClass_Name(String class_Name) {
        this.Class_Name = class_Name;
    }

    public String getClass_Level() {
        return Class_Level;
    }

    public void setClass_Level(String class_Level) {
        this.Class_Level = class_Level;
    }

    public String getClass_Timetable() {
        return Class_Timetable;
    }

    public void setClass_Timetable(String class_Timetable) {
        this.Class_Timetable = class_Timetable;
    }

    public LocalDate getClass_Start_Date() {
        return Class_Start_Date;
    }

    public void setClass_Start_Date(LocalDate class_Start_Date) {
        this.Class_Start_Date = class_Start_Date;
    }

    public LocalDate getClass_End_Date() {
        return Class_End_Date;
    }

    public void setClass_End_Date(LocalDate class_End_Date) {
        this.Class_End_Date = class_End_Date;
    }

    public int getClass_Capacity() {
        return Class_Capacity;
    }

    public void setClass_Capacity(int class_Capacity) {
        this.Class_Capacity = class_Capacity;
    }

    public int getClass_Fee() {
        return Class_Fee;
    }

    public void setClass_Fee(int class_Fee) {
        this.Class_Fee = class_Fee;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        this.StaffID = staffID;
    }
}
