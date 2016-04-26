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

public class Estudio extends Consulta{
    private Integer claveEstudio;
    private String estudio;
    private Integer claveCuestionarioInicial;
    private Integer claveCuestionarioFinalizacion;

    public Integer getClaveCuestionarioFinalizacion() {
        return claveCuestionarioFinalizacion;
    }

    public void setClaveCuestionarioFinalizacion(Integer claveCuestionarioFinalizacion) {
        this.claveCuestionarioFinalizacion = claveCuestionarioFinalizacion;
    }
    
    public Integer getClaveCuestionarioInicial() {
        return claveCuestionarioInicial;
    }

    public void setClaveCuestionarioInicial(Integer claveCuestionarioInicial) {
        this.claveCuestionarioInicial = claveCuestionarioInicial;
    }

    public Integer getClaveEstudio() {
        return claveEstudio;
    }

    public void setClaveEstudio(Integer claveEstudio) {
        this.claveEstudio = claveEstudio;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }
    
   public Estudio(Integer claveEstudio, Usuario usuario) throws Fallo {
        super.setTabla("fide_estudio");
        super.setLlavePrimaria("claveEstudio");
        super.setPk(String.valueOf(claveEstudio));
        super.setUsuario(usuario);
        
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_estudio where claveEstudio=".concat(String.valueOf(claveEstudio)));

            if (rs.next()) {      
                this.claveEstudio = rs.getInt("claveEstudio");
                this.estudio = rs.getString("estudio");
                this.claveCuestionarioInicial = rs.getInt("claveCuestionarioInicial");
                this.claveCuestionarioFinalizacion = rs.getInt("claveCuestionarioFinalizacion");
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
}
