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
import javax.faces.application.FacesMessage;

/**
 *
 * @author tw
 */
public class ChargeCode {
    
    String code, name, description ;
    double amount, registeredDate;
    String locationId;

    public ChargeCode() {
    }

    public ChargeCode(String code, String name, String description, double amount, double registeredDate) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.registeredDate = registeredDate;
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
                String column = "code";
                if (columnName.contains(column)) {
                    this.code = rs.getString(column);
                }
                column = "name";
                if (columnName.contains(column)) {
                    this.name = rs.getString(column);
                }
                column = "description";
                if (columnName.contains(column)) {
                    this.description = rs.getString(column);
                }
                column = "amount";
                if (columnName.contains(column)) {
                    this.amount = rs.getDouble(column);
                }
                column = "registered_date";
                if (columnName.contains(column)) {
                    this.registeredDate = rs.getDouble(column);
                }
                column = "location_id";
                if (columnName.contains(column)) {
                    this.locationId = Integer.toString(rs.getInt(column));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<FacesMessage> checkInsertValidate(){
        List<FacesMessage> msgs = new ArrayList<>();
        if(code.isEmpty()){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Code is required.");
            msgs.add(msg);
        }
        if(name.isEmpty()){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Name is required.");
            msgs.add(msg);
        }
        if(description.isEmpty()){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Description is required.");
            msgs.add(msg);
        }
        return msgs;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(double registeredDate) {
        this.registeredDate = registeredDate;
    }
    
    public String getRegisteredDateString(){
        return getDateStringWithTime((long)registeredDate);
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    
    
    
}
