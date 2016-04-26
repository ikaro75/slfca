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
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
        <link type="text/css" href="css/south-street/jquery-ui.css"  rel="stylesheet"/>
        <link type="text/css" href="css/vista.css" rel="stylesheet" />
        <link type="text/css" href="css/agile_carousel.css" rel="stylesheet"/>
        <link type="image/x-icon" href="img/favicon.ico" rel="shortcut icon"  />
        <script type="text/javascript" src="jQuery/js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" src="jQuery/js/login.js"></script>
        <script type="text/javascript" src="jQuery/js/agile_carousel.alpha.js"></script>
        <title>Sistema de Control y Seguimiento del Programa Nacional de Sustitución de Lámparas Incandescentes por Fluorescentes
Compactas Autobalastradas (LFCA) &copy; / Iniciar sesi&oacute;n</title>
        <style>
            .agile_carousel .numbered_button {
               float:none !important;
            }
        </style>
    </head>

    <body>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
        <table width="35%" border="0" align="center" cellpadding="5" cellspacing="0">
            <tr>
                <td>
                    <br /><br />
                    <div align="center">
                        <table style="width:600px">
                            <tr>
                                <td><%if (sesion!=null) { %><img src="<%=((LinkedHashMap) sesion.getConfiguracion().getParametros().get(sesion.getConfiguracion().getParametros().keySet().toString().replace("[", "").replace("]", "").split(",")[0])).get("enterprise_login_logo").toString()%>" style="width:170px; height:auto; margin-left: 170px;" /><%}%></td>
                                <td style="width:100%">&nbsp;</td>
                            </tr>
                        </table>
                    </div>
                    <br /><br/>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="divCarousel">
                        <div id="divLogin">
                            <form action="control?$cmd=login" method="post" name="frmLogin" id="frmLogin">
                                <table width="75%" border="0" align="center" cellpadding="5" cellspacing="0">
                                    <tr>
                                        <td width="48%" ><div id="usuario" align="right" class="etiqueta_forma">Usuario</div></td>
                                        <td width="52%"><div align="right">
                                                <input name="email" type="text" id="email" style="width:200px;" />
                                            </div></td>
                                    </tr>
                                    <tr>
                                        <td ><div id="contrasena" align="right" class="etiqueta_forma">Contrase&ntilde;a</div></td>
                                        <td><div align="right">
                                                <input name="password" type="password" id="password" style="width:200px;"/>
                                                <% if (request.getParameter("$app")!=null) { %>
                                                <input name="$app" type="hidden" id="$app"  value="<%=request.getParameter("$app")%>"/>
                                                <% } %>
                                            </div></td>
                                    </tr>
                                    <%if (sesion!=null) { if (sesion.getConfiguracion().getParametros().size()>1) { %>
                                    <tr>
                                        <td><div align="right" class="etiqueta_forma" >Ambiente</div></td>
                                        <td><div align="right"><select id="$config" name="$config" style="width:204px;"><%
                                            for (Object key :  sesion.getConfiguracion().getParametros().keySet()) {
                                                %><option value="<%=Utilerias.decodeURIComponentXX(key.toString())%>"><%=Utilerias.decodeURIComponentXX(key.toString())%></option><%
                                            }                                        
                                        %></select></div></td>
                                    </tr>
                                    <% } else { 
                                    %><tr style="display:none">
                                        <td><div align="right" class="etiqueta_forma" >Ambiente</div></td>
                                        <td><div align="right"><input id="$config" name="$config" type="hidden" value="<%=((LinkedHashMap)sesion.getConfiguracion().getParametros().get(sesion.getConfiguracion().getParametros().keySet().toString().replace("[", "").replace("]", ""))).get("enterprise_name")%>"></div></td>
                                    </tr><%
                                    } }%><tr>
                                        <td><div align="right">
                                            </div></td>
                                        <td><div align="right">
                                                <button id="iniciarsesion" <%if (!enableLogin) {%>disabled <%}%>>Iniciar sesi&oacute;n</button>
                                            </div></td>
                                    </tr>
                                    <%   if (mensaje!=null) { 
                                        if (!mensaje.equals("")) { %>
                                    <tr>
                                        <td colspan="2">
                                            <div class="ui-widget" id="divMsgLogin">
                                                <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                                                    <p id="msjLogin"><span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span><%=mensaje%></p>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                  <% } } %>
                                </table>
                            </form>
                        </div>
                        <div id="divLostPw">
                            <table width="75%" border="0" align="center" cellpadding="5" cellspacing="0">
                                <tr>
                                    <td width="48%"><div id="usuario" align="right" class="etiqueta_forma">Usuario</div></td>
                                    <td width="52%"><div align="right">
                                            <input name="rc" type="text" id="rc" size="24" />
                                        </div></td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div align="right">
                                            <button id="btnRecuperarPw">Recuperar contrase&ntilde;a</button>
                                        </div></td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="ui-widget" id="divMsjRecuperaPW">
                                            <div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
                                                <p id="msjRecuperaPW"><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span><strong></strong></p>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
        <div style="position:absolute; bottom: auto; text-align: right; font-family: verdana; margin-left: 200px;">Versi&oacute;n beta.20160405.16.29</div>
    </body>
</html>
<% request.getSession().setAttribute("mensaje_login","");%>
