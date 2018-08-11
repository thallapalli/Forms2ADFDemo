package view;



import javax.faces.context.FacesContext;


import oracle.adf.view.rich.component.rich.nav.RichButton;

import oracle.apps.xdo.XDOException;

import oracle.jbo.ApplicationModule;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;
  

    import java.io.OutputStream;

    import java.text.SimpleDateFormat;

    import java.util.Date;

    import javax.el.ELContext;
    import javax.el.ExpressionFactory;
    import javax.el.ValueExpression;

    import javax.faces.application.Application;
    import javax.faces.context.FacesContext;

import model.service.HrAppModuleImpl;

import oracle.adf.view.rich.component.rich.input.RichInputText;
    import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
    import oracle.adf.view.rich.component.rich.output.RichMessage;

    import oracle.apps.xdo.XDOException;

    import oracle.jbo.ApplicationModule;

    import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
    import org.apache.myfaces.trinidad.util.Service;
public class IndexBacking {
    private RichButton cb1;

    public IndexBacking() {
    }

    public String onGenerateReport() {
        // Add event code here...
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExtendedRenderKitService etks = Service.getService(fctx.getRenderKit(), ExtendedRenderKitService.class);

        //id of generate report button
        String id = this.getCb1().getClientId();     //'id' used as param in customHandler javascript function in hr.jspx
                           
                   etks.addScript(fctx, "customHandler('" + id + "');");
        return null;
    }

    private static Object resolveExpression(String expression) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        Application app = fctx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = fctx.getELContext();

        ValueExpression elExpression = elFactory.createValueExpression(elContext, expression, Object.class);

        return elExpression.getValue(elContext);

    }


    private static ApplicationModule getApplicationModuleForDataControl(String name) {
        return (ApplicationModule) resolveExpression("#{data." + name + ".dataProvider}");
    }
    public void HrReport(FacesContext facesContext, OutputStream outputStream) throws XDOException{
                       
                       
               try{
                   HrAppModuleImpl am = (HrAppModuleImpl)this.getApplicationModuleForDataControl("HrAppModuleDataControl");
                  
                          
                   
                               
                   //Method to be called here from applicationModuleImpl class
                   byte[] result = am.getHrReport("90");
                   outputStream.write(result);  
                   facesContext.responseComplete();
               }
               
               catch(Exception e){
                   System.out.println(e.getMessage());
               }       
               
           }

    public void setCb1(RichButton cb1) {
        this.cb1 = cb1;
    }

    public RichButton getCb1() {
        return cb1;
    }
}
