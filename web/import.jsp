<?xml version="1.0" encoding="UTF-8"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="mx.org.fide.backend.*" %>
<%@ page import="mx.org.fide.modelo.*" %>
<%  
    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");
    //Obtener el usuario
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
    if (usuario == null) {
        request.getRequestDispatcher("/index.jsp");
    }
    
    StringBuilder resultado = new StringBuilder();
    //obtener forma
    Forma f = new Forma();
    if((request.getParameter("$app")==null)) {
            %><error>No se ha definido el parametro $app</error><%
        return;
    }    

    if((request.getParameter("$prefijo_tablas")==null)) {
            %><error>No se ha definido el parametro $prefijo_tablas</error><%
        return;
    }    
    
    
    try {
        resultado.append(f.importa(Integer.parseInt(request.getParameter("$app")), request.getParameter("$prefijo_tablas"), usuario));
    } catch (Exception e) {
        resultado.append("<error>").append(e.getMessage()).append("</error>");
    }
    %><respuesta><%=resultado.toString() %></respuesta>
