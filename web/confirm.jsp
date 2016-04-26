<%@page import="java.text.SimpleDateFormat"%>
<%@page import="mx.org.fide.modelo.Consulta"%>
<%@page import="mx.org.fide.modelo.Fallo"%>
<%@page import="mx.org.fide.modelo.Conexion"%>
<%@page import="mx.org.fide.agenda.Actividad"%>
<%@page import="mx.org.fide.modelo.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html><%
String mensaje="";  
StringBuilder resultadoXML = new StringBuilder();
String divError="<div style='width: 70%;margin-left: auto ;margin-right: auto;' class='ui-state-error ui-corner-all' style='padding: 0 .7em;'><p><span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span>%mensaje</p></div>";
String divInfo ="<div style='width: 70%;margin-left: auto ;margin-right: auto;' class='ui-state-highlight ui-corner-all' style='margin-top: 20px; padding: 0 .7em;'><p><span class='ui-icon ui-icon-info' style='float: left; margin-right: .3em;'></span>%mensaje</p></div>";

try {    

     String respuesta = "";
     Integer claveActividad = 0;
     Actividad a = null;
     Actividad ac = null;
     Usuario u = new Usuario(); 
     Consulta cnsActividad = null;
     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
     
     if (request.getParameter("$ca") == null || request.getParameter("$ca")=="") {
         throw new Fallo ("Error=No se especificó la clave de la actividad, verifique");
     } 
         
     claveActividad = Integer.parseInt(request.getParameter("$ca"));

     if (request.getParameter("$r") == null || request.getParameter("$r")=="") {
            throw new Fallo ("Error=No se especificó la respuesta, verifique");
     }    

     if (request.getParameter("$r").equals("2") || request.getParameter("$r").equals("3")) {
         
     } else {
       throw new Fallo ("Error=Respuesta especificada no válida, verifique");
     }

     Conexion cx = new Conexion();
     u.setCx(cx);
     u.setIp(request.getRemoteAddr());
     u.setNavegador(request.getHeader("user-agent"));

     a = new Actividad(claveActividad, u);
     u.setClave(a.getClaveEmpleadoAsignado());
                    
     //Se cambia el estatus de la actividad a "confirmado" o "cancelado" 
     cnsActividad = new Consulta(101, "update", claveActividad.toString() , "",null, u);
     cnsActividad.getCampos().get("actividad").setValor(a.getActividad());
     cnsActividad.getCampos().get("clave_estatus").setValor(request.getParameter("$r"));
     cnsActividad.getCampos().get("clave_categoria").setValor(a.getClaveCategoria().toString());
     cnsActividad.getCampos().get("clave_empleado_solicitante").setValor(a.getClaveEmpleadoSolicitante().toString());
     cnsActividad.getCampos().get("clave_empleado_asignado").setValor(a.getClaveEmpleadoAsignado().toString());
     cnsActividad.getCampos().get("fecha_inicial").setValor(formatter.format(a.getFechaInicial()));
     cnsActividad.getCampos().get("clave_forma").setValor(String.valueOf(a.getClaveForma()));
     cnsActividad.getCampos().get("clave_registro").setValor(a.getClaveRegistro().toString());
     ac = new Actividad (cnsActividad);
     ac.getCampos().remove("actividad");
     ac.getCampos().remove("clave_categoria");
     ac.getCampos().remove("clave_empleado_solicitante");
     ac.getCampos().remove("clave_empleado_asignado");
     ac.getCampos().remove("fecha_inicial");
     ac.getCampos().remove("fecha_final");
     ac.getCampos().remove("clave_forma");
     
     resultadoXML.append(ac.update());
     
     if (request.getParameter("$r").equals("2")) {
         mensaje="Se ha confirmado su asistencia al evento <strong>".concat(a.getActividad())
                 .concat("</strong><br />Hora: ").concat(formatter.format(a.getFechaInicial())).concat("</br />");
         
         if (a.getLugar()!=null)
                mensaje = mensaje.concat("Lugar: ").concat(a.getLugar());
         
     } else if ((request.getParameter("$r").equals("3"))) {
         mensaje="Se ha cancelado su asistencia al evento <strong>".concat(a.getActividad())
                 .concat("</strong><br />Hora: ").concat(formatter.format(a.getFechaInicial())).concat("</br />");
         
         if (a.getLugar()!=null)
                mensaje = mensaje.concat("Lugar: ").concat(a.getLugar());

     }
     
} catch(NumberFormatException n) {
    mensaje = "Error=Actividad especificada no válida, verifique";
} catch(Exception e) {
    mensaje = e.getMessage();
}   

%><html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Intranet ILCE / Confirme la cita agendada</title>
        <link href="img/ilce.ico" type="image/x-icon" rel="shortcut icon" />
        
        <link type="text/css" href="css/start/jquery.ui.all.css"  rel="stylesheet"/>
    </head>
    <body style="margin:0px; padding: 0px;">
        <div id="banner" style="margin: 0px; padding: 0px" >
            <img src="img/logo_intranet_4.png"  />
    </div>    
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <div style="text-align: center" class="ui-widget"><% if (mensaje.startsWith("Error=")) {%><%=divError.replaceAll("%mensaje", mensaje.replaceAll("Error=", "")) %><% } else { %><%=divInfo.replaceAll("%mensaje", mensaje) %><% } %></div>
    </body>
</html>
