/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
//import java.util.ArrayList;
import java.util.List;
//import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
//import javax.faces.view.ViewScoped;
//import javax.inject.Inject;
import javax.inject.Named;
//import org.primefaces.context.RequestContext;

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

//    String email;
//    String password;
    String findType = "or", findId, findName, findEmail, findRole, findSpecialty;
//    String page;
//    String msg;
//    boolean inUserAccnt = false;

//    public String login() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        FacesMessage message = null;
//        boolean loggedIn = false;
//
//        page = "homeDoc";
//        msg = "";
//        em = new Employee(email, password);
//        DbDAO dao = new DbDAO();
//        dao.loginEmployee(em);
//        if (em.errormsg == null || em.errormsg.equals("")) {
//            msg = "Wrong password or user name";
//            password = "";
//            email = "";
//            em = null;
//            loggedIn = false;
//            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", msg);
////            return "/loginEmployee.xhtml?faces-redirect=true";
//        } else {
//            System.out.println("CHECK@@@");
//            System.out.println("CHECK@@@ " + em.fn);
//            System.out.println("CHECK@@@ " + em.accessToken);
//            loggedIn = true;
//
//            context.addCallbackParam("loggedIn", loggedIn);
//            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
//            message = new FacesMessage("Welcome ", em.fn);
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return "/home.xhtml?faces-redirect=true";
//        }
//        FacesContext.getCurrentInstance().addMessage(null, message);
//        context.addCallbackParam("loggedIn", loggedIn);
//        return "/home.xhtml?faces-redirect=true";
//    }


//    String email;
//    String password;
//    String findType="or", findId, findName, findEmail, findRole, findSpecialty;
//    String page;
//    String msg;
//    boolean inUserAccnt = false;
    
//    public String login(){
//        RequestContext context = RequestContext.getCurrentInstance();
//        FacesMessage message = null;
//        boolean loggedIn = false;
//        
//        page = "homeDoc";
//        msg = "";
//        em = new Employee(email, password);
//        DbDAO dao = new DbDAO();
//        dao.loginEmployee(em);
//        if (em.errormsg == null || em.errormsg.equals("")){
//            msg = "Wrong password or user name";
//            password = "";
//            email = "";
//            em = null;
//            loggedIn = false;
//            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", msg);
////            return "/loginEmployee.xhtml?faces-redirect=true";
//        }
//        else{
//            System.out.println("CHECK@@@");
//            System.out.println("CHECK@@@ " + em.fn);
//            System.out.println("CHECK@@@ " + em.accessToken);
//            loggedIn = true;
//            
//            context.addCallbackParam("loggedIn", loggedIn);
//            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
//            message = new FacesMessage("Welcome ", em.fn);
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return "/home.xhtml?faces-redirect=true";
//        }
//        FacesContext.getCurrentInstance().addMessage(null, message);
//        context.addCallbackParam("loggedIn", loggedIn);
//        return "/home.xhtml?faces-redirect=true";
//    }
    

    public void findEmployee() {
        DbDAO dao = new DbDAO();
        findList = dao.findEmployee(findType, findId, findName, findEmail, findRole, findSpecialty);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findList.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findList.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
//        resetFindItem();
//        return "/userInfo/find_employee.xhtml?faces-redirect=true";
    }

    public void resetAllItem() {
        // Find Employee
        findList = null;
        selectedEm = null;
//        email = null;
//        password = null;
//        page = null;
//        msg = null;
        resetFindItem();
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

    public void setFindType(String findType) {
        this.findType = findType;
    }

    public String getFindType() {
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


//    public boolean isInUserAccnt() {
//        return inUserAccnt;
//    }
//
//    public void setInUserAccnt(boolean inUserAccnt) {
//        this.inUserAccnt = inUserAccnt;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setPage(String page) {
//        this.page = page;
//    }
//
//    public String getPage() {
//        return page;
//    }

    
//    public boolean isInUserAccnt() {
//        return inUserAccnt;
//    }
//
//    public void setInUserAccnt(boolean inUserAccnt) {
//        this.inUserAccnt = inUserAccnt;
//    }
    
//    public void setEmail(String email){
//        this.email=email;
//    }
//
//    public String getEmail(){
//        return email;
//    }
//    
//    public void setPassword(String password){
//        this.password=password;
//    }
//    
//    public String getPassword(){
//        return password;
//    }
//    
//    public void setMsg(String msg){
//        this.msg=msg;
//    }
//    
//    public String getMsg(){
//        return msg;
//    }
//    
//    public void setPage(String page){
//        this.page=page;
//    }
//    
//    public String getPage(){
//        return page;
//    }


}
