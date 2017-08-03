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
@ManagedBean(name="rxnormService", eager = true)
@ApplicationScoped
public class RxNORMService {
    
    private List<RxNORM> list;
    
    @PostConstruct
    public void init() {
        list = getAllList();
    }
    
    public static List<RxNORM> getAllList(){
        List<RxNORM> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getRxNORMCodes(null);
        return list;
    }
    
    public static List<RxNORM> getFilteredList(String query){
        List<RxNORM> list = new ArrayList<>();
        DbDAO dao = new DbDAO();
        list = dao.getRxNORMCodes(query);
        return list;
    }

    public List<RxNORM> getList() {
        return list;
    }
    
}
