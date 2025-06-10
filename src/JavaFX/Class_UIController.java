package JavaFX;

import Ultility.SQLConnector;
import Entity.Class;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@SuppressWarnings("unchecked")
public class Class_UIController extends BaseMenuController {

    @FXML private TableView<Class> fulltable;
    @FXML private Label totalstaff; // Should be renamed to totalclass in FXML for clarity
    @FXML private Label feedback;
    @FXML private TextField namefield;
    @FXML private TextField birthdatefiield; // Should be startdatefield in FXML for clarity
    @FXML private TextField genderfield;     // Should be levelField in FXML for clarity
    @FXML private TextField mobilefield;     // Should be staffidfield in FXML for clarity
    @FXML private TextField idfield;
    @FXML private Button findbtn;
    @FXML private Button editbtn;
    @FXML private Button delbtn;
    @FXML private TextField Enddate;
    @FXML private TextField capacity;
    @FXML private TextField fee;

    private ObservableList<Class> classList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        this.initializeMenuBar();

        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement()) {
            classList.clear();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Class");
            while (rs.next()) {
                classList.add(new Class(
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
            fulltable.setItems(classList);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Class");
            if (rs.next()) {
                totalstaff.setText("Total class in database: " + rs.getInt(1));
            }
        } catch (Exception e) {
            feedback.setText("Error loading classes: " + e.getMessage());
            e.printStackTrace();
        }

        findbtn.setOnAction(e -> findClassById());
        editbtn.setOnAction(e -> editSelectedClass());
        delbtn.setOnAction(e -> deleteSelectedClass());

        // Populate fields when a row is selected
        fulltable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, newSel) -> {
                if (newSel != null) {
                    idfield.setText(newSel.getClassID());
                    namefield.setText(newSel.getClass_Name());
                    birthdatefiield.setText(newSel.getClass_Start_Date().toString());
                    Enddate.setText(newSel.getClass_End_Date().toString());
                    genderfield.setText(newSel.getClass_Level());
                    mobilefield.setText(newSel.getStaffID());
                    capacity.setText(String.valueOf(newSel.getClass_Capacity()));
                    fee.setText(String.valueOf(newSel.getClass_Fee()));
                    feedback.setText("");
                }
            }
        );
    }

    private void setupClassTable() {
        fulltable.getColumns().clear();
        TableColumn<Class, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ClassID"));
        TableColumn<Class, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Class_Name"));
        TableColumn<Class, String> levelCol = new TableColumn<>("Level");
        levelCol.setCellValueFactory(new PropertyValueFactory<>("Class_Level"));
        TableColumn<Class, String> timetableCol = new TableColumn<>("Timetable");
        timetableCol.setCellValueFactory(new PropertyValueFactory<>("Class_Timetable"));
        TableColumn<Class, String> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<>("Class_Start_Date"));
        TableColumn<Class, String> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("Class_End_Date"));
        TableColumn<Class, Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(new PropertyValueFactory<>("Class_Capacity"));
        TableColumn<Class, Integer> feeCol = new TableColumn<>("Fee");
        feeCol.setCellValueFactory(new PropertyValueFactory<>("Class_Fee"));
        TableColumn<Class, String> staffCol = new TableColumn<>("Staff ID");
        staffCol.setCellValueFactory(new PropertyValueFactory<>("StaffID"));
        fulltable.getColumns().addAll(idCol, nameCol, levelCol, timetableCol, startCol, endCol, capCol, feeCol, staffCol);
    }

    private void findClassById() {
        String id = idfield.getText().trim();
        if (id.isEmpty()) {
            feedback.setText("Please enter a class ID.");
            return;
        }
        String sql = "SELECT * FROM Class WHERE Class_ID = ?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                namefield.setText(rs.getString("Class_Name"));
                birthdatefiield.setText(rs.getDate("Class_Start_Date").toLocalDate().toString());
                genderfield.setText(rs.getString("Class_Level"));
                mobilefield.setText(rs.getString("Staff_ID"));
                feedback.setText("Class found.");
            } else {
                feedback.setText("No class found with ID: " + id);
                namefield.clear();
                birthdatefiield.clear();
                genderfield.clear();
                mobilefield.clear();
            }
        } catch (Exception e) {
            feedback.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editSelectedClass() {
        String id = idfield.getText().trim();
        if (id.isEmpty()) {
            feedback.setText("Please enter a class ID.");
            return;
        }
        String name = namefield.getText().trim();
        String start = birthdatefiield.getText().trim();
        String end = Enddate.getText().trim();
        String level = genderfield.getText().trim();
        String staffId = mobilefield.getText().trim();
        String cap = capacity.getText().trim();
        String feeVal = fee.getText().trim();

        if (id.isEmpty() || name.isEmpty() || start.isEmpty() || end.isEmpty() || level.isEmpty() || staffId.isEmpty() || cap.isEmpty() || feeVal.isEmpty()) {
            feedback.setText("All fields must be filled.");
            return;
        }
        java.sql.Date sqlStart;
        java.sql.Date sqlEnd;
        int capInt, feeInt;
        try {
            sqlStart = java.sql.Date.valueOf(start);
            sqlEnd = java.sql.Date.valueOf(end);
            capInt = Integer.parseInt(cap);
            feeInt = Integer.parseInt(feeVal);
        } catch (Exception e) {
            feedback.setText("Dates must be in yyyy-MM-dd format and capacity/fee must be numbers.");
            return;
        }

        String sql = "UPDATE Class SET Class_Name=?, Class_Start_Date=?, Class_End_Date=?, Class_Level=?, Staff_ID=?, Class_Capacity=?, Class_Fee=? WHERE Class_ID=?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, sqlStart);
            pstmt.setDate(3, sqlEnd);
            pstmt.setString(4, level);
            pstmt.setString(5, staffId);
            pstmt.setInt(6, capInt);
            pstmt.setInt(7, feeInt);
            pstmt.setString(8, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                feedback.setText("Class updated successfully!");
                initialize();
            } else {
                feedback.setText("Failed to update class.");
            }
        } catch (Exception e) {
            feedback.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteSelectedClass() {
        String id = idfield.getText().trim();
        if (id.isEmpty()) {
            feedback.setText("Please enter a class ID.");
            return;
        }

        // Check if class is referenced in Enrollment table
        String checkSql = "SELECT COUNT(*) FROM Enrollment WHERE Class_ID = ?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                feedback.setText("Cannot delete: This class has enrolled students. Please remove all enrollments for this class before deleting.");
                return;
            }
        } catch (Exception e) {
            feedback.setText("Error checking enrollments: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        String sql = "DELETE FROM Class WHERE Class_ID=?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                feedback.setText("Class deleted successfully!");
                initialize();
                idfield.clear();
                namefield.clear();
                birthdatefiield.clear();
                Enddate.clear();
                genderfield.clear();
                mobilefield.clear();
                capacity.clear();
                fee.clear();
            } else {
                feedback.setText("Failed to delete class.");
            }
        } catch (Exception e) {
            feedback.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
