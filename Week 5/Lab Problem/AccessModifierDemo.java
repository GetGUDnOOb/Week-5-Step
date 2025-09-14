package com.company.security;

public class AccessModifierDemo {

    // Fields with different access modifiers
    private int privateField;
    String defaultField; // default access (no modifier)
    protected double protectedField;
    public boolean publicField;

    // Constructor to initialize all fields
    public AccessModifierDemo(int privateField, String defaultField, double protectedField, boolean publicField) {
        this.privateField = privateField;
        this.defaultField = defaultField;
        this.protectedField = protectedField;
        this.publicField = publicField;
    }

    // Methods with different access modifiers
    private void privateMethod() {
        System.out.println("Private method called");
    }

    void defaultMethod() {
        System.out.println("Default method called");
    }

    protected void protectedMethod() {
        System.out.println("Protected method called");
    }

    public void publicMethod() {
        System.out.println("Public method called");
    }

    // Method to test internal access to all members
    public void testInternalAccess() {
        System.out.println("Accessing fields internally:");
        System.out.println("privateField = " + privateField);
        System.out.println("defaultField = " + defaultField);
        System.out.println("protectedField = " + protectedField);
        System.out.println("publicField = " + publicField);

        System.out.println("\nCalling methods internally:");
        privateMethod();
        defaultMethod();
        protectedMethod();
        publicMethod();
    }

    public static void main(String[] args) {
        AccessModifierDemo demo = new AccessModifierDemo(10, "default", 3.14, true);

        // Accessing fields from the same class (main method is inside the class)
        System.out.println("From main method:");
        System.out.println("demo.privateField = " + demo.privateField); // works (same class)
        System.out.println("demo.defaultField = " + demo.defaultField); // works (same class)
        System.out.println("demo.protectedField = " + demo.protectedField); // works (same class)
        System.out.println("demo.publicField = " + demo.publicField); // works everywhere

        // Accessing methods from the same class
        demo.privateMethod();    // works (same class)
        demo.defaultMethod();    // works (same class)
        demo.protectedMethod();  // works (same class)
        demo.publicMethod();     // works everywhere

        // Call testInternalAccess method to show internal accessibility
        demo.testInternalAccess();
    }
}

// Second class in the SAME package
class SamePackageTest {

    public static void testAccess() {
        AccessModifierDemo demo = new AccessModifierDemo(20, "package", 2.71, false);

        System.out.println("From SamePackageTest:");

        // Accessing fields
        // System.out.println(demo.privateField); // ERROR: privateField not accessible outside AccessModifierDemo
        System.out.println("defaultField = " + demo.defaultField);     // works (same package)
        System.out.println("protectedField = " + demo.protectedField); // works (same package)
        System.out.println("publicField = " + demo.publicField);       // works everywhere

        // Accessing methods
        // demo.privateMethod();    // ERROR: privateMethod not accessible outside AccessModifierDemo
        demo.defaultMethod();       // works (same package)
        demo.protectedMethod();     // works (same package)
        demo.publicMethod();        // works everywhere
    }

    public static void main(String[] args) {
        testAccess();
    }
}
