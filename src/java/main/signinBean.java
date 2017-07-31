/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author 김정훈
 */
@Named(value = "signinBean")
@SessionScoped
public class signinBean implements Serializable {

    String id;
//    String email;
    String password;
    String msg;
    String page;
    String loginType = "patient";
    Patient p;
    Employee em;
    boolean isPatientLogin = false;
//    boolean ispatientlogin=true;
//    boolean isclinicianlogin=false;

   
    public void selectlogin(String type) {
        loginType = type;
//        if (type.equals("patient")) {
//            ispatientlogin = true;
//            isclinicianlogin = false;
//            return;
//        }
//        else{
//            ispatientlogin = false;
//            isclinicianlogin = true;
//            return;
//        }
    }
    
    public String login() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().clear();
        FacesMessage message;
        boolean loggedIn = false;
        msg = "";
        DbDAO dao = new DbDAO();
//        if ("patient".equals(loginType)) {
//                p = new Patient(email, password);
//                dao.loginPatient(p);
//                if (p.errormsg == null || p.errormsg.equals("")) {
//                    // can be invalidate
//                    msg = "Wrong password or user name";
//                    password = "";
//                    email = "";
//                    p = null;
//                    loggedIn = false;
//                    message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", msg);
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                    context.addCallbackParam("loggedIn", loggedIn);
//                    return "";
//                } else {
//                    page = "home";
//                    loggedIn = true;
//                    context.addCallbackParam("loggedIn", loggedIn);
//                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
//                    message = new FacesMessage("Welcome ", p.fn);
//                    FacesContext.getCurrentInstance().addMessage(null, message);
//                    isPatientLogin = true;
//                    return "/home.xhtml?faces-redirect=true";
//                }
//
//        } 
//        else {
            page = "homeEmployee";
            em = new Employee(id, password);
            dao.loginEmployee(em);
            if (em.errormsg == null || em.errormsg.equals("")) {
                msg = "Wrong password or user name";
                password = "";
//                email = "";
                em = null;
                loggedIn = false;
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", msg);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            } else {
                loggedIn = true;

                context.addCallbackParam("loggedIn", loggedIn);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                message = new FacesMessage("Welcome ", em.fn);
                FacesContext.getCurrentInstance().addMessage(null, message);
                isPatientLogin = false;
                return "/home.xhtml?faces-redirect=true";
            }
            
//        }
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Patient getP() {
        return p;
    }

    public void setP(Patient p) {
        this.p = p;
    }

    public Employee getEm() {
        return em;
    }

    public void setEm(Employee em) {
        this.em = em;
    }
    
    
    
//    public boolean isIspatientlogin() {
//        return ispatientlogin;
//    }
//
//    public void setIspatientlogin(boolean ispatientlogin) {
//        this.ispatientlogin = ispatientlogin;
//    }
//
//      public boolean isIsclinicianlogin() {
//        return isclinicianlogin;
//    }
//
//    public void setIsclinicianlogin(boolean isclinicianlogin) {
//        this.isclinicianlogin = isclinicianlogin;
//    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    
}
