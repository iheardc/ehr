/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.el.ELContext;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import static main.menuBean.pathCont;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author 김정훈
 */
@Named(value = "insuranceBean")
@SessionScoped
public class insuranceBean implements Serializable {

    private Patient selectedP;
    private Insurance selectedIns;
    private List<Insurance> findList;
    private List<Insur_medicine> findMlist;
    boolean isShowMore = false;

    public void onPatientSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        selectPatient();
    }

    public void selectPatient() {
        isShowMore = true;

        DbDAO dao = new DbDAO();
        findList = dao.findInsuracne(selectedP.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
//        selectedP = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public int selectage() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        int current = year - DbDAO.getDateYear((long) (double) selectedP.dob);
        return current;
    }

    public String selectsex() {
        if (selectedP.gender.equals("male")) {
            return "M";
        } else {
            return "F";
        }
    }

    public void selectInsurance() {
        DbDAO dao = new DbDAO();
        findMlist = dao.findInsMedicine(selectedIns.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findMlist.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findMlist.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
//        selectedP = null;
        RequestContext context = RequestContext.getCurrentInstance();

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientBean bean = (patientBean) elContext.getELResolver().getValue(elContext, null, "patientBean");
        bean.reset();
        pathCont = "/insurance/insurance_detail.xhtml";
    }

    public String selectpatienttype() {
        if (selectedIns.patient_type.equals("Out-Patient")) {
            return "Out-Patient";
        } else {
            return "In-Patient";
        }
    }

    public String selectpatienttype2() {
        if (selectedIns.patient_type2.equals("Diagnosis")) {
            return "Diagnosis";
        } else if (selectedIns.patient_type2.equals("All-Inclusive")) {
            return "All-Inclusive";
        } else {
            return "Unbundled";
        }
    }
    

    public boolean selectpatienttype3() {
        return "Pharmacy".equals(selectedIns.patient_type3);
       
    }
    
     public String selectdetailtype(){
        if("Discharge".equals(selectedIns.detail_type)){
            return "Discharge";
        }
        else if("Died".equals(selectedIns.detail_type)){
            return "Died";
        }
        else if("Transferred Out".equals(selectedIns.detail_type)){
            return "Transferred Out";
        }
        else{
            return "Absconded/Discharged against medical advise";
        }
    }

     public String selectdetailtype2(){
         if("Chronic Follow-up".equals(selectedIns.detail_type2))
         {
             return "Chronic Follow-up";
         }
         else
         {
             return "Emgergency/Acute Episode";
         }
     }
     
     
    public Patient getSelectedP() {
        return selectedP;
    }

    public void setSelectedP(Patient selectedP) {
        this.selectedP = selectedP;
    }

    public List<Insurance> getFindList() {
        return findList;
    }

    public void setFindList(List<Insurance> findList) {
        this.findList = findList;
    }

    public boolean getIsShowMore() {
        return isShowMore;
    }

    public void setIsShowMore(boolean isShowMore) {
        this.isShowMore = isShowMore;
    }

    public Insurance getSelectedIns() {
        return selectedIns;
    }

    public void setSelectedIns(Insurance selectedIns) {
        this.selectedIns = selectedIns;
    }

    public List<Insur_medicine> getFindMlist() {
        return findMlist;
    }

    public void setFindMlist(List<Insur_medicine> findMlist) {
        this.findMlist = findMlist;
    }
    
    
}
