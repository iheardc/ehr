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
@ManagedBean(name="employeeService", eager = true)
@ApplicationScoped
public class EmployeeService {
    
    public static List<Employee> list;
    
    @PostConstruct
    public void init() {
        list = getAllList();
    }
    
    public static List<Employee> getAllList(){
        return new DbDAO().findAllEmployeeNames("");
    }

    public static List<Employee> getList() {
        return list;
    }
    
    public static List<Employee> getRoleList(String role){
        role = role.toLowerCase();
        List<Employee> filteredList = new ArrayList<>();
        for(Employee em : list){
            if(em.getRole().toLowerCase().equals(role)){
                filteredList.add(em);
            }
        }
        return filteredList;
    }
    
}
