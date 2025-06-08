package LoginFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {
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
    public Label label; // Optional: add a Label in FXML for messages

    @FXML
    public void initialize() {
        // Optional: initialization logic
        if (label != null) {
            label.setText("");
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
        System.out.println("Username: " + user);
        System.out.println("Password: " + pass);
        if (label != null) {
            label.setText("Login attempted for " + user);
        }
    }
}

