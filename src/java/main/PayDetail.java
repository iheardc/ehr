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
public class PayDetail {
    
    String id, chargeCode, discountReason;
    Double amount, discountAmount;
    
    public PayDetail(){
        
    }

    public PayDetail(String id, String chargeCode, String discountReason, Double amount, Double discountAmount) {
        this.id = id;
        this.chargeCode = chargeCode;
        this.discountReason = discountReason;
        this.amount = amount;
        this.discountAmount = discountAmount;
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
                column = "charge_code";
                if (columnName.contains(column)) {
                    this.chargeCode = rs.getString(column);
                }
                column = "discount_reason";
                if (columnName.contains(column)) {
                    this.discountReason = rs.getString(column);
                }
                column = "amount";
                if (columnName.contains(column)) {
                    this.amount = rs.getDouble(column);
                }
                column = "discount_amount";
                if (columnName.contains(column)) {
                    this.discountAmount = rs.getDouble(column);
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

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getDiscountReason() {
        return discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    
    
}
