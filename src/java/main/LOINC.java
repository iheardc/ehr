/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

/**
 *
 * @author tw
 */
public class LOINC {
    
    String code, descriptionShort, description ;
    
    public LOINC(){
        
    }

    public LOINC(String code, String descriptionShort, String description) {
        this.code = code;
        this.descriptionShort = descriptionShort;
        this.description = description;
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
                String column = "LOINC_CODE";
                if (columnName.contains(column)) {
                    this.code = rs.getString(column);
                }
                column = "test_name";
                if (columnName.contains(column)) {
                    this.description = rs.getString(column);
                }
                column = "test_name_short";
                if (columnName.contains(column)) {
                    this.descriptionShort = rs.getString(column);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }
    
    @Override
    public String toString(){
        return code + " : " + descriptionShort;
    }
    
}
