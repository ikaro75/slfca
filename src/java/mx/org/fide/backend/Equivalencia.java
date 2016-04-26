package mx.org.fide.backend;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 *
 * @author daniel.martinez
 */
public class Equivalencia extends Consulta {

    private Integer claveEquivalencia;
    private Integer claveEmpleado;
    private Integer claveForma;
    private String tablaAnterior;
    private String tablaNueva;
    private Integer claveEstatus;

    public Equivalencia(Integer claveEquivalencia, Usuario usuario) {
        super.setUsuario(usuario);
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        this.claveEquivalencia = claveEquivalencia;
        try {
            ResultSet rs = oDb.getRs("select * from be_equivalencia where clave_equivalencia=".concat(claveEquivalencia.toString()));

            if (rs.next()) {
                this.claveEquivalencia = rs.getInt("clave_equivalencia");
                this.claveEmpleado = rs.getInt("clave_empleado");
                this.tablaAnterior = rs.getString("tabla_anterior");
                this.tablaNueva = rs.getString("tabla_nueva");
                this.claveEstatus = rs.getInt("clave_estatus");

            } else {
                rs.close();
                throw new Fallo("Error al recuperar definición de equivalencia");
            }

            rs.close();
        } catch (Exception e) {

        } finally {
            oDb.cierraConexion();
            oDb = null;
        }
    }

    public Equivalencia(Consulta c) throws Fallo {
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder q = new StringBuilder();
        try {
            Object o = this;
            Class<?> clazz = o.getClass();
            Method setter = null;

            for (Field field : clazz.getDeclaredFields()) {
                StringBuilder dbField = new StringBuilder();
                String[] aTemp = field.getName().split("(?=\\p{Upper})");
                for (Integer k = 0; k < aTemp.length; k++) {
                    if (k + 1 < aTemp.length) {
                        dbField.append(aTemp[k].toLowerCase()).append("_");
                    } else {
                        dbField.append(aTemp[k].toLowerCase());
                    }
                }

                if (c.getCampos().get(dbField.toString()) != null) {
                    if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("varchar")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), String.class);
                    } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("int")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Integer.class);
                    } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("float")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Float.class);
                    } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("decimal")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Double.class);
                    } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), java.util.Date.class);
                    }

                    if (c.getCampos().get(dbField.toString()).getValor() != null && !c.getAccion().equals("delete")) {

                        //Valida si el valor del campo es una cadena vacío 
                        if (c.getCampos().get(dbField.toString()).getValor().equals("") && !c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("varchar")) {
                            if (c.getCampos().get(dbField.toString()).getObligatorio() == 1 && !c.getCampos().get(field.getName()).isAutoIncrement()) {
                                throw new Fallo("El valor del campo ".concat(c.getCampos().get(dbField.toString()).getAlias()).concat(" no es válido, verifique"));
                            }
                        } else {
                            if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("varchar")) {
                                setter.invoke(this, c.getCampos().get(dbField.toString()).getValor());
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("int")) {
                                setter.invoke(this, Integer.parseInt(c.getCampos().get(dbField.toString()).getValor()));
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("float")) {
                                setter.invoke(this, Float.parseFloat(c.getCampos().get(dbField.toString()).getValor()));
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("decimal")) {
                                setter.invoke(this, Double.parseDouble(c.getCampos().get(dbField.toString()).getValor()));
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                                setter.invoke(this, formatter.parse(c.getCampos().get(dbField.toString()).getValor()));
                            }
                        }
                    } else if (c.getCampos().get(dbField.toString()).isAutoIncrement()) {
                        setter.invoke(this, Integer.parseInt(c.getPk()));
                    } else if (c.getCampos().get(dbField).getObligatorio() == 1) {
                        throw new Fallo("No se especificó ".concat(c.getCampos().get(dbField.toString()).getAlias()).concat(", verifique"));
                    }
                }
            }

            //Aquí debe ir el código para cargar desde el constructor los detalles del pagare
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public Integer getClaveEquivalencia() {
        return claveEquivalencia;
    }

    public void setClaveEquivalencia(Integer claveEquivalencia) {
        this.claveEquivalencia = claveEquivalencia;
    }

    public int getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    public String getTablaAnterior() {
        return tablaAnterior;
    }

    public void setTablaAnterior(String tablaAnterior) {
        this.tablaAnterior = tablaAnterior;
    }

    public String getTablaNueva() {
        return tablaNueva;
    }

    public void setTablaNueva(String tablaNueva) {
        this.tablaNueva = tablaNueva;
    }

    public int getClaveEstatus() {
        return claveEstatus;
    }

    public void setClaveEstatus(Integer claveEstatus) {
        this.claveEstatus = claveEstatus;
    }

    public String insert() throws Fallo {
        StringBuilder q = new StringBuilder();
        StringBuilder resultadoXML = new StringBuilder();
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Boolean startedTransaction = false;
        ResultSet rsFormas;
        ResultSet rsCamposForma;
        ResultSet rsSys;

        try {
            //1. Abre transaccion
            startedTransaction = true;
            oDb.execute("BEGIN TRANSACTION");

            resultadoXML.append(super.insert(true));
            //2. Se hace un distinct de todos los campos de las formas basadas en esa tabla para después insertarlas en una tabla de equivalencias
            q = new StringBuilder("SELECT DISTINCT campo FROM be_campo_forma WHERE clave_forma IN (SELECT clave_forma FROM be_forma WHERE tabla='").append(this.tablaAnterior).append("')");
            rsCamposForma = oDb.getRs(q.toString());

            while (rsCamposForma.next()) {
                //Se verifica si el campo existe en la tabla de sistema
                q = new StringBuilder("SELECT so.name as table_name, sc.name as column_name FROM SYSOBJECTS so, syscolumns sc ")
                        .append("WHERE so.XTYPE='u' and so.id=sc.id and so.name = '").append(this.tablaAnterior)
                        .append("' AND sc.name='").append(rsCamposForma.getString("campo")).append("'");

                rsSys = oDb.getRs(q.toString());

                if (!rsSys.next()) {
                    resultadoXML.append("<warning>No se encontro el campo [").append(rsCamposForma.getString("campo")).append("] en la tabla ").append(this.tablaAnterior).append(", seleccione el campo que lo sustituirá</warning>");
                    q = new StringBuilder("INSERT INTO be_equivalencia_campo (clave_equivalencia,campo_anterior)")
                            .append(" VALUES('").append(super.getPk()).append("','").append(rsCamposForma.getString("campo")).append("')");
                    oDb.execute(q.toString());
                }
            }

            //3. Se hace un select de los campos que no están en el diccionario de datos
            q = new StringBuilder("SELECT so.name as table_name, sc.name as column_name FROM SYSOBJECTS so, syscolumns sc ")
                    .append("WHERE so.XTYPE='u' and so.id=sc.id and so.name = '").append(this.tablaAnterior).append("'");
            rsSys = oDb.getRs(q.toString());

            while (rsSys.next()) {
                //Se verifica si el campo existe en el diccionario de datos
                q = new StringBuilder("SELECT DISTINCT clave_forma, clave_perfil FROM be_campo_forma WHERE clave_forma IN (SELECT clave_forma FROM be_forma WHERE tabla='").append(this.tablaAnterior).append("')");
                rsFormas = oDb.getRs(q.toString());
                while (rsFormas.next()) {
                    q = new StringBuilder("SELECT DISTINCT clave_campo FROM be_campo_forma WHERE clave_forma=").append(rsFormas.getInt("clave_forma")).append(" AND clave_perfil=").append(rsFormas.getInt("clave_perfil")).append(" AND campo='").append(rsSys.getString("column_name")).append("'");
                    rsCamposForma = oDb.getRs(q.toString());

                    if (!rsCamposForma.next()) {
                        resultadoXML.append("<warning>se agrego el campo [").append(rsSys.getString("column_name")).append("] al diccionario de datos</warning>");
                        q = new StringBuilder("INSERT INTO be_campo_forma")
                                .append("(clave_forma, clave_perfil,campo,alias_campo,obligatorio,tipo_control,evento,clave_forma_foranea,")
                                .append(" filtro_foraneo,edita_forma_foranea,ayuda,activo,tamano,visible)")
                                .append(" SELECT ").append(rsFormas.getInt("clave_forma")).append(",").append(rsFormas.getInt("clave_perfil"))
                                .append(",sc.name,sc.name,0,null,null,null,null,0,null,1,")
                                .append(" CASE sc.xtype ")
                                .append("    WHEN 56 THEN 70 ")
                                .append("    WHEN 104 THEN 70 ")
                                .append("    WHEN 167 THEN prec ")
                                .append("    WHEN 60 THEN 100 ")
                                .append("    WHEN 62 THEN 100 ")
                                .append("    WHEN 106 THEN 100 ")
                                .append("    WHEN 35 THEN 250 ")
                                .append("    WHEN 61 THEN 150 ")
                                .append("    WHEN 40 THEN 150 ")
                                .append(" END as tamano, 1")
                                .append(" FROM SYSOBJECTS so, syscolumns sc WHERE so.XTYPE='u' and so.id=sc.id and so.name='")
                                .append(rsSys.getString("table_name")).append("' AND sc.name='").append(rsSys.getString("column_name")).append("'");
                        oDb.execute(q.toString());
                    }

                }
            }

            oDb.execute("COMMIT");
            oDb.cierraConexion();
            oDb = null;

            if (!resultadoXML.toString().contains("warning")) {
                resultadoXML.append("<warning>No se detectaron cambios en la tabla</warning>");
            }

            return resultadoXML.toString();
        } catch (Exception e) {

            if (startedTransaction) {
                oDb.execute("ROLLBACK");
            }

            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(e.getMessage());
        }
    }

    public String update() throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rsEquivalencias;
        ResultSet rsCamposForma;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        String consulta;
        String regex;
        Conexion cx = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Boolean startedTransaction = false;
        try {

            //1. Abre transaccion
            startedTransaction = true;
            cx.execute("BEGIN TRANSACTION");

            //Ya se definió la tabla de equivalencias, ahora se deben hacer las sustituciones
            q = new StringBuilder("SELECT * FROM be_equivalencia_campo WHERE clave_equivalencia='").append(this.claveEquivalencia).append("' AND NOT campo_nuevo IS NULL AND campo_nuevo<>''");
            rsEquivalencias = cx.getRs(q.toString());

            while (rsEquivalencias.next()) {
                //Sustituye los campos actualizados
                q = new StringBuilder("SELECT clave_campo FROM be_campo_forma WHERE clave_forma IN (SELECT clave_forma FROM be_forma WHERE tabla='")
                        .append(this.tablaAnterior)
                        .append("') AND campo='").append(rsEquivalencias.getString("campo_anterior")).append("'");

                rsCamposForma = cx.getRs(q.toString());
                while (rsCamposForma.next()) {
                    if (rsEquivalencias.getString("campo_nuevo").equals("[Borrar de consultas y diccionario]")) {
                        q = new StringBuilder("DELETE FROM be_campo_forma WHERE clave_campo=").append(rsCamposForma.getInt("clave_campo"));                        
                        resultadoXML.append("<warning>Se elimino del diccionario el campo [").append(rsEquivalencias.getString("campo_anterior")).append("]</warning>");
                    } else {
                        q = new StringBuilder("UPDATE be_campo_forma SET campo='").append(rsEquivalencias.getString("campo_nuevo"))
                                .append("' WHERE clave_campo=").append(rsCamposForma.getInt("clave_campo"));
                        resultadoXML.append("<warning>Se actualizó el campo [").append(rsEquivalencias.getString("campo_anterior")).append("] por [").append(rsEquivalencias.getString("campo_nuevo")).append("] en el diccionario de datos</warning>");
                    }    
                    cx.execute(q.toString());
                }

                //Sustituye las referencias a los campos modificados en las consultas asociadas a las forma de la tabla que cambió
                q = new StringBuilder("SELECT clave_consulta, consulta FROM be_consulta_forma WHERE clave_forma IN (SELECT clave_forma FROM be_forma WHERE tabla='")
                        .append(this.tablaAnterior)
                        .append("') AND consulta LIKE '%").append(rsEquivalencias.getString("campo_anterior")).append("%'");

                rsCamposForma = cx.getRs(q.toString());
                while (rsCamposForma.next()) {
                    regex=".*\\b".concat(rsEquivalencias.getString("campo_anterior")).concat("|")
                                  .concat(rsEquivalencias.getString("campo_anterior")).concat(",\\b.*");
                    consulta=rsCamposForma.getString("consulta");
                    if (rsEquivalencias.getString("campo_nuevo").equals("[Borrar de consultas y diccionario]")) 
                        consulta=consulta.replaceAll(regex,"");
                    else
                        consulta=consulta.replaceAll(regex,rsEquivalencias.getString("campo_nuevo"));
                    
                    resultadoXML.append("<warning>Se reemplazó la consulta [").append(rsCamposForma.getString("consulta")).append("] por [").append(consulta).append("]</warning>");
                    q = new StringBuilder("UPDATE be_consulta_forma SET consulta='").append(consulta.replaceAll("'", "''")).append("'")
                            .append(" WHERE clave_consulta=").append(rsCamposForma.getInt("clave_consulta"));
                    cx.execute(q.toString());
                }

            }
            cx.execute("COMMIT");
            return resultadoXML.toString();
        } catch (Exception e) {
            if (startedTransaction) {
                cx.execute("ROLLBACK");
            }

            cx.cierraConexion();
            cx = null;
            throw new Fallo(e.getMessage());

        }
    }

    public String actualizaConsultasYDiccionario(String tablaAnterior, String tablaNueva, Usuario usuario, Integer paso) throws Fallo {
        StringBuilder resultado = new StringBuilder("");
        StringBuilder q = new StringBuilder("");
        String pk = "";
        Conexion cx = usuario.getCx();
        ResultSet rsSys;
        ResultSet rsEquivalencias;
        ResultSet rsCamposForma;

        try {
            if (paso == 1) {

                //Verifica si existe la tabla en el catálogo de sistemas
                q = new StringBuilder("SELECT so.name as table_name FROM SYSOBJECTS ")
                        .append("WHERE so.XTYPE='u' AND so.name = '").append(tablaAnterior).append("'");

                rsSys = cx.getRs(q.toString());

                if (!rsSys.next() && tablaNueva == null) {
                    return "<warning><![CDATA[No se encontró la tabla ".concat(tablaAnterior).concat("]]></warning>");
                }

                if (tablaNueva == null) {
                    q = new StringBuilder("UPDATE be_forma SET tabla='").append(tablaNueva).append("' WHERE tabla='").append(tablaAnterior).append("'");
                    cx.execute(q.toString());
                    tablaAnterior = tablaNueva;
                }

                rsSys = cx.getRs(q.toString());
                pk = rsSys.getString("pk");

                //Se hace un distinct de todos los campos de las formas basadas en esa tabla para después insertarlas en una tabla de equivalencias
                q = new StringBuilder("SELECT DISTINCT campo FROM be_campo_forma WHERE clave_forma IN (SELECT clave_forma FROM be_forma WHERE tabla='").append(tablaAnterior).append("')");
                rsCamposForma = cx.getRs(q.toString());

                while (rsCamposForma.next()) {
                    //Se verifica si el campo existe en la tabla de sistema
                    q = new StringBuilder("SELECT so.name as table_name, sc.name as column_name FROM SYSOBJECTS so, syscolumns sc ")
                            .append("WHERE so.XTYPE='u' and so.id=sc.id and so.name = '").append(tablaAnterior)
                            .append("' AND sc.name='").append(rsCamposForma.getString("campo")).append("'");

                    rsSys = cx.getRs(q.toString());

                    if (!rsSys.next()) {
                        q = new StringBuilder("INSERT INTO be_equivalencia_campo (timestamp,clave_empleado, tabla, campo_anterior)")
                                .append(" VALUES('").append(pk).append("',").append(usuario.getClave()).append(",")
                                .append("'").append(tablaAnterior).append("','").append(rsCamposForma.getString("campo")).append("')");
                        cx.execute(q.toString());
                    }
                }
            } else if (paso == 2) {

                //Ya se definió la tabla de equivalencias, ahora se deben hacer las sustituciones
                q = new StringBuilder("SELECT * FROM be_equivalencia_campo WHERE clave_equivalencia='").append(this.claveEquivalencia).append("'");
                rsEquivalencias = cx.getRs(q.toString());

                while (rsEquivalencias.next()) {
                    //Sustituye los campos actualizados
                    q = new StringBuilder("SELECT clave_campo FROM be_campo_forma WHERE clave_forma IN (SELECT clave_forma FROM be_tabla WHERE tabla='")
                            .append(rsEquivalencias.getString("tabla"))
                            .append("') AND campo='").append(rsEquivalencias.getString("campo_anterior")).append("'");

                    rsCamposForma = cx.getRs(q.toString());
                    while (rsCamposForma.next()) {
                        q = new StringBuilder("UPDATE be_campo_forma SET campo='").append(rsEquivalencias.getString("campo_nuevo"))
                                .append("' WHERE clave_campo=").append(rsCamposForma.getInt("clave_campo"));
                        cx.execute(q.toString());
                    }

                    //Sustituye las referencias a los campos modificados en las consutlas asociadas a las forma de la tabla que cambió
                    q = new StringBuilder("SELECT clave_consulta FROM be_consulta_forma WHERE clave_forma IN (SELECT clave_forma FROM be_tabla WHERE tabla='")
                            .append(rsEquivalencias.getString("tabla"))
                            .append("') AND consulta LIKE '%").append(rsEquivalencias.getString("campo_anterior")).append("%'");

                    rsCamposForma = cx.getRs(q.toString());
                    while (rsCamposForma.next()) {
                        q = new StringBuilder("UPDATE be_consulta_forma SET consulta=REPLACE(CONVERT(varchar(max),consulta),'")
                                .append(rsEquivalencias.getString("campo_anterior")).append("',")
                                .append(rsEquivalencias.getString("campo_nuevo")).append("')")
                                .append(" WHERE clave_consulta=").append(rsCamposForma.getInt("clave_consulta"));
                        cx.execute(q.toString());
                    }

                }
            }

            return resultado.toString();

        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            cx.cierraConexion();
        }

    }

    @Override
    public String toString() {
        return "mx.org.fide.backend.BeEquivalencia[ claveEquivalencia=" + claveEquivalencia + " ]";
    }

}
