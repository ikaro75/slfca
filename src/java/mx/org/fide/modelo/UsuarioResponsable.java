/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.modelo;

/**
 *
 * @author Daniel
 */
public class UsuarioResponsable extends Usuario{
    String flujo_dato;
    String proceso;
    String asunto;
    int secuencia;
    String notificacion;
    String campo_seguimiento_estatus;

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCampo_seguimiento_estatus() {
        return campo_seguimiento_estatus;
    }

    public void setCampo_seguimiento_estatus(String campo_seguimiento_estatus) {
        this.campo_seguimiento_estatus = campo_seguimiento_estatus;
    }

    public String getFlujo_dato() {
        return flujo_dato;
    }

    public void setFlujo_dato(String flujo_dato) {
        this.flujo_dato = flujo_dato;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }
    
     public Integer getClave() {
        return this.getClave();
    }

    public UsuarioResponsable(String email, String nombre, String apellido_paterno, String apellido_materno, String flujo_dato, String proceso, String asunto, int secuencia, String notificacion, String campo_seguimiento_estatus) {
        this.setEmail(email);
        this.setNombre(nombre);
        this.setApellido_paterno(apellido_paterno);
        this.setApellido_materno(apellido_materno);
        this.flujo_dato = flujo_dato;
        this.proceso = proceso;
        this.asunto = asunto;
        this.secuencia = secuencia;
        this.notificacion = notificacion;
        this.campo_seguimiento_estatus = campo_seguimiento_estatus;
    }
    

}
