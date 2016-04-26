package mx.org.fide.encuesta;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import java.sql.ResultSet;

public class PreguntaParticipante extends Consulta {
    private Integer clavePreguntaParticipante;
    private Integer claveCuestionarioParticipante;
    private Integer clavePregunta;
    private Integer claveEstatusPregunta;
    private Integer claveEmpleadoParticipante;

    public Integer getClavePreguntaParticipante() {
        return clavePreguntaParticipante;
    }

    public void setClavePreguntaParticipante(Integer clavePreguntaParticipante) {
        this.clavePreguntaParticipante = clavePreguntaParticipante;
    }

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

    public Integer getClaveEstatusPregunta() {
        return claveEstatusPregunta;
    }

    public void setClaveEstatusPregunta(Integer claveEstatusPregunta) {
        this.claveEstatusPregunta = claveEstatusPregunta;
    }

    public Integer getClaveEmpleadoParticipante() {
        return claveEmpleadoParticipante;
    }

    public void setClaveEmpleadoParticipante(Integer claveEmpleadoParticipante) {
        this.claveEmpleadoParticipante = claveEmpleadoParticipante;
    }
    
    public PreguntaParticipante (Integer clavePreguntaParticipante, Conexion cx) throws Fallo {
        /* super.setTabla("ex_pregunta");
        super.setLlavePrimaria("clavePregunta");
        super.setPk(String.valueOf(clavePregunta)); */

        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        try {
            rs = oDb.getRs("select * from fide_pregunta_participante where clavePreguntaParticipante=".concat(String.valueOf(clavePreguntaParticipante)));

            if (rs.next()) {

                this.clavePreguntaParticipante = rs.getInt("clavePreguntaParticipante");
                this.claveCuestionarioParticipante =  rs.getInt("clave_respuesta_participante");
                this.clavePregunta = rs.getInt("clavePregunta");
                this.claveEstatusPregunta = rs.getInt("claveEstatusPregunta");
                this.claveEmpleadoParticipante = rs.getInt("clave_empleado_participante");                     

                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró la pregunta relacionada al participante especificada");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la pregunta relacionada al participante especificada: ".concat(e.getMessage()) );
        }
    }
    
    public PreguntaParticipante (Integer claveCuestionarioParticipante, Integer clavePregunta, Conexion cx) throws Fallo {
        /* super.setTabla("ex_pregunta");
        super.setLlavePrimaria("clavePregunta");
        super.setPk(String.valueOf(clavePregunta)); */

        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        try {
            rs = oDb.getRs("select * from fide_pregunta_participante where clave_respuesta_participante=".concat(claveCuestionarioParticipante.toString()).concat(" AND clavePregunta=").concat(clavePregunta.toString()));

            if (rs.next()) {
                this.clavePreguntaParticipante = rs.getInt("clavePreguntaParticipante");
                this.claveCuestionarioParticipante =  rs.getInt("clave_respuesta_participante");
                this.clavePregunta = rs.getInt("clavePregunta");
                this.claveEstatusPregunta = rs.getInt("claveEstatusPregunta");
                this.claveEmpleadoParticipante = rs.getInt("clave_empleado_participante");          
            } else {

                throw new Fallo("No se encontró la pregunta relacionada al participante especificada");
            }
        } catch (Exception e) {

            throw new Fallo("Error al recuperar la pregunta relacionada al participante especificada: ".concat(e.getMessage()) );
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;            
        }
    }    
    
}
