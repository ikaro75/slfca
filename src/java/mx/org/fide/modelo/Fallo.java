package mx.org.fide.modelo;

/**
 * Se encarga de reportar errores en la aplicación
 */
public class Fallo extends Exception {
    Fallo(){}
    
    /**
     * Constructor
     * @param mensaje   mensaje de error presentado
     */
    public Fallo(String mensaje){
        super(mensaje);
    }
}
