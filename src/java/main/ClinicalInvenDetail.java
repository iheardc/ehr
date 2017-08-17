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
public class ClinicalInvenDetail {
    
    String id, RxNORM_code;
    Double qty, usedQty, registeredDate, expireDate;

    public ClinicalInvenDetail() {
    }

    public ClinicalInvenDetail(String id, String RxNORM_code, Double qty, Double usedQty, Double registeredDate, Double expireDate) {
        this.id = id;
        this.RxNORM_code = RxNORM_code;
        this.qty = qty;
        this.usedQty = usedQty;
        this.registeredDate = registeredDate;
        this.expireDate = expireDate;
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
                column = "RxNORM_code";
                if (columnName.contains(column)) {
                    this.RxNORM_code = rs.getString(column);
                }
                column = "qty";
                if (columnName.contains(column)) {
                    this.qty = rs.getDouble(column);
                }
                column = "used_qty";
                if (columnName.contains(column)) {
                    this.usedQty = rs.getDouble(column);
                }
                column = "registered_date";
                if (columnName.contains(column)) {
                    this.registeredDate = rs.getDouble(column);
                }
                column = "expire_date";
                if (columnName.contains(column)) {
                    this.expireDate = rs.getDouble(column);
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

    public String getRxNORM_code() {
        return RxNORM_code;
    }

    public void setRxNORM_code(String RxNORM_code) {
        this.RxNORM_code = RxNORM_code;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getUsedQty() {
        return usedQty;
    }

    public void setUsedQty(Double usedQty) {
        this.usedQty = usedQty;
    }

    public Double getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Double registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getRegisteredDateString() {
        return getDateStringWithTime((long) ((double) registeredDate));
    }

    public Double getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Double expireDate) {
        this.expireDate = expireDate;
    }

    public String getExpireDateString() {
        return getDateString((long) ((double) expireDate));
    }
    
    
    
    
    
}
