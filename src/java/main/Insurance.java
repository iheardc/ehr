/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author 김정훈
 */
public class Insurance {

    String id, patient_id, scheme;
    String location_id;
    double date_of_claim;
    String patient_type, patient_type2, patient_type3;
    String detail_type, detail_type2;
    String member_no, serial_no, hosp_record_no;
    String g_drg;
    String health_facility;
    double third_visit;
    double admission_date;
    double discharge_date;
    String spell, cc_code, physician;
    String specialty, specialty_code;
    String date_received, action1, signed1, signed2, action2, signed3;
    double date1, date2;
    ArrayList<Insur_diagnosis> insur_diag = new ArrayList<Insur_diagnosis>();
    ArrayList<Insur_investigations> insur_invest = new ArrayList<Insur_investigations>();
    ArrayList<Insur_medicine> insur_med = new ArrayList<Insur_medicine>();
    ArrayList<Insur_procedure> insur_proce = new ArrayList<Insur_procedure>();
    String errormsg;

    public Insurance() {
        
    }

    public Insurance(String id, String patient_id, String location_id, String scheme, double date_of_claim, String patient_type, String patient_type2, String patient_type3, String detail_type, String detail_type2, String member_no, String serial_no, String hosp_record_no, String g_drg, double third_visit, String spell, String cc_code, String physician, String specialty, String specialty_code, String date_received, String action1, String signed1, String signed2, String action2, String signed3, double date1, double date2, double admission_date, double discharge_date, String health_facility) {
        this.id = id;
        this.patient_id = patient_id;
        this.location_id=location_id;
        this.scheme = scheme;
        this.date_of_claim = date_of_claim;
        this.patient_type = patient_type;
        this.patient_type2 = patient_type2;
        this.patient_type3 = patient_type3;
        this.detail_type = detail_type;
        this.detail_type2 = detail_type2;
        this.member_no = member_no;
        this.serial_no = serial_no;
        this.hosp_record_no = hosp_record_no;
        this.g_drg = g_drg;
        this.third_visit = third_visit;
        this.spell = spell;
        this.cc_code = cc_code;
        this.physician = physician;
        this.specialty = specialty;
        this.specialty_code = specialty_code;
        this.date_received = date_received;
        this.action1 = action1;
        this.signed1 = signed1;
        this.signed2 = signed2;
        this.action2 = action2;
        this.signed3 = signed3;
        this.date1 = date1;
        this.date2 = date2;
        this.admission_date=admission_date;
        this.discharge_date=discharge_date;
        this.health_facility=health_facility;
    }

    public ArrayList<String> getKeySet(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount(); //number of column
//            String columnName[] = new String[count];
            ArrayList<String> columnName = new ArrayList<>();

            for (int i = 1; i <= count; i++) {
                columnName.add(metaData.getColumnLabel(i));
//                columnName[i - 1] = metaData.getColumnLabel(i);
//                System.out.println(columnName[i - 1]);
            }
            return columnName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void buildInsurance(ResultSet rs) {
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = Integer.toString(rs.getInt(column));
                }
                column = "patient_id";
                if (columnName.contains(column)) {
                    this.patient_id = Integer.toString(rs.getInt(column));
                }
                column = "location_id";
                if (columnName.contains(column)) {
                    this.location_id = Integer.toString(rs.getInt(column));
                }
                column = "scheme";
                if (columnName.contains(column)) {
                    this.scheme = rs.getString(column);
                }
                column = "date_of_claim";
                if (columnName.contains(column)) {
                    this.date_of_claim = rs.getDouble(column);
                }
                column = "patient_type";
                if (columnName.contains(column)) {
                    this.patient_type = rs.getString(column);
                }
                column = "patient_type2";
                if (columnName.contains(column)) {
                    this.patient_type2 = rs.getString(column);
                }
                column = "patient_type3";
                if (columnName.contains(column)) {
                    this.patient_type3 = rs.getString(column);
                }
                column = "detail_type";
                if (columnName.contains(column)) {
                    this.detail_type = rs.getString(column);
                }
                column = "detail_type2";
                if (columnName.contains(column)) {
                    this.detail_type2 = rs.getString(column);
                }
                column = "Member_No";
                if (columnName.contains(column)) {
                    this.member_no = rs.getString(column);
                }
                column = "Serial_No";
                if (columnName.contains(column)) {
                    this.serial_no = rs.getString(column);
                }

                column = "Hosp_Record_No";
                if (columnName.contains(column)) {
                    this.hosp_record_no = rs.getString(column);
                }
                column = "Ghana_DRG";
                if (columnName.contains(column)) {
                    this.g_drg = rs.getString(column);
                }
                column = "3rd_visit";
                if (columnName.contains(column)) {
                    this.third_visit = rs.getDouble(column);
                }
                column = "spell";
                if (columnName.contains(column)) {
                    this.spell = rs.getString(column);
                }
                column = "CC_Code";
                if (columnName.contains(column)) {
                    this.cc_code= rs.getString(column);
                }
                column = "health_facility";
                if (columnName.contains(column)) {
                    this.health_facility= rs.getString(column);
                }

                column = "admission_date";
                if (columnName.contains(column)) {
                    this.admission_date = rs.getDouble(column);
                }
                column = "discharge_date";
                if (columnName.contains(column)) {
                    this.discharge_date = rs.getDouble(column);
                }
                
                column = "Physician";
                if (columnName.contains(column)) {
                    this.physician = rs.getString(column);
                }
                column = "specialty";
                if (columnName.contains(column)) {
                    this.specialty = rs.getString(column);
                }
                column = "specialty_code";
                if (columnName.contains(column)) {
                    this.specialty_code = rs.getString(column);
                }
                column = "date_received";
                if (columnName.contains(column)) {
                    this.date_received = rs.getString(column);
                }
                column = "action1";
                if (columnName.contains(column)) {
                    this.action1 = rs.getString(column);
                }
                column = "date1";
                if (columnName.contains(column)) {
                    this.date1 = rs.getDouble(column);
                }
                column = "signed1";
                if (columnName.contains(column)) {
                    this.signed1 = rs.getString(column);
                }
                column = "signed2";
                if (columnName.contains(column)) {
                    this.signed2 = rs.getString(column);
                }
                column = "action2";
                if (columnName.contains(column)) {
                    this.action2 = rs.getString(column);
                }

                column = "date2";
                if (columnName.contains(column)) {
                    this.date2 = rs.getDouble(column);
                }
                column = "signed3";
                if (columnName.contains(column)) {
                    this.signed3 = rs.getString(column);
                }
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public double getDate_of_claim() {
        return date_of_claim;
    }

    public void setDate_of_claim(double date_of_claim) {
        this.date_of_claim = date_of_claim;
    }

    public String getPatient_type() {
        return patient_type;
    }

    public void setPatient_type(String patient_type) {
        this.patient_type = patient_type;
    }

    public String getPatient_type2() {
        return patient_type2;
    }

    public void setPatient_type2(String patient_type2) {
        this.patient_type2 = patient_type2;
    }

    public String getPatient_type3() {
        return patient_type3;
    }

    public void setPatient_type3(String patient_type3) {
        this.patient_type3 = patient_type3;
    }

    public String getDetail_type() {
        return detail_type;
    }

    public void setDetail_type(String detail_type) {
        this.detail_type = detail_type;
    }

    public String getDetail_type2() {
        return detail_type2;
    }

    public void setDetail_type2(String detail_type2) {
        this.detail_type2 = detail_type2;
    }

    public String getMember_no() {
        return member_no;
    }

    public void setMember_no(String member_no) {
        this.member_no = member_no;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getHosp_record_no() {
        return hosp_record_no;
    }

    public void setHosp_record_no(String hosp_record_no) {
        this.hosp_record_no = hosp_record_no;
    }

    public String getG_drg() {
        return g_drg;
    }

    public void setG_drg(String g_drg) {
        this.g_drg = g_drg;
    }

    public double getThird_visit() {
        return third_visit;
    }

    public void setThird_visit(double third_visit) {
        this.third_visit = third_visit;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getCc_code() {
        return cc_code;
    }

    public void setCc_code(String cc_code) {
        this.cc_code = cc_code;
    }

    public String getPhysician() {
        return physician;
    }

    public void setPhysician(String physician) {
        this.physician = physician;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSpecialty_code() {
        return specialty_code;
    }

    public void setSpecialty_code(String specialty_code) {
        this.specialty_code = specialty_code;
    }

    public double getAdmission_date() {
        return admission_date;
    }

    public void setAdmission_date(double admission_date) {
        this.admission_date = admission_date;
    }

    public double getDischarge_date() {
        return discharge_date;
    }

    public void setDischarge_date(double discharge_date) {
        this.discharge_date = discharge_date;
    }


    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
    }

    public String getAction1() {
        return action1;
    }

    public void setAction1(String action1) {
        this.action1 = action1;
    }

    public String getSigned1() {
        return signed1;
    }

    public void setSigned1(String signed1) {
        this.signed1 = signed1;
    }

    public String getSigned2() {
        return signed2;
    }

    public void setSigned2(String signed2) {
        this.signed2 = signed2;
    }

    public String getAction2() {
        return action2;
    }

    public void setAction2(String action2) {
        this.action2 = action2;
    }

    public String getSigned3() {
        return signed3;
    }

    public void setSigned3(String signed3) {
        this.signed3 = signed3;
    }

    public double getDate1() {
        return date1;
    }

    public void setDate1(double date1) {
        this.date1 = date1;
    }

    public double getDate2() {
        return date2;
    }

    public void setDate2(double date2) {
        this.date2 = date2;
    }

    public ArrayList<Insur_diagnosis> getInsur_diag() {
        return insur_diag;
    }

    public void setInsur_diag(ArrayList<Insur_diagnosis> insur_diag) {
        this.insur_diag = insur_diag;
    }

    public ArrayList<Insur_investigations> getInsur_invest() {
        return insur_invest;
    }

    public void setInsur_invest(ArrayList<Insur_investigations> insur_invest) {
        this.insur_invest = insur_invest;
    }

    public ArrayList<Insur_medicine> getInsur_med() {
        return insur_med;
    }

    public void setInsur_med(ArrayList<Insur_medicine> insur_med) {
        this.insur_med = insur_med;
    }

    public ArrayList<Insur_procedure> getInsur_proce() {
        return insur_proce;
    }

    public void setInsur_proce(ArrayList<Insur_procedure> insur_proce) {
        this.insur_proce = insur_proce;
    }

    public String getHealth_facility() {
        return health_facility;
    }

    public void setHealth_facility(String health_facility) {
        this.health_facility = health_facility;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }
    
    public String getDocString(){
        return getDateString((long) ((double) date_of_claim));
    
    }
    public String getAdmission_dateString() {
        if(admission_date==0.0)
        {
            return "";
        }
        return getDateString((long) ((double) admission_date));
    }
    
    public String getDischarge_dateString() {
        if(discharge_date==0.0)
        {
            return "";
        }
        return getDateString((long) ((double) discharge_date));
    }
    
    public String getThird_visitString() {
        if(third_visit==0.0)
        {
            return "";
        }
        return getDateString((long) ((double) third_visit));
    }


    public static String getDateString(long time) {

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(currentDate);
    }
}
