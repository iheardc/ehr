/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.dropbox.core.DbxException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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

    // View more information when patient Check-In
    boolean isShowMoreInfo;

    // Medical History
    ArrayList<Forms> visitHistory = new ArrayList<Forms>();
    DropBox db = new DropBox();
    String flName, view, mime;

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
        selectedP = null;
        isShowMoreInfo = false;
        resetFindItem();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    public void onPatientSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        selectPatient();
    }

    public void onHistoryPatientSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        visitHistory = new ArrayList<>();
        db = new DropBox();
        db.connectDB(selectedP);
        try {
            db.viewAllDocuments(visitHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Forms f : visitHistory) {
            System.out.println(f.date + ", " + f.name + ", " + f.size + ", " + f.type);
        }

        selectPatient();
    }

    public void selectPatient() {
        isShowMoreInfo = true;
        resetFindItem();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
//        context.update(":form");
    }

    public void checkIn() {

        DbDAO dao = new DbDAO();
        dao.checkInPatient(selectedP);

        if (selectedP.errormsg.length() > 0) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", p.errormsg);
            FacesContext.getCurrentInstance().addMessage(null, message);
//            return "";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage("Check-In", "Successfully Check-In " + selectedP.getName() + ".");
            FacesContext.getCurrentInstance().addMessage(null, message);
            reset();
        }

//        return "/checkin/checkin_patient?faces-redirect=true";
        menuBean.checkinPatient();
    }

    public void assignDoctor() {

//        return menuBean.triage();
        menuBean.triage();
    }

    public void changeFindNameListener(String name) {
        this.findName = name;
    }

    public void reset() {
        resetFindItem();
        findList = null;
        selectedP = null;
        isShowMoreInfo = false;
        this.db = null;
        this.mime = null;
        this.view = null;
        this.flName = null;
    }

    public void resetFindItem() {
        findId = null;
        findName = null;
        findDoB = null;
    }

    public List<Patient> patientCompleteItem(String query) {
        query = query.toLowerCase();
        findName = query;
        return PatientService.getFilteredList(query);
//        List<Patient> filteredList = new ArrayList<>();
//        for (Patient item : allPatientNames) {
//            if (item.isSameName(query)) {
//                filteredList.add(item);
//            }
//        }
//        return filteredList;
    }

    private String dateToDoubleString(Date date) {
        if (date == null) {
            return "";
        } else {
            return date.getTime() + "";
        }
    }

    public void back(){
        menuBean.patientMedicalHistory();
    }
    public void viewFile(String fileName) {
        try {
            flName = fileName;
            view = "";
            String ext = db.getExtens(fileName);
            ext = ext.toLowerCase();
            if (fileName.toLowerCase().equals("metadata.xml")) {
                view = db.viewMetaFileContext();
                mime = "text/html";
            } else if (ext.equals(".xml") || ext.equals(".xsl")) {
                String xslName = db.getXSL(fileName);
                mime = "text/html";
                if(xslName.length() > 1){
                    view = db.transferXSLT(fileName, xslName);
                    if(view.equals("error")){
                        view = db.getFileDB(fileName).toString();
                    }
                }else{
                    view = "";
                }
            }else if(ext.equals(".txt")){
                mime = "text/plain";
                view = "";
            }else if(ext.equals(".doc") || ext.equals(".docx")){
                mime = "application/msword";
                view = "";
            }else if(ext.equals(".ppt") || ext.equals(".pptx")){
                mime = "application/vnd.ms-powerpoint";
                view = "";
            }else if(ext.equals(".pdf")){
                mime = "application/pdf";
                view = "";
            }else{
                view = "";
                mime = "image/jpg";
            }
            
            menuBean.viewMHDocument();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public ByteArrayOutputStream showContent() throws DbxException, IOException{
        System.out.println("!!!!!! Show content filename " + flName);
        ByteArrayOutputStream outArr;
        String ext = db.getExtens(flName);System.out.println("!!!!!! Show content ext " + ext);
        if (!view.equals("")){
            outArr = db.getByteArrayOutView(view);
            System.out.println("!!!!!! view =  " + view);
        }
        else{
            outArr = db.getByteArrayOutFile(flName);
            System.out.println("!!!!!! view = 0 file " + flName);
        }
        return outArr;
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
        if (findName == null) {
            return;
        }
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

    public StreamedContent getImage() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
//            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
//            return new DefaultStreamedContent();
//        } 
//        return new DefaultStreamedContent(new ByteArrayInputStream(selectedP.arr));    
        if (selectedP.arr == null || selectedP.arr.length <= 0) {
            return new DefaultStreamedContent();
        } else {
            return new DefaultStreamedContent(new ByteArrayInputStream(selectedP.arr));
        }
    }

    public ArrayList<Forms> getVisitHistory() {
        return visitHistory;
    }

    public void setVisitHistory(ArrayList<Forms> visitHistory) {
        this.visitHistory = visitHistory;
    }

    public String getFlName() {
        return flName;
    }

    public void setFlName(String flName) {
        this.flName = flName;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

}
