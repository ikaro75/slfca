/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.administracion;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class DocumentoPartida extends Consulta {
    private Integer clavePartida;
    private Integer claveDocumento;
    private Integer claveProducto;
    private Float cantidad;
    private Integer claveUnidad;
    private Integer claveMoneda;
    private Double tipoCambio;
    private Double importeUnitario;
    private Integer claveImpuesto;
    private Double importeImpuesto;
    private Double importeTotal;
    private Integer claveMonedaGastoEnvio;
    private Double importeGastoEnvio;
    private Double tipoCambioGastoEnvio;

    public DocumentoPartida() {
    }

    public DocumentoPartida(Integer clavePartida) {
        this.clavePartida = clavePartida;
    }

    public DocumentoPartida(Integer clavePartida, Integer claveDocumento, Integer claveProducto, Float cantidad, Integer claveUnidad, Integer claveMoneda, Double tipoCambio, Double importeUnitario, Integer claveImpuesto, Double importeImpuesto, Double importeTotal, Integer claveMonedaGastoEnvio, Double importeGastoEnvio, Double tipoCambioGastoEnvio) {
        this.clavePartida = clavePartida;
        this.claveDocumento = claveDocumento;
        this.claveProducto = claveProducto;
        this.cantidad = cantidad;
        this.claveUnidad = claveUnidad;
        this.claveMoneda = claveMoneda;
        this.tipoCambio = tipoCambio;
        this.importeUnitario = importeUnitario;
        this.claveImpuesto = claveImpuesto;
        this.importeImpuesto = importeImpuesto;
        this.importeTotal = importeTotal;
        this.claveMonedaGastoEnvio = claveMonedaGastoEnvio;
        this.importeGastoEnvio = importeGastoEnvio;
        this.tipoCambioGastoEnvio = tipoCambioGastoEnvio;
    }

    public DocumentoPartida(Consulta c) throws Fallo {
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
                for (Integer k=0; k<aTemp.length; k++) {
                    if (k+1<aTemp.length) {
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
                    } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("float"))  {
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
                                setter.invoke(this, c.getCampos().get(field.getName()).getValor());
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
    
    public Integer getClavePartida() {
        return clavePartida;
    }

    public void setClavePartida(Integer clavePartida) {
        this.clavePartida = clavePartida;
    }

    public Integer getClaveDocumento() {
        return claveDocumento;
    }

    public void setClaveDocumento(Integer claveDocumento) {
        this.claveDocumento = claveDocumento;
    }

    public Integer getClaveProducto() {
        return claveProducto;
    }

    public void setClaveProducto(Integer claveProducto) {
        this.claveProducto = claveProducto;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getClaveUnidad() {
        return claveUnidad;
    }

    public void setClaveUnidad(Integer claveUnidad) {
        this.claveUnidad = claveUnidad;
    }

    public Integer getClaveMoneda() {
        return claveMoneda;
    }

    public void setClaveMoneda(Integer claveMoneda) {
        this.claveMoneda = claveMoneda;
    }

    public Double getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(Double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public Double getImporteUnitario() {
        return importeUnitario;
    }

    public void setImporteUnitario(Double importeUnitario) {
        this.importeUnitario = importeUnitario;
    }

    public Integer getClaveImpuesto() {
        return claveImpuesto;
    }

    public void setClaveImpuesto(Integer claveImpuesto) {
        this.claveImpuesto = claveImpuesto;
    }

    public Double getImporteImpuesto() {
        return importeImpuesto;
    }

    public void setImporteImpuesto(Double importeImpuesto) {
        this.importeImpuesto = importeImpuesto;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Integer getClaveMonedaGastoEnvio() {
        return claveMonedaGastoEnvio;
    }

    public void setClaveMonedaGastoEnvio(Integer claveMonedaGastoEnvio) {
        this.claveMonedaGastoEnvio = claveMonedaGastoEnvio;
    }

    public Double getImporteGastoEnvio() {
        return importeGastoEnvio;
    }

    public void setImporteGastoEnvio(Double importeGastoEnvio) {
        this.importeGastoEnvio = importeGastoEnvio;
    }

    public Double getTipoCambioGastoEnvio() {
        return tipoCambioGastoEnvio;
    }

    public void setTipoCambioGastoEnvio(Double tipoCambioGastoEnvio) {
        this.tipoCambioGastoEnvio = tipoCambioGastoEnvio;
    }

 public String insert() throws Fallo {
        Consulta c = null;
        ResultSet rs;
        StringBuilder q = new StringBuilder();
        StringBuilder resultadoXML = new StringBuilder();
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer folioPagare;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Boolean startedTransaction = false;
        Integer claveBitacora;

        //Al insertar una reservación, falta:
        //1. Generar pendiente para hostess de persona en espera
        try {
            startedTransaction = true;
            oDb.execute("START TRANSACTION");
            
            //Las reglas de negocio son de acuerdo al tipo de documento que se está insertando
            Documento documento = new Documento(this.claveDocumento, super.getUsuario());
            Producto producto = new Producto(this.claveProducto, super.getUsuario());
            
            if (documento.getClave_tipo_documento()==6) { //Compra directa
                //1. Se aumenta el inventario
                Float cantidad = this.cantidad;
                if (producto.getclave_unidad_entrada()!=this.claveUnidad) {
                    if (producto.getClave_unidad_salida()!=this.claveUnidad) {
                        throw new Fallo("La unidad especificada  no concuerda con unidad previamente definida, verifique");
                    } else {
                      cantidad = cantidad / producto.getFactor_entre_unidades();
                    }
                }
                
                q = new StringBuilder().append("UPDATE administrax_producto SET existencia=ifnull(existencia,0)+").append(cantidad).append(" WHERE clave_producto=").append(this.claveProducto);
                resultadoXML.append("<update_administrax_producto>").append(q).append("</update_administrax_producto>");
                oDb.execute(q.toString());
                
                //2. Se guarda la partida
                resultadoXML.append(super.insert(oDb));
            } else if (documento.getClave_tipo_documento()==11) {  //Tarjeta de almacén
                 resultadoXML.append(super.insert(oDb));
                /* if (producto.getClave_tipo_producto()==1) { //Materia prima
                    //Inserción directa
                    //Calcula IVA
                    q = new StringBuilder().append("SELECT porcentaje FROM administrax_impuesto WHERE clave_impuesto=").append(producto.getClave_impuesto_predeterminado());
                    rs = oDb.getRs(q.toString());
                    if (!rs.next())     {
                         throw new Fallo("No se encontró el impuesto para el producto ".concat(producto.getProducto()));
                    }
                    
                    Double importeImpuesto = this.cantidad * this.importeUnitario * (rs.getInt("porcentaje")==0?0:rs.getInt("porcentaje")/100);
                    
                    q = new StringBuilder().append("INSERT INTO administrax_documento_partida")
                        .append("clave_documento, clave_producto, cantidad, clave_unidad, clave_moneda, tipo_cambio,")
                        .append("importe_unitario, clave_impuesto, importe_impuesto, importe_total)") 
                        .append(" VALUES (").append(documento.getClave_documento()).append(",")
                        .append(producto.getClave_producto()).append(",")
                        .append(this.cantidad).append(",")
                        .append(this.claveUnidad).append(",")
                        .append(this.claveMoneda).append(",")
                        .append(this.tipoCambio).append(",")
                        .append(this.importeUnitario).append(",")
                        .append(producto.getClave_impuesto_predeterminado()).append(",")
                        .append(importeImpuesto).append(",")
                        .append((this.cantidad * this.importeUnitario) + importeImpuesto).append(")");
                    
                    oDb.execute(q.toString());
                    
                    // Ahora descuenta el producto del inventario
                    producto.setExistencia(producto.getExistencia()-this.cantidad);
                    //El producto debe mandar notiicacion en caso de que se llegue al stock minimo
                    producto.update(oDb);

                } else if (producto.getClave_tipo_producto()==2) { //Producto terminado
                    //Extrae materias primas
                    q = new StringBuilder().append("SELECT clave_producto FROM restaurantex_producto_ingrediente WHERE clave_producto=").append(this.claveProducto);
                    rs = oDb.getRs(q.toString());
                    while (rs.next()) {
                        producto = new Producto(rs.getInt("clave_producto"), super.getUsuario());
                        q = new StringBuilder().append("SELECT porcentaje FROM administrax_impuesto WHERE clave_impuesto=").append(producto.getClave_impuesto_predeterminado());
                        rs = oDb.getRs(q.toString());
                        if (!rs.next())     {
                             throw new Fallo("No se encontró el impuesto para el producto ".concat(producto.getProducto()));
                        }

                        Double importeImpuesto = this.cantidad * this.importeUnitario * (rs.getInt("porcentaje")==0?0:rs.getInt("porcentaje")/100);

                        q = new StringBuilder().append("INSERT INTO administrax_documento_partida")
                            .append("clave_documento, clave_producto, cantidad, clave_unidad, clave_moneda, tipo_cambio,")
                            .append("importe_unitario, clave_impuesto, importe_impuesto, importe_total)") 
                            .append(" VALUES (").append(documento.getClave_documento()).append(",")
                            .append(producto.getClave_producto()).append(",")
                            .append(this.cantidad).append(",")
                            .append(this.claveUnidad).append(",")
                            .append(this.claveMoneda).append(",")
                            .append(this.tipoCambio).append(",")
                            .append(this.importeUnitario).append(",")
                            .append(producto.getClave_impuesto_predeterminado()).append(",")
                            .append(importeImpuesto).append(",")
                            .append((this.cantidad * this.importeUnitario) + importeImpuesto).append(")");

                        oDb.execute(q.toString());                        
                        // Ahora descuenta el producto del inventario
                        producto.setExistencia(producto.getExistencia()-this.cantidad);
                        //El producto debe mandar notiicacion en caso de que se llegue al stock minimo
                        producto.update(oDb);
                    }
                    //Inserción de materias
                } else if (producto.getClave_tipo_producto()==3) {//Paquete
                    //Extrae productos
                    //Extrae materias primas
                    q = new StringBuilder().append("SELECT clave_producto FROM restaurantex_producto_ingrediente WHERE clave_producto=").append(this.claveProducto);
                    rs = oDb.getRs(q.toString());
                    while (rs.next()) {
                        producto = new Producto(rs.getInt("clave_producto"), super.getUsuario());
                        //Se trata de una materia prima
                        if (producto.getClave_tipo_producto()==1) {
                            q = new StringBuilder().append("SELECT porcentaje FROM administrax_impuesto WHERE clave_impuesto=").append(producto.getClave_impuesto_predeterminado());
                            rs = oDb.getRs(q.toString());
                            if (!rs.next())     {
                                 throw new Fallo("No se encontró el impuesto para el producto ".concat(producto.getProducto()));
                            }

                            Double importeImpuesto = this.cantidad * this.importeUnitario * (rs.getInt("porcentaje")==0?0:rs.getInt("porcentaje")/100);

                            q = new StringBuilder().append("INSERT INTO administrax_documento_partida")
                                .append("clave_documento, clave_producto, cantidad, clave_unidad, clave_moneda, tipo_cambio,")
                                .append("importe_unitario, clave_impuesto, importe_impuesto, importe_total)") 
                                .append(" VALUES (").append(documento.getClave_documento()).append(",")
                                .append(producto.getClave_producto()).append(",")
                                .append(this.cantidad).append(",")
                                .append(this.claveUnidad).append(",")
                                .append(this.claveMoneda).append(",")
                                .append(this.tipoCambio).append(",")
                                .append(this.importeUnitario).append(",")
                                .append(producto.getClave_impuesto_predeterminado()).append(",")
                                .append(importeImpuesto).append(",")
                                .append((this.cantidad * this.importeUnitario) + importeImpuesto).append(")");

                            oDb.execute(q.toString());                        
                            // Ahora descuenta el producto del inventario
                            producto.setExistencia(producto.getExistencia()-this.cantidad);
                            //El producto debe mandar notiicacion en caso de que se llegue al stock minimo
                            producto.update(oDb);                        
                        } else if (producto.getClave_tipo_producto()==2) { 
                            //Extrae materias primas
                            q = new StringBuilder().append("SELECT clave_producto FROM restaurantex_producto_ingrediente WHERE clave_producto=").append(this.claveProducto);
                            rs = oDb.getRs(q.toString());
                            while (rs.next()) {
                                producto = new Producto(rs.getInt("clave_producto"), super.getUsuario());
                                q = new StringBuilder().append("SELECT porcentaje FROM administrax_impuesto WHERE clave_impuesto=").append(producto.getClave_impuesto_predeterminado());
                                rs = oDb.getRs(q.toString());
                                if (!rs.next())     {
                                     throw new Fallo("No se encontró el impuesto para el producto ".concat(producto.getProducto()));
                                }

                                Double importeImpuesto = this.cantidad * this.importeUnitario * (rs.getInt("porcentaje")==0?0:rs.getInt("porcentaje")/100);

                                q = new StringBuilder().append("INSERT INTO administrax_documento_partida")
                                    .append("clave_documento, clave_producto, cantidad, clave_unidad, clave_moneda, tipo_cambio,")
                                    .append("importe_unitario, clave_impuesto, importe_impuesto, importe_total)") 
                                    .append(" VALUES (").append(documento.getClave_documento()).append(",")
                                    .append(producto.getClave_producto()).append(",")
                                    .append(this.cantidad).append(",")
                                    .append(this.claveUnidad).append(",")
                                    .append(this.claveMoneda).append(",")
                                    .append(this.tipoCambio).append(",")
                                    .append(this.importeUnitario).append(",")
                                    .append(producto.getClave_impuesto_predeterminado()).append(",")
                                    .append(importeImpuesto).append(",")
                                    .append((this.cantidad * this.importeUnitario) + importeImpuesto).append(")");

                                oDb.execute(q.toString());                        
                                // Ahora descuenta el producto del inventario
                                producto.setExistencia(producto.getExistencia()-this.cantidad);
                                //El producto debe mandar notiicacion en caso de que se llegue al stock minimo
                                producto.update(oDb);    
                            }    
                        }
                    }                    
                }    
   

                /*        q = new StringBuilder().append("SELECT clave_empleado FROM be_empleado WHERE clave_perfil=12");
                        rs = oDb.getRs(q.toString());

                        while (rs.next()) {
                            q = new StringBuilder().append("INSERT INTO be_actividad")
                                    .append("(actividad,fecha_inicial,")
                                    .append("clave_estatus, clave_empleado_solicitante, alertar_con_anticipacion, clave_empleado_asignado,")
                                    .append("observacion, clave_forma, clave_registro)")
                                    .append("VALUES ('")
                                    .append("<a href=\"#\" class=\"gridlink\" id=\"-36-604-").append(claveDocumento).append("\">").append("El producto ").append(producto.getProducto()).append(" ha alcanzado el nivel de stock mínimo. Resurta, por favor.',")
                                    .append("CURDATE()")
                                    .append("',0,1,0,").append(rs.getInt("clave_empleado")).append(",").append(this.nota == null ? "NULL" : "'".concat(this.nota.replaceAll("'", "''")).concat("'")).append(",601,0")
                                    .append(")");

                            resultadoXML.append("<insert_actividad>").append(q).append("</insert_actividad>");
                            oDb.execute(q.toString());
                        } */
                
            } 
            
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Boolean startedTransaction = false;
        Integer claveBitacora;
        Integer clavePoliza;
        try {

            //1. Abre transaccion
            startedTransaction = true;
            oDb.execute("START TRANSACTION");
          //Las reglas de negocio son de acuerdo al tipo de documento que se está insertando
            Documento documento = new Documento(this.claveDocumento, super.getUsuario());
            Producto producto = new Producto(this.claveProducto, super.getUsuario());
            
            if (documento.getClave_tipo_documento()==6) { //Compra directa
                //1. Se aumenta el inventario
                Float cantidad = this.cantidad;
                if (producto.getclave_unidad_entrada()!=this.claveUnidad) {
                    if (producto.getClave_unidad_salida()!=this.claveUnidad) {
                        throw new Fallo("La unidad especificada  no concuerda con unidad previamente definida, verifique");
                    } else {
                      cantidad = cantidad / producto.getFactor_entre_unidades();
                    }
                }
                
                q.append("UPDATE administrax_producto SET existencia=ifnull(existencia,0)-").append(super.getCampos().get("cantidad").getValorOriginal()).append("+").append(cantidad).append(" WHERE clave_producto=").append(this.claveProducto);
                resultadoXML.append("<update_administrax_producto>").append(q).append("</update_administrax_producto>");
                oDb.execute(q.toString());
                
                //2. Se guarda la partida
                resultadoXML.append(super.update(oDb));
            }
  
            oDb.execute("COMMIT");
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
    
}
