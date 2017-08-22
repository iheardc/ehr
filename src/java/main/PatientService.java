/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author tw
 */
@ManagedBean(name="patientService", eager = true)
@ApplicationScoped
public class PatientService {
    
    public static List<Patient> list;
    
    @PostConstruct
    public void init() {
//        list = getAllList();
    }
    
    public static List<Patient> getAllList(){
        return new DbDAO().getPatientNames(signinBean.locationId, null);
    }
    
    public static List<Patient> getFilteredList(String query){
        return new DbDAO().getPatientNames(signinBean.locationId, query);
    }

    public static List<Patient> getList() {
        if(list == null || list.size() <= 0){
            list = getAllList();
        }
        return list;
    }
    
}
