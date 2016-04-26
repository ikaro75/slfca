/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.comunidad;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;

/**
 *
 * @author Daniel
 */
public class Publicacion {
    
    private Integer clavePublicacion;
    private Integer clavePublicacionPadre;
    private Integer claveEmpleado;
    private String publicacion;
    private Date fecha;
    private Integer claveEstatus;
    private ArrayList<Publicacion> publicacionesHijas;
    public Integer getClavePublicacion() {
        return clavePublicacion;
    }

    public void setClavePublicacion(Integer clavePublicacion) {
        this.clavePublicacion = clavePublicacion;
    }

    public Integer getClavePublicacionPadre() {
        return clavePublicacionPadre;
    }

    public void setClavePublicacionPadre(Integer clavePublicacionPadre) {
        this.clavePublicacionPadre = clavePublicacionPadre;
    }

    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    public String getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(String publicacion) {
        this.publicacion = publicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getClaveEstatus() {
        return claveEstatus;
    }

    public void setClaveEstatus(Integer claveEstatus) {
        this.claveEstatus = claveEstatus;
    }
    
    
    public ArrayList<Publicacion> getPublicacionesHijo () throws Fallo {
        Conexion oDb = new Conexion();
        ResultSet rs = null;
        try {
            rs = oDb.getRs("SELECT * FROM cmnd_ctvd_publicacion WHERE clave_publicacion_padre=".concat(this.clavePublicacion.toString()));
            while (rs.next()) {
                Publicacion p = new Publicacion((Integer) rs.getInt("clave_publicacion"), (Integer) rs.getInt("clave_empleado"), rs.getString("publicacion"), rs.getDate("fecha"), (Integer) rs.getInt("clave_estatus"));
                this.publicacionesHijas.add(p);
            }
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            oDb.cierraConexion();
            return this.publicacionesHijas;
        }
        
    }    

    public Publicacion(Integer clavePublicacion, Integer claveEmpleado, String publicacion, Date fecha, Integer claveEstatus) {
        this.clavePublicacion = clavePublicacion;
        this.claveEmpleado = claveEmpleado;
        this.publicacion = publicacion;
        this.fecha = fecha;
        this.claveEstatus = claveEstatus;
    }
    
    public Publicacion (Integer clavePublicacion) throws Fallo {
         Conexion oDb = new Conexion();
         ResultSet rs = null;
         try {
             rs = oDb.getRs("SELECT * FROM cmnd_ctvd_publicacion WHERE clave_publicacion=".concat(this.clavePublicacion.toString()));
             if (rs.next()) {
                 this.clavePublicacion= rs.getInt("clave_publicacion");
                 this.publicacion = rs.getString("publicacion");
                 this.claveEmpleado = rs.getInt("clave_empleado");
                 this.claveEstatus = rs.getInt("clave_estatus");
                 this.clavePublicacionPadre = rs.getInt("clave_publicacion_padre");
                 this.fecha = rs.getDate("fecha");
             } else {
                 throw new Fallo("No se encontró la publicación señalada, verifique");
             }
         } catch (Exception e) {
             throw new Fallo(e.getMessage());
         } finally {
             oDb.cierraConexion();
         }
        
    
    }
    
}
