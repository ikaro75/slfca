/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.comunidad;

import java.sql.ResultSet;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 * Clase contacto que se encarga de crear los contactos.
 * Se extiende de la clase Usuario.
 * @author Daniel
 */

public class Contacto extends Usuario {
    private Integer claveEmpleadoContacto;
    private Integer claveEmpleado;
    private Integer claveContacto;

    /**
     * Constructor sin parametros.
     */
    public Contacto() {
    }
            
    
    /**
     * Constructor de la clase con parametros.
     * @param claveEmpleadoContacto Clave del empleado contacto.
     * @param cx Conexion a la base de datos.
     * @throws Fallo 
     */
    public Contacto(Integer claveEmpleadoContacto, Conexion cx) throws Fallo {
        Conexion oDb = null;
       
        ResultSet rs = null;
        try {
            oDb = new Conexion();
            //Se recupera la clave de empleado del contacto
            rs = oDb.getRs("select *  from cmnd_cntc_contacto where clave_empleado_contacto=".concat(claveEmpleadoContacto.toString()));
            if (rs.next()) {
                this.claveEmpleadoContacto = rs.getInt("clave_empleado_contacto");
                this.claveEmpleado =rs.getInt("clave_empleado");
                this.claveContacto = rs.getInt("clave_contacto");
                
                
            } else {
                throw new Fallo("Error al recuperar datos del contacto");
            }    
            super.setCx(cx);
            super.setClave(this.claveContacto);

        } catch(Exception e) {
             throw new Fallo(e.getMessage());
        } finally {
            rs = null;
            oDb.cierraConexion();
        }
    }
    
}