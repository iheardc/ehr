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
import java.util.Random;

/**
 *
 * @author tw
 */
public class Payment {
    
    String id, billingSEQ, receivedBy, method;
    Double paidAmount, date;
    
    public Payment(){
        
    }

    public Payment(String id, String billingSEQ, String receivedBy, String method, Double paidAmount, Double date) {
        this.id = id;
        this.billingSEQ = billingSEQ;
        this.receivedBy = receivedBy;
        this.method = method;
        this.paidAmount = paidAmount;
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
    
    public void buildInfo(ResultSet rs) {
        ArrayList<String> columnName = getKeySet(rs);
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = rs.getString(column);
                }
                column = "billing_SEQ";
                if (columnName.contains(column)) {
                    this.billingSEQ = rs.getString(column);
                }
                column = "received_by";
                if (columnName.contains(column)) {
                    this.receivedBy = rs.getString(column);
                }
                column = "method";
                if (columnName.contains(column)) {
                    this.method = rs.getString(column);
                }
                column = "paid_amount";
                if (columnName.contains(column)) {
                    this.paidAmount = rs.getDouble(column);
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

    public String getBillingSEQ() {
        return billingSEQ;
    }

    public void setBillingSEQ(String billingSEQ) {
        this.billingSEQ = billingSEQ;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getDate() {
        return date;
    }

    public void setDate(Double date) {
        this.date = date;
    }

    public String getDateString() {
        return getDateString((long) ((double) date));
    }

    public static String getDateString(long time) {

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.format(currentDate);

    }

    
    
}
