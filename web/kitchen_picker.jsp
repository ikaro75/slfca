<?xml version="1.0" encoding="UTF-8"?><%@page import="mx.org.fide.modelo.Usuario"
%><%@page import="mx.org.fide.restaurantex.Empresa"
%><%@page import="mx.org.fide.restaurantex.Cocina"
%><%@page import="mx.org.fide.restaurantex.Cuenta"
%><%@page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%
response.setContentType("text/xml;charset=ISO-8859-1"); 
request.setCharacterEncoding("UTF8");

Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

if (usuario == null) {
    request.getRequestDispatcher("/index.jsp");
}

Integer claveEmpresa=0;
Integer claveEmpleado=0;
String tipoAccion="";
claveEmpresa= Integer.parseInt(request.getParameter("$empresa"));
claveEmpleado= Integer.parseInt(request.getParameter("$empleado"));
tipoAccion=request.getParameter("$ta");

Empresa empresa = new Empresa(claveEmpresa, usuario);

if (tipoAccion.equals("open")) { %>
<administrax>
    <html>
        <form id="object_picker" name="object_picker">
           <div id="cocinas"><h3>Cocinas</h3><%
           for (Cocina cocina : empresa.getCocinasDisponibles(claveEmpleado)) {
                   %><input type="checkbox" <%
                   Integer claveCocinero=cocina.getClaveEmpleado();
                   
                   if (claveCocinero==claveEmpleado) {
                       %> checked="checked" <%
                   }                   
                   %> id="clave_cocina_<%=cocina.getClaveCocina()%>" name="clave_cocina_<%=cocina.getClaveCocina()%>" value="1" /><%=cocina.getCocina()%>
                   <br /><%
           }
           
           %></div>
        </form>
    </html>
</administrax>
<% } else if (tipoAccion.equals("save")) {
    
    for (Cocina cocina : empresa.getCocinasDisponibles(claveEmpleado)) {
                //Si la tenia el cocinero y la deseleccionó
                
                if (cocina.getClaveEmpleado()!=claveEmpleado && request.getParameter("clave_cocina_".concat(cocina.getClaveCocina().toString()))==null) {
                    continue;
                } else if (cocina.getClaveCocina()==claveEmpleado && request.getParameter("clave_cocina".concat(cocina.getClaveCocina().toString()))==null) {
                    cocina.setPk(cocina.getClaveCocina().toString());   
                    cocina.setSQL(577, "update", cocina.getClaveCocina().toString(), "");
                    cocina.setCampos();  
                    cocina.setClaveEmpleado(claveEmpleado);  
                    cocina.getCampos().get("clave_empleado").setValor("null");
                    cocina.update(true); 
                } else if (cocina.getClaveEmpleado()==0 && request.getParameter("clave_cocina_".concat(cocina.getClaveCocina().toString()))!=null) {
                    cocina.setPk(cocina.getClaveCocina().toString());   
                    cocina.setSQL(577 , "update", cocina.getClaveCocina().toString(), "");
                    cocina.setCampos();  
                    cocina.setClaveEmpleado(claveEmpleado);  
                    cocina.getCampos().get("clave_empleado").setValor(claveEmpleado.toString());
                    cocina.update(true);
                    
                    //Se le asignan también las actividdes al  cocinero
                    /* if (claveCuentAbierta>0) {
                        Cuenta cuenta = new Cuenta(claveCuentAbierta, usuario);
                        cuenta.getCampos().get("clave_empleado").setValor(claveEmpleado.toString());
                        cuenta.update();
                    }*/
                }
        }
    %><ok /><%
    } 
%>
