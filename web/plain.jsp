<?xml version="1.0" encoding="ISO-8859-1"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="java.util.ArrayList"
%><%@ page import="mx.org.fide.modelo.*"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="java.util.LinkedHashMap"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat"%><% 

response.setContentType("text/xml;charset=ISO-8859-1"); 
request.setCharacterEncoding("UTF8");

String error=""; 
int forma=0;
String tipoAccion="";
String w = "";
String source="";
LinkedHashMap <String,Campo> campos= new LinkedHashMap <String,Campo>();
ArrayList<ArrayList> registros= new ArrayList<ArrayList>();
Forma tempForma= new Forma();
        
Usuario user=(Usuario)request.getSession().getAttribute("usuario");

if (user == null) { 
   request.getRequestDispatcher("/index.jsp");
}

try {
    
    if (request.getParameter("$cf")==null)
        error="Falta parámetro $cf";
    else {
        try {
            forma= Integer.parseInt(request.getParameter("$cf"));
        }
        catch (Exception e) { 
             throw new Fallo("El parámetro $cf no es válido, verifique");
        }
    }
   
    if (request.getParameter("$ta")==null)
        throw new Fallo("Falta parámetro $ta");
    else  
        tipoAccion=request.getParameter("$ta");
    
    String pk="0";
    pk = request.getParameter("$pk");
    /*if (request.getParameter("$pk")!=null) {
        try {
            pk = String.valueOf(Integer.parseInt(request.getParameter("$pk")));
        }
        catch (Exception e) { 
             pk = "'".concat(request.getParameter("$pk")).concat("'");
        }
    }*/
    
    if (request.getParameter("$w")!=null)
        w=request.getParameter("$w");
    
    tempForma = new Forma(new Consulta(forma,tipoAccion, pk, w, null, user), false);
    source=tempForma.getSQL();
   
} catch (Fallo f) {
    error= f.getMessage() + ";" + f.getStackTrace();
} catch (Exception e) {
    error= e.getMessage();
} finally { %>
<qry>
    <sql><![CDATA[<%=source%>]]></sql><%
     if (error==null) error="";
     if (!error.equals("")) {%>
    <error><![CDATA[<%=error%>]]></error> 
</qry>    
<%      return; }
}

try {
    campos = tempForma.getCampos();
    registros = tempForma.getRegistros();
} catch (Exception e) {
    error= e.getMessage();
} finally {
  if (error==null) error="";
  if (!error.equals("")) { %>
    <error><![CDATA[<%=error%>]]></error>
</qry><%
    return;
    }  
}%> 
<permisos><%
if (tempForma.isSelect()) {%>
    <permiso><clave_permiso>1</clave_permiso></permiso><%} 
if (tempForma.isInsert()) {%>
    <permiso><clave_permiso>2</clave_permiso></permiso><%}
if (tempForma.isUpdate()) {%>
    <permiso><clave_permiso>3</clave_permiso></permiso><%} 
if (tempForma.isDelete()) {%>
    <permiso><clave_permiso>4</clave_permiso></permiso><%}  
%></permisos>
<configuracion_forma>
	<alias_tab><![CDATA[<%=tempForma.getAliasTab()%>]]></alias_tab>
	<evento_forma tipo=""><![CDATA[<%=tempForma.getEventoForma()%>]]></evento_forma>
	<instrucciones><![CDATA[<%=tempForma.getInstrucciones()%>]]></instrucciones>
	<forma><![CDATA[<%=tempForma.getForma()%>]]></forma>
	<prefiltro><![CDATA[<%=tempForma.isMuestraFormasForeaneas()%>]]></prefiltro>
</configuracion_forma>
<% 

int i=0;
/* Si no viene con registros solo muestra la estructura de datos */
if (registros.size()==0) { 
%><registro>
<%
    for (Campo campo : campos.values()) {
     %>
     <<%=campo.getNombre()%><% if (campo.isAutoIncrement()) {%> autoincrement="TRUE" <%}%> tipo_dato="<%=campo.getTipoDato()%>"><![CDATA[]]></<%=campo.getNombre()%>>
     <%
   }     
%></registro><%    
} else {
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat nfMoney = new DecimalFormat("$###,###,###,##0.00");
    
    String data;
    for (ArrayList registro : registros) {
       int k=0;     
       %><registro>
           <%
            for (Campo campo : campos.values()) { 
            //Le asigna el valor anterior del campo para registrarlo en la bitácora
            if (registro.get(k)!=null)
            campo.setValorOriginal(registro.get(k).toString());
            
            if (registro.get(k)==null) 
                data="";
            else if (campo.getTipoDato().equals("smalldatetime"))
                data=df.format(registro.get(k));
                       else if (campo.getTipoDato().equals("bit"))
                          data = registro.get(k).toString().equals("true")?"1":"0";
                                       //else if (campo.getTipoDato().equals("money"))
                                       //    data = nfMoney.format(registro.get(k));
                                                       else
                                                           data=registro.get(k).toString();
            
       %><<%=campo.getNombre()%> <% if (campo.isAutoIncrement()) {%> autoincrement="TRUE" <%}%> tipo_dato="<%=campo.getTipoDato()%>"><![CDATA[<%=data%>]]></<%=campo.getNombre()%>>
             <%
           k++;}     
        %></registro> 
        <%
        }
   }
%></qry><%
/* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
request.getSession().setAttribute("usuario", user); 
%>