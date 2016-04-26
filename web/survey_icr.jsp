<?xml version="1.0" encoding="UTF-8"?><%@page contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" 
%><%@page import="java.util.ArrayList"
%><%@page import="mx.org.fide.modelo.*"
%><%@page import="mx.org.fide.encuesta.*"
%><%@page import="mx.org.fide.backend.Forma"
%><%@page import="mx.org.fide.utilerias.Utilerias"
%><%@page import="java.text.SimpleDateFormat" 
%><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat"
%><%@page import="java.util.Date"
%><%@page import="mx.org.fide.reporte.Reporte"
%><%@page import="mx.org.fide.utilerias.AeSimpleMD5"
%><%@page import="java.util.LinkedHashMap"
%><%
    response.setContentType("text/xml;charset=ISO-8859-1"); 
    request.setCharacterEncoding("UTF8");

    String error = "";
    Integer claveCuestionario = 0;
    Integer claveCuestionarioParticipante = 0;
    Integer claveSeccion = 0;
    int forma = 270;
    int pk = 0;
    String tipoAccion = "";
    String w = "";
    String source = "";
    Usuario user = null;
    
    Estudio estudio = null;
    EstudioParticipante estudioParticipante =  null;
    CuestionarioParticipante cuestionarioActual = null;
    Cuestionario cuestionario = null;
    Seccion seccion = null;
    RespuestaParticipante respuestaParticipante;
    PreguntaParticipante preguntaParticipante;
    Forma frmRespuestaParticipante = new Forma();
    Forma frmCalificacionAuditoria= new Forma();
    Forma frmPreguntaAuditor = new Forma();
    Forma frmPreguntaParticipante = new Forma();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    StringBuilder resultado = new StringBuilder("");
    String siguienteAccion= "";
    
    user = (Usuario) request.getSession().getAttribute("usuario");
    
    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }

    //Para emitir códigos de autorización es necesario asegurarnos que se guarda el datstamp de la sesión para validar la clave
    user.setSesion(request.getSession().getId());

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
        
        cuestionarioActual = new CuestionarioParticipante(claveCuestionarioParticipante, "update", user.getReglasDeReemplazo(), user.getCx());
        estudioParticipante = new EstudioParticipante(cuestionarioActual.getClaveEstudioParticipante(), "update", user.getReglasDeReemplazo(), user.getCx());
        cuestionario = new Cuestionario(cuestionarioActual.getClaveCuestionario(), user.getCx());
        estudio= new Estudio(cuestionario.getClaveEstudio(),user.getCx());
        
        /*
         * Verifica si existe la relación entre el participante y el
         * cuestionario y los permisos del perfil del usuario
         */
        user.setForma(279, user.getClavePerfil());
        user.getForma(279).setConsulta("select", String.valueOf(pk), w, user.getReglasDeReemplazo(), user.getCx());
        source = user.getForma(279).getConsulta().getSQL();
        /*campos = user.getForma(279).getConsulta().getCampos();*/
        
        if (!user.getForma(279).isSelect() || !user.getForma(279).isUpdate()) {
            throw new Fallo("Acceso denegado.");            
        }
        
        user.getForma(279).getConsulta().getCampos().get("claveCuestionario").setValor(String.valueOf(cuestionarioActual.getClaveCuestionario()));
        user.getForma(279).getConsulta().getCampos().get("claveParticipante").setValor(String.valueOf(cuestionarioActual.getClaveParticipante()));
        user.getForma(279).getConsulta().getCampos().get("claveEstatus").setValor(String.valueOf(cuestionarioActual.getClaveEstatus()));
        //cuestionarioActual = new CuestionarioParticipante(user.getForma(279).getConsulta(), user.getCx());
        
        /*user.setForma(281, user.getClavePerfil());
        user.getForma(281).setConsulta("foreign", "0", "", user.getReglasDeReemplazo(), user.getCx());*/
        
        frmRespuestaParticipante.setClaveForma(277, user.getClavePerfil(), user.getCx());
        frmRespuestaParticipante.setConsulta("update", "0", "", user.getReglasDeReemplazo(), user.getCx());
        frmRespuestaParticipante.getConsulta().setRegistros(user.getCx());                 
                
        frmCalificacionAuditoria.setClaveForma(281, user.getClavePerfil(), user.getCx());
        frmCalificacionAuditoria.setConsulta("foreign", "0", "", user.getReglasDeReemplazo(), user.getCx());
        frmCalificacionAuditoria.getConsulta().setRegistros(user.getCx()); 

        frmPreguntaAuditor.setClaveForma(287, user.getClavePerfil(), user.getCx());
        frmPreguntaAuditor.setConsulta("update", "0", "", user.getReglasDeReemplazo(), user.getCx());
        frmPreguntaAuditor.getConsulta().setRegistros(user.getCx()); 
        
        frmPreguntaParticipante.setClaveForma(292, user.getClavePerfil(), user.getCx());    
        frmPreguntaParticipante.setConsulta("update", "0", "", user.getReglasDeReemplazo(), user.getCx());
        frmPreguntaParticipante.getConsulta().setRegistros(user.getCx()); 
        /*frmCalificacionAuditoria.setSQL(user.getCx(), 281, "foreign", "", "", user.getReglasDeReemplazo());
        source = frmCalificacionAuditoria.getConsulta().getSQL(); 
        camposfrmCalificacionAuditoria= frmCalificacionAuditoria.getConsulta().getCampos();
        registrosfrmCalificacionAuditoria= frmCalificacionAuditoria.getConsulta().getRegistros();*/
        
        Pregunta pregunta = new Pregunta(cuestionarioActual.getSiguientePregunta(), user.getCx());
        seccion = new Seccion(pregunta.getClaveSeccion(), user.getCx());

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
        /*
         * Verifica si existe la relación entre el participante y el
         * cuestionario
         */
        if (cuestionarioActual.getClaveEstatus()==1 && user.getClavePerfil()==3) {
            throw new Fallo("El cuestionario no ha sido inicializado");
        }

        if (cuestionarioActual.getClaveEstatus()==2 && user.getClavePerfil()==3) {
            throw new Fallo("El cuestionario no ha sido contestado");
        }

        if (cuestionarioActual.getClaveEstatus()==3 && user.getClavePerfil()==3) {
            throw new Fallo("El cuestionario no ha sido finalizado");
        }
        
        if (cuestionarioActual.getClaveEstatus()==5 && user.getClavePerfil()==2) {
            throw new Fallo("El cuestionario ya fue contestado anteriormente, el paciente resultó excluido y está en espera de ser revisado por un auditor.");
        }

        if (cuestionarioActual.getClaveEstatus()==7 && user.getClavePerfil()==2) {
            throw new Fallo("El cuestionario ya fue contestado anteriormente y está en espera de ser revisado por un auditor.");
        }
        
        /*if (cuestionarioActual.getClaveEstatus()==8 && user.getClavePerfil()==3) {
            tipoAccion="show-authentication-form";
        }*/
        
        if (cuestionarioActual.getClaveEstatus()==9 && user.getClavePerfil()==3) {
            throw new Fallo("El cuestionario ya fue auditado anteriormente y se realizaron observaciones");
        }
        
        if (cuestionarioActual.getClaveEstatus()==10 && user.getClavePerfil()==3) {
            throw new Fallo("El cuestionario ya fue auditado anteriormente y no se realizaron observaciones");
        }
        /*if (cuestionarioActual.getClaveEstatus()==6 && user.getClavePerfil()==2) {
           tipoAccion="show-authetication-form";
        } */       
        
        %>
<configuracion_survey>
    <estudio><%=estudio.getEstudio()%></estudio>
    <cuestionario><%=cuestionario.getCuestionario()%></cuestionario>
    <evento><%=cuestionario.getEvento()%></evento>
    <seccion_actual><%=seccion.getClaveSeccion()%></seccion_actual>
    <primera_seccion><%=cuestionario.getPrimeraSeccion(user.getCx())%></primera_seccion>
    <ultima_seccion><%=cuestionario.getUltimaSeccion(user.getCx())%></ultima_seccion>
    <estatus><%=cuestionarioActual.getClaveEstatus()%></estatus><%
    if (user.getClavePerfil()==3) {
   %><respuestaCondicional_5_siEstaEsLaRespuesta><%=cuestionarioActual.hayAlgunaRespuestaParticipanteConSiEstaEsLaRespuestaIgualA5enEstaSeccion(seccion.getClaveSeccion(),user.getCx())%></respuestaCondicional_5_siEstaEsLaRespuesta><% } %>
</configuracion_survey><%

if (tipoAccion.equals("open")) {%>       
<html>        
    <![CDATA[
    <form id="surveyform_<%=cuestionarioActual.getClaveCuestionarioParticipante()%>" class="forma">
        <table>
            <tr><td><h1><%=estudio.getEstudio()%> / <%=cuestionario.getCuestionario()%></h1></td></tr>
            <tr><td><h2><%=cuestionario.getDescripcion()%></h2></td></tr>
            <tr><td><h3><%=seccion.getSeccion()%></h3></td>    
            </tr><%if (seccion.getInstruccion() != null) {
            %><tr><td><h4 style="font-size: 12.5px"><%=seccion.getInstruccion()%></h4></td></tr><%
                }
                for (Pregunta pregunta : seccion.getPreguntas(user.getCx())) {%><%
                    if (pregunta.getInstruccion() != null) {%>
            <tr><td><strong><%=pregunta.getInstruccion()%></strong></td></tr><% }
            %><tr class="trPregunta"><% preguntaParticipante = new PreguntaParticipante (claveCuestionarioParticipante, pregunta.getClavePregunta(),user.getCx()); %>   
                <td style="font-family: arial;font-size: 13px;" >
                    <div id="pregunta_<%=pregunta.getClavePregunta()%>"><%=pregunta.getPregunta()%><% if (pregunta.getObligatoria()) { %>&nbsp;&nbsp;( <span class="mensajeobligatorio">Obligatoria</span>*)<% } %>
                <div class="pendingQuestion" tipo_accion="update" <% if (preguntaParticipante.getClaveEstatusPregunta()==1) {%>tipo="pause"<% } else { %>tipo="play"<% } %> forma="292" control="preguntaParticipante_<%=preguntaParticipante.getClavePreguntaParticipante()%>" /></td> 
                <td><%
                    if (frmPreguntaAuditor.getConsulta().getCampos().get("claveCalificacionAuditoria").getVisible()==1) {
                    //Se coloca en el id del control la llave primaria que le corresponde
                    preguntaAuditor = new PreguntaAuditor (claveCuestionarioParticipante, pregunta.getClavePregunta(),user.getCx());
                %><select tipo_dato="int" style="font-family: arial;"  id="claveCalificacionAuditoria_<%=preguntaAuditor.getClavePreguntaAuditor()%>" name="claveCalificacionAuditoria_<%=preguntaAuditor.getClavePreguntaAuditor()%>" <%
                      if (frmPreguntaAuditor.getConsulta().getCampos().get("claveCalificacionAuditoria").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2 ) {%> disabled="disabled"<% } else { %> class="obligatorio"<% }%>><option value=""></option><%
                        for (int m = 0; m < frmCalificacionAuditoria.getConsulta().getRegistros().size(); m++) {
                   %><option value="<%=frmCalificacionAuditoria.getConsulta().getRegistros().get(m).get(0)%>" <% if (preguntaAuditor.getClaveCalificacionAuditoria()==frmCalificacionAuditoria.getConsulta().getRegistros().get(m).get(0)) {%>selected="selected"<%}%>><%=frmCalificacionAuditoria.getConsulta().getRegistros().get(m).get(1)%></option><%
                        }
                }
                %>
                </select></td>
            </tr>    
            <tr>
                <td style="font-family: arial;font-size: 13px;" id="tdPreguntaParticipante_<%=preguntaParticipante.getClavePreguntaParticipante()%>"><%
                        switch (pregunta.getClaveTipoPregunta()) { 
                            case 1:
                                /* Pregunta abierta  */
                            try {
                                respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx()); 
                            %><input type="text"  id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" value="<% if (respuestaParticipante.getRespuesta()!=null) { %><%=respuestaParticipante.getRespuesta()%><% } %>" class="singleInput <%if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1 ) { %>obligatorio<% } 
                                if (pregunta.getClaveTipoDatoRespuesta()==3) {%><%=" fecha\" onBlur='javascript:check_date(this)'"%><%} else { %>"<% } 
                                if (pregunta.getClaveTipoDatoRespuesta()==2) {%><%=" onBlur='javascript:check_number(this);' "%><%} 
                                if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2 ) {%> disabled="disabled" <% }
                                } catch (Exception e) {
                                    
                                }
                            %>/>
                            <br /><% if (pregunta.getTextoFinal()!=null) { %><%=pregunta.getTextoFinal()%><% }
                            break;
                        case 2:
                            /* Pregunta abierta extendida (text area) */
                            try {
                                respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx());
                                %><textarea type="text" class="singleInput" id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" <%if (pregunta.getObligatoria()) {%> class="obligatorio" <% } if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2) {%> readonly<% }%>><% if (respuestaParticipante.getRespuesta()!=null) {%><%=respuestaParticipante.getRespuesta()%><% }                                
                                } catch (Exception e) {
                                }
                            %></textarea><br /><% if (pregunta.getTextoFinal()!=null){%><%=pregunta.getTextoFinal()%><% }
                                break;
                        case 3:
                            /* Opciones excluyentes con botones de radio*/
                            ArrayList <Integer> respuestasTipoOtro = pregunta.respuestasTipoOtro(user.getCx());
                            respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx());
                            StringBuilder condicionOtro = new StringBuilder();
                            for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                             try {
                                %><input tipo_dato="int" type="radio" <%
                                if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1) {%> class="obligatorio"<% }
                                 %> id="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>"<%
                                
                                if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2) {%> disabled="disabled"<% }
                                 %> value="<%
                                if (respuesta.getCodigoRespuesta() != null) {%><%=respuesta.getCodigoRespuesta()%><% } 
                                else {%><%=respuesta.getClaveRespuesta()%>" <% }
                                
                                if (respuesta.getClaveRespuesta().intValue()==respuestaParticipante.getClaveRespuesta().intValue()) {
                                    %> checked="checked" <% }
                                
                                if (respuesta.getOtro()) {
                                %> onclick="javascript:document.getElementById('respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>').disabled=(this.value==<%=respuesta.getClaveRespuesta()%>?false:true)" <%
                                } else {
                                    //Es necesario saber si hay respuesta de tipo otro para deshabilitar
                                    if (respuestasTipoOtro.size()>0) {
                                        for (Integer l=0; l<respuestasTipoOtro.size(); l++) {
                                            condicionOtro.append("this.value!=").append(respuestasTipoOtro.get(l));
                                            if (l<respuestasTipoOtro.size()-1) {
                                                condicionOtro.append(" && ");
                                            }
                                        }
                                        %> onclick="javascript:document.getElementById('respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>').disabled=((<%=condicionOtro.toString()%>)?true:false)" <% 
                                    }
                                }

                            %> /><%=respuesta.getRespuesta()%><% 
                                if (respuesta.getOtro()){
                                %>&nbsp;<input type="text" style="float: none; width: 40%;" id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" value="<% if (respuestaParticipante.getRespuesta()!=null) { %><%=respuestaParticipante.getRespuesta()%><% } %>" class="singleInput<%   
                                        if (pregunta.getClaveTipoDatoRespuesta()==3) {%><%=" fecha\" onBlur='javascript:check_date(this)'"%><%} else { %>"<% } 
                                        if (pregunta.getClaveTipoDatoRespuesta()==2) {%><%=" onBlur='javascript:check_number(this)' "%><%} 
                                        if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2) {%> disabled="disabled" <% }
                                        if (respuesta.getClaveRespuesta()!=respuestaParticipante.getClaveRespuesta()) {%> disabled="disabled" <% } %>><br /><%
                                } else {
                                        %><br /><% 
                                } 
                                
                            } catch (Exception e) {
                            }
                           }  
 
                            break;
                        case 4:
                            /* Opciones excluyentes en lista desplegable*/
                            respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null,null, user.getCx());
                            %><select tipo_dato="int" tabindex="2" <%
                            if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1) {%> class="obligatorio"<% } %> id="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" <% if (preguntaParticipante.getClaveEstatusPregunta()==2) {%>disabled="disabled"<%}%>  ><%
                                    for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                                %><option value="<% 
                                        if (respuesta.getCodigoRespuesta() != null) {
                                        %><%=respuesta.getCodigoRespuesta()%><%} 
                                        else { 
                                        %><%=respuesta.getRespuesta()%><%
                                }%>"<%
                                        try {
                                            respuestaParticipante = new RespuestaParticipante (cuestionarioActual.getClaveCuestionarioParticipante(), "claveRespuesta",respuesta.getClaveRespuesta(),null,null, user.getCx());
                                            if (respuesta.getClaveRespuesta()==respuestaParticipante.getClaveRespuesta()) {
                                        %>selected="selected"<% } 
                                        } catch (Exception e) {

                                        }
                                        %>><%=respuesta.getRespuesta()%></option><%
                               }
                             %></select><%
                                break;
                            case 5:
                                /* Opciones múltiples*/
                                for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                                    try {
                                        respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "claveRespuesta", respuesta.getClaveRespuesta(), null, null, user.getCx());
                                %><input type="checkbox" tipo_dato="int" id="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" <%if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1) {%> class="obligatorio pregunta<%=respuesta.getClavePregunta()%> "<% } %> value="<% 
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
                                
                                 if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2) {%> disabled="disabled" <% }      
                                } catch (Exception e) {
                                    error= e.getMessage();
                                }
                                %>><%=respuesta.getRespuesta()%><br/><%
                                }
                                    break;
                             case 6:
                                /* Fill in the blanks*/
                                try {
                                    respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx());
                                %><input type="text" class="singleInput" id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" value="<%=respuestaParticipante.getRespuesta()%>" <%if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1) {%> class="obligatorio"<% } %> />
                                    <%=pregunta.getTextoFinal()%><%
                                } catch (Exception e) {
                                }
                                %>/><%
                                break;
                             case 7:
                                /* Pregunta abierta extendida (text area)*/
                                try {
                                respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx());
                                %><textarea type="text" class="singleInput" id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" <%if (pregunta.getObligatoria()) {%> class="obligatorio"<% } %> ><%=respuestaParticipante.getRespuesta()%></textarea><%
                                } catch (Exception e) {
                                }
                                %>></textarea><br />;<%=pregunta.getTextoFinal()%><%
                                        break;
                             default:
                                break;
                       }
                    %><br></td>
            </tr>

        <%}%>    
        </table>
        <input type="hidden" tipo_dato="int" name="$ccp" value="<%=cuestionarioActual.getClaveCuestionarioParticipante()%>">
        <input tipo_dato="datetime" id="fecha_realimentacion" name="fecha_realimentacion" type="hidden" value="%now">
        <input tipo_dato="int" id="clave_empleado_auditor" name="clave_empleado_auditor"  type="hidden" value="<%=user.getClave()%>" />
        <input type="hidden" name="$cs" value="<%=seccion.getClaveSeccion()%>">
        <input type="hidden" name="$cf" value="277">
        <input type="hidden" name="$pk" value="125">
        <input type="hidden" id="$hacia" name="$hacia" value="">
    </form> 
    ]]></html><% }
     else if (tipoAccion.equals("open-pending-questions")) { %>       
<html>        
    <![CDATA[
    <form id="surveyform_<%=cuestionarioActual.getClaveCuestionarioParticipante()%>" class="forma">
        <table>
            <tr><td><h1><%=estudio.getEstudio()%> / <%=cuestionario.getCuestionario()%></h1></td></tr>
            <tr><td><h2><%=cuestionario.getDescripcion()%></h2></td></tr>
            <% Integer secciones=0;
            for (Seccion seccionT : cuestionarioActual.getSeccionesConPreguntasPendientes(user.getCx())) { %>
            <tr><td><h3><%=seccionT.getSeccion()%></h3></td>    
            </tr><%if (seccionT.getInstruccion() != null) {
            %><tr><td><h4 style="font-size: 12.5px"><%=seccionT.getInstruccion()%></h4></td></tr><%
                }
                for (Pregunta pregunta : seccionT.getPreguntasPendientes(user.getCx())) { %><%
                    if (pregunta.getInstruccion() != null) {%>
            <tr><td><strong><%=pregunta.getInstruccion()%></strong></td></tr><% }
            %><tr class="trPregunta"><% preguntaParticipante = new PreguntaParticipante (claveCuestionarioParticipante, pregunta.getClavePregunta(),user.getCx()); %>   
                <td style="font-family: arial;font-size: 13px;" >
                    <div id="pregunta_<%=pregunta.getClavePregunta()%>"><%=pregunta.getPregunta()%><% if (pregunta.getObligatoria()) { %>&nbsp;&nbsp;( <span class="mensajeobligatorio">Obligatoria</span>*)<% } %>
                <div class="pendingQuestion" tipo_accion="update" <% if (preguntaParticipante.getClaveEstatusPregunta()==1) {%>tipo="pause"<% } else { %>tipo="play"<% } %> forma="292" control="preguntaParticipante_<%=preguntaParticipante.getClavePreguntaParticipante()%>" /></td> 
                <td><%
                    if (frmPreguntaAuditor.getConsulta().getCampos().get("claveCalificacionAuditoria").getVisible()==1) {
                    //Se coloca en el id del control la llave primaria que le corresponde
                    preguntaAuditor = new PreguntaAuditor (claveCuestionarioParticipante, pregunta.getClavePregunta(),user.getCx());
                %><select tipo_dato="int" style="font-family: arial;"  id="claveCalificacionAuditoria_<%=preguntaAuditor.getClavePreguntaAuditor()%>" name="claveCalificacionAuditoria_<%=preguntaAuditor.getClavePreguntaAuditor()%>" <%
                      if (frmPreguntaAuditor.getConsulta().getCampos().get("claveCalificacionAuditoria").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2 ) {%> disabled="disabled"<% } else { %> class="obligatorio"<% }%>><option value=""></option><%
                        for (int m = 0; m < frmCalificacionAuditoria.getConsulta().getRegistros().size(); m++) {
                   %><option value="<%=frmCalificacionAuditoria.getConsulta().getRegistros().get(m).get(0)%>" <% if (preguntaAuditor.getClaveCalificacionAuditoria()==frmCalificacionAuditoria.getConsulta().getRegistros().get(m).get(0)) {%>selected="selected"<%}%>><%=frmCalificacionAuditoria.getConsulta().getRegistros().get(m).get(1)%></option><%
                        }
                }
                %>
                </select></td>
            </tr>    
            <tr>
                <td style="font-family: arial;font-size: 13px;" id="tdPreguntaParticipante_<%=preguntaParticipante.getClavePreguntaParticipante()%>"><%
                        switch (pregunta.getClaveTipoPregunta()) { 
                            case 1:
                                /* Pregunta abierta  */
                            try {
                                respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx()); 
                            %><input type="text"  id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" value="<% if (respuestaParticipante.getRespuesta()!=null) { %><%=respuestaParticipante.getRespuesta()%><% } %>" class="singleInput <%if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1 ) { %>obligatorio<% } 
                                if (pregunta.getClaveTipoDatoRespuesta()==3) {%><%=" fecha\" onBlur='javascript:check_date(this)'"%><%} else { %>"<% } 
                                if (pregunta.getClaveTipoDatoRespuesta()==2) {%><%=" onBlur='javascript:check_number(this);' "%><%} 
                                if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2 ) {%> disabled="disabled" <% }
                                } catch (Exception e) {
                                    
                                }
                            %>/>
                            <br /><% if (pregunta.getTextoFinal()!=null) { %><%=pregunta.getTextoFinal()%><% }
                            break;
                        case 2:
                            /* Pregunta abierta extendida (text area) */
                            try {
                                respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx());
                                %><textarea type="text" class="singleInput" id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" <%if (pregunta.getObligatoria()) {%> class="obligatorio" <% } if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2) {%> readonly<% }%>><% if (respuestaParticipante.getRespuesta()!=null) {%><%=respuestaParticipante.getRespuesta()%><% }                                
                                } catch (Exception e) {
                                }
                            %></textarea><br /><% if (pregunta.getTextoFinal()!=null){%><%=pregunta.getTextoFinal()%><% }
                                break;
                        case 3:
                            /* Opciones excluyentes con botones de radio*/
                            ArrayList <Integer> respuestasTipoOtro = pregunta.respuestasTipoOtro(user.getCx());
                            respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx());
                            StringBuilder condicionOtro = new StringBuilder();
                            for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                             try {
                                %><input tipo_dato="int" type="radio" <%
                                if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1) {%> class="obligatorio"<% }
                                 %> id="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>"<%
                                
                                if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2) {%> disabled="disabled"<% }
                                 %> value="<%
                                if (respuesta.getCodigoRespuesta() != null) {%><%=respuesta.getCodigoRespuesta()%><% } 
                                else {%><%=respuesta.getClaveRespuesta()%>" <% }
                                
                                if (respuesta.getClaveRespuesta().intValue()==respuestaParticipante.getClaveRespuesta().intValue()) {
                                    %> checked="checked" <% }
                                
                                if (respuesta.getOtro()) {
                                %> onclick="javascript:document.getElementById('respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>').disabled=(this.value==<%=respuesta.getClaveRespuesta()%>?false:true)" <%
                                } else {
                                    //Es necesario saber si hay respuesta de tipo otro para deshabilitar
                                    if (respuestasTipoOtro.size()>0) {
                                        for (Integer l=0; l<respuestasTipoOtro.size(); l++) {
                                            condicionOtro.append("this.value!=").append(respuestasTipoOtro.get(l));
                                            if (l<respuestasTipoOtro.size()-1) {
                                                condicionOtro.append(" && ");
                                            }
                                        }
                                        %> onclick="javascript:document.getElementById('respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>').disabled=((<%=condicionOtro.toString()%>)?true:false)" <% 
                                    }
                                }

                            %> /><%=respuesta.getRespuesta()%><% 
                                if (respuesta.getOtro()){
                                %>&nbsp;<input type="text" style="float: none; width: 40%;" id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" value="<% if (respuestaParticipante.getRespuesta()!=null) { %><%=respuestaParticipante.getRespuesta()%><% } %>" class="singleInput<%   
                                        if (pregunta.getClaveTipoDatoRespuesta()==3) {%><%=" fecha\" onBlur='javascript:check_date(this)'"%><%} else { %>"<% } 
                                        if (pregunta.getClaveTipoDatoRespuesta()==2) {%><%=" onBlur='javascript:check_number(this)' "%><%} 
                                        if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2) {%> disabled="disabled" <% }
                                        if (respuesta.getClaveRespuesta()!=respuestaParticipante.getClaveRespuesta()) {%> disabled="disabled" <% } %>><br /><%
                                } else {
                                        %><br /><% 
                                } 
                                
                            } catch (Exception e) {
                            }
                           }  
 
                            break;
                        case 4:
                            /* Opciones excluyentes en lista desplegable*/
                            respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null,null, user.getCx());
                            %><select tipo_dato="int" tabindex="2" <%
                            if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1) {%> class="obligatorio"<% } %> id="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" <% if (preguntaParticipante.getClaveEstatusPregunta()==2) {%>disabled="disabled"<%}%>  ><%
                                    for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                                %><option value="<% 
                                        if (respuesta.getCodigoRespuesta() != null) {
                                        %><%=respuesta.getCodigoRespuesta()%><%} 
                                        else { 
                                        %><%=respuesta.getRespuesta()%><%
                                }%>"<%
                                        try {
                                            respuestaParticipante = new RespuestaParticipante (cuestionarioActual.getClaveCuestionarioParticipante(), "claveRespuesta",respuesta.getClaveRespuesta(),null,null, user.getCx());
                                            if (respuesta.getClaveRespuesta()==respuestaParticipante.getClaveRespuesta()) {
                                        %>selected="selected"<% } 
                                        } catch (Exception e) {

                                        }
                                        %>><%=respuesta.getRespuesta()%></option><%
                               }
                             %></select><%
                                break;
                            case 5:
                                /* Opciones múltiples*/
                                for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                                    try {
                                        respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "claveRespuesta", respuesta.getClaveRespuesta(), null, null, user.getCx());
                                %><input type="checkbox" tipo_dato="int" id="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="claveRespuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" <%if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1) {%> class="obligatorio pregunta<%=respuesta.getClavePregunta()%> "<% } %> value="<% 
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
                                
                                 if (frmRespuestaParticipante.getConsulta().getCampos().get("claveRespuesta").getActivo()==0 || preguntaParticipante.getClaveEstatusPregunta()==2) {%> disabled="disabled" <% }      
                                } catch (Exception e) {
                                    error= e.getMessage();
                                }
                                %>><%=respuesta.getRespuesta()%><br/><%
                                }
                                break;
                             case 6:
                                /* Fill in the blanks*/
                                try {
                                    respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx());
                                %><input type="text" class="singleInput" id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" value="<%=respuestaParticipante.getRespuesta()%>" <%if (pregunta.getObligatoria() && preguntaParticipante.getClaveEstatusPregunta()==1) {%> class="obligatorio"<% } %> />
                                    <%=pregunta.getTextoFinal()%><%
                                } catch (Exception e) {
                                }
                                %>/><%
                                break;
                             case 7:
                                /* Pregunta abierta extendida (text area)*/
                                try {
                                respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), null, null, user.getCx());
                                %><textarea type="text" class="singleInput" id="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" name="respuesta_<%=respuestaParticipante.getClaveRespuestaParticipante()%>" <%if (pregunta.getObligatoria()) {%> class="obligatorio"<% } %> ><%=respuestaParticipante.getRespuesta()%></textarea><%
                                } catch (Exception e) {
                                }
                                %>></textarea><br />;<%=pregunta.getTextoFinal()%><%
                                        break;
                             default:
                                break;
                       }
                    %><br></td>
            </tr>

        <% }
        secciones++;
        }
           if (secciones==0) {%>
           <tr id="tr_empty_survey"><td>No hay preguntas marcadas como pendientes. </td></tr><%
           }
               %>    
        </table>
        <input type="hidden" tipo_dato="int" name="$ccp" value="<%=cuestionarioActual.getClaveCuestionarioParticipante()%>">
        <input tipo_dato="datetime" id="fecha_realimentacion" name="fecha_realimentacion" type="hidden" value="%now">
        <input tipo_dato="int" id="clave_empleado_auditor" name="clave_empleado_auditor"  type="hidden" value="<%=user.getClave()%>" />
        <input type="hidden" name="$cs" value="<%=seccion.getClaveSeccion()%>">
        <input type="hidden" name="$cf" value="277">
        <input type="hidden" name="$pk" value="125">
        <input type="hidden" id="$hacia" name="$hacia" value="">
    </form> 
    ]]></html><%
         
     }
     else if (tipoAccion.equals("save"))  {
         
          if (request.getParameter("$ccp") == null) {
            throw new Fallo("Falta parámetro $ccp");
        } else {
            try {
                claveSeccion = Integer.parseInt(request.getParameter("$cs"));
                
            } catch (Exception e) {
                throw new Fallo("El parámetro $cs no es válido, verifique");
            }
            
            Integer claveRespuestaParticipante=0;
            seccion = new Seccion(claveSeccion, user.getCx());
            for (Pregunta pregunta : seccion.getPreguntas(user.getCx())) {
                
                if (cuestionarioActual.getClaveEstatus()>4 && user.getClavePerfil()==2)
                    break;
                
                preguntaAuditor = new PreguntaAuditor (claveCuestionarioParticipante, pregunta.getClavePregunta(),user.getCx());
                
                if (request.getParameter("claveCalificacionAuditoria_".concat(preguntaAuditor.getClavePreguntaAuditor().toString()))!=null) {
                    if (!request.getParameter("claveCalificacionAuditoria_".concat(preguntaAuditor.getClavePreguntaAuditor().toString())).equals("")) {
                        frmPreguntaAuditor.getConsulta().setPk(preguntaAuditor.getClavePreguntaAuditor().toString());
                        frmPreguntaAuditor.getConsulta().getCampos().get("claveCalificacionAuditoria").setValor(request.getParameter("claveCalificacionAuditoria_".concat(preguntaAuditor.getClavePreguntaAuditor().toString())));
                        frmPreguntaAuditor.getConsulta().getCampos().get("fechaAuditoria").setValor(formatter.format(new Date()));
                        frmPreguntaAuditor.getConsulta().getCampos().get("clave_empleado_auditor").setValor(user.getClave().toString());
                        frmPreguntaAuditor.getConsulta().update(user.getClave(), user.getIp(), user.getNavegador(), 287, user.getCx(), true);
                        siguienteAccion="continua";
                    }    
                }
                
                switch (pregunta.getClaveTipoPregunta()) {
                    case 1: case 2: case 6: case 7:
                        /* Pregunta abierta, abierta extendida, fill in the blanks, editor web */
                        respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), "update",user.getReglasDeReemplazo(), user.getCx());
                        claveRespuestaParticipante= respuestaParticipante.getClaveRespuestaParticipante();
                        
                        if (request.getParameter("respuesta_".concat(claveRespuestaParticipante.toString()))==null)
                            continue;
                        
                        if (request.getParameter("respuesta_".concat(claveRespuestaParticipante.toString())).equals(""))
                            continue;
                        
                        respuestaParticipante.setRespuesta(Utilerias.decodeURIComponentXX(request.getParameter("respuesta_".concat(claveRespuestaParticipante.toString()))));
                        respuestaParticipante.getCampos().get("respuesta").setValor(Utilerias.decodeURIComponentXX(respuestaParticipante.getRespuesta()));
                        respuestaParticipante.getCampos().get("fecha").setValor( formatter.format(new Date()) );
                        respuestaParticipante.getCampos().get("clave_empleado").setValor(String.valueOf(user.getClave())); 
                        resultado.append(respuestaParticipante.update(user.getClave(), user.getIp(), user.getNavegador(),277,  user.getCx(), true));
                        if (resultado.toString().contains("<error>"))
                            throw new Fallo("Error al guardar respuesta ".concat(claveRespuestaParticipante.toString()).concat(" error: ").concat(resultado.toString().substring(resultado.toString().indexOf("<error><![CDATA[")).replace("<error><![CDATA[", "").replace("']]></error>", "")));
                        
                        break;
                    case 3: case 4:
                        /* Opciones excluyentes con botones de radio y opciones excluyentes en lista desplegable  */
                        StringBuilder siEstaEsLaRespuesta= new StringBuilder("");
                        Integer siguientePregunta;
                        
                        respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "clavePregunta", pregunta.getClavePregunta(), "update", user.getReglasDeReemplazo(), user.getCx());
                        claveRespuestaParticipante= respuestaParticipante.getClaveRespuestaParticipante();

                        if (request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString()))==null)
                            continue;
                        
                        if (request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString())).equals(""))
                            continue;
                                                                      
                        respuestaParticipante.setClaveRespuesta(Integer.parseInt(request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString()))));
                        Respuesta respuestaSeleccionada = new Respuesta(respuestaParticipante.getClaveRespuesta(),user.getCx());
                        
                        //Se requiere llenar los campos del objeto consulta.campo
                        if (respuestaSeleccionada.getOtro()) {
                            if (request.getParameter("respuesta_".concat(respuestaParticipante.getClaveRespuestaParticipante().toString()))!=null && request.getParameter("respuesta_".concat(respuestaParticipante.getClaveRespuestaParticipante().toString()))!="") {
                                respuestaParticipante.getCampos().get("respuesta").setValor(Utilerias.decodeURIComponentXX(request.getParameter("respuesta_".concat(respuestaParticipante.getClaveRespuestaParticipante().toString()))));
                            }
                        }
                        respuestaParticipante.getCampos().get("claveRespuesta").setValor(respuestaParticipante.getClaveRespuesta().toString());
                        respuestaParticipante.getCampos().get("fecha").setValor( formatter.format(new Date()) );
                        respuestaParticipante.getCampos().get("clave_empleado").setValor(String.valueOf(user.getClave())); 
                        resultado.append(respuestaParticipante.update(user.getClave(), user.getIp(), user.getNavegador(),277,  user.getCx(), true));
                        if (resultado.toString().contains("<error>")) {
                            throw new Fallo("Error al guardar respuesta ".concat(claveRespuestaParticipante.toString()).concat(" ").concat(resultado.toString().substring(resultado.toString().indexOf("<error><![CDATA[")).replace("<error><![CDATA[", "").replace("']]></error>", "")));
                        } else {
                            //El flujo de información depende en primer lugar de la respuesta seleccionada
                            //la siguiente acción se determina colocando el valor de CuestionarioParticipante.siguientePregunta
                            switch (respuestaSeleccionada.getSiEstaEsLaRespuesta()){
                            case 0: //No realizar operación especial
                                //Verifica si es la última pregunta del cuestionario
                                if (siguienteAccion.equals("")) {
                                    cuestionarioActual.setClaveEstatus(3);
                                    cuestionarioActual.getCampos().get("claveEstatus").setValor("3");
                                    siguienteAccion="continua";
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
                                if (cuestionarioActual.getClaveEstatus()!=4) {
                                    estudioParticipante.setClaveEstatus(4);
                                    estudioParticipante.getCampos().get("claveEstatus").setValor("4");
                                    estudioParticipante.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx());
                                    cuestionarioActual.setClaveEstatus(4);
                                    cuestionarioActual.getCampos().get("claveEstatus").setValor("4");
                                    cuestionarioActual.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx(), request.getSession().getId());
                                }    
                                break;
                            }
                        }
                        
                        break;
                    case 5:
                        /* Opciones múltiples   */
                        for (Respuesta respuesta : pregunta.getRespuestas(user.getCx())) {
                            respuestaParticipante = new RespuestaParticipante(cuestionarioActual.getClaveCuestionarioParticipante(), "claveRespuesta", respuesta.getClaveRespuesta(), "update", user.getReglasDeReemplazo(), user.getCx());
                            claveRespuestaParticipante= respuestaParticipante.getClaveRespuestaParticipante();
                            
                            if (request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString()))==null)
                            continue;
                        
                            if (request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString())).equals(""))
                            continue;
                            
                            respuestaParticipante.setClaveRespuesta(Integer.parseInt(request.getParameter("claveRespuesta_".concat(claveRespuestaParticipante.toString()))));
                            respuestaParticipante.getCampos().get("respuesta").setValor(respuestaParticipante.getClaveRespuesta().toString());
                            respuestaParticipante.getCampos().get("fecha").setValor( formatter.format(new Date()) );
                            respuestaParticipante.getCampos().get("clave_empleado").setValor(String.valueOf(user.getClave()));                             
                            resultado.append(respuestaParticipante.update(user.getClave(), user.getIp(), user.getNavegador(),277,  user.getCx(), true));
                            if (resultado.toString().contains("<error>"))
                                throw new Fallo("Error al guardar respuesta ".concat(claveRespuestaParticipante.toString()).concat(" ").concat(resultado.toString().substring(resultado.toString().indexOf("<error><![CDATA[")).replace("<error><![CDATA[", "").replace("']]></error>", "")));
                            
                        }                       
                        break;   
                }
            }
            
            //Caso en el que se excluyó el estudio
            if (user.getClavePerfil()==2 && cuestionarioActual.getClaveEstatus()==4 ) {
                siguienteAccion="Solicita autenticacion";
            } else if (user.getClavePerfil()==2 && cuestionarioActual.getClaveEstatus()==5 ) {
                siguienteAccion="Excluye estudio";
            } else if (user.getClavePerfil()==3  && (cuestionarioActual.getClaveEstatus()==5 || cuestionarioActual.getClaveEstatus()==8)  && cuestionarioActual.hayAlgunaRespuestaParticipanteConSiEstaEsLaRespuestaIgualA5enEstaSeccion(seccion.getClaveSeccion(),user.getCx())){
                   cuestionarioActual.setClaveEstatus(8);
                   cuestionarioActual.getCampos().get("claveEstatus").setValor("8");
                   cuestionarioActual.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx(), request.getSession().getId());
                   //siguienteAccion="abre formulario ".concat(estudio.getClaveCuestionarioFinalizacion().toString());
                   siguienteAccion="Solicita autenticacion";
            } else if (request.getParameter("$hacia").equals("atras")) {
                Integer siguientePregunta = new Seccion(seccion.getAnterior(user.getCx()),user.getCx()).getPrimeraPregunta(user.getCx());
                cuestionarioActual.getCampos().get("siguientePregunta").setValor(siguientePregunta.toString());
                cuestionarioActual.setSiguientePregunta(siguientePregunta);    
                cuestionarioActual.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx(), true);
                
                if (siguienteAccion.equals(""))
                        siguienteAccion="continua";
            } else {
                //¿Es la última seccion?
                if (seccion.getClaveSeccion()==cuestionario.getUltimaSeccion(user.getCx())) {
                    //Cierra cuestionario y emite alertas
                    
                    if (user.getClavePerfil()==1 || user.getClavePerfil()==2) {
                        //Pone la siguiente pregunta en el inicio del cuestionario para la revisión del auditor
                        cuestionarioActual.getCampos().get("siguientePregunta").setValor(cuestionario.getPrimeraPregunta(user.getCx()).toString() );
                        cuestionarioActual.setClaveEstatus(6);
                        cuestionarioActual.getCampos().get("claveEstatus").setValor("6");
                    }
                    if (user.getClavePerfil()==3) {
                        cuestionarioActual.setClaveEstatus(8);
                        cuestionarioActual.getCampos().get("claveEstatus").setValor("8");
                        //Es necesario validar si hubo alguna anotación por parte del auditor para establecer el estatus 7=Por corrección del investigador
                        /*if (cuestionarioActual.hayObservacionesEnAuditoria(user.getCx())) {
                            cuestionarioActual.setClaveEstatus(8);
                            cuestionarioActual.getCampos().get("claveEstatus").setValor("8");
                        } else {
                            cuestionarioActual.setClaveEstatus(9);
                            cuestionarioActual.getCampos().get("claveEstatus").setValor("9");
                        }*/
                    }
                    cuestionarioActual.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx(), request.getSession().getId());
                    //siguienteAccion="abre formulario ".concat(estudio.getClaveCuestionarioFinalizacion().toString());
                    siguienteAccion="Solicita autenticacion";
                    //siguienteAccion="Finaliza cuestionario";
                    
                }else {   
                    
                    if (user.getClavePerfil()==2 && cuestionarioActual.getClaveEstatus()==2 ) {
                        cuestionarioActual.getCampos().get("claveEstatus").setValor("3");
                    }
   
                    cuestionarioActual.getCampos().get("siguientePregunta").setValor(new Seccion(seccion.getSiguiente(user.getCx()),user.getCx()).getPrimeraPregunta(user.getCx()).toString());    
                    cuestionarioActual.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx(), true);
                    
                    if (siguienteAccion.equals("")) {
                        siguienteAccion="continua";
                    }
               }   
            }                      
        }
    } else if (tipoAccion.equals("show-authentication-form"))  { 
%><html>        
    <![CDATA[       
    <form id="surveyautheticationform_<%=cuestionarioActual.getClaveCuestionarioParticipante()%>" class="forma" action="?$ccp=<%=claveCuestionarioParticipante%>&$ta=valid-authentication">
        <table>
            <tr><td>Se ha enviado un código de autorización a tu correo registrado para finalizar este formulario, por favor copia e ingresa el código de autorización (<span class="mensajeobligatorio">Obligatorio</span>*)</td></tr>
            <tr><td><input id="auth_code" class="singleInput obligatorio" /></td></tr>
        </table>
    </form>
   ]]> 
</html><%
    } else if (tipoAccion.equals("valid-authentication")) {
        if (cuestionarioActual.getClaveEstatus()==4 && user.getClavePerfil()==2) {
            if (request.getParameter("auth_code").equals(AeSimpleMD5.MD5(request.getSession().getId()))) {
                cuestionarioActual.getCampos().get("siguientePregunta").setValor(cuestionario.getPrimeraPregunta(user.getCx()).toString() );
                cuestionarioActual.setClaveEstatus(5);
                cuestionarioActual.getCampos().get("claveEstatus").setValor("5");
                cuestionarioActual.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx(), request.getSession().getId()); 
                estudioParticipante.getCampos().get("claveEstatus").setValor("5");
                estudioParticipante.getCampos().get("claveCuestionarioActual").setValor(estudio.getClaveCuestionarioFinalizacion().toString());
                estudioParticipante.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx());
                siguienteAccion="Excluye estudio";
                %><alert><![CDATA[El cuestionario ha sido excluido exitosamente y se ha enviado a un auditor para su revisión, gracias por su colaboración]]></alert><%
            } else {
               %><alert><![CDATA[Código de autenticación no válido]]></alert><%                
            }
            
        } else if (cuestionarioActual.getClaveEstatus()==6 && user.getClavePerfil()==2) {
            if (request.getParameter("auth_code").equals(AeSimpleMD5.MD5(request.getSession().getId()))) {
                cuestionarioActual.getCampos().get("siguientePregunta").setValor(cuestionario.getPrimeraPregunta(user.getCx()).toString() );
                cuestionarioActual.setClaveEstatus(7);
                cuestionarioActual.getCampos().get("claveEstatus").setValor("7");
                cuestionarioActual.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx(), request.getSession().getId()); 
                /* Cambia el estatus del estudio del participante así como la clave del cuestionario actual */
                
                estudioParticipante.getCampos().get("claveEstatus").setValor("3");
                estudioParticipante.getCampos().get("claveCuestionarioActual").setValor(estudioParticipante.getCuestionarioSiguiente(user.getCx()).toString());
                estudioParticipante.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx());
                siguienteAccion="Finaliza cuestionario";
                %><alert><![CDATA[El cuestionario ha finalizado exitosamente y ha sido enviado a un auditor para su revisión, gracias por su colaboración]]></alert><%
            } else {
               %><alert><![CDATA[Código de autenticación no válido]]></alert><%                
            }
        } else if (cuestionarioActual.getClaveEstatus()==8 && user.getClavePerfil()==3) {
            if (request.getParameter("auth_code").equals(AeSimpleMD5.MD5(request.getSession().getId()))) {
                cuestionarioActual.getCampos().get("siguientePregunta").setValor(cuestionario.getPrimeraPregunta(user.getCx()).toString());
                Boolean hayObservacion = cuestionarioActual.hayObservacionesEnAuditoria(user.getCx());
                if (hayObservacion) {
                    cuestionarioActual.setClaveEstatus(9);
                    cuestionarioActual.getCampos().get("claveEstatus").setValor("9");
                } else {
                    cuestionarioActual.setClaveEstatus(10);
                    cuestionarioActual.getCampos().get("claveEstatus").setValor("10");                    
                }
                cuestionarioActual.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx(), request.getSession().getId()); 
                siguienteAccion="Finaliza cuestionario";
                if (hayObservacion) { %><alert><![CDATA[La auditoría ha finalizado exitosamente y ha sido solicitado al investigador que corrija el formulario, gracias por su colaboración]]></alert><% }
                else { %><alert><![CDATA[La auditoría ha finalizado exitosamente y no hubo observaciones]]></alert><% } 
            } else {
               %><alert><![CDATA[Código de autenticación no válido]]></alert><%                
            }
        }
    }  else if (tipoAccion.equals("send-validation-code")) {
                cuestionarioActual.getCampos().get("claveEstatus").setValor(cuestionarioActual.getClaveEstatus().toString());
                cuestionarioActual.update(user.getClave(), user.getIp(), user.getNavegador(), 279, user.getCx(), request.getSession().getId()); 
               %><alert><![CDATA[El código de autenticación ha sido enviado a su correo registrado, verifique]]></alert><%                
    } else if (cuestionarioActual.getClaveEstatus()==6 && user.getClavePerfil()==2) {
            if (request.getParameter("auth_code").equals(AeSimpleMD5.MD5(request.getSession().getId()))) {
                cuestionarioActual.getCampos().get("siguientePregunta").setValor(cuestionario.getPrimeraPregunta(user.getCx()).toString() );
                cuestionarioActual.setClaveEstatus(6);
                cuestionarioActual.getCampos().get("claveEstatus").setValor("7");
                siguienteAccion="continua";
                %><alert><![CDATA[El cuestionario ha finalizado exitosamente y ha sido enviado a un auditor para su revisión, gracias por su colaboración]]></alert><%
            } else {
               %><alert><![CDATA[Código de autenticación no válido]]></alert><%                
            }
        }
    
    } catch (Fallo f) {
      error = f.getMessage();
    }
      catch (Exception e) {
      error = e.getMessage();
    } finally {%><%
        /*
         * guarda el objeto usuario en la sesión para aprovechar los objetos que se
         * tiene abiertos
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