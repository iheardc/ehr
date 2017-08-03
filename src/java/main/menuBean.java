/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.ArrayList;
import javax.el.ELContext;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author tw
 */
@Named(value = "menuBean")
@SessionScoped
public class menuBean implements Serializable {

    public static String pathCont = "/home.xhtml";
    public static String pathLeft = "/leftMenu/employee_leftMenu.xhtml";

    public menuBean() {

    }

    public void reset(){
        pathCont = "/home.xhtml";
        pathLeft = "/leftMenu/employee_leftMenu.xhtml";
    }
    
    public static void redirectHome() {
        pathCont = "/home.xhtml";
    }
    
    public void redirectHomeAjax(){
        pathCont = "/home.xhtml";
    }
    
    public String getLoginPath(){
        return "/welcome.xhtml";
    }
    
    public String redirectLoginPath(){
        return "/welcome.xhtml?faces-redirect=true";
    }

    // User Information
    public static void viewPatientDemographics() {
        pathCont = "/userInfo/view_patient.xhtml";
    }

    public static void viewEmployeeDemographics() {
        pathCont = "/userInfo/view_employee.xhtml";
    }

    public static void findEmployee() {
        pathCont = "/userInfo/find_employee.xhtml";
    }

    // Patient Check-In
    public static void newPatient() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signupBean bean = (signupBean) elContext.getELResolver().getValue(elContext, null, "signupBean");
        bean.reset();
        pathCont = "/checkin/create_patient.xhtml";
    }

    public static void checkinPatient() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientBean bean = (patientBean) elContext.getELResolver().getValue(elContext, null, "patientBean");
        bean.reset();
        pathCont = "/checkin/checkin_patient.xhtml";
    }

    // Nursing Station
    public static void patientStatus() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientStatusBean bean = (patientStatusBean) elContext.getELResolver().getValue(elContext, null, "patientStatusBean");
        bean.reset();
        pathCont = "/nursing_station/patient_status.xhtml";
    }

    public static void triage() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        triageBean bean = (triageBean) elContext.getELResolver().getValue(elContext, null, "triageBean");
        bean.reset();
        pathCont = "/nursing_station/triage.xhtml";
    }

    // Treatment
    public static void diagnosis() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        treatmentBean bean = (treatmentBean) elContext.getELResolver().getValue(elContext, null, "treatmentBean");
        bean.reset();
        pathCont = "/treatment/diagnosis.xhtml";
    }

    public static void writeVisitSummary() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        treatmentBean bean = (treatmentBean) elContext.getELResolver().getValue(elContext, null, "treatmentBean");
        bean.reset();
        pathCont = "/treatment/write_visit_summary.xhtml";
    }

    // Admin
    public static void newEmployee() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signupBean bean = (signupBean) elContext.getELResolver().getValue(elContext, null, "signupBean");
        bean.reset();
        pathCont = "/admin/create_employee.xhtml";
    }

    public String getPathCont() {
        return pathCont;
    }

    public void setPathCont(String pathCont) {
        this.pathCont = pathCont;
    }

    public String getPathLeft() {
        return pathLeft;
    }

    public void setPathLeft(String pathLeft) {
        menuBean.pathLeft = pathLeft;
    }
    
    public boolean isRendered(String strs){
        
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean bean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        
        if(bean.em.role == null || "".equals(bean.em.role)){
            return false;
        }
        
        String role = bean.em.role.toLowerCase();
        for(String s : strs.split("/")){
            if(s.toLowerCase().equals(role)){
                return true;
            }
        }
        
        return false;
    }

}
