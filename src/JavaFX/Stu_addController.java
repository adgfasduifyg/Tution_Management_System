package JavaFX;

import Ultility.SQLConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

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

public class Stu_addController extends BaseMenuController {
    @FXML
    private TableView<Student> studenttable;

    @FXML
    private MenuItem sessout;
    @FXML
    private MenuItem progquit;
    @FXML
    private MenuItem viewstudent;
    @FXML
    private MenuItem addstudent;
    @FXML
    private MenuItem editstudent;
    @FXML
    private MenuItem delstudent;
    @FXML
    private MenuItem viewstaff;
    @FXML
    private MenuItem addstaff;
    @FXML
    private MenuItem editstaff;
    @FXML
    private MenuItem delstaff;
    @FXML
    private MenuItem viewclass;
    @FXML
    private MenuItem addclass;
    @FXML
    private MenuItem editclass;
    @FXML
    private MenuItem delclass;
    @FXML
    private MenuItem viewenroll;
    @FXML
    private MenuItem addenroll;
    @FXML
    private MenuItem editenrol;
    @FXML
    private MenuItem delenroll;
    @FXML
    private MenuItem viewbill;
    @FXML
    private MenuItem addbill;
    @FXML
    private MenuItem editbill;
    @FXML
    private MenuItem delbill;

    @FXML
    private Button stuaddbtn;
    @FXML
    private TextField entername;
    @FXML
    private TextField enterbirth;
    @FXML
    private TextField entergender;
    @FXML
    private TextField entermobile;
    @FXML
    private Label feedback;
    @FXML
    private Label totalstu;

    @FXML
    public void initialize() {
        this.initializeMenuBar();
        System.out.println("Initializing Stu_addController...");
        try (Connection conn = SQLConnector.getConnection();
            Statement stmt = conn.createStatement()) {
                System.out .println("Connected to the database successfully!");
                // --- Student Table ---
            ObservableList<Student> students = FXCollections.observableArrayList();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Student");
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
            System.out.println("Loaded students: " + students.size());
            setupStudentTable();
            studenttable.setItems(students);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Student");
            if (rs.next()) {
                totalstu.setText("Total Students: " + rs.getInt(1));
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        stuaddbtn.setOnAction(e -> addnewstudent());
    }

    private void setupStudentTable() {
        studenttable.getColumns().clear();
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
        studenttable.getColumns().addAll(idCol, nameCol, genderCol, mobileCol, birthCol, regCol);
    }

    public void addnewstudent() {
        String name = entername.getText().trim();
        String birth = enterbirth.getText().trim();
        String gender = entergender.getText().trim();
        String mobile = entermobile.getText().trim();

        if (name.isEmpty() || birth.isEmpty() || gender.isEmpty() || mobile.isEmpty()) {
            feedback.setText("Please fill in all fields.");
            return;
        }

        // Validate gender
        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female") && !gender.equalsIgnoreCase("Other")) {
            feedback.setText("Gender must be 'Male', 'Female', or 'Other'.");
            return;
        }

        // Validate mobile
        if (!mobile.matches("0\\d{9}")) {
            feedback.setText("Mobile must be 10 digits and start with 0.");
            return;
        }

        // Validate birthdate format (yyyy-MM-dd)
        java.sql.Date sqlBirth;
        try {
            sqlBirth = java.sql.Date.valueOf(birth);
        } catch (Exception e) {
            feedback.setText("Birthdate must be in yyyy-MM-dd format.");
            return;
        }

        String sql = "INSERT INTO Student (Stu_Name, Stu_Birthdate, Stu_Gender, Stu_Mobile) VALUES (?, ?, ?, ?)";

        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, sqlBirth);
            pstmt.setString(3, gender);
            pstmt.setString(4, mobile);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                feedback.setText("Student added successfully!");
                // Optionally, refresh the table and total count
                initialize();
                // Clear input fields
                entername.clear();
                enterbirth.clear();
                entergender.clear();
                entermobile.clear();
            } else {
                feedback.setText("Failed to add student.");
            }
        } catch (Exception e) {
            feedback.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}