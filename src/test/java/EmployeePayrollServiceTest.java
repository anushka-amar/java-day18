import com.bridgelabz.jdbc.entity.EmployeePayrollData;
import com.bridgelabz.jdbc.service.EmployeePayrollService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.jdbc.service.EmployeePayrollService.IOService.DB_IO;
import static com.bridgelabz.jdbc.service.EmployeePayrollService.IOService.FILE_IO;

public class EmployeePayrollServiceTest {
    Assertions Assert;

    /* File IO Test */
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries(){
        EmployeePayrollData[] arrayOfEmp = {
                new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
                new EmployeePayrollData(2, "Bill Gates", 200000.0),
                new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0)
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));
        employeePayrollService.writeEmployeePayrollData(FILE_IO);
        employeePayrollService.printData(FILE_IO);
        long entries = employeePayrollService.countEntries(FILE_IO);
        Assert.assertEquals(3, entries);
    }

//    @Test
//    public void givenFileOnReadingFromFileShouldMatchEmployeeCount(){
//        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
//        long entries = employeePayrollService.readEmployeePayrollData(FILE_IO);
//        Assert.assertEquals(3, entries);
//    }

    /* UC2 JDBC */
    @Test
    public void givenEmployeePayrollINDB_WhenRetrieved_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    /* UC3 JDBC */
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldUpdateInDB(){

        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 4000000.0);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    /* UC5 JDBC */
    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2018, 01, 03);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollForDateRange(DB_IO, startDate, endDate);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    /* UC6 JDBC */
    public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("M").equals(200000.00)&&
                averageSalaryByGender.get("F").equals(5000000.00));
    }
}