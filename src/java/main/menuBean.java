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
import org.primefaces.context.RequestContext;

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

    public void reset() {
        pathCont = "/home.xhtml";
        pathLeft = "/leftMenu/employee_leftMenu.xhtml";
    }

    public static void redirectHome() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sbean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        switch (sbean.em.role) {
            case "doctor": {
                treatmentBean bean = (treatmentBean) elContext.getELResolver().getValue(elContext, null, "treatmentBean");
                bean.reset();
                bean.findDynaPatient();
                break;
            }
            case "nurse": {
                triageBean bean = (triageBean) elContext.getELResolver().getValue(elContext, null, "triageBean");
                bean.reset();
                bean.findDynaPatient();
                break;
            }
        }
        pathCont = "/home.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    public static void detailInsurance() {
        pathCont = "/insurance/insurance_detail.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    public static void viewInsurance() {
        pathCont = "/insurance/insurance_view.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    // User Information
    public static void viewPatientDemographics() {
        pathCont = "/userInfo/view_patient.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    public static void viewEmployeeDemographics() {
        pathCont = "/userInfo/view_employee.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    public static void findEmployee() {
        pathCont = "/userInfo/find_employee.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    // Patient Information
    public static void patientMedicalHistoryWithReset() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientBean bean = (patientBean) elContext.getELResolver().getValue(elContext, null, "patientBean");
        bean.reset();
        pathCont = "/patientInfo/medical_history_patient.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    public static void patientMedicalHistory() {
        pathCont = "/patientInfo/medical_history_patient.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    public static void viewMHDocument() {
        pathCont = "/patientInfo/viewFileContent.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    // Patient Check-In
    public static void newPatient() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signupBean bean = (signupBean) elContext.getELResolver().getValue(elContext, null, "signupBean");
        bean.reset();
        pathCont = "/checkin/create_patient.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    public static void checkinPatient() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientBean bean = (patientBean) elContext.getELResolver().getValue(elContext, null, "patientBean");
        bean.reset();
        pathCont = "/checkin/checkin_patient.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    // Nursing Station
    public static void patientStatus() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientStatusBean bean = (patientStatusBean) elContext.getELResolver().getValue(elContext, null, "patientStatusBean");
        bean.reset();
        pathCont = "/nursing_station/patient_status.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    public static void triage() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        triageBean bean = (triageBean) elContext.getELResolver().getValue(elContext, null, "triageBean");
        bean.reset();
        pathCont = "/nursing_station/triage.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    // Treatment
    public static void diagnosis() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        treatmentBean bean = (treatmentBean) elContext.getELResolver().getValue(elContext, null, "treatmentBean");
        bean.reset();
        pathCont = "/treatment/diagnosis.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    public static void writeVisitSummary() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        treatmentBean bean = (treatmentBean) elContext.getELResolver().getValue(elContext, null, "treatmentBean");
        bean.reset();
        pathCont = "/treatment/write_visit_summary.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }
    
    // Prescription
    public static void fulFillPrescription(){
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        prescriptionBean bean = (prescriptionBean) elContext.getELResolver().getValue(elContext, null, "prescriptionBean");
        bean.reset();
        pathCont = "/prescription/fulfill_prescription.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    // Payment
    public static void viewPatientBill() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        paymentBean bean = (paymentBean) elContext.getELResolver().getValue(elContext, null, "paymentBean");
        bean.reset();
        pathCont = "/payment/view_patient_bill.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }

    // Admin
    public static void newEmployee() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signupBean bean = (signupBean) elContext.getELResolver().getValue(elContext, null, "signupBean");
        bean.reset();
        pathCont = "/admin/create_employee.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
    }
    
    //Inventory
    public static void manageSupplyInventory() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        inventorySupplyBean bean = (inventorySupplyBean) elContext.getELResolver().getValue(elContext, null, "inventorySupplyBean");
        bean.reset();
        pathCont = "/inventory/manage_supply_inventory.xhtml";
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
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

    public boolean isRendered(String strs) {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean bean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");

        if (bean.em.role == null || "".equals(bean.em.role)) {
            return false;
        }

        String role = bean.em.role.toLowerCase();
        for (String s : strs.split("/")) {
            if (s.toLowerCase().equals(role)) {
                return true;
            }
        }

        return false;
    }

}
