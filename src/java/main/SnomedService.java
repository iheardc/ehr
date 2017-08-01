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
        list.add(new SnomedCT("123456", "Allergy"));
        list.add(new SnomedCT("223456", "Bllergy"));
        list.add(new SnomedCT("323456", "Cllergy"));
        list.add(new SnomedCT("423456", "Dllergy"));
        list.add(new SnomedCT("523456", "Ellergy"));
        list.add(new SnomedCT("623456", "Fllergy"));
        list.add(new SnomedCT("723456", "Gllergy"));
        list.add(new SnomedCT("823456", "Hllergy"));
        return list;
    }

    public List<SnomedCT> getList() {
        return list;
    }
    
}
