package Entity;

import java.time.LocalDate;
import java.util.Date;
@SuppressWarnings("unused")

public class Staff {
    private String StaffID;
    private String Staff_Name;
    private LocalDate Staff_Birthdate;
    private String Staff_Gender;
    private String Staff_Mobile;
    private String Staff_Role;
    private LocalDate Staff_Joining_Date;

    public Staff() {

    }

    public Staff(String staffID, String staff_Name, LocalDate staff_Birthdate, String staff_Gender, String staff_Mobile, String staff_Role, LocalDate staff_Joining_Date) {
        this.StaffID = staffID;
        this.Staff_Name = staff_Name;
        this.Staff_Birthdate = staff_Birthdate;
        this.Staff_Gender = staff_Gender;
        this.Staff_Mobile = staff_Mobile;
        this.Staff_Role = staff_Role;
        this.Staff_Joining_Date = staff_Joining_Date;
    }
    
    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        this.StaffID = staffID;
    }

    public String getStaff_Name() {
        return Staff_Name;
    }

    public void setStaff_Name(String staff_Name) {
        this.Staff_Name = staff_Name;
    }

    public LocalDate getStaff_Birthdate() {
        return Staff_Birthdate;
    }

    public void setStaff_Birthdate(LocalDate staff_Birthdate) {
        this.Staff_Birthdate = staff_Birthdate;
    }

    public String getStaff_Gender() {
        return Staff_Gender;
    }

    public void setStaff_Gender(String staff_Gender) {
        this.Staff_Gender = staff_Gender;
    }

    public String getStaff_Mobile() {
        return Staff_Mobile;
    }

    public void setStaff_Mobile(String Staff_Mobile) {
        this.Staff_Mobile = Staff_Mobile;
    }

    public String getStaff_Role() {
        return Staff_Role;
    }

    public void setStaff_Role(String staff_Role) {
        this.Staff_Role = staff_Role;
    }

    public LocalDate getStaff_Joining_Date() {
        return Staff_Joining_Date;
    }

    public void setStaff_Joining_Date(LocalDate Staff_Joining_Date) {
        this.Staff_Joining_Date = Staff_Joining_Date;
    }
}
