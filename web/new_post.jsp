<?xml version="1.0" encoding="UTF-8"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="java.util.ArrayList"
%><%@ page import="mx.org.fide.modelo.*"
%><%@page import="mx.org.fide.backend.Nota"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat" 
%><%@page import="mx.org.fide.reporte.Reporte"
%><%@page import="java.io.File"
%><%@page import="java.io.FileOutputStream" 
%><%@page import="java.io.FileInputStream" 
%><%@page import="java.nio.channels.FileChannel"
%><%@page import="java.util.LinkedHashMap"
%><%
//response.setContentType("text/xml"); 

    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");
    
    Usuario user = (Usuario) request.getSession().getAttribute("usuario");

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }
    
    String error = "";
    int forma = 299;
    int pk = 0;
    String tipoAccion = "select";
    String w = "";
    String source = "";
    Usuario usuario = null;
    Integer pagina = 1;  
    Integer numeroDeRegistros = 20;
    StringBuilder sidx = new StringBuilder("");
    String sord = "desc";
    Forma frmTemp = new Forma();
    LinkedHashMap<String, Campo> campos = new LinkedHashMap<String, Campo>();;
    ArrayList<ArrayList> registros = new ArrayList<ArrayList>();
    Consulta cnsPublicaciones = new Consulta();
    Consulta cnsPublicacionesHijas = new Consulta();
    usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
        request.getRequestDispatcher("/index.jsp");
    }
        
    try {

       if (request.getParameter("$cf") == null) {
            throw new Fallo("Falta parámetro $cf");
        } else {
            try {
                forma = Integer.parseInt(request.getParameter("$cf"));
            } catch (Exception e) {
                throw new Fallo("El parámetro $cf no es válido, verifique");
            }
        }
                
        if (request.getParameter("$w") != null) {
            w = request.getParameter("$w");
        }

        try {
            if (request.getParameter("page") != null) {
                pagina = Integer.parseInt(request.getParameter("page"));
            }
        } catch (Exception e) {
            throw new Fallo("El parámetro page no es válido, verifique");
        }

        try {
            if (request.getParameter("rows") != null) {
                numeroDeRegistros = Integer.parseInt(request.getParameter("rows"));
            }
        } catch (Exception e) {
            error = "El parámetro rows no es válido, verifique";
        }

        if (request.getParameter("sidx") != null) {
           sidx.append(request.getParameter("sidx"));
        }

        if (request.getParameter("sord") != null) {
            sord = request.getParameter("sord");
        }
        
        cnsPublicaciones = new Consulta(forma,tipoAccion, String.valueOf(pk), w, usuario, numeroDeRegistros, pagina, sidx.toString(), sord);
        frmTemp = new Forma(cnsPublicaciones, false);  

        source = cnsPublicaciones.getSQL();
        campos = cnsPublicaciones.getCampos();
        registros = cnsPublicaciones.getRegistros();

    } catch (Fallo f) {
        error = f.getMessage();

    } catch (Exception e) {
        error = e.getMessage();
    } finally {%><qry>
    <sql><![CDATA[<%=source%>]]></sql><%
        if (error == null) {
            error = "";
        }
        if (!error.equals("")) {%>
    <error><![CDATA[<%=error%>]]></error>
</qry><%
            return;
        }
    }
    
    
double total_paginas=0;
      
 if( cnsPublicaciones.getNumeroDeRegistros() >0 ) {
	total_paginas = Math.ceil(cnsPublicaciones.getNumeroDeRegistros()/numeroDeRegistros);
        if ((cnsPublicaciones.getNumeroDeRegistros() % numeroDeRegistros)>0)
                total_paginas+=1;
} else {
	total_paginas = 0;
}
%>
<permisos><% if (frmTemp.isSelect()) {%>
    <permiso><clave_permiso>1</clave_permiso></permiso><%}
        if (frmTemp.isInsert()) {%>
    <permiso><clave_permiso>2</clave_permiso></permiso><%}
        if (frmTemp.isUpdate()) {%>
    <permiso><clave_permiso>3</clave_permiso></permiso><%}
        if (frmTemp.isDelete()) {%>
    <permiso><clave_permiso>4</clave_permiso></permiso><%}
        if (frmTemp.isSensitiveData()) {%>
    <permiso><clave_permiso>5</clave_permiso></permiso><% }%>
</permisos>
<pagina><%=pagina%></pagina>
<total><%= total_paginas %></total>
<registros><%=cnsPublicaciones.getNumeroDeRegistros()%></registros><%
int i = 0;
 
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        NumberFormat nfMoney = new DecimalFormat("$###,###,###,##0.00");

        String data;
        Usuario empleado = new Usuario();
        for (ArrayList registro : registros) {
            int k = 0;
            empleado.setCx(usuario.getCx());
            empleado.setClave(Integer.parseInt(registro.get(2).toString()));
            
    %>
<html><![CDATA[
    <div id="comentario_<%=registro.get(0)%>" class="post">
    <table class="forma">
        <tbody>
            <tr>
                <td style="vertical-align:top; width:60px;"><img src="<%=empleado.getFoto()%>" style="width:50px; height:58px !important" border="1" /></td>
                <td style="vertical-align:top;">
                    <strong><a href="#" id="contactProfile_<%=empleado.getClave()%>" class="lnkContactProfile"><%=empleado.getNombre().concat(" ").concat(empleado.getApellidos())%></a></strong>
                    <abbr class="timeago" title="<%=dtf.format(registro.get(6))%>"><%=dtf.format(registro.get(6))%></abbr>
                    <br />
                    <div >
                        <%=registro.get(3)%>
                    </div>
                </td>
                <td style="vertical-align:top; width:30px">
                    <div style="float:right">
                        <div title="Eliminar comentario" style="cursor: pointer; float: right; display: none;" class="closeLnkFiltro ui-icon ui-icon-close" pk="<%=registro.get(0).toString()%>"></div>
                    </div>
                </td>
            </tr>
            <tr style="margin-bottom:5px;">
                <td>&nbsp;</td>
                <td><a class="comentar_link" id="lnkcomentar_<%=registro.get(0).toString()%>" href="#">Comentar</a>&nbsp;|&nbsp;<a class="compartir_link" id="lnkcompartir_<%=registro.get(0).toString()%>" href="#">Compartir</a></td>
                <td>&nbsp;</td>
            </tr>
            <% // aqui va código para incluir las publicaciones hijas  
                cnsPublicacionesHijas = new Consulta(357,tipoAccion, String.valueOf(pk), "clave_publicacion_padre=".concat(registro.get(0).toString()), usuario);
                for (ArrayList registroHijo : cnsPublicacionesHijas.getRegistros()) {
                    empleado.setClave(Integer.parseInt(registroHijo.get(2).toString()));
                    %><tr class="trrespuesta_<%=registro.get(0).toString()%> trrespuesta" >
                        <td>&nbsp;</td>
                        <td>
                            <table class="forma ui-state-highlight" style="margin-left: 0px;">
                            <tbody>
                                <tr>
                                    <td style="vertical-align: top; width:60px;"><img src="<%=empleado.getFoto()%>" style="width:50px; height:58px !important" border="1"></td>
                                    <td><strong><%=empleado.getNombre()%></strong> <abbr class="timeago" title="<%=dtf.format(registro.get(4))%>"><%=dtf.format(registro.get(4))%></abbr><br><div style="width: 98%; height: 20px; overflow: hidden; word-wrap: break-word; resize: horizontal;"><%=registroHijo.get(3).toString()%></div></td>
                                </tr>
                            </tbody>
                            </table>
                        </td>
                      </tr><%
                }    
            %>
            <tr id="trrespuesta_<%=registro.get(0).toString()%>" class="trrespuesta_<%=registro.get(0).toString()%> trrespuesta" style="display: none;">
                <td>&nbsp;</td>
                <td style="padding: 0px;">
                    <form class="forma" id="replyForm_195_<%=registro.get(0).toString()%>" name="replyForm_195_<%=registro.get(0).toString()%>" method="POST">
                        <table class="forma ui-state-highlight" style="margin-left: 0px;">
                            <tbody>
                                <tr>
                                    <td style="vertical-align: top; width:60px;">
                                        <img src="<%=usuario.getFoto()%>" style="width:50px; height:58px !important" border="1" />
                                    </td>
                                    <td><strong><%=usuario.getNombre()%></strong><br /><textarea class="replycomment" id="respuesta_<%=registro.get(0).toString()%>" name="respuesta_<%=registro.get(0).toString()%>" style="width: 98%; height: 20px; overflow: hidden; word-wrap: break-word; resize: horizontal;"></textarea>
                                    </td>
                                </tr>
                                <tr style="display:none;">
                                    <td>&nbsp;</td>
                                    <td>
                                        <input type="hidden" id="clave_empleado" name="clave_empleado" value="<%=usuario.getClave()%>"><input type="hidden" id="clave_estatus" name="clave_estatus" value="1">
                                        <input type="hidden" id="fecha" name="fecha" value="%ahora">
                                        <input type="hidden" id="clave_publicacion_padre" name="clave_publicacion_padre" value="<%=registro.get(0).toString()%>">
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </td>
            </tr>
        </tbody>
    </table>
  </div>
<% } %><% if (pagina<total_paginas) { %><div style="text-align:center; width:100%; margin-left:auto; margin-right:auto;"><button class="btnPaginador" forma="<%=forma%>" pagina="<%=pagina+1%>">Ver más publicaciones</button></div><% } %>]]></html>    
</qry><%
        /* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
  request.getSession().setAttribute("usuario", usuario);
%>