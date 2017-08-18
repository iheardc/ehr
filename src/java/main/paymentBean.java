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
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author tw
 */
@Named(value = "paymentBean")
@SessionScoped
public class paymentBean extends patientBean implements Serializable {

//    Date date;
    private List<DynamicInfo> findDynaList;
    private DynamicInfo selectedD;

    // View result table when click Search
    boolean isShowDynaTable;

    List<PayInfo> payList;
    PayInfo selectedPay;

    // PAY
    String payMethod, payMethodOther;
    double payAmountDue;

    @Override
    public void onPatientSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        DbDAO dao = new DbDAO();
        payList = dao.getChargesDueList(selectedP);

        selectPatient();
    }

    @Override
    public void selectPatient() {

        isShowMoreInfo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");

    }

    public void onPaymentSelectRowSelect(SelectEvent event) {

        resetPayForm();
        selectedPay.getPaymentDetailInfo();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");

    }

    public void payAmountDueChangeListener() {
        if(this.payAmountDue > this.selectedPay.balance){
            this.payAmountDue = this.selectedPay.balance;
        }else if(this.payAmountDue < 0.0){
            this.payAmountDue = 0.0;
        }
    }
    
    public void pay(){
        
        if(payAmountDue <= 0.0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error ", "Pay amount due is required."));
        }else{
            
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
            DbDAO dao = new DbDAO();
            if(dao.payAmountDue(selectedPay, sBean.em, payMethod, payAmountDue)){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully", "TODO"));
                reset();
                RequestContext.getCurrentInstance().update("form");
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "TODO"));
            }
        }
        
    }

    public void reset() {
        super.reset();
        resetPatientFindItem();
        resetMoreForm();
    }

    public void resetPatientFindItem() {
//        date = DbDAO.getTodayDate();
        findDynaList = null;
        isShowDynaTable = false;
        selectedD = null;
        payList = null;
    }

    public void resetMoreForm() {
        isShowMoreInfo = false;
        resetPayForm();
    }
    
    public void resetPayForm(){
        payMethod = "Cash";
        payMethodOther = null;
        payAmountDue = 0.0;
    }

    public DynamicInfo getSelectedD() {
        return selectedD;
    }

    public void setSelectedD(DynamicInfo selectedD) {
        this.selectedD = selectedD;
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
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

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayMethodOther() {
        return payMethodOther;
    }

    public void setPayMethodOther(String payMethodOther) {
        this.payMethodOther = payMethodOther;
    }

    public double getPayAmountDue() {
        return payAmountDue;
    }

    public void setPayAmountDue(double payAmountDue) {
        this.payAmountDue = payAmountDue;
    }

}
