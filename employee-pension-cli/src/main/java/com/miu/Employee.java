package com.miu;

import java.time.LocalDate;

public class Employee {
    public long employeeId;
    public String firstName;
    public String lastName;
    public LocalDate employmentDate;
    public double yearlySalary;
    public PensionPlan pensionPlan; // nullable


    public LocalDate getEmploymentDate() {
        return employmentDate;
    }
}
