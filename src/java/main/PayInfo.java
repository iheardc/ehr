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
import java.util.Random;

/**
 *
 * @author tw
 */
public class PayInfo {

    String SEQ, billedTo, PIF;
    Double billingAmount, balance, date, PIFDate, total;

    List<PayDetail> detail;
    List<Payment> history;

    public PayInfo() {

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
                column = "bal";
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
                column = "total";
                if (columnName.contains(column)) {
                    this.total = rs.getDouble(column);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getPaymentDetailInfo() {

        if (this.detail == null) {
            detail = new ArrayList<>();
            new DbDAO().getPaymentDetailInfo(this);
        }

        getPaymentHistory();

    }

    public void getPaymentHistory() {

        if (this.history == null) {
            history = new ArrayList<>();
            new DbDAO().getPaymentHistory(this);
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

    public String getDateString() {
        return getDateString((long) ((double) date));
    }

    public static String getDateString(long time) {

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.format(currentDate);

    }

    public Double getPIFDate() {
        return PIFDate;
    }

    public void setPIFDate(Double PIFDate) {
        this.PIFDate = PIFDate;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotal() {
        return total;
//        Random rand = new Random();
//        return (double)rand.nextInt(300) + 50;
    }
    
    public Double getTotalAmountDue(){
        return Math.max(0, total-balance);
    }

    public List<PayDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<PayDetail> detail) {
        this.detail = detail;
    }

    public List<Payment> getHistory() {
        return history;
    }

    public void setHistory(List<Payment> history) {
        this.history = history;
    }

    public Double getAllAmount() {
        Double a = 0.0;
        if (detail != null) {
            for (PayDetail pa : detail) {
                a += pa.amount;
            }
        }
        return a;
    }

    public Double getAllDiscount() {
        Double a = 0.0;
        if (detail != null) {
            for (PayDetail pa : detail) {
                a += pa.discountAmount;
            }
        }
        return a;
    }

    public Double getAllAmountDue() {
        Double a = 0.0;
        if (detail != null) {
            for (PayDetail pa : detail) {
                a += pa.getAmountDue();
            }
        }
        return a;
    }
    
    public Double getAllHistoryAmount(){
        Double a = 0.0;
        if (detail != null) {
            for (Payment pa : history) {
                a += pa.paidAmount;
            }
        }
        return a;
    }

}
