package JavaFX;

import Entity.Staff;
import Entity.Student; 
import Ultility.SQLConnector;
import Ultility.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

@SuppressWarnings({"unused" , "unchecked"})

// password123

public class StaffDetailChangController {

    @FXML
    private TableView<Staff> StaffDetailBefore; // Change from Student to Staff


    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField newname;

    @FXML
    private TextField newmobile;

    @FXML
    private TextField newbirthdate;

    @FXML
    private TextField newgender;

    @FXML
    private TextField newpassword;

    @FXML
    private TextField currentpassword;

    @FXML
    private Button applychanges;

    @FXML
    private Label feedback;

    @FXML
    public void initialize() {
        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement()) {
            ObservableList<Staff> staffList = FXCollections.observableArrayList();
            String staffID = Session.getCurrentStaffID();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Staff WHERE Staff_ID = '" + staffID + "'");
            while (rs.next()) {
                Staff staff = new Staff(
                    rs.getString("Staff_ID"),
                    rs.getString("Staff_Name"),
                    rs.getDate("Staff_Birthdate").toLocalDate(),
                    rs.getString("Staff_Gender"),
                    rs.getString("Staff_Mobile"),
                    rs.getString("Staff_Role"),
                    rs.getDate("Staff_Joining_Date").toLocalDate()
                );
                staffList.add(staff);
            }
            setupStaffTable();
            StaffDetailBefore.setItems(staffList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupStaffTable() {
        StaffDetailBefore.getColumns().clear();
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
        birthCol.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getStaff_Birthdate() != null ? cellData.getValue().getStaff_Birthdate().toString() : ""
        ));
        TableColumn<Staff, String> joinCol = new TableColumn<>("Joining Date");
        joinCol.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getStaff_Joining_Date() != null ? cellData.getValue().getStaff_Joining_Date().toString() : ""
        ));
        StaffDetailBefore.getColumns().addAll(idCol, nameCol, genderCol, mobileCol, roleCol, birthCol, joinCol);
    }

    @FXML
    private void applyChangesToStaff() {
        String staffID = Session.getCurrentStaffID();
        String currPassword = currentpassword.getText();
        String newName = newname.getText();
        String newMobile = newmobile.getText();
        String newBirthdate = newbirthdate.getText();
        String newGender = newgender.getText();
        String newPassword = newpassword.getText();

        feedback.setText(""); // Clear previous feedback

        if (currPassword == null || currPassword.isEmpty()) {
            feedback.setText("Please enter your current password.");
            return;
        }

        // Optional: Validate mobile number format
        if (!newMobile.isEmpty() && (!newMobile.matches("0\\d{9}"))) {
            feedback.setText("Mobile number must start with 0 and be 10 digits.");
            return;
        }

        // Optional: Validate gender
        if (!newGender.isEmpty() && 
            !(newGender.equalsIgnoreCase("Male") || newGender.equalsIgnoreCase("Female") || newGender.equalsIgnoreCase("Other"))) {
            feedback.setText("Gender must be Male, Female, or Other.");
            return;
        }

        // Optional: Validate birthdate format (yyyy-MM-dd)
        if (!newBirthdate.isEmpty()) {
            try {
                LocalDate.parse(newBirthdate);
            } catch (Exception e) {
                feedback.setText("Birthdate must be in yyyy-MM-dd format.");
                return;
            }
        }

        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            // Check current password
            ResultSet rs = stmt.executeQuery("SELECT Login_Password FROM LOGIN WHERE Staff_ID = '" + staffID + "'");
            if (rs.next()) {
                String dbPassword = rs.getString("Login_Password");
                if (!currPassword.equals(dbPassword)) {
                    feedback.setText("Current password is incorrect.");
                    return;
                }
            } else {
                feedback.setText("User not found.");
                return;
            }

            // Build update query dynamically
            StringBuilder updateStaff = new StringBuilder("UPDATE Staff SET ");
            boolean needComma = false;
            if (!newName.isEmpty()) {
                updateStaff.append("Staff_Name = '").append(newName).append("'");
                needComma = true;
            }
            if (!newMobile.isEmpty()) {
                if (needComma) updateStaff.append(", ");
                updateStaff.append("Staff_Mobile = '").append(newMobile).append("'");
                needComma = true;
            }
            if (!newBirthdate.isEmpty()) {
                if (needComma) updateStaff.append(", ");
                updateStaff.append("Staff_Birthdate = '").append(newBirthdate).append("'");
                needComma = true;
            }
            if (!newGender.isEmpty()) {
                if (needComma) updateStaff.append(", ");
                updateStaff.append("Staff_Gender = '").append(newGender).append("'");
            }
            updateStaff.append(" WHERE Staff_ID = '").append(staffID).append("'");

            // Only update if at least one field is filled
            if (updateStaff.toString().contains("=")) {
                stmt.executeUpdate(updateStaff.toString());
            }

            // Update password if provided
            if (!newPassword.isEmpty()) {
                stmt.executeUpdate("UPDATE LOGIN SET Login_Password = '" + newPassword + "' WHERE Staff_ID = '" + staffID + "'");
            }

            welcomeLabel.setText("Information updated successfully!");
            feedback.setText(""); // Clear feedback on success
            initialize(); // Refresh table

        } catch (Exception e) {
            e.printStackTrace();
            feedback.setText("Error updating information.");
        }
    }

}
