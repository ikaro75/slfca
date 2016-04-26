/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.encuesta;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class RespuestaParticipante extends Consulta{
    private Integer claveRespuestaParticipante;
    private Integer claveCuestionarioParticipante;
    private Integer clavePregunta;
    private Integer claveRespuesta;
    private String respuesta;
    private String otro;
    private String codigoRespuesta;
    private Date fecha;
    private Integer clave_empleado;
    
    public Integer getClaveCuestionarioParticipante() {
        return claveCuestionarioParticipante;
    }
    public void setClaveCuestionarioParticipante(Integer claveCuestionarioParticipante) {
        this.claveCuestionarioParticipante = claveCuestionarioParticipante;
    }
    

    public Integer getClavePregunta() {
        return clavePregunta;
    }

    public void setClavePregunta(Integer clavePregunta) {
        this.clavePregunta = clavePregunta;
    }

    public Integer getClaveRespuesta() {
        return claveRespuesta;
    }

    public void setClaveRespuesta(Integer claveRespuesta) {
        this.claveRespuesta = claveRespuesta;
    }

    public Integer getClaveRespuestaParticipante() {
        return claveRespuestaParticipante;
    }

    public void setClaveRespuestaParticipante(Integer claveRespuestaParticipante) {
        this.claveRespuestaParticipante = claveRespuestaParticipante;
    }

    public Integer getClave_empleado() {
        return clave_empleado;
    }

    public void setClave_empleado(Integer clave_empleado) {
        this.clave_empleado = clave_empleado;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getOtro() {
        return otro;
    }

    public void setOtro(String otro) {
        this.otro = otro;
    }
    
    public RespuestaParticipante(Integer claveRespuestaParticipante, Usuario usuario) throws Fallo {
        super.setTabla("fide_respuesta_participante");
        super.setLlavePrimaria("claveRespuestaParticipante");
        super.setPk(String.valueOf(claveRespuestaParticipante)); 
        super.setUsuario(usuario);
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_respuesta_participante where clave_respuesta_participante=".concat(String.valueOf(claveRespuestaParticipante)));

            if (rs.next()) {
                this.claveRespuestaParticipante = rs.getInt("clave_respuesta_participante");
                this.claveCuestionarioParticipante =  rs.getInt("clave_cuestionario_participante");
                this.clavePregunta =  rs.getInt("clave_pregunta");
                this.claveRespuesta =  rs.getInt("clave_respuesta");
                this.respuesta = rs.getString("respuesta");
                this.otro = rs.getString("otro");
                this.codigoRespuesta = rs.getString("codigo_respuesta");
                this.fecha = rs.getDate("fecha");
                this.clave_empleado =  rs.getInt("clave_empleado)");

                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró la respuesta especificada");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la respuesta");
        }
    }

    public RespuestaParticipante(Integer claveCuestionarioParticipante, String nombreCampo, Integer valorCampo, Usuario usuario) throws Fallo {                
        super.setTabla("fide_respuesta_participante");
        super.setLlavePrimaria("clave_respuesta_participante");
        super.setUsuario(usuario);
        
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_respuesta_participante where clave_cuestionario_participante=".concat(String.valueOf(claveCuestionarioParticipante)).concat(" and ").concat(nombreCampo).concat("=").concat(String.valueOf(valorCampo)));

            if (rs.next()) {
                super.setPk(String.valueOf(claveRespuestaParticipante)); 
                this.claveRespuestaParticipante = rs.getInt("clave_respuesta_participante");
                this.claveCuestionarioParticipante =  rs.getInt("clave_cuestionario_participante");
                this.clavePregunta =  rs.getInt("clave_pregunta");
                this.claveRespuesta =  rs.getInt("clave_respuesta");
                this.respuesta = rs.getString("respuesta");
                this.otro = rs.getString("otro");
                this.codigoRespuesta = rs.getString("codigo_respuesta");
                this.fecha = rs.getDate("fecha");
                this.clave_empleado =  rs.getInt("clave_empleado");

                super.setPk(String.valueOf(this.claveRespuestaParticipante));   
                //super.setCampos();
                /*if (accion!=null) {//No se hace la carga si la acción es nula
                    super.setSQL(cx, 277 , accion, this.claveRespuestaParticipante.toString(), "", reglasDeReemplazo);
                    super.setCampos(cx);
                }*/
                
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró la respuesta especificada");
            }
        } catch (Exception e) {
            rs = null;
            //oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la respuesta");
        }
    }
}
