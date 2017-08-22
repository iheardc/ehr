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
 * @author 김정훈
 */
public class Insur_diagnosis {

    String id, icd_10, insurance_id, g_drg, diagnosis;

    public Insur_diagnosis() {

    }

    public Insur_diagnosis(String id, String icd_10, String insurance_id, String g_drg, String diagnosis) {
        this.id = id;
        this.icd_10 = icd_10;
        this.insurance_id = insurance_id;
        this.g_drg = g_drg;
        this.diagnosis = diagnosis;
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
    
    public void buildInsurdiagnosis(ResultSet rs) {
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = Integer.toString(rs.getInt(column));
                }
                column = "insurance_ID";
                if (columnName.contains(column)) {
                    this.insurance_id = Integer.toString(rs.getInt(column));
                }
                column = "diagnosis";
                if (columnName.contains(column)) {
                    this.diagnosis = rs.getString(column);
                }
                column = "G_DRG";
                if (columnName.contains(column)) {
                    this.g_drg = rs.getString(column);
                }
                 column = "ICD_10";
                if (columnName.contains(column)) {
                    this.icd_10 = rs.getString(column);
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

    public String getIcd_10() {
        return icd_10;
    }

    public void setIcd_10(String icd_10) {
        this.icd_10 = icd_10;
    }

    public String getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(String insurance_id) {
        this.insurance_id = insurance_id;
    }

    public String getG_drg() {
        return g_drg;
    }

    public void setG_drg(String g_drg) {
        this.g_drg = g_drg;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

}
