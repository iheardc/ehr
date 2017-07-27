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
@ManagedBean(name="synomedService", eager = true)
@ApplicationScoped
public class SynomedService {
    
    private List<SynomedCT> list;
    
    @PostConstruct
    public void init() {
        list = getAllList();
    }
    
    public static List<SynomedCT> getAllList(){
        List<SynomedCT> list = new ArrayList<>();
        list.add(new SynomedCT("123456", "Allergy"));
        list.add(new SynomedCT("223456", "Bllergy"));
        list.add(new SynomedCT("323456", "Cllergy"));
        list.add(new SynomedCT("423456", "Dllergy"));
        list.add(new SynomedCT("523456", "Ellergy"));
        list.add(new SynomedCT("623456", "Fllergy"));
        list.add(new SynomedCT("723456", "Gllergy"));
        list.add(new SynomedCT("823456", "Hllergy"));
        return list;
    }

    public List<SynomedCT> getList() {
        return list;
    }
    
}
