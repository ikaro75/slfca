<?xml version="1.0" encoding="UTF-8"?><%@page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@page import="java.io.IOException"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.nio.channels.FileChannel"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="org.apache.tomcat.util.http.fileupload.FileItem"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.tomcat.util.http.fileupload.DiskFileUpload"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList" 
%><%@page import="mx.org.fide.modelo.*"
%><%@page import="mx.org.fide.encuesta.*"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat"
%><%@page import="mx.org.fide.reporte.Reporte"
%><%@page import="java.util.LinkedHashMap"
%><%@page import="java.util.Date"%><%@page import="mx.org.fide.utilerias.Utilerias"%><%

//El objetivo es crear el html que contenga la sección de preguntas seleccionado por llave_primaria,
// incluyendo las posibles respuestas, y si ya fue contestada presentar la respuesta,
// se debe considerar la presentación del control para auditoria (catálogo)
//response.setContentType("text/xml"); 

    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");

    String error = "";
    int forma = 0;;
    String tipoAccion = "";
    String w = "";
    String source = "";
    String result = "";
    int pk = 0;
    Forma frmTemp  = new Forma();
    response.setContentType("text/xml");
    Usuario user = (Usuario) request.getSession().getAttribute("usuario");
    
    String participante ="";
    Integer claveCuestionario = 0;
    Integer claveCuestionarioParticipante = 0;
    int claveSeccion = 0;

    Consulta consulta = null;
    CuestionarioParticipante cuestionarioActual = null;
    Cuestionario cuestionario = null;
    Seccion seccion = null;
    RespuestaParticipante respuestaParticipante = null;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    StringBuilder resultado = new StringBuilder("");
    String siguienteAccion = "";

    Forma frmRespuestaParticipante = new Forma();

    LinkedHashMap campos = new LinkedHashMap();
    Campo campo = null;
    ArrayList<ArrayList> registros = new ArrayList<ArrayList>();

    user = (Usuario) request.getSession().getAttribute("usuario");
    
    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }

    HashMap requestParameters = new HashMap();

    DiskFileUpload requestWU = new DiskFileUpload();
    requestWU.setSizeMax(-1); // 2MB
    requestWU.setSizeThreshold(20480); // tamaño por encima del cual los ficheros son escritos directamente en disco
    File folder = new File("C:\\slfca\\".concat(String.valueOf(user.getClave())));

    if (!folder.exists()) {
        folder.mkdir();
    }


    try {
        requestWU.setRepositoryPath("C://slfca//".concat(String.valueOf(user.getClave())));
        List requestFields = requestWU.parseRequest(request);
        
        for (int i = 0; i < requestFields.size(); i++) {
            FileItem fiField = (FileItem) requestFields.get(i);
            if (fiField.getName() != null && !fiField.getName().equals("")) {
                String fileName = Utilerias.decodeURIComponentX(fiField.getName());
                String extension = fileName.substring(fileName.indexOf(".") + 1);
                if (extension.equalsIgnoreCase("jsp")
                        || extension.equalsIgnoreCase("php")
                        || extension.equalsIgnoreCase("js")
                        || extension.equalsIgnoreCase("com")
                        || extension.equalsIgnoreCase("dll")
                        || extension.equalsIgnoreCase("pl")
                        || extension.equalsIgnoreCase("py")
                        || extension.equalsIgnoreCase("exe")
                        || extension.equalsIgnoreCase("vb")
                        || extension.equalsIgnoreCase("reg")
                        || extension.equalsIgnoreCase("perl")
                        || extension.equalsIgnoreCase("java")
                        || extension.equalsIgnoreCase("bat")
                        || extension.equalsIgnoreCase("asp")) {
                    //Se borra el archivo
                    fiField.delete();
                    throw new Fallo("Tipo de archivo no permitido");
                }

                try {
                    // construimos un objeto file para recuperar el trayecto completo
                    File archivo = new File(Utilerias.decodeURIComponent((fileName)));

                    //Se guarda el archivo en la librería
                    archivo = new File("c:\\slfca\\".concat(String.valueOf(user.getClave())).concat("\\").concat(archivo.getName()));
                    // escribimos el fichero colgando del nuevo path
                    fiField.write(archivo);

                    //Se deja una copia en el caché
                    String sAppPath=application.getRealPath("/").replace("\\build", "");

                     File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(user.getClave())).concat("\\"));
                     if (!forderDestino.exists()) {
                        forderDestino.mkdir();
                     } 

                     File archivoDestino = new File(forderDestino.getAbsolutePath().concat("\\").concat(archivo.getName()));
					 
                     if (!archivoDestino.exists()) {
                        archivoDestino.createNewFile();
                     }
					 
                     try {
			    FileChannel origen = new FileInputStream(archivo).getChannel();
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
                        error = "Error - No fue posible escribir el archivo en la carpeta temporal: ".concat(e.getMessage());
                    }
					 
                    requestParameters.put(fiField.getFieldName(),"/slfca/temp/".concat(user.getClave().toString()).concat("/").concat(Utilerias.decodeURIComponent(fileName)));
                } catch (IOException ioe) {
                    System.out.printf(ioe.getMessage());
                    error = ioe.getMessage();
                    //throw new Fallo(ioe.getMessage() );
                } catch (Exception e) {
                    System.out.printf(e.getMessage());
                    error = e.getMessage();
                    //throw new Fallo(e.getMessage() );
                }
            } else {
                //Va construyendo el queryParameter para llamar por get a register.jsp
                requestParameters.put(fiField.getFieldName(), Utilerias.decodeURIComponentXX(fiField.getString()));
            }

        }
    } catch (Exception e) {
        System.out.printf(e.getMessage());
        error = e.getMessage();
    }
    

    try {

        if (request.getParameter("$ccp") == null) {
            throw new Fallo("Falta parámetro $ccp");
        } else {
            try {
                claveCuestionarioParticipante = Integer.parseInt(request.getParameter("$ccp"));
            } catch (Exception e) {
                throw new Fallo("El parámetro $ccp no es válido, verifique");
            }
        }

        if (request.getParameter("$ta") == null) {
            throw new Fallo("Falta parámetro $ta");
        } else {
            tipoAccion = request.getParameter("$ta");
        }

        if (requestParameters.get("$pk") != null) {
            try {
                pk = Integer.parseInt((String) requestParameters.get("$pk"));
            } catch (Exception e) {
                throw new Fallo("El parámetro $pk no es válido, verifique");
            }
        }

        if (request.getParameter("$w") != null) {
            w = request.getParameter("$w");
        }

        cuestionarioActual = new CuestionarioParticipante(claveCuestionarioParticipante, user);
        cuestionario = new Cuestionario(cuestionarioActual.getClaveCuestionario(), user);

        /* Verifica si existe la relación entre el participante y el cuestionario y los permisos del perfil del usuario*/
        cuestionarioActual.setSQL(633, "update", String.valueOf(pk), w);
        
        if  (cuestionarioActual.getClavePunto()!=0) {
            consulta = new Consulta(681,"select",cuestionarioActual.getClavePunto().toString(),"clave_punto=".concat(cuestionarioActual.getClavePunto().toString()), null,user);
            participante = "<br />".concat(consulta.getRegistros().get(0).get(3).toString().concat(" ").concat(consulta.getRegistros().get(0).get(1).toString().replaceAll("\\<div.*?>", "").replaceAll("\\</div>", "").replaceAll("\\<a>", "")));
            if (participante.indexOf("<a")>-1) {
                participante = participante.substring(0,participante.indexOf("<a")).concat("<br />").concat(consulta.getRegistros().get(0).get(4).toString()).concat("<br />").concat(formatter.format(cuestionarioActual.getFechaInicio()));
            } else {
                participante = participante.concat("<br />").concat(consulta.getRegistros().get(0).get(4).toString()).concat("<br />").concat(formatter.format(cuestionarioActual.getFechaInicio()));
            }
            
        }
        
         if (cuestionarioActual.getClaveBeneficiario()!=0) {
            consulta = new Consulta(613,"select",cuestionarioActual.getClaveBeneficiario().toString(),"clave_beneficiario=".concat(cuestionarioActual.getClaveBeneficiario().toString()), null,user);
            participante = "<br />".concat(consulta.getRegistros().get(0).get(2).toString());
            participante = participante.concat("<br />").concat(formatter.format(cuestionarioActual.getFechaInicio()));
        }
         
        /*consulta = new Consulta(367, "select",String.valueOf(pk), w, user);

         source = consulta .getSQL();
         campos = consulta.getCampos();
         registros = consulta.getRegistros();        
         consulta.getCampos().get("clave_cuestionario").setValor(String.valueOf(cuestionarioActual.getClaveCuestionario()));
         consulta.getCampos().get("clave_estudio_participante").setValor(String.valueOf(cuestionarioActual.getClaveEstudioParticipante()));
         consulta.getCampos().get("clave_participante").setValor(String.valueOf(cuestionarioActual.getClaveParticipante()));
         consulta.getCampos().get("clave_estatus").setValor(String.valueOf(cuestionarioActual.getClaveEstatus()));
         consulta = new CuestionarioParticipante(consulta);
         cuestionarioActual = new CuestionarioParticipante(consulta);*/
        cuestionarioActual.getCampos().get("clave_cuestionario").setValor(String.valueOf(cuestionarioActual.getClaveCuestionario()));
        cuestionarioActual.getCampos().get("clave_punto").setValor(String.valueOf(cuestionarioActual.getClavePunto()));
        cuestionarioActual.getCampos().get("clave_beneficiario").setValor(String.valueOf(cuestionarioActual.getClaveBeneficiario()));
        cuestionarioActual.getCampos().get("clave_estatus").setValor(String.valueOf(cuestionarioActual.getClaveEstatus()));
        cuestionarioActual.setClaveCuestionarioParticipante(claveCuestionarioParticipante);

        frmRespuestaParticipante = new Forma(new Consulta(634, "update", "0", "",null, user), false);
        /* Es necesario recuperar la clave de la siguiente pregunta,
         sin embargo al momento de darse de alta el cuestionario participante se debe
         insertar la primera pregunta del cuestionario, la pregunta debe recuperar para llevar a la clave de la sección en donde se debe iniciar/reiniciar */

        /**
         * *** ATENCION - aqui me quedé, hace falta implementar validacion
         * cuando cuestionarioActual.getSiguientePregunta() es nulo **
         */
        Pregunta pregunta = new Pregunta(cuestionarioActual.getSiguientePregunta(), user);
        seccion = new Seccion(pregunta.getClaveSeccion(), user);

    } catch (Fallo f) {
        error = f.getMessage();

    } catch (Exception e) {
        error = e.getMessage();

    } finally {%><survey><%
        if (error == null) {
            error = "";
        }
        if (!error.equals("")) {%>
    <error><![CDATA[<%=error%>]]></error>
</survey><%
            return;
        }
    }

    try {
        /* Verifica si existe la relación entre el participante y el cuestionario */

        /*cuestionarioActual =new CuestionarioParticipante(seccion.getClaveCuestionario(), user);*/

        if (cuestionarioActual.getClaveEstatus() == 4) {
            throw new Fallo("El cuestionario ya fue contestado anteriormente y el participante resultó eliminado.");
        }

        if (cuestionarioActual.getClaveEstatus() == 5) {
            throw new Fallo("El cuestionario ya fue contestado anteriormente y fue excluido.");
        }

        cuestionario = new Cuestionario(seccion.getClaveCuestionario(), user);%>
<configuracion_survey>
    <cuestionario><%=cuestionario.getCuestionario()%></cuestionario>
    <evento><%=cuestionario.getEvento()%></evento>
    <seccion_actual><%=seccion.getClaveSeccion()%></seccion_actual>
    <primera_seccion><%=cuestionario.getPrimeraSeccion()%></primera_seccion>
    <ultima_seccion><%=cuestionario.getUltimaSeccion()%></ultima_seccion>
    <estatus><%=cuestionarioActual.getClaveEstatus()%></estatus>
</configuracion_survey>      
<% if (tipoAccion.equals("open")) {%> 
<html>        
    <![CDATA[
    <form id="surveyform_<%=cuestionarioActual.getClaveCuestionarioParticipante()%>" class="forma">
        <table style="width: 80%;">
            <tr>
                <td >
                    <h1><%=cuestionario.getCuestionario().concat(" ").concat(participante)%></h1>
                </td>    
            </tr>
            <%if (cuestionario.getDescripcion()!=null) {%>
            <tr>
                <td>
                    <h2><%=cuestionario.getDescripcion()%></h2>
                </td>    
            </tr><%} 
            if (!seccion.getSeccion().equals("-")) {%>    
            <tr>
                <td>
                   
                </td>    
            </tr><%}
            
            if (seccion.getInstruccion() != null) {
            %><tr>
                <td>
                    <h4 style="font-size: 12.5px"><%=seccion.getInstruccion()%></h4>
                </td>    
            </tr><%
                }
                for (Pregunta pregunta : seccion.getPreguntas()) {%><%
                    if (pregunta.getInstruccion() != null) {%>
            <tr>
                <td><strong><%=pregunta.getInstruccion()%></strong></td>
            </tr><% }
            %><tr class="trPregunta">  
                <td style="font-family: arial;font-size: 13px;"><div id="pregunta_<%=pregunta.getClavePregunta()%>"><%=pregunta.getPregunta()%><% if (pregunta.getObligatoria()) {%>&nbsp;&nbsp;(<span class="mensajeobligatorio">Obligatoria</span>*)<% }%></div></td>
            </tr>    
            <tr>
                <td style="font-family: arial;font-size: 13px;">
                    <%
                        switch (pregunta.getClaveTipoPregunta()) {
                            case 1:
                                /* Pregunta abierta  */
                                try {
                                    respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clave_pregunta", pregunta.getClavePregunta(), user);
                    %><input type="text"  id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" value="<% if (respuestaParticipante.getRespuesta() != null) {%><%=respuestaParticipante.getRespuesta()%><% }%>" class="singleInput <%if (pregunta.getObligatoria()) {%>obligatorio<% }
                          if (pregunta.getClaveTipoDatoRespuesta() == 3) {%><%=" fecha\" onBlur='javascript:check_date(this)'"%><%} else {%>"<% }
                              if (pregunta.getClaveTipoDatoRespuesta() == 2) {%><%=" onBlur='javascript:check_number(this);' "%><%}
                                                         if (frmRespuestaParticipante.getCampos().get("clave_respuesta").getActivo() == 0) {%> disabled="disabled" <% }
                                                     } catch (Exception e) {
                           %>Algo pasó mal<%                                  }
                           %>/>
                    <br /><% if (pregunta.getTextoFinal() != null) {%><%=pregunta.getTextoFinal()%><% }
                            break;
                        case 2:
                            /* Pregunta abierta extendida (text area) */
                            try {
                                respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clave_pregunta", pregunta.getClavePregunta(), user);
                        %><textarea type="text" id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" class="singleInput<%if (pregunta.getObligatoria()) {%> obligatorio <% }%>"<% if (frmRespuestaParticipante.getCampos().get("clave_respuesta").getActivo() == 0) {%> readonly<% }%>><% if (respuestaParticipante.getRespuesta() != null) {%><%=respuestaParticipante.getRespuesta()%><% }
                                } catch (Exception e) {
                                }
                        %></textarea><br /><% if (pregunta.getTextoFinal() != null) {%><%=pregunta.getTextoFinal()%><% }
                                break;
                        case 3:
                        /* Opciones excluyentes con botones de radio*/
                            ArrayList <Integer> respuestasTipoOtro = pregunta.respuestasTipoOtro();
                            respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clave_pregunta", pregunta.getClavePregunta(),user);
                            StringBuilder condicionOtro = new StringBuilder();
                            for (Respuesta respuesta : pregunta.getRespuestas()) {
                             try {
                                %><input tipo_dato="int" type="radio" <%
                                if (pregunta.getObligatoria()) {%> class="obligatorio"<% }
                                 %> id="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>"<%
                                
                                if (frmRespuestaParticipante.getCampos().get("clave_respuesta").getActivo()==0 ) {%> disabled="disabled"<% }
                                 %> value="<%
                                if (respuesta.getCodigoRespuesta() != null) {%><%=respuesta.getCodigoRespuesta()%><% } 
                                else {%><%=respuesta.getClaveRespuesta()%>" <% }
                                
                                if (respuesta.getClaveRespuesta().intValue()==respuestaParticipante.getClaveRespuesta().intValue()) {
                                    %> checked="checked" <% }
                                
                                if (respuesta.getOtro()) {
                                %> onclick="javascript:$.each($('#surveyform_<%=cuestionarioActual.getClaveCuestionarioParticipante()%> input[name=otro_<%=respuestaParticipante.getClaveRespuestaParticipante()%>]'),function(index, el){$(this).attr('disabled','disabled');});$(this).next().removeAttr('disabled','')"<%
                                } else {
                                    //Es necesario saber si hay respuesta de tipo otro para deshabilitar
                                    %> onclick="javascript:$.each($('#surveyform_<%=cuestionarioActual.getClaveCuestionarioParticipante()%> input[name=otro_<%=respuestaParticipante.getClaveRespuestaParticipante()%>]'),function(index, el){$(this).attr('disabled','disabled');});"<% 
                                }

                            %> /><%=respuesta.getRespuesta()%><% 
                                if (respuesta.getOtro()){
                                %>&nbsp;<input type="text" style="float: none; width: 40%;" id="otro_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="otro_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" value="<% if (respuestaParticipante.getOtro() !=null && respuesta.getClaveRespuesta().intValue() == respuestaParticipante.getClaveRespuesta().intValue()) { %><%=respuestaParticipante.getOtro()%><% } %>" class="singleInput<%   
                                        if (pregunta.getClaveTipoDatoRespuesta()==3) {%><%=" fecha\" onBlur='javascript:check_date(this)'"%><%} else { %>"<% } 
                                        if (pregunta.getClaveTipoDatoRespuesta()==2) {%><%=" onBlur='javascript:check_number(this)' "%><%} 
                                        if (frmRespuestaParticipante.getCampos().get("clave_respuesta").getActivo()==0) {%> disabled="disabled" <% }
                                        if (respuesta.getClaveRespuesta()!=respuestaParticipante.getClaveRespuesta()) {%> disabled="disabled" <% } %>><% if (respuesta.getTextoDespuesDeCampoOtro()!=null) {%><%=respuesta.getTextoDespuesDeCampoOtro()%><% } %><br /><%
                                } else {
                                        %><br /><% 
                                } 
                                
                            } catch (Exception e) {
                            }
                           }  
 
                            break;
                    case 4:
                    /*
                     * Opciones excluyentes en lista desplegable
                     */
                        %><select id="<%=pregunta.getClavePregunta()%>"><%
                        for (Respuesta respuesta : pregunta.getRespuestas()) {
                            if (respuesta.getCodigoRespuesta() != null) {
                                    %><option  value="<%=respuesta.getCodigoRespuesta()%>" /><%=respuesta.getRespuesta()%><%
                        } else {
                                    %><option value="<%=respuesta.getClaveRespuesta()%>"  /><%=respuesta.getRespuesta()%><%
                                }
                            }
                            %></select><%
                        break;
                    case 5:
                        /* Opciones múltiples*/
                        for (Respuesta respuesta : pregunta.getRespuestas()) {
                            try {
                                respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "claveRespuesta", respuesta.getClaveRespuesta(), user);
                        %><input type="checkbox" tipo_dato="int" id="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" <%if (pregunta.getObligatoria()) {%> class="obligatorio pregunta<%=respuesta.getClavePregunta()%> "<% } %> value="<% 
                        if (respuesta.getCodigoRespuesta() != null) {
                        %><%=respuesta.getCodigoRespuesta()%><%} 
                        else { 
                        %><%=respuesta.getClaveRespuesta()%><%
                        }
                        %>"<%
                        if (respuestaParticipante.getRespuesta()!=null) {
                            if (respuesta.getClaveRespuesta().intValue()==Integer.parseInt(respuestaParticipante.getRespuesta())) {
                        %> checked="checked"<% } 
                          }

                         if (frmRespuestaParticipante.getCampos().get("claveRespuesta").getActivo()==0) {%> disabled="disabled" <% }      
                        } catch (Exception e) {
                            error= e.getMessage();
                        }
                        %>><%=respuesta.getRespuesta()%><br/><%
                        }
                        break;
                case 6:
                    %><input id="pregunta_<%=pregunta.getClavePregunta()%>" />&nbsp;<%=pregunta.getTextoFinal() != null ? pregunta.getTextoFinal() : ""%> <%
                    break;
                case 7:
                    /*
                     * Editor web con formato
                     */
                    break;
                case 8: 
                    //Codigo para traer archivo de la carpeta del usuario y copiar al temp (si existe)
                    
                default:

                    break;
            }
                    %><br /></td>
            </tr>
            <% }%>    
        </table>
        <input type="hidden" tipo_dato="int" name="$ccp" value="<%=cuestionarioActual.getClaveCuestionarioParticipante()%>">
        <input type="hidden" name="$cs" value="<%=seccion.getClaveSeccion()%>">
        <input type="hidden" name="$cf" value="634">
        <input type="hidden" name="$pk" value="<%=pk%>">
        <input type="hidden" id="$hacia" name="$hacia" value="">
    </form> 
]]></html><% } else if (tipoAccion.equals("save")) {

            if (request.getParameter("$ccp") == null) {
                throw new Fallo("Falta parámetro $ccp");
            } else {
                try {
                    claveSeccion = Integer.parseInt(request.getParameter("$cs"));

                } catch (Exception e) {
                    throw new Fallo("El parámetro $cs no es válido, verifique");
                }

                Integer claveRespuestaParticipante = 0;
                seccion = new Seccion(claveSeccion, user);
                for (Pregunta pregunta : seccion.getPreguntas()) {

                    switch (pregunta.getClaveTipoPregunta()) {
                        case 1:  case 2: case 6: case 7: case 8: 
                            /* Pregunta abierta, abierta extendida, fill in the blanks, editor web */
                            respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clave_pregunta", pregunta.getClavePregunta(), user);
                            claveRespuestaParticipante = respuestaParticipante.getClaveRespuestaParticipante();
                            
                            requestParameters.get("respuesta_".concat(claveRespuestaParticipante.toString()));
                            if (request.getParameter("respuesta_".concat(claveRespuestaParticipante.toString())) == null) {
                                continue;
                            }

                            if (request.getParameter("respuesta_".concat(claveRespuestaParticipante.toString())).equals("")) {
                                continue;
                            }
                            
                            if (pregunta.getClaveTipoPregunta()==8) {
                                
                            }
                            respuestaParticipante.setSQL(634, "update", claveRespuestaParticipante.toString(), "");
                            respuestaParticipante.setRespuesta(Utilerias.decodeURIComponentXX(request.getParameter("respuesta_".concat(claveRespuestaParticipante.toString()))));
                            respuestaParticipante.getCampos().get("respuesta").setValor(pregunta.getClaveTipoPregunta()==8?Utilerias.decodeURIComponentXX(respuestaParticipante.getRespuesta().replace("C:\\fakepath\\","")):Utilerias.decodeURIComponentXX(respuestaParticipante.getRespuesta()));
                            respuestaParticipante.getCampos().get("fecha").setValor(formatter.format(new Date()));
                            respuestaParticipante.getCampos().get("clave_empleado").setValor(String.valueOf(user.getClave()));
                            resultado.append(respuestaParticipante.update(true));
                            if (resultado.toString().contains("<error>")) {
                                throw new Fallo("Error al guardar respuesta ".concat(claveRespuestaParticipante.toString()).concat(" error: ").concat(resultado.toString().substring(resultado.toString().indexOf("<error><![CDATA[")).replace("<error><![CDATA[", "").replace("']]></error>", "")));
                            }

                            break;
                        case 3: case 4:
                            /* Opciones excluyentes con botones de radio y opciones excluyentes en lista desplegable  */
                            StringBuilder siEstaEsLaRespuesta = new StringBuilder("");
                            Integer siguientePregunta;

                            respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clave_pregunta", pregunta.getClavePregunta(), user);
                            respuestaParticipante.setSQL(634, "update", respuestaParticipante.getClaveRespuestaParticipante().toString() , w);
                            claveRespuestaParticipante = respuestaParticipante.getClaveRespuestaParticipante();

                            if (request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString())) == null) {
                                continue;
                            }

                            if (request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString())).equals("")) {
                                continue;
                            }

                            respuestaParticipante.setClaveRespuesta(Integer.parseInt(request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString()))));
                            Respuesta respuestaSeleccionada = new Respuesta(respuestaParticipante.getClaveRespuesta(), user);

                            //Se requiere llenar los campos del objeto consulta.campo
                            if (respuestaSeleccionada.getOtro()) {
                                if (request.getParameter("otro_".concat(respuestaParticipante.getClaveRespuestaParticipante().toString())) != null && request.getParameter("otro_".concat(respuestaParticipante.getClaveRespuestaParticipante().toString())) != "") {
                                    respuestaParticipante.getCampos().get("otro").setValor(Utilerias.decodeURIComponentXX(request.getParameter("otro_".concat(respuestaParticipante.getClaveRespuestaParticipante().toString()))));
                                }
                            }
                            respuestaParticipante.getCampos().get("clave_respuesta").setValor(respuestaParticipante.getClaveRespuesta().toString());
                            respuestaParticipante.getCampos().get("fecha").setValor(formatter.format(new Date()));
                            respuestaParticipante.getCampos().get("clave_empleado").setValor(String.valueOf(user.getClave()));
                            resultado.append(respuestaParticipante.update(true));
                            if (resultado.toString().contains("<error>")) {
                                throw new Fallo("Error al guardar respuesta ".concat(claveRespuestaParticipante.toString()).concat(" ").concat(resultado.toString().substring(resultado.toString().indexOf("<error><![CDATA[")).replace("<error><![CDATA[", "").replace("']]></error>", "")));
                            } else {
                                //El flujo de información depende en primer lugar de la respuesta seleccionada
                                //la siguiente acción se determina colocando el valor de CuestionarioParticipante.siguientePregunta
                                switch (respuestaSeleccionada.getSiEstaEsLaRespuesta()) {
                                    case 0: //No realizar operación especial
                                        //Verifica si es la última pregunta del cuestionario
                                        if (siguienteAccion.equals("")) {
                                            cuestionarioActual.setClaveEstatus(3);
                                            cuestionarioActual.getCampos().get("clave_estatus").setValor("3");
                                            siguienteAccion = "continua";
                                        }
                                        break;
                                    case 1: //Ir a la pregunta...
                                        break;
                                    case 2: //No despegar las preguntas...
                                        break;
                                    case 3: //No desplegar la sección
                                        break;
                                    case 4: //Excluir al participante y aplicar cuestionario de finalización
                                        //Si alguna de las respuestas ya marcó como finalizado el cuestionario no hace falta volverlo a marcar
                                        if (cuestionarioActual.getClaveEstatus() != 4) {
                                            cuestionarioActual.setClaveEstatus(4);
                                            cuestionarioActual.getCampos().get("clave_estatus").setValor("4");
                                            cuestionarioActual.update(true);
                                        }
                                        break;
                                }
                            }

                            break;
                        case 5:
                            /* Opciones múltiples   */
                            for (Respuesta respuesta : pregunta.getRespuestas()) {
                                respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clave_respuesta", respuesta.getClaveRespuesta(), user);
                                respuestaParticipante.setSQL(634, "update", respuestaParticipante.getClaveRespuestaParticipante().toString() , w);
                                claveRespuestaParticipante = respuestaParticipante.getClaveRespuestaParticipante();

                                if (request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString())) == null) {
                                    continue;
                                }

                                if (request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString())).equals("")) {
                                    continue;
                                }

                                respuestaParticipante.setClaveRespuesta(Integer.parseInt(request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString()))));
                                respuestaParticipante.getCampos().get("respuesta").setValor(respuestaParticipante.getClaveRespuesta().toString());
                                respuestaParticipante.getCampos().get("fecha").setValor(formatter.format(new Date()));
                                respuestaParticipante.getCampos().get("clave_empleado").setValor(String.valueOf(user.getClave()));
                                resultado.append(respuestaParticipante.update(true));
                                if (resultado.toString().contains("<error>")) {
                                    throw new Fallo("Error al guardar respuesta ".concat(claveRespuestaParticipante.toString()).concat(" ").concat(resultado.toString().substring(resultado.toString().indexOf("<error><![CDATA[")).replace("<error><![CDATA[", "").replace("']]></error>", "")));
                                }

                            }
                            break;
                    }
                }

                //Caso en el que se excluyó el estudio
                if (request.getParameter("$hacia").equals("atras")) {
                    Integer siguientePregunta = new Seccion(seccion.getAnterior(), user).getPrimeraPregunta();
                    cuestionarioActual.getCampos().get("siguiente_pregunta").setValor(siguientePregunta.toString());
                    cuestionarioActual.setSiguientePregunta(siguientePregunta);
                    cuestionarioActual.update(true);

                    if (siguienteAccion.equals("")) {
                        siguienteAccion = "continua";
                    }
                } else if (request.getParameter("$hacia").equals("adelante")){
                    //¿Es la última seccion?
                    if (seccion.getClaveSeccion() == cuestionario.getUltimaSeccion()) {
                        //Cierra cuestionario y emite alertas
                        //Pone la siguiente pregunta en el inicio del cuestionario para la revisión del auditor
                        cuestionarioActual.getCampos().get("siguiente_pregunta").setValor(cuestionario.getPrimeraPregunta().toString());
                        cuestionarioActual.getCampos().get("fecha_final").setValor("%ahora");
                        cuestionarioActual.setClaveEstatus(6);
                        cuestionarioActual.getCampos().get("clave_estatus").setValor("6");
                        cuestionarioActual.update(true);
                        //siguienteAccion="abre formulario ".concat(estudio.getClaveCuestionarioFinalizacion().toString());
                        //siguienteAccion="Solicita autenticacion";
                        siguienteAccion = "Finaliza cuestionario";

                    } else {

                        if (user.getClavePerfil() == 2 && cuestionarioActual.getClaveEstatus() == 2) {
                            cuestionarioActual.getCampos().get("clave_estatus").setValor("3");
                        }

                        cuestionarioActual.getCampos().get("siguiente_pregunta").setValor(new Seccion(seccion.getSiguiente(), user).getPrimeraPregunta().toString());
                        cuestionarioActual.update(true);

                        if (siguienteAccion.equals("")) {
                            siguienteAccion = "continua";
                        }
                    }
                } else if (request.getParameter("$hacia").equals("guarda")){          
                    siguienteAccion = "cierra";
                }
            }
        }
    } catch (Fallo f) {
        error = f.getMessage();

    } catch (Exception e) {
        error = e.getMessage();

    } finally {%><%
        /*
         * guarda el objeto usuario en la sesión para aprovechar los objetos que
         * se tiene abiertos
         */

        request.getSession().setAttribute("usuario", user);
        if (error == null) {
            error = "";
    }
    if (!error.equals("")) {%>
    <error><![CDATA[<%=error%>]]></error>
        <% }

            if (!siguienteAccion.equals("")) {%>
    <siguiente_accion><![CDATA[<%=siguienteAccion%>]]></siguiente_accion>
        <%=resultado%>
        <% }
        }%>
</survey>