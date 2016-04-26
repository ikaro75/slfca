<%@ page import="mx.org.fide.modelo.*" %><%
  Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
  
  if (usuario==null) {
      //Crea usuario invitado para que sea posible registrarse
      Conexion cx = new Conexion();
      usuario = new Usuario();
      usuario.setCx(cx);
      usuario.setClave(2);
      request.getSession().setAttribute("usuario", usuario);
  }
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <title>empleox.com.mx - Bolsa de trabajo</title>

        <!-- librerias para cargar dialogo  -->
        <script type="text/javascript" src="jQuery/js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="jQuery/js/jquery-ui-1.8.21.custom.min.js"></script>

        <!-- Menu -->
        <script src="jQuery/js/jquery.ui.menu.js" type="text/javascript"></script>

        <!-- Carrousel -->
        <script type="text/javascript" src="jQuery/js/agile_carousel.alpha.js"></script>

        <link rel="stylesheet" type="text/css" media="screen" href="css/agile_carousel.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="css/redmond/jquery.ui.all.css" />
        <link rel="stylesheet" type="text/css" media="screen" href="css/jquery.tooltip.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/style.css"/>
        <link rel="stylesheet" type="text/css" media="screen" href="css/empleox.css"/>

        <!-- Tooltip -->
        <script src="jQuery/js/jquery.tooltip.min.js" type="text/javascript"></script>
        
        <script src="jQuery/js/funciones.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.field_toolbar.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.empleox.form.js" type="text/javascript"></script>
        <script src="jQuery/js/jquery.pi.formqueue.js" type="text/javascript"></script>
        
        <!--Datetime picker -->
        <script src="jQuery/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
        
        <script src="jQuery/js/empleox.js" type="text/javascript"></script>
        
    </head>
    <body id="top">
        <div class="main">
            <div id="roldiv">
                <a id="verytop"></a>
                <div id="banner" style="float: left;" ><img src="img/empleox_banner.png" /></div>
                <div id='divCarouselEmpleox'> 
                    <div id='carruselCandidato'>
                        <br />
                        <br />
                        <br />
                        <div id="tabsCandidato">
                            <ul>
                                <li><a href="#tabInicioCandidato"><span class="ui-icon ui-icon-home">Inicio</span></a></li>
                                <%if (usuario!=null) { %><li><a href="#tabDatosCandidato">Mis datos</a></li><% } %>
                                <div id="loginmenuCandidato" style="width: 100%">
                                    <div style="float: right; clear: right;"><button id="btnLoginCandidato">Iniciar sesión</button><button id="btnRegisterCandidato">Registro</button></div>
                                </div>                                
                            </ul>
                            <div id="tabInicioCandidato">
                                <ul>
                                    <li><a href="#tabBusquedaEmpleo">Búsqueda de empleo</a></li>
                                    <%if (usuario!=null) { %><li><a href="#tabMisPostulacionesCandidato">Mis postulaciones</a></li><% } %>
                                </ul>
                                <div id="tabBusquedaEmpleo">
                                    <table style="width: 100%">
                                        <tr>
                                            <td style="vertical-align:top; font-size: 13px;" colspan="2">
                                                <img src="img/encuentra_empleox.png" />
                                            </td>
                                        </tr>                                
                                        <tr>
                                            <td >
                                                <form id="formaBusquedaEmpleo">
                                                    <table style="width:100%">
                                                        <tr id="tr_empleo">
                                                            <td style="width: 20%">
                                                                Empleo buscado
                                                            </td>
                                                            <td style="width: 80%">
                                                                <input id="empleo" style="width: 100%" />
                                                            </td>                                                
                                                        </tr>
                                                        <tr id="tr_clave_estado">
                                                            <td style="width: 20%">
                                                                Estado
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_estado" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>    
                                                        <tr id="tr_clave_ciudad">
                                                            <td style="width: 20%">
                                                                Ciudad
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_ciudad" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>
                                                        <tr id="tr_clave_categoria">
                                                            <td style="width: 20%">
                                                                Categoría
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_categoria" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>                                            
                                                        <tr id="tr_clave_area">
                                                            <td style="width: 20%">
                                                                Área
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_area" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>
                                                        <tr id="tr_clave_sector">
                                                            <td style="width: 20%">
                                                                Sector
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_sector" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>                                            
                                                        <tr id="tr_w1">
                                                            <td style="width: 20%">
                                                                Salario
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="w1" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>                                            
                                                        <tr id="tr_w2">
                                                            <td style="width: 20%">
                                                                Fecha de publicacion
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="w2" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>                                            
                                                        <tr>
                                                            <td style="width: 20%">
                                                                &nbsp;
                                                            </td>
                                                            <td style="width: 80%; text-align:right">
                                                                <button id="btnBuscarEmpleos" >Buscar empleos</button><br /><a id="lnkBusquedaAvanzadaEmpleos" href="#" style="margin-top: 10px;" >M&aacute;s opciones</a>
                                                            </td>
                                                        </tr>                                                                                        
                                                    </table>
                                                </form>    
                                            </td>
                                        </tr>
                                    </table>
                                    <br />
                                    <hr style="width: 960px;" />                                     
                                    <div id="empleos_destacados">
                                        <a id="a_empleos_destacados"></a>
                                        <table>
                                            <tr>
                                                <td style="vertical-align:top; font-size: 13px;" colspan="2">
                                                    <img src="img/empleox_destacados.png" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="width: 200px; vertical-align:top; font-size: 13px;">
                                                    &nbsp;
                                                </td>
                                                <td style="width: 760px; ">
                                                    &nbsp;                            
                                                </td>                        
                                            </tr>
                                        </table>       
                                    </div>

                                    <br />
                                    <hr style="width: 960px;" />
                                    <div id="empleos_por_area">
                                        <a id="a_empleos_por_area" ></a>
                                        <table>
                                            <tr>
                                                <td style="vertical-align:top;" colspan="2">
                                                    <img src="img/empleox_por_area.png" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="width: 200px; vertical-align:top; ">
                                                    &nbsp;
                                                </td>
                                                <td style="width: 760px;">
                                                    &nbsp;
                                                </td>
                                            </tr>                    
                                        </table>   
                                    </div>                                    
                                </div>
                                <% if (usuario!=null) {%>
                                <div id="tabMisPostulacionesCandidato">
                                </div><% }%>    
                            </div>
                            <% if (usuario!=null) { %>
                            <div id="tabDatosCandidato">
                                <div id="tabCurriculumCandidato">
                                    <ul>
                                        <li><a href="#tabDatosGeneralesPostulante">Generales</a></li>
                                        <li><a href="#tabEstudiosPostulante">Estudios académicos</a></li>
                                        <li><a href="#tabIdiomasPostulante">Idiomas</a></li>
                                        <li><a href="#tabExperienciaPostulante">Experiencia profesional</a></li>
                                        <li><a href="#tabAreasExperienciaPostulante">Áreas de experiencia</a></li>
                                        <li><a href="#tabHabilidadesPostulante">Otras habilidades</a></li>                                        
                                    </ul>                                    
                                    <div id="tabDatosGeneralesPostulante"> &nbsp;</div> 
                                    <div id="tabEstudiosPostulante"></div>
                                    <div id="tabIdiomasPostulante"></div>
                                    <div id="tabExperienciaPostulante"></div>
                                    <div id="tabAreasExperienciaPostulante"></div>
                                    <div id="tabHabilidadesPostulante"></div>                                    
                                </div>
                            </div>
                            <% } %>
                        </div>    
                    </div>    
                    <br />
                    <hr style="width: 960px;" />                    
                    <div id='carruselEmpleador'>
                        <br />
                        <br />
                        <br />

                        <div id="tabsEmpleador">
                            <ul>
                                <li><a href="#tabInicioEmpleador"><span class="ui-icon ui-icon-home">Inicio</span></a></li>
                                <% if (usuario!=null) {%>
                                <li><a href="#tabDatosEmpleador">Mis datos</a></li>
                                <% } %>
                                <div id="loginmenuEmpleador" style="width: 100%">
                                    <div style="float: right; clear: right;"><button id="btnLoginEmpleador">Iniciar sesión</button><button id="btnRegisterEmpleador">Registro</button></div>
                                </div>                            
                            </ul>
                            <div id="tabInicioEmpleador">
                                <ul>
                                    <li><a href="#tabBusquedaCVs">Búsqueda de currículums</a></li>
                                    <% if (usuario!=null) {%><li><a href="#tabMisPostulacionesEmpleador">Mis postulaciones</a></li><% } %>
                                </ul>
                                <div id="tabBusquedaCVs">
                                    <table style="width: 100%">
                                        <tr>
                                            <td style="vertical-align:top; font-size: 13px;" colspan="2">
                                                <img src="img/encuentre_postulantes.png" />
                                            </td>
                                        </tr>                                 
                                        <tr>
                                            <td>
                                                <form id="formaBusquedaCvs">
                                                    <table style="width:100%">
                                                        <tr id="tr_titulo_cv">
                                                            <td style="width: 20%">
                                                                Palabra clave
                                                            </td>
                                                            <td style="width: 80%">
                                                                <input id="titulo_cv" style="width: 100%" />
                                                            </td>                                                
                                                        </tr>
                                                        <tr id="tr_w1">
                                                            <td style="width: 20%">
                                                                Edad
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="w1" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>    
                                                        <tr id="tr_clave_genero">
                                                            <td style="width: 20%">
                                                                Género
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_genero" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>
                                                        <tr id="tr_clave_area">
                                                            <td style="width: 20%">
                                                                Área de estudios
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_area" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>                                            
                                                        <tr id="tr_clave_nivel_estudio">
                                                            <td style="width: 20%">
                                                                Nivel de estudios
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_nivel_estudio" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>
                                                        <tr id="tr_institucion">
                                                            <td style="width: 20%">
                                                                Institución
                                                            </td>
                                                            <td style="width: 80%">
                                                                <input id="institucion" style="width: 100%" />
                                                            </td>                                                
                                                        </tr>
                                                        <tr id="tr_clave_idioma">
                                                            <td style="width: 20%">
                                                                Idioma
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_idioma" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>
                                                        <tr id="tr_clave_area_experiencia">
                                                            <td style="width: 20%">
                                                                Área de experiencia
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="clave_area_experiencia" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>                                                                                                                           
                                                        <tr id="tr_w2">
                                                            <td style="width: 20%">
                                                                Sueldo pretendido
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="w2" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>                                            
                                                        <tr id="tr_w3">
                                                            <td style="width: 20%">
                                                                Fecha de publicacion
                                                            </td>
                                                            <td style="width: 80%">
                                                                <select id="w3" style="width: 100%" >
                                                                    <option>Cualquiera</option>
                                                                </select>    
                                                            </td>                                                
                                                        </tr>                                            
                                                        <tr>
                                                            <td style="width: 20%">
                                                                &nbsp;
                                                            </td>
                                                            <td style="width: 80%; text-align:right">
                                                                <button id="btnBuscarCVs" >Buscar CVs</button><br /><a id="lnkBusquedaAvanzadaCVs" href="#" style="margin-top: 10px;" >M&aacute;s opciones</a>
                                                            </td>
                                                        </tr>                                                                                        
                                                    </table>
                                                </form>
                                            </td>
                                        </tr>
                                    </table>    
                                </div>
                                <% if (usuario!=null) {%>
                                <div id="tabMisPostulacionesEmpleador">
                                </div>
                                <% } %>
                            </div>
                            <% if (usuario!=null) {%>
                            <div id="tabDatosEmpleador">
                                <div id="tabPerfilEmpresa">
                                    <ul>
                                        <li><a href="#tabDatosGeneralesEmpresa">Generales</a></li>
                                        <li><a href="#tabAvisos">Avisos</a></li>
                                    </ul>                                    
                                    <div id="tabDatosGeneralesEmpresa"></div>
                                    <div id="tabAvisos"></div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>    
                <!--
                <div id="divlogin">
                    <form>
                        <table>
                            <tr><td>Correo electrónico</td><td><input type="text" id="email" value=""/></td></tr>
                            <tr><td>Contraseña</td><td><input type="text" id="pw" value=""/></td></tr>
                            <tr><td>&nbsp;</td><td><a href="#" id="olvidecontrasena">Olvidé mi contraseña</a></td></tr>
                            <tr><td>&nbsp;</td><td><button type="button" id="btnlogin" value="Iniciar sesión">Iniciar sesión</button></td></tr>
                        </table>
                    </form>
                </div> -->

                <hr style="width: 960px;" />
                <div id="legal">
                    <table width="100%" style="margin: 0 auto">
                        <tr>
                            <td><a href="#verytop">Ir arriba</a></td>
                            <td><a href="#nuestrasaplicaciones">Nuestras aplicaciones</a></td>
                            <td><a href="#acercade">Acerca de nosotros</a></td>
                            <td><a href="#contactanos">Contáctanos</a></td>
                            <td>&nbsp;</td>
                            <td>© Todos los derechos reservados 2012 AdministraX</td>
                        </tr>
                    </table>               
                </div>
            </div> 
    </body>
</html>
