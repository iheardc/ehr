package main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author degla
 */
@Named(value = "serviceBean")
@SessionScoped
public class ServiceBean implements Serializable {
    
    UploadedFile fileUpload;
    byte[] data = null;
    byte[] nopic;

    /**
     * Creates a new instance of serviceBean
     */
    public ServiceBean() throws IOException {
        this.fileUpload = null;
        ResourceHandler resourceHandler = FacesContext.getCurrentInstance().getApplication().getResourceHandler();
        InputStream is = resourceHandler.createResource("nopic.png", "images").getInputStream();
//        InputStream is = resourceHandler.createResource("images/nopic.png").getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1];
        int length; 
        while ((length = is.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        nopic = output.toByteArray();
        is.close();
        output.close();
    }
    public void reset(){
       data = null;
       fileUpload = null;
    }
    public void fileUpload(FileUploadEvent event){
        fileUpload = event.getFile();
        data = fileUpload.getContents();
        System.out.println("ServiceBean " + data.length + fileUpload.getFileName());
    }

    public UploadedFile getFileUpload(){
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public byte[] getData() throws IOException {
        if (data == null){
            return nopic;
        }
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
}
