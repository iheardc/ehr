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
import javax.el.ELContext;
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
@Named(value = "prescriptionBean")
@SessionScoped
public class prescriptionBean implements Serializable {

    List<Prescription> allPrescription;
    List<Prescription> myPrescription;
    Prescription selectedPres;

    public void findPrescription() {
        reset();
        findAllPrescription();
        findMyPrescription();
    }

    public void findAllPrescription() {
        DbDAO dao = new DbDAO();
        allPrescription = dao.getPrescriptionList("WFP", null);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (allPrescription.size() > 0) {
            message = new FacesMessage("Search ", "There are " + allPrescription.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void findMyPrescription() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        DbDAO dao = new DbDAO();
        myPrescription = dao.getPrescriptionList("ING", sBean.em.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (myPrescription.size() > 0) {
            message = new FacesMessage("Search ", "There are " + myPrescription.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void onAllPrescriptionRowSelect(SelectEvent event) {
//        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
//        FacesContext.getCurrentInstance().addMessage(null, msg);

        selectAllPrescription();
    }

    public void onMyPrescriptionRowSelect(SelectEvent event) {
//        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
//        FacesContext.getCurrentInstance().addMessage(null, msg);

        selectMyPrescription();
    }

    public void selectAllPrescription() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void selectMyPrescription() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void accept() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        FacesMessage message;
        if ((new DbDAO()).acceptPrescription(selectedPres, sBean.em)) {
            findPrescription();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully ", "!");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed ", "!");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void complete() {

    }

    public void reset() {
        resetFindItem();
        selectedPres = null;
    }

    public void resetFindItem() {
        allPrescription = new ArrayList<>();
        myPrescription = new ArrayList<>();
    }

    private String dateToDoubleString(Date date) {
        if (date == null) {
            return "";
        } else {
            return date.getTime() + "";
        }
    }

    public List<Prescription> getAllPrescription() {
        return allPrescription;
    }

    public void setAllPrescription(List<Prescription> allPrescription) {
        this.allPrescription = allPrescription;
    }

    public List<Prescription> getMyPrescription() {
        return myPrescription;
    }

    public void setMyPrescription(List<Prescription> myPrescription) {
        this.myPrescription = myPrescription;
    }

    public Prescription getSelectedPres() {
        return selectedPres;
    }

    public void setSelectedPres(Prescription selectedPres) {
        if (selectedPres != null) {
            this.selectedPres = selectedPres;
        }
    }

}
