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
@Named(value = "paymentBean")
@SessionScoped
public class paymentBean implements Serializable {

    Date date;

    private List<DynamicInfo> findDynaList;
    private DynamicInfo selectedD;

    // View result table when click Search
    boolean isShowDynaTable;

    boolean isShowMore;

    List<PayInfo> payList;
    PayInfo selectedPay;

    public void findDynaPatient() {
        DbDAO dao = new DbDAO();
        List<DynamicInfo> dList = dao.searchPatientInDynamicWithDate("ALL", null, DbDAO.dateToDouble(date), DbDAO.dateToDouble(date) + 86400000);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (dList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + dList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);

        findDynaList = dList;

        isShowDynaTable = true;

        selectedD = null;
        resetMoreForm();

//        resetFindItem();
        RequestContext.getCurrentInstance().update("form");
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    public void onDynaSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((DynamicInfo) event.getObject()).p.getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        DbDAO dao = new DbDAO();
        payList = dao.getPayList(selectedD.p, DbDAO.dateToDouble(date), DbDAO.dateToDouble(date) + 86400000);

        selectDynaPatient();
    }

    public void selectDynaPatient() {
//        resetMoreForm();
        isShowMore = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void submitPayment() {

        DbDAO dao = new DbDAO();
        dao.checkOutPatient(selectedD);

        if (selectedD.errormsg.length() > 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed ", selectedD.errormsg));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully ", "Successfully Check-Out"));
            menuBean.viewPatientBill();
            RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
        }

    }

    public void reset() {
        resetPatientFindItem();
        resetMoreForm();
    }

    public void resetPatientFindItem() {
        date = DbDAO.getTodayDate();
        findDynaList = null;
        isShowDynaTable = false;
        selectedD = null;
        payList = null;
    }

    public void resetMoreForm() {
        isShowMore = false;
    }

    public DynamicInfo getSelectedD() {
        return selectedD;
    }

    public void setSelectedD(DynamicInfo selectedD) {
        this.selectedD = selectedD;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<DynamicInfo> getFindDynaList() {
        return findDynaList;
    }

    public void setFindDynaList(List<DynamicInfo> findDynaList) {
        this.findDynaList = findDynaList;
    }

    public boolean isIsShowDynaTable() {
        return isShowDynaTable;
    }

    public void setIsShowDynaTable(boolean isShowDynaTable) {
        this.isShowDynaTable = isShowDynaTable;
    }

    public boolean isIsShowMore() {
        return isShowMore;
    }

    public void setIsShowMore(boolean isShowMore) {
        this.isShowMore = isShowMore;
    }

    public List<PayInfo> getPayList() {
        return payList;
    }

    public void setPayList(List<PayInfo> payList) {
        this.payList = payList;
    }

    public PayInfo getSelectedPay() {
        return selectedPay;
    }

    public void setSelectedPay(PayInfo selectedPay) {
        this.selectedPay = selectedPay;
    }

}
