package main;


import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.w3c.dom.ProcessingInstruction;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author degla
 */
public class DropBox {
    
    private static final String ACCESS_TOKEN = "mEoWZw2C-FAAAAAAAAAADFLSNMq6XJDft-Uc1EGl5dTJpUAv6Nq8ZCjU1EmX2Csv";
    
    String msg; 
    String  extension, flName;
    String search;//, view;
    boolean connectedToDB;
    private static final String META = "metadata.xml";
    MetaData meta;
    Forms forms;
    
    String test = "TEST";
    
    String patientPHRPath, patientEHRPath;
    UploadedFile fileUpload;
    //Part fileUpload;
    //File inputFile;//for upload after Part transfered to file
    
    DbxClient client;
    DbxRequestConfig config;
    int DEFAULT_BUFFER_SIZE = 100240;
    public DropBox(){
        meta = new MetaData();
    }
    //connect to DB
    public void connectDB(Patient p){
//        try {//connect to DB
            msg = "";
            config = new DbxRequestConfig("EHR", Locale.getDefault().toString());
            client = new DbxClient(config, ACCESS_TOKEN);
//            p.DBaccnt = "Linked to DropBox account: " + client.getAccountInfo().displayName;
            connectedToDB = true;
            msg = "connected";
            patientPHRPath = p.getRealId() + "/PHR";
            patientEHRPath = p.getRealId() + "/EHR";
//            p.error = false;System.out.println("Pat connected");
//        } catch (DbxException ex) { 
//            connectedToDB = false;System.out.println("Pat not connected" + ex);
//            p.DBaccnt = "Not connected: Wrong access token. Please see 'HELP' for instructions.";
//        }        
    } 
    public void connectDB(Employee em){
//        try {//connect to DB
            msg = "";
            config = new DbxRequestConfig("EHR", Locale.getDefault().toString());
            client = new DbxClient(config, ACCESS_TOKEN);
//            d.DBaccnt = "Linked to DropBox account: " + client.getAccountInfo().displayName;
            connectedToDB = true;
            msg = "connected";
            //d.error = false;
            System.out.println("Doc connected" );
//        } catch (DbxException ex) { 
//            connectedToDB = false;
//            System.out.println("Doc not connected" + ex);
            //d.DBaccnt = "Not connected: Wrong access token. Please see 'HELP' for instructions.";
//        }        
    } 
    public void connectDB(String str) throws DbxException{
//        try {//connect to DB
            //msg = "";
            config = new DbxRequestConfig("EHR", Locale.getDefault().toString());
            client = new DbxClient(config, str);
            //client.getAccountInfo();
            //System.out.println("Linked to DropBox account: " + client.getAccountInfo().displayName);
            //connectedToDB = true;
            //msg = "connected";
            //d.error = false;
//        } catch (DbxException ex) { 
//            connectedToDB = false;
//            //d.DBaccnt = "Not connected: Wrong access token. Please see 'HELP' for instructions.";
//        }        
    } 
        //connect to DB
 
    //create array list of all documents in DB
    public void viewAllDocuments(ArrayList list)throws Exception{
        DbxEntry.WithChildren listing = client.getMetadataWithChildren("/" + patientEHRPath); 
        for (DbxEntry child : listing.children) {
            String date;
            DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
            Date dateobj = child.asFile().lastModified;
            date = df.format(dateobj);
            forms = new Forms(child.name , child.asFile().humanSize, date);
            if(!forms.name.equals("metadata.xml"))
                list.add(forms);
        }
    }    
    //download selected file. Add select path!
    public void downloadFile(String file, String path) throws Exception{
        OutputStream outputStream = new FileOutputStream(path + file);
        try {
            DbxEntry.File downloadedFile = client.getFile("/" + patientEHRPath + "/" + file, null,outputStream);
        } finally {
            outputStream.close();
        }
    }     
    //delete selected file form DB
    public void deleteFile(String file){
        try {
            client.delete("/" + patientEHRPath + "/" + file);//deletes file from DB
        } catch (DbxException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateMetaOnDelete(file);//udates Metadata
    }
    //update metadat file when file deleted
    public void updateMetaOnDelete(String delFileNM) {
        try{
        Scanner scan = new Scanner(getFileDB(META).toString());//gets meta file 
        StringBuilder strBuilder = new StringBuilder(10000);
        while(scan.hasNext()){//reads meta file line by line
            String line = scan.nextLine().trim();
            if(!line.equals("")){
                InputSource source = new InputSource(new StringReader(line));

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(source);

                XPathFactory xpathFactory = XPathFactory.newInstance();
                XPath xpath = xpathFactory.newXPath();

                String title = xpath.evaluate("/metadata/Title", document);//gets value of Title XML from each meta entry

                if(!title.equals(delFileNM)){//if title is not deleted file, add to meta string
                    strBuilder.append(line + "\n");
                }
            }
        }
        saveMeta(strBuilder.toString());//saves string to metafile
        }
        catch(Exception e){}
    }     
    
    //upload file to DB
//    public void uploadFile(FileUploadEvent event){
//        System.out.println("In fileUpload db");
//        try{
////            selectedUpload = fileUpload.getSubmittedFileName();//path
////            InputStream inputStream = fileUpload.getInputStream();
////            fileUpload = event.getFile();
//            this.fileUpload = event.getFile();
//            addMetaFileInfo();
//            selectedUpload = fileUpload.getFileName();
//            InputStream inputStream = fileUpload.getInputstream();
//            
//            System.out.println("!!!!! path:= " + selectedUpload);
//            setExtens(selectedUpload);//file name and extension
//            //inputFile = new File(inputStream);//file and its path 
//            //check if file is duplicated if found delete
//            DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");            
//            for (DbxEntry child : listing.children){
//                if(child.name.equals(flName)){
//                    deleteFile(flName);
//                }
//            }            
//            //upload
//            //FileInputStream inputStream = new FileInputStream(inputFile);
//            DbxEntry.File uploadedFile = client.uploadFile("/" + flName,
//                DbxWriteMode.force(), fileUpload.getSize(), inputStream);//upload to DB , overwrite file?
//            addMetaFileInfo(); //get info from a file
//        }
//        catch(Exception e){System.out.println("!!!ERROR " + e);}
//    }  
    public void uploadFile(UploadedFile fileUpload){
        this.fileUpload = fileUpload;
        addMetaFileInfo();
    }
    
    //add meta info from uploaded file
    public void addMetaFileInfo(){//receive info from the downloaded file   
        meta = new MetaData();
        meta.title = fileUpload.getFileName(); 
        setExtens(meta.title);
        meta.type = extension;
        meta.format = Long.toString(fileUpload.getSize());
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssZ");
        Date dateobj = new Date();
        meta.date = df.format(dateobj);
    } 
    public void addMetaNewRecord(Patient p, Employee em){
        meta = new MetaData();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssZ");
        Date dateobj = new Date();
        String date = df.format(dateobj);
        meta.title = p.ln + date + ".xml"; 
        meta.type = ".xml";
        meta.creator = "Dr. " + em.ln;
        meta.date = date;   
//        try {
//            saveMeta((getFileDB(META).append("\n" + toXML())).toString());
//        } catch (Exception ex) {
//            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    //update metadata file/ add 
    public void finishUpload() throws Exception{
        try{  
            InputStream inputStream = fileUpload.getInputstream();
            //check if file is duplicated if found delete
            flName = meta.title;
//            DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");            
//            for (DbxEntry child : listing.children){
//                if(child.name.equals(flName)){
//                    deleteFile(flName);
//                }
//            }            
            DbxEntry.File uploadedFile = client.uploadFile("/" + patientEHRPath + "/" + flName,
            DbxWriteMode.force(), fileUpload.getSize(), inputStream);//upload to DB , overwrite file?
            System.out.println("!!!!!!1 " + meta.title);
            saveMeta((getFileDB(META).append("\n" + toXML())).toString());
        }
        catch(Exception e){System.out.println("ERROR DB fin upload " + e.toString());}
    } 
    public void finishUploadNew() {
        try {
            saveMeta((getFileDB(META).append("\n" + toXML())).toString());
        } catch (IOException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //view file
    public String viewFile(String file) throws Exception{
        String view = "";
        //setExtens(file);
        if (file.equals(META)){
            Scanner scan = new Scanner(getFileDB(META).toString());      
            while(scan.hasNext()){
                //view = formatXML(view);meta
                String line = scan.nextLine();
                if(!line.equals("")){
                    view = view + transferXSLTForMeta(line).toString();
                    //displayFile(view, "text/html");
                }
            }
            //displayFile(view, "text/html");
        }
        else view = getFileDB(file).toString();
//        System.out.println("Formatade before " + view);
//        view = allInline(view);
//        System.out.println("Formatade after " + view +" !!@@#@@ FORMATED end");
        return view;//displays file in the form
//                System.out.println("Formatade before " + content);
//        content = allInline(content);
//                System.out.println("Formatade after " + content);
    } 
    public String viewMetaFileContext() throws Exception{
        String view = "";
        Scanner scan = new Scanner(getFileDB(META).toString());      
        while(scan.hasNext()){
            String line = scan.nextLine();
            if(!line.equals("")){
                view = view + transferXSLTForMeta(line).toString();
            }
        }
        return view;
    }
    //retrieve XSL file name from XML processing instruction
    public String getXSL(String file){
        Scanner scan;
        try {
            scan = new Scanner(getFileDBUnformated(file));

            while(scan.hasNext()){
                String line = scan.nextLine().trim();
                System.out.println(line);
                if(!line.equals("")){
                    InputSource source = new InputSource(new StringReader(line));
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document document = db.parse(source);
                    XPathFactory xpathFactory = XPathFactory.newInstance();
                    XPath xpath = xpathFactory.newXPath();
                    String searchField = xpath.evaluate("/processing-instruction('xml-stylesheet')", document);

                    if(searchField.length() > 1){
                        String xslName = "";
                        int f = searchField.indexOf("=", searchField.toLowerCase().indexOf("href")) + 2;
                        int l = searchField.toLowerCase().indexOf("xsl", f) + 3;
                        xslName = searchField.substring(f, l); 
                        return xslName;
                    }
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    //apply XSL to XML for HTML view
    public String transferXSLT(String fileXML, String fileXSL)throws Exception{
        //String outputHTML = "";
        try{
            DataOutputStream output = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream("xml")));
            try {
                DbxEntry.File downloadedFile = client.getFile("/" + patientEHRPath + "/" + fileXML, null,output);
            } finally {
                output.close();
            }
                InputStream inputXSL = null;
            //if CDA read from server otherwise from Dropbox
            if(!fileXSL.equals("CDA.xsl")){
                DataOutputStream output2 = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream("cda")));
                try {
                    DbxEntry.File downloadedFile = client.getFile("/" + patientEHRPath + "/" + fileXSL, null,output2);
                } finally {
                    output2.close();
                }    
                inputXSL = new DataInputStream(
                    new BufferedInputStream(new FileInputStream("cda")));                
            }else{
                FacesContext currentContext = FacesContext.getCurrentInstance();
                ResourceHandler resourceHandler = currentContext.getApplication().getResourceHandler();
                javax.faces.application.Resource resource = resourceHandler.createResource("CDA.xsl");

                inputXSL = new DataInputStream(new BufferedInputStream(resource.getInputStream()));                
            }
//InputStream inputXSL;
            InputStream dataXML = new DataInputStream(
                new BufferedInputStream(new FileInputStream("xml")));

            StringWriter stringWriter = new StringWriter();
                    //StreamResult streamResult = new StreamResult(stringWriter);

            TransformerFactory factory = TransformerFactory.newInstance();
            StreamSource xslStream = new StreamSource(inputXSL);
            Transformer transformer = factory.newTransformer(xslStream);
            StreamSource in = new StreamSource(dataXML);
            StreamResult out = new StreamResult(stringWriter);
            //StreamResult out2 = new StreamResult(outHTML);
            transformer.transform(in, out);
                   // transformer.transform(in, outHTML);
    //
    //        System.out.println("The generated HTML file is:" + stringWriter.toString());

            dataXML.close();
            inputXSL.close();
            //outHTML.close();


            return stringWriter.toString();            
        }
        catch(Exception e){
            Logger.getLogger(e.toString());
            return "error";
        }

        //return "error";
    }
   
    //apply XSL to XML for HTML view
    public StringWriter transferXSLTForMeta(String fileXML)throws Exception{
        //String outputHTML = "";
        DataOutputStream output = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream("xml")));
        try {
            output.writeBytes(fileXML);
        } finally { output.close();}
        FacesContext currentContext = FacesContext.getCurrentInstance();
        ResourceHandler resourceHandler = currentContext.getApplication().getResourceHandler();
        javax.faces.application.Resource resource = resourceHandler.createResource("DC.xsl");
        InputStream dataXML = new DataInputStream(new BufferedInputStream(new FileInputStream("xml")));
        InputStream inputXSL = new DataInputStream(new BufferedInputStream(resource.getInputStream()));
        StringWriter stringWriter = new StringWriter();
        TransformerFactory factory = TransformerFactory.newInstance();
        StreamSource xslStream = new StreamSource(inputXSL);
        Transformer transformer = factory.newTransformer(xslStream);
        StreamSource in = new StreamSource(dataXML);
        StreamResult out = new StreamResult(stringWriter);
        transformer.transform(in, out);
        dataXML.close();
        inputXSL.close();
        return stringWriter;
    } 

    //get metadata for specific file
    public String getMetadataForFile(String fileName) {
        try{
            Scanner scan = new Scanner(getFileDB(META).toString());//gets meta file 
            StringBuilder strBuilder = new StringBuilder(10000);
            while(scan.hasNext()){//reads meta file line by line
                String line = scan.nextLine().trim();
                if(!line.equals("")){
                    InputSource source = new InputSource(new StringReader(line));
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document document = db.parse(source);
                    XPathFactory xpathFactory = XPathFactory.newInstance();
                    XPath xpath = xpathFactory.newXPath();
                    String title = xpath.evaluate("/metadata/Title", document);//gets value of Title XML from each meta entry
                    if(title.equals(fileName)){//if title is not deleted file, add to meta string
                        strBuilder.append(line + "\n");
//                        System.out.println("META!!!! " + strBuilder.toString());
                        return strBuilder.toString().trim();
                    }
                }
            }
        }
        catch(Exception e){}
        return "";
    }   
    //search DB metadata file
    public void search(ArrayList list, String search, String item)throws Exception{
        //String line;
        //list = new ArrayList<>();
        Scanner scan = new Scanner(getFileDB(META).toString());
        while(scan.hasNext()){
            String line = scan.nextLine();
            if(!line.equals("")){
                InputSource source = new InputSource(new StringReader(line));
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(source);

                XPathFactory xpathFactory = XPathFactory.newInstance();
                XPath xpath = xpathFactory.newXPath();

                String searchField = xpath.evaluate("/metadata/" + item, document);
                if(searchField.toLowerCase().contains(search.toLowerCase())){
                    forms = new Forms(xpath.evaluate("/metadata/Title", document)
                            , xpath.evaluate("/metadata/Format", document), xpath.evaluate("/metadata/Date", document));

                    list.add(forms);
                }
            }
        }
    }    



    
    public void displayFile(String view, String mime) throws FileNotFoundException, IOException, DbxException{
        int DEFAULT_BUFFER_SIZE = 100240;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        InputStream is = new ByteArrayInputStream(view.getBytes());
        input = new BufferedInputStream(is);

        response.reset();
        response.setHeader("Content-Type", mime);
        //response.setHeader("Content-Length", "LENGTH");
        response.setHeader("Content-Disposition", "inline; filename= + getFileName() + ");
        output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        System.out.println("INPUTSTRIAM " + input.toString()); 
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
            System.out.println("!!!! LENGTH " + length);
        }

        output.flush();
        //facesContext.responseComplete();
        output.close();
        input.close();
        //facesContext.renderResponse();
        facesContext.responseComplete(); 
        //return "http://docs.oracle.com/javaee/7/api/javax/faces/context/FacesContext.html";
    }
    public void displayFileOther(String file, String mime) throws Exception{
        int DEFAULT_BUFFER_SIZE = 100240;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        BufferedInputStream input = null;
        BufferedOutputStream output = null; 

        try {
        output = new BufferedOutputStream(new FileOutputStream("temp.txt"));
        try {
            DbxEntry.File downloadedFile = client.getFile("/" + patientEHRPath + "/" + file, null,output);
        } finally {
            output.close();
        }
        String s;
        input =   new BufferedInputStream(new FileInputStream("temp.txt"));
        response.reset();
        response.setHeader("Content-Type", mime);
            //response.setHeader("Content-Length", "LENGTH");
        response.setHeader("Content-Disposition", "inline; filename=" + file);
        output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
        
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        System.out.println("INPUTSTRIAM " + input.toString()); 
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
            System.out.println("!!!! LENGTH " + length);
        }
System.out.println("@@@@@@@ !!!!!in DB content" + output.toString().length());
            // Finalize task.
            output.flush();
        } finally {
            output.close();
            input.close();
        }
        facesContext.responseComplete();
    }
    public ByteArrayOutputStream getByteArrayOutFile(String file) throws FileNotFoundException, DbxException, IOException{
        BufferedInputStream input = null;
        BufferedOutputStream output = null; 
        ByteArrayOutputStream outArr = null;
        output = new BufferedOutputStream(new FileOutputStream("temp.txt"));
        try {
            DbxEntry.File downloadedFile = client.getFile("/" + patientEHRPath + "/" + file, null,output);
        } finally {
            output.close();
        }
        input =   new BufferedInputStream(new FileInputStream("temp.txt"));
        outArr = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = input.read(buffer)) > 0) {
            outArr.write(buffer, 0, length);
        }
        input.close();
        return outArr;
    }
    public ByteArrayOutputStream getByteArrayOutView(String view){
        ByteArrayOutputStream outArr = null;
        byte[] bytes = view.getBytes();       
        outArr = new ByteArrayOutputStream(bytes.length);
        outArr.write(bytes, 0, bytes.length);       
        return outArr;
    }
//    public void convertToHTML(String file){
//        try{
//            DataOutputStream output = new DataOutputStream(
//                new BufferedOutputStream(new FileOutputStream("file")));
//            try {
//                DbxEntry.File downloadedFile = client.getFile("/" + file, null,output);
//            } finally {
//                output.close();
//            }
//            InputStream inputStr = new BufferedInputStream(new FileInputStream("file"));
//            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStr);
// 
//            // 2) Prepare HTML settings
//            HtmlSettings htmlSettings = new HtmlSettings();
// 
//            // 3) Convert WordprocessingMLPackage to HTML
//            OutputStream out = new BufferedOutputStream(new FileOutputStream(new File("out.html")));
//            StringWriter stringWriter = new StringWriter();
//            AbstractHtmlExporter exporter = new HtmlExporterNG2();
//            StreamResult result = new StreamResult(stringWriter);
//            exporter.html(wordMLPackage, result, htmlSettings);
// 
//
//            System.out.println("!!! strwriter to html" + stringWriter.toString() + "Success!!!");
//            inputStr.close();
//            //outHTML.close();
//
//
//            return stringWriter.toString();            
//        }
//        catch(Exception e){
//            Logger.getLogger(e.toString());
//            return "error";
//        }
//        
//    }
       // private ImageService service;
    public byte[] getBuffer() throws FileNotFoundException, DbxException, IOException{
                int DEFAULT_BUFFER_SIZE = 100240;
                byte[] buffer;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            output = new BufferedOutputStream(new FileOutputStream("temp.txt"));
        try {
            DbxEntry.File downloadedFile = client.getFile("/" + patientEHRPath + "/" + "model.pdf", null,output);
        } finally {
            output.close();
        }
        input =   new BufferedInputStream(new FileInputStream("temp.txt"));

            buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            System.out.println("INPUTSTRIAM " + input.toString()); 
            while ((length = input.read(buffer)) > 0) {
                System.out.println("!!!! LENGTH " + length);
            }
        } finally {
//            close(input);
            input.close();
        }

        //facesContext.responseComplete();
        return buffer;
    }
    public StreamedContent getImage() throws IOException, DbxException {
        //FacesContext context = FacesContext.getCurrentInstance();
        ByteArrayInputStream inputArray;
        ByteArrayOutputStream outputArray;

            BufferedInputStream input = null;
            // output = null;
            byte[] data;
            try {
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("temp.txt"));
            try {
                DbxEntry.File downloadedFile = client.getFile("/" + patientEHRPath + "/" + "pic.jpg", null,output);
            } finally {
                output.close();
            }
                input =   new BufferedInputStream(new FileInputStream("temp.txt"));
                outputArray = new ByteArrayOutputStream();
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int length;
                System.out.println("INPUTSTRIAM " + input.toString()); 
                while ((length = input.read(buffer)) > 0) {
                    outputArray.write(buffer, 0, length);
                    System.out.println("!!!! LENGTH " + length);
                }
                data = outputArray.toByteArray();
                //inputArray = new ByteArrayInputStream(data);
            } finally {
                // Gently close streams.
                //close(outputArray);
//                close(input);
                input.close();
            }
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
//            String id = context.getExternalContext().getRequestParameterMap().get("id");
//            Image image = service.find(Long.valueOf(id));
//Image img =new ImageSe();
            return new DefaultStreamedContent(new ByteArrayInputStream (data));
//        }
    }
    



//methods for delete, upload, view
    //reads file and append content to String formated 
    public StringBuilder getFileDB(String file)throws Exception{
        DataOutputStream output = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream("temp.txt")));
        try {
            DbxEntry.File downloadedFile = client.getFile("/" + patientEHRPath + "/" + file, null,output);
        } finally {
            output.close();
        }
        String s;
//        InputStream input = new DataInputStream(
//            new BufferedInputStream(new InputStreamReader(new FileInputStream("temp.txt"), "UTF-8")));
        BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream("temp.txt")));

        //BufferedReader bfr = new BufferedReader(new InputStreamReader(input));
        StringBuilder content = new StringBuilder(100000);
        while((s = bfr.readLine())!= null){
            content.append("\n" + s.trim());
        }
        //input.close();
        return content;
    }
    //reads meta data file and append content to String Unformated XML string for XSL file retrieval 
    private String getFileDBUnformated(String file)throws Exception{
        DataOutputStream output = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream("temp.txt")));
        try {
            DbxEntry.File downloadedFile = client.getFile("/" + patientEHRPath + "/" + file, null,output);
        } finally {
            output.close();
        }
        String s;
//        InputStream input = new DataInputStream(
//            new BufferedInputStream(new FileInputStream("temp.txt")));
        BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream("temp.txt")));
        //BufferedReader bfr = new BufferedReader(new InputStreamReader(input));
        StringBuilder content = new StringBuilder(100000);
        while((s = bfr.readLine())!= null){
//            System.out.println("index starts " + s.indexOf("<") + "\n" + s);
//            if(s.indexOf("<") < 0) continue;
//            s = s.substring(s.indexOf("<"));
            content.append(s.trim());
        }
        //input.close();
//        System.out.println("UN-Formatade before " + content);
//        content = allInline(content);
//                System.out.println("UN-Formatade after " + content);

        return content.toString().replaceAll("ï»¿", "");
    }    

    //saves meta string to file meta
    public void saveMeta(String src) throws FileNotFoundException, DbxException, IOException, Exception{
        File meta = new File(META);
        //System.out.println("!!!!!2! " + this.meta.title);
        PrintWriter output = new PrintWriter(meta);
        output.println(src);
        output.close();
        FileInputStream metaStream = new FileInputStream(meta);
        DbxEntry.File uploadeMeta = client.uploadFile("/" + patientEHRPath + "/" + META,
            DbxWriteMode.force(), meta.length(), metaStream);
        //deleteTempMeta();
    }    
    public void saveNewHl7(String src, Patient p, String date) throws FileNotFoundException, DbxException, IOException, Exception{
        File hl7 = new File(p.ln + date + ".xml");
        //System.out.println("!!!!!2! " + this.meta.title);
        PrintWriter output = new PrintWriter(hl7);
        output.println(src);
        output.close();
        FileInputStream hl7Stream = new FileInputStream(hl7);
        DbxEntry.File uploadeMeta = client.uploadFile("/" + patientEHRPath + "/" + p.ln + date + ".xml",
            DbxWriteMode.force(), hl7.length(), hl7Stream);
        //deleteTempMeta();
//                meta = new MetaData();
//        meta.title = p.lastName + date + ".xml"; 
//        meta.type = ".xml";
//        meta.format = String.valueOf(hl7.length());
//        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
//        Date dateobj = new Date();
//        meta.date = df.format(dateobj);
        meta.format = String.valueOf(hl7.length());
        saveMeta((getFileDB(META).append("\n" + toXML())).toString());
    } 
    public void saveNewHl7(String src, Employee em, String date) throws FileNotFoundException, DbxException, IOException, Exception{
        File hl7 = new File(em.ln + date + ".xml");
        //System.out.println("!!!!!2! " + this.meta.title);
        PrintWriter output = new PrintWriter(hl7);
        output.println(src);
        output.close();
        FileInputStream hl7Stream = new FileInputStream(hl7);
        DbxEntry.File uploadeMeta = client.uploadFile("/" + patientEHRPath + "/" + em.ln + date + ".xml",
            DbxWriteMode.force(), hl7.length(), hl7Stream);
        //deleteTempMeta();
//                meta = new MetaData();
//        meta.title = d.lastName + date + ".xml"; 
//        meta.type = ".xml";
//        meta.format = String.valueOf(hl7.length());
//        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
//        Date dateobj = new Date();
//        meta.date = df.format(dateobj);
        saveMeta((getFileDB(META).append("\n" + toXML())).toString());
        
    } 
    //From local comp
    public void connectDBfromLocal(String acc){
        try {//connect to DB
            //msg = "";
            config = new DbxRequestConfig("EHR", Locale.getDefault().toString());
            client = new DbxClient(config, acc);
            //String str = "Linked to DropBox account: " + client.getAccountInfo().displayName;
            System.out.println("Linked to DropBox account: " + client.getAccountInfo().displayName);
            connectedToDB = true;
            //msg = "connected";
            //p.error = false;
        } catch (DbxException ex) { 
            //connectedToDB = false;
            System.out.println("Not connected: Wrong access token. Please see 'HELP' for instructions.");
        }        
    } 
    public void saveNewHl7FromLocal(String src, String ln, String date) {
        File hl7 = new File(ln + date + ".xml");
        //System.out.println("!!!!!2! " + this.meta.title);
        try{
            PrintWriter output = new PrintWriter(hl7);
            output.println(src);
            output.close();
            FileInputStream hl7Stream = new FileInputStream(hl7);
            DbxEntry.File uploadeMeta = client.uploadFile("/" + patientEHRPath + "/" + ln + date + ".xml",
                DbxWriteMode.force(), hl7.length(), hl7Stream);
            //deleteTempMeta();
            meta = new MetaData();
            meta.title = ln + date + ".xml"; 
            meta.type = ".xml";
            meta.format = String.valueOf(hl7.length());
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();
            meta.date = df.format(dateobj);
            saveMeta((getFileDB(META).append("\n" + toXML())).toString());            
        }
        catch(Exception ex){
            //System.out.println("Error in DB " + ex);
        }
    } 
   
    //create XML/Dublin Core meta data for a file
    public String toXML() throws Exception{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("metadata");
        doc.appendChild(rootElement);
        rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:dc", "http://purl.org/dc/elements/1.1/");

        Element title = doc.createElement("Title");
        title.appendChild(doc.createTextNode(this.meta.title));
        rootElement.appendChild(title);

        Element creator = doc.createElement("Creator");
        creator.appendChild(doc.createTextNode(this.meta.creator));
        rootElement.appendChild(creator);

        Element subject = doc.createElement("Subject");
        subject.appendChild(doc.createTextNode(this.meta.subject));
        rootElement.appendChild(subject);

        Element descript = doc.createElement("Description");
        descript.appendChild(doc.createTextNode(this.meta.description));
        rootElement.appendChild(descript);

        Element publisher = doc.createElement("Publisher");
        publisher.appendChild(doc.createTextNode(this.meta.publisher));
        rootElement.appendChild(publisher);

        Element contrib = doc.createElement("Contributor");
        contrib.appendChild(doc.createTextNode(this.meta.contributor));
        rootElement.appendChild(contrib);

        Element date = doc.createElement("Date");
        date.appendChild(doc.createTextNode(this.meta.date));
        rootElement.appendChild(date);

        Element type = doc.createElement("Type");
        type.appendChild(doc.createTextNode(this.meta.type));
        rootElement.appendChild(type);

        Element format = doc.createElement("Format");
        format.appendChild(doc.createTextNode(this.meta.format));
        rootElement.appendChild(format);

        Element ident = doc.createElement("Identifier");
        ident.appendChild(doc.createTextNode(this.meta.identifier));
        rootElement.appendChild(ident);

        Element source = doc.createElement("Source");
        source.appendChild(doc.createTextNode(this.meta.source));
        rootElement.appendChild(source);

        Element lang = doc.createElement("Language");
        lang.appendChild(doc.createTextNode(this.meta.lang));
        rootElement.appendChild(lang);

        Element relation = doc.createElement("Relation");
        relation.appendChild(doc.createTextNode(this.meta.relation));
        rootElement.appendChild(relation);

        Element cover = doc.createElement("Coverage");
        cover.appendChild(doc.createTextNode(this.meta.coverage));
        rootElement.appendChild(cover);

        Element rights = doc.createElement("Rights");
        rights.appendChild(doc.createTextNode(this.meta.rights));
        rootElement.appendChild(date);
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource src = new DOMSource(doc);

        StreamResult res = new StreamResult(new StringWriter());
        transformer.transform(src, res);

        return (res.getWriter().toString());//save to string XML
    }    
    public void newHL7(Employee em, String date, String bp, String gl, String temp) throws Exception{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("ClinicalDocument");
        ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"CDA.xsl\"");
        doc.appendChild(rootElement);
        doc.insertBefore(pi, rootElement);
        rootElement.setAttribute("xmlns", "urn:hl7-org:v3");
        rootElement.setAttribute("xmlns:n1", "urn:hl7-org:v3");
        rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttribute("xmlns:voc", "urn:hl7-org:v3/voc");
        rootElement.setAttribute("xsi:schemaLocation", "urn:hl7-org:v3 CDA.xsd");
        
        Element tempId1 = doc.createElement("templateId");
        rootElement.appendChild(tempId1);
        tempId1.setAttribute("extension", "HL7 General Header Constraints");
        tempId1.setAttribute("root", "2.16.840.1.113883.10.20.3");
        
        Element id = doc.createElement("id");
        rootElement.appendChild(id);
        id.setAttribute("root", em.ln);
        id.setAttribute("extension", date);
        
        Element code = doc.createElement("code");
        rootElement.appendChild(code);
        code.setAttribute("code", "34117-2");
        code.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        code.setAttribute("codeSystemName", "LOINC");
        code.setAttribute("displayName", "History and Physical");
        
        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode("Physical exam " + date));
        rootElement.appendChild(title);
        
        Element time = doc.createElement("effectiveTime");
        rootElement.appendChild(time);
        time.setAttribute("value", date);
        
        Element conf = doc.createElement("confidentialityCode");
        rootElement.appendChild(conf);
        conf.setAttribute("code", "N");
        conf.setAttribute("codeSystem", "2.16.840.1.113883.5.25");
        
        Element lang = doc.createElement("languageCode");
        rootElement.appendChild(lang);
        lang.setAttribute("code", "en-US");
        
        Element recordTarget = doc.createElement("recordTarget");
        rootElement.appendChild(recordTarget);
            
            Element patientRole = doc.createElement("patientRole");
            recordTarget.appendChild(patientRole);
            
                Element patId = doc.createElement("id");
                patientRole.appendChild(patId);
//                patId.setAttribute("extension", em.DOB);
                patId.setAttribute("root", em.ln);
                
                Element addr = doc.createElement("addr");
                patientRole.appendChild(addr);               
                    Element street = doc.createElement("streetAddressLine");
                    street.appendChild(doc.createTextNode(em.address));
                    addr.appendChild(street);
                    Element city = doc.createElement("city");
                    city.appendChild(doc.createTextNode(em.city));
                    addr.appendChild(city);
                    Element state = doc.createElement("state");
                    state.appendChild(doc.createTextNode(em.state));
                    addr.appendChild(state);
                    Element zip = doc.createElement("postalCode");
                    zip.appendChild(doc.createTextNode(em.zip));
                    addr.appendChild(zip);
                Element telecom =doc.createElement("telecom");
                patientRole.appendChild(telecom);
                telecom.setAttribute("nullFlavor", "UNK");
                telecom.setAttribute("use", "HP");
                Element patient = doc.createElement("patient");
                patientRole.appendChild(patient);
                    Element name = doc.createElement("name");
                    patient.appendChild(name);
                        Element given = doc.createElement("given");
                        given.appendChild(doc.createTextNode(em.fn));
                        name.appendChild(given);
                        Element family = doc.createElement("family");
                        family.appendChild(doc.createTextNode(em.ln));
                        name.appendChild(family);
                        
                    Element administrativeGenderCode = doc.createElement("administrativeGenderCode");
                    patient.appendChild(administrativeGenderCode);
                    String gender = (em.gender.equals("male")) ? "M":"F";
                    administrativeGenderCode.setAttribute("code", gender);
                    administrativeGenderCode.setAttribute("codeSystem", "2.16.840.1.113883.5.1");
                    
//                    if(d.DOB.length() == 10){
//                        Element birhtTime = doc.createElement("birthTime");
//                        patient.appendChild(birhtTime);
//                        String bd = d.DOB.substring(6);
//                        bd += d.DOB.substring(0, 2);
//                        bd += d.DOB.substring(3, 5);
//                        birhtTime.setAttribute("value", bd);       
//                    }

        
        Element component = doc.createElement("component");
        rootElement.appendChild(component);
            Element structuredbody = doc.createElement("structuredBody");
            component.appendChild(structuredbody);
                Element component2 = doc.createElement("component");
                structuredbody.appendChild(component2);
                    Element section = doc.createElement("section");
                    component2.appendChild(section);
                        Element id2 = doc.createElement("id");
                        section.appendChild(id2);
                        id2.setAttribute("root", "1.3.6.1.4.1.21367.2010.1.2.777");
                        id2.setAttribute("extension", "CodedVitalSigns_" + date);
                        Element code2 = doc.createElement("code");
                        section.appendChild(code2);
                        code2.setAttribute("code", "8716-3");
                        code2.setAttribute("displayName", "VITAL SIGNS");
                        code2.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
                        code2.setAttribute("codeSystemName", "LOINC");
                        Element title2 = doc.createElement("title");
                        title2.appendChild(doc.createTextNode("Coded Vital Signs"));
                        section.appendChild(title2);
                        Element text = doc.createElement("text");
                        text.appendChild(doc.createTextNode("Initial Evaluation: BP: " + bp + "  Glucose: " + gl +
                                "   Temp: " + temp));
                        section.appendChild(text);
                        
                    

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource src = new DOMSource(doc);

        StreamResult res = new StreamResult(new StringWriter());
        transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        transformer.transform(src, res);
        System.out.println(res.getWriter());
        saveNewHl7(res.getWriter().toString(), em, date);
        //return (res.getWriter().toString());//save to string XML
    }        
    public void newHL7(Patient p, String date, String bp, String gl, String temp) throws Exception{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("ClinicalDocument");
        ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"CDA.xsl\"");
        doc.appendChild(rootElement);
        doc.insertBefore(pi, rootElement);
        rootElement.setAttribute("xmlns", "urn:hl7-org:v3");
        rootElement.setAttribute("xmlns:n1", "urn:hl7-org:v3");
        rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttribute("xmlns:voc", "urn:hl7-org:v3/voc");
        rootElement.setAttribute("xsi:schemaLocation", "urn:hl7-org:v3 CDA.xsd");
        
        Element tempId1 = doc.createElement("templateId");
        rootElement.appendChild(tempId1);
        tempId1.setAttribute("extension", "HL7 General Header Constraints");
        tempId1.setAttribute("root", "2.16.840.1.113883.10.20.3");
        
        Element id = doc.createElement("id");
        rootElement.appendChild(id);
        id.setAttribute("root", p.ln);
        id.setAttribute("extension", date);
        
        Element code = doc.createElement("code");
        rootElement.appendChild(code);
        code.setAttribute("code", "34117-2");
        code.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        code.setAttribute("codeSystemName", "LOINC");
        code.setAttribute("displayName", "History and Physical");
        
        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode("Physical exam " + date));
        rootElement.appendChild(title);
        
        Element time = doc.createElement("effectiveTime");
        rootElement.appendChild(time);
        time.setAttribute("value", date);
        
        Element conf = doc.createElement("confidentialityCode");
        rootElement.appendChild(conf);
        conf.setAttribute("code", "N");
        conf.setAttribute("codeSystem", "2.16.840.1.113883.5.25");
        
        Element lang = doc.createElement("languageCode");
        rootElement.appendChild(lang);
        lang.setAttribute("code", "en-US");
        
        Element recordTarget = doc.createElement("recordTarget");
        rootElement.appendChild(recordTarget);
            
            Element patientRole = doc.createElement("patientRole");
            recordTarget.appendChild(patientRole);
            
                Element patId = doc.createElement("id");
                patientRole.appendChild(patId);
                patId.setAttribute("extension", p.getDobString());
                patId.setAttribute("root", p.ln);
                
                Element addr = doc.createElement("addr");
                patientRole.appendChild(addr);               
                    Element street = doc.createElement("streetAddressLine");
                    street.appendChild(doc.createTextNode(p.address));
                    addr.appendChild(street);
                    Element city = doc.createElement("city");
                    city.appendChild(doc.createTextNode(p.city));
                    addr.appendChild(city);
                    Element state = doc.createElement("state");
                    state.appendChild(doc.createTextNode(p.state));
                    addr.appendChild(state);
                    Element zip = doc.createElement("postalCode");
                    zip.appendChild(doc.createTextNode(p.zip));
                    addr.appendChild(zip);
                Element telecom =doc.createElement("telecom");
                patientRole.appendChild(telecom);
                telecom.setAttribute("nullFlavor", "UNK");
                telecom.setAttribute("use", "HP");
                Element patient = doc.createElement("patient");
                patientRole.appendChild(patient);
                    Element name = doc.createElement("name");
                    patient.appendChild(name);
                        Element given = doc.createElement("given");
                        given.appendChild(doc.createTextNode(p.fn));
                        name.appendChild(given);
                        Element family = doc.createElement("family");
                        family.appendChild(doc.createTextNode(p.ln));
                        name.appendChild(family);
                        
                    Element administrativeGenderCode = doc.createElement("administrativeGenderCode");
                    patient.appendChild(administrativeGenderCode);
                    String gender = (p.gender.equals("male")) ? "M":"F";
                    administrativeGenderCode.setAttribute("code", gender);
                    administrativeGenderCode.setAttribute("codeSystem", "2.16.840.1.113883.5.1");
                    
//                    if(p.DOB.length() == 10){
//                        Element birhtTime = doc.createElement("birthTime");
//                        patient.appendChild(birhtTime);
//                        String bd = p.DOB.substring(6);
//                        bd += p.DOB.substring(0, 2);
//                        bd += p.DOB.substring(3, 5);
//                        birhtTime.setAttribute("value", bd);       
//                    }
                        Element birhtTime = doc.createElement("birthTime");
                        patient.appendChild(birhtTime);
                        birhtTime.setAttribute("value", p.getDobString());    

        
        Element component = doc.createElement("component");
        rootElement.appendChild(component);
            Element structuredbody = doc.createElement("structuredBody");
            component.appendChild(structuredbody);
                Element component2 = doc.createElement("component");
                structuredbody.appendChild(component2);
                    Element section = doc.createElement("section");
                    component2.appendChild(section);
                        Element id2 = doc.createElement("id");
                        section.appendChild(id2);
                        id2.setAttribute("root", "1.3.6.1.4.1.21367.2010.1.2.777");
                        id2.setAttribute("extension", "CodedVitalSigns_" + date);
                        Element code2 = doc.createElement("code");
                        section.appendChild(code2);
                        code2.setAttribute("code", "8716-3");
                        code2.setAttribute("displayName", "VITAL SIGNS");
                        code2.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
                        code2.setAttribute("codeSystemName", "LOINC");
                        Element title2 = doc.createElement("title");
                        title2.appendChild(doc.createTextNode("Coded Vital Signs"));
                        section.appendChild(title2);
                        Element text = doc.createElement("text");
                        text.appendChild(doc.createTextNode("Initial Evaluation: BP: " + bp + "  Glucose: " + gl +
                                "   Temp: " + temp));
                        section.appendChild(text);
                        
                    

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource src = new DOMSource(doc);

        StreamResult res = new StreamResult(new StringWriter());
        transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        transformer.transform(src, res);
        System.out.println(res.getWriter());
        saveNewHl7(res.getWriter().toString(), p, date);
        //return (res.getWriter().toString());//save to string XML
    }    
    public void newRecord(Patient p, hl7 hl, Employee em, String date) throws Exception{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("ClinicalDocument");
        ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"CDA.xsl\"");
        doc.appendChild(rootElement);
        doc.insertBefore(pi, rootElement);
        rootElement.setAttribute("xmlns", "urn:hl7-org:v3");
        rootElement.setAttribute("xmlns:n1", "urn:hl7-org:v3");
        rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttribute("xmlns:voc", "urn:hl7-org:v3/voc");
        rootElement.setAttribute("xsi:schemaLocation", "urn:hl7-org:v3 CDA.xsd");
        
        Element tempId1 = doc.createElement("templateId");
        rootElement.appendChild(tempId1);
        tempId1.setAttribute("extension", "HL7 General Header Constraints");
        tempId1.setAttribute("root", "2.16.840.1.113883.10.20.3");
        
        Element id = doc.createElement("id");
        rootElement.appendChild(id);
        id.setAttribute("root", p.ln);
        id.setAttribute("extension", date);
        
        Element code = doc.createElement("code");
        rootElement.appendChild(code);
        code.setAttribute("code", "34117-2");
        code.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
        code.setAttribute("codeSystemName", "LOINC");
        code.setAttribute("displayName", "History and Physical");
        
        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode("Physical exam " + p.ln + " " + date));
        rootElement.appendChild(title);
        
        Element time = doc.createElement("effectiveTime");
        rootElement.appendChild(time);
        time.setAttribute("value", date);
        
        Element conf = doc.createElement("confidentialityCode");
        rootElement.appendChild(conf);
        conf.setAttribute("code", "N");
        conf.setAttribute("codeSystem", "2.16.840.1.113883.5.25");
        
        Element lang = doc.createElement("languageCode");
        rootElement.appendChild(lang);
        lang.setAttribute("code", "en-US");
        
        Element recordTarget = doc.createElement("recordTarget");
        rootElement.appendChild(recordTarget);
            
            Element patientRole = doc.createElement("patientRole");
            recordTarget.appendChild(patientRole);
            
                Element patId = doc.createElement("id");
                patientRole.appendChild(patId);
                patId.setAttribute("extension", p.getDobString());
                patId.setAttribute("root", p.email);
                
                Element addr = doc.createElement("addr");
                patientRole.appendChild(addr);               
                    Element street = doc.createElement("streetAddressLine");
                    street.appendChild(doc.createTextNode(p.address));
                    addr.appendChild(street);
                    Element city = doc.createElement("city");
                    city.appendChild(doc.createTextNode(p.city));
                    addr.appendChild(city);
                    Element state = doc.createElement("state");
                    state.appendChild(doc.createTextNode(p.state));
                    addr.appendChild(state);
                    Element zip = doc.createElement("postalCode");
                    zip.appendChild(doc.createTextNode(p.zip));
                    addr.appendChild(zip);
                Element telecom =doc.createElement("telecom");
                patientRole.appendChild(telecom);
                telecom.setAttribute("value", p.phone);
                telecom.setAttribute("use", "HP");
                Element patient = doc.createElement("patient");
                patientRole.appendChild(patient);
                    Element name = doc.createElement("name");
                    patient.appendChild(name);
                        Element given = doc.createElement("given");
                        given.appendChild(doc.createTextNode(p.fn));
                        name.appendChild(given);
                        Element family = doc.createElement("family");
                        family.appendChild(doc.createTextNode(p.ln));
                        name.appendChild(family);
                        
                    Element administrativeGenderCode = doc.createElement("administrativeGenderCode");
                    patient.appendChild(administrativeGenderCode);
                    String gender = (p.gender.equals("male")) ? "M":"F";
                    administrativeGenderCode.setAttribute("code", gender);
                    administrativeGenderCode.setAttribute("codeSystem", "2.16.840.1.113883.5.1");
                    
//                    if(p.DOB.length() == 10){
//                        Element birhtTime = doc.createElement("birthTime");
//                        patient.appendChild(birhtTime);
//                        String bd = p.DOB.substring(6);
//                        bd += p.DOB.substring(0, 2);
//                        bd += p.DOB.substring(3, 5);
//                        birhtTime.setAttribute("value", bd);       
//                    }
                        Element birhtTime = doc.createElement("birthTime");
                        patient.appendChild(birhtTime);
                        birhtTime.setAttribute("value", p.getDobString());

        
        Element author = doc.createElement("author");
        rootElement.appendChild(author);
            
            Element assignedAuthor = doc.createElement("assignedAuthor");
            author.appendChild(assignedAuthor);
            
                Element addrAuth = doc.createElement("addr");
                assignedAuthor.appendChild(addrAuth);               
                    Element streetAuth = doc.createElement("streetAddressLine");
                    streetAuth.appendChild(doc.createTextNode(em.address));
                    addrAuth.appendChild(streetAuth);
                    Element cityAuth = doc.createElement("city");
                    cityAuth.appendChild(doc.createTextNode(em.city));
                    addrAuth.appendChild(cityAuth);
                    Element stateAuth = doc.createElement("state");
                    stateAuth.appendChild(doc.createTextNode(em.state));
                    addrAuth.appendChild(stateAuth);
                    Element zipAuth = doc.createElement("postalCode");
                    zipAuth.appendChild(doc.createTextNode(em.zip));
                    addrAuth.appendChild(zipAuth);
                Element telecomAuth =doc.createElement("telecom");
                assignedAuthor.appendChild(telecomAuth);
                telecomAuth.setAttribute("use", "WP");
                telecomAuth.setAttribute("value", em.phone);

                Element assignedPerson = doc.createElement("assignedPerson");
                assignedAuthor.appendChild(assignedPerson);
                    Element nameAuth = doc.createElement("name");
                    assignedPerson.appendChild(nameAuth);
                        Element prefix = doc.createElement("prefix");
                        prefix.appendChild(doc.createTextNode("Dr."));
                        nameAuth.appendChild(prefix);
                        Element givenAuth = doc.createElement("given");
                        givenAuth.appendChild(doc.createTextNode(em.fn));
                        nameAuth.appendChild(givenAuth);
                        Element familyAuth = doc.createElement("family");
                        familyAuth.appendChild(doc.createTextNode(em.ln));
                        nameAuth.appendChild(familyAuth);
                        
                  
                    
        Element component1 = doc.createElement("component");
        rootElement.appendChild(component1);
            Element structuredbody = doc.createElement("structuredBody");
            component1.appendChild(structuredbody);

                Element component2 = doc.createElement("component");
                structuredbody.appendChild(component2);
                    Element section2 = doc.createElement("section");
                    component2.appendChild(section2);
//                        Element id2 = doc.createElement("id");
//                        section2.appendChild(id2);
//                        id2.setAttribute("root", "1.3.6.1.4.1.21367.2010.1.2.777");
//                        id2.setAttribute("extension", "CodedVitalSigns_" + date);
//                        Element code12 = doc.createElement("code");
//                        section2.appendChild(code12);
//                        code12.setAttribute("code", "8716-3");
//                        code12.setAttribute("displayName", "VITAL SIGNS");
//                        code12.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
//                        code12.setAttribute("codeSystemName", "LOINC");
                        Element title2 = doc.createElement("title");
                        title2.appendChild(doc.createTextNode("Chief Complaint"));
                        section2.appendChild(title2);
                        Element text2 = doc.createElement("text");
                        text2.appendChild(doc.createTextNode(hl.complaint));
                        section2.appendChild(text2);   
                        
                Element component3 = doc.createElement("component");
                structuredbody.appendChild(component3);
                    Element section3 = doc.createElement("section");
                    component3.appendChild(section3);
                        Element title3 = doc.createElement("title");
                        title3.appendChild(doc.createTextNode("History of Present Illness"));
                        section3.appendChild(title3);
                        Element text3 = doc.createElement("text");
                        text3.appendChild(doc.createTextNode(hl.presentIllness));
                        section3.appendChild(text3);                         

                Element component4 = doc.createElement("component");
                structuredbody.appendChild(component4);
                    Element section4 = doc.createElement("section");
                    component4.appendChild(section4);
                        Element title4 = doc.createElement("title");
                        title4.appendChild(doc.createTextNode("History of Past Illness"));
                        section4.appendChild(title4);
                        Element text4 = doc.createElement("text");
                        text4.appendChild(doc.createTextNode(hl.pastIllnes));
                        section4.appendChild(text4); 

                Element component5 = doc.createElement("component");
                structuredbody.appendChild(component5);
                    Element section5 = doc.createElement("section");
                    component5.appendChild(section4);
                        Element title5 = doc.createElement("title");
                        title5.appendChild(doc.createTextNode("Coded History of Infection"));
                        section5.appendChild(title5);
                        Element text5 = doc.createElement("text");
                        text5.appendChild(doc.createTextNode(hl.infection));
                        section5.appendChild(text5);                        
                        
                Element component6 = doc.createElement("component");
                structuredbody.appendChild(component4);
                    Element section6 = doc.createElement("section");
                    component6.appendChild(section4);
                        Element title6 = doc.createElement("title");
                        title6.appendChild(doc.createTextNode("Pregnancy History"));
                        section6.appendChild(title6);
                        Element text6 = doc.createElement("text");
                        text6.appendChild(doc.createTextNode(hl.pregnancy));
                        section6.appendChild(text6);
 
                Element component7 = doc.createElement("component");
                structuredbody.appendChild(component7);
                    Element section7 = doc.createElement("section");
                    component7.appendChild(section7);
                        Element title7 = doc.createElement("title");
                        title7.appendChild(doc.createTextNode("Coded Social History"));
                        section7.appendChild(title7);
                        Element text7 = doc.createElement("text");
                        text7.appendChild(doc.createTextNode(hl.socialHistory));
                        section7.appendChild(text7);                        
             
                Element component8 = doc.createElement("component");
                structuredbody.appendChild(component8);
                    Element section8 = doc.createElement("section");
                    component8.appendChild(section8);
                        Element title8 = doc.createElement("title");
                        title8.appendChild(doc.createTextNode("Family History"));
                        section8.appendChild(title8);
                        Element text8 = doc.createElement("text");
                        text8.appendChild(doc.createTextNode(hl.familyHistory));
                        section8.appendChild(text8);                        
                        
                Element component9 = doc.createElement("component");
                structuredbody.appendChild(component9);
                    Element section9 = doc.createElement("section");
                    component9.appendChild(section9);
                        Element title9 = doc.createElement("title");
                        title9.appendChild(doc.createTextNode("Allergies and Adverse Reactions"));
                        section9.appendChild(title9);
                        Element text9 = doc.createElement("text");
                        text9.appendChild(doc.createTextNode(hl.allergy));
                        section9.appendChild(text9);                        
                        
                Element component10 = doc.createElement("component");
                structuredbody.appendChild(component10);
                    Element section10 = doc.createElement("section");
                    component10.appendChild(section10);
                        Element title10 = doc.createElement("title");
                        title10.appendChild(doc.createTextNode("Review of Systems"));
                        section10.appendChild(title10);
                        Element text10 = doc.createElement("text");
                        text10.appendChild(doc.createTextNode(hl.pastIllnes));
                        section10.appendChild(text10);   
                        
                Element component11 = doc.createElement("component");
                structuredbody.appendChild(component11);
                    Element section11 = doc.createElement("section");
                    component11.appendChild(section11);
                        Element title11 = doc.createElement("title");
                        title11.appendChild(doc.createTextNode("Coded Detailed Physical Examination"));
                        section11.appendChild(title11);
                        Element text11 = doc.createElement("text");
                        text11.appendChild(doc.createTextNode(hl.exam));
                        section11.appendChild(text11); 
                        
                Element component12 = doc.createElement("component");
                section11.appendChild(component12);
                    Element section12 = doc.createElement("section");
                    component12.appendChild(section12);
                        Element title12 = doc.createElement("title");
                        title12.appendChild(doc.createTextNode("Coded Vital Signs"));
                        section12.appendChild(title12);
                        Element text12 = doc.createElement("text");
                        text12.appendChild(doc.createTextNode("Initial Evaluation: " + hl.vitals));
                        section12.appendChild(text12);     
                        
                Element component13 = doc.createElement("component");
                structuredbody.appendChild(component13);
                    Element section13 = doc.createElement("section");
                    component13.appendChild(section13);
                        Element title13 = doc.createElement("title");
                        title13.appendChild(doc.createTextNode("Visit Summary"));
                        section13.appendChild(title13);
                        Element text13 = doc.createElement("text");
                        text13.appendChild(doc.createTextNode(hl.visitSummary));
                        section13.appendChild(text13);                       
//                Element component12 = doc.createElement("component");
//                structuredbody.appendChild(component12);
//                    Element section12 = doc.createElement("section");
//                    component12.appendChild(section12);
//                        Element id3 = doc.createElement("id");
//                        section12.appendChild(id3);
//                        id3.setAttribute("root", "1.3.6.1.4.1.21367.2010.1.2.777");
//                        id3.setAttribute("extension", "CodedVitalSigns_" + date);
//                        Element code12 = doc.createElement("code");
//                        section12.appendChild(code12);
//                        code12.setAttribute("code", "8716-3");
//                        code12.setAttribute("displayName", "VITAL SIGNS");
//                        code12.setAttribute("codeSystem", "2.16.840.1.113883.6.1");
//                        code12.setAttribute("codeSystemName", "LOINC");
//                        Element title12 = doc.createElement("title");
//                        title12.appendChild(doc.createTextNode("Coded Vital Signs"));
//                        section12.appendChild(title12);
//                        Element text12 = doc.createElement("text");
//                        text12.appendChild(doc.createTextNode("Initial Evaluation: BP: " + "bp" + "  Glucose: " + "gl" +
//                                "   Temp: " + "temp"));
//                        section12.appendChild(text12);
                        
                    

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource src = new DOMSource(doc);

        StreamResult res = new StreamResult(new StringWriter());
        transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        transformer.transform(src, res);
        System.out.println(res.getWriter());
        saveNewHl7(res.getWriter().toString(), p, date);
        //return (res.getWriter().toString());//save to string XML
    }    
    //get file name and extension from a path
    public void setExtens (String st){
        int l = st.lastIndexOf(".");
        int f = st.lastIndexOf("\\");
        extension = st.substring(l, st.length());
        flName = st.substring(f + 1, st.length());
    }  
    public String getExtens (String st){
        int l = st.lastIndexOf(".");
        int f = st.lastIndexOf("\\");
        extension = st.substring(l, st.length());
        flName = st.substring(f + 1, st.length());
        return extension;
    }   
    //reset user's metadata
    private void deleteTempMeta(){
        meta = new MetaData();
    }    
    //format meta file from string XML to valid XML UNUSED!!!
    private String formatXML(String st) throws TransformerException, XPathExpressionException, SAXException, ParserConfigurationException, IOException{
        Scanner scan = new Scanner(st);
        StringWriter returnXML = new StringWriter() ;
        StringWriter stringWriter;
        while(scan.hasNext()){
            
            String xml = scan.nextLine().trim();
            if(!xml.equals("")){

                Document document = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

                XPath xPath = XPathFactory.newInstance().newXPath();
                NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
                                                              document,
                                                              XPathConstants.NODESET);

                for (int i = 0; i < nodeList.getLength(); ++i) {
                    Node node = nodeList.item(i);
                    node.getParentNode().removeChild(node);
                }

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                stringWriter = new StringWriter();
                StreamResult streamResult = new StreamResult(stringWriter);

                transformer.transform(new DOMSource(document), streamResult);

                System.out.println(stringWriter.toString());
                returnXML.append("\n" + stringWriter.toString());
            }
        }
        return returnXML.toString();
    }     
    //getters/setters
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isConnectedToDB() {
        return connectedToDB;
    }

    public void setConnectedToDB(boolean connectedToDB) {
        this.connectedToDB = connectedToDB;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFlName() {
        return flName;
    }

    public void setFlName(String flName) {
        this.flName = flName;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public MetaData getMeta() {
        return meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }

//    public Part getFileUpload() {
//        return fileUpload;
//    }
//
//    public void setFileUpload(Part fileUpload) {
//        this.fileUpload = fileUpload;
//    }
    

//    public String getView() {
//        return view;
//    }
//
//    public void setView(String view) {
//        this.view = view;
//    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void fileUpload(FileUploadEvent event){
        this.fileUpload = event.getFile();
        addMetaFileInfo();
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
    
}
