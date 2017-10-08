/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author tw
 */
public class Patient {

    String id, email, password, fn, ln, name, gender, phone;
    String address, city, state, zip, country;
    Double dob;
    String occupation, religion;
    String emFN, emLN, emEmail, emPhone, emRelationship, emAddress, emCity, emState, emZip;
    String posAddress, posCity, posState, posZip;
    String errormsg;
    String DBaccnt = "";
    byte[] arr;
    String locationId;

    public Patient() {

    }

    public Patient(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Patient(String id, String email, String password, String fn, String ln, String name, String gender, String phone, String address, String city, String state, String zip, String country, Double dob, String occupation, String religion, String emFN, String emLN, String emEmail, String emPhone, String emRelationship, String emAddress, String emCity, String emState, String emZip, String posAddress, String posCity, String posState, String posZip, byte[] arr, String locationId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fn = fn;
        this.ln = ln;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.dob = dob;
        this.occupation = occupation;
        this.religion = religion;
        this.emFN = emFN;
        this.emLN = emLN;
        this.emEmail = emEmail;
        this.emPhone = emPhone;
        this.emRelationship = emRelationship;
        this.emAddress = emAddress;
        this.emCity = emCity;
        this.emState = emState;
        this.emZip = emZip;
        this.posAddress = posAddress;
        this.posCity = posCity;
        this.posState = posState;
        this.posZip = posZip;
        this.arr = arr;
        this.locationId = locationId;
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

    public void buildPatient(ResultSet rs) {
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = Integer.toString(rs.getInt(column));
                }
                column = "email";
                if (columnName.contains(column)) {
                    this.email = rs.getString(column);
                }
                column = "first_name";
                if (columnName.contains(column)) {
                    this.fn = rs.getString(column);
                }
                column = "last_name";
                if (columnName.contains(column)) {
                    this.ln = rs.getString(column);
                }
                column = "name";
                if (columnName.contains(column)) {
                    this.name = rs.getString(column);
                }
                column = "gender";
                if (columnName.contains(column)) {
                    this.gender = rs.getString(column);
                }
                column = "phone";
                if (columnName.contains(column)) {
                    this.phone = rs.getString(column);
                }
                column = "pic";
                if (columnName.contains(column)) {
                    this.arr = rs.getBytes(column);
                }
                column = "date_of_birth";
                if (columnName.contains(column)) {
                    this.dob = rs.getDouble(column);
                }
                column = "occupation";
                if (columnName.contains(column)) {
                    this.occupation = rs.getString(column);
                }
                column = "religion";
                if (columnName.contains(column)) {
                    this.religion = rs.getString(column);
                }

                column = "address";
                if (columnName.contains(column)) {
                    this.address = rs.getString(column);
                }
                column = "city";
                if (columnName.contains(column)) {
                    this.city = rs.getString(column);
                }
                column = "state";
                if (columnName.contains(column)) {
                    this.state = rs.getString(column);
                }
                column = "zip";
                if (columnName.contains(column)) {
                    this.zip = rs.getString(column);
                }
                column = "country";
                if (columnName.contains(column)) {
                    this.country = rs.getString(column);
                }

                column = "emFN";
                if (columnName.contains(column)) {
                    this.emFN = rs.getString(column);
                }
                column = "emLN";
                if (columnName.contains(column)) {
                    this.emLN = rs.getString(column);
                }
                column = "emEmail";
                if (columnName.contains(column)) {
                    this.emEmail = rs.getString(column);
                }
                column = "emPhone";
                if (columnName.contains(column)) {
                    this.emPhone = rs.getString(column);
                }
                column = "emRelationship";
                if (columnName.contains(column)) {
                    this.emRelationship = rs.getString(column);
                }
                column = "emAddress";
                if (columnName.contains(column)) {
                    this.emAddress = rs.getString(column);
                }
                column = "emCity";
                if (columnName.contains(column)) {
                    this.emCity = rs.getString(column);
                }
                column = "emState";
                if (columnName.contains(column)) {
                    this.emState = rs.getString(column);
                }
                column = "emZip";
                if (columnName.contains(column)) {
                    this.emZip = rs.getString(column);
                }

                column = "postalAddress";
                if (columnName.contains(column)) {
                    this.posAddress = rs.getString(column);
                }
                column = "postalCity";
                if (columnName.contains(column)) {
                    this.posCity = rs.getString(column);
                }
                column = "postalState";
                if (columnName.contains(column)) {
                    this.posState = rs.getString(column);
                }
                column = "postalZip";
                if (columnName.contains(column)) {
                    this.posZip = rs.getString(column);
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

    public boolean isSameName(String query) {
        query = query.toLowerCase();
        try {
            return (fn.toLowerCase().startsWith(query) || ln.toLowerCase().startsWith(query));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getRealId() {
        String realId = "";

        String middle;
        if (ln == null || "".equals(ln)) {
            middle = fn.substring(0, Math.min(fn.length(), 3));
            int len = middle.length();
            for (int i = len; i < 3; i++) { // ab to abb / a to aaa
                middle += middle.substring(len - 1, len);
            }
        }else{
            middle = ln.substring(0, Math.min(ln.length(), 3));
            int len = middle.length();
            for (int i = len; i < 3; i++) { // ab to abb / a to aaa
                middle += middle.substring(len - 1, len);
            }
        }
        
        realId = String.format("%011d", Integer.parseInt(id)) + middle + getDateMonthString((long)((double)dob));

        return realId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getFn() {
        return fn;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getLn() {
        return ln;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return fn + " " + ln;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setDob(Double dob) {
        this.dob = dob;
    }

    public Double getDob() {
        return dob;
    }

    public String getDobString() {
        return getDateString((long) ((double) dob));
    }

    public static String getDateString(long time) {

        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(currentDate);

    }
    
    public static String getDateMonthString(long time){
        Date currentDate = new Date(time);
        DateFormat df = new SimpleDateFormat("MM");
        return df.format(currentDate);
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getReligion() {
        return religion;
    }

    public void setEmFN(String emFN) {
        this.emFN = emFN;
    }

    public String getEmFN() {
        return emFN;
    }

    public void setEmLN(String emLN) {
        this.emLN = emLN;
    }

    public String getEmLN() {
        return emLN;
    }
    
    public String getEmName(){
        return emFN + " " + emLN;
    }

    public void setEmEmail(String emEmail) {
        this.emEmail = emEmail;
    }

    public String getEmEmail() {
        return emEmail;
    }

    public void setEmPhone(String emPhone) {
        this.emPhone = emPhone;
    }

    public String getEmPhone() {
        return emPhone;
    }

    public void setEmRelationship(String emRelationship) {
        this.emRelationship = emRelationship;
    }

    public String getEmRelationship() {
        return emRelationship;
    }

    public void setEmAddress(String emAddress) {
        this.emAddress = emAddress;
    }

    public String getEmAddress() {
        return emAddress;
    }

    public void setEmCity(String emCity) {
        this.emCity = emCity;
    }

    public String getEmCity() {
        return emCity;
    }

    public void setEmState(String emState) {
        this.emState = emState;
    }

    public String getEmState() {
        return emState;
    }

    public void setEmZip(String emZip) {
        this.emZip = emZip;
    }

    public String getEmZip() {
        return emZip;
    }

    public void setPosAddress(String posAddress) {
        this.posAddress = posAddress;
    }

    public String getPosAddress() {
        return posAddress;
    }

    public void setPosCity(String posCity) {
        this.posCity = posCity;
    }

    public String getPosCity() {
        return posCity;
    }

    public void setPosState(String posState) {
        this.posState = posState;
    }

    public String getPosState() {
        return posState;
    }

    public void setPosZip(String posZip) {
        this.posZip = posZip;
    }

    public String getPosZip() {
        return posZip;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDBaccnt() {
        return DBaccnt;
    }

    public void setDBaccnt(String DBaccnt) {
        this.DBaccnt = DBaccnt;
    }
    
    public StreamedContent getImage() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
//            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
//            return new DefaultStreamedContent();
//        } 
//        return new DefaultStreamedContent(new ByteArrayInputStream(selectedP.arr));    
        if (arr == null || arr.length <= 0) {
            return new DefaultStreamedContent();
        } else {
            return new DefaultStreamedContent(new ByteArrayInputStream(arr));
        }
    }

}
