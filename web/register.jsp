<?xml version="1.0" encoding="UTF-8"?><%@ page contentType="text/xml charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="java.util.ArrayList"
%><%@ page import="mx.org.fide.modelo.*" 
%><%@ page import="mx.org.fide.utilerias.*" 
%><%@page import="mx.org.fide.backend.Forma"%><%
//request.setCharacterEncoding("UTF-8");
    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");

    String error = ""; 
    int forma = 0;
    String tipoAccion = "";
    String w = ""; 
    String source = "";
    String result = "";
    int pk = 0;
    Forma frmTemp = new Forma();
    response.setContentType("text/xml");
    Usuario user = (Usuario) request.getSession().getAttribute("usuario");

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }

    try {
        if (request.getParameter("$cf") == null) {
            throw new Fallo("Falta parámetro $cf");
        } else {
            forma = Integer.parseInt(request.getParameter("$cf"));
        }

        if (request.getParameter("$ta") == null) {
            throw new Fallo("Falta parámetro $ta");
        } else {
            tipoAccion = request.getParameter("$ta");
        }


        if (request.getParameter("$pk") != null) {
            pk = Integer.parseInt(request.getParameter("$pk"));
        }

        if (request.getParameter("$w") != null) {
            w = request.getParameter("$w");
        }

    } catch (Fallo f) {
        error = f.getMessage();
    } catch (Exception e) {
        error = e.getMessage();
    } finally {%>
<qry><%
    if (!error.equals("")) {%>
    <error><![CDATA[<%=error%>]]></error>
</qry><%
        return;
    }%>
<%
    }

    try {
        //Si no está abierto el objeto forma
            if (!tipoAccion.equals("delete") && !tipoAccion.equals("duplicate")) {
                frmTemp = new Forma (new Consulta(forma,tipoAccion, String.valueOf(pk), w, null,user), false); 
                source = frmTemp.getSQL();
            } else {
                frmTemp = new Forma (new Consulta(forma,"update", String.valueOf(pk), w, null,user), false); 
                frmTemp.setAccion(tipoAccion);
            }
      

        if ((!tipoAccion.equals("delete") /* && !tipoAccion.equals("duplicate") */)) {
            String valor;
            Integer i =-1;
            for (Campo campo : frmTemp.getCampos().values()) {
                i++;
                if (!campo.isAutoIncrement()) {
                    
                    if (tipoAccion.equals("update")) {
                        if (frmTemp.getRegistros().get(0).get(i) != null) {
                            campo.setValorOriginal(frmTemp.getRegistros().get(0).get(i).toString());
                        }
                    }
                    
                    if (request.getParameter(campo.getNombre()) != null || (campo.getTipoDato().toLowerCase().equals("bit") || campo.getTipoDato().toLowerCase().equals("tinyint"))) {
                        //valor=Utilerias.decodeURIComponentXX(request.getParameter(campo.getNombre()));
                        if ((campo.getTipoDato().toLowerCase().equals("bit") || campo.getTipoDato().toLowerCase().equals("tinyint")) && 
                             request.getParameter(campo.getNombre()) == null) {
                            valor = "0";
                        } else {
                            valor = Utilerias.decodeURIComponentXX(request.getParameter(campo.getNombre()));
                        }
                        
                        //if (!valor.equals(""))
                        campo.setValor(valor);
                    }
                }
            }
      }
    } catch (Fallo f) {
        error = f.getMessage();
    } catch (Exception e) {
        error = e.getMessage();
    } finally {%>
<sql><![CDATA[<%=source%>]]></sql>
<% if (!error.equals("")) {%>
<error><![CDATA[<%=error%>]]></error>
</qry>
<%
            return;
        }
    }

%><clave_consulta><%=frmTemp.getClaveConsulta()%></clave_consulta><%
    
try {
    Desencadenador d= new Desencadenador(frmTemp);
         
    if (tipoAccion.equals("insert")) {
        result = d.insert(); 
    } else if (tipoAccion.equals("update")) {
        result = d.update();
    } else if (tipoAccion.equals("delete")) { 
        result = d.delete();
    } else if (tipoAccion.equals("duplicate")) {
        result = d.duplicate();
    }

} catch (Exception e) {
    if ((e.getMessage()!=null))
        error = e.getMessage();
    else 
        error = "Error no identificado";
} finally {
        if (!error.equals("")) {%>
<error><![CDATA[<%=error%>]]></error>
</qry>
<%
            return;
}%>
<result><%=result%></result>
</qry><%
        /* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
        request.getSession().setAttribute("usuario", user);

        //Recarga  el objeto usuario si es que se actualizó
        if (frmTemp.getTabla().equals("empleado") && request.getParameter("$pk").equals(String.valueOf(user.getClave()))) {
            user.setSesion(request.getSession().getId());
        }
}%>
