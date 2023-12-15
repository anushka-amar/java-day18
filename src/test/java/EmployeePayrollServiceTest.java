import com.bridgelabz.jdbc.entity.EmployeePayrollData;
import com.bridgelabz.jdbc.service.EmployeePayrollService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.bridgelabz.jdbc.service.EmployeePayrollService.IOService.DB_IO;
import static com.bridgelabz.jdbc.service.EmployeePayrollService.IOService.FILE_IO;

public class EmployeePayrollServiceTest {
    Assertions Assert;

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

    @Test
    public void givenFileOnReadingFromFileShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        long entries = employeePayrollService.readEmployeePayrollData(FILE_IO);
        Assert.assertEquals(3, entries);
    }

//    @Test
//    public void givenEmployeePayrollINDB_WhenRetrieved_ShouldMatchEmployeeCount(){
//        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
//        List<EmployeePayrollData> employeePayrollDataList = employeePayrollService.readEmployeePayrollData(DB_IO);
//        Assert.assertEquals(3, employeePayrollDataList.size());
//    }
}
