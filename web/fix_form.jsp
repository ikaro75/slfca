<%@page import="mx.org.fide.backend.Equivalencia"%>
<%@page import="java.sql.Date"%><%@page import="java.sql.Timestamp"%><%@page import="java.util.LinkedHashMap"%><%@page import="java.util.ArrayList"%><%@page import="mx.org.fide.modelo.*"%><%@page import="mx.org.fide.backend.Forma"%><%@page import="java.text.NumberFormat"%><%@page import="java.text.DecimalFormat"%><%@page import="java.text.SimpleDateFormat"%><%@page contentType="application/json" pageEncoding="UTF-8"%><%
    String error = "";
    Integer forma = 627;
    Integer clave_forma=0;
    String tipoAccion = "";
    String w = "";
    String source = "";
    LinkedHashMap<String, Campo> campos = new LinkedHashMap<String, Campo>();
    ArrayList<ArrayList> registros = new ArrayList<ArrayList>();
    Equivalencia equivalencia; 

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    NumberFormat nfMoney = new DecimalFormat("$###,###,###,##0.00");

    Usuario user = (Usuario) request.getSession().getAttribute("usuario"); 

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
        return;
    }

    try {

        tipoAccion="insert";
        String pk = "0";

        //parámetros utilizados por la agenda para delimitar fecha inicial y fecha final
        if (request.getParameter("clave_forma") == null) {
            throw new Fallo("No se especificó el parametro clave_forma");
        } else { 
            try {
                clave_forma= Integer.parseInt(request.getParameter("clave_forma"));

            } catch (Exception e) {
                throw new Fallo("Error al convertir parámetro clave forma a entero");
            }
        }
        
        equivalencia = new Equivalencia(new Consulta(forma,tipoAccion, pk, w,null, user));
        equivalencia.getCampos().get("clave_forma").setValor(request.getParameter("clave_forma"));
        equivalencia.insert(true);


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

    /* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
    request.getSession().setAttribute("usuario", user);
%>]
