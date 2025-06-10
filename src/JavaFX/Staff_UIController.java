package JavaFX;

import Ultility.SQLConnector;
import Entity.Staff;
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
public class Staff_UIController extends BaseMenuController {

    @FXML private TableView<Staff> fulltable;
    @FXML private Label totalstaff;
    @FXML private Label feedback;
    @FXML private TextField namefield;
    @FXML private TextField birthdatefiield;
    @FXML private TextField genderfield;
    @FXML private TextField mobilefield;
    @FXML private TextField idfield;
    @FXML private Button findbtn;
    @FXML private Button editbtn;
    @FXML private Button delbtn;

    private ObservableList<Staff> staffList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        this.initializeMenuBar();

        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement()) {
            staffList.clear();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Staff");
            while (rs.next()) {
                staffList.add(new Staff(
                        rs.getString("Staff_ID"),
                        rs.getString("Staff_Name"),
                        rs.getDate("Staff_Birthdate").toLocalDate(),
                        rs.getString("Staff_Gender"),
                        rs.getString("Staff_Mobile"),
                        rs.getString("Staff_Role"),
                        rs.getDate("Staff_Joining_Date").toLocalDate()
                ));
            }
            setupStaffTable();
            fulltable.setItems(staffList);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM Staff");
            if (rs.next()) {
                totalstaff.setText("Total staff in database: " + rs.getInt(1));
            }
        } catch (Exception e) {
            feedback.setText("Error loading staff: " + e.getMessage());
            e.printStackTrace();
        }

        findbtn.setOnAction(e -> findStaffById());
        editbtn.setOnAction(e -> editSelectedStaff());
        delbtn.setOnAction(e -> deleteSelectedStaff());

        // Populate fields when a row is selected
        fulltable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, newSel) -> {
                if (newSel != null) {
                    idfield.setText(newSel.getStaffID());
                    namefield.setText(newSel.getStaff_Name());
                    birthdatefiield.setText(newSel.getStaff_Birthdate().toString());
                    genderfield.setText(newSel.getStaff_Gender());
                    mobilefield.setText(newSel.getStaff_Mobile());
                    feedback.setText("");
                }
            }
        );
    }

    private void setupStaffTable() {
        fulltable.getColumns().clear();
        TableColumn<Staff, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("StaffID"));
        TableColumn<Staff, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Name"));
        TableColumn<Staff, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Gender"));
        TableColumn<Staff, String> mobileCol = new TableColumn<>("Mobile");
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Mobile"));
        TableColumn<Staff, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Role"));
        TableColumn<Staff, String> birthCol = new TableColumn<>("Birthdate");
        birthCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Birthdate"));
        TableColumn<Staff, String> joinCol = new TableColumn<>("Joining Date");
        joinCol.setCellValueFactory(new PropertyValueFactory<>("Staff_Joining_Date"));
        fulltable.getColumns().addAll(idCol, nameCol, genderCol, mobileCol, roleCol, birthCol, joinCol);
    }

    private void findStaffById() {
        String id = idfield.getText().trim();
        if (id.isEmpty()) {
            feedback.setText("Please enter a staff ID.");
            return;
        }
        String sql = "SELECT * FROM Staff WHERE Staff_ID = ?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                namefield.setText(rs.getString("Staff_Name"));
                birthdatefiield.setText(rs.getDate("Staff_Birthdate").toLocalDate().toString());
                genderfield.setText(rs.getString("Staff_Gender"));
                mobilefield.setText(rs.getString("Staff_Mobile"));
                feedback.setText("Staff found.");
            } else {
                feedback.setText("No staff found with ID: " + id);
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

    private void editSelectedStaff() {
        String id = idfield.getText().trim();
        if (id.isEmpty()) {
            feedback.setText("Please enter a staff ID.");
            return;
        }
        String name = namefield.getText().trim();
        String birth = birthdatefiield.getText().trim();
        String gender = genderfield.getText().trim();
        String mobile = mobilefield.getText().trim();

        if (id.isEmpty() || name.isEmpty() || birth.isEmpty() || gender.isEmpty() || mobile.isEmpty()) {
            feedback.setText("All fields must be filled.");
            return;
        }
        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female") && !gender.equalsIgnoreCase("Other")) {
            feedback.setText("Gender must be 'Male', 'Female', or 'Other'.");
            return;
        }
        if (!mobile.matches("0\\d{9}")) {
            feedback.setText("Mobile must be 10 digits and start with 0.");
            return;
        }
        java.sql.Date sqlBirth;
        try {
            sqlBirth = java.sql.Date.valueOf(birth);
        } catch (Exception e) {
            feedback.setText("Birthdate must be in yyyy-MM-dd format.");
            return;
        }

        String sql = "UPDATE Staff SET Staff_Name=?, Staff_Birthdate=?, Staff_Gender=?, Staff_Mobile=? WHERE Staff_ID=?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, sqlBirth);
            pstmt.setString(3, gender);
            pstmt.setString(4, mobile);
            pstmt.setString(5, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                feedback.setText("Staff updated successfully!");
                initialize();
            } else {
                feedback.setText("Failed to update staff.");
            }
        } catch (Exception e) {
            feedback.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteSelectedStaff() {
        String id = idfield.getText().trim();
        if (id.isEmpty()) {
            feedback.setText("Please enter a staff ID.");
            return;
        }

        // Check if staff is referenced in Class table
        String checkSql = "SELECT COUNT(*) FROM Class WHERE Staff_ID = ?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                feedback.setText("Cannot delete: This staff is currently assigned to one or more classes. Please reassign or remove them from all classes before deleting.");
                return;
            }
        } catch (Exception e) {
            feedback.setText("Error checking staff-class assignment: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        String sql = "DELETE FROM Staff WHERE Staff_ID=?";
        try (Connection conn = SQLConnector.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                feedback.setText("Staff deleted successfully!");
                initialize();
                idfield.clear();
                namefield.clear();
                birthdatefiield.clear();
                genderfield.clear();
                mobilefield.clear();
            } else {
                feedback.setText("Failed to delete staff.");
            }
        } catch (Exception e) {
            feedback.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
