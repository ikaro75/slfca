/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.finanzas;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.sql.ResultSet;

/**
 *
 * @author Daniel
 */
public class PolizaDetalle extends Consulta {

    Integer clave_poliza_detalle;
    Integer clave_poliza;
    Integer folio_pagare;
    Integer clave_tipo_transaccion;
    Boolean cargo;
    Float importe;
    Integer clave_moneda;
    Float tipo_cambio;
    Integer clave_beneficiario;
    Integer clave_cuenta;
    Integer clave_clase;
    String folio_cheque;

    public PolizaDetalle() {
        
    }
    
    public PolizaDetalle(Integer clave_poliza_detalle, Usuario usuario) throws Fallo {
        super.setTabla("fx_poliza_detalle");
        super.setLlavePrimaria("clave_poliza_detalle");
        super.setPk(String.valueOf(clave_poliza_detalle));
        super.setUsuario(usuario);
        
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fx_poliza_detalle where clave_poliza_detalle=".concat(String.valueOf(clave_poliza_detalle)));

            if (rs.next()) {
                this.clave_poliza_detalle = rs.getInt("clave_poliza_detalle");
                this.clave_poliza = rs.getInt("clave_poliza");
                this.folio_pagare = rs.getInt("folio_pagare");
                this.clave_tipo_transaccion= rs.getInt("clave_tipo_transaccion");
                this.cargo = rs.getBoolean("cargo");
                this.importe = rs.getFloat("importe");
                this.clave_moneda = rs.getInt("clave_moneda");
                this.tipo_cambio = rs.getFloat("tipo_cambio");
                this.clave_beneficiario = rs.getInt("clave_beneficiario");
                this.clave_cuenta = rs.getInt("clave_cuenta");
                this.clave_clase = rs.getInt("clave_clase");
                this.folio_cheque = rs.getString("folio_cheque");
                
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró el detalle de la póliza especificada");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar el detalle de la póliza");
        }
    }

    public PolizaDetalle(Consulta c, Conexion cx) throws Fallo {
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
        ResultSet rs;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        try {          
            if (c.getCampos().get("clave_poliza_detalle").getValor() != null) {
                this.clave_poliza_detalle = Integer.parseInt(c.getCampos().get("clave_poliza_detalle").getValor());
            }
            
            if (c.getCampos().get("clave_poliza").getValor() != null) {
                this.clave_poliza = Integer.parseInt(c.getCampos().get("clave_poliza").getValor());
            }

            if (c.getCampos().get("folio_pagare").getValor() != null) {
                this.folio_pagare = Integer.parseInt(c.getCampos().get("folio_pagare").getValor());
            }
                        
            if (c.getCampos().get("clave_tipo_transaccion").getValor() != null) {
                this.clave_poliza = Integer.parseInt(c.getCampos().get("clave_tipo_transaccion").getValor());
            }
                        
            if (c.getCampos().get("cargo").getValor() != null) {
                this.cargo = Boolean.parseBoolean(c.getCampos().get("cargo").getValor());
            }

            if (c.getCampos().get("importe").getValor() != null) {
                this.importe = Float.parseFloat(c.getCampos().get("importe").getValor());
            }
            
            if (c.getCampos().get("clave_moneda").getValor() != null) {
                this.clave_moneda = Integer.parseInt(c.getCampos().get("clave_moneda").getValor());
            }                     

            if (c.getCampos().get("importe").getValor() != null) {
                this.importe = Float.parseFloat(c.getCampos().get("importe").getValor());
            }
                        
                        
            if (c.getCampos().get("tipo_cambio").getValor() != null) {
                this.tipo_cambio = Float.parseFloat(c.getCampos().get("tipo_cambio").getValor());
            }

            if (c.getCampos().get("clave_beneficiario").getValor() != null) {
                this.clave_beneficiario = Integer.parseInt(c.getCampos().get("clave_beneficiario").getValor());
            }

            if (c.getCampos().get("clave_cuenta").getValor() != null) {
                this.clave_cuenta = Integer.parseInt(c.getCampos().get("clave_cuenta").getValor());
            }

            if (c.getCampos().get("clave_clase").getValor() != null) {
                this.clave_clase = Integer.parseInt(c.getCampos().get("clave_clase").getValor());
            }

            if (c.getCampos().get("folio_cheque").getValor() != null) {
                this.folio_cheque = c.getCampos().get("folio_cheque").getValor();
            }


        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public Integer getClave_tipo_transaccion() {
        return clave_tipo_transaccion;
    }

    public void setClave_tipo_transaccion(Integer clave_tipo_transaccion) {
        this.clave_tipo_transaccion = clave_tipo_transaccion;
    }

    public Integer getFolio_pagare() {
        return folio_pagare;
    }

    public void setFolio_pagare(Integer folio_pagare) {
        this.folio_pagare = folio_pagare;
    }

    public Boolean getCargo() {
        return cargo;
    }

    public void setCargo(Boolean cargo) {
        this.cargo = cargo;
    }

    public Integer getClave_beneficiario() {
        return clave_beneficiario;
    }

    public void setClave_beneficiario(Integer clave_beneficiario) {
        this.clave_beneficiario = clave_beneficiario;
    }

    public Integer getClave_clase() {
        return clave_clase;
    }

    public void setClave_clase(Integer clave_clase) {
        this.clave_clase = clave_clase;
    }

    public Integer getClave_cuenta() {
        return clave_cuenta;
    }

    public void setClave_cuenta(Integer clave_cuenta) {
        this.clave_cuenta = clave_cuenta;
    }

    public Integer getClave_moneda() {
        return clave_moneda;
    }

    public void setClave_moneda(Integer clave_moneda) {
        this.clave_moneda = clave_moneda;
    }

    public Integer getClave_poliza() {
        return clave_poliza;
    }

    public void setClave_poliza(Integer clave_poliza) {
        this.clave_poliza = clave_poliza;
    }

    public Integer getClave_poliza_detalle() {
        return clave_poliza_detalle;
    }

    public void setClave_poliza_detalle(Integer clave_poliza_detalle) {
        this.clave_poliza_detalle = clave_poliza_detalle;
    }

    public String getFolio_cheque() {
        return folio_cheque;
    }

    public void setFolio_cheque(String folio_cheque) {
        this.folio_cheque = folio_cheque;
    }

    public Float getImporte() {
        return importe;
    }

    public void setImporte(Float importe) {
        this.importe = importe;
    }

    public Float getTipo_cambio() {
        return tipo_cambio;
    }

    public void setTipo_cambio(Float tipo_cambio) {
        this.tipo_cambio = tipo_cambio;
    }
}
