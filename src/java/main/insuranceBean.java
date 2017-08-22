/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.el.ELContext;
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

    String msg = "NOT registered yet";

    private Patient selectedP;
    private Insurance selectedIns;
    private Insurance saveIns;
    private DynamicInfo dynamicInfos;
    private List<Insurance> findList;
    private List<Insur_medicine> findMlist;
    private List<Insur_investigations> findIlist;
    private List<Insur_diagnosis> findDlist;
    private List<Insur_procedure> findPlist;
    private List<DynamicInfo> findOlist;
    boolean isShowMore = false;
    boolean isShowMore2 = false;
    Date date;
    double dates;
    String g_drg;

    //insurance
    String id, patient_id, scheme;
    String organization_name;
    double date_of_claim;
    String patient_type, patient_type2, patient_type3;
    String detail_type, detail_type2;
    String member_no, serial_no, hosp_record_no;
    String G_DRG;
    String health_facility;
    double third_visit;
    double admission_date;
    double discharge_date;
    String spell, cc_code, physician;
    String specialty, specialty_code;
    String date_received, action1, signed1, signed2, action2, signed3;
    double date1, date2;

    public void what_patient_type() {
        if ("ADM".equals(dynamicInfos.status)) {
            patient_type = "In-Patient";
        } else {
            patient_type = "Out-Patient";
        }
    }

    public void registrationInsurance() {

        if (selectedP == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed", "Select patient!!"));
            return;
        }

        what_patient_type();
        DbDAO dao = new DbDAO();
        saveIns = new Insurance(id, selectedP.id, scheme, organization_name, date_of_claim, patient_type, patient_type2, patient_type3, detail_type, detail_type2, member_no, serial_no, hosp_record_no, G_DRG, third_visit, spell, cc_code, physician, specialty, specialty_code, date_received, action1, signed1, signed2, action2, signed3, date1, date2, admission_date, discharge_date, health_facility);

        dao.insertNewInsurance(saveIns);

        //failed
        if (saveIns.errormsg.length()
                > 0) {
            msg = saveIns.errormsg;
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", saveIns.errormsg);
            FacesContext.getCurrentInstance().addMessage(null, message);
//            return "";
        } else {
            reset();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage message = new FacesMessage("Successfylly registered ", "Your registration has been successfully completed!");
            FacesContext.getCurrentInstance().addMessage(null, message);
//            return "/admin/create_employee.xhtml?faces-redirect=true";
            menuBean.writeInsurance();
        }
        RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");

    }

    public void reset() {
//        id=null; 
//        patient_id=null;
        selectedP = null;
        scheme = null;
//        date_of_claim=0;
//        patient_type=null;
//        organization_name = null;
        patient_type2 = null;
//        patient_type3=null;
        detail_type = null;
//        detail_type2=null;
        member_no = null;
        serial_no = null;
        hosp_record_no = null;
//        G_DRG=null;
        health_facility = null;
//        third_visit=0;
//        admission_date=0;
//        discharge_date=0;
//        spell=null;
//        cc_code=null; 
//        physician=null;
        specialty = null;
        specialty_code = null;
//        date_received=null; 
//        action1=null; 
//        signed1=null; 
//        signed2=null; 
//        action2=null; 
//        signed3=null;
//        date1=0; 
//        date2=0;
    }

    public void onPatientSelectRowSelectTo(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        viewdate();
    }

    public void viewdate() {
        isShowMore2 = true;
        DbDAO dao = new DbDAO();
        findOlist = dao.finddate(selectedP.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findOlist.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findOlist.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
//        selectedP = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form");
    }

    public void onPatientSelectRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Patient Selected", ((Patient) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        selectPatient();
    }

    public void selectPatient() {
        isShowMore = true;

        DbDAO dao = new DbDAO();
        findList = dao.findInsurance(selectedP.id);

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

    public int selectage() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        int current = year - DbDAO.getDateYear((long) (double) selectedP.dob);
        return current;
    }

    public String selectsex() {
        if (selectedP.gender.equals("male")) {
            return "M";
        } else {
            return "F";
        }
    }

    public void VselectInsurance() {
        selectInsurmedicine();
        selectInsurinvestigations();
        selectInsurdiagnosis();
        selectInsurprocedure();
        pathCont = "/insurance/insurance_viewdetail.xhtml";
    }

//    public void WselectInsurance(Patient p){
//        selectInsurmedicine();
//        selectInsurinvestigations();
//        selectInsurdiagnosis();
//        selectInsurprocedure();
//        pathCont = "/insurance/insurance_writedetail.xhtml";
//    }
    public void selectInsurprocedure() {
        DbDAO dao = new DbDAO();
        findPlist = dao.findInsProcedure(selectedIns.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findPlist.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findPlist.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
//        selectedP = null;
        RequestContext context = RequestContext.getCurrentInstance();

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientBean bean = (patientBean) elContext.getELResolver().getValue(elContext, null, "patientBean");
        bean.reset();
//        pathCont = "/insurance/insurance_viewdetail.xhtml";
    }

    public void selectInsurdiagnosis() {
        DbDAO dao = new DbDAO();
        findDlist = dao.findInsDiagnosis(selectedIns.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findDlist.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findDlist.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
//        selectedP = null;
        RequestContext context = RequestContext.getCurrentInstance();

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientBean bean = (patientBean) elContext.getELResolver().getValue(elContext, null, "patientBean");
        bean.reset();
//        pathCont = "/insurance/insurance_viewdetail.xhtml";
    }

    public void selectInsurinvestigations() {
        DbDAO dao = new DbDAO();
        findIlist = dao.findInsInvestigations(selectedIns.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findIlist.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findIlist.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
//        selectedP = null;
        RequestContext context = RequestContext.getCurrentInstance();

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientBean bean = (patientBean) elContext.getELResolver().getValue(elContext, null, "patientBean");
        bean.reset();
//        pathCont = "/insurance/insurance_viewdetail.xhtml";
    }

    public void selectInsurmedicine() {
        DbDAO dao = new DbDAO();
        findMlist = dao.findInsMedicine(selectedIns.id);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message;
        if (findMlist.size() > 0) {
            message = new FacesMessage("Search ", "There are " + findMlist.size() + " results.");
        } else {
            message = new FacesMessage("Search ", "There are no matching results.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
//        selectedP = null;
        RequestContext context = RequestContext.getCurrentInstance();

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        patientBean bean = (patientBean) elContext.getELResolver().getValue(elContext, null, "patientBean");
        bean.reset();
//        pathCont = "/insurance/insurance_viewdetail.xhtml";
    }

    public String selectpatienttype() {
        if (selectedIns.patient_type.equals("Out-Patient")) {
            return "Out-Patient";
        } else {
            return "In-Patient";
        }
    }

    public String selectpatienttype2() {
        if (selectedIns.patient_type2.equals("Diagnosis")) {
            return "Diagnosis";
        } else if (selectedIns.patient_type2.equals("All-Inclusive")) {
            return "All-Inclusive";
        } else {
            return "Unbundled";
        }
    }

    public double total() {
        double totals = 0;
        for (int i = 0; i < findMlist.size(); i++) {
            totals = totals + findMlist.get(i).total_amt;
        }
        return totals;
    }

    public void onDateSelect(SelectEvent event) {
        dates = Double.parseDouble(Long.toString(date.getTime()));
        System.out.println(Double.toString(dates));
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public boolean selectpatienttype3() {
        return "Pharmacy".equals(selectedIns.patient_type3);

    }

    public String selectdetailtype() {
        if ("Discharge".equals(selectedIns.detail_type)) {
            return "Discharge";
        } else if ("Died".equals(selectedIns.detail_type)) {
            return "Died";
        } else if ("Transferred Out".equals(selectedIns.detail_type)) {
            return "Transferred Out";
        } else {
            return "Absconded/Discharged against medical advise";
        }
    }

    public String selectdetailtype2() {
        if ("Chronic Follow-up".equals(selectedIns.detail_type2)) {
            return "Chronic Follow-up";
        } else {
            return "Emgergency/Acute Episode";
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

    public List<Insur_medicine> getFindMlist() {
        return findMlist;
    }

    public void setFindMlist(List<Insur_medicine> findMlist) {
        this.findMlist = findMlist;
    }

    public List<Insur_investigations> getFindIlist() {
        return findIlist;
    }

    public void setFindIlist(List<Insur_investigations> findIlist) {
        this.findIlist = findIlist;
    }

    public List<Insur_diagnosis> getFindDlist() {
        return findDlist;
    }

    public void setFindDlist(List<Insur_diagnosis> findDlist) {
        this.findDlist = findDlist;
    }

    public List<Insur_procedure> getFindPlist() {
        return findPlist;
    }

    public void setFindPlist(List<Insur_procedure> findPlist) {
        this.findPlist = findPlist;
    }

    public DynamicInfo getDynamicInfos() {
        return dynamicInfos;
    }

    public void setDynamicInfos(DynamicInfo dynamicInfos) {
        this.dynamicInfos = dynamicInfos;
    }

    public List<DynamicInfo> getFindOlist() {
        return findOlist;
    }

    public void setFindOlist(List<DynamicInfo> findOlist) {
        this.findOlist = findOlist;
    }

    public boolean isIsShowMore2() {
        return isShowMore2;
    }

    public void setIsShowMore2(boolean isShowMore2) {
        this.isShowMore2 = isShowMore2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDates() {
        return dates;
    }

    public void setDates(double dates) {
        this.dates = dates;
    }

    public String getG_drg() {
        return g_drg;
    }

    public void setG_drg(String g_drg) {
        this.g_drg = g_drg;
    }

    public Insurance getSaveIns() {
        return saveIns;
    }

    public void setSaveIns(Insurance saveIns) {
        this.saveIns = saveIns;
    }

    //insurance
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public double getDate_of_claim() {
        return date_of_claim;
    }

    public void setDate_of_claim(double date_of_claim) {
        this.date_of_claim = date_of_claim;
    }

    public String getPatient_type() {
        return patient_type;
    }

    public void setPatient_type(String patient_type) {
        this.patient_type = patient_type;
    }

    public String getPatient_type2() {
        return patient_type2;
    }

    public void setPatient_type2(String patient_type2) {
        this.patient_type2 = patient_type2;
    }

    public String getPatient_type3() {
        return patient_type3;
    }

    public void setPatient_type3(String patient_type3) {
        this.patient_type3 = patient_type3;
    }

    public String getDetail_type() {
        return detail_type;
    }

    public void setDetail_type(String detail_type) {
        this.detail_type = detail_type;
    }

    public String getDetail_type2() {
        return detail_type2;
    }

    public void setDetail_type2(String detail_type2) {
        this.detail_type2 = detail_type2;
    }

    public String getMember_no() {
        return member_no;
    }

    public void setMember_no(String member_no) {
        this.member_no = member_no;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getHosp_record_no() {
        return hosp_record_no;
    }

    public void setHosp_record_no(String hosp_record_no) {
        this.hosp_record_no = hosp_record_no;
    }

    public String getG_DRG() {
        return G_DRG;
    }

    public void setG_DRG(String G_DRG) {
        this.G_DRG = G_DRG;
    }

    public String getHealth_facility() {
        return health_facility;
    }

    public void setHealth_facility(String health_facility) {
        this.health_facility = health_facility;
    }

    public double getThird_visit() {
        return third_visit;
    }

    public void setThird_visit(double third_visit) {
        this.third_visit = third_visit;
    }

    public double getAdmission_date() {
        return admission_date;
    }

    public void setAdmission_date(double admission_date) {
        this.admission_date = admission_date;
    }

    public double getDischarge_date() {
        return discharge_date;
    }

    public void setDischarge_date(double discharge_date) {
        this.discharge_date = discharge_date;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getCc_code() {
        return cc_code;
    }

    public void setCc_code(String cc_code) {
        this.cc_code = cc_code;
    }

    public String getPhysician() {
        return physician;
    }

    public void setPhysician(String physician) {
        this.physician = physician;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSpecialty_code() {
        return specialty_code;
    }

    public void setSpecialty_code(String specialty_code) {
        this.specialty_code = specialty_code;
    }

    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
    }

    public String getAction1() {
        return action1;
    }

    public void setAction1(String action1) {
        this.action1 = action1;
    }

    public String getSigned1() {
        return signed1;
    }

    public void setSigned1(String signed1) {
        this.signed1 = signed1;
    }

    public String getSigned2() {
        return signed2;
    }

    public void setSigned2(String signed2) {
        this.signed2 = signed2;
    }

    public String getAction2() {
        return action2;
    }

    public void setAction2(String action2) {
        this.action2 = action2;
    }

    public String getSigned3() {
        return signed3;
    }

    public void setSigned3(String signed3) {
        this.signed3 = signed3;
    }

    public double getDate1() {
        return date1;
    }

    public void setDate1(double date1) {
        this.date1 = date1;
    }

    public double getDate2() {
        return date2;
    }

    public void setDate2(double date2) {
        this.date2 = date2;
    }

}
