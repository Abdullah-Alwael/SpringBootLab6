package com.spring.boot.springbootlab6.Controller;

import com.spring.boot.springbootlab6.Api.ApiResponse;
import com.spring.boot.springbootlab6.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    //    1. Get all employees: Retrieves a list of all employees.
    @GetMapping("/list")
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    //    2. Add a new employee: Adds a new employee to the system.
    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getFieldError().getDefaultMessage());
        }
        employees.add(employee);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee added successfully"));
    }

    //    3. Update an employee: Updates an existing employee's information.
    @PutMapping("/update/{iD}")
    public ResponseEntity<?> updateEmployee(@PathVariable String iD, @Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getFieldError().getDefaultMessage());
        }
        boolean updated = false;
        employee.setID(iD); // make sure the user does not update the id, it will always assume the same iD,
        // the iD in the body will always be ignored i.e. {"iD":200 . . .} with param iD = 100 will change back to 100

        for (Employee e : employees) {
            if (e.getID().equals(iD)) {
                employees.set(employees.indexOf(e), employee);
                updated = true;
                break;
            }
        }
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error, employee not found"));
        }
    }

    //    4. Delete an employee: Deletes an employee from the system.
//    Note:
//       ▪ {Verify that the employee exists.}
    @DeleteMapping("/delete/{iD}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String iD) {
        boolean deleted = false;

        for (Employee e : employees) {
            if (e.getID().equals(iD)) {
                employees.remove(e);
                deleted = true;
                break;
            }
        }

        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error, employee not found"));
        }
    }

    //    5. Search Employees by Position: Retrieves a list of employees based on their
//    position (supervisor or coordinator).
//    Note:
//       ▪ {Ensure that the position parameter is valid (either "supervisor" or "coordinator").}
    @GetMapping("filter/by-position/{position}")
    public ResponseEntity<?> searchEmployeeByPosition(@PathVariable String position) {
        if (!position.matches("^(supervisor|coordinator)$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("position must be" +
                    " either supervisor or coordinator only (case sensitive) "));
        }

        ArrayList<Employee> foundByPosition = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getPosition().equals(position)) {
                foundByPosition.add(e);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(foundByPosition);
    }

//    6. Get Employees by Age Range: Retrieves a list of employees within a specified
//    age range.
//    Note:
//       ▪ {Ensure that minAge and maxAge are valid age values.}

    @GetMapping("/filter/by-age/{minAge}/{maxAge}")
    public ResponseEntity<?> searchEmployeeByAge(@PathVariable int minAge, @PathVariable int maxAge) {
        if (minAge < 26 || maxAge < 26) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Age must be older than 25"));
        }

        if (maxAge < minAge) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("max age can not be " +
                    "less than min age"));
        }

        ArrayList<Employee> foundByAge = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getAge() >= minAge && e.getAge() <= maxAge) { // inclusive
                foundByAge.add(e);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(foundByAge);
    }

//    7. Apply for annual leave: Allow employees to apply for annual leave.
//    Note:
//       ▪ {Verify that the employee exists.}
//       ▪ {The employee must not be on leave (the onLeave flag must be false).}
//       ▪ {The employee must have at least one day of annual leave remaining.}
//       ▪ Behavior:
//       ▪ {Set the onLeave flag to true.}
//       ▪ {Reduce the annualLeave by 1.}

    @PutMapping("/apply-for-annual-leave/{iD}")
    public ResponseEntity<?> applyForAnnualLeave(@PathVariable String iD) {
        boolean employeeExists = false;

        for (Employee e : employees) {
            if (e.getID().equals(iD)) {
                if (e.isOnLeave()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error, the employee must" +
                            " not already be on leave"));
                }

                if (e.getAnnualLeave() < 1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error, the employee " +
                            "must have at least one day of annual leave remaining"));
                }

                e.setOnLeave(true);
                e.setAnnualLeave(e.getAnnualLeave() - 1);

                employeeExists = true;
                break;
            }
        }
        if (employeeExists) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee applied for annual" +
                    " leave successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error, employee not found"));
        }
    }

//    8. Get Employees with No Annual Leave: Retrieves a list of employees who have
//          used up all their annual leave.

    @GetMapping("/filter/without-annual-leave")
    public ResponseEntity<?> listEmployeesWithoutAnnualLeave() {
        ArrayList<Employee> withoutAnnualLeave = new ArrayList<>();

        for (Employee e : employees) {
            if (e.getAnnualLeave() < 1) {
                withoutAnnualLeave.add(e);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(withoutAnnualLeave);
    }

//    9. Promote Employee: Allows a supervisor to promote an employee to the position
//          of supervisor if they meet certain criteria.
//
//   Note:
//       ▪ {Verify that the employee with the specified ID exists.}
//       ▪ {Ensure that the requester (user making the request) is a supervisor.}
//       ▪ {Validate that the employee's age is at least 30 years.}
//       ▪ {Confirm that the employee is not currently on leave.}
//       ▪ {Change the employee's position to "supervisor" if they meet the criteria.}
//    Extra:
//       ▪ {check if employee's position is already a "supervisor"}

    @PutMapping("/promote/{employeeID}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String employeeID, @Valid @RequestBody Employee requester,
                                             Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getFieldError().getDefaultMessage());
        }

        boolean employeeExists = false;
        boolean supervisorExists = false;

        for (Employee e : employees) { // check for supervisor
            if (e.getID().equals(requester.getID())) {
                if (!e.getPosition().equals("supervisor")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error, requester" +
                            " must be a supervisor"));
                }
                supervisorExists = true;
                break;
            }
        }

        if (!supervisorExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error, supervisor does not exist"));
        }

        for (Employee e : employees) { // check for employee
            if (e.getID().equals(employeeID)) {
                if (e.getPosition().equals("supervisor")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error, employee is" +
                            " already a supervisor"));
                }
                if (e.getAge() < 30) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error, employee " +
                            "age must be at least 30 years old"));
                }
                if (e.isOnLeave()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error, employee " +
                            "is on leave."));
                }

                e.setPosition("supervisor");
                employeeExists = true;
                break;
            }
        }
        if (employeeExists) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee promoted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error, employee not found"));
        }
    }

}