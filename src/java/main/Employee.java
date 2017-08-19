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
import javax.inject.Inject;

/**
 *
 * @author tw
 */
public class Employee {
    
    @Inject
    ServiceBean service; 

    String id, loginId, email, password, fn, ln, name, gender, phone;
    String role, license, location;
    String address, city, state, zip, country;
    int authority;
    byte[] arr;
    List<String> specialtyList;
    String specialty;
    String errormsg;

    public Employee() {

    }

    public Employee(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public Employee(String id, String loginId, String email, String password, String fn, String ln, String name, String gender, String phone, String role, String license, String location, String address, String city, String state, String zip, String country, int authority, byte[] arr, List<String> specialtyList) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.fn = fn;
        this.ln = ln;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.role = role;
        this.license = license;
        this.location = location;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.authority = authority;
        this.arr = arr;
        this.specialtyList = specialtyList;
        this.specialty = specialty;
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

    public void buildEmployee(ResultSet rs) {
        ArrayList<String> columnName = getKeySet(rs);
//        Employee em = new Employee();
        if (columnName != null) {
            try {
                String column = "id";
                if (columnName.contains(column)) {
                    this.id = Integer.toString(rs.getInt(column));
                }
                column = "login_id";
                if (columnName.contains(column)) {
                    this.loginId = rs.getString(column);
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
                column = "role";
                if (columnName.contains(column)) {
                    this.role = rs.getString(column);
                }
                column = "license";
                if (columnName.contains(column)) {
                    this.license = rs.getString(column);
                }
                column = "gender";
                if (columnName.contains(column)) {
                    this.gender = rs.getString(column);
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
                column = "location ID";
                if (columnName.contains(column)) {
                    this.location = Integer.toString(rs.getInt(column));
                }
                column = "phone";
                if (columnName.contains(column)) {
                    this.phone = rs.getString(column);
                }
                column = "pic";
                if (columnName.contains(column)) {
                    this.arr = rs.getBytes(column);
                }
                column = "authority";
                if (columnName.contains(column)) {
                    this.authority = rs.getInt(column);
                }
                column = "specialty";
                if (columnName.contains(column)) {
                    this.specialtyList = new ArrayList<>();
                    this.specialty = rs.getString(column);
                    for (String s : this.specialty.split(",")) {
                        this.specialtyList.add(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean isSameName(String query){
        query = query.toLowerCase();
        try{
            return (fn.toLowerCase().startsWith(query) || ln.toLowerCase().startsWith(query));
        }catch(Exception e){
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
        
        realId = String.format("%011d", Integer.parseInt(id)) + middle + role;

        return realId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
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
//        return name;
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

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicense() {
        return license;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
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

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getAuthority() {
        return authority;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialtyList(ArrayList<String> specialtyList) {
        this.specialtyList = specialtyList;
    }

    public void setSpecialtyList(String specialtyList) {
        ArrayList<String> list = new ArrayList<>();
        list.add(specialtyList);
        this.specialtyList = list;
    }

    public List<String> getSpecialtyList() {
        return specialtyList;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public byte[] getArr() {
        return arr;
    }

    public void setArr(byte[] arr) {
        this.arr = arr;
    }
    
    

}
