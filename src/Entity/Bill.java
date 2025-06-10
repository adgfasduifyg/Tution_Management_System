package Entity;

import java.time.LocalDate;

public class Bill {
    private String Bill_ID;
    private String Enrollment_ID;
    private LocalDate Bill_Date;
    private String Bill_Payment_Status;
    private String Bill_Payment_Method;
    private String Bill_Note;

    public Bill() {
    }

    public Bill(String bill_ID, String enrollment_ID, LocalDate bill_Date, String bill_Payment_Status, String bill_Payment_Method, String bill_Note) {
        this.Bill_ID = bill_ID;
        this.Enrollment_ID = enrollment_ID;
        this.Bill_Date = bill_Date;
        this.Bill_Payment_Status = bill_Payment_Status;
        this.Bill_Payment_Method = bill_Payment_Method;
        this.Bill_Note = bill_Note;
    }

    public String getBill_ID() {
        return Bill_ID;
    }

    public void setBill_ID(String bill_ID) {
        this.Bill_ID = bill_ID;
    }

    public String getEnrollment_ID() {
        return Enrollment_ID;
    }

    public void setEnrollment_ID(String enrollment_ID) {
        this.Enrollment_ID = enrollment_ID;
    }

    public LocalDate getBill_Date() {
        return Bill_Date;
    }

    public void setBill_Date(LocalDate bill_Date) {
        this.Bill_Date = bill_Date;
    }

    public String getBill_Payment_Status() {
        return Bill_Payment_Status;
    }

    public void setBill_Payment_Status(String bill_Payment_Status) {
        this.Bill_Payment_Status = bill_Payment_Status;
    }

    public String getBill_Payment_Method() {
        return Bill_Payment_Method;
    }

    public void setBill_Payment_Method(String bill_Payment_Method) {
        this.Bill_Payment_Method = bill_Payment_Method;
    }

    public String getBill_Note() {
        return Bill_Note;
    }

    public void setBill_Note(String bill_Note) {
        this.Bill_Note = bill_Note;
    }
}
