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
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
@Named(value = "treatmentBean")
@SessionScoped
public class treatmentBean implements Serializable {

    String patientStatus;

    private List<DynamicInfo> findWFDList;
    private List<DynamicInfo> findWFDRList;
    private DynamicInfo selectedD;

    // View result table when click Search
    boolean isShowWFDTable;
    boolean isShowWFDRTable;

    boolean isShowWFDInfo;
    boolean isShowWFDRInfo;
    boolean isShowWriteSummary;

    ArrayList<Forms> visitHistory = new ArrayList<Forms>();

    // Find Patient
    private boolean isShowPatientTable;
    private List<Patient> findPatientList;
    private Patient selectedPatient;
    private String findPatientName;
    private Date findPatientDoB;

    // Write Prescription
    Prescription prescription;
    RxNORM presTempRx;
    int presTempSD, presTempNDD, presTempTDD;
    String presTempUsage;

    // Write Visit Summary
    private Date datetime;
    private String visitSummary;
    DropBox db = new DropBox();
    hl7 hl7 = new hl7();

    // Injection Order
    private List<HCPCS> injectionList, injectionTempList;

    public void findDynaPatient() {

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");

        DbDAO dao = new DbDAO();
        List<DynamicInfo> dList = dao.searchPatientInDynamic(patientStatus, sBean.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (dList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + dList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);

        findWFDList = new ArrayList<DynamicInfo>();
        findWFDRList = new ArrayList<DynamicInfo>();
        for (DynamicInfo dy : dList) {
            if (DbDAO.DYNAMIN_DATA[2].equals(dy.status)) {
                findWFDList.add(dy);
            } else if (DbDAO.DYNAMIN_DATA[3].equals(dy.status)) {
                findWFDRList.add(dy);
            }
        }
        isShowWFDTable = findWFDList.size() > 0;
        isShowWFDRTable = findWFDRList.size() > 0;

//        resetFindItem();
        RequestContext.getCurrentInstance().update("form");
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    public void onWFDSelectRowSelect(SelectEvent event) {
        resetMoreForm();
        selectedD.getCheifComplaints(null);
        selectedD.getVitalSigns();
        selectedD.getPreviousVisitHistory();
        FacesMessage msg = new FacesMessage("Patient Selected", ((DynamicInfo) event.getObject()).p.getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);

//        visitHistory = new ArrayList<>();
//        db = new DropBox();
//        db.connectDB(selectedD.p);
//        try {
//            db.viewAllDocuments(visitHistory);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        for (Forms f : visitHistory) {
//            System.out.println(f.date + ", " + f.name + ", " + f.size + ", " + f.type);
//        }
        selectDynaWFDPatient();
//        new Thread() {
//            @Override
//            public void run() {
//                selectedD.getCheifComplaints(treatmentBean.this);
//            }
//        }.start();
    }

    public void onWFDRSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((DynamicInfo) event.getObject()).p.getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        selectDynaWFDRPatient();
    }

    public void onSearchPatientSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        isShowWriteSummary = true;
        selectedD = new DynamicInfo();
        selectedD.p = selectedPatient;
        RequestContext.getCurrentInstance().update("form");
    }

    public void selectDynaWFDPatient() {
        isShowWFDInfo = true;
        isShowWFDRInfo = false;
//        resetMoreForm();
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.update("form");
        RequestContext.getCurrentInstance().update("form");
    }

    public void selectDynaWFDRPatient() {
        isShowWFDInfo = false;
        isShowWFDRInfo = true;
//        resetMoreForm();
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.update("form");
        RequestContext.getCurrentInstance().update("form");
    }

    public void findPatient() {
        DbDAO dao = new DbDAO();
        findPatientList = dao.findPatient("", findPatientName, dateToDoubleString(findPatientDoB));

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findPatientList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findPatientList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        isShowPatientTable = true;
        FacesContext.getCurrentInstance().addMessage(null, message);
        RequestContext.getCurrentInstance().update("form");
//        resetFindItem();
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    private String dateToDoubleString(Date date) {
        if (date == null) {
            return "";
        } else {
            return date.getTime() + "";
        }
    }

    public void submitNewRecord() {

        try {
            //viewAllDocuments();
            db = new DropBox();
            db.connectDB(selectedPatient);
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
            db.newRecord(selectedPatient, hl7, sBean.em, Double.toString(DbDAO.getTodayMillisecondsWithTime()));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage("Successfylly uploaded ", "Visit summary was successfully uploaded");
            FacesContext.getCurrentInstance().addMessage(null, message);
            //dbTempPat.finishUploadNew();
        } catch (Exception ex) {
//            Logger.getLogger(userBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        menuBean.writeVisitSummary();
    }

    public String viewFile(String file) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage("View", file);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "";
//        flName = file;
//        checkNewMSGDoc();
//        allMsgs();
//        view = "";
//        String ext = db.getExtens(file);
//        if (file.toLowerCase().equals("metadata.xml")){
//            view = db.viewMetaFileContext();
//            mime = "text/html";
//        }// || db.getExtens(file).equals(".txt"))
//        else if(ext.toLowerCase().equals(".xml") || ext.toLowerCase().equals(".xsl")){
//            String xslName = db.getXSL(file);//gets processing instruction file name
//            mime = "text/html";
//            if(xslName.length() > 1){
//                view = db.transferXSLT(file, xslName);
//                if(view.equals("error"))
//                    view = db.getFileDB(file).toString();//no corresponding XSL file                           
//            }
//            else view = "";  //no instruction         
//        }
//        else if(ext.toLowerCase().equals(".txt")){
//            mime = "text/plain";
//            view = "";
//        }
//        else if(ext.toLowerCase().equals(".doc") || ext.toLowerCase().equals(".docx")){
//            mime = "application/msword";
//            view = "";
//        }
//        else if(ext.toLowerCase().equals(".ppt") || ext.toLowerCase().equals(".pptx")){
//            mime = "application/vnd.ms-powerpoint";
//            view = "";
//        }
//        else if(ext.toLowerCase().equals(".pdf")){
//            view = ""; 
//            mime = "application/pdf";
//        }
//        else {
//            view = ""; 
//            mime = "image/jpg";
//        }
//        return "/contentDoc/viewDoc.xhtml?faces-redirect=true";  
    }

    public void reset() {
        resetPatientFindItem();
        resetMoreForm();
    }

    public void resetPatientFindItem() {
        patientStatus = null;
        findWFDList = null;
        findWFDRList = null;
        isShowWFDInfo = false;
        isShowWFDRInfo = false;
        isShowWFDTable = false;
        isShowWFDRTable = false;
        selectedD = null;
        isShowPatientTable = false;
        selectedPatient = null;
    }

    public void resetMoreForm() {
        isShowWriteSummary = false;
        findPatientName = null;
        findPatientDoB = null;
        datetime = DbDAO.getTodayDateWithTime();
        visitSummary = null;
        injectionList = new ArrayList<>();
        injectionTempList = new ArrayList<>();
        prescription = new Prescription();
        resetPrescriptionItem();
    }

    public void resetPrescriptionItem() {
        presTempRx = new RxNORM();
        presTempSD = 0;
        presTempNDD = 0;
        presTempTDD = 0;
        presTempUsage = null;
    }

    public void submitDiagnosis() {

        DbDAO dao = new DbDAO();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        Employee doc = sBean.em;
        if (prescription.detail.size() > 0) {
            boolean result = dao.insertNewPrescription(selectedD.p, doc, prescription);
            if (result) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully", "Prescription was successfully registered."));
            }else{
                return;
            }
        }

        if (visitSummary != null && !"".equals(visitSummary)) {
            String vitalStr = "";
            if (selectedD.temperature != null && !"".equals(selectedD.temperature)) {
                vitalStr += "\nTemperature : " + selectedD.temperature + "°F";
            }
            if (selectedD.bloodPressure != null && !"".equals(selectedD.bloodPressure)) {
                vitalStr += "\nBlood Pressure : " + selectedD.bloodPressure;
            }
            if (selectedD.spo2 != null && !"".equals(selectedD.spo2)) {
                vitalStr += "\nSpO2 : " + selectedD.spo2 + "%";
            }
            if (selectedD.weight != null && !"".equals(selectedD.weight)) {
                vitalStr += "\nWeight : " + selectedD.weight + "lb";
            }
            String cheifStr = "";
            for(SnomedCT s : this.selectedD.cheifComplaintList){
                cheifStr += s.toString() + "\n";
            }
            
            try {
                //viewAllDocuments();
                db = new DropBox();
                db.connectDB(selectedD.p);
                db.newVisitSummary(selectedD.p, cheifStr, visitSummary, doc, DbDAO.getTodayDateString(), vitalStr);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesMessage message = new FacesMessage("Successfylly uploaded ", "Visit summary was successfully uploaded");
                FacesContext.getCurrentInstance().addMessage(null, message);
                //dbTempPat.finishUploadNew();
            } catch (Exception ex) {
//            Logger.getLogger(userBean.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                FacesMessage message = new FacesMessage("Upload Failed", "Failed to upload visit summary");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
        }
        
        if(dao.changePatientDynamicStatus(selectedD.id, DbDAO.DYNAMIN_DATA[5]).length() > 0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed", "Please try again."));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully", "Successfully finished"));
            menuBean.diagnosis();
        }
        

    }

    public void submitDiagnosisWithResult() {

    }

    public List<RxNORM> rxnormCompleteItem(String query) {
        if (injectionList == null) {
            injectionList = new ArrayList<>();
        }
        query = query.toLowerCase();
        return RxNORMService.getFilteredList(query);
    }

    public List<HCPCS> hcpcsCompleteItem(String query) {
        if (injectionList == null) {
            injectionList = new ArrayList<>();
        }
        query = query.toLowerCase();
        return HCPCSService.getFilteredList(query);
    }

    public void onInjectionItemSelect(SelectEvent event) {
        injectionList.addAll(injectionTempList);
        injectionTempList.clear();
        RequestContext.getCurrentInstance().update("form");
    }

    public void removeInjection(int index) {
        if (index < injectionList.size()) {
            injectionList.remove(index);
        }
    }

    public void addPrescriptionDetail() {
        PrescriptionDetail temp = new PrescriptionDetail();
        temp.rx = presTempRx;
        temp.singleDose = presTempSD;
        temp.numOfDailyDos = presTempNDD;
        temp.totalDosingDays = presTempTDD;
        temp.usage = presTempUsage;
        prescription.getDetail().add(temp);
        resetPrescriptionItem();
    }

    public void removePrescriptionDetail(int index) {
        if (index < prescription.detail.size()) {
            prescription.detail.remove(index);
        }
        RequestContext.getCurrentInstance().update("form:pres_popup");
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }

    public List<DynamicInfo> getFindWFDList() {
        return findWFDList;
    }

    public void setFindWFDList(List<DynamicInfo> findWFDList) {
        this.findWFDList = findWFDList;
    }

    public List<DynamicInfo> getFindWFDRList() {
        return findWFDRList;
    }

    public void setFindWFDRList(List<DynamicInfo> findWFDRList) {
        this.findWFDRList = findWFDRList;
    }

    public DynamicInfo getSelectedD() {
        return selectedD;
    }

    public void setSelectedD(DynamicInfo selectedD) {
        this.selectedD = selectedD;
    }

    public boolean isIsShowWFDTable() {
        return isShowWFDTable;
    }

    public void setIsShowWFDTable(boolean isShowWFDTable) {
        this.isShowWFDTable = isShowWFDTable;
    }

    public boolean isIsShowWFDRTable() {
        return isShowWFDRTable;
    }

    public void setIsShowWFDRTable(boolean isShowWFDRTable) {
        this.isShowWFDRTable = isShowWFDRTable;
    }

    public boolean isIsShowWFDInfo() {
        return isShowWFDInfo;
    }

    public void setIsShowWFDInfo(boolean isShowWFDInfo) {
        this.isShowWFDInfo = isShowWFDInfo;
    }

    public boolean isIsShowWFDRInfo() {
        return isShowWFDRInfo;
    }

    public void setIsShowWFDRInfo(boolean isShowWFDRInfo) {
        this.isShowWFDRInfo = isShowWFDRInfo;
    }

    public boolean getIsShowMoreInfo() {
        return isShowWFDInfo || isShowWFDRInfo;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getVisitSummary() {
        return visitSummary;
    }

    public void setVisitSummary(String visitSummary) {
        this.visitSummary = visitSummary;
    }

    public String getFindPatientName() {
        return findPatientName;
    }

    public void setFindPatientName(String findPatientName) {
        this.findPatientName = findPatientName;
    }

    public Date getFindPatientDoB() {
        return findPatientDoB;
    }

    public void setFindPatientDoB(Date findPatientDoB) {
        this.findPatientDoB = findPatientDoB;
    }

    public boolean isIsShowPatientTable() {
        return isShowPatientTable;
    }

    public void setIsShowPatientTable(boolean isShowPatientTable) {
        this.isShowPatientTable = isShowPatientTable;
    }

    public List<Patient> getFindPatientList() {
        return findPatientList;
    }

    public void setFindPatientList(List<Patient> findPatientList) {
        this.findPatientList = findPatientList;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public boolean isIsShowWriteSummary() {
        return isShowWriteSummary;
    }

    public void setIsShowWriteSummary(boolean isShowWriteSummary) {
        this.isShowWriteSummary = isShowWriteSummary;
    }

    public ArrayList<Forms> getVisitHistory() {
        return visitHistory;
    }

    public void setVisitHistory(ArrayList<Forms> visitHistory) {
        this.visitHistory = visitHistory;
    }

    public DropBox getDb() {
        return db;
    }

    public void setDb(DropBox db) {
        this.db = db;
    }

    public hl7 getHl7() {
        return hl7;
    }

    public void setHl7(hl7 hl7) {
        this.hl7 = hl7;
    }

    public List<HCPCS> getInjectionList() {
        return injectionList;
    }

    public void setInjectionList(List<HCPCS> injectionList) {
        this.injectionList = injectionList;
    }

    public List<HCPCS> getInjectionTempList() {
        return injectionTempList;
    }

    public void setInjectionTempList(List<HCPCS> injectionTempList) {
        this.injectionTempList = injectionTempList;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public RxNORM getPresTempRx() {
        return presTempRx;
    }

    public void setPresTempRx(RxNORM presTempRx) {
        this.presTempRx = presTempRx;
    }

    public int getPresTempSD() {
        return presTempSD;
    }

    public void setPresTempSD(int presTempSD) {
        this.presTempSD = presTempSD;
    }

    public int getPresTempNDD() {
        return presTempNDD;
    }

    public void setPresTempNDD(int presTempNDD) {
        this.presTempNDD = presTempNDD;
    }

    public int getPresTempTDD() {
        return presTempTDD;
    }

    public void setPresTempTDD(int presTempTDD) {
        this.presTempTDD = presTempTDD;
    }

    public String getPresTempUsage() {
        return presTempUsage;
    }

    public void setPresTempUsage(String presTempUsage) {
        this.presTempUsage = presTempUsage;
    }

}
