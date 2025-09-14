import java.io.Serializable;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EmployeeBean implements Serializable {
    private String employeeId;
    private String firstName;
    private String lastName;
    private double salary;
    private String department;
    private Date hireDate;
    private boolean isActive;

    // Default no-arg constructor (JavaBean requirement)
    public EmployeeBean() {
    }

    // Parameterized constructor for convenience
    public EmployeeBean(String employeeId, String firstName, String lastName,
                        double salary, String department, Date hireDate, boolean isActive) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        setSalary(salary);  // use setter for validation
        this.department = department;
        this.hireDate = hireDate;
        this.isActive = isActive;
    }

    // Getters (JavaBean conventions)
    public String getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getSalary() {
        return salary;
    }

    public String getDepartment() {
        return department;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters (JavaBean conventions) with validation
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary must be positive.");
        }
        this.salary = salary;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Computed properties (getters without corresponding fields)
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    public int getYearsOfService() {
        if (hireDate == null) return 0;
        LocalDate hireLocalDate = hireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(hireLocalDate, now);
    }

    public String getFormattedSalary() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return formatter.format(salary);
    }

    // Derived properties with validation
    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            this.firstName = "";
            this.lastName = "";
            return;
        }
        String[] parts = fullName.trim().split("\\s+", 2);
        this.firstName = parts[0];
        this.lastName = parts.length > 1 ? parts[1] : "";
    }

    // Override toString()
    @Override
    public String toString() {
        return "EmployeeBean{" +
                "employeeId='" + employeeId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", salary=" + getFormattedSalary() +
                ", department='" + department + '\'' +
                ", hireDate=" + hireDate +
                ", yearsOfService=" + getYearsOfService() +
                ", isActive=" + isActive +
                '}';
    }

    // Override equals() and hashCode() based on employeeId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeBean)) return false;
        EmployeeBean that = (EmployeeBean) o;
        return Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    // Main method demonstrating usage and JavaBean features
    public static void main(String[] args) throws InterruptedException {
        // Create using default constructor + setters
        EmployeeBean emp1 = new EmployeeBean();
        emp1.setEmployeeId("E001");
        emp1.setFullName("John Doe");
        emp1.setSalary(55000);
        emp1.setDepartment("IT");
        emp1.setHireDate(new Date(120, 0, 15)); // Jan 15, 2020 (year=2020 - 1900)
        emp1.setActive(true);

        // Create using parameterized constructor
        EmployeeBean emp2 = new EmployeeBean("E002", "Jane", "Smith", 72000, "HR", new Date(118, 5, 1), false);

        // Show getter usage
        System.out.println(emp1);
        System.out.println(emp2);

        // Test computed properties
        System.out.println(emp1.getFullName());          // John Doe
        System.out.println(emp1.getYearsOfService());    // e.g., 5
        System.out.println(emp1.getFormattedSalary());   // $55,000.00 or locale currency

        // Test validation in setter
        try {
            emp2.setSalary(-1000);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught invalid salary: " + e.getMessage());
        }

        // Array of employees for sorting/filtering
        EmployeeBean[] employees = new EmployeeBean[] {
            emp1,
            emp2,
            new EmployeeBean("E003", "Alice", "Brown", 68000, "Finance", new Date(115, 3, 20), true),
            new EmployeeBean("E004", "Bob", "White", 47000, "IT", new Date(122, 8, 5), true)
        };

        // Sort by salary descending
        Arrays.sort(employees, (a, b) -> Double.compare(b.getSalary(), a.getSalary()));
        System.out.println("\nEmployees sorted by salary (desc):");
        for (EmployeeBean e : employees) {
            System.out.println(e.getFullName() + ": " + e.getFormattedSalary());
        }

        // Filter active employees
        System.out.println("\nActive employees:");
        Arrays.stream(employees)
                .filter(EmployeeBean::isActive)
                .forEach(e -> System.out.println(e.getFullName()));

        // Bulk operation example: Increase salary by 10% for all
        Arrays.stream(employees).forEach(e -> e.setSalary(e.getSalary() * 1.10));
        System.out.println("\nSalaries after 10% raise:");
        for (EmployeeBean e : employees) {
            System.out.println(e.getFullName() + ": " + e.getFormattedSalary());
        }

        // Use utility class to print properties
        System.out.println("\nProperties of emp1 via reflection:");
        JavaBeanProcessor.printAllProperties(emp1);

        // Copy properties from emp2 to emp1
        System.out.println("\nCopying properties from emp2 to emp1...");
        JavaBeanProcessor.copyProperties(emp2, emp1);
        System.out.println("emp1 after copy:");
        System.out.println(emp1);
    }
}

import java.lang.reflect.Method;

class JavaBeanProcessor {

    // Print all properties using reflection (getter methods)
    public static void printAllProperties(EmployeeBean emp) {
        if (emp == null) {
            System.out.println("EmployeeBean is null");
            return;
        }
        Method[] methods = emp.getClass().getMethods();
        try {
            for (Method method : methods) {
                if (isGetter(method)) {
                    String propName = getterToPropertyName(method.getName());
                    Object value = method.invoke(emp);
                    System.out.printf("%s = %s%n", propName, value);
                }
            }
        } catch (Exception e) {
            System.out.println("Reflection error: " + e.getMessage());
        }
    }

    // Copy properties from source to target (only properties with setters)
    public static void copyProperties(EmployeeBean source, EmployeeBean target) {
        if (source == null || target == null) return;

        Method[] methods = source.getClass().getMethods();

        try {
            for (Method getter : methods) {
                if (isGetter(getter)) {
                    String propName = getterToPropertyName(getter.getName());
                    Method setter = findSetter(target.getClass(), propName, getter.getReturnType());
                    if (setter != null) {
                        Object value = getter.invoke(source);
                        setter.invoke(target, value);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Reflection error during copy: " + e.getMessage());
        }
    }

    // Check if method is a getter
    private static boolean isGetter(Method method) {
        if (method.getParameterCount() != 0) return false;
        if (method.getName().startsWith("get") && method.getName().length() > 3
                && !method.getReturnType().equals(void.class)) {
            return true;
        }
        if (method.getName().startsWith("is") && method.getName().length() > 2
                && method.getReturnType().equals(boolean.class)) {
            return true;
        }
        return false;
    }

    // Convert getter method name to property name
    private static String getterToPropertyName(String methodName) {
        if (methodName.startsWith("get")) {
            return decapitalize(methodName.substring(3));
        } else if (methodName.startsWith("is")) {
            return decapitalize(methodName.substring(2));
        }
        return methodName;
    }

    // Find setter method for property by name and param type
    private static Method findSetter(Class<?> clazz, String propertyName, Class<?> paramType) {
        String setterName = "set" + capitalize(propertyName);
        try {
            return clazz.getMethod(setterName, paramType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    // Utility to decapitalize string
    private static String decapitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    // Utility to capitalize string
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
