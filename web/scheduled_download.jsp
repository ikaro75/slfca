<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Calendar"
%><%@page import="mx.org.fide.mail.Mail"
%><%@page import="java.io.BufferedWriter"
%><%@page import="java.io.FileWriter"
%><%@page import="java.util.ArrayList"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="mx.org.fide.modelo.*"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat"
%><%@page import="java.util.LinkedHashMap"
%><%@page import="java.io.File"%><% 

String error="";
Integer pagina=1;
int forma=0;
String tipoAccion="";
String w = "";
String source="";
LinkedHashMap<String,Campo> campos = new LinkedHashMap<String,Campo>();
ArrayList<ArrayList> registros= new ArrayList<ArrayList>();
Forma f = new Forma();
Calendar cal = Calendar.getInstance();
File archivoDestino = null;
SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddhhmm");

Usuario user=(Usuario)request.getSession().getAttribute("usuario");
if (user == null) {
    request.getRequestDispatcher("/index.jsp");
}

String pk="0";

if (request.getParameter("$pk")!=null)
    pk = request.getParameter("$pk");

if (request.getParameter("$ta")==null)
    throw new Fallo("Falta parámetro $ta");
else  
    tipoAccion=request.getParameter("$ta");
    
if (request.getParameter("$w")!=null)
        w=request.getParameter("$w").replaceAll("\\$inicial=",">=").replaceAll("\\$final=","<=");
    
try {
    if (request.getParameter("$cf")==null)
        throw new Fallo("Falta parámetro $cf");
    else {
        try {
            forma= Integer.parseInt(request.getParameter("$cf"));
            //1. Verifica el folder destino en el servidor
            String sAppPath=application.getRealPath("/").replace("\\build", "");
            File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(user.getClave())).concat("\\"));
            if (!forderDestino.exists()) {
               forderDestino.mkdir();
            } 

            archivoDestino = new File(forderDestino.getAbsolutePath().concat("\\").concat(request.getParameter("$file").replace("á","a").replace("é","e").replace("í","i").replace("ó", "o").replace("ú","u").concat(" ").concat(sd.format(cal.getTime()))).concat(".xls"));

            if (!archivoDestino.exists()) {
               archivoDestino.createNewFile();
            }
            
            f = new Forma(new Consulta(forma,tipoAccion, String.valueOf(pk), w, archivoDestino.getAbsoluteFile().toString(),user), false);
        }
        catch (Exception e) { 
            Mail userMail = new Mail(user);
            userMail.sendEmail("noresponder@fide.org.mx",
                request.getParameter("email"),
                "Error al generar descarga de excel programada",
                "<img src='http://ahorrateunaluz.org.mx:8080/slfca/img/fide_banner.png'><br><br><div style='font-size:12px; font-family:arial'>Estimado usuario(a)<br><br>Ocurrió un error al generar excel programado :".concat(e.getMessage()),
                "");
            return;
        }
    }

    Mail userMail = new Mail(user);
    userMail.sendEmail("noresponder@fide.org.mx",
        user.getEmail(),
        "Su descarga programada del catálogo '".concat(f.getForma()).concat("' del Sistema de Control y Seguimiento del Programa Nacional de Sustitución de Lámparas Incandescentes por Fluorescentes Compactas Autobalastradas (LFCA) está lista"),
        "<img src='http://ahorrateunaluz.org.mx:8080/slfca/img/fide_banner.png'><br><br><div style='font-size:12px; font-family:arial'>Estimado usuario(a)<br><br>Su descarga ya está disponible en la liga: "
        .concat("<a href='http://ahorrateunaluz.org.mx:8080/slfca/control?$cmd=download&$file=").concat(URLEncoder.encode(archivoDestino.getAbsolutePath(),"UTF-8"))
        .concat("' target='_blank'>http://ahorrateunaluz.org.mx:8080/slfca/control?$cmd=download&$file=".concat(URLEncoder.encode(archivoDestino.getAbsolutePath(),"UTF-8")))
        .concat("</a>"),
        "");

    
} catch (Fallo fa) {
    error= fa.getMessage() + ";" + fa.getStackTrace();
    Mail userMail = new Mail(user);
    userMail.sendEmail("noresponder@fide.org.mx",
        request.getParameter("email"),
        "Error al generar descarga de excel programada",
        "<img src='http://ahorrateunaluz.org.mx:8080/slfca/img/fide_banner.png'><br><br><div style='font-size:12px; font-family:arial'>Estimado usuario(a)<br><br>Ocurrió un error al generar excel programado :".concat(error),
        "");
    
} catch (Exception e) {
    error= e.getMessage();
    Mail userMail = new Mail(user);
    userMail.sendEmail("noresponder@fide.org.mx",
        request.getParameter("email"),
        "Error al generar descarga de excel programada",
        "<img src='http://ahorrateunaluz.org.mx:8080/slfca/img/fide_banner.png'><br><br><div style='font-size:12px; font-family:arial'>Estimado usuario(a)<br><br>Ocurrió un error al generar excel programado :".concat(error),
        "");    
} finally { 
     if (error==null) error="";
     if (!error.equals("")) {
       return; }
}

/* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
request.getSession().setAttribute("usuario", user); 
%>