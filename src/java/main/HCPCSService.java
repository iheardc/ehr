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
@ManagedBean(name="hcpcsService", eager = true)
@ApplicationScoped
public class HCPCSService {
    
    private List<HCPCS> list;
    
    @PostConstruct
    public void init() {
        list = getAllList();
    }
    
    public static List<HCPCS> getAllList(){
        List<HCPCS> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getHCPCSCodes(null);
        return list;
    }
    
    public static List<HCPCS> getFilteredList(String query){
        List<HCPCS> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getHCPCSCodes(query);
        return list;
    }

    public List<HCPCS> getList() {
        return list;
    }
    
}
