/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.backend;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import mx.org.fide.agenda.Actividad; 
import mx.org.fide.mail.Mail;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 * Notas asociadas a las formas como comentarios
 */
public class Nota  extends Consulta {
    private Integer claveNota;
    private Integer claveForma;
    private Integer claveRegistro;
    private Integer claveEmpleado;
    private String titulo;
    private String mensaje;
    private String fechaNota;
    private String foto;
    private String nombre;

    /**
     * Recupera clave de usuario que insertó nota
     * @return Clave de usuario
     */
    public int getClaveEmpleado() {
        return claveEmpleado;
    }

    /**
     * Establece clave de usuario que insertó nota
     * @param claveEmpleado Clave de usuario que insertó nota
     */
    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    /**
     * Recupera clave de la forma con la que se asoció la nota
     * @return Clave de la forma con la que se asoció la nota
     */
    public Integer getClaveForma() {
        return claveForma;
    }

    /**
     * Establece clave de la forma con la que se asoció la nota
     * @param claveForma Clave de la forma con la que se asoció la nota
     */
    @Override
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Recupera llave primaria de la nota
     * @return Clave de la nota
     */
    public Integer getClaveNota() {
        return claveNota;
    }

    /**
     * Establece llave primaria de la nota 
     * @param claveNota Clave de la nota
     */
    public void setClaveNota(Integer claveNota) {
        this.claveNota = claveNota;
    }

    /**
     * Recupera llave primaria del registro de la forma a la cual se asoció la nota 
     * @return Llave primaria del registro de la forma asociada a la nota
     */
    public int getClaveRegistro() {
        return claveRegistro;
    }

    /**
     * Establece la llave primaria del registro de la forma a la cual se asoció la nota 
     * @param claveRegistro Llave primaria del registro de la forma asociada a la nota
     */
    public void setClaveRegistro(Integer claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

    /**
     * Recupera la fecha y hora de la nota
     * @return fecha y hora de la nota
     */
    public String getFechaNota() {
        return fechaNota;
    }

    /**
     * Establece la fecha y hora de la nota
     * @param fechaNota fecha y hora de la nota
     */
    public void setFechaNota(String fechaNota) {
        this.fechaNota = fechaNota;
    }

    /**
     * Recupera el cuerpo del mensaje de la nota
     * @return Cuerpo del mensaje de la nota
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el cuerpo del mensaje de la nota
     * @param mensaje Cuerpo del mensaje de la nota
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Recupera el título de la nota
     * @return  Título de la nota
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título de la nota
     * @param titulo  Título de la nota
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Recupera la foto del usuario que publica la nota
     * @return Ruta relativa del archivo del avatar del usuario
     */
    public String getFoto() {
        return foto;
    }

    /**
     * Establece la foto del usuario que publica la nota
     * @param foto Ruta relativa del archivo del avatar del usuario
     */
    public void setFoto(String foto) {
        this.foto = foto;
    }

    /**
     * Recupera el nombre del usuario que publica la nota
     * @return Nombre del usuario que publica la nota
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario que publica la nota
     * @param nombre Nombre del usuario que publica la nota
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Constructor del objeto con parámetros
     * @param claveNota     Llave primaria de la nota
     * @param claveForma    Clave de la forma asociada
     * @param claveRegistro Llave primaria del registro relacionado a la forma
     * @param claveEmpleado Clave del empleado que publicó la nota
     * @param titulo        Título de la nota    
     * @param mensaje       Cuerpo del mensaje de la nota 
     * @param fechaNota     Fecha y hora de creación de la nota
     * @param foto          Ruta relativa del archivo relacionao al avatar del usuario que publicó la nota
     * @param nombre        Nombre del usuario que publicó la nota
     */
    public Nota(Integer claveNota, Integer claveForma, Integer claveRegistro, Integer claveEmpleado, String titulo, String mensaje, String fechaNota, String foto, String nombre) {
        this.claveNota = claveNota;
        this.claveForma = claveForma;
        this.claveRegistro = claveRegistro;
        this.claveEmpleado = claveEmpleado;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaNota = fechaNota;
        this.foto = foto;
        this.nombre = nombre; 
    }
    
/**
     * Constructor de la clase ActividadContacto
     * @param c Consulta a ejecutar SQL
     * @param cx Conexiòn a la base de datos
     * @throws Fallo 
     */
    public Nota(Consulta c) throws Fallo {
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            if (c.getCampos().get("clave_nota").getValor() != null) {
                this.claveNota = Integer.parseInt(c.getCampos().get("clave_nota").getValor());
            }

            if (c.getCampos().get("clave_forma").getValor() != null && !c.getAccion().equals("delete")) {
                this.claveForma = Integer.parseInt(c.getCampos().get("clave_forma").getValor());
            } else {
                throw new Fallo("No se especificó la clave de la forma, verifique");
            }

            if (c.getCampos().get("clave_registro").getValor() != null && !c.getAccion().equals("delete")) {
                this.claveRegistro = Integer.parseInt(c.getCampos().get("clave_registro").getValor());
            } else {
                throw new Fallo("No se especificó la clave del registro, verifique");
            }
            
            if (c.getCampos().get("clave_empleado").getValor() != null && !c.getAccion().equals("delete")) {
                this.claveEmpleado = Integer.parseInt(c.getCampos().get("clave_empleado").getValor());
            } else {
                throw new Fallo("No se especificó la clave del empleado, verifique");
            }            

            if (c.getCampos().get("titulo").getValor() != null && !c.getAccion().equals("delete")) {
                this.titulo = c.getCampos().get("titulo").getValor();
            }

            if (c.getCampos().get("mensaje").getValor() != null && !c.getAccion().equals("delete")) {
                this.mensaje = c.getCampos().get("mensaje").getValor();
            } else {
                throw new Fallo("No se especificó el mensaje, verifique");
            } 
            
            if (c.getCampos().get("fecha_nota").getValor() != null && !c.getAccion().equals("delete")) {
                this.fechaNota = c.getCampos().get("fecha_nota").getValor();
            } else {
                throw new Fallo("No se especificó la fecha, verifique");
            }             
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    /**
     * Ejecuta <code>INSERT</code> relacionado con la consulta, previamente deben haber sido llenados y validados los campos, 
     * cuenta con la opción para no ejecutar la sentencia sino solo generarla y devolverla, en caso de que se ejecute, también 
     * se registra la operación en la bitácora
     * @param claveEmpleado Clave del empleado que efectua el INSERT
     * @param ip            Dirección IP del empleado que efectua el INSERT
     * @param browser       Navegador con el que se efectua el INSERT
     * @param forma         Clave de la forma asociada a la consulta
     * @param cx            Objeto de tipo <code>mx.edu.ilce.modelo.Conexión</code> con los detalles de la base de datos
     * @return              Codigo XML con la sentencia SQL de la operación efectuada; en caso de que se ejecuté también devolverá la llave primaria, así como la descripción de errores y warnings
     * @throws Fallo        si ocurre un error relacionado a la base de datos
     */
    public String insert(Usuario destinatario, String entidad, Boolean propaga) throws Fallo {

        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        String resultadoInsert="";
        StringBuilder q = new StringBuilder();
        String notificacion="";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Conexion oDb = super.getUsuario().getCx();
        Usuario u = super.getUsuario();
        Usuario c = new Usuario();
        Mail mail = new Mail(destinatario);
        c.setCx(destinatario.getCx());
        String s = "";
        
        try {
            //Inserta la nota
            q.append(super.insert(true));
            resultadoXML.append(q.toString());

            if (resultadoXML.toString().contains("<error>")) {
                return resultadoXML.toString();
            }

            //La pregunta es: ¿a quién se le deben enviar las notificaciones? ¿Cuales son los usuario asociados a esta nota? 
            //La respuesta es: 
  
            
            //Tambien se deben insertar notas para las actividades compartidas
            notificacion = u.getNombre().concat(" ").concat(u.getApellido_paterno()).concat(" ha publicado un comentario ").concat((!entidad.equals("")?" en ".concat(entidad):"" )).concat(":<br /><br /><strong>").concat(this.mensaje).concat("</strong>");
            
            if (this.claveForma== 101 && propaga) {
                // Se deben insertar en los registros de todas las actividades relacionadas
                Actividad a = new Actividad(this.claveRegistro, u);
                if (a.getClaveActividadOrigen()==null || a.getClaveActividadOrigen()==0) 
                    s = "select clave_actividad, clave_empleado_asignado from be_actividad where clave_actividad_origen=".concat(String.valueOf(this.claveRegistro));
                else 
                    s = "select clave_actividad, clave_empleado_asignado  from be_actividad where clave_actividad in (select clave_actividad_origen from be_actividad where clave_actividad=".concat(String.valueOf(this.claveRegistro)).concat(")")
                        .concat(" union ")
                        .concat(" select clave_actividad, clave_empleado_asignado from be_actividad where clave_actividad <> ".concat(String.valueOf(this.claveRegistro)).concat(" and clave_actividad_origen in (select clave_actividad from be_actividad where clave_actividad in (select clave_actividad_origen from be_actividad where clave_actividad=".concat(String.valueOf(this.claveRegistro)).concat("))")));
                
                rs = oDb.getRs(s);
                
                while (rs.next()) {
                     this.getCampos().get("clave_registro").setValor(rs.getString("clave_actividad"));
                     this.claveRegistro =rs.getInt("clave_actividad");
                     q.append(super.insert(true));
                     
                     c.setClave(rs.getInt("clave_empleado_asignado"));

                     if (c.getEmail()!=null) {
                        mail.sendEmail("noresponder@fide.org.mx", c.getEmail(),
                            u.getNombre().concat(" ").concat(u.getApellido_paterno()).concat(" ha publicado un comentario").concat((!entidad.equals("")?" en ".concat(entidad):"" )),
                            notificacion, null);
                        resultadoXML.append("<notificacion><![CDATA[Se envió la notificacion '").append(notificacion).append("]]></notificacion>");
                     } else {
                        resultadoXML.append("<warning><![CDATA[El usuario ").append(c.getNombre()).append(" ").append(c.getApellido_paterno()).append(" no tiene un correo registrado y no fue posible notificarsele.").append("]]></warning>");
                     }
                }

                resultadoXML.append(q);
               
            }   
            else {
                if (destinatario.getClave()!=this.claveEmpleado) {
                    if (destinatario.getEmail()!=null) {
                        mail.sendEmail("intranet@ilce.edu.mx", destinatario.getEmail(),
                            super.getUsuario().getNombre().concat(" ").concat(super.getUsuario().getApellido_paterno()).concat(" ha publicado un comentario").concat((!entidad.equals("")?" en ".concat(entidad):"" )),
                            notificacion, null);                            
                        resultadoXML.append("<notificacion><![CDATA[Se envió la notificacion '").append(notificacion).append("' a ").append(destinatario.getNombre()).append(" ").append(destinatario.getApellido_paterno()).append("]]></notificacion>");
                    }  else {
                        resultadoXML.append("<warning><![CDATA[El usuario ").append(destinatario.getNombre()).append(" ").append(destinatario.getApellido_paterno()).append(" no tiene un correo registrado y no fue posible notificarsele.").append("]]></warning>");
                    }
                }                 
            }
        } catch (Exception e) {
            throw new Fallo(resultadoXML.toString().concat("<detalles>").concat(e.getMessage()).concat("</detalles>"));
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }
        return resultadoXML.toString();
    }    
    
}
