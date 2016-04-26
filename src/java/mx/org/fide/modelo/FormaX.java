package mx.org.fide.modelo;

import mx.org.fide.reporte.Reporte;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mx.org.fide.reporte.Reporte.TipoReporte;

/**
 *
 * @author Daniel
 */
public class FormaX {

    private int claveForma;
    private String forma;
    private boolean select;
    private boolean insert;
    private boolean update;
    private boolean delete;
    private boolean sensitiveData;
    private boolean report;
    private String aliasTab;
    private Integer claveAplicacion;
    private String evento;
    private String eventoGrid;
    private String instrucciones;
    private boolean prefiltro;
    private boolean permiteDuplicarRegistro;
    private boolean permiteInsertarComentario;
    private boolean permiteMultiseleccion;
    private Consulta consulta;
    private String tabla;
    private String llavePrimaria;
    private String campoSeguimientoFlujo;
    private Integer claveTipoGrid;
    private ArrayList<UsuarioResponsable> usuariosFlujo;
    private ArrayList<Reporte> reportes;
    private ArrayList<Nota> notas;
    private ArrayList<FormaX> formasForaneas;
    private ArrayList<UsuarioResponsable> usuariosNotas;
    private ArrayList<Portlet> portlets;
    
    private boolean definicionDeGrid;
    private boolean muestraFormasForeaneas;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isSensitiveData() {
        return sensitiveData;
    }

    public void setSensitiveData(boolean sensitiveData) {
        this.sensitiveData = sensitiveData;
    }

    public boolean isUpdate() {
        return update;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getLlavePrimaria() {
        return llavePrimaria;
    }

    public void setLlavePrimaria(String llavePrimaria) {
        this.llavePrimaria = llavePrimaria;
    }

    public String getCampoSeguimientoFlujo() {
        return campoSeguimientoFlujo;
    }

    public void setCampoSeguimientoFlujo(String campoSeguimientoFlujo) {
        this.campoSeguimientoFlujo = campoSeguimientoFlujo;
    }

    public Integer getClaveTipoGrid() {
        return claveTipoGrid;
    }

    public void setClaveTipoGrid(Integer claveTipoGrid) {
        this.claveTipoGrid = claveTipoGrid;
    }
    
    public int getClaveForma() {
        return claveForma;
    }

    public void setClaveForma(int claveForma) {
        this.claveForma = claveForma;
    }

    public void setClaveForma(int claveForma, Usuario usuario) throws Fallo {

        this.claveForma = claveForma;
        Conexion oDb = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());

        //En caso de que se solicite una forma con clave negativa
        if (this.claveForma < 0) {
            this.claveAplicacion=1;
            this.aliasTab = "(Reservado)";
            this.evento = null;
            this.eventoGrid = null;
            this.forma = "(Reservado)";
            this.tabla = "(Reservado)";
            this.llavePrimaria = "(Reservado)";
            this.campoSeguimientoFlujo = "(Reservado)";
            this.instrucciones = null;
            this.prefiltro = false;
            this.select = false;
            this.insert = false;
            this.update = false;
            this.delete = false;
            this.sensitiveData = false;
            return;
        }

        ResultSet rsForma;

        try {
            String s="";
            if (oDb.getDbType()==Conexion.DbType.MSSQL){
                s = "select f.tabla, f.llave_primaria, f.alias_tab,f.evento,f.evento_grid,f.instrucciones,f.forma,f.prefiltro,f.permite_duplicar_registro,f.permite_insertar_comentarios,f.muestra_formas_foraneas,f.campo_seguimiento_flujo,f.clave_tipo_grid,f.clave_aplicacion, f.permite_multiseleccion,"
                    + "(select top 1 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=1) as selectx,"
                    + "(select top 1 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=2) as insertx,"
                    + "(select top 1 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=3) as updatex,"
                    + "(select top 1 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=4) as deletex,"
                    + "(select top 1 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=5) as sensitivedata, "
                    + "(select top 1 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=6) as reportex"
                    + " from "
                    + " forma f, aplicacion a, perfil_aplicacion pa "
                    + " where a.clave_aplicacion=f.clave_aplicacion "
                    + " and a.clave_aplicacion=pa.clave_aplicacion "
                    + " and pa.clave_perfil=" + usuario.getClavePerfil()
                    + " and f.clave_forma=" + claveForma;
            } else if (oDb.getDbType()==Conexion.DbType.MYSQL) {
                s = "select f.tabla, f.llave_primaria, f.alias_tab,f.evento,f.evento_grid,f.instrucciones,f.forma,f.prefiltro,f.permite_duplicar_registro,f.permite_insertar_comentarios,f.muestra_formas_foraneas, f.campo_seguimiento_flujo,f.clave_tipo_grid,f.clave_aplicacion, f.permite_multiseleccion,"
                    + "(select 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=1 limit 1) as selectx,"
                    + "(select 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=2 limit 1) as insertx,"
                    + "(select 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=3 limit 1) as updatex,"
                    + "(select 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=4 limit 1) as deletex,"
                    + "(select 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=5 limit 1) as sensitivedata, "
                    + "(select 1 from permiso_forma where clave_forma=f.clave_forma AND clave_perfil=" + usuario.getClavePerfil() + " AND clave_permiso=6 limit 1 ) as reportex"
                    + " from "
                    + " forma f, aplicacion a, perfil_aplicacion pa "
                    + " where a.clave_aplicacion=f.clave_aplicacion "
                    + " and a.clave_aplicacion=pa.clave_aplicacion "
                    + " and pa.clave_perfil=" + usuario.getClavePerfil()
                    + " and f.clave_forma=" + claveForma;                
            }
            
            rsForma = oDb.getRs(s);

            if (rsForma.next()) {
                this.aliasTab = rsForma.getString("alias_tab");
                this.claveAplicacion = rsForma.getInt("clave_aplicacion");
                this.evento = rsForma.getString("evento");
                this.eventoGrid = rsForma.getString("evento_grid");
                this.forma = rsForma.getString("forma");
                this.tabla = rsForma.getString("tabla");
                this.llavePrimaria = rsForma.getString("llave_primaria");
                this.campoSeguimientoFlujo = rsForma.getString("campo_seguimiento_flujo");
                this.instrucciones = rsForma.getString("instrucciones");
                this.prefiltro = rsForma.getBoolean("prefiltro");
                this.select = rsForma.getBoolean("selectx");
                this.insert = rsForma.getBoolean("insertx");
                this.update = rsForma.getBoolean("updatex");
                this.delete = rsForma.getBoolean("deletex");
                this.sensitiveData = rsForma.getBoolean("sensitivedata");
                this.report = rsForma.getBoolean("reportex");
                this.permiteDuplicarRegistro = rsForma.getBoolean("permite_duplicar_registro");
                this.permiteInsertarComentario = rsForma.getBoolean("permite_insertar_comentarios");
                this.muestraFormasForeaneas= rsForma.getBoolean("muestra_formas_foraneas");
                this.claveTipoGrid = rsForma.getInt("clave_tipo_grid");
                this.permiteMultiseleccion = rsForma.getBoolean("permite_multiseleccion");
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

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getAliasTab() {
        return aliasTab;
    }

    public void setAliasTab(String aliasTab) {
        this.aliasTab = aliasTab;
    }

    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getEventoGrid() {
        return eventoGrid;
    }

    public void setEventoGrid(String eventoGrid) {
        this.eventoGrid = eventoGrid;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public boolean isPrefiltro() {
        return prefiltro;
    }

    public void setPrefiltro(boolean prefiltro) {
        this.prefiltro = prefiltro;
    }

    public boolean isPermiteDuplicarRegistro() {
        return permiteDuplicarRegistro;
    }

    public void setPermite_duplicar_registro(boolean permiteDuplicarRegistro) {
        this.permiteDuplicarRegistro = permiteDuplicarRegistro;
    }

    public boolean isPermiteInsertarComentario() {
        return permiteInsertarComentario;
    }

    public void setPermiteInsertarComentario(boolean permiteInsertarComentario) {
        this.permiteInsertarComentario = permiteInsertarComentario;
    }

    public boolean isPermiteMultiseleccion() {
        return permiteMultiseleccion;
    }

    public void setPermiteMultiseleccion(boolean permiteMultiseleccion) { 
        this.permiteMultiseleccion = permiteMultiseleccion;
    }
    
    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public void setConsulta(String accion, String pk, String w, Usuario usuario) throws Fallo {
        try {
            this.consulta = new Consulta(this.claveForma, accion, pk, w, null, usuario);
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public void setConsulta(Integer claveForma, String accion, String pk, String w, Usuario usuario, Integer registros, Integer pagina, String sidx, String sord) throws Fallo {
        try {
            this.consulta = new Consulta(this.claveForma, accion, pk, w, usuario,registros, pagina, sidx, sord);
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public void setConsulta(String accion, String pk, String w, Usuario usuario, Integer registros, Integer pagina, String sidx, String sord, Integer nodo) throws Fallo {
        try {
            this.consulta = new Consulta(this.claveForma, accion, pk, w, usuario, registros, pagina, sidx, sord, nodo);
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }
        
    public ArrayList<UsuarioResponsable> getUsuariosFlujo() {
        return usuariosFlujo;
    }

    public void setUsuariosFlujo(ArrayList<UsuarioResponsable> usuariosFlujo) {
        this.usuariosFlujo = usuariosFlujo;
    }

    /*public void setUsuariosFlujo(Conexion cx, int claveRegistro) throws Fallo {
        this.usuariosFlujo = new ArrayList<UsuarioResponsable>();
        UsuarioResponsable usuarioResponsable;
        ResultSet rsUsuarioFlujo;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        String s = "SELECT email,"
                + " nombre, apellido_paterno, apellido_materno, "
                + " (SELECT flujo FROM flujo_datos WHERE clave_flujo=flujo_datos_forma.clave_flujo) as flujo_dato,"
                + " flujo_datos_forma.proceso,"
                + " flujo_datos_forma.asunto,"
                + " flujo_datos_forma.secuencia,"
                + " CONVERT(varchar(8000),flujo_datos_forma.notificacion) as notificacion,"
                + " flujo_datos_forma.campo_seguimiento_estatus"
                + " FROM flujo_datos_forma, responsable_flujo_datos, empleado_proyecto, empleado "
                + " WHERE flujo_datos_forma.clave_flujo_forma=responsable_flujo_datos.clave_flujo_forma"
                + " AND flujo_datos_forma.clave_forma=" + this.claveForma
                + " AND responsable_flujo_datos.clave_responsable_asignado=empleado_proyecto.clave_rol"
                + " AND empleado.clave_empleado=empleado_proyecto.clave_empleado"
                + " AND responsable_flujo_datos.clave_tipo_responsable=1"
                + " AND flujo_datos_forma.enviar_notificacion=1"
                + " AND  empleado_proyecto.clave_proyecto="+ String.valueOf(claveRegistro) 
                + " UNION "
                + " SELECT DISTINCT email as email_responsable,"
                + " nombre,apellido_paterno,apellido_materno, "
                + " (SELECT flujo FROM flujo_datos WHERE clave_flujo=flujo_datos_forma.clave_flujo) as flujo_dato,"
                + " flujo_datos_forma.proceso,"
                + " flujo_datos_forma.asunto,"
                + " flujo_datos_forma.secuencia,"
                + " CONVERT(varchar(8000),flujo_datos_forma.notificacion) as notificacion, "
                + " flujo_datos_forma.campo_seguimiento_estatus "
                + " FROM flujo_datos_forma, responsable_flujo_datos, empleado "
                + " WHERE flujo_datos_forma.clave_flujo_forma=responsable_flujo_datos.clave_flujo_forma "
                + " AND flujo_datos_forma.clave_forma=" + this.claveForma 
                + " AND responsable_flujo_datos.clave_responsable_asignado=empleado.clave_empleado "
                + " AND flujo_datos_forma.enviar_notificacion=1"
                + " AND responsable_flujo_datos.clave_tipo_responsable=2";

        try {
            rsUsuarioFlujo = oDb.getRs(s);

            while (rsUsuarioFlujo.next()) {

                usuarioResponsable = new UsuarioResponsable(
                        rsUsuarioFlujo.getString("email"),
                        rsUsuarioFlujo.getString("nombre"),
                        rsUsuarioFlujo.getString("apellido_paterno"),
                        rsUsuarioFlujo.getString("apellido_materno"),
                        rsUsuarioFlujo.getString("flujo_dato"),
                        rsUsuarioFlujo.getString("proceso"),
                        rsUsuarioFlujo.getString("asunto"),
                        rsUsuarioFlujo.getInt("secuencia"),
                        rsUsuarioFlujo.getString("notificacion"),
                        rsUsuarioFlujo.getString("campo_seguimiento_estatus"));
                this.usuariosFlujo.add(usuarioResponsable);
            }

            rsUsuarioFlujo.close();
        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }

    } */

    public void setReportes(ArrayList<Reporte> reportes) {
        this.reportes = reportes;
    }

    public ArrayList<Reporte> getReportes(Usuario usuario) throws Fallo {
        this.reportes = new ArrayList<Reporte>();
        Reporte reporte;
        ResultSet rsReportes;
        Conexion oDb = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
        String s = "SELECT * FROM reporte WHERE clave_forma=".concat(Integer.toString(this.claveForma)).concat(" AND clave_perfil=").concat(usuario.getClavePerfil().toString());

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
                        rsReportes.getString("consulta").replace("%clave_empleado", usuario.getClavePerfil().toString())
                               .replace("%clave_area", usuario.getClaveOficina().toString())
                               .replace("%clave_perfil", usuario.getClavePerfil().toString())
                               .replaceAll("\\$pk", "0"),
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

    public boolean isDefinicionDeGrid() {
        return definicionDeGrid;
    }

    public void setDefinicionDeGrid(boolean definicionDeGrid) {
        this.definicionDeGrid = definicionDeGrid;
    }

    public boolean isMuestraFormasForeaneas() {
        return muestraFormasForeaneas;
    }

    public void setMuestraFormasForeaneas(boolean muestraFormasForeaneas) {
        this.muestraFormasForeaneas = muestraFormasForeaneas;
    }

    public ArrayList<Nota> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<Nota> notas) {
        this.notas = notas;
    }

    public ArrayList<Nota> getNotas(Integer claveRegistro, Conexion cx) throws Fallo {
        this.notas = new ArrayList<Nota>();
        Nota nota;
        ResultSet rsNotas;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        String s;
        try {
            /* //Es necesario verificar si ya tenemos las fotos en el caché
            s ="SELECT DISTINCT empleado.clave_empleado, empleado.foto FROM empleado, nota WHERE empleado.clave_empleado=nota.clave_empleado AND nota.clave_forma=".concat(Integer.toString(this.claveForma)).concat(" AND nota.clave_registro=").concat(claveRegistro.toString());
            rsNotas= oDb.getRs(s);
            while (rsNotas.next()) {
            
            } */

            s = "SELECT empleado.nombre + ' ' + empleado.apellido_paterno as nombre, empleado.foto, nota_forma.* "+
                "FROM nota_forma, empleado WHERE nota_forma.clave_empleado=empleado.clave_empleado AND clave_forma=".concat(Integer.toString(this.claveForma)).concat(" AND clave_registro=").concat(claveRegistro.toString());
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

    public boolean isDefinicionDelGrid() {
        return definicionDeGrid;
    }

    public void setDefinicionDelGrid(boolean definicionDeGrid) {
        this.definicionDeGrid = definicionDeGrid;
    }

    public ArrayList<FormaX> getFormasForaneas() {
        return formasForaneas;
    }

    public void setFormasForaneas(ArrayList<FormaX> formasForaneas) {
        this.formasForaneas = formasForaneas;
    }

    public ArrayList<FormaX> getFormasForaneas(Integer clavePerfil, Conexion cx) throws Fallo {
        this.formasForaneas  = new ArrayList<FormaX>();
        FormaX forma;
        ResultSet rsFormasForaneas;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        String s = "SELECT forma.clave_forma, forma.forma "
                   .concat("FROM forma, permiso_forma ")
                   .concat(" WHERE forma.clave_forma=permiso_forma.clave_forma")
                   .concat(" AND forma.clave_forma_padre=").concat(Integer.toString(this.claveForma))
                   .concat(" AND permiso_forma.clave_permiso=1 ")
                   .concat(" AND permiso_forma.clave_perfil=").concat(Integer.toString(clavePerfil))
                   .concat("ORDER BY forma.orden_tab");

        try {
            rsFormasForaneas = oDb.getRs(s);

            while (rsFormasForaneas.next()) {
                forma = new FormaX();
                forma.setClaveForma(rsFormasForaneas.getInt("clave_forma"));
                forma.setForma(rsFormasForaneas.getString("forma"));
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
                this.portlets.add(new Portlet(rs.getInt("clave_portlet"), rs.getInt("clave_forma"), rs.getInt("clave_perfil"),  rs.getInt("clave_tipo"), rs.getString("titulo"), rs.getString("codigo"), rs.getInt("orden")));
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
        
    public ArrayList<UsuarioResponsable> getUsuariosNotas() {
        return usuariosFlujo;
    }

    public void setUsuariosNotas(ArrayList<UsuarioResponsable> usuariosNotas) {
        this.usuariosNotas = usuariosNotas;
    }

    /*public void setUsuariosNota(Conexion cx, int claveRegistro) throws Fallo {
        this.usuariosNotas = new ArrayList<UsuarioResponsable>();
        UsuarioResponsable usuarioResponsable;
        ResultSet rsUsuariosNota;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        String s = "SELECT email,"
                + " nombre, apellido_paterno, apellido_materno, "
                + " FROM flujo_datos_forma, responsable_flujo_datos, empleado_proyecto, empleado "
                + " WHERE flujo_datos_forma.clave_flujo_forma=responsable_flujo_datos.clave_flujo_forma"
                + " AND flujo_datos_forma.clave_forma=" + this.claveForma
                + " AND responsable_flujo_datos.clave_responsable_asignado=empleado_proyecto.clave_rol"
                + " AND empleado.clave_empleado=empleado_proyecto.clave_empleado"
                + " AND responsable_flujo_datos.clave_tipo_responsable=1"
                + " AND flujo_datos_forma.enviar_notificacion=1"
                + " AND  empleado_proyecto.clave_proyecto="+ String.valueOf(claveRegistro);

        try {
            rsUsuariosNota = oDb.getRs(s);

            while (rsUsuariosNota.next()) {

                usuarioResponsable = new UsuarioResponsable(
                        rsUsuariosNota.getString("email"),
                        rsUsuariosNota.getString("nombre"),
                        rsUsuariosNota.getString("apellido_paterno"),
                        rsUsuariosNota.getString("apellido_materno"),
                        rsUsuariosNota.getString("flujo_dato"),
                        rsUsuariosNota.getString("proceso"),
                        rsUsuariosNota.getString("asunto"),
                        rsUsuariosNota.getInt("secuencia"),
                        rsUsuariosNota.getString("notificacion"),
                        rsUsuariosNota.getString("campo_seguimiento_estatus"));
                this.usuariosFlujo.add(usuarioResponsable);
            }

            rsUsuariosNota.close();
        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }

    }  */
}
