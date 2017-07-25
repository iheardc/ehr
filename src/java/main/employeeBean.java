/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author tw
 */
@Named(value = "employeeBean")
@SessionScoped
public class employeeBean implements Serializable {

//    @Inject
    Employee em;

    // Find Employee
    private List<Employee> findList;
    private Employee selectedEm;
    String findType="or", findId, findName, findEmail, findRole, findSpecialty;

    public void findEmployee() {
//        System.out.println(findId + ", " + findName + ", " + findEmail + ", " + findRole + ", " + findSpecialty);
        DbDAO dao = new DbDAO();
        findList = dao.findEmployee(findType, findId, findName, findEmail, findRole, findSpecialty);
//        System.out.println(findList.size());
//        for (Employee em : findList) {
//            System.out.println(em.getEmail());
//        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if(findList.size() > 0){
            message = new FacesMessage("Search ", "There are " + findList.size() + " results.");
        }else{
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
//        resetFindItem();
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    public void resetFindItem() {
        findType = "or";
        findId = null;
        findName = null;
        findEmail = null;
        findRole = null;
        findSpecialty = null;
    }

    public void setEm(Employee em) {
        this.em = em;
    }

    public Employee getEm() {
        return em;
    }

    public void setFindList(List<Employee> findList) {
        this.findList = findList;
    }

    public List<Employee> getFindList() {
        return findList;
    }

    public void setSelectedEm(Employee selectedEm) {
        this.selectedEm = selectedEm;
    }

    public Employee getSelectedEm() {
        return selectedEm;
    }
    
    public void setFindType(String findType){
        this.findType = findType;
    }
    
    public String getFindType(){
        return findType;
    }

    public void setFindId(String findId) {
        this.findId = findId;
    }

    public String getFindId() {
        return findId;
    }

    public void setFindName(String findName) {
        this.findName = findName;
    }

    public String getFindName() {
        return findName;
    }

    public void setFindEmail(String findEmail) {
        this.findEmail = findEmail;
    }

    public String getFindEmail() {
        return findEmail;
    }

    public void setFindRole(String findRole) {
        this.findRole = findRole;
    }

    public String getFindRole() {
        return findRole;
    }

    public void setFindSpecialty(String findSpecialty) {
        this.findSpecialty = findSpecialty;
    }

    public String getFindSpecialty() {
        return findSpecialty;
    }

}
