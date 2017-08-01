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

    // Write Visit Summary
    private Date datetime;
    private String visitSummary;
    DropBox db = new DropBox();
    hl7 hl7 = new hl7();

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

    public String submitNewRecord() {
//        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssZ");
//        Date dateobj = new Date();
//        String date = df.format(dateobj);
//        if(!tempDocEmail.equalsIgnoreCase("None")){
//            submitRefer();
//        }

        try {
            //viewAllDocuments();
            db = new DropBox();
            db.connectDB(selectedPatient);
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
            db.newRecord(selectedPatient, hl7, sBean.em, Double.toString(DbDAO.getTodayMillisecondsWithTime()));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage("Successfylly uploaded ", " successfully uploaded");
            FacesContext.getCurrentInstance().addMessage(null, message);
            //dbTempPat.finishUploadNew();
        } catch (Exception ex) {
//            Logger.getLogger(userBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        //updateAllDocuments();
//        bp = "";
//        glucose = "";
//        temperature = "";
        return menuBean.writeVisitSummary();
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
    }

    public void submitDiagnosis() {

    }

    public void submitDiagnosisWithResult() {

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

}
