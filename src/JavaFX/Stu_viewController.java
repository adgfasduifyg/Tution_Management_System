package JavaFX;

import Ultility.SQLConnector;
import Entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
@SuppressWarnings("unchecked")

public class Stu_viewController extends BaseMenuController {

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
    private TableView<Student> studenttable;
    @FXML
    private Label totalstu;

    @FXML
    public void initialize() {
        this.initializeMenuBar();
        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement()) {
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
            setupStudentTable();
            studenttable.setItems(students);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Student");
            if (rs.next()) {
                totalstu.setText("Total Students: " + rs.getInt(1));
            }
        } catch (Exception e) {
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
