package com.bridgelabz.jdbc.service;

import com.bridgelabz.jdbc.entity.EmployeePayrollData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO}
    private List<EmployeePayrollData> employeePayrollList;
    public EmployeePayrollService(){}
    public EmployeePayrollService(List<EmployeePayrollData>
                                          employeePayrollList) {
        this.employeePayrollList = employeePayrollList;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
    }

    /* method to read rmp data from
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

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService){
        if(ioService.equals(IOService.DB_IO)){
            this.employeePayrollList = new EmployeePayrollDBService().readData();
        }
        return this.employeePayrollList;
    }

    public void writeEmployeePayrollData(IOService ioService){
        if(ioService.equals(IOService.CONSOLE_IO)){
            System.out.println("\nWriting Employee Payroll roaster to Console\n"+ employeePayrollList);

        }else if(ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollServiceFileIOService().writeData(employeePayrollList);
        }
    }

//    public long readEmployeePayrollData(IOService ioService){
//        if(ioService.equals(IOService.FILE_IO)){
//            this.employeePayrollList = new EmployeePayrollServiceFileIOService().readData();
//        }
//        return employeePayrollList.size();
//    }

    public void printData(IOService ioService){
        if(ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollServiceFileIOService().printData();
        }
    }

    public long countEntries(IOService ioService){
        if(ioService.equals(IOService.FILE_IO)){
            return new EmployeePayrollServiceFileIOService().countEntries();
        }
        return 0;
    }
}
