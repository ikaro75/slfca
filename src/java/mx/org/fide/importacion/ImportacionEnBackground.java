package mx.org.fide.importacion;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.org.fide.modelo.Fallo;

/**
 *
 * @author daniel.martinez
 */
public class ImportacionEnBackground extends Thread {

    private Importacion importacion = null;
    private String error = "";
    
    public ImportacionEnBackground() {
    }

    public Importacion getImportacion() {
        return importacion;
    }

    public void setImportacion(Importacion importacion) {
        this.importacion = importacion;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
        
    
    public ImportacionEnBackground(Importacion importacion) {
        this.importacion = importacion;
    }

    @Override
    public void run()  {
        try {
            while (true) {
                if (this.importacion!=null)  {
                    if (this.importacion.getClaveEstatus()==1 || this.importacion.getClaveEstatus()==2) {
                        this.importacion.setClaveEstatus(2);
                        this.importacion.update();
                        
                        if (!this.importacion.getError().equals("")) {
                            this.error =this.importacion.getError();
                            this.importacion=null;
                        }
                        
                    } else if (this.importacion.getClaveEstatus()==3) { //Ya terminó
                        this.importacion=null;
                    }
                } else {
                    Thread.sleep(1000);
                }    
            }
        } catch (Fallo ex) {
            //Limpia el objeto importa
            this.error =this.importacion.getError();
        } catch (InterruptedException ex) {
            this.error =this.importacion.getError();
        }
    }
}
