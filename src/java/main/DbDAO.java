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
            + "address, city, state, zip, country, "
            + "pic) "
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            + "postalAddress, postalCity, postalState, postalZip, "
            + "pic) "
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
    private static final String FIND_PATIENT_WITH_DATE
            = "select * from patient as P "
            + "LEFT OUTER JOIN( "
            + "SELECT patient_id, count(id) as cot "
            + "FROM outpatient_dynamic "
            + "WHERE ? <= date and date <= ? "
            + "GROUP BY patient_id) as B on (B.patient_id = P.id) "
            + "where id like ? and (first_name like ? or last_name like ? or concat_ws(' ',first_name,last_name) like ?) and date_of_birth like ? and IFNULL(B.cot,0) <= 0";
    private static final String FIND_PATIENT
            = "SELECT * FROM patient "
            + "WHERE id like ? and (first_name like ? or last_name like ? or concat_ws(' ',first_name,last_name) like ?) and date_of_birth like ?";
    private static final String FIND_INSURMEDICINE
            = "SELECT * FROM insurance_medicine WHERE insurance_id = ?";
    private static final String FIND_INSURINVESTIGATIONS
            = "SELECT * FROM insurance_investigations WHERE insurance_id = ?";
    private static final String FIND_INSURDIAGNOSIS
            = "SELECT * FROM insurance_diagnosis WHERE insurance_id = ?";
    private static final String FIND_INSURPROCEDURE
            = "SELECT * FROM insurance_procedure WHERE insurance_id = ?";
    private static final String FIND_INSURANCE
            = "SELECT * FROM insurance "
            + "WHERE patient_id=?";
    private static final String CHECK_IN_PATIENT
            = "INSERT INTO outpatient_dynamic(patient_id, patient_fn, patient_ln, patient_gender, patient_dob, date, status) VALUES(?, ?, ?, ?, ?, ?, ?);";
    private static final String CHECK_OUT_PATIENT
            = "UPDATE outpatient_dynamic SET "
            + "status=?,date=? "
            + "WHERE id=?";
    private static final String MAKE_NEW_BILLING
            = "INSERT INTO billing(billed_to, date) VALUES(?, ?)";
    private static final String SEARCH_DYNAMIC_TABLE_WITH_STATUS
            = "SELECT A.*, B.pic as patient_pic "
            + "FROM outpatient_dynamic as A LEFT OUTER JOIN ( "
            + "SELECT id,pic FROM patient "
            + "GROUP BY id) as B on(B.id = A.patient_id) "
            + "WHERE A.status like ? and IFNULL(A.doctor_id,'') like ?"
            + "GROUP BY A.id;";
    private static final String SEARCH_DYNAMIC_TABLE_WITH_STATUS_AND_DATE
            = "SELECT A.*, B.pic as patient_pic "
            + "FROM outpatient_dynamic as A LEFT OUTER JOIN ( "
            + "SELECT id,pic FROM patient "
            + "GROUP BY id) as B on(B.id = A.patient_id) "
            + "WHERE A.status like ? and IFNULL(A.doctor_id,'') like ? and ? <= date and date <= ? "
            + "GROUP BY A.id;";
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
    private static final String GET_PATIENT_NAMES
            = "SELECT id, first_name, last_name FROM patient WHERE first_name like ? or last_name like ? or concat_ws(' ',first_name,last_name) like ?";
    private static final String GET_EMPLOYEE_NAMES_WITH_ROLE
            = "SELECT id, first_name, last_name, role FROM employee WHERE role like ? and (first_name like ? or last_name like ? or concat_ws(' ',first_name,last_name) like ?)";
    private static final String ASSIGN_DOCTOR
            = "UPDATE outpatient_dynamic SET "
            + "doctor_id=?,doctor_fn=?,doctor_ln=?,nurse_id=?,nurse_fn=?,nurse_ln=?,status=?,date=? "
            + "WHERE id=?";
    private static final String ADD_CHEIF_COMPLAINT
            = "INSERT INTO chief_complaint(outpatient_dynamic_id, patient_id, code, description) "
            + "VALUES(?,?,?,?);";
    private static final String ADD_VITAL_SIGN
            = "INSERT INTO vital_sign(outpatient_dynamic_id, patient_id, temperature, SPO2, weight, blood_pressure) "
            + "VALUES(?,?,?,?,?,?);";
    private static final String GET_CHEIF_COMPLAINT_FROM_DYNAMIC
            = "SELECT * FROM chief_complaint WHERE outpatient_dynamic_id=?";
    private static final String GET_VITAL_SIGN_FROM_DYNAMIC
            = "SELECT * FROM vital_sign WHERE outpatient_dynamic_id=?";
    private static final String FIND_ALL_STATUS//Michelle
            = "SELECT * FROM outpatient_dynamic";
    private static final String GET_PATIENT_VISIT_HISTORY
            = "SELECT  A.*, B.chief as chief , C.injection as injection, D.temperature, D.SPO2, D.weight, D.blood_pressure "
            + "FROM outpatient_dynamic as A "
            + "LEFT OUTER JOIN ( "
            + "SELECT outpatient_dynamic_id, GROUP_CONCAT(CONCAT(SNOMED_CT_Code, ':', description) SEPARATOR ', ') AS chief "
            + "FROM chief_complaint "
            + "GROUP BY outpatient_dynamic_id) as B on (B.outpatient_dynamic_id = A.id) "
            + "LEFT OUTER JOIN ( "
            + "SELECT outpatient_dynamic_id, GROUP_CONCAT(CONCAT(RxNORM_Code, ':', description) SEPARATOR ', ') AS injection "
            + "FROM injection "
            + "GROUP BY outpatient_dynamic_id) as C on (C.outpatient_dynamic_id = A.id) "
            + "LEFT OUTER JOIN ( "
            + "SELECT * FROM vital_sign "
            + "GROUP BY id) as D on (D.outpatient_dynamic_id = A.id) "
            + "WHERE A.patient_id like ? and A.id not like ? "
            + "GROUP BY A.id;";
    private static final String GET_RXNORM_CODES
            = "select * from rxnorm_code WHERE code like ? or description like ?";
    private static final String GET_HCPCS_CODES
            = "select * from hcpcs_code WHERE code like ? or description like ?";
    private static final String GET_SNOMEDCT_CODES
            = "select * from snomed_ct_code WHERE code like ? or description like ?";
    private static final String UPDATE_PATIENT_DAYNAMIC_STATUS
            = "UPDATE outpatient_dynamic SET "
            + "date=?,status=? "
            + "WHERE id=?";
    private static final String FIND_PATIENT_BILLING
            = "SELECT  A.*, ifnull(B.total,0) as total, (ifnull(B.total, 0) - ifnull(C.paid, 0)) as bal "
            + "FROM billing as A "
            + "LEFT OUTER JOIN ( "
            + "SELECT billing_SEQ, SUM(amount) as total "
            + "FROM charge_detail "
            + "GROUP BY billing_SEQ) as B on (B.billing_SEQ = A.SEQ) "
            + "LEFT OUTER JOIN ( "
            + "SELECT billing_SEQ, SUM(paid_amount) as paid "
            + "FROM payment "
            + "GROUP BY billing_SEQ) as C on (C.billing_SEQ = A.SEQ) "
            + "WHERE billed_to like ? and PIF is null "
            + "GROUP BY A.SEQ";

    // Additional
    public static String[] DYNAMIN_DATA = {
        "WFN", // waiting for nurse
        "WFNI", // waiting for nurse(injection)
        "WFD", // waiting for doctor

        // Michelle added below
        "WFR", // Waiting for Result  
        "WFDR", // Waiting for Doctor with Result
        "WFP", // Waiting for Prescription
        "WFB", // Waiting for Bill
        "CKO", // Check Out
        "ADM", // Admission
        "TRF" // Transfer
    };

    public static double getTodayMillisecondsWithoutTime() {
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

    public static Date getTodayDateWithTime() {
        Calendar now = Calendar.getInstance();
        Date date = null;
        try {
            String cdate = String.format("%d%02d%02d%02d%02d%02d", now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            date = sdf.parse(cdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date getTodayDate() {
        Calendar now = Calendar.getInstance();
        Date date = null;
        try {
            String cdate = String.format("%d%02d%02d", now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            date = sdf.parse(cdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static int getDateYear(long time) {

        Date currentDate = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return Integer.parseInt(df.format(currentDate));

    }

    public static String getDateString(long time) {

        Date currentDate = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
        return df.format(currentDate);

    }

    public static String getTodayDateString() {
        return getDateString((long) ((double) getTodayMillisecondsWithTime()));
    }

    public static Double dateToDouble(Date date) {
        return Double.parseDouble(date.getTime() + "");
    }

    // Query Function
    private boolean checkEmployeeEmailDuplicate(Employee em) {
        if (em.email != null || !"".equals(em.email)) {
            return false;
        }
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

            pstmt.setBytes(14, em.arr);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            String autoInsertedKey = (rs.next()) ? rs.getString(1) : null;
            rs.close();

            if (em.getSpecialtyList().size() > 0) {
                insertNewEmployeeSpecialty(connect, em, autoInsertedKey);
            }

            em.errormsg = "";//"Thank you for registering with us!";
        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            em.errormsg = "Email already exists! Please choose another email.";
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public void insertNewEmployeeSpecialty(Connection connect, Employee em, String id) {

        PreparedStatement pstmt = null;
        try {
//            connect = DbConnectionPools.getPoolConnection();
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

            pstmt.setBytes(28, p.arr);

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

    public List<Insur_procedure> findInsProcedure(String findId) {
        List<Insur_procedure> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_INSURPROCEDURE);
            pstmt.setString(1, findId);
            System.out.println(pstmt.toString());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Insur_procedure ins = new Insur_procedure();
                ins.buildInsurprocedure(rs);
                list.add(ins);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findInsurance procedure : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Insur_diagnosis> findInsDiagnosis(String findId) {
        List<Insur_diagnosis> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_INSURDIAGNOSIS);
            pstmt.setString(1, findId);
            System.out.println(pstmt.toString());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Insur_diagnosis ins = new Insur_diagnosis();
                ins.buildInsurdiagnosis(rs);
                list.add(ins);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findInsurance diagnosis : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Insur_investigations> findInsInvestigations(String findId) {
        List<Insur_investigations> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_INSURINVESTIGATIONS);
            pstmt.setString(1, findId);
            System.out.println(pstmt.toString());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Insur_investigations ins = new Insur_investigations();
                ins.buildInsurinvestigations(rs);
                list.add(ins);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findInsurance investigations : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Insur_medicine> findInsMedicine(String findId) {
        List<Insur_medicine> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_INSURMEDICINE);
            pstmt.setString(1, findId);
            System.out.println(pstmt.toString());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Insur_medicine ins = new Insur_medicine();
                ins.buildInsurmedicine(rs);
                list.add(ins);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findInsurance medicine : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Insurance> findInsuracne(String findId) {
        List<Insurance> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_INSURANCE);
            pstmt.setString(1, findId);
            System.out.println(pstmt.toString());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Insurance ins = new Insurance();
                ins.buildInsurance(rs);
                list.add(ins);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findInsurance : " + e.toString());
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
            System.out.println("ERROR!!!! findPatient : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Patient> findPatientWithDate(Double date, String findId, String findName, String findDoB) {
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
            pstmt = connect.prepareStatement(FIND_PATIENT_WITH_DATE);
            pstmt.setDouble(1, date - 86400000);
            pstmt.setDouble(2, date);
            pstmt.setString(3, findId);
            pstmt.setString(4, findName);
            pstmt.setString(5, findName);
            pstmt.setString(6, findName);
            pstmt.setString(7, findDoB);
            System.out.println(pstmt.toString());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.buildPatient(rs);
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! findPatientWithDate : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

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

    public List<DynamicInfo> searchPatientInDynamic(String status, String doctor_id) {
        if (status == null || "".equals(status) || "All".equals(status)) {
            status = "%";
        }
        if (doctor_id == null || "".equals(doctor_id)) {
            doctor_id = "%";
        }
        List<DynamicInfo> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();

            pstmt = connect.prepareStatement(SEARCH_DYNAMIC_TABLE_WITH_STATUS);
            pstmt.setString(1, status);
            pstmt.setString(2, doctor_id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DynamicInfo d = new DynamicInfo();
                d.buildDynamicInfo(rs);
                list.add(d);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! searchPatientInDynamic : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<DynamicInfo> searchPatientInDynamicWithDate(String status, String doctor_id, Double start, Double finish) {
        if (status == null || "".equals(status) || "ALL".equals(status)) {
            status = "%";
        }
        if (doctor_id == null || "".equals(doctor_id)) {
            doctor_id = "%";
        }
        List<DynamicInfo> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();

            pstmt = connect.prepareStatement(SEARCH_DYNAMIC_TABLE_WITH_STATUS_AND_DATE);
            pstmt.setString(1, status);
            pstmt.setString(2, doctor_id);
            pstmt.setDouble(3, start);
            pstmt.setDouble(4, finish);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DynamicInfo d = new DynamicInfo();
                d.buildDynamicInfo(rs);
                list.add(d);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! searchPatientInDynamicWithDate : " + e.toString());
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

    public List<Patient> getPatientNames(String query) {
        if (query == null || "".equals(query)) {
            query = "%";
        } else {
            query = "%" + query + "%";
        }
        List<Patient> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_PATIENT_NAMES);

            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.buildPatient(rs);
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! getPatientNames : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<Employee> getEmployeeNames(String role, String query) {
        if (role == null || "".equals(role)) {
            role = "%";
        }
        if (query == null || "".equals(query)) {
            query = "%";
        } else {
            query = "%" + query + "%";
        }
        List<Employee> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_EMPLOYEE_NAMES_WITH_ROLE);

            pstmt.setString(1, role);
            pstmt.setString(2, query);
            pstmt.setString(3, query);
            pstmt.setString(4, query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Employee em = new Employee();
                em.buildEmployee(rs);
                list.add(em);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! getEmployeeNames : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public String changePatientDynamicStatus(String id, String status) {
        String result = "";
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(UPDATE_PATIENT_DAYNAMIC_STATUS);

            pstmt.setDouble(1, getTodayMillisecondsWithTime());
            pstmt.setString(2, status);
            pstmt.setString(3, id);

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            result = e.getMessage();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return result;
    }

    public void assignDoctor(DynamicInfo d, Employee doc, Employee nurse, List<SnomedCT> cheifList, String temperature, String spo2, String weight, String bloodPressure) {
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
            pstmt.setDouble(8, getTodayMillisecondsWithTime());
            pstmt.setString(9, d.id);

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

    public boolean addCheifComplaint(Connection connect, DynamicInfo d, List<SnomedCT> cheifList) {
        boolean result = false;
        PreparedStatement pstmt = null;
//        Connection connect = null;
        try {
//            connect = DbConnectionPools.getPoolConnection();
            for (SnomedCT s : cheifList) {
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
        if (temperature == null && spo2 == null && weight == null && bloodPressure == null) {
            return true;
        } else {
            if (temperature == null) {
                temperature = "";
            }
            if (spo2 == null) {
                spo2 = "";
            }
            if (weight == null) {
                weight = "";
            }
            if (bloodPressure == null) {
                bloodPressure = "";
            }
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

    public List<SnomedCT> getCheifComplaint(String dynamicId) {
        if (dynamicId == null) {
            dynamicId = "";
        }
        List<SnomedCT> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_CHEIF_COMPLAINT_FROM_DYNAMIC);
            pstmt.setString(1, dynamicId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SnomedCT s = new SnomedCT();
                s.buildSnomed(rs);
                list.add(s);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! getCheifComplaint : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public void getVitalSign(DynamicInfo d) {
        if (d == null) {
            return;
        }
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_VITAL_SIGN_FROM_DYNAMIC);
            pstmt.setString(1, d.id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                d.buildDynamicInfoVital(rs);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! getCheifComplaint : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    // Michelle added. Find ALL patient status
    public List<DynamicInfo> findPatientStatus() {

        List<DynamicInfo> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_ALL_STATUS);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DynamicInfo d = new DynamicInfo();
                d.buildDynamicInfo(rs);
                list.add(d);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public void patientVisitHistory(DynamicInfo d) {
        List<DynamicInfo> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_PATIENT_VISIT_HISTORY);
            pstmt.setString(1, d.p.id);
            pstmt.setString(2, d.id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DynamicInfo dy = new DynamicInfo();
                dy.buildDynamicInfo(rs);
                list.add(dy);
            }
            d.setPreviousHistory(list);
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public List<RxNORM> getRxNORMCodes(String query) {
        if (query == null || "".equals(query)) {
            query = "%";
        } else {
            query = "%" + query + "%";
        }
        List<RxNORM> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_RXNORM_CODES);
            pstmt.setString(1, query);
            pstmt.setString(2, query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                RxNORM rx = new RxNORM();
                rx.buildRxNORM(rs);
                list.add(rx);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<HCPCS> getHCPCSCodes(String query) {
        if (query == null || "".equals(query)) {
            query = "%";
        } else {
            query = "%" + query + "%";
        }
        List<HCPCS> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_HCPCS_CODES);
            pstmt.setString(1, query);
            pstmt.setString(2, query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                HCPCS rx = new HCPCS();
                rx.buildHCPCS(rs);
                list.add(rx);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<SnomedCT> getSNOMEDCTCodes(String query) {
        if (query == null || "".equals(query)) {
            query = "%";
        } else {
            query = "%" + query + "%";
        }
        List<SnomedCT> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_SNOMEDCT_CODES);
            pstmt.setString(1, query);
            pstmt.setString(2, query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SnomedCT sn = new SnomedCT();
                sn.buildSnomed(rs);
                list.add(sn);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    private static final String INSERT_PRESCRIPTION
            = "INSERT INTO prescription("
            + "doctor_id, patient_id, comment, status, date) "
            + "VALUES(?,?,?,?,?)";

    public boolean insertNewPrescription(Patient p, Employee doc, Prescription pres) {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(INSERT_PRESCRIPTION, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, doc.id);
            pstmt.setString(2, p.id);
            pstmt.setString(3, pres.comment);
            pstmt.setString(4, "WFP");
            pstmt.setDouble(5, getTodayMillisecondsWithTime());

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            String autoInsertedKey = (rs.next()) ? rs.getString(1) : null;
            rs.close();

            if (pres.getDetail().size() > 0) {
                insertPrescriptionDetail(connect, pres, autoInsertedKey);
            }
            result = true;
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return result;
    }

    private static final String INSERT_PRESCRIPTION_DETAIL
            = "INSERT INTO prescription_detail("
            + "prescription_id, RxNORM_code, name, single_dose, num_of_daily_dose, total_dosing_days, description) "
            + "VALUES(?,?,?,?,?,?,?)";

    public void insertPrescriptionDetail(Connection connect, Prescription pres, String id) {

        PreparedStatement pstmt = null;
        try {
//            connect = DbConnectionPools.getPoolConnection();
            for (PrescriptionDetail de : pres.getDetail()) {
                pstmt = connect.prepareStatement(INSERT_PRESCRIPTION_DETAIL);

                pstmt.setString(1, id);
                pstmt.setString(2, de.rx.code);
                pstmt.setString(3, de.rx.description);
                pstmt.setDouble(4, de.singleDose);
                pstmt.setDouble(5, de.numOfDailyDos);
                pstmt.setDouble(6, de.totalDosingDays);
                pstmt.setString(7, de.usage);

                pstmt.executeUpdate();
            }

//            em.errormsg = "";//"Thank you for registering with us!";
        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
//            em.errormsg = "Failed to add specialty.Failed to add specialty.";
        }
    }

    public static final String ADD_PRESCRIPTION_CHARGE_CODE
            = "INSERT INTO charge_detail(billing_SEQ, charge_code, amount) VALUES( "
            + "(SELECT SEQ FROM billing WHERE billed_to like ? order by SEQ DESC limit 0, 1), "
            + "?, "
            + "(SELECT amount FROM charge_code WHERE code = ?) "
            + ")";
    public static final String COMPLETE_PRESCRIPTION
            = "UPDATE prescription SET status='DONE', date_complete=? WHERE id=?";

    public boolean completePrescription(Prescription pres) {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {

            int count = 0;
            connect = DbConnectionPools.getPoolConnection();
            for (PrescriptionDetail de : pres.detail) {

                pstmt = connect.prepareStatement(ADD_PRESCRIPTION_CHARGE_CODE);
                pstmt.setString(1, pres.p.id);
                pstmt.setString(2, de.rx.code);
                pstmt.setString(3, de.rx.code);

                pstmt.executeUpdate();

                count += 1;

            }

            if (count == pres.detail.size()) {
                pstmt = connect.prepareStatement(COMPLETE_PRESCRIPTION);
                pstmt.setDouble(1, getTodayMillisecondsWithTime());
                pstmt.setString(2, pres.id);

                pstmt.executeUpdate();
            }

            result = true;

        } catch (Exception e) {
            System.out.println("ERROR! Add Prescription Bill" + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return result;
    }

    public List<PayInfo> getChargesDueList(Patient p) {
        List<PayInfo> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(FIND_PATIENT_BILLING);
            pstmt.setString(1, p.id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PayInfo pa = new PayInfo();
                System.out.println(rs.getDouble("bal"));
                pa.buildInfo(rs);
                list.add(pa);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public static final String GET_PAY_DETAIL_INFO
            = "SELECT * FROM charge_detail WHERE billing_SEQ=?";

    public void getPaymentDetailInfo(PayInfo pi) {
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_PAY_DETAIL_INFO);
            pstmt.setString(1, pi.SEQ);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PayDetail pa = new PayDetail();
                pa.buildInfo(rs);
                pi.detail.add(pa);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public static final String GET_PAYMENT_HISTORY
            = "SELECT * FROM payment WHERE billing_SEQ=?";

    public void getPaymentHistory(PayInfo pi) {
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_PAYMENT_HISTORY);
            pstmt.setString(1, pi.SEQ);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Payment pa = new Payment();
                pa.buildInfo(rs);
                pi.history.add(pa);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public void checkOutPatient(DynamicInfo d) {
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(CHECK_OUT_PATIENT);

            pstmt.setString(1, DYNAMIN_DATA[7]);
            pstmt.setDouble(2, getTodayMillisecondsWithTime());
            pstmt.setString(3, d.id);

            pstmt.executeUpdate();

            d.errormsg = "";//"Thank you for registering with us!";

        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
            d.errormsg += e.getMessage();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
    }

    public static final String GET_PRESCRIPTION
            = "SELECT A.*, B.first_name as patient_fn, B.last_name as patient_ln, B.pic as patient_pic, C.first_name as doctor_fn, C.last_name as doctor_ln, D.first_name as pharmacist_fn, D.last_name as pharmacist_ln "
            + "FROM prescription as A "
            + "LEFT OUTER JOIN ( "
            + "SELECT * FROM patient "
            + "GROUP BY id) as B on(B.id = A.patient_id) "
            + "LEFT OUTER JOIN ( "
            + "SELECT * FROM employee "
            + "GROUP BY id) as C on(C.id = A.doctor_id) "
            + "LEFT OUTER JOIN ( "
            + "SELECT * FROM employee "
            + "GROUP BY id) as D on(D.id = A.pharmacist_id) "
            + "WHERE status like ? and ifnull(pharmacist_id,'') like ? "
            + "GROUP BY A.id;";
    public static final String GET_PRESCRIPTION_DETAIL
            = "select * from prescription_detail WHERE prescription_id = ?";

    public List<Prescription> getPrescriptionList(String status, String pharmacistId) {
        if (status == null || status.isEmpty()) {
            status = "%";
        }
        if (pharmacistId == null || pharmacistId.isEmpty()) {
            pharmacistId = "%";
        }
        List<Prescription> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_PRESCRIPTION);
            pstmt.setString(1, status);
            pstmt.setString(2, pharmacistId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Prescription pr = new Prescription();
                pr.buildPrescription(rs);
                pr.detail = getPrescriptionDetailList(connect, pr.id);
                list.add(pr);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<PrescriptionDetail> getPrescriptionDetailList(Connection connect, String id) {
        List<PrescriptionDetail> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        try {
            pstmt = connect.prepareStatement(GET_PRESCRIPTION_DETAIL);
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PrescriptionDetail pr = new PrescriptionDetail();
                pr.buildDetail(rs);
                list.add(pr);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        }
        return list;
    }

    public static final String ACCEPT_PRESCRIPTION
            = "UPDATE prescription SET pharmacist_id=?, status='ING' WHERE id=?";

    public boolean acceptPrescription(Prescription pres, Employee pharmacist) {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(ACCEPT_PRESCRIPTION);

            pstmt.setString(1, pharmacist.id);
            pstmt.setString(2, pres.id);

            pstmt.executeUpdate();

            result = true;

        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return result;
    }

// INVENTORY ----------------------------------------------------------------------
    public static final String ADD_NEW_CLINICAL_ITEM
            = "INSERT INTO medicine_inventory(RxNORM_code, name, threshold, purchase_price, sell_price) VALUES(?,?,?,?,?)";

    public boolean insertNewClinical(ClinicalInven cl) {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(ADD_NEW_CLINICAL_ITEM);

            pstmt.setString(1, cl.rx.code);
            pstmt.setString(2, cl.rx.description);
            pstmt.setDouble(3, cl.threshold);
            pstmt.setDouble(4, cl.purchasePrice);
            pstmt.setDouble(5, cl.sellPrice);

            pstmt.executeUpdate();

            result = true;

        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return result;
    }
    public static final String UPDATE_CLINICAL_ITEM_BASIC_INFO
            = "UPDATE medicine_inventory SET threshold=?, purchase_price=?, sell_price=? WHERE RxNORM_code=?";

    public boolean updateClinicalBasicInfo(ClinicalInven cl) {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(UPDATE_CLINICAL_ITEM_BASIC_INFO);

            pstmt.setDouble(1, cl.threshold);
            pstmt.setDouble(2, cl.purchasePrice);
            pstmt.setDouble(3, cl.sellPrice);
            pstmt.setString(4, cl.rx.code);

            pstmt.executeUpdate();

            result = true;

        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return result;
    }
    public static final String ADD_CLINICAL_ITEM
            = "INSERT INTO inventory_detail(RxNORM_code, qty, used_qty, registered_date, expire_date) VALUES(?,?,'0',?,?)";

    public boolean addClinicalItem(ClinicalInven cl, List<ClinicalInvenDetail> detail) {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            for (ClinicalInvenDetail cid : detail) {
                pstmt = connect.prepareStatement(ADD_CLINICAL_ITEM);

                pstmt.setString(1, cl.rx.code);
                pstmt.setDouble(2, cid.qty);
                pstmt.setDouble(3, getTodayMillisecondsWithTime());
                pstmt.setDouble(4, cid.expireDate);

                pstmt.executeUpdate();
            }

            result = true;

        } catch (Exception e) {
            System.out.println("ERROR!!!!" + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return result;
    }


    public static final String GET_CLINICAL_ITEM
            = "SELECT  A.*, ifnull(B.qty,0) as current_qty, ifnull(C.expired_qty, 0) as expired_qty "
            + "FROM medicine_inventory as A "
            + "LEFT OUTER JOIN ( "
            + "SELECT RxNORM_code, (SUM(qty) - SUM(used_qty)) as qty "
            + "FROM inventory_detail "
            + "WHERE qty > used_qty "
            + "GROUP BY RxNORM_code) as B on (B.RxNORM_code = A.RxNORM_code) "
            + "LEFT OUTER JOIN ( "
            + "SELECT RxNORM_code, SUM(qty-used_qty) as expired_qty "
            + "FROM inventory_detail "
            + "WHERE qty > used_qty and expire_date < ? "
            + "group by RxNORM_code "
            + ") as C on (C.RXNORM_code = A.RxNORM_code) "
            + "WHERE A.RxNORM_code like ? "
            + "GROUP BY A.RxNORM_code";

    public List<ClinicalInven> getClinicalItems(RxNORM rx) {
        String code = "";
        if (rx == null) {
            code = "%";
        } else {
            code = rx.code;
        }
        List<ClinicalInven> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_CLINICAL_ITEM);
            pstmt.setDouble(1, getTodayMillisecondsWithoutTime());
            pstmt.setString(2, code);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ClinicalInven c = new ClinicalInven();
                c.build(rs);
                c.detail = getClinicalItemDetails(connect, c);
                list.add(c);
            }
        } catch (Exception e) {
            System.out.println("ERROR! getClinicalItems" + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public static final String GET_CLINICAL_ITEM_DETAIL
            = "SELECT * FROM inventory_detail WHERE RxNORM_code = ? ORDER BY id DESC";

    public List<ClinicalInvenDetail> getClinicalItemDetails(Connection connect, ClinicalInven cl) {
        List<ClinicalInvenDetail> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        try {
            pstmt = connect.prepareStatement(GET_CLINICAL_ITEM_DETAIL);
            pstmt.setString(1, cl.rx.code);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ClinicalInvenDetail c = new ClinicalInvenDetail();
                c.build(rs);
                list.add(c);
            }
        } catch (Exception e) {
            System.out.println("ERROR! getClinicalItemDetails" + e.toString());
        }
        return list;
    }

    public static final String GET_SUPPLY
            = "SELECT A.*, B.id, B.qty, B.used_qty, B.registered_date "
            + "FROM supply_inventory as A "
            + "LEFT OUTER JOIN ( "
            + "SELECT * FROM supply_detail "
            + "GROUP BY id) as B on(B.supply_inventory_id = A.id) "
            + "WHERE name like ? and type like ? "
            + "GROUP BY A.id;";
    public static final String GET_SUPPLY_DETAIL
            = "select * from supply_detail WHERE supply_inventory_id = ?";

    public List<InventorySupply> getSupplyList(String name, String type) {
        if (name == null || name.isEmpty()) {
            name = "%";
        }
        if (type == null || type.isEmpty()) {
            type = "%";
        }
        List<InventorySupply> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_SUPPLY);
            pstmt.setString(1, name);
            pstmt.setString(2, type);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                InventorySupply i = new InventorySupply();
                i.buildInventorySupply(rs);
                i.supplyDetail = getSupplyDetailList(connect, i.id);
                list.add(i);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

    public List<InventorySupplyDetail> getSupplyDetailList(Connection connect, String id) {
        List<InventorySupplyDetail> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        try {
            pstmt = connect.prepareStatement(GET_SUPPLY_DETAIL);
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                InventorySupplyDetail isd = new InventorySupplyDetail();
                isd.buildDetail(rs);
                list.add(isd);
            }
        } catch (Exception e) {
            System.out.println("ERROR! " + e.toString());
        }
        return list;
    }

    // For auto-completion of Name text field in manage_supply_inventory.xhtml
    private static final String GET_SUPPLY_NAMES
            = "SELECT name FROM supply_inventory WHERE name like ? or concat_ws(' ',name) like ?";

    public List<InventorySupply> getSupplyNames(String query) {
        if (query == null || "".equals(query)) {
            query = "%";
        } else {
            query = "%" + query + "%";
        }
        List<InventorySupply> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection connect = null;
        try {
            connect = DbConnectionPools.getPoolConnection();
            pstmt = connect.prepareStatement(GET_SUPPLY_NAMES);

            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                InventorySupply i = new InventorySupply();
                i.buildInventorySupply(rs);
                list.add(i);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!! getSupplyNames : " + e.toString());
            e.printStackTrace();
        } finally {
            DbConnectionPools.closeResources(connect, pstmt);
        }
        return list;
    }

}
