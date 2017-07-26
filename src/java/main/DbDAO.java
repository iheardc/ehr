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

    private static final String LOGIN_PATIENT = "SELECT * FROM patient WHERE email=? AND password=?";
    private static final String LOGIN_EMPLOYEE = "SELECT * FROM employee WHERE email=? AND password=?";
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
            + "SELECT employee_id, GROUP_CONCAT(specialty SEPARATOR ',') AS specialty "
            + "FROM specialty "
            + "GROUP BY employee_id) as B on (B.employee_id = A.id) "
            + "WHERE A.id in (SELECT employee_id FROM specialty WHERE specialty like ?) or A.id like ? or A.first_name like ? or A.last_name like ? or A.email like ? or A.role like ? "
            + "GROUP BY A.id;";
    private static final String FIND_EMPLOYEE_AND
            = "SELECT  A.*, B.specialty as specialty "
            + "FROM employee as A LEFT OUTER JOIN ("
            + "SELECT employee_id, GROUP_CONCAT(specialty SEPARATOR ',') AS specialty "
            + "FROM specialty "
            + "GROUP BY employee_id) as B on (B.employee_id = A.id) "
            + "WHERE A.id in (SELECT employee_id FROM specialty WHERE specialty like ?) and A.id like ? and A.first_name like ? and A.last_name like ? and A.email like ? and A.role like ? "
            + "GROUP BY A.id;";
    private static final String FIND_PATIENT
            = "SELECT * FROM patient "
            + "WHERE id like ? and (first_name like ? or last_name like ?) and date_of_birth like ?";
    private static final String CHECK_IN_PATIENT
            = "INSERT INTO outpatient_dynamic(patient_id, date, status) VALUES(?, ?, ?);";
    private static final String MAKE_NEW_BILLING
            = "INSERT INTO billing(billed_to, date) VALUES(?, ?)";

    // Additional
    public static String[] DYNAMIN_DATA = {
        "WFN", // waiting for nurse
        "WFNI" // waiting for nurse(injection)
    };

    public static double getTodayMilliseconds() {
        Calendar now = Calendar.getInstance();
        return (double) ((long) getMilliseconds(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH)));
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
        for (String spec : em.getSpecialtyList()) {
            PreparedStatement pstmt = null;
            Connection connect = null;
            try {
                connect = DbConnectionPools.getPoolConnection();
                pstmt = connect.prepareStatement(ADD_EMPLOYEE_SPECIALTY);

                pstmt.setString(1, id);
                pstmt.setString(2, spec);

                pstmt.executeUpdate();

                em.errormsg = "";//"Thank you for registering with us!";
            } catch (Exception e) {
                System.out.println("ERROR!!!!" + e.toString());
                em.errormsg = "Failed to add specialty.Failed to add specialty.";
            } finally {
                DbConnectionPools.closeResources(connect, pstmt);
            }
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
            pstmt.setString(5, findEmail);
            pstmt.setString(6, findRole);
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
            pstmt.setString(4, findDoB);

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

    public void checkInPatient(Patient p) {
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(CHECK_IN_PATIENT);

            pstmt.setInt(1, Integer.parseInt(p.id));
            pstmt.setDouble(2, getTodayMilliseconds());
            pstmt.setString(3, DYNAMIN_DATA[0]);

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
    
    public void makeNewBillingInfo(Patient p){
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(MAKE_NEW_BILLING);

            pstmt.setInt(1, Integer.parseInt(p.id));
            pstmt.setDouble(2, getTodayMilliseconds());

            pstmt.executeUpdate();

            p.errormsg += "";//"Thank you for registering with us!";
        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            p.errormsg += "Failed to Make Billing Info";
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

}
