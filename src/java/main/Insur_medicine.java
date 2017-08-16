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
public class Insur_medicine {

    String id, insurance_id;
    String charge_code, medicine_name;
    double unit_price, qt, total_amt, date;
    String dose, status;

    public Insur_medicine() {

    }

    public Insur_medicine(String id, String insurance_id, String charge_code, String medicine_name, double unit_price, double qt, double total_amt, double date, String dose, String status) {
        this.id = id;
        this.insurance_id = insurance_id;
        this.charge_code = charge_code;
        this.medicine_name = medicine_name;
        this.unit_price = unit_price;
        this.qt = qt;
        this.total_amt = total_amt;
        this.date = date;
        this.dose = dose;
        this.status = status;
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
     
     public void buildInsurmedicine(ResultSet rs) {
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = Integer.toString(rs.getInt(column));
                }
                column = "insurance_ID";
                if (columnName.contains(column)) {
                    this.insurance_id = Integer.toString(rs.getInt(column));
                }
                column = "charge_code";
                if (columnName.contains(column)) {
                    this.charge_code = rs.getString(column);
                }
                column = "medicine_name";
                if (columnName.contains(column)) {
                    this.medicine_name = rs.getString(column);
                }
                column = "unit_price";
                if (columnName.contains(column)) {
                    this.unit_price = rs.getDouble(column);
                }
                column = "Qt";
                if (columnName.contains(column)) {
                    this.qt = rs.getDouble(column);
                }
                column = "Total_Amt";
                if (columnName.contains(column)) {
                    this.total_amt = rs.getDouble(column);
                }
                column = "Date";
                if (columnName.contains(column)) {
                    this.date = rs.getDouble(column);
                }
                column = "Dose";
                if (columnName.contains(column)) {
                    this.dose = rs.getString(column);
                }
                column = "status";
                if (columnName.contains(column)) {
                    this.status = rs.getString(column);
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

    public String getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(String insurance_id) {
        this.insurance_id = insurance_id;
    }

    public String getCharge_code() {
        return charge_code;
    }

    public void setCharge_code(String charge_code) {
        this.charge_code = charge_code;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getQt() {
        return qt;
    }

    public void setQt(double qt) {
        this.qt = qt;
    }

    public double getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(double total_amt) {
        this.total_amt = total_amt;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getDateString(long time) {

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(currentDate);

    }
    
    public String getDatesString(){
        return getDateString((long)((double)date));
    }
    
}
