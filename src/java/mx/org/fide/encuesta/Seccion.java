package mx.org.fide.encuesta;

import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.sql.ResultSet;
import java.util.ArrayList;
/**
 *
 * @author Personal
 */
public class Seccion extends Consulta{
    private Integer claveSeccion;
    private String seccion;
    private Integer claveCuestionario;
    private String instruccion;
    private Integer orden;
    private ArrayList<Pregunta> preguntas ;

    public Integer getClaveCuestionario() {
        return claveCuestionario;
    }

    public void setClaveCuestionario(Integer claveCuestionario) {
        this.claveCuestionario = claveCuestionario;
    }

    public Integer getClaveSeccion() {
        return claveSeccion;
    }

    public void setClaveSeccion(Integer claveSeccion) {
        this.claveSeccion = claveSeccion;
    }

    public String getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(String instruccion) {
        this.instruccion = instruccion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public ArrayList<Pregunta> getPreguntas() throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Pregunta pregunta = null;
        this.preguntas = new  ArrayList<Pregunta>();
        
        try {
            rs = oDb.getRs("select clave_pregunta from fide_pregunta where clave_seccion=".concat(String.valueOf(this.claveSeccion)).concat(" ORDER BY orden"));

            while (rs.next()) {
                pregunta = new Pregunta(rs.getInt("clave_pregunta"), super.getUsuario());
                this.preguntas.add(pregunta);
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar el cuestionario: ".concat(e.getMessage()) );
        }  
        finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;            
        }
        return preguntas;
    }

    public void setPreguntas(ArrayList<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
    
   public Integer getPrimeraPregunta() throws Fallo {
        Integer primeraPregunta;
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select orden, clave_pregunta from fide_pregunta where clave_seccion=".concat(this.claveSeccion.toString()).concat(" order by orden asc limit 0,1"));
            if (!rs.next()) {
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("Error al recuperar la primera pregunta de la sección.");
            } else {
                primeraPregunta = rs.getInt("clave_pregunta");
                rs.close();
                oDb.cierraConexion();
                return primeraPregunta;
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la primera pregunta de la sección: ".concat(e.getMessage()));
        }

    }
   
    public Integer getUltimaPregunta() throws Fallo {
        Integer ultimaPregunta;
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select orden, clave_pregunta from fide_pregunta where clave_seccion=".concat(this.claveSeccion.toString()).concat(" order by orden desc limit 0,1"));
            if (!rs.next()) {
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("Error al recuperar la última pregunta de la sección.");
            } else {
                ultimaPregunta = rs.getInt("clave_pregunta");
                rs.close();
                oDb.cierraConexion();
                return ultimaPregunta;
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la última pregunta de la sección: ".concat(e.getMessage()));
        }

    }   

    public Integer getAnterior() throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer seccionAnterior;
        try {            
            rs = oDb.getRs("select clave_seccion from fide_seccion_pregunta where orden<".concat(this.orden.toString()).concat(" and clave_cuestionario=".concat(claveCuestionario.toString()).concat(" order by orden desc limit 0,1")));

            if (rs.next()) {
                seccionAnterior = rs.getInt("clave_seccion");
                rs.close();
                oDb.cierraConexion();
                return  seccionAnterior;
            } else {
                 throw new Fallo("No hay sección anterior");
            }
        } catch (Exception e) {
                 throw new Fallo(e.getMessage());
        }
    }

    public Integer getSiguiente() throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer seccionSiguiente;
        try {            
            rs = oDb.getRs("select clave_seccion from fide_seccion_pregunta where orden>".concat(orden.toString()).concat(" and clave_cuestionario=".concat(claveCuestionario.toString()).concat(" order by orden limit 0,1")));

            if (rs.next()) {
                seccionSiguiente = rs.getInt("clave_seccion");
                rs.close();
                oDb.cierraConexion();
                return seccionSiguiente;
            } else {
                 throw new Fallo("No hay sección posterior");
            }
        } catch (Exception e) {
                 throw new Fallo(e.getMessage());
        }
    }
    
   public ArrayList<Pregunta>  getPreguntasPendientes() throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        ArrayList<Pregunta> preguntas = new ArrayList<Pregunta>();
        
        try {
            q = new StringBuilder("select clave_pregunta from fide_pregunta where clave_seccion=".concat(this.claveSeccion.toString()).concat(" and clave_pregunta in ( ")
            .concat("select clave_pregunta from fide_pregunta_participante where clave_estatus_pregunta=2)  order by orden asc"));
            rs = oDb.getRs(q.toString());
            while (rs.next()) {
                preguntas.add(new Pregunta(rs.getInt("clave_pregunta"), super.getUsuario()));
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar la primera pregunta del cuestionario: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            return preguntas; 
        }
    }
   
    public Seccion(Integer claveSeccion, Usuario usuario) throws Fallo {
        super.setTabla("fide_seccion_pregunta");
        super.setLlavePrimaria("clave_seccion");
        super.setPk(String.valueOf(claveSeccion)); 
        super.setUsuario(usuario);
                
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_seccion_pregunta where clave_seccion=".concat(String.valueOf(claveSeccion)));

            if (rs.next()) {
                this.claveSeccion = rs.getInt("clave_seccion");
                this.seccion = rs.getString("seccion");
                this.claveCuestionario = rs.getInt("clave_cuestionario");
                this.instruccion = rs.getString("instruccion");
                this.orden = rs.getInt("orden");   

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
            throw new Fallo("Error al recuperar el cuestionario: ".concat(e.getMessage()) );
        }
    }



}
