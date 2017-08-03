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
    private List<SnomedCT> scodeItems = new ArrayList<SnomedCT>();
    // Current selected specialty
    private List<SnomedCT> findScodeList;
    // All the items available in the application
    private List<SnomedCT> scodeAllItems = new ArrayList<SnomedCT>();

    private List<String> specAllItems = new ArrayList<String>();
    
    // Find Doctor
    String findDocName, findDocSpecialty;
    List<Employee> findDocList;
    Employee selectedDoc;

    String temperature, weight, spo2, bloodPressure;

    public void findDynaPatient() {
        DbDAO dao = new DbDAO();
        List<DynamicInfo> dList = dao.searchPatientInDynamic(patientStatus, null);

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
        for (DynamicInfo dy : dList) {
            if (DbDAO.DYNAMIN_DATA[0].equals(dy.status)) {
                findWNFList.add(dy);
            } else if (DbDAO.DYNAMIN_DATA[1].equals(dy.status)) {
                findWNFIList.add(dy);
            }
        }
        isShowWFNTable = findWNFList.size() > 0;
        isShowWFNITable = findWNFIList.size() > 0;
        selectedD = null;
        resetMoreForm();

//        resetFindItem();
        RequestContext.getCurrentInstance().update("form");
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    public void onWFNSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((DynamicInfo) event.getObject()).p.getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        selectDynaAssignPatient();
    }

    public void selectDynaAssignPatient() {
        resetMoreForm();
        isShowAssignInfo = true;
        isShowInjectionInfo = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void onWFNISelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((DynamicInfo) event.getObject()).p.getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        selectDynaInjectionPatient();
    }

    public void selectDynaInjectionPatient() {
        resetMoreForm();
        isShowAssignInfo = false;
        isShowInjectionInfo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void findDoctor() {

        DbDAO dao = new DbDAO();
        findDocList = dao.findDoctor(findDocName, findDocSpecialty);

        FacesMessage message;
        if (findDocList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findDocList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void onDoctorSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Doctor Selected", ((Employee) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void assignDoctor() {
        boolean isEnterChiefComplaint;
        if (findScodeList == null) {
            isEnterChiefComplaint = false;
        } else {
            isEnterChiefComplaint = findScodeList.size() > 0;
        }
        boolean isEnterAssignDoc = selectedDoc != null;
        if (isEnterChiefComplaint && isEnterAssignDoc) {

            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
            DbDAO dao = new DbDAO();
            dao.assignDoctor(selectedD, selectedDoc, sBean.em, findScodeList, temperature, spo2, weight, bloodPressure);

            if (selectedD.errormsg.length() > 0) {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", selectedD.errormsg);
                FacesContext.getCurrentInstance().addMessage(null, message);
//                return "";
            } else { // success
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesMessage message = new FacesMessage("Successfylly assigned ", selectedD.p.getName() + " to " + selectedDoc.getName());
                FacesContext.getCurrentInstance().addMessage(null, message);
                reset();
//                return menuBean.triage();
                menuBean.triage();
            }

        } else {
            if (!isEnterChiefComplaint) {
                FacesMessage message = new FacesMessage("Error ", "Cheif Complaint");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            if (!isEnterAssignDoc) {
                FacesMessage message = new FacesMessage("Error ", "Doctor");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
//            return "";
        }
    }

    public String finishInjection() {
        return "";
    }

    public List<String> specCompleteItem(String query) {
        query = query.toLowerCase();
        List<String> filteredList = new ArrayList<String>();
        for (String item : specAllItems) {
            if (item.toLowerCase().startsWith(query)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    public List<Employee> employeeCompleteItem(String query) {
        query = query.toLowerCase();
        findDocName = query;
        return EmployeeService.getFilteredList("doctor", query);
//        List<Employee> filteredList = new ArrayList<>();
//        for (Employee item : allEmployeeNames) {
//            if (item.isSameName(query)) {
//                filteredList.add(item);
//            }
//        }
//        return filteredList;
    }

    public void reset() {
        resetPatientFindItem();
        resetMoreForm();
    }

    public void resetPatientFindItem() {
        patientStatus = null;
        findWNFList = null;
        findWNFIList = null;
        isShowWFNTable = false;
        isShowWFNITable = false;
        selectedD = null;
    }

    public void resetMoreForm() {
        isShowAssignInfo = false;
        isShowInjectionInfo = false;
        findDocName = null;
        findDocSpecialty = null;
        findDocList = null;
        selectedDoc = null;
        temperature = null;
        weight = null;
        spo2 = null;
        bloodPressure = null;
        setAllScode();
        setAllSpecialty();
    }

    private void setAllScode() {

        scodeItems = new ArrayList<SnomedCT>();
        findScodeList = new ArrayList<SnomedCT>();
        scodeAllItems = new ArrayList<SnomedCT>();

        scodeAllItems = SnomedService.getAllList();
        scodeItems.addAll(scodeAllItems);
    }

    public List<SnomedCT> scodeCompleteItem(String query) {
        if (findScodeList == null) {
            findScodeList = new ArrayList<>();
        }
        query = query.toLowerCase();
        List<SnomedCT> filteredList = new ArrayList<SnomedCT>();
        for (SnomedCT item : scodeAllItems) {
            if (item.isContain(query) && !findScodeList.contains(item)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    private void setAllSpecialty() {
        findDocSpecialty = null;
        specAllItems = new ArrayList<String>();

        specAllItems.add("Allergy and immunology");
        specAllItems.add("Adolescent medicine");
        specAllItems.add("Anaesthesiology");
        specAllItems.add("Aerospace medicine");
        specAllItems.add("Pathology");
        specAllItems.add("Cardiology");
        specAllItems.add("Cardiothoracic surgery");
        specAllItems.add("Child and adolescent psychiatry and psychotherapy");
        specAllItems.add("Clinical neurophysiology");
        specAllItems.add("Colon and Rectal Surgery");
        specAllItems.add("Dermatology-Venereology");
        specAllItems.add("Emergency medicine");
        specAllItems.add("Endocrinology");
        specAllItems.add("Gastroenterology");
        specAllItems.add("General practice");
        specAllItems.add("Geriatrics");
        specAllItems.add("Obstetrics and gynaecology");
        specAllItems.add("Health informatics");
        specAllItems.add("Hospice and palliative medicine");
        specAllItems.add("Infectious disease");
        specAllItems.add("Internal medicine");
        specAllItems.add("Interventional radiology");
        specAllItems.add("Vascular medicine");
        specAllItems.add("Microbiology");
        specAllItems.add("Nephrology");
        specAllItems.add("Neurology");
        specAllItems.add("Neurosurgery");
        specAllItems.add("Nuclear medicine");
        specAllItems.add("Occupational medicine");
        specAllItems.add("Ophthalmology");
        specAllItems.add("Orthodontics");
        specAllItems.add("Orthopaedics");
        specAllItems.add("Oral and maxillofacial surgery");
        specAllItems.add("Otorhinolaryngology");
        specAllItems.add("Paediatrics");
        specAllItems.add("Paediatric allergology");
        specAllItems.add("Paediatric cardiology");
        specAllItems.add("Paediatric endocrinology and diabetes");
        specAllItems.add("Paediatric gastroenterology, hepatology and nutrition");
        specAllItems.add("Paediatric haematology and oncology");
        specAllItems.add("Paediatric infectious diseases");
        specAllItems.add("Neonatology");
        specAllItems.add("Paediatric nephrology");
        specAllItems.add("Paediatric respiratory medicine");
        specAllItems.add("Paediatric rheumatology");
        specAllItems.add("Paediatric surgery");
        specAllItems.add("Physical medicine and rehabilitation");
        specAllItems.add("Plastic, reconstructive and aesthetic surgery");
        specAllItems.add("Pulmonology");
        specAllItems.add("Psychiatry");
        specAllItems.add("Public Health");
        specAllItems.add("Radiation Oncology");
        specAllItems.add("Radiology");
        specAllItems.add("Sports medicine");
        specAllItems.add("Neuroradiology");
        specAllItems.add("General surgery");
        specAllItems.add("Urology");
        specAllItems.add("Vascular surgery");
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
        if (findDocName == null) {
            return;
        }
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

    public List<SnomedCT> getScodeItems() {
        return scodeItems;
    }

    public void setScodeItems(List<SnomedCT> scodeItems) {
        this.scodeItems = scodeItems;
    }

    public List<SnomedCT> getFindScodeList() {
        return findScodeList;
    }

    public void setFindScodeList(List<SnomedCT> findScodeList) {
        this.findScodeList = findScodeList;
    }

    public List<SnomedCT> getScodeAllItems() {
        return scodeAllItems;
    }

    public void setScodeAllItems(List<SnomedCT> scodeAllItems) {
        this.scodeAllItems = scodeAllItems;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public boolean getIsShowMoreInfo() {
        return isShowAssignInfo || isShowInjectionInfo;
    }

    public List<String> getSpecAllItems() {
        return specAllItems;
    }

    public void setSpecAllItems(List<String> specAllItems) {
        this.specAllItems = specAllItems;
    }

}
