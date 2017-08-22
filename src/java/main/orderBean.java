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
@Named(value = "orderBean")
@SessionScoped
public class orderBean implements Serializable {

    // LAB ORDER
    List<OrderInfo> allLabOrders;
    List<OrderInfo> myLabOrders;

    // IMAGING
    List<OrderInfo> allImagingOrders;
    List<OrderInfo> myImagingOrders;

    OrderInfo selectedOrder;

    public void findLabOrder() {
        reset();
        findAllLabOrder();
        findMyLabOrder();
    }

    public void findAllLabOrder() {
        DbDAO dao = new DbDAO();
        allLabOrders = dao.getLabOrderList(signinBean.locationId, "WFT", null);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (allLabOrders.size() > 0) {
            message = new FacesMessage("Search ", "There are " + allLabOrders.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void findMyLabOrder() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        DbDAO dao = new DbDAO();
        myLabOrders = dao.getLabOrderList(signinBean.locationId, "ING", sBean.em.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (myLabOrders.size() > 0) {
            message = new FacesMessage("Search ", "There are " + myLabOrders.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void findImagingOrder() {
        reset();
        findAllImagingOrder();
        findMyImagingOrder();
    }

    public void findAllImagingOrder() {
        DbDAO dao = new DbDAO();
        allImagingOrders = dao.getImagingOrderList(signinBean.locationId, "WFT", null);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (allImagingOrders.size() > 0) {
            message = new FacesMessage("Search ", "There are " + allImagingOrders.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void findMyImagingOrder() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        DbDAO dao = new DbDAO();
        myImagingOrders = dao.getImagingOrderList(signinBean.locationId, "ING", sBean.em.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (myImagingOrders.size() > 0) {
            message = new FacesMessage("Search ", "There are " + myImagingOrders.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void onAllOrderRowSelect(SelectEvent event) {
//        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
//        FacesContext.getCurrentInstance().addMessage(null, msg);

        selectAllOrder();
    }

    public void onMyOrderRowSelect(SelectEvent event) {
//        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
//        FacesContext.getCurrentInstance().addMessage(null, msg);

        selectMyOrder();
    }

    public void selectAllOrder() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void selectMyOrder() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void acceptLab() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        FacesMessage message;
        if ((new DbDAO()).acceptLabOrder(selectedOrder, sBean.em)) {
            findLabOrder();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully ", "TODO");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed ", "TODO");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void acceptImaging() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        FacesMessage message;
        if ((new DbDAO()).acceptImagingOrder(selectedOrder, sBean.em)) {
            findImagingOrder();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully ", "TODO");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed ", "TODO");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void complete() {

//        FacesMessage message;
//        DbDAO dao = new DbDAO();
//        if (dao.completePrescription(selectedPres)) {
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully ", "TODO");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            menuBean.fulFillPrescription();
//        } else {
//            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed ", "TODO");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
    }

    public void reset() {
        resetFindItem();
        selectedOrder = null;
    }

    public void resetFindItem() {
        allLabOrders = new ArrayList<>();
        myLabOrders = new ArrayList<>();
        allImagingOrders = new ArrayList<>();
        myImagingOrders = new ArrayList<>();
    }

    private String dateToDoubleString(Date date) {
        if (date == null) {
            return "";
        } else {
            return date.getTime() + "";
        }
    }

    public List<OrderInfo> getAllLabOrders() {
        return allLabOrders;
    }

    public void setAllLabOrders(List<OrderInfo> allLabOrders) {
        this.allLabOrders = allLabOrders;
    }

    public List<OrderInfo> getMyLabOrders() {
        return myLabOrders;
    }

    public void setMyLabOrders(List<OrderInfo> myLabOrders) {
        this.myLabOrders = myLabOrders;
    }

    public List<OrderInfo> getAllImagingOrders() {
        return allImagingOrders;
    }

    public void setAllImagingOrders(List<OrderInfo> allImagingOrders) {
        this.allImagingOrders = allImagingOrders;
    }

    public List<OrderInfo> getMyImagingOrders() {
        return myImagingOrders;
    }

    public void setMyImagingOrders(List<OrderInfo> myImagingOrders) {
        this.myImagingOrders = myImagingOrders;
    }

    public OrderInfo getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(OrderInfo selectedOrder) {
        if (selectedOrder != null) {
            this.selectedOrder = selectedOrder;
        }
    }

}
