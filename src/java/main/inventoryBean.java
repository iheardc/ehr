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
 * @author mb
 */
@Named(value = "inventoryBean")
@SessionScoped
public class inventoryBean implements Serializable {

    // ADD CLINICAL ITEMS
    ClinicalInven cl;

    boolean isEditBasicField;
    boolean isShowBasicField;
    boolean isShowBasicFieldEdit;
    boolean isShowMore;

    double addTempQty;
    Date addTempExpDate;
    List<ClinicalInvenDetail> addTempList;
    
    // MANAGE CLINICAL ITEMS
    ClinicalInven selectedInven;
    List<ClinicalInven> clinicalItems;
    List<ClinicalInven> orderRequiredClinicalItems;
    List<ClinicalInven> closeExpDateClinicalItems;

    public void onRxNORMSelect(SelectEvent event) {
        
        addTempList = new ArrayList<>();

        DbDAO dao = new DbDAO();
        List<ClinicalInven> list = dao.getClinicalItems(cl.rx);

        isShowBasicField = list.size() <= 0;
        isShowBasicFieldEdit = !isShowBasicField;

        if (isShowBasicField) {
            RxNORM tempRx = cl.rx;
            cl = new ClinicalInven();
            cl.rx = tempRx;
            isEditBasicField = true;
        }

        if (isShowBasicFieldEdit) {
            isEditBasicField = false;
            cl = list.get(0);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");

    }
    public void onRxNORMSelectInManage(SelectEvent event) {

        searchAllManageInfo();

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");

    }

    public void createClinicalInventory() {

        FacesMessage message;
        DbDAO dao = new DbDAO();
        if (isShowBasicFieldEdit) {
            if (dao.updateClinicalBasicInfo(cl)) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully ", "TODO");
                isShowMore = true;
                isEditBasicField = false;
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed ", "TODO");
            }
        } else {
            if (dao.insertNewClinical(cl)) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully ", "TODO");
                isShowMore = true;
                isEditBasicField = false;
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed ", "TODO");
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, message);

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");

    }

    public void saveDetailInventory() {

        FacesMessage message;
        DbDAO dao = new DbDAO();
        if (dao.addClinicalItem(cl, addTempList)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully ", "TODO");
            FacesContext.getCurrentInstance().addMessage(null, message);
            reset();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form");
            RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed ", "TODO");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void editableBasciField() {
        isEditBasicField = true;
    }

//    public void onRxNORMSelect(SelectEvent event) {
//        cl.rx = tempRx;
//        RequestContext.getCurrentInstance().update("form");
//    }
    public void addAddedList() {
        if (addTempQty <= 0.0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Quantity is required."));
        }
        if (addTempExpDate == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Expiration date is required."));
        }
        if (addTempQty > 0.0 && addTempExpDate != null) {
            ClinicalInvenDetail cid = new ClinicalInvenDetail();
            cid.qty = this.addTempQty;
            cid.expireDate = DbDAO.dateToDouble(addTempExpDate);
            addTempList.add(cid);
            addTempQty = 0.0;
            addTempExpDate = null;
        }
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.update("form");
    }

    public void removeAddedList(int index) {
        if (addTempList.size() > index) {
            addTempList.remove(index);
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form");
        }
    }
    
    
    /*========= Manage Clinical Item ==========*/
    public void searchAllManageInfo(){
        searchClinicalItems();
        searchOrderRequiredItems();
        searchCloseExpDateItems();
    }
    public void searchClinicalItems(){
        DbDAO dao = new DbDAO();
        this.clinicalItems = dao.getClinicalItems(cl.rx);
    }
    public void searchOrderRequiredItems(){
        DbDAO dao = new DbDAO();
        this.orderRequiredClinicalItems = dao.getOrderRequiredClinicalItems(null);
    }
    public void searchCloseExpDateItems(){
        DbDAO dao = new DbDAO();
        this.closeExpDateClinicalItems = dao.getCloseExpiredClinicalItems(null);
    }

    public void reset() {
        cl = new ClinicalInven();
        isEditBasicField = false;
        isShowBasicField = false;
        isShowBasicFieldEdit = false;
        isShowMore = false;
        addTempQty = 0.0;
        addTempExpDate = null;
        addTempList = new ArrayList<>();
        resetManage();
    }
    
    public void resetManage(){
        this.selectedInven = null;
        this.clinicalItems = null;
        this.orderRequiredClinicalItems = null;
        this.closeExpDateClinicalItems = null;
    }

    public List<RxNORM> rxnormCompleteItem(String query) {
        query = query.toLowerCase();
        return RxNORMService.getFilteredList(query);
    }

    public ClinicalInven getCl() {
        return cl;
    }

    public void setCl(ClinicalInven cl) {
        this.cl = cl;
    }

    public boolean isIsEditBasicField() {
        return isEditBasicField;
    }

    public void setIsEditBasicField(boolean isEditBasicField) {
        this.isEditBasicField = isEditBasicField;
    }

    public boolean isIsShowBasicField() {
        return isShowBasicField;
    }

    public void setIsShowBasicField(boolean isShowBasicField) {
        this.isShowBasicField = isShowBasicField;
    }

    public boolean isIsShowBasicFieldEdit() {
        return isShowBasicFieldEdit;
    }

    public void setIsShowBasicFieldEdit(boolean isShowBasicFieldEdit) {
        this.isShowBasicFieldEdit = isShowBasicFieldEdit;
    }

    public boolean isIsShowMore() {
        return isShowMore;
    }

    public void setIsShowMore(boolean isShowMore) {
        this.isShowMore = isShowMore;
    }

    public double getAddTempQty() {
        return addTempQty;
    }

    public void setAddTempQty(double addTempQty) {
        this.addTempQty = addTempQty;
    }

    public Date getAddTempExpDate() {
        return addTempExpDate;
    }

    public void setAddTempExpDate(Date addTempExpDate) {
        this.addTempExpDate = addTempExpDate;
    }

    public List<ClinicalInvenDetail> getAddTempList() {
        return addTempList;
    }

    public void setAddTempList(List<ClinicalInvenDetail> addTempList) {
        this.addTempList = addTempList;
    }

    public double getAllAddedQty() {
        double a = 0.0;
        for (ClinicalInvenDetail cid : addTempList) {
            a += cid.qty;
        }
        return a;
    }

    public ClinicalInven getSelectedInven() {
        return selectedInven;
    }

    public void setSelectedInven(ClinicalInven selectedInven) {
        this.selectedInven = selectedInven;
    }

    public List<ClinicalInven> getClinicalItems() {
        return clinicalItems;
    }

    public void setClinicalItems(List<ClinicalInven> clinicalItems) {
        this.clinicalItems = clinicalItems;
    }

    public List<ClinicalInven> getOrderRequiredClinicalItems() {
        return orderRequiredClinicalItems;
    }

    public void setOrderRequiredClinicalItems(List<ClinicalInven> orderRequiredClinicalItems) {
        this.orderRequiredClinicalItems = orderRequiredClinicalItems;
    }

    public List<ClinicalInven> getCloseExpDateClinicalItems() {
        return closeExpDateClinicalItems;
    }

    public void setCloseExpDateClinicalItems(List<ClinicalInven> closeExpDateClinicalItems) {
        this.closeExpDateClinicalItems = closeExpDateClinicalItems;
    }
    
    

}
