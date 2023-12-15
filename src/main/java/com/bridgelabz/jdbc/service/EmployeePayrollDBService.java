package com.bridgelabz.jdbc.service;

import com.bridgelabz.jdbc.entity.EmployeePayrollData;

import java.net.ConnectException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

    /* UC1 - established connection */
    private static Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "kiit";
        Connection connection;
        System.out.println("Connecting to database: "+jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is Successful!!!"+connection);
        return connection;
    }

    /* UC2 - retrieve employee payroll table from database */
    public List<EmployeePayrollData> readData(){
        String sql = "SELECT * FROM employee_payroll; ";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement(); // this statement is used to execute our sql queries
            ResultSet resultSet = statement.executeQuery(sql); //executing the query on statement and adding to result

            while (resultSet.next()){ //enumerating through the result set and populating our  employeePayroll object
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
            connection.close(); //if we close the connection we don't need to close the statement or resultSet separately
        } catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }
}
