package com.bridgelabz.jdbc.service;

import com.bridgelabz.jdbc.entity.EmployeePayrollData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollServiceFileIOService {

    public static String PAYROLL_FILE_NAME = "payroll-file.txt";

    /* method that populates all our data from the list
    * to StringBuffer and then that is used to write into
    * the file*/
    public List<EmployeePayrollData> readData(){
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try{
            Files.lines(new File(PAYROLL_FILE_NAME).toPath())
                    .map(line -> line.trim())
                    .forEach(line -> System.out.println(line));
        }catch (IOException e){
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    public void writeData(List<EmployeePayrollData> employeePayrollList) {
        StringBuffer empBuffer = new StringBuffer();
        employeePayrollList.forEach(employee -> {
            String employeeDataString = employee.toString().concat("\n");
            empBuffer.append(employeeDataString);
        });

        try{
            Files.write(Paths.get(PAYROLL_FILE_NAME), empBuffer.toString().getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void printData() {
        try{
            Files.lines(new File(PAYROLL_FILE_NAME).toPath())
                    .forEach(System.out::println);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /* goes through each line using the lines
    * method in Files and counts it */
    public long countEntries() {
        long entries = 0;
        try{
            entries = Files.lines(new File(PAYROLL_FILE_NAME).toPath()).count();
        }catch (IOException e){
            e.printStackTrace();
        }
        return entries;
    }
}
