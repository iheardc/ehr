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
    
    public menuBean(){
        
    }
    
    // Header
    public static String redirectHome(){
        return "/home.xhtml?faces-redirect=true";
    }
    
    // User Information
    public static String viewPatientDemographics(){
        return "/userInfo/view_patient.xhtml?faces-redirect=true";
    }
    public static String viewEmployeeDemographics(){
        return "/userInfo/view_employee.xhtml?faces-redirect=true";
    }
    public static String findEmployee(){
        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }
    
    // Patient Check-In
    public static String newPatient(){
        return "/checkin/create_patient?faces-redirect=true";
    }
    public static String checkinPatient(){
        return "/checkin/checkin_patient?faces-redirect=true";
    }
    
    // Nursing Station
    public static String patientStatus(){
        return "/nursing_station/patient_status.xhtml?faces-redirect=true";
    }    
    public static String triage(){
        return "/nursing_station/triage?faces-redirect=true";
    }
    
    // Admin
    public static String newEmployee(){
        return "/admin/create_employee?faces-redirect=true";
    }
}
