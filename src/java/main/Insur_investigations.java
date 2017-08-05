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
public class Insur_investigations {

    String id, insurance_id, inveestigations;
    double date;
    String g_drg;
    double cost;
    String status;

    public Insur_investigations() {

    }

    public Insur_investigations(String id, String insurance_id, String inveestigations, double date, String g_drg, double cost, String status) {
        this.id = id;
        this.insurance_id = insurance_id;
        this.inveestigations = inveestigations;
        this.date = date;
        this.g_drg = g_drg;
        this.cost = cost;
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

    public String getInveestigations() {
        return inveestigations;
    }

    public void setInveestigations(String inveestigations) {
        this.inveestigations = inveestigations;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public String getG_drg() {
        return g_drg;
    }

    public void setG_drg(String g_drg) {
        this.g_drg = g_drg;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
