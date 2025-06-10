package JavaFX;

import Ultility.SQLConnector;
import Entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
@SuppressWarnings("unchecked")

public class Stu_edit_delController extends BaseMenuController {

    @FXML private TableView<Student> studenttable;
    @FXML private Label totalstu;
    @FXML private Label feedback;
    @FXML private TextField entername;
    @FXML private TextField enterbirth;
    @FXML private TextField entergender;
    @FXML private TextField entermobile;
    @FXML private TextField enterid;
    @FXML private Button stueditbtn;
    @FXML private Button studelbtn;
    @FXML private Button stufindbyidbtn;

    private ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        this.initializeMenuBar();

        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement()) {
            students.clear();
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
            setupStudentTable();
            studenttable.setItems(students);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Student");
            if (rs.next()) {
                totalstu.setText("Total Students: " + rs.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Populate fields when a row is selected
        studenttable.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Student>() {
                @Override
                public void changed(ObservableValue<? extends Student> obs, Student oldSel, Student newSel) {
                    if (newSel != null) {
                        enterid.setText(newSel.getStu_ID());
                        entername.setText(newSel.getStu_Name());
                        enterbirth.setText(newSel.getStu_Birthdate().toString());
                        entergender.setText(newSel.getStu_Gender());
                        entermobile.setText(newSel.getStu_Mobile());
                        feedback.setText("");
                    }
                }
            }
        );

        // Edit button logic
        stueditbtn.setOnAction(e -> editSelectedStudent());

        // Delete button logic
        studelbtn.setOnAction(e -> deleteSelectedStudent());

        // Find by ID button logic
        stufindbyidbtn.setOnAction(e -> findStudentById());
    }

    private void findStudentById() {
        String id = enterid.getText().trim();
        if (id.isEmpty()) {
            feedback.setText("Please enter a student ID.");
            return;
        }
        String sql = "SELECT * FROM Student WHERE Stu_ID = ?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                entername.setText(rs.getString("Stu_Name"));
                enterbirth.setText(rs.getDate("Stu_Birthdate").toLocalDate().toString());
                entergender.setText(rs.getString("Stu_Gender"));
                entermobile.setText(rs.getString("Stu_Mobile"));
                feedback.setText("Student found.");
            } else {
                feedback.setText("No student found with ID: " + id);
                entername.clear();
                enterbirth.clear();
                entergender.clear();
                entermobile.clear();
            }
        } catch (Exception e) {
            feedback.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editSelectedStudent() {
        String id = enterid.getText().trim();
        if (id.isEmpty()) {
            feedback.setText("Please enter a student ID.");
            return;
        }
        String name = entername.getText().trim();
        String birth = enterbirth.getText().trim();
        String gender = entergender.getText().trim();
        String mobile = entermobile.getText().trim();

        if (id.isEmpty() || name.isEmpty() || birth.isEmpty() || gender.isEmpty() || mobile.isEmpty()) {
            feedback.setText("All fields must be filled.");
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
        // Validate birthdate format
        java.sql.Date sqlBirth;
        try {
            sqlBirth = java.sql.Date.valueOf(birth);
        } catch (Exception e) {
            feedback.setText("Birthdate must be in yyyy-MM-dd format.");
            return;
        }

        String sql = "UPDATE Student SET Stu_Name=?, Stu_Birthdate=?, Stu_Gender=?, Stu_Mobile=? WHERE Stu_ID=?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, sqlBirth);
            pstmt.setString(3, gender);
            pstmt.setString(4, mobile);
            pstmt.setString(5, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                feedback.setText("Student updated successfully!");
                initialize();
            } else {
                feedback.setText("Failed to update student.");
            }
        } catch (Exception e) {
            feedback.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteSelectedStudent() {
        String id = enterid.getText().trim();
        if (id.isEmpty()) {
            feedback.setText("Please enter a student ID.");
            return;
        }
        String sql = "DELETE FROM Student WHERE Stu_ID=?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                feedback.setText("Student deleted successfully!");
                initialize();
                // Clear fields
                enterid.clear();
                entername.clear();
                enterbirth.clear();
                entergender.clear();
                entermobile.clear();
            } else {
                feedback.setText("Failed to delete student.");
            }
        } catch (Exception e) {
            feedback.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
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
}
