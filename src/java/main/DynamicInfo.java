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
import java.util.List;

/**
 *
 * @author tw
 */
public class DynamicInfo {
    
    String id, status;
    Patient p;
    Employee doc;
    Employee n;
    Double date;
    
    // Cheif Complaint
    List<SynomedCT> cheifComplaintList;
    
    // Vital Sign
    String temperature, spo2, weight, bloodPressure;
    
    // Injection
    ArrayList<String[]> injectionList;
    
    String errormsg;

    public DynamicInfo() {
        p = new Patient();
        doc = new Employee();
        n = new Employee();
    }

    public DynamicInfo(String id, String status, Patient p, Employee doc, Employee n, Double date, List<SynomedCT> cheifComplaintList, String temperature, String spo2, String weight, String bloodPressure, ArrayList<String[]> injectionList, String errormsg) {
        this.id = id;
        this.status = status;
        this.p = p;
        this.doc = doc;
        this.n = n;
        this.date = date;
        this.cheifComplaintList = cheifComplaintList;
        this.temperature = temperature;
        this.spo2 = spo2;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.injectionList = injectionList;
        this.errormsg = errormsg;
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
    
    public void buildDynamicInfo(ResultSet rs){
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = Integer.toString(rs.getInt(column));
                }
                column = "date";
                if (columnName.contains(column)) {
                    this.date = rs.getDouble(column);
                }
                column = "status";
                if (columnName.contains(column)) {
                    this.status = rs.getString(column);
                }
                column = "patient_id";
                if (columnName.contains(column)) {
                    this.p.id = rs.getString(column);
                }
                column = "patient_fn";
                if (columnName.contains(column)) {
                    this.p.fn = rs.getString(column);
                }
                column = "patient_ln";
                if (columnName.contains(column)) {
                    this.p.ln = rs.getString(column);
                }
                column = "patient_gender";
                if (columnName.contains(column)) {
                    this.p.gender = rs.getString(column);
                }
                column = "patient_dob";
                if (columnName.contains(column)) {
                    this.p.dob = rs.getDouble(column);
                }
                column = "doctor_id";
                if (columnName.contains(column)) {
                    this.doc.id = rs.getString(column);
                }
                column = "doctor_fn";
                if (columnName.contains(column)) {
                    this.doc.fn = rs.getString(column);
                }
                column = "doctor_ln";
                if (columnName.contains(column)) {
                    this.doc.ln = rs.getString(column);
                }
                column = "nurse_id";
                if (columnName.contains(column)) {
                    this.n.id = rs.getString(column);
                }
                column = "nurse_fn";
                if (columnName.contains(column)) {
                    this.n.fn = rs.getString(column);
                }
                column = "nurse_ln";
                if (columnName.contains(column)) {
                    this.n.ln = rs.getString(column);
                }
                column = "temperature";
                if (columnName.contains(column)) {
                    this.temperature = rs.getString(column);
                }
                column = "SPO2";
                if (columnName.contains(column)) {
                    this.spo2 = rs.getString(column);
                }
                column = "weight";
                if (columnName.contains(column)) {
                    this.weight = rs.getString(column);
                }
                column = "blood_pressure";
                if (columnName.contains(column)) {
                    this.bloodPressure = rs.getString(column);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void getCheifComplaints(FinishListener listener){
        DbDAO dao = new DbDAO();
        cheifComplaintList = dao.getCheifComplaint(id);
        
        if(listener != null){
            listener.execute();
        }
    }
    
    public void getInjections(){
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }
    
    public String getStatusFull(){
        switch(status){
            case "WFN":
                return "Waiting for Nurse";
            case "WFNI":
                return "Waiting for Nurse(Injection)";
            case "CKI": //Michelle Added More Status Here
                return "Check-In";
            case "WFD":
                return "Waiting for Doctor without Result";
            case "WFR":
                return "Waiting for Result";    
            case "WFDR":
                return "Waiting for Doctor with Result";
            case "WFP":
                return "Waiting for Prescription";   
            case "WFB":
                return "Waiting for Bill";
            case "CKO":
                return "Check Out";    
            default:
                return "N/A";
        }
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public Patient getP() {
        return p;
    }

    public void setP(Patient p) {
        this.p = p;
    }

    public Employee getDoc() {
        return doc;
    }

    public void setDoc(Employee doc) {
        this.doc = doc;
    }

    public Employee getN() {
        return n;
    }

    public void setN(Employee n) {
        this.n = n;
    }

    public Double getDate() {
        return date;
    }

    public String getDateString() {
        return dateAndTimeToString((long)((double)date));
    }
    public static String dateAndTimeToString(long time){

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.format(currentDate);

    }

    public void setDate(Double date) {
        this.date = date;
    }

    public List<SynomedCT> getCheifComplaintList() {
        return cheifComplaintList;
    }

    public void setCheifComplaintList(List<SynomedCT> cheifComplaintList) {
        this.cheifComplaintList = cheifComplaintList;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public ArrayList<String[]> getInjectionList() {
        return injectionList;
    }

    public void setInjectionList(ArrayList<String[]> injectionList) {
        this.injectionList = injectionList;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }
    
    
    
    
}
