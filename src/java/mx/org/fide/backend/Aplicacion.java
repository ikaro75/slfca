package mx.org.fide.backend;

import java.sql.ResultSet;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 * Clase que contiene las aplicaciones y se encarga de recuperar las subaplicaciones y las formas que la integran.
 * Se extiende de la clase Consulta y sobreescribe los métodos para insertar, actualizar y borrar
 */
public class Aplicacion extends Consulta {

    private Integer claveAplicacion;
    private String aplicacion;
    private int claveFormaPrincipal;
    private String formaPrincipal;
    private Integer claveAplicacionPadre;
    private Integer orden;
    private Integer claveCategoria;
    private Boolean prefiltroFormaPrincipal;
    /**
     * Constructor de la clase
     */
    public Aplicacion() {

    }
    
/**
     * Constructor de la clase actividad
     * @param claveActividad Clave de la actividad.
     * @param cx Conexión a la base de datos.
     */
    public Aplicacion(Integer claveAplicacion, Usuario usuario) {
        super.setUsuario(usuario);
        this.claveAplicacion = claveAplicacion;

        try {
           ResultSet rs = super.getUsuario().getCx().getRs("select *,(select forma from be_forma where clave_forma=be_aplicacion.clave_forma_principal) as  forma_principal, (select prefiltro from be_forma where clave_forma=be_aplicacion.clave_forma_principal) as prefiltro_forma_principal from be_aplicacion where clave_aplicacion=".concat(claveAplicacion.toString()));
           
           if (rs.next()) {
               this.claveAplicacion=rs.getInt("clave_aplicacion");
               this.aplicacion=rs.getString("aplicacion");
               this.claveAplicacionPadre=rs.getInt("clave_aplicacion_padre");
               this.claveCategoria=rs.getInt("clave_categoria");
               this.claveFormaPrincipal = rs.getInt("clave_forma_principal");
               this.formaPrincipal = rs.getString("forma_principal");
               this.claveCategoria =rs.getInt("clave_categoria");
               this.prefiltroFormaPrincipal = rs.getBoolean("prefiltro_forma_principal");
                                                       
           } else {
               throw new Fallo ("Error al recuperar la aplicación especificada");
           }

        } catch(Exception e ){ 
            
        }
    }
    /**
     * Constructor de la clase que se apoya en una consulta definida y una conexión
     * @param c     Instancia de clase <code>com.administrax.modelo.Consulta</code>
     * @param cx    instancia de clase <code>com.administrax.modelo.Conexion</code>
     */
    public Aplicacion(Consulta c) {
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
        
        if (super.getPk()!=null)
            this.claveAplicacion = Integer.parseInt(super.getPk());
        
        this.aplicacion = super.getCampos().get("aplicacion").getValor();
        
        if (super.getCampos().get("clave_forma_principal")!=null)
            this.claveFormaPrincipal = Integer.parseInt(super.getCampos().get("clave_forma_principal").getValor().toString());

       if (super.getCampos().get("clave_categoria")!=null)
            this.claveCategoria = Integer.parseInt(super.getCampos().get("clave_categoria").getValor().toString());
       
       if (super.getCampos().get("clave_aplicacion_padre")!=null)
           if (!super.getCampos().get("clave_aplicacion_padre").getValor().equals(""))
                this.claveCategoria = Integer.parseInt(super.getCampos().get("clave_aplicacion_padre").getValor().toString());

    }

    /**
     * Recupera la forma princicpal de la aplicación
     * @return Clave de la forma principal de la aplicación
     */
    public int getClaveFormaPrincipal() {
        return claveFormaPrincipal;
    }

    /**
     * Establece la forma principal de la aplicación
     * @param claveFormaPrincipal   Clave de la forma principal de la aplicación
     */
    public void setClaveFormaPrincipal(int claveFormaPrincipal) {
        this.claveFormaPrincipal = claveFormaPrincipal;
    }

    /**
     * Recupera el nombre de la forma principal de la aplicación
     * @return Nombre de la forma principal de la aplicación
     */
    public String getFormaPrincipal() {
        return formaPrincipal;
    }

    /**
     * Establece nombre de la forma principal de aplicación 
     * @param formaPrincipal    Nombre de la forma principal de la aplicación
     */
    public void setFormaPrincipal(String formaPrincipal) {
        this.formaPrincipal = formaPrincipal;
    }

    public Boolean getPrefiltroFormaPrincipal() {
        return prefiltroFormaPrincipal;
    }

    public void setPrefiltroFormaPrincipal(Boolean prefiltroFormaPrincipal) {
        this.prefiltroFormaPrincipal = prefiltroFormaPrincipal;
    }

    /**
     * Recupera nombre de la aplicación 
     * @return nombre de la aplicación
     */
    public String getAplicacion() {
        return aplicacion;
    }

    /**
     * Establece nombre de la aplicación 
     * @param aplicacion    Nombre de la aplicación
     */
    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    /**
     * Recupera clave de la aplicación 
     * @return  Clave de la aplicación 
     */
    @Override
     public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    /**
     *  Establece clave de la aplicación
     * @param claveAplicacion   Clave de la aplicación
     */
    @Override
    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }

    /**
     * Recupera la aplicación padre
     * @return Clave aplicación padre
     */
    public Integer getClaveAplicacionPadre() {
        return claveAplicacionPadre;
    }

    /**
     * Establece la aplicación padre
     * @param claveAplicacionPadre Clave aplicación padre
     */
    public void setClaveAplicacionPadre(Integer claveAplicacionPadre) {
        this.claveAplicacionPadre = claveAplicacionPadre;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) { 
        this.orden = orden;
    }
    /**
     * Recupera clave de la categoria a la que pertenece la aplicación, cada tab (apliaciones y comunidad) es una categoria
     * @return Clave de la categoria de la aplicación
     */
    public int getClaveCategoria() {
        return claveCategoria;
    }

    /**
     * Establece clave de la categoria a la que pertenece la aplicación, cada tab (apliaciones y comunidad) es una categoria
     * @param claveCategoria Clave de la categoria de la aplicación
     */
    public void setClaveCategoria(int claveCategoria) {
        this.claveCategoria = claveCategoria;
    }

    /**
     * Inserta en la base de datos de destino una nueva aplicación incluyendo la relación del perfil del administrador 
     * @param claveEmpleado Clave del empleado que está insertando la aplicación
     * @param ip            Dirección IP de la computadora desde donde se hace la inserción
     * @param browser       Descripción del navegador desde donde se hace la inserción
     * @param forma         Clave de la forma asignada a la tabla aplicación
     * @param cx            Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @return              Código XML que indica la instrucción ejecutada para insertar la aplicación, la llave primaria y mensajes de error y warning si estos ocurren
     * @throws Fallo        Objeto <code>com.administrax.modelo.Fallo</code> Si ocurre un error al hacer operaciones en la base de datos
     */
    public String insert() throws Fallo {
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = super.getUsuario().getCx();

        resultado.append(super.insert(true));

        if (resultado.toString().split("<error>").length > 1) {
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }
        
        //Se debe recuperar la llave primaria de la aplicación
        this.claveAplicacion=Integer.parseInt(resultado.toString().split("<pk>")[1].replace("</pk>",""));
        
        String s = "insert into be_perfil_aplicacion (clave_perfil, clave_aplicacion, activo) values(1,".concat(String.valueOf(this.getClaveAplicacion())).concat(", 1)");
        try {
            oDb.execute(s.toString());
        } catch (Exception e) {
            resultado.append("<warning><![CDATA[No se insertaron permisos para el administrador: ").append(e.getMessage()).append("]]></warning>");
        }

        return resultado.toString();
    }

    /**
     * Actualiza registro de aplicación en la base de datos 
     * @param claveEmpleado Clave del empleado que ejecuta la actualización
     * @param ip            Dirección IP de la computadora desde donde se hace la actualización
     * @param browser       Descripción del navegador desde donde se hace la actualización
     * @param forma         Clave de la forma asignada a la tabla aplicación
     * @param cx            Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @return              Código XML que indica la instrucción ejecutada para actualizar la aplicación, la llave primaria y mensajes de error y warning si estos ocurren
     * @throws Fallo        Objeto <code>com.administrax.modelo.Fallo</code> Si ocurre un error al hacer operaciones en la base de datos
     */
    public String update() throws Fallo {
        StringBuilder resultado = new StringBuilder();
        return resultado.append(super.update(true)).toString();
    }

    /**
     * Elimina registro de aplicación en la base de datos
     * @param claveEmpleado Clave del empleado que ejecuta la actualización
     * @param ip            Dirección IP de la computadora desde donde se hace la eliminación
     * @param browser       Descripción del navegador desde donde se hace la eliminación
     * @param forma         Clave de la forma asignada a la tabla aplicación
     * @param cx            Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @return              Código XML que indica la instrucción ejecutada para eliminar la aplicación, la llave primaria y mensajes de error y warning si estos ocurren
     * @throws Fallo        Objeto <code>com.administrax.modelo.Fallo</code> Si ocurre un error al hacer operaciones en la base de datos
     */
    public String delete(Usuario usuario) throws Fallo {
        super.setUsuario(usuario);
        StringBuilder resultado = new StringBuilder();
        StringBuilder s= new StringBuilder("");
        Conexion oDb =this.getUsuario().getCx();
         
        s.append("DELETE FROM be_perfil_aplicacion WHERE clave_aplicacion=").append(this.getClaveAplicacion());

        try {
            oDb.execute(s.toString());
        } catch (Exception e) {
            resultado.append("<warning><![CDATA[No se eliminaron los permisos de la aplicacion: ").append(e.getMessage()).append("]]></warning>");
        }
        
        super.setTabla("be_aplicacion");
        super.setLlavePrimaria("clave_aplicacion");
        super.setPk(String.valueOf(this.getClaveAplicacion()));
        
        resultado.append(super.delete(true,usuario));

        if (resultado.toString().split("<error>").length > 1) {
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }

        return resultado.toString();
    }

    /**
     * Duplica el registro de la aplicación haciendo uso de su ancestro
     * @param claveEmpleado Clave del empleado que ejecuta la actualización
     * @param ip            Dirección IP de la computadora desde donde se hace la duplicación 
     * @param browser       Descripción del navegador desde donde se hace la duplicación
     * @param forma         Clave de la forma asignada a la tabla aplicación
     * @param cx            Objeto de tipo <code>com.administrax.modelo.Conexión</code> con los detalles de la base de datos
     * @return              Código XML que indica la instrucción ejecutada para eliminar la aplicación, la llave primaria y mensajes de error y warning si estos ocurren
     * @throws Fallo         Objeto <code>com.administrax.modelo.Fallo</code> Si ocurre un error al hacer operaciones en la base de datos
     */
    @Override
    public String duplicate() throws Fallo {
        StringBuilder resultadoXML = new StringBuilder();
        return resultadoXML.append(super.duplicate()).toString();
    }

    @Override
    public String toString() {
        return "Aplicacion{" + "claveAplicacion=" + claveAplicacion + ", aplicacion=" + aplicacion + ", claveFormaPrincipal=" + claveFormaPrincipal + "}";
    }
}
