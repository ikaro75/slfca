<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="mx.org.fide.utilerias.Utilerias"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="mx.org.fide.controlador.Sesion"%><%
    response.setContentType("text/html;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");
    
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
    request.setCharacterEncoding("UTF8");     
%><!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link type="image/x-icon" href="img/favicon.ico" rel="shortcut icon"  />
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Sistema de Control y Seguimiento del Programa Nacional de Sustitución de Lámparas Incandescentes por Fluorescentes
Compactas Autobalastradas (LFCA) &copy; / Iniciar sesi&oacute;n</title>

    <!-- Bootstrap -->
    <link type="text/css" rel="stylesheet" href="bootstrap-3.3.6-dist/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet"  href="css/south-street/jquery-ui.css"  />    
    <link type="text/css" href="css/signin.css" rel="stylesheet" />
    <link type="text/css" rel="stylesheet" href="jquery-ui-bootstrap/assets/css/font-awesome.min.css" />
    <link type="text/css" rel="stylesheet" href="jquery-ui-bootstrap/assets/css/docs.css" />
    <link type="text/css" rel="stylesheet" href="jquery-ui-bootstrap/assets/js/google-code-prettify/prettify.css" />
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <div class="container">

      <form class="form-signin" action="control?$cmd=login" method="post" name="frmLogin" id="frmLogin">
        <h2 class="form-signin-heading"><%if (sesion!=null) { %><img class="img-responsive center-block" src="<%=((LinkedHashMap) sesion.getConfiguracion().getParametros().get(sesion.getConfiguracion().getParametros().keySet().toString().replace("[", "").replace("]", "").split(",")[0])).get("enterprise_login_logo").toString()%>"/><%}%></h2>
        <label for="inputEmail" class="sr-only">Usuario</label>
        <input type="email" id="inputEmail" class="form-control" placeholder="Usuario" required autofocus>
        <label for="password" class="sr-only">Contrase&ntilde;a</label>
        <input type="password" id="password" class="form-control" placeholder="Contrase&ntilde;a" required>
        <%if (sesion!=null) { if (sesion.getConfiguracion().getParametros().size()>1) { %>
        <label for="$config" class="sr-only">Ambiente</label>
        <select id="$config" name="$config" class="form-control"><%
            for (Object key :  sesion.getConfiguracion().getParametros().keySet()) {
                %><option value="<%=Utilerias.decodeURIComponentXX(key.toString())%>"><%=Utilerias.decodeURIComponentXX(key.toString())%></option><%
            }                                        
        %></select>
        <% } else { 
        %><label for="inputAmbiente" class="sr-only">Ambiente</label>
          <input id="$config" name="$config" type="hidden" value="<%=((LinkedHashMap)sesion.getConfiguracion().getParametros().get(sesion.getConfiguracion().getParametros().keySet().toString().replace("[", "").replace("]", ""))).get("enterprise_name")%>">
        <%
        } }%>        
        <div style="padding-top: 1em; padding-bottom: 1em"><a href="#">Olvidé mi contrase&ntilde;a</a></div>
        <button class="btn btn-lg btn-primary btn-block" type="submit" <%if (!enableLogin) {%>disabled <%}%> id="iniciarsesion">Iniciar sesi&oacute;n</button>
      </form>

    </div> <!-- /container -->

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script type="text/javascript" src="jquery-ui-bootstrap/assets/js/vendor/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="jquery-ui-bootstrap/assets/js/vendor/jquery-migrate-1.2.1.min.js" ></script>
    
    <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>-->
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script type="text/javascript" src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.21.custom.min.js"></script>
    <script type="text/javascript" src="jquery-ui-bootstrap/assets/js/google-code-prettify/prettify.js" ></script>
    <script type="text/javascript" src="jQuery/js/login.js"></script>
  </body>
</html>

