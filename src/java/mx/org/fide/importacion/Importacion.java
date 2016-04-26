package mx.org.fide.importacion;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import mx.org.fide.cfe.ValidacionBeneficiario;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
/**
 *
 * @author daniel.martinez
 */
public class Importacion extends Consulta {

    private Integer claveImportacion;
    private Date fecha;
    private Integer claveEmpleado;
    private Integer claveDefinicion;
    private Integer clavePunto;
    private String archivo;
    private String consulta;
    private Integer claveEstatus;
    private Integer claveTipoOrdenamiento;
    private Excel excel = null;
    private Txt txt = null;
    private Integer registrosPorProcesar = 0;
    private Integer registrosProcesados = 0;
    private String error = "";

    public Importacion() {
    }

    public Importacion(Integer claveImportacion, Date fecha, int claveEmpleado, int claveDefinicion, int clavePunto, String archivo, String consulta, int claveEstatus, int claveTipoOrdenamiento) {
        this.claveImportacion = claveImportacion;
        this.fecha = fecha;
        this.claveEmpleado = claveEmpleado;
        this.claveDefinicion = claveDefinicion;
        this.clavePunto = clavePunto;
        this.archivo = archivo;
        this.consulta = consulta;
        this.claveEstatus = claveEstatus;
        this.claveTipoOrdenamiento = claveTipoOrdenamiento;
    }

    public Importacion(Integer claveImportacion, Usuario usuario) throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        this.setUsuario(usuario);
        Conexion oDb = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_importacion where clave_importacion=".concat(String.valueOf(claveImportacion)));

            if (rs.next()) {
                this.claveImportacion = rs.getInt("clave_importacion");
                super.setPk(rs.getString("clave_importacion"));
                this.fecha = rs.getDate("fecha");
                this.claveEmpleado = rs.getInt("clave_empleado");
                this.claveDefinicion = rs.getInt("clave_definicion");
                this.clavePunto = rs.getInt("clave_punto");
                this.archivo = rs.getString("archivo");
                this.consulta = rs.getString("consulta");
                this.claveEstatus = rs.getInt("clave_estatus");
                this.registrosPorProcesar = rs.getInt("registros_por_procesar");
                this.registrosProcesados = rs.getInt("registros_procesados");
                this.claveTipoOrdenamiento = rs.getInt("clave_tipo_ordenamiento");
                if (this.archivo.toLowerCase().endsWith("txt")) {
                    this.txt = new Txt(this.archivo);
                }
            } else {
                throw new Fallo("No se encontró la importación especificada");
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar la importación: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        }
    }

    public Importacion(Consulta c) throws Fallo {
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
                    if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("varchar") || c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("text")) {
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
                        if ((c.getCampos().get(dbField.toString()).getValor().equals("") && (!c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("varchar") || !c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("text")))) {
                            if (c.getCampos().get(dbField.toString()).getObligatorio() == 1 && !c.getCampos().get(field.getName()).isAutoIncrement()) {
                                throw new Fallo("El valor del campo ".concat(c.getCampos().get(dbField.toString()).getAlias()).concat(" no es válido, verifique"));
                            }
                        } else {
                            if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("varchar") || c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("text")) {
                                if (c.getCampos().get(dbField.toString()) != null) {
                                    setter.invoke(this, c.getCampos().get(dbField.toString()).getValor());
                                }
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("int")) {
                                if (!c.getCampos().get(dbField.toString()).getValor().toLowerCase().equals("null")) {
                                    setter.invoke(this, Integer.parseInt(c.getCampos().get(dbField.toString()).getValor()));
                                }
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("float")) {
                                setter.invoke(this, Float.parseFloat(c.getCampos().get(dbField.toString()).getValor()));
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("decimal")) {
                                setter.invoke(this, Double.parseDouble(c.getCampos().get(dbField.toString()).getValor()));
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                                //Cuando la fecha viene con el valor "%ahora" que indica que la fecha se va tomar del servidor de la base de datos se omite
                                if (!c.getCampos().get(dbField.toString()).getValor().equals("%ahora")) {
                                    setter.invoke(this, formatter.parse(c.getCampos().get(dbField.toString()).getValor()));
                                }
                            }
                        }
                    } else if (c.getCampos().get(dbField.toString()).isAutoIncrement()) {
                        setter.invoke(this, Integer.parseInt(c.getPk()));
                    } else if (c.getCampos().get(dbField.toString()).getObligatorio() == 1) {
                        throw new Fallo("No se especificó ".concat(c.getCampos().get(dbField.toString()).getAlias()).concat(", verifique"));
                    }
                }
            }

            //Aquí debe ir el código para cargar desde el constructor los detalles del pagare
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }

    }

    public Integer getClaveImportacion() {
        return claveImportacion;
    }

    public void setClaveImportacion(Integer claveImportacion) {
        this.claveImportacion = claveImportacion;
    }

    public void setClaveDefinicion(Integer claveDefinicion) {
        this.claveDefinicion = claveDefinicion;
    }

    public Integer getClaveDefinicion() {
        return claveDefinicion;
    }

    public Integer getClavePunto() {
        return clavePunto;
    }

    public void setClavePunto(Integer clavePunto) {
        this.clavePunto = clavePunto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(java.util.Date fecha) {
        this.fecha = fecha;
    }

    public int getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public Integer getClaveEstatus() {
        return claveEstatus;
    }

    public void setClaveEstatus(Integer claveEstatus) {
        this.claveEstatus = claveEstatus;
    }

    public Integer getClaveTipoOrdenamiento() {
        return claveTipoOrdenamiento;
    }

    public void setClaveTipoOrdenamiento(Integer claveTipoOrdenamiento) {
        this.claveTipoOrdenamiento = claveTipoOrdenamiento;
    }

    public Excel getExcel() {
        return excel;
    }

    public void setExcel(Excel excel) {
        this.excel = excel;
    }

    public Txt getTxt() {
        return txt;
    }

    public void setTxt(Txt txt) {
        this.txt = txt;
    }

    public Integer getRegistrosPorProcesar() {
        return registrosPorProcesar;
    }

    public void setRegistrosPorProcesar(Integer registrosPorProcesar) {
        this.registrosPorProcesar = registrosPorProcesar;
    }

    public Integer getRegistrosProcesados() {
        return registrosProcesados;
    }

    public void setRegistrosProcesados(Integer registrosProcesados) {
        this.registrosProcesados = registrosProcesados;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String insert() throws Fallo {
        Consulta c = null;
        String error = "";
        StringBuilder q = new StringBuilder();
        StringBuilder resultadoXML = new StringBuilder();
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Boolean startedTransaction = false;
        Integer claveBitacora;

        try {
            startedTransaction = true;
            oDb.execute("BEGIN TRANSACTION");

            /*if (this.archivo.endsWith("txt")) {
             this.txt = new Txt(this.archivo);
             this.registrosPorProcesar = txt.getRenglones();
             super.getCampos().get("registros_por_procesar").setValor(this.registrosPorProcesar.toString());
             this.registrosProcesados = 0;
             super.getCampos().get("registros_procesados").setValor("0");
             }*/
            resultadoXML.append(super.insert(oDb));
            oDb.execute("COMMIT");
            oDb.cierraConexion();
            oDb = null;
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
        Consulta c = null;
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Boolean startedTransaction = false;
        Integer claveBitacora;
        Date fechaImportacion = null;
        DefinicionImportacion definicionImportacion = null;

        try {

            //1. Abre transaccion
            startedTransaction = true;
            oDb.execute("BEGIN TRANSACTION");
            resultadoXML.append(super.update(true));
            /*Fase de validación */
            if (this.claveEstatus == 2) {

                //2. Carga la definición de importación
                definicionImportacion = new DefinicionImportacion(this.getClaveDefinicion(), this.getUsuario());
                if (definicionImportacion.getDefinicionImportacionDetalles().isEmpty()) {
                    throw new Fallo("No está definida la estructura del archivo a importar, verifique");
                }

                //3. Verifica el tipo del archivo
                if (definicionImportacion.getClaveTipoArchivo() == 1 && !this.archivo.toLowerCase().endsWith(".txt")) {
                    throw new Fallo("La extensión del archivo debe ser TXT, verifique");
                } else if (definicionImportacion.getClaveTipoArchivo() == 2 && !this.archivo.toLowerCase().endsWith(".xlsx")) {
                    throw new Fallo("La extensión del archivo debe ser XLSX, verifique");
                } else if (definicionImportacion.getClaveTipoArchivo() == 3 && !this.archivo.toLowerCase().endsWith(".csv")) {
                    throw new Fallo("La extensión del archivo debe ser CSV, verifique");
                }

                if (definicionImportacion.getClaveTipoImportacion() == 2 && definicionImportacion.getClaveTipoArchivo() == 1) {
                    /*txt.setRegistroActual(this.registrosProcesados);
                     txt.validaImportacionAvancesDICONSA(this,definicionImportacion, super.getUsuario());*/
                    /**
                     * ********** Avances de beneficiarios **************
                     */
                    oDb.execute("COMMIT");
                    startedTransaction = false;

                    //1. Crea una tabla de trabajo
                    fechaImportacion = Calendar.getInstance().getTime();
                    oDb.execute("CREATE TABLE avances_".concat(formatter.format(fechaImportacion)).
                            concat("(rpu varchar(100) NULL)"));

                    //2. Importa el archivo de texto
                    q = new StringBuilder("BULK INSERT ").append("avances_".concat(formatter.format(fechaImportacion))).
                            append(" FROM '").append(this.archivo).append("' WITH (ROWTERMINATOR = '0x0a')");
                    oDb.execute(q.toString());

                    //Se crea nueva tabla con número de renglon y se parsean los campos
                    q = new StringBuilder("CREATE TABLE avances__").append(formatter.format(fechaImportacion)).append(" (renglon int IDENTITY,rpu varchar(100), fecha_captura varchar(8), fecha_carga varchar(8), clave_tipo_padron int, id_tienda varchar(100))");
                    oDb.execute(q.toString());

                    //Se crea paginación en esta página temporal
                    q = new StringBuilder("INSERT INTO avances__").append(formatter.format(fechaImportacion)).append(" (rpu,fecha_captura,fecha_carga,clave_tipo_padron,id_tienda)")
                            .append("SELECT SUBSTRING(rpu,1,12), SUBSTRING(rpu,14,8), SUBSTRING(rpu,23,8),SUBSTRING(rpu,32,1), REPLACE(REPLACE(SUBSTRING(rpu,34,10),CHAR(10),''),CHAR(13),'') FROM ")
                            .append("avances_").append(formatter.format(fechaImportacion));
                    oDb.execute(q.toString());

                    //Se vacía de la tabla temporal a importacion detalle
                    q = new StringBuilder("INSERT INTO fide_importacion_detalle (clave_importacion,renglon, rpu, fecha_carga, fecha_captura,clave_tipo_padron,id_control, clave_estatus, importado)")
                            .append(" SELECT ").append(this.claveImportacion).append(",renglon, rpu,fecha_carga, fecha_captura,clave_tipo_padron,id_tienda, 0,0 FROM ")
                            .append("avances__").append(formatter.format(fechaImportacion));
                    oDb.execute(q.toString());

                    // La asignación de errores va a ser sobre la tabla de detalles de importaciones 
                    // Se valida que los RPUs estén en la tabla de beneficiarios si son del padron extendido, ampliado o potencial  
                    q = new StringBuilder("UPDATE fide_importacion_detalle ")
                            .append("SET mensaje_error='El RPU indicado no se encuentra en el padrón de beneficiarios', clave_estatus=17 ")
                            .append(" WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_tipo_padron IN (1,2,3) AND clave_estatus=0 AND rpu NOT IN (SELECT rpu FROM fide_beneficiario)");
                    oDb.execute(q.toString());

                    // Valida los RPUs de beneficiarios a quienes ya se les entregaron sus lámparas si son del padron extendido, ampliado o potencial
                    q = new StringBuilder("UPDATE fide_importacion_detalle ")
                            .append("SET mensaje_error='El RPU indicado ya fue anteriormente marcado como entregado', clave_estatus=17 ")
                            .append(" WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_tipo_padron IN (1,2,3) AND clave_estatus=0 AND rpu IN (SELECT rpu FROM fide_beneficiario WHERE clave_estatus=2 AND clave_tipo_padron IN (1,2,3))");
                    oDb.execute(q.toString());

                    oDb.execute("SET DATEFORMAT ymd");

                    // Descarta las fecha de captura incorrectas 
                    q = new StringBuilder("UPDATE fide_importacion_detalle ")
                            .append("SET mensaje_error='La fecha de captura no es válida', clave_estatus=17 ")
                            .append(" WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=0 AND ISDATE(fecha_captura)=0");
                    oDb.execute(q.toString());

                    // Descarta las fecha de carga incorrectas 
                    q = new StringBuilder("UPDATE fide_importacion_detalle ")
                            .append("SET mensaje_error='La fecha de carga no es válida', clave_estatus=17 ")
                            .append(" WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=0 AND ISDATE(fecha_carga)=0");
                    oDb.execute(q.toString());
                    
                    //Verifica qué beneficiarios de los padrones 1, 2 y 3 ya vienen en el archivo de importación
                    q = new StringBuilder("SELECT rpu, COUNT(rpu) FROM fide_importacion_detalle WHERE clave_tipo_padron IN (1,2,3) and clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=0 GROUP BY rpu HAVING COUNT(rpu)>1");
                    rs = oDb.getRs(q.toString());
                    while (rs.next()) {
                        q = new StringBuilder("UPDATE fide_importacion_detalle SET clave_estatus=17, mensaje_error='El RPU indicado está duplicado en el archivo de importación' WHERE clave_importacion=").append(this.claveImportacion).append(" AND rpu='").append(rs.getString("rpu")).append("' AND renglon>(SELECT MIN(renglon) FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion).append(" AND rpu='").append(rs.getString("rpu")).append("' AND clave_importacion=").append(this.claveImportacion).append(")");
                        oDb.execute(q.toString());
                    };
                    
                    // Valida el resto de RPUs si son del padron extendido, ampliado o potencial
                    q = new StringBuilder("UPDATE fide_importacion_detalle ")
                            .append("SET clave_estatus=16,")
                            .append(" consulta_resultante='UPDATE fide_beneficiario SET clave_estatus=2, fecha_lectura=''' + fecha_carga + ''', fecha_carga=''' + fecha_captura + ''' WHERE rpu='''+ rpu +''''")
                            .append(" WHERE clave_tipo_padron IN (1,2,3) and clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=0");
                    oDb.execute(q.toString());
                        
                    // Se valida el padron insitu
                    //1. Se valida si un RPU no tiene longitud =12
                    q = new StringBuilder("UPDATE fide_importacion_detalle SET clave_estatus=9, mensaje_error='El número de servicio no tiene 12 dígitos' WHERE LEN(rpu)!=12 AND clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=0");
                    oDb.execute(q.toString());
                    
                    //Se valida si no existen los id_control
                    q = new StringBuilder("UPDATE fide_importacion_detalle SET clave_estatus=10, mensaje_error='La tienda no se encuentra registrada' WHERE id_control NOT IN (SELECT id_control FROM fide_punto_entrega) AND clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=0");
                    oDb.execute(q.toString());
                    
                            
                    
                    q = new StringBuilder("UPDATE fide_importacion_detalle ") 
                        .append("SET clave_estatus=14, ")
                        .append(" mensaje_error='Ya está registrado', ")
                        .append("consulta_resultante='INSERT INTO fide_beneficiario (rpu,nombre,tarifa,clave_estado,clave_municipio,poblacion,direccion,clave_punto,' + ")
                        .append("'clave_tipo_padron,clave_estado_entrega, clave_municipio_entrega,clave_localidad_entrega,fecha_registro,fecha_lectura,fecha_carga,'+ ")
                        .append("'clave_estatus,clave_carpeta, clave_importacion_detalle)VALUES (''' + ")
                        .append("fide_beneficiario.rpu + ''',''' + ISNULL(fide_beneficiario.nombre,'') + ''',''' + ISNULL(fide_beneficiario.tarifa,'') + ''',' + ")
                        .append("CONVERT(varchar,fide_beneficiario.clave_estado) + ',' + CONVERT(varchar,fide_beneficiario.clave_municipio) + ',''' + ")
                        .append("ISNULL(REPLACE(fide_beneficiario.poblacion,'''',''''''),'') + ''',''' + ISNULL(REPLACE(fide_beneficiario.direccion,'''',''''''),'') + ''',' + ")
                        .append("(SELECT CONVERT(varchar,clave_punto) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',4,'  + ")
                        .append("(SELECT CONVERT(varchar,clave_estado) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',' + ")
                        .append("(SELECT CONVERT(varchar,clave_municipio) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control))+ ',' + ")
                        .append("ISNULL((SELECT  CONVERT(varchar,clave_localidad) FROM fide_punto_entrega WHERE fide_punto_entrega.id_control=CONVERT(INT,fide_importacion_detalle.id_control)),'NULL') + ")
                        .append("',GETDATE(),''' + CONVERT(varchar,fide_importacion_detalle.fecha_carga) + ''',''' + ")
                        .append("CONVERT(varchar,fide_importacion_detalle.fecha_captura) + ")
                        .append("''',2,%clave_carpeta,' + CONVERT(varchar,fide_importacion_detalle.clave_importacion_detalle) + ')'")
                        .append("FROM fide_beneficiario  ")
                        .append("WHERE fide_importacion_detalle.rpu=fide_beneficiario.rpu ") 
                        .append("AND fide_importacion_detalle.clave_tipo_padron=4 ")
                        .append("AND fide_importacion_detalle.clave_importacion=").append(this.claveImportacion) 
                        .append("  AND fide_importacion_detalle.clave_estatus=0");
          
                         oDb.execute(q.toString());
                         
                         //Valida si no está en el padron de programas anteriores
                         String [] aProgramasAnteriores = {"Programa Luz Sustentable 1","Programa Luz Sustentable 2","Piloto en el Estado de Michoacán","Piloto en el Estado de Sonora","Piloto en el Estado de Chihuahua","Piloto en el Estado de Guerrero"};
                         String [] aEstatus = {"4","5","6","7","7","7"};
                         
                         for (int i=0; i<aProgramasAnteriores.length; i++) {
                             //Se debe tomar la info de la tienda como info del beneficiario
                                q = new StringBuilder("UPDATE fide_importacion_detalle ") 
                                    .append("SET clave_estatus=").append(aEstatus[i]).append(",")
                                    .append("mensaje_error='El RPU consultado participó en el ").append(aProgramasAnteriores[i]).append("',")
                                    .append("consulta_resultante='INSERT INTO fide_beneficiario (rpu,nombre,clave_estado,clave_municipio,poblacion,clave_punto,clave_tipo_padron,clave_estado_entrega,' +")
                                    .append("' clave_municipio_entrega,clave_localidad_entrega,fecha_registro,fecha_lectura,fecha_carga,clave_estatus,clave_carpeta, clave_importacion_detalle) ' + ")
                                    .append("'VALUES (''' + fide_padron_anterior.rpu + ''','''',' + ")
                                    .append("(SELECT CONVERT(varchar,clave_estado) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',' + ")
                                    .append("(SELECT CONVERT(varchar,clave_municipio) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',''' + ")
                                    .append("ISNULL((SELECT fide_localidad.localidad FROM fide_punto_entrega, fide_localidad WHERE fide_punto_entrega.clave_localidad= fide_localidad.clave_localidad AND fide_punto_entrega.id_control=CONVERT(int,fide_importacion_detalle.id_control)),'NULL') + ''',' + ")
                                    .append("(SELECT CONVERT(varchar,clave_punto) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',4,' + ")
                                    .append("(SELECT CONVERT(varchar,clave_estado) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',' + ")
                                    .append("(SELECT CONVERT(varchar,clave_municipio) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control))+ ',' + ")
                                    .append("ISNULL((SELECT CONVERT(varchar,clave_localidad) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)),'NULL')+ ',GETDATE(),''' + ")
                                    .append("CONVERT(varchar,fide_importacion_detalle.fecha_carga) + ''',''' + ")
                                    .append("CONVERT(varchar,fide_importacion_detalle.fecha_captura) + ''',2,%clave_carpeta,' + ")
                                    .append("CONVERT(varchar,fide_importacion_detalle.clave_importacion_detalle) + ')' ")
                                    .append("FROM fide_padron_anterior ")
                                    .append(" WHERE fide_importacion_detalle.rpu=fide_padron_anterior.rpu  ")
                                    .append(" AND fide_importacion_detalle.clave_tipo_padron=4 ")
                                    .append(" AND fide_importacion_detalle.clave_importacion=").append(this.claveImportacion) 
                                    .append(" AND fide_importacion_detalle.clave_estatus=0 ")
                                    .append(" AND fide_padron_anterior.programa=").append(i);
                                oDb.execute(q.toString());
                         }
                         
                         //3. Verifica si el RPU se encuentra en la tabla FIDE_PADRON_CFE
                        q = new StringBuilder("UPDATE fide_importacion_detalle ")
                            .append("SET clave_estatus=16, ")
                            .append("mensaje_error='Solicitud de ingreso exitosa.',  ")
                            .append("consulta_resultante='INSERT INTO fide_beneficiario (rpu,nombre,tarifa,clave_estado,clave_municipio,poblacion, direccion,clave_punto,")
                            .append("clave_tipo_padron,clave_estado_entrega, clave_municipio_entrega,clave_localidad_entrega,fecha_registro,fecha_lectura,fecha_carga,clave_estatus,clave_carpeta, clave_importacion_detalle) ' +  ")
                            .append("'VALUES (''' + fide_padron_cfe.rpu + ''',''' + ISNULL(REPLACE(fide_padron_cfe.nombre,'''',''''''),'') + ''', ''' + ISNULL(fide_padron_cfe.tarifa,'') + ''',' + ")
                            .append("(SELECT CONVERT(varchar,clave_estado) FROM fide_estado WHERE codigo_estado_cfe=SUBSTRING(fide_padron_cfe.claveedomun,1,1)) + ',' + ")
                            .append("ISNULL((SELECT TOP 1 CONVERT(varchar,clave_municipio) FROM fide_municipio, fide_estado WHERE fide_municipio.clave_estado=fide_estado.clave_estado AND fide_municipio.clave_municipio_cfe=SUBSTRING(fide_padron_cfe.claveedomun,2,3) AND fide_estado.codigo_estado_cfe=SUBSTRING(fide_padron_cfe.claveedomun,1,1)),'NULL') + ',''' + ")
                            .append("fide_padron_cfe.poblacioncfe + ''',''' + REPLACE(fide_padron_cfe.direccion,'''','''''') + ''',' + ")
                            .append("(SELECT CONVERT(varchar,clave_punto) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',4,' + ")                                
                            .append("(SELECT CONVERT(varchar,clave_estado) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ','  + ")
                            .append("(SELECT CONVERT(varchar,clave_municipio) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',' + ")
                            .append("ISNULL((SELECT CONVERT(varchar,clave_localidad) FROM fide_punto_entrega WHERE fide_punto_entrega.id_control=CONVERT(int,fide_importacion_detalle.id_control)),'NULL') + ',' + ")
                            .append(" 'GETDATE(),'''+  CONVERT(varchar,fide_importacion_detalle.fecha_carga) + ''', ''' +  ")
                            .append(" CONVERT(varchar,fide_importacion_detalle.fecha_captura) + ''',2,%clave_carpeta,' + ")
                            .append(" CONVERT(varchar,fide_importacion_detalle.clave_importacion_detalle) + ')'  ")
                            .append(" FROM fide_padron_cfe ")
                            .append(" WHERE fide_importacion_detalle.rpu=fide_padron_cfe.rpu  ") 
                            .append(" AND clave_tipo_padron=4 ")
                            .append(" AND clave_importacion=").append(this.claveImportacion)
                            .append(" AND clave_estatus=0");    
                             oDb.execute(q.toString());
                             
                            //4. el resto de beneficiarios de pdrón 4 son marcados como pendientes de ser consultados en sicom
                             q = new StringBuilder("UPDATE fide_importacion_detalle ")
                                    .append("SET clave_estatus=18, mensaje_error='Beneficiario pendiente de ser validado en SICOM.',")
                                    .append("consulta_resultante='INSERT INTO fide_beneficiario (rpu,nombre,tarifa,clave_estado,clave_municipio,poblacion,direccion,")
                                    .append("clave_punto,clave_tipo_padron,clave_estado_entrega,clave_municipio_entrega,clave_localidad_entrega,")
                                    .append("fecha_registro,fecha_lectura,fecha_carga,clave_estatus,clave_carpeta,clave_importacion_detalle) VALUES (''' + ")
                                    .append("fide_importacion_detalle.rpu + ''','''', '''', 0,null,null,null,'+ ")
                                    .append("(SELECT CONVERT(varchar,clave_punto) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ")
                                    .append("',4,' + ")
                                    .append("(SELECT CONVERT(varchar,clave_estado) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',' +")
                                    .append("(SELECT CONVERT(varchar,clave_municipio) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)) + ',' + ")
                                    .append("ISNULL((SELECT CONVERT(varchar,clave_localidad) FROM fide_punto_entrega WHERE id_control=CONVERT(int,fide_importacion_detalle.id_control)),'NULL') +  ")
                                    .append("',GETDATE(),''' + CONVERT(varchar,fide_importacion_detalle.fecha_carga) + ''', ''' + ")
                                    .append("CONVERT(varchar,fide_importacion_detalle.fecha_captura) + ''',2,%clave_carpeta,'+ ")
                                    .append("CONVERT(varchar,fide_importacion_detalle.clave_importacion_detalle) + ')' ")
                                    .append("WHERE clave_tipo_padron=4 AND clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=0");
                            oDb.execute(q.toString());
                        
                        
                    // 1. Generación del cursor de los beneficiarios del padron in situ para validar uno por uno
                    /*
                            q = new StringBuilder("SELECT clave_importacion_detalle, rpu, id_control, fecha_carga, fecha_captura, renglon FROM fide_importacion_detalle WHERE clave_tipo_padron=4 AND clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=0");
                    rs = oDb.getRs(q.toString());
                    ValidacionBeneficiario v = new ValidacionBeneficiario();
                    String mensaje = "";
                    Integer clavePunto = 0;
                    Integer claveEstadoEntrega = 0;
                    Integer claveMunicipioEntrega = 0;
                    Integer claveLocalidadEntrega = 0;
                    String poblacion = "";
                    String tarifa = "";

                    //2. Mientras no se llegue al final del cursor se debe
                    while (rs.next()) {
                        //Validar al usuario
                        if (rs.getString("rpu").equals("713930811323")) {
                            System.out.println("Stop");                  }
                        try {
                            mensaje = v.valida(rs.getString("rpu"), rs.getString("id_control").replace("\r", ""), 4, this.getUsuario());
                        } catch (Exception e) {
                            mensaje = e.getMessage();
                        }

                        // 2. Mientras no se llegue al final del cursos se debe:
                        // 2.1 Validar al usuario
                        // 2.2 Insertarlo marcándolo como entregado en la tienda correspondientes 
                        //No se llegó a validar el id_control
                        if (v.getClavePunto() == null) {
                            ResultSet rsTemp = oDb.getRs("SELECT clave_punto, clave_estado, clave_municipio, clave_localidad, (SELECT localidad FROM fide_localidad WHERE clave_localidad=fide_punto_entrega.clave_localidad) as poblacion FROM fide_punto_entrega WHERE id_control=".concat(rs.getString("id_control")));

                            if (rsTemp.next()) {
                                clavePunto = rsTemp.getInt("clave_punto");
                                claveEstadoEntrega = rsTemp.getInt("clave_estado");
                                claveMunicipioEntrega = rsTemp.getInt("clave_municipio");
                                claveLocalidadEntrega = rsTemp.getInt("clave_localidad");
                                poblacion = rsTemp.getString("poblacion");
                                tarifa = v.getTarifa() == null ? "" : v.getTarifa();
                            } else {
                                mensaje = mensaje.concat("; no está definida la tienda ").concat(rs.getString("id_control"));
                            }
                        } else {
                            clavePunto = v.getClavePunto();
                            claveEstadoEntrega = v.getClaveEstadoEntrega();
                            claveMunicipioEntrega = v.getClaveMunicipioEntrega();
                            claveLocalidadEntrega = v.getClaveLocalidadEntrega();
                            poblacion = v.getPoblacion();
                            tarifa = v.getTarifa();
                        }

                        resultadoXML = new StringBuilder("INSERT INTO fide_beneficiario (rpu,nombre")
                                .append(",tarifa,clave_estado,clave_municipio")
                                .append(",poblacion")
                                .append(",direccion")
                                .append(",clave_punto,clave_tipo_padron,clave_municipio_entrega")
                                .append(",fecha_entrega,fecha_registro,clave_estado_entrega")
                                .append(",clave_localidad_entrega,fecha_lectura")
                                .append(",fecha_carga,clave_estatus,clave_carpeta, clave_importacion_detalle)")
                                .append("VALUES (")
                                .append("'").append(v.getRpu()).append("',").append(v.getNombre() == null ? "''" : "'".concat(v.getNombre().replaceAll("'", "''")).concat("'")).append(",")
                                .append(v.getTarifa() == null ? "''" : "'".concat(v.getTarifa()).concat("'")).append(",").append(!v.getTomaInfoDeLaTienda() ? v.getClaveEstado() : claveEstadoEntrega).append(",").append(!v.getTomaInfoDeLaTienda() ? v.getClaveMunicipio() : claveMunicipioEntrega).append(",")
                                .append(v.getPoblacion() == null ? "null" : "'".concat(v.getPoblacion().replaceAll("'", "''")).concat("'")).append(",")
                                .append(v.getDireccion() == null ? "null" : "'".concat(v.getDireccion().replaceAll("'", "''")).concat("'")).append(",")
                                .append(clavePunto).append(",4,").append(claveMunicipioEntrega).append(",")
                                .append(v.getFechaEntrega() == null ? "null" : "'".concat(v.getFechaEntrega()).concat("'")).append(",GETDATE(),").append(claveEstadoEntrega).append(",")
                                .append(claveLocalidadEntrega).append(",'").append(rs.getString("fecha_carga")).append("','")
                                .append(rs.getString("fecha_captura")).append("',2,%clave_carpeta,").append(rs.getInt("clave_importacion_detalle")).append(")");

                        q = new StringBuilder("UPDATE fide_importacion_detalle ")
                                .append("SET clave_estatus=").append(v.getClaveEstatusImportacionDetalle())
                                .append(" ,consulta_resultante='").append(resultadoXML.toString().replaceAll("'", "''")).append("', mensaje_error='").append(mensaje.replaceAll("'", "''")).append("'")
                                .append(" WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));

                        oDb.execute(q.toString());
                    } */

                    q = new StringBuilder("UPDATE fide_importacion SET clave_estatus=3 WHERE clave_importacion=").append(this.claveImportacion.toString());
                    oDb.execute(q.toString());

                } else if (definicionImportacion.getClaveTipoImportacion() == 1 && definicionImportacion.getClaveTipoArchivo() == 2) {
                    // Abre el archivo
                    /**
                     * ********** Nuevos beneficiarios del padrón ampliado
                     * ***********
                     */
                    Excel excel = new Excel(this.archivo);
                    excel.importaBeneficiariosPadronAmpliado(this, definicionImportacion, super.getUsuario());

                } else if (definicionImportacion.getClaveTipoImportacion() == 3 && definicionImportacion.getClaveTipoArchivo() == 3) {
                    String fechaInstalacion = "";
                    String fechaRetiro = "";
                    String mensajeError = "";
                    /**
                     * ********** Importación de lecturas MRV ***********
                     */
                    oDb.execute("COMMIT");
                    startedTransaction = false;
                    //1. Crea una tabla de trabajo
                    fechaImportacion = Calendar.getInstance().getTime();
                    oDb.execute("CREATE TABLE #mrv_".concat(formatter.format(fechaImportacion)).
                            concat("(fecha varchar(100),hora varchar(100),estatusd varchar(100),estatus varchar(100))"));

                    //2. Importa el archivo de texto
                    q = new StringBuilder("BULK INSERT ").append("#mrv_".concat(formatter.format(fechaImportacion))).
                            append(" FROM '").append(this.archivo).append("' WITH ( FIELDTERMINATOR = ',', ROWTERMINATOR = '0x0a')");
                    oDb.execute(q.toString());

                    //3. Extrae el número de serie del medidor
                    String numeroSerie = "";
                    rs = oDb.getRs("SELECT LTRIM(RTRIM(REPLACE(Fecha,'Serial Number: ',''))) as numero_serie from #mrv_".concat(formatter.format(fechaImportacion)).concat(" where fecha like '%Serial Number: %'"));
                    if (rs.next()) {
                        numeroSerie = rs.getString("numero_serie");
                    } else {
                        mensajeError = "No se encontró el número de serie del medidor en el archivo";
                    }

                    //4. Pasa a otra tabla de trabajo la info con la conversión de fecha
                    q = new StringBuilder("SELECT *,convert(datetime,replace(replace(fecha,char(13),''),char(10),'') + ' ' + replace(replace(hora,' ',''),'.','')) as FechaC INTO #mrv__")
                            .append(formatter.format(fechaImportacion))
                            .append(" FROM #mrv_").append(formatter.format(fechaImportacion))
                            .append(" WHERE ISDATE(LTRIM(RTRIM(replace(replace(fecha,char(13),''),char(10),'')))) = 1");
                    oDb.execute(q.toString());

                    //5. Recupera  las horas de instalación y retiro.
                    q = new StringBuilder("SELECT fechac FROM #mrv__").append(formatter.format(fechaImportacion)).append(" WHERE EstatusD = 'Was OFF'");
                    rs = oDb.getRs(q.toString());

                    if (rs.next()) {
                        fechaInstalacion = rs.getString("fechac").replaceAll("-", "/").substring(0, 16);
                        if (rs.next()) {
                            fechaRetiro = rs.getString("fechac").replaceAll("-", "/").substring(0, 16);
                        }
                    }

                    q = new StringBuilder("DELETE #mrv__").append(formatter.format(fechaImportacion)).append(" WHERE EstatusD = 'Was OFF'");
                    oDb.execute(q.toString());

                    String potencia = this.getArchivo().split("_")[1];
                    String rpu = this.getArchivo().split("_")[2].toLowerCase().replace(".csv", "");
                    String zonaInstalacion = "";
                    if (this.getArchivo().split("_").length < 4) {
                        mensajeError = "El archivo no tiene el código de la zona de instalación";
                    } else {
                        zonaInstalacion = this.getArchivo().split("_")[3].toLowerCase().replace(".csv", "");
                    }
                    String nombreBeneficiario = "";
                    Integer claveBeneficiario = 0;
                    Integer claveMedidor = 0;
                    Integer claveMedidorBeneficiario = 0;
                    Integer claveTipoMedicion = 0;
                    Integer claveZonaInstalacion = 0;

                    //5. Busca la clave del beneficiario
                    q = new StringBuilder("SELECT clave_beneficiario, nombre FROM fide_beneficiario WHERE rpu='").append(rpu).append("'");
                    rs = oDb.getRs(q.toString());
                    if (rs.next()) {
                        claveBeneficiario = rs.getInt("clave_beneficiario");
                        nombreBeneficiario = rs.getString("nombre");
                    } else {
                        mensajeError = "El RPU especificado no se encontró en el padron de beneficiarios";
                    }

                    //6. Busca la clave del medidor 
                    if (mensajeError.equals("")) {
                        q = new StringBuilder("SELECT clave_medidor FROM fide_medidor WHERE numero_serie='").append(numeroSerie).append("'");
                        rs = oDb.getRs(q.toString());
                        if (rs.next()) {
                            claveMedidor = rs.getInt("clave_medidor");
                        } else {
                            mensajeError = "El número de serie especificado no se encontró en el catálogo de medidores";
                        }
                    }

                    //6.1 Valida el código de la zona de instalacion
                    q = new StringBuilder("SELECT clave_zona_instalacion FROM fide_zona_instalacion WHERE codigo_zona_instalacion='").append(zonaInstalacion).append("'");
                    rs = oDb.getRs(q.toString());
                    if (rs.next()) {
                        claveZonaInstalacion = rs.getInt("clave_zona_instalacion");
                    } else {
                        mensajeError = "El código especificado de la zona de instalación no se encontró en catálogo";
                    }

                    //7. Inserta la relación entre beneficiario y medidor
                    if (mensajeError.equals("")) {
                        try {
                            claveTipoMedicion = Integer.parseInt(potencia) < 200 ? 1 : 2;
                            oDb.execute("SET DATEFORMAT ymd");
                            q = new StringBuilder("INSERT INTO fide_medidor_beneficiario")
                                    .append("(clave_beneficiario,clave_medidor,")
                                    .append("responsable_instalacion_equipos,")
                                    .append("responsable_autorizacion,")
                                    .append("clave_zona_instalacion,")
                                    .append("clave_tipo_medicion,")
                                    .append("fecha_instalacion_medidor,")
                                    .append("fecha_instalacion_lamparas,")
                                    .append("fecha_retiro_equipos)")
                                    .append(" VALUES(").append(claveBeneficiario).append(",")
                                    .append(claveMedidor).append(",'")
                                    .append(super.getUsuario().getNombre()).append(" ").append(super.getUsuario().getApellido_paterno())
                                    .append(" ").append(super.getUsuario().getApellido_materno() == null ? "" : super.getUsuario().getApellido_materno())
                                    .append("','").append(nombreBeneficiario)
                                    .append("',").append(claveZonaInstalacion)
                                    .append(",").append(claveTipoMedicion)
                                    .append(",'").append(fechaInstalacion)
                                    .append("','").append(fechaInstalacion)
                                    .append("','").append(fechaRetiro)
                                    .append("')");
                            rs = oDb.execute(q.toString());

                            //Recupera la llave primaria del registro recién insertado
                            if (rs != null) {
                                if (rs.next()) {
                                    claveMedidorBeneficiario = rs.getInt(1);
                                }
                            } else {
                                q = new StringBuilder("SELECT MAX(clave_medidor_beneficiario) as clave_medidor_beneficiario FROM fide_medidor_beneficiario WHERE clave_beneficiario=").append(claveBeneficiario).append(" AND clave_medidor=").append(claveMedidor);
                                rs = oDb.getRs(q.toString());
                                if (rs.next()) {
                                    claveMedidorBeneficiario = rs.getInt(1);
                                }
                            }
                        } catch (NumberFormatException nfe) {
                            mensajeError = "Error al insertar relación medidor beneficiario (Potencia no válida)";
                        } catch (Exception e) {
                            mensajeError = "Error al insertar relación medidor beneficiario (".concat(e.getMessage()).concat(")");
                            //Se vuelve a crear el objeto de conexión 
                            //oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
                        }
                    }

                    //6. Transfiere los registros a importacion_detalle
                    formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    if (!mensajeError.equals("")) {
                        q = new StringBuilder("INSERT INTO fide_importacion_detalle (clave_importacion,renglon,rpu,consulta_resultante,mensaje_error, clave_estatus) ")
                                .append("SELECT ").append(this.claveImportacion.toString()).append(",ROW_NUMBER() over (order by FechaC),'").append(rpu)
                                .append("',null,'").append(mensajeError).append("',17")
                                .append(" FROM #mrv__").append(formatter.format(fechaImportacion));
                    } else {
                        //Actualización de la clave_medidor_beneficiario
                        q = new StringBuilder("UPDATE fide_importacion SET clave_medidor_beneficiario=").append(claveMedidorBeneficiario).append(" WHERE clave_importacion=").append(this.claveImportacion);
                        oDb.execute(q.toString());

                        q = new StringBuilder("INSERT INTO fide_importacion_detalle (clave_importacion,renglon,rpu,consulta_resultante, mensaje_error, clave_estatus, importado) ")
                                .append("SELECT ").append(this.claveImportacion.toString()).append(",a.renglon,'").append(rpu).append("',")
                                .append("'INSERT INTO fide_lectura_medidor_beneficiario (clave_medidor_beneficiario,clave_tipo_medicion,potencia,fecha_inicial,fecha_final) ")
                                .append("SELECT ").append(claveMedidorBeneficiario).append(",").append(claveTipoMedicion).append(",")
                                .append(potencia).append(",'''+ CONVERT(varchar,a.FechaC,121) + ''','''+ CONVERT(varchar,b.FechaC,121) + '''',null,16,0 FROM (")
                                .append("SELECT FechaC, ROW_NUMBER() OVER (ORDER BY FechaC) as renglon FROM #mrv__").append(formatter.format(fechaImportacion)).append(" WHERE Estatus LIKE '%1%' )a ")
                                .append("LEFT JOIN(SELECT FechaC,ROW_NUMBER() OVER (ORDER BY FechaC) as renglon FROM #mrv__").append(formatter.format(fechaImportacion)).append(" WHERE Estatus LIKE  '%0%') b ON a.renglon = b.renglon ");
                    }

                    oDb.execute(q.toString());

                    q = new StringBuilder("SELECT COUNT(*) as registros_procesados FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion.toString());
                    rs = oDb.getRs(q.toString());

                    if (rs.next()) {
                        this.registrosProcesados = rs.getInt("registros_procesados");
                        this.registrosPorProcesar = 0;
                    }

                    q = new StringBuilder("UPDATE fide_importacion ")
                            .append(" SET registros_importados=(SELECT COUNT(clave_importacion_detalle) FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion).append("),")
                            .append("registros_procesados=0,")
                            .append("registros_por_procesar=(SELECT COUNT(clave_importacion_detalle) FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=16)")
                            .append(",clave_estatus=3 WHERE clave_importacion=").append(this.claveImportacion.toString());
                    oDb.execute(q.toString());

                } else if (definicionImportacion.getClaveTipoImportacion() == 4) {
                    //Busca el id_control del punto de entrega
                    String idControl = "";
                    String claveEstadoEntrega = "";
                    String claveMunicipioEntrega = "";
                    String claveLocalidadEntrega = "";
                    String error = "";
                    String claveEstatusImportacionDetalle = "1";
                    q = new StringBuilder("SELECT id_control, clave_estado,clave_municipio,clave_localidad FROM fide_punto_entrega WHERE clave_punto=").append(this.clavePunto);
                    rs = oDb.getRs(q.toString());

                    if (rs.next()) {
                        idControl = rs.getString("id_control");
                        claveEstadoEntrega = rs.getString("clave_estado");
                        claveMunicipioEntrega = rs.getString("clave_municipio");
                        claveLocalidadEntrega = rs.getString("clave_localidad");
                    } else {
                        error = "No se encontró el ID control de la tienda seleccionada, verifique por favor";
                        claveEstatusImportacionDetalle = "10";
                    }
                    //Inserta los registros del padrón de cfe al buffer de importación

                    if (error.equals("")) {
                        //Se insertan solo los registros de los beneficiarios que no están en el padrón
                        q = new StringBuilder("INSERT INTO fide_importacion_detalle (clave_importacion,renglon,rpu,id_control,consulta_resultante,clave_estatus,nombre,importado)")
                                .append("SELECT ").append(this.claveImportacion).append(",ROW_NUMBER() OVER (order by rpu) as renglon, rpu,'").append(idControl).append("',")
                                .append("'INSERT INTO fide_beneficiario (rpu,nombre,clave_estado,clave_municipio,poblacion,direccion,colonia,")
                                .append("clave_punto,clave_tipo_padron,clave_estado_entrega,clave_municipio_entrega,clave_localidad_entrega,fecha_entrega,fecha_registro,clave_estatus)")
                                .append("VALUES('''+rpu+''','''+ REPLACE(nombre,'''','''''') +''','+CONVERT(varchar,clave_estado)+','+CONVERT(varchar,clave_municipio)+','''+ REPLACE(POBLACIONCFE,'''','''''') + ''',''' + REPLACE(DIRECCION,'''','''''') + ''',''' + REPLACE(COLONIA,'''','''''') + ''',")
                                .append(this.clavePunto).append(",3,").append(claveEstadoEntrega).append(",").append(claveMunicipioEntrega).append(",").append(claveLocalidadEntrega == null ? "null" : claveLocalidadEntrega).append(",GETDATE()+30,GETDATE(),1)',16,NOMBRE,0 FROM fide_padron_cfe ")
                                .append(" WHERE rpu NOT IN (SELECT rpu FROM fide_beneficiario WHERE clave_tipo_padron<>4) AND ").append(this.consulta).append(this.claveTipoOrdenamiento == 1 ? " ORDER BY NOMBRE" : " ORDER BY COLONIA");
                        oDb.execute(q.toString());

                        //Se insertan solo los registros de los beneficiarios que ya están en el padrón 
                        q = new StringBuilder("INSERT INTO fide_importacion_detalle (clave_importacion,renglon,rpu,id_control,clave_estatus,nombre,importado, mensaje_error)")
                                .append("SELECT ").append(this.claveImportacion).append(",ROW_NUMBER() OVER (order by rpu) as renglon, rpu,'").append(idControl).append("',17,NOMBRE,0,'El RPU indicado ya está registrado en el padrón de beneficiarios' FROM fide_padron_cfe ")
                                .append(" WHERE rpu IN (SELECT rpu FROM fide_beneficiario) AND ").append(this.consulta);
                        oDb.execute(q.toString());

                    } else {
                        q = new StringBuilder("INSERT INTO fide_importacion_detalle (clave_importacion,renglon,rpu,id_control,clave_estatus,mensaje_error,nombre,importado)")
                                .append("SELECT ").append(this.claveImportacion).append(",ROW_NUMBER() OVER (order by rpu) as renglon, rpu,'',10,'").append(error).append("', nombre FROM fide_padron_cfe WHERE ").append(this.consulta);
                        oDb.execute(q.toString());
                    }

                    q = new StringBuilder("UPDATE fide_importacion SET clave_estatus=3 WHERE clave_importacion=").append(this.claveImportacion.toString());
                    oDb.execute(q.toString());

                } else {
                    throw new Fallo("Tipo de importación no especificada, verifique");
                }

            } else if (this.claveEstatus == 4) {  /* Fase de importación */

                definicionImportacion = new DefinicionImportacion(this.getClaveDefinicion(), this.getUsuario());

                if (definicionImportacion.getClaveTipoImportacion() == 1) {  //Nuevos beneficiarios
                    oDb.execute("COMMIT");
                    startedTransaction = false;
                    q = new StringBuilder("SELECT * FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion.toString())
                            .append(" AND clave_estatus in (15) AND consulta_resultante IS NOT NULL AND importado=0");
                    rs = oDb.getRs(q.toString());
                    oDb.execute("SET DATEFORMAT ymd");

                    while (rs.next()) {

                        q = new StringBuilder(rs.getString("consulta_resultante"));
                        try {

                            //Es requisito volver a verificar si no está ya en la tabla fide_beneficiario puyesto que se quitó el constraint para eviatr rpus duplicados
                            /*if (q.toString().toLowerCase().startsWith("INSERT INTO fide_beneficiario") && rs.getInt("clave_tipo_padron")!=4) {

                             }*/
                            oDb.execute(q.toString());
                            q = new StringBuilder("UPDATE fide_importacion_detalle SET importado=1 WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));
                            oDb.execute(q.toString());
                        } catch (Exception e) {

                        }
                    }

                    q = new StringBuilder("UPDATE fide_importacion ")
                            .append(" SET /*registros_importados=(SELECT COUNT(clave_importacion_detalle) FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=18),")
                            .append("registros_procesados=(SELECT COUNT(clave_importacion_detalle) FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=18),")
                            .append("registros_por_procesar=(SELECT COUNT(clave_importacion_detalle) FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=16)")
                            .append(",*/clave_estatus=4 WHERE clave_importacion=").append(this.claveImportacion.toString());
                    oDb.execute(q.toString());

                } else if (definicionImportacion.getClaveTipoImportacion() == 2) { //Avances de entrega 
                    oDb.execute("COMMIT");
                    startedTransaction = false;
                    oDb.execute("SET DATEFORMAT ymd");
                    //Crea tabla temporal
                    fechaImportacion = Calendar.getInstance().getTime();

                    //Se importan solamente los registros de padrones tipo 1,2 y 3
                    q = new StringBuilder("SELECT rpu, fecha_captura, fecha_carga, id_control INTO avance_").append(formatter.format(fechaImportacion)).append(" FROM fide_importacion_detalle WHERE clave_tipo_padron<4 AND clave_estatus=16 AND importado=0 AND clave_importacion=").append(this.claveImportacion.toString());
                    rs = oDb.execute(q.toString());

                    oDb.execute("DISABLE TRIGGER asigna_pagina ON fide_beneficiario");
                    q = new StringBuilder("UPDATE fide_beneficiario ")
                            .append(" SET fecha_lectura=avance_").append(formatter.format(fechaImportacion)).append(".fecha_carga,")
                            .append(" fecha_carga=avance_").append(formatter.format(fechaImportacion)).append(".fecha_captura,")
                            .append(" clave_estatus=2")
                            .append(" FROM ")
                            .append(" fide_beneficiario, avance_").append(formatter.format(fechaImportacion))
                            .append(" WHERE fide_beneficiario.rpu=avance_").append(formatter.format(fechaImportacion)).append(".rpu");

                    oDb.execute(q.toString());

                    //Se importan solamente los registros del padrón tipo 4 
                    q = new StringBuilder("SELECT clave_importacion_detalle, id_control, consulta_resultante FROM fide_importacion_detalle WHERE importado=0 AND clave_tipo_padron=4 AND clave_importacion=").append(this.claveImportacion.toString());
                    rs = oDb.getRs(q.toString());
                    ResultSet rsCarpeta = null;
                    Integer claveCarpeta = 0;
                    Integer clavePunto = 0;

                    DecimalFormat dfFormatter = new DecimalFormat("0000000000");
                    String cq = "";
                    while (rs.next()) {
                        //Verifica si hay carpetas de padrón insitu para tal tienda
                        q = new StringBuilder("SELECT fide_punto_entrega.clave_punto, fide_carpeta.* FROM fide_carpeta, fide_punto_entrega WHERE fide_carpeta.clave_punto=fide_punto_entrega.clave_punto AND fide_carpeta.clave_tipo_carpeta=4 AND fide_punto_entrega.id_control=").append(rs.getString("id_control"));
                        rsCarpeta = oDb.getRs(q.toString());

                        if (rsCarpeta.next()) {
                            claveCarpeta = rsCarpeta.getInt("clave_carpeta");
                            clavePunto = rsCarpeta.getInt("clave_punto");
                        } else {
                            //Recupera el clave_punto a partir del iDCOntrol

                            q = new StringBuilder("SELECT clave_punto FROM fide_punto_entrega WHERE id_control=").append(rs.getInt("id_control"));
                            rsCarpeta = oDb.getRs(q.toString());

                            if (rsCarpeta.next()) {
                                clavePunto = rsCarpeta.getInt("clave_punto");
                            }

                            q = new StringBuilder("INSERT INTO fide_carpeta (carpeta, clave_punto, clave_tipo_carpeta) VALUES('")
                                    .append(dfFormatter.format(rs.getInt("id_control"))).append("IS',").append(clavePunto).append(",4)");
                            oDb.execute(q.toString());

                            q = new StringBuilder("SELECT MAX(clave_carpeta) as clave_carpeta FROM fide_carpeta WHERE clave_punto=").append(clavePunto).append(" AND clave_tipo_carpeta=4");
                            rsCarpeta = oDb.getRs(q.toString());
                            if (rsCarpeta.next()) {
                                claveCarpeta = rsCarpeta.getInt("clave_carpeta");
                            }
                        }

                        // Se asigna la clave de la carpeta antes de insertarla 
                        q = new StringBuilder(rs.getString("consulta_resultante").replaceAll("%clave_carpeta", claveCarpeta.toString()));
                        oDb.execute(q.toString());

                        q = new StringBuilder("UPDATE fide_importacion_detalle SET importado=1 WHERE clave_importacion_detalle=").append(rs.getInt("clave_importacion_detalle"));
                        oDb.execute(q.toString());
                    }

                    oDb.execute("ENABLE TRIGGER asigna_pagina ON fide_beneficiario");

                    q = new StringBuilder("UPDATE fide_importacion_detalle SET importado=1 WHERE clave_importacion=").append(this.claveImportacion.toString()).append(" AND clave_estatus=16");
                    oDb.execute(q.toString());
                } else if (definicionImportacion.getClaveTipoImportacion() == 3) {  // Lecturas de MRV.
                    oDb.execute("COMMIT");
                    startedTransaction = false;
                    q = new StringBuilder("SELECT * FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion.toString())
                            .append(" AND clave_estatus in (16) AND consulta_resultante IS NOT NULL AND importado=0");
                    rs = oDb.getRs(q.toString());

                    oDb.execute("SET DATEFORMAT ymd");

                    while (rs.next()) {
                        q = new StringBuilder(rs.getString("consulta_resultante"));
                        try {
                            oDb.execute(q.toString());
                            q = new StringBuilder("UPDATE fide_importacion_detalle SET importado=1 WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));
                            oDb.execute(q.toString());
                        } catch (Exception e) {
                            throw new Fallo(e.getMessage());
                        }
                    }

                    q = new StringBuilder("UPDATE fide_importacion ")
                            .append(" SET /*registros_importados=(SELECT COUNT(clave_importacion_detalle) FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=18),")
                            .append("registros_procesados=(SELECT COUNT(clave_importacion_detalle) FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=18),")
                            .append("registros_por_procesar=(SELECT COUNT(clave_importacion_detalle) FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion).append(" AND clave_estatus=16)")
                            .append(",*/clave_estatus=4 WHERE clave_importacion=").append(this.claveImportacion.toString());
                    oDb.execute(q.toString());
                } else if (definicionImportacion.getClaveTipoImportacion() == 4) { //Padrón potencial
                    oDb.execute("COMMIT");
                    startedTransaction = false;
                    oDb.execute("SET DATEFORMAT ymd");
                    q = new StringBuilder("SELECT * FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion.toString())
                            .append(" AND clave_estatus in (16) AND consulta_resultante IS NOT NULL AND importado=0");
                    rs = oDb.getRs(q.toString());

                    while (rs.next()) {
                        q = new StringBuilder(rs.getString("consulta_resultante"));
                        try {
                            oDb.execute(q.toString());
                            q = new StringBuilder("UPDATE fide_importacion_detalle SET importado=1 WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));
                            oDb.execute(q.toString());
                        } catch (Exception e) {
                            throw new Fallo(e.getMessage());
                        }
                    }

                    //Inserta carpetas  
                    q = new StringBuilder("insert into fide_carpeta (carpeta,clave_punto,clave_tipo_carpeta)")
                            .append("select distinct (select substring(right('0000000000'+convert(varchar,id_control),10),1,6) from fide_punto_entrega where clave_punto=fide_beneficiario.clave_punto) + '_01_'+ poblacion,")
                            .append("clave_punto, 3 FROM fide_beneficiario WHERE rpu IN (SELECT rpu FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion.toString()).append(")");
                    rs = oDb.execute(q.toString());
                    Integer claveCarpeta = 0;
                    if (rs != null) {
                        if (rs.next()) {
                            claveCarpeta = rs.getInt(1);
                        }
                    } else {
                        q = new StringBuilder("SELECT MAX(clave_carpeta) as clave_carpeta FROM fide_carpeta WHERE clave_punto=").append(this.clavePunto).append(" AND clave_tipo_carpeta=3");
                        rs = oDb.getRs(q.toString());
                        if (rs.next()) {
                            claveCarpeta = rs.getInt(1);
                        }
                    }
                    //Desactiva trigger
                    oDb.execute("DISABLE TRIGGER asigna_pagina ON fide_beneficiario");
                    //Actualiza beneficiarios
                    q = new StringBuilder("update fide_beneficiario set clave_carpeta=").append(claveCarpeta)
                            .append(" WHERE rpu IN (SELECT rpu FROM fide_importacion_detalle WHERE clave_importacion=").append(this.claveImportacion.toString()).append(")");
                    oDb.execute(q.toString());

                    //Ejecuta stored procedure para paginar 
                    oDb.execute("exec paginacion");
                    q = new StringBuilder("UPDATE fide_importacion SET clave_estatus=4 WHERE clave_importacion=").append(this.claveImportacion.toString());
                    oDb.execute(q.toString());

                    //Reactiva trigger
                    oDb.execute("ENABLE TRIGGER asigna_pagina ON fide_beneficiario");

                }
            } else if (this.claveEstatus == 5) { //Extrae información para padrón in situ
                
                //1. Carga la definición de importación
                 oDb.execute("COMMIT");
                 startedTransaction = false;
                definicionImportacion = new DefinicionImportacion(this.getClaveDefinicion(), this.getUsuario());
                if (definicionImportacion.getDefinicionImportacionDetalles().isEmpty()) {
                    throw new Fallo("No está definida la estructura del archivo a importar, verifique");
                }
                
                if (definicionImportacion.getClaveTipoImportacion() == 2) { //Avances de entrega 
                    oDb.execute("SET DATEFORMAT ymd");
                    
                    String mensaje = "";
                    Integer clavePunto = 0;
                    Integer claveEstadoEntrega = 0;
                    Integer claveMunicipioEntrega = 0;
                    Integer claveLocalidadEntrega = 0;
                    String poblacion = "";
                    String tarifa = "";
                    ValidacionBeneficiario v = new ValidacionBeneficiario();

                    //Se importan solamente los registros de padrones tipo 4 que están por validar en sicom
                    q = new StringBuilder("SELECT rpu, fecha_captura, fecha_carga, id_control,clave_importacion_detalle FROM fide_importacion_detalle WHERE clave_tipo_padron=4 AND clave_estatus=18 AND importado=1 AND clave_importacion=").append(this.claveImportacion.toString());
                    
                    rs = oDb.getRs(q.toString());

                    oDb.execute("DISABLE TRIGGER asigna_pagina ON fide_beneficiario");
                    while (rs.next()) {
                        //if (rs.getString("rpu").equals("713930211377")) {
                        //    System.out.println("Stop");                  }

                        try {
                          mensaje = v.getDataFromSICOM(rs.getString("rpu"), super.getUsuario());
                        } catch (Exception e) {
                          mensaje = e.getMessage();
                        }

                        if (v.getClavePunto() == null  && v.getTomaInfoDeLaTienda()) {
                            ResultSet rsTemp = oDb.getRs("SELECT clave_punto, clave_estado, clave_municipio, clave_localidad, (SELECT localidad FROM fide_localidad WHERE clave_localidad=fide_punto_entrega.clave_localidad) as poblacion FROM fide_punto_entrega WHERE id_control=".concat(rs.getString("id_control")));

                            if (rsTemp.next()) {
                                clavePunto = rsTemp.getInt("clave_punto");
                                claveEstadoEntrega = rsTemp.getInt("clave_estado");
                                claveMunicipioEntrega = rsTemp.getInt("clave_municipio");
                                claveLocalidadEntrega = rsTemp.getInt("clave_localidad");
                                poblacion = rsTemp.getString("poblacion");
                                tarifa = v.getTarifa() == null ? "" : v.getTarifa();
                            } else {
                                mensaje = mensaje.concat("; no está definida la tienda ").concat(rs.getString("id_control"));
                            }
                        } else {
                            clavePunto = v.getClavePunto();
                            claveEstadoEntrega = v.getClaveEstadoEntrega();
                            claveMunicipioEntrega = v.getClaveMunicipioEntrega();
                            claveLocalidadEntrega = v.getClaveLocalidadEntrega();
                            poblacion = v.getPoblacion();
                            tarifa = v.getTarifa() == null ? "" : v.getTarifa();
                        }

                        try {
                            if (v.getTomaInfoDeLaTienda()) {
                                q = new StringBuilder().append("UPDATE fide_beneficiario SET tarifa='").append(tarifa).append("',")
                                        .append("clave_estado=").append(claveEstadoEntrega)
                                        .append(",clave_municipio=").append(claveMunicipioEntrega)
                                        .append(",poblacion='").append(poblacion)
                                        .append("' WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));

                            } else {
                                q = new StringBuilder().append("UPDATE fide_beneficiario SET tarifa='").append(tarifa).append("',")
                                        .append("nombre=").append(v.getNombre()==null?"''":"'".concat(v.getNombre().replaceAll("'", "''")).concat("'"))
                                        .append(",direccion=").append(v.getDireccion()==null?"null":"'".concat(v.getDireccion()).concat("'"))
                                        .append(",clave_estado=").append(v.getClaveEstado())
                                        .append(",clave_municipio=").append(v.getClaveMunicipio())
                                        .append(",poblacion=").append(v.getPoblacion()==null?"null":"'".concat(v.getPoblacion().replaceAll("'", "''")).concat("'"))
                                        .append(" WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));
                            }
                            oDb.execute(q.toString());

                            q = new StringBuilder().append("UPDATE fide_importacion_detalle SET mensaje_error='").append(v.getMensajeValidacion()).append("',")
                                    .append("clave_estatus=").append(v.getClaveEstatusImportacionDetalle())
                                    .append(" WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));

                            oDb.execute(q.toString());

                        } catch (Exception e) {
                            throw new Fallo(e.getMessage());
                        }
                    }
                    
                    //Reactiva trigger
                    oDb.execute("ENABLE TRIGGER asigna_pagina ON fide_beneficiario");
                    
                    if (startedTransaction) {
                        oDb.execute("COMMIT");
                    }
                }
                
            }
        } catch (Exception e) {
            this.error = e.getMessage();
            if (startedTransaction) {
                try {
                    oDb.execute("ROLLBACK");
                } catch (Exception exp) {

                }
            }
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(e.getMessage());
        }
        
        return resultadoXML.toString();
    }    
        

    public static void main(String args[]) {
        try {
            Conexion cx = new Conexion("10.55.210.5:1433", "SLFCA", "dmartinez", "dmartinez", Conexion.DbType.valueOf("MSSQL"));
            Usuario usuario = new Usuario();
            usuario.setCx(cx);

            ResultSet rsImportaciones;

            StringBuilder q = new StringBuilder("select * from fide_importacion where clave_definicion=7 and clave_empleado=597 order by clave_importacion");
            rsImportaciones = cx.getRs(q.toString());
            while (rsImportaciones.next()) {
                usuario.setClave(rsImportaciones.getInt("clave_empleado"));

                //Borra el detalle de las importaciones            
                q = new StringBuilder("DELETE FROM fide_importacion_detalle WHERE clave_importacion=").append(rsImportaciones.getInt("clave_importacion"));
                cx.execute(q.toString());

                //Actualiza el estatus de la importacion
                q = new StringBuilder("UPDATE fide_importacion_detalle SET clave_estatus=1 WHERE clave_importacion=").append(rsImportaciones.getInt("clave_importacion"));
                //cx.execute(q.toString());

                //Valida importación
                Importacion i = new Importacion(rsImportaciones.getInt("clave_importacion"), usuario);
                i.setClaveEstatus(2);
                i.update();

                //Importa
                i.setClaveEstatus(4);
                i.update();
            }
        } catch (Exception e) {
            System.out.println("Error :".concat(e.getMessage()));
        }

    }
}
