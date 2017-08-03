/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author 김정훈
 */
public class Insur_medicine {

    String id, insurance_id;
    String charge_code, medicine_name;
    double unit_price, qt, total_amt, date;
    String dose, status;

    public Insur_medicine() {

    }

    public Insur_medicine(String id, String insurance_id, String charge_code, String medicine_name, double unit_price, double qt, double total_amt, double date, String dose, String status) {
        this.id = id;
        this.insurance_id = insurance_id;
        this.charge_code = charge_code;
        this.medicine_name = medicine_name;
        this.unit_price = unit_price;
        this.qt = qt;
        this.total_amt = total_amt;
        this.date = date;
        this.dose = dose;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(String insurance_id) {
        this.insurance_id = insurance_id;
    }

    public String getCharge_code() {
        return charge_code;
    }

    public void setCharge_code(String charge_code) {
        this.charge_code = charge_code;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getQt() {
        return qt;
    }

    public void setQt(double qt) {
        this.qt = qt;
    }

    public double getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(double total_amt) {
        this.total_amt = total_amt;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
