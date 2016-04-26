/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.controlador; 

import mx.org.fide.configuracion.Configuracion;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 *
 * @author Daniel
 */
public class Sesion implements HttpSessionBindingListener {
    private Configuracion configuracion = null;
    Integer usuariosConectados = new Integer(0);
    HashMap <Integer,Usuario> usuarios = new HashMap <Integer,Usuario>();

    public Sesion() throws Fallo {
        try {
            this.configuracion= new Configuracion();  
        } catch(Exception ex) {
            throw new Fallo(ex.getMessage());
        }    
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }

    public Integer getUsuariosConectados() {
        return usuariosConectados;
    }

    public void setUsuariosConectados(Integer usuariosConectados) {
        this.usuariosConectados = usuariosConectados;
    }

    public HashMap<Integer, Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(HashMap<Integer, Usuario> usuarios) {
        this.usuarios = usuarios;
    }
  
    public void valueBound(HttpSessionBindingEvent arg0) {
       System.out.println("Usuario añadido a la sesión");
        ServletContext contexto = arg0.getSession().getServletContext();
        Sesion temp = (Sesion) contexto.getAttribute("sesion");
        synchronized (contexto) {
            //this.usuariosConectados = temp.usuariosConectados++;
            contexto.setAttribute("sesion", this);
        }
    }

    public void valueUnbound(HttpSessionBindingEvent arg0) {
       System.out.println("Usuario eliminado de sesión");
        ServletContext contexto = arg0.getSession().getServletContext();
        synchronized (contexto) {
            Sesion temp = (Sesion) contexto.getAttribute("sesion");
            
            this.usuariosConectados = temp.usuariosConectados--;
            contexto.setAttribute("sesion", this);
        }
    }
}
