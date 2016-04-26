<?xml version='1.0' encoding='ISO-8859-1'?><%@
page contentType="text/html" pageEncoding="UTF-8"%><%@
page import="mx.org.fide.controlador.Sesion"%><%@
page import="mx.org.fide.interfaz.Interfaz"%><%@
page import="mx.org.fide.modelo.Usuario"%><%@
page import="mx.org.fide.modelo.*" %><%@
page import="mx.org.fide.utilerias.StringUtils" %>
<% 
    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("ISO-8859-1");
    
    Sesion sesion = (Sesion) request.getSession().getServletContext().getAttribute("sesion");
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
    if (usuario == null) {
        request.getRequestDispatcher("/login.jsp?$app=interfaz").forward(request, response);
        return;
    }
    
    Interfaz interfaz = new Interfaz(usuario);
    StringBuilder html = new StringBuilder();
    String foto = "img/sin_foto.jpg";
    Integer appActual=0;    
    Integer formaActual=0;
    String app = ""; 
    
    if (request.getParameter("app")!=null)  {
        appActual=Integer.parseInt(request.getParameter("app")); 
        //Busca en las aplicaciones la app actual
        for (Aplicacion aplicacion: interfaz.getAplicaciones()) {
            if (aplicacion.getClaveAplicacion()==appActual) {
                formaActual = aplicacion.getClaveFormaPrincipal();
                app = aplicacion.getAplicacion();
                break;
            }
        }
    }  else  {
        appActual=interfaz.getAplicaciones().get(0).getClaveAplicacion(); 
        formaActual  = interfaz.getAplicaciones().get(0).getClaveFormaPrincipal(); 
        app = interfaz.getAplicaciones().get(0).getAplicacion();
    }
html.append(interfaz.getAppMenu(0, appActual));%><html><![CDATA[ <%=StringUtils.unescapeHtml3(html.toString())%>]]></html>