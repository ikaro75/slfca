/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.modelo;

import mx.org.fide.agenda.Actividad;
import mx.org.fide.backend.Aplicacion;
import mx.org.fide.backend.Forma;
import mx.org.fide.backend.Nota;
import mx.org.fide.comunidad.ActividadContacto;
import mx.org.fide.comunidad.ArchivoContacto;

/**
 * Se encarga de filtrar las operaciones de base de datos a manera de trigger.
 * Al ejecutarse una operación INSERT, UPDATE, DELETE o DUPLICATE
 * la clase verifica la tabla sobre la que se ejecutará la operación y redirecciona
 * tal operación a la clase correspondiente, en caso de que no se haya relacionado
 * una tabla con una clase, el disparador ejecutará las sentencias SQL utilizando 
 * la clase Consulta
 * @author Daniel
 */
public class Disparador {

    private Consulta consulta;

    /**
     * Constructor
     * @param consulta Objeto <code>com.administrax.modelo.Consulta</code> 
     */
    public Disparador(Consulta consulta) {
        this.consulta = consulta;
    }

    /**
     * Se encarga de verificar si hay una relación entre la tabla sobre a que se ejecuta el INSERT y algún objeto,
     * en caso de haberla se carga el objeto y se invoca el método correspondiente a la operación.
     * @param claveEmpleado     Clave del empleado que ejecuta la operación INSERT
     * @param ip                Dirección IP del empleado que ejecuta la operación INSERT
     * @param browser           Navegador con el que se ejecuta la operación INSERT
     * @param forma             Clave de la forma asociada a la consulta
     * @param cx                Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @param sessionTimeStamp  Tiempo y hora de la sesión del usuario que ejecuta la operación
     * @return                  Código XML con sentencias ejecutadas en la operación, si ocurren error se incluyen
     * @throws Fallo            si ocurre un error relacionado a la base de datos
     */
    public String insert() throws Fallo {

        if (this.consulta.getTabla().equals("be_aplicacion")) {
            Aplicacion a = new Aplicacion(this.consulta);
            return a.insert();
        }  else if (this.consulta.getTabla().equals("be_forma")) {
            Forma f = new Forma(this.consulta, true);
            return f.insert();
        } else if (this.consulta.getTabla().equals("cmnd_dcm_archivo_contacto")) {
            ArchivoContacto ac = new ArchivoContacto(this.consulta);
            return ac.insert();
        } else if (this.consulta.getTabla().equals("be_actividad_contacto")) {
            ActividadContacto ac = new ActividadContacto(this.consulta);
            return ac.insert();
        } else if (this.consulta.getTabla().equals("be_nota_forma")) {
            Nota n = new Nota(this.consulta);
            
            Usuario destinatario = new Usuario();
            destinatario.setCx(this.consulta.getUsuario().getCx());
            destinatario.setClave(n.getClaveEmpleado());
            String entidad="";
            if (n.getClaveForma()==101) {
                Actividad a = new Actividad(n.getClaveRegistro(),this.consulta.getUsuario());
                entidad = "la actividad ".concat(a.getActividad());
            }    
            
            return n.insert(destinatario, entidad, true);    
        }else {
            return this.consulta.insert(true);
        }

    }

    /**
     * Se encarga de verificar si hay una relación entre la tabla sobre a que se ejecuta el UPDATE y algún objeto,
     * en caso de haberla se carga el objeto y se invoca el método correspondiente a la operación.
     * @param claveEmpleado     Clave del empleado que ejecuta la operación UPDATE
     * @param ip                Dirección IP del empleado que ejecuta la operación UPDATE
     * @param browser           Navegador con el que se ejecuta la operación UPDATE
     * @param forma             Clave de la forma asociada a la consulta
     * @param cx                Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @param sessionTimeStamp  Tiempo y hora de la sesión del usuario que ejecuta la operación
     * @return                  Código XML con sentencias ejecutadas en la operación, si ocurren error se incluyen
     * @throws Fallo            si ocurre un error relacionado a la base de datos
     */
    public String update() throws Fallo {
         if (this.consulta.getTabla().equals("be_aplicacion")) {
            Aplicacion a = new Aplicacion(this.consulta);
            return a.update();
        }  else if (this.consulta.getTabla().equals("be_forma")) {
            Forma f = new Forma(this.consulta,true);
            return f.update();
        } else if (this.consulta.getTabla().equals("be_actividad")) {
            Actividad a = new Actividad(this.consulta);
            return a.update();
            
        } else {
            return this.consulta.update(true);
        }
    }

    /**
     * Se encarga de verificar si hay una relación entre la tabla sobre a que se ejecuta el DELETE y algún objeto,
     * en caso de haberla se carga el objeto y se invoca el método correspondiente a la operación.
     * @param claveEmpleado     Clave del empleado que ejecuta la operación DELETE
     * @param ip                Dirección IP del empleado que ejecuta la operación DELETE
     * @param browser           Navegador con el que se ejecuta la operación DELETE
     * @param forma             Clave de la forma asociada a la consulta
     * @param cx                Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @return                  Código XML con sentencias ejecutadas en la operación, si ocurren error se incluyen
     * @throws Fallo            si ocurre un error relacionado a la base de datos
     */
    public String delete() throws Fallo {
        if (this.consulta.getTabla().equals("be_aplicacion")) {
            Aplicacion a = new Aplicacion();
            a.setClaveAplicacion(Integer.parseInt(this.consulta.getPk()));
            return a.delete(this.consulta.getUsuario());
        } else if (this.consulta.getTabla().equals("be_forma")) {
            Forma f = new Forma(this.consulta,false);
            return f.delete();
        } else {
            return this.consulta.delete(false,this.consulta.getUsuario());
        }
        
    }
    
    /**
     * Se encarga de verificar si hay una relación entre la tabla sobre a que se ejecuta la duplicación y algún objeto,
     * en caso de haberla se carga el objeto y se invoca el método correspondiente a la operación.
     * @param claveEmpleado     Clave del empleado que ejecuta la operación
     * @param ip                Dirección IP del empleado que ejecuta la operación 
     * @param browser           Navegador con el que se ejecuta la operación 
     * @param forma             Clave de la forma asociada a la consulta
     * @param cx                Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @return                  Código XML con sentencias ejecutadas en la operación, si ocurren error se incluyen
     * @throws Fallo            si ocurre un error relacionado a la base de datos
     */
    public String duplicate() throws Fallo {

        return this.consulta.duplicate();

    };    
}
