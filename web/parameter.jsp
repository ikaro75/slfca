<?xml version="1.0" encoding="UTF-8"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@page import="java.util.ArrayList"
%><%@page import="mx.org.fide.modelo.*"
%><%@page import="java.util.LinkedHashMap"
%><%@page import="mx.org.fide.backend.Forma"%><%

    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");

    String error = "";
    int forma = 0;
    int pk = 0;
    String tipoAccion = "";
    String w = "";
    String source = "";
    Usuario user = null;
    LinkedHashMap<String, Campo> campos = new LinkedHashMap<String, Campo>();
    LinkedHashMap<String, Campo> camposForaneos = new LinkedHashMap<String, Campo>();
    ArrayList<ArrayList> registros = new ArrayList<ArrayList>();
    ArrayList<ArrayList> registrosForaneos = new ArrayList<ArrayList>();
    Forma frmParametros = new Forma();
    Forma frmForanea = null;
    user = (Usuario) request.getSession().getAttribute("usuario");
    Campo campoForaneo = null;

    if (user == null) {
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
        frmParametros = new Forma(new Consulta(forma, tipoAccion, String.valueOf(pk), w, null,user), false);
        source = frmParametros.getSQL();

        campos = frmParametros.getCampos();
        registros = frmParametros.getRegistros();

    } catch (Fallo f) {
        error = f.getMessage();

    } catch (Exception e) {
        error = e.getMessage();

    } finally {%>
<qry>
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

    try {
        for (ArrayList registro : registros) {%><registro>
    <clave_parametro><%=registro.get(0).toString()%></clave_parametro>
    <parametro><![CDATA[<%=registro.get(1).toString()%>]]></parametro>
    <clave_reporte><%=registro.get(2).toString()%></clave_reporte>
    <alias><![CDATA[<%=registro.get(3).toString()%>]]></alias>
    <tipo_dato><%=registro.get(4).toString()%></tipo_dato>
    <valor><![CDATA[<%=registro.get(5) == null ? "" : registro.get(5).toString()%>]]></valor>
    <valor_predeterminado><![CDATA[<%=registro.get(6) == null ? "" : registro.get(6).toString()%>]]></valor_predeterminado>
    <clave_forma_foranea><%
        /* Extrae los datos del foraneo */
        if (registro.get(7) != null) {
            Boolean cargaForaneosRetrasada = registro.get(8) == null ? false : (registro.get(8).toString().equals("1") ? true : false);
            try {
                if (!cargaForaneosRetrasada) {
                    frmForanea = new Forma(new Consulta(Integer.parseInt(registro.get(7).toString()), tipoAccion, "", "", null,user), false);
                    camposForaneos = frmForanea.getCampos();
                    registrosForaneos = frmForanea.getRegistros();
                }
            } catch (Exception e) {
                error = e.getMessage();
            }

            if (error.equals("")) {%>
        <foraneo clave_forma="<%=registro.get(7).toString()%>">
            <qry_<%=registro.get(1).toString()%>><%
                if (!error.equals("")) {%>
                <error><![CDATA["Problemas al recuperar la consulta de la forma foranea ligada al campo <%=registro.get(1).toString()%>: <%=error%>]]></error> <%
                    //Limpia variable error después de reportarlo
                    error = "";
                } else {
                    %><sql_<%=registro.get(1).toString()%>>
                    <![CDATA[<%=frmForanea.getSQL()%>]]> 
                </sql_<%=registro.get(1).toString()%>><%
                    int iRegistrosForaneos = frmForanea.getRegistros().size();
                    if (!cargaForaneosRetrasada) {
                        for (int m = 0; m < iRegistrosForaneos; m++) {
                %>
                <registro_<%=registro.get(1).toString()%>>
                    <%  Object keySet[] = frmForanea.getCampos().keySet().toArray();
                        int iCamposForaneos = frmForanea.getCampos().size();
                        for (int n = 0; n < iCamposForaneos; n++) {
                            campoForaneo = camposForaneos.get(keySet[n]);
                            String campoForaneoNombre = campoForaneo.getNombre();
                    %><<%=campoForaneoNombre%> tipo_dato='<%=campoForaneo.getTipoDato()%>'>
                    <![CDATA[<%=registrosForaneos.get(m).get(n)%>]]>
                    </<%=campoForaneoNombre%>><%
                        }
                    %>
                </registro_<%=registro.get(1).toString()%>><%
                            }
                        }
                    }
                %>
            </qry_<%=registro.get(1).toString()%>>
        </foraneo>    
        <% } else {%>
        <foraneo><error_foraneo><%=error%></error_foraneo></foraneo>
        <%    error = "";
                        }
                    }%></clave_forma_foranea>
    <carga_dato_foraneos_retrasada><![CDATA[<%=registro.get(8) == null ? "0" : registro.get(8).toString()%>]]></carga_dato_foraneos_retrasada>
    <evento><![CDATA[<%=registro.get(9) == null ? "" : registro.get(9).toString()%>]]></evento>
    <visible><%=(registro.get(10) == null ? "0" : registro.get(10).toString())%></visible>
    <obligatorio><%=(registro.get(11) == null ? "0" : registro.get(11).toString())%></obligatorio>
    <ayuda><![CDATA[<%=registro.get(12) == null ? "" : registro.get(12).toString()%>]]></ayuda>
</registro><%
    }
} catch (Exception e) {
%><error><%=e.getMessage()%></error><%
            }  /* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
    %></qry>
