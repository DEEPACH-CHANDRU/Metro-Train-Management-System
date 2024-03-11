import java.sql.*;
import java.util.Scanner;

public class MetroTrainManagementSystem {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/metro_db";
    static final String USER = "username";
    static final String PASS = "password";

    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;
    static PreparedStatement pstmt = null;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            boolean running = true;
            while (running) {
                System.out.println("Metro Train Management System");
                System.out.println("1. Add Passenger");
                System.out.println("2. View Passenger Details");
                System.out.println("3. Update Passenger Details");
                System.out.println("4. Delete Passenger");
                System.out.println("5. Exit Train");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addPassenger();
                        break;
                    case 2:
                        viewPassengerDetails();
                        break;
                    case 3:
                        updatePassengerDetails();
                        break;
                    case 4:
                        deletePassenger();
                        break;
                    case 5:
                        exitTrain();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private static void addPassenger() throws SQLException {
        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine();
        System.out.print("Enter source: ");
        String source = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter entry time (YYYY-MM-DD HH:MM:SS): ");
        String entryTime = scanner.nextLine();
        System.out.print("Enter exit time (YYYY-MM-DD HH:MM:SS): ");
        String exitTime = scanner.nextLine();
        System.out.print("Has the passenger exited the train? (Y/N): ");
        String exitStatus = scanner.nextLine();

        String sql = "INSERT INTO passengers (name, source, destination, entry_time, exit_time, exit_status) VALUES (?, ?, ?, ?, ?, ?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, source);
        pstmt.setString(3, destination);
        pstmt.setString(4, entryTime);
        pstmt.setString(5, exitTime);
        pstmt.setString(6, exitStatus);
        pstmt.executeUpdate();
        System.out.println("Passenger added successfully.");
    }

    private static void viewPassengerDetails() throws SQLException {
        System.out.print("Enter passenger ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "SELECT * FROM passengers WHERE id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("Passenger Details:");
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Source: " + rs.getString("source"));
            System.out.println("Destination: " + rs.getString("destination"));
            System.out.println("Entry Time: " + rs.getString("entry_time"));
            System.out.println("Exit Time: " + rs.getString("exit_time"));
            System.out.println("Exit Status: " + rs.getString("exit_status"));
        } else {
            System.out.println("Passenger not found!");
        }
    }

    private static void updatePassengerDetails() throws SQLException {
        System.out.print("Enter passenger ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new source: ");
        String newSource = scanner.nextLine();
        System.out.print("Enter new destination: ");
        String newDestination = scanner.nextLine();
        System.out.print("Enter new entry time (YYYY-MM-DD HH:MM:SS): ");
        String newEntryTime = scanner.nextLine();
        System.out.print("Enter new exit time (YYYY-MM-DD HH:MM:SS): ");
        String newExitTime = scanner.nextLine();
        System.out.print("Has the passenger exited the train? (Y/N): ");
        String newExitStatus = scanner.nextLine();
		 long entryMillis=Timestamp.valueOf(newEntryTime).getTime();
        long exitMillis=Timestamp.valueOf(newExitTime).getTime();
        long durationInMillis=exitMillis-entryMillis;
        double durationInHours=durationInMillis/(1000.0*60*60);
        if(durationInHours>1.20) {
        	System.out.println("maximum duration exceeded,please ensure the journey is within 1 hour 20 minutes,and  inform the station officer please help to exit a station ");
        	  System.out.println();
        	return;
        }

        String sql = "UPDATE passengers SET source=?, destination=?, entry_time=?, exit_time=?, exit_status=? WHERE id=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newSource);
        pstmt.setString(2, newDestination);
        pstmt.setString(3, newEntryTime);
        pstmt.setString(4, newExitTime);
        pstmt.setString(5, newExitStatus);
        pstmt.setInt(6, id);
        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Passenger details updated successfully.");
        } else {
            System.out.println("Passenger not found!");
        }
    }

    private static void deletePassenger() throws SQLException {
        System.out.print("Enter passenger ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "DELETE FROM passengers WHERE id=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        int rowsDeleted = pstmt.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("Passenger removed successfully.");
        } else {
            System.out.println("Passenger not found!");
        }
    }

    private static void exitTrain() throws SQLException {
        System.out.print("Enter passenger ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "UPDATE passengers SET exit_status='Y' WHERE id=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Passenger exit status updated successfully.");
        } else {
            System.out.println("Passenger not found!");
        }
    }
}
