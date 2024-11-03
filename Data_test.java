import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Enumeration;

public class Data_test{

	public static void main(String[] args) throws ClassNotFoundException, SQLException{



		//checking for registered drivers
	     System.out.println("Drivers registered initially");
	     Enumeration enumm = DriverManager.getDrivers();
	     int count=1;
	     while(enumm.hasMoreElements()){
	         Driver dr=(Driver)enumm.nextElement();
	         System.out.println(count+" "+dr);
	         count++;
	     }
		

		// Register the JDBC driver
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		// Specify the connection URL, username, and password
		String url = "jdbc:sqlserver://SCOTT";
		String username = "weckmanscott";
		String password = "Isengard";

		// Create the connection
		Connection connection = DriverManager.getConnection(url, username, password);

	}
}