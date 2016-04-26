<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="mx.org.fide.utilerias.Utilerias"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="mx.org.fide.controlador.Sesion"%><%
    response.setContentType("text/html;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");
    StringBuilder myresponse = new StringBuilder();
    Sesion sesion = (Sesion) request.getSession().getServletContext().getAttribute("sesion");
    Boolean enableLogin= true;

    if (sesion == null) {
        try {
            sesion = new Sesion();
            request.getSession().getServletContext().setAttribute("sesion",sesion);

        } catch(Exception e) {
            
            if (e.getMessage()!=null)
                request.getSession().setAttribute("mensaje_login","Problemas al cargar la configuración: ".concat(e.getMessage()));
            else 
                request.getSession().setAttribute("mensaje_login","Problemas al cargar la configuración.");
            enableLogin = false;
        }    
    }
    
    String mensaje=(String) request.getSession().getAttribute("mensaje_login");

    for (Object key :  sesion.getConfiguracion().getParametros().keySet()) {
            myresponse.append("<option value='").append(Utilerias.decodeURIComponentXX(key.toString())).append("'>").append(Utilerias.decodeURIComponentXX(key.toString())).append("</option>");
    }                                        
    %><%=myresponse.toString()%>