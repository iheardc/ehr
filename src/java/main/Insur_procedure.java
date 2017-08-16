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
public class Insur_procedure {

    String id, insurance_id, procedure, g_drg;
    double date;

    public Insur_procedure() {

    }

    public Insur_procedure(String id, String insurance_id, String procedure, String g_drg, double date) {
        this.id = id;
        this.insurance_id = insurance_id;
        this.procedure = procedure;
        this.g_drg = g_drg;
        this.date = date;
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

    public void buildInsurprocedure(ResultSet rs) {
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
                column = "procedure";
                if (columnName.contains(column)) {
                    this.procedure = rs.getString(column);
                }
                column = "G-DRG";
                if (columnName.contains(column)) {
                    this.g_drg = rs.getString(column);
                }
                 column = "date";
                if (columnName.contains(column)) {
                    this.date = rs.getDouble(column);
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

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getG_drg() {
        return g_drg;
    }

    public void setG_drg(String g_drg) {
        this.g_drg = g_drg;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

     public String getDateString() {
        return getDateString((long) ((double) date));
    }
     
    public static String getDateString(long time) {

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(currentDate);
    }

}
