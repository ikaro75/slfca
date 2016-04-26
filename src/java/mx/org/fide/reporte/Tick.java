/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.reporte;

/**
 * Contiene las etiquetas de las series de datos del reporte
 */
public class Tick {
    String tick;
    String simbolo;
    
    /**
     * Recupera la etiqueta de la serie de datos
     * @return
     */
    public String getTick() {
        return tick;
    }

    /**
     * Establece la etiqueta de la serie de datos
     * @param tick Etiqueta de la serie de datos
     */
    public void setTick(String tick) {
        this.tick = tick;
    }

    /**
     * Recupera la abreviación de la etiqueta de la serie de datos
     * @return Abreviación de la etiqueta de la serie de datos
     */
    public String getSimbolo() {
        return simbolo;
    }

    /**
     * Recupera la abreviación de la etiqueta de la serie de datos
     * @param simbolo Abreviación de la etiqueta de la serie de datos
     */
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
   
    /**
     * Constructor
     * @param tick Etiqueta de la serie de datos
     */
    public Tick(String tick) {
        this.tick = tick;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tick)) {
           return false; 
        }
        
        Tick temp=(Tick)o;
        return this.tick.equals(temp.tick);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.tick != null ? this.tick.hashCode() : 0);
        return hash;
    }
    
    
}

