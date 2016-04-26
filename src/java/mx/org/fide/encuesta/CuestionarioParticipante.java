package mx.org.fide.encuesta;

import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Usuario;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.controlador.Controlador;
import mx.org.fide.mail.Mail;
import mx.org.fide.utilerias.AeSimpleMD5;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.catalina.connector.Request;

public class CuestionarioParticipante extends Consulta {

    private Integer claveCuestionarioParticipante;
    private Integer clavePunto;
    private Integer claveBeneficiario;
    private Integer claveCuestionario;
    private Date fechaInicio;
    private Date fechaFinal;
    private Integer siguientePregunta;
    private Integer claveEstatus;
    private ArrayList<RespuestaParticipante> respuestasParticipante;
    private ArrayList<Integer> preguntasPendientes;
    String autorizacion;
    /*
     * En esta clase se debe validar que no se dupliquen los cuestionarios de un
     * participante
     *
     */

    public Integer getClaveCuestionario() {
        return claveCuestionario;
    }

    public void setClaveCuestionario(Integer claveCuestionario) {
        this.claveCuestionario = claveCuestionario;
    }

    public Integer getClaveCuestionarioParticipante() {
        return claveCuestionarioParticipante;
    }

    public void setClaveCuestionarioParticipante(Integer claveCuestionarioParticipante) {
        this.claveCuestionarioParticipante = claveCuestionarioParticipante;
    }

    public Integer getClaveEstatus() {
        return claveEstatus;
    }

    public void setClaveEstatus(Integer claveEstatus) {
        this.claveEstatus = claveEstatus;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Integer getSiguientePregunta() {
        return siguientePregunta;
    }

    public void setSiguientePregunta(Integer siguientePregunta) {
        this.siguientePregunta = siguientePregunta;
    }

    public Integer getSiguienteSeccion(Conexion cx) throws Fallo {
        try {
            Pregunta pregunta = new Pregunta(this.siguientePregunta, super.getUsuario());
            return pregunta.getClaveSeccion();
        } catch (Exception e) {
            throw new Fallo("Error al recuperar la ultima sección del cuestionario: ".concat(e.getMessage()));
        }
    }

    public ArrayList<RespuestaParticipante> getRespuestasParticipante(Conexion cx) throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        RespuestaParticipante respuestaParticipante = null;
        this.respuestasParticipante = new ArrayList<RespuestaParticipante>();

        try {
            rs = oDb.getRs("select clave_respuesta_participante from fide_respuesta_participante where clave_cuestionario_participante=".concat(String.valueOf(this.claveCuestionarioParticipante)));

            while (rs.next()) {
                respuestaParticipante = new RespuestaParticipante(rs.getInt("clave_respuesta_participante"), super.getUsuario());
                this.respuestasParticipante.add(respuestaParticipante);
            }
            rs.close();
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar respuestas del participante : ".concat(e.getMessage()));
        }
        return respuestasParticipante;
    }

    public void setRespuestasParticipante(ArrayList<RespuestaParticipante> respuestasParticipante) {
        this.respuestasParticipante = respuestasParticipante;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public ArrayList<Seccion> getSeccionesConPreguntasPendientes() throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        ArrayList<Seccion> secciones = new ArrayList<Seccion>();
        q = new StringBuilder("select distinct clave_seccion from fide_pregunta where clave_pregunta in ("
                .concat("select clave_pregunta from fide_pregunta_participante where claveEstatusPregunta=2 and clave_cuestionario_participante=")
                .concat(this.claveCuestionarioParticipante.toString()).concat(")"));

        try {
            rs = oDb.getRs(q.toString());
            while (rs.next()) {
                secciones.add(new Seccion(rs.getInt("clave_seccion"), super.getUsuario()));
            }

        } catch (Exception e) {
            throw new Fallo("Error al recuperar las secciones con preguntas pendientes: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            return secciones;
        }
    }

    public ArrayList<Integer> getPreguntasPendientes(Conexion cx) throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        this.preguntasPendientes = new ArrayList<Integer>();

        try {
            rs = oDb.getRs("select clave_pregunta_participante from fide_pregunta_participante where "
                    .concat("and claveEstatusPregunta=2 and ")
                    .concat("clave_cuestionario_participante=").concat(this.claveCuestionarioParticipante.toString()));

            while (rs.next()) {
                this.respuestasParticipante.add(new RespuestaParticipante(rs.getInt("clave_respuesta_participante"), super.getUsuario()));
            }

        } catch (Exception e) {

            throw new Fallo("Error al recuperar preguntas pendientes del participante : ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        }
        return this.preguntasPendientes;
    }

    public Integer getClavePunto() {
        return clavePunto;
    }

    public void setClavePunto(Integer clavePunto) {
        this.clavePunto = clavePunto;
    }

    public Integer getClaveBeneficiario() {
        return claveBeneficiario;
    }

    public void setClaveBeneficiario(Integer claveBeneficiario) {
        this.claveBeneficiario = claveBeneficiario;
    }

    public CuestionarioParticipante(Integer claveCuestionarioParticipante, Usuario usuario) throws Fallo {

        super.setTabla("fide_cuestionario");
        super.setLlavePrimaria("clave_cuestionario");
        super.setPk(String.valueOf(claveCuestionario));
        super.setUsuario(usuario);

        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        ResultSet rsTemp = null;
        Conexion oDb = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_cuestionario_participante where clave_cuestionario_participante=".concat(String.valueOf(claveCuestionarioParticipante)));

            if (rs.next()) {

                this.claveCuestionarioParticipante = rs.getInt("clave_cuestionario_participante");
                this.claveBeneficiario = rs.getInt("clave_beneficiario");
                this.clavePunto = rs.getInt("clave_punto");
                this.claveCuestionario = rs.getInt("clave_cuestionario");
                this.fechaInicio = rs.getDate("fecha_inicio");
                this.fechaFinal = rs.getDate("fecha_final");
               if (rs.getInt("siguiente_pregunta") == 0) {

                    q = new StringBuilder();
                    q.append("SELECT TOP 1 fide_pregunta.orden as primerPregunta, clave_pregunta FROM fide_seccion_pregunta,fide_pregunta WHERE fide_seccion_pregunta.clave_seccion=fide_pregunta.clave_seccion AND fide_seccion_pregunta.clave_cuestionario=").append(String.valueOf(this.claveCuestionario)).append(" ORDER BY fide_pregunta.orden");
                    rsTemp = oDb.getRs(q.toString());
                    if (!rsTemp.next()) {
                        throw new Fallo("No fue posible identificar la primer pregunta del cuestionario seleccionado, verifique");
                    } else {
                        if (rsTemp.getObject("clave_pregunta") == null) {
                            throw new Fallo("No fue posible identificar la primer pregunta del cuestionario seleccionado, verifique");
                        } else {
                            this.siguientePregunta = rsTemp.getInt("clave_pregunta");
                        }
                    } 
                    
                    rsTemp.close();
                    rsTemp = null;
                } else {
                        this.siguientePregunta = rs.getInt("siguiente_pregunta");
               }

                this.claveEstatus = rs.getInt("clave_estatus");

                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró el cuestionario especificado");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar el cuestionario: ".concat(e.getMessage()));
        }
    }

    public CuestionarioParticipante(Integer claveCuestionario, Integer claveParticipante) throws Fallo {
        super.setTabla("fide_cuestionario_participante");
        super.setLlavePrimaria("clave_cuestionario_participante");
        super.setPk(String.valueOf(claveCuestionario));

        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        ResultSet rsTemp = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_cuestionario_participante where clave_cuestionario=".concat(String.valueOf(claveCuestionario)).concat(" AND claveParticipante=").concat(String.valueOf(claveParticipante)));

            if (rs.next()) {

                this.claveCuestionarioParticipante = rs.getInt("clave_cuestionario_participante");
                this.claveBeneficiario = rs.getInt("clave_beneficiario");
                this.clavePunto = rs.getInt("clave_punto");
                this.claveCuestionario = rs.getInt("clave_cuestionario");
                this.fechaInicio = rs.getDate("fecha_inicio");
                this.fechaFinal = rs.getDate("fecha_final");
                //Al inicilizarse la siguiente pregunta debe ser la clave del objeto pregunta 
                if (rs.getInt("siguiente_pregunta") == 0) {

                    q = new StringBuilder();
                    q.append("SELECT TOP 1 fide_pregunta.orden as primerPregunta, clave_pregunta FROM fide_seccion_pregunta,fide_pregunta WHERE fide_seccion_pregunta.clave_seccion=fide_pregunta.clave_seccion AND fide_seccion_pregunta.clave_cuestionario=").append(String.valueOf(this.claveCuestionario)).append(" ORDER BY fide_pregunta.orden");
                    rsTemp = oDb.getRs(q.toString());
                    if (!rsTemp.next()) {
                        throw new Fallo("No fue posible identificar la primer pregunta del cuestionario seleccionado, verifique");
                    } else {
                        if (rsTemp.getObject("clave_pregunta") == null) {
                            throw new Fallo("No fue posible identificar la primer pregunta del cuestionario seleccionado, verifique");
                        } else {
                            this.siguientePregunta = rsTemp.getInt("clave_pregunta");
                        }
                    }
                } else {
                        this.siguientePregunta = rs.getInt("siguiente_pregunta");
               }
                this.claveEstatus = rs.getInt("clave_estatus");

                oDb = null;

            } else {
                throw new Fallo("No se encontró el cuestionario especificado");
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar el cuestionario: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        }
    }

    public CuestionarioParticipante(Consulta c) throws Fallo {
        super.setAccion(c.getAccion());
        super.setCampos(c.getCampos());
        super.setClaveAplicacion(c.getClaveAplicacion());
        super.setClaveForma(c.getClaveForma());
        super.setClaveConsulta(c.getClaveConsulta());
        super.setClavePerfil(c.getClavePerfil());
        super.setIdx(c.getIdx());
        super.setLimiteDeRegistros(c.getLimiteDeRegistros());
        super.setLlavePrimaria(c.getLlavePrimaria());
        super.setNumeroDeRegistros(c.getNumeroDeRegistros());
        super.setOrdx(c.getOrdx());
        super.setPagina(c.getPagina());
        super.setPk(c.getPk());
        super.setRegistros(c.getRegistros());
        super.setSQL(c.getSQL());
        super.setTabla(c.getTabla());
        super.setW(c.getW());
        super.setUsuario(c.getUsuario());

        StringBuilder q = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                
        try {
            if (c.getCampos().get("clave_cuestionario_participante") != null) {
                this.claveCuestionarioParticipante = Integer.parseInt(c.getCampos().get("clave_cuestionario_participante").getValor());
            }

            if (c.getCampos().get("clave_cuestionario").getValor() != null) {
                this.claveCuestionario = Integer.parseInt(c.getCampos().get("clave_cuestionario").getValor());
            } else {
                throw new Fallo("No se especificó la clave del cuestionario, verifique");
            }

            if (c.getCampos().get("clave_punto") == null && c.getCampos().get("clave_beneficiario") == null) {
                throw new Fallo("Es necesario especificar punto de entrega o beneficiario, verifique");
            }

            if (c.getCampos().get("clave_punto") != null) {
                this.clavePunto = Integer.parseInt(c.getCampos().get("clave_punto").getValor());
            }

            if (c.getCampos().get("clave_beneficiario") != null) {
                this.claveBeneficiario = Integer.parseInt(c.getCampos().get("clave_beneficiario").getValor());
            }

            if (c.getCampos().get("fecha_inicio").getValor() == null) {
                c.getCampos().get("fecha_inicio").setValor(formatter.format(new Date()));
                this.fechaInicio = new Date();
            } else if (c.getCampos().get("fecha_inicio").getValor().equals("")) {
                c.getCampos().get("fecha_inicio").setValor(formatter.format(new Date()));
                this.fechaInicio = new Date();
            } else {
                try {
                    this.fechaInicio = formatter.parse(c.getCampos().get("fecha_inicio").getValor());
                } catch (Exception ex) {
                    formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    this.fechaInicio = formatter.parse(c.getCampos().get("fecha_inicio").getValor());
                }
            }

            //Al inicilizarse la siguiente pregunta debe ser la clave del objeto pregunta 
            if (c.getCampos().get("siguiente_pregunta") == null) {
                Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
                ResultSet rs;
                q = new StringBuilder();
                q.append("SELECT TOP 1 fide_pregunta.orden as primerPregunta, clave_pregunta FROM fide_seccion_pregunta,fide_pregunta WHERE fide_seccion_pregunta.clave_seccion=fide_pregunta.clave_seccion AND fide_seccion_pregunta.clave_cuestionario=").append(String.valueOf(this.claveCuestionario)).append(" ORDER BY fide_pregunta.orden");
                rs = oDb.getRs(q.toString());
                if (!rs.next()) {
                    throw new Fallo("No fue posible identificar la primer pregunta del cuestionario seleccionado, verifique");
                } else {
                    if (rs.getObject("clave_pregunta") == null) {
                        throw new Fallo("No fue posible identificar la primer pregunta del cuestionario seleccionado, verifique");
                    } else {
                        c.getCampos().get("siguiente_pregunta").setValor(rs.getString("clave_pregunta"));
                        this.siguientePregunta = rs.getInt("clave_pregunta");
                    }
                }

            } else if (c.getCampos().get("siguiente_pregunta").getValor() == null || c.getCampos().get("siguiente_pregunta").getValor().equals("")) {
                Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
                ResultSet rs;
                q = new StringBuilder();
                q.append("SELECT TOP 1 fide_pregunta.orden as primerPregunta, clave_pregunta FROM fide_seccion_pregunta,fide_pregunta WHERE fide_seccion_pregunta.clave_seccion=fide_pregunta.clave_seccion AND fide_seccion_pregunta.clave_cuestionario=").append(String.valueOf(this.claveCuestionario)).append(" ORDER BY fide_pregunta.orden");
                rs = oDb.getRs(q.toString());
                if (!rs.next()) {
                    throw new Fallo("No fue posible identificar la primer pregunta del cuestionario seleccionado, verifique");
                } else {
                    if (rs.getObject("clave_pregunta") == null) {
                        throw new Fallo("No fue posible identificar la primer pregunta del cuestionario seleccionado, verifique");
                    } else {
                        this.siguientePregunta = rs.getInt("clave_pregunta");
                        c.getCampos().get("siguiente_pregunta").setValor(this.siguientePregunta.toString());
                    }
                }
            } else {
                this.siguientePregunta = Integer.parseInt(c.getCampos().get("siguiente_pregunta").getValor());
            }

            if (c.getCampos().get("clave_estatus").getValor() != null) {
                this.claveEstatus = Integer.parseInt(c.getCampos().get("clave_estatus").getValor());
            } else {
                throw new Fallo("No se especificó el estatus de la encuesta, verifique");
            }
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public CuestionarioParticipante(Integer claveCuestionarioParticipante, String accion) throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_cuestionario_participante where clave_cuestionario_participante=".concat(claveCuestionarioParticipante.toString()));

            if (rs.next()) {
                this.claveCuestionarioParticipante = rs.getInt("clave_cuestionario_participante");
                this.claveBeneficiario = rs.getInt("clave_beneficiario");
                this.clavePunto = rs.getInt("clave_punto");
                this.claveCuestionario = rs.getInt("clave_cuestionario");
                this.fechaInicio = rs.getDate("fecha_inicio");
                this.fechaFinal = rs.getDate("fecha_final");
                this.siguientePregunta = rs.getInt("siguiente_pregunta");
                this.claveEstatus = rs.getInt("clave_estatus");

                super.setPk(String.valueOf(this.claveCuestionarioParticipante));

                /*if (accion!=null) {//No se hace la carga si la acción es nula
                 super.setSQL();
                 super.setCampos(cx);
                 }*/
                rs.close();
                oDb.cierraConexion();

            } else {
                rs.close();
                oDb.cierraConexion();
                throw new Fallo("No se encontró el cuestionario especificado");
            }
        } catch (Exception e) {
            oDb.cierraConexion();
            throw new Fallo("Error al recuperar el cuestionario");
        }
    }

    public String insert() throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer claveBitacora;
        Boolean startedTransaction = false;
                
        try {
            //Verifica si no ha sido de dada de alta la relación entre beneficiario y cuestionario
            if (this.claveBeneficiario != null) {
                q.append("SELECT clave_cuestionario FROM fide_cuestionario_participante WHERE clave_cuestionario=").append(this.claveCuestionario).append(" AND clave_beneficiario=").append(this.claveBeneficiario);
                rs = oDb.getRs(q.toString());
                if (rs.next()) {
                    rs.close();
                    throw new Fallo("Este cuestionario ya ha sido asignado a este beneficiario anteriormente, verifique");
                }
            }
            
            //El bloque de abajo se quita porque es posible realizar varios cuestionarios de supervisión  a un punto de entrega 11/02/2015
            //Verifica si no ha sido de dada de alta la relación entre puto de entrega y cuestionario
            /*if (this.clavePunto != null) {
                q.append("SELECT clave_cuestionario FROM fide_cuestionario_participante WHERE clave_cuestionario=").append(this.claveCuestionario).append(" AND clave_punto=").append(this.clavePunto);
                rs = oDb.getRs(q.toString());
                if (rs.next()) {
                    rs.close();
                    throw new Fallo("Este cuestionario ya ha sido asignado a este beneficiario anteriormente, verifique");
                }
            }*/

            //1. Abre transaccion
            if (oDb.getDbType() == Conexion.DbType.MYSQL) {
                oDb.execute("START TRANSACTION");
                
            } else if (oDb.getDbType() == Conexion.DbType.MSSQL) {
                oDb.execute("BEGIN TRANSACTION");
                oDb.execute("SET DATEFORMAT YMD");
            }

            startedTransaction = true;
            //2. Inserta nuevo fide_cuestionario_participante
            resultadoXML.append("<insert_cuestionarioParticipante>").append(super.insert(oDb)).append("</insert_cuestionarioParticipante>");
            this.claveCuestionarioParticipante = Integer.parseInt(super.getPk());

            //2.1 Selecciona las secciones del cuestionario
            for (Seccion seccion : new Cuestionario(this.claveCuestionario, super.getUsuario()).getSecciones()) {
                //2.1.1 Selecciona las preguntas de la seccion
                for (Pregunta pregunta : seccion.getPreguntas()) {
                    //2.1.2 Selecciona las respuestas de las preguntas
                    if (pregunta.getClaveTipoPregunta() == 5) {
                        for (Respuesta respuesta : pregunta.getRespuestas()) {
                            q = new StringBuilder();
                            q.append("INSERT INTO fide_respuesta_participante (clave_cuestionario_participante")
                                    .append(",clave_pregunta,clave_respuesta,fecha,clave_empleado)")
                                    .append(" VALUES(")
                                    .append(this.claveCuestionarioParticipante).append(",")
                                    .append(pregunta.getClavePregunta()).append(",")
                                    .append(respuesta.getClaveRespuesta()).append(",'")
                                    .append(dateFormatter.format(new Date())).append("',")
                                    .append(super.getUsuario().getClave()).append(")");
                            resultadoXML.append("<insert_cuestionarioParticipante><![CDATA[".concat(q.toString()).concat("]]></insert_cuestionarioParticipante>"));

                            rs = oDb.execute(q.toString());

                            if (rs != null) {
                                if (rs.next()) {
                                    claveBitacora = rs.getInt(1);

                                    resultadoXML.append("<pk>").append(claveBitacora).append("</pk>");

                                    //Inserta en bitácora
                                    oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,"
                                            .concat("ip,navegador,clave_forma,clave_registro) ")
                                            .concat("VALUES(").concat(String.valueOf(super.getUsuario().getClave()))
                                            .concat(",'").concat(dateFormatter.format(new Date())).concat("',")
                                            .concat("2,'").concat(super.getUsuario().getIp()).concat("','").concat(super.getUsuario().getNavegador()).concat("',277,")
                                            .concat(String.valueOf(claveBitacora)).concat(")"));

                                } else {
                                    throw new Fallo("Error al recuperar clave de bitacora");
                                }
                            }

                        }
                    } else {
                        q = new StringBuilder().append("INSERT INTO fide_respuesta_participante (clave_cuestionario_participante,clave_pregunta,fecha,clave_empleado) VALUES(").append(this.claveCuestionarioParticipante).append(",").append(pregunta.getClavePregunta()).append(",'").append(dateFormatter.format(new Date())).append("',").append(super.getUsuario().getClave()).append(")");

                        resultadoXML.append("<insert_cuestionarioParticipante><![CDATA[".concat(q.toString()).concat("]]></insert_cuestionarioParticipante>"));
                        rs = oDb.execute(q.toString());

                        if (rs != null) {
                            if (rs.next()) {
                                claveBitacora = rs.getInt(1);

                                resultadoXML.append("<pk>").append(claveBitacora).append("</pk>");

                                //Inserta en bitácora
                                oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,"
                                        .concat("ip,navegador,clave_forma,clave_registro) ")
                                        .concat("VALUES(").concat(String.valueOf(super.getUsuario().getClave()))
                                        .concat(",'").concat(dateFormatter.format(new Date())).concat("',")
                                        .concat("2,'").concat(super.getUsuario().getIp()).concat("','").concat(super.getUsuario().getNavegador()).concat("',277,")
                                        .concat(String.valueOf(claveBitacora)).concat(")"));

                            } else {
                                throw new Fallo("Error al recuperar clave de bitacora");
                            }
                        }
                    }
                }
            }

            //2.3 Cambia el estatus
            this.claveEstatus = 2;
            q = new StringBuilder();
            q.append("UPDATE fide_cuestionario_participante SET clave_estatus=2 WHERE clave_cuestionario_participante=").append(this.claveCuestionarioParticipante.toString());
            resultadoXML.append("<insert_cuestionarioParticipante><![CDATA[".concat(q.toString()).concat("]]></insert_cuestionarioParticipante>"));
            rs = oDb.execute(q.toString());

            if (rs != null) {

                if (rs.next()) {
                    claveBitacora = rs.getInt(1);
                    resultadoXML.append("<pk>").append(claveBitacora).append("</pk>");

                    //Inserta en bitácora
                    oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,"
                            .concat("ip,navegador,clave_forma,clave_registro) ")
                            .concat("VALUES(").concat(String.valueOf(super.getUsuario().getClave()))
                            .concat(",'").concat(dateFormatter.format(new Date())).concat("',")
                            .concat("2,'").concat(super.getUsuario().getIp()).concat("','").concat(super.getUsuario().getNavegador()).concat("',287,")
                            .concat(String.valueOf(claveBitacora)).concat(")"));

                } else {
                    throw new Fallo("Error al recuperar clave de bitacora");
                }
            }

            oDb.execute("COMMIT");

        } catch (Exception e) {
            if (startedTransaction)
                oDb.execute("ROLLBACK");
            throw new Fallo(resultadoXML.toString().concat("<detalles>").concat(e.getMessage()).concat("</detalles>"));
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }
        return resultadoXML.toString();
    }

    public String insert(Conexion oDb) throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Integer claveBitacora;
        Consulta c = null;

        try {
            //Verifica si no ha sido de dada de alta la relación entre beneficiario y cuestionario
            if (this.claveBeneficiario != null) {
                q.append("SELECT clave_cuestionario FROM fide_cuestionario_participante WHERE clave_cuestionario=").append(this.claveCuestionario).append(" AND clave_beneficiario=").append(this.claveBeneficiario);
                rs = oDb.getRs(q.toString());
                if (rs.next()) {
                    rs.close();
                    throw new Fallo("Este cuestionario ya ha sido asignado a este beneficiario anteriormente, verifique");
                }
            }

            //Verifica si no ha sido de dada de alta la relación entre puto de entrega y cuestionario
            if (this.clavePunto != null) {
                q.append("SELECT clave_cuestionario FROM fide_cuestionario_participante WHERE clave_cuestionario=").append(this.claveCuestionario).append(" AND clave_punto=").append(this.clavePunto);
                rs = oDb.getRs(q.toString());
                if (rs.next()) {
                    rs.close();
                    throw new Fallo("Este cuestionario ya ha sido asignado a este beneficiario anteriormente, verifique");
                }
            }

            //2. Inserta nuevo fide_cuestionario_participante
            resultadoXML.append("<insert_cuestionarioParticipante>").append(super.insert(oDb)).append("</insert_cuestionarioParticipante>");
            this.claveCuestionarioParticipante = Integer.parseInt(super.getPk());

            //2.1 Selecciona las secciones del cuestionario
            for (Seccion seccion : new Cuestionario(this.claveCuestionario, super.getUsuario()).getSecciones()) {
                //2.1.1 Selecciona las preguntas de la seccion
                for (Pregunta pregunta : seccion.getPreguntas()) {
                    //2.1.2 Selecciona las respuestas de las preguntas
                    if (pregunta.getClaveTipoPregunta() == 5) {
                        for (Respuesta respuesta : pregunta.getRespuestas()) {
                            c = new Consulta(369, "insert", "0", "", null,super.getUsuario());
                            c.getCampos().get("clave_cuestionario_participante").setValor(this.claveCuestionarioParticipante.toString());
                            c.getCampos().get("clave_pregunta").setValor(pregunta.getClavePregunta().toString());
                            c.getCampos().get("clave_respuesta").setValor(respuesta.getClaveRespuesta().toString());
                            c.getCampos().get("fecha").setValor(dateFormatter.format(new Date()));
                            c.getCampos().get("clave_empleado").setValor(super.getUsuario().getClave().toString());
                            resultadoXML.append(c.insert(oDb).replaceAll("insert>", "insert_respuesta_participante>"));

                        }
                    } else {
                        c = new Consulta(369, "insert", "0", "", null, super.getUsuario());
                        c.getCampos().get("clave_cuestionario_participante").setValor(this.claveCuestionarioParticipante.toString());
                        c.getCampos().get("clave_pregunta").setValor(pregunta.getClavePregunta().toString());
                        c.getCampos().get("fecha").setValor(dateFormatter.format(new Date()));
                        c.getCampos().get("clave_empleado").setValor(super.getUsuario().getClave().toString());
                        resultadoXML.append(c.insert(oDb).replaceAll("insert>", "insert_respuesta_participante>"));
                    }
                }
            }

            //2.3 Cambia el estatus
            super.setAccion("update");
            super.getCampos().get("clave_estatus").setValor("2");
            resultadoXML.append(this.update(oDb));

        } catch (Exception e) {
            throw new Fallo(resultadoXML.toString().concat("<detalles>").concat(e.getMessage()).concat("</detalles>"));
        }
        return resultadoXML.toString();
    }

    public String delete(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        try {
            //1. Abre transaccion
            if (oDb.getDbType() == Conexion.DbType.MYSQL) {
                oDb.execute("START TRANSACTION");
            } else if (oDb.getDbType() == Conexion.DbType.MSSQL) {
                oDb.execute("BEGIN TRANSACTION");
                oDb.execute("SET DATEFORMAT YMD");
            }

            //2. Borra respuestas del participante
            for (RespuestaParticipante respuestaParticipante : this.getRespuestasParticipante(cx)) {
                q.append("DELETE FROM fide_respuesta_participante WHERE clave_respuesta_participante=").append(respuestaParticipante.getClaveRespuestaParticipante());
                resultadoXML.append("<delete_cuestionarioParticipante><![CDATA[".concat(q.toString()).concat("]]></delete_cuestionarioParticipante>"));
                oDb.execute(q.toString());

                //Inserta en bitácora
                oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,"
                        .concat("ip,navegador,clave_forma,clave_registro) ")
                        .concat("VALUES(").concat(String.valueOf(claveEmpleado))
                        .concat(",'").concat(dateFormatter.format(new Date())).concat("',")
                        .concat("4,'").concat(ip).concat("','").concat(browser).concat("',277,")
                        .concat(String.valueOf(respuestaParticipante.getClaveRespuestaParticipante().toString())).concat(")"));

            }

            //3. Borra cuestionario del participante
            q = new StringBuilder();
            q.append(super.delete(false,super.getUsuario()));
            resultadoXML.append("<delete_cuestionarioParticipante><![CDATA[".concat(q.toString()).concat("]]></delete_cuestionarioParticipante>"));
            oDb.execute(q.toString());

            //5. Inserta eliminación en bitácora
            oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,"
                    .concat("ip,navegador,clave_forma,clave_registro) ")
                    .concat("VALUES(").concat(String.valueOf(claveEmpleado))
                    .concat(",'").concat(dateFormatter.format(new Date())).concat("',")
                    .concat("4,'").concat(ip).concat("','").concat(browser).concat("',")
                    .concat(String.valueOf(forma)).concat(",")
                    .concat(this.getClaveCuestionarioParticipante().toString()).concat(")"));

            return resultadoXML.toString();
        } catch (Exception e) {
            oDb.execute("ROLLBACK");
            throw new Fallo(resultadoXML.toString().concat("<error>").concat(e.getMessage()).concat("</error>"));
        }

    }
}
