package JavaFX;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public abstract class BaseMenuController {
    // File menu
    @FXML protected MenuItem sessout;
    @FXML protected MenuItem progquit;

    // Student menu
    @FXML protected MenuItem viewstudent;
    @FXML protected MenuItem addstudent;
    @FXML protected MenuItem editstudent;
    @FXML protected MenuItem delstudent;

    // Staff menu
    @FXML protected MenuItem viewstaff;
    @FXML protected MenuItem addstaff;
    @FXML protected MenuItem editstaff;
    @FXML protected MenuItem delstaff;

    // Class menu
    @FXML protected MenuItem viewclass;
    @FXML protected MenuItem addclass;
    @FXML protected MenuItem editclass;
    @FXML protected MenuItem delclass;

    // Enrollment menu
    @FXML protected MenuItem viewenroll;
    @FXML protected MenuItem addenroll;
    @FXML protected MenuItem editenrol;
    @FXML protected MenuItem delenroll;

    // Bill menu
    @FXML protected MenuItem viewbill;
    @FXML protected MenuItem addbill;
    @FXML protected MenuItem editbill;
    @FXML protected MenuItem delbill;

    @FXML
    public void initializeMenuBar() {
        if (sessout != null) {
            sessout.setOnAction(e -> {
                Ultility.Session.clearSession();
                try {
                    MainFX.showLoginPage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
        if (progquit != null) {
            progquit.setOnAction(e -> System.exit(0));
        }
        if (viewstudent != null) {
            viewstudent.setOnAction(e -> {
                try {
                    MainFX.showviewstudent();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
        if (addstudent != null) {
            addstudent.setOnAction(e -> {
                try {
                    MainFX.showAddStudentPage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        if (editstudent != null) {
            editstudent.setOnAction(e -> {
                try {
                    MainFX.showEditDeleteStudentPage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        if (delstudent != null) {
            delstudent.setOnAction(e -> {
                try {
                    MainFX.showEditDeleteStudentPage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        // Add similar handlers for other menu items as needed
    }
}