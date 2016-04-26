<?xml version="1.0" encoding="UTF-8"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="java.util.ArrayList"
%><%@ page import="mx.org.fide.modelo.*"
%><%@page import="mx.org.fide.backend.Nota"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat" 
%><%@page import="mx.org.fide.reporte.Reporte"
%><%@page import="java.io.File"
%><%@page import="java.io.FileOutputStream" 
%><%@page import="java.io.FileInputStream" 
%><%@page import="java.nio.channels.FileChannel" 
%><%@page import="java.util.LinkedHashMap" 
%><%
//response.setContentType("text/xml"); 

    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");

    String error = "";
    int forma = 0;
    int pk = 0;
    String tipoAccion = "";
    String w = "";
    String source = "";
    Usuario user = null;
    Integer pagina = 1;  
    Integer numeroDeRegistros = 20;
    StringBuilder sidx = new StringBuilder("");
    String sord = "desc";
    
    LinkedHashMap<String, Campo> campos = new LinkedHashMap<String, Campo>();;
    ArrayList<ArrayList> registros = new ArrayList<ArrayList>();
    Forma frmTemp = new Forma();
    user = (Usuario) request.getSession().getAttribute("usuario");

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

        try {
            if (request.getParameter("page") != null) {
                pagina = Integer.parseInt(request.getParameter("page"));
            }
        } catch (Exception e) {
            throw new Fallo("El parámetro page no es válido, verifique");
        }

        try {
            if (request.getParameter("rows") != null) {
                numeroDeRegistros = Integer.parseInt(request.getParameter("rows"));
            }
        } catch (Exception e) {
            error = "El parámetro rows no es válido, verifique";
        }

        if (request.getParameter("sidx") != null) {
           sidx.append(request.getParameter("sidx"));
        }

        if (request.getParameter("sord") != null) {
            sord = request.getParameter("sord");
        }
        
        frmTemp = new Forma(new Consulta(forma,tipoAccion, String.valueOf(pk), w, user, numeroDeRegistros, pagina, sidx.toString(), sord), false);
        //frmTemp.setConsulta(tipoAccion, String.valueOf(pk), w, user.getReglasDeReemplazo(), user.getCx());
        source = frmTemp.getSQL();

        campos = frmTemp.getCampos();
        registros = frmTemp.getRegistros();

    } catch (Fallo f) {
        error = f.getMessage();

    } catch (Exception e) {
        error = e.getMessage();

    } finally {%><qry>
    <sql><![CDATA[<%=source%>]]></sql><%
        if (error == null) {
            error = "";
        }
        if (!error.equals("")) {%>
    <error><![CDATA[<%=error%>]]></error>
</qry><%
            return;
        }
    }%>
<permisos><% if (frmTemp.isSelect()) {%>
    <permiso><clave_permiso>1</clave_permiso></permiso><%}
        if (frmTemp.isInsert()) {%>
    <permiso><clave_permiso>2</clave_permiso></permiso><%}
        if (frmTemp.isUpdate()) {%>
    <permiso><clave_permiso>3</clave_permiso></permiso><%}
        if (frmTemp.isDelete()) {%>
    <permiso><clave_permiso>4</clave_permiso></permiso><%}
</permisos>    
<configuracion_forma>
    <alias_tab><![CDATA[<%=frmTemp.getAliasTab()%>]]></alias_tab>
    <evento_forma><![CDATA[<%=frmTemp.getEventoForma()%>]]></evento_forma>
    <instrucciones><![CDATA[<% if (frmTemp.getInstrucciones() != null) {%><%=frmTemp.getInstrucciones()%> <% }%>]]></instrucciones>
    <llave_primaria><![CDATA[<%=frmTemp.getLlavePrimaria()%>]]></llave_primaria>
    <forma><![CDATA[<%=frmTemp.getForma()%>]]></forma>
    <muestra_formas_foraneas><![CDATA[<%=frmTemp.isMuestraFormasForeaneas()%>]]></muestra_formas_foraneas>
    <permite_duplicar_registro><![CDATA[<%=frmTemp.isPermiteDuplicarRegistro()%>]]></permite_duplicar_registro>
    <permite_insertar_comentarios><![CDATA[<%=frmTemp.isPermiteInsertarComentario()%>]]></permite_insertar_comentarios>
    <permite_guardar_como_plantilla><![CDATA[<%=frmTemp.isPermiteGuardarComoPlantilla()%>]]></permite_guardar_como_plantilla>
</configuracion_forma>  
<% if (frmTemp.isMuestraFormasForeaneas() && tipoAccion.equals("update")) {%><formas_foraneas><%
    ArrayList<Forma> formasForaneas = frmTemp.getFormasForaneas();
    for (Forma formaForanea : formasForaneas) {
    %><clave_forma_foranea><%=formaForanea.getClaveForma()%></clave_forma_foranea><%
    %><forma_foranea><%=formaForanea.getForma()%></forma_foranea><%
        }
        %></formas_foraneas><%
       }%>                
    <% if (frmTemp.isPermiteInsertarComentario() && tipoAccion.equals("update")) {%><notas_forma><%
        //Recupera de la base de datos los reportes y los coloca en la propiedad reporte de la forma del usuario
            ArrayList<Nota> notas = frmTemp.getNotas(pk);
        String foto;
        //Se ejecuta el primer reporte de la lista por default
        for (Nota n : notas) {
            //Verifica si ya está el archivo en el cache
            if (n.getFoto() != null) {
                try {
                    File archivoOrigen = new File("c:\\intranet2.0\\".concat(String.valueOf(n.getClaveEmpleado())).concat("\\").concat(n.getFoto()));
                    String sAppPath = application.getRealPath("/").replace("\\build\\web", "");
                    File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(n.getClaveEmpleado())).concat("\\"));
                    if (!forderDestino.exists()) {
                        forderDestino.mkdir();
                    }

                    File archivoDestino = new File(forderDestino.getAbsolutePath().replace("C:\\Users\\Daniel\\Documents\\NetBeansProjects\\", "C:\\apache-tomcat-6.0\\webapps\\").concat("\\").concat(archivoOrigen.getName()));

                    if (!archivoDestino.exists()) {
                        archivoDestino.createNewFile();

                        if (!archivoOrigen.exists()) {
                            foto = "Error - No se encontró el archivo ".concat(n.getFoto());
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
                                foto = "Error - No fue posible escribir el archivo ".concat(n.getFoto()).concat(" en carpeta web: ").concat(e.getMessage());
                            }

                            foto = request.getContextPath().concat("/").concat(n.getFoto());

                        }
                    } else {
                        foto = request.getContextPath().concat("/").concat(n.getFoto());
                    }
                } catch (Exception e) {
                    throw new Fallo(e.getMessage());
                }
            } else {
                foto = "";
            }
    %>
    <nota id="<%=n.getClaveNota()%>">
        <foto><![CDATA[<%=foto%>]]></foto>
        <nombre><![CDATA[<%=n.getNombre()%>]]></nombre>
        <titulo><![CDATA[<%=n.getTitulo()%>]]></titulo>
        <mensaje><![CDATA[<%=n.getMensaje()%>]]></mensaje>
        <fecha_nota><![CDATA[<%=n.getFechaNota()%>]]></fecha_nota>
    </nota><%
        }
    
    %>    
</notas_forma>        
<% } 
    
double total_paginas=0;
      
 if( frmTemp.getNumeroDeRegistros() >0 ) {
	total_paginas = Math.ceil(frmTemp.getNumeroDeRegistros()/numeroDeRegistros);
        if ((frmTemp.getNumeroDeRegistros() % numeroDeRegistros)>0)
                total_paginas+=1;
} else {
	total_paginas = 0;
}
%><pagina><%=pagina%></pagina>
<total><%= total_paginas %></total>
<registros><%=frmTemp.getNumeroDeRegistros()%></registros><%
int i = 0;
 /* Si no viene con registros solo muestra la estructura de datos */
 if (registros.size() == 0) {%>
<registro>
    <%
        for (Campo campo : campos.values()) {
    %><<%=campo.getNombre()%> <% if (campo.isAutoIncrement()) {%>autoincrement="TRUE" <%}%> tipo_dato="<%=campo.getTipoDato()%>"><![CDATA[]]>
    </<%=campo.getNombre()%>>
    <%
        }
    %></registro><%
    } else {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        NumberFormat nfMoney = new DecimalFormat("$###,###,###,##0.00");

        String data;
        for (ArrayList registro : registros) {
            int k = 0;
    %><registro>
    <%
        for (Campo campo : campos.values()) {
            //Le asigna el valor anterior del campo para registrarlo en la bitácora
            if (registro.get(k) != null) {
                campo.setValorOriginal(registro.get(k).toString());
            }

            if (registro.get(k) == null) {
                data = "";
            } else if (campo.getTipoDato().toLowerCase().equals("smalldatetime") || campo.getTipoDato().toLowerCase().equals("date")) {
                data = df.format(registro.get(k));
            } else if (campo.getTipoDato().toLowerCase().equals("datetime")) {
                data = dtf.format(registro.get(k));
            } else if (campo.getTipoDato().toLowerCase().equals("bit")) {
                data = registro.get(k).toString().equals("true") || registro.get(k).toString().equals("1") ? "1" : "0";
            } else if (campo.getTipoDato().toLowerCase().equals("money") || campo.getTipoDato().toLowerCase().equals("decimal")) {
                data = nfMoney.format(registro.get(k));
            } else {
                data = registro.get(k).toString();
            }

            //Verifica si el campo se refiere a algún archivo
            if (campo.getTipoControl() != null && data != null) {
                if (campo.getTipoControl().equalsIgnoreCase("file") && !data.equals("")) {
                    //Verifica si ya está el archivo en el cache
                    try {
                        File archivoOrigen = new File("c:\\intranet2.0\\".concat(data.replaceAll("/temp/", "").replace("/", "\\")));
                        String sAppPath = application.getRealPath("/").replace("C:\\Users\\Daniel\\Documents\\NetBeansProjects\\intranet\\build\\web\\", "C:\\apache-tomcat-6.0\\webapps\\intranet\\");
                        File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(user.getClave())).concat("\\"));
                        if (!forderDestino.exists()) {
                            if (forderDestino.mkdir()) {
                                System.out.println("Subdirectorio temporal creado");
                            } else {
                                throw new Fallo("No fue posible crear el directorio temporal '".concat(forderDestino.getAbsolutePath()).concat("'"));
                            }
                        }

                        File archivoDestino = new File(forderDestino.getAbsolutePath().concat("\\").concat(archivoOrigen.getName()));

                        if (!archivoDestino.exists()) {
                            archivoDestino.createNewFile();

                            if (!archivoOrigen.exists()) {
                                data = "Error - No se encontró el archivo ".concat(data);
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
                                    data = "Error - No fue posible escribir el archivo ".concat(data).concat(" en carpeta web: ").concat(e.getMessage());
                                }

                                //data = request.getContextPath().concat(data);

                            }
                        } else {
                            //data = request.getContextPath().concat(data);
                        }
                    } catch (Exception e) {
                        data = "Error: No fue posible recuperar el documento solicitado, ".concat(e.getMessage());
                    }
                }
            }
            
            if (campo.getNombre().equals("clave_publicacion_padre")) {
            
            }
            //Se trae la foto del empleado al directorio temp si el campo es igual a clave_empleado
            String foto = "";
            Usuario u = new Usuario();
            if (campo.getNombre().equals("clave_empleado")) {
                try {
                    u.setCx(user.getCx());
                    u.setClave(Integer.parseInt(campo.getValorOriginal()));
                    
                    if (u.getFoto()!=null) {
                        File archivoOrigen = new File("c:\\intranet2.0\\".concat(u.getFoto().replace("/intranet/temp/","")));

                        String sAppPath= "";
                        if (application.getRealPath("/").equals("C:\\Users\\Daniel\\Documents\\NetBeansProjects\\intranet\\build\\web\\")) {
                            sAppPath = application.getRealPath("/");
                        } else {
                            sAppPath = application.getRealPath("/");
                        }


                        File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(u.getClave())).concat("\\"));
                        if (!forderDestino.exists()) {
                            forderDestino.mkdir();
                        }

                        //File archivoDestino = new File(forderDestino.getAbsolutePath().replace("C:\\Users\\Daniel\\Documents\\NetBeansProjects\\", "C:\\apache-tomcat-6.0\\webapps\\").concat("\\").concat(archivoOrigen.getName()));
                        File archivoDestino = new File(forderDestino.getAbsolutePath().concat("\\").concat(archivoOrigen.getName()));

                        if (!archivoDestino.exists()) {
                            archivoDestino.createNewFile();

                            if (!archivoOrigen.exists()) {
                                foto = "Error - No se encontró el archivo ".concat(u.getFoto());
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
                                    foto = "Error - No fue posible escribir el archivo ".concat(u.getFoto()).concat(" en carpeta web: ").concat(e.getMessage());
                                }

                                foto = u.getFoto();

                            }
                        } else {
                            foto = u.getFoto();
                        }
                    }    
                } catch (Exception e) {
                    throw new Fallo(e.getMessage());
                }
            } else {
                foto = "";
            }

    %><<%=campo.getNombre()%> <% if (campo.isAutoIncrement()) {%> autoincrement="TRUE" <%}%>tipo_dato="<%=campo.getTipoDato()%>"><![CDATA[<%=data%>]]><%
          if (campo.getNombre().equals("clave_empleado")) {%><foto><%=foto%></foto>
    <nombre_empleado><%=u.getNombre()%></nombre_empleado>
    <% }%></<%=campo.getNombre()%>>
    <%       
            k++;
        }
    %></registro>   
    <%
            }
        }
    %></qry><%
        /* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
    request.getSession().setAttribute("usuario", user);

%>