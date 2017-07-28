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
public class Employee {

    String id, email, password, fn, ln, name, gender, phone, pic;
    String role, license, location;
    String address, city, state, zip, country;
    String accessToken;
    int authority;
    List<String> specialtyList;
    String specialty;
    String errormsg;

    public Employee() {

    }

    public Employee(String id, String password) {
        this.id = id;
        this.password = password;
    }
//    public Employee(String id, String email, String password, String fn, String ln, String name, String gender, String phone, String pic,
//            String role, String license, String location,
//            String address, String city, String state, String zip, String country,
//            String accessToken, int authority, String specialty){
//        
//        this(email, password);
//        this.id = id;
//        this.fn = fn;
//        this.ln = ln;
//        this.name = name;
//        this.gender = gender;
//        this.phone = phone;
//        this.pic = pic;
//        this.role = role;
//        this.license = license;
//        this.location = location;
//        this.address = address;
//        this.city = city;
//        this.state = state;
//        this.zip = zip;
//        this.country = country;
//        this.accessToken = accessToken;
//        this.authority = authority;
//        this.setSpecialty(specialty);
//    }

    public Employee(String id, String email, String password, String fn, String ln, String name, String gender, String phone, String pic,
            String role, String license, String location,
            String address, String city, String state, String zip, String country,
            String accessToken, int authority, List<String> specialtyList) {

        this(id, password);
        this.email = email;
        this.fn = fn;
        this.ln = ln;
//        this.name = name;
        this.name = fn + " " + ln;
        this.gender = gender;
        this.phone = phone;
        this.pic = pic;
        this.role = role;
        this.license = license;
        this.location = location;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.accessToken = accessToken;
        this.authority = authority;
        this.specialtyList = specialtyList;
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
                    this.pic = rs.getString(column);
                }
                column = "authority";
                if (columnName.contains(column)) {
                    this.authority = rs.getInt(column);
                }
                column = "access_token";
                if (columnName.contains(column)) {
                    this.accessToken = rs.getString(column);
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
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

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
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

}
