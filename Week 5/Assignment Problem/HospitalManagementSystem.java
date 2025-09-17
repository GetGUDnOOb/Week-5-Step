import java.time.LocalDate;
import java.util.*;

public class HospitalManagementSystem {

    // ✅ a. Immutable MedicalRecord class
    public static final class MedicalRecord {
        private final String recordId;
        private final String patientDNA;
        private final String[] allergies;
        private final String[] medicalHistory;
        private final LocalDate birthDate;
        private final String bloodType;

        public MedicalRecord(String recordId, String patientDNA, String[] allergies,
                             String[] medicalHistory, LocalDate birthDate, String bloodType) {
            if (!isHIPAACompliant(recordId, patientDNA)) {
                throw new IllegalArgumentException("HIPAA compliance failed.");
            }
            this.recordId = recordId;
            this.patientDNA = patientDNA;
            this.allergies = Arrays.copyOf(allergies, allergies.length);
            this.medicalHistory = Arrays.copyOf(medicalHistory, medicalHistory.length);
            this.birthDate = birthDate;
            this.bloodType = bloodType;
        }

        private boolean isHIPAACompliant(String recordId, String dna) {
            return recordId != null && dna != null && !recordId.isBlank() && !dna.isBlank();
        }

        public String getRecordId() {
            return recordId;
        }

        public String getPatientDNA() {
            return patientDNA;
        }

        public String[] getAllergies() {
            return Arrays.copyOf(allergies, allergies.length);
        }

        public String[] getMedicalHistory() {
            return Arrays.copyOf(medicalHistory, medicalHistory.length);
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public String getBloodType() {
            return bloodType;
        }

        public final boolean isAllergicTo(String substance) {
            for (String allergy : allergies) {
                if (allergy.equalsIgnoreCase(substance)) return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "MedicalRecord{" +
                    "recordId='" + recordId + '\'' +
                    ", birthDate=" + birthDate +
                    ", bloodType='" + bloodType + '\'' +
                    ", allergies=" + Arrays.toString(allergies) +
                    '}';
        }
    }

    // ✅ b. Patient class with privacy levels
    public static class Patient {
        private final String patientId;
        private final MedicalRecord medicalRecord;

        private String currentName;
        private String emergencyContact;
        private String insuranceInfo;

        private int roomNumber;
        private String attendingPhysician;

        // Emergency admission
        public Patient(String currentName) {
            this.patientId = "TEMP-" + System.currentTimeMillis();
            this.currentName = currentName;
            this.medicalRecord = null;
        }

        // Standard admission
        public Patient(String patientId, MedicalRecord medicalRecord,
                       String currentName, String emergencyContact, String insuranceInfo,
                       int roomNumber, String attendingPhysician) {
            if (patientId == null || medicalRecord == null)
                throw new IllegalArgumentException("Missing required info for standard admission.");
            this.patientId = patientId;
            this.medicalRecord = medicalRecord;
            this.currentName = currentName;
            this.emergencyContact = emergencyContact;
            this.insuranceInfo = insuranceInfo;
            this.roomNumber = roomNumber;
            this.attendingPhysician = attendingPhysician;
        }

        // Transfer admission
        public Patient(Patient otherPatient) {
            this.patientId = otherPatient.patientId;
            this.medicalRecord = otherPatient.medicalRecord;
            this.currentName = otherPatient.currentName;
            this.emergencyContact = otherPatient.emergencyContact;
            this.insuranceInfo = otherPatient.insuranceInfo;
            this.roomNumber = otherPatient.roomNumber;
            this.attendingPhysician = otherPatient.attendingPhysician;
        }

        // Package-private info
        String getBasicInfo() {
            return "Name: " + currentName + ", ID: " + patientId + ", Physician: " + attendingPhysician;
        }

        public String getPublicInfo() {
            return "Name: " + currentName + ", Room: " + roomNumber;
        }

        // Setters with validation
        public void setCurrentName(String currentName) {
            if (currentName != null && !currentName.isBlank())
                this.currentName = currentName;
        }

        public void setEmergencyContact(String emergencyContact) {
            this.emergencyContact = emergencyContact;
        }

        public void setInsuranceInfo(String insuranceInfo) {
            this.insuranceInfo = insuranceInfo;
        }

        public void setRoomNumber(int roomNumber) {
            this.roomNumber = roomNumber;
        }

        public void setAttendingPhysician(String attendingPhysician) {
            this.attendingPhysician = attendingPhysician;
        }

        public String getPatientId() {
            return patientId;
        }

        public MedicalRecord getMedicalRecord() {
            return medicalRecord;
        }

        @Override
        public String toString() {
            return "Patient{" +
                    "patientId='" + patientId + '\'' +
                    ", currentName='" + currentName + '\'' +
                    ", roomNumber=" + roomNumber +
                    ", attendingPhysician='" + attendingPhysician + '\'' +
                    '}';
        }
    }

    // ✅ d. Staff classes
    public static class Doctor {
        private final String licenseNumber;
        private final String specialty;
        private final Set<String> certifications;

        public Doctor(String licenseNumber, String specialty, Set<String> certifications) {
            this.licenseNumber = licenseNumber;
            this.specialty = specialty;
            this.certifications = Set.copyOf(certifications);
        }

        public String getLicenseNumber() {
            return licenseNumber;
        }

        public String getSpecialty() {
            return specialty;
        }

        public Set<String> getCertifications() {
            return certifications;
        }

        @Override
        public String toString() {
            return "Doctor{" +
                    "licenseNumber='" + licenseNumber + '\'' +
                    ", specialty='" + specialty + '\'' +
                    '}';
        }
    }

    public static class Nurse {
        private final String nurseId;
        private final String shift;
        private final List<String> qualifications;

        public Nurse(String nurseId, String shift, List<String> qualifications) {
            this.nurseId = nurseId;
            this.shift = shift;
            this.qualifications = List.copyOf(qualifications);
        }

        public String getNurseId() {
            return nurseId;
        }

        public String getShift() {
            return shift;
        }

        public List<String> getQualifications() {
            return qualifications;
        }

        @Override
        public String toString() {
            return "Nurse{" +
                    "nurseId='" + nurseId + '\'' +
                    ", shift='" + shift + '\'' +
                    '}';
        }
    }

    public static class Administrator {
        private final String adminId;
        private final List<String> accessPermissions;

        public Administrator(String adminId, List<String> accessPermissions) {
            this.adminId = adminId;
            this.accessPermissions = List.copyOf(accessPermissions);
        }

        public String getAdminId() {
            return adminId;
        }

        public List<String> getAccessPermissions() {
            return accessPermissions;
        }

        @Override
        public String toString() {
            return "Administrator{" +
                    "adminId='" + adminId + '\'' +
                    ", accessPermissions=" + accessPermissions +
                    '}';
        }
    }

    // ✅ e. HospitalSystem with access control
    public static class HospitalSystem {
        private final Map<String, Object> patientRegistry = new HashMap<>();

        public static final String PRIVACY_DOCTOR = "FULL_ACCESS";
        public static final String PRIVACY_NURSE = "LIMITED_ACCESS";
        public static final String PRIVACY_ADMIN = "ADMIN_ACCESS";

        public boolean admitPatient(Object patient, Object staff) {
            if (!(patient instanceof Patient)) return false;
            if (!validateStaffAccess(staff, patient)) return false;

            Patient p = (Patient) patient;
            patientRegistry.put(p.getPatientId(), p);
            return true;
        }

        private boolean validateStaffAccess(Object staff, Object patient) {
            return (staff instanceof Doctor || staff instanceof Nurse || staff instanceof Administrator);
        }

        void printPatientRegistry() {
            for (Map.Entry<String, Object> entry : patientRegistry.entrySet()) {
                System.out.println("Patient ID: " + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    // ✅ Main method to test functionality
    public static void main(String[] args) {
        // Create a medical record
        MedicalRecord record = new MedicalRecord(
                "MR001",
                "AGCTTAGCTA",
                new String[]{"Peanuts", "Penicillin"},
                new String[]{"Asthma", "Surgery 2019"},
                LocalDate.of(1990, 5, 20),
                "O+"
        );

        // Create a patient (standard admission)
        Patient patient = new Patient(
                "P001",
                record,
                "John Doe",
                "Jane Doe - 123456789",
                "Blue Cross",
                205,
                "Dr. Smith"
        );

        // Create staff
        Doctor doc = new Doctor("D001", "Cardiology", Set.of("ACLS", "BLS"));
        Nurse nurse = new Nurse("N001", "Night", List.of("IV", "CPR"));
        Administrator admin = new Administrator("A001", List.of("ALL"));

        // Create hospital system
        HospitalSystem system = new HospitalSystem();

        // Admit patient
        boolean admitted = system.admitPatient(patient, doc);
        System.out.println("Patient admitted: " + admitted);

        // Print patient registry
        system.printPatientRegistry();

        // Public info access
        System.out.println("Public Info: " + patient.getPublicInfo());
        // Allergy check
        System.out.println("Is allergic to Penicillin? " + record.isAllergicTo("Penicillin"));
    }
}
