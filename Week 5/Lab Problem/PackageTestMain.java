// File: com/company/main/PackageTestMain.java
package com.company.main;

import com.company.security.AccessModifierDemo;

public class PackageTestMain {
    public static void main(String[] args) {
        AccessModifierDemo demo = new AccessModifierDemo(1, "default", 1.1, true);

        System.out.println("Accessing fields from different package (non-subclass):");

        // System.out.println(demo.privateField);    // ERROR: privateField not accessible (private)
        // System.out.println(demo.defaultField);    // ERROR: defaultField not accessible (package-private)
        // System.out.println(demo.protectedField);  // ERROR: protectedField not accessible via object ref outside package if not subclass
        System.out.println("publicField = " + demo.publicField); // Works

        System.out.println("\nAccessing methods from different package (non-subclass):");

        // demo.privateMethod();    // ERROR: privateMethod not accessible
        // demo.defaultMethod();    // ERROR: defaultMethod not accessible (package-private)
        // demo.protectedMethod();  // ERROR: protectedMethod not accessible via object ref outside package if not subclass
        demo.publicMethod(); // Works

        /*
         * Summary:
         * private and default members are NOT accessible outside package.
         * protected members are NOT accessible outside package via object ref unless subclass.
         * public members accessible everywhere.
         */
    }
}

// File: com/company/extended/ExtendedDemo.java
package com.company.extended;

import com.company.security.AccessModifierDemo;

public class ExtendedDemo extends AccessModifierDemo {

    public ExtendedDemo(int privateField, String defaultField, double protectedField, boolean publicField) {
        super(privateField, defaultField, protectedField, publicField);
    }

    public void testInheritedAccess() {
        System.out.println("Testing inherited fields and methods in subclass:");

        // System.out.println(privateField);  // ERROR: privateField NOT inherited
        // System.out.println(defaultField);  // ERROR: defaultField not accessible (package-private)
        System.out.println("protectedField = " + protectedField); // Works
        System.out.println("publicField = " + publicField);       // Works

        // privateMethod();  // ERROR: privateMethod NOT inherited
        // defaultMethod();  // ERROR: defaultMethod not accessible (package-private)
        System.out.println("Calling protectedMethod():");
        protectedMethod();  // Works
        System.out.println("Calling publicMethod():");
        publicMethod();     // Works
    }

    @Override
    protected void protectedMethod() {
        System.out.println("Overridden protected method in ExtendedDemo called");
    }

    public static void main(String[] args) {
        ExtendedDemo child = new ExtendedDemo(10, "child", 2.5, false);
        AccessModifierDemo parent = new AccessModifierDemo(5, "parent", 3.3, true);

        System.out.println("Access from child object:");
        child.testInheritedAccess();

        System.out.println("\nAccess from parent object in different package:");
        // System.out.println(parent.privateField);  // ERROR
        // System.out.println(parent.defaultField);  // ERROR
        // System.out.println(parent.protectedField);// ERROR via object ref
        System.out.println("publicField = " + parent.publicField);

        // parent.privateMethod();   // ERROR
        // parent.defaultMethod();   // ERROR
        // parent.protectedMethod(); // ERROR via object ref
        parent.publicMethod();

        System.out.println("\nCalling overridden protectedMethod() on child:");
        child.protectedMethod(); // Calls overridden version
    }
}
