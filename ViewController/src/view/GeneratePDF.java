package view;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.net.URLDecoder;

import java.util.Arrays;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.service.HrAppModuleImpl;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.share.ADFContext;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;

@WebServlet(name = "GeneratePDF", urlPatterns = { "/generatepdf" })
public class GeneratePDF extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    private String filePath;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get requested file by path info.

        String requestedFile = "checklist.pdf"; //test

        // I want to invoke a pdf that is located on the machine where the application is running
        this.filePath = "C:\\JDeveloper\\mywork\\Forms2ADFDemo\\ViewController\\public_html\\resources";
        // Check if file is actually supplied to the request URI.
        if (requestedFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Decode the file name (might contain spaces and on) and prepare file object.

        // Decode the file name (might contain spaces and on) and prepare file object.
        File file = new File(filePath, URLDecoder.decode(requestedFile, "UTF-8"));

        // Check if file actually exists in filesystem.
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Get content type by filename.
        String contentType = getServletContext().getMimeType(file.getName());

        // If content type is unknown, then set the default value.
        // For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
        // To add new content types, add new mime-mapping entry in web.xml.
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Init servlet response.
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "filename=\"" + file.getName() + "\"");

        // Prepare streams.
        BufferedInputStream input = null;
        BufferedOutputStream output = null;


        try {

           // byte[] b = populate(output, response);
          
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);

            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.


            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        
            
            
            
            
        }
        
        
        finally {
            // Gently close streams.
         
            close(output);
            close(input);
        }
    }

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it.
                e.printStackTrace();
            }
        }
    }

    public byte[] populate(BufferedOutputStream output, HttpServletResponse response) throws IOException {

        output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

        BindingContext bc = (BindingContext) ADFContext.getCurrent()
                                                       .getSessionScope()
                                                       .get("data");
        // get the data control based on its name
     
        String appmod = "model.service.HrAppModule";
                   String configname = "HrAppModuleLocal";
                   HrAppModuleImpl am = (HrAppModuleImpl)Configuration.createRootApplicationModule(appmod, configname);
       // HrAppModuleImpl am = (HrAppModuleImpl) dc.getDataProvider();

        byte[] data = am.getHrReport("90"); //TODO change this


       return data;
    
       
        
    }
}
