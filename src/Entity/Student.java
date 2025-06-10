package Entity;

import java.time.LocalDate;

public class Student {
    private String Stu_ID;
    private String Stu_Name;
    private LocalDate Stu_Birthdate;
    private String Stu_Gender;
    private String Stu_Mobile;
    private LocalDate Stu_Registration_Date;

    public Student() {
    }

    public Student(String stu_ID, String stu_Name, LocalDate stu_Birthdate, String stu_Gender, String stu_Mobile, LocalDate stu_Registration_Date) {
        this.Stu_ID = stu_ID;
        this.Stu_Name = stu_Name;
        this.Stu_Birthdate = stu_Birthdate;
        this.Stu_Gender = stu_Gender;
        this.Stu_Mobile = stu_Mobile;
        this.Stu_Registration_Date = stu_Registration_Date;
    }

    public String getStu_ID() {
        return Stu_ID;
    }

    public void setStu_ID(String stu_ID) {
        this.Stu_ID = stu_ID;
    }

    public String getStu_Name() {
        return Stu_Name;
    }

    public void setStu_Name(String stu_Name) {
        this.Stu_Name = stu_Name;
    }

    public LocalDate getStu_Birthdate() {
        return Stu_Birthdate;
    }

    public void setStu_Birthdate(LocalDate stu_Birthdate) {
        this.Stu_Birthdate = stu_Birthdate;
    }

    public String getStu_Gender() {
        return Stu_Gender;
    }

    public void setStu_Gender(String stu_Gender) {
        this.Stu_Gender = stu_Gender;
    }

    public String getStu_Mobile() {
        return Stu_Mobile;
    }

    public void setStu_Mobile(String stu_Mobile) {
        this.Stu_Mobile = stu_Mobile;
    }

    public LocalDate getStu_Registration_Date() {
        return Stu_Registration_Date;
    }

    public void setStu_Registration_Date(LocalDate stu_Registration_Date) {
        this.Stu_Registration_Date = stu_Registration_Date;
    }
}
