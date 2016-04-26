<%@page import="java.io.File"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileOutputStream" %>
<%@page import="java.io.FileInputStream" %>
<%@page import="java.nio.channels.FileChannel"%>
<%@ page import="mx.org.fide.modelo.*" %>
<%@page import="mx.org.fide.backend.Aplicacion"%>
<%
    Aplicacion a = null;
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
    
    if (usuario==null) { 
        String url="/login.jsp".concat(request.getQueryString()==null?"":"?url=".concat(request.getServletPath()).concat(request.getQueryString()!=null?"?".concat(request.getQueryString()):"" ));
        request.getRequestDispatcher(url).forward(request, response);
        return;
    }
   
      //Abre página de aplicación
    String comandoQS="";
    if (request.getParameter("cqs")!=null) {
        String [] aComandoQS = request.getParameter("cqs").split("/"); 
        
        for (int i=0; i<aComandoQS.length; i++) {
            if (aComandoQS[i].split("=")[0].equals("app")) {
                a = new Aplicacion(Integer.parseInt(aComandoQS[i].split("=")[1]),usuario);
                comandoQS="category=".concat(String.valueOf(a.getClaveCategoria())).concat(";app=").concat(String.valueOf(a.getClaveAplicacion())).concat(";grid=").concat(String.valueOf(a.getClaveFormaPrincipal())).concat(";");
            }
            
            if (aComandoQS[i].split("=")[0].equals("insertform")) {
                if (a==null) 
                    comandoQS="error=Aplicación no definida, asegúrese de incluir el parámetro app primero";
                else 
                    comandoQS=comandoQS.concat("insertForm=0;");
            }
            
            if (aComandoQS[i].split("=")[0].equals("updateform")) {
                if (a==null) 
                    comandoQS= "error=Aplicación no definida, asegúrese de incluir el parámetro app primero";
                else 
                    comandoQS=comandoQS.concat("updateForm=").concat(aComandoQS[i].split("=")[1]).concat(";");
            }
            
            if (aComandoQS[i].split("=")[0].equals("foreignform")) {
                if (comandoQS.contains("updateform")) 
                    comandoQS= "error=Forma a editar no definida, asegúrese de incluir el parámetro updateform primero";
                else 
                    comandoQS=comandoQS.concat("foreignform=").concat(aComandoQS[i].split("=")[1]).concat(";");
            }
            
        }

    }
    
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title id="title">intranet 2.0 / ILCE</title>
        <link href="img/ilce.ico" type="image/x-icon" rel="shortcut icon" />

        <!-- librerias para cargar dialogo  --> 
        <script type="text/javascript" src="jQuery/js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.21.custom.min.js"></script>

        <!-- app portlets -->
        <script type="text/javascript" src="jQuery/js/jquery.appportlet.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.portletlog.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.portletfilter.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.promocion.portletreport.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.profiler.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.portletcontact.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.portletgroup.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.portletdocument.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.reportParameterForm.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.cqs.js" ></script>
        <!-- Cookie -->
        <script type="text/javascript"  src="jQuery/js/jquery.cookie.js" ></script>

        <!-- jqGrid -->
        <script type="text/javascript" src="jQuery/js/grid.locale-es.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.jqGrid.min.js"></script>
        <script type="text/javascript" src="jQuery/js/grid.subgrid.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.jstree.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.linkpreview.js"></script>


        <!--Datetime picker -->
        <script src="jQuery/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>

        <!-- Calculator -->
        <script type="text/javascript" src="jQuery/js/jquery.calculator.min.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.calculator-es.js" ></script>

        <!-- Menu -->
        <script src="jQuery/js/jquery.ui.menu.js" type="text/javascript"></script>

        <!-- Autocomplete -->
        <script type="text/javascript" src="jQuery/js/combobox.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.core.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.widget.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.button.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.position.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.ui.autocomplete.js"></script>

        <!-- Carrousel -->
        <script type="text/javascript" src="jQuery/js/agile_carousel.alpha.js"></script>

        <!-- Agenda -->
        <script type="text/javascript" src="jQuery/js/fullcalendar.js"></script>

        <!-- Tooltip -->
        <script src="jQuery/js/jquery.tooltip.min.js" type="text/javascript"></script>

        <!-- form plugin para considerar uploads  -->
        <script type="text/javascript" src="jQuery/js/jquery.form.js"></script>

        <script src="jQuery/js/backend.js" type="text/javascript"></script>
        <script src="jQuery/js/promocion.js" type="text/javascript"></script>
        <script src="jQuery/js/recursos.js" type="text/javascript"></script>
        <script type="text/javascript" src="jQuery/js/alertmanager.js"></script>
        <script type="text/javascript" src="jQuery/js/cambia_password.js" ></script>
        <script type="text/javascript" src="jQuery/js/documento.js" ></script>
        <script type="text/javascript" src="jQuery/js/grupos.js" ></script>
        <script type="text/javascript" src="jQuery/js/jquery.pi.post.js" ></script>

        <link rel="stylesheet" type="text/css" media="screen" href="css/agile_carousel.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="css/start/jquery.ui.all.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jqGrid/css/ui.jqgrid.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/jquery.tooltip.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/style.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/vista.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/calculator/jquery.calculator.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="jQuery/js/jwysiwyg-master/jquery.wysiwyg.css" />
        <link rel='stylesheet' type="text/css" media="screen" href='css/fullcalendar.css'  />
        <link rel='stylesheet' type="text/css" media="print"  href='css/fullcalendar.print.css'   />

        <script src="jQuery/js/funciones.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.promocion.desktop.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.promocion.gridqueue.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.field_toolbar.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.promocion.session.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.form.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.formqueue.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.accordion.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.tab.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.grid.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.promocion.appmenu.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.comments.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.wall.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.timeago.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.timeago.es.js" type="text/javascript"></script>

        <script type="text/javascript" src="jQuery/js/jwysiwyg-master/jquery.wysiwyg.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.autosize-min.js"></script>
        <script type="text/javascript" src="jQuery/js/sets/html/set.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery.themeswitcher.min.js"></script>
        <script src="jQuery/js/vistapromocion.js" type="text/javascript"></script>

    </head>
    <body id="top">

        <div id="banner" >
            <div style="float: left; position: relative;">
                <img src="img/logo_intranet_4.png"  />
            </div>
            <div id="sessionMenu" style="float: right; position: relative; "></div>
        </div>
        <div id="tabcontainer" style="display: inline-block; width: 100%; margin-top: -15px;">
            <div id="tabs" >
                <ul >
                    <li><a href="#tabInicio"><span class="ui-icon ui-icon-home" style="float: left;"></span>Inicio </a></li>
                    <li><a href="#tabComunidad"><span class="ui-icon ui-icon-contact" style="float: left;"></span>Comunidad </a></li>
                    <li><a href="#tabPromocion"><span class="ui-icon ui-icon-tag" style="float: left;"></span>Promoci&oacute;n </a></li>
                    <li><a href="#tabPoliticasyProcesos"><span class="ui-icon ui-icon-script" style="float: left;"></span> Pol&iacute;ticas y procesos </a></li>
                    <li><a href="#tabFormatosInstitucionales"><span class="ui-icon ui-icon-document" style="float: left;"></span> Formatos institucionales</a></li>
                </ul>
                <div style="padding-right: 5px; float: right; padding-top: 3px; position: relative; top: -30px; width:350px">
                    <div id="switcher" style="float:right; top:0px"><a href='http://siap.ilce.edu.mx/plataforma' target='_blank' class="menu_link"><img src="/intranet/img/logo_siap.png" style="width: 20px; vertical-align: middle;" />  SIAP 2.0</a> <span style="color:#fff">|</span> <a href='http://correo.ilce.edu.mx' target='_blank' class="menu_link"><span class="ui-icon ui-icon-mail-closed" style="display: inline-block;"></span> Correo</a></div>
                </div>
                <div id="tabInicio">
                  <div id="tab_0_0" style="display: inline-block;" >
                        <ul>
                            <li><a href="#tab_2_29_299_0">&Uacute;ltimas noticias</a></li>
                        </ul>
                        <div id="tab_2_29_299_0">
                            <div id="divCompartir" >
                                <div id="divMuro" style="clear:both">
                                    <br />
                                    <br />
                                    <br />
                                    <br /><div style='margin:0 auto 0 auto; width:100%; text-align: center'>Cargando <img src='img/loading.gif' /></div>
                                </div>
                            </div>                                
                        </div> 
                    </div>
                </div>
                <div id="tabComunidad">
                    <div id="appMenu_2_0" aplicacion="0" class="appmenu"></div><br/>
                    <div id="tab_2_0" style="height: 150%;">
                        <ul>
                            <li><a href='#colaboracion'>Colaboraci&oacute;n</a></li>
                        </ul>
                        <div id='colaboracion'>
                                <div style="width: 81%; float: left; margin-right: 10px"><div id="agenda"></div></div>
                                <div class="column ui-sortable">
                                <div class="portlet">
                                    <div class="portlet-header">Avisos</div>
                                    <div class="portlet-content">
                                        <p>Bienvenid@ al Intranet de ILCE</p>
                                    </div>
                                </div>
                                <div class="portlet">
                                    <div class="portlet-header">Contactos</div>
                                    <div class="portlet-content" id='portletContacto'>
                                    </div>
                                </div>
                                <div class="portlet">
                                    <div class="portlet-header">Grupos de contactos</div>
                                    <div class="portlet-content" id='portletGrupo'>
                                    </div>
                                </div>
                                <div class="portlet">
                                    <div class="portlet-header">Mis documentos</div>
                                    <div class="portlet-content" id='portletDocumento'>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>    
                </div>                   
                <div id="tabPromocion">
                    <div id="tab_1_34" style="display: inline-block; width: 100%;">
                        <ul><li><a href="#appTab_1_34_314">Grupos de inter&eacute;s</a></li></ul>
                        <div id="appTab_1_34_314">
                            <div class="column ui-sortable">
                                <div class="portlet" id="reportPortlet_34_314_0" type="template">
                                    <div class="portlet-header">Reportes</div>
                                    <div class="portlet-content" style="word-wrap: break-word; overflow: auto; height: 87%;">
                                        <p></p>
                                    </div>
                                </div>    
                                <div class="portlet" id="filterPortlet_34_314_0" type="template">
                                    <div class="portlet-header">Mis filtros</div>
                                    <div class="portlet-content" style="word-wrap: break-word; overflow: auto; height: 87%;">
                                        <p></p>
                                    </div>
                                </div>
                                <div class="portlet" id="kitPortlet_34_314_0" type="template">
                                    <div class="portlet-header">Kit promocional</div>
                                    <div class="portlet-content" style="word-wrap: break-word; overflow: auto; height: 87%;">
                                        <div class="reporte"><a href="/intranet/promocion/Catalogo_ILCE_Radio_y_TV.pdf" target="_blank" class="lnkKit">Cat&aacutelogo ILCE - Radio y TV</a></div>
                                        <div class="reporte"><a href="/intranet/promocion/ILCE_presentacion(2013).pptx" target="_blank" class="lnkKit">ILCE Presentacion (2013)</a></div>
                                        <div class="reporte"><a href="/intranet/promocion/Productos Radio y TV ILCE.m4v" target="_blank" class="lnkKit">Productos de Radio y TV</a></div>
                                        <div class="reporte"><a href="/intranet/promocion/PROMO MAESTRIA MDHYSP.m4v" target="_blank" class="lnkKit">Promo Maestr&iacute;a MDHYSP</a></div>
                                        <div class="reporte"><a href="/intranet/promocion/VIDEO INSTITUCIONAL 2013.m4v" target="_blank" class="lnkKit">Video Institucional 2013</a></div>
                                    </div>
                                </div>  
                            </div>
                            <div id="entityCaruosel_34_314_0" class="entityCarrousel">
                                <div id="divgrid_34_314_0" app="34" form="314" indesktop="true" openkardex="false"  leyendas='Nuevo grupo de inter&eacute;s, Editar grupo de inter&eacute;s' class="queued_grids"></div>
                            </div>                                
                        </div>
                   </div>
                 </div>
                <div id="tabPoliticasyProcesos">
                    
                </div>
                <div id="tabFormatosInstitucionales">
                    <div>
                        <ul>
                            <li>Formatos institucionales
                                <ul>
                                    <li><a href="/documentos/formato basico.docx" target="_blank">Formato básico</a></li>
                                    <li><a href="/documentos/FORMATO DE ORDEN DEL DIA.doc" target="_blank">Formato de orden del día</a></li>
                                    <li><a href="/documentos/FORMATO PARA MINUTAS.doc" target="_blank">Formato de orden del día</a></li>
                                    <li><a href="/documentos/FORMATO PROPUESTAS TECNICO-ECONOMICA.doc" target="_blank">Formato de propuestas técnico-económicas</a></li>
                                 </ul>
                                <ul>
                                    <li><a href="/documentos/formato basico.docx" target="_blank">Formato básico</a></li>
                                    <li><a href="/documentos/FORMATO DE ORDEN DEL DIA.doc" target="_blank">Formato de orden del día</a></li>
                                    <li><a href="/documentos/FORMATO PARA MINUTAS.doc" target="_blank">Formato de orden del día</a></li>
                                    <li><a href="/documentos/FORMATO PROPUESTAS TECNICO-ECONOMICA.doc" target="_blank">Formato de propuestas técnico-económicas</a></li>
                                 </ul>
                            </li>
                            <li>Documentos jurídicos
                                 <ul>
                                    <li><a href="/documentos/Acuerdo de Sede ILCE.pdf" target="_blank">Acuerdo de SEDE ILCE</a></li>
                                    <li><a href="/documentos/Convenio_de cooperacion ILCE.pdf" target="_blank">Convenio de colaboración ILCE</a></li>
                                    <li><a href="/documentos/CONVENIO_MARCO DE COLABORACION.doc" target="_blank">Convenio de marco de colaboración</a></li>
                                    <li><a href="/documentos/REQUERIMIENTOS A GPOS DE INTERES PARA CONVENIO MARCO.pdf" target="_blank">Requirimientos a Grupos de Interés para Convenio Marco</a></li>
                                 </ul>                                
                            </li>
                       </ul>     
                    </div>                    
                </div>    
                <div id ="footer" ></div>
            </div>
            <input type="hidden" name="_ce_" id="_ce_" value="<%=usuario.getClave()%>" />
            <input type="hidden" name="_cp_" id="_cp_" value="<%=usuario.getClavePerfil()%>" />
            <input type="hidden" name="_cp_" id="_ca_" value="<%=usuario.getClaveArea()%>" />
            <input type="hidden" name="_gq_" id="_gq_" value="" />
            <input type="hidden" name="_cache_" id="_cache_" value="" />
            <input type="hidden" name="_gado_" id="_gado_" value="true" />
            <input type="hidden" name="_refri_" id="_refri_" value="<%=comandoQS%>" />
            <input type="hidden" name="_status_" id="_status_" value="" />
        </div>
        <div id="divwait" title="Espere un momento, por favor"><br /><p style="text-align: center"><img src='img/throbber.gif' /><br /><br />&nbsp;Cargando preferencias del usuario</p></div>
    </body>
</html>
