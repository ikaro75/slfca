/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.administracion;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Producto extends Consulta {

    private Integer clave_producto;
    private String producto;
    private Integer clave_tipo_producto;
    private Float precio;
    private Integer clave_moneda;
    private Float existencia;
    private Integer clave_categoria;
    private Integer clave_linea;
    private Integer clave_cuenta;
    private Integer clave_unidad_entrada;
    private Integer clave_unidad_salida;
    private Float factor_entre_unidades;
    private Integer clave_control_almacen;
    private String volumen;
    private String peso;

    private Integer clave_impuesto_predeterminado;
    private Integer clave_tipo_costeo;
    private Float stock_minimo;
    private Float stock_maximo;
    private Boolean manejo_de_numeros_de_serie;
    private Boolean manejo_de_lotes;
    private Boolean manejo_pedimentos_aduanales;
    private Integer tiempo_surtido;
    private String codigo_barra;
    private String imagen;
    private Boolean activo;
    private Float tipo_cambio;

    public Producto() {
    }

    public Producto(Integer clave_producto) {
        this.clave_producto = clave_producto;
    }

    public Producto(Integer clave_producto, Usuario usuario) throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        this.setUsuario(usuario);
        Conexion oDb = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());

        try {
            rs = oDb.getRs("select * from administrax_producto where clave_producto=".concat(String.valueOf(clave_producto)));

            if (rs.next()) {
                this.clave_producto = rs.getInt("clave_producto");
                this.producto = rs.getString("producto");
                this.clave_categoria = rs.getInt("clave_categoria");
                this.clave_linea = rs.getInt("clave_linea");
                this.clave_cuenta = rs.getInt("clave_cuenta");
                this.clave_unidad_entrada = rs.getInt("clave_unidad_entrada");
                this.clave_unidad_salida = rs.getInt("clave_unidad_salida");
                this.factor_entre_unidades = rs.getFloat("factor_entre_unidades");
                this.clave_control_almacen = rs.getInt("clave_control_almacen");
                this.volumen = rs.getString("volumen");
                this.peso = rs.getString("peso");
                this.precio = rs.getFloat("precio");
                this.clave_moneda = rs.getInt("clave_moneda");
                this.existencia = rs.getFloat("existencia");
                this.clave_impuesto_predeterminado = rs.getInt("clave_impuesto_predeterminado");
                this.clave_tipo_costeo = rs.getInt("clave_tipo_costeo");
                this.stock_minimo = rs.getFloat("stock_minimo");
                this.stock_maximo = rs.getFloat("stock_maximo");
                this.manejo_de_numeros_de_serie = rs.getBoolean("manejo_de_numeros_de_serie");
                this.manejo_de_lotes = rs.getBoolean("manejo_de_lotes");
                this.manejo_pedimentos_aduanales = rs.getBoolean("manejo_pedimentos_aduanales");
                this.tiempo_surtido = rs.getInt("tiempo_surtido");
                this.codigo_barra = rs.getString("codigo_barra");
                this.imagen = rs.getString("imagen");
                this.activo = rs.getBoolean("activo");
                this.tipo_cambio = rs.getFloat("tipo_cambio");
                this.clave_tipo_producto = rs.getInt("clave_tipo_producto");
            } else {
                throw new Fallo("No se encontró el producto especificado");
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar el producto: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        }
    }

    public Producto(Consulta c) throws Fallo {
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

    public Integer getClave_producto() {
        return clave_producto;
    }

    public void setClave_producto(Integer clave_producto) {
        this.clave_producto = clave_producto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getClave_tipo_producto() {
        return clave_tipo_producto;
    }

    public void setClave_tipo_producto(Integer clave_tipo_producto) {
        this.clave_tipo_producto = clave_tipo_producto;
    }

    public Integer getClave_categoria() {
        return clave_categoria;
    }

    public void setClave_categoria(Integer clave_categoria) {
        this.clave_categoria = clave_categoria;
    }

    public Integer getClave_linea() {
        return clave_linea;
    }

    public void setClave_linea(Integer clave_linea) {
        this.clave_linea = clave_linea;
    }

    public Integer getClave_cuenta() {
        return clave_cuenta;
    }

    public void setClave_cuenta(Integer clave_cuenta) {
        this.clave_cuenta = clave_cuenta;
    }

    public Integer getclave_unidad_entrada() {
        return clave_unidad_entrada;
    }

    public void setClave_unidad_entrada(Integer clave_unidad_entrada) {
        this.clave_unidad_entrada = clave_unidad_entrada;
    }

    public Integer getClave_unidad_salida() {
        return clave_unidad_salida;
    }

    public void setClave_unidad_salida(Integer clave_unidad_salida) {
        this.clave_unidad_salida = clave_unidad_salida;
    }

    public Float getFactor_entre_unidades() {
        return factor_entre_unidades;
    }

    public void setFactor_entre_unidades(Float factor_entre_unidades) {
        this.factor_entre_unidades = factor_entre_unidades;
    }

    public Integer getClave_control_almacen() {
        return clave_control_almacen;
    }

    public void setClave_control_almacen(Integer clave_control_almacen) {
        this.clave_control_almacen = clave_control_almacen;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getClave_moneda() {
        return clave_moneda;
    }

    public void setClave_moneda(Integer clave_moneda) {
        this.clave_moneda = clave_moneda;
    }

    public Float getExistencia() {
        return existencia;
    }

    public void setExistencia(Float existencia) {
        this.existencia = existencia;
    }

    /*public ArrayList<DocumentoPartida> setExistencia(Float cantidadADescontar, ArrayList<DocumentoPartida> documentoPartidas, Conexion oDb) {
        StringBuilder q;
        StringBuilder resultXML;
        ResultSet rs;
        Float porcentajeImpuesto = 0f;
        Float cantidadStock = 0f;
        Float importeImpuesto = 0f;
        DocumentoPartida documentoPartida;
        //Retira el inventario de acuerdo al tipo de producto
        //El producto es una materia prima

        //resultadoXML.append("<update_administrax_producto>").append(q).append("</update_administrax_producto>");
        try {
            q = new StringBuilder().append("UPDATE administrax_producto SET existencia=existencia-").append(cantidadADescontar).append(" WHERE clave_producto=").append(this.clave_producto);
            oDb.execute(q.toString());

            if (this.clave_tipo_producto == 1) {
                //Si es materia prima 
                if (this.stock_minimo <= this.existencia - cantidadADescontar) {

                    // Aquí debe de agregarse partida de documento al arreglo
                    cantidadStock = (this.stock_maximo - (this.existencia - cantidadADescontar));
                    q = new StringBuilder().append("SELECT porcentaje FROM administrax_impuesto WHERE clave_impuesto=").append(this.clave_impuesto_predeterminado);
                    rs = oDb.getRs(q.toString());
                    if (rs.next()) {
                        porcentajeImpuesto = rs.getFloat("porcentaje");
                    }

                    importeImpuesto = (porcentajeImpuesto > 0) ? porcentajeImpuesto / 100 : porcentajeImpuesto * this.getPrecio() * cantidadStock;
                   //                                        (null, null, Integer claveProducto, Float cantidad, Integer claveUnidad, Integer claveMoneda, Double tipoCambio, Double importeUnitario, Integer claveImpuesto, Double importeImpuesto, Double importeTotal, Integer claveMonedaGastoEnvio, Double importeGastoEnvio, Double tipoCambioGastoEnvio) {
                    documentoPartida = new DocumentoPartida(null, null, this.clave_producto,  this.stock_maximo - cantidadStock, this.clave_unidad_entrada, this.clave_moneda, 1, this.precio * cantidadStock, this.clave_impuesto_predeterminado, importeImpuesto, (this.precio * cantidadStock) + importeImpuesto, this.clave_moneda, 0.0, 1.0);
                    documentoPartidas.add(documentoPartida);
                
                }
            } else if (this.clave_tipo_producto == 2 || this.clave_tipo_producto == 3) {
                // Producto elaborado  
                q = new StringBuilder().append("select clave_ingrediente, cantidad from restaurantex_producto_ingrediente where clave_producto=").append(this.getClave_producto());
                ResultSet rsIngredientesProducto = oDb.getRs(q.toString());
                while (rsIngredientesProducto.next()) {
                        //El ingrediente es un producto
                    //Es necesario verificar la cantidad en existencia para emitir notificación de punto de reorden de producto
                    Producto ingrediente = new Producto(rsIngredientesProducto.getInt("clave_ingrediente"), super.getUsuario());
                    documentoPartidas=ingrediente.setExistencia(cantidadADescontar * rsIngredientesProducto.getFloat("cantidad"), documentoPartidas, oDb);                    

                } 
        } 
        
        return documentoPartidas;
    }catch(Exception ex)  {
        
    }
}*/
    
public Integer getClave_impuesto_predeterminado() {
        return clave_impuesto_predeterminado;
    }

    public void setClave_impuesto_predeterminado(Integer clave_impuesto_predeterminado) {
        this.clave_impuesto_predeterminado = clave_impuesto_predeterminado;
    }

    public Integer getclave_tipo_costeo() {
        return clave_tipo_costeo;
    }

    public void setClave_tipo_costeo(Integer clave_tipo_costeo) {
        this.clave_tipo_costeo = clave_tipo_costeo;
    }

    public Float getStock_minimo() {
        return stock_minimo;
    }

    public void setStock_minimo(Float stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

    public Float getStock_maximo() {
        return stock_maximo;
    }

    public void setStock_maximo(Float stock_maximo) {
        this.stock_maximo = stock_maximo;
    }

    public Boolean getManejo_de_numeros_de_serie() {
        return manejo_de_numeros_de_serie;
    }

    public void setManejo_de_numeros_de_serie(Boolean manejo_de_numeros_de_serie) {
        this.manejo_de_numeros_de_serie = manejo_de_numeros_de_serie;
    }

    public Boolean getManejo_de_lotes() {
        return manejo_de_lotes;
    }

    public void setManejo_de_lotes(Boolean manejo_de_lotes) {
        this.manejo_de_lotes = manejo_de_lotes;
    }

    public Boolean getManejo_pedimentos_aduanales() {
        return manejo_pedimentos_aduanales;
    }

    public void setManejo_pedimentos_aduanales(Boolean manejo_pedimentos_aduanales) {
        this.manejo_pedimentos_aduanales = manejo_pedimentos_aduanales;
    }

    public Integer getTiempo_surtido() {
        return tiempo_surtido;
    }

    public void setTiempo_surtido(Integer tiempo_surtido) {
        this.tiempo_surtido = tiempo_surtido;
    }

    public String getCodigo_barra() {
        return codigo_barra;
    }

    public void setCodigo_barra(String codigo_barra) {
        this.codigo_barra = codigo_barra;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Float getTipo_cambio() {
        return tipo_cambio;
    }

    public void setTipo_cambio(Float tipo_cambio) {
        this.tipo_cambio = tipo_cambio;
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

    public String update(Conexion oDb) throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        Consulta c = null;
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        //Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Boolean startedTransaction = false;
        Integer claveBitacora;
        Integer clavePoliza;
        Integer claveDocumento=0;
        Integer folioSolicitud;
        Float cantidadStock = 0f;
        Float cantidadADescontar =0f;
        Float importeImpuesto =0f;
        Documento documento;
        DocumentoPartida documentoPartida = new DocumentoPartida();
        Calendar calendario = Calendar.getInstance();
        Float candidadRequeridaReceta = 0f;
        Producto ingrediente;

        try {

            //1. Abre transaccion
            startedTransaction = true;
            
            //Administra tarjetas salida de almacén
            if (this.getClave_tipo_producto()==1) {
                //Se debe de descontar del inventario los productos y la materia prima relacionadas al producto y a los productos del paquete
                //para ello se crea una tarjeta de salida de almacen
                // Se verifica si ya hay una tarjeta de salida para el producto especificado
                q = new StringBuilder().append("SELECT clave_documento FROM administrax_producto WHERE clave_producto=").append(this.clave_producto);
                rs = oDb.getRs(q.toString());                
                if (rs.next()) {
                    documento = new Documento(rs.getInt("clave_documento"),super.getUsuario());
                } else  {
                    //Extrae la empresa a la que pertenece el documento
                    q = new StringBuilder().append("SELECT clave_empresa FROM restaurantex_sala WHERE clave_sala IN (SELECT clave_sala FROM restaurantex_mesa WHERE clave_mesa IN (SELECT clave_mesa FROM restaurantex_cuenta WHERE clave_cuenta=").append(this.getClave_cuenta()).append("))");
                    rs= oDb.getRs(q.toString());
                    
                    if (!rs.next()) {
                        throw new Fallo("<error>No encontré la empresa del control de almacén del producto ".concat(this.producto).concat(", verifique</error>"));
                    }
                    
                    documento = new Documento();                    
                    documento.setClave_empresa(rs.getInt("clave_empresa"));
                    //Extrae el almacen al que pertenece el producto
                    q = new StringBuilder().append("SELECT clave_almacen FROM administrax_almacen_producto WHERE clave_producto =").append(this.clave_producto);
                    rs= oDb.getRs(q.toString());
                    
                    if (!rs.next()) {
                        resultadoXML.append("<warning>No encontró el almacen del producto ".concat(this.producto).concat(", verifique</warning>"));
                    }
                                        
                    documento.setClave_producto(this.clave_producto);
                    
                    documento.setFecha(calendario.getTime());
                    documento.setClave_tipo_documento(11);
                    documento.setClave_estatus(0);
                    //En el insert está lo necesario para establecer el folio del documento
                    documento.insert(oDb);
                }
                
                // Ahora se deben registrar la partida de las tarjetas de almacén
                cantidadADescontar =Float.parseFloat(super.getCampos().get("existencia").getValorOriginal()) - Float.parseFloat(this.existencia.toString());
                documentoPartida.setClaveDocumento(documento.getClave_documento());
                documentoPartida.setClaveProducto(this.clave_producto);
                documentoPartida.setCantidad(cantidadADescontar);
                documentoPartida.setClaveUnidad(this.clave_unidad_salida);
                documentoPartida.setClaveMoneda(this.clave_moneda);
                documentoPartida.setTipoCambio(Double.parseDouble(this.tipo_cambio.toString()));
                documentoPartida.setImporteUnitario(Double.parseDouble(cantidadADescontar.toString()) * Double.parseDouble(this.precio.toString()));
                //Al insertar la partida se debe de descomponer 
                //el producto de acuerdo a su tipo, incluir impuestos
                //y calcular el total
                documentoPartida.insert(oDb);
            } else if(this.getClave_tipo_producto()==2) {
                //Producto terminado
                //Extrae materias primas
                q = new StringBuilder().append("SELECT clave_producto, cantidad FROM restaurantex_producto_ingrediente WHERE clave_producto=").append(this.clave_producto);
                rs = oDb.getRs(q.toString());
                while (rs.next()) {
                    candidadRequeridaReceta = rs.getFloat("cantidad");
                    ingrediente = new Producto(rs.getInt("clave_producto"), super.getUsuario());
                    
                    // Se verifica si ya hay una tarjeta de salida para el producto especificado
                    q = new StringBuilder().append("SELECT clave_documento FROM administrax_documento WHERE clave_producto=").append(ingrediente.clave_producto);
                    rs = oDb.getRs(q.toString());                
                    if (rs.next()) {
                        documento = new Documento(rs.getInt("clave_documento"),super.getUsuario());
                    } else  {
                        //Extrae la empresa a la que pertenece el documento
                        q = new StringBuilder().append("SELECT clave_empresa FROM administrax_almacen WHERE clave_almacen IN (SELECT clave_sala FROM restaurantex_mesa WHERE clave_mesa IN (SELECT clave_mesa FROM restaurantex_cuenta WHERE clave_cuenta=").append(this.clave_cuenta).append("))");
                        rs= oDb.getRs(q.toString());

                        if (!rs.next()) {
                            throw new Fallo("<error>No encontré la empresa del control de almacén del producto ".concat(this.producto).concat(", verifique</error>"));
                        }

                        documento = new Documento();                    
                        documento.setClave_empresa(rs.getInt("clave_empresa"));
                        //Extrae el almacen al que pertenece el producto
                        q = new StringBuilder().append("SELECT clave_almacen FROM administrax_almacen_producto WHERE clave_producto =").append(this.clave_producto);
                        rs= oDb.getRs(q.toString());

                        if (!rs.next()) {
                            resultadoXML.append("<warning>No encontró el almacen del producto ".concat(this.producto).concat(", verifique</warning>"));
                        }

                        documento.setClave_producto(this.clave_producto);

                        documento.setFecha(calendario.getTime());
                        documento.setClave_tipo_documento(11);
                        documento.setClave_estatus(0);
                        //En el insert está lo necesario para establecer el folio del documento
                        documento.insert(oDb);
                    }
                    
                    q = new StringBuilder().append("SELECT porcentaje FROM administrax_impuesto WHERE clave_impuesto=").append(ingrediente.clave_impuesto_predeterminado);
                    rs = oDb.getRs(q.toString());
                    if (!rs.next())     {
                         throw new Fallo("No se encontró el impuesto para el producto ".concat(ingrediente.getProducto()));
                    }
                    
                    cantidadADescontar =(Float.parseFloat(ingrediente.getCampos().get("existencia").getValorOriginal()) - Float.parseFloat(ingrediente.existencia.toString())) * rs.getFloat("cantidad");
                    importeImpuesto = cantidadADescontar * this.precio * (rs.getInt("porcentaje")==0?0:rs.getInt("porcentaje")/100);

                    q = new StringBuilder().append("INSERT INTO administrax_documento_partida")
                        .append("clave_documento, clave_producto, cantidad, clave_unidad, clave_moneda, tipo_cambio,")
                        .append("importe_unitario, clave_impuesto, importe_impuesto, importe_total)") 
                        .append(" VALUES (").append(documento.getClave_documento()).append(",")
                        .append(ingrediente.getClave_producto()).append(",")
                        .append(cantidadADescontar * candidadRequeridaReceta).append(",")
                        .append(ingrediente.clave_unidad_salida).append(",")
                        .append(ingrediente.clave_moneda).append(",")
                        .append(ingrediente.tipo_cambio).append(",")
                        .append(ingrediente.precio).append(",")
                        .append(ingrediente.clave_impuesto_predeterminado).append(",")
                        .append(importeImpuesto).append(",")
                        .append((cantidadADescontar * candidadRequeridaReceta * ingrediente.precio) + importeImpuesto).append(")");

                    oDb.execute(q.toString());
                    
                    // Ahora descuenta el producto del inventario
                    q = new StringBuilder().append("UPDATE producto SET existencia=existencia-").append(cantidadADescontar).append(" WHERE clave_producto=").append(ingrediente.clave_producto);
                    resultadoXML.append(q.toString());
                    oDb.execute(q.toString());
                    }            
            } else if(this.getClave_tipo_producto()==3) {
            
            }
            
            //Emite notificación
            if (this.existencia<this.stock_minimo && this.clave_tipo_producto==1) {
                //Abre solicitud de compra
                //Calcula el folio que le toca al documento
                
                q = new StringBuilder().append("SELECT folio FROM administrax_tipo_documento WHERE clave_tipo_documento=9");
                rs = oDb.getRs(q.toString());

                if (rs.next()) {
                    folioSolicitud = rs.getInt("folio");
                    q = new StringBuilder().append("UPDATE administrax_tipo_documento SET folio=folio+1 WHERE clave_tipo_documento=9");
                    oDb.execute(q.toString());
                } else {
                    throw new Fallo("<error>No se encontró el folio para el tipo de documento indicado, verifique</error>");
                }
                
                //Inserta el pedido
                q = new StringBuilder().append("INSERT INTO administrax_documento ")
                        .append("(clave_empresa, fecha, clave_tipo_documento, folio_documento, clave_estatus,observaciones,clave_impuesto, clave_proveedor) ")
                        .append("VALUES (").append(",CURDATE(),9").append(folioSolicitud.toString())
                        .append(",'Solicitud generada automáticamente al alcanzarse el stock mínimo del producto',").append(this.getClave_impuesto_predeterminado()).append(",NULL)");

                resultadoXML.append("<insert_administrax_documento>").append(q).append("</insert_administrax_documento>");
                rs = oDb.execute(q.toString());
                if (rs.next()) {
                    claveDocumento = rs.getInt(1);
                }
                
                cantidadStock = this.getStock_maximo() - this.getExistencia();
                q = new StringBuilder().append("SELECT porcentaje FROM administrax_impuesto WHERE clave_impuesto=").append(this.getClave_impuesto_predeterminado());
                rs = oDb.getRs(q.toString());
                if (!rs.next())     {
                     throw new Fallo("No se encontró el impuesto para el producto ".concat(this.getProducto()));
                }

                importeImpuesto = cantidadStock * this.precio * (rs.getInt("porcentaje")==0?0:rs.getInt("porcentaje")/100);
                        
                //Inserta el detalle del pedido
                q = new StringBuilder().append("INSERT INTO administrax_documento_partida")
                        .append("(clave_documento, clave_producto, cantidad, clave_unidad, clave_moneda, tipo_cambio, importe_unitario, clave_impuesto, importe_impuesto, importe_total)")
                        .append(" VALUES(").append(claveDocumento).append(",")
                        .append(this.getClave_producto()).append(",").append(cantidadStock).append(",").append(this.getclave_unidad_entrada()).append(",").append(this.getClave_moneda())
                        .append(",1,").append(this.getPrecio() * cantidadStock).append(",").append(this.getClave_impuesto_predeterminado()).append(",").append(importeImpuesto)
                        .append((this.getPrecio() * cantidadStock) + importeImpuesto).append(")");

                resultadoXML.append("<insert_administrax_documento_partida>").append(q.toString()).append("</insert_administrax_documento_partida>");
                oDb.execute(q.toString());
                // Emite notificaciones en caso de que la existencia sea menor al stock minimo 
                q = new StringBuilder().append("SELECT clave_empleado FROM be_empleado WHERE clave_perfil=12");
                rs = oDb.getRs(q.toString());

                while (rs.next()) {
                    q = new StringBuilder().append("INSERT INTO be_actividad")
                            .append("(actividad,fecha_inicial,")
                            .append("clave_estatus, clave_empleado_solicitante, alertar_con_anticipacion, clave_empleado_asignado,")
                            .append("observacion, clave_forma, clave_registro)")
                            .append("VALUES ('")
                            .append("<a href=\"#\" class=\"gridlink\" id=\"-36-604-").append(claveDocumento).append("\">").append("El producto ").append(this.getProducto()).append(" ha alcanzado el nivel de stock mínimo. Resurta, por favor.',")
                            .append("CURDATE()")
                            .append("',0,1,0,").append(rs.getInt("clave_empleado")).append(",NULL,601,0")
                            .append(")");

                    resultadoXML.append("<insert_actividad>").append(q).append("</insert_actividad>");
                    oDb.execute(q.toString());                
                }
            }
            
            //oDb.execute("COMMIT");
            return resultadoXML.toString();
        } catch (Exception e) {
            //if (startedTransaction) {
            //    oDb.execute("ROLLBACK");
            //}

            //oDb.cierraConexion();
            //oDb = null;
            throw new Fallo(e.getMessage());

        }
    }

}
