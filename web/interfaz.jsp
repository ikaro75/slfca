<%@page import="mx.org.fide.backend.Forma"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="mx.org.fide.controlador.Sesion"%>
<%@page import="mx.org.fide.interfaz.Arbol"%>
<%@page import="mx.org.fide.modelo.Usuario"%>
<%@page import="mx.org.fide.interfaz.Interfaz"%>
<%@page import="java.util.ArrayList"%>
<%@page import="mx.org.fide.modelo.*" %><%
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
%><!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html;charset=iso-8859-1" />
		<meta name="viewport" content="width=device-width initial-scale=1.0 maximum-scale=1.0 user-scalable=yes" />

		<title>AdministraX - <%=sesion.getConfiguracion().getConfiguracionActual()%> </title>

		<link type="text/css" rel="stylesheet" href="css/jquery.mmenu.all.css" />
                <link type="text/css" rel="stylesheet" href="css/interfaz.css" />
                <link type="text/css" rel="stylesheet" href="css/demo.css" />
		<link type="text/css" rel="stylesheet" href="css/cupertino/jquery-ui.css" media="screen"/>
                <link type="text/css" rel="stylesheet" href="jQuery/js/jqGrid/css/ui.jqgrid.css" media="screen" />

		<script type="text/javascript" src="jQuery/js/jquery-1.7.2.min.js"></script>
                <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.16.custom.min.js"></script>
		<script type="text/javascript" src="jQuery/js/jquery.mmenu.min.js"></script>

                <!-- jqGrid -->
                <script type="text/javascript" src="jQuery/js/grid.locale-es.js" ></script>
                <script type="text/javascript" src="jQuery/js/jquery.jqGrid.min.js"></script>
                <script type="text/javascript" src="jQuery/js/grid.subgrid.js"></script>
                <script type="text/javascript" src="jQuery/js/grid.treegrid.js" > </script>
                <script type="text/javascript" src="jQuery/js/jquery.pi.grid2.js" ></script>
                <script type="text/javascript" src="jQuery/js/funciones.js" ></script>
                
                <!--Datetime picker -->
                <script type="text/javascript" src="jQuery/js/jquery-ui-timepicker-addon.js" ></script>

                <!-- Calculator -->
                <script type="text/javascript" src="jQuery/js/jquery.calculator.min.js" ></script>
                <script type="text/javascript" src="jQuery/js/jquery.calculator-es.js" ></script>

                <!-- Menu -->
                <script type="text/javascript" src="jQuery/js/jquery.ui.menu.js"></script>
                
                <!-- form plugin para considerar uploads  -->
                <script type="text/javascript" src="jQuery/js/jquery.form.js"></script>
                
                <!-- Tooltip -->
                <script type="text/javascript" src="jQuery/js/jquery.tooltip.min.js"></script>

                <!-- Time ago -->
                <script type="text/javascript" src="jQuery/js/jquery.timeago.js"></script>
                <script type="text/javascript" src="jQuery/js/jquery.timeago.es.js" ></script>

                <!-- librerias de la interfaz -->
                <script type="text/javascript" src="jQuery/js/jquery.pi.field_toolbar2.js" ></script>                
                <script type="text/javascript" src="jQuery/js/jquery.pi.form2.js" ></script>
                <script type="text/javascript" src="jQuery/js/jquery.pi.reportes.js"></script>
                <script type="text/javascript" src="jQuery/js/jquery.pi.reportParameterForm.js"></script>
                <script type="text/javascript" src="jQuery/js/jquery.pi.comments2.js"></script>
                <script type="text/javascript" src="jQuery/js/jquery.pi.portlet.js"></script>
                <!-- librerias de las aplicaciones -->
                <script type="text/javascript" src="jQuery/js/backend.js" ></script>

		<script type="text/javascript">
			$(function() {
                                $('html').addClass('mm-black');
				$('nav#menu').mmenu();
			});
		</script>
	</head>
	<body id="top">
		<div id="page">
			<div id="header">
				<a href="#menu"></a>
                                <img src="img/cantina_logo.png" class="logo_plataforma" id="logo_plataforma"/>
                                <% 
                                    if (usuario.getFoto()!=null) {
                                         foto="/uploads/".concat(usuario.getClave().toString()).concat("/").concat(usuario.getFoto());
                                    }
                                %><div style="color:#000; font-size:12px; float: right;">
                                    <table style="width:100%; margin: 0px; border: 0px;">
                                        <tr>
                                            <td style="text-align: right; "><span style="display:block; height: 16px;margin-top: -15px;" id="_un_"><%=usuario.getNombre().concat(" ").concat(usuario.getApellido_paterno())%></span>
                                                <a class="session_menu" href="control?$cmd=logout" id="lnkCerrarSesion">Cerrar sesión </a>
                                            </td>
                                            <td><img style="width:30px; vertical-align: baseline;" src="<%=foto%>" id="avatar"/></td>
                                        </tr>
                                    </table>
                                  </div>
                        </div>
			<div id="content">
                        <% if (request.getParameter("app")!=null)  {
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
                            Forma form = new Forma();
                            form.setClaveForma(formaActual);
                            %>
                            <h3 class="ui-widget-header ui-corner-all" style="height: 25px;margin-top: 0px; font-size: 15px;padding-top: 10px;">&nbsp;&nbsp;<%=app%></h3>
                            <% 
                            ArrayList<Portlet> portlets = form.getPortlets(usuario.getCx());
                            if (portlets.size()>0) { %>
                            <div id="leftPane_<%=appActual%>_<%=formaActual%>_0" class="leftPane">
                                <div id="accordion_<%=appActual%>_<%=formaActual%>_0" class='accordionContainer'>
                                <%  for (Portlet portlet : form.getPortlets(usuario.getCx())) {
                                       %><h3>&nbsp;<%=portlet.getTitulo()%></h3>
                                        <div style="height: auto;" class="<%=portlet.getClave_portlet()%>" id="<%=portlet.getClave_portlet()%>_<%=portlet.getClave_forma()%>">
                                            <%=portlet.getHtml(formaActual, usuario)%>
                                        </div>
                                  <% } %>
                                </div>
                            </div>
                            <% } %>    
                            <div id="rigthPane_<%=appActual%>_<%=formaActual%>_0" <%if (portlets.size()>0) { %>class="rigthPane"<% } %>>
                                <div id="divGrid_<%=appActual%>_<%=formaActual%>_0" class="queued_grids" app="<%=appActual%>" form="<%=formaActual%>" wsParameters="" ></div>
                            </div>                            
                        </div>
			<nav id="menu" class="mm-light">
                                <% html.append(interfaz.getAppMenu(0, appActual)); %>
                                <%=html.toString()%>
			</nav>
		</div>
                <script type="text/javascript" language="javascript">
                    $(document).ready(function() {
                        $("#divwait").dialog({
                                height: 140,
                                modal: true,
                                autoOpen: true,
                                closeOnEscape:false
                        });
                        $("#accordion_<%=appActual%>_<%=formaActual%>_0").accordion({
                        active: false,
                        /*fillSpace:true, */
                        autoHeight: false,
                        collapsible: true,
                        change: function() {
                            $(this).find('h3').blur();
                        }});
                        $("#divGrid_<%=appActual%>_<%=formaActual%>_0").appgrid2({
                                app: <%=appActual%>,
                                entidad: <%=formaActual%>,
                                pk:0,
                                editingApp: <%=appActual%>,
                                wsParameters:"",
                                height:"65%",
                                originatingObject:""
                            });
                            
                         $(".1,.2,.3,").portlets();
                           
                    }); //close $(
                </script>
            <input type="hidden" name="_ce_" id="_ce_" value="<%=usuario.getClave()%>" />
            <input type="hidden" name="_cp_" id="_cp_" value="<%=usuario.getClavePerfil()%>" />
            <input type="hidden" name="_cp_" id="_ca_" value="<%=usuario.getClaveOficina()%>" />
            <input type="hidden" name="_gq_" id="_gq_" value="" />
            <input type="hidden" name="_gado_" id="_gado_" value="true" />
            <input type="hidden" name="_cache_" id="_cache_" value="true" />
            <div id="divwait" title="Espere un momento, por favor"><br /><p style="text-align: center"><img src='img/throbber.gif' />&nbsp;Creando lo increible</p></div>
	</body>
</html>