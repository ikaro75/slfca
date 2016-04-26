package mx.org.fide.agenda;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import mx.org.fide.mail.Mail;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 * Clase que contiene las actividades de la agenda y se encarga de recuperarla y agregarlas.
 * @author Daniel
 */
public class CategoriaActividad extends Consulta{
    private Integer claveCategoria;
    private String categoria;

    public Integer getClaveCategoria() {
        return claveCategoria;
    }

    public void setClaveCategoria(Integer claveCategoria) {
        this.claveCategoria = claveCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

  
    /**
     * Constructor de la clase actividad
     * @param claveActividad Clave de la actividad.
     * @param cx Conexión a la base de datos.
     */
    public CategoriaActividad(Integer claveCategoria, Usuario usuario) {
        super.setUsuario(usuario);
        this.claveCategoria = claveCategoria;        
        try {
           ResultSet rs = super.getUsuario().getCx().getRs("select * from be_categoria_actividad where clave_categoria=".concat(claveCategoria.toString()));
           
           if (rs.next()) {
               this.claveCategoria=rs.getInt("clave_categoria");
               this.categoria =rs.getString("categoria");
                                    
           } else {
               throw new Fallo ("Error al recuperar categoría especificada");
           }

        } catch(Exception e ){ 
            
        }
    }
    
     /**
     * Constructor de la clave actividad que envia la consulta y la conexón
     * @param c Parametro donde se envía la consulta
     * @param cx Parametro para la conexión
     * @throws Fallo Envía el fallo o error
     */
    public CategoriaActividad(Consulta c) throws Fallo {
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

        StringBuilder q = new StringBuilder();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        
        try {;
        
            if (c.getCampos().get("clave_categoria").getValor() != null) {
                this.claveCategoria = Integer.parseInt(c.getCampos().get("clave_categoria").getValor());
            } else {
                if (c.getPk()!=null && !c.getPk().equals("")) {
                    this.claveCategoria = Integer.parseInt(c.getPk());
                } else {
                    throw new Fallo("No se especificó la clave de la categoría, verifique");
                }    
            }
    
            if (c.getCampos().get("categoria").getValor() != null && !c.getAccion().equals("delete") && !c.getCampos().get("actividad").getValor().equals("")) {
                this.categoria = c.getCampos().get("categoria").getValor();
            } else {
                if (c.getAccion().equals("update")) {
                    if (c.getCampos().get("categoria").getValorOriginal()!=null) {
                        this.categoria = c.getCampos().get("categoria").getValorOriginal();
                    }  else {
                        throw new Fallo("No se especificó la descripción de la categoria, verifique");
                    }                        
                } else {
                    throw new Fallo("No se especificó la descripción de la categoria, verifique");
                }    
            }

            

        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }
     
 /**
     * Inserta registro de la actividad los permisos del administrador,
     * las consultas relacionadas a la forma para mostrar el grid, crear el formulario de inserción y actualización,
     * y presentar la información de actividades recientes, así como el diccionario de datos de la forma para
     * el perfil de administrador
     * @param claveEmpleado
     * @param ip
     * @param browser
     * @param forma
     * @param cx
     * @return
     * @throws Fallo
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

       this.claveCategoria = Integer.parseInt(resultado.toString().split("<pk>")[1].substring(0, resultado.toString().split("<pk>")[1].indexOf("</pk>")));
       return resultado.toString();
    }

    /**
     * Actualiza registro de forma en la base de datos
     * @param claveEmpleado Clave del usuario que realiza la actualización
     * @param ip            IP del usuario que realiza la actualización
     * @param browser       Navegador con el que se realiza la actualización
     * @param forma         Clave de la forma que se actualiza
     * @param cx            Objeto <code>com.administrax.modelo.Conexion</code> que contiene los datos de la conexión a la db     
     * @return              Codigo XML indicando la sentencia ejecutada para actualizar la forma, la llave primaria así como cualquier error o advertencia
     * @throws Fallo        Si ocurre algún error al momento de interactuar con la base de datos
     */
    public String update() throws Fallo {
        String q;
        StringBuilder resultado = new StringBuilder();
        String asunto = "";
        String notificacion="";
        Conexion oDb = super.getUsuario().getCx();
        Conexion cxContacto = new Conexion();
        Conexion cxUsuario = new Conexion();
        Boolean hayCambios = false;
        ResultSet rs;
        Mail mail = new Mail(super.getUsuario());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd//MM/yyyy HH:mm");
        Boolean soyInvitado=true;
        //Deben de actualizarse los registros asociados
                
        try {
            
            resultado.append(super.update(true));        
        
        } catch(Exception e) {
            resultado.append("<error><![CDATA[").append(e.getMessage()).append("]]></error>");
            throw new Fallo(resultado.toString());
        } finally {
            oDb.cierraConexion();
            return resultado.append(super.update(true)).toString();
        }    
    }

    /**
     * Elimina registros de forma de la base de datos
     * @param claveEmpleado Clave del usuario que realiza la actualización
     * @param ip            IP del usuario que realiza la actualización
     * @param browser       Navegador con el que se realiza la actualización
     * @param forma         Clave de la forma que se actualiza
     * @param cx            Objeto <code>com.administrax.modelo.Conexion</code> que contiene los datos de la conexión a la db     
     * @return              Codigo XML indicando la sentencia ejecutada para actualizar la forma, la llave primaria así como cualquier error o advertencia
     * @throws Fallo        Si ocurre algún error al momento de interactuar con la base de datos
     */
    public String delete() throws Fallo {
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = super.getUsuario().getCx();
        super.setTabla("be_categoria_actividad");
        super.setLlavePrimaria("clave_categoria");
        super.setPk(String.valueOf(this.claveCategoria));

        resultado.append(super.delete(true,super.getUsuario()));

        if (resultado.toString().split("<error>").length > 1) {
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }

        return resultado.toString();
    }    
    
    
    
    
}

