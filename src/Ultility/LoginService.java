package Ultility;


import Ultility.SQLConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@SuppressWarnings("unused")

public class LoginService {

    public boolean checkLogin(String staffID, String password) {
        String sql = "SELECT 1 FROM LOGIN WHERE Staff_ID = ? AND Login_Password = ?";
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staffID);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true if a match is found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAdmin(String staffID) {
        String sql = "SELECT Staff_Role FROM Staff WHERE Staff_ID = ?";
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staffID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return "Admin".equalsIgnoreCase(rs.getString("Staff_Role"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getStaffName(String staffID) {
        String sql = "SELECT Staff_Name FROM Staff WHERE Staff_ID = ?";
        try (Connection conn = SQLConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staffID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Staff_Name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}