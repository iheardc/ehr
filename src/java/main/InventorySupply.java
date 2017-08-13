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
 * @author mb
 */
public class InventorySupply {
    
    String id, name, type;
    Double threshold;
    List<InventorySupplyDetail> supplyDetail;
    
    public InventorySupply() {
        id = "";
        name = "";
        type = "";
        threshold = 0.0;
        supplyDetail = new ArrayList<>();
    }
    
    public InventorySupply(String id, String name, String type, Double threshold) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.threshold = threshold;
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
    
    public void buildInventorySupply(ResultSet rs)  {
        ArrayList<String> columnName = getKeySet(rs);
        
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = rs.getString(column);
                }
                column = "name";
                if (columnName.contains(column)) {
                    this.name = rs.getString(column);
                }
                column = "type";
                if (columnName.contains(column)) {
                    this.type = rs.getString(column);
                }
                column = "threshold";
                if (columnName.contains(column)) {
                    this.threshold = rs.getDouble(column);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
    }

    
// Getters & Setters
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public List<InventorySupplyDetail> getSupplyDetail() {
        return supplyDetail;
    }

    public void setSupplyDetail(List<InventorySupplyDetail> supplyDetail) {
        this.supplyDetail = supplyDetail;
    }
    
}
