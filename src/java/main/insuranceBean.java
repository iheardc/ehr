/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
    boolean isShowMore=false;
    
    public void onPatientSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        selectPatient();
    }

    public void selectPatient() {
        isShowMore = true;

        DbDAO dao = new DbDAO();
        findList = dao.findInsuracne(selectedP.id, selectedP.dob);

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
    
    private String dateToDoubleString(Date date) {
        if (date == null) {
            return "";
        } else {
            return date.getTime() + "";
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
    
    

    
}
