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
        <title id="title">Sistema de Control y Seguimiento del Programa Nacional de Sustituci�n de L�mparas Incandescentes por Fluorescentes
            Compactas Autobalastradas (LFCA) / <%=sesion.getConfiguracion().getConfiguracionActual()%> </title>
        <link type="image/x-icon" href="img/favicon.ico" rel="shortcut icon"  />

        <!-- librerias para cargar dialogo  -->
        <script type="text/javascript" src="jQuery/js/jquery-1.7.2.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.21.custom.min.js?id=<%=usuario.getSesion()%>"></script>

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
        <script type="text/javascript" src="jQuery/js/grid.treegrid.js?id=<%=usuario.getSesion()%>" ></script>

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

        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/jqPlot/jquery.jqplot.min.css"/>

        <link rel="stylesheet" type="text/css" media="screen" href="css/agile_carousel.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="css/south-street/jquery.ui.all.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jqGrid/css/ui.jqgrid.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/jquery.tooltip.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/style.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/vista.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/calculator/jquery.calculator.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jwysiwyg-master/jquery.wysiwyg.css" />
        
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

               <!-- soporte para charts  -->
        <script type="text/javascript" src="jQuery/jqPlot/jquery.jqplot.min.js?id=<%=usuario.getSesion()%>" ></script> 
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.canvasTextRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.canvasAxisLabelRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.dateAxisRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.pieRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.donutRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.bubbleRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.barRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.categoryAxisRenderer.min.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.pointLabels.min.js?id=<%=usuario.getSesion()%>"></script>
        
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.highlighter.min.js"  ></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.cursor.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jwysiwyg-master/jquery.wysiwyg.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/sets/html/set.js?id=<%=usuario.getSesion()%>"></script>
        <script type="text/javascript" src="jQuery/js/jquery.themeswitcher.min.js?id=<%=usuario.getSesion()%>"></script>
        <script src="jQuery/js/charts.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>
        <script src="jQuery/js/vista.js?id=<%=usuario.getSesion()%>" type="text/javascript"></script>


    </head>
    <body id="top">

        <div id="banner">
            <div style="float: left;">
                <img src="<%=((LinkedHashMap) sesion.getConfiguracion().getParametros().get(sesion.getConfiguracion().getConfiguracionActual())).get("enterprise_banner_logo").toString()%>"/></div>
            <div id="sessionMenu" style="float: right; position: relative; "></div>
        </div>
        <!-- 
        <div class="menus_plataforma">
            <div id="app_menu" style="float:left">
                <div>
                    <a href='#' id='menu_inicio' >Inicio</a>
                    <a href='#' id='menu_aplicaciones' >Aplicaciones</a>
                    <a href='#' id='menu_splitter' >&nbsp;</a>
                    <a href='#' id='menu_mapa' >Mapa del sitio</a>
                    <a href='#' id='menu_ayuda' >Ayuda</a>
                    <a href='#' id='menu_contacto' >Contacto</a>
                </div>
                <ul id="apps">
                </ul>    
            </div>
        </div>
        -->
        <div id="tabcontainer">
            <div id="tabs">
                <ul>
                    <li><a href="#tabInicio">Inicio</a></li>
                    <li><a href="#tabAplicaciones">Aplicaciones</a></li>
                    <li><a href="#tabMapaDelSitio">Mapa del sitio</a></li>
                    <li><a href="#tabAyuda">Ayuda</a></li>
                    <li><a href="#tabContacto">Contacto</a></li>
                </ul>
                <div id="tabInicio">
                    <div id="tabUser">    
                        <div id="tabPendientes">
                            <div id="avance_acumulado" style="width:30%; height: 50em; float:left; margin-right: 3%" >
                                <br /><br /><br /><br /><br /><br />
                                <p id=loader_avance_acumulado" style="text-align: center">Generaci&oacute;n de gr&aacute;fica en progreso... <br><img src='img/loading.gif' /></p>
                            </div>
                            <div id="avance_diferencial" style="width:30%; height: 50em; float:left; margin-right: 3%" >
                                <br /><br /><br /><br /><br /><br />
                                <p id=loader_avance_diferencial" style="text-align: center">Generaci&oacute;n de gr&aacute;fica en progreso... <br><img src='img/loading.gif' /></p>                                
                            </div>
                            <div id="porcentaje_avance"  style="width:30%; height: 50em; float:left; margin-right: 3%">
                                <br /><br /><br /><br /><br /><br />
                                <p id=loader_porcentaje_avance" style="text-align: center">Generaci&oacute;n de gr&aacute;fica en progreso... <br><img src='img/loading.gif' /></p>
                            </div>

                            <!-- <div class="column ui-sortable" style="width:15%; float:left;">
                                <div class="portlet">
                                    <div class="portlet-header">B&uacute;squeda</div>
                                    <div class="portlet-content">
                                        <table>
                                            <tr>
                                                <td>
                                                    B&uacute;squeda por palabra clave
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <input id="kw" type="text" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><select id="clave_estado"></select></td>
                                            </tr>
                                            <tr>
                                                <td><select id="clave_municipio"></select></td>
                                            </tr>
                                            <tr>
                                                <td><select id="clave_localidad"></select></td>
                                            </tr>                                            
                                        </table>    
                                    </div>
                                </div>
                            </div> -->
                            <br />
                            <div id="entityCaruosel_129_745_0" class="entityCarrousel" style="width:100%; float:left; padding-top: 2em" >
                                <div class="gridCarrousel">
                                    <div id="divgrid_129_745_0" class="queued_grids" app="129" form="745" wsParameters="" titulo="Avances de entrega" leyendas="Nueva actividad, Editar actividad" inDesktop="true" openKardex="false"></div>
                                </div>
                            </div>
                        </div>    
                    </div>          
                </div>
                <div id="tabAplicaciones">
                    <div id="appMenu_1_0" aplicacion="0" class="appmenu"></div><br/>
                    <div id="tab_1_0">
                        <ul>
                            <li><a href='#tabMisAplicaciones'>C�mo usar mis aplicaciones</a></li>
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
                                                        <td><img src="img/paso1.png" /></td><td class='instrucciones'>Ve a la pesta�a "Aplicaciones"</td>
                                                    </tr>
                                                    <tr>
                                                        <td><img src="img/paso2.png" /></td><td class='instrucciones'>Haz clic en el bot�n de la aplicaci�n que deseas abrir; inmediatamente despu�s se abrir� una pesta�a con el nombre de la aplicaci�n seleccionada con el cat�logo principal</td>
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
                                                <h1>Agrega un registro nuevo al cat�logo</h1>
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
                                                        <td>Haz clic encima del bot�n <span class='ui-icon ui-icon-plus' style='display:inline-block'></span>de la barra de herramientas del cat�logo. <br />
                                                            La p�gina desplegar� una ventana solicitando la informaci�n que se requiere para agregar el nuevo registro. Los campos marcados con (*) son obligatorios.</td>
                                                    </tr>
                                                    <tr>
                                                        <td><img src="img/paso2.png" /></td>
                                                        <td>Ingresa los datos conforme se solicitan</td>
                                                    </tr>
                                                    <tr>
                                                        <td><img src="img/paso3.png" /></td>
                                                        <td>Presiona el bot�n "Guardar"; esto cerrara la ventana e incorporar� el nuevo registro al cat�logo. </td>
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
                                                <h1>Edita un registro del cat�logo</h1>
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
                                                        <td>Selecciona del cat�logo el registro que deseas editar.</td>    
                                                    </tr>
                                                    <tr>
                                                        <td><img src="img/paso2.png" /></td>
                                                        <td>Haz clic encima del bot�n <span class='ui-icon ui-icon-pencil' style='display:inline-block'></span>de la barra de herramientas del cat�logo. <br />
                                                            La p�gina desplegar� una ventana con la informaci�n del registro seleccionado para que lo edites. </td>
                                                    </tr>
                                                    <tr>
                                                        <td><img src="img/paso3.png" /></td>
                                                        <td>Edita los campos necesarios,  aquellos marcados con (*) son obligatorios.</td>
                                                    </tr>    
                                                    <tr>
                                                        <td><img src="img/paso4.png" /></td>
                                                        <td>Presiona el bot�n "Guardar"; esto cerrara la ventana y actualizar� el registro.</td>
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
                                                <h1>Elimina un registro del cat�logo</h1>
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
                                                        <td>Selecciona del cat�logo el registro que deseas eliminar.</td>
                                                    </tr>    
                                                    <tr>
                                                        <td><img src="img/paso2.png" /></td>
                                                        <td>Haz clic encima del bot�n <span class='ui-icon ui-icon-trash' style='display:inline-block'></span>de la barra de herramientas del cat�logo. <br />
                                                            La p�gina solicitar� que confirmes la eliminaci�n.</td> 
                                                    </tr>
                                                    <tr>
                                                        <td><img src="img/paso3.png" /></td>
                                                        <td>Presiona OK en el dialogo de confirmaci�n para proceder con el borrado</td>
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
                                                <h1>Filtra los registros del cat�logo</h1>
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
                                                        <td>Haz clic encima del bot�n <span class='ui-icon ui-icon-search' style='display:inline-block'></span>de la barra de herramientas del cat�logo. <br />
                                                            La p�gina desplegar� una ventana para ingresar los criterios de filtrado</td> 
                                                    </tr>
                                                    <tr>
                                                        <td><img src="img/paso2.png" /></td>
                                                        <td>Ingresa al menos un criterio de filtrado; puedes seleccionar m�s de uno. Mientras m�s criterios utilices m�s reducida ser� el resultado de la b�squeda.</td>
                                                    </tr>        
                                                    <tr>
                                                        <td><img src="img/paso3.png" /></td>
                                                        <td>Si deseas guardar el filtro para su posterior uso, as&iacute;gnale un nombre en el campo <a href="javascript:void(0);" class="tooltipLink" id="img/guardar_filtro_como.png">Guardar filtro como</a>:"; recuperalo posteriormente en el men� <a href="javascript:void(0);" class="tooltipLink" id="img/mis_filtros.png">"Mis filtros"</a> que se encuentra a la izquierda del cat�logo. </td>
                                                    </tr>        
                                                    <tr>
                                                        <td><img src="img/paso4.png" /></td>
                                                        <td>Presiona el bot�n "Buscar"; esto cerrar� la ventana y mostrar� el resultado en el cat�logo. Para remover el filtro y restaurar los registros haz clic encima de la liga (Quitar filtro) que se encuentra junto al titulo del cat�logo." </td>
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
                                                <h3>Agrega a tus favoritos los cat�logos que utilizas m�s frecuentemente</h3>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <img src='img/favoritos.png'  class='helpScreen'/>
                                            </td>
                                            <td class='instrucciones'>
                                                <p class='instrucciones'>
                                                    Puedes agregar a la pesta�a de inicio los cat�logos que utilizas m�s frecuentemente en "Mis favoritos", para agregar un favorito haz lo siguiente:</p>
                                                <ol>
                                                    <li class='instrucciones'>Ve a la pesta�a "Aplicaciones"</li>
                                                    <li class='instrucciones'>Haz clic en el bot�n de la aplicaci�n que deseas abrir</li>
                                                    <li class='instrucciones'>Haz clic en el bot�n <span class='ui-icon ui-icon-star' style='display:inline-block'></span> de la barra de herramientas del cat�logo que deseas hacer tu favorito</li>
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
                                        <p class="instrucciones">Aqu� se muestran todas y cada una de las partes que componen al sistema para el perfil actual, seleccione la liga correspondiente para acceder al componente deseado.</p>
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
                                                <li>Inicio: en donde se encuentran las pesta�as "Pendientes" y "Favoritos".
                                                    <ul>
                                                        <li>En "Pendientes" puedes ver y atender tus actividades</li>
                                                        <li>En "Favoritos" puedes tener los cat�logos que uses con mayor frecuencia a la mano</li>
                                                    </ul>
                                                </li>
                                                <li>Aplicaciones: aqu� encontrar�s los m�dulos que conforman la plataforma. Cada aplicaci�n cuenta con un cat�logo principal y del cual se desprenden otros cat�logos relacionados en forma de kardex.</li>
                                                <li>Mapa del sitio: es el listado con las ligas hacia todos las partes de la plataforma.</li>
                                                <li>Ayuda: contiene el �ndice del manual en l�nea.</li>
                                                <li>Contacto: aqu� se presenta la informaci�n para contactar al administrador de la plataforma.</li>
                                            </ul>
                                        </p>
                                        <p class="instrucciones">
                                            Por otra parte, los cat�logos se presentan en una cuadr�cula y dependiendo de los permisos de tu perfil se mostrar�n botones para insertar <span class='ui-icon ui-icon-plus' style='display:inline-block'></span> , editar <span class='ui-icon ui-icon-pencil' style='display:inline-block'></span>, eliminar<span class='ui-icon ui-icon-trash' style='display:inline-block'></span>, hacer b�squedas de registros<span class='ui-icon ui-icon-search' style='display:inline-block'></span>, asi como abrir el kardex<span class='ui-icon ui-icon-newwin' style='display:inline-block'></span> con los cat�logos relacionados al registro seleccionado.
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
                                            Dentro de la pesta�a "Inicio", puedes encontrar "Pendientes" puedes ver las actividades por realizar as� como la liga para ingresar los datos propios del pendiente; cada perfil tiene sus propios pendientes, y son configurados por el administrador del sistema. <br /><br/>
                                        </p>
                                        <!-- <p class="instrucciones">
                                            <strong>Temas relacionados</strong>
                                            <ul>
                                               <li><a href="#" class="lnkAyuda" id="ayuda-4">Agrega a tus favoritos los cat�logos que m�s utilizas </a></li> 
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
                                                <li><a href="#" class="lnkAyuda" id="ayuda-8">Agrega un registro nuevo al cat�logo</a></li>
                                                <li><a href="#" class="lnkAyuda" id="ayuda-9">Edita un registro del cat�logo</a></li>
                                                <li><a href="#" class="lnkAyuda" id="ayuda-10">Elimina un registro del cat�logo</a></li>
                                                <li><a href="#" class="lnkAyuda" id="ayuda-11">Filtra los registros del cat�logo</a></li>
                                                <li><a href="#" class="lnkAyuda" id="ayuda-12">Agrega a tus favoritos los cat�logos que utilizas m�s frecuentemente</a></li>
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
                                            Aqu� se muestran todas y cada una de las partes que componen al sistema para el perfil actual, selecciona la liga correspondiente para acceder al componente deseado.
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
                                    <p class="instrucciones">Env�e un correo con sus sugerencias, comentarios, solicitudes y reportes de errores a <a href="mailto:daniel.martinez05@cfe.gob.mx">daniel.martinez05@cfe.gob.mx</a></p>
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
<% }%>