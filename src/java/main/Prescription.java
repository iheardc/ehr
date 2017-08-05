/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author tw
 */
public class Prescription {

    String id, comment, status;
    Double date, dateComplete;
    Patient p;
    Employee doc, n, pharmacist;
    
    List<PrescriptionDetail> detail;

    public Prescription() {
        p = new Patient();
        doc = new Employee();
        n = new Employee();
        pharmacist = new Employee();
        detail = new ArrayList<>();
        id = "";
        comment = "";
        status = "";
    }

    public Prescription(String id, String comment, String status, Double date, Double dateComplete, Patient p, Employee doc, Employee n, Employee pharmacist, List<PrescriptionDetail> detail) {
        this();
        this.id = id;
        this.comment = comment;
        this.status = status;
        this.date = date;
        this.dateComplete = dateComplete;
        this.p = p;
        this.doc = doc;
        this.n = n;
        this.pharmacist = pharmacist;
        this.detail = detail;
    }

    public ArrayList<String> getKeySet(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            ArrayList<String> columnName = new ArrayList<>();

            for (int i = 1; i <= count; i++) {
                columnName.add(metaData.getColumnLabel(i));
            }
            return columnName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void buildPrescription(ResultSet rs) {
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = rs.getString(column);
                }
                column = "comment";
                if (columnName.contains(column)) {
                    this.comment = rs.getString(column);
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
                column = "pharmacist_id";
                if (columnName.contains(column)) {
                    this.pharmacist.id = rs.getString(column);
                }
                column = "pharmacist_fn";
                if (columnName.contains(column)) {
                    this.pharmacist.fn = rs.getString(column);
                }
                column = "pharmacist_ln";
                if (columnName.contains(column)) {
                    this.pharmacist.ln = rs.getString(column);
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Double getDateComplete() {
        return dateComplete;
    }

    public void setDateComplete(Double dateComplete) {
        this.dateComplete = dateComplete;
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

    public Employee getPharmacist() {
        return pharmacist;
    }

    public void setPharmacist(Employee pharmacist) {
        this.pharmacist = pharmacist;
    }

    public List<PrescriptionDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<PrescriptionDetail> detail) {
        this.detail = detail;
    }

}
