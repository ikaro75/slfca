package mx.org.fide.finanzas;

import mx.org.fide.modelo.Campo;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Poliza extends Consulta {

    private Integer clave_poliza;
    private Date fecha;
    private Integer clave_empleado;
    private Integer clave_tipo_transaccion;
    private Integer clave_estatus;
    private Boolean excluido;
    private String nota;
    private ArrayList<PolizaDetalle> polizaDetalles = new ArrayList<PolizaDetalle>();

    public Poliza() {
        super.setTabla("fx_poliza");
        super.setLlavePrimaria("clave_poliza");
    }

    public Poliza(Integer clave_poliza, Usuario usuario) throws Fallo {
        super.setTabla("fx_poliza");
        super.setLlavePrimaria("clave_poliza");
        super.setPk(String.valueOf(clave_poliza));
        super.setUsuario(usuario);

        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fx_poliza where clave_poliza=".concat(String.valueOf(clave_poliza)));

            if (rs.next()) {

                this.clave_poliza = rs.getInt("clave_poliza");
                this.fecha = rs.getDate("fecha");
                this.clave_empleado = rs.getInt("clave_empleado");
                this.clave_tipo_transaccion = rs.getInt("clave_tipo_transaccion");
                this.clave_estatus = rs.getInt("clave_estatus");
                this.excluido = rs.getBoolean("excluido");
                this.nota = rs.getString("nota");

                rs = oDb.getRs("SELECT clave_poliza_detalle FROM fx_poliza_detalle WHERE clave_poliza=".concat(this.clave_poliza.toString()));

                while (rs.next()) {
                    PolizaDetalle polizaDetalle = new PolizaDetalle(rs.getInt("clave_poliza_detalle"), this.getUsuario());
                    this.polizaDetalles.add(polizaDetalle);
                }

                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró la póliza especificada");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la póliza");
        }
    }

    public Poliza(Consulta c) throws Fallo {
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
        this.polizaDetalles = new ArrayList<PolizaDetalle>();

        //Necesario para verificar si se deben insertar o actualizar el detalle
        Conexion oDb = new Conexion(c.getUsuario().getCx().getServer(),
                c.getUsuario().getCx().getDb(),
                c.getUsuario().getCx().getUser(),
                c.getUsuario().getCx().getPw(),
                c.getUsuario().getCx().getDbType());
        ResultSet rs = null;
        try {
            if (!c.getPk().equals("0")) {
                this.clave_poliza = Integer.parseInt(c.getPk());
            }

            if (c.getCampos().get("fecha").getValor() != null) {
                this.fecha = formatter.parse(c.getCampos().get("fecha").getValor());
            } else {
                throw new Fallo("No se especificó la fecha, verifique");
            }

            if (c.getCampos().get("clave_empleado").getValor() != null) {
                this.clave_empleado = Integer.parseInt(c.getCampos().get("clave_empleado").getValor());
            } else {
                throw new Fallo("No se especificó el empleado, verifique");
            }

            if (c.getCampos().get("clave_tipo_transaccion").getValor() != null) {
                this.clave_tipo_transaccion = Integer.parseInt(c.getCampos().get("clave_tipo_transaccion").getValor());
            } else {
                throw new Fallo("No se especificó el tipo de transacción, verifique");
            }

            if (c.getCampos().get("clave_estatus").getValor() != null) {
                this.clave_estatus = Integer.parseInt(c.getCampos().get("clave_estatus").getValor());
            } else {
                throw new Fallo("No se especificó el estatus, verifique");
            }

            if (c.getCampos().get("excluido").getValor() != null) {
                this.excluido = Boolean.parseBoolean(c.getCampos().get("excluido").getValor());
            }

            if (c.getCampos().get("nota").getValor() != null) {
                this.nota = c.getCampos().get("nota").getValor();
            }

            if (c.getCampos().get("importe").getValor() == null) {
                throw new Fallo("El importe no se especificó, verifique");
            }

            if (Float.parseFloat(c.getCampos().get("importe").getValor()) == 0) {
                throw new Fallo("El importe no puede ser igual a cero, verifique");
            }
            /* Detalle del cargo */
            PolizaDetalle polizaDetalle = new PolizaDetalle();
            // Se determina si ya está registrado el detalle
            if (this.clave_poliza != null) {
                rs = oDb.getRs("SELECT clave_poliza_detalle FROM fx_poliza_detalle WHERE clave_poliza=".concat(this.clave_poliza.toString()).concat(" AND cargo=1"));
                if (rs.next()) {
                    polizaDetalle.setClave_poliza_detalle(rs.getInt("clave_poliza_detalle"));
                }
            }

            if (c.getCampos().get("clave_poliza_detalle_origen") != null) {
                if (c.getCampos().get("clave_poliza_detalle_origen").getValor() != null) {
                    polizaDetalle.setClave_poliza_detalle(Integer.parseInt(c.getCampos().get("clave_poliza_detalle_origen").getValor()));
                }
            }

            if (this.clave_tipo_transaccion == 1 /* Depósito */)  {
                polizaDetalle.setCargo(false);

                if (c.getCampos().get("clave_cuenta_destino").getValor() == null) {
                    throw new Fallo("No se especificó la cuenta origen");
                } else {
                    polizaDetalle.setClave_cuenta(Integer.parseInt(c.getCampos().get("clave_cuenta_destino").getValor()));
                }
            } else if (this.clave_tipo_transaccion == 2
                    || this.clave_tipo_transaccion == 3
                    || this.clave_tipo_transaccion == 4
                    || this.clave_tipo_transaccion == 5
                    || this.clave_tipo_transaccion == 6
                    || this.clave_tipo_transaccion == 7
                    || this.clave_tipo_transaccion == 8
                    || this.clave_tipo_transaccion == 9
                    || this.clave_tipo_transaccion == 10 /* Transfencia de pagaré */
                    || this.clave_tipo_transaccion == 11
                    || this.clave_tipo_transaccion == 12) {
                polizaDetalle.setCargo(false);

                if (c.getCampos().get("clave_cuenta_origen").getValor() == null) {
                    throw new Fallo("No se especificó la cuenta origen");
                } else {
                    polizaDetalle.setClave_cuenta(Integer.parseInt(c.getCampos().get("clave_cuenta_origen").getValor()));
                }

            } else {
                throw new Fallo("Tipo de transacción desconocida");
            }

            polizaDetalle.setImporte(Float.parseFloat(c.getCampos().get("importe").getValor()));
            polizaDetalle.setClave_tipo_transaccion(this.clave_tipo_transaccion);

            if (c.getCampos().get("folio_pagare") != null) {
                if (c.getCampos().get("folio_pagare").getValor() != null) {
                    polizaDetalle.setFolio_pagare(Integer.parseInt(c.getCampos().get("folio_pagare").getValor()));
                }
            }

            if (c.getCampos().get("clave_moneda").getValor() == null) {
                throw new Fallo("No se especificó la moneda");
            } else {
                polizaDetalle.setClave_moneda(Integer.parseInt(c.getCampos().get("clave_moneda").getValor()));
            }

            if (c.getCampos().get("tipo_cambio").getValor() == null) {
                throw new Fallo("No se especificó el tipo de cambio");
            } else {
                polizaDetalle.setTipo_cambio(Float.parseFloat(c.getCampos().get("tipo_cambio").getValor()));
            }

            if (c.getCampos().get("clave_beneficiario").getValor() != null && !c.getCampos().get("clave_beneficiario").getValor().equals("")) {
                polizaDetalle.setClave_beneficiario(Integer.parseInt(c.getCampos().get("clave_beneficiario").getValor()));
            }

            if (c.getCampos().get("clave_clase").getValor() != null && !c.getCampos().get("clave_clase").getValor().equals("")) {
                polizaDetalle.setClave_clase(Integer.parseInt(c.getCampos().get("clave_clase").getValor()));
            }

            if (c.getCampos().get("folio_cheque").getValor() != null && !c.getCampos().get("folio_cheque").getValor().equals("")) {
                polizaDetalle.setFolio_cheque(c.getCampos().get("folio_cheque").getValor());
            }

            this.polizaDetalles.add(polizaDetalle);

            /* Detalle del abono */
            //La lógica de la aplicación depende de si se tienen transacciones divididas

            if (c.getCampos().get("dividir_transaccion").getValor().equals("0")) {
                polizaDetalle = new PolizaDetalle();

                if (this.clave_poliza != null) {
                    rs = oDb.getRs("SELECT clave_poliza_detalle FROM fx_poliza_detalle WHERE clave_poliza=".concat(this.clave_poliza.toString()).concat(" AND cargo=0"));
                    if (rs.next()) {
                        polizaDetalle.setClave_poliza_detalle(rs.getInt("clave_poliza_detalle"));
                    }
                }

                if (c.getCampos().get("clave_poliza_detalle_destino") != null) {
                    if (c.getCampos().get("clave_poliza_detalle_destino").getValor() != null) {
                        polizaDetalle.setClave_poliza_detalle(Integer.parseInt(c.getCampos().get("clave_poliza_detalle_destino").getValor()));
                    }
                }

                if (this.clave_tipo_transaccion == 1  /* Depósito*/ ){
                    polizaDetalle.setCargo(true);

                    if (c.getCampos().get("clave_cuenta_origen").getValor() == null) {
                        if (c.getCampos().get("dividir_transaccion").getValor() == null) {
                            throw new Fallo("No se especificó la cuenta destino");
                        } else {
                        }
                    } else {
                        polizaDetalle.setClave_cuenta(Integer.parseInt(c.getCampos().get("clave_cuenta_origen").getValor()));
                    }

                } else if (this.clave_tipo_transaccion == 2
                        || this.clave_tipo_transaccion == 3
                        || this.clave_tipo_transaccion == 4
                        || this.clave_tipo_transaccion == 5
                        || this.clave_tipo_transaccion == 6
                        || this.clave_tipo_transaccion == 7
                        || this.clave_tipo_transaccion == 8
                        || this.clave_tipo_transaccion == 9
                        || this.clave_tipo_transaccion == 10 /* Transfencia de pagaré */
                        || this.clave_tipo_transaccion == 11
                        || this.clave_tipo_transaccion == 12) {
                    polizaDetalle.setCargo(true);

                    if (c.getCampos().get("clave_cuenta_destino").getValor() == null) {
                        throw new Fallo("No se especificó la cuenta destino");
                    } else {
                        polizaDetalle.setClave_cuenta(Integer.parseInt(c.getCampos().get("clave_cuenta_destino").getValor()));
                    }

                }
                
                if (c.getCampos().get("folio_pagare") != null) {
                    if (c.getCampos().get("folio_pagare").getValor() != null) {
                        polizaDetalle.setFolio_pagare(Integer.parseInt(c.getCampos().get("folio_pagare").getValor()));
                    }
                }
                
                polizaDetalle.setImporte(Float.parseFloat(c.getCampos().get("importe").getValor()));
                polizaDetalle.setClave_tipo_transaccion(this.clave_tipo_transaccion);
                if (c.getCampos().get("clave_moneda").getValor() == null) {
                    throw new Fallo("No se especificó la moneda");
                } else {
                    polizaDetalle.setClave_moneda(Integer.parseInt(c.getCampos().get("clave_moneda").getValor()));
                }

                if (c.getCampos().get("tipo_cambio").getValor() == null) {
                    throw new Fallo("No se especificó el tipo de cambio");
                } else {
                    polizaDetalle.setTipo_cambio(Float.parseFloat(c.getCampos().get("tipo_cambio").getValor()));
                }

                if (c.getCampos().get("clave_beneficiario").getValor() != null) {
                    polizaDetalle.setClave_beneficiario(Integer.parseInt(c.getCampos().get("clave_beneficiario").getValor()));
                }

                if (c.getCampos().get("clave_clase").getValor() != null && !c.getCampos().get("clave_clase").getValor().equals("")) {
                    polizaDetalle.setClave_clase(Integer.parseInt(c.getCampos().get("clave_clase").getValor()));
                }

                if (c.getCampos().get("folio_cheque").getValor() != null && !c.getCampos().get("folio_cheque").getValor().equals("")) {
                    polizaDetalle.setFolio_cheque(c.getCampos().get("folio_cheque").getValor());
                }

                this.polizaDetalles.add(polizaDetalle);
            } else {
                //Si dividir_transaccion=1 quiere decir que no viene con una sola transacción, 
                //sino que está divididas en varias las cuales tienen como clave_poliza = - (clave_empleado)

                try {
                    if (this.clave_poliza != null) {
                        rs = oDb.getRs("select clave_poliza_detalle from fx_poliza_detalle where clave_poliza_detalle<>".concat(c.getCampos().get("clave_poliza_detalle_origen").getValor()).concat(" AND (clave_poliza=-").concat(String.valueOf(this.clave_empleado)).concat(" OR clave_poliza=").concat(this.clave_poliza.toString()).concat(")"));
                    } else {
                        rs = oDb.getRs("select clave_poliza_detalle from fx_poliza_detalle where clave_poliza=-".concat(this.clave_empleado.toString()));
                    }

                    /*if (!rs.next()) {
                     throw new Fallo("No se encontraron subtransacciones");
                     }  */

                    while (rs.next()) {
                        polizaDetalle = new PolizaDetalle(rs.getInt("clave_poliza_detalle"), this.getUsuario());

                        if (this.clave_tipo_transaccion == 1)            /* Depósito */
                        {  polizaDetalle.setCargo(false);
                        } else {
                            polizaDetalle.setCargo(true);
                        }

                        if (polizaDetalle.getClave_tipo_transaccion() == null || polizaDetalle.getClave_tipo_transaccion() == 0) {
                            Cuenta cuenta = new Cuenta(polizaDetalle.getClave_cuenta(), this.getUsuario());

                            if (cuenta.getClave_tipo_cuenta() < 6) {
                                if (this.clave_tipo_transaccion == 2 && !polizaDetalle.cargo) {
                                    polizaDetalle.clave_tipo_transaccion = this.clave_tipo_transaccion;
                                } else {
                                    polizaDetalle.clave_tipo_transaccion = 3;
                                }
                            } else {
                                polizaDetalle.clave_tipo_transaccion = this.clave_tipo_transaccion;
                            }
                        }
                        
                        if (c.getCampos().get("folio_pagare") != null) {
                            if (c.getCampos().get("folio_pagare").getValor() != null) {
                                polizaDetalle.setFolio_pagare(Integer.parseInt(c.getCampos().get("folio_pagare").getValor()));
                            }
                        }
                        
                        polizaDetalle.setClave_poliza(this.clave_poliza);
                        polizaDetalle.setClave_moneda(Integer.parseInt(c.getCampos().get("clave_moneda").getValor()));
                        polizaDetalle.setTipo_cambio(Float.parseFloat(c.getCampos().get("tipo_cambio").getValor()));

                        this.polizaDetalles.add(polizaDetalle);
                    }

                    rs.close();
                    rs = null;
                    oDb.cierraConexion();
                    oDb = null;

                } catch (Exception e) {
                    rs = null;
                    oDb.cierraConexion();
                    oDb = null;
                    throw new Fallo("Error al recuperar la póliza");
                }
            }

        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public Integer getClave_empleado() {
        return clave_empleado;
    }

    public void setClave_empleado(Integer clave_empleado) {
        this.clave_empleado = clave_empleado;
    }

    public Integer getClave_estatus() {
        return clave_estatus;
    }

    public void setClave_estatus(Integer clave_estatus) {
        this.clave_estatus = clave_estatus;
    }

    public Integer getClave_poliza() {
        return clave_poliza;
    }

    public void setClave_poliza(Integer clave_poliza) {
        this.clave_poliza = clave_poliza;
    }

    public Integer getClave_tipo_transaccion() {
        return clave_tipo_transaccion;
    }

    public void setClave_tipo_transaccion(Integer clave_tipo_transaccion) {
        this.clave_tipo_transaccion = clave_tipo_transaccion;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public ArrayList<PolizaDetalle> getPolizaDetalles() {
        return polizaDetalles;
    }

    public void setPolizaDetalles(ArrayList<PolizaDetalle> polizaDetalles) {
        this.polizaDetalles = polizaDetalles;
    }

    public String insert() throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
        NumberFormat moneyFormatter = new DecimalFormat("$###,###,###,##0.00");
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer claveBitacora;
        Boolean startedTransaction = false;
        try {
            //Verifica que la fecha de la póliza no sea anterior a la del cierre
            rs = oDb.getRs("select max(fecha_cierre) as ultimocierre from pagarex_calendario_cierre where clave_estatus>1");
            if (rs.next()) {
                if (rs.getDate("ultimocierre")==null) 
                    resultadoXML.append("<warning><![CDATA[No es posible determinar la fecha del último cierre, verifique el calendario de pagos").append("]]></warning>");                    
                else {     
                    if (this.fecha.compareTo(rs.getDate("ultimocierre")) <=0) {
                        throw new Fallo("No es posible insertar una operación con fecha menor al último cierre, verifique ");
                    }
                }
            } else {
                resultadoXML.append("<warning><![CDATA[No es posible determinar la fecha del último cierre, verifique el calendario de pagos").append("]]></warning>");
            }

            startedTransaction = true;
            //1. Abre transaccion
            if (oDb.getDbType() == Conexion.DbType.MYSQL) {
                oDb.execute("START TRANSACTION");
            } else if (oDb.getDbType() == Conexion.DbType.MSSQL) {
                oDb.execute("BEGIN TRANSACTION");
                oDb.execute("SET DATEFORMAT YMD");
            }

            //2. Inserta póliza
            q.append("INSERT INTO fx_poliza (fecha, clave_empleado, clave_tipo_transaccion,clave_estatus,excluido,nota)").append("VALUES('").append(dateFormatter.format(this.fecha)).append("',").append(this.clave_empleado).append(",").append(this.clave_tipo_transaccion).append(",").append(this.clave_estatus).append(",").append(this.excluido ? "1" : "0").append(",").append(this.nota == null ? "null" : "'".concat(this.nota).concat("'")).append(")");

            resultadoXML.append("<insert_poliza><![CDATA[".concat(q.toString()).concat("]]></insert_poliza>"));
            rs = oDb.execute(q.toString());

            if (rs != null) {

                if (rs.next()) {
                    claveBitacora = rs.getInt(1);
                    this.clave_poliza = claveBitacora;
                    resultadoXML.append("<pk>").append(claveBitacora).append("</pk>");

                    //Inserta en bitácora
                    if (super.getUsuario().getCx().getDbType() == Conexion.DbType.MYSQL) {
                        oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,clave_forma,clave_registro) "
                                + "VALUES(" + super.getUsuario().getClave() + ",CURDATE(),2,'" + super.getUsuario().getIp() + "','" + super.getUsuario().getNavegador() + "','" + super.getClaveForma() + "','" + claveBitacora + "')");
                    } else if (super.getUsuario().getCx().getDbType() == Conexion.DbType.MSSQL) {
                        oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,clave_forma,clave_registro) "
                                + "VALUES(" + super.getUsuario().getClave() + ",GETDATE(),2,'" + super.getUsuario().getIp() + "','" + super.getUsuario().getNavegador() + "','" + super.getClaveForma() + "','" + claveBitacora + "')");
                    }

                } else {
                    throw new Fallo("Error al recuperar clave de bitacora");
                }

                //3. Inserta pólizas detalle
                for (PolizaDetalle pd : this.polizaDetalles) {
                    pd.setClave_poliza(this.clave_poliza);
                    //4. Verifica los niveles de alerta, límites y presupestos de las cuentas origen y destino
                    Cuenta c = new Cuenta(pd.getClave_cuenta(), this.getUsuario());

                    /* Si se trata de una cuenta bancaria */
                    if (c.getClave_tipo_cuenta() <= 5) {
                        Float saldoActual = c.getSaldoActual("");
                        if (c.getLimite() != 0) { //Si está establecido un límite para la cuenta
                            if (saldoActual + pd.getImporte() >= c.getLimite()) {
                                resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.getImporte())).concat(" y está por rebasar o ha rebasado el limite establecido de ").concat(moneyFormatter.format(c.getLimite())).concat("]]></warning>"));
                            }
                        }

                        if (c.getNivel_de_alerta() != 0 && this.getClave_tipo_transaccion() != 1) { //Si está establecido un nivel de alerta
                            if (saldoActual + pd.getImporte() <= c.getNivel_de_alerta()) {
                                resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.getImporte())).concat(" y está por rebasar o ha rebasado el nivel de alerta establecido de ").concat(moneyFormatter.format(c.getNivel_de_alerta())).concat("]]></warning>"));
                            }
                        }
                    } else {
                        Float saldoActual = c.getSaldoActual("");
                        Float presupuestoActual = c.getPresupuestoActual((this.fecha.getMonth() + 1));
                        if (presupuestoActual != 0) {
                            if (saldoActual + pd.importe > presupuestoActual) {
                                resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.importe)).concat(" y está por rebasar o ha rebasado el presupuesto establecido de ").concat(moneyFormatter.format(c.getLimite())).concat("]]></warning>"));
                            }
                        }
                    }

                    if (pd.getClave_poliza_detalle() == null) {
                        q = new StringBuilder("INSERT INTO fx_poliza_detalle (clave_poliza,cargo, clave_tipo_transaccion,folio_pagare, importe, clave_moneda, tipo_cambio, clave_beneficiario, clave_cuenta, clave_clase, folio_cheque)").append("VALUES (").append(pd.getClave_poliza()).append(",").append(pd.getCargo() ? "1" : "0").append(",").append(pd.getClave_tipo_transaccion()).append(",").append(pd.getFolio_pagare()).append(",").append(pd.getImporte()).append(",").append(pd.getClave_moneda().toString()).append(",").append(pd.getTipo_cambio().toString()).append(",").append(pd.getClave_beneficiario() == null ? "null" : pd.getClave_beneficiario().toString()).append(",").append(pd.getClave_cuenta() == null ? "null" : pd.getClave_cuenta().toString()).append(",").append(pd.getClave_clase() == null ? "null" : pd.getClave_clase().toString()).append(",").append(pd.getFolio_cheque()).append(")");
                        resultadoXML.append("<insert_poliza_detalle><![CDATA[".concat(q.toString()).concat("]]></insert_poliza_detalle>"));
                    } else {
                        q = new StringBuilder("UPDATE fx_poliza_detalle SET clave_poliza=".concat(this.clave_poliza.toString()).concat(",cargo=").concat(pd.getCargo().toString()).concat(",clave_tipo_transaccion=").concat(pd.getClave_tipo_transaccion().toString()).concat(",folio_pagare=").concat(pd.getFolio_pagare().toString()).concat(",clave_moneda=").concat(pd.getClave_moneda().toString()).concat(",tipo_cambio=").concat(pd.getTipo_cambio().toString()).concat(" WHERE clave_poliza_detalle=").concat(pd.getClave_poliza_detalle().toString()));
                        resultadoXML.append("<update_poliza_detalle><![CDATA[".concat(q.toString()).concat("]]></update_poliza_detalle>"));
                    }
                    oDb.execute(q.toString());
                }
            }


            oDb.execute("COMMIT");
            oDb.cierraConexion();
            oDb = null;
        } catch (Exception e) {
            if (startedTransaction) {
                oDb.execute("ROLLBACK");
            }

            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(e.getMessage());
        }
        return resultadoXML.toString();
    }

    @Override
    public String insert(Conexion oDb) throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
        NumberFormat moneyFormatter = new DecimalFormat("$###,###,###,##0.00");

        Integer claveBitacora;

        try {

            rs = oDb.getRs("select max(fecha_cierre) as ultimocierre from pagarex_calendario_cierre where clave_estatus>1");
            if (rs.next()) {
                if (rs.getDate("ultimocierre")==null) 
                    resultadoXML.append("<warning><![CDATA[No es posible determinar la fecha del último cierre, verifique el calendario de pagos").append("]]></warning>");                    
                else {     
                    if (this.fecha.compareTo(rs.getDate("ultimocierre")) <=0) {
                        throw new Fallo("No es posible insertar una operación con fecha menor al último cierre, verifique ");
                    }
                }
            } else {
                resultadoXML.append("<warning><![CDATA[No es posible determinar la fecha del último cierre, verifique el calendario de pagos").append("]]></warning>");
            }

            //2. Inserta póliza
            q.append("INSERT INTO fx_poliza (fecha, clave_empleado, clave_tipo_transaccion,clave_estatus,excluido,nota)")
                    .append("VALUES('").append(dateFormatter.format(this.fecha)).append("',")
                    .append(this.clave_empleado).append(",")
                    .append(this.clave_tipo_transaccion).append(",").append(this.clave_estatus).append(",").append(this.excluido ? "1" : "0").append(",").append(this.nota == null ? "null" : "'".concat(this.nota).concat("'")).append(")");

            resultadoXML.append("<insert_poliza><![CDATA[".concat(q.toString()).concat("]]></insert_poliza>"));
            rs = oDb.execute(q.toString());

            if (rs != null) {

                if (rs.next()) {
                    claveBitacora = rs.getInt(1);
                    this.clave_poliza = claveBitacora;
                    super.setPk(claveBitacora.toString());
                    resultadoXML.append("<pk>").append(claveBitacora).append("</pk>");

                    //Inserta en bitácora
                    if (super.getUsuario().getCx().getDbType() == Conexion.DbType.MYSQL) {
                        oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,clave_forma,clave_registro) "
                                + "VALUES(" + super.getUsuario().getClave() + ",CURDATE(),2,'" + super.getUsuario().getIp() + "','" + super.getUsuario().getNavegador() + "','" + super.getClaveForma() + "','" + claveBitacora + "')");
                    } else if (super.getUsuario().getCx().getDbType() == Conexion.DbType.MSSQL) {
                        oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,clave_forma,clave_registro) "
                                + "VALUES(" + super.getUsuario().getClave() + ",GETDATE(),2,'" + super.getUsuario().getIp() + "','" + super.getUsuario().getNavegador() + "','" + super.getClaveForma() + "','" + claveBitacora + "')");
                    }

                } else {
                    throw new Fallo("Error al recuperar clave de bitacora");
                }

                //3. Inserta pólizas detalle
                for (PolizaDetalle pd : this.polizaDetalles) {
                    pd.setClave_poliza(this.clave_poliza);
                    //4. Verifica los niveles de alerta, límites y presupestos de las cuentas origen y destino
                    Cuenta c = new Cuenta(pd.getClave_cuenta(), this.getUsuario());

                    /* Si se trata de una cuenta bancaria */
                    if (c.getClave_tipo_cuenta() <= 5) {
                        Float saldoActual = c.getSaldoActual("");
                        if (c.getLimite() != 0) { //Si está establecido un límite para la cuenta
                            if (saldoActual + pd.getImporte() >= c.getLimite()) {
                                resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.getImporte())).concat(" y está por rebasar o ha rebasado el limite establecido de ").concat(moneyFormatter.format(c.getLimite())).concat("]]></warning>"));
                            }
                        }

                        if (c.getNivel_de_alerta() != 0 && this.getClave_tipo_transaccion() != 1) { //Si está establecido un nivel de alerta
                            if (saldoActual + pd.getImporte() <= c.getNivel_de_alerta()) {
                                resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.getImporte())).concat(" y está por rebasar o ha rebasado el nivel de alerta establecido de ").concat(moneyFormatter.format(c.getNivel_de_alerta())).concat("]]></warning>"));
                            }
                        }
                    } else {
                        Float saldoActual = c.getSaldoActual("");
                        Float presupuestoActual = c.getPresupuestoActual((this.fecha.getMonth() + 1));
                        if (presupuestoActual != 0) {
                            if (saldoActual + pd.importe > presupuestoActual) {
                                resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.importe)).concat(" y está por rebasar o ha rebasado el presupuesto establecido de ").concat(moneyFormatter.format(c.getLimite())).concat("]]></warning>"));
                            }
                        }
                    }

                    if (pd.getClave_poliza_detalle() == null) {
                        q = new StringBuilder("INSERT INTO fx_poliza_detalle (clave_poliza,cargo, clave_tipo_transaccion, folio_pagare, importe, clave_moneda, tipo_cambio, clave_beneficiario, clave_cuenta, clave_clase, folio_cheque)").append("VALUES (").append(pd.getClave_poliza()).append(",").append(pd.getCargo() ? "1" : "0").append(",").append(pd.getClave_tipo_transaccion()).append(",").append(pd.getFolio_pagare()).append(",").append(pd.getImporte()).append(",").append(pd.getClave_moneda().toString()).append(",").append(pd.getTipo_cambio().toString()).append(",").append(pd.getClave_beneficiario() == null ? "null" : pd.getClave_beneficiario().toString()).append(",").append(pd.getClave_cuenta() == null ? "null" : pd.getClave_cuenta().toString()).append(",").append(pd.getClave_clase() == null ? "null" : pd.getClave_clase().toString()).append(",").append(pd.getFolio_cheque()).append(")");
                        resultadoXML.append("<insert_poliza_detalle><![CDATA[".concat(q.toString()).concat("]]></insert_poliza_detalle>"));
                    } else {
                        q = new StringBuilder("UPDATE fx_poliza_detalle SET clave_poliza=".concat(this.clave_poliza.toString()).concat(",cargo=").concat(pd.getCargo().toString()).concat(",clave_tipo_transaccion=").concat(pd.getClave_tipo_transaccion().toString()).concat(",folio_pagare=").concat(pd.getFolio_pagare().toString()).concat(",clave_moneda=").concat(pd.getClave_moneda().toString()).concat(",tipo_cambio=").concat(pd.getTipo_cambio().toString()).concat(" WHERE clave_poliza_detalle=").concat(pd.getClave_poliza_detalle().toString()));
                        resultadoXML.append("<update_poliza_detalle><![CDATA[".concat(q.toString()).concat("]]></update_poliza_detalle>"));
                    }
                    oDb.execute(q.toString());
                }
            }

        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
        return resultadoXML.toString();
    }

    public String update() throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
        NumberFormat moneyFormatter = new DecimalFormat("$###,###,###,##0.00");
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer claveBitacora;

        try {
            //Verifica las fechas del cierre
            rs = oDb.getRs("select max(fecha_cierre) as ultimocierre from pagarex_calendario_cierre where clave_estatus>1");
            if (rs.next()) {
                Date fechaOriginal = new Date(Integer.parseInt(super.getCampos().get("fecha").getValorOriginal().split("-")[0]), Integer.parseInt(super.getCampos().get("fecha").getValorOriginal().split("-")[1])-1, Integer.parseInt(super.getCampos().get("fecha").getValorOriginal().split("-")[2]));
                if (rs.getDate("ultimocierre")==null) {
                    resultadoXML.append("<warning><![CDATA[No es posible determinar la fecha del último cierre, verifique el calendario de pagos").append("]]></warning>");                    
                } else {
                    if (fechaOriginal.compareTo(rs.getDate("ultimocierre"))<0) {
                        throw new Fallo("No es posible actualizar una operación con una fecha menor o igual al último cierre, verifique ");
                    }                    
                }
            } else {
                resultadoXML.append("<warning><![CDATA[No es posible determinar la fecha del último cierre, verifique el calendario de pagos").append("]]></warning>");
            }            
            
            //1. Abre transaccion
            if (oDb.getDbType() == Conexion.DbType.MYSQL) {
                oDb.execute("START TRANSACTION");
            } else if (oDb.getDbType() == Conexion.DbType.MSSQL) {
                oDb.execute("BEGIN TRANSACTION");
                oDb.execute("SET DATEFORMAT YMD");
            }

            //2. Actualiza póliza
            q.append("UPDATE fx_poliza SET ").append("fecha='").append(dateFormatter.format(this.fecha)).append("',clave_empleado=").append(this.clave_empleado).append(",clave_tipo_transaccion=").append(this.clave_tipo_transaccion).append(",clave_estatus=").append(this.clave_estatus).append(",excluido=").append(this.excluido ? "1" : "0").append(",nota=").append(this.nota == null ? "null" : "'".concat(this.nota).concat("'")).append(" WHERE clave_poliza=").append(this.clave_poliza);

            resultadoXML.append("<update_poliza><![CDATA[".concat(q.toString()).concat("]]></update_poliza>"));
            rs = oDb.execute(q.toString());

            String t = "";

            //Inserta en bitácora
            if (super.getUsuario().getCx().getDbType() == Conexion.DbType.MYSQL) {
                t = "INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,clave_forma,clave_registro) "
                        + "VALUES(" + super.getUsuario().getClave() + ",CURDATE(),3,'" + super.getUsuario().getIp() + "','" + super.getUsuario().getNavegador() + "','" + super.getClaveForma() + "','" + this.clave_poliza + "')";

            } else if (super.getUsuario().getCx().getDbType() == Conexion.DbType.MYSQL) {
                t = "INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,clave_forma,clave_registro) "
                        + "VALUES(" + super.getUsuario().getClave() + ",GETDATE(),3,'" + super.getUsuario().getIp() + "','" + super.getUsuario().getNavegador() + "','" + super.getClaveForma() + "','" + this.clave_poliza + "')";
            }

            rs = oDb.execute(t);
            if (rs.next()) {
                claveBitacora = rs.getInt(1);
                resultadoXML.append("<bitacora>").append(claveBitacora).append("</bitacora>");

                //Inserta detalles de la bitácora
                for (Campo campo : super.getCampos().values()) {
                    if (campo.isAutoIncrement() || campo.getValor() == null) {
                        continue;
                    }

                    boolean sonRealmenteValoresDiferentes = false;

                    if (campo.getValorOriginal() == null && (campo.getValor() != null && !campo.getValor().equals(""))) {
                        sonRealmenteValoresDiferentes = true;
                    } else if (campo.getValorOriginal() != null && campo.getValor() == null) {
                        sonRealmenteValoresDiferentes = true;
                    } else if ((campo.getValor().equals("") && campo.getValorOriginal() != null)) {   
                        sonRealmenteValoresDiferentes = true;
                    } else if (!(campo.getValor().equals("") && campo.getValorOriginal() == null)) {
                         sonRealmenteValoresDiferentes = true;
                    } else if ((campo.getValor().equals("") && campo.getValorOriginal() == null)) {
                         sonRealmenteValoresDiferentes = false;
                    } else {
                        // Se hace la comparación de acuerdo al tipo del campo
                        if ((campo.getTipoDato().toLowerCase().equals("text") || campo.getTipoDato().toLowerCase().equals("varchar")) && !campo.getValor().equals(campo.getValorOriginal())) {
                            sonRealmenteValoresDiferentes = true;
                        } else if (campo.getTipoDato().toLowerCase().equals("int")) {
                            if (Integer.parseInt(campo.getValor().toString()) != Integer.parseInt(campo.getValorOriginal().toString())) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        } else if (campo.getTipoDato().toLowerCase().equals("bit") || campo.getTipoDato().toLowerCase().equals("tinyint")) {
                            if (Boolean.parseBoolean(campo.getValor()) != Boolean.parseBoolean(campo.getValorOriginal())) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        } else if (campo.getTipoDato().toLowerCase().equals("datetime")) {
                            String hora = campo.getValor().split(" ")[1];
                            String fecha = campo.getValor().split(" ")[0];
                            if (!new java.util.Date(fecha.split("/")[2].concat("/").concat(fecha.split("/")[1]).concat("/").concat(fecha.split("/")[0])).equals(new java.util.Date(campo.getValorOriginal().substring(0, 10).replaceAll("-", "/")))) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        } else if (campo.getTipoDato().toLowerCase().equals("smalldatetime")) {
                            if (!new java.util.Date(campo.getValor().split("/")[2].concat("/").concat(campo.getValor().split("/")[1]).concat("/").concat(campo.getValor().split("/")[0])).equals(new java.util.Date(campo.getValorOriginal().substring(0, 10).replaceAll("-", "/")))) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        } else if ((campo.getTipoDato().toLowerCase().equals("money") || campo.getTipoDato().toLowerCase().equals("float"))) {
                            if (Float.parseFloat(campo.getValor()) != Float.parseFloat(campo.getValorOriginal())) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        }
                    }

                    if (sonRealmenteValoresDiferentes) {
                        q = new StringBuilder("INSERT INTO be_bitacora_detalle (clave_bitacora, campo, valor_anterior, valor_actual) VALUES(").append(claveBitacora).append(",'").append(campo.getNombre()).append("',");
                        if (campo.getValorOriginal() == null) {
                            q.append("null,");
                        } else {
                            q.append("'").append(campo.getValorOriginal().toString().replaceAll("'", "''")).append("',");
                        }

                        if (campo.getValor() == null) {
                            q.append("null)");
                        } else {
                            q.append("'").append(campo.getValor().toString().replaceAll("'", "''")).append("')");
                        }

                        oDb.execute(q.toString());
                    }
                }
            }


            //3. Actualiza pólizas detalle
            for (PolizaDetalle pd : this.polizaDetalles) {
                pd.setClave_poliza(this.clave_poliza);
                //4. Verifica los niveles de alerta, límites y presupestos de las cuentas origen y destino
                Cuenta c = new Cuenta(pd.getClave_cuenta(), this.getUsuario());

                /* Si se trata de una cuenta bancaria */
                if (c.getClave_tipo_cuenta() <= 5) {
                    Float saldoActual = c.getSaldoActual("");
                    if (c.getLimite() != 0) { //Si está establecido un límite para la cuenta
                        if (saldoActual + pd.getImporte() >= c.getLimite()) {
                            resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.getImporte())).concat(" y está por rebasar o ha rebasado el limite establecido de ").concat(moneyFormatter.format(c.getLimite())).concat("]]></warning>"));
                        }
                    }

                    if (c.getNivel_de_alerta() != 0) { //Si está establecido un nivel de alerta
                        if (saldoActual + pd.getImporte() <= c.getNivel_de_alerta()) {
                            resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.getImporte())).concat(" y está por rebasar o ha rebasado el nivel de alerta establecido de ").concat(moneyFormatter.format(c.getNivel_de_alerta())).concat("]]></warning>"));
                        }
                    }
                } else {
                    Float saldoActual = c.getSaldoActual("");
                    Float presupuestoActual = c.getPresupuestoActual((this.fecha.getMonth() + 1));
                    if (presupuestoActual != 0) {
                        if (saldoActual + pd.importe > presupuestoActual) {
                            resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.importe)).concat(" y está por rebasar o ha rebasado el presupuesto establecido de ").concat(moneyFormatter.format(presupuestoActual)).concat("]]></warning>"));
                        }
                    }
                }

                //q = new StringBuilder("UPDATE fx_poliza_detalle SET ").append("cargo=").append(pd.getCargo() ? "1" : "0").append(",importe=").append(pd.getImporte()).append(",clave_moneda=").append(pd.getClave_moneda().toString()).append(",tipo_cambio=").append(pd.getTipo_cambio().toString()).append(",clave_beneficiario=").append(pd.getClave_beneficiario() == null ? "null" : pd.getClave_beneficiario().toString()).append(",clave_cuenta=").append(pd.getClave_cuenta() == null ? "null" : pd.getClave_cuenta().toString()).append(",clave_clase=").append(pd.getClave_clase() == null ? "null" : pd.getClave_clase().toString()).append(",folio_cheque=").append(pd.getFolio_cheque() == null ? "null" : "'".concat(pd.getFolio_cheque()).concat("'")).append(" WHERE clave_poliza_detalle=").append(pd.getClave_poliza_detalle().toString());
                if (pd.getClave_poliza_detalle() == null) {
                    q = new StringBuilder("INSERT INTO fx_poliza_detalle (clave_poliza,cargo, clave_tipo_transaccion, folio_pagare, ")
                            .append("importe, clave_moneda, tipo_cambio, clave_beneficiario, clave_cuenta, clave_clase,")
                            .append("folio_cheque) VALUES (").append(pd.getClave_poliza()).append(",")
                            .append(pd.getCargo() ? "1" : "0").append(pd.getClave_tipo_transaccion()).append(",")
                            .append(pd.getClave_tipo_transaccion()).append(",")
                            .append(pd.getFolio_pagare()).append(",")
                            .append(pd.getImporte()).append(",").append(pd.getClave_moneda().toString()).append(",")
                            .append(pd.getTipo_cambio().toString()).append(",")
                            .append(pd.getClave_beneficiario() == null ? "null" : pd.getClave_beneficiario().toString()).append(",")
                            .append(pd.getClave_cuenta() == null ? "null" : pd.getClave_cuenta().toString()).append(",")
                            .append(pd.getClave_clase() == null ? "null" : pd.getClave_clase().toString()).append(",")
                            .append(pd.getFolio_cheque() == null ? "null" : pd.getFolio_cheque()).append(")");
                    resultadoXML.append("<insert_poliza_detalle><![CDATA[".concat(q.toString()).concat("]]></insert_poliza_detalle>"));
                } else {
                    q = new StringBuilder("UPDATE fx_poliza_detalle SET clave_poliza=").append(this.clave_poliza.toString())
                            .append(",cargo=").append(pd.getCargo().toString())
                            .append(",clave_tipo_transaccion=").append(pd.getClave_tipo_transaccion().toString())
                            .append(",importe=").append(pd.getImporte())
                            .append(",clave_moneda=").append(pd.getClave_moneda().toString())
                            .append(",tipo_cambio=").append(pd.getTipo_cambio().toString())
                            .append(",clave_beneficiario=").append(pd.getClave_beneficiario() == null ? "null" : pd.getClave_beneficiario())
                            .append(",clave_cuenta=").append(pd.getClave_cuenta() == null ? "null" : pd.getClave_cuenta().toString())
                            .append(",clave_clase=").append(pd.getClave_clase() == null ? "null" : pd.getClave_clase().toString())
                            .append(",folio_cheque=").append(pd.getFolio_cheque() == null ? "null" : "'".concat(pd.getFolio_cheque().replace("'", "''")).concat("'"))
                            .append(" WHERE clave_poliza_detalle=").append(pd.getClave_poliza_detalle().toString());
                    resultadoXML.append("<update_poliza_detalle><![CDATA[".concat(q.toString()).concat("]]></update_poliza_detalle>"));
                }

                oDb.execute(q.toString());
            }

            oDb.execute("COMMIT");
        } catch (Exception e) {
            oDb.execute("ROLLBACK");
            throw new Fallo(e.getMessage());
        }
        return resultadoXML.toString();
    }

    @Override
    public String update(Conexion oDb) throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
        NumberFormat moneyFormatter = new DecimalFormat("$###,###,###,##0.00");
        Integer claveBitacora;
        Date fechaOriginal = null;
        
        try {
            //Verifica las fechas del cierre
            rs = oDb.getRs("select max(fecha_cierre) as ultimocierre from pagarex_calendario_cierre where clave_estatus>1");
            if (rs.next()) {
                if (super.getCampos().get("fecha").getValorOriginal()!=null) {
                    fechaOriginal = new Date(Integer.parseInt(super.getCampos().get("fecha").getValorOriginal().split("-")[0]), Integer.parseInt(super.getCampos().get("fecha").getValorOriginal().split("-")[1])-1, Integer.parseInt(super.getCampos().get("fecha").getValorOriginal().split("-")[2]));
                } else {
                    fechaOriginal = this.fecha;
                }   
                
                if (rs.getDate("ultimocierre")==null) {
                    resultadoXML.append("<warning><![CDATA[No es posible determinar la fecha del último cierre, verifique el calendario de pagos").append("]]></warning>");                    
                } else {
                    if (fechaOriginal.compareTo(rs.getDate("ultimocierre"))<0) {
                        throw new Fallo("No es posible actualizar una operación con una fecha menor o igual al último cierre, verifique ");
                    }                    
                }
            } else {
                resultadoXML.append("<warning><![CDATA[No es posible determinar la fecha del último cierre, verifique el calendario de pagos").append("]]></warning>");
            }  
            
            //2. Actualiza póliza
            q.append("UPDATE fx_poliza SET ").append("fecha='").append(dateFormatter.format(this.fecha)).append("',clave_empleado=").append(this.clave_empleado).append(",clave_tipo_transaccion=").append(this.clave_tipo_transaccion).append(",clave_estatus=").append(this.clave_estatus).append(",excluido=").append(this.excluido ? "1" : "0").append(",nota=").append(this.nota == null ? "null" : "'".concat(this.nota).concat("'")).append(" WHERE clave_poliza=").append(this.clave_poliza);

            resultadoXML.append("<update_poliza><![CDATA[".concat(q.toString()).concat("]]></update_poliza>"));
            rs = oDb.execute(q.toString());

            String t = "";

            //Inserta en bitácora
            if (super.getUsuario().getCx().getDbType() == Conexion.DbType.MYSQL) {
                t = "INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,clave_forma,clave_registro) "
                        + "VALUES(" + super.getUsuario().getClave() + ",CURDATE(),3,'" + super.getUsuario().getIp() + "','" + super.getUsuario().getNavegador() + "','" + super.getClaveForma() + "','" + this.clave_poliza + "')";

            } else if (super.getUsuario().getCx().getDbType() == Conexion.DbType.MYSQL) {
                t = "INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,clave_forma,clave_registro) "
                        + "VALUES(" + super.getUsuario().getClave() + ",GETDATE(),3,'" + super.getUsuario().getIp() + "','" + super.getUsuario().getNavegador() + "','" + super.getClaveForma() + "','" + this.clave_poliza + "')";
            }

            rs = oDb.execute(t);
            if (rs.next()) {
                claveBitacora = rs.getInt(1);
                resultadoXML.append("<bitacora>").append(claveBitacora).append("</bitacora>");

                //Inserta detalles de la bitácora
                for (Campo campo : super.getCampos().values()) {
                    if (campo.isAutoIncrement() || campo.getValor() == null) {
                        continue;
                    }

                    boolean sonRealmenteValoresDiferentes = false;

                    if (campo.getValorOriginal() == null && (campo.getValor() != null && !campo.getValor().equals(""))) {
                        sonRealmenteValoresDiferentes = true;
                    } else if (campo.getValorOriginal() != null && campo.getValor() == null) {
                        sonRealmenteValoresDiferentes = true;
                    } else if (!(campo.getValor().equals("") && campo.getValorOriginal() == null)) {
                        // Se hace la comparación de acuerdo al tipo del campo
                        if ((campo.getTipoDato().toLowerCase().equals("text") || campo.getTipoDato().toLowerCase().equals("varchar")) && !campo.getValor().equals(campo.getValorOriginal())) {
                            sonRealmenteValoresDiferentes = true;
                        } else if (campo.getTipoDato().toLowerCase().equals("int")) {
                            if (Integer.parseInt(campo.getValor().toString()) != Integer.parseInt(campo.getValorOriginal().toString())) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        } else if (campo.getTipoDato().toLowerCase().equals("bit") || campo.getTipoDato().toLowerCase().equals("tinyint")) {
                            if (Boolean.parseBoolean(campo.getValor()) != Boolean.parseBoolean(campo.getValorOriginal())) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        } else if (campo.getTipoDato().toLowerCase().equals("datetime")) {
                            String hora = campo.getValor().split(" ")[1];
                            String fecha = campo.getValor().split(" ")[0];
                            if (!new java.util.Date(fecha.split("/")[2].concat("/").concat(fecha.split("/")[1]).concat("/").concat(fecha.split("/")[0])).equals(new java.util.Date(campo.getValorOriginal().substring(0, 10).replaceAll("-", "/")))) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        } else if (campo.getTipoDato().toLowerCase().equals("smalldatetime")) {
                            if (!new java.util.Date(campo.getValor().split("/")[2].concat("/").concat(campo.getValor().split("/")[1]).concat("/").concat(campo.getValor().split("/")[0])).equals(new java.util.Date(campo.getValorOriginal().substring(0, 10).replaceAll("-", "/")))) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        } else if ((campo.getTipoDato().toLowerCase().equals("money") || campo.getTipoDato().toLowerCase().equals("float"))) {
                            if (Float.parseFloat(campo.getValor()) != Float.parseFloat(campo.getValorOriginal())) {
                                sonRealmenteValoresDiferentes = true;
                            }
                        }
                    }

                    if (sonRealmenteValoresDiferentes) {
                        q = new StringBuilder("INSERT INTO be_bitacora_detalle (clave_bitacora, campo, valor_anterior, valor_actual) VALUES(").append(claveBitacora).append(",'").append(campo.getNombre()).append("',");
                        if (campo.getValorOriginal() == null) {
                            q.append("null,");
                        } else {
                            q.append("'").append(campo.getValorOriginal().toString().replaceAll("'", "''")).append("',");
                        }

                        if (campo.getValor() == null) {
                            q.append("null)");
                        } else {
                            q.append("'").append(campo.getValor().toString().replaceAll("'", "''")).append("')");
                        }

                        oDb.execute(q.toString());
                    }
                }
            }


            //3. Actualiza pólizas detalle
            for (PolizaDetalle pd : this.polizaDetalles) {
                pd.setClave_poliza(this.clave_poliza);
                //4. Verifica los niveles de alerta, límites y presupestos de las cuentas origen y destino
                Cuenta c = new Cuenta(pd.getClave_cuenta(), this.getUsuario());

                /* Si se trata de una cuenta bancaria */
                if (c.getClave_tipo_cuenta() <= 5) {
                    Float saldoActual = c.getSaldoActual("");
                    if (c.getLimite() != 0) { //Si está establecido un límite para la cuenta
                        if (saldoActual + pd.getImporte() >= c.getLimite()) {
                            resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.getImporte())).concat(" y está por rebasar o ha rebasado el limite establecido de ").concat(moneyFormatter.format(c.getLimite())).concat("]]></warning>"));
                        }
                    }

                    if (c.getNivel_de_alerta() != 0) { //Si está establecido un nivel de alerta
                        if (saldoActual + pd.getImporte() <= c.getNivel_de_alerta()) {
                            resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.getImporte())).concat(" y está por rebasar o ha rebasado el nivel de alerta establecido de ").concat(moneyFormatter.format(c.getNivel_de_alerta())).concat("]]></warning>"));
                        }
                    }
                } else {
                    Float saldoActual = c.getSaldoActual("");
                    Float presupuestoActual = c.getPresupuestoActual((this.fecha.getMonth() + 1));
                    if (presupuestoActual != 0) {
                        if (saldoActual + pd.importe > presupuestoActual) {
                            resultadoXML.append("<warning><![CDATA[El saldo de la cuenta ".concat(c.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + pd.importe)).concat(" y está por rebasar o ha rebasado el presupuesto establecido de ").concat(moneyFormatter.format(presupuestoActual)).concat("]]></warning>"));
                        }
                    }
                }

                //q = new StringBuilder("UPDATE fx_poliza_detalle SET ").append("cargo=").append(pd.getCargo() ? "1" : "0").append(",importe=").append(pd.getImporte()).append(",clave_moneda=").append(pd.getClave_moneda().toString()).append(",tipo_cambio=").append(pd.getTipo_cambio().toString()).append(",clave_beneficiario=").append(pd.getClave_beneficiario() == null ? "null" : pd.getClave_beneficiario().toString()).append(",clave_cuenta=").append(pd.getClave_cuenta() == null ? "null" : pd.getClave_cuenta().toString()).append(",clave_clase=").append(pd.getClave_clase() == null ? "null" : pd.getClave_clase().toString()).append(",folio_cheque=").append(pd.getFolio_cheque() == null ? "null" : "'".concat(pd.getFolio_cheque()).concat("'")).append(" WHERE clave_poliza_detalle=").append(pd.getClave_poliza_detalle().toString());
                if (pd.getClave_poliza_detalle() == null) {
                    q = new StringBuilder("INSERT INTO fx_poliza_detalle (clave_poliza,cargo, clave_tipo_transaccion, folio_pagare, importe, clave_moneda, tipo_cambio, clave_beneficiario, clave_cuenta, clave_clase, folio_cheque)").append("VALUES (").append(pd.getClave_poliza()).append(",").append(pd.getCargo() ? "1" : "0").append(pd.getClave_tipo_transaccion()).append(",").append(pd.getFolio_pagare()).append(",").append(pd.getImporte()).append(",").append(pd.getClave_moneda().toString()).append(",").append(pd.getTipo_cambio().toString()).append(",").append(pd.getClave_beneficiario() == null ? "null" : pd.getClave_beneficiario().toString()).append(",").append(pd.getClave_cuenta() == null ? "null" : pd.getClave_cuenta().toString()).append(",").append(pd.getClave_clase() == null ? "null" : pd.getClave_clase().toString()).append(",").append(pd.getFolio_cheque()).append(")");
                    resultadoXML.append("<insert_poliza_detalle><![CDATA[".concat(q.toString()).concat("]]></insert_poliza_detalle>"));
                } else {
                    q = new StringBuilder("UPDATE fx_poliza_detalle SET clave_poliza=").append(this.clave_poliza.toString())
                            .append(",cargo=").append(pd.getCargo().toString())
                            .append(",clave_tipo_transaccion=").append(pd.getClave_tipo_transaccion().toString())
                            .append(",folio_pagare=").append(pd.getFolio_pagare().toString())
                            .append(",importe=").append(pd.getImporte().toString())
                            .append(",clave_moneda=").append(pd.getClave_moneda().toString())
                            .append(",tipo_cambio=").append(pd.getTipo_cambio().toString())
                            .append(",clave_beneficiario=").append(pd.getClave_beneficiario().toString())
                            .append(",clave_cuenta=").append(pd.getClave_cuenta().toString())
                            .append(",clave_clase=").append((pd.getClave_clase()==null)?"null":pd.getClave_clase().toString())
                            .append(",folio_cheque='").append((pd.folio_cheque==null)?"null":pd.folio_cheque.replace("'", "''")).append("' WHERE clave_poliza_detalle=").append(pd.getClave_poliza_detalle().toString());
                    resultadoXML.append("<update_poliza_detalle><![CDATA[".concat(q.toString()).concat("]]></update_poliza_detalle>"));
                }

                oDb.execute(q.toString());
            }

        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
        return resultadoXML.toString();
    }

    public String delete(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        resultado.append(super.delete(true,super.getUsuario()));

        if (resultado.toString().split("<error>").length > 1) {
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }

        oDb.execute("DELETE FROM fx_poliza_detalle WHERE clave_poliza=".concat(String.valueOf(this.clave_poliza)));
        return resultado.toString();
    }

    public String duplicate(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        StringBuilder resultadoXML = new StringBuilder();

        //Si se trata de subtransacciones    
        if (forma == 251) {
            Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
            String s = "insert into fx_transaccion".concat(" (fecha, clave_tipo_transaccion, importe, clave_moneda, tipo_cambio,clave_beneficiario, clave_cuenta, clave_categoria, clave_transaccion_padre, clave_clase, folio_cheque, clave_estatus, excluido, nota, tiene_subtransacciones)").concat(" select fecha, clave_tipo_transaccion, importe, clave_moneda, tipo_cambio,clave_beneficiario, clave_cuenta, clave_categoria, ").concat(String.valueOf(claveEmpleado * -1)).concat(",clave_clase, folio_cheque, clave_estatus, excluido, nota, tiene_subtransacciones from fx_transaccion where clave_transaccion_padre=").concat(this.getPk().toString());

            resultadoXML.append("<duplicate><![CDATA[").append(s).append("]></duplicate>");
            oDb.execute(s);

        } else {
            resultadoXML.append(super.duplicate());
        }

        return resultadoXML.toString();
    }
}
