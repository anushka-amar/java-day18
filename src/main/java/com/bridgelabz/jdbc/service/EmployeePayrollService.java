package com.bridgelabz.jdbc.service;

import com.bridgelabz.jdbc.entity.EmployeePayrollData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {


    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO}
    private List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDBService employeePayrollDBService;
    public EmployeePayrollService(){
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }
    public EmployeePayrollService(List<EmployeePayrollData>
                                          employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
    }

    /* method to read emp data from
       console and adds to the list */
    public void readEmployeePayrollData(Scanner consoleInputReader){
        System.out.println("Enter Employee ID: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee name: ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee salary");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary)); //asking for the emp details and creating object out of it and adding to list
    }

    /* method to read and populate our object */
    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService){
        if(ioService.equals(IOService.DB_IO)){
            this.employeePayrollList = employeePayrollDBService.readData();
        }
        return this.employeePayrollList;
    }


    /* method to retrieve employees joining in a particular date */
    public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService ioService,
                                                                     LocalDate startDate, LocalDate endDate) {
        if(ioService.equals(IOService.DB_IO)){
            return employeePayrollDBService.getEmployeePayrollForDateRange(startDate, endDate);
        }
        return null;
    }

    /* method to check sync of the object w DB */
    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollList.get(0).equals(getEmployeePayrollData(name));
    }

    /* UC-3 update salary */
    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if(result == 0) return;

        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if(employeePayrollData != null){
            employeePayrollData.salary = salary;
        }
    }

    /* converts list into stream and filters
    * by name, if null is returned we don't update salary
    * else we update salary */
    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    /* method to write to file */
    public void writeEmployeePayrollData(IOService ioService){
        if(ioService.equals(IOService.CONSOLE_IO)){
            System.out.println("\nWriting Employee Payroll roaster to Console\n"+ employeePayrollList);

        }else if(ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollServiceFileIOService().writeData(employeePayrollList);
        }
    }

    /* method to print data in file */
    public void printData(IOService ioService){
        if(ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollServiceFileIOService().printData();
        }
    }

    /* method to count the total entries in the file */
    public long countEntries(IOService ioService){
        if(ioService.equals(IOService.FILE_IO)){
            return new EmployeePayrollServiceFileIOService().countEntries();
        }
        return 0;
    }
}