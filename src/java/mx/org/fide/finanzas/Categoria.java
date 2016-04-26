/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.finanzas;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import java.sql.ResultSet;

/**
 *
 * @author Daniel
 */
public class Categoria extends Consulta {

    private Integer clave_categoria;
    private String categoria;
    private Integer clave_categoria_padre;
    private Integer clave_tipo_categoria;
    private Integer clave_tipo_presupuesto;
    private Boolean usar_mismo_presupuesto_cada_mes;
    private Boolean pasar_presupuesto_excedente_al_siguiente_mes;
    private Float presupuesto_mensual;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getClave_categoria() {
        return clave_categoria;
    }

    public void setClave_categoria(Integer clave_categoria) {
        this.clave_categoria = clave_categoria;
    }

    public Integer getClave_categoria_padre() {
        return clave_categoria_padre;
    }

    public void setClave_categoria_padre(Integer clave_categoria_padre) {
        this.clave_categoria_padre = clave_categoria_padre;
    }

    public Integer getClave_tipo_categoria() {
        return clave_tipo_categoria;
    }

    public void setClave_tipo_categoria(Integer clave_tipo_categoria) {
        this.clave_tipo_categoria = clave_tipo_categoria;
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

    public Boolean getUsar_mismo_presupuesto_cada_mes() {
        return usar_mismo_presupuesto_cada_mes;
    }

    public void setUsar_mismo_presupuesto_cada_mes(Boolean usar_mismo_presupuesto_cada_mes) {
        this.usar_mismo_presupuesto_cada_mes = usar_mismo_presupuesto_cada_mes;
    }

    public Float getPresupuesto_mensual(Conexion cx, String w, Integer mes) throws Fallo {
        if (this.getUsar_mismo_presupuesto_cada_mes()) {
            return presupuesto_mensual; 
        } else {
              try {
                    ResultSet rs = null;
                    Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
                    rs = oDb.getRs("select presupuesto from fx_presupuesto_categoria where clave_categoria=" + this.clave_categoria + " AND mes="+ String.valueOf(mes));
                    
                    if (rs.next()) {
                        return rs.getFloat("presupuesto");
                    }
                    else {
                        return 0f;
                    }
              } catch (Exception e) {
                  throw new Fallo ("Error al calcular el presupuesto mensual de la categoria ".concat(this.categoria).concat(": ").concat(e.getMessage()));
              }
        }
    }

    public void setPresupuesto_mensual(Float presupuesto_mensual) {
        this.presupuesto_mensual = presupuesto_mensual;
    }

    public Float getSaldoActual(Conexion cx, String w) throws Fallo{
        try {
        ResultSet rs = null;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        rs = oDb.getRs("select sum(importe) as saldo_actual from fx_transaccion where clave_categoria=".concat(String.valueOf(this.clave_categoria)).concat(!w.equals("")?" AND ".concat(w):""));
        
        if (rs.next()) {
            if (rs.getObject("saldo_actual")==null)
                return 0f;
            else 
                return rs.getFloat("saldo_actual");            
        } else {
            return 0f;
        }
        
        } catch(Exception e) {
            throw new Fallo("Error al recuperar el saldo de la categoria ".concat(this.categoria).concat(": ").concat(e.getMessage()));
        }
    }
    
    public Categoria(Consulta c, Conexion cx) throws Fallo {
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

        /*private Integer clave_categoria;
        private String categoria;
        private Integer clave_categoria_padre;
        private Integer clave_tipo_categoria;
        private Integer clave_tipo_presupuesto;
        private Boolean usar_mismo_presupuesto_cada_mes;
        private Boolean pasar_presupuesto_excedente_al_siguiente_mes;    */
    }

    public Categoria(Integer claveCategoria, Conexion cx) throws Fallo {
        super.setTabla("fx_categoria");
        super.setLlavePrimaria("clave_categoria");
        super.setPk(String.valueOf(claveCategoria));

        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        try {
            rs = oDb.getRs("select * from fx_categoria where clave_categoria=".concat(String.valueOf(claveCategoria)));

            if (rs.next()) {
                this.clave_categoria = rs.getInt("clave_categoria");
                this.categoria = rs.getString("categoria");
                this.clave_categoria_padre = rs.getInt("clave_categoria_padre");
                this.clave_tipo_categoria = rs.getInt("clave_tipo_categoria");
                this.usar_mismo_presupuesto_cada_mes = rs.getBoolean("usar_mismo_presupuesto_cada_mes");
                this.pasar_presupuesto_excedente_al_siguiente_mes = rs.getBoolean("pasar_presupuesto_excedente_al_siguiente_mes");
                this.presupuesto_mensual= rs.getFloat("presupuesto_mensual");
                   
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró la categoria especificada");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la categoria");
        }
    }

    public String insert(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        ResultSet rs;
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());

        //se inserta la nueva categoria 
        resultado.append(super.insert(true));
        if (resultado.toString().split("<error>").length > 1) {
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }

        this.clave_categoria = Integer.parseInt(resultado.toString().split("<pk>")[1].substring(0, resultado.toString().split("<pk>")[1].indexOf("</pk>")));

        //Se deben agregar los registros del presupuesto
        for (int i = 1; i <= 12; i++) {
            oDb.execute("INSERT INTO fx_presupuesto_categoria (clave_categoria,mes,presupuesto, alerta) VALUES(".concat(String.valueOf(this.clave_categoria)).concat(",").concat(String.valueOf(i)).concat(",0,0)"));
        }
        return resultado.toString();
    }

    public String update(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        ResultSet rs;
        StringBuilder resultado = new StringBuilder();
        return resultado.toString();
    }

    public String delete(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        resultado.append(super.delete(true,super.getUsuario()));
        //Borra los presupuestos de la categoria eliminada
        oDb.execute("DELETE from fx_presupuesto_categoria WHERE clave_categoria =".concat(String.valueOf(this.clave_categoria)));
        return resultado.toString();

    }
}
