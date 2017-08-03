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
public class HCPCS {
    
    String code, description ;
    
    public HCPCS(){
        
    }

    public HCPCS(String code, String description) {
        this.code = code;
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
    
    public void buildHCPCS(ResultSet rs) {
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "code";
                if (columnName.contains(column)) {
                    this.code = rs.getString(column);
                }
                column = "description";
                if (columnName.contains(column)) {
                    this.description = rs.getString(column);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean isContain(String query){
        return (code.toLowerCase().startsWith(query) || description.toLowerCase().startsWith(query));
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
    
}
