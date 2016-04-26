<%@page import="mx.org.fide.controlador.Sesion"%>
<%@page import="mx.org.fide.configuracion.Configuracion"%>
<%@page import="mx.org.fide.backend.Forma"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="mx.org.fide.modelo.*"%>
<%@page import="java.text.SimpleDateFormat" %> 
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<% 

response.setContentType("text/html;charset=ISO-8859-1"); 
request.setCharacterEncoding("UTF8"); 

String error=""; 
int forma=0;;
String tipoAccion="";
String w = "";
String source="";
HashMap<String,Campo> campos= new HashMap<String,Campo> ();
ArrayList<ArrayList> registros= new ArrayList<ArrayList>();
Forma frmTemp = new Forma();        
Sesion sesion = (Sesion) request.getSession().getServletContext().getAttribute("sesion");

if (sesion == null) {
    sesion = new Sesion();
    request.getSession().getServletContext().setAttribute("sesion", sesion);
}

Object[] aParametros = sesion.getConfiguracion().getParametros().keySet().toArray();
sesion.getConfiguracion().setConfiguracionActual(aParametros[0].toString());

Usuario usuario = new Usuario();
Conexion cx = new Conexion(sesion);
int nUsuario = cx.getLogin(request.getParameter("email"), request.getParameter("password"));
usuario.setCx(cx);
usuario.setConfiguracion(sesion.getConfiguracion());
usuario.setClave(nUsuario);
request.getSession().setAttribute("usuario", usuario);

if (usuario == null) {
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
    
    frmTemp = new Forma(new Consulta(forma,tipoAccion, pk, w, null, usuario),false);
    source=frmTemp.getSQL();
 
   
} catch (Fallo f) {
    error= f.getMessage() + ";" + f.getStackTrace();
} catch (Exception e) {
    error= e.getMessage();
} finally { %>
<head>
    <title><%=frmTemp.getForma()%></title>
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
    campos = frmTemp.getCampos();
    registros = frmTemp.getRegistros();
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
                                                           data=registro.get(k).toString().replaceAll("\\<a.*?>", "").replaceAll("\\</a>", "").split("-")[0];
            
       %><td><%=data%></td>
       <%
           k++;}     
        }
     %></tr>
    </table><%    
   }
%></body><%
/* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
request.getSession().setAttribute("usuario", usuario); 
%>