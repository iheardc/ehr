/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author tw
 */
@Named(value = "menuBean")
@SessionScoped
public class menuBean implements Serializable {
    
    public static String pathCont = "login.xhtml";
    public static String pathLeft = "/leftMenu/employee_leftMenu.xhtml";
    
    public menuBean(){
        
    }
    
    // Header
//    public static String redirectHome(){
//        return "/home.xhtml";
//    }
    public static void redirectHome(){
        pathCont = "/home.xhtml";
    }
    
    // User Information
    public static void viewPatientDemographics(){
        pathCont = "/userInfo/view_patient.xhtml";
    }
    public static void viewEmployeeDemographics(){
        pathCont = "/userInfo/view_employee.xhtml";
    }
    public static void findEmployee(){
        pathCont = "/userInfo/find_employee.xhtml";
    }
    
    // Patient Check-In
    public static void newPatient(){
        pathCont = "/checkin/create_patient.xhtml";
    }
    public static void checkinPatient(){
        pathCont = "/checkin/checkin_patient.xhtml";
    }
    
    // Nursing Station
    public static void patientStatus(){
        pathCont = "/nursing_station/patient_status.xhtml";
    }    
    public static void triage(){
        pathCont = "/nursing_station/triage.xhtml";
    }
    
    // Treatment
    public static void diagnosis(){
        pathCont = "/treatment/diagnosis.xhtml";
    }
    public static void writeVisitSummary(){
        pathCont = "/treatment/write_visit_summary.xhtml";
    }
    
    // Admin
    public static void newEmployee(){
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
    
    
    
    
}
