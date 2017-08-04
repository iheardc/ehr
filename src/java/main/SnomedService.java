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
@ManagedBean(name="snomedService", eager = true)
@ApplicationScoped
public class SnomedService {
    
    private List<SnomedCT> list;
    
   @PostConstruct
    public void init() {
        list = getAllList();
    }
    
    public static List<SnomedCT> getAllList(){
        List<SnomedCT> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getSNOMEDCTCodes(null);
        return list;
    }
    
    public static List<SnomedCT> getFilteredList(String query){
        List<SnomedCT> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getSNOMEDCTCodes(query);
        return list;
    }

    public List<SnomedCT> getList() {
        return list;
    }
    
}
