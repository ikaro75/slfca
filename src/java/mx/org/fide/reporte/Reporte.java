/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.reporte;

import mx.org.fide.jasper.Parametro;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;

/**
 * Contiene los datos para generar reportes en HTML a partir de las formas definidas
 */
public class Reporte {
        
    /**
     * Clase interna con la definición de los tipos de reporte
     */
    public enum TipoReporte {
        /**
         *  Reporte elaborado con HTML y jQuery
         */
        jquery,
        /**
         * Reporte elaborado con Jasper iReport
         */
        jasper
    }
    int claveReporte;
    int claveForma;
    int clavePerfil;
    String reporte;
    String jsp;
    String jrxml;
    String tituloTick;
    String colorSeries;
    String consulta;
    TipoReporte tipoReporte;
    boolean generarEnInsercion;
    boolean generarEnActualizacion;

    ArrayList<Tick> ticks= new ArrayList <Tick>();
    ArrayList<Serie> series= new ArrayList <Serie>();

    /**
     * Recupera descripción de reporte
     * @return  Descripción de reporte
     */
    public String getReporte() {
        return reporte;
    }

    /**
     * Establece descripción de reporte 
     * @param Reporte Descripción de reporte
     */
    public void setReporte(String Reporte) {
        this.reporte = Reporte;
    }

    /**
     * Recupera clave del reporte
     * @return  Clave del reporte
     */
    public int getClaveReporte() {
        return claveReporte;
    }

    /**
     * Establece clave del reporte
     * @param claveReporte Clave del reporte
     */ 
    public void setClaveReporte(int claveReporte) {
        this.claveReporte = claveReporte;
    }

    /**
     * Establece al objeto la clave del reporte y recupera los datos a partir de la base de datos
     * @param claveReporte  Clave del reporte
     * @param clavePerfil   Clave del perfil del usuario
     * @param cx            Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @throws Fallo        si ocurre algún error relacionado a la base de datos
     */
    public void setClaveReporte(int claveReporte, int clavePerfil, Conexion cx) throws Fallo {
        this.claveReporte = claveReporte;
        Conexion oDb =new Conexion (cx.getServer(), cx.getDb(), cx.getUser(),cx.getPw(),cx.getDbType());
        ResultSet rsReporte;

        try {
            String s = "select * from be_reporte where clave_reporte= " + claveReporte;

            rsReporte = oDb.getRs(s);

            if (rsReporte.next()) {
                this.reporte = rsReporte.getString("reporte");
                this.claveForma = rsReporte.getInt("clave_forma");
                this.clavePerfil= rsReporte.getInt("clave_perfil");
                this.jsp = rsReporte.getString("jsp");
                this.jrxml = rsReporte.getString("jrxml");
                this.consulta = rsReporte.getString("consulta");
                this.tituloTick = rsReporte.getString("etiqueta_tick");
                this.colorSeries = rsReporte.getString("color_series");
                this.tipoReporte= TipoReporte.values()[rsReporte.getInt("clave_tipo_reporte")-1];
                this.generarEnInsercion = rsReporte.getInt("generar_en_insercion")==0?false:true;
                this.generarEnActualizacion = rsReporte.getInt("generar_en_actualizacion")==0?false:true;

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
            oDb= null;
        }
    }

    /**
     * Recupera la clave de a forma asociada a la forma
     * @return Clave de la forma asociada a la forma
     */
    public int getClaveForma() {
        return claveForma;
    }

    /**
     * Establece la clave de a forma asociada a la forma
     * @param claveForma Clave de la forma asociada a la forma
     */
    public void setClaveForma(int claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Recupera Colores de la serie para las gráficas 
     * @return Cadena con colores en formato RGB y separadas por coma para cada serie de datos
     */
    public String getColorSeries() {
        return colorSeries;
    }

    /**
     * Establece de la serie para las gráficas 
     * @param colorSeries Cadena con colores en formato RGB y separadas por coma para cada serie de datos
     */
    public void setColorSeries(String colorSeries) {
        this.colorSeries = colorSeries;
    }

    /**
     * Recupera tipo de reporte
     * @return Tipo de reporte
     */
    public TipoReporte getTipoReporte() {
        return tipoReporte;
    }

    /**
     * Establece tipo de reporte
     * @param tipoReporte Tipo de reporte
     */
    public void setTipoReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    /**
     * Recupera etiquetas de las series de datos
     * @return  Etiquetas de series de datos
     */
    public String getTituloTick() {
        return tituloTick;
    }

    /**
     * Establece etiquetas de las series de datos
     * @param tituloTick Etiquetas de series de datos
     */
    public void setTituloTick(String tituloTick) {
        this.tituloTick = tituloTick;
    }

    /**
     * Recupera consulta en la que se basa el reporte
     * @return  Código SQL compatible con la base de datos
     */
    public String getConsulta() {
        return consulta;
    }

    /**
     * Establece consulta en la que se basa el reporte
     * @param consulta Código SQL compatible con la base de datos
     */
    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    /**
     * Recupera etiquetas de series de todos los datos de la consulta 
     * @return  Objeto <code>ArrayList<Tick></code> con etiquetas de series de todos los datos de la consulta 
     */
    public ArrayList<Tick> getTicks() {
        return ticks;
    }

    /**
     * Establece etiquetas de series de todos los datos de la consulta 
     * @param ticks  Objeto <code>ArrayList<Tick></code> con etiquetas de series de todos los datos de la consulta 
     */ 
    public void setTicks(ArrayList<Tick> ticks) {
        this.ticks = ticks;
    }

    /**
     * Recupera nombres de series de todos los datos de la consulta
     * @return Objeto <code>ArrayList<Tick></code> con nombres de series de todos los datos de la consulta
     */
    public ArrayList<Serie> getSeries() {
        return series;
    }

    /**
     * Establece nombres de series de todos los datos de la consulta
     * @param series Objeto <code>ArrayList<Tick></code> con nombres de series de todos los datos de la consulta
     */ 
    public void setSeries(ArrayList<Serie> series) {
        this.series = series;
    }

    /**
     * Recupera clave del perfil asociado al reporte
     * @return Clave del perfil asociado al reporte
     */
    public int getClavePerfil() {
        return clavePerfil;
    }

    /**
     * Establece clave del perfil asociado al reporte
     * @param clavePerfil Clave del perfil asociado al reporte
     */
    public void setClavePerfil(int clavePerfil) {
        this.clavePerfil = clavePerfil;
    }

    /**
     * Recupera marca que define si el reporte será lanzado al insertar un registro de la forma asociada
     * @return  Verdadero si se debe lanzar el reporte al insertar un registro de la forma asociada, flaso de otro modo
     */
    public boolean isGenerarEnActualizacion() {
        return generarEnActualizacion;
    }

    /**
     * Establece marca que define si el reporte será lanzado al insertar un registro de la forma asociada
     * @param generarEnActualizacion Verdadero si se debe lanzar el reporte al insertar un registro de la forma asociada, flaso de otro modo
     */
    public void setGenerarEnActualizacion(boolean generarEnActualizacion) {
        this.generarEnActualizacion = generarEnActualizacion;
    }

    /**
     * Recupera marca que define si el reporte será lanzado al actualizar un registro de la forma asociada
     * @return Verdadero si se debe lanzar el reporte al actualizar un registro de la forma asociada, flaso de otro modo
     */
    public boolean isGenerarEnInsercion() {
        return generarEnInsercion;
    }

    /**
     * Establece marca que define si el reporte será lanzado al actualizar un registro de la forma asociada
     * @param generarEnInsercion  Verdadero si se debe lanzar el reporte al actualizar un registro de la forma asociada, flaso de otro modo
     */ 
    public void setGenerarEnInsercion(boolean generarEnInsercion) {
        this.generarEnInsercion = generarEnInsercion;
    }

    /**
     * Recupera ruta relativa del archivo JRXML de iReport asociado al reporte
     * @return  Ruta relativa del archivo JRXML de iReport asociado al reporte
     */
    public String getJrxml() {
        return jrxml;
    }

    /**
     * Establece ruta relativa del archivo JRXML de iReport asociado al reporte
     * @param jrxml Ruta relativa del archivo JRXML de iReport asociado al reporte
     */
    public void setJrxml(String jrxml) {
        this.jrxml = jrxml;
    }

    /**
     * Recupera ruta relativa del archivo JSP que se invocará para generar el reporte
     * @return Ruta relativa del archivo JSP que se invocará para generar el reporte
     */
    public String getJsp() {
        return jsp;
    }

    /**
     * Establece ruta relativa del archivo JSP que se invocará para generar el reporte
     * @param jsp Ruta relativa del archivo JSP que se invocará para generar el reporte
     */
    public void setJsp(String jsp) {
        this.jsp = jsp;
    }

    /**
     * Desglosa las etiquetas de las series de datos del reporte
     * @return Cadena con etiquetas de las series de datos separadas por coma
     */
    public String imprimeTicks() {
        StringBuilder sb = new StringBuilder();
        for (Tick t: this.ticks) {
            sb.append("'").append(t.tick.replaceAll("'", "")).append("',");
        }
        
        return sb.deleteCharAt(sb.length()-1).toString();
    }

    /**
     * Desglosa los símbolos de las series de datos del reporte
     * @return Cadena con símbolos de las series de datos separadas por coma
     */
    public String imprimeSimbolos() {
        StringBuilder sb = new StringBuilder();
        for (Tick t: this.ticks) {
            if (t.simbolo!=null)
                sb.append("'").append(t.simbolo.replaceAll("'", "")).append("',");
        }
        
        return sb.deleteCharAt(sb.length()-1).toString();
    }
    
    /**
     * Desglosa la descripción de las series de datos del reporte
     * @return Cadena con descripción de las series de datos separadas por coma
     */
    public String imprimeSeries() {
        StringBuilder sb = new StringBuilder();
        for (Serie s: this.series) {
            sb.append("'").append(s.etiqueta.replaceAll("'", "''")).append("',");
        }
        
        return sb.deleteCharAt(sb.length()-1).toString();        
    }

    /**
     * Constructor
     * @param claveReporte              Llave primaria del reporte
     * @param claveForma                Clave de la forma asociada al reporte
     * @param reporte                   Descripción del reporte
     * @param jsp                       Ruta relativa del archivo JSP que se invocará al generar reporte
     * @param jrxml                     Ruta relativa del archivo JRXML que se invocará al generar reporte
     * @param tituloTick                Tïtulo de las etiquetas que se mostrarán en el reporte
     * @param colorSeries               Cadena con colores en formato RGB separads por coma, para asignarlas a cada serie de datos
     * @param consulta                  Consulta SQL  en la que se basa el reporte compatible con la base de datos
     * @param tipoReporte               Tipo de rpeorte: puede ser jquery o jasper
     * @param generarEnInsercion        Marca para indicar si se dispara el reporte al momento de insertar registros en la forma asociada
     * @param generarEnActualizacion    Marca para indicar si se dispara el reporte al momento de actualizar registros en la forma asociada
     */
    public Reporte(int claveReporte, int claveForma, String reporte, String jsp, String jrxml, String tituloTick, String colorSeries, String consulta, TipoReporte tipoReporte, boolean generarEnInsercion, boolean generarEnActualizacion) {
        this.claveReporte = claveReporte;
        this.claveForma = claveForma;
        this.reporte = reporte;
        this.jsp = jsp;
        this.jrxml = jrxml;
        this.tituloTick = tituloTick;
        this.colorSeries = colorSeries;
        this.consulta = consulta;
        this.tipoReporte = tipoReporte;
        this.generarEnInsercion = generarEnInsercion;
        this.generarEnActualizacion = generarEnActualizacion;
    }



    /**
     * Contructor sin parámetros
     */
    public Reporte() {
    }

    
    /**
     * Genera el reporte ejecutando su consulta, solo aplica para reportes de tipo jquery.
     * @param oDb    Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @throws Fallo Si ocurre algún error en la base de datos
     */
    public void ejecutaConsulta(Conexion oDb) throws Fallo {
        
        if (this.consulta == null) {
            throw new Fallo("La consulta del reporte no ha sido definida, verique");
        }
        
        Tick t;
        Serie s;
        String[] colores;
        
        if (this.colorSeries!=null)  
           colores=this.colorSeries.split(";");
        else
           colores=null;
        
        //Se aplican las reglas de rremplazo al momento de llenar el bean, solo es necesario ejecutar la consulta
        ResultSet rsReporte;
        try {
                //Es necesario alterar getRs para que el cursor que devuelve pueda regresar al inicio
                rsReporte = oDb.getRs(this.consulta, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
                ticks.clear();
                //Llena el arreglo de ticks
                while ( rsReporte.next()) {               
                    //Si no se ha insertado el ticker, se inserta 
                    if (!ticks.contains(t=new Tick(rsReporte.getString("ticker")))) {
                        if (rsReporte.getString("simbolo")!=null) 
                            t.setSimbolo(rsReporte.getString("simbolo"));
                        ticks.add(t);
                    }
                }
                
                //Llena el arreglo de series
                rsReporte.beforeFirst();
                series.clear();
                while ( rsReporte.next()) {
                    //Si no se ha insertado la serie, se inserta 
                    if (!series.contains(s=new Serie(rsReporte.getString("etiqueta_serie")))) {
                        
                        //Se busca el color que le corresponda a la serie
                        if (colores!=null) {
                            for (int i=0; i<colores.length;i++) {
                                if (colores[i].split("=")[0].trim().equals(rsReporte.getString("etiqueta_serie").trim())) {
                                    s.setColor(colores[i].split("=")[1]);
                                    break;
                                }
                                
                            }
                        }
                        
                        //Se agregan tantos datos como Ticks
                        for (int i=0; i<ticks.size(); i++) {
                            s.getDatos().add(Float.valueOf("0.0"));
                        }
                        series.add(s);
                    }
                }

                //Llena los datos de acuerdo a los ticks y a las series
                rsReporte.beforeFirst();
                
               while ( rsReporte.next()) {
                    //Se busca la posición del registro en el arreglo de ticks
                   int nTick=ticks.indexOf(new Tick(rsReporte.getString("ticker")));
                   int nSerie=series.indexOf(new Serie(rsReporte.getString("etiqueta_serie")));
                   s = series.get(nSerie);
                   s.getDatos().set(nTick, rsReporte.getFloat("dato_serie"));
                }    
               
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
 
            oDb.cierraConexion();
        }
    }
}
