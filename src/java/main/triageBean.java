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
    
    String patientStatus;
    
    private List<DynamicInfo> findWNFList;
    private List<DynamicInfo> findWNFIList;
    private DynamicInfo selectedD;

    // View result table when click Search
    boolean isShowWFNTable;
    boolean isShowWFNITable;
    
    boolean isShowAssignInfo;
    boolean isShowInjectionInfo;
    
    // Enter Chief Complaint
    // The items currently available for selection
    private List<SynomedCT> scodeItems = new ArrayList<SynomedCT>();
    // Current selected specialty
    private List<SynomedCT> findScodeList;
    // All the items available in the application
    private List<SynomedCT> scodeAllItems = new ArrayList<SynomedCT>();
    
    
    // Find Doctor
    String findDocName, findDocSpecialty;
    List<Employee> findDocList;
    Employee selectedDoc;
    
    Double temperature, weight, spo2;
    String bloodPressure;
    
    
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
        
//        resetFindItem();
        RequestContext.getCurrentInstance().update("form");
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    public void selectDynaAssignPatient() {
        isShowAssignInfo = true;
        isShowInjectionInfo = false;
        resetMoreForm();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void selectDynaInjectionPatient() {
        isShowAssignInfo = false;
        isShowInjectionInfo = true;
        resetMoreForm();
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
        return "";
    }
    
    public String finishInjection(){
        return "";
    }

    public void reset() {
        resetPatientFindItem();
        resetMoreForm();
    }

    public void resetPatientFindItem() {
        patientStatus = null;
        findWNFList = null;
        findWNFIList = null;
        isShowAssignInfo = false;
        isShowInjectionInfo = false;
        isShowWFNTable = false;
        isShowWFNITable = false;
        selectedD = null;
    }
    
    public void resetMoreForm(){
        findDocName = null;
        findDocSpecialty = null;
        findDocList = null;
        selectedDoc = null;
        temperature = null;
        weight = null;
        spo2 = null;
        bloodPressure = null;
        setAllScode();
    }
    
    private void setAllScode(){
        
        scodeItems = new ArrayList<SynomedCT>();
        findScodeList = new ArrayList<SynomedCT>();
        scodeAllItems = new ArrayList<SynomedCT>();
        
        scodeAllItems = SynomedService.getAllList();
        scodeItems.addAll(scodeAllItems);
    }

    public List<SynomedCT> scodeCompleteItem(String query) {
        if(findScodeList == null){
            findScodeList = new ArrayList<>();
        }
        query = query.toLowerCase();
        List<SynomedCT> filteredList = new ArrayList<SynomedCT>();
        for (SynomedCT item : scodeAllItems) {
            if (item.isContain(query) && !findScodeList.contains(item)) {
                filteredList.add(item);
            }
        }
        return filteredList;
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

    public boolean isIsShowAssignInfo() {
        return isShowAssignInfo;
    }

    public void setIsShowAssignInfo(boolean isShowAssignInfo) {
        this.isShowAssignInfo = isShowAssignInfo;
    }

    public boolean isIsShowInjectionInfo() {
        return isShowInjectionInfo;
    }

    public void setIsShowInjectionInfo(boolean isShowInjectionInfo) {
        this.isShowInjectionInfo = isShowInjectionInfo;
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

    public List<SynomedCT> getScodeItems() {
        return scodeItems;
    }

    public void setScodeItems(List<SynomedCT> scodeItems) {
        this.scodeItems = scodeItems;
    }

    public List<SynomedCT> getFindScodeList() {
        return findScodeList;
    }

    public void setFindScodeList(List<SynomedCT> findScodeList) {
        this.findScodeList = findScodeList;
    }

    public List<SynomedCT> getScodeAllItems() {
        return scodeAllItems;
    }

    public void setScodeAllItems(List<SynomedCT> scodeAllItems) {
        this.scodeAllItems = scodeAllItems;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getSpo2() {
        return spo2;
    }

    public void setSpo2(Double spo2) {
        this.spo2 = spo2;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
    
    public boolean getIsShowMoreInfo(){
        return isShowAssignInfo || isShowInjectionInfo;
    }
    
    
    
    
}
