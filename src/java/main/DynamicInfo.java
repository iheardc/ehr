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
 * @author tw
 */
public class DynamicInfo {
    
    String id, status;
    Patient p;
    Employee doc;
    Employee n;
    Double date;
    
    // Injection
    String injectionMedicine;
    Double injectionDose;

    public DynamicInfo() {
        p = new Patient();
        doc = new Employee();
        n = new Employee();
    }

    public DynamicInfo(String id, String status, Patient p, Employee doc, Employee n, Double date, String injectionMedicine, Double injectionDose) {
        this();
        this.id = id;
        this.status = status;
        this.p = p;
        this.doc = doc;
        this.n = n;
        this.date = date;
        this.injectionMedicine = injectionMedicine;
        this.injectionDose = injectionDose;
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
                column = "injeaction_medicine";
                if (columnName.contains(column)) {
                    this.injectionMedicine = rs.getString(column);
                }
                column = "injection_dose";
                if (columnName.contains(column)) {
                    this.injectionDose = rs.getDouble(column);
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

    public String getStatus() {
        return status;
    }
    
    public String getStatusFull(){
        switch(status){
            case "WFN":
                return "Waiting for Nurse";
            case "WFNI":
                return "Waiting for Nurse(Injection)";
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

    public String getInjectionMedicine() {
        return injectionMedicine;
    }

    public void setInjectionMedicine(String injectionMedicine) {
        this.injectionMedicine = injectionMedicine;
    }

    public Double getInjectionDose() {
        return injectionDose;
    }

    public void setInjectionDose(Double injectionDose) {
        this.injectionDose = injectionDose;
    }

    
    
}
