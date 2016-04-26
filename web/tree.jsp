<?xml version='1.0' encoding='ISO-8859-1'?>
<%@page contentType="text/xml; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
%><%@page import="java.util.ArrayList" 
%><%@page import="mx.org.fide.modelo.*" 
%><%@page import="java.text.DecimalFormat"
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.util.LinkedHashMap"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="mx.org.fide.controlador.Sesion"
%><%@page import="mx.org.fide.reporte.Reporte"%><%

    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("ISO-8859-1");

    String error = "";
    int forma = 0;;
    String tipoAccion = "";
    String dp = "body";
    String w = "";
    String source = "";
    Integer pagina = 1;
    Integer registros = 50;
    StringBuilder sidx = new StringBuilder("");
    String sord = "desc";
    Forma frmTemp = new Forma();
    Integer nodo =0;
    Integer nivel = 0;
        
    LinkedHashMap <String,Campo> campos;
    ArrayList<ArrayList> aRegistros;

    int pk = 0;

    Usuario user = (Usuario) request.getSession().getAttribute("usuario");

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }

    try {
        if (request.getParameter("$cf") != null) {
            try {
                forma = Integer.parseInt(request.getParameter("$cf"));
            } catch (Exception e) {
                throw new Fallo("El parámetro $cf no es válido, verifique");
            }
        } else {
            throw new Fallo("Falta parámetro $cf");
        }

        if (request.getParameter("$ta") == null) {
            throw new Fallo("Falta parámetro $ta");
        } else {
            tipoAccion = request.getParameter("$ta");
        }

        if (request.getParameter("$pk") != null) {
            try {
                pk = Integer.parseInt(request.getParameter("$pk"));
            } catch (Exception e) {
                throw new Fallo("El parámetro $pk no es válido, verifique");
            }
        }

        if (request.getParameter("$w") != null) {
            w = request.getParameter("$w");
        }

        if (request.getParameter("$dp") != null) {
            dp = request.getParameter("$dp");
        }
        
         if (request.getParameter("nodeid") == null || request.getParameter("nodeid").equals("")) {
             nodo =0;
         } else {
             try {
                nodo = Integer.parseInt(request.getParameter("nodeid"));
             } catch (Exception e) {
                throw new Fallo("El parámetro nodeid no es válido, verifique");
            }
        }

        if (request.getParameter("n_level") != null && !request.getParameter("n_level").equals("")) {
             try {
                nivel = Integer.parseInt(request.getParameter("n_level"))+1;
                                
             } catch (Exception e) {
                throw new Fallo("El parámetro n_level no es válido, verifique");
            }
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
                registros = Integer.parseInt(request.getParameter("rows"));
            }
        } catch (Exception e) {
            error = "El parámetro rows no es válido, verifique";
        }

        if (request.getParameter("sidx") != null) {
            String[] aSidx = request.getParameter("sidx").toString().split("_");
            for (int i = 0; i < aSidx.length - 3; i++) {
                sidx.append(aSidx[i]);
                if (i < aSidx.length - 4) {
                    sidx.append("_");
                }
            }
        }

        if (request.getParameter("sord") != null) {
            sord = request.getParameter("sord");
        }

        frmTemp = new Forma(new Consulta(forma, tipoAccion, String.valueOf(pk), w, user, registros, pagina, sidx.toString(), sord, nodo),false);
        frmTemp.setDefinicionDelGrid(dp.equals("header") ? true : false);
        source = frmTemp.getSQL();
        error = "";
    } catch (Fallo f) {
        error = f.getMessage();

    } catch (Exception e) {
        error = e.getMessage();
    } finally {
%><qry>
    <sql><![CDATA[<%=source%>]]></sql>
    <%
        if (error == null) {
            error = "";
        }
        if (!error.equals("")) {%>
    <error><![CDATA[<%=error%>]]></error>
</qry><%
            return;
        }
    }
%><permisos><% if (frmTemp.isSelect()) {%>
    <permiso><clave_permiso>1</clave_permiso></permiso><%}
        if (frmTemp.isInsert()) {%>
    <permiso><clave_permiso>2</clave_permiso></permiso>    <%}
        if (frmTemp.isUpdate()) {%>
    <permiso><clave_permiso>3</clave_permiso></permiso><%}
        if (frmTemp.isDelete()) {%>
    <permiso><clave_permiso>4</clave_permiso></permiso><%}
        if (frmTemp.isReport()) {%>
    <permiso><clave_permiso>6</clave_permiso></permiso><% }%>
</permisos>
<configuracion_grid>
    <alias_tab><![CDATA[<%=frmTemp.getAliasTab()%>]]></alias_tab>
    <evento_grid tipo=""><![CDATA[<%=frmTemp.getEventoGrid()%>]]></evento_grid>
    <instrucciones><![CDATA[<%=frmTemp.getInstrucciones()%>]]></instrucciones>
    <forma><![CDATA[<%=frmTemp.getForma()%>]]></forma>
</configuracion_grid><%
    if (frmTemp.isReport()) {
%><reportes><% /*Recupera de la base de datos los reportes y los coloca en la propiedad reporte de la forma del usuario*/
                ArrayList <Reporte> reportes = frmTemp.getReportes();

        //Se ejecuta el primer reporte de la lista por default
        for (Reporte r : reportes) {
%><reporte id="<%=r.getClaveReporte()%>"><![CDATA[<%=r.getReporte()%>]]></reporte><%
        }
%></reportes>
<%}

    try {
        campos = frmTemp.getCampos();
        aRegistros = frmTemp.getRegistros();
    } catch (Exception e) {
        error = e.getMessage();%>
<error><%=error%></error></qry>
<% return;
    }

    double total_paginas = 0;

    if (frmTemp.getNumeroDeRegistros() > 0) {
        total_paginas = Math.ceil(frmTemp.getNumeroDeRegistros() / registros);
        if ((frmTemp.getNumeroDeRegistros() % registros) > 0) {
            total_paginas += 1;
        }
    } else {
        total_paginas = 0;
    }
    
    Integer campoPadre =0;
    for (Campo campo : campos.values()) {
        int k = 0;
    }
%>
<rows>
    <page><%=pagina%></page>
    <total><%= total_paginas%></total>
    <records><%=frmTemp.getNumeroDeRegistros()%></records>
    <column_definition><%
        for (Campo campo : campos.values()) {
            int l = 0;
        %>
        <<%=campo.getNombre()%> tipo_dato="<%=campo.getTipoDato()%>">
        <alias_campo><![CDATA[<%=campo.getAlias()%>]]></alias_campo>
        <obligatorio><%=campo.getObligatorio()%></obligatorio>
        <tipo_control><![CDATA[<%=campo.getTipoControl()%>]]></tipo_control>
        <evento><![CDATA[<%=campo.getEvento()%>]]></evento>
        <clave_forma_foranea><%=campo.getClaveFormaForanea()%></clave_forma_foranea>
        <filtro_foraneo><%=campo.getFiltroForaneo()%></filtro_foraneo>
        <edita_forma_foranea><%=campo.getEditaFormaForanea()%></edita_forma_foranea>
        <no_permitir_valor_foraneo_nulo><%=campo.getNoPermitirValorForaneoNulo()%></no_permitir_valor_foraneo_nulo>
        <ayuda><![CDATA[<%=campo.getAyuda()%>]]></ayuda>
        <dato_sensible><%=campo.getDatoSensible()%></dato_sensible>
        <activo><%=campo.getActivo()%></activo>
        <tamano><%=campo.getTamano()%></tamano>
        <visible><%=campo.getVisible()%></visible>
        <valor_predeterminado><![CDATA[<%=campo.getValorPredeterminado()%>]]></valor_predeterminado>
        <justificar_cambio><%=campo.getJustificarCambio()%></justificar_cambio>
        <usado_para_agrupar><%=campo.getUsadoParaAgrupar()%></usado_para_agrupar>
        </<%=campo.getNombre()%>><%
                l++;
            }
        %>
    </column_definition><%
        int k = 0;
        
        /*if (request.getParameter("n_level")!=null) {
                    nivel=nivel+1;
        }*/
        
        if (!dp.equals("header")) {
            NumberFormat nfMoney = new DecimalFormat("$###,###,###,##0.00");
            NumberFormat nfReal = new DecimalFormat("###,###,###,##0.00");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            
             for (ArrayList registro : aRegistros) {
    %><row><%
        k = 0;
        String resultado = "";
        Object aKeySet[]=campos.keySet().toArray();
        Boolean esHoja = false;
        String clavePadre="";
        for (Object dato : registro) { 
            if (campos.get(aKeySet[k]).getNombre().equals("_hijos")) {
                if (Integer.parseInt(dato.toString())==0) 
                    { esHoja=true; }
               else { esHoja=false;}
                
            }
            
           if (aKeySet[k].equals(frmTemp.getLlavePrimaria().concat("_padre"))) {
                if (dato==null) {
                    clavePadre="NULL";
                } else {
                    clavePadre = dato.toString();
                } 
            }
                        
            if (dato != null) {
                if (k>=aKeySet.length) {
                    break;
                }
                
                if (campos.get(aKeySet[k]).getTipoDato().equals("money")) {
                    resultado = dato.toString();//nfMoney.format(dato);
                } else if (campos.get(aKeySet[k]).getTipoDato().equals("smalldatetime")) {
                    resultado = sdf.format(dato);
                } else if (campos.get(aKeySet[k]).getTipoDato().equals("datetime")) {
                    resultado = sd.format(dato);
                } else if (request.getParameter("$removeTags") != null) {//Elimina las ligas de la descripción de actividad
                    resultado = dato.toString().replaceAll("\\<a.*?>", "").replaceAll("\\</a>", "");
                } else {
                    if (dato.toString().startsWith("$estatus_online")) {
                        String  claveUsuario=dato.toString().split(" ")[0].split("_")[2];
                        //Se recupera de la sesión el estatus del usuario
                        Sesion sesion = (Sesion) request.getSession().getServletContext().getAttribute("sesion");
                        dato="<div class='divChatContact' estatus='offline' clave='".concat(claveUsuario).concat("'>").concat(dato.toString().replaceAll("\\$estatus_online_".concat(claveUsuario),"<img src='img/offline.png' />")).concat("</div>");
                        resultado=dato.toString();
                        if (sesion==null) {
                            resultado = dato.toString().replaceAll("\\$estatus_online_".concat(claveUsuario),"<img src='img/offline.png' />");
                        } else {
                            Object[] aClaveUsuario = sesion.getUsuarios().values().toArray();
                            for (int i=0; i<aClaveUsuario.length; i++) {
                                Usuario temp =(Usuario) aClaveUsuario[i];
                                if (temp.getClave()==Integer.parseInt(claveUsuario)) {
                                    dato=dato.toString().replaceAll("offline","online");
                                    resultado = dato.toString();
                                    break;
                                }
                            }                            
                        }
                        

                    } else {
                        resultado = dato.toString();
                    }
                    
                }
            } else {
                resultado = "";
            }
            if (!campos.get(aKeySet[k]).getNombre().equals("_hijos")) {%>
        <cell><![CDATA[<%=resultado%>]]></cell><% }
                k++;
        }
        
        %><cell><%=nivel%></cell>
        <cell><![CDATA[<%=clavePadre%>]]></cell><%
        if (esHoja) { 
	%><cell>true</cell><%
        } else {%><cell>false</cell><% }%>
          <cell>false</cell>
          </row><%
            }
        }
    %></rows>
</qry>
