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
<%  if (request.getParameter("formato").equals("pdf")){
        response.setContentType("application/pdf");
        //response.setHeader("Content-Disposition",  "inline; filename='report.pdf'");
    } else if (request.getParameter("formato").equals("xls")) {   
        response.setContentType("application/xls");
        //response.setHeader("Content-Disposition",  "inline; filename='report.xls'");
    }            
    /* @page contentType="application/pdf" 
     / response.setHeader("Content-Disposition", "attachment; filename=\"" + request.getParameter("$a") + ".pdf\""); */
    Usuario user = (Usuario) request.getSession().getAttribute("usuario");

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }

    String token;
    StringBuilder debug = new StringBuilder();
    ArrayList<Parametro> definicionParametros = new ArrayList<Parametro>();
    ReporteJasper reporte = new ReporteJasper(Integer.parseInt(request.getParameter("$cr")), user.getCx());

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
        
        JasperExportManager.exportReportToPdfFile(reporte.getPrint(), application.getRealPath("/").replace("\\build", "").concat("//temp//").concat(reporte.getJrxml()).concat(".pdf"));

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
        entrada.close();
        //String name = f.getName().s(jasperPrint, destFileName);ubstring(f.getName().lastIndexOf("/") + 1,f.getName().length());

    } catch (JRException ex) {
        ex.printStackTrace();
        response.getWriter().print(ex.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().print(e.getMessage());
    } finally {
        reporte = null;
    }


%>