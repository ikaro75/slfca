/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.backend;

/**
 * Contiene la estructura que define a cada registro de la configuración global del sistema
 */
public class Configuracion {
    private Integer claveParametro;
    private Integer claveAplicacion;
    private Integer claveEmpleado;
    private Integer parametro;
    private Integer valor;

    /**
     * Recupera la clave de la aplicación del registro de configuración
     * @return Clave de la aplicación del parámetro de configuración
     */
    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    /**
     * Establece clave de la aplicación del parámetro de configuración
     * @param claveAplicacion   Clave de la aplicación
     */
    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    /**
     * Recupera clave de empleado 
     * @return
     */
    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    /**
     * Establece clave de empleado en el parámetro de configuración
     * @param claveEmpleado Clave del empleado
     */
    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    /**
     * Recupera clave del parametro 
     * @return  Llave primaria del parámetro
     */
    public Integer getClaveParametro() {
        return claveParametro;
    }

    /**
     * Establece Llave primaria del parámetro
     * @param claveParametro    Llave primaria del parámetro
     */
    public void setClaveParametro(Integer claveParametro) {
        this.claveParametro = claveParametro;
    }

    /**
     * Recupera parámetro
     * @return Nombre del parámetro
     */
    public Integer getParametro() {
        return parametro;
    }

    /**
     * Establece nombre a parámetro
     * @param parametro Nombre del parámetro
     */
    public void setParametro(Integer parametro) {
        this.parametro = parametro;
    }

    /**
     * Recupera valor del parametro
     * @return valor del parámetro
     */
    public Integer getValor() {
        return valor;
    }

    /**
     *  Establece valor a parámetro
     * @param valor Valor del parámetro
     */
    public void setValor(Integer valor) {
        this.valor = valor;
    }

    
}
