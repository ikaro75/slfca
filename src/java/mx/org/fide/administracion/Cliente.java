
package mx.org.fide.administracion;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class Cliente extends Consulta {
    private Integer claveCliente;
    private String nombre;
    private String razonSocial;
    private Integer claveClienteContactoDe;
    private String rfc;
    private String curp;
    private String domicilio;
    private String telefono;
    private String email;
    private Integer claveCategoria;
    private Integer claveZona;
    private int claveEstatus;
    private Integer diasCredito;
    private Float limiteCredito;
    private Float porcentajeDescuento;
    private Integer diaCobranza;
    private Integer diaCorte;
    private Integer claveVendedor;
    private String puesto;

    public Cliente() {
    }

    public Cliente(Integer claveCliente) {
        this.claveCliente = claveCliente;
    }

    public Cliente(Integer claveCliente, Usuario usuario) throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());

        try {
            rs = oDb.getRs("select * from administrax_cliente where clave_cliente=".concat(String.valueOf(claveCliente)));

            if (rs.next()) {
                this.claveCliente=rs.getInt("clave_cliente");
                this.nombre=rs.getString("nombre");
                this.razonSocial=rs.getString("razon_social");
                this.claveClienteContactoDe=rs.getInt("clave_cliente_contacto_de");
                this.rfc=rs.getString("rfc");
                this.curp=rs.getString("curp");
                this.domicilio=rs.getString("domicilio");
                this.telefono=rs.getString("telefono");
                this.email=rs.getString("email");
                this.claveCategoria=rs.getInt("clave_categoria");
                this.claveZona=rs.getInt("clave_zona");
                this.claveEstatus=rs.getInt("clave_estatus"); 
                this.diasCredito=rs.getInt("dias_credito"); 
                this.limiteCredito =rs.getFloat("limite_credito"); 
                this.porcentajeDescuento =rs.getFloat("porcentaje_descuento");
                this.diaCobranza =rs.getInt("dia_cobranza");
                this.diaCorte =rs.getInt("dia_corte");
                this.claveVendedor =rs.getInt("clave_vendedor");
                this.puesto =rs.getString("puesto");         

            } else {
                throw new Fallo("No se encontró el cliente especificado");
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar el cliente: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        }
    }

  public Cliente(Consulta c) throws Fallo {
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
                if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("varchar")) {
                    setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), String.class);
                } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("int")) {
                    setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Integer.class);
                } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("float") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("decimal")) {
                    setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Float.class);
                } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                    setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), java.util.Date.class);
                }

                if (c.getCampos().get(field.getName()) != null) {
                    if (c.getCampos().get(field.getName()).getValor() != null && !c.getAccion().equals("delete")) {

                        //Valida si el valor del campo es una cadena vacío 
                        if (c.getCampos().get(field.getName()).getValor().equals("") && !c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("varchar")) {
                            if (c.getCampos().get(field.getName()).getObligatorio() == 1 && !c.getCampos().get(field.getName()).isAutoIncrement()) {
                                throw new Fallo("El valor del campo ".concat(c.getCampos().get(field.getName()).getAlias()).concat(" no es válido, verifique"));
                            }
                        } else {
                            if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("varchar")) {
                                setter.invoke(this, c.getCampos().get(field.getName()).getValor());
                            } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("int")) {
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
                } else {
                    if (c.getCampos().get(field.getName()).getObligatorio() == 1) {
                        throw new Fallo("No se especificó ".concat(field.getName()).concat(", verifique"));
                    }
                }
            }

            //Aquí debe ir el código para cargar desde el constructor los detalles del pagare


        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }

    }
 
    public Integer getClaveCliente() {
        return claveCliente;
    }

    public void setClaveCliente(Integer claveCliente) {
        this.claveCliente = claveCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Integer getClaveClienteContactoDe() {
        return claveClienteContactoDe;
    }

    public void setClaveClienteContactoDe(Integer claveClienteContactoDe) {
        this.claveClienteContactoDe = claveClienteContactoDe;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getClaveCategoria() {
        return claveCategoria;
    }

    public void setClaveCategoria(Integer claveCategoria) {
        this.claveCategoria = claveCategoria;
    }

    public Integer getClaveZona() {
        return claveZona;
    }

    public void setClaveZona(Integer claveZona) {
        this.claveZona = claveZona;
    }

    public int getClaveEstatus() {
        return claveEstatus;
    }

    public void setClaveEstatus(int claveEstatus) {
        this.claveEstatus = claveEstatus;
    }

    public Integer getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(Integer diasCredito) {
        this.diasCredito = diasCredito;
    }

    public Float getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(Float limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public Float getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Float porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Integer getDiaCobranza() {
        return diaCobranza;
    }

    public void setDiaCobranza(Integer diaCobranza) {
        this.diaCobranza = diaCobranza;
    }

    public Integer getDiaCorte() {
        return diaCorte;
    }

    public void setDiaCorte(Integer diaCorte) {
        this.diaCorte = diaCorte;
    }

    public Integer getClaveVendedor() {
        return claveVendedor;
    }

    public void setClaveVendedor(Integer claveVendedor) {
        this.claveVendedor = claveVendedor;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }


    
}
