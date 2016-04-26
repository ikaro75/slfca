<?xml version="1.0" encoding="ISO-8859-1"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="java.util.ArrayList"
%><%@ page import="mx.org.fide.modelo.*"
%><%@ page import="mx.org.fide.sms.*"
%><%@page import="mx.org.fide.cfe.ValidacionBeneficiario"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="java.util.LinkedHashMap"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat"
%><%@page import="java.util.Calendar"%><% 

response.setContentType("text/xml;charset=ISO-8859-1"); 
request.setCharacterEncoding("UTF8");
String resultadoValidacionRPU= "";
String rpu = "";
String idControl="";
String error=""; 
String estatus="";

LinkedHashMap <String,Campo> campos= new LinkedHashMap <String,Campo>();
ArrayList<ArrayList> registros= new ArrayList<ArrayList>();
Forma frmBeneficiario= new Forma();
Forma frmTienda = new Forma();
        
Usuario user=(Usuario)request.getSession().getAttribute("usuario");
ValidacionBeneficiario validacionBeneficiario = new ValidacionBeneficiario();

//1. Validar RPU
try {
    if (request.getParameter("$rpu")==null) {
        estatus="rechazado";
        throw new Fallo("No se especificó el RPU, verifique");
    } else {
        rpu=request.getParameter("$rpu");
    }
    
    if (rpu.length()!=12) {
        estatus="rechazado";    
        throw new Fallo("Favor de verificar el número de servicio del usuario de 12 dígitos");
    }
    //2. Validar Tienda ID
    if (request.getParameter("$id_control")==null) {
        estatus="rechazado";
        throw new Fallo("No se especificó la clave de la tienda, verifique");
    } else {
        idControl=request.getParameter("$id_control");
    }
    
    resultadoValidacionRPU =validacionBeneficiario.valida(rpu, idControl, 2, user);
    resultadoValidacionRPU =resultadoValidacionRPU.replace("<![CDATA[", "").replace("]]>", "");    
} catch(Exception e) {
    error= e.getMessage().replace("<![CDATA[", "").replace("]]>", "");
}

if (!error.equals("")) {
    resultadoValidacionRPU = error;
}
%><ahorrateunaluz>
    <resultado><![CDATA[<%=resultadoValidacionRPU%>]]></resultado>
    <estatus><%=validacionBeneficiario.getEstatusSolicitud()%></estatus>
</ahorrateunaluz><%
/* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
request.getSession().setAttribute("usuario", user); 
%>
