package mx.org.fide.finanzas;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaccion extends Consulta {

    Integer clave_transaccion;
    Integer clave_poliza;
    Integer clave_tipo_transaccion;
    Boolean cargo;
    Float importe;
    Integer clave_moneda;
    Float tipo_cambio;
    Integer clave_beneficiario;
    Integer clave_cuenta;
    Integer clave_clase;
    String folio_cheque;

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

    public Integer getClave_tipo_transaccion() {
        return clave_tipo_transaccion;
    }

    public void setClave_tipo_transaccion(Integer clave_tipo_transaccion) {
        this.clave_tipo_transaccion = clave_tipo_transaccion;
    }

    public Integer getClave_transaccion() {
        return clave_transaccion;
    }

    public void setClave_transaccion(Integer clave_transaccion) {
        this.clave_transaccion = clave_transaccion;
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

    public Transaccion(Integer claveTransaccion, Conexion cx) throws Fallo {
        super.setTabla("fx_transaccion");
        super.setLlavePrimaria("clave_transaccion");
        super.setPk(String.valueOf(claveTransaccion));

        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        try {
            rs = oDb.getRs("select * from fx_transaccion where clave_transaccion=".concat(String.valueOf(claveTransaccion)));

            if (rs.next()) {

                this.clave_transaccion = rs.getInt("clave_transaccion");
                this.clave_poliza = rs.getInt("clave_poliza");
                this.clave_tipo_transaccion = rs.getInt("clave_tipo_transaccion");
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
                throw new Fallo("No se encontró la transaccion especificada");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la transaccion");
        }
    }

    public Transaccion(Consulta c, Conexion cx) throws Fallo {
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
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public String insert(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultado = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat moneyFormatter = new DecimalFormat("$###,###,###,##0.00");
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        Float saldoActual = 0f;
        Cuenta cuenta;

        // Es necesario verificar los límites de las cuentas involucradas si no se trata de subtrasacciones 
        if (this.clave_cuenta != null) {
            try {
                //en el caso de subtransacciones no es posible verificar saldos de cuentas
                cuenta = new Cuenta(this.clave_cuenta, this.getUsuario());
                saldoActual = cuenta.getSaldoActual("");

                if (this.clave_tipo_transaccion != 1) {
                    if ((((saldoActual + this.importe) < cuenta.getLimite() || (saldoActual + this.importe) > cuenta.getNivel_de_alerta()) && this.clave_tipo_transaccion == 1)
                            || ((saldoActual + this.importe) < cuenta.getLimite() || (saldoActual + this.importe) < cuenta.getNivel_de_alerta()) && (this.clave_tipo_transaccion == 2 || this.clave_tipo_transaccion == 3)) {
                        resultado.append("<warning><![CDATA[El saldo de la cuenta ".concat(cuenta.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + this.importe)).concat(" y está por rebasar o ha rebasado el limite de ").concat(moneyFormatter.format(cuenta.getLimite())).concat(" establecido]]></warning>"));
                    }
                }
                //Se verifica también el limite de presupuesto de la categoria a abonar
                /*if (this.clave_categoria != null) {
                categoria = new Categoria(this.clave_categoria, cx);
                saldoActual = categoria.getSaldoActual(cx, "");
                Float presupuestoMensual = categoria.getPresupuesto_mensual(cx, "", this.fecha.getMonth() + 1) * -1;
                if (presupuestoMensual != 0) {
                if ((saldoActual + this.importe) < presupuestoMensual) {
                resultado.append("<warning><![CDATA[El presupuesto de la categoría ".concat(categoria.getCategoria()).concat(" está por ser rebasado o ha sido rebasado.]]></warning>"));
                }
                }
                }*/


            } catch (Exception e) {
                oDb.cierraConexion();
                oDb = null;
                return "<error><![CDATA[No fue posible determinar el límite de la cuenta]]></error>";
            }

        }
        /* Depósitos y retiros */
        /* En caso de que sea subtransacción no tiene clave_tipo:transaccion, puesto que la hereda del padre */

        /*if (this.clave_tipo_transaccion != null) {
        if (this.clave_tipo_transaccion == 1 || this.clave_tipo_transaccion == 2) {
        
        //1.  Si se trata de una transacción padre
        if (this.tiene_subtransacciones > 0) {
        try {
        //1.1 Guardar el padre
        resultado.append(super.insert(claveEmpleado, ip, browser, forma, cx));
        } catch (Exception e) {
        oDb.cierraConexion();
        oDb = null;
        return resultado.toString();
        }
        
        if (resultado.toString().split("<error>").length > 1) {
        oDb.cierraConexion();
        oDb = null;
        throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }
        
        //1.2 Recuperar el id
        this.clave_transaccion = Integer.parseInt(resultado.toString().split("<pk>")[1].substring(0, resultado.toString().split("<pk>")[1].indexOf("</pk>")));
        
        // 1.3 Actualizar las subtransacciones con clave_transaccion_padre = $("#_ce_").val()-1 que es lo mismo que usuario.claveUsuario,
        // se asocia la cuenta de la transacción principal con las hijas que tienen categorías menor a 0
        
        q.append("UPDATE fx_transaccion SET clave_transaccion_padre=").append(this.clave_transaccion).append(",clave_moneda=").append(this.clave_moneda).append(",tipo_cambio=").append(this.tipo_cambio).append(",clave_tipo_transaccion=").append(this.clave_tipo_transaccion).append(",clave_cuenta=").append(this.clave_cuenta).append(",excluido=0").append(" WHERE clave_categoria>0 AND clave_transaccion_padre=-").append(claveEmpleado);
        
        rs = oDb.execute(q.toString());
        
        //¿Cuál es la cuenta destino que se encuentra en el campo clave_actividad de la transacción hijo con signo negativo
        try {
        q = new StringBuilder().append("select clave_categoria from fx_transaccion where clave_transaccion_padre=-").append(claveEmpleado).append(" and clave_categoria<0");
        rs = oDb.getRs(q.toString());
        
        if (rs.next()) {
        Integer cuentaDestino = rs.getInt("clave_categoria");
        q = new StringBuilder().append("update fx_transaccion set clave_transaccion_padre=").append(this.clave_transaccion).append(",clave_cuenta=").append((cuentaDestino * -1)).append(",clave_moneda=").append(this.clave_moneda).append(",tipo_cambio=").append(this.tipo_cambio).append(",excluido=0").append(",clave_tipo_transaccion=").append(this.clave_tipo_transaccion).append(",clave_categoria=-").append(this.clave_cuenta).append(" where clave_categoria<0 AND clave_transaccion_padre=-").append(claveEmpleado);
        resultado.append(oDb.execute(q.toString()));
        }
        } catch (Exception e) {
        //Es posible que no se encuentre ningun registro si no se realizó una transferencia entre cuentas
        }
        
        } else {
        try {
        //Guarda la transacción sin subtransacciones
        resultado.append(super.insert(claveEmpleado, ip, browser, forma, cx));
        } catch (Exception e) {
        oDb.cierraConexion();
        oDb = null;
        return resultado.toString();
        }
        }
        } else if (this.clave_tipo_transaccion == 3 || this.clave_tipo_transaccion == 4) {
        try {
        //Le asigna 1 al valor de el campo TIENE_SUBTRANSACCIONES antes de que se inserte :-)
        super.getCampos().get(15).setValor("1");
        //Las transferencias colocan un valor negativo en el campo categoria
        super.getCampos().get(8).setValor("-".concat(super.getCampos().get(8).getValor()));
        
        super.getCampos().get(3).setValor(String.valueOf(this.importe * -1));
        //1.1 Guardar el padre
        resultado.append(super.insert(claveEmpleado, ip, browser, forma, cx));
        
        //Verifica que no hubo error al insertarlo 
        
        if (resultado.toString().split("<error>").length > 1) {
        oDb.cierraConexion();
        oDb = null;
        throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }
        
        //1.2 Recuperar el id
        //this.clave_transaccion = Integer.parseInt(resultado.toString().split("<pk>")[1].replace("</pk>", ""));
        this.clave_transaccion = Integer.parseInt(resultado.toString().split("<pk>")[1].substring(0, resultado.toString().split("<pk>")[1].indexOf("</pk>")));
        //1.3 Insertar subtransacción destino asignando el id recien recuperado como la clave_transaccion_padre 
        
        /* Si no se inserta a través de la lista de campos no se registra en la bitácora */
        /*super.getCampos().get(9).setValor(this.clave_transaccion.toString());
        super.getCampos().get(3).setValor(this.importe.toString());
        super.getCampos().get(7).setValor(String.valueOf(this.clave_categoria));
        super.getCampos().get(8).setValor(String.valueOf(this.clave_cuenta * -1));
        super.getCampos().get(15).setValor("0"); */

        /*resultado.append(super.insert(claveEmpleado, ip, browser, forma, cx));
        
        } catch (Exception e) {
        oDb.cierraConexion();
        oDb = null;
        throw new Fallo(e.getMessage());
        }
        }
        } else {
        resultado.append(super.insert(claveEmpleado, ip, browser, forma, cx));
        }*/

        return resultado.toString();
    }

    public String update(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        ResultSet rs;
        StringBuilder resultado = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat moneyFormatter = new DecimalFormat("$###,###,###,##0.00");
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        Cuenta cuenta;
        Float saldoActual = 0f;


        // Es necesario verificar los límites de las cuentas involucradas si no se trata de subtrasacciones 
        /*if (this.clave_cuenta != null) {
        try {
        cuenta = new Cuenta(this.clave_cuenta, cx);
        rs = oDb.getRs("SELECT cuenta, limite_de_cuenta, nivel_de_alerta FROM fx_cuenta WHERE clave_cuenta=" + this.clave_cuenta);
        saldoActual = cuenta.getSaldoActual(cx, "");
        
        if ((saldoActual + this.importe < cuenta.getLimite_de_cuenta() || saldoActual + this.importe < cuenta.getNivel_de_alerta())) {
        resultado.append("<warning><![CDATA[El saldo de la cuenta ".concat(cuenta.getCuenta()).concat(" es de ").concat(moneyFormatter.format(saldoActual + this.importe)).concat(" y está por rebasar o ha rebasado el limite de ").concat(moneyFormatter.format(cuenta.getLimite_de_cuenta())).concat(" establecido]]></warning>"));
        }
        
        } catch (Exception e) {
        oDb.cierraConexion();
        oDb = null;
        return "<error><![CDATA[No fue posible determinar el límite de la cuenta]]></error>";
        }
        }
        //Se debe verificar el tipo de transacción anterior si no se trata de una subtrasacción editada
        //si la transacción anterior fue una transferencia o un retiro por cajero
        //se deben borrar las subtransacciones
        //si se trata de un deposito o retiro con subtransacciones, se debe verificar el número de subtransacciones, 
        //y si no hay se deben borrar las anteriores cuyo clave_transaccion padre = $pk
        if (super.getCampos().get(2).getValorOriginal() != null) {
        if ((super.getCampos().get(2).getValorOriginal().equals("3") && !super.getCampos().get(2).getValor().equals("3"))
        || (super.getCampos().get(2).getValorOriginal().equals("4") && !super.getCampos().get(2).getValor().equals("4"))) {
        oDb.execute("DELETE FROM fx_transaccion WHERE clave_transaccion_padre=".concat(String.valueOf(this.clave_transaccion)));
        }
        }
        /* Depósitos y retiros */
        /* En caso de que sea subtransacción no tiene clave_tipo:transaccion, puesto que la hereda del padre */
        /*if (this.clave_tipo_transaccion != null) {
        if (this.clave_tipo_transaccion == 1 || this.clave_tipo_transaccion == 2) {
        //1.  Si se trata de una transacción padre
        if (this.tiene_subtransacciones > 0) {
        try {
        //1.1 Guardar el padre
        resultado.append(super.update(claveEmpleado, ip, browser, forma, cx));
        } catch (Exception e) {
        oDb.cierraConexion();
        oDb = null;
        return resultado.toString();
        }
        
        //1.2 Recuperar el id
        this.clave_transaccion = Integer.parseInt(resultado.toString().split("<pk>")[1].substring(0, resultado.toString().split("<pk>")[1].indexOf("</pk>")));
        
        //1.3 Actualizar las subtransacciones con clave_transaccion_padre = $("#_ce_").val()-1 que es lo mismo que usuario.claveUsuario
        q.append("UPDATE fx_transaccion SET clave_transaccion_padre=").append(this.clave_transaccion).append(" WHERE clave_transaccion_padre=-").append(claveEmpleado);
        
        //Se busca el número de registros actualizados para asignarlos a tiene_subtransacciones
        rs = oDb.execute(q.toString());
        } else {
        try {
        //Guarda la transacción sin subtransacciones
        resultado.append(super.update(claveEmpleado, ip, browser, forma, cx));
        } catch (Exception e) {
        oDb.cierraConexion();
        oDb = null;
        return resultado.toString();
        }
        }
        } else if (this.clave_tipo_transaccion == 3 || this.clave_tipo_transaccion == 4) {
        try {
        //Le asigna 1 al valor de el campo TIENE_SUBTRANSACCIONES antes de que se inserte :-)
        super.getCampos().get(15).setValor("1");
        //Las transferencias colocan un valor negativo en el campo categoria
        if (Integer.parseInt(super.getCampos().get(8).getValor()) > 0) {
        super.getCampos().get(8).setValor("-".concat(super.getCampos().get(8).getValor()));
        }
        
        super.getCampos().get(3).setValor(String.valueOf(this.importe * -1));
        //1.1 Guardar el padre
        resultado.append(super.update(claveEmpleado, ip, browser, forma, cx));
        
        //Verifica que no hubo error al insertarlo 
        
        if (resultado.toString().split("<error>").length > 1) {
        oDb.cierraConexion();
        oDb = null;
        throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }
        
        //1.2 Recuperar el id
        //this.clave_transaccion = Integer.parseInt(resultado.toString().split("<pk>")[1].substring(0,resultado.toString().split("<pk>")[1].indexOf("</pk>")));
        //1.3 Insertar subtransacción destino asignando el id recien recuperado como la clave_transaccion_padre 
        
        /* Si no se inserta a través de la lista de campos no se registra en la bitácora */
        /* Se debe recuperar la clave de la transacción hija */
        /*rs = oDb.getRs("SELECT clave_transaccion FROM fx_transaccion WHERE clave_transaccion_padre=".concat(this.clave_transaccion.toString()));
        
        if (rs.next()) {
        super.getCampos().get(0).setValor(rs.getString("clave_transaccion"));
        super.setPk(rs.getString("clave_transaccion"));
        } else {
        throw new Fallo("No se encontró subtransacción");
        }
        
        
        super.getCampos().get(9).setValor(this.clave_transaccion.toString());
        super.getCampos().get(3).setValor(this.importe.toString());
        super.getCampos().get(7).setValor(String.valueOf(this.clave_categoria));
        super.getCampos().get(8).setValor(String.valueOf(this.clave_cuenta * -1));
        super.getCampos().get(15).setValor("0");
        
        resultado.append(super.update(claveEmpleado, ip, browser, forma, cx));
        
        } catch (Exception e) {
        oDb.cierraConexion();
        oDb = null;
        throw new Fallo(e.getMessage());
        }
        }
        } else {
        resultado.append(super.update(claveEmpleado, ip, browser, forma, cx));
        }*/

        return resultado.toString();
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

        oDb.execute("DELETE FROM fx_transaccion WHERE clave_transaccion_padre=".concat(String.valueOf(this.clave_transaccion)));
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
    /*public static void main(String args[]) {
    
    Transaccion t;
    
    try {
    Conexion c = new Conexion();
    configuracion = new Consulta(2, "select", "0", "", null, c);
    ArrayList<Campo> cd = configuracion.getCampos();
    ArrayList<ArrayList> d = configuracion.getRegistros();
    
    System.out.println("*** Inicio de definición de columnas ***");
    for (Campo fd : cd) {
    System.out.println(fd.toString());
    }
    
    System.out.println("*** Fin de definición de columnas ***");
    System.out.println("*** Inicio de datos ***");
    int i = 0;
    for (ArrayList r : d) {
    System.out.println("");
    i = 0;
    for (Object sd : r) {
    System.out.println(cd.get(i).nombre + ": " + sd.toString());
    i++;
    }
    }
    System.out.println("*** Fin de datos ***");
    } catch (Fallo ex) {
    Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    }*/
}
