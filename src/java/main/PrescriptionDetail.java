/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

/**
 *
 * @author tw
 */
public class PrescriptionDetail {
    
    String id, type, usage;
    RxNORM rx;
    double singleDose, numOfDailyDos, totalDosingDays;

    public PrescriptionDetail() {
        rx = new RxNORM();
        id = "";
        type = "";
        usage = "";
        singleDose = 0.0;
        numOfDailyDos = 0.0;
        totalDosingDays = 0.0;
    }

    public PrescriptionDetail(String id, String type, String usage, RxNORM rx, double singleDose, double numOfDailyDos, double totalDosingDays) {
        this();
        this.id = id;
        this.type = type;
        this.usage = usage;
        this.rx = rx;
        this.singleDose = singleDose;
        this.numOfDailyDos = numOfDailyDos;
        this.totalDosingDays = totalDosingDays;
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
    
    public void buildDetail(ResultSet rs){
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = rs.getString(column);
                }
                column = "type";
                if (columnName.contains(column)) {
                    this.type = rs.getString(column);
                }
                column = "usage";
                if (columnName.contains(column)) {
                    this.usage = rs.getString(column);
                }
                column = "single_dose";
                if (columnName.contains(column)) {
                    this.singleDose = rs.getDouble(column);
                }
                column = "num_of_daily_dose";
                if (columnName.contains(column)) {
                    this.numOfDailyDos = rs.getDouble(column);
                }
                column = "total_dosing_days";
                if (columnName.contains(column)) {
                    this.totalDosingDays = rs.getDouble(column);
                }
                column = "RxNORM_code";
                if (columnName.contains(column)) {
                    this.rx.code = rs.getString(column);
                }
                column = "name";
                if (columnName.contains(column)) {
                    this.rx.description = rs.getString(column);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public RxNORM getRx() {
        return rx;
    }

    public void setRx(RxNORM rx) {
        this.rx = rx;
    }

    public double getSingleDose() {
        return singleDose;
    }

    public void setSingleDose(double singleDose) {
        this.singleDose = singleDose;
    }

    public double getNumOfDailyDos() {
        return numOfDailyDos;
    }

    public void setNumOfDailyDos(double numOfDailyDos) {
        this.numOfDailyDos = numOfDailyDos;
    }

    public double getTotalDosingDays() {
        return totalDosingDays;
    }

    public void setTotalDosingDays(double totalDosingDays) {
        this.totalDosingDays = totalDosingDays;
    }
    
    
    
    
}
