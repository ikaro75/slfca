package mx.org.fide.jasper;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Genera reportes a partir del runtime de Jasper Reports, es necesario crear el
 * reporte en iReport y luego registrarlo en el backend del sistema asignandolo
 * a un perfil de usuarios.
 */
public class ReporteJasper {

    private int claveReporte;
    private int claveForma;
    private int clavePerfil;
    private String reporte;
    private String jsp;
    private String jrxml;
    private boolean generarEnInsercion;
    private boolean generarEnActualizacion;
    private JasperPrint jp = null;
    private ArrayList<Parametro> definicionParametros = new ArrayList<Parametro>();
    private Map parametros = new HashMap();

    /**
     * Recupera la llave primaria del reporte
     *
     * @return Llave primaria del reporte
     */
    public int getClaveReporte() {
        return claveReporte;
    }

    /**
     * Establece la llave primaria del reporte
     *
     * @param claveReporte Llave primaria del reporte
     */
    public void setClaveReporte(int claveReporte) {
        this.claveReporte = claveReporte;
    }

    /**
     * Recupera la clave de la forma asociada
     *
     * @return Clave de la forma asociada al reporte
     */
    public int getClaveForma() {
        return claveForma;
    }

    /**
     * Establece la clave de la forma asociada
     *
     * @param claveForma Clave de la forma asociada al reporte
     */
    public void setClaveForma(int claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Recupera la clave del perfil del usuario asociado al reporte
     *
     * @return Clave del perfil del usuario asociado al reporte
     */
    public int getClavePerfil() {
        return clavePerfil;
    }

    /**
     * Establece la clave del perfil del usuario asociado al reporte
     *
     * @param clavePerfil Clave del perfil del usuario asociado al reporte
     */
    public void setClavePerfil(int clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    /**
     * Recupera objeto
     * <code>JasperPrint</code> que generará reporte
     *
     * @return objeto <code>JasperPrint</code>
     */
    public JasperPrint getPrint() {
        return jp;
    }

    /**
     * Establece objeto
     * <code>JasperPrint</code> que generará reporte
     *
     * @param jp objeto <code>JasperPrint</code> que generará documento
     */
    public void setPrint(JasperPrint jp) {
        this.jp = jp;
    }

    /**
     * Recupera parámetros asociados al reporte
     *
     * @return objeto <code>ArrayList</code> con parámetros asociados al reporte
     */
    public ArrayList<Parametro> getDefinicionParametros() {
        return definicionParametros;
    }

    /**
     * Establece parámetros asociados al reporte
     *
     * @param definicionParametros <code>ArrayList</code> con parámetros
     * asociados al reporte
     */
    public void setDefinicionParametros(ArrayList<Parametro> definicionParametros) {
        this.definicionParametros = definicionParametros;
    }

    /**
     * Recupera parámetros asociados al reporte desde la base de datos
     *
     * @param cx Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de conexión a la base
     * @throws Fallo si ocurre un error al acceder a la base de datos.
     */
    public void getDefinicionParametros(Conexion cx) throws Fallo {
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        this.definicionParametros.clear();
        String s = "SELECT * FROM be_parametro_reporte WHERE clave_reporte=".concat(String.valueOf(this.claveReporte));
        ResultSet rsParametros;
        try {
            rsParametros = oDb.getRs(s);

            while (rsParametros.next()) {
                Parametro definicionParametro = new Parametro(
                        rsParametros.getString("parametro"),
                        rsParametros.getString("tipo_dato"),
                        rsParametros.getString("valor"));

                this.definicionParametros.add(definicionParametro);

            }
        } catch (Exception e) {
            // Fallo en algun momento.
            throw new Fallo(e.getMessage());

        }
    }

    /**
     * Establece la conexión a la base de datos al reporte así como la plantilla
     * de iReport
     *
     * @param cx Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de conexión a la base
     * @throws Fallo si ocurre un error relacionado con JasperReports
     */
    public final void setPrint(Conexion cx) throws Fallo {
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        /*  Se repite el mecanismo de conexión de la clase Conexion con el fin de evitar 
         * hacerla pública por motivos de seguridad */
      Connection conn = null;
        try {
            switch (oDb.getDbType()) {
                case MSSQL:
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    conn = DriverManager.getConnection("jdbc:sqlserver://" + oDb.getServer() + ";databaseName=" + oDb.getDb() + ";selectMethod=cursor;", oDb.getUser(), oDb.getPw());
                    break;
                case MYSQL:
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://" + oDb.getServer() + "/" + oDb.getDb(), oDb.getUser(), oDb.getPw());
                    break;
            }
        } catch (Exception e) {
            // Fallo en algun momento.
            throw new Fallo(e.getMessage());
        }

        try {
            this.jp = JasperFillManager.fillReport("C:/reportes/".concat(this.jrxml).concat(".jasper"), this.parametros, conn);
        } catch (JRException ex) {
            throw new Fallo(ex.getMessage());
        } finally {
            try {
                conn.close();
                conn = null;
            } catch (Exception e) {
                // Fallo en algun momento.
                throw new Fallo(e.getMessage());
            }
        }
    }

    /**
     * Recupera la marca que define si se debe lanzar el reporte al realizar
     * actualización en regsitros de la forma asociada
     *
     * @return Verdadero si se debe lanzar el reporte al actualizar registros de
     * la forma asociada o falso de otro modo
     */
    public boolean isGenerarEnActualizacion() {
        return generarEnActualizacion;
    }

    /**
     * Establece la marca que define si se debe lanzar el reporte al realizar
     * actualización de regsitros de la forma asociada
     *
     * @param generarEnActualizacion Verdadero si se debe lanzar el reporte al
     * actualizar registros de la forma asociada o falso de otro modo
     */
    public void setGenerarEnActualizacion(boolean generarEnActualizacion) {
        this.generarEnActualizacion = generarEnActualizacion;
    }

    /**
     * Recupera la marca que define si se debe lanzar el reporte al realizar
     * inserción de registros de la forma asociada
     *
     * @return Verdadero si se debe lanzar el reporte al insertar registros de
     * la forma asociada o falso de otro modo
     */
    public boolean isGenerarEnInsercion() {
        return generarEnInsercion;
    }

    /**
     * Establece la marca que define si se debe lanzar el reporte al realizar
     * inserción de registros de la forma asociada
     *
     * @param generarEnInsercion Verdadero si se debe lanzar el reporte al
     * insertar registros de la forma asociada o falso de otro modo
     */
    public void setGenerarEnInsercion(boolean generarEnInsercion) {
        this.generarEnInsercion = generarEnInsercion;
    }

    /**
     * Recupera objeto
     * <code>JasperPrint</code> que generará reporte
     *
     * @return objeto <code>JasperPrint</code> que generará reporte
     */
    public JasperPrint getJp() {
        return jp;
    }

    /**
     * Establece objeto
     * <code>JasperPrint</code> que generará reporte
     *
     * @param jp objeto <code>JasperPrint</code> que generará reporte
     */
    public void setJp(JasperPrint jp) {
        this.jp = jp;
    }

    /**
     * Recupera ruta relativa de archivo
     * <code>JRXML</code> de iReport
     *
     * @return ruta relativa de archivo <code>JRXML</code> de iReport
     */
    public String getJrxml() {
        return jrxml;
    }

    /**
     * Establece ruta relativa de archivo
     * <code>JRXML</code> de iReport
     *
     * @param jrxml ruta relativa de archivo <code>JRXML</code> de iReport
     */
    public void setJrxml(String jrxml) {
        this.jrxml = jrxml;
    }

    /**
     * Recupera archivo JSP que se encargará de invocar a la clase y generar el
     * reporte
     *
     * @return Nombre del archivo JSP que se encargará de invocar a la clase y
     * generar el reporte sin ruta
     */
    public String getJsp() {
        return jsp;
    }

    /**
     * Establece archivo JSP que se encargará de invocar a la clase y generar el
     * reporte
     *
     * @param jsp Nombre del archivo JSP que se encargará de invocar a la clase
     * y generar el reporte sin ruta
     */
    public void setJsp(String jsp) {
        this.jsp = jsp;
    }

    /**
     * Recupera parámetros asociados al reporte
     *
     * @return objeto <code>Map</code> con parámetros asociados al reporte
     */
    public Map getParametros() {
        return parametros;
    }

    /**
     * Establece parámetros asociados al reporte
     *
     * @param parametros objeto <code>Map</code> con parámetros asociados al
     * reporte
     */
    public void setParametros(Map parametros) {
        this.parametros = parametros;
    }

    /**
     * Recupera descripción del reporte
     *
     * @return Descripción del reporte
     */
    public String getReporte() {
        return reporte;
    }

    /**
     * Establece descripción del reporte
     *
     * @param reporte Descripción del reporte
     */
    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    /**
     * Constructor del reporte con parámetros
     *
     * @param claveReporte Llave primaria del reporte
     * @param cx Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de conexión a la base
     * @throws Fallo si ocurre un error al acceder a la base de datos
     */
    public ReporteJasper(int claveReporte, Conexion cx) throws Fallo {
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        ResultSet rsReporte;
        this.claveReporte = claveReporte;
        
        try {
            String s = "select * from be_reporte where clave_reporte= " + claveReporte;

            rsReporte = oDb.getRs(s);

            if (rsReporte.next()) {
                this.reporte = rsReporte.getString("reporte");
                this.claveForma = rsReporte.getInt("clave_forma");
                this.clavePerfil = rsReporte.getInt("clave_perfil");
                this.jsp = rsReporte.getString("jsp");
                this.jrxml = rsReporte.getString("jrxml");
                this.generarEnInsercion = rsReporte.getInt("generar_en_insercion") == 0 ? false : true;
                this.generarEnActualizacion = rsReporte.getInt("generar_en_actualizacion") == 0 ? false : true;
            } else {
                throw new Fallo("No se encontró el reporte seleccionado");
            }

            rsReporte.close();
        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }
    }
}
