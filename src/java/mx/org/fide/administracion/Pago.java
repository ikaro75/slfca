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
import java.util.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class Pago extends Consulta {
    private Integer clavePago;
    private Integer claveCuenta;
    private Date fecha;
    private Integer claveTipoTransaccion;
    private Double importePago;
    private Integer claveMoneda;
    private Double tipoCambio;
    private Integer claveFormaPago;
    private String referenciaDocumento;
    private Integer claveCuentaOrigen;
    private Integer claveCuentaDestino;
    
    private Integer claveEmpleado;
    private Integer claveDocumento;
    private Integer claveEstatus;
    private String restaurantexClaveCuentas;
    
    public Pago() {
    }

    public Pago(Integer clavePago) {
        this.clavePago = clavePago;
    }

    public Pago(Integer clavePago, Integer claveDocumento, Date fecha, Integer claveTipoTransaccion, Double importePago, Integer claveMoneda, Integer claveEstatus, String restaurantexClaveCuentas) {
        this.clavePago = clavePago;
        this.claveDocumento = claveDocumento;
        this.fecha = fecha;
        this.claveTipoTransaccion = claveTipoTransaccion;
        this.importePago = importePago;
        this.claveMoneda = claveMoneda;
        this.claveEstatus = claveEstatus;
    }

    public Pago(Consulta c) throws Fallo {
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
                                if (c.getCampos().get(dbField.toString())!=null)
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

    public Integer getClavePago() {
        return clavePago;
    }

    public void setClavePago(Integer clavePago) {
        this.clavePago = clavePago;
    }

    public Integer getClaveCuenta() {
        return claveCuenta;
    }

    public void setClaveCuenta(Integer claveCuenta) {
        this.claveCuenta = claveCuenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getClaveTipoTransaccion() {
        return claveTipoTransaccion;
    }

    public void setClaveTipoTransaccion(Integer claveTipoTransaccion) {
        this.claveTipoTransaccion = claveTipoTransaccion;
    }

    public Double getImportePago() {
        return importePago;
    }

    public void setImportePago(Double importePago) {
        this.importePago = importePago;
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

    public Integer getClaveFormaPago() {
        return claveFormaPago;
    }

    public void setClaveFormaPago(Integer claveFormaPago) {
        this.claveFormaPago = claveFormaPago;
    }

    public String getReferenciaDocumento() {
        return referenciaDocumento;
    }

    public void setReferenciaDocumento(String referenciaDocumento) {
        this.referenciaDocumento = referenciaDocumento;
    }

    public Integer getClaveCuentaOrigen() {
        return claveCuentaOrigen;
    }

    public void setClaveCuentaOrigen(Integer claveCuentaOrigen) {
        this.claveCuentaOrigen = claveCuentaOrigen;
    }

    public Integer getClaveCuentaDestino() {
        return claveCuentaDestino;
    }

    public void setClaveCuentaDestino(Integer claveCuentaDestino) {
        this.claveCuentaDestino = claveCuentaDestino;
    }

    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    public Integer getClaveDocumento() {
        return claveDocumento;
    }

    public void setClaveDocumento(Integer claveDocumento) {
        this.claveDocumento = claveDocumento;
    }

    public Integer getClaveEstatus() {
        return claveEstatus;
    }

    public void setClaveEstatus(Integer claveEstatus) {
        this.claveEstatus = claveEstatus;
    }

    public String getRestaurantexClaveCuentas() {
        return restaurantexClaveCuentas;
    }

    public void setRestaurantexClaveCuentas(String restaurantexClaveCuentas) {
        this.restaurantexClaveCuentas = restaurantexClaveCuentas;
    }
    
    public String insert() throws Fallo {
        /*Consulta c = null;
        ResultSet rs;
        StringBuilder q = new StringBuilder();
        StringBuilder resultadoXML = new StringBuilder();
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer folioPagare;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Boolean startedTransaction = false;
        Integer claveBitacora;
        Double importePago=0d;
        Double importeDescuento=0d;
        Double importePropina=0d;
        Double saldoComanda=0d;
        //Al insertar pago solo falta verificar el saldo de la cuenta, si es cero o menor entonces la cuenta cambia de estado a pagado
        //y se libera la mesa
        
        try {
            startedTransaction = true;
            oDb.execute("START TRANSACTION");
            resultadoXML.append(super.insert(oDb));
            Cuenta cuenta= new Cuenta(this.claveCuenta, super.getUsuario());
            //Aplica los pagos a las comandas
            q.append("SELECT clave_cuenta_detalle, importe_total, importe_pagado")
             .append(" FROM restaurantex_cuenta_detalle")
             .append(" WHERE clave_cuenta=").append(cuenta.getClave_cuenta())
             .append(this.restaurantexClaveCuentas!=null && !this.restaurantexClaveCuentas.equals("")?" AND clave_cuenta_detalle IN (".concat(this.restaurantexClaveCuentas).concat(")"):"");
            rs = oDb.getRs(q.toString());
            importePago = this.importePago;
            saldoComanda = 0d;
            while (rs.next()) {
                importeDescuento=rs.getDouble("importe_total") * (cuenta.getPorcentaje_descuento()==0?0:cuenta.getPorcentaje_descuento()/100);
                importePropina = (rs.getDouble("importe_total")-importeDescuento) * (cuenta.getPorcentaje_propina()==0?0:cuenta.getPorcentaje_propina()/100);
                saldoComanda = rs.getDouble("importe_total") - importeDescuento + importePropina  - rs.getDouble("importe_pagado");
                
                if (importePago>saldoComanda) {
                    q = new StringBuilder("").append("UPDATE restaurantex_cuenta_detalle SET importe_pagado=").append(saldoComanda).append(" WHERE clave_cuenta_detalle=").append(rs.getInt("clave_cuenta_detalle"));
                    resultadoXML.append(q);
                    oDb.execute(q.toString());
                } else if (importePago<=saldoComanda) {
                    q = new StringBuilder("").append("UPDATE restaurantex_cuenta_detalle SET importe_pagado=").append(importePago).append(" WHERE clave_cuenta_detalle=").append(rs.getInt("clave_cuenta_detalle"));
                    resultadoXML.append(q);
                    oDb.execute(q.toString());
                    break;
                }
                importePago-=saldoComanda;
            }
            //Verifica la suma de los pagos asociados a la cuenta
            q = new StringBuilder().append("SELECT SUM(importe_pago) as pagado FROM administrax_pago WHERE clave_cuenta=").append(this.claveCuenta);
            rs = oDb.getRs(q.toString());
            if (rs.next()) {
                importePago= rs.getDouble("pagado");
            }
            
            // Si el importe del pago es mayor o igual se cambia el estatus de la cuenta
            if (importePago>cuenta.getImporteTotal()) {
                cuenta.getCampos().get("clave_estatus").setValor("6");
            } else {
                cuenta.getCampos().get("clave_estatus").setValor("5");
            }
            resultadoXML.append(cuenta.update(oDb));
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
        ResultSet rs;*/
        StringBuilder resultadoXML = new StringBuilder();
        /*StringBuilder q = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Boolean startedTransaction = false;
        Integer claveBitacora;
        Integer clavePoliza;
        Float importePago = 0f;
        try {

            resultadoXML.append(super.insert(oDb));
            //Verifica la suma de los pagos asociados a la cuenta
            q = new StringBuilder().append("SELECT SUM(importe_pago) as pagado FROM administrax_pago WHERE clave_cuenta=").append(this.claveCuenta);
            rs = oDb.getRs(q.toString());
            if (rs.next()) {
                importePago= rs.getFloat("importe_pago");
            }
            
            Cuenta cuenta = new Cuenta(this.claveCuenta, super.getUsuario());
            // Si el importe del pago es mayor o igual se cambia el estatus de la cuenta
            if (importePago>cuenta.getImporteTotal()) {
                cuenta.getCampos().get("clave_estatus").setValor("6");
            } else {
                cuenta.getCampos().get("clave_estatus").setValor("5");
            }
            
            cuenta.update(oDb);
            oDb.execute("COMMIT");
            oDb.cierraConexion();
            oDb = null;*/
            return resultadoXML.toString();
        /*} catch (Exception e) {
            if (startedTransaction) {
                oDb.execute("ROLLBACK");
            }

            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(e.getMessage());

        }*/
    }
    
}
