<?xml version="1.0" encoding="UTF-8"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="mx.org.fide.restaurantex.*" %><%@ page import="mx.org.fide.modelo.*" %>
<%  
    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");
    //Obtener el usuario
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
    if (usuario == null) {
        request.getRequestDispatcher("/index.jsp");
    }
    
    StringBuilder resultado = new StringBuilder();

    if((request.getParameter("$clave_sala")==null)) {
            %><error>No se ha definido el parametro $clave_sala</error><%
        return;
    }    

    if((request.getParameter("$numero_mesas")==null)) {
            %><error>No se ha definido el parametro $numero_mesas</error><%
        return;
    }    
    
    //obtener forma
    Sala s = new Sala(Integer.parseInt(request.getParameter("$clave_sala")),usuario);
    
    try {
        resultado.append(s.agregaMesas(Integer.parseInt(request.getParameter("$numero_mesas"))));
    } catch (Exception e) {
        resultado.append("<error>").append(e.getMessage()).append("</error>");
    }
    %><respuesta><%=resultado.toString() %></respuesta>
