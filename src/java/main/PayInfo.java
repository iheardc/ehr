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
public class PayInfo {
    
    String SEQ, billedTo, PIF;
    Double billingAmount, balance, date, PIFDate;
    
    public PayInfo(){
        
    }

    public PayInfo(String SEQ, String billedTo, String PIF, Double billingAmount, Double balance, Double date, Double PIFDate) {
        this.SEQ = SEQ;
        this.billedTo = billedTo;
        this.PIF = PIF;
        this.billingAmount = billingAmount;
        this.balance = balance;
        this.date = date;
        this.PIFDate = PIFDate;
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
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "SEQ";
                if (columnName.contains(column)) {
                    this.SEQ = rs.getString(column);
                }
                column = "billed_to";
                if (columnName.contains(column)) {
                    this.billedTo = rs.getString(column);
                }
                column = "PIF";
                if (columnName.contains(column)) {
                    this.PIF = rs.getString(column);
                }
                column = "billing_amount";
                if (columnName.contains(column)) {
                    this.billingAmount = rs.getDouble(column);
                }
                column = "balance";
                if (columnName.contains(column)) {
                    this.balance = rs.getDouble(column);
                }
                column = "date";
                if (columnName.contains(column)) {
                    this.date = rs.getDouble(column);
                }
                column = "PIF_date";
                if (columnName.contains(column)) {
                    this.PIFDate = rs.getDouble(column);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String SEQ) {
        this.SEQ = SEQ;
    }

    public String getBilledTo() {
        return billedTo;
    }

    public void setBilledTo(String billedTo) {
        this.billedTo = billedTo;
    }

    public String getPIF() {
        return PIF;
    }

    public void setPIF(String PIF) {
        this.PIF = PIF;
    }

    public Double getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(Double billingAmount) {
        this.billingAmount = billingAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getDate() {
        return date;
    }

    public void setDate(Double date) {
        this.date = date;
    }
    
    public String getDateString(){
        return getDateString((long)((double)date));
    }

    public static String getDateString(long time) {

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(currentDate);

    }

    public Double getPIFDate() {
        return PIFDate;
    }

    public void setPIFDate(Double PIFDate) {
        this.PIFDate = PIFDate;
    }
    
    public Double getTotal(){
        Random rand = new Random();
        return (double)rand.nextInt(300) + 50;
    }
    
    
}
