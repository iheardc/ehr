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
@ManagedBean(name="chargeCodeService", eager = true)
@ApplicationScoped
public class ChargeCodeService {
    
    private List<ChargeCode> list;
    
    @PostConstruct
    public void init() {
        list = getAllList();
    }
    
    public static List<ChargeCode> getAllList(){
        List<ChargeCode> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getChargeCodes(null);
        return list;
    }
    
    public static List<ChargeCode> getFilteredList(String query){
        List<ChargeCode> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getChargeCodes(query);
        return list;
    }

    public List<ChargeCode> getList() {
        return list;
    }
    
}
