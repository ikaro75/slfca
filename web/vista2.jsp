<%@page import="java.util.LinkedHashMap"%>
<%@page import="mx.org.fide.controlador.Sesion"%>
<%@page import="mx.org.fide.modelo.*" %><%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
response.setDateHeader("Expires", 0); // Proxies.
Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
Sesion sesion = (Sesion) request.getSession().getServletContext().getAttribute("sesion");

if (usuario == null) {
    response.sendRedirect("login.jsp");
} else {

    if (sesion == null) {
        sesion = new Sesion();
        request.getSession().getServletContext().setAttribute("sesion", sesion);
}
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />        
        <title id="title">Sistema de Control y Seguimiento del Programa Nacional de Sustitución de Lámparas Incandescentes por Fluorescentes
Compactas Autobalastradas (LFCA) / <%=sesion.getConfiguracion().getConfiguracionActual()%> </title>
        <link type="image/x-icon" href="img/favicon.ico" rel="shortcut icon"  />
        
        <link type="text/css" rel="stylesheet" href="bootstrap-3.3.6-dist/css/bootstrap.min.css" />
        <link type="text/css" rel="stylesheet" href="jquery-ui-bootstrap/css/custom-theme/jquery-ui-1.10.3.custom.css" />
        <link type="text/css" rel="stylesheet" href="jquery-ui-bootstrap/assets/css/font-awesome.min.css" />
        <link type="text/css" rel="stylesheet" href="jquery-ui-bootstrap/assets/css/docs.css" />
        <link type="text/css" rel="stylesheet" href="jquery-ui-bootstrap/assets/js/google-code-prettify/prettify.css" />
    
        <!-- librerias para cargar dialogo  -->
        <script type="text/javascript" src="jquery-ui-bootstrap/assets/js/vendor/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="jquery-ui-bootstrap/assets/js/vendor/jquery-migrate-1.2.1.min.js" ></script>
        <script type="text/javascript" src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.21.custom.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jquery-ui-bootstrap/assets/js/google-code-prettify/prettify.js" ></script>
    
        <!-- app portlets -->
        <script type="text/javascript" src="jQuery/js/jquery.appportlet.js?id=<%=usuario.getSesion()%>" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.portletlog.js?id=<%=usuario.getSesion()%>" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.portletfilter.js?id=<%=usuario.getSesion()%>" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.portletreport.js?id=<%=usuario.getSesion()%>" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.prefilter.js?id=<%=usuario.getSesion()%>" ></script>

        <!-- Cookie -->
        <script type="text/javascript"  src="jQuery/js/jquery.cookie.js?id=<%=usuario.getSesion()%>" ></script>

        <!-- jqGrid -->
        <script type="text/javascript" src="jQuery/js/grid.locale-es.js?id=<%=usuario.getSesion()%>" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.jqGrid.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/grid.subgrid.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery.jstree.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/grid.treegrid.js?id=<%=usuario.getSesion()%>" > </script>
        
        <!-- jqPlot -->
        <script type="text/javascript" src="jQuery/jqPlot/jquery.jqplot.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.canvasTextRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.canvasAxisLabelRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.dateAxisRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.pieRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.donutRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.bubbleRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.barRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.categoryAxisRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.pointLabels.min.js?id=<%=usuario.getSesion()%>"></script>

        <!--Datetime picker -->
        <script type="text/javascript"src="jQuery/js/jquery-ui-timepicker-addon.js?id=<%=usuario.getSesion()%>"></script>

        <!-- Calculator -->
        <script type="text/javascript" src="jQuery/js/jquery.calculator.min.js?id=<%=usuario.getSesion()%>" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.calculator-es.js?id=<%=usuario.getSesion()%>" ></script>

        <!-- Menu -->
        <script type="text/javascript" src="jQuery/js/jquery.ui.menu.js"></script>

        <!-- Autocomplete -->
        <script type="text/javascript" src="jQuery/js/combobox.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.core.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.widget.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.button.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.position.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.autocomplete.js?id=<%=usuario.getSesion()%>"></script>
        
        <!-- Carrousel -->
        <script type="text/javascript" src="jQuery/js/agile_carousel.alpha.js?id=<%=usuario.getSesion()%>"></script>

        <!-- Tooltip -->
        <script type="text/javascript" src="jQuery/js/jquery.tooltip.min.js?id=<%=usuario.getSesion()%>" ></script>

        <!-- form plugin para considerar uploads  -->
        <script type="text/javascript" src="jQuery/js/jquery.form.js?id=<%=usuario.getSesion()%>"></script>
        
        <script type="text/javascript" src="jQuery/js/backend.js?id=<%=usuario.getSesion()%>"></script>
        
        <script type="text/javascript" src="jQuery/js/jquery.reportParameterForm.js?id=<%=usuario.getSesion()%>" ></script>
        <script type="text/javascript" src="jQuery/js/alertmanager.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/cambia_password.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.survey_icr.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/mrv.js?id=<%=usuario.getSesion()%>"></script>
        
        <link rel="stylesheet" href="bootstrap-3.3.6-dist/css/bootstrap.min.css"  />
        
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/jqPlot/jquery.jqplot.min.css"/>
        
        <link rel="stylesheet" type="text/css" media="screen" href="css/agile_carousel.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="css/south-street/jquery.ui.all.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jqGrid/css/ui.jqgrid.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/jquery.tooltip.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/style.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/vista.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/calculator/jquery.calculator.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jwysiwyg-master/jquery.wysiwyg.css" />

        <link rel="stylesheet" href="jquery-ui-bootstrap/assets/css/font-awesome.min.css"/>
        <link rel="stylesheet" href="jquery-ui-bootstrap/assets/css/docs.css" />
        <link rel="stylesheet" href="jquery-ui-bootstrap/assets/js/google-code-prettify/prettify.css" />
        
        <script src="jQuery/js/funciones.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.desktop.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.gridqueue.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.field_toolbar.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.session.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.form.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.formqueue.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.accordion.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.tab.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.grid.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.treeMenu.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.appmenu.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.comments.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.timeago.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/jquery.timeago.es.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/padron.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/punto_entrega.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/proveedor.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/importacion.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/cuestionario_supervision.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/encuesta_satisfaccion.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/calendario_tarifa.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        
        <script type="text/javascript" src="jQuery/js/jwysiwyg-master/jquery.wysiwyg.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/sets/html/set.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery.themeswitcher.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/charts.js?id=<%=usuario.getSesion()%>" ></script>
        <script type="text/javascript" src="jQuery/js/vista.js?id=<%=usuario.getSesion()%>" ></script>
        
        
    </head>
    <body id="top">
        <nav class="navbar navbar-inverse">
          <div class="container-fluid">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>                        
              </button>
              <a class="navbar-brand" href="#"><img src="<%=((LinkedHashMap) sesion.getConfiguracion().getParametros().get(sesion.getConfiguracion().getConfiguracionActual())).get("enterprise_banner_logo").toString()%>"/></a>
            </div>
            <div class="collapse navbar-collapse" id="myNavbar">
              <ul class="nav navbar-nav">
                <li class="active"><a href="#">Inicio</a></li>
                <li class="dropdown">
                  <a class="dropdown-toggle" data-toggle="dropdown" href="#">Aplicaciones<span class="caret"></span></a>
                  <ul class="dropdown-menu">
                    <li><a href="#">Pendientes</a></li>
                    <li><a href="#">Padrón de beneficiarios</a></li>
                    <li><a href="#">Almacenes</a></li>
                    <li><a href="#">Tiendas</a></li>
                    <li><a href="#">Oficinas regionales</a></li>
                    <li><a href="#">Proveedores</a></li>
                    <li><a href="#">Medidores</a></li>
                    <li><a href="#">Georreferencias</a></li>
                    <li><a href="#">Admnistrador de cuestionarios</a></li>
                    <li><a href="#">importación de datos</a></li>
                  </ul>
                </li>

              </ul>
              <ul class="nav navbar-nav navbar-right">
                <li><a href="#"><span class="glyphicon glyphicon-user"></span>Daniel Martínez</a></li>
                <li><a href="#"><span class="glyphicon glyphicon-log-out"></span> Cerrar sesión</a></li>
              </ul>
            </div>
          </div>
        </nav>
           
        <div id="tabcontainer">
            <div id="tabs">
                <ul>
                    <li><a href="#tabInicio">Inicio</a></li>
                    <li><a href="#tabAplicaciones">Aplicaciones</a>   
                    </li>
                    <li><a href="#tabMapaDelSitio">Mapa del sitio</a></li>
                    <li><a href="#tabAyuda">Ayuda</a></li>
                    <li><a href="#tabContacto">Contacto</a></li>
                </ul>
                <div id="tabInicio">
                    <div id="tabUser">    
                        <ul>
                            <li><a href="#tabPendientes">Pendientes</a></li>
                            <!--<li><a href="#tabFavoritos">Favoritos</a></li> -->
                        </ul>
                        <div id="tabPendientes">
                            <div id="divgrid_1_101_0" class="queued_grids" app="1" form="101" wsParameters="" titulo="Mis pendientes" leyendas="Nueva actividad, Editar actividad" inDesktop="true" openKardex="false" ></div>
                            <div class="blank_space">&nbsp;</div>
                            <div id="divgrid_1_102_0" class="queued_grids*" app="1" form="102" wsParameters="" titulo="Pendientes que yo asigné" leyendas="Nueva actividad, Editar actividad" inDesktop="true" openKardex="false" />
                        </div>
                    </div>          
                </div>
                <div class="column ui-sortable" style="width:28%">
                    <div class="portlet">
                        <div class="portlet-header">Avisos</div>
                        <div class="portlet-content">
                           Versi&oacute;n beta.20160222.11.47
                        </div>
                    </div>
                    <div class="portlet">
                        <div class="portlet-header">Manual</div>
                        <div class="portlet-content">
                            <p>Aquí va el manual de usuario</p>
                        </div>
                    </div>
                    <div class="portlet">
                        <div class="portlet-header">Distribución de LFCA por mes</div>
                        <div class="portlet-content">
                            <div id="entregas_x_mes"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tabAplicaciones">
                <div id="appMenu_1_0" aplicacion="0" class="appmenu"></div><br/>
                <div id="tab_1_0">
                    <ul>
                        <li><a href='#tabMisAplicaciones'>Cómo usar mis aplicaciones</a></li>
                    </ul>
                    <div id='tabMisAplicaciones'>
                        <div id='divCarouselMisAplicaciones'>
                            <div id='ayudaComoUsarMisAplicaciones'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h1>Comienza a usar tus aplicaciones</h1>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_usar_aplicaciones.png'  class='helpScreen'/>
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                En este espacio puedes abrir tus aplicaciones, para comenzar haz lo siguiente:</p>
                                            <table>
                                                <tr>
                                                    <td><img src="img/paso1.png" /></td><td class='instrucciones'>Ve a la pestaña "Aplicaciones"</td>
                                                </tr>
                                                <tr>
                                                    <td><img src="img/paso2.png" /></td><td class='instrucciones'>Haz clic en el botón de la aplicación que deseas abrir; inmediatamente después se abrirá una pestaña con el nombre de la aplicación seleccionada con el catálogo principal</td>
                                                </tr>
                                            </table>        
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id='ayudaComoAgregarUnRegistro'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h1>Agrega un registro nuevo al catálogo</h1>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_agregar_registros.png'  class='helpScreen' />
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Para agregar un registro sigue los siguientes pasos:</p>
                                            <table>
                                                <tr>
                                                    <td><img src="img/paso1.png" /></td>
                                                    <td>Haz clic encima del botón <span class='ui-icon ui-icon-plus' style='display:inline-block'></span>de la barra de herramientas del catálogo. <br />
                                                    La página desplegará una ventana solicitando la información que se requiere para agregar el nuevo registro. Los campos marcados con (*) son obligatorios.</td>
                                                </tr>
                                                <tr>
                                                    <td><img src="img/paso2.png" /></td>
                                                    <td>Ingresa los datos conforme se solicitan</td>
                                                </tr>
                                                <tr>
                                                    <td><img src="img/paso3.png" /></td>
                                                    <td>Presiona el botón "Guardar"; esto cerrara la ventana e incorporará el nuevo registro al catálogo. </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id='ayudaComoEditarUnRegistro'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h1>Edita un registro del catálogo</h1>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_editar_registros.png' class='helpScreen' />
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Para editar un registro sigue los siguientes pasos:</p>
                                            <table>
                                                <tr>
                                                    <td><img src="img/paso1.png" /></td>
                                                    <td>Selecciona del catálogo el registro que deseas editar.</td>    
                                                </tr>
                                                <tr>
                                                    <td><img src="img/paso2.png" /></td>
                                                    <td>Haz clic encima del botón <span class='ui-icon ui-icon-pencil' style='display:inline-block'></span>de la barra de herramientas del catálogo. <br />
                                                    La página desplegará una ventana con la información del registro seleccionado para que lo edites. </td>
                                                </tr>
                                                <tr>
                                                    <td><img src="img/paso3.png" /></td>
                                                    <td>Edita los campos necesarios,  aquellos marcados con (*) son obligatorios.</td>
                                                </tr>    
                                                <tr>
                                                    <td><img src="img/paso4.png" /></td>
                                                    <td>Presiona el botón "Guardar"; esto cerrara la ventana y actualizará el registro.</td>
                                                </tr>                                                
                                            </table>                                        
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id='ayudaComoEliminarUnRegistro'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h1>Elimina un registro del catálogo</h1>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_eliminar_registros.png' class='helpScreen' />
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Para eliminar un registro sigue los siguientes pasos:</p>
                                            <table>
                                                <tr>
                                                    <td><img src="img/paso1.png" /></td>
                                                    <td>Selecciona del catálogo el registro que deseas eliminar.</td>
                                                </tr>    
                                                <tr>
                                                   <td><img src="img/paso2.png" /></td>
                                                   <td>Haz clic encima del botón <span class='ui-icon ui-icon-trash' style='display:inline-block'></span>de la barra de herramientas del catálogo. <br />
                                                       La página solicitará que confirmes la eliminación.</td> 
                                                </tr>
                                                <tr>
                                                    <td><img src="img/paso3.png" /></td>
                                                    <td>Presiona OK en el dialogo de confirmación para proceder con el borrado</td>
                                                </tr>
                                            </table>                                        
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id='ayudaComoFiltrarRegistros'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h1>Filtra los registros del catálogo</h1>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/como_filtrar_registros.png' class='helpScreen' />
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Filtra para encontrar el conjunto de registros que cumplen con tus criterios de filtrado. Para filtrar registros sigue los siguientes pasos:</p>
                                            <table> 
                                                <tr>
                                                    <td><img src="img/paso1.png" /></td>
                                                    <td>Haz clic encima del botón <span class='ui-icon ui-icon-search' style='display:inline-block'></span>de la barra de herramientas del catálogo. <br />
                                                        La página desplegará una ventana para ingresar los criterios de filtrado</td> 
                                                </tr>
                                                <tr>
                                                    <td><img src="img/paso2.png" /></td>
                                                    <td>Ingresa al menos un criterio de filtrado; puedes seleccionar más de uno. Mientras más criterios utilices más reducida será el resultado de la búsqueda.</td>
                                                </tr>        
                                                <tr>
                                                    <td><img src="img/paso3.png" /></td>
                                                    <td>Si deseas guardar el filtro para su posterior uso, as&iacute;gnale un nombre en el campo <a href="javascript:void(0);" class="tooltipLink" id="img/guardar_filtro_como.png">Guardar filtro como</a>:"; recuperalo posteriormente en el menú <a href="javascript:void(0);" class="tooltipLink" id="img/mis_filtros.png">"Mis filtros"</a> que se encuentra a la izquierda del catálogo. </td>
                                                </tr>        
                                                <tr>
                                                    <td><img src="img/paso4.png" /></td>
                                                    <td>Presiona el botón "Buscar"; esto cerrará la ventana y mostrará el resultado en el catálogo. Para remover el filtro y restaurar los registros haz clic encima de la liga (Quitar filtro) que se encuentra junto al titulo del catálogo." </td>
                                                </tr>        
                                            </table>                                        
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <!-- <div id='ayudaComoAgregarCatalogoAFavoritos'>
                                <table >
                                    <tr>
                                        <td colspan='2'>
                                            <h3>Agrega a tus favoritos los catálogos que utilizas más frecuentemente</h3>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <img src='img/favoritos.png'  class='helpScreen'/>
                                        </td>
                                        <td class='instrucciones'>
                                            <p class='instrucciones'>
                                                Puedes agregar a la pestaña de inicio los catálogos que utilizas más frecuentemente en "Mis favoritos", para agregar un favorito haz lo siguiente:</p>
                                            <ol>
                                                <li class='instrucciones'>Ve a la pestaña "Aplicaciones"</li>
                                                <li class='instrucciones'>Haz clic en el botón de la aplicación que deseas abrir</li>
                                                <li class='instrucciones'>Haz clic en el botón <span class='ui-icon ui-icon-star' style='display:inline-block'></span> de la barra de herramientas del catálogo que deseas hacer tu favorito</li>
                                            </ol>        
                                        </td>
                                    </tr>
                                </table>
                            </div> -->
                        </div>
                    </div>       
                </div>
            </div>    
            <div id="tabMapaDelSitio">
                <div id="divCarouselMapa">
                    <div id="mapa">
                        <table>
                            <tr>
                                <td>
                                    <h1 class="instrucciones">Mapa del sitio</h1>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <p class="instrucciones">Aquí se muestran todas y cada una de las partes que componen al sistema para el perfil actual, seleccione la liga correspondiente para acceder al componente deseado.</p>
                                    <dl>
                                        <dt class="instrucciones" ><a id="mapLink-tabInicio" class="maplink" href="#">Inicio</a>
                                            <dl class="instrucciones">
                                                <dt class="instrucciones"><a id="mapLink-tabInicio-tabUser-tabPendientes" class="maplink" href="#">Pendientes</a></dt>
                                                <dt><a id="mapLink-tabInicio-tabUser-tabFavoritos" class="maplink" href="#">Favoritos</a>
                                                    <dl id="tabMisFavoritos_in_map">

                                                    </dl>
                                                </dt>
                                            </dl>
                                        </dt>
                                        <dt class="instrucciones"><a id="mapLink-tabAplicaciones" class="maplink" href="#">Aplicaciones</a>
                                            <dl id="app_menu_in_map">
                                            </dl>
                                        </dt>
                                        <dt class="instrucciones"><a id="mapLink-tabMapaDelSitio" class="maplink" href="#">Mapa de sitio</a></dt>
                                        <dt class="instrucciones"><a id="mapLink-tabAyuda" class="maplink" href="#">Ayuda</a></dt>
                                        <dt class="instrucciones"><a id="mapLink-tabContacto" class="maplink" href="#">Contacto</a></dt>
                                    </dl>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
            <div id="tabAyuda">
                <div id="divCarouselAyuda">
                    <div id="ayudaIndice">
                        <table>
                            <tr>
                                <td>
                                    <h1 class="instrucciones">Indice de temas de ayuda</h1>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <div style="text-align: center;">
                                        <table style="margin: 0 auto;text-align: left;">
                                            <tr>
                                                <td style="vertical-align:top;  text-align: left; width:450px">
                                                    <p class="instrucciones">
                                                        <ol> 
                                                            <li><a href="#" class="lnkAyuda" id="ayuda-2">Vistazo general al sistema</a></li>
                                                            <li><a href="#" class="lnkAyuda" id="ayuda-3">Inicio</a></li>
                                                            <li><a href="#" class="lnkAyuda" id="ayuda-4">Aplicaciones</a></li>
                                                            <li><a href="#" class="lnkAyuda" id="ayuda-14">Mapa del sitio</a></li>
                                                        </ol>            
                                                    </p> 
                                                </td>
                                                <td>
                                                    <img src='img/portada_ayuda.png' style='border: 1px'  class='helpScreen'></img>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                    <div id="ayudaVistazo">
                        <table>
                            <tr>
                                <td>
                                    <h2 class="instrucciones">Vistazo general al sistema</h2>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <p class="instrucciones">
                                        La plataforma cuenta con 5 partes principales: <br />
                                        <ul>
                                            <li>Inicio: en donde se encuentran las pestañas "Pendientes" y "Favoritos".
                                                <ul>
                                                    <li>En "Pendientes" puedes ver y atender tus actividades</li>
                                                    <li>En "Favoritos" puedes tener los catálogos que uses con mayor frecuencia a la mano</li>
                                                </ul>
                                            </li>
                                            <li>Aplicaciones: aquí encontrarás los módulos que conforman la plataforma. Cada aplicación cuenta con un catálogo principal y del cual se desprenden otros catálogos relacionados en forma de kardex.</li>
                                            <li>Mapa del sitio: es el listado con las ligas hacia todos las partes de la plataforma.</li>
                                            <li>Ayuda: contiene el índice del manual en línea.</li>
                                            <li>Contacto: aquí se presenta la información para contactar al administrador de la plataforma.</li>
                                        </ul>
                                    </p>
                                    <p class="instrucciones">
                                        Por otra parte, los catálogos se presentan en una cuadrícula y dependiendo de los permisos de tu perfil se mostrarán botones para insertar <span class='ui-icon ui-icon-plus' style='display:inline-block'></span> , editar <span class='ui-icon ui-icon-pencil' style='display:inline-block'></span>, eliminar<span class='ui-icon ui-icon-trash' style='display:inline-block'></span>, hacer búsquedas de registros<span class='ui-icon ui-icon-search' style='display:inline-block'></span>, asi como abrir el kardex<span class='ui-icon ui-icon-newwin' style='display:inline-block'></span> con los catálogos relacionados al registro seleccionado.
                                    </p>
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                    <div id="ayudaInicio">
                        <table>
                            <tr>
                                <td>
                                    <h2 class="instrucciones">Inicio</h2>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <p class="instrucciones">
                                        Dentro de la pestaña "Inicio", puedes encontrar "Pendientes" puedes ver las actividades por realizar así como la liga para ingresar los datos propios del pendiente; cada perfil tiene sus propios pendientes, y son configurados por el administrador del sistema. <br /><br/>
                                    </p>
                                    <!-- <p class="instrucciones">
                                        <strong>Temas relacionados</strong>
                                        <ul>
                                           <li><a href="#" class="lnkAyuda" id="ayuda-4">Agrega a tus favoritos los catálogos que más utilizas </a></li> 
                                            <li><a href="#" class="lnkAyuda" id="ayuda-5">Elimina los favoritos que ya no utilizas</a></li>
                                        </ul>
                                    </p> --> 
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                    <div id="ayudaAplicacion">
                        <table>
                            <tr>
                                <td>
                                    <h2 class="instrucciones">Aplicaciones</h2>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <p class="instrucciones">
                                        <ul>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-7">Comienza a usar tus aplicaciones</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-8">Agrega un registro nuevo al catálogo</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-9">Edita un registro del catálogo</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-10">Elimina un registro del catálogo</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-11">Filtra los registros del catálogo</a></li>
                                            <li><a href="#" class="lnkAyuda" id="ayuda-12">Agrega a tus favoritos los catálogos que utilizas más frecuentemente</a></li>
                                        </ul>
                                    </p> 
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                    <div id="ayudaMapa">
                        <table>
                            <tr>
                                <td>
                                    <h2 class="instrucciones">Mapa del sitio</h2>
                                </td>
                            </tr>
                            <tr>
                                <td class="instrucciones">
                                    <p class="instrucciones">
                                        Aquí se muestran todas y cada una de las partes que componen al sistema para el perfil actual, selecciona la liga correspondiente para acceder al componente deseado.
                                    </p> 
                                </td>                                    
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
            <div id="tabContacto">
                <div>
                    <table style="width:100%"> 
                        <tr>
                            <td>
                                <h2>Contacto</h2>
                            </td>
                        </tr>
                        <tr>
                            <td style="width:100%">
                                <p class="instrucciones">Envíe un correo con sus sugerencias, comentarios, solicitudes y reportes de errores a <a href="mailto:daniel.martinez05@cfe.gob.mx">daniel.martinez05@cfe.gob.mx</a></p>
                            </td>
                        </tr>
                        <tr>
                            <td style="width:100%">
                            </td>
                        </tr>                        
                    </table>
                </div>
            </div>
            <div id="tabBusqueda">
            </div>                 
        </div>
        <input type="hidden" name="_ce_" id="_ce_" value="<%=usuario.getClave()%>" />
        <input type="hidden" name="_cp_" id="_cp_" value="<%=usuario.getClavePerfil()%>" />
        <input type="hidden" name="_ca_" id="_ca_" value="<%=usuario.getClaveOficina()%>" />
        <input type="hidden" name="_enterprise_" id="_enterprise_" value="1" />
        <input type="hidden" name="_gq_" id="_gq_" value="" />
        <input type="hidden" name="_gado_" id="_gado_" value="true" />
        <input type="hidden" name="_cache_" id="_cache_"/>
        <input type="hidden" name="_status_" id="_status_"/>
        <input type="hidden" name="_sm_" id="_sm_" value="0"/>
        <input type="hidden" name="_sc_" id="_sc_" value="0"/>
        <div id="divwait" title="Espere un momento, por favor"><br /><p style="text-align: center"><img src='img/throbber.gif' /><br /><br />&nbsp;Cargando preferencias del usuario</p></div>
    </body>
</html>
<% } %>