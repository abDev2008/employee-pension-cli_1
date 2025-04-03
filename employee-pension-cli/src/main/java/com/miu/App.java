package com.miu;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class App {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();

        Employee e1 = new Employee();
        e1.employeeId = 1;
        e1.firstName = "Daniel";
        e1.lastName = "Agar";
        e1.employmentDate = LocalDate.parse("2018-01-17");
        e1.yearlySalary = 105945.50;
        PensionPlan p1 = new PensionPlan();
        p1.planReferenceNumber = "EX1089";
        p1.enrollmentDate = LocalDate.parse("2023-01-17");
        p1.monthlyContribution = 100.00;
        e1.pensionPlan = p1;
        employees.add(e1);

        Employee e2 = new Employee();
        e2.employeeId = 2;
        e2.firstName = "Benard";
        e2.lastName = "Shaw";
        e2.employmentDate = LocalDate.parse("2022-09-03");
        e2.yearlySalary = 197750.00;
        e2.pensionPlan = null;
        employees.add(e2);

        Employee e3 = new Employee();
        e3.employeeId = 3;
        e3.firstName = "Carly";
        e3.lastName = "Agar";
        e3.employmentDate = LocalDate.parse("2014-05-16");
        e3.yearlySalary = 842000.75;
        PensionPlan p3 = new PensionPlan();
        p3.planReferenceNumber = "SM2307";
        p3.enrollmentDate = LocalDate.parse("2019-11-04");
        p3.monthlyContribution = 1555.50;
        e3.pensionPlan = p3;
        employees.add(e3);

        Employee e4 = new Employee();
        e4.employeeId = 4;
        e4.firstName = "Wesley";
        e4.lastName = "Schneider";
        e4.employmentDate = LocalDate.parse("2022-07-21");
        e4.yearlySalary = 74500.00;
        e4.pensionPlan = null;
        employees.add(e4);

        Employee e5 = new Employee();
        e5.employeeId = 5;
        e5.firstName = "Anna";
        e5.lastName = "Wiltord";
        e5.employmentDate = LocalDate.parse("2022-06-15");
        e5.yearlySalary = 85750.00;
        e5.pensionPlan = null;
        employees.add(e5);

        Employee e6 = new Employee();
        e6.employeeId = 6;
        e6.firstName = "Yosef";
        e6.lastName = "Tesfalem";
        e6.employmentDate = LocalDate.parse("2022-10-31");
        e6.yearlySalary = 100000.00;
        e6.pensionPlan = null;
        employees.add(e6);

        employees.sort(Comparator
                .comparingDouble((Employee e) -> -e.yearlySalary)
                .thenComparing(e -> e.lastName));

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        System.out.println("--- All Employees (Sorted) ---");
        System.out.println(gson.toJson(employees));

        System.out.println("\n--- Quarterly Upcoming Enrollees ---");
        List<Employee> upcoming = getQuarterlyUpcomingEnrollees(employees);
        System.out.println(gson.toJson(upcoming));
    }

    public static List<Employee> getQuarterlyUpcomingEnrollees(List<Employee> employees) {
        LocalDate today = LocalDate.now();

        int currentQuarter = (today.getMonthValue() - 1) / 3 + 1;
        int nextQuarterStartMonth = currentQuarter * 3 + 1;
        int year = today.getYear();
        if (nextQuarterStartMonth > 12) {
            nextQuarterStartMonth = 1;
            year += 1;
        }

        LocalDate startOfNextQuarter = LocalDate.of(year, nextQuarterStartMonth, 1);
        LocalDate endOfNextQuarter = startOfNextQuarter.plusMonths(3).minusDays(1);

        return employees.stream()
                .filter(e -> e.pensionPlan == null)
                .filter(e -> {
                    LocalDate eligibilityDate = e.employmentDate.plusYears(3);
                    return (!eligibilityDate.isBefore(startOfNextQuarter)) &&
                            (!eligibilityDate.isAfter(endOfNextQuarter));
                })
                .sorted(Comparator.comparing(Employee::getEmploymentDate).reversed())
                .collect(Collectors.toList());
    }


    static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return LocalDate.parse(in.nextString());
        }
    }
}
