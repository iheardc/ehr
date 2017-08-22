/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import javax.el.ELContext;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author tw
 */
@Named(value = "signupBean")
@SessionScoped
public class signupBean implements Serializable {

    @Inject
    ServiceBean service;
    String msg = "NOT registered yet";

    String id, loginId, email, password, rePassword, fn, ln, name, gender, phone, pic;
    String address, city, state, zip, country;
    String accessToken;

    // Employee Only
    String role, license, location;
    int authority;
    // The items currently available for selection
    private List<String> specItems = new ArrayList<String>();
    // Current selected specialty
    private List<String> specialty = new ArrayList<String>();
    // All the items available in the application
    private List<String> specAllItems = new ArrayList<String>();

    // Patient Only
    Double dob;
    String occupation, religion;
    String emFN, emLN, emEmail, emPhone, emRelationship, emAddress, emCity, emState, emZip;
    String posAddress, posCity, posState, posZip;
    Date dob2;

    Employee em = new Employee();
    Patient p = new Patient();
    
    boolean isUpdateMode = false;

    public signupBean() {
        setAllSpecialty();
    }

    public void registrationEmployee() throws IOException {

        DbDAO dao = new DbDAO();

        byte[] arr = service.getData();
//        em = new Employee(id, email, password, fn, ln, name, gender, phone,
//                role, license, location,
//                address, city, state, zip, country,
//                accessToken, authority, specialty);
        em = new Employee(id, loginId, email, password, fn, ln, name, gender, phone,
                role, license, location,
                address, city, state, zip, country,
                authority, arr, specialty);

        dao.insertNewEmployee(em);

        //failed
        if (em.errormsg.length()
                > 0) {
            msg = em.errormsg;
            password = "";
            rePassword = "";
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", em.errormsg);
            FacesContext.getCurrentInstance().addMessage(null, message);
//            return "";
        } else {
            reset();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage("Successfylly registered ", "Your registration has been successfully completed!");
            FacesContext.getCurrentInstance().addMessage(null, message);
//            return "/admin/create_employee.xhtml?faces-redirect=true";
            menuBean.newEmployee();
        }
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");

    }
    
    public void updateEmployee() throws IOException{
        // TODO
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfylly updated ", "Your information has been successfully updated!"));
    }

    public void registrationPatient() throws IOException {

        DbDAO dao = new DbDAO();

        byte[] arr = service.getData();
        p = new Patient(id, email, password, fn, ln, name, gender, phone, address, city, state, zip, country, dateToDouble(dob2), occupation, religion, emFN, emLN, emEmail, emPhone, emRelationship, emAddress, emCity, emState, emZip, posAddress, posCity, posState, posZip, arr);

        dao.insertNewPatient(p);

        //failed
        if (p.errormsg.length() > 0) {
            msg = p.errormsg;
            password = "";
            rePassword = "";
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", p.errormsg);
            FacesContext.getCurrentInstance().addMessage(null, message);
//            return "";
        } else { // success
            reset();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage("Successfylly registered ", "Your registration has been successfully completed!");
            FacesContext.getCurrentInstance().addMessage(null, message);
//            return "/checkin/create_patient.xhtml?faces-redirect=true";
            menuBean.newPatient();
        }
        
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");

    }

    private Double dateToDouble(Date date) {
        return Double.parseDouble(dob2.getTime() + "");
    }

    public void onDateSelect(SelectEvent event) {
        dob = Double.parseDouble(Long.toString(dob2.getTime()));
        System.out.println(Double.toString(dob));
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public void cancelPatient() {
        reset();
//        return menuBean.newPatient();
        menuBean.newPatient();
    }

    public void cancelEmployee() {
        reset();
//        return menuBean.newEmployee();
        menuBean.newEmployee();
    }

    public void reset() {
        msg = null;
        id = null;
        loginId = null;
        email = null;
        password = null;
        rePassword = null;
        fn = null;
        ln = null;
        name = null;
        gender = null;
        phone = null;
        pic = null;
        role = null;
        license = null;
        location = null;
        address = null;
        city = null;
        state = null;
        zip = null;
        country = null;
        accessToken = null;
        authority = 0;
        specialty = null;
        dob = null;
        dob2 = null;
        occupation = null;
        religion = null;
        emFN = null;
        emLN = null;
        emEmail = null;
        emPhone = null;
        emRelationship = null;
        emAddress = null;
        emCity = null;
        emState = null;
        emZip = null;
        posAddress = null;
        posCity = null;
        posState = null;
        posZip = null;
        isUpdateMode = false;

        setAllSpecialty();

        System.out.println("Reset all form.");
    }

    private void setAllSpecialty() {
        specItems = new ArrayList<String>();
        specialty = new ArrayList<String>();
        specAllItems = new ArrayList<String>();

        specAllItems.add("Allergy and immunology");
        specAllItems.add("Adolescent medicine");
        specAllItems.add("Anaesthesiology");
        specAllItems.add("Aerospace medicine");
        specAllItems.add("Pathology");
        specAllItems.add("Cardiology");
        specAllItems.add("Cardiothoracic surgery");
        specAllItems.add("Child and adolescent psychiatry and psychotherapy");
        specAllItems.add("Clinical neurophysiology");
        specAllItems.add("Colon and Rectal Surgery");
        specAllItems.add("Dermatology-Venereology");
        specAllItems.add("Emergency medicine");
        specAllItems.add("Endocrinology");
        specAllItems.add("Gastroenterology");
        specAllItems.add("General practice");
        specAllItems.add("Geriatrics");
        specAllItems.add("Obstetrics and gynaecology");
        specAllItems.add("Health informatics");
        specAllItems.add("Hospice and palliative medicine");
        specAllItems.add("Infectious disease");
        specAllItems.add("Internal medicine");
        specAllItems.add("Interventional radiology");
        specAllItems.add("Vascular medicine");
        specAllItems.add("Microbiology");
        specAllItems.add("Nephrology");
        specAllItems.add("Neurology");
        specAllItems.add("Neurosurgery");
        specAllItems.add("Nuclear medicine");
        specAllItems.add("Occupational medicine");
        specAllItems.add("Ophthalmology");
        specAllItems.add("Orthodontics");
        specAllItems.add("Orthopaedics");
        specAllItems.add("Oral and maxillofacial surgery");
        specAllItems.add("Otorhinolaryngology");
        specAllItems.add("Paediatrics");
        specAllItems.add("Paediatric allergology");
        specAllItems.add("Paediatric cardiology");
        specAllItems.add("Paediatric endocrinology and diabetes");
        specAllItems.add("Paediatric gastroenterology, hepatology and nutrition");
        specAllItems.add("Paediatric haematology and oncology");
        specAllItems.add("Paediatric infectious diseases");
        specAllItems.add("Neonatology");
        specAllItems.add("Paediatric nephrology");
        specAllItems.add("Paediatric respiratory medicine");
        specAllItems.add("Paediatric rheumatology");
        specAllItems.add("Paediatric surgery");
        specAllItems.add("Physical medicine and rehabilitation");
        specAllItems.add("Plastic, reconstructive and aesthetic surgery");
        specAllItems.add("Pulmonology");
        specAllItems.add("Psychiatry");
        specAllItems.add("Public Health");
        specAllItems.add("Radiation Oncology");
        specAllItems.add("Radiology");
        specAllItems.add("Sports medicine");
        specAllItems.add("Neuroradiology");
        specAllItems.add("General surgery");
        specAllItems.add("Urology");
        specAllItems.add("Vascular surgery");
        specItems.addAll(specAllItems);
    }
    
    public void setUpdateValue(){
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        signinBean sBean = (signinBean) elContext.getELResolver().getValue(elContext, null, "signinBean");
        isUpdateMode = true;
        
        setAllSpecialty();
        
        Employee em = sBean.em;
        
        id = em.id;
        loginId = em.loginId;
        email = em.email;
        password = null;
        rePassword = null;
        fn = em.fn;
        ln = em.ln;
        name = em.name;
        gender = em.gender;
        phone = em.phone;
//        pic = em.pic;
        role = em.role;
        license = em.license;
        location = em.location;
        address = em.address;
        city = em.city;
        state = em.state;
        zip = em.zip;
        country = em.country;
//        accessToken = null;
        authority = em.authority;
        specialty = em.specialtyList;
//        dob = em.;
//        dob2 = null;
//        occupation = em.o;
//        religion = null;
//        emFN = null;
//        emLN = null;
//        emEmail = null;
//        emPhone = null;
//        emRelationship = null;
//        emAddress = null;
//        emCity = null;
//        emState = null;
//        emZip = null;
//        posAddress = null;
//        posCity = null;
//        posState = null;
//        posZip = null;
    }

    public List<String> specCompleteItem(String query) {
        query = query.toLowerCase();
        List<String> filteredList = new ArrayList<String>();
        for (String item : specAllItems) {
            if (item.toLowerCase().startsWith(query) && !specialty.contains(item)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    public List<String> getSpecItems() {
        return specItems;
    }

    public List<String> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(List<String> selectedItems) {
        this.specialty = selectedItems;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getFn() {
        return fn;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getLn() {
        return ln;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicense() {
        return license;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getAuthority() {
        return authority;
    }

    public void setDob(Double dob) {
        this.dob = dob;
    }

    public Double getDob() {
        return dob;
    }

    public void setDob2(Date dob2) {
        this.dob2 = dob2;
    }

    public Date getDob2() {
        return dob2;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getReligion() {
        return religion;
    }

    public void setEmFN(String emFN) {
        this.emFN = emFN;
    }

    public String getEmFN() {
        return emFN;
    }

    public void setEmLN(String emLN) {
        this.emLN = emLN;
    }

    public String getEmLN() {
        return emLN;
    }

    public void setEmEmail(String emEmail) {
        this.emEmail = emEmail;
    }

    public String getEmEmail() {
        return emEmail;
    }

    public void setEmPhone(String emPhone) {
        this.emPhone = emPhone;
    }

    public String getEmPhone() {
        return emPhone;
    }

    public void setEmRelationship(String emRelationship) {
        this.emRelationship = emRelationship;
    }

    public String getEmRelationship() {
        return emRelationship;
    }

    public void setEmAddress(String emAddress) {
        this.emAddress = emAddress;
    }

    public String getEmAddress() {
        return emAddress;
    }

    public void setEmCity(String emCity) {
        this.emCity = emCity;
    }

    public String getEmCity() {
        return emCity;
    }

    public void setEmState(String emState) {
        this.emState = emState;
    }

    public String getEmState() {
        return emState;
    }

    public void setEmZip(String emZip) {
        this.emZip = emZip;
    }

    public String getEmZip() {
        return emZip;
    }

    public void setPosAddress(String posAddress) {
        this.posAddress = posAddress;
    }

    public String getPosAddress() {
        return posAddress;
    }

    public void setPosCity(String posCity) {
        this.posCity = posCity;
    }

    public String getPosCity() {
        return posCity;
    }

    public void setPosState(String posState) {
        this.posState = posState;
    }

    public String getPosState() {
        return posState;
    }

    public void setPosZip(String posZip) {
        this.posZip = posZip;
    }

    public String getPosZip() {
        return posZip;
    }

    public boolean isIsUpdateMode() {
        return isUpdateMode;
    }

    public void setIsUpdateMode(boolean isUpdateMode) {
        this.isUpdateMode = isUpdateMode;
    }

    public Employee getEm() {
        return em;
    }

    public void setEm(Employee em) {
        this.em = em;
    }

    public Patient getP() {
        return p;
    }

    public void setP(Patient p) {
        this.p = p;
    }
    
    
}
