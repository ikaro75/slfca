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

public class Respuesta extends Consulta {
    private Integer claveRespuesta;
    private Integer clavePregunta;
    private String codigoRespuesta;
    private String respuesta;
    private Integer siEstaEsLaRespuesta;
    private String accion;
    private Boolean respuestaCorrecta;
    private Boolean otro;
    private String textoDespuesDeCampoOtro;
    private Integer claveTipoDatoRespuesta;

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

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public Boolean getOtro() {
        return otro;
    }

    public void setOtro(Boolean otro) {
        this.otro = otro;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    
    public Boolean getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(Boolean respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    @Override
    public String getAccion() {
        return accion;
    }

    @Override
    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    public Integer getSiEstaEsLaRespuesta() {
        return siEstaEsLaRespuesta;
    }

    public void setSiEstaEsLaRespuesta(Integer siEstaEsLaRespuesta) {
        this.siEstaEsLaRespuesta = siEstaEsLaRespuesta;
    }

    public String getTextoDespuesDeCampoOtro() {
        return textoDespuesDeCampoOtro;
    }

    public void setTextoDespuesDeCampoOtro(String textoDespuesDeCampoOtro) {
        this.textoDespuesDeCampoOtro = textoDespuesDeCampoOtro;
    }

    public Integer getClaveTipoDatoRespuesta() {
        return claveTipoDatoRespuesta;
    }

    public void setClaveTipoDatoRespuesta(Integer claveTipoDatoRespuesta) {
        this.claveTipoDatoRespuesta = claveTipoDatoRespuesta;
    }
    
    public Respuesta(Integer claveRespuesta, Usuario usuario) throws Fallo {
        super.setTabla("fide_respuesta");
        super.setLlavePrimaria("clave_respuesta");
        super.setPk(String.valueOf(claveRespuesta)); 
        super.setUsuario(usuario);
        
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_respuesta where clave_respuesta=".concat(String.valueOf(claveRespuesta)));

            if (rs.next()) {

                this.claveRespuesta = rs.getInt("clave_respuesta");
                this.clavePregunta =  rs.getInt("clave_pregunta");
                this.codigoRespuesta = rs.getString("codigo_respuesta");
                this.respuesta = rs.getString("respuesta");
                this.siEstaEsLaRespuesta = rs.getInt("si_esta_es_la_respuesta");
                this.accion = rs.getString("accion");        
                this.respuestaCorrecta = rs.getBoolean ("respuesta_correcta");
                this.otro = rs.getBoolean ("otro");
                this.textoDespuesDeCampoOtro = rs.getString("texto_despues_de_campo_otro");
                this.claveTipoDatoRespuesta= rs.getInt("clave_tipo_dato_respuesta");
                

            } else {
                throw new Fallo("No se encontró la respuesta especificada");
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar la respuesta: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;            
        }
    }
}
