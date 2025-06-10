package JavaFX;

import Ultility.LoginService;
import Ultility.SQLConnector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@SuppressWarnings("unused")

public class LoginController {
    @FXML
    private TextField username;

    @FXML
    public PasswordField password;

    @FXML
    private TextField passwordVisible;

    @FXML
    private Button showPasswordBtn;

    @FXML
    public Button login;

    @FXML
    public Label loginlabel; // Optional: add a Label in FXML for messages

    @FXML
    public void initialize() {
        // Optional: initialization logic
        if (loginlabel != null) {
            loginlabel.setText("");
        }
        login.setOnAction(e -> handleLogin());

        // Sync password fields
        password.textProperty().addListener((obs, oldVal, newVal) -> passwordVisible.setText(newVal));
        passwordVisible.textProperty().addListener((obs, oldVal, newVal) -> password.setText(newVal));

        showPasswordBtn.setOnMousePressed(e -> {
            passwordVisible.setText(password.getText());
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);
            password.setVisible(false);
            password.setManaged(false);
        });
        showPasswordBtn.setOnMouseReleased(e -> {
            password.setText(passwordVisible.getText());
            password.setVisible(true);
            password.setManaged(true);
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
        });
    }

    public void handleLogin() {
        String user = username.getText();
        String pass = password.getText();
        LoginService loginService = new LoginService();
        if (loginService.checkLogin(user, pass)) {
            // Check if user is admin
            if (loginService.isAdmin(user)) {
                String staffName = loginService.getStaffName(user);
                if (loginlabel != null) loginlabel.setText("Login successful! Welcome " + staffName + " !");
                // Switch to welcome page
                Platform.runLater(() -> {
                    try {
                        Ultility.Session.setCurrentStaffID(user);
                        MainFX.showWelcomePage();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } else {
                String staffName = loginService.getStaffName(user);
                if (loginlabel != null) loginlabel.setText("Login successful! Welcome " + staffName + " !");
                try {
                    Thread.sleep(2000); // Wait for 2 seconds before switching to welcome page
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                // TODO: Show other dashboard for non-admin users
            }
        } else {
            if (loginlabel != null) loginlabel.setText("Invalid StaffID or Password.");
        }
    }

    
}

