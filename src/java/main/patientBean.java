/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.ArrayList;
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

    String email;
    String password;
    String msg;
    String page;
    Date findDoB;

    // Triage
    private List<DynamicInfo> findWNFList;
    private List<DynamicInfo> findWNFIList;
    private DynamicInfo selectedD;
    String patientStatus;

    // View result table when click Search
    boolean isShowWFNTable;
    boolean isShowWFNITable;
    // View more information when patient Check-In
    boolean isShowMoreInfo;

    public String login() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().clear();
        FacesMessage message;
        boolean loggedIn = false;

        msg = "";
        p = new Patient(email, password);
        DbDAO dao = new DbDAO();
        dao.loginPatient(p);
        if (p.errormsg == null || p.errormsg.equals("")) {
            // can be invalidate
            msg = "Wrong password or user name";
            password = "";
            email = "";
            p = null;
            loggedIn = false;
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", msg);
        } else {
            page = "home";
            loggedIn = true;
            context.addCallbackParam("loggedIn", loggedIn);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            message = new FacesMessage("Welcome ", p.fn);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "/home.xhtml?faces-redirect=true";
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loggedIn", loggedIn);
        return "";
    }

    public void findPatient() {
        DbDAO dao = new DbDAO();
        findList = dao.findPatient(findId, findName, dateToDoubleString(findDoB));

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

    public void findDynaPatient() {
        DbDAO dao = new DbDAO();
        List<DynamicInfo> dList = dao.searchPatientInDynamic(patientStatus);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (dList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + dList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        findWNFList = new ArrayList<DynamicInfo>();
        findWNFIList = new ArrayList<DynamicInfo>();
        for(DynamicInfo dy : dList){
            if(DbDAO.DYNAMIN_DATA[0].equals(dy.status)){
                findWNFList.add(dy);
            }else if(DbDAO.DYNAMIN_DATA[1].equals(dy.status)){
                findWNFIList.add(dy);
            }
        }
        isShowWFNTable = findWNFList.size() > 0;
        isShowWFNITable = findWNFIList.size() > 0;
        
        resetFindItem();
        RequestContext.getCurrentInstance().update("form");
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    public void selectPatient() {
        isShowMoreInfo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
//        context.update(":form");
    }

    public void selectDynaPatient() {
        isShowMoreInfo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
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
    
    public String assignDoctor(){
        
        
        return menuBean.triage();
    }

    public void reset() {
        resetFindItem();
        findList = null;
        selectedP = null;
        findWNFList = null;
        findWNFIList = null;
        selectedD = null;
        isShowMoreInfo = false;
        isShowWFNTable = false;
        isShowWFNITable = false;
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

    public List<DynamicInfo> getFindWNFList() {
        return findWNFList;
    }

    public void setFindWNFList(List<DynamicInfo> findWNFList) {
        this.findWNFList = findWNFList;
    }

    public List<DynamicInfo> getFindWNFIList() {
        return findWNFIList;
    }

    public void setFindWNFIList(List<DynamicInfo> findWNFIList) {
        this.findWNFIList = findWNFIList;
    }

    public DynamicInfo getSelectedD() {
        return selectedD;
    }

    public void setSelectedD(DynamicInfo selectedD) {
        this.selectedD = selectedD;
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

    public boolean isIsShowWFNTable() {
        return isShowWFNTable;
    }

    public void setIsShowWFNTable(boolean isShowWFNTable) {
        this.isShowWFNTable = isShowWFNTable;
    }

    public boolean isIsShowWFNITable() {
        return isShowWFNITable;
    }

    public void setIsShowWFNITable(boolean isShowWFNITable) {
        this.isShowWFNITable = isShowWFNITable;
    }

    public void setIsShowMoreInfo(boolean isShowMoreInfo) {
        this.isShowMoreInfo = isShowMoreInfo;
    }

    public boolean getIsShowMoreInfo() {
        return isShowMoreInfo;
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

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
