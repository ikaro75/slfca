package mx.org.fide.agenda;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import mx.org.fide.mail.Mail;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 * Clase que contiene las actividades de la agenda y se encarga de recuperarla y agregarlas.
 * @author Daniel
 */
public class Actividad extends Consulta{
    private Integer claveActividad;
    private String actividad;
    private Integer claveCategoria;
    private Date fechaInicial;
    private Date fechaFinal;
    private String lugar;
    private Integer claveFlujoForma;
    private Integer claveProyecto;
    private Integer claveEstatus;
    private Integer claveEmpleadoSolicitante;
    private Integer alertarConAnticipacion;
    private Integer claveEmpleadoAsignado;
    private String observacion;
    private Integer claveForma;
    private Integer claveRegistro;
    private Integer claveActividadOrigen;

    /**
     * Recupera la actividad de la agenda.
     * @return Clave de actividad.
     */
    public Integer getClaveActividad() {
        return claveActividad;
    }

    /**
     * Establece la clave de una actividad de la agenda.
     * @param claveActividad Clave de la actividad.
     */
    public void setClaveActividad(Integer claveActividad) {
        this.claveActividad = claveActividad;
    }

    /**
     * Recupera la actividad de la agenda.
     * @return Actividad de la agenda.
     */
    public String getActividad() {
        return actividad;
    }

    /**
     * Establece la actividad de la agenda.
     * @param actividad Actividad de la agenda.
     */
    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    /**
     * Recupera la categoria de la actividad de la agenda.
     * @return Clave de la categoria.
     */
    public Integer getClaveCategoria() {
        return claveCategoria;
    }

     /**
     * Establece la categoria de la actividad de la agenda.
     * @param claveCategoria Clave de la categoria de la agenda.
     */
    public void setClaveCategoria(Integer claveCategoria) {
        this.claveCategoria = claveCategoria;
    }

    /**
     * Recupera la fecha inicial de la actividad de la agenda.
     * @return Fecha inicial de la actividad de la agenda.
     */
    public Date getFechaInicial() {
        return fechaInicial;
    }

    /**
     * Establece la fecha inicial de la actividad de la agenda.
     * @param fechaInicial Fecha inicial de la actividad de la agenda.
     */
    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    /**
     * Recupera la fecha final de la actividad de la agenda.
     * @return Fecha final de la actividad de la agenda.
     */
    public Date getFechaFinal() {
        return fechaFinal;
    }

    /**
     * Establece la fecha final de la actividad de la agenda.
     * @param fechaFinal Fecha final de la actividad de la agenda.
     */
    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    /**
     * Recupera el lugar de la actividad.
     * @return Lugar de la actividad.
     */
    public String getLugar() {
        return lugar;
    }

    /**
     * Establece el lugar de la actividad de la agenda.
     * @param lugar Lugar de la actividad de la agenda.
     */
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

   /**
     * Recupera el flujo de la forma vinculada a la actividad.
     * @return Clave del flujo de la forma.
     */
    public Integer getClaveFlujoForma() {
        return claveFlujoForma;
    }

    /**
     * Establece el flujo de la forma vinculada a la actividad.
     * @param claveFlujoForma Clave del flujo de la forma vinculada a la actividad.
     */
    public void setClaveFlujoForma(Integer claveFlujoForma) {
        this.claveFlujoForma = claveFlujoForma;
    }

    /**
     * Recupera el proyecto vinculado a la actividad de la agenda.
     * @return Clave de proyecto vinculado a la actividad de la agenda.
     */
    public Integer getClaveProyecto() {
        return claveProyecto;
    }

    /**
     * Establece el proyecto vinculado a la actividad de la agenda.
     * @param claveProyecto Clave de proyecto vinculado a la actividad de la agenda.
     */
    public void setClaveProyecto(Integer claveProyecto) {
        this.claveProyecto = claveProyecto;
    }

    /**
     * Recupera el estatus de la actividad de la agenda.
     * @return Clave del estatus de la actividad.
     */
    public Integer getClaveEstatus() {
        return claveEstatus;
    }

    /**
     * Establece el estatus de la actividad de la agenda.
     * @param claveEstatus Clave del estatus de la actividad.
     */
    public void setClaveEstatus(Integer claveEstatus) {
        this.claveEstatus = claveEstatus;
    }

    /**
     * Recupera al empleado solicitante de la actividad.
     * @return Clave del empleado solicitante.
     */
    public Integer getClaveEmpleadoSolicitante() {
        return claveEmpleadoSolicitante;
    }

    /**
     * Establece al empleado solicitante de la actividad.
     * @param claveEmpleadoSolicitante Clave del empleado solicitante.
     */
    public void setClaveEmpleadoSolicitante(Integer claveEmpleadoSolicitante) {
        this.claveEmpleadoSolicitante = claveEmpleadoSolicitante;
    }

    /**
     * Recupera la alerta de anticipación de la actividad.
     * @return Alerta de anticipación de la actividad.
     */
    public Integer getAlertarConAnticipacion() {
        return alertarConAnticipacion;
    }

    /**
     * Establece la alerta de anticipación de la actividad.
     * @param alertarConAnticipacion Alerta de anticipación de la actividad.
     */
    public void setAlertarConAnticipacion(Integer alertarConAnticipacion) {
        this.alertarConAnticipacion = alertarConAnticipacion;
    }

    /**
     * Recupera al empleado que se le asigno la actividad.
     * @return Clave del empleado al que se le asigna la actividad.
     */
    public Integer getClaveEmpleadoAsignado() {
        return claveEmpleadoAsignado;
    }

    /**
     * Establece el empleado que se le asignara la actividad.
     * @param claveEmpleadoAsignado Clave del empleado al que se le asignara la actividad.
     */
    public void setClaveEmpleadoAsignado(Integer claveEmpleadoAsignado) {
        this.claveEmpleadoAsignado = claveEmpleadoAsignado;
    }

    /**
     * Recupera la observación de la actividad.
     * @return Observación de la actividad.
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * Establece la observación de la actividad.
     * @param observacion Observación de la actividad.
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    /**
     * Recupera la forma de la actividad.
     * @return Clave de la forma de la actividad.
     */
    @Override
    public Integer getClaveForma() {
        return claveForma;
    }

    /**
     * Establece la forma de la actividad.
     * @param claveForma Clave de la forma de la actividad.
     */
    public void setClaveForma(Integer claveForma) {
        this.claveForma = claveForma;
    }

    /**
     * Recupera el registro de la actividad.
     * @return Clave del registro de la actividad.
     */
    public Integer getClaveRegistro() {
        return claveRegistro;
    }

    /**
     * Establece el registro de la actividad.
     * @param claveRegistro Clave del registro de la actividad.
     */
    public void setClaveRegistro(Integer claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

    public Integer getClaveActividadOrigen() {
        return claveActividadOrigen;
    }

    public void setClaveActividadOrigen(Integer claveActividadOrigen) {
        this.claveActividadOrigen = claveActividadOrigen;
    }
  
    /**
     * Constructor de la clase actividad
     * @param claveActividad Clave de la actividad.
     * @param cx Conexión a la base de datos.
     */
    public Actividad(Integer claveActividad, Usuario usuario) {
        super.setUsuario(usuario);
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(),super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(),super.getUsuario().getCx().getDbType());
        this.claveActividad = claveActividad;
        try {
           ResultSet rs = oDb.getRs("select * from be_actividad where clave_actividad=".concat(claveActividad.toString()));
           
           if (rs.next()) {
               this.claveActividad=rs.getInt("clave_actividad");
               this.actividad=rs.getString("actividad");
               this.claveCategoria=rs.getInt("clave_categoria");
               this.fechaInicial=rs.getDate("fecha_inicial");
               this.fechaFinal=rs.getDate("fecha_final");
               this.lugar=rs.getString("lugar");
               this.claveFlujoForma= rs.getInt("clave_flujo_forma");
               this.claveProyecto= rs.getInt("clave_proyecto");
               this.claveEstatus= rs.getInt("clave_estatus");
               this.claveEmpleadoSolicitante= rs.getInt("clave_empleado_solicitante");
               this.alertarConAnticipacion= rs.getInt("alertar_con_anticipacion");
               this.claveEmpleadoAsignado= rs.getInt("clave_empleado_asignado");
               this.observacion=rs.getString("observacion");
               this.claveForma=rs.getInt("clave_forma");
               this.claveRegistro=rs.getInt("clave_registro");
               this.claveActividadOrigen=rs.getInt("clave_actividad_origen");            
                                                       
           } else {
               rs.close();
               throw new Fallo ("Error al recuperar actividad especificada");
           }
           
           rs.close();
        } catch(Exception e ){ 
            
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }
    }
    
     /**
     * Constructor de la clave actividad que envia la consulta y la conexón
     * @param c Parametro donde se envía la consulta
     * @param cx Parametro para la conexión
     * @throws Fallo Envía el fallo o error
     */
    public Actividad(Consulta c) throws Fallo {
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
        
        try {
        
            if (c.getCampos().get("clave_actividad").getValor() != null) {
                this.claveActividad = Integer.parseInt(c.getCampos().get("clave_actividad").getValor());
            } else {
                if (c.getPk()!=null && !c.getPk().equals("")) {
                    this.claveActividad = Integer.parseInt(c.getPk());
                } else {
                    throw new Fallo("No se especificó la clave de la actividad, verifique");
                }    
            }
    
            if (c.getCampos().get("actividad").getValor() != null && !c.getAccion().equals("delete") && !c.getCampos().get("actividad").getValor().equals("")) {
                this.actividad = c.getCampos().get("actividad").getValor();
            } else {
                if (c.getAccion().equals("update")) {
                    if (c.getCampos().get("actividad").getValorOriginal()!=null) {
                        this.actividad = c.getCampos().get("actividad").getValorOriginal();
                    }  else {
                        throw new Fallo("No se especificó la descripción de la actividad, verifique");
                    }                        
                } else {
                    throw new Fallo("No se especificó la descripción de la actividad, verifique");
                }    
            }

            if (c.getCampos().get("clave_categoria").getValor() != null) {
                this.claveCategoria = Integer.parseInt(c.getCampos().get("clave_categoria").getValor());
            } else {
                if (!c.getAccion().equals("update")) {
                    throw new Fallo("No se especificó la clave de la categoría, verifique");
                }    
            }

            if (c.getCampos().get("fecha_inicial").getValor() != null) {
                this.fechaInicial = formatter.parse(c.getCampos().get("fecha_inicial").getValor());
            } else {
                throw new Fallo("No se especificó la fecha inicial de la actividad, verifique");
            }
            
            if (c.getCampos().get("fecha_final").getValor() != null) {
                if (!c.getCampos().get("fecha_final").getValor().equals("")) {
                    this.fechaFinal =(Date) formatter.parse(c.getCampos().get("fecha_final").getValor());
                }     
            }
             
            if (c.getCampos().get("lugar").getValor() != null ) {
                this.lugar = c.getCampos().get("lugar").getValor();
            } 
            
            if (c.getCampos().get("clave_flujo_forma") != null) {
                if (c.getCampos().get("clave_flujo_forma").getValor() != null) {
                    this.claveFlujoForma = Integer.parseInt(c.getCampos().get("clave_flujo_forma").getValor());
                }    
            } 
            
            if (c.getCampos().get("clave_proyecto") != null) {
                if (c.getCampos().get("clave_proyecto").getValor() != null) {
                    this.claveProyecto = Integer.parseInt(c.getCampos().get("clave_proyecto").getValor());
                } 
            }
            
            if (c.getCampos().get("clave_estatus").getValor() != null) {
                this.claveEstatus = Integer.parseInt(c.getCampos().get("clave_estatus").getValor());
            } else {
                if (c.getAccion().equals("update")) {
                    if (c.getCampos().get("clave_estatus").getValorOriginal()!=null) {
                        this.claveEstatus = Integer.parseInt(c.getCampos().get("clave_estatus").getValorOriginal());
                    }  else {
                        throw new Fallo("No se especificó el estatus de la actividad, verifique");
                    }     
                } else {
                        throw new Fallo("No se especificó el estatus de la actividad, verifique");
                }    
            }

            if (c.getCampos().get("clave_empleado_solicitante").getValor() != null) {
                this.claveEmpleadoSolicitante = Integer.parseInt(c.getCampos().get("clave_empleado_solicitante").getValor());
            } else {
                if (!c.getAccion().equals("update")) {
                    throw new Fallo("No se especificó al empleado solicitante, verifique");
                }    
            }
            
            if (c.getCampos().get("alertar_con_anticipacion") != null) {
                if  (c.getCampos().get("alertar_con_anticipacion").getValor()!=null) {
                    this.alertarConAnticipacion = Integer.parseInt(c.getCampos().get("alertar_con_anticipacion").getValor());
                }    
            } 

             if (c.getCampos().get("clave_empleado_asignado").getValor() != null) {
                this.claveEmpleadoAsignado = Integer.parseInt(c.getCampos().get("clave_empleado_asignado").getValor());
            } else {
                 if (!c.getAccion().equals("update")) {
                    throw new Fallo("No se especificó al empleado asignado, verifique");
                 }   
            }
            
            if (c.getCampos().get("observacion") != null ) {
                if (c.getCampos().get("observacion").getValor() != null ) {
                    this.lugar = c.getCampos().get("observacion").getValor();
                }    
            } 
            
           
            if (c.getCampos().get("clave_forma").getValor() != null) {
                if (!c.getCampos().get("clave_forma").getValor().equals("")) {
                    this.claveForma = Integer.parseInt(c.getCampos().get("clave_forma").getValor());
                }    
            } 
            
            if (c.getCampos().get("clave_registro").getValor() != null) {
                 if (!c.getCampos().get("clave_registro").getValor().equals("")) {
                    this.claveRegistro = Integer.parseInt(c.getCampos().get("clave_registro").getValor());
                 }   
            } 
            
            if (c.getCampos().get("clave_actividad_origen")!=null) {
                if (c.getCampos().get("clave_actividad_origen").getValor() != null) {
                     if (!c.getCampos().get("clave_actividad_origen").getValor().equals("")) {
                        this.claveActividadOrigen = Integer.parseInt(c.getCampos().get("clave_actividad_origen").getValor());
                     }   
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

       this.claveActividad = Integer.parseInt(resultado.toString().split("<pk>")[1].substring(0, resultado.toString().split("<pk>")[1].indexOf("</pk>")));
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
        Boolean hayCambios = false;
        ResultSet rs;
        Mail mail = new Mail(super.getUsuario());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd//MM/yyyy HH:mm");
        Boolean soyInvitado=true;
        //Deben de actualizarse los registros asociados
                
        try {
            Consulta c = (Consulta) super.clone();
            resultado.append(c.update(true));
            
            q="select * from be_actividad where clave_forma=101 and clave_registro=".concat(this.claveActividad.toString());
            rs = oDb.getRs(q); 
        
            while (rs.next()) {
                soyInvitado=false;
                                
                if (super.getCampos().get("actividad").getValor()!=null) {
                    if (! super.getCampos().get("actividad").getValor().equals(super.getCampos().get("actividad").getValorOriginal())) {
                      hayCambios = true;  
                    }
                }
                
                if (super.getCampos().get("clave_categoria").getValor()!=null) {
                    if (!super.getCampos().get("clave_categoria").getValor().equals(super.getCampos().get("clave_categoria").getValorOriginal())) {
                      hayCambios = true;  
                    }
                }
                
                String horaInicialNueva = super.getCampos().get("fecha_inicial").getValor().split(" ")[1];
                String fechaInicialNueva = super.getCampos().get("fecha_inicial").getValor().split(" ")[0];

                String horaInicialOriginal = super.getCampos().get("fecha_inicial").getValorOriginal().split(" ")[1];
                String fechaInicialOriginal = super.getCampos().get("fecha_inicial").getValorOriginal().split(" ")[0];

                if (!fechaInicialNueva.equals(fechaInicialOriginal.split("-")[2].concat("/").concat(fechaInicialOriginal.split("-")[1]).concat("/").concat(fechaInicialOriginal.split("-")[0])) || !horaInicialNueva.equals(horaInicialOriginal)) {
                    hayCambios = true;
                } 
                
                if (super.getCampos().get("fecha_final").getValor()==null && super.getCampos().get("fecha_final").getValorOriginal()!=null){
                    hayCambios = true; }
                else if (super.getCampos().get("fecha_final").getValor()!=null && super.getCampos().get("fecha_final").getValorOriginal()==null) {
                     hayCambios = true; }
                else if (super.getCampos().get("fecha_final").getValor()!=null && super.getCampos().get("fecha_final").getValorOriginal()!=null) {
                    String horaFinalNueva = super.getCampos().get("fecha_final").getValor().split(" ")[1];
                    String fechaFinalNueva = super.getCampos().get("fecha_final").getValor().split(" ")[0];

                    String horaFinalOriginal = super.getCampos().get("fecha_final").getValorOriginal().split(" ")[1];
                    String fechaFinalOriginal = super.getCampos().get("fecha_final").getValorOriginal().split(" ")[0];

                    if (!fechaFinalNueva.equals(fechaFinalOriginal.split("-")[2].concat("/").concat(fechaInicialOriginal.split("-")[1]).concat("/").concat(fechaFinalOriginal.split("-")[0])) || !horaFinalNueva.equals(horaFinalOriginal)) {
                        hayCambios = true;
                    } 
                }

                if (super.getCampos().get("lugar").getValor()!=null) {
                        if (! super.getCampos().get("lugar").getValor().equals(super.getCampos().get("lugar").getValorOriginal())) {
                          hayCambios = true;  
                        }
                }

                if (super.getCampos().get("clave_estatus").getValor()!=null) {
                    if (! super.getCampos().get("clave_estatus").getValor().equals(super.getCampos().get("clave_estatus").getValorOriginal())) {
                      hayCambios = true;  
                    }
                }
                
                q = "UPDATE be_actividad SET ".concat("fecha_inicial='").concat(formatter.format(this.fechaInicial)).concat("'");
                
                if (this.actividad!=null) 
                  q = q.concat(",actividad='").concat(this.actividad.replace("'", "''")).concat("'");
                
                if (this.claveCategoria!=null)
                    q = q.concat(",clave_categoria=").concat(this.claveCategoria.toString());
                
                if (this.lugar!=null)    
                    q = q.concat(",lugar='").concat(this.lugar.replaceAll("'", "''") ).concat("'");
                
                if (this.claveEmpleadoSolicitante!=null)
                    q = q.concat(",clave_empleado_solicitante=").concat(this.claveEmpleadoSolicitante.toString());
                
                if (this.fechaFinal!=null) {
                    q = q.concat(",fecha_final='").concat(formatter.format(this.fechaFinal)).concat("'");
                } else {
                    q = q.concat(",fecha_final=null");
                }
                
                if (this.observacion!=null) {
                    q = q.concat(",observacion='").concat(this.observacion.replaceAll("'", "''")).concat("'");
                }
                
                if (this.claveForma!=null ) {
                    if (this.claveForma!=0)
                        q = q.concat(",clave_forma=").concat(String.valueOf(this.claveForma));
                }
                    
                if (this.claveRegistro!=null) {
                    q = q.concat(",clave_registro=").concat(this.claveRegistro.toString());
                }
                     
                q = q.concat(" WHERE clave_actividad=").concat(rs.getString("clave_actividad") );
                
                resultado.append("<update_be_actividad>".concat(q).concat("</update_be_actividad>"));
                oDb.execute(q);
                
                //Hace falta envíar correo de notificación de cambio en la agenda
                Usuario contacto = new Usuario();

                contacto.setCx(cxContacto);
                contacto.setClave(rs.getInt("clave_empleado_asignado"));

                
                if (hayCambios) {
                    if (contacto.getEmail()!=null ) {
                        
                        
                        if (this.getClaveEstatus()==0 )  {
                           notificacion =  "Estimado(a) ".concat(contacto.getNombre()).concat(":<br /><br />")
                                           .concat("Se hicieron cambios con respecto a la actividad <strong>").concat(this.getActividad()).concat("</strong> previamente agendada; <br />")
                                           .concat("Hora de inicio: ").concat(formatter2.format(this.fechaInicial)).concat("<br />");
                          
                           if (this.lugar!=null) {
                               if (!this.lugar.equals("")) {
                                    notificacion = notificacion.concat("Lugar: ").concat(this.lugar).concat("<br />");
                                }
                           }
                           
                           notificacion = notificacion.concat("<a href='http://siap.ilce.edu.mx/intranet/confirm.jsp?$ca=").concat(rs.getString("clave_actividad")).concat("&$r=2'>Si asistiré</a><br />")
                                           .concat("<a href='http://siap.ilce.edu.mx/intranet/confirm.jsp?$ca=").concat(rs.getString("clave_actividad")).concat("&$r=3'>No asistiré</a><br />")
                                           .concat("Para consultar la agenda accede a http://siap.ilce.edu.mx/intranet/.<br /><br /> ")
                                           .concat(super.getUsuario().getNombre());
                            
                        } else if ( this.getClaveEstatus()==1) {    
                           notificacion =  "Estimado(a) ".concat(contacto.getNombre()).concat(":<br /><br />")
                                           .concat("Se hicieron cambios con respecto a la  actividad <strong>").concat(this.getActividad()).concat("</strong> previamente agendada; ")
                                           .concat("para atenderla accede a http://siap.ilce.edu.mx/intranet/.<br /><br /> ")
                                           .concat(super.getUsuario().getNombre());                            
                        } else if (this.getClaveEstatus()==2) { //Confirmado
                           notificacion =  "Estimado(a) ".concat(contacto.getNombre()).concat(":<br /><br />")
                                           .concat("Confirmo mi asistencia al evento <strong>").concat(this.getActividad()).concat("</strong> previamente agendado ")
                                           .concat(super.getUsuario().getNombre())
                                           .concat("<br><br>Para consultar la agenda accede a http://siap.ilce.edu.mx/intranet/.")
                                   ; }
                        else if (this.getClaveEstatus()==3) {
                           notificacion = "Estimado(a) ".concat(contacto.getNombre()).concat(":<br /><br />")
                                           .concat("Se canceló la  actividad <strong>").concat(this.getActividad()).concat("</strong> previamente agendada; ")
                                           .concat("para verificarla accede a http://siap.ilce.edu.mx/intranet/.<br /><br /> ")
                                           .concat(super.getUsuario().getNombre());
                        }
                        
                        //mail.sendEmail("intranet@ilce.edu.mx", contacto.getEmail(),
                        //       super.getUsuario().getNombre().concat(" ").concat(super.getUsuario().getApellidos()).concat(" te ha reagendado la actividad ").concat(this.getActividad()),
                        //        notificacion, null);
                        
                        resultado.append("<notification>Se ha enviado un correo a ").append(contacto.getEmail()).append(" notificándole los cambios</notification>");
                    } else {
                        resultado.append("<warning><![CDATA[El usuario ").append(contacto.getNombre()).append(" ").append(contacto.getApellido_paterno()).append(" no tiene un correo registrado y no fue posible notificársele.").append("]]></warning>");
                    }
                }  else {
                    resultado.append("<notification>No se realizaron notificaciones al no encontrarse cambios en el registro</notification>");
                }     
            }
            
            if (soyInvitado) {
                //Se debe buscar el registro del organizador y enviarle la confirmación de asistencia 
                if (this.getClaveEmpleadoSolicitante().intValue()!=this.getUsuario().getClave().intValue()) {
                    q="select * from be_actividad where clave_actividad=".concat(this.getClaveRegistro().toString());
                    rs = oDb.getRs(q);

                    if (rs.next()) {
                        Usuario organizador = new Usuario();
                        organizador.setCx(cxContacto);

                        organizador.setClave(rs.getInt("clave_empleado_solicitante"));

                        if (this.claveEstatus==2) {
                            asunto = super.getUsuario().getNombre().concat(" ").concat(super.getUsuario().getApellido_paterno()).concat(" ha confirmado su asistencia al evento ").concat(this.getActividad());
                            notificacion = "Estimado(a) ".concat(organizador.getNombre()).concat(" ").concat(organizador.getApellido_paterno()).concat(":<br /><br />")
                                            .concat(super.getUsuario().getNombre()).concat(" ").concat(super.getUsuario().getApellido_paterno()).concat(" ha confirmado su asistencia al evento <strong>").concat(this.getActividad()).concat("</strong> previamente agendada; ")
                                            .concat("para verificar la agenda accede a http://siap.ilce.edu.mx/intranet/.<br /><br /> ")
                                            .concat(super.getUsuario().getNombre());  
                        } else if (this.claveEstatus==3) { 
                            asunto = super.getUsuario().getNombre().concat(" ").concat(super.getUsuario().getApellido_paterno()).concat(" ha confirmado que no asistirá al evento ").concat(this.getActividad());
                            notificacion = "Estimado(a) ".concat(organizador.getNombre()).concat(organizador.getApellido_paterno()).concat(":<br /><br />")
                                            .concat(super.getUsuario().getNombre()).concat(" ").concat(super.getUsuario().getApellido_paterno()).concat(" ha confirmado que no asistirá al evento <strong>").concat(this.getActividad()).concat("</strong> previamente agendada; ")
                                            .concat("para verificar la agenda accede a http://siap.ilce.edu.mx/intranet/.<br /><br /> ")
                                            .concat(super.getUsuario().getNombre());                          
                        } 

                         mail.sendEmail("intranet@ilce.edu.mx", organizador.getEmail(),
                                    asunto,  notificacion, null);
                    }
                }
            }

        } catch(Exception e) {
            resultado.append("<error><![CDATA[").append(e.getMessage()).append("]]></error>");
            throw new Fallo(resultado.toString());
        } finally {
            oDb.cierraConexion();
            return resultado.toString();
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
        super.setTabla("be_actividad");
        super.setLlavePrimaria("clave_actividad");
        super.setPk(String.valueOf(this.claveActividad));

        resultado.append(super.delete(true, super.getUsuario()));

        if (resultado.toString().split("<error>").length > 1) {
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }

        return resultado.toString();
    }    

}
