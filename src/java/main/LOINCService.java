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
@ManagedBean(name="loincService", eager = true)
@ApplicationScoped
public class LOINCService {
    
    private List<LOINC> list;
    
   @PostConstruct
    public void init() {
//        list = getAllList();
    }
    
    public static List<LOINC> getAllList(){
        List<LOINC> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getLOINCCodes(null);
        return list;
    }
    
    public static List<LOINC> getFilteredList(String query){
        List<LOINC> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getLOINCCodes(query);
        return list;
    }

    public List<LOINC> getList() {
        if(list == null || list.size() <= 0){
            list = getAllList();
        }
        return list;
    }
    
}
