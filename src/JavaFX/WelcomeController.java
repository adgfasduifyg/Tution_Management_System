package JavaFX;

import Ultility.SQLConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Action;

import Entity.Student;
import Entity.Staff;
import Entity.Class;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

@SuppressWarnings({"unused" , "unchecked"})


public class WelcomeController extends BaseMenuController {
    @FXML
    private Label studentCountLabel;
    @FXML
    private Label teacherCountLabel;
    @FXML
    private Label activeClassCountLabel;

    @FXML
    private TableView<Student> totalstudentdetail;
    @FXML
    private TableView<Staff> totalteacherdetail;
    @FXML
    private TableView<Class> totalclassdetail;
    @FXML
    private TableView<Staff> accountdetail; // Changed to Staff

    @FXML
    private Hyperlink StaffDetailChanging;

    // File menu
    @FXML private MenuItem sessout;
    @FXML private MenuItem progquit;

    // Student menu
    @FXML private MenuItem viewstudent;
    @FXML private MenuItem addstudent;
    @FXML private MenuItem editstudent;
    @FXML private MenuItem delstudent;

    // Staff menu
    @FXML private MenuItem viewstaff;
    @FXML private MenuItem addstaff;
    @FXML private MenuItem editstaff;
    @FXML private MenuItem delstaff;

    // Class menu
    @FXML private MenuItem viewclass;
    @FXML private MenuItem addclass;
    @FXML private MenuItem editclass;
    @FXML private MenuItem delclass;

    // Enrollment menu
    @FXML private MenuItem viewenroll;
    @FXML private MenuItem addenroll;
    @FXML private MenuItem editenrol;
    @FXML private MenuItem delenroll;

    // Bill menu
    @FXML private MenuItem viewbill;
    @FXML private MenuItem addbill;
    @FXML private MenuItem editbill;
    @FXML private MenuItem delbill;

    @FXML
    public void initialize() {
        // Initialize menu bar
        this.initializeMenuBar();
        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            // Total students
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Student");
            if (rs.next()) {
                studentCountLabel.setText("Total Students: " + rs.getInt(1));
            }

            // Total teachers
            rs = stmt.executeQuery("SELECT COUNT(*) FROM Staff WHERE Staff_Role = 'Teacher'");
            if (rs.next()) {
                teacherCountLabel.setText("Total Teachers: " + rs.getInt(1));
            }

            // Active classes
            rs = stmt.executeQuery("SELECT COUNT(*) FROM Class WHERE Class_Start_Date <= GETDATE() AND Class_End_Date >= GETDATE()");
            if (rs.next()) {
                activeClassCountLabel.setText("Active Classes: " + rs.getInt(1));
            }

            // --- Student Table ---
            ObservableList<Student> students = FXCollections.observableArrayList();
            rs = stmt.executeQuery("SELECT * FROM Student");
            while (rs.next()) {
                students.add(new Student(
                    rs.getString("Stu_ID"),
                    rs.getString("Stu_Name"),
                    rs.getDate("Stu_Birthdate").toLocalDate(),
                    rs.getString("Stu_Gender"),
                    rs.getString("Stu_Mobile"),
                    rs.getDate("Stu_Registration_Date").toLocalDate()
                ));
            }
            setupStudentTable();
            totalstudentdetail.setItems(students);

            // --- Teacher Table ---
            ObservableList<Staff> teachers = FXCollections.observableArrayList();
            rs = stmt.executeQuery("SELECT * FROM Staff WHERE Staff_Role = 'Teacher'");
            while (rs.next()) {
                teachers.add(new Staff(
                    rs.getString("Staff_ID"),
                    rs.getString("Staff_Name"),
                    rs.getDate("Staff_Birthdate").toLocalDate(),
                    rs.getString("Staff_Gender"),
                    rs.getString("Staff_Mobile"),
                    rs.getString("Staff_Role"),
                    rs.getDate("Staff_Joining_Date").toLocalDate()
                ));
            }
            setupTeacherTable();
            totalteacherdetail.setItems(teachers);

            // --- Class Table ---
            ObservableList<Class> classes = FXCollections.observableArrayList();
            rs = stmt.executeQuery("SELECT * FROM Class");
            while (rs.next()) {
                classes.add(new Class(
                    rs.getString("Class_ID"),
                    rs.getString("Class_Name"),
                    rs.getString("Class_Level"),
                    rs.getString("Class_Timetable"),
                    rs.getDate("Class_Start_Date").toLocalDate(),
                    rs.getDate("Class_End_Date").toLocalDate(),
                    rs.getInt("Class_Capacity"),
                    rs.getInt("Class_Fee"),
                    rs.getString("Staff_ID")
                ));
            }
            setupClassTable();
            totalclassdetail.setItems(classes);

            // --- Account Detail Table: Use Staff class ---
            String adminID = Ultility.Session.getCurrentStaffID();
            ObservableList<Staff> accountDetails = FXCollections.observableArrayList();
            rs = stmt.executeQuery("SELECT * FROM Staff WHERE Staff_ID = '" + adminID + "'");
            if (rs.next()) {
                accountDetails.add(new Staff(
                    rs.getString("Staff_ID"),
                    rs.getString("Staff_Name"),
                    rs.getDate("Staff_Birthdate").toLocalDate(),
                    rs.getString("Staff_Gender"),
                    rs.getString("Staff_Mobile"),
                    rs.getString("Staff_Role"),
                    rs.getDate("Staff_Joining_Date").toLocalDate()
                ));
            }
            setupAccountTable();
            accountdetail.setItems(accountDetails);

        } catch (Exception e) {
            e.printStackTrace();
            studentCountLabel.setText("Total Students: ?");
            teacherCountLabel.setText("Total Teachers: ?");
            activeClassCountLabel.setText("Active Classes: ?");
        }

        // Set up hyperlink action
        StaffDetailChanging.setOnAction(this::onHyperlinkClick);

        // Add handler for "Add Student" menu item
        if (addstudent != null) {
            addstudent.setOnAction(this::onAddStudentMenuClick);
        }
    }

    private void setupStudentTable() {
        totalstudentdetail.getColumns().clear();
        TableColumn<Student, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("Stu_ID"));
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Stu_Name"));
        TableColumn<Student, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("Stu_Gender"));
        TableColumn<Student, String> mobileCol = new TableColumn<>("Mobile");
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("Stu_Mobile"));
        TableColumn<Student, String> birthCol = new TableColumn<>("Birthdate");
        birthCol.setCellValueFactory(new PropertyValueFactory<>("Stu_Birthdate"));
        TableColumn<Student, String> regCol = new TableColumn<>("Reg Date");
        regCol.setCellValueFactory(new PropertyValueFactory<>("Stu_Registration_Date"));
        totalstudentdetail.getColumns().addAll(idCol, nameCol, genderCol, mobileCol, birthCol, regCol);
    }

    private void setupTeacherTable() {
        totalteacherdetail.getColumns().clear();
        TableColumn<Staff, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("StaffID"));
        TableColumn<Staff, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Name"));
        TableColumn<Staff, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Role"));
        TableColumn<Staff, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Mobile"));
        TableColumn<Staff, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Gender"));
        TableColumn<Staff, String> birthCol = new TableColumn<>("Birthdate");
        birthCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Birthdate"));
        TableColumn<Staff, String> joinCol = new TableColumn<>("Join Date");
        joinCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Joining_Date"));
        totalteacherdetail.getColumns().addAll(idCol, nameCol, roleCol, phoneCol, genderCol, birthCol, joinCol);
    }

    private void setupClassTable() {
        totalclassdetail.getColumns().clear();
        TableColumn<Class, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ClassID"));
        TableColumn<Class, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Class_Name"));
        TableColumn<Class, String> levelCol = new TableColumn<>("Level");
        levelCol.setCellValueFactory(new PropertyValueFactory<>("Class_Level"));
        TableColumn<Class, String> timetableCol = new TableColumn<>("Timetable");
        timetableCol.setCellValueFactory(new PropertyValueFactory<>("Class_Timetable"));
        TableColumn<Class, String> startCol = new TableColumn<>("Start");
        startCol.setCellValueFactory(new PropertyValueFactory<>("Class_Start_Date"));
        TableColumn<Class, String> endCol = new TableColumn<>("End");
        endCol.setCellValueFactory(new PropertyValueFactory<>("Class_End_Date"));
        TableColumn<Class, Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(new PropertyValueFactory<>("Class_Capacity"));
        TableColumn<Class, Integer> feeCol = new TableColumn<>("Fee");
        feeCol.setCellValueFactory(new PropertyValueFactory<>("Class_Fee"));
        TableColumn<Class, String> staffCol = new TableColumn<>("StaffID");
        staffCol.setCellValueFactory(new PropertyValueFactory<>("StaffID"));
        totalclassdetail.getColumns().addAll(idCol, nameCol, levelCol, timetableCol, startCol, endCol, capCol, feeCol, staffCol);
    }

    private void setupAccountTable() {
        accountdetail.getColumns().clear();
        TableColumn<Staff, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("StaffID"));
        TableColumn<Staff, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Name"));
        TableColumn<Staff, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Role"));
        TableColumn<Staff, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Mobile"));
        TableColumn<Staff, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Gender"));
        TableColumn<Staff, String> birthCol = new TableColumn<>("Birthdate");
        birthCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Birthdate"));
        TableColumn<Staff, String> joinCol = new TableColumn<>("Join Date");
        joinCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Joining_Date"));
        accountdetail.getColumns().addAll(idCol, nameCol, roleCol, phoneCol, genderCol, birthCol, joinCol);
    }

    public void onHyperlinkClick(ActionEvent event) {
        String user = Ultility.Session.getCurrentStaffID();
        // Handle hyperlink click to change staff details
        // This could open a new window or dialog for editing staff details
        System.out.println("Hyperlink clicked: ");
        Platform.runLater(() -> {
                    try {
                        Ultility.Session.setCurrentStaffID(user);
                        MainFX.showStaffDetailChangePage();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
        // You can implement the logic to open a new window or dialog here
    }

    private void onAddStudentMenuClick(ActionEvent event) {
        try {
            MainFX.showAddStudentPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}