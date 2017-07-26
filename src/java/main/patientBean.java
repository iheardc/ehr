/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author tw
 */
@Named(value = "patientBean")
@SessionScoped
public class patientBean implements Serializable {

    Patient p;

    // Find Patient
    private List<Patient> findList;
    private Patient selectedP;
    String findId, findName;
    Date findDoB;
    
    // Triage
    String patientStatus;

    // View More Information When Patient Check-In
    boolean isShowMoreInfo;

    public void findPatient() {
//        System.out.println(findId + ", " + findName + ", " + findEmail + ", " + findRole + ", " + findSpecialty);
        DbDAO dao = new DbDAO();
        findList = dao.findPatient(findId, findName, dateToDoubleString(findDoB));
//        System.out.println(findList.size());
//        for (Employee em : findList) {
//            System.out.println(em.getEmail());
//        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        resetFindItem();
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

//    public void selectPatient(Patient p){
//        this.selectedP = p;
//    }
    public void selectPatient() {
        isShowMoreInfo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
//        context.update(":form");
    }

    public String checkIn() {

        DbDAO dao = new DbDAO();
        dao.checkInPatient(selectedP);

        if (selectedP.errormsg.length() > 0) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", p.errormsg);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage("Check-In", "Successfully Check-In " + selectedP.getName() + ".");
            FacesContext.getCurrentInstance().addMessage(null, message);
            reset();
        }

        return "/checkin/checkin_patient?faces-redirect=true";
    }

    public void reset() {
        resetFindItem();
        findList = null;
        selectedP = null;
        isShowMoreInfo = false;

    }

    public void resetFindItem() {
        findId = null;
        findName = null;
        findDoB = null;
        patientStatus = null;
    }

    private String dateToDoubleString(Date date) {
        if (date == null) {
            return "";
        } else {
            return date.getTime() + "";
        }
    }

    public void setP(Patient p) {
        this.p = p;
    }

    public Patient getP() {
        return p;
    }

    public String getFindId() {
        return findId;
    }

    public void setFindId(String findId) {
        this.findId = findId;
    }

    public void setFindName(String findName) {
        this.findName = findName;
    }

    public String getFindName() {
        return findName;
    }

    public void setFindDoB(Date findDoB) {
        this.findDoB = findDoB;
    }

    public Date getFindDoB() {
        return findDoB;
    }

    public void setFindList(List<Patient> findList) {
        this.findList = findList;
    }

    public List<Patient> getFindList() {
        return findList;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }

    public void setSelectedP(Patient selectedP) {
        this.selectedP = selectedP;
    }

    public Patient getSelectedP() {
        return selectedP;
    }

    public void setIsShowMoreInfo(boolean isShowMoreInfo) {
        this.isShowMoreInfo = isShowMoreInfo;
    }

    public boolean getIsShowMoreInfo() {
        return isShowMoreInfo;
    }

}
