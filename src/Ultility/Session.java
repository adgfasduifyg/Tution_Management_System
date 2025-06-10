package Ultility;

public class Session {
    private static String currentStaffID;

    public static void setCurrentStaffID(String staffID) {
        currentStaffID = staffID;
    }

    public static String getCurrentStaffID() {
        return currentStaffID;
    }

    public static void clearSession() {
        currentStaffID = null;
    }
}