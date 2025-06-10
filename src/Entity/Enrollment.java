package Entity;

import java.time.LocalDate;

public class Enrollment {
    private String enrollmentID;
    private String stuID;
    private String classID;
    private LocalDate enrollmentDate;
    private String enrollmentStatus;

    public Enrollment() {
    }

    public Enrollment(String enrollmentID, String stuID, String classID, LocalDate enrollmentDate, String enrollmentStatus) {
        this.enrollmentID = enrollmentID;
        this.stuID = stuID;
        this.classID = classID;
        this.enrollmentDate = enrollmentDate;
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getEnrollmentID() {
        return enrollmentID;
    }

    public void setEnrollmentID(String enrollmentID) {
        this.enrollmentID = enrollmentID;
    }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }
}
