package JavaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLoginPage();
        
    }

    public static void showLoginPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("LoginFX.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 1280, 740));
        primaryStage.show();
    }

    public static void showWelcomePage() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("WelcomePage.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }

    public static void showStaffDetailChangePage() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("StaffDetailChangeFX.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Staff Detail");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }

    public static void showAddStudentPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("Stu_add.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Add Student");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }

    public static void showviewstudent() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("Stu_view.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("View Student");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }

    public static void showEditDeleteStudentPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("Stu_edit_del.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Edit/Delete Student");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}