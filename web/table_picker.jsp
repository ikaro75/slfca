<?xml version="1.0" encoding="UTF-8"?><%@page import="mx.org.fide.modelo.Usuario"
%><%@page import="mx.org.fide.restaurantex.Empresa"
%><%@page import="mx.org.fide.restaurantex.Sala"
%><%@page import="mx.org.fide.restaurantex.Mesa"
%><%@page import="mx.org.fide.restaurantex.Cuenta"
%><%@page import="java.util.ArrayList"
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
           <div id="salas"><%
           for (Sala sala : empresa.getSalas()) {
               %><h3><%=sala.getSala()%></h3>
               <% 
               ArrayList <Mesa> mesasDisponibles =sala.getMesasDisponibles(claveEmpleado);
               
               if (mesasDisponibles.size()==0) {
                   %><div>No hay mesas disponibles en la sala</div><%
               }
                   
               for (Mesa mesa : mesasDisponibles) {
                   %><input type="checkbox" <%
                   Integer claveMesero=mesa.getClave_empleado();
                   
                   if (claveMesero==claveEmpleado) {
                       %> checked="checked" <%
                   }
                   Integer cuentaAbierta=mesa.getCuentaAbierta();
                   
                   %> id="clave_mesa_<%=mesa.getClave_mesa()%>" name="clave_mesa_<%=mesa.getClave_mesa()%>" value="1" />Mesa <%=mesa.getNumero()%><%
                   if (cuentaAbierta>0) {
                       Cuenta cuenta = new Cuenta(cuentaAbierta, usuario);
                       Usuario mesero = new Usuario();
                       mesero.setCx(usuario.getCx());
                       
                       mesero.setClave(cuenta.getClave_empleado());
                       %> (Cuenta <%=cuenta.getClave_cuenta()%> abierta por <%=mesero.getNombre()%> <%=mesero.getApellidos()%> a las <%=cuenta.getfecha_apertura()%>)
                       <%                       
                   }
                   %><br /><%
               }
           }
           
        //1. Se deben mostrar las salas y las mesas de la empresa y el estatus de sus cuentas
           %></div>
        </form>
    </html>
</administrax>
<% } else if (tipoAccion.equals("save")) {
    Integer claveCuentAbierta=0;
    for (Sala sala : empresa.getSalas()) {
            for (Mesa mesa : sala.getMesasDisponibles(claveEmpleado)) {
                //Si la tenia el mesero y la deseleccionó
                
                if (mesa.getClave_empleado()!=claveEmpleado && request.getParameter("clave_mesa_".concat(mesa.getClave_mesa().toString()))==null) {
                    continue;
                } else if (mesa.getClave_empleado()==claveEmpleado && request.getParameter("clave_mesa_".concat(mesa.getClave_mesa().toString()))==null) {
                    mesa.setPk(mesa.getClave_mesa().toString());   
                    mesa.setSQL(583 , "update", mesa.getClave_mesa().toString(), "");
                    mesa.setCampos();  
                    mesa.setClave_empleado(claveEmpleado);  
                    mesa.getCampos().get("clave_empleado").setValor("null");
                    mesa.update(true); 
                } else if (mesa.getClave_empleado()==0 && request.getParameter("clave_mesa_".concat(mesa.getClave_mesa().toString()))!=null) {
                    mesa.setPk(mesa.getClave_mesa().toString());   
                    mesa.setSQL(583 , "update", mesa.getClave_mesa().toString(), "");
                    mesa.setCampos();  
                    mesa.setClave_empleado(claveEmpleado);  
                    mesa.getCampos().get("clave_empleado").setValor(claveEmpleado.toString());
                    mesa.update(true);
                    
                    claveCuentAbierta= mesa.getCuentaAbierta();
                    //Se le asigna tambien la cuenta abierta al mesero
                    if (claveCuentAbierta>0) {
                        Cuenta cuenta = new Cuenta(claveCuentAbierta, usuario);
                        cuenta.getCampos().get("clave_empleado").setValor(claveEmpleado.toString());
                        cuenta.update();
                    }
                }
                      
             
            }
        }
    %><ok /><%
    } 
%>
