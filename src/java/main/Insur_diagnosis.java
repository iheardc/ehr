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
public class Insur_diagnosis {

    String id, idc_10, insurance_id, g_drg, diagnosis, status;

    public Insur_diagnosis() {

    }

    public Insur_diagnosis(String id, String idc_10, String insurance_id, String g_drg, String diagnosis, String status) {
        this.id = id;
        this.idc_10 = idc_10;
        this.insurance_id = insurance_id;
        this.g_drg = g_drg;
        this.diagnosis = diagnosis;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdc_10() {
        return idc_10;
    }

    public void setIdc_10(String idc_10) {
        this.idc_10 = idc_10;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
