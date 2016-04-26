/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.comunidad;

import java.sql.ResultSet;
import java.util.ArrayList;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;

/**
 * Clasepara crar grupos de contactos.
 * @author Daniel
 */
public class Grupo {
    private Integer claveGrupo;
    private String grupo;
    private Integer claveEmpleado;
    private ArrayList<Contacto> contactos = new ArrayList<Contacto>();
    
    /**
     * Recupera el grupo.
     * @return Clave del grupo.
     */
    public Integer getClaveGrupo() {
        return claveGrupo;
    }

    /**
     * Establece el grupo de contactos.
     * @param claveGrupo Clave del grupo.
     */
    public void setClaveGrupo(Integer claveGrupo) {
        this.claveGrupo = claveGrupo;
    }

    /**
     * Recupera el nombre del grupo de contactos.
     * @return Nombre del grupo de contactos.
     */
    public String getGrupo() {
        return grupo;
    }

    /**
     * Establece el nombre del grupo de contactos.
     * @param grupo Nombre del grupo de contactos.
     */
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    /**
     * Recupera el empleado al que pernece el grupo.
     * @return Clave del empleado.
     */
    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    /**
     * Establece el empleado que creara el grupo.
     * @param claveEmpleado Clave del empleado.
     */
    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

   /**
     * Recupera la lista de contactos del grupo.
     * @return Lista de contactos.
     */
    public ArrayList<Contacto> getContactos() {
        return contactos;
    }

    /**
     * Establece la lista de contactos.
     * @param contactos Lista de contactos.
     */
    public void setContactos(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
    }
   
    /**
     * Recupera los contactos del grupo en la lista desde la base de datos.
     * @param cx Conexión.
     * @return Lista de contactos.
     */
    public ArrayList<Contacto> getContactos(Conexion cx) {
       Conexion oDb= null;

       try {
           oDb= new Conexion();

           this.contactos.clear();
           ResultSet rs = null;
           rs = oDb.getRs("select * from cmnd_grp_contacto_grupo where clave_grupo=".concat(this.claveGrupo.toString()) );
           while (rs.next()) {
               Contacto c = new Contacto(rs.getInt("clave_empleado_contacto"), cx);
               this.contactos.add(c);
           }
           
       } catch(Exception e) {
           throw new Fallo("Error al recuperar los contactos del grupo especificado: ".concat(e.getMessage()));
       } finally {
           oDb.cierraConexion();
           return this.contactos;
       }

    }

    /**
    * Constructor que recupera los grupos de contactos de la base de datos.
    * @param claveGrupo Clave del grupo.
    * @param cx Conexión a la base de datos.
    */
    public Grupo(Integer claveGrupo, Conexion cx) {
        Conexion oDb= cx;
        ResultSet rs = null;
        try {
            rs = oDb.getRs("select * from cmnd_grp_grupo where clave_grupo=".concat(claveGrupo.toString()));
            
            if (rs.next()) {
                this.claveGrupo = rs.getInt("clave_grupo");
                this.grupo = rs.getString("grupo");
                this.claveEmpleado=rs.getInt("clave_empleado");
            } else {
                throw new Fallo("Error al recuperar el grupo especificado");
            }
        } catch(Exception e) {
            
        } finally {
            oDb.cierraConexion();
        }
    }
   
   
}
