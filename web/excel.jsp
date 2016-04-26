<%@page contentType="application/vnd.ms-excel" pageEncoding="iso-8859-1" 
%><%@page import="java.util.ArrayList"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="mx.org.fide.modelo.*"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat"
%><%@page import="java.util.LinkedHashMap"%><% 

String error="";
Integer pagina=1;
int forma=0;
String tipoAccion="";
String w = "";
String source="";
 LinkedHashMap<String,Campo> campos = new LinkedHashMap<String,Campo>();
ArrayList<ArrayList> registros= new ArrayList<ArrayList>();
Forma f = new Forma();
        
Usuario user=(Usuario)request.getSession().getAttribute("usuario");
if (user == null) {
    request.getRequestDispatcher("/index.jsp");
}

String pk="0";

if (request.getParameter("$pk")!=null)
    pk = request.getParameter("$pk");

if (request.getParameter("$ta")==null)
    throw new Fallo("Falta parámetro $ta");
else  
    tipoAccion=request.getParameter("$ta");
    
if (request.getParameter("$w")!=null)
        w=request.getParameter("$w").replaceAll("\\$inicial=",">=").replaceAll("\\$final=","<=");
    
try {
    if (request.getParameter("$cf")==null)
        throw new Fallo("Falta parámetro $cf");
    else {
        try {
            forma= Integer.parseInt(request.getParameter("$cf"));
            f = new Forma(new Consulta(forma,tipoAccion, String.valueOf(pk), w, null,user), false);
            response.setHeader("Content-Disposition", "attachment; filename="+ f.getForma()+".xls");
        }
        catch (Exception e) { 
             throw new Fallo(e.getMessage());
        }
    }
    /*if (request.getParameter("$pk")!=null) {
        try {
            pk = String.valueOf(Integer.parseInt(request.getParameter("$pk")));
        }
        catch (Exception e) { 
             pk = "'".concat(request.getParameter("$pk")).concat("'");
        }
    }*/
    
    source=f.getSQL();
 
   
} catch (Fallo fa) {
    error= fa.getMessage() + ";" + fa.getStackTrace();
} catch (Exception e) {
    error= e.getMessage();
} finally { %>
<head>
    <title><%=f.getForma()%></title>
    <style>
        td { font-family: Verdana, Arial, Helvetica, sans-serif ; font-size: 10px; }
    </style>
</head>
<body>
<p>
    <!---<![CDATA[<%=source%>]]> ---><%
     if (error==null) error="";
     if (!error.equals("")) {%>
     <span><%=error%></span>
</p>    
<%      return; }%>
<%    
}

try {
    campos = f.getCampos();
    registros = f.getRegistros();
} catch (Exception e) {
    error= e.getMessage();
} finally {
  if (error==null) error="";
  if (!error.equals("")) { %>
   Error: <%=error%>]]>
<%
    return;
    }  
} 

int i=0;
/* Si no viene con registros solo muestra la estructura de datos */
if (registros.size()==0) { 
%><table><tr>
<%
    for (Campo campo : campos.values()) {
     %>
     <td><%=campo.getAlias()%></td>
     <%
   }     
%></tr></table>
<%    
} else {

    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat nfMoney = new DecimalFormat("$###,###,###,##0.00");
    %><table><tr><%
    for (Campo campo : campos.values()) {
     %>
     <td><%=campo.getAlias()%></td>
     <%
    }%></tr><%    
    String data;
    for (ArrayList registro : registros) {
       int k=0;     
       %><tr>
           <%
           for (Campo campo : campos.values()) {
            if (registro.get(k)==null) 
                data="";
            else if (campo.getTipoDato().equals("smalldatetime"))
                data=df.format(registro.get(k));
                       else if (campo.getTipoDato().equals("bit"))
                          data = registro.get(k).toString().equals("true")?"1":"0";
                                       //else if (campo.getTipoDato().equals("money"))
                                       //    data = nfMoney.format(registro.get(k));
                                                       else
                                                           data=registro.get(k).toString().replaceAll("\\<a.*?>", "").replaceAll("\\</a>", "");//.split("-")[0];
            
       %><td><%=data%></td>
       <%
           k++;}     
        }
     %></tr>
    </table><%    
 }
%></body><%
/* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
request.getSession().setAttribute("usuario", user); 
%>