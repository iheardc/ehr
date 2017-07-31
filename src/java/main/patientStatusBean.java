/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author mb
 */
@Named(value = "patientStatusBean")
@SessionScoped
public class patientStatusBean implements Serializable {

    String patientStatus;
    private DynamicInfo selectedD;
    
    private List<DynamicInfo> findALLList; // All Statuses  
    private List<DynamicInfo> findWNFList; // Waiting for Nurse
    private List<DynamicInfo> findWNFIList; // Waiting for Nurse (Injection)
    private List<DynamicInfo> findWFDList; // Waiting for Doctor without Result
    private List<DynamicInfo> findWFRList; // Waiting for Result
    private List<DynamicInfo> findWFDRList; // Waiting for Doctor with Result
    private List<DynamicInfo> findWFPList; // Waiting for Prescription
    private List<DynamicInfo> findWFBList; // Waiting for Bill
    private List<DynamicInfo> findCKOList; // Check Out

    // View result table when click Search
    boolean isShowALLTable;
    boolean isShowWFNTable;
    boolean isShowWFNITable;
    boolean isShowWFDTable;
    boolean isShowWFRTable;
    boolean isShowWFDRTable;
    boolean isShowWFPTable;
    boolean isShowWFBTable;
    boolean isShowCKOTable;

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
        findWFDList = new ArrayList<DynamicInfo>();
        findWFRList = new ArrayList<DynamicInfo>();
        findWFDRList = new ArrayList<DynamicInfo>();
        findWFPList = new ArrayList<DynamicInfo>();
        findWFBList = new ArrayList<DynamicInfo>();
        findCKOList = new ArrayList<DynamicInfo>();
        findALLList = new ArrayList<DynamicInfo>();
        
        if (patientStatus.equals("ALL")){
            findALLList = dao.findPatientStatus();
        }
        else {
            for (DynamicInfo dy : dList) {
                if (DbDAO.DYNAMIN_DATA[0].equals(dy.status)) {
                    findWNFList.add(dy);
                } else if (DbDAO.DYNAMIN_DATA[1].equals(dy.status)) {
                    findWNFIList.add(dy);
                }
                else if (DbDAO.DYNAMIN_DATA[2].equals(dy.status)) {
                    findWFDList.add(dy);
                }
                else if (DbDAO.DYNAMIN_DATA[3].equals(dy.status)) {
                    findWFRList.add(dy);
                }
                else if (DbDAO.DYNAMIN_DATA[4].equals(dy.status)) {
                    findWFDRList.add(dy);
                }
                else if (DbDAO.DYNAMIN_DATA[5].equals(dy.status)) {
                    findWFPList.add(dy);
                }
                else if (DbDAO.DYNAMIN_DATA[6].equals(dy.status)) {
                    findWFBList.add(dy);
                }
                else if (DbDAO.DYNAMIN_DATA[7].equals(dy.status)) {
                    findCKOList.add(dy);
                }
            }
            
        }
        
        isShowALLTable = findALLList.size() > 0;
        isShowWFNTable = findWNFList.size() > 0;
        isShowWFNITable = findWNFIList.size() > 0;
        isShowWFDTable = findWFDList.size() > 0;
        isShowWFRTable = findWFRList.size() > 0;
        isShowWFDRTable = findWFDRList.size() > 0;
        isShowWFPTable = findWFPList.size() > 0;
        isShowWFBTable = findWFBList.size() > 0;
        isShowCKOTable = findCKOList.size() > 0;

//        resetFindItem();
        RequestContext.getCurrentInstance().update("form");
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    public void reset() {
        patientStatus = null;
        selectedD = null;
        
        findWNFList = null;
        findWNFIList = null;
        findWFDList = null;
        findWFRList = null;
        findWFDRList = null;
        findWFPList = null;
        findWFBList = null;
        findCKOList = null;
        findALLList = null;
        
        isShowWFNTable = false;
        isShowWFNITable = false;
        isShowWFDTable = false;
        isShowWFRTable = false;
        isShowWFDRTable = false;
        isShowWFPTable = false;
        isShowWFBTable = false;
        isShowCKOTable = false;
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

    public List<DynamicInfo> getFindALLList() {
        return findALLList;
    }

    public void setFindALLList(List<DynamicInfo> findALLList) {
        this.findALLList = findALLList;
    }

    public List<DynamicInfo> getFindWFDList() {
        return findWFDList;
    }

    public void setFindWFDList(List<DynamicInfo> findWFDList) {
        this.findWFDList = findWFDList;
    }

    public List<DynamicInfo> getFindWFRList() {
        return findWFRList;
    }

    public void setFindWFRList(List<DynamicInfo> findWFRList) {
        this.findWFRList = findWFRList;
    }

    public List<DynamicInfo> getFindWFDRList() {
        return findWFDRList;
    }

    public void setFindWFDRList(List<DynamicInfo> findWRDRList) {
        this.findWFDRList = findWRDRList;
    }

    public List<DynamicInfo> getFindWRPList() {
        return findWFPList;
    }

    public void setFindWRPList(List<DynamicInfo> findWRPList) {
        this.findWFPList = findWRPList;
    }

    public List<DynamicInfo> getFindWRBList() {
        return findWFBList;
    }

    public void setFindWRBList(List<DynamicInfo> findWRBList) {
        this.findWFBList = findWRBList;
    }

    public List<DynamicInfo> getFindCKOList() {
        return findCKOList;
    }

    public void setFindCKOList(List<DynamicInfo> findCKOList) {
        this.findCKOList = findCKOList;
    }

    public boolean isIsShowALLTable() {
        return isShowALLTable;
    }

    public void setIsShowALLTable(boolean isShowALLTable) {
        this.isShowALLTable = isShowALLTable;
    }

    public boolean isIsShowWFDTable() {
        return isShowWFDTable;
    }

    public void setIsShowWFDTable(boolean isShowWFDTable) {
        this.isShowWFDTable = isShowWFDTable;
    }

    public boolean isIsShowWFRTable() {
        return isShowWFRTable;
    }

    public void setIsShowWFRTable(boolean isShowWFRTable) {
        this.isShowWFRTable = isShowWFRTable;
    }

    public boolean isIsShowWFDRTable() {
        return isShowWFDRTable;
    }

    public void setIsShowWFDRTable(boolean isShowWRDRTable) {
        this.isShowWFDRTable = isShowWRDRTable;
    }

    public boolean isIsShowWRPTable() {
        return isShowWFPTable;
    }

    public void setIsShowWRPTable(boolean isShowWRPTable) {
        this.isShowWFPTable = isShowWRPTable;
    }

    public boolean isIsShowWRBTable() {
        return isShowWFBTable;
    }

    public void setIsShowWRBTable(boolean isShowWRBTable) {
        this.isShowWFBTable = isShowWRBTable;
    }

    public boolean isIsShowCKOTable() {
        return isShowCKOTable;
    }

    public void setIsShowCKOTable(boolean isShowCKOTable) {
        this.isShowCKOTable = isShowCKOTable;
    }

}