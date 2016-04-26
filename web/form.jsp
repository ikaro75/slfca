<?xml version="1.0" encoding="UTF-8"?><%@ page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@page import="java.util.ArrayList"
%><%@page import="mx.org.fide.modelo.*"
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
    Forma frmForma = new Forma();
    LinkedHashMap<String,Campo> campos = new LinkedHashMap<String,Campo>();
    ArrayList<ArrayList> registros = new ArrayList<ArrayList>();

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
        
        frmForma = new Forma(new Consulta(forma,tipoAccion, String.valueOf(pk), w, null, user), false);
        source = frmForma.getSQL();

        campos = frmForma.getCampos();
        registros = frmForma.getRegistros();

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
<permisos><% if (frmForma.isSelect()) {%>
    <permiso><clave_permiso>1</clave_permiso></permiso><%}
        if (frmForma.isInsert()) {%>
    <permiso><clave_permiso>2</clave_permiso></permiso><%}
        if (frmForma.isUpdate()) {%>
    <permiso><clave_permiso>3</clave_permiso></permiso><%}
        if (frmForma.isDelete()) {%>
    <permiso><clave_permiso>4</clave_permiso></permiso><%}
%></permisos>
<configuracion_forma>
    <clave_aplicacion><![CDATA[<%=frmForma.getClaveAplicacion()%>]]></clave_aplicacion>
    <clave_forma><![CDATA[<%=frmForma.getClaveForma()%>]]></clave_forma>
    <forma><![CDATA[<%=frmForma.getForma()%>]]></forma>
    <alias_tab><![CDATA[<%=frmForma.getAliasTab()%>]]></alias_tab>
    <evento_forma><![CDATA[<%=frmForma.getEventoForma()%>]]></evento_forma>
    <instrucciones><![CDATA[<% if (frmForma.getInstrucciones()!=null) { %><%=frmForma.getInstrucciones()%> <% } %>]]></instrucciones>
    <llave_primaria><![CDATA[<%=frmForma.getLlavePrimaria()%>]]></llave_primaria>
    <pk><![CDATA[<%=pk%>]]></pk>
    <muestra_formas_foraneas><![CDATA[<%=frmForma.isMuestraFormasForeaneas()%>]]></muestra_formas_foraneas>
    <clave_tipo_presentacion_forma_foranea><![CDATA[<%=frmForma.getClaveTipoPresentacionFormaForanea()%>]]></clave_tipo_presentacion_forma_foranea>
    <permite_duplicar_registro><![CDATA[<%=frmForma.isPermiteDuplicarRegistro()%>]]></permite_duplicar_registro>
    <permite_insertar_comentarios><![CDATA[<%=frmForma.isPermiteInsertarComentario()%>]]></permite_insertar_comentarios>
    <permite_guardar_como_plantilla><![CDATA[<%=frmForma.isPermiteGuardarComoPlantilla()%>]]></permite_guardar_como_plantilla>
</configuracion_forma>  
<% if (frmForma.isMuestraFormasForeaneas() && tipoAccion.equals("update")) {%><formas_foraneas><%    
ArrayList<Forma> formasForaneas=frmForma.getFormasForaneas();    
    for (Forma formaForanea : formasForaneas) {
            %><clave_forma_foranea><%=formaForanea.getClaveForma()%></clave_forma_foranea><%
            %><forma_foranea><%=formaForanea.getForma()%></forma_foranea><%
            %><clave_tipo_presentacion_forma_foranea><%=formaForanea.getClaveTipoPresentacionFormaForanea()%></clave_tipo_presentacion_forma_foranea><%
    } 
   %></formas_foraneas><%
} %>                
<% if (frmForma.isPermiteInsertarComentario() && tipoAccion.equals("update")) {%><notas_forma><%
    //Recupera de la base de datos los reportes y los coloca en la propiedad reporte de la forma del usuario
    ArrayList<Nota> notas = frmForma.getNotas(pk);
    String foto;
    //Se ejecuta el primer reporte de la lista por default
    for (Nota n : notas) {
        //Verifica si ya está el archivo en el cache
        if (n.getFoto() != null) {
            try {
                File archivoOrigen = new File("c:\\slfca\\".concat(String.valueOf(n.getClaveEmpleado())).concat("\\").concat(n.getFoto().substring(n.getFoto().lastIndexOf("/"))));
                String sAppPath = application.getRealPath("/").replace("\\build\\web", "");
                File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(n.getClaveEmpleado())).concat("\\"));
                if (!forderDestino.exists()) {
                    forderDestino.mkdir();
                }

                File archivoDestino = new File(forderDestino.getAbsolutePath().concat("\\").concat(archivoOrigen.getName()));

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

                        foto =n.getFoto();

                    }
                } else {
                    foto = n.getFoto();
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
<%}
    if (frmForma.isReport()) {
%><reportes><%
    //Recupera de la base de datos los reportes y los coloca en la propiedad reporte de la forma del usuario
    ArrayList<Reporte> reportes = frmForma.getReportes();

    //Se ejecuta el primer reporte de la lista por default
    for (Reporte r : reportes) {
        if ((r.isGenerarEnInsercion() && tipoAccion.equals("insert")) || (r.isGenerarEnActualizacion() && tipoAccion.equals("update"))) {
    %><reporte id="<%=r.getClaveReporte()%>"><![CDATA[<%=r.getReporte()%>]]></reporte><%
            }
        }
    %></reportes>
    <%}
        int i = 0;
        /* Si no viene con registros solo muestra la estructura de datos */
        if (registros.size() == 0) {%>
<registro>
    <%
        for (Campo campo : campos.values()) {
    %><<%=campo.getNombre()%> <% if (campo.isAutoIncrement()) {%>autoincrement="TRUE" <%}%> tipo_dato="<%=campo.getTipoDato()%>"><![CDATA[]]>
    <clave_campo><![CDATA[<%=campo.getClave()%>]]></clave_campo>
    <alias_campo><![CDATA[<%=campo.getAlias()%>]]></alias_campo>
    <obligatorio><%=campo.getObligatorio()%></obligatorio>
    <tipo_control><![CDATA[<%=campo.getTipoControl() == null ? "" : campo.getTipoControl()%>]]></tipo_control>
    <evento><![CDATA[<%=campo.getEvento() == null ? "" : campo.getEvento()%>]]></evento>
    <filtro_foraneo><%=campo.getFiltroForaneo() == null ? "" : campo.getClaveFormaForanea()%></filtro_foraneo>
    <no_permitir_valor_foraneo_nulo><%=campo.getNoPermitirValorForaneoNulo()%></no_permitir_valor_foraneo_nulo>
    <ayuda><![CDATA[<%=campo.getAyuda() == null ? "" : campo.getAyuda()%>]]></ayuda>
    <activo><%=campo.getActivo()%></activo>
    <tamano><%=campo.getTamano()%></tamano>
    <visible><%=campo.getVisible()%></visible>
    <valor_predeterminado><![CDATA[<%=campo.getValorPredeterminado() == null ? "" : campo.getValorPredeterminado()%>]]></valor_predeterminado>
    <justificar_cambio><%=campo.getJustificarCambio()%></justificar_cambio>
    <usado_para_agrupar><%=campo.getUsadoParaAgrupar()%></usado_para_agrupar>
    <permitir_rangos_en_busqueda><%=campo.getPermitirRangosEnBusqueda()%></permitir_rangos_en_busqueda>
    <carga_dato_foraneos_retrasada><%=campo.getCargaDatoForaneosRetrasada()%></carga_dato_foraneos_retrasada><%

        /* Extrae los datos del foraneo */
        if (campo.getClaveFormaForanea() != 0) {

            if (campo.getCargaDatoForaneosRetrasada() != 1) {
                try {
                        if (campo.getFiltroForaneo() != null) {
                            campo.setFormaForanea(campo.getClaveFormaForanea(), "", request.getParameter(campo.getFiltroForaneo()), user);
                        } else {
                            campo.setFormaForanea(campo.getClaveFormaForanea(), Integer.toString(pk), "", user);
                        }
                    } catch (Exception e) {
                        error = e.getMessage();
                    }
            }

            if (error.equals("")) {%>
    <foraneo clave_forma="<%=campo.getClaveFormaForanea()%>" <% if (campo.getEditaFormaForanea() > 0) {%>agrega_registro="true"<% }%> >
        <qry_<%=campo.getNombre()%>><%
            if (!error.equals("")) {%>
            <error><![CDATA["Problemas al recuperar la consulta de la forma foranea ligada al campo <%=campo.getNombre()%>: <%=error%>]]></error> <%
                //Limpia variable error después de reportarlo
                error = "";
            } else {
            %><sql_<%=campo.getNombre()%>><%
                if (campo.getCargaDatoForaneosRetrasada() == 1) {
                %><![CDATA["Foraneo con carga retrasada activada"]]><% } else {%><![CDATA[<%=campo.getFormaForanea().getSQL()%>]]><% }
                %></sql_<%=campo.getNombre()%>><%

                    if (campo.getCargaDatoForaneosRetrasada() != 1) {
                        for (int m = 0; m < campo.getFormaForanea().getRegistros().size(); m++) {
                %>
            <registro_<%=campo.getNombre()%>> 
                <%
                    for (int n = 0; n < campo.getFormaForanea().getCampos().size(); n++) { 
                        Object keySet[] =campo.getFormaForanea().getCampos().keySet().toArray();
                        Campo campoForaneo = campo.getFormaForanea().getCampos().get(keySet[n]);
                %><<%=campoForaneo.getNombre()%> tipo_dato='<%=campoForaneo.getTipoDato()%>'><![CDATA[<%=campo.getFormaForanea().getRegistros().get(m).get(n)%>]]></<%=campoForaneo.getNombre()%>><%
                    }
                %>
            </registro_<%=campo.getNombre()%>><%
                        }
                    }
                }
            %>
        </qry_<%=campo.getNombre()%>>
    </foraneo>    
    <% } else {%>
    <foraneo><error_foraneo><%=error%></error_foraneo></foraneo>
    <%    error = "";
            }
        }
        %>
    <tabla_busqueda><![CDATA[<%=campo.getTablaBusqueda()%>]]></tabla_busqueda>
    </<%=campo.getNombre()%>>
    <%
        }
    %></registro><%
    } else {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        NumberFormat nfMoney = new DecimalFormat("$###,###,###,##0.00");
        NumberFormat nfInt = new DecimalFormat("###,###,###,##0");
        NumberFormat nfFloat = new DecimalFormat("###,###,###,##0.00");
        
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
            }else if (campo.getTipoDato().toLowerCase().equals("bit")) {
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
                        File archivoOrigen = new File("c:\\slfca\\".concat(data.replaceAll("/temp/", "").replace("/","\\")));
                        String sAppPath = application.getRealPath("/").replace("C:\\Users\\Daniel\\Documents\\NetBeansProjects\\intranet\\build\\web\\","C:\\apache-tomcat-6.0\\webapps\\intranet\\");
                        File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(user.getClave())).concat("\\"));
                        if (!forderDestino.exists()) {
                            if (forderDestino.mkdir())
                                System.out.println("Subdirectorio temporal creado");
                            else 
                                throw new Fallo("No fue posible crear el directorio temporal '".concat(forderDestino.getAbsolutePath()).concat("'"));
                        }

                        File archivoDestino = new File(forderDestino.getAbsolutePath().concat("\\").concat(archivoOrigen.getName()));

                        if (!archivoDestino.exists()) {
                            archivoDestino.createNewFile();

                            if (!archivoOrigen.exists()) {
                                data = "Error - No se encontró el archivo ".concat(data);
                            } else {
                                try {

                                    FileChannel origen = new FileInputStream(archivoOrigen).getChannel();
                                    FileChannel destino = new FileOutputStream(archivoDestino).getChannel();

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
                        data ="Error: No fue posible recuperar el documento solicitado, ".concat(e.getMessage());
                    }
                }
            }
    %><<%=campo.getNombre()%> <% if (campo.isAutoIncrement()) {%> autoincrement="TRUE" <%}%>tipo_dato="<%=campo.getTipoDato()%>"><![CDATA[<%=data%>]]>
    <clave_campo><%=campo.getClave()%></clave_campo>
    <alias_campo><![CDATA[<%=campo.getAlias()%>]]></alias_campo>
    <obligatorio><%=campo.getObligatorio()%></obligatorio>
    <tipo_control><![CDATA[<%=campo.getTipoControl() == null ? "" : campo.getTipoControl()%>]]></tipo_control>
    <evento><![CDATA[<%=campo.getEvento() == null ? "" : campo.getEvento()%>]]></evento>
    <filtro_foraneo><%=campo.getFiltroForaneo() == null ? "" : campo.getClaveFormaForanea()%></filtro_foraneo>
    <no_permitir_valor_foraneo_nulo><%=campo.getNoPermitirValorForaneoNulo()%></no_permitir_valor_foraneo_nulo>
    <ayuda><![CDATA[<%=campo.getAyuda() == null ? "" : campo.getAyuda()%>]]></ayuda>
    <activo><%=campo.getActivo()%></activo>
    <tamano><%=campo.getTamano()%></tamano>
    <visible><%=campo.getVisible()%></visible>
    <valor_predeterminado><![CDATA[<%=campo.getValorPredeterminado() == null ? "" : campo.getValorPredeterminado()%>]]></valor_predeterminado>
    <justificar_cambio><%=campo.getJustificarCambio()%></justificar_cambio>
    <usado_para_agrupar><%=campo.getUsadoParaAgrupar()%></usado_para_agrupar>
    <permitir_rangos_en_busqueda><%=campo.getPermitirRangosEnBusqueda()%></permitir_rangos_en_busqueda><%

        /* Extrae los datos del foraneo */
        if (campo.getClaveFormaForanea() != 0) {
            //Si todavia no se ha cargado el catálogo foraneo, entonces se carga si no es de carga retrasada
            if (campo.getCargaDatoForaneosRetrasada() != 1) {
                if (campo.getFormaForanea() == null) {
                    if (campo.getFiltroForaneo() != null) {
                        try {
                            campo.setFormaForanea(campo.getClaveFormaForanea(),"", request.getParameter(campo.getFiltroForaneo()), user);
                        } catch (Exception e) {
                            error = e.getMessage();
                        }
                    } else {
                        try {
                            campo.setFormaForanea(campo.getClaveFormaForanea(), Integer.toString(pk), "", user);
                        } catch (Exception e) {
                            error = e.getMessage();
                        }

                    }
                }
            }

    %>
    <foraneo clave_forma="<%=campo.getClaveFormaForanea()%>" <% if (campo.getEditaFormaForanea() > 0) {%>agrega_registro="true"<% }%>>
        <qry_<%=campo.getNombre()%>><%
            if (!error.equals("")) {%>
            <error><![CDATA["Problemas al recuperar la consulta de la forma foranea ligada al campo <%=campo.getNombre()%>: <%=error%>]]></error> <%
                //Limpia variable error después de reportarlo
                error = "";
            } else {%>
            <sql_<%=campo.getNombre()%>><%
                if (campo.getCargaDatoForaneosRetrasada() == 1) {
                %><![CDATA["Foraneo con carga retrasada activada"]]><% } else {%><![CDATA[<%=campo.getFormaForanea().getSQL()%>]]><% }
                %></sql_<%=campo.getNombre()%>><%

                    if (campo.getCargaDatoForaneosRetrasada() != 1) {
                        for (int m = 0; m < campo.getFormaForanea().getRegistros().size(); m++) {
                %>
            <registro_<%=campo.getNombre()%>>
                <%
                    for (int n = 0; n < campo.getFormaForanea().getCampos().size(); n++) {
                        Object keySet[] =campo.getFormaForanea().getCampos().keySet().toArray();
                        Campo campoForaneo = campo.getFormaForanea().getCampos().get(keySet[n]);
                %><<%=campoForaneo.getNombre()%> tipo_dato='<%=campoForaneo.getTipoDato()%>'><![CDATA[<%=campo.getFormaForanea().getRegistros().get(m).get(n)%>]]></<%=campoForaneo.getNombre()%>>
                <%
                    }
                %></registro_<%=campo.getNombre()%>><%
                            }
                        }
                    }
                %></qry_<%=campo.getNombre()%>>
    </foraneo>
    <tabla_busqueda><![CDATA[<%=campo.getTablaBusqueda()%>]]></tabla_busqueda>
    <% }
    %>
    </<%=campo.getNombre()%>>
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