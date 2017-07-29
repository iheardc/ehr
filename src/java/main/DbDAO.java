/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tw
 */
public class DbDAO {

//    private static final String LOGIN_PATIENT = "SELECT * FROM patient WHERE email=? AND password=?";
    private static final String LOGIN_EMPLOYEE = "SELECT * FROM employee WHERE id=? AND password=?";
    private static final String ADD_EMPLOYEE_STMT_NEW
            = "INSERT INTO employee("
            + "email, password, first_name, last_name, gender, phone, "
            + "role, license, "
            + "address, city, state, zip, country"
            + ") "
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String CHECK_EMPLOYEE_EMAIL_DUPLICATE
            = "SELECT COUNT(*) as count FROM employee "
            + "WHERE email=?";
    private static final String ADD_EMPLOYEE_SPECIALTY
            = "INSERT INTO specialty("
            + "employee_id, specialty"
            + ") "
            + "VALUES(?,?)";
    private static final String ADD_PATIENT_STMT_NEW
            = "INSERT INTO patient("
            + "email, password, first_name, last_name, gender, phone, "
            + "date_of_birth, occupation, religion, "
            + "address, city, state, zip, country, "
            + "emFN, emLN, emAddress, emCity, emState, emZip, emRelationship, emPhone, emEmail, "
            + "postalAddress, postalCity, postalState, postalZip"
            + ") "
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//    private static final String FIND_EMPLOYEE
//            = "SELECT * FROM employee WHERE id like ? and first_name like ? and last_name like ? and email like ? and role like ?";
    private static final String FIND_EMPLOYEE_OR
            = "SELECT  A.*, B.specialty as specialty "
            + "FROM employee as A LEFT OUTER JOIN ("
            + "SELECT employee_id, GROUP_CONCAT(specialty SEPARATOR ', ') AS specialty "
            + "FROM specialty "
            + "GROUP BY employee_id) as B on (B.employee_id = A.id) "
            + "WHERE A.id in (SELECT employee_id FROM specialty WHERE specialty like ?) or A.id like ? or (A.first_name like ? or A.last_name like ? or concat_ws(' ',first_name,last_name) like ?) or A.email like ? or A.role like ? "
            + "GROUP BY A.id;";
    private static final String FIND_EMPLOYEE_AND
            = "SELECT  A.*, B.specialty as specialty "
            + "FROM employee as A LEFT OUTER JOIN ("
            + "SELECT employee_id, GROUP_CONCAT(specialty SEPARATOR ', ') AS specialty "
            + "FROM specialty "
            + "GROUP BY employee_id) as B on (B.employee_id = A.id) "
            + "WHERE A.id in (SELECT employee_id FROM specialty WHERE specialty like ?) and A.id like ? and (A.first_name like ? or A.last_name like ? or concat_ws(' ',first_name,last_name) like ?) and (concat_ws(' ',first_name,last_name) like ?) and A.email like ? and A.role like ? "
            + "GROUP BY A.id;";
    private static final String FIND_PATIENT
            = "SELECT * FROM patient "
            + "WHERE id like ? and (first_name like ? or last_name like ? or concat_ws(' ',first_name,last_name) like ?) and date_of_birth like ?";
    private static final String CHECK_IN_PATIENT
            = "INSERT INTO outpatient_dynamic(patient_id, patient_fn, patient_ln, patient_gender, patient_dob, date, status) VALUES(?, ?, ?, ?, ?, ?, ?);";
    private static final String MAKE_NEW_BILLING
            = "INSERT INTO billing(billed_to, date) VALUES(?, ?)";
    private static final String SEARCH_DYNAMIC_TABLE_WITH_STATUS
            = "SELECT * FROM outpatient_dynamic WHERE status like ?";
    private static final String FIND_DOCTOR
            = "SELECT * FROM employee WHERE role='doctor' and (first_name like ? or last_name like ? or concat_ws(' ',first_name,last_name) like ?)";
    private static final String FIND_DOCTOR_WITH_SPECIALTY
            = "SELECT  A.*, B.specialty as specialty "
            + "FROM employee as A LEFT OUTER JOIN ( "
            + "SELECT employee_id, GROUP_CONCAT(specialty SEPARATOR ', ') AS specialty "
            + "FROM specialty "
            + "GROUP BY employee_id) as B on (B.employee_id = A.id) "
            + "WHERE A.id in (SELECT employee_id FROM specialty WHERE specialty like ?) and role='doctor' and (A.first_name like ? or A.last_name like ? or concat_ws(' ',first_name,last_name) like ?) "
            + "GROUP BY A.id;";
    private static final String FIND_ALL_PATIENT_NAME
            = "SELECT id, first_name, last_name FROM patient";
    private static final String FIND_ALL_EMPLOYEE_NAME_WITH_ROLE
            = "SELECT id, first_name, last_name, role FROM employee WHERE role like ?";
    private static final String ASSIGN_DOCTOR
            = "UPDATE outpatient_dynamic SET "
            + "doctor_id=?,doctor_fn=?,doctor_ln=?,nurse_id=?,nurse_fn=?,nurse_ln=?,status=? "
            + "WHERE id=?";
    private static final String ADD_CHEIF_COMPLAINT
            = "INSERT INTO chief_complaint(outpatient_dynamic_id, patient_id, SNOMED_CT_Code, description) "
            + "VALUES(?,?,?,?);";
    private static final String ADD_VITAL_SIGN
            = "INSERT INTO vital_sign(outpatient_dynamic_id, patient_id, temperature, SPO2, weight, blood_pressure) "
            + "VALUES(?,?,?,?,?,?);";

    // Additional
    public static String[] DYNAMIN_DATA = {
        "WFN", // waiting for nurse
        "WFNI", // waiting for nurse(injection)
        "WFD" // waiting for doctor
    };

    public static double getTodayMillisecondsWithOutTime() {
        Calendar now = Calendar.getInstance();
        return (double) ((long) getMilliseconds(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH)));
    }

    public static double getTodayMillisecondsWithTime() {
        Calendar now = Calendar.getInstance();
        return (double) ((long) getMilliseconds(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND)));
    }

    public static long getMilliseconds(int year, int month, int day) {

        long days = 0;

        try {
            String cdate = String.format("%d%02d%02d", year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(cdate);
            days = date.getTime();
//            System.out.println(days);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;

    }

    public static long getMilliseconds(int year, int month, int day, int hour, int min, int sec) {

        long days = 0;

        try {
            String cdate = String.format("%d%02d%02d%02d%02d%02d", year, month, day, hour, min, sec);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = sdf.parse(cdate);
            days = date.getTime();
//            System.out.println(days);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;

    }

    // Query Function
    private boolean checkEmployeeEmailDuplicate(Employee em) {

        boolean isDuplicate = false;

        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(CHECK_EMPLOYEE_EMAIL_DUPLICATE);
            pstmt.setString(1, em.email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
//                p.email = rs.getString("email");
//                p.password = rs.getString("password");
                int count = Integer.parseInt(rs.getString("count"));
                if (count > 0) {
                    isDuplicate = true;
                }
            }
            em.errormsg = "";
        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            em.errormsg = "Email already exists! Please choose another email.";
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return isDuplicate;
    }

    public void insertNewEmployee(Employee em) {
        if (checkEmployeeEmailDuplicate(em)) {
            System.out.println("ERROR!!!! Email already exists!");
            em.errormsg = "Email already exists! Please choose another email.";
            return;
        }
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(ADD_EMPLOYEE_STMT_NEW, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, em.email);
            pstmt.setString(2, em.password);
            pstmt.setString(3, em.fn);
            pstmt.setString(4, em.ln);
            pstmt.setString(5, em.gender);
            pstmt.setString(6, em.phone);

//            pstmt.setString(7, em.location);
            pstmt.setString(7, em.role);
            pstmt.setString(8, em.license);

            pstmt.setString(9, em.address);
            pstmt.setString(10, em.city);
            pstmt.setString(11, em.state);
            pstmt.setString(12, em.zip);
            pstmt.setString(13, em.country);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            String autoInsertedKey = (rs.next()) ? rs.getString(1) : null;
            rs.close();

            if (em.getSpecialtyList().size() > 0) {
                insertNewEmployeeSpecialty(em, autoInsertedKey);
            }

            em.errormsg = "";//"Thank you for registering with us!";
        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            em.errormsg = "Email already exists! Please choose another email.";
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public void insertNewEmployeeSpecialty(Employee em, String id) {

        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            for (String spec : em.getSpecialtyList()) {
                pstmt = connect.prepareStatement(ADD_EMPLOYEE_SPECIALTY);

                pstmt.setString(1, id);
                pstmt.setString(2, spec);

                pstmt.executeUpdate();
            }

            em.errormsg = "";//"Thank you for registering with us!";
        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            em.errormsg = "Failed to add specialty.Failed to add specialty.";
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public void insertNewPatient(Patient p) {
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(ADD_PATIENT_STMT_NEW);

            pstmt.setString(1, p.email);
            pstmt.setString(2, p.password);
            pstmt.setString(3, p.fn);
            pstmt.setString(4, p.ln);
            pstmt.setString(5, p.gender);
            pstmt.setString(6, p.phone);

//            pstmt.setString(7, em.location);
            pstmt.setDouble(7, p.dob);
            pstmt.setString(8, p.occupation);
            pstmt.setString(9, p.religion);

            pstmt.setString(10, p.address);
            pstmt.setString(11, p.city);
            pstmt.setString(12, p.state);
            pstmt.setString(13, p.zip);
            pstmt.setString(14, p.country);

            pstmt.setString(15, p.emFN);
            pstmt.setString(16, p.emLN);
            pstmt.setString(17, p.emAddress);
            pstmt.setString(18, p.emCity);
            pstmt.setString(19, p.emState);
            pstmt.setString(20, p.emZip);
            pstmt.setString(21, p.emRelationship);
            pstmt.setString(22, p.emPhone);
            pstmt.setString(23, p.emEmail);

            pstmt.setString(24, p.posAddress);
            pstmt.setString(25, p.posCity);
            pstmt.setString(26, p.posState);
            pstmt.setString(27, p.posZip);

            pstmt.executeUpdate();

            p.errormsg = "";//"Thank you for registering with us!";
        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            p.errormsg = "Email already exists! Please choose another email.";
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public List<Employee> findEmployee(String findType, String findId, String findName, String findEmail, String findRole, String findSpecialty) {
        if (findId == null || "".equals(findId)) {
            findId = "%";
        }
        if (findName == null || "".equals(findName)) {
            findName = "%";
        } else {
            findName = "%" + findName + "%";
        }
        if (findEmail == null || "".equals(findEmail)) {
            findEmail = "%";
        }
        if (findRole == null || "".equals(findRole) || "all".equals(findRole)) {
            findRole = "%";
        }
        if (findSpecialty == null || "".equals(findSpecialty)) {
            findSpecialty = "%";
        }
        List<Employee> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            if ("or".equals(findType)) {
                pstmt = connect.prepareStatement(FIND_EMPLOYEE_OR);
            } else {
                pstmt = connect.prepareStatement(FIND_EMPLOYEE_AND);
            }
            pstmt.setString(1, findSpecialty);
            pstmt.setString(2, findId);
            pstmt.setString(3, findName);
            pstmt.setString(4, findName);
            pstmt.setString(5, findName);
            pstmt.setString(6, findEmail);
            pstmt.setString(7, findRole);
//            pstmt.setString(6, findSpecialty);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Employee em = new Employee();
                em.buildEmployee(rs);
                list.add(em);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findEmployee : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

//    private static final String FIND_PATIENT
//            = "SELECT * FROM patient "
//            + "WHERE first_name like ? or last_name like ? and date_of_birth like ?";
    public List<Patient> findPatient(String findId, String findName, String findDoB) {
        if (findId == null || "".equals(findId)) {
            findId = "%";
        }
        if (findName == null || "".equals(findName)) {
            findName = "%";
        } else {
            findName = "%" + findName + "%";
        }
        if (findDoB == null || "".equals(findDoB) || "0".equals(findDoB)) {
            findDoB = "%";
        }
        List<Patient> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_PATIENT);
            pstmt.setString(1, findId);
            pstmt.setString(2, findName);
            pstmt.setString(3, findName);
            pstmt.setString(4, findName);
            pstmt.setString(5, findDoB);
            System.out.println(pstmt.toString());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.buildPatient(rs);
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findEmployee : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

//    public void loginPatient(Patient p) {
//        PreparedStatement pstmt = null;
//        Connection connect = null;
//        try {
//            connect = DbConnectionPools.getPoolConnection();
//            pstmt = connect.prepareStatement(LOGIN_PATIENT);
//            pstmt.setString(1, p.email);
//            pstmt.setString(2, p.password);
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                p.buildPatient(rs);
//                p.errormsg = "Success";
//            }
//        } catch (Exception e) {
//        } finally {
//            DbConnectionPools.closeResources(connect, pstmt);
//        }
//    }

    public void loginEmployee(Employee em) {
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(LOGIN_EMPLOYEE);
            pstmt.setString(1, em.id);
            pstmt.setString(2, em.password);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                em.buildEmployee(rs);
                em.errormsg = "Success";
            }
        } catch (Exception e) {
            System.out.println("ERROR login Employee dao " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public void checkInPatient(Patient p) {
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(CHECK_IN_PATIENT);

            pstmt.setInt(1, Integer.parseInt(p.id));
            pstmt.setString(2, p.fn);
            pstmt.setString(3, p.ln);
            pstmt.setString(4, p.getGender());
            pstmt.setDouble(5, p.getDob());
            pstmt.setDouble(6, getTodayMillisecondsWithTime());
            pstmt.setString(7, DYNAMIN_DATA[0]);

            pstmt.executeUpdate();

            p.errormsg = "";//"Thank you for registering with us!";
        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            p.errormsg = "Failed to Check-In";
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        makeNewBillingInfo(p); // When patient Check-In, billing information is created automatically.
    }

    public void makeNewBillingInfo(Patient p) {
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(MAKE_NEW_BILLING);

            pstmt.setInt(1, Integer.parseInt(p.id));
            pstmt.setDouble(2, getTodayMillisecondsWithTime());

            pstmt.executeUpdate();

            p.errormsg += "";//"Thank you for registering with us!";
        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            p.errormsg += "Failed to Make Billing Info";
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public List<DynamicInfo> searchPatientInDynamic(String status) {
        if (status == null || "".equals(status)) {
            status = "%";
        }
        List<DynamicInfo> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(SEARCH_DYNAMIC_TABLE_WITH_STATUS);
            pstmt.setString(1, status);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DynamicInfo d = new DynamicInfo();
                d.buildDynamicInfo(rs);
                list.add(d);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findEmployee : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Employee> findDoctor(String findDocName) {
        if (findDocName == null || "".equals(findDocName)) {
            findDocName = "%";
        } else {
            findDocName = "%" + findDocName + "%";
        }
        List<Employee> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_DOCTOR);
            pstmt.setString(1, findDocName);
            pstmt.setString(2, findDocName);
            pstmt.setString(3, findDocName);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Employee em = new Employee();
                em.buildEmployee(rs);
                list.add(em);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findDoctor : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Employee> findDoctor(String findDocName, String findDocSpecialty) {
        if (findDocName == null || "".equals(findDocName)) {
            findDocName = "%";
        } else {
            findDocName = "%" + findDocName + "%";
        }
        if (findDocSpecialty == null || "".equals(findDocSpecialty)) {
            findDocSpecialty = "%";
        } else {
            findDocSpecialty = "%" + findDocSpecialty + "%";
        }
        List<Employee> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_DOCTOR_WITH_SPECIALTY);
            pstmt.setString(1, findDocSpecialty);
            pstmt.setString(2, findDocName);
            pstmt.setString(3, findDocName);
            pstmt.setString(4, findDocName);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Employee em = new Employee();
                em.buildEmployee(rs);
                list.add(em);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findDoctorWithSpecialty : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Patient> findAllPatientNames() {
        List<Patient> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_ALL_PATIENT_NAME);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.buildPatient(rs);
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findEmployee : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Employee> findAllEmployeeNames(String role) {
        if (role == null || "".equals(role)) {
            role = "%";
        }
        List<Employee> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_ALL_EMPLOYEE_NAME_WITH_ROLE);
            pstmt.setString(1, role);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Employee em = new Employee();
                em.buildEmployee(rs);
                list.add(em);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findEmployee : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public void assignDoctor(DynamicInfo d, Employee doc, Employee nurse, List<SynomedCT> cheifList, String temperature, String spo2, String weight, String bloodPressure) {
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(ASSIGN_DOCTOR);

            pstmt.setString(1, doc.id);
            pstmt.setString(2, doc.fn);
            pstmt.setString(3, doc.ln);
            pstmt.setString(4, nurse.id);
            pstmt.setString(5, nurse.fn);
            pstmt.setString(6, nurse.ln);
            pstmt.setString(7, DYNAMIN_DATA[2]);
            pstmt.setString(8, d.id);

            pstmt.executeUpdate();

            d.errormsg = "";//"Thank you for registering with us!";

            if (!addCheifComplaint(connect, d, cheifList)) {
                throw new Exception();
            }
            if (!addVitalSign(connect, d, temperature, spo2, weight, bloodPressure)) {
                throw new Exception();
            }

        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            d.errormsg += " Fail to Assign Doctor";
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public boolean addCheifComplaint(Connection connect, DynamicInfo d, List<SynomedCT> cheifList) {
        boolean result = false;
        PreparedStatement pstmt = null;
//        Connection connect = null;
        try {
//            connect = DbConnectionPools.getPoolConnection();
            for (SynomedCT s : cheifList) {
                pstmt = connect.prepareStatement(ADD_CHEIF_COMPLAINT);

                pstmt.setString(1, d.id);
                pstmt.setString(2, d.p.id);
                pstmt.setString(3, s.code);
                pstmt.setString(4, s.description);

                pstmt.executeUpdate();
            }
            result = true;

        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            d.errormsg += " Fail to add Cheif complaint";
        }
        return result;
    }

//    private static final String ADD_VITAL_SIGN
//            = "INSERT INTO vital_sign(outpatient_dynamic_id, patient_id, temperature, SPO2, weight, blood_pressure) "
//            + "VALUES(?,?,?,?,?,?);";
    public boolean addVitalSign(Connection connect, DynamicInfo d, String temperature, String spo2, String weight, String bloodPressure) {
        if(temperature == null && spo2 == null && weight == null && bloodPressure == null){
            return true;
        }else{
            if(temperature == null)
                temperature = "";
            if(spo2 == null)
                spo2 = "";
            if(weight == null)
                weight = "";
            if(bloodPressure == null)
                bloodPressure = "";
        }
        boolean result = false;
        PreparedStatement pstmt = null;
//        Connection connect = null;
        try {
//            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(ADD_VITAL_SIGN);

            pstmt.setString(1, d.id);
            pstmt.setString(2, d.p.id);
            pstmt.setString(3, temperature);
            pstmt.setString(4, spo2);
            pstmt.setString(5, weight);
            pstmt.setString(6, bloodPressure);

            pstmt.executeUpdate();

            result = true;

        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            d.errormsg += " Fail to add Vital Sign";
        }
        return result;
    }

}
