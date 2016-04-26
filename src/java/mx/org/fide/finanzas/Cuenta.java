package mx.org.fide.finanzas;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class Cuenta extends Consulta {

    private Integer clave_cuenta;
    private String cuenta;
    private Integer clave_cuenta_padre;
    private Integer clave_tipo_cuenta;
    private Integer clave_manejo_transferencia;
    private Float saldo_inicial;
    private Integer clave_moneda;
    private Float nivel_de_alerta;
    private Integer clave_tipo_presupuesto;
    private Float presupuesto_mensual;
    private Boolean usar_mismo_presupuesto_cada_mes;
    private Boolean pasar_presupuesto_excedente_al_siguiente_mes;
    private String siguiente_folio_cheque;
    private Float limite;

    public Integer getClave_cuenta() {
        return clave_cuenta;
    }

    public void setClave_cuenta(Integer clave_cuenta) {
        this.clave_cuenta = clave_cuenta;
    }

    public Integer getClave_cuenta_padre() {
        return clave_cuenta_padre;
    }

    public void setClave_cuenta_padre(Integer clave_cuenta_padre) {
        this.clave_cuenta_padre = clave_cuenta_padre;
    }

    public Integer getClave_manejo_transferencia() {
        return clave_manejo_transferencia;
    }

    public void setClave_manejo_transferencia(Integer clave_manejo_transferencia) {
        this.clave_manejo_transferencia = clave_manejo_transferencia;
    }

    public Integer getClave_moneda() {
        return clave_moneda;
    }

    public void setClave_moneda(Integer clave_moneda) {
        this.clave_moneda = clave_moneda;
    }

    public Integer getClave_tipo_cuenta() {
        return clave_tipo_cuenta;
    }

    public void setClave_tipo_cuenta(Integer clave_tipo_cuenta) {
        this.clave_tipo_cuenta = clave_tipo_cuenta;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public Float getLimite() {
        return limite;
    }

    public void setLimite(Float limitea) {
        this.limite = limite;
    }

    public Float getNivel_de_alerta() {
        return nivel_de_alerta;
    }

    public void setNivel_de_alerta(Float nivel_de_alerta) {
        this.nivel_de_alerta = nivel_de_alerta;
    }

    public Integer getClave_tipo_presupuesto() {
        return clave_tipo_presupuesto;
    }

    public void setClave_tipo_presupuesto(Integer clave_tipo_presupuesto) {
        this.clave_tipo_presupuesto = clave_tipo_presupuesto;
    }

    public Boolean getPasar_presupuesto_excedente_al_siguiente_mes() {
        return pasar_presupuesto_excedente_al_siguiente_mes;
    }

    public void setPasar_presupuesto_excedente_al_siguiente_mes(Boolean pasar_presupuesto_excedente_al_siguiente_mes) {
        this.pasar_presupuesto_excedente_al_siguiente_mes = pasar_presupuesto_excedente_al_siguiente_mes;
    }

    public Float getPresupuesto_mensual() {
        return presupuesto_mensual;
    }

    public void setPresupuesto_mensual(Float presupuesto_mensual) {
        this.presupuesto_mensual = presupuesto_mensual;
    }

    public Float getSaldo_inicial() {
        return saldo_inicial;
    }

    public void setSaldo_inicial(Float saldo_inicial) {
        this.saldo_inicial = saldo_inicial;
    }

    public String getSiguiente_folio_cheque() {
        return siguiente_folio_cheque;
    }

    public void setSiguiente_folio_cheque(String siguiente_folio_cheque) {
        this.siguiente_folio_cheque = siguiente_folio_cheque;
    }

    public Boolean getUsar_mismo_presupuesto_cada_mes() {
        return usar_mismo_presupuesto_cada_mes;
    }

    public void setUsar_mismo_presupuesto_cada_mes(Boolean usar_mismo_presupuesto_cada_mes) {
        this.usar_mismo_presupuesto_cada_mes = usar_mismo_presupuesto_cada_mes;
    }

    public Float getSaldoActual(String w) throws Fallo {
        try {
            ResultSet rs = null;
            Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
            rs = oDb.getRs("select sum(importe) as saldo_actual from fx_poliza_detalle where clave_cuenta=".concat(String.valueOf(this.clave_cuenta)).concat(!w.equals("") ? " AND ".concat(w) : ""));
            if (rs.next()) {
                if (rs.getObject("saldo_actual") == null) {
                    return this.saldo_inicial;
                } else {
                    return rs.getFloat("saldo_actual") + this.saldo_inicial;
                }
            } else {
                return this.saldo_inicial;
            }

        } catch (Exception e) {
            throw new Fallo("Error al recuperar el saldo de la cuenta ".concat(this.cuenta).concat(": ").concat(e.getMessage()));
        }
    }

    public Float getPresupuestoActual(int mes) throws Fallo {
        try {
            ResultSet rs = null;
            Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
            if (this.clave_tipo_presupuesto == 0) {
                return 0f;
            } else {
                if (this.usar_mismo_presupuesto_cada_mes) {
                    return this.presupuesto_mensual;
                } else {
                    rs = oDb.getRs("select presupuesto, alerta from fx_presupuesto where clave_cuenta=".concat(String.valueOf(this.clave_cuenta)).concat(" AND mes=").concat(String.valueOf(mes)));
                    if (rs.next()) {
                        if (rs.getObject("presupuesto") == null) {
                            return 0f;
                        } else {
                            return rs.getFloat("presupuesto");
                        }
                    } else {
                        return 0f;
                    }
                }
            }

        } catch (Exception e) {
            throw new Fallo("Error al recuperar el presupuesto actual de la cuenta ".concat(this.cuenta).concat(": ").concat(e.getMessage()));
        }
    }

    public Cuenta(Integer claveCuenta, Usuario usuario) throws Fallo {
        super.setTabla("fx_cuenta");
        super.setLlavePrimaria("clave_cuenta");
        super.setPk(String.valueOf(claveCuenta));
        super.setUsuario(usuario);
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fx_cuenta where clave_cuenta=".concat(String.valueOf(claveCuenta)));

            if (rs.next()) {

                this.clave_cuenta = rs.getInt("clave_cuenta");
                this.cuenta = rs.getString("cuenta");
                this.clave_cuenta_padre = rs.getInt("clave_cuenta_padre");
                this.clave_cuenta_padre = rs.getInt("clave_cuenta_padre");
                this.clave_tipo_cuenta = rs.getInt("clave_tipo_cuenta");
                this.clave_manejo_transferencia = rs.getInt("clave_manejo_transferencia");
                this.saldo_inicial = rs.getFloat("saldo_inicial");
                this.clave_moneda = rs.getInt("clave_moneda");
                this.nivel_de_alerta = rs.getFloat("nivel_de_alerta");
                this.clave_tipo_presupuesto = rs.getInt("clave_tipo_presupuesto");
                this.presupuesto_mensual = rs.getFloat("presupuesto_mensual");
                this.limite = rs.getFloat("limite");
                this.usar_mismo_presupuesto_cada_mes = rs.getBoolean("usar_mismo_presupuesto_cada_mes");
                this.pasar_presupuesto_excedente_al_siguiente_mes = rs.getBoolean("pasar_presupuesto_excedente_al_siguiente_mes");
                this.siguiente_folio_cheque = rs.getString("siguiente_folio_cheque");

                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró la cuenta especificada");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la cuenta");
        }
    }

    public Cuenta(Consulta c) throws Fallo {
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

        StringBuilder q = new StringBuilder();

        try {

            if (c.getCampos().get("clave_cuenta").getValor() != null) {
                this.clave_cuenta = Integer.parseInt(c.getCampos().get("clave_cuenta").getValor());
            }

            if (c.getCampos().get("cuenta").getValor() != null) {
                this.cuenta = c.getCampos().get("cuenta").getValor();
            } else {
                throw new Fallo("No se especificó la cuenta, verifique");
            }

            if (c.getCampos().get("clave_cuenta_padre").getValor() != null) {
                this.clave_cuenta_padre = Integer.parseInt(c.getCampos().get("clave_cuenta_padre").getValor());
            }

            if (c.getCampos().get("clave_tipo_cuenta").getValor() != null) {
                this.clave_tipo_cuenta = Integer.parseInt(c.getCampos().get("clave_tipo_cuenta").getValor());
            } else {
                throw new Fallo("No se especificó el tipo de cuenta, verifique");
            }

            if (c.getCampos().get("clave_manejo_transferencia").getValor() != null) {
                this.clave_manejo_transferencia = Integer.parseInt(c.getCampos().get("clave_manejo_transferencia").getValor());
            }

            if (c.getCampos().get("saldo_inicial").getValor() != null) {
                this.saldo_inicial = Float.parseFloat(c.getCampos().get("saldo_inicial").getValor());
            }

            if (c.getCampos().get("clave_moneda").getValor() != null) {
                this.clave_moneda = Integer.parseInt(c.getCampos().get("clave_moneda").getValor());
            } else {
                throw new Fallo("No se especificó la moneda de la cuenta, verifique");
            }

            if (c.getCampos().get("nivel_de_alerta").getValor() != null) {
                this.nivel_de_alerta = Float.parseFloat(c.getCampos().get("nivel_de_alerta").getValor());
            }

            if (c.getCampos().get("clave_tipo_presupuesto").getValor() != null) {
                this.clave_tipo_presupuesto = Integer.parseInt(c.getCampos().get("clave_tipo_presupuesto").getValor());
            }

            if (c.getCampos().get("presupuesto_mensual").getValor() != null) {
                this.presupuesto_mensual = Float.parseFloat(c.getCampos().get("presupuesto_mensual").getValor());
            }

            if (c.getCampos().get("usar_mismo_presupuesto_cada_mes").getValor() != null) {
                this.usar_mismo_presupuesto_cada_mes = Boolean.parseBoolean(c.getCampos().get("usar_mismo_presupuesto_cada_mes").getValor());
            }

            if (c.getCampos().get("pasar_presupuesto_excedente_al_siguiente_mes").getValor() != null) {
                this.pasar_presupuesto_excedente_al_siguiente_mes = Boolean.parseBoolean(c.getCampos().get("pasar_presupuesto_excedente_al_siguiente_mes").getValor());
            }

            if (c.getCampos().get("siguiente_folio_cheque").getValor() != null) {
                this.siguiente_folio_cheque = c.getCampos().get("siguiente_folio_cheque").getValor();
            }

            if (c.getCampos().get("limite").getValor() != null) {
                this.limite = Float.parseFloat(c.getCampos().get("limite").getValor());
            }

        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public String insert(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        //'En caso de que el tipo de cuenta sea 6 o 7 mete los meses'
        //se inserta la nueva categoria 
        resultado.append(super.insert(true));
        if (resultado.toString().split("<error>").length > 1) {
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }

        this.clave_cuenta = Integer.parseInt(resultado.toString().split("<pk>")[1].substring(0, resultado.toString().split("<pk>")[1].indexOf("</pk>")));

        if (this.clave_tipo_cuenta == 6 || this.clave_tipo_cuenta == 7) {
            //Se deben agregar los registros del presupuesto
            for (int i = 1; i <= 12; i++) {
                oDb.execute("INSERT INTO fx_presupuesto (clave_cuenta,mes,presupuesto, alerta) VALUES(".concat(String.valueOf(this.clave_cuenta)).concat(",").concat(String.valueOf(i)).concat(",0,0)"));
            }
        }
        return resultado.toString();
    }

    public String update(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        StringBuilder resultado = new StringBuilder();
        NumberFormat moneyFormatter = new DecimalFormat("$###,###,###,##0.00");

        //Se deben verificar los niveles contra el saldo por si cambio el saldo inicial
        if (this.clave_tipo_cuenta <= 5) {

            if (this.limite != null) {
                if (this.saldo_inicial <= this.limite) {
                    resultado.append("<warning><![CDATA[El saldo de la cuenta ").append(this.cuenta).append(" es de ").append(moneyFormatter.format(this.saldo_inicial)).append(" y está por rebasar o ha rebasado el limite establecido de ").append(moneyFormatter.format(this.limite)).append("]]></warning>");
                }
            }

            if (this.nivel_de_alerta != null) {
                if (this.saldo_inicial <= this.nivel_de_alerta) {
                    resultado.append("<warning><![CDATA[El saldo de la cuenta ").append(this.cuenta).append(" es de ").append(moneyFormatter.format(this.saldo_inicial)).append(" y está por rebasar o ha rebasado el nivel de alerta establecido de ").append(moneyFormatter.format(this.nivel_de_alerta)).append("]]></warning>");
                }
            }
        } else {
            Float presupuestoActual = this.getPresupuestoActual(Calendar.getInstance().get(Calendar.MONTH) + 1);

            if (presupuestoActual != 0) {
                if (this.getSaldoActual("") <= presupuestoActual) {
                    resultado.append("<warning><![CDATA[El saldo de la cuenta ").append(this.cuenta).append(" es de ").append(moneyFormatter.format(this.saldo_inicial)).append(" y está por rebasar o ha rebasado el nivel de alerta establecido de ").append(moneyFormatter.format(this.limite)).append("]]></warning>");
                }
            }
        }

        resultado.append(super.update(true));

        return resultado.toString();

    }

    public String delete(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        resultado.append(super.delete(true,super.getUsuario()));
        //Borra los presupuestos de la categoria eliminada
        oDb.execute("DELETE from fx_presupuesto_categoria WHERE clave_categoria =".concat(String.valueOf(this.clave_cuenta)));
        return resultado.toString();

    }
}
