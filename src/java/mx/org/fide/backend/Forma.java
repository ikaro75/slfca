package mx.org.fide.backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Portlet;
import mx.org.fide.modelo.Usuario;
import mx.org.fide.reporte.Reporte;
import mx.org.fide.reporte.Reporte.TipoReporte;

/**
 * Recupera de la base de datos la información relacionada a la forma de cada
 * tabla de cada aplicación y también se encarga de crearla considerando los
 * permisos del administrador, el diccionario de datos y las consultas asociadas
 * a la forma
 */
public class Forma extends Consulta {

    private Integer claveForma;
    private String forma;
    private boolean select;
    private boolean insert;
    private boolean update;
    private boolean delete;
    private boolean report;
    private String aliasTab;
    private Integer claveAplicacion;
    private String eventoForma;
    private String eventoGrid;
    private String instrucciones;
    private boolean muestraFormasForeaneas;
    private Integer claveTipoPresentacionFormaForanea;
    private boolean permiteDuplicarRegistro;
    private boolean permiteInsertarComentario;
    private boolean permiteMultiseleccion;
    private boolean permiteGuardarComoPlantilla;
    private Integer frecuenciaActualizacion;
    private String tabla;
    private String llavePrimaria;
    private String busquedaRapida;
    private String campoSeguimientoFlujo;
    private Integer claveTipoGrid;
    private ArrayList<Reporte> reportes;
    private ArrayList<Nota> notas;
    private ArrayList<Forma> formasForaneas;
    private ArrayList<Portlet> portlets;
    private boolean prefiltro;
    private boolean definicionDeGrid;

    /**
     * Constructor de la forma
     */
    public Forma() {
    }

    /**
     * Constructor de la forma a partir de
     * <code>com.administrax.modelo.Consulta</code> y
     * <code>com.administrax.modelo.Conexión</code>
     *
     * @param c Instancia de clase <code>com.administrax.modelo.Consulta</code>
     * @param cx instancia de clase <code>com.administrax.modelo.Conexion</code>
     * @throws Fallo si ocurre un error al omitir valores en la consulta
     */
    public Forma(Consulta c, Boolean cargaDesdeRequest) throws Fallo {
        try {
            super.setAccion(c.getAccion());
            super.setCampos(c.getCampos());
            super.setClaveAplicacion(c.getClaveAplicacion());
            super.setClaveForma(c.getClaveForma());
            super.setClaveConsulta(c.getClaveConsulta());
            super.setClavePerfil(c.getClavePerfil());
            super.setIdx(c.getIdx());
            super.setLimiteDeRegistros(c.getLimiteDeRegistros());
            super.setLlavePrimaria(c.getLlavePrimaria());
            super.setNumeroDeRegistros(c.getNumeroDeRegistros());
            super.setOrdx(c.getOrdx());
            super.setPagina(c.getPagina());
            super.setPk(c.getPk());
            super.setRegistros(c.getRegistros());
            super.setSQL(c.getSQL());
            super.setTabla(c.getTabla());
            super.setW(c.getW());
            super.setUsuario(c.getUsuario());

            if (cargaDesdeRequest) {

                if (super.getAccion().equals("delete")) {
                    this.claveForma = super.getClaveForma();
                    this.tabla = super.getTabla();
                    this.llavePrimaria = super.getLlavePrimaria();
                } else {
                    if (super.getCampos().get("clave_forma").getValor() != null) {
                        this.claveForma = Integer.parseInt(super.getCampos().get("clave_forma").getValor().toString());
                    }

                    this.forma = super.getCampos().get("forma").getValor().toString();

                    if (super.getCampos().get("alias_tab").getValor() != null) {
                        this.aliasTab = super.getCampos().get("alias_tab").getValor().toString();
                    }

                    if (super.getCampos().get("evento_forma").getValor() != null) {
                        this.eventoForma = super.getCampos().get("evento_forma").getValor().toString();
                    }

                    if (super.getCampos().get("instrucciones").getValor() != null) {
                        this.instrucciones = super.getCampos().get("instrucciones").getValor().toString();
                    }

                    if (super.getCampos().get("muestra_formas_foraneas").getValor() != null) {
                        this.muestraFormasForeaneas = Boolean.parseBoolean(super.getCampos().get("muestra_formas_foraneas").getValor().toString());
                    }

                    if (super.getCampos().get("permite_duplicar_registro").getValor() != null) {
                        this.permiteDuplicarRegistro = Boolean.parseBoolean(super.getCampos().get("permite_duplicar_registro").getValor().toString());
                    }

                    if (super.getCampos().get("permite_insertar_comentarios").getValor() != null) {
                        this.permiteInsertarComentario = Boolean.parseBoolean(super.getCampos().get("permite_insertar_comentarios").getValor().toString());
                    }

                    if (super.getCampos().get("permite_guardar_como_plantilla").getValor() != null) {
                        this.permiteGuardarComoPlantilla = Boolean.parseBoolean(super.getCampos().get("permite_guardar_como_plantilla").getValor().toString());
                    }

                    if (super.getCampos().get("permite_multiseleccion").getValor() != null) {
                        this.permiteMultiseleccion = Boolean.parseBoolean(super.getCampos().get("permite_multiseleccion").getValor().toString());
                    }

                    if (super.getCampos().get("frecuencia_actualizacion").getValor() != null && !super.getCampos().get("frecuencia_actualizacion").getValor().equals("")) {
                        this.frecuenciaActualizacion = Integer.parseInt(super.getCampos().get("frecuencia_actualizacion").getValor());
                    }

                    if (super.getCampos().get("clave_tipo_grid").getValor() != null && !super.getCampos().get("clave_tipo_grid").getValor().equals("")) {
                        this.claveTipoGrid = Integer.parseInt(super.getCampos().get("clave_tipo_grid").getValor());
                    }
                    
                    if (super.getCampos().get("prefiltro").getValor() != null && !super.getCampos().get("prefiltro").getValor().equals("")) {
                        this.prefiltro = Boolean.parseBoolean(super.getCampos().get("prefiltro").getValor());
                    }

                    this.tabla = super.getCampos().get("tabla").getValor().toString();
                    this.llavePrimaria = super.getCampos().get("llave_primaria").getValor().toString();
                    
                    if (super.getCampos().get("busqueda_rapida").getValor() != null && !super.getCampos().get("busqueda_rapida").getValor().equals("")) {
                        this.busquedaRapida =  super.getCampos().get("busqueda_rapida").getValor().toString();;
                    }
                }

            } else {
                this.claveForma = super.getClaveForma();
                Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

                //En caso de que se solicite una forma con clave negativa
                if (this.claveForma < 0) {
                    this.aliasTab = "(Reservado)";
                    this.eventoForma = null;
                    this.forma = "(Reservado)";
                    this.tabla = "(Reservado)";
                    this.llavePrimaria = "(Reservado)";
                    this.instrucciones = null;
                    this.select = false;
                    this.insert = false;
                    this.update = false;
                    this.delete = false;
                    return;
                }

                ResultSet rsForma;

                try {
                    StringBuilder s = new StringBuilder("");
                    if (oDb.getDbType() == Conexion.DbType.MSSQL) {
                        s = new StringBuilder("select f.tabla, f.llave_primaria, f.alias_tab,f.evento_forma,f.evento_grid,f.instrucciones,f.forma,f.muestra_formas_foraneas, f.clave_tipo_presentacion_forma_foranea, f.permite_duplicar_registro,f.permite_insertar_comentarios,f.permite_guardar_como_plantilla,f.frecuencia_actualizacion,f.clave_tipo_grid, f.permite_multiseleccion,f.prefiltro,f.busqueda_rapida,")
                                .append("(select top 1 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=1) as selectx,")
                                .append("(select top 1 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=2) as insertx,")
                                .append("(select top 1 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=3) as updatex,")
                                .append("(select top 1 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=4) as deletex,")
                                .append("(select top 1 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=6) as reportex")
                                .append(" from ")
                                .append(" be_forma f, be_permiso_forma pf ")
                                .append(" where f.clave_forma=pf.clave_forma ")
                                .append(" and pf.clave_permiso=1 ")
                                .append(" and pf.clave_perfil=").append(super.getUsuario().getClavePerfil())
                                .append(" and f.clave_forma=").append(claveForma);
                    } else if (oDb.getDbType() == Conexion.DbType.MYSQL) {
                        s = new StringBuilder("select f.tabla, f.llave_primaria, f.alias_tab,f.evento_forma,f.evento_grid,f.instrucciones,f.forma,f.muestra_formas_foraneas, f.clave_tipo_presentacion_forma_foranea,f.permite_duplicar_registro,f.permite_insertar_comentarios,f.permite_guardar_como_plantilla,f.frecuencia_actualizacion,f.clave_tipo_grid,f.permite_multiseleccion,f.prefiltro,f.busqueda_rapida,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=1 limit 1) as selectx,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=2 limit 1) as insertx,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=3 limit 1) as updatex,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=4 limit 1) as deletex,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=6 limit 1 ) as reportex")
                                .append(" from ")
                                .append(" be_forma f, be_permiso_forma pf ")
                                .append(" where f.clave_forma=pf.clave_forma")
                                .append(" and pf.clave_permiso=1")
                                .append(" and pf.clave_perfil=").append(super.getUsuario().getClavePerfil())
                                .append(" and f.clave_forma=").append(claveForma);
                    } else if (oDb.getDbType() == Conexion.DbType.ORACLE) {
                        s = new StringBuilder("select f.tabla, f.llave_primaria, f.alias_tab,f.evento_forma,f.evento_grid,f.instrucciones,f.forma,f.muestra_formas_foraneas,f.clave_tipo_presentacion_forma_foranea,f.permite_duplicar_registro,f.permite_insertar_comentarios,f.permite_guardar_como_plantilla,f.frecuencia_actualizacion,f.clave_tipo_grid,f.permite_multiseleccion,f.prefiltro,f.busqueda_rapida,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=1 AND rownum=1) as selectx,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=2 AND rownum=1) as insertx,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=3 AND rownum=1) as updatex,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=4 AND rownum=1) as deletex,")
                                .append("(select 1 from be_permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + super.getUsuario().getClavePerfil() + " AND clave_permiso=6 AND rownum=1 ) as reportex")
                                .append(" from ")
                                .append("be_forma f,")
                                .append("be_aplicacion a, be_permiso_forma pf")
                                .append(" where f.clave_forma=pf.clave_forma ")
                                .append(" and pf.clave_permiso=1 ")
                                .append(" and pf.clave_perfil=").append(super.getUsuario().getClavePerfil())
                                .append(" and f.clave_forma=").append(claveForma);
                    }

                    rsForma = oDb.getRs(s.toString());

                    if (rsForma.next()) {
                        this.aliasTab = rsForma.getString("alias_tab");
                        this.eventoForma = rsForma.getString("evento_forma");
                        this.eventoGrid = rsForma.getString("evento_grid");
                        this.forma = rsForma.getString("forma");
                        this.tabla = rsForma.getString("tabla");
                        this.llavePrimaria = rsForma.getString("llave_primaria");
                        this.busquedaRapida = rsForma.getString("busqueda_rapida");
                        this.instrucciones = rsForma.getString("instrucciones");
                        this.select = rsForma.getBoolean("selectx");
                        this.insert = rsForma.getBoolean("insertx");
                        this.update = rsForma.getBoolean("updatex");
                        this.delete = rsForma.getBoolean("deletex");
                        this.report = rsForma.getBoolean("reportex");
                        this.muestraFormasForeaneas = rsForma.getBoolean("muestra_formas_foraneas");
                        this.claveTipoPresentacionFormaForanea = rsForma.getInt("clave_tipo_presentacion_forma_foranea");
                        this.permiteDuplicarRegistro = rsForma.getBoolean("permite_duplicar_registro");
                        this.permiteInsertarComentario = rsForma.getBoolean("permite_insertar_comentarios");
                        this.permiteGuardarComoPlantilla = rsForma.getBoolean("permite_guardar_como_plantilla");
                        this.permiteMultiseleccion = rsForma.getBoolean("permite_multiseleccion");
                        this.frecuenciaActualizacion = rsForma.getInt("frecuencia_actualizacion");
                        this.claveTipoGrid = rsForma.getInt("clave_tipo_grid");
                        this.prefiltro = rsForma.getBoolean("prefiltro");
                    } else {
                        throw new Fallo("No se encontró la forma seleccionada para este perfil");
                    }

                    rsForma.close();
                } catch (SQLException sqlex) {
                    throw new Fallo(sqlex.toString());
                } catch (Exception e) {
                    throw new Fallo(e.getMessage());
                } finally {
                    oDb.cierraConexion();
                    oDb = null;
                }
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                throw new Fallo("Error al construir forma ".concat(this.forma).concat(": ").concat(e.getMessage()));
            } else {
                throw new Fallo("Error al construir forma ".concat(this.forma).concat(": ").concat(e.toString()));
            }
        }
    }

    /**
     * Recupera el permiso para borrar para el perfil del usuario actual
     *
     * @return Verdadero si se cuenta con permisos para borrar, o falso de lo
     * contrario
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * Establece el permiso para borrar para el perfil del usuario actual
     *
     * @param delete Verdadero para dar permiso de eliminación en esa forma,
     * falso para denegarlo.
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    /**
     * Recupera el permiso para insertar para el perfil del usuario actual
     *
     * @return Verdadero si se cuenta con permisos para insertar, o falso de lo
     * contrario
     */
    public boolean isInsert() {
        return insert;
    }

    /**
     * Establece el permiso para insertar para el perfil del usuario actual
     *
     * @param insert Verdadero para dar permiso de inserción en esa forma, falso
     * para denegarlo.
     */
    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    /**
     * Recupera el permiso para visualizar registro para el perfil del usuario
     * actual
     *
     * @return Verdadero si se cuenta con permisos para visualizar, o falso de
     * lo contrario
     */
    public boolean isSelect() {
        return select;
    }

    /**
     * Establece el permiso para visualizar para el perfil del usuario actual
     *
     * @param select Verdadero para dar permiso de visualización en esa forma,
     * falso para denegarlo.
     */
    public void setSelect(boolean select) {
        this.select = select;
    }

    /**
     * Recupera el permiso para actualizar registros para el perfil del usuario
     * actual
     *
     * @return Verdadero si se cuenta con permisos para actualizar datos
     * sensibles, o falso de lo contrario
     */
    public boolean isUpdate() {
        return update;
    }

    /**
     * Establece el permiso para actualizar registros para el perfil del usuario
     * actual
     *
     * @param update Verdadero para dar permiso de actualización en esa forma,
     * falso para denegarlo.
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }

    /**
     * Recupera el permiso para visualizar reportes relacionados a la forma para
     * el perfil del usuario actual
     *
     * @return Verdadero si se cuenta con permisos para actualizar datos
     * sensibles, o falso de lo contrario
     */
    public boolean isReport() {
        return report;
    }

    /**
     * Establece el permiso para visualizar reportes relacionados a la forma
     * para el perfil del usuario actual
     *
     * @param report Verdadero si se cuenta con permisos para visualizar
     * reportes relacionados a la forma, o falso de lo contrario
     */
    public void setReport(boolean report) {
        this.report = report;
    }

    /**
     * Recupera la tabla a la que está vinculada la forma
     *
     * @return Nombre de la tabla a la que está vinculada la forma
     */
    public String getTabla() {
        return tabla;
    }

    /**
     * Establece la tabla a la que está vinculada la forma
     *
     * @param tabla Nombre de la tabla a la que está vinculada la forma
     */
    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    /**
     * Recupera el nombre del campo que conforma la llave primaria de la tabla
     * vinculada con la forma
     *
     * @return Nombre del campo llave primaria de la tabla vinculada con la
     * forma
     */
    public String getLlavePrimaria() {
        return llavePrimaria;
    }

    /**
     * Establece el nombre del campo que conforma la llave primaria de la tabla
     * vinculada con la forma
     *
     * @param llavePrimaria Nombre del campo llave primaria de la tabla
     * vinculada con la forma
     */
    public void setLlavePrimaria(String llavePrimaria) {
        this.llavePrimaria = llavePrimaria;
    }

    public String getBusquedaRapida() {
        return busquedaRapida;
    }

    public void setBusquedaRapida(String busquedaRapida) {
        this.busquedaRapida = busquedaRapida;
    }
    
    /**
     * Recupera la llave primaria del registro de la forma
     *
     * @return Clave de la forma
     */
    public Integer getClaveForma() {
        return claveForma;
    }

    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    public Integer getClaveTipoGrid() {
        return claveTipoGrid;
    }

    public void setClaveTipoGrid(Integer claveTipoGrid) {
        this.claveTipoGrid = claveTipoGrid;
    }

    /**
     * Recupera descripción la forma
     *
     * @return Descripción de la forma
     */
    public String getForma() {
        return forma;
    }

    /**
     * Establece descripción la forma
     *
     * @param forma Descripción de la forma
     */
    public void setForma(String forma) {
        this.forma = forma;
    }

    /**
     * Recupera descripción de la entidad a la que hace referencia la forma
     * mostrada en la bitácora
     *
     * @return Descripción de la entidad a la que hace referencia la forma
     * mostrada en la bitácora, de este valor se toman las dos primeras letras
     * para verificar el género de la entidad y aplicar títulos apropiados en
     * las operaciones de inserción y edición. Las dos primeras letras deberán
     * ser "el" o "la", por ejemplo: "la forma". En este caso por tratarse de un
     * sustantivo femenino se mostrará la etiqueta "Nueva forma" al momento de
     * que se genere la forma de inserción
     */
    public String getAliasTab() {
        return aliasTab;
    }

    /**
     * Establece descripción de la entidad a la que hace referencia la forma
     * mostrada en la bitácora
     *
     * @param aliasTab Descripción de la entidad a la que hace referencia la
     * forma mostrada en la bitácora. Al momento de definir este atributo debe
     * de especificarse el artículo "la" o "el" dependiendo del género de la
     * entidad. Esto se verá reflejado en los títulos de las formas así como en
     * la construcción de frases que reporten la bitácora
     */
    public void setAliasTab(String aliasTab) {
        this.aliasTab = aliasTab;
    }

    /**
     * Recupera código javascript que se ejecutará al momento en que se muestra
     * la forma. Tal código puede incluir jQuery. Se recomienda escribir
     * únicamente el nombre de la función con el fin de invocarla. La función
     * invocada deberá estar en un archivo .js con el nombre de la aplicación y
     * deberá tener el nombre de la forma así como el sufijo "_init".
     *
     * @return Código javascript que se ejecutará al momento en que se muestra
     * la forma.
     */
    public String getEventoForma() {
        return eventoForma;
    }

    /**
     * Establece código javascript que se ejecutará al momento en que se muestra
     * la forma. Tal código puede incluir jQuery. Se recomienda escribir
     * únicamente el nombre de la función con el fin de invocarla. La función
     * invocada deberá estar en un archivo .js con el nombre de la aplicación y
     * deberá tener el nombre de la forma así como el sufijo "_init".
     *
     * @param eventoForma código javascript a ejecutarse al momento en que se
     * muestra la forma en la capa vista.
     */
    public void setEventoForma(String eventoForma) {
        this.eventoForma = eventoForma;
    }

    /**
     * Recupera código javascript que se ejecutará al momento en que se muestra
     * el grid. Tal código puede incluir jQuery. Se recomienda escribir
     * únicamente el nombre de la función con el fin de invocarla. La función
     * invocada deberá estar en un archivo .js con el nombre de la aplicación y
     * deberá tener el nombre de la forma así como el sufijo "_grid_init".
     *
     * @return Código javascript que se ejecutará al momento en que se muestra
     * el grid.
     */
    public String getEventoGrid() {
        return eventoGrid;
    }

    /**
     * Recupera código javascript que se ejecutará al momento en que se muestra
     * el grid. Tal código puede incluir jQuery. Se recomienda escribir
     * únicamente el nombre de la función con el fin de invocarla. La función
     * invocada deberá estar en un archivo .js con el nombre de la aplicación y
     * deberá tener el nombre de la forma así como el sufijo "_grid_init".
     *
     * @param eventoGrid Código javascript que se ejecutará al momento en que se
     * muestra el grid.
     */
    public void setEventoGrid(String eventoGrid) {
        this.eventoGrid = eventoGrid;
    }

    /**
     * Recupera las instrucciones relacionadas a la forma
     *
     * @return Instrucciones relacionada a la forma
     */
    public String getInstrucciones() {
        return instrucciones;
    }

    /**
     * Establece las instrucciones relacionadas a la forma
     *
     * @param instrucciones Intrucciones
     */
    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    /**
     * Recupera la marca para mostrar otras formas relacionadas a la forma
     *
     * @return Verdadero si muestra las formas relacionadas, falso de otro modo.
     */
    public boolean isMuestraFormasForeaneas() {
        return muestraFormasForeaneas;
    }

    public Integer getClaveTipoPresentacionFormaForanea() {
        return claveTipoPresentacionFormaForanea;
    }

    public void setClaveTipoPresentacionFormaForanea(Integer claveTipoPresentacionFormaForanea) {
        this.claveTipoPresentacionFormaForanea = claveTipoPresentacionFormaForanea;
    }

    /**
     * Establece la marca para mostrar otras formas relacionadas a la forma
     *
     * @param muestraFormasForeaneas Verdadero para inidcar que se deben mostrar
     * las formas foraneas, falso de otro modo
     */
    public void setMuestraFormasForeaneas(boolean muestraFormasForeaneas) {
        this.muestraFormasForeaneas = muestraFormasForeaneas;
    }

    /**
     * Recupera la marca para activar lo necesario para llevar a cabo la
     * duplicación de un registro desde la capa vista Para tal efecto debe
     * hacerse uso del método duplicate de la consulta, o deberá sobreescribirse
     * el método en la clase de la entidad asociada.
     *
     * @return Verdadero si se permite la duplicación, falso de otro modo
     */
    public boolean isPermiteDuplicarRegistro() {
        return permiteDuplicarRegistro;
    }

    /**
     * Establece la marca para activar la duplicación de un registro
     *
     * @param permiteDuplicarRegistro Verdadero si se permite la duplicación,
     * falso de otro modo
     */
    public void setPermite_duplicar_registro(boolean permiteDuplicarRegistro) {
        this.permiteDuplicarRegistro = permiteDuplicarRegistro;
    }

    /**
     * Recupera la marca para activar los mecanismos que permiten agregar
     * comentarios asociados a la forma en la capa vista
     *
     * @return Verdadero si se permite la inserción de comentarios en la forma.
     * Los mecanismos de inserción se dan desde la capa vista.
     */
    public boolean isPermiteInsertarComentario() {
        return permiteInsertarComentario;
    }

    /**
     * Establece la marca para activar los mecanismos que permiten agregar
     * comentarios asociados a la forma en la capa vista
     *
     * @param permiteInsertarComentario Verdadero si se permite la inserción de
     * comentarios en la forma. Los mecanismos de inserción se dan desde la capa
     * vista.
     */
    public void setPermiteInsertarComentario(boolean permiteInsertarComentario) {
        this.permiteInsertarComentario = permiteInsertarComentario;
    }

    /**
     * Recupera la marca para activar los mecanismos que permiten a los usuarios
     * guardar un registro como plantilla
     *
     * @return Verdadero si se permite la inserción de registro como plantilla.
     */
    public boolean isPermiteGuardarComoPlantilla() {
        return permiteGuardarComoPlantilla;
    }

    /**
     * Establece la marca para activar los mecanismos que permiten a los
     * usuarios guardar un registro como plantilla
     *
     * @param permiteGuardarComoPlantilla Verdadero si se permite la inserción
     * de registro como plantilla.
     */
    public void setPermiteGuardarComoPlantilla(boolean permiteGuardarComoPlantilla) {
        this.permiteGuardarComoPlantilla = permiteGuardarComoPlantilla;
    }

    public boolean isPermiteMultiseleccion() {
        return permiteMultiseleccion;
    }

    public void setPermiteMultiseleccion(boolean permiteMultiseleccion) {
        this.permiteMultiseleccion = permiteMultiseleccion;
    }

    public Integer getFrecuenciaActualizacion() {
        return frecuenciaActualizacion;
    }

    public void setFrecuenciaActualizacion(Integer frecuenciaActualizacion) {
        this.frecuenciaActualizacion = frecuenciaActualizacion;
    }

    public Boolean getPrefiltro() {
        return prefiltro;
    }

    public void setPrefiltro(Boolean prefiltro) {
        this.prefiltro = prefiltro;
    }
    
    
    /**
     * Recupera el objeto <code>com.administrax.modelo.Consulta</code>de la
     * forma
     *
     * @return Consulta asociada a la forma
     */
    /*public Consulta getConsulta() {
     return consulta;
     }*/
    /**
     * Establece el objeto <code>com.administrax.modelo.Consulta</code>de la
     * forma
     *
     * @param consulta Consulta asociada a la forma
     */
    /*public void setConsulta(Consulta consulta) {
     this.consulta = consulta;
     }*/
    /**
     * Establece el objeto <code>com.administrax.modelo.Consulta</code> de la
     * forma a partir de los parámetros del constructor
     *
     * @param accion Tipo de consulta (SELECT, INSERT, UPDATE, LOG)
     * @param pk Valor de la llave primaria de la consulta
     * @param w Clausula WHERE de la consulta
     * @param reglasDeReemplazo <code>ArrayList</code> que contiene los
     * parámetros que serán reemplazados al momento de resolver la consulta
     * @param oDb Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de la conexión a la db
     * @throws Fallo Si ocurren problemas al ejecutar la consulta en la base de
     * datos
     */
    /*public void setConsulta(String accion, String pk, String w, ArrayList reglasDeReemplazo, Conexion oDb) throws Fallo {
     try {
     this.consulta = new Consulta(this.claveForma, accion, pk, w, reglasDeReemplazo, oDb);
     } catch (Exception e) {
     throw new Fallo(e.getMessage());
     }
     }*/
    /**
     * Establece el objeto <code>com.administrax.modelo.Consulta</code> de la
     * forma a partir de los parámetros del constructor y considerando los
     * parámetros de paginación del grid
     *
     * @param accion Tipo de consulta (SELECT, INSERT, UPDATE, LOG)
     * @param pk Valor de la llave primaria de la consulta
     * @param w Clausula WHERE de la consulta
     * @param reglasDeReemplazo <code>ArrayList</code> que contiene los
     * parámetros que serán reemplazados al momento de resolver la consulta
     * @param registros Número de registros que contiene la página
     * @param pagina Número de la página que se va a presentar en el grid
     * @param sidx Columna sobre la que se realizará el ordenamiento
     * @param sord Tipo de ordenamiento, ASC o DESC
     * @param oDb Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de la conexión a la db
     * @throws Fallo Si ocurren problemas al ejecutar la consulta en la base de
     * datos
     */
    /*public void setConsulta(String accion, String pk, String w, ArrayList reglasDeReemplazo, Integer registros, Integer pagina, String sidx, String sord, Conexion oDb) throws Fallo {
     try {
     this.consulta = new Consulta(this.claveForma, accion, pk, w, reglasDeReemplazo, registros, pagina, sidx, sord, oDb);
     } catch (Exception e) {
     throw new Fallo(e.getMessage());
     }
     }*/
    /**
     * Establece <code>ArrayList</code> con reportes asociados a la forma
     *
     * @param reportes <code>ArrayList</code> de reportes asociados a la forma
     */
    public void setReportes(ArrayList<Reporte> reportes) {
        this.reportes = reportes;
    }

    /**
     * Recupera <code>ArrayList</code> con reportes asociados a la forma
     *
     * @param clavePerfil Clave del perfil del usuario
     * @param cx Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de la conexión a la db
     * @return              <code>ArrayList</code> con reportes asociados a la forma
     * @throws Fallo En caso de ocurrir problemas al acceder a la base de datos
     */
    public ArrayList<Reporte> getReportes() throws Fallo {
        this.reportes = new ArrayList<Reporte>();
        Reporte reporte;
        ResultSet rsReportes;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        String s = "SELECT * FROM be_reporte WHERE clave_forma=".concat(Integer.toString(this.claveForma)).concat(" AND clave_perfil=").concat(super.getUsuario().getClavePerfil().toString());

        try {
            rsReportes = oDb.getRs(s);

            while (rsReportes.next()) {
                reporte = new Reporte(
                        rsReportes.getInt("clave_reporte"),
                        rsReportes.getInt("clave_forma"),
                        rsReportes.getString("reporte"),
                        rsReportes.getString("jsp"),
                        rsReportes.getString("jrxml"),
                        rsReportes.getString("etiqueta_tick"),
                        rsReportes.getString("color_series"),
                        rsReportes.getString("consulta").replace("%clave_empleado", super.getUsuario().getClave().toString())
                        .replace("%clave_oficina", super.getUsuario().getClaveOficina().toString())
                        .replace("%clave_perfil", super.getUsuario().getClavePerfil().toString()),
                        TipoReporte.values()[rsReportes.getInt("clave_tipo_reporte") - 1],
                        rsReportes.getInt("generar_en_insercion") == 0 ? false : true,
                        rsReportes.getInt("generar_en_actualizacion") == 0 ? false : true);

                this.reportes.add(reporte);
            }

            rsReportes.close();
            return reportes;
        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }
    }

    /**
     * Recupera la marca de definición del grid
     *
     * @return Verdadero si es una definición del grid, falso de otra forma
     */
    public boolean isDefinicionDelGrid() {
        return definicionDeGrid;
    }

    /**
     * Establece la marca de definición del grid
     *
     * @param definicionDeGrid Verdadero si es una definición del grid, falso de
     * otra forma
     */
    public void setDefinicionDelGrid(boolean definicionDeGrid) {
        this.definicionDeGrid = definicionDeGrid;
    }

    /**
     * Recupera <code>ArrayList</code> con notas asociadas a la forma
     *
     * @return
     */
    public ArrayList<Nota> getNotas() {
        return notas;
    }

    /**
     * Establece comentarios asociados a la forma
     *
     * @param notas <code>ArrayList</code> de comentarios asociados a la forma
     */
    public void setNotas(ArrayList<Nota> notas) {
        this.notas = notas;
    }

    /**
     * Recupera comentarios asociados a la forma desde la base de datos
     *
     * @param claveRegistro Llave primaria de la forma
     * @param cx Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de la conexión a la db
     * @return              <code>ArrayList</code> de comentarios asociados a la forma
     * @throws Fallo si ocurre un error al acceder a la base de datos
     */
    public ArrayList<Nota> getNotas(Integer claveRegistro) throws Fallo {
        this.notas = new ArrayList<Nota>();
        Nota nota;
        ResultSet rsNotas;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        String s;
        try {
            /* //Es necesario verificar si ya tenemos las fotos en el caché
             s ="SELECT DISTINCT empleado.clave_empleado, empleado.foto FROM empleado, nota WHERE empleado.clave_empleado=nota.clave_empleado AND nota.clave_forma=".concat(Integer.toString(this.claveForma)).concat(" AND nota.clave_registro=").concat(claveRegistro.toString());
             rsNotas= oDb.getRs(s);
             while (rsNotas.next()) {
            
             } */

            s = "SELECT be_empleado.nombre + ' ' + be_empleado.apellido_paterno + ' ' + be_empleado.apellido_materno as nombre, be_empleado.foto, be_nota_forma.* "
                    + "FROM be_nota_forma, be_empleado WHERE be_nota_forma.clave_empleado=be_empleado.clave_empleado AND clave_forma=".concat(Integer.toString(this.claveForma)).concat(" AND clave_registro=").concat(claveRegistro.toString());
            rsNotas = oDb.getRs(s);

            while (rsNotas.next()) {
                nota = new Nota(
                        rsNotas.getInt("clave_nota"),
                        rsNotas.getInt("clave_forma"),
                        rsNotas.getInt("clave_registro"),
                        rsNotas.getInt("clave_empleado"),
                        rsNotas.getString("titulo"),
                        rsNotas.getString("mensaje"),
                        rsNotas.getString("fecha_nota"),
                        rsNotas.getString("foto"),
                        rsNotas.getString("nombre"));

                this.notas.add(nota);
            }

            rsNotas.close();
            return notas;
        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }
    }

    /**
     * Establece formas foraneas asociadas a la forma
     *
     * @param formasForaneas <code>ArrayList</code> de formas asociadas a la
     * forma
     */
    public void setFormasForaneas(ArrayList<Forma> formasForaneas) {
        this.formasForaneas = formasForaneas;
    }

    /**
     * Recupera formas foraneas asociadas a la forma de la base de datos
     *
     * @param cx Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de la conexión a la db
     * @return <code>ArrayList</code> de formas asociadas a la forma
     * @throws Fallo si ocurre un error al acceder a la base de datos
     */
    public ArrayList<Forma> getFormasForaneas() throws Fallo {
        this.formasForaneas = new ArrayList<Forma>();
        Forma forma;
        ResultSet rsFormasForaneas;
        Conexion oDb = super.getUsuario().getCx();
        String s = "SELECT be_forma.clave_forma, be_forma.forma, be_forma.clave_tipo_presentacion_forma_foranea FROM be_forma, be_permiso_forma WHERE be_forma.clave_forma=be_permiso_forma.clave_forma AND be_permiso_forma.clave_perfil="
                .concat(super.getUsuario().getClavePerfil().toString())
                .concat(" AND be_permiso_forma.clave_permiso=1 AND be_forma.clave_forma_padre=").concat(Integer.toString(this.claveForma)).concat(" ORDER BY orden_tab");

        try {
            rsFormasForaneas = oDb.getRs(s);

            while (rsFormasForaneas.next()) {
                forma = new Forma();
                forma.setClaveForma(rsFormasForaneas.getInt("clave_forma"));
                forma.setForma(rsFormasForaneas.getString("forma"));
                forma.setClaveTipoPresentacionFormaForanea(rsFormasForaneas.getInt("clave_tipo_presentacion_forma_foranea"));
                this.formasForaneas.add(forma);
            }

            rsFormasForaneas.close();
            return this.formasForaneas;
        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }
    }

    public ArrayList<Portlet> getPortlets() {
        return portlets;
    }

    public ArrayList<Portlet> getPortlets(Conexion cx) throws Fallo {
        ResultSet rs;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        this.portlets = new ArrayList<Portlet>();
        String s = "SELECT clave_portlet, clave_forma, clave_perfil, clave_tipo,titulo,codigo, orden"
                .concat(" FROM be_portlet_forma")
                .concat(" WHERE clave_forma=").concat(String.valueOf(this.claveForma));

        try {
            rs = oDb.getRs(s);

            while (rs.next()) {
                this.portlets.add(new Portlet(rs.getInt("clave_portlet"), rs.getInt("clave_forma"), rs.getInt("clave_perfil"), rs.getInt("clave_tipo"), rs.getString("titulo"), rs.getString("codigo"), rs.getInt("orden")));
            }

            rs.close();
        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }

        return portlets;
    }

    public void setPortlets(ArrayList<Portlet> portlets) {
        this.portlets = portlets;
    }

    /**
     * Inserta registro de forma en la base de datos considerando los permisos
     * del administrador, las consultas relacionadas a la forma para mostrar el
     * grid, crear el formulario de inserción y actualización, y presentar la
     * información de actividades recientes, así como el diccionario de datos de
     * la forma para el perfil de administrador
     *
     * @param claveEmpleado
     * @param ip
     * @param browser
     * @param forma
     * @param cx
     * @return
     * @throws Fallo
     */
    public String insert() throws Fallo {
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        resultado.append(super.insert(true));

        if (resultado.toString().split("<error>").length > 1) {
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }

        this.claveForma = Integer.parseInt(resultado.toString().split("<pk>")[1].substring(0, resultado.toString().split("<pk>")[1].indexOf("</pk>")));

        try {
            oDb.execute("INSERT INTO be_permiso_forma (clave_permiso,clave_forma,clave_perfil) VALUES(1,".concat(String.valueOf(this.claveForma)).concat(",1)"));
            oDb.execute("INSERT INTO be_permiso_forma (clave_permiso,clave_forma,clave_perfil) VALUES(2,".concat(String.valueOf(this.claveForma)).concat(",1)"));
            oDb.execute("INSERT INTO be_permiso_forma (clave_permiso,clave_forma,clave_perfil) VALUES(3,".concat(String.valueOf(this.claveForma)).concat(",1)"));
            oDb.execute("INSERT INTO be_permiso_forma (clave_permiso,clave_forma,clave_perfil) VALUES(4,".concat(String.valueOf(this.claveForma)).concat(",1)"));
        } catch (Exception e) {
            resultado.append("<warning><![CDATA[Error al insertar permisos de administrador a la nueva forma: ").append(e.getMessage()).append("]]></warning>");
        }

        if (super.getCampos().get("tabla").getValor() != null) {
            try {
                oDb.execute("INSERT INTO be_consulta_forma (clave_forma, clave_perfil, consulta, tipo_consulta) VALUES("
                        .concat(String.valueOf(this.claveForma))
                        .concat(",1,'select * from ")
                        .concat(super.getCampos().get("tabla").getValor())
                        .concat("','select')"));

                oDb.execute("INSERT INTO be_consulta_forma (clave_forma, clave_perfil, consulta, tipo_consulta) VALUES("
                        .concat(String.valueOf(this.claveForma))
                        .concat(",1,'select * from ")
                        .concat(super.getCampos().get("tabla").getValor())
                        .concat(" where ")
                        .concat(super.getCampos().get("llave_primaria").getValor())
                        .concat("=$pk','insert')"));

                oDb.execute("INSERT INTO be_consulta_forma (clave_forma, clave_perfil, consulta, tipo_consulta) VALUES("
                        .concat(String.valueOf(this.claveForma))
                        .concat(",1,'select * from ")
                        .concat(super.getCampos().get("tabla").getValor())
                        .concat(" where ")
                        .concat(super.getCampos().get("llave_primaria").getValor())
                        .concat("=$pk','update')"));

                oDb.execute("INSERT INTO be_consulta_forma (clave_forma, clave_perfil, consulta, tipo_consulta) VALUES("
                        .concat(String.valueOf(this.claveForma))
                        .concat(",1,'select * from ")
                        .concat(super.getCampos().get("tabla").getValor())
                        .concat("','foreign')"));

                oDb.execute("INSERT INTO be_consulta_forma (clave_forma, clave_perfil, consulta, tipo_consulta) VALUES("
                        .concat(String.valueOf(this.claveForma))
                        .concat(",1,'select * from ")
                        .concat(super.getCampos().get("tabla").getValor())
                        .concat(" where ")
                        .concat(super.getCampos().get("llave_primaria").getValor())
                        .concat("=$pk','lookup')"));

                if (oDb.getDbType() == mx.org.fide.modelo.Conexion.DbType.MSSQL) {
                    oDb.execute("INSERT INTO be_consulta_forma (clave_forma, clave_perfil, consulta, tipo_consulta) VALUES("
                            .concat(String.valueOf(this.claveForma))
                            .concat(",1,'select top 10 ba.clave_bitacora, ba.fecha, case isnull(foto,'''')  \n")
                            .concat(" when '''' then ''<img src=\"img/alguien.jpg\" class=\"bitacora\" />''\n")
                            .concat(" else ''<img src=\"'' + foto  + ''\" class=\"bitacora\" />'' end as foto,\n")
                            .concat(" e.nombre + '' '' + e.apellido_paterno as nombre,\n")
                            .concat(" te.tipo_evento as clave_tipo_evento,\n")
                            .concat(" lower(f.alias_tab) as entidad,\n")
                            .concat(" x.").concat(super.getCampos().get("llave_primaria").getValor()).concat(" as descripcion_entidad,\n")
                            .concat(" ba.clave_forma,\n")
                            .concat(" ba.clave_registro\n")
                            .concat(" from be_bitacora ba,\n")
                            .concat(" be_empleado e, be_tipo_evento te,be_forma f,\n")
                            .concat(super.getCampos().get("tabla").getValor()).concat(" x\n")
                            .concat(" where")
                            .concat(" ba.clave_empleado=e.clave_empleado\n")
                            .concat(" and ba.clave_tipo_evento=te.clave_tipo_evento\n")
                            .concat(" and ba.clave_forma=f.clave_forma\n")
                            .concat(" and ba.clave_tipo_evento IN (2,3)\n")
                            .concat(" and x.").concat(super.getCampos().get("llave_primaria").getValor()).concat("= ba.clave_registro\n")
                            .concat(" and ba.clave_forma=").concat(String.valueOf(this.claveForma)).concat("\n")
                            .concat(" order by ba.fecha desc")
                            .concat("','log')"));
                } else if (oDb.getDbType() == mx.org.fide.modelo.Conexion.DbType.MYSQL) {
                    oDb.execute("INSERT INTO be_consulta_forma (clave_forma, clave_perfil, consulta, tipo_consulta) VALUES("
                            .concat(String.valueOf(this.claveForma))
                            .concat(",1,'select ba.clave_bitacora, ba.fecha, case isnull(foto) \n")
                            .concat(" when 1 then ''<img src=\"img/alguien.jpg\" class=\"bitacora\" />''\n")
                            .concat(" else concat(''<img src=\"'',foto,''\" class=\"bitacora\" />'') end as foto,\n")
                            .concat(" concat(e.nombre,'' '',e.apellido_paterno,'' '',e.apellido_materno) as nombre,\n")
                            .concat(" te.tipo_evento as clave_tipo_evento,\n")
                            .concat(" lower(f.alias_tab) as entidad,\n")
                            .concat(" x.").concat(super.getCampos().get("llave_primaria").getValor()).concat(" as descripcion_entidad,\n")
                            .concat(" ba.clave_forma,\n")
                            .concat(" ba.clave_registro\n")
                            .concat(" from be_bitacora ba,\n")
                            .concat(" be_empleado e, be_tipo_evento te,be_forma f,\n")
                            .concat(super.getCampos().get("tabla").getValor()).concat(" x\n")
                            .concat(" where")
                            .concat(" ba.clave_empleado=e.clave_empleado\n")
                            .concat(" and ba.clave_tipo_evento=te.clave_tipo_evento\n")
                            .concat(" and ba.clave_forma=f.clave_forma\n")
                            .concat(" and ba.clave_tipo_evento IN (2,3)\n")
                            .concat(" and x.").concat(super.getCampos().get("llave_primaria").getValor()).concat("= ba.clave_registro\n")
                            .concat(" and ba.clave_forma=").concat(String.valueOf(this.claveForma)).concat("\n")
                            .concat(" order by ba.fecha desc limit 1,10")
                            .concat("','log')"));
                } else if (oDb.getDbType() == mx.org.fide.modelo.Conexion.DbType.ORACLE) {
                    oDb.execute("INSERT INTO be_consulta_forma (clave_forma, clave_perfil, consulta, tipo_consulta) VALUES("
                            .concat(String.valueOf(this.claveForma))
                            .concat(",1,'select ba.clave_bitacora, ba.fecha, case NVL(foto,''es nulo'') \n")
                            .concat(" when ''es nulo'' then ''<img src=\"img/alguien.jpg\" class=\"bitacora\" />''\n")
                            .concat(" else concat(''<img src=\"'',concat(foto,''\" class=\"bitacora\" />'')) end as foto,\n")
                            .concat(" concat(e.nombre,concat('' '',e.apellido_paterno)) as nombre,\n")
                            .concat(" te.tipo_evento as clave_tipo_evento,\n")
                            .concat(" lower(f.alias_tab) as entidad,\n")
                            .concat(" x.").concat(super.getCampos().get("llave_primaria").getValor()).concat(" as descripcion_entidad,\n")
                            .concat(" ba.clave_forma,\n")
                            .concat(" ba.clave_registro\n")
                            .concat(" from be_bitacora ba,\n")
                            .concat(" be_empleado e, be_tipo_evento te,be_forma f,\n")
                            .concat(super.getCampos().get("tabla").getValor()).concat(" x\n")
                            .concat(" where")
                            .concat(" ba.clave_empleado=e.clave_empleado\n")
                            .concat(" and ba.clave_tipo_evento=te.clave_tipo_evento\n")
                            .concat(" and ba.clave_forma=f.clave_forma\n")
                            .concat(" and ba.clave_tipo_evento IN (2,3)\n")
                            .concat(" and x.").concat(super.getCampos().get("llave_primaria").getValor()).concat("= ba.clave_registro\n")
                            .concat(" and ba.clave_forma=").concat(String.valueOf(this.claveForma)).concat("\n")
                            .concat(" and ba.clave_forma=").concat(String.valueOf(this.claveForma)).concat("\n")
                            .concat(" and rownum >=1 and rownum <=10 ")
                            .concat(" order by ba.fecha desc ")
                            .concat("','log')"));
                }

            } catch (Exception e) {
                resultado.append("<warning><![CDATA[Error al insertar consultas de administrador a la nueva forma: ").append(e.getMessage()).append("]]></warning>");
            }
        }

        try {
            if (oDb.getDbType() == mx.org.fide.modelo.Conexion.DbType.MSSQL) {
                String s = "INSERT INTO be_campo_forma"
                        .concat("(clave_forma, clave_perfil,campo,alias_campo,obligatorio,tipo_control,evento,clave_forma_foranea,")
                        .concat(" filtro_foraneo,edita_forma_foranea,ayuda,activo,tamano,visible)")
                        .concat(" SELECT ").concat(String.valueOf(this.claveForma)).concat(",1,sc.name,sc.name,0,null,null,null,null,0,null,1,")
                        .concat(" CASE sc.xtype ")
                        .concat("    WHEN 56 THEN 70 ")
                        .concat("    WHEN 104 THEN 70 ")
                        .concat("    WHEN 167 THEN prec ")
                        .concat("    WHEN 60 THEN 100 ")
                        .concat("    WHEN 62 THEN 100 ")
                        .concat("    WHEN 106 THEN 100 ")
                        .concat("    WHEN 35 THEN 250 ")
                        .concat("    WHEN 61 THEN 150 ")
                        .concat("    WHEN 40 THEN 150 ")
                        .concat(" END as tamano, 1")
                        .concat(" FROM SYSOBJECTS so, syscolumns sc WHERE so.XTYPE='u' and so.id=sc.id and so.name='")
                        .concat(super.getCampos().get("tabla").getValor())
                        .concat("'");
                oDb.execute(s);

            } else if (oDb.getDbType() == mx.org.fide.modelo.Conexion.DbType.MYSQL) {
                String s = "INSERT INTO be_campo_forma"
                        .concat("(clave_forma, clave_perfil,campo,alias_campo,obligatorio,tipo_control,evento,clave_forma_foranea,")
                        .concat(" filtro_foraneo,edita_forma_foranea,ayuda,activo,tamano,visible)")
                        .concat(" SELECT ").concat(String.valueOf(this.claveForma)).concat(",1,column_name,column_name,0,null,null,null,null,0,null,1,")
                        .concat(" CASE data_type ")
                        .concat("    WHEN 'int' THEN 70 ")
                        .concat("    WHEN 'bigint' THEN 70 ")
                        .concat("    WHEN 'bit' THEN 70 ")
                        .concat("    WHEN 'varchar' THEN character_maximum_length ")
                        .concat("    WHEN 'decimal' THEN 100 ")
                        .concat("    WHEN 'double' THEN 100 ")
                        .concat("    WHEN 'float' THEN 100 ")
                        .concat("    WHEN 'text' THEN 250 ")
                        .concat("    WHEN 'longtext' THEN 250 ")
                        .concat("    WHEN 'date' THEN 150 ")
                        .concat("    WHEN 'datetime' THEN 150 ")
                        .concat("    WHEN 'tinyint' THEN 70 ")
                        .concat(" END as tamano, 1")
                        .concat(" FROM information_schema.`COLUMNS` C WHERE table_name='").concat(super.getCampos().get("tabla").getValor()).concat("' AND table_schema='").concat(oDb.getDb()).concat("'");
                oDb.execute(s);
            } else if (oDb.getDbType() == mx.org.fide.modelo.Conexion.DbType.ORACLE) {
                ResultSet rsTemp = oDb.getRs("SELECT ".concat(String.valueOf(this.claveForma)).concat(" as clave_forma,1 as clave_perfil,lower(column_name) as campo,lower(column_name) as alias_campo,")
                        .concat("0 as obligatorio ,1 as activo,data_length*4 as tamano, 1 as visible")
                        .concat(" FROM SYS.ALL_TAB_COLS WHERE lower(table_name)='").concat(super.getCampos().get("tabla").getValor()).concat("'"));

                while (rsTemp.next()) {
                    String s = "INSERT INTO be_campo_forma"
                            .concat("(clave_forma, clave_perfil,campo,alias_campo,obligatorio,")
                            .concat("dato_sensible,activo,tamano,visible)")
                            .concat(" VALUES(").concat(rsTemp.getString("clave_forma")).concat(",")
                            .concat(rsTemp.getString("clave_perfil")).concat(",")
                            .concat("'").concat(rsTemp.getString("campo")).concat("',")
                            .concat("'").concat(rsTemp.getString("alias_campo")).concat("',")
                            .concat(rsTemp.getString("obligatorio")).concat(",")
                            .concat(rsTemp.getString("activo")).concat(",")
                            .concat(rsTemp.getString("tamano")).concat(",")
                            .concat(rsTemp.getString("visible")).concat(")");

                    oDb.execute(s);
                }

            }
        } catch (Exception e) {
            resultado.append("<warning><![CDATA[Error al insertar el diccionario de datos del administrador a la nueva forma: ").append(e.getMessage()).append("]]></warning>");
        }

        return resultado.toString();
    }

    /**
     * Actualiza registro de forma en la base de datos
     *
     * @param claveEmpleado Clave del usuario que realiza la actualización
     * @param ip IP del usuario que realiza la actualización
     * @param browser Navegador con el que se realiza la actualización
     * @param forma Clave de la forma que se actualiza
     * @param cx Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de la conexión a la db
     * @return Codigo XML indicando la sentencia ejecutada para actualizar la
     * forma, la llave primaria así como cualquier error o advertencia
     * @throws Fallo Si ocurre algún error al momento de interactuar con la base
     * de datos
     */
    public String update() throws Fallo {
        StringBuilder resultado = new StringBuilder();
        return resultado.append(super.update(true)).toString();
    }

    /**
     * Elimina registros de forma de la base de datos
     *
     * @param claveEmpleado Clave del usuario que realiza la actualización
     * @param ip IP del usuario que realiza la actualización
     * @param browser Navegador con el que se realiza la actualización
     * @param forma Clave de la forma que se actualiza
     * @param cx Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de la conexión a la db
     * @return Codigo XML indicando la sentencia ejecutada para actualizar la
     * forma, la llave primaria así como cualquier error o advertencia
     * @throws Fallo Si ocurre algún error al momento de interactuar con la base
     * de datos
     */
    public String delete() throws Fallo {
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        super.setTabla("be_forma");
        super.setLlavePrimaria("clave_forma");

        String s;

        try {
            s = "DELETE FROM be_permiso_forma WHERE clave_forma=".concat(super.getPk());
            oDb.execute(s.toString());
        } catch (Exception e) {
            resultado.append("<warning><![CDATA[No se eliminaron los permisos de la forma: ").append(e.getMessage()).append("]]></warning>");
        }

        try {
            s = "DELETE FROM be_consulta_forma WHERE clave_forma=".concat(super.getPk());
            oDb.execute(s.toString());
        } catch (Exception e) {
            resultado.append("<warning><![CDATA[No se eliminaron los permisos de la forma: ").append(e.getMessage()).append("]]></warning>");
        }

        try {
            s = "DELETE FROM be_campo_forma WHERE clave_forma=".concat(super.getPk());
            oDb.execute(s.toString());
        } catch (Exception e) {
            resultado.append("<warning><![CDATA[No se eliminaron los permisos de la forma: ").append(e.getMessage()).append("]]></warning>");
        }

        resultado.append(super.delete(true,super.getUsuario()));

        if (resultado.toString().split("<error>").length > 1) {
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }

        return resultado.toString();
    }

    /**
     * Duplica registro de forma en la base de datos
     *
     * @param claveEmpleado Clave del usuario que realiza la actualización
     * @param ip IP del usuario que realiza la actualización
     * @param browser Navegador con el que se realiza la actualización
     * @param forma Clave de la forma que se actualiza
     * @param cx Objeto <code>com.administrax.modelo.Conexion</code> que
     * contiene los datos de la conexión a la db
     * @return Codigo XML indicando la sentencia ejecutada para actualizar la
     * forma, la llave primaria así como cualquier error o advertencia
     * @throws Fallo Si ocurre algún error al momento de interactuar con la base
     * de datos
     */
    @Override
    public String duplicate() throws Fallo {
        StringBuilder resultadoXML = new StringBuilder();
        return resultadoXML.append(super.duplicate()).toString();
    }

    /**
     * Genera registros de formas a partir de una consulta a las tablas de
     * sistema y permitiendo hacer uso de comodines.
     *
     * @param aplicacion Clave de la aplicación en donde se crearán las formas
     * @param prefijoTabla Prefijo de las tablas que conforman la aplicación
     * @param usuario Usuario que realiza la importación
     * @return Codigo XML indicando las sentencias ejecutadas para inserción de
     * las formas, sus llaves primarias así como cualquier error o advertencia
     * @throws Fallo SQLException Si ocurre algpun error al acceder a la base de
     * datos
     */
    public String importa(Integer aplicacion, String prefijoTabla, Usuario usuario) throws Fallo {
        StringBuilder resultado = new StringBuilder("");
        Conexion cx = usuario.getCx();
        ResultSet rs;

        try {
            String s = "";
            //Extraer nombre de las tablas
            if (cx.getDbType() == mx.org.fide.modelo.Conexion.DbType.MSSQL) {
                s = "SELECT so.name as table_name, sc.name as column_name FROM SYSOBJECTS so, syscolumns sc WHERE so.XTYPE='u' and so.id=sc.id and so.name LIKE '".concat(prefijoTabla).concat("'");
            } else if (cx.getDbType() == mx.org.fide.modelo.Conexion.DbType.ORACLE) {
                s = "SELECT table_name, column_name FROM SYS.ALL_TAB_COLS WHERE table_name LIKE '".concat(prefijoTabla).concat("' AND owner='CECYTEM' AND column_id=1");
            } else if (cx.getDbType() == mx.org.fide.modelo.Conexion.DbType.MYSQL) {
                s = "SELECT table_name, column_name FROM information_schema.`COLUMNS` WHERE table_name LIKE '".concat(prefijoTabla).concat("' and COLUMN_KEY='PRI'").concat(" AND table_schema='").concat(cx.getDb()).concat("'");
            }
            rs = cx.getRs(s);
            Integer i = 0;
            while (rs.next()) {
                //1. Crear objeto forma
                Consulta c = new Consulta(3, "insert", "0", "",null, usuario);
                c.getCampos().get("clave_aplicacion").setValor(aplicacion.toString());
                c.getCampos().get("forma").setValor(rs.getString("table_name"));
                c.getCampos().get("tabla").setValor(rs.getString("table_name"));
                c.getCampos().get("llave_primaria").setValor(rs.getString("column_name"));
                c.getCampos().get("evento_forma").setValor("//".concat(rs.getString("table_name").concat("_init();")));
                c.getCampos().get("alias_tab").setValor(rs.getString("table_name"));
                c.getCampos().get("permite_duplicar_registro").setValor("0");
                c.getCampos().get("clave_tipo_grid").setValor("1");

                Forma f = new Forma(c, true);

                //2. Llamar a forma.insert(); 
                resultado.append("<importacion><tabla>").append(rs.getString("table_name")).append("</tabla>").append(f.insert()).append("</importacion>");
                i++;
            }

            if (resultado.toString().contains("warning")) {
                resultado.append("<resumen>").append(i).append(" tabla(s) importada(s) con errores, verifique").append("</resumen>");
            } else {
                resultado.append("<resumen>").append(i).append(" tabla(s) importada(s) exitosamente").append("</resumen>");
            }

            return resultado.toString();

        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            cx.cierraConexion();
        }
    }
    
    public Boolean exporta_excel(String archivo, Usuario usuario) throws Fallo {
        return true;
    }
}
