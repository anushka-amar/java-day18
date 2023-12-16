package com.bridgelabz.jdbc.service;

import com.bridgelabz.jdbc.entity.EmployeePayrollData;

import java.net.ConnectException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {

    private PreparedStatement employeePayrollDataStatement; //makes our class singleton
    private static EmployeePayrollDBService employeePayrollDBService;

    private EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null) {
            employeePayrollDBService = new EmployeePayrollDBService();
        }
        return employeePayrollDBService;
    }

    /* UC1 - established connection */
    private static Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "kiit";
        Connection connection;
        System.out.println("Connecting to database: " + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is Successful!!!" + connection);
        return connection;
    }

    /* UC2 - retrieve employee payroll table from database */
    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll; ";
        return this.getEmployeePayrollDataUsingDB(sql);
    }


    /* UC-3 update salary */
    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    public int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql = String.format("Update employee_payroll set salary = %.2f where name = '%s", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /* UC-5 method to retrieve employees starting at a particular date */
    public List<EmployeePayrollData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("SELECT * FROM employee_payroll WHERE start BETWEEN '%s' AND '%s'",
                Date.valueOf(startDate), Date.valueOf(endDate));

        return this.getEmployeePayrollDataUsingDB(sql);
    }

    /* UC-6 find average salary by gender */
    public Map<String, Double> getAverageSalaryByGender() {
     String sql = "SELECT gender, AVG(salary) as avg_salary FROM employee_payroll GROUP BY gender;";
     Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement(); // this statement is used to execute our sql queries
            ResultSet resultSet = statement.executeQuery(sql); //executing the query on statement and adding to result
            while (resultSet.next()){
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("avg_salary");
                genderToAverageSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }

    /* UC-7 ability to add new employee to DB */
    public EmployeePayrollData addEmployeeToPayrollUC7(String name, double salary, LocalDate start, String gender) {
        int employeeId = -1;
        EmployeePayrollData employeePayrollData = null;
        String sql = String.format("INSERT INTO employee_payroll (name, gender, salary, start)"+
                " VALUE ( '%s', '%s', '%s', '%s' );", name, gender, salary, Date.valueOf(start));

        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if(rowAffected == 1){
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) employeeId = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData(employeeId, name, salary, start);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollData;
    }

    /* UC-8 to add to new table payroll_details
    when a new employee is added to payroll
     */
    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate start, String gender) {
        int employeeId = -1;
        Connection connection = null;
        EmployeePayrollData employeePayrollData = null;
        try{
            connection = this.getConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT INTO employee_payroll (name, gender, salary, start) VALUE "+
                    "( '%s', '%s', '%s', '%s' )", name, gender, salary, Date.valueOf(start));
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if(rowAffected == 1){
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()){
                    employeeId = resultSet.getInt(1);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()){
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format("INSERT INTO payroll_details "+
                    "( employee_id, basic_pay, deductions, taxable_pay, tax, net_pay) VALUE "+
                    "( %s, %s, %s, %s, %s, %s )", employeeId, salary, deductions, taxablePay, tax, netPay);
            int rowAffected = statement.executeUpdate(sql);
            if(rowAffected == 1){
                employeePayrollData = new EmployeePayrollData(employeeId, name, salary, start);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollData;
    }

    /* method to execute sql queries and populate our list */
    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement(); // this statement is used to execute our sql queries
            ResultSet resultSet = statement.executeQuery(sql); //executing the query on statement and adding to result
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollDataList = null;
        if (this.employeePayrollDataStatement == null) {
            this.preparedStatementForEmployeeData();
        }
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private void preparedStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}