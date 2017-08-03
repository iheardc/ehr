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
public class Insur_procedure {

    String id, insurance_id, procedure, g_drg;
    double date;
    String status;

    public Insur_procedure() {

    }

    public Insur_procedure(String id, String insurance_id, String procedure, String g_drg, double date, String status) {
        this.id = id;
        this.insurance_id = insurance_id;
        this.procedure = procedure;
        this.g_drg = g_drg;
        this.date = date;
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

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getG_drg() {
        return g_drg;
    }

    public void setG_drg(String g_drg) {
        this.g_drg = g_drg;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
