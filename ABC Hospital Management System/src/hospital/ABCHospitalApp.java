package hospital;

import hospital.dao.HospitalDAO;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * ABC Hospital Management System
 *
 * This version mirrors the feature areas shown in the supplied video:
 * dashboard, patient/staff/appointment/admission/clinical/laboratory/
 * pharmacy/billing/administration/user-account management.
 */
public class ABCHospitalApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final HospitalDAO db = new HospitalDAO();

    private static int currentUserId;
    private static String currentUsername;
    private static String currentRole;

    public static void main(String[] args) {
        System.out.println();
        line('=');
        System.out.println("          ABC HOSPITAL MANAGEMENT SYSTEM");
        line('=');

        if (!login()) {
            System.out.println("Login failed. Goodbye.");
            return;
        }

        while (true) {
            dashboard();
            mainMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> patientMenu();
                    case 2 -> staffMenu();
                    case 3 -> appointmentMenu();
                    case 4 -> admissionMenu();
                    case 5 -> clinicalMenu();
                    case 6 -> laboratoryMenu();
                    case 7 -> pharmacyMenu();
                    case 8 -> billingMenu();
                    case 9 -> administrationMenu();
                    case 10 -> userAccountMenu();
                    case 0 -> {
                        System.out.println("Logged out. Thank you for using ABC Hospital.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                showError(e);
            }
        }
    }

    // =========================================================
    //  / DASHBOARD
    // =========================================================

    private static boolean login() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.println();
            line('=');
            System.out.println("                         LOGIN");
            line('=');

            String username = readLine("Username: ");
            String password = readLine("Password: ");

            try {
                Map<String,Object> user = db.one("""
                        SELECT UserId, Username, Role, IsActive
                        FROM Users
                        WHERE Username = ? AND PasswordHash = ?
                        """, username, sha256(password));

                if (user == null) {
                    System.out.println("Invalid username or password.");
                    continue;
                }

                if (!Boolean.TRUE.equals(user.get("IsActive"))
                        && !Integer.valueOf(1).equals(user.get("IsActive"))) {
                    System.out.println("This account is inactive.");
                    return false;
                }

                currentUserId = ((Number)user.get("UserId")).intValue();
                currentUsername = String.valueOf(user.get("Username"));
                currentRole = String.valueOf(user.get("Role"));

                System.out.println("Login successful.");
                return true;

            } catch (Exception e) {
                showError(e);
                return false;
            }
        }
        return false;
    }

    private static void dashboard() {
        try {
            System.out.println();
            line('=');
            System.out.println("                    ABC HOSPITAL DASHBOARD");
            line('=');
            System.out.printf("%-22s : %s%n", "Logged-in User", currentUsername);
            System.out.printf("%-22s : %s%n", "Role", currentRole);
            System.out.printf("%-22s : %s%n", "Account Status", "ACTIVE");
            line('-');
            System.out.printf("%-22s : %s%n", "Patients", db.count("Patient"));
            System.out.printf("%-22s : %s%n", "Staff", db.count("Staff"));
            System.out.printf("%-22s : %s%n", "Appointments", db.count("Appointment"));
            System.out.printf("%-22s : %s%n", "Active Admissions",
                    HospitalDAO.intValue(db.one(
                            "SELECT COUNT(*) FROM Admission WHERE Status = 'ACTIVE'")));
            System.out.printf("%-22s : %s%n", "Pending Lab Tests",
                    HospitalDAO.intValue(db.one(
                            "SELECT COUNT(*) FROM LaboratoryTest WHERE Status = 'PENDING'")));
            line('-');
            System.out.printf("%-22s : ONLINE%n", "System Status");
            line('=');
        } catch (Exception e) {
            showError(e);
        }
    }

    private static void mainMenu() {
        System.out.println("""

                1.  Patient Management
                2.  Staff Management
                3.  Appointment Management
                4.  Admission & Bed Management
                5.  Clinical Management
                6.  Laboratory Services
                7.  Pharmacy Services
                8.  Billing & Payment
                9.  Hospital Administration
                10. User Account
                0.  Logout
                """);
        line('=');
    }

    // =========================================================
    // PATIENT MANAGEMENT
    // =========================================================

    private static void patientMenu() throws Exception {
        while (true) {
            title("PATIENT MANAGEMENT");
            System.out.println("""
                    1. Register Patient
                    2. View All Patients
                    3. Find Patient
                    4. Update Patient
                    5. Delete Patient
                    6. View Patient Profile
                    7. View Medical History
                    8. View Patient Appointments
                    9. View Patient Admissions
                    10. View Patient Prescriptions
                    11. View Patient Billing History
                    0. Back
                    """);
            int c = readInt("Enter your choice: ");
            switch (c) {
                case 1 -> registerPatient();
                case 2 -> listPatients();
                case 3, 6 -> viewPatient();
                case 4 -> updatePatient();
                case 5 -> deletePatient();
                case 7 -> medicalHistory();
                case 8 -> patientAppointments();
                case 9 -> patientAdmissions();
                case 10 -> patientPrescriptions();
                case 11 -> patientBilling();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void registerPatient() throws Exception {
        title("REGISTER PATIENT");
        String first = required("First Name");
        String last = required("Last Name");
        char gender = readChar("Gender (M/F): ");
        LocalDate dob = readDate("Date of Birth (yyyy-MM-dd): ");
        String phone = readLine("Phone: ");
        String email = readLine("Email: ");
        String street = readLine("Street: ");
        String city = readLine("City: ");
        String country = readLine("Country: ");
        String blood = readLine("Blood Group: ");
        String genotype = readLine("Genotype: ");
        String allergies = readLine("Allergies: ");
        String emergency = readLine("Emergency Contact: ");
        String emergencyPhone = readLine("Emergency Phone: ");

        db.transaction(List.of(c -> {
            int personId;
            try (PreparedStatement ps = c.prepareStatement("""
                    INSERT INTO Person
                    (FirstName, LastName, Gender, DateOfBirth, Phone, Email,
                     Street, City, Country)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, first); ps.setString(2, last);
                ps.setString(3, String.valueOf(gender)); ps.setDate(4, java.sql.Date.valueOf(dob));
                ps.setString(5, phone); ps.setString(6, email); ps.setString(7, street);
                ps.setString(8, city); ps.setString(9, country);
                ps.executeUpdate();
                try (var keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new java.sql.SQLException("PersonId was not generated.");
                    personId = keys.getInt(1);
                }
            }

            try (PreparedStatement ps = c.prepareStatement("""
                    INSERT INTO Patient
                    (BloodGroup, Genotype, Allergies, EmergencyContact, EmergencyPhone, PersonId)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, blood); ps.setString(2, genotype); ps.setString(3, allergies);
                ps.setString(4, emergency); ps.setString(5, emergencyPhone); ps.setInt(6, personId);
                ps.executeUpdate();
                try (var keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new java.sql.SQLException("PatientId was not generated.");
                    System.out.println("Patient registered. Patient ID: " + keys.getInt(1));
                }
            }
        }));
    }

    private static void listPatients() throws Exception {
        title("ALL PATIENTS");
        printRows(db.query("""
                SELECT pt.PatientId AS ID, p.FirstName, p.LastName, p.Gender,
                       p.DateOfBirth AS DOB, p.Phone, p.Email, p.City,
                       pt.BloodGroup, pt.Genotype, pt.Allergies
                FROM Patient pt JOIN Person p ON p.PersonId = pt.PersonId
                ORDER BY pt.PatientId
                """));
    }

    private static void viewPatient() throws Exception {
        int id = readInt("Patient ID: ");
        title("PATIENT PROFILE");
        printRows(db.query("""
                SELECT pt.PatientId AS PatientID, p.FirstName, p.LastName, p.Gender,
                       p.DateOfBirth, p.Phone, p.Email, p.Street, p.City, p.Country,
                       pt.BloodGroup, pt.Genotype, pt.Allergies,
                       pt.EmergencyContact, pt.EmergencyPhone
                FROM Patient pt JOIN Person p ON p.PersonId = pt.PersonId
                WHERE pt.PatientId = ?
                """, id));
    }

    private static void updatePatient() throws Exception {
        int id = readInt("Patient ID to update: ");
        Map<String,Object> patient = db.one("""
                SELECT pt.PersonId, p.*
                FROM Patient pt JOIN Person p ON p.PersonId = pt.PersonId
                WHERE pt.PatientId = ?
                """, id);
        if (patient == null) { System.out.println("Patient not found."); return; }

        String first = required("First Name");
        String last = required("Last Name");
        char gender = readChar("Gender (M/F): ");
        LocalDate dob = readDate("Date of Birth (yyyy-MM-dd): ");
        String phone = readLine("Phone: ");
        String email = readLine("Email: ");
        String street = readLine("Street: ");
        String city = readLine("City: ");
        String country = readLine("Country: ");
        String blood = readLine("Blood Group: ");
        String genotype = readLine("Genotype: ");
        String allergies = readLine("Allergies: ");
        String emergency = readLine("Emergency Contact: ");
        String emergencyPhone = readLine("Emergency Phone: ");
        int personId = ((Number)patient.get("PersonId")).intValue();

        db.transaction(List.of(c -> {
            try (PreparedStatement ps = c.prepareStatement("""
                    UPDATE Person SET FirstName=?, LastName=?, Gender=?, DateOfBirth=?,
                    Phone=?, Email=?, Street=?, City=?, Country=? WHERE PersonId=?
                    """)) {
                ps.setString(1,first); ps.setString(2,last); ps.setString(3,String.valueOf(gender));
                ps.setDate(4,java.sql.Date.valueOf(dob)); ps.setString(5,phone); ps.setString(6,email);
                ps.setString(7,street); ps.setString(8,city); ps.setString(9,country); ps.setInt(10,personId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("""
                    UPDATE Patient SET BloodGroup=?, Genotype=?, Allergies=?,
                    EmergencyContact=?, EmergencyPhone=? WHERE PatientId=?
                    """)) {
                ps.setString(1,blood); ps.setString(2,genotype); ps.setString(3,allergies);
                ps.setString(4,emergency); ps.setString(5,emergencyPhone); ps.setInt(6,id);
                ps.executeUpdate();
            }
        }));
        System.out.println("Patient updated successfully.");
    }

    private static void deletePatient() throws Exception {
        int id = readInt("Patient ID to delete: ");
        if (!db.exists("SELECT 1 FROM Patient WHERE PatientId=?", id)) {
            System.out.println("Patient not found."); return;
        }
        System.out.println("This deletes the patient and their Person row only if dependent records allow it.");
        if (!confirm("Continue")) return;

        Map<String,Object> p = db.one("SELECT PersonId FROM Patient WHERE PatientId=?", id);
        int personId = ((Number)p.get("PersonId")).intValue();
        db.transaction(List.of(c -> {
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM Patient WHERE PatientId=?")) {
                ps.setInt(1,id); ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM Person WHERE PersonId=?")) {
                ps.setInt(1,personId); ps.executeUpdate();
            }
        }));
        System.out.println("Patient deleted.");
    }

    private static void medicalHistory() throws Exception {
        int id = readInt("Patient ID: ");
        title("PATIENT MEDICAL HISTORY");
        System.out.println("\n-- DIAGNOSES --");
        printRows(db.query("""
                SELECT d.DiagnosisId, d.DiagnosisDate, d.Condition, d.Description, d.Notes,
                       CONCAT(p.FirstName,' ',p.LastName) AS Doctor
                FROM Diagnose d
                JOIN Doctor dr ON dr.DoctorId=d.DoctorId
                JOIN Staff s ON s.StaffId=dr.StaffId
                JOIN Person p ON p.PersonId=s.PersonId
                WHERE d.PatientId=? ORDER BY d.DiagnosisDate DESC
                """,id));
        System.out.println("\n-- TREATMENTS --");
        printRows(db.query("""
                SELECT TreatmentId, TreatmentDate, TreatmentName, Description, Status, Notes
                FROM Treatment WHERE PatientId=? ORDER BY TreatmentDate DESC
                """,id));
        System.out.println("\n-- LABORATORY TESTS --");
        printRows(db.query("""
                SELECT LaboratoryTestId, TestName, TestDate, Result, Status
                FROM LaboratoryTest WHERE PatientId=? ORDER BY TestDate DESC
                """,id));
        System.out.println("\n-- ADMISSIONS --");
        printRows(db.query("""
                SELECT AdmissionId, AdmissionDate, DischargeDate, Reason, Status
                FROM Admission WHERE PatientId=? ORDER BY AdmissionDate DESC
                """,id));
    }

    private static void patientAppointments() throws Exception {
        int id = readInt("Patient ID: ");
        printRows(db.query("""
                SELECT a.AppointmentId, a.AppointmentDate, a.Reason, a.Status, a.Notes,
                       CONCAT(p.FirstName,' ',p.LastName) AS Doctor
                FROM Appointment a
                JOIN Doctor d ON d.DoctorId=a.DoctorId
                JOIN Staff s ON s.StaffId=d.StaffId
                JOIN Person p ON p.PersonId=s.PersonId
                WHERE a.PatientId=? ORDER BY a.AppointmentDate DESC
                """,id));
    }

    private static void patientAdmissions() throws Exception {
        int id = readInt("Patient ID: ");
        printRows(db.query("""
                SELECT AdmissionId, AdmissionDate, DischargeDate, Reason, Status
                FROM Admission WHERE PatientId=? ORDER BY AdmissionDate DESC
                """,id));
    }

    private static void patientPrescriptions() throws Exception {
        int id = readInt("Patient ID: ");
        printRows(db.query("""
                SELECT pr.PrescriptionId, pr.PrescriptionDate,
                       CONCAT(p.FirstName,' ',p.LastName) AS Doctor,
                       m.Name AS Medication, pi.Dosage, pi.Frequency,
                       pi.Duration, pi.DurationUnit, pi.Instructions
                FROM Prescription pr
                JOIN Doctor d ON d.DoctorId=pr.DoctorId
                JOIN Staff s ON s.StaffId=d.StaffId
                JOIN Person p ON p.PersonId=s.PersonId
                LEFT JOIN PrescriptionItem pi ON pi.PrescriptionId=pr.PrescriptionId
                LEFT JOIN Medication m ON m.MedicationId=pi.MedicationId
                WHERE pr.PatientId=? ORDER BY pr.PrescriptionDate DESC
                """,id));
    }

    private static void patientBilling() throws Exception {
        int id = readInt("Patient ID: ");
        printRows(db.query("""
                SELECT i.InvoiceId, i.InvoiceDate, i.TotalAmount, i.Status,
                       COALESCE(SUM(pm.Amount),0) AS Paid,
                       i.TotalAmount-COALESCE(SUM(pm.Amount),0) AS Balance
                FROM Invoice i LEFT JOIN Payment pm ON pm.InvoiceId=i.InvoiceId
                WHERE i.PatientId=?
                GROUP BY i.InvoiceId,i.InvoiceDate,i.TotalAmount,i.Status
                ORDER BY i.InvoiceDate DESC
                """,id));
    }

    // =========================================================
    // STAFF MANAGEMENT
    // =========================================================

    private static void staffMenu() throws Exception {
        while (true) {
            title("STAFF MANAGEMENT");
            System.out.println("""
                    1. Doctor Management
                    2. Nurse Management
                    3. Pharmacist Management
                    4. Laboratory Technician Management
                    5. View All Staff
                    6. Find Staff
                    7. View Staff by Department
                    8. Staff Account Management
                    0. Back
                    """);
            int c = readInt("Enter your choice: ");
            switch (c) {
                case 1 -> doctorMenu();
                case 2 -> nurseMenu();
                case 3 -> pharmacistMenu();
                case 4 -> labTechMenu();
                case 5 -> viewAllStaff();
                case 6 -> findStaff();
                case 7 -> staffByDepartment();
                case 8 -> staffAccounts();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void doctorMenu() throws Exception {
        staffSubtypeMenu("DOCTOR", "Doctor", "Specialization", "LicenseNumber",
                "Doctor", "DoctorId", "Doctor");
    }

    private static void nurseMenu() throws Exception {
        staffSubtypeMenu("NURSE", "Nurse", "Qualification", "NursingLicense",
                "Nurse", "NurseId", "Nurse");
    }

    private static void pharmacistMenu() throws Exception {
        staffSubtypeMenu("PHARMACIST", "Pharmacist", "Qualification", "LicenseNumber",
                "Pharmacist", "PharmacistId", "Pharmacist");
    }

    private static void labTechMenu() throws Exception {
        staffSubtypeMenu("LABORATORY_TECHNICIAN", "Laboratory Technician",
                "Qualification", "LicenseNumber",
                "LaboratoryTechnician", "LaboratoryTechnicianId", "Laboratory Technician");
    }

    private static void staffSubtypeMenu(String role, String label, String qualificationColumn,
                                         String licenseColumn, String table, String idColumn,
                                         String displayRole) throws Exception {
        while (true) {
            title(label.toUpperCase() + " MANAGEMENT");
            System.out.println("""
                    1. Register
                    2. View All
                    3. Find
                    4. Update
                    5. Delete
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c) {
                case 1 -> registerSubtype(role, label, qualificationColumn, licenseColumn, table, idColumn);
                case 2 -> viewSubtype(table,idColumn);
                case 3 -> findSubtype(table,idColumn);
                case 4 -> updateSubtype(role,label,qualificationColumn,licenseColumn,table,idColumn);
                case 5 -> deleteSubtype(table,idColumn);
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void registerSubtype(String role, String label, String qualificationColumn,
                                        String licenseColumn, String table, String idColumn) throws Exception {
        title("REGISTER " + label.toUpperCase());
        String first=required("First Name"), last=required("Last Name");
        char gender=readChar("Gender (M/F): ");
        LocalDate dob=readDate("Date of Birth (yyyy-MM-dd): ");
        String phone=readLine("Phone: "), email=readLine("Email: ");
        String street=readLine("Street: "), city=readLine("City: "), country=readLine("Country: ");
        LocalDate employment=readDate("Employment Date (yyyy-MM-dd): ");
        BigDecimal salary=readDecimal("Salary: ");
        int dept=readInt("Department ID (0 for none): ");
        String qualification=readLine(label+" Qualification: ");
        String license=required(label+" License Number");

        db.transaction(List.of(c -> {
            int personId, staffId;
            try (PreparedStatement ps=c.prepareStatement("""
                    INSERT INTO Person(FirstName,LastName,Gender,DateOfBirth,Phone,Email,Street,City,Country)
                    VALUES(?,?,?,?,?,?,?,?,?)
                    """,Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,first);ps.setString(2,last);ps.setString(3,String.valueOf(gender));
                ps.setDate(4,java.sql.Date.valueOf(dob));ps.setString(5,phone);ps.setString(6,email);
                ps.setString(7,street);ps.setString(8,city);ps.setString(9,country);ps.executeUpdate();
                try(var k=ps.getGeneratedKeys()){k.next();personId=k.getInt(1);}
            }
            try(PreparedStatement ps=c.prepareStatement("""
                    INSERT INTO Staff(EmploymentDate,Salary,DepartmentId,PersonId)
                    VALUES(?,?,NULLIF(?,0),?)
                    """,Statement.RETURN_GENERATED_KEYS)){
                ps.setDate(1,java.sql.Date.valueOf(employment));ps.setBigDecimal(2,salary);
                ps.setInt(3,dept);ps.setInt(4,personId);ps.executeUpdate();
                try(var k=ps.getGeneratedKeys()){k.next();staffId=k.getInt(1);}
            }
            String sql = switch(table) {
                case "Doctor" -> "INSERT INTO Doctor(Specialization,LicenseNumber,StaffId) VALUES(?,?,?)";
                case "Nurse" -> "INSERT INTO Nurse(NursingLicense,Qualification,StaffId) VALUES(?,?,?)";
                case "Pharmacist" -> "INSERT INTO Pharmacist(Qualification,LicenseNumber,StaffId) VALUES(?,?,?)";
                default -> "INSERT INTO LaboratoryTechnician(Qualification,LicenseNumber,StaffId) VALUES(?,?,?)";
            };
            try(PreparedStatement ps=c.prepareStatement(sql)){
                ps.setString(1, table.equals("Nurse") ? license : qualification);
                ps.setString(2, table.equals("Nurse") ? qualification : license);
                ps.setInt(3,staffId); ps.executeUpdate();
            }
        }));
        System.out.println(label+" registered successfully.");
    }

    private static void viewSubtype(String table,String idColumn) throws Exception {
        printRows(db.query("""
                SELECT x.%s AS ID, p.FirstName, p.LastName, p.Phone, p.Email,
                       s.EmploymentDate, s.Salary, d.Name AS Department
                FROM %s x JOIN Staff s ON s.StaffId=x.StaffId
                JOIN Person p ON p.PersonId=s.PersonId
                LEFT JOIN Department d ON d.DepartmentId=s.DepartmentId
                ORDER BY x.%s
                """.formatted(idColumn,table,idColumn)));
    }

    private static void findSubtype(String table,String idColumn) throws Exception {
        int id=readInt(idColumn+" ID: ");
        printRows(db.query("""
                SELECT x.%s AS ID, p.*, s.EmploymentDate, s.Salary,
                       d.Name AS Department
                FROM %s x JOIN Staff s ON s.StaffId=x.StaffId
                JOIN Person p ON p.PersonId=s.PersonId
                LEFT JOIN Department d ON d.DepartmentId=s.DepartmentId
                WHERE x.%s=?
                """.formatted(idColumn,table,idColumn),id));
    }

    private static void updateSubtype(String role,String label,String qualificationColumn,
                                      String licenseColumn,String table,String idColumn) throws Exception {
        int id=readInt(idColumn+" ID: ");
        Map<String,Object> row=db.one("""
                SELECT s.StaffId,s.PersonId FROM %s x
                JOIN Staff s ON s.StaffId=x.StaffId WHERE x.%s=?
                """.formatted(table,idColumn),id);
        if(row==null){System.out.println(label+" not found.");return;}
        int staffId=((Number)row.get("StaffId")).intValue();
        int personId=((Number)row.get("PersonId")).intValue();

        String first=required("First Name"), last=required("Last Name");
        char gender=readChar("Gender (M/F): ");
        LocalDate dob=readDate("Date of Birth: ");
        String phone=readLine("Phone: "),email=readLine("Email: ");
        LocalDate employment=readDate("Employment Date: ");
        BigDecimal salary=readDecimal("Salary: ");
        int dept=readInt("Department ID (0 for none): ");
        String qualification=readLine(label+" Qualification: ");
        String license=required(label+" License Number");

        db.transaction(List.of(c->{
            try(PreparedStatement ps=c.prepareStatement("""
                    UPDATE Person SET FirstName=?,LastName=?,Gender=?,DateOfBirth=?,Phone=?,Email=?
                    WHERE PersonId=?
                    """)){
                ps.setString(1,first);ps.setString(2,last);ps.setString(3,String.valueOf(gender));
                ps.setDate(4,java.sql.Date.valueOf(dob));ps.setString(5,phone);ps.setString(6,email);ps.setInt(7,personId);
                ps.executeUpdate();
            }
            try(PreparedStatement ps=c.prepareStatement("""
                    UPDATE Staff SET EmploymentDate=?,Salary=?,DepartmentId=NULLIF(?,0) WHERE StaffId=?
                    """)){
                ps.setDate(1,java.sql.Date.valueOf(employment));ps.setBigDecimal(2,salary);
                ps.setInt(3,dept);ps.setInt(4,staffId);ps.executeUpdate();
            }
            String sql=switch(table){
                case "Doctor" -> "UPDATE Doctor SET Specialization=?,LicenseNumber=? WHERE DoctorId=?";
                case "Nurse" -> "UPDATE Nurse SET NursingLicense=?,Qualification=? WHERE NurseId=?";
                case "Pharmacist" -> "UPDATE Pharmacist SET Qualification=?,LicenseNumber=? WHERE PharmacistId=?";
                default -> "UPDATE LaboratoryTechnician SET Qualification=?,LicenseNumber=? WHERE LaboratoryTechnicianId=?";
            };
            try(PreparedStatement ps=c.prepareStatement(sql)){
                ps.setString(1,table.equals("Nurse")?license:qualification);
                ps.setString(2,table.equals("Nurse")?qualification:license);ps.setInt(3,id);ps.executeUpdate();
            }
        }));
        System.out.println(label+" updated.");
    }

    private static void deleteSubtype(String table,String idColumn) throws Exception {
        int id=readInt(idColumn+" ID: ");
        Map<String,Object> row=db.one("""
                SELECT s.StaffId,s.PersonId FROM %s x
                JOIN Staff s ON s.StaffId=x.StaffId WHERE x.%s=?
                """.formatted(table,idColumn),id);
        if(row==null){System.out.println("Not found.");return;}
        if(!confirm("Delete this staff member"))return;
        int staff=((Number)row.get("StaffId")).intValue(), person=((Number)row.get("PersonId")).intValue();
        db.transaction(List.of(c->{
            try(PreparedStatement ps=c.prepareStatement("DELETE FROM "+table+" WHERE "+idColumn+"=?")){
                ps.setInt(1,id);ps.executeUpdate();
            }
            try(PreparedStatement ps=c.prepareStatement("DELETE FROM Staff WHERE StaffId=?")){
                ps.setInt(1,staff);ps.executeUpdate();
            }
            try(PreparedStatement ps=c.prepareStatement("DELETE FROM Person WHERE PersonId=?")){
                ps.setInt(1,person);ps.executeUpdate();
            }
        }));
        System.out.println("Deleted.");
    }

    private static void viewAllStaff() throws Exception {
        title("ALL STAFF");
        printRows(db.query("""
                SELECT s.StaffId, p.FirstName, p.LastName, p.Phone, p.Email,
                       s.EmploymentDate, s.Salary, d.Name AS Department,
                       CASE
                         WHEN EXISTS(SELECT 1 FROM Doctor x WHERE x.StaffId=s.StaffId) THEN 'DOCTOR'
                         WHEN EXISTS(SELECT 1 FROM Nurse x WHERE x.StaffId=s.StaffId) THEN 'NURSE'
                         WHEN EXISTS(SELECT 1 FROM Pharmacist x WHERE x.StaffId=s.StaffId) THEN 'PHARMACIST'
                         WHEN EXISTS(SELECT 1 FROM LaboratoryTechnician x WHERE x.StaffId=s.StaffId)
                           THEN 'LABORATORY_TECHNICIAN'
                         ELSE 'STAFF'
                       END AS Role
                FROM Staff s JOIN Person p ON p.PersonId=s.PersonId
                LEFT JOIN Department d ON d.DepartmentId=s.DepartmentId
                ORDER BY s.StaffId
                """));
    }

    private static void findStaff() throws Exception {
        int id=readInt("Staff ID: ");
        printRows(db.query("""
                SELECT s.StaffId,p.FirstName,p.LastName,p.Gender,p.DateOfBirth,p.Phone,p.Email,
                       s.EmploymentDate,s.Salary,d.Name AS Department
                FROM Staff s JOIN Person p ON p.PersonId=s.PersonId
                LEFT JOIN Department d ON d.DepartmentId=s.DepartmentId
                WHERE s.StaffId=?
                """,id));
    }

    private static void staffByDepartment() throws Exception {
        int id=readInt("Department ID: ");
        printRows(db.query("""
                SELECT s.StaffId,p.FirstName,p.LastName,p.Phone,
                       d.Name AS Department,s.EmploymentDate
                FROM Staff s JOIN Person p ON p.PersonId=s.PersonId
                JOIN Department d ON d.DepartmentId=s.DepartmentId
                WHERE s.DepartmentId=? ORDER BY p.LastName,p.FirstName
                """,id));
    }

    private static void staffAccounts() throws Exception {
        title("STAFF ACCOUNT MANAGEMENT");
        printRows(db.query("""
                SELECT u.UserId,u.Username,u.Role,u.IsActive,u.StaffId,
                       CONCAT(p.FirstName,' ',p.LastName) AS StaffName
                FROM Users u LEFT JOIN Staff s ON s.StaffId=u.StaffId
                LEFT JOIN Person p ON p.PersonId=s.PersonId ORDER BY u.UserId
                """));
    }

    // =========================================================
    // APPOINTMENTS
    // =========================================================

    private static void appointmentMenu() throws Exception {
        while(true){
            title("APPOINTMENT MANAGEMENT");
            System.out.println("""
                    1. Create Appointment
                    2. View All Appointments
                    3. Find Appointment
                    4. Update Appointment
                    5. Cancel Appointment
                    6. Delete Appointment
                    7. View Patient Appointments
                    8. View Doctor Appointments
                    9. Today's Appointments
                    10. Upcoming Appointments
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c){
                case 1->createAppointment(); case 2->allAppointments(); case 3->findAppointment();
                case 4->updateAppointment(); case 5->cancelAppointment(); case 6->deleteById("Appointment","AppointmentId");
                case 7->patientAppointments(); case 8->doctorAppointments(); case 9->todayAppointments();
                case 10->upcomingAppointments(); case 0->{return;} default->System.out.println("Invalid choice.");
            }
        }
    }

    private static void createAppointment() throws Exception {
        int patient=readInt("Patient ID: "), doctor=readInt("Doctor ID: ");
        LocalDateTime date=readDateTime("Appointment date/time (yyyy-MM-ddTHH:mm): ");
        String reason=readLine("Reason: "),notes=readLine("Notes: ");
        int id=db.executeInsert("""
                INSERT INTO Appointment(PatientId,DoctorId,AppointmentDate,Reason,Status,Notes)
                VALUES(?,?,?,?,?,?)
                """,patient,doctor,date,reason,"SCHEDULED",notes);
        System.out.println("Appointment created. ID: "+id);
    }

    private static void allAppointments() throws Exception {
        printRows(db.query("""
                SELECT a.AppointmentId,a.AppointmentDate,a.Reason,a.Status,
                       CONCAT(pp.FirstName,' ',pp.LastName) AS Patient,
                       CONCAT(dp.FirstName,' ',dp.LastName) AS Doctor
                FROM Appointment a
                JOIN Patient pa ON pa.PatientId=a.PatientId
                JOIN Person pp ON pp.PersonId=pa.PersonId
                JOIN Doctor dr ON dr.DoctorId=a.DoctorId
                JOIN Staff ds ON ds.StaffId=dr.StaffId
                JOIN Person dp ON dp.PersonId=ds.PersonId
                ORDER BY a.AppointmentDate
                """));
    }

    private static void findAppointment() throws Exception {
        int id=readInt("Appointment ID: ");
        printRows(db.query("SELECT * FROM Appointment WHERE AppointmentId=?",id));
    }

    private static void updateAppointment() throws Exception {
        int id=readInt("Appointment ID: ");
        LocalDateTime dt=readDateTime("Appointment date/time: ");
        String reason=readLine("Reason: "),status=readLine("Status: "),notes=readLine("Notes: ");
        db.executeUpdate("""
                UPDATE Appointment SET AppointmentDate=?,Reason=?,Status=?,Notes=?
                WHERE AppointmentId=?
                """,dt,reason,status,notes,id);
        System.out.println("Appointment updated.");
    }

    private static void cancelAppointment() throws Exception {
        int id=readInt("Appointment ID: ");
        db.executeUpdate("UPDATE Appointment SET Status='CANCELLED' WHERE AppointmentId=?",id);
        System.out.println("Appointment cancelled.");
    }

    private static void doctorAppointments() throws Exception {
        int id=readInt("Doctor ID: ");
        printRows(db.query("SELECT * FROM Appointment WHERE DoctorId=? ORDER BY AppointmentDate",id));
    }

    private static void todayAppointments() throws Exception {
        printRows(db.query("""
                SELECT * FROM Appointment
                WHERE CAST(AppointmentDate AS DATE)=CAST(GETDATE() AS DATE)
                ORDER BY AppointmentDate
                """));
    }

    private static void upcomingAppointments() throws Exception {
        printRows(db.query("""
                SELECT * FROM Appointment
                WHERE AppointmentDate >= SYSDATETIME() AND Status='SCHEDULED'
                ORDER BY AppointmentDate
                """));
    }

    // =========================================================
    // ADMISSION / BED
    // =========================================================

    private static void admissionMenu() throws Exception {
        while(true){
            title("ADMISSION & BED MANAGEMENT");
            System.out.println("""
                    1. Admit Patient
                    2. View All Admissions
                    3. Find Admission
                    4. Update Admission
                    5. Discharge Patient
                    6. View Active Admissions
                    7. Ward Management
                    8. Room Management
                    9. Bed Management
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c){
                case 1->admitPatient(); case 2->allAdmissions(); case 3->findAdmission();
                case 4->updateAdmission(); case 5->dischargePatient(); case 6->activeAdmissions();
                case 7->wardManagement(); case 8->roomManagement(); case 9->bedManagement();
                case 0->{return;} default->System.out.println("Invalid choice.");
            }
        }
    }

    private static void admitPatient() throws Exception {
        int patient=readInt("Patient ID: ");
        LocalDateTime admission=readDateTime("Admission date/time: ");
        String reason=readLine("Reason: ");
        int id=db.executeInsert("""
                INSERT INTO Admission(PatientId,AdmissionDate,Reason,Status)
                VALUES(?,?,?,'ACTIVE')
                """,patient,admission,reason);
        System.out.println("Patient admitted. Admission ID: "+id);
    }

    private static void allAdmissions() throws Exception {
        printRows(db.query("""
                SELECT a.AdmissionId,a.PatientId,a.AdmissionDate,a.DischargeDate,a.Reason,a.Status,
                       CONCAT(p.FirstName,' ',p.LastName) AS Patient
                FROM Admission a JOIN Patient pt ON pt.PatientId=a.PatientId
                JOIN Person p ON p.PersonId=pt.PersonId ORDER BY a.AdmissionDate DESC
                """));
    }

    private static void findAdmission() throws Exception {
        int id=readInt("Admission ID: ");
        printRows(db.query("SELECT * FROM Admission WHERE AdmissionId=?",id));
    }

    private static void updateAdmission() throws Exception {
        int id=readInt("Admission ID: ");
        LocalDateTime admission=readDateTime("Admission date/time: ");
        String reason=readLine("Reason: "),status=readLine("Status: ");
        db.executeUpdate("""
                UPDATE Admission SET AdmissionDate=?,Reason=?,Status=? WHERE AdmissionId=?
                """,admission,reason,status,id);
        System.out.println("Admission updated.");
    }

    private static void dischargePatient() throws Exception {
        int id=readInt("Admission ID: ");
        LocalDateTime discharge=readDateTime("Discharge date/time: ");
        db.executeUpdate("""
                UPDATE Admission SET DischargeDate=?,Status='DISCHARGED'
                WHERE AdmissionId=?
                """,discharge,id);
        System.out.println("Patient discharged.");
    }

    private static void activeAdmissions() throws Exception {
        printRows(db.query("""
                SELECT a.AdmissionId,a.PatientId,a.AdmissionDate,a.Reason,
                       CONCAT(p.FirstName,' ',p.LastName) AS Patient
                FROM Admission a JOIN Patient pt ON pt.PatientId=a.PatientId
                JOIN Person p ON p.PersonId=pt.PersonId
                WHERE a.Status='ACTIVE' ORDER BY a.AdmissionDate
                """));
    }

    private static void wardManagement() throws Exception {
        simpleEntityMenu("WARD MANAGEMENT","Ward","WardId",
                new String[]{"Name","WardType","Capacity"});
    }

    private static void roomManagement() throws Exception {
        simpleEntityMenu("ROOM MANAGEMENT","Room","RoomId",
                new String[]{"WardId","RoomNumber","RoomType","Capacity"});
    }

    private static void bedManagement() throws Exception {
        simpleEntityMenu("BED MANAGEMENT","Bed","BedId",
                new String[]{"RoomId","BedNumber","Occupied"});
    }

    // =========================================================
    // CLINICAL
    // =========================================================

    private static void clinicalMenu() throws Exception {
        while(true){
            title("CLINICAL MANAGEMENT");
            System.out.println("""
                    1. Diagnosis Management
                    2. Treatment Management
                    3. Medical Records
                    4. Nurse Assignment
                    5. Patient Medical History
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c){
                case 1->diagnosisManagement(); case 2->treatmentManagement();
                case 3->medicalRecords(); case 4->nurseAssignments(); case 5->medicalHistory();
                case 0->{return;} default->System.out.println("Invalid choice.");
            }
        }
    }

    private static void diagnosisManagement() throws Exception {
        title("DIAGNOSIS MANAGEMENT");
        int c=crudChoice();
        if(c==1){
            int patient=readInt("Patient ID: "),doctor=readInt("Doctor ID: ");
            LocalDate date=readDate("Diagnosis date: ");
            String condition=required("Condition"),desc=readLine("Description: "),notes=readLine("Notes: ");
            System.out.println("Created diagnosis ID: "+db.executeInsert("""
                    INSERT INTO Diagnose(PatientId,DoctorId,DiagnosisDate,Condition,Description,Notes)
                    VALUES(?,?,?,?,?,?)
                    """,patient,doctor,date,condition,desc,notes));
        } else if(c==2) printRows(db.query("SELECT * FROM Diagnose ORDER BY DiagnosisDate DESC"));
        else if(c==3){int id=readInt("Diagnosis ID: ");printRows(db.query("SELECT * FROM Diagnose WHERE DiagnosisId=?",id));}
        else if(c==4){int id=readInt("Diagnosis ID: ");String cond=required("Condition"),desc=readLine("Description: "),notes=readLine("Notes: ");db.executeUpdate("UPDATE Diagnose SET Condition=?,Description=?,Notes=? WHERE DiagnosisId=?",cond,desc,notes,id);}
        else if(c==5) deleteById("Diagnose","DiagnosisId");
    }

    private static void treatmentManagement() throws Exception {
        title("TREATMENT MANAGEMENT");
        int c=crudChoice();
        if(c==1){
            int patient=readInt("Patient ID: "),doctor=readInt("Doctor ID: "),diag=readInt("Diagnosis ID (0 for none): ");
            LocalDate date=readDate("Treatment date: ");
            String name=required("Treatment Name"),desc=readLine("Description: "),notes=readLine("Notes: "),status=readLine("Status: ");
            System.out.println("Created treatment ID: "+db.executeInsert("""
                    INSERT INTO Treatment(PatientId,DoctorId,DiagnosisId,TreatmentDate,TreatmentName,Description,Notes,Status)
                    VALUES(?,?,NULLIF(?,0),?,?,?,?,?)
                    """,patient,doctor,diag,date,name,desc,notes,status));
        } else if(c==2) printRows(db.query("SELECT * FROM Treatment ORDER BY TreatmentDate DESC"));
        else if(c==3){int id=readInt("Treatment ID: ");printRows(db.query("SELECT * FROM Treatment WHERE TreatmentId=?",id));}
        else if(c==4){int id=readInt("Treatment ID: ");String name=required("Treatment Name"),desc=readLine("Description: "),notes=readLine("Notes: "),status=readLine("Status: ");db.executeUpdate("UPDATE Treatment SET TreatmentName=?,Description=?,Notes=?,Status=? WHERE TreatmentId=?",name,desc,notes,status,id);}
        else if(c==5) deleteById("Treatment","TreatmentId");
    }

    private static int crudChoice(){
        System.out.println("1. Create\n2. View All\n3. Find\n4. Update\n5. Delete\n0. Back");
        return readInt("Enter your choice: ");
    }

    private static void medicalRecords() throws Exception {
        title("MEDICAL RECORDS");
        int c=crudChoice();
        if(c==1){
            int p=readInt("Patient ID: "); LocalDate d=readDate("Created date: ");
            System.out.println("Medical record ID: "+db.executeInsert(
                    "INSERT INTO MedicalRecord(PatientId,CreatedDate) VALUES(?,?)",p,d));
        } else if(c==2) printRows(db.query("SELECT * FROM MedicalRecord ORDER BY CreatedDate DESC"));
        else if(c==3){int id=readInt("Medical Record ID: ");printRows(db.query("SELECT * FROM MedicalRecord WHERE MedicalRecordId=?",id));}
        else if(c==5) deleteById("MedicalRecord","MedicalRecordId");
    }

    private static void nurseAssignments() throws Exception {
        title("NURSE ASSIGNMENT");
        int c=crudChoice();
        if(c==1){
            int nurse=readInt("Nurse ID: "),patient=readInt("Patient ID: "),admission=readInt("Admission ID (0 for none): ");
            LocalDateTime date=readDateTime("Assignment date/time: ");
            String shift=readLine("Shift: "),status=readLine("Status: "),notes=readLine("Notes: ");
            System.out.println("Assignment ID: "+db.executeInsert("""
                    INSERT INTO NurseAssignment(NurseId,PatientId,AdmissionId,AssignmentDate,Shift,Status,Notes)
                    VALUES(?,?,NULLIF(?,0),?,?,?,?)
                    """,nurse,patient,admission,date,shift,status,notes));
        } else if(c==2) printRows(db.query("SELECT * FROM NurseAssignment ORDER BY AssignmentDate DESC"));
        else if(c==3){int id=readInt("Assignment ID: ");printRows(db.query("SELECT * FROM NurseAssignment WHERE NurseAssignmentId=?",id));}
        else if(c==4){int id=readInt("Assignment ID: ");String shift=readLine("Shift: "),status=readLine("Status: "),notes=readLine("Notes: ");db.executeUpdate("UPDATE NurseAssignment SET Shift=?,Status=?,Notes=? WHERE NurseAssignmentId=?",shift,status,notes,id);}
        else if(c==5) deleteById("NurseAssignment","NurseAssignmentId");
    }

    // =========================================================
    // LABORATORY
    // =========================================================

    private static void laboratoryMenu() throws Exception {
        while(true){
            title("LABORATORY SERVICES");
            System.out.println("""
                    1. Create Laboratory Test
                    2. View All Laboratory Tests
                    3. Find Laboratory Test
                    4. Update Laboratory Test
                    5. Delete Laboratory Test
                    6. View Patient Tests
                    7. View Pending Tests
                    8. View Completed Tests
                    9. Record Test Result
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c){
                case 1->createLabTest();case 2->printRows(db.query("SELECT * FROM LaboratoryTest ORDER BY TestDate DESC"));
                case 3->{int id=readInt("Test ID: ");printRows(db.query("SELECT * FROM LaboratoryTest WHERE LaboratoryTestId=?",id));}
                case 4->updateLabTest();case 5->deleteById("LaboratoryTest","LaboratoryTestId");
                case 6->{int p=readInt("Patient ID: ");printRows(db.query("SELECT * FROM LaboratoryTest WHERE PatientId=? ORDER BY TestDate DESC",p));}
                case 7->printRows(db.query("SELECT * FROM LaboratoryTest WHERE Status='PENDING' ORDER BY TestDate"));
                case 8->printRows(db.query("SELECT * FROM LaboratoryTest WHERE Status='COMPLETED' ORDER BY TestDate DESC"));
                case 9->recordLabResult();case 0->{return;}default->System.out.println("Invalid choice.");
            }
        }
    }

    private static void createLabTest() throws Exception {
        int p=readInt("Patient ID: "),tech=readInt("Laboratory Technician ID: ");
        String name=required("Test Name"),range=readLine("Reference Range: ");
        LocalDateTime date=readDateTime("Test date/time: ");
        int id=db.executeInsert("""
                INSERT INTO LaboratoryTest(PatientId,LaboratoryTechnicianId,TestName,TestDate,ReferenceRange,Status)
                VALUES(?,?,?,?,?,'PENDING')
                """,p,tech,name,date,range);
        System.out.println("Laboratory test ID: "+id);
    }

    private static void updateLabTest() throws Exception {
        int id=readInt("Test ID: ");String name=required("Test Name"),range=readLine("Reference Range: "),status=readLine("Status: ");
        db.executeUpdate("UPDATE LaboratoryTest SET TestName=?,ReferenceRange=?,Status=? WHERE LaboratoryTestId=?",name,range,status,id);
    }

    private static void recordLabResult() throws Exception {
        int id=readInt("Test ID: ");String result=required("Result: ");
        db.executeUpdate("UPDATE LaboratoryTest SET Result=?,Status='COMPLETED' WHERE LaboratoryTestId=?",result,id);
        System.out.println("Result recorded.");
    }

    // =========================================================
    // PHARMACY
    // =========================================================

    private static void pharmacyMenu() throws Exception {
        while(true){
            title("PHARMACY SERVICES");
            System.out.println("""
                    1. Medication Management
                    2. Prescription Management
                    3. Dispense Medication
                    4. View Dispensing Records
                    5. View Patient Prescriptions
                    6. View Available Medications
                    7. View Low Stock Medications
                    8. Update Medication Stock
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c){
                case 1->medicationManagement();case 2->prescriptionManagement();case 3->dispenseMedication();
                case 4->printRows(db.query("SELECT * FROM MedicationDispensing ORDER BY DispensingDate DESC"));
                case 5->patientPrescriptions();
                case 6->printRows(db.query("SELECT * FROM Medication WHERE QuantityInStock>0 ORDER BY Name"));
                case 7->printRows(db.query("SELECT * FROM Medication WHERE QuantityInStock<=10 ORDER BY QuantityInStock"));
                case 8->updateMedicationStock();case 0->{return;}default->System.out.println("Invalid choice.");
            }
        }
    }

    private static void medicationManagement() throws Exception {
        title("MEDICATION MANAGEMENT");
        int c=crudChoice();
        if(c==1){
            String name=required("Name"),desc=readLine("Description: "),form=readLine("Dosage Form: ");
            BigDecimal price=readDecimal("Price: ");int qty=readInt("Quantity in stock: ");
            System.out.println("Medication ID: "+db.executeInsert("""
                    INSERT INTO Medication(Name,Description,DosageForm,Price,QuantityInStock)
                    VALUES(?,?,?,?,?)
                    """,name,desc,form,price,qty));
        } else if(c==2) printRows(db.query("SELECT * FROM Medication ORDER BY Name"));
        else if(c==3){int id=readInt("Medication ID: ");printRows(db.query("SELECT * FROM Medication WHERE MedicationId=?",id));}
        else if(c==4){int id=readInt("Medication ID: ");String name=required("Name"),desc=readLine("Description: "),form=readLine("Dosage Form: ");BigDecimal price=readDecimal("Price: ");db.executeUpdate("UPDATE Medication SET Name=?,Description=?,DosageForm=?,Price=? WHERE MedicationId=?",name,desc,form,price,id);}
        else if(c==5) deleteById("Medication","MedicationId");
    }

    private static void prescriptionManagement() throws Exception {
        title("PRESCRIPTION MANAGEMENT");
        int c=crudChoice();
        if(c==1){
            int patient=readInt("Patient ID: "),doctor=readInt("Doctor ID: ");LocalDate date=readDate("Prescription date: ");
            int id=db.executeInsert("INSERT INTO Prescription(PatientId,DoctorId,PrescriptionDate) VALUES(?,?,?)",patient,doctor,date);
            System.out.println("Prescription ID: "+id);
            if(confirm("Add a prescription item")) addPrescriptionItem(id);
        } else if(c==2) printRows(db.query("SELECT * FROM Prescription ORDER BY PrescriptionDate DESC"));
        else if(c==3){int id=readInt("Prescription ID: ");printRows(db.query("SELECT * FROM Prescription WHERE PrescriptionId=?",id));printRows(db.query("SELECT * FROM PrescriptionItem WHERE PrescriptionId=?",id));}
        else if(c==4){int id=readInt("Prescription ID: ");LocalDate date=readDate("Prescription date: ");db.executeUpdate("UPDATE Prescription SET PrescriptionDate=? WHERE PrescriptionId=?",date,id);}
        else if(c==5) deleteById("Prescription","PrescriptionId");
    }

    private static void addPrescriptionItem(int prescriptionId) throws Exception {
        int med=readInt("Medication ID: ");String dosage=readLine("Dosage: "),freq=readLine("Frequency: ");
        int duration=readInt("Duration: ");String unit=readLine("Duration Unit: "),instructions=readLine("Instructions: ");
        db.executeInsert("""
                INSERT INTO PrescriptionItem(PrescriptionId,MedicationId,Dosage,Frequency,Duration,DurationUnit,Instructions)
                VALUES(?,?,?,?,?,?,?)
                """,prescriptionId,med,dosage,freq,duration,unit,instructions);
    }

    private static void dispenseMedication() throws Exception {
        int prescription=readInt("Prescription ID: "),item=readInt("Prescription Item ID: ");
        int pharmacist=readInt("Pharmacist ID: "),patient=readInt("Patient ID: "),quantity=readInt("Quantity: ");
        String notes=readLine("Notes: ");
        db.transaction(List.of(c->{
            try(PreparedStatement ps=c.prepareStatement("""
                    INSERT INTO MedicationDispensing
                    (PrescriptionId,PrescriptionItemId,PharmacistId,PatientId,DispensingDate,Quantity,Status,Notes)
                    VALUES(?,?,?,?,SYSDATETIME(),?,'DISPENSED',?)
                    """)){
                ps.setInt(1,prescription);ps.setInt(2,item);ps.setInt(3,pharmacist);ps.setInt(4,patient);ps.setInt(5,quantity);ps.setString(6,notes);ps.executeUpdate();
            }
            try(PreparedStatement ps=c.prepareStatement("""
                    UPDATE Medication SET QuantityInStock=QuantityInStock-?
                    WHERE MedicationId=(SELECT MedicationId FROM PrescriptionItem WHERE PrescriptionItemId=?)
                    AND QuantityInStock>=?
                    """)){
                ps.setInt(1,quantity);ps.setInt(2,item);ps.setInt(3,quantity);
                if(ps.executeUpdate()!=1) throw new java.sql.SQLException("Insufficient medication stock or invalid item.");
            }
        }));
        System.out.println("Medication dispensed.");
    }

    private static void updateMedicationStock() throws Exception {
        int id=readInt("Medication ID: "),qty=readInt("New quantity: ");
        db.executeUpdate("UPDATE Medication SET QuantityInStock=? WHERE MedicationId=?",qty,id);
        System.out.println("Stock updated.");
    }

    // =========================================================
    // BILLING
    // =========================================================

    private static void billingMenu() throws Exception {
        while(true){
            title("BILLING & PAYMENT");
            System.out.println("""
                    1. Create Invoice
                    2. View All Invoices
                    3. Find Invoice
                    4. Update Invoice
                    5. Add Invoice Item
                    6. View Invoice Items
                    7. Record Payment
                    8. View Payments
                    9. Patient Billing History
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c){
                case 1->createInvoice();case 2->printRows(db.query("SELECT * FROM Invoice ORDER BY InvoiceDate DESC"));
                case 3->{int id=readInt("Invoice ID: ");printRows(db.query("SELECT * FROM Invoice WHERE InvoiceId=?",id));}
                case 4->updateInvoice();case 5->addInvoiceItem();case 6->viewInvoiceItems();
                case 7->recordPayment();case 8->viewPayments();case 9->patientBilling();
                case 0->{return;}default->System.out.println("Invalid choice.");
            }
        }
    }

    private static void createInvoice() throws Exception {
        int patient=readInt("Patient ID: ");LocalDate date=readDate("Invoice date: ");
        int id=db.executeInsert("INSERT INTO Invoice(PatientId,InvoiceDate,Status) VALUES(?,?, 'UNPAID')",patient,date);
        System.out.println("Invoice ID: "+id);
    }

    private static void updateInvoice() throws Exception {
        int id=readInt("Invoice ID: ");String status=readLine("Status: ");
        db.executeUpdate("UPDATE Invoice SET Status=? WHERE InvoiceId=?",status,id);
    }

    private static void addInvoiceItem() throws Exception {
        int invoice=readInt("Invoice ID: "),qty=readInt("Quantity: ");
        String desc=required("Description: ");BigDecimal price=readDecimal("Unit Price: ");
        db.executeUpdate("""
                INSERT INTO InvoiceItem(InvoiceId,Description,Quantity,UnitPrice)
                VALUES(?,?,?,?)
                """,invoice,desc,qty,price);
        recalculateInvoice(invoice);
        System.out.println("Invoice item added.");
    }

    private static void viewInvoiceItems() throws Exception {
        int invoice=readInt("Invoice ID: ");
        printRows(db.query("SELECT * FROM InvoiceItem WHERE InvoiceId=?",invoice));
    }

    private static void recordPayment() throws Exception {
        int invoice=readInt("Invoice ID: ");BigDecimal amount=readDecimal("Amount: ");
        LocalDate date=readDate("Payment date: ");String method=readLine("Payment Method: ");
        db.executeInsert("INSERT INTO Payment(InvoiceId,Amount,PaymentDate,PaymentMethod) VALUES(?,?,?,?)",invoice,amount,date,method);
        recalculateInvoiceStatus(invoice);
        System.out.println("Payment recorded.");
    }

    private static void viewPayments() throws Exception {
        int invoice=readInt("Invoice ID: ");
        printRows(db.query("SELECT * FROM Payment WHERE InvoiceId=? ORDER BY PaymentDate DESC",invoice));
    }

    private static void recalculateInvoice(int invoiceId) throws Exception {
        db.executeUpdate("""
                UPDATE Invoice SET TotalAmount=(SELECT COALESCE(SUM(Amount),0) FROM InvoiceItem WHERE InvoiceId=?)
                WHERE InvoiceId=?
                """,invoiceId,invoiceId);
        recalculateInvoiceStatus(invoiceId);
    }

    private static void recalculateInvoiceStatus(int invoiceId) throws Exception {
        db.executeUpdate("""
                UPDATE Invoice SET Status =
                  CASE WHEN COALESCE((SELECT SUM(Amount) FROM Payment WHERE InvoiceId=?),0)
                         >= TotalAmount AND TotalAmount > 0 THEN 'PAID'
                       WHEN COALESCE((SELECT SUM(Amount) FROM Payment WHERE InvoiceId=?),0) > 0 THEN 'PARTIALLY_PAID'
                       ELSE 'UNPAID' END
                WHERE InvoiceId=?
                """,invoiceId,invoiceId,invoiceId);
    }

    // =========================================================
    // ADMINISTRATION
    // =========================================================

    private static void administrationMenu() throws Exception {
        while(true){
            title("HOSPITAL ADMINISTRATION");
            System.out.println("""
                    1. Department Management
                    2. Ward Management
                    3. Room Management
                    4. Bed Management
                    5. User Management
                    6. Role Management
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c){
                case 1->departmentManagement();case 2->wardManagement();case 3->roomManagement();
                case 4->bedManagement();case 5->userManagement();case 6->roleManagement();
                case 0->{return;}default->System.out.println("Invalid choice.");
            }
        }
    }

    private static void departmentManagement() throws Exception {
        simpleEntityMenu("DEPARTMENT MANAGEMENT","Department","DepartmentId",
                new String[]{"Name","Description","Location"});
    }

    /**
     * Generic small administration editor.
     * Numeric fields: IDs/capacity. Everything else is entered as text.
     */
    private static void simpleEntityMenu(String heading,String table,String idColumn,String[] fields) throws Exception {
        while(true){
            title(heading);
            System.out.println("1. Create\n2. View All\n3. Find\n4. Update\n5. Delete\n0. Back");
            int c=readInt("Enter your choice: ");
            if(c==0)return;
            if(c==1){
                Object[] values=readFields(table,fields);
                StringBuilder cols=new StringBuilder(),marks=new StringBuilder();
                for(int i=0;i<fields.length;i++){if(i>0){cols.append(',');marks.append(',');}cols.append(fields[i]);marks.append('?');}
                int id=db.executeInsert("INSERT INTO "+table+"("+cols+") VALUES("+marks+")",values);
                System.out.println("Created ID: "+id);
            } else if(c==2) printRows(db.query("SELECT * FROM "+table+" ORDER BY "+idColumn));
            else if(c==3){int id=readInt(idColumn+": ");printRows(db.query("SELECT * FROM "+table+" WHERE "+idColumn+"=?",id));}
            else if(c==4){
                int id=readInt(idColumn+": ");Object[] values=readFields(table,fields);
                StringBuilder set=new StringBuilder();
                for(String f:fields){if(!set.isEmpty())set.append(',');set.append(f).append("=?");}
                Object[] params=Arrays.copyOf(values,values.length+1);params[values.length]=id;
                db.executeUpdate("UPDATE "+table+" SET "+set+" WHERE "+idColumn+"=?",params);
                System.out.println("Updated.");
            } else if(c==5) deleteById(table,idColumn);
        }
    }

    private static Object[] readFields(String table,String[] fields) {
        Object[] v=new Object[fields.length];
        for(int i=0;i<fields.length;i++){
            String f=fields[i].toLowerCase();
            if(f.contains("date")) v[i]=readDate(fields[i]+" (yyyy-MM-dd): ");
            else if(f.contains("id") || f.contains("capacity")) v[i]=readInt(fields[i]+": ");
            else if(f.equals("occupied")) v[i]=readBoolean(fields[i]+" (Y/N): ");
            else if(f.equals("price") || f.equals("salary")) v[i]=readDecimal(fields[i]+": ");
            else v[i]=readLine(fields[i]+": ");
        }
        return v;
    }

    private static void userManagement() throws Exception {
        while(true){
            title("USER MANAGEMENT");
            System.out.println("""
                    1. Create User
                    2. View All Users
                    3. Find User
                    4. Update User
                    5. Deactivate User
                    6. Activate User
                    7. Reset Password
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c){
                case 1->createUser();case 2->printRows(db.query("SELECT UserId,Username,Role,StaffId,IsActive FROM Users ORDER BY UserId"));
                case 3->{int id=readInt("User ID: ");printRows(db.query("SELECT UserId,Username,Role,StaffId,IsActive FROM Users WHERE UserId=?",id));}
                case 4->updateUser();case 5->setUserActive(false);case 6->setUserActive(true);case 7->resetPassword();
                case 0->{return;}default->System.out.println("Invalid choice.");
            }
        }
    }

    private static void createUser() throws Exception {
        String username=required("Username"),password=required("Password");
        String role=required("Role (STAFF/DOCTOR/NURSE/PHARMACIST/LABORATORY_TECHNICIAN)");
        int staff=readInt("Staff ID (0 for none): ");
        int id=db.executeInsert("""
                INSERT INTO Users(Username,PasswordHash,Role,StaffId)
                VALUES(?,?,?,NULLIF(?,0))
                """,username,sha256(password),role.toUpperCase(),staff);
        System.out.println("User created. ID: "+id);
    }

    private static void updateUser() throws Exception {
        int id=readInt("User ID: ");String username=required("Username");
        String role=required("Role");int staff=readInt("Staff ID (0 for none): ");
        db.executeUpdate("UPDATE Users SET Username=?,Role=?,StaffId=NULLIF(?,0) WHERE UserId=?",username,role.toUpperCase(),staff,id);
    }

    private static void setUserActive(boolean active) throws Exception {
        int id=readInt("User ID: ");
        db.executeUpdate("UPDATE Users SET IsActive=? WHERE UserId=?",active,id);
        System.out.println(active?"User activated.":"User deactivated.");
    }

    private static void resetPassword() throws Exception {
        int id=readInt("User ID: ");String password=required("New Password");
        db.executeUpdate("UPDATE Users SET PasswordHash=? WHERE UserId=?",sha256(password),id);
        System.out.println("Password reset.");
    }

    private static void roleManagement() throws Exception {
        title("ROLE MANAGEMENT");
        System.out.println("""
                Available roles:
                1. STAFF
                2. DOCTOR
                3. NURSE
                4. PHARMACIST
                5. LABORATORY_TECHNICIAN

                Roles are enforced by the Users.Role CHECK constraint.
                """);
        printRows(db.query("SELECT Role,COUNT(*) AS UserCount FROM Users GROUP BY Role ORDER BY Role"));
    }

    // =========================================================
    // USER ACCOUNT
    // =========================================================

    private static void userAccountMenu() throws Exception {
        while(true){
            title("USER ACCOUNT");
            System.out.println("""
                    1. View My Profile
                    2. View My Role
                    3. View Account Status
                    4. Change Password
                    0. Back
                    """);
            int c=readInt("Enter your choice: ");
            switch(c){
                case 1->myProfile();case 2->System.out.println("Role: "+currentRole);
                case 3->printRows(db.query("SELECT UserId,Username,Role,IsActive FROM Users WHERE UserId=?",currentUserId));
                case 4->changePassword();case 0->{return;}default->System.out.println("Invalid choice.");
            }
        }
    }

    private static void myProfile() throws Exception {
        printRows(db.query("""
                SELECT u.UserId,u.Username,u.Role,u.IsActive,
                       s.StaffId,p.FirstName,p.LastName,p.Gender,p.DateOfBirth,p.Phone,p.Email,
                       d.Name AS Department
                FROM Users u LEFT JOIN Staff s ON s.StaffId=u.StaffId
                LEFT JOIN Person p ON p.PersonId=s.PersonId
                LEFT JOIN Department d ON d.DepartmentId=s.DepartmentId
                WHERE u.UserId=?
                """,currentUserId));
    }

    private static void changePassword() throws Exception {
        String old=required("Current Password"),newPassword=required("New Password");
        Map<String,Object> row=db.one("SELECT UserId FROM Users WHERE UserId=? AND PasswordHash=?",currentUserId,sha256(old));
        if(row==null){System.out.println("Current password is incorrect.");return;}
        db.executeUpdate("UPDATE Users SET PasswordHash=? WHERE UserId=?",sha256(newPassword),currentUserId);
        System.out.println("Password changed.");
    }

    // =========================================================
    // GENERIC HELPERS
    // =========================================================

    private static void deleteById(String table,String idColumn) throws Exception {
        int id=readInt(idColumn+": ");
        if(confirm("Delete record "+id)) {
            db.executeUpdate("DELETE FROM "+table+" WHERE "+idColumn+"=?",id);
            System.out.println("Deleted.");
        }
    }

    private static void printRows(List<Map<String,Object>> rows) {
        if(rows==null || rows.isEmpty()){System.out.println("No records found.");return;}
        for(Map<String,Object> row:rows){
            line('-');
            row.forEach((k,v)->System.out.printf("%-24s : %s%n",k,String.valueOf(v)));
        }
        line('-');
    }

    private static void title(String text) {
        System.out.println();
        line('=');
        System.out.println(center(text, 80));
        line('=');
    }

    private static void line(char c) {
        System.out.println(String.valueOf(c).repeat(80));
    }

    private static String center(String s,int width){
        if(s.length()>=width)return s;
        int left=(width-s.length())/2;
        return " ".repeat(left)+s;
    }

    private static String readLine(String prompt){
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static String required(String field){
        while(true){
            String s=readLine(field+": ");
            if(!s.isBlank())return s;
            System.out.println(field+" is required.");
        }
    }

    private static int readInt(String prompt){
        while(true){
            try{return Integer.parseInt(readLine(prompt));}
            catch(NumberFormatException e){System.out.println("Enter a valid whole number.");}
        }
    }

    private static BigDecimal readDecimal(String prompt){
        while(true){
            try{return new BigDecimal(readLine(prompt));}
            catch(NumberFormatException e){System.out.println("Enter a valid decimal number.");}
        }
    }

    private static char readChar(String prompt){
        while(true){
            String s=readLine(prompt).toUpperCase();
            if(s.length()==1 && (s.charAt(0)=='M'||s.charAt(0)=='F'))return s.charAt(0);
            System.out.println("Enter M or F.");
        }
    }

    private static LocalDate readDate(String prompt){
        while(true){
            try{return LocalDate.parse(readLine(prompt));}
            catch(DateTimeParseException e){System.out.println("Use yyyy-MM-dd.");}
        }
    }

    private static LocalDateTime readDateTime(String prompt){
        while(true){
            try{return LocalDateTime.parse(readLine(prompt));}
            catch(DateTimeParseException e){System.out.println("Use yyyy-MM-ddTHH:mm.");}
        }
    }

    private static boolean readBoolean(String prompt){
        while(true){
            String s=readLine(prompt).toUpperCase();
            if(s.equals("Y")||s.equals("YES")||s.equals("1")||s.equals("TRUE")) return true;
            if(s.equals("N")||s.equals("NO")||s.equals("0")||s.equals("FALSE")) return false;
            System.out.println("Enter Y or N.");
        }
    }

    private static boolean confirm(String message){
        return readLine(message+" (Y/N): ").equalsIgnoreCase("Y");
    }

    private static void showError(Exception e){
        System.out.println();
        System.out.println("Operation failed: "+e.getMessage());
        if(e instanceof java.sql.SQLException sql && sql.getSQLState()!=null)
            System.out.println("SQLState: "+sql.getSQLState());
    }

    private static String sha256(String value) throws Exception {
        MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes=digest.digest(value.getBytes(StandardCharsets.UTF_8));
        StringBuilder out=new StringBuilder();
        for(byte b:bytes)out.append(String.format("%02x",b));
        return out.toString();
    }
}
