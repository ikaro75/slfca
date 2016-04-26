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

public class Pregunta extends Consulta{
    /* Propias de la base de datos */
    private Integer clavePregunta;
    private Integer claveSeccion;
    private String instruccion;
    private String pregunta;
    private Integer claveTipoPregunta;
    private Integer orden;
    private Integer claveTipoDatoRespuesta;
    private Boolean obligatoria;
    private String textoFinal;
    private ArrayList<Respuesta> respuestas;
    private Boolean hayUnaRespuestaQueOcultaPreguntas;
    
    /*
    select
ex_cuestionario.claveCuestionario,
ex_cuestionario.cuestionario,
fide_seccion_cuestionario.seccion,
fide_seccion_cuestionario.instruccion as instruccionSeccion,
fide_pregunta.instruccion as instruccionPregunta,
fide_pregunta.clavePregunta,
fide_pregunta.pregunta,
fide_pregunta.claveTipoPregunta,
fide_pregunta.claveTipoDatoRespuesta,
fide_pregunta.obligatoria,
fide_pregunta.textoFinal,
fide_pregunta.orden
from fide_cuestionario, fide_seccion_cuestionario, fide_pregunta
where fide_cuestionario.claveCuestionario=fide_seccion_cuestionario.claveCuestionario and
fide_seccion_cuestionario.claveSeccion=fide_pregunta.claveSeccion and
fide_pregunta.clavePregunta=1 */

    public Integer getClavePregunta() {
        return clavePregunta;
    }

    public void setClavePregunta(Integer clavePregunta) {
        this.clavePregunta = clavePregunta;
    }

    public Integer getClaveSeccion() {
        return claveSeccion;
    }

    public void setClaveSeccion(Integer claveSeccion) {
        this.claveSeccion = claveSeccion;
    }

    public Integer getClaveTipoDatoRespuesta() {
        return claveTipoDatoRespuesta;
    }

    public void setClaveTipoDatoRespuesta(Integer claveTipoDatoRespuesta) {
        this.claveTipoDatoRespuesta = claveTipoDatoRespuesta;
    }

    public Integer getClaveTipoPregunta() {
        return claveTipoPregunta;
    }

    public void setClaveTipoPregunta(Integer claveTipoPregunta) {
        this.claveTipoPregunta = claveTipoPregunta;
    }

    public String getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(String instruccion) {
        this.instruccion = instruccion;
    }

    public Boolean getObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(Boolean obligatoria) {
        this.obligatoria = obligatoria;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getTextoFinal() {
        return textoFinal;
    }

    public void setTextoFinal(String textoFinal) {
        this.textoFinal = textoFinal;
    }

    public ArrayList<Respuesta> getRespuestas () throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Respuesta respuesta = null;
        this.respuestas = new  ArrayList<Respuesta>();
        try {
            rs = oDb.getRs("select clave_respuesta from fide_respuesta where clave_pregunta=".concat(String.valueOf(this.clavePregunta)).concat(" ORDER BY orden"));

            while (rs.next()) {
                respuesta = new Respuesta(rs.getInt("clave_respuesta"), super.getUsuario());
                this.respuestas .add(respuesta);
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar respuestas: ".concat(e.getMessage()) );
        }  finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        }
        return respuestas;
    }

    public void setRespuestas(ArrayList<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public Integer getAnterior(Integer claveCuestionario, Integer orden) throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer preguntaAnterior;
        try {            
            rs = oDb.getRs("select clave_pregunta from fide_pregunta where orden<".concat(orden.toString()).concat(" and clave_seccion in (select clave_seccion from fide_seccion_cuestionario where clave_cuestionario=".concat(claveCuestionario.toString()).concat(") order by orden desc limit 0,1")));

            if (rs.next()) {
                preguntaAnterior = rs.getInt("clave_pregunta");
                rs.close();
                return  preguntaAnterior;
            } else {
                 throw new Fallo("No hay pregunta anterior");
            }
        } catch (Exception e) {
                 throw new Fallo(e.getMessage());
        } finally {
          oDb.cierraConexion();
        }
    }

    public Integer getSiguiente(Integer claveCuestionario, Integer ordenPregunta) throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer preguntaSiguiente;
        try {            
            rs = oDb.getRs("select clave_pregunta from fide_pregunta where orden>".concat(orden.toString()).concat(" and claveSeccion in (select claveSeccion from fide_seccion_cuestionario where clave_cuestionario=".concat(claveCuestionario.toString()).concat(") order by orden limit 0,1")));

            if (rs.next()) {
                preguntaSiguiente = rs.getInt("clave_pregunta");
                rs.close();
                return preguntaSiguiente;
            } else {
                throw new Fallo("No hay pregunta posterior");
            }
        } catch (Exception e) {
                throw new Fallo(e.getMessage());
        } finally {
                oDb.cierraConexion();
        }
    }

    public Boolean getHayUnaRespuestaQueOcultaPreguntas() throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Boolean hayUnaRespuestaQueOcultaPreguntas = false;
        try {            
            rs = oDb.getRs("select count(clave_respuesta) as oculta_preguntas FROM fide_respuesta WHERE si_esta_es_la_respuesta=2 AND clave_pregunta=".concat(this.clavePregunta.toString()));

            if (rs.next()) {
                hayUnaRespuestaQueOcultaPreguntas= rs.getInt("oculta_preguntas")==0?false:true;
                rs.close();
            } else {
                throw new Fallo("No hay pregunta posterior");
            }
            return hayUnaRespuestaQueOcultaPreguntas;
        } catch (Exception e) {
                throw new Fallo(e.getMessage());
        } finally {
                oDb.cierraConexion();
        }
    }

    public void setHayUnaRespuestaQueOcultaPreguntas(Boolean hayUnaRespuestaQueOcultaPreguntas) {
        this.hayUnaRespuestaQueOcultaPreguntas = hayUnaRespuestaQueOcultaPreguntas;
    }
    
    
            
    public ArrayList<Integer> respuestasTipoOtro() throws Fallo  {
        ArrayList<Integer> resultado = new ArrayList();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        try {
            rs = oDb.getRs("select clave_respuesta from fide_respuesta where clave_pregunta=".concat(String.valueOf(this.clavePregunta)).concat(" and otro=1"));

            while (rs.next()) {
                resultado.add(rs.getInt("clave_respuesta"));
            }    

            rs.close();
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            
            return resultado;
            
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la pregunta: ".concat(e.getMessage()) );
        }
    }
    
    public Pregunta(Integer clavePregunta, Usuario usuario) throws Fallo {
        super.setTabla("fide_pregunta");
        super.setLlavePrimaria("clave_pregunta");
        super.setPk(String.valueOf(clavePregunta)); 
        super.setUsuario(usuario);

        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_pregunta where clave_pregunta=".concat(String.valueOf(clavePregunta)));

            if (rs.next()) {

                this.clavePregunta = rs.getInt("clave_pregunta");
                this.claveSeccion =  rs.getInt("clave_seccion");
                this.instruccion = rs.getString("instruccion");
                this.pregunta = rs.getString("pregunta");
                this.claveTipoPregunta = rs.getInt("clave_tipo_pregunta");
                this.orden = rs.getInt("orden");
                this.claveTipoDatoRespuesta = rs.getInt("clave_tipo_dato_respuesta");          
                this.obligatoria = rs.getBoolean ("obligatoria");
                this.textoFinal = rs.getString("texto_final");

                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró la pregunta especificada");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la pregunta: ".concat(e.getMessage()) );
        }
    }
}
