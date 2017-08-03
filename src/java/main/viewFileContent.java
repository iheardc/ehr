package main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author degla
 */
@WebServlet(description = "TestServlet Description", urlPatterns = { "/ehr/viewFileContent" })
public class viewFileContent extends HttpServlet {
    
    @Inject
    patientBean pBean;
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("OUTPUT CONTENT META pat  !!! innnnn " );
        //BufferedInputStream input = null;
//        DbxClient client = userBean.getDb().client;
        String file = pBean.getFlName();
        String mime = pBean.getMime();
//        String ext = userBean.getDb().getExtens(file);
        BufferedOutputStream output; 
        ByteArrayOutputStream outArr = null;

        try {
            //outArr = userBean.db.getByteArrayOut(file, client);
            outArr = pBean.showContent();System.out.println("OUTPUT CONTENT META pat  !!! " + mime + outArr.size());
        } catch (Exception ex) {
//            Logger.getLogger(viewFile.class.getName()).log(Level.SEVERE, null, ex);
        } 
        response.reset();
        response.setHeader("Content-Type", mime);
        response.setHeader("Content-Length", Integer.toString(outArr.size()));
        response.setHeader("Content-Disposition", "inline; filename=" + file);
        output = new BufferedOutputStream(response.getOutputStream());

        outArr.writeTo(output);
        output.flush();
        output.close();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                System.out.println("!!!!POST");

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
