/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.jasper;
/**
 * Parámetro asociado a un reporte 
 */
public class Parametro {
        
        String parametro;
        String tipoDato;
        String valor;

     /**
     * Recupera parámetro
     * @return Parámetro
     */
    public String getParametro() {
            return parametro;
        }

     /**
     * Establece parámetro
     * @param parametro Parámetro
     */
    public void setParametro(String parametro) {
            this.parametro = parametro;
        }

    /**
     * Recupera tipo de dato del parámetro, los tipos de datos pueden ser cualquiera de los empleados en los campos
     * de la base de datos
     * @return Tipo de dato del parámetro
     */
    public String getTipoDato() {
            return tipoDato;
        }

     /**
     * Establece tipo de datos del parámetro, los tipos de datos pueden ser cualquiera de los empleados en los campos
     * de la base de datos
     * @param tipoDato  Tipo de dato del parámetro
     */
    public void setTipoDato(String tipoDato) {
            this.tipoDato = tipoDato;
        }

    /**
     * Recupera valor del parámetro
     * @return Valor del parametro 
     */
    public String getValor() {
            return valor;
        }

     /**
     * Establece valor del parámetro
     * @param valor Valor del parametro 
     */
    public void setValor(String valor) {
            this.valor = valor;
        }

    /**
     * Constructor del parámetro con parámetros
     * @param parametro Descripción del parámetro
     * @param tipoDato  Tipo de dato del parámetro
     * @param valor     Valor del parámetro
     */
    public Parametro(String parametro, String tipoDato, String valor) {
            this.parametro = parametro;
            this.tipoDato = tipoDato;
            this.valor = valor;
        }
        
}
