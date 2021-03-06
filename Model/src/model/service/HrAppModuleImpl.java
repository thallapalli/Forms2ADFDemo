package model.service;


import model.queries.HrEmployeesImpl;

import model.service.common.HrAppModule;

import oracle.jbo.server.ApplicationModuleImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import oracle.apps.xdo.template.FOProcessor;
import oracle.apps.xdo.template.RTFProcessor;

import oracle.jbo.VariableValueManager;

import oracle.jbo.server.ViewLinkImpl;
import oracle.jbo.server.ViewObjectImpl;

import oracle.xml.parser.v2.XMLNode;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri Aug 10 23:28:25 CDT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class HrAppModuleImpl extends ApplicationModuleImpl implements HrAppModule {
    /**
     * This is the default constructor (do not remove).
     */
    public HrAppModuleImpl() {
    }

    /**
     * Container's getter for HrDepartments.
     * @return HrDepartments
     */
    public ViewObjectImpl getHrDepartments() {
        return (ViewObjectImpl) findViewObject("HrDepartments");
    }

    /**
     * Container's getter for HrEmployees.
     * @return HrEmployees
     */
    public HrEmployeesImpl getHrEmployees() {
        return (HrEmployeesImpl) findViewObject("HrEmployees");
    }

    /**
     * Container's getter for HrLov17.
     * @return HrLov17
     */
    public ViewObjectImpl getHrLov17() {
        return (ViewObjectImpl) findViewObject("HrLov17");
    }

    /**
     * Container's getter for HrDepartmentsEmployees.
     * @return HrDepartmentsEmployees
     */
    public ViewLinkImpl getHrDepartmentsEmployees() {
        return (ViewLinkImpl) findViewLink("HrDepartmentsEmployees");
    }
    XMLNode xmlnode5 = null;    
              
            
            public byte[] getHrReport(String deptId){     
                
                byte[] databytes = null;    
                
                
                try{
                    String str = "ReportTemplates/HR.rtf";
                    File file = new File(str);
                    String actualPathofFile = file.getAbsolutePath();
                    
                    //creating xsl template      
                    RTFProcessor rtfp  = new RTFProcessor(actualPathofFile);
                    ByteArrayOutputStream xslotptstream = new ByteArrayOutputStream();
                    rtfp.setOutput(xslotptstream);
                    rtfp.process();
                    
                    //merge xsl template with vo data to generate report & return outputstream
                    ByteArrayInputStream xslinptstream = new ByteArrayInputStream(xslotptstream.toByteArray());
                    
                    FOProcessor foprocessor = new FOProcessor();
                    ByteArrayInputStream bytedatastream = new ByteArrayInputStream(getXMLDataHr(deptId));
                    foprocessor.setData(bytedatastream);
                    foprocessor.setTemplate(xslinptstream);
                    
                    ByteArrayOutputStream pdfotptstream = new ByteArrayOutputStream();
                    foprocessor.setOutput(pdfotptstream);
                    
                    byte otptfileformat = FOProcessor.FORMAT_PDF;
                    
                    foprocessor.setOutputFormat(otptfileformat);
                    foprocessor.generate();
                    
                    databytes = pdfotptstream.toByteArray();           
                    
                    
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
                
                return databytes;
            }
            
            public byte[] getXMLDataHr(String deptId){    
                
                byte[] databytes = null;
                StringBuffer sb = new StringBuffer();
                String finalXMLData = null;
                
                try{
                    HrEmployeesImpl vo = getHrEmployees();  
                    
                    
                    vo.executeQuery();
                    
                    if(vo.getEstimatedRowCount() > 0){                  
                         
                        
                        xmlnode5 = (XMLNode)vo.writeXML(0, 0L);           
                    
                        if (xmlnode5 != null) {           
                            StringWriter sb1 = new StringWriter();
                            xmlnode5.print(new PrintWriter(sb1));
                            sb.append(sb1.toString());               
                                   
                        }
                    
                    
                        finalXMLData = sb.toString();
                        //System.out.println("final -" + finalXMLData);
                        databytes = finalXMLData.getBytes();
                    }
                    else{
                        sb = new StringBuffer();
                        sb.append("<?xml version='1.0' encoding='utf-8' ?>");
                        sb.append("<Employees>");                
                        sb.append("<EmployeesRow>");                
                        
                        sb.append("<DepartmentId>"+ deptId +"</DepartmentId>");         
                        
                        sb.append("</EmployeesRow>");                
                        sb.append("</Employees>");
                        
                        finalXMLData = sb.toString();
                        databytes = finalXMLData.getBytes();
                        
                        
                        }
                        
                        //System.out.println("final -" + finalXMLData);
                }
                
                
                catch(Exception e){
                    System.out.println("Error :" + e.getMessage());
                }
                
                return databytes;
            }
    
}

