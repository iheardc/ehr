/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mb
 */
public class InventorySupplyDetail {
    
    int detailID, supplyID;
    double qty, used_qty, registered_date;
    
    public InventorySupplyDetail() {
        detailID = 0;
        supplyID = 0;
        qty = 0.0;
        used_qty = 0.0;
        registered_date = 0.0;
    }
    
    public InventorySupplyDetail(int detailID, int supplyID, double qty, double used_qty, double registered_date) {
        this.detailID = detailID;
        this.supplyID = supplyID;
        this.qty = qty;
        this.used_qty = used_qty;
        this.registered_date = registered_date;
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
        if (columnName != null) {
            try {
                String column = "detailID";
                if (columnName.contains(column)) {
                    this.detailID = rs.getInt(column);
                }
                column = "supplyID";
                if (columnName.contains(column)) {
                    this.supplyID = rs.getInt(column);
                }
                column = "qty";
                if (columnName.contains(column)) {
                    this.qty = rs.getDouble(column);
                }
                column = "used_qty";
                if (columnName.contains(column)) {
                    this.used_qty = rs.getDouble(column);
                }
                column = "registered_date";
                if (columnName.contains(column)) {
                    this.registered_date = rs.getDouble(column);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

// Getters & Setters
    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public int getSupplyID() {
        return supplyID;
    }

    public void setSupplyID(int supplyID) {
        this.supplyID = supplyID;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getUsed_qty() {
        return used_qty;
    }

    public void setUsed_qty(double used_qty) {
        this.used_qty = used_qty;
    }

    public double getRegistered_date() {
        return registered_date;
    }

    public void setRegistered_date(double registered_date) {
        this.registered_date = registered_date;
    }
    
// Return registered_date as String
    
    public String getDateString() {
        return getDateString((long) ((double) registered_date));
    }
    
    public static String getDateString(long time) {
        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.format(currentDate);
    }
    
}
