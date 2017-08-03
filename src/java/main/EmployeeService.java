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
        return new DbDAO().getEmployeeNames(null, null);
    }
    
    
    public static List<Employee> getFilteredList(String role){
        return new DbDAO().getEmployeeNames(role, null);
    }
    
    public static List<Employee> getFilteredList(String role, String query){
        return new DbDAO().getEmployeeNames(role, query);
    }

    public static List<Employee> getList() {
        return list;
    }
    
}
