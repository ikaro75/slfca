<%@page import="net.sf.jasperreports.engine.JRExporter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="net.sf.jasperreports.engine.JasperExportManager"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%> 
<%@page import="net.sf.jasperreports.engine.JRException"%>
<%@page import="net.sf.jasperreports.engine.JRExporterParameter"%>
<%@page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@page import="net.sf.jasperreports.engine.JasperReport"%>
<%@page import="net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter"%>
<%@page import="net.sf.jasperreports.engine.export.JRXlsExporter"%>
<%@page import="net.sf.jasperreports.engine.export.JRXlsExporterParameter"%>
<%@page import="net.sf.jasperreports.engine.util.JRLoader"%>
<%@page import="net.sf.jasperreports.engine.export.JRPdfExporter"%>
<%@page import="net.sf.jasperreports.engine.export.JRPdfExporterParameter"%>
<%@page import="mx.org.fide.modelo.*"%>
<%@page import="mx.org.fide.jasper.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.InputStream"%>
<% 
   Usuario user = (Usuario) request.getSession().getAttribute("usuario");

   if (user == null) {
        request.getRequestDispatcher("/index.jsp");
   }
   
   OutputStream ouputStream =  response.getOutputStream();
   JRExporter exporter = null;
   String token;
   StringBuilder debug = new StringBuilder();
   ReporteJasper reporte = new ReporteJasper(Integer.parseInt(request.getParameter("$cr")),user.getCx());
 
   
   if ("pdf".equalsIgnoreCase(request.getParameter("formato"))) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename='".concat((request.getParameter("$na")==null?reporte.getJrxml():request.getParameter("$na"))).concat(".pdf").concat("'"));
        exporter = new  JRPdfExporter();
    } else if ("excel".equalsIgnoreCase(request.getParameter("formato"))) {
        response.setContentType("application/xls");
        response.setHeader("Content-Disposition", "inline; filename='".concat((request.getParameter("$na")==null?reporte.getJrxml():request.getParameter("$na"))).concat(".xls").concat("'"));
        exporter = new  JRXlsExporter();       
           }
    
    if (request.getParameter("$p") != null) {

        for (int i = 0; i < request.getParameterValues("$p").length; i++) {
            /* Si el parámetro empieza  con i% o m% debe de pasarse a valor numérico el segundo token */
            token = request.getParameterValues("$p")[i];
            if (token.split("=")[0].startsWith("i|") || token.split("=")[0].startsWith("m|")) {
                String parametro;
                Integer valor;

                try {
                    valor = Integer.parseInt(token.split("=")[1]);
                } catch (Exception e) {
                    throw new Fallo("El valor del parámetro ".concat(token.split("=")[0]).concat(" no es válido, verifique"));
                }
                parametro = token.split("=")[0].substring(2);
                reporte.getParametros().put(parametro, valor);
            } else if (token.split("=")[0].startsWith("d|")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date theDate = formatter.parse(token.split("=")[1]);

                String parametro = token.split("=")[0].substring(2);
                java.sql.Date valor = new java.sql.Date(theDate.getTime());
                reporte.getParametros().put(parametro, valor);
            } else {
                String parametro = token.split("=")[0].substring(2);
                String valor = token.split("=")[1];
                reporte.getParametros().put(parametro, valor);
            }
        }
    }
      
    try {
        //Verifica si existe el reporte 
        File archivoReporte = new File("C:\\reportes\\".concat(reporte.getJrxml()).concat(".jasper"));

        if (!archivoReporte.exists()) {
            debug.append("<debug>El archivo ").append("C:\\reportes\\").append(reporte.getJrxml()).append(".jasper").append(" no se encuentra</debug>");
        }

        
       reporte.setPrint(user.getCx());       
       exporter.setParameter(JRExporterParameter.JASPER_PRINT,  reporte.getJp());
       exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,  ouputStream);
       exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.FALSE); // delete records the bottom of the blank lines
       exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.FALSE);
       exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE) ;// remove the extra ColumnHeader
       exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE) ;// display a border
       exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.FALSE) ;
       exporter.exportReport();
       
       /* JasperExportManager.exportReportToPdfFile(reporte.getPrint(), application.getRealPath("/").replace("\\build", "").concat("//temp//").concat(reporte.getJrxml()).concat(".pdf"));
        archivoReporte = new File(application.getRealPath("/").replace("\\build\\web", "").concat("//temp//").concat(reporte.getJrxml()).concat(".pdf"));
        if (!archivoReporte.exists()) {
            debug.append("<debug>El archivo ").append(application.getRealPath("/").replace("\\build", "").concat("//temp//").concat(reporte.getJrxml()).concat(".pdf")).append(" no se encuentra</debug>");
        }

        InputStream entrada = new FileInputStream(application.getRealPath("/").replace("\\build", "").concat("//temp//").concat(reporte.getJrxml()).concat(".pdf"));

        byte[] lectura = new byte[entrada.available()];
        entrada.read(lectura);
        response.setContentLength(lectura.length);
        response.getOutputStream().write(lectura);
        response.getOutputStream().flush();
        response.getOutputStream().close();
        //response.getWriter().print(debug);
        entrada.close();*/
        //String name = f.getName().s(jasperPrint, destFileName);ubstring(f.getName().lastIndexOf("/") + 1,f.getName().length());

    } catch  (JRException e) {
        throw new  ServletException(e);
    } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().print(e.getMessage());
    } finally {
        reporte = null;
        if (ouputStream !=  null)  {
            try        {
            ouputStream.close();
        }
            catch (IOException  ex){
                System.out.print(ex.getMessage());
            }
        }        
    }


%>