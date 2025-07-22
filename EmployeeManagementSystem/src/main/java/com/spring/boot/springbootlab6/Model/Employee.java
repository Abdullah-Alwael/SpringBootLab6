package com.spring.boot.springbootlab6.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty(message = "ID can not be empty")
    @Size(min = 3, message = "ID must not be less than 3 characters")
    private String iD;

    @NotEmpty(message = "name can not be empty")
    @Size(min = 5, message = "name can not be less than 5 characters")
    @Pattern(regexp = "^\\D*$", message = "name must not contain numbers")
    private String name;

    @Email(message = "email must be valid!")
    private String email;

    @Pattern(regexp = "^05\\d{8}$", message = "phone number must start with 05 and must be 10 digits only!") // must start with 05 and must be 10 digits
    private String phoneNumber;

    @NotNull(message = "age can not be empty")
    @Positive(message = "age must be a positive number")
    @Min(value = 26, message = "age must be older than 25") // more than 25
    private int age;

    @NotEmpty(message = "position must not be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$", message = "position must be" +
            " either supervisor or coordinator only (case sensitive) ")
    private String position;

    @AssertFalse(message = "onLeave must be initially set to false")
    private boolean onLeave;

    @NotNull(message = "hireDate must not be empty")
    @PastOrPresent(message = "hireDate can not be in the future")
    private LocalDate hireDate;

    @NotNull(message = "annualLeave can not be empty")
    @Positive(message = "annualLeave must be a positive number")
    private int annualLeave;
}
