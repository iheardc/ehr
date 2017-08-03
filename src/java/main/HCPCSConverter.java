/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author tw
 */
@FacesConverter("hcpcsConverter")
public class HCPCSConverter implements Converter {

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
//                ThemeService service = (ThemeService) fc.getExternalContext().getApplicationMap().get("themeService");
//                return service.getThemes().get(Integer.parseInt(value));
                HCPCSService service = (HCPCSService) fc.getExternalContext().getApplicationMap().get("hcpcsService");
                List<HCPCS> list = service.getList();
                for(HCPCS h : list){
                    if(h.getCode().equals(value)){
                        return h;
                    }
                }
                return null;
            } catch (Exception e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
//            return String.valueOf(((Theme) object).getId());
            return ((HCPCS) object).getCode();
        } else {
            return null;
        }
    }

}
