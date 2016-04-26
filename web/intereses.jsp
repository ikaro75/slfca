<?xml version="1.0" encoding="ISO-8859-1"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@page import="mx.org.fide.pagarex.CalendarioCierre"
%><%@page import="mx.org.fide.modelo.Usuario"
%><%@page import="mx.org.fide.modelo.Fallo"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.util.Date"
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat"%><% 
response.setContentType("text/xml;charset=ISO-8859-1");
request.setCharacterEncoding("UTF8");
%><root><%
        
Usuario usuario=(Usuario)request.getSession().getAttribute("usuario");
Integer folioPagare=0;
Date fechaCierre = null;
SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

try {
    if (usuario == null) { 
       request.getRequestDispatcher("/index.jsp");
    }

    if (request.getParameter("folio")==null) {
        %><error>Falta el parametro folio, verifique</error><%
        return;
    } else {
           try {
                folioPagare= Integer.parseInt(request.getParameter("folio"));
            }
            catch (Exception e) { 
                throw new Fallo("El parametro folio no es válido, verifique");
            }
    }


    if (request.getParameter("fecha")==null) {
        %><error>Falta el parametro fecha, verifique</error><%
        return;
    } else {
           try {
                fechaCierre= formatter.parse(request.getParameter("fecha"));
            }
            catch (Exception e) { 
                  throw new Fallo("El parametro fecha no es válido, verifique");
            }
    }

    CalendarioCierre c = new CalendarioCierre (usuario);

    try {
        %><intereses><%=c.calculaInteres(folioPagare, fechaCierre)*-1%></intereses><%
    } catch(Exception ex) { 
        throw new Fallo(ex.getMessage()); 
    } 
} catch(Exception ex) {
    %><error><%=ex.getMessage()%></error><% 
}%>
</root>