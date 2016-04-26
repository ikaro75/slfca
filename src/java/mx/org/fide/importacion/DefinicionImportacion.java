
package mx.org.fide.importacion;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 *
 * @author daniel.martinez
 */

public class DefinicionImportacion  {
    private Integer claveDefinicion;
    private Integer claveTipoImportacion;
    private String importacion;
    private Date fechaCreacion;
    private Integer claveEmpleado;
    private Integer claveTipoArchivo;
    private String caracterSeparador;
    private String caracterNuevaLinea;
    private ArrayList<DefinicionImportacionDetalle> definicionImportacionDetalles = new ArrayList<DefinicionImportacionDetalle>();
            
    public DefinicionImportacion() {
    }
    
    public DefinicionImportacion(Integer claveDefinicion, Usuario usuario) throws Fallo {
              
      Conexion cx = new Conexion(usuario.getCx().getServer(),usuario.getCx().getDb(),usuario.getCx().getUser(),usuario.getCx().getPw(),usuario.getCx().getDbType());
      ResultSet rs = cx.getRs("SELECT * FROM fide_definicion_importacion WHERE clave_definicion=".concat(claveDefinicion.toString()));
      ResultSet rsDefImp;
      
      try {
      
      if (rs.next()) {
          this.claveDefinicion = rs.getInt("clave_definicion");
          this.claveTipoImportacion = rs.getInt("clave_tipo_importacion");
          this.importacion = rs.getString("importacion");
          this.fechaCreacion = rs.getDate("fecha_creacion");
          this.claveEmpleado = rs.getInt("clave_empleado");
          this.claveTipoArchivo = rs.getInt("clave_tipo_archivo");
          this.caracterSeparador = rs.getString("caracter_separador");
          this.caracterNuevaLinea = rs.getString("caracter_nueva_linea");
          
          //Recupera el detalle
          rs = cx.getRs("select * from fide_definicion_importacion_detalle WHERE clave_definicion=".concat(claveDefinicion.toString()));
          while (rs.next()) {
            DefinicionImportacionDetalle definicionImportacionDetalle = new DefinicionImportacionDetalle();
            definicionImportacionDetalle.setClaveDefinicionDetalle(rs.getInt("clave_definicion_detalle"));
            definicionImportacionDetalle.setClaveDefinicion(rs.getInt("clave_definicion"));
            definicionImportacionDetalle.setColumna(rs.getInt("columna"));
            definicionImportacionDetalle.setClaveCampoDefinicion(rs.getInt("clave_campo_definicion"));
            definicionImportacionDetalle.setValorPredefinido(rs.getString("valor_predefinido"));
            
            rsDefImp = cx.getRs("SELECT * FROM fide_definicion_importacion_campo WHERE clave_campo_definicion=".concat(rs.getString("clave_campo_definicion")));
            if (rsDefImp.next()) {
                String campo = rsDefImp.getString("campo");
                String tabla = campo.split("\\.")[0];
                campo = campo.split("\\.")[1];
                definicionImportacionDetalle.setCampo(rsDefImp.getString("campo"));
                definicionImportacionDetalle.setOpcional(rsDefImp.getBoolean("opcional"));
                rsDefImp = cx.getRs("SELECT TOP 1 ".concat(campo).concat(" FROM ").concat(tabla));
                definicionImportacionDetalle.setTipoDato(rsDefImp.getMetaData().getColumnTypeName(1));
            } else {
                 throw new Fallo ("No se encontró la definición del campo de importación ".concat(rs.getString("clave_campo_definicion")));
            }
            definicionImportacionDetalles.add(definicionImportacionDetalle);
          }
      } else {
          throw new Fallo ("No se encontró la definición de importación ".concat(claveDefinicion.toString()));
      }
      } catch(Exception ex) {
          throw new Fallo (ex.getMessage());
      } finally {
          cx.cierraConexion();
      }
      
      
    }
    
    public DefinicionImportacion(Integer claveDefinicion) {
        this.claveDefinicion = claveDefinicion;
    }

    public DefinicionImportacion(Integer claveDefinicion, Integer claveTipoImportacion, String importacion, Date fechaCreacion, Integer claveEmpleado) {
        this.claveDefinicion = claveDefinicion;
        this.claveTipoImportacion = claveTipoImportacion;
        this.importacion = importacion;
        this.fechaCreacion = fechaCreacion;
        this.claveEmpleado = claveEmpleado;
    }

    public Integer getClaveDefinicion() {
        return claveDefinicion;
    }

    public void setClaveDefinicion(Integer claveDefinicion) {
        this.claveDefinicion = claveDefinicion;
    }

    public Integer getClaveTipoImportacion() {
        return claveTipoImportacion;
    }

    public void setClaveTipoImportacion(Integer claveTipoImportacion) {
        this.claveTipoImportacion = claveTipoImportacion;
    }

    public String getImportacion() {
        return importacion;
    }

    public void setImportacion(String importacion) {
        this.importacion = importacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    public Integer getClaveTipoArchivo() {
        return claveTipoArchivo;
    }

    public void setClaveTipoArchivo(Integer claveTipoArchivo) {
        this.claveTipoArchivo = claveTipoArchivo;
    }

    public String getCaracterSeparador() {
        return caracterSeparador;
    }

    public void setCaracterSeparador(String caracterSeparador) {
        this.caracterSeparador = caracterSeparador;
    }

    public String getCaracterNuevaLinea() {
        return caracterNuevaLinea;
    }

    public void setCaracterNuevaLinea(String caracterNuevaLinea) {
        this.caracterNuevaLinea = caracterNuevaLinea;
    }

    public ArrayList<DefinicionImportacionDetalle> getDefinicionImportacionDetalles() {
        return definicionImportacionDetalles;
    }

    public void setDefinicionImportacionDetalles(ArrayList<DefinicionImportacionDetalle> definicionImportacionDetalles) {
        this.definicionImportacionDetalles = definicionImportacionDetalles;
    }

    
    
}
