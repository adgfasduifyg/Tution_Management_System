import Ultility.*;
@SuppressWarnings("unused")

public class nah {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        // You can add more functionality here if needed
        try {
            SQLConnector.getConnection();
            System.out.println("connection success");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
