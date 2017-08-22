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
public class OrderInfo {
    
    String id, orderDescription, resultDescription, status;
    Double date, dateComplete;
    boolean isEmergency;
    LOINC loinc;
    Employee doc, tech;
    Patient p;
    String locationId;

    public OrderInfo() {
        p = new Patient();
        doc = new Employee();
        tech = new Employee();
        loinc = new LOINC();
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
    
    public void build(ResultSet rs) {
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = rs.getString(column);
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
                column = "patient_pic";
                if (columnName.contains(column)) {
                    this.p.arr = rs.getBytes(column);
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
                column = "technician_id";
                if (columnName.contains(column)) {
                    this.tech.id = rs.getString(column);
                }
                column = "radiologist_id";
                if (columnName.contains(column)) {
                    this.tech.id = rs.getString(column);
                }
                column = "tech_fn";
                if (columnName.contains(column)) {
                    this.tech.fn = rs.getString(column);
                }
                column = "tech_ln";
                if (columnName.contains(column)) {
                    this.tech.ln = rs.getString(column);
                }
                column = "LOINC_CODE";
                if (columnName.contains(column)) {
                    this.loinc.code = rs.getString(column);
                }
                column = "test_name";
                if (columnName.contains(column)) {
                    this.loinc.description = rs.getString(column);
                }
                column = "test_name_short";
                if (columnName.contains(column)) {
                    this.loinc.descriptionShort = rs.getString(column);
                }
                column = "order_description";
                if (columnName.contains(column)) {
                    this.orderDescription = rs.getString(column);
                }
                column = "result_description";
                if (columnName.contains(column)) {
                    this.resultDescription = rs.getString(column);
                }
                column = "status";
                if (columnName.contains(column)) {
                    this.status = rs.getString(column);
                }
                column = "date";
                if (columnName.contains(column)) {
                    this.date = rs.getDouble(column);
                }
                column = "date_complete";
                if (columnName.contains(column)) {
                    this.dateComplete = rs.getDouble(column);
                }
                column = "emergency";
                if (columnName.contains(column)) {
                    this.isEmergency = "1".equals(rs.getString(column));
                }
                column = "location_id";
                if (columnName.contains(column)) {
                    this.locationId = Integer.toString(rs.getInt(column));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public String getDateString(long time) {

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(currentDate);

    }
    public String getDateStringWithTime(long time) {

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.format(currentDate);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getDate() {
        return date;
    }

    public void setDate(Double date) {
        this.date = date;
    }
    
    public String getDateString(){
        return getDateStringWithTime((long)((double)date));
    }

    public Double getDateComplete() {
        return dateComplete;
    }

    public void setDateComplete(Double dateComplete) {
        this.dateComplete = dateComplete;
    }
    
    public String getDateCompleteString(){
        return getDateStringWithTime((long)((double)dateComplete));
    }

    public boolean isIsEmergency() {
        return isEmergency;
    }

    public void setIsEmergency(boolean isEmergency) {
        this.isEmergency = isEmergency;
    }

    public LOINC getLoinc() {
        return loinc;
    }

    public void setLoinc(LOINC loinc) {
        this.loinc = loinc;
    }

    public Employee getDoc() {
        return doc;
    }

    public void setDoc(Employee doc) {
        this.doc = doc;
    }

    public Employee getTech() {
        return tech;
    }

    public void setTech(Employee tech) {
        this.tech = tech;
    }

    public Patient getP() {
        return p;
    }

    public void setP(Patient p) {
        this.p = p;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    
    
    
}
