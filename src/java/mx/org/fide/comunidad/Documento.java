
package mx.org.fide.comunidad;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 * Clase para compartir documentos dentro de la comunidad.
 * @author Daniel
 */
public class Documento {
    private Integer claveArchivo;
    private String descripcion;
    private String archivo;
    private Integer claveCategoria;
    private String palabrasClave;
    private Date fecha;
    private Integer claveEmpleado;
    private Integer claveForma;
    private Integer claveRegistro;

    /**
     * Recupera la clave del archivo que se compartirá.
     * @return  Clave del archivo.
     */
    public Integer getClaveArchivo() {
        return claveArchivo;
    }

    /**
     * Establece la clave del archivo a compartir.
     * @param claveArchivo Clave del archivo.
     */
    public void setClaveArchivo(Integer claveArchivo) {
        this.claveArchivo = claveArchivo;
    }

    /**
     * Recupera la descripción del archivo.
     * @return Descripcion del archivo
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del archivo.
     * @param descripcion Descripcion del archivo
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Recupera la ruta archivo.
     * @return Ruta del archivo.
     */
    public String getArchivo() {
        return archivo;
    }

    /**
     * Establece la ruta del archivo.
     * @param archivo Ruta del archivo.
     */
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    /**
     * Recupera la categoria del archivo.
     * @return Clave de la categoria.
     */
    public Integer getClaveCategoria() {
        return claveCategoria;
    }

   /**
     * Establece la categoria del archivo.
     * @param claveCategoria Clave de la categoria.
     */
    public void setClaveCategoria(Integer claveCategoria) {
        this.claveCategoria = claveCategoria;
    }

    
    /**
     * Recupera las palabras clave.
     * @return Palabras clave.
     */
    public String getPalabrasClave() {
        return palabrasClave;
    }

    /**
     * Establece las palabras clave.
     * @param palabrasClave Palabras clave.
     */
    public void setPalabrasClave(String palabrasClave) {
        this.palabrasClave = palabrasClave;
    }

    /**
     * Recupera la fecha del archivo.
     * @return Fecha del archivo.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece fecha del archivo.
     * @param fecha Fecha del archivo.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Recupera al empleado que compartió el documento.
     * @return Clave del empleado.
     */
    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    /**
     * Establece el empleado que compartirá el documento.
     * @param claveEmpleado Clave del empleado.
     */
    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    /**
     * Recupera la clave de la forma relacionada con el documento.
     * @return Clave forma.
     */
    public Integer getClaveForma() {
        return claveForma;
    }

    /**
     * Establece la clave de la forma relacionada con el documento.
     * @param claveForma Clave forma.
     */
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Recupera el registro.
     * @return Clave del registro.
     */
    public Integer getClaveRegistro() {
        return claveRegistro;
    }

    /**
     * Establece el registro.
     * @param claveRegistro Clave del registro.
     */
    public void setClaveRegistro(Integer claveRegistro) {
        this.claveRegistro = claveRegistro;
    }
    
    /**
     * En desarrollo
     * @param claveCarpeta
     * @throws Fallo
     */
    public void mueveACarpeta(String claveCarpeta) throws Fallo {
        try {
                /*File archivoOrigen = new File("c:\\intranet2.0\\".concat(this.claveEmpleado.toString()).concat("\\").concat(this.archivo));
                String sAppPath = application.getRealPath("/").replace("\\build\\web", "");
                File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(n.getClaveEmpleado())).concat("\\"));
                if (!forderDestino.exists()) {
                    forderDestino.mkdir();
                }

                File archivoDestino = new File(forderDestino.getAbsolutePath().concat("\\").concat(archivoOrigen.getName()));

                if (!archivoDestino.exists()) {
                    archivoDestino.createNewFile();

                    if (!archivoOrigen.exists()) {
                        foto = "Error - No se encontró el archivo ".concat(n.getFoto());
                    } else {
                        try {

                            FileChannel origen = null;
                            FileChannel destino = null;
                            origen = new FileInputStream(archivoOrigen).getChannel();
                            destino = new FileOutputStream(archivoDestino).getChannel();

                            if (destino != null && origen != null) {
                                destino.transferFrom(origen, 0, origen.size());
                            }
                            if (origen != null) {
                                origen.close();
                            }
                            if (destino != null) {
                                destino.close();
                            }
                        } catch (Exception e) {
                            foto = "Error - No fue posible escribir el archivo ".concat(n.getFoto()).concat(" en carpeta web: ").concat(e.getMessage());
                        }

                        foto = request.getContextPath().concat("/temp/").concat(String.valueOf(n.getClaveEmpleado())).concat("/").concat(n.getFoto());

                    }
                } else {
                    foto = request.getContextPath().concat("/temp/").concat(String.valueOf(n.getClaveEmpleado())).concat("/").concat(n.getFoto());
                }*/
            } catch (Exception e) {
                throw new Fallo(e.getMessage());
            }
    }
    /**
     * Constructor de la clase documento.
     * @param claveArchivo Clave del documento.
     * @param cx Conexión a la base de datos.
     */
    public Documento(Integer claveArchivo, Conexion cx) {

        this.claveArchivo = claveArchivo;
        try {
           ResultSet rs = cx.getRs("select * from be_archivo where clave_archivo=".concat(claveArchivo.toString()));
           
           if (rs.next()) {
               this.claveArchivo=rs.getInt("clave_archivo");
               this.descripcion=rs.getString("descripcion");
               this.archivo=rs.getString("archivo");
               this.claveCategoria= rs.getInt("clave_categoria");
               this.palabrasClave=rs.getString("palabras_clave");
               this.fecha= rs.getDate("fecha");
               this.claveEmpleado= rs.getInt("clave_empleado");
               this.claveForma=rs.getInt("clave_forma");
               this.claveRegistro=rs.getInt("clave_registro");                            
                                                       
           } else {
               throw new Fallo ("Error al recuperar archivo especificado");
           }

        } catch(Exception e ){ 
            
        }
    }
    
    
    
    
}
