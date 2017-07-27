/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author tw
 */
public class SynomedCT {
    
    String code, description;
    
    public SynomedCT(){
        
    }

    public SynomedCT(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public boolean isContain(String query){
        return (code.toLowerCase().startsWith(query) || description.toLowerCase().startsWith(query));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public boolean equals(Object obj){
        SynomedCT s = (SynomedCT)obj;
        return (code.equals(s.code) && description.equals(s.description));
    }
    
}
