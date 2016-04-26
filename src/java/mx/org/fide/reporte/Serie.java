/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.reporte;
import java.util.ArrayList;

/**
 * Contiene las series de datos del reporte
 */
public class Serie {
    String etiqueta;
    String color;
    ArrayList <Float> datos = new ArrayList <Float>() ;

    /**
     * Recupera los datos de las series
     * @return Objeto <code>ArrayList<Float></code> con los datos de las series
     */
    public ArrayList<Float> getDatos() {
        return datos;
    }

    /**
     * Establece los datos de las series
     * @param datos Objeto <code>ArrayList<Float></code> con los datos de las series
     */
    public void setDatos(ArrayList<Float> datos) {
        this.datos = datos;
    }

    /**
     * Establece la etiqueta de la serie
     * @return Etiqueta de la serie
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Recupera la etiqueta de la serie  
     * @param etiqueta Etiqueta de la serie
     */
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Recupera cadena con colores en formato RGB separados por comas para cada serie
     * @return cadena con colores en formato RGB separados por comas para cada serie
     */
    public String getColor() {
        return color;
    }

    /**
     * Establece cadena con colores en formato RGB separados por comas para cada serie
     * @param color cadena con colores en formato RGB separados por comas para cada serie
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Constructor
     * @param etiqueta Etiqueta de la serie
     */
    public Serie(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Genera una cadena con las series concatenadas y separadas por una coma
     * @return Cadena con las series concatenadas y separadas por una coma
     */
    public String imprimeDatos() {
        StringBuilder sb = new StringBuilder();
        for (Float f: this.datos) {
            sb.append(f).append(",");
        }
        
        return sb.deleteCharAt(sb.length()-1).toString();        
    }
        
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Serie)) {
           return false; 
        }
        
        Serie temp=(Serie)o;
        return this.etiqueta.equals(temp.etiqueta);
       
    }
}
