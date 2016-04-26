<?xml version='1.0' encoding='ISO-8859-1'?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"
%><%@page import="java.util.ArrayList" 
%><%@page import="java.util.Map"
%><%@page import="mx.org.fide.modelo.*" 
%><%@page import="java.text.DecimalFormat"
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.SimpleDateFormat"
%><%@page import="mx.org.fide.reporte.*"
%><%@page import="java.util.LinkedHashMap"
%><%@page import="mx.org.fide.backend.Forma"%><% 

    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("ISO-8859-1");

    String error = ""; 
    int forma = 0;;
    String tipoConsulta = "";
    String dp = "body";
    String w = "";
    String source = "";
    Integer pagina = 1;  
    Integer registros = 50;
    StringBuilder sidx = new StringBuilder("");
    String sord = "desc";
    Forma frmTemp = new Forma();
    LinkedHashMap <String,Campo> campos;
    ArrayList<ArrayList> aRegistros;

    String pk = "";

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
            tipoConsulta = request.getParameter("$ta");
        }

        if (request.getParameter("$pk") != null) {
            try {
                pk = request.getParameter("$pk");
            } catch (Exception e) {
                throw new Fallo("El parámetro $pk no es válido, verifique");
            }
        }

        if (request.getParameter("$w") != null && !request.getParameter("$w").equals("") ) {
            w = request.getParameter("$w").replaceAll("\\$inicial=",">=").replaceAll("\\$final=","<=");
        }

        if (request.getParameter("$dp") != null) {
            dp = request.getParameter("$dp");
        }

        try {
            //Revisar: jqGrid manda dos parámetros page; es necesario tomar solamente aquel que tenga mayor valor.
            Map<String, String[]> map = request.getParameterMap();
            if (map.get("page")==null) {
                pagina = 1;
            } else if (map.get("page")[map.get("page").length -1]!= null) {
                pagina = Integer.parseInt(map.get("page")[map.get("page").length -1]);
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
        
        //Parsea el where
        if (request.getParameter("sord") != null) {
            sord = request.getParameter("sord");
        }
        
        frmTemp = new Forma(new Consulta(forma, tipoConsulta, pk, w, user, registros, pagina, sidx.toString(), sord),false);
        frmTemp.setDefinicionDelGrid(dp.equals("header") ? true : false);
        source = frmTemp.getSQL();
        error = "";
    } catch (Fallo f) {
        error = f.getMessage();

    } catch (Exception e) {
        if (e.getMessage()==null)
            error = "Error al ejecutar la consulta";
        else 
            error = e.getMessage();
    } finally {
%><qry>
    <sql><![CDATA[<%=source.replaceAll("\\[", "\\\\\\[") %>]]></sql>
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
    <clave_aplicacion><![CDATA[<%=frmTemp.getClaveAplicacion()%>]]></clave_aplicacion>
    <clave_forma><![CDATA[<%=forma%>]]></clave_forma>
    <alias_tab><![CDATA[<%=frmTemp.getAliasTab()%>]]></alias_tab>
    <evento_grid tipo=""><![CDATA[<%=frmTemp.getEventoGrid()%>]]></evento_grid>
    <instrucciones><![CDATA[<%=frmTemp.getInstrucciones()%>]]></instrucciones>
    <forma><![CDATA[<%=frmTemp.getForma()%>]]></forma>
    <permite_multiseleccion><![CDATA[<%=frmTemp.isPermiteMultiseleccion()%>]]></permite_multiseleccion>
    <frecuencia_actualizacion><![CDATA[<%=frmTemp.getFrecuenciaActualizacion()%>]]></frecuencia_actualizacion>
    <clave_tipo_grid><![CDATA[<%=frmTemp.getClaveTipoGrid()%>]]></clave_tipo_grid>
</configuracion_grid><%
    if (frmTemp.isReport()) {%>
<reportes><%
    //Recupera de la base de datos los reportes y los coloca en la propiedad reporte de la forma del usuario
    ArrayList<Reporte> reportes = frmTemp.getReportes();

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

double total_paginas=0;
String formato=null;      
 if( frmTemp.getNumeroDeRegistros() >0 ) {
	total_paginas = Math.ceil(frmTemp.getNumeroDeRegistros()/registros);
        if ((frmTemp.getNumeroDeRegistros() % registros)>0)
                total_paginas+=1;
} else {
	total_paginas = 0;
}   
%>
<rows>
    <page><%=pagina%></page>
    <total><%= total_paginas %></total>
    <records><%=frmTemp.getNumeroDeRegistros()%></records>
    <column_definition><%
        for (Campo campo : campos.values()) {
            int k = 0;
            
            if (campo.getNombre().equals("_hijos") || campo.getNombre().toString().toLowerCase().equals("numeroderegistro")) {
                continue;
            }            
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
        <activo><%=campo.getActivo()%></activo>
        <tamano><%=campo.getTamano()%></tamano>
        <visible><%=campo.getVisible()%></visible>
        <valor_predeterminado><![CDATA[<%=campo.getValorPredeterminado()%>]]></valor_predeterminado>
        <justificar_cambio><%=campo.getJustificarCambio()%></justificar_cambio>
        <usado_para_agrupar><%=campo.getUsadoParaAgrupar()%></usado_para_agrupar>
        <formato><%=campo.getFormato()%></formato>
        </<%=campo.getNombre()%>><%
                k++;
            }
        %>
    </column_definition><%
        if (!dp.equals("header")) {
            for (ArrayList registro : aRegistros) {
    %>
    <row><%
        int k = 0;
        String resultado = "";
        Object aKeySet[]=campos.keySet().toArray();

        for (Object dato : registro) {
           if (k>=aKeySet.length) {
                    break;
           }
            
            NumberFormat nfMoney = new DecimalFormat(campos.get(aKeySet[k]).getFormato()!=null?campos.get(aKeySet[k]).getFormato():"###,###,###,##0.00");
            NumberFormat nfReal = new DecimalFormat(campos.get(aKeySet[k]).getFormato()!=null?campos.get(aKeySet[k]).getFormato():"###,###,###,##0.00");
            SimpleDateFormat sdf = new SimpleDateFormat(campos.get(aKeySet[k]).getFormato()!=null?campos.get(aKeySet[k]).getFormato():"dd/MM/yyyy");
            SimpleDateFormat sd = new SimpleDateFormat(campos.get(aKeySet[k]).getFormato()!=null?campos.get(aKeySet[k]).getFormato():"yyyy/MM/dd hh:mm");
            
            if (dato != null) {
                if (campos.get(aKeySet[k]).getTipoDato().toLowerCase().equals("money") || campos.get(aKeySet[k]).getTipoDato().toLowerCase().equals("decimal")) {
                    resultado = dato.toString(); // nfMoney.format(dato) 
                } else if (campos.get(aKeySet[k]).getTipoDato().toLowerCase().equals("double")) {
                    resultado = nfReal.format(dato);
                } else if (campos.get(aKeySet[k]).getTipoDato().toLowerCase().equals("smalldatetime")) {
                    resultado = sdf.format(dato);
                } else if (campos.get(aKeySet[k]).getTipoDato().toLowerCase().equals("datetime")) {
                    resultado = sd.format(dato);
                } else if (request.getParameter("$removeTags") != null) {//Elimina las ligas de la descripción de actividad
                    resultado = dato.toString().replaceAll("\\<a.*?>", "").replaceAll("\\</a>", "");
                } else {
                    resultado = dato.toString();
                }
            } else {
                resultado = "";
            }

            if (!campos.get(aKeySet[k]).getNombre().equals("_hijos") && !campos.get(aKeySet[k]).getNombre().toString().toLowerCase().equals("numeroderegistro")) {%>
            <cell><![CDATA[<%=resultado%>]]></cell><% }
                k++;
            }
        %>
    </row><%
            }

        }
    %></rows>
</qry>