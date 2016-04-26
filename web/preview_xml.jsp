<?xml version="1.0" encoding="UTF-8"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@page import="mx.org.fide.utilerias.LinkPreview"%><%
    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");
    String link = request.getParameter("link");
    if (!link.startsWith("http://") && !link.startsWith("https://")) {
        link = "http://" + link;
    }
    LinkPreview _LP = new LinkPreview();
    String status = _LP.getWebPage(link);
%><preview>
    <titulo><![CDATA[<%=new String (_LP.title.getBytes(), "utf-8")%>]]></titulo>
    <descripcion><![CDATA[<%=new String (_LP.description.getBytes(), "utf-8")%>]]></descripcion>
    <estatus><%=status%></estatus>
    <ogimage><%=_LP.ogImage%></ogimage>
    <% for (String imagen : _LP.images) { %><src><%=imagen%></src><% } %>
</preview>

