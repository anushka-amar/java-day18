package practiceconnection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class DBDemo {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String username = "root";
        String password = "kiit";

        Connection connection; //establishing java sql connection

        //checking if mysql driver is loaded or not
        try{
            Class.forName("com.mysql.cj.jdbc.Driver"); //returns the Class object associated with the class or interface provided
            System.out.println("Driver loaded");
        }catch (ClassNotFoundException e){
            throw new IllegalStateException("cannot find the driver in the classpath!", e);
        }
        listDrivers();

        try{ //connecting to our db
            System.out.println("Connecting to database:"+jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection is successful!!!!"+ connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void listDrivers(){
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()){
            Driver driver = (Driver) driverList.nextElement();
            System.out.println(" "+driver.getClass().getName());
        }
    }
}