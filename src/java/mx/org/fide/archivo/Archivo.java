/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.archivo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import mx.org.fide.modelo.Fallo;

/**
 * Clase encargada de operaciones de lectura y escritura de archivos de texto en el servidor
 */

public class Archivo {

    Properties propiedades = new Properties();
    InputStream is = null;

    /**
     * Obtiene las propiedades del archivo de properties
     * @return Objeto de clase <code>java.util.Properties</code> que contiene el contenido del archivo de configuración
     */
    public Properties getPropiedades() {
        return propiedades;
    }

    /**
    * Establece las propiedades del archivo de properties
    * @param propiedades Variable de tipo <code>java.util.Properties</code> que contiene el hashmap con el contenido del archivo
    */
    public void setPropiedades(Properties propiedades) {
        this.propiedades = propiedades;
    }

    /**
     * Lee las propiedades del archivo properties que contiene los parámetros globales de configuración de la aplicación
     * @param archivo   Ruta absoluta del archivo que se va a leer
     * @throws Fallo    si ocurre un problema relacionado a E/S
    */    
    public void lee(String archivo) throws Fallo {
        try {
            propiedades.load(Archivo.class.getResourceAsStream(archivo));
        } catch (IOException ioe) {
                throw new Fallo("Error al leer el archivo ".concat(archivo).concat(".").concat(ioe.getMessage()));
        }

    }
}
