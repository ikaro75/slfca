<%@page import="java.sql.Date"%><%@page import="java.sql.Timestamp"%><%@page import="java.util.LinkedHashMap"%><%@page import="java.util.ArrayList"%><%@page import="mx.org.fide.modelo.*"%><%@page import="mx.org.fide.backend.Forma"%><%@page import="java.text.NumberFormat"%><%@page import="java.text.DecimalFormat"%><%@page import="java.text.SimpleDateFormat"%><%@page contentType="application/json" pageEncoding="UTF-8"%><%
    String error = "";
    int forma = 0;
    String tipoAccion = "";
    String w = "";
    String source = "";
    LinkedHashMap<String, Campo> campos = new LinkedHashMap<String, Campo>(); 
    ArrayList<ArrayList> registros = new ArrayList<ArrayList>();
    Forma tempForma = new Forma(); 

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    NumberFormat nfMoney = new DecimalFormat("$###,###,###,##0.00");

    Usuario user = (Usuario) request.getSession().getAttribute("usuario");

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
        return;
    }
    
    if (request.getParameter("$cf") == null) {
            throw new Fallo("Falta parámetro $cf");
        } else {
            try {
                forma = Integer.parseInt(request.getParameter("$cf"));
            } catch (Exception e) {
                throw new Fallo("El parámetro $cf no es válido, verifique");
            }
        }
    
    try {
        tipoAccion=request.getParameter("$ta");
        String pk = "0";
        
        tempForma = new Forma(new Consulta(forma,tipoAccion, pk, w, null, user),false);
        source = tempForma.getSQL();


    } catch (Fallo f) {
        error = f.getMessage() + ";" + f.getStackTrace();
    } catch (Exception e) {
        error = e.getMessage();
    } finally {
        if (error == null) {
            error = "";
        }
        if (!error.equals("")) {%>
[{"error":"<%=error%>"]<%
            return;
        }
    }

    try {
        campos = tempForma.getCampos();
        registros = tempForma.getRegistros();
    } catch (Exception e) {
        error = e.getMessage();
    } finally {
        if (error == null) {
            error = "";
            }
            if (!error.equals("")) {%>
[{"error":"<%=error%>"]<%
            return;
        }
    }

    int i = 0;
    /* Si no viene con registros solo muestra la estructura de datos */

    String data;
%>[<%
     int j = 0;
     for (ArrayList registro : registros) {
         int k = 0;
%>[<%
        for (Campo campo : campos.values()) {
            //Le asigna el valor anterior del campo para registrarlo en la bitácora
            if (registro.get(k) != null) {
                campo.setValorOriginal(registro.get(k).toString());
            }

            if (registro.get(k) == null) {
                data = "";
            } else if (campo.getTipoDato().equals("smalldatetime")) {
                data = df.format(registro.get(k));
            } else if (campo.getTipoDato().equals("bit")) {
                data = registro.get(k).toString().equals("true") ? "1" : "0";
            } //else if (campo.getTipoDato().equals("money"))
            //    data = nfMoney.format(registro.get(k));
            else {
                data = registro.get(k).toString();
            }

 if (campo.getTipoDato().toLowerCase().equals("int") || campo.getTipoDato().toLowerCase().equals("bit") || campo.getTipoDato().toLowerCase().equals("float")) {%><%=data%><%} else { %>"<%=data%>"<% }  
    if (k<campos.size()-1) {%><%= "," %><%} else {%>]<%}
    k++;}     
        if (j<registros.size()-1) {%><%= "," %><%} 
            j++;
    }
    /* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
    request.getSession().setAttribute("usuario", user);
%>]
