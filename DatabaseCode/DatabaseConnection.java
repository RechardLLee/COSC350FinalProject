// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.Driver;
// import java.sql.SQLException;
// import java.util.Enumeration;

// public class Data_test{

// 	public static void main(String[] args) throws ClassNotFoundException, SQLException{



// 		//checking for registered drivers
// 	     System.out.println("Drivers registered initially");
// 	     Enumeration enumm = DriverManager.getDrivers();
// 	     int count=1;
// 	     while(enumm.hasMoreElements()){
// 	         Driver dr=(Driver)enumm.nextElement();
// 	         System.out.println(count+" "+dr);
// 	         count++;
// 	     }
		

// 		// Register the JDBC driver
// 		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

// 		// Specify the connection URL, username, and password
// 		String url = "jdbc:sqlserver://SCOTT/MSSQLSERVER;encrypt=true;trustServerCertificate=true;databaseName=Scott";
// 		String username = "generalLogin";
// 		String password = "password";

// 		// Create the connection
// 		Connection connection = DriverManager.getConnection(url, username, password);

// 	}
// }

import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) throws ClassNotFoundException, SQLException{

    	// Register the JDBC driver
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl =
                "jdbc:sqlserver://gambling-data.database.windows.net:1433;"
                        + "database=SCOTT;"
                        + "user=weckmanscott;"
                        + "password=Isengard1;"
                        + "encrypt=true;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        try (Connection connection = DriverManager.getConnection(connectionUrl);) {
            // Code here.


             if (connection != null) {
                System.out.println("The connection has been successfully established.");
                
                DatabaseMetaData dm = connection.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }
            connection.close();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

        // finally {
        //             try {
        //                 if (connection != null && !connection.isClosed()) {
        //                     connection.close();
        //                 }
        //             } catch (SQLException ex) {
        //                 ex.printStackTrace();
        //             }
        //         }
    }
}