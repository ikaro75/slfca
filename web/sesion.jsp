<?xml version='1.0' encoding='ISO-8859-1'?><%@page contentType="text/xml" pageEncoding="UTF-8"
%><%@ page import="mx.org.fide.modelo.*"
%><%@page import="java.io.FileOutputStream"
%><%@page import="java.io.FileInputStream"
%><%@page import="java.nio.channels.FileChannel"
%><%@page import="java.io.File" %><%

    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");

    Usuario user = (Usuario) request.getSession().getAttribute("usuario");
    String error = "";
    String debug = "";
    String sAppPath = "";
	String foto ="";

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }

    if (user.getFoto() != null) {
        File archivoOrigen = new File("c:\\slfca\\".concat(user.getFoto().replaceAll("/slfca/temp/", "").replace("/","\\")));
        if (!archivoOrigen.exists()) {
			debug = "<debug>El archivo origen ".concat(archivoOrigen.getAbsolutePath()).concat(" no se encontro </debug>");
            user.setFoto(null);
        } else {
            try {
		debug = "<debug>Se encontró el archivo origen ".concat(archivoOrigen.getAbsolutePath()).concat("</debug>");
                sAppPath = application.getRealPath("/").replace("\\build", "");
                //Hay que borrar el web para producción
                File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(user.getClave())).concat("\\"));
                if (!forderDestino.exists()) {
		    debug = debug.concat("<debug>No se encontro el folder destino ").concat(forderDestino.getAbsolutePath()).concat("</debug>");
                    forderDestino.mkdir();
                }
                
                File archivoDestino = new File(forderDestino.getAbsolutePath().concat("\\").concat(archivoOrigen.getName()));

                if (!archivoDestino.exists()) {
		    debug = debug.concat("<debug>No se encontro el archivo destino ").concat(archivoDestino.getAbsolutePath()).concat("</debug>");
                    archivoDestino.createNewFile();

                    if (!archivoOrigen.exists()) {
                        foto = "Error - No se encontró el archivo ".concat(user.getFoto());
                    } else {
                        try {

                            FileChannel origen = null;
                            FileChannel destino = null;
                            origen = new FileInputStream(archivoOrigen).getChannel();
                            destino = new FileOutputStream(archivoDestino).getChannel();

                            if (destino != null && origen != null) { 
                                destino.transferFrom(origen, 0, origen.size());
                            }
                            if (origen != null) {
                                origen.close();
                            }
                            if (destino != null) {
                                destino.close();
                            }
                        } catch (Exception e) {
                            foto = "Error - No fue posible escribir el archivo ".concat(user.getFoto()).concat(" en carpeta web: ").concat(e.getMessage());
                        }

                        foto = user.getFoto();

                    }
                } else {
                    foto = user.getFoto();
                }
                user.setFoto(user.getFoto());
            } catch (Exception e) {
                error = "<error><![CDATA[".concat(e.getLocalizedMessage()).concat(" sAppPath=").concat(sAppPath).concat("]]></error>");
                user.setFoto(null);
            }
        } 
            
    }

%>
<qry source="(sesion de usuario)">
    <registro id="1">
        <clave_empleado tipo_dato="string"><![CDATA[<%= user.getClave()%>]]></clave_empleado>
        <nombre tipo_dato="string"><![CDATA[<%= user.getNombre()%>]]></nombre>
        <apellido_paterno tipo_dato="string"><![CDATA[ <%=user.getApellido_paterno()%> ]]></apellido_paterno>
        <apellido_materno tipo_dato="string"><![CDATA[ <%=user.getApellido_materno()%> ]]></apellido_materno>
        <email tipo_dato="string"><![CDATA[<%= user.getEmail()%>]]></email>
        <perfil tipo_dato="string"><![CDATA[<%= user.getPerfil()%>]]></perfil>
        <clave_perfil tipo_dato="integer"><![CDATA[<%= user.getClavePerfil()%>]]></clave_perfil>
        <foto tipo_dato="string"><![CDATA[<%=user.getFoto() == null ? "" : foto%>]]></foto>
        <cambia_password tipo_dato="bit"><![CDATA[<%=user.getCambiaPassword()%>]]></cambia_password>
        <sesion tipo_dato="bit"><![CDATA[<%=request.getSession().getId()%>]]></sesion><%=debug%>
    </registro>
    <%=error%>
</qry>