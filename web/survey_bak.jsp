<?xml version="1.0" encoding="UTF-8"?><%@page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@page import="java.util.ArrayList"
%><%@page import="mx.org.fide.modelo.*"
%><%@page import="mx.org.fide.encuestax.*"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat"
%><%@page import="mx.org.fide.reporte.Reporte"
%><%@page import="java.util.LinkedHashMap"
%><%

//El objetivo es crear el html que contenga la sección de preguntas seleccionado por llave_primaria,
// incluyendo las posibles respuestas, y si ya fue contestada presentar la respuesta,
// se debe considerar la presentación del control para auditoria (catálogo)
//response.setContentType("text/xml"); 

    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");

    String error = "";
    Integer claveCuestionario = 0;
    Integer claveCuestionarioParticipante = 0;
    int forma = 270;
    int claveSeccion = 0;
    int pk = 0;
    String tipoAccion = "";
    String w = "";
    String source = "";
    Usuario user = null;
    
    CuestionarioParticipante cuestionarioActual;
    Cuestionario cuestionario;
    Seccion seccion = null;
    RespuestaParticipante respuestaParticipante;

    LinkedHashMap campos = new LinkedHashMap();
    Campo campo = null;
    ArrayList<ArrayList> registros = new ArrayList<ArrayList>();

    user = (Usuario) request.getSession().getAttribute("usuario");

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
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
        
        cuestionarioActual = new CuestionarioParticipante(claveCuestionarioParticipante,user.getCx());
        
        /* Verifica si existe la relación entre el participante y el cuestionario y los permisos del perfil del usuario*/
        user.setForma(279, user.getClavePerfil());
        user.getForma(279).setConsulta("select", String.valueOf(pk), w, user.getReglasDeReemplazo(), user.getCx());
        source = user.getForma(279).getConsulta().getSQL();
        campos =user.getForma(279).getConsulta().getCampos();
        registros = user.getForma(279).getConsulta().getRegistros();        
        user.getForma(279).getConsulta().getCampos().get("claveCuestionario").setValor(String.valueOf(cuestionarioActual.getClaveCuestionario()));
        user.getForma(279).getConsulta().getCampos().get("claveParticipante").setValor(String.valueOf(cuestionarioActual.getClaveParticipante()));
        user.getForma(279).getConsulta().getCampos().get("claveEstatus").setValor(String.valueOf(cuestionarioActual.getClaveEstatus()));
        cuestionarioActual = new CuestionarioParticipante(user.getForma(279).getConsulta(),user.getCx());

        /* Es necesario recuperar la clave de la siguiente pregunta,
           sin embargo al momento de darse de alta el cuestionario participante se debe
           insertar la primera pregunta del cuestionario, la pregunta debe recuperar para llevar a la clave de la sección en donde se debe iniciar/reiniciar */
        
        /***** ATENCION - aqui me quedé, hace falta implementar validacion cuando cuestionarioActual.getSiguientePregunta() es nulo ***/
        Pregunta pregunta = new Pregunta(cuestionarioActual.getSiguientePregunta(), user.getCx());
        
        seccion = new Seccion(pregunta.getClaveSeccion(), user.getCx());
        /*
         * user.setForma(forma, user.getClavePerfil());
         * user.getForma(forma).setConsulta(tipoAccion, String.valueOf(pk), w,
         * user.getReglasDeReemplazo(), user.getCx()); source =
         * user.getForma(forma).getConsulta().getSQL(); campos =
         * user.getForma(forma).getConsulta().getCampos(); registros =
         * user.getForma(forma).getConsulta().getRegistros();
         */

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
        
        cuestionarioActual =new CuestionarioParticipante(seccion.getClaveCuestionario(), user.getClave(), user.getCx());
        
        if (cuestionarioActual.getClaveEstatus()==3) 
            throw new Fallo("El cuestionario ya fue contestado anteriormente y el paciente resultó excluido.");
        
        if (cuestionarioActual.getClaveEstatus()==4) 
            throw new Fallo("El cuestionario ya fue contestado anteriormente y el paciente resultó eliminado.");

        if (cuestionarioActual.getClaveEstatus()==5) 
            throw new Fallo("El cuestionario ya fue contestado anteriormente y fue finalizado.");
                   
        cuestionario = new Cuestionario(seccion.getClaveCuestionario(), user.getCx());%>
<configuracion_survey>
    <cuestionario><%=cuestionario.getCuestionario()%></cuestionario>
    <evento><%=cuestionario.getEvento()%></evento>
</configuracion_survey>        
<html>        
<![CDATA[
<form id="surveyform_<%=cuestionario.getClaveCuestionario()%>" class="forma">
<table>
    <tr>
        <td >
            <h1><%=cuestionario.getCuestionario()%></h1>
        </td>    
    </tr>
    <tr>
        <td>
            <h2><%=cuestionario.getDescripcion()%></h2>
        </td>    
    </tr>    
    <tr>
        <td>
            <h3><%=seccion.getSeccion()%></h3>
        </td>    
    </tr><%
    if (seccion.getInstruccion()!=null) {
    %><tr>
        <td>
            <h4 style="font-size: 12.5px"><%=seccion.getInstruccion()%></h4>
        </td>    
    </tr><%
    }
    for (Pregunta pregunta : seccion.getPreguntas(user.getCx())) {%><%
      if (pregunta.getInstruccion()!=null) {%>
    <tr>
        <td><strong><%=pregunta.getInstruccion()%></strong></td>
    </tr><% }
    %><tr>    
        <td style="font-family: arial;font-size: 13px;"><%=pregunta.getPregunta()%></td>
    </tr>    
    <tr>
        <td style="font-family: arial;font-size: 13px;">
            <input type="hidden" tipo_dato="int" id="claveCuestionarioParticipante" name="claveCuestionarioParticipante" value="15">
           << sw w  id="clavePregunta" name="clavePregunta " <%
            switch (pregunta.getClaveTipoPregunta()) {
                case 1:
                    /*
                     * Pregunta abierta
                     */
            %><input id="pregunta_<%=pregunta.getClavePregunta()%>" /><br />;<%=pregunta.getTextoFinal()%><%
                        break;
                    case 2:
                        /*
                         * Pregunta abierta extendida (text area)
                         */
            %><textarea id="pregunta_<%=pregunta.getClavePregunta()%>" /><br />;<%=pregunta.getTextoFinal()%><%
                         break;
                     case 3:
                         /*
                          * Opciones excluyentes con botones de radio
                          */
                         for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                             if (respuesta.getCodigoRespuesta() != null) {
            %><input type="radio" value="<%=respuesta.getCodigoRespuesta()%>" id="pregunta_<%=pregunta.getClavePregunta()%>"  /><%=respuesta.getRespuesta()%><br /><%
                    } else {
            %><input type="radio" value="<%=respuesta.getClaveRespuesta()%>" id="pregunta_<%=pregunta.getClavePregunta()%>"  /><%=respuesta.getRespuesta()%><br /><%
                                }
                            }
                            break;
                        case 4:
                            /*
                             * Opciones excluyentes en lista desplegable
                             */
            %><select id="<%=pregunta.getClavePregunta()%>"><%
                    for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                        if (respuesta.getCodigoRespuesta() != null) {
                %><option  value="<%=respuesta.getCodigoRespuesta()%>" /><%=respuesta.getRespuesta()%><%
                    } else {
                %><option value="<%=respuesta.getClaveRespuesta()%>"  /><%=respuesta.getRespuesta()%><%
                            }
                        }
                %></select><%
                        break;
                    case 5:
                        /*
                         * Opciones múltiples
                         */
                        for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                            if (respuesta.getCodigoRespuesta() != null) {
                %><input type="checkbox" value="<%=respuesta.getCodigoRespuesta()%>" id="pregunta_<%=pregunta.getClavePregunta()%>_<%=respuesta.getClaveRespuesta()%>"  /><%=respuesta.getRespuesta()%><br /><%
                    } else {
            %><input type="checkbox" value="<%=respuesta.getClaveRespuesta()%>" id="pregunta_<%=pregunta.getClavePregunta()%>_<%=respuesta.getClaveRespuesta()%>" /><%=respuesta.getRespuesta()%><br /><%
                                }
                            }
                            break;
                        case 6:
            %><input id="pregunta_<%=pregunta.getClavePregunta()%>" />&nbsp;<%=pregunta.getTextoFinal()%> <%
                                break;
                            case 7:
                                /*
                                 * Editor web con formato
                                 */
                                break;
                            default:

                                break;
                        }
            %><br /></td>
    </tr>
    <% }%>    
</table>
<input type="hidden" id="ta" name="$ta" value="" />
<input type="hidden" id="ca" name="$ca" value=""  />
<input type="hidden" id="cf" name="$cf" value=""  />
<input type="hidden" id="pk" name="$pk" value=""  />
</form> <%
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
}%>
]]></html>
</survey>    