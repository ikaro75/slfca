<?xml version="1.0" encoding="UTF-8"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="mx.org.fide.modelo.*"%>
<%@page import="mx.org.fide.backend.Forma"%>
<%
    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");
    Forma frmTemp = new Forma();
    Usuario user = (Usuario) request.getSession().getAttribute("usuario");
    String error = "";
    String q = ""; 
    StringBuilder s = new StringBuilder();
    if (user == null) {
        request.getRequestDispatcher("/index.jsp"); 
    }

    try {

        if (request.getParameter("q") == null) {
            throw new Fallo("Falta consulta");
        }

        //Aplica reglas de reemplazo
        q = request.getParameter("q").replace("$pk", "0")
                .replace("%clave_empleado",user.getClave().toString())
                .replace("%clave_oficina",user.getClaveOficina().toString())
                .replace("%clave_perfil",user.getClavePerfil().toString());
        
        user.getCx().validateSQL(q);
        
    } catch (Fallo f) {
        error = f.getMessage();
    } catch (Exception e) {
        error = e.getMessage();
    } finally {
        if (!error.equals("")) {
            s.append("<error><![CDATA[").append(error).append("]]></error>");
        } else {
            s.append("<resultado><![CDATA[".concat(q).concat("]]></resultado>"));
        }
    }
%><qry>
    <sql><![CDATA[<%=request.getParameter("$consulta")%>]]></sql>
    <%=s%>
</qry>