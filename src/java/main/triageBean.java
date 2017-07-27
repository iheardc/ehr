/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.ArrayList;
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
@Named(value = "triageBean")
@SessionScoped
public class triageBean implements Serializable {
    
    
    private Patient selectedP;
    
    private List<DynamicInfo> findWNFList;
    private List<DynamicInfo> findWNFIList;
    private DynamicInfo selectedD;
    String patientStatus;

    // View result table when click Search
    boolean isShowWFNTable;
    boolean isShowWFNITable;
    // View more information when patient Check-In
    boolean isShowMoreInfo;
    
    
    // Find Doctor
    String findDocName, findDocSpecialty;
    List<Employee> findDocList;
    Employee selectedDoc;
    
    
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

    public void selectDynaPatient() {
        isShowMoreInfo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }
    

    public void findDoctor() {

        DbDAO dao = new DbDAO();
        findDocList = dao.findDoctor(findDocName, findDocSpecialty);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findDocList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findDocList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void selectDoctor() {
        
        
    }
    
    public String assignDoctor(){
        
        
        return menuBean.triage();
    }

    public void reset() {
        resetFindItem();
//        findList = null;
//        selectedP = null;
        findWNFList = null;
        findWNFIList = null;
        selectedD = null;
        isShowMoreInfo = false;
        isShowWFNTable = false;
        isShowWFNITable = false;
        findDocList = null;
        selectedDoc = null;
    }

    public void resetFindItem() {
//        findId = null;
//        findName = null;
//        findDoB = null;
        patientStatus = null;
        findDocName = null;
        findDocSpecialty = null;
    }

    public Patient getSelectedP() {
        return selectedP;
    }

    public void setSelectedP(Patient selectedP) {
        this.selectedP = selectedP;
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

    public boolean isIsShowMoreInfo() {
        return isShowMoreInfo;
    }

    public void setIsShowMoreInfo(boolean isShowMoreInfo) {
        this.isShowMoreInfo = isShowMoreInfo;
    }

    public String getFindDocName() {
        return findDocName;
    }

    public void setFindDocName(String findDocName) {
        this.findDocName = findDocName;
    }

    public String getFindDocSpecialty() {
        return findDocSpecialty;
    }

    public void setFindDocSpecialty(String findDocSpecialty) {
        this.findDocSpecialty = findDocSpecialty;
    }

    public List<Employee> getFindDocList() {
        return findDocList;
    }

    public void setFindDocList(List<Employee> findDocList) {
        this.findDocList = findDocList;
    }

    public Employee getSelectedDoc() {
        return selectedDoc;
    }

    public void setSelectedDoc(Employee selectedDoc) {
        this.selectedDoc = selectedDoc;
    }
    
    
    
    
}
