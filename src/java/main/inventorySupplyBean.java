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

@Named(value = "inventorySupplyBean")
@SessionScoped
public class inventorySupplyBean implements Serializable {
    
    // Find Supply
    private boolean isShowSupplyTable;
    private List<InventorySupply> findSupplyList;
    private InventorySupply clickedSupply;
    private InventorySupply selectedSupply;
    private String findSupplyName;
    private String findSupplyType;
    private boolean isShowSupplySummary;
    
    public void findSupply() {
        reset();
        findAllSupply();
    }
    
    public void findAllSupply(){
        DbDAO dao = new DbDAO();
        findSupplyList = dao.getSupplyList(findSupplyName, findSupplyType);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findSupplyList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findSupplyList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        isShowSupplyTable = true;
        FacesContext.getCurrentInstance().addMessage(null, message);
        RequestContext.getCurrentInstance().update("form");
    }

    public void onAllSRowSelect(SelectEvent event) {
        selectAllSupply();
    }

    public void selectAllSupply() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    /* TDL
    public void accept() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        FacesMessage message;
        if ((new DbDAO()).acceptPrescription(selectedSupply, sBean.em)) {
            findSupply();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully ", "!");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed ", "!");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    */

    public void reset() {
        resetFindItem();
        selectedSupply = null;
    }

    public void resetFindItem() {
        findSupplyList = new ArrayList<>();
    }

    private String dateToDoubleString(Date date) {
        if (date == null) {
            return "";
        } else {
            return date.getTime() + "";
        }
    }
    
// For auto-completion of name text field on manage_supply_inventory.xhtml
    public List<InventorySupply> supplyCompleteItem(String query) {
        query = query.toLowerCase();
        findSupplyName = query;
        return new DbDAO().getSupplyNames(query);
    }
    
// For row selection on manage_supply_inventory.xhtml
    public void onSearchSupplySelectRow(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Supply Selected", ((InventorySupply) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        isShowSupplySummary = true;
        clickedSupply = new InventorySupply();
        clickedSupply = selectedSupply;
        RequestContext.getCurrentInstance().update("form");
    }

    
// Getters & Setters
    
    public boolean isIsShowSupplyTable() {
        return isShowSupplyTable;
    }

    public void setIsShowSupplyTable(boolean isShowSupplyTable) {
        this.isShowSupplyTable = isShowSupplyTable;
    }

    public List<InventorySupply> getFindSupplyList() {
        return findSupplyList;
    }

    public void setFindSupplyList(List<InventorySupply> findSupplyList) {
        this.findSupplyList = findSupplyList;
    }

    public InventorySupply getSelectedSupply() {
        return selectedSupply;
    }

    public void setSelectedSupply(InventorySupply selectedSupply) {
        this.selectedSupply = selectedSupply;
    }

    public String getFindSupplyName() {
        return findSupplyName;
    }

    public void setFindSupplyName(String findSupplyName) {
        this.findSupplyName = findSupplyName;
    }

    public String getFindSupplyType() {
        return findSupplyType;
    }

    public void setFindSupplyType(String findSupplyType) {
        this.findSupplyType = findSupplyType;
    }

    public boolean isIsShowSupplySummary() {
        return isShowSupplySummary;
    }

    public void setIsShowSupplySummary(boolean isShowSupplySummary) {
        this.isShowSupplySummary = isShowSupplySummary;
    }

    
}
