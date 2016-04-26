<?xml version="1.0" encoding="ISO-8859-1"?><%@ page contentType="text/xml" pageEncoding="UTF-8" 
%><%@page import="java.util.ArrayList"
%><%@page import="mx.org.fide.backend.Aplicacion"
%><%@page import="mx.org.fide.modelo.*" 
%><% 
response.setContentType("text/xml;charset=ISO-8859-1");
request.setCharacterEncoding("UTF8");
Integer ap=null;
Integer c = null;

if (request.getParameter("$ap") != null) {
    try {
        ap = Integer.parseInt(request.getParameter("$ap")); 
    } catch (Exception e) {
        throw new Fallo("El parámetro $ap no es válido, verifique");
    }
}

Usuario user=(Usuario)request.getSession().getAttribute("usuario");

if (user == null) {
   request.getRequestDispatcher("/index.jsp"); 
}

user.setAplicaciones(ap);
%>
<qry source="(Aplicaciones del usuario)"><%
    for (Aplicacion app: user.getAplicaciones()) { 
        /*if (app.getClaveCategoria()==c) {*/
%><registro>
    <aplicacion tipo_dato="string"><![CDATA[<%=app.getAplicacion()%>]]></aplicacion>
    <clave_categoria tipo_dato="integer"><![CDATA[<%=app.getClaveCategoria()%>]]></clave_categoria>
    <clave_aplicacion tipo_dato="integer"><![CDATA[<%=app.getClaveAplicacion()%>]]></clave_aplicacion>
    <clave_aplicacion_padre tipo_dato="integer"><![CDATA[<%=app.getClaveAplicacionPadre()%>]]></clave_aplicacion_padre>
    <clave_forma tipo_dato="integer"><![CDATA[<%=app.getClaveFormaPrincipal()%>]]></clave_forma>
    <forma tipo_dato="integer" prefiltro="<%=app.getPrefiltroFormaPrincipal()%>"><![CDATA[<%=app.getFormaPrincipal()%>]]></forma>
  </registro><% /* } */
    }
%></qry>