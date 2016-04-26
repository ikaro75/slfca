
package mx.org.fide.administracion;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class Documento extends Consulta {
    private Integer clave_documento;
    private Integer clave_documento_padre;
    private Integer clave_empresa;
    private Date fecha;
    private Integer clave_tipo_documento;
    private Integer folio_documento;
    private Integer clave_cliente;
    private Integer clave_proveedor;
    private Integer clave_producto;
    private Integer clave_almacen;
    private String direccion_entrega;
    private String referencia;
    private Date fecha_recepcion;
    private Date fecha_pago;
    private Integer clave_estatus;
    private Integer pago_a_plazo;
    private String observaciones;
    private Integer clave_moneda_enganche;
    private Float tipo_cambio_enganche;
    private Float importe_enganche;
    private Integer numero_pagos;
    private Integer clave_moneda_pagos;
    private Float tipo_cambio_pagos;
    private Float importe_pagos;
    private Integer dias_plazo;
    private Date fecha_primer_pago;
    private Float tasa_interes_pago;

    /**
     * Clase interna que define el tipo de documento
     */
    public enum TipoDeDocumento {
        /**
         * Documentos asociados a una venta
         */
        ventaCotizacion,
        ventaPedido, 
        ventaRemision,
        ventaFactura,
        ventaDevolucion,
        
        /**
         * Documentos asociados a una compra 
         */
        compraDirecta,
        compraRecepcion,
        compraOrden,
        compraSolicitud,
        compraDevolucion,
        
        /** 
         * Documento asociado con almacen
         */
        tarjetaAlmacen
    };
    
    public Documento() {
    }

    public Documento(Integer clave_documento, Usuario usuario) {
        super.setUsuario(usuario);
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(),super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(),super.getUsuario().getCx().getDbType());

        this.clave_documento = clave_documento;
        try {
           ResultSet rs = oDb.getRs("select * from administrax_documento where clave_documento=".concat(clave_documento.toString()));
           
           if (rs.next()) {
                this.clave_documento = rs.getInt("clave_documento");
                this.clave_documento_padre = rs.getInt("clave_documento_padre");
                this.clave_empresa = rs.getInt("clave_empresa");
                this.fecha = rs.getDate("fecha");
                this.clave_tipo_documento = rs.getInt("clave_tipo_documento");
                this.folio_documento = rs.getInt("folio_documento");
                this.clave_cliente = rs.getInt("clave_cliente");
                this.clave_proveedor  = rs.getInt("clave_proveedor");
                this.clave_almacen = rs.getInt("clave_almacen");
                this.direccion_entrega = rs.getString("direccion_entrega"); 
                this.referencia = rs.getString("referencia"); 
                this.fecha_recepcion = rs.getDate("fecha_recepcion");
                this.fecha_pago = rs.getDate("fecha_pago");
                this.clave_estatus = rs.getInt("clave_estatus");
                this.pago_a_plazo = rs.getInt("pago_a_plazo");
                this.observaciones = rs.getString("observaciones"); 
                this.clave_moneda_enganche = rs.getInt("clave_moneda_enganche");
                this.tipo_cambio_enganche = rs.getFloat("tipo_cambio_enganche");
                this.importe_enganche = rs.getFloat("importe_enganche");
                this.numero_pagos = rs.getInt("numero_pagos");
                this.clave_moneda_pagos = rs.getInt("clave_moneda_pagos");
                this.tipo_cambio_pagos = rs.getFloat("tipo_cambio_pagos");
                this.importe_pagos = rs.getFloat("importe_pagos");
                this.dias_plazo = rs.getInt("dias_plazo");
                this.fecha_primer_pago = rs.getDate("fecha_primer_pago");
                this.tasa_interes_pago = rs.getFloat("tasa_interes_pago");
    
           } else {
               rs.close();
               throw new Fallo ("Error al recuperar actividad especificada");
           }
           
           rs.close();
        } catch(Exception e ){ 
            
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }        
    }

    public Documento(Integer clave_documento, Integer clave_empresa, Date fecha, Integer clave_tipo_documento, Integer folio_documento, Date fecha_recepcion) {
        this.clave_documento = clave_documento;
        this.clave_empresa = clave_empresa;
        this.fecha = fecha;
        this.clave_tipo_documento = clave_tipo_documento;
        this.folio_documento = folio_documento;
        this.fecha_recepcion = fecha_recepcion;
    }

    public Documento(Consulta c) throws Fallo {
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

                if (c.getCampos().get(field.getName()) != null) {
                    if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("varchar")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), String.class);
                    } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("int") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("bigint")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Integer.class);
                    } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("float") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("decimal")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Float.class);
                    } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), java.util.Date.class);
                    }
                    
                    if (c.getCampos().get(field.getName()).getValor() != null && !c.getAccion().equals("delete")) {

                        //Valida si el valor del campo es una cadena vacío 
                        if (c.getCampos().get(field.getName()).getValor().equals("") && !c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("varchar")) {
                            if (c.getCampos().get(field.getName()).getObligatorio() == 1 && !c.getCampos().get(field.getName()).isAutoIncrement()) {
                                throw new Fallo("El valor del campo ".concat(c.getCampos().get(field.getName()).getAlias()).concat(" no es válido, verifique"));
                            }
                        } else {
                            if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("varchar")) {
                                setter.invoke(this, c.getCampos().get(field.getName()).getValor());
                            } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("int") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("bigint")) {
                                setter.invoke(this, Integer.parseInt(c.getCampos().get(field.getName()).getValor()));
                            } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("float") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("decimal")) {
                                setter.invoke(this, Float.parseFloat(c.getCampos().get(field.getName()).getValor()));
                            } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                                setter.invoke(this, formatter.parse(c.getCampos().get(field.getName()).getValor()));
                            }
                        }
                    } else if (c.getCampos().get(field.getName()).isAutoIncrement()) {
                        setter.invoke(this, Integer.parseInt(c.getPk()));
                    } else if (c.getCampos().get(field.getName()).getObligatorio() == 1) {
                        throw new Fallo("No se especificó ".concat(c.getCampos().get(field.getName()).getAlias()).concat(", verifique"));
                    }
                } 
            }

            //Aquí debe ir el código para cargar desde el constructor los detalles del pagare


        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }

    }

    public Integer getClave_documento() {
        return clave_documento;
    }

    public void setClave_documento(Integer clave_documento) {
        this.clave_documento = clave_documento;
    }

    public Integer getClave_documento_padre() {
        return clave_documento_padre;
    }

    public void setClave_documento_padre(Integer clave_documento_padre) {
        this.clave_documento_padre = clave_documento_padre;
    }

    public Integer getClave_empresa() {
        return clave_empresa;
    }

    public void setClave_empresa(Integer clave_empresa) {
        this.clave_empresa = clave_empresa;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getClave_tipo_documento() {
        return clave_tipo_documento;
    }

    public void setClave_tipo_documento(Integer clave_tipo_documento) {
        this.clave_tipo_documento = clave_tipo_documento;
    }

    public Integer getFolio_documento() {
        return folio_documento;
    }

    public void setFolio_documento(Integer folio_documento) {
        this.folio_documento = folio_documento;
    }
    
    public Integer getClave_cliente() {
        return clave_cliente;
    }

    public void setClave_cliente(Integer clave_cliente) {
        this.clave_cliente = clave_cliente;
    }

    public Integer getClave_proveedor() {
        return clave_proveedor;
    }

    public void setClave_proveedor(Integer clave_proveedor) {
        this.clave_proveedor = clave_proveedor;
    }

    public Integer getClave_producto() {
        return clave_producto;
    }

    public void setClave_producto(Integer clave_producto) {
        this.clave_producto = clave_producto;
    }
    
    
    public Integer getClave_almacen() {
        return clave_almacen;
    }

    public void setClave_almacen(Integer clave_almacen) {
        this.clave_almacen = clave_almacen;
    }

    public String getDireccion_entrega() {
        return direccion_entrega;
    }

    public void setDireccion_entrega(String direccion_entrega) {
        this.direccion_entrega = direccion_entrega;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Date getFecha_recepcion() {
        return fecha_recepcion;
    }

    public void setFecha_recepcion(Date fecha_recepcion) {
        this.fecha_recepcion = fecha_recepcion;
    }

    public Date getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Date fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public Integer getClave_estatus() {
        return clave_estatus;
    }

    public void setClave_estatus(Integer clave_estatus) {
        this.clave_estatus = clave_estatus;
    }

    public Integer getPago_a_plazo() {
        return pago_a_plazo;
    }

    public void setPago_a_plazo(Integer pago_a_plazo) {
        this.pago_a_plazo = pago_a_plazo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getClave_moneda_enganche() {
        return clave_moneda_enganche;
    }

    public void setClave_moneda_enganche(Integer clave_moneda_enganche) {
        this.clave_moneda_enganche = clave_moneda_enganche;
    }

    public Float getTipo_cambio_enganche() {
        return tipo_cambio_enganche;
    }

    public void setTipo_cambio_enganche(Float tipo_cambio_enganche) {
        this.tipo_cambio_enganche = tipo_cambio_enganche;
    }

    public Float getImporte_enganche() {
        return importe_enganche;
    }

    public void setImporte_enganche(Float importe_enganche) {
        this.importe_enganche = importe_enganche;
    }

    public Integer getNumero_pagos() {
        return numero_pagos;
    }

    public void setNumero_pagos(Integer numero_pagos) {
        this.numero_pagos = numero_pagos;
    }

    public Integer getClave_moneda_pagos() {
        return clave_moneda_pagos;
    }

    public void setClave_moneda_pagos(Integer clave_moneda_pagos) {
        this.clave_moneda_pagos = clave_moneda_pagos;
    }

    public Float getTipo_cambio_pagos() {
        return tipo_cambio_pagos;
    }

    public void setTipo_cambio_pagos(Float tipo_cambio_pagos) {
        this.tipo_cambio_pagos = tipo_cambio_pagos;
    }

    public Float getImporte_pagos() {
        return importe_pagos;
    }

    public void setImporte_pagos(Float importe_pagos) {
        this.importe_pagos = importe_pagos;
    }

    public Integer getDias_plazo() {
        return dias_plazo;
    }

    public void setDias_plazo(Integer dias_plazo) {
        this.dias_plazo = dias_plazo;
    }

    public Date getFecha_primer_pago() {
        return fecha_primer_pago;
    }

    public void setFecha_primer_pago(Date fecha_primer_pago) {
        this.fecha_primer_pago = fecha_primer_pago;
    }

    public Float getTasa_interes_pago() {
        return tasa_interes_pago;
    }

    public void setTasa_interes_pago(Float tasa_interes_pago) {
        this.tasa_interes_pago = tasa_interes_pago;
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

        try {
            //1. Abre transaccion
            startedTransaction = true;
            oDb.execute("START TRANSACTION");
            //Se debe de extraer el folio de compras
            q.append("SELECT folio FROM administrax_tipo_documento WHERE clave_tipo_documento=").append(this.getClave_tipo_documento());
            
            rs= oDb.getRs(q.toString());
            
            if (rs.next()) {
                this.folio_documento= rs.getInt("folio") + 1;
                this.getCampos().get("folio_documento").setValor(this.folio_documento.toString());
            }    
            else 
               throw new Fallo("No fue posible definir el folio del documento");
            
            //... y luego se debe de actualizar el control de folios
            q = new StringBuilder().append("UPDATE administrax_tipo_documento SET folio=folio+1 WHERE clave_tipo_documento=").append(this.getClave_tipo_documento());
            oDb.execute(q.toString());
            resultadoXML.append(super.insert(oDb));
            
            if (resultadoXML.toString().contains("error")) {
                throw new Fallo("Error al insertar documento, verifique");
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
            //Al actualizar una reservación, falta:
            //1. Tomar el número id de la mesa para realizar el insert con la relación reservación mesa
            //2. Generar el pendiente para el mesero de abrir cuenta
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

