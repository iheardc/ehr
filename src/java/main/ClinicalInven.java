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

/**
 *
 * @author tw
 */
public class ClinicalInven {
    
    RxNORM rx;
    String location;
    Double threshold, purchasePrice, sellPrice;
    Double currentQty, expiredDate;
    
    List<ClinicalInvenDetail> detail;

    public ClinicalInven() {
        rx = new RxNORM();
    }

    public ClinicalInven(RxNORM rx, String location, Double threshold, Double purchasePrice, Double sellPrice) {
        this();
        this.rx = rx;
        this.location = location;
        this.threshold = threshold;
        this.purchasePrice = purchasePrice;
        this.sellPrice = sellPrice;
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
                String column = "RxNORM_code";
                if (columnName.contains(column)) {
                    this.rx.code = rs.getString(column);
                }
                column = "name";
                if (columnName.contains(column)) {
                    this.rx.description = rs.getString(column);
                }
                column = "location";
                if (columnName.contains(column)) {
                    this.location = rs.getString(column);
                }
                column = "threshold";
                if (columnName.contains(column)) {
                    this.threshold = rs.getDouble(column);
                }
                column = "purchase_price";
                if (columnName.contains(column)) {
                    this.purchasePrice = rs.getDouble(column);
                }
                column = "sell_price";
                if (columnName.contains(column)) {
                    this.sellPrice = rs.getDouble(column);
                }
                column = "expired_date";
                if (columnName.contains(column)) {
                    this.expiredDate = rs.getDouble(column);
                }
                column = "current_qty";
                if (columnName.contains(column)) {
                    this.currentQty = rs.getDouble(column);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    

    public RxNORM getRx() {
        return rx;
    }

    public void setRx(RxNORM rx) {
        this.rx = rx;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public List<ClinicalInvenDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<ClinicalInvenDetail> detail) {
        this.detail = detail;
    }

    public Double getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(Double currentQty) {
        this.currentQty = currentQty;
    }

    public Double getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Double expiredDate) {
        this.expiredDate = expiredDate;
    }
    
    public double getAllQty(){
        double a = 0.0;
        for(ClinicalInvenDetail de : detail){
            a += de.qty;
        }
        return a;
    }
    
    public double getAllUsedQty(){
        double a = 0.0;
        for(ClinicalInvenDetail de : detail){
            a += de.usedQty;
        }
        return a;
    }
    
}
