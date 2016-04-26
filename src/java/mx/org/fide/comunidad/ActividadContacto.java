/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.comunidad;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import mx.org.fide.agenda.Actividad;
import mx.org.fide.agenda.CategoriaActividad;
import mx.org.fide.backend.Forma;
import mx.org.fide.backend.Nota;
import mx.org.fide.mail.Mail;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 * Clase que se encarga de guardar la actividad de un contacto se extiende de la clase consulta.
 * @author Daniel
 */
public class ActividadContacto extends Consulta {
    private Integer claveActividadContacto;
    private Integer claveActividad;
    private Integer claveContacto;
    private Integer claveGrupo;

    /**
     * Recupera la actividad con la relación del contacto.
     * @return Clave ActividadContacto
     */
    public Integer getClaveActividadContacto() {
        return claveActividadContacto;
    }

    /**
     * Recupera la actividad con la relación del contacto.
     * @param claveActividadContacto  Clave ActividadContacto
     */
    public void setClaveActividadContacto(Integer claveActividadContacto) {
        this.claveActividadContacto = claveActividadContacto;
    }

    /**
     * Recupera la actividad.
     * @return Clave de la actividad.
     */
    public Integer getClaveActividad() {
        return claveActividad;
    }

    /**
     * Establece la actividad que se compartira con el contacto.
     * @param claveActividad  Clave de actividad.
     */
    public void setClaveActividad(Integer claveActividad) {
        this.claveActividad = claveActividad;
    }

    /**
     * Recupera al contacto con el que se comparte la actividad.
     * @return Clave del contacto.
     */
    public Integer getClaveContacto() {
        return claveContacto;
    }

    /**
     * Establece al contacto con el que se compartira la actividad.
     * @param claveContacto Clave del contacto.
     */
    public void setClaveContacto(Integer claveContacto) {
        this.claveContacto = claveContacto;
    }

    /**
     * Recupera el grupo donde se encuentra el contacto.
     * @return Clave del grupo.
     */
    public Integer getClaveGrupo() {
        return claveGrupo;
    }

    /**
     * Establece el grupo donde se encuentra el contacto.
     * @param claveGrupo Clave del grupo.
     */
    public void setClaveGrupo(Integer claveGrupo) {
        this.claveGrupo = claveGrupo;
    }

    /**
     * Constructor de la clase ActividadContacto
     * @param c Consulta a ejecutar SQL
     * @param cx Conexiòn a la base de datos
     * @throws Fallo 
     */
    public ActividadContacto(Consulta c) throws Fallo {
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
            if (c.getCampos().get("clave_actividad_contacto").getValor() != null) {
                this.claveActividadContacto = Integer.parseInt(c.getCampos().get("clave_actividad_contacto").getValor());
            }

            if (c.getCampos().get("clave_actividad").getValor() != null && !c.getAccion().equals("delete")) {
                this.claveActividad = Integer.parseInt(c.getCampos().get("clave_actividad").getValor());
            } else {
                throw new Fallo("No se especificó la clave de la actividad, verifique");
            }

            if (c.getCampos().get("clave_contacto").getValor() != null && !c.getAccion().equals("delete") && !c.getCampos().get("clave_contacto").getValor().equals("")) {
                this.claveContacto = Integer.parseInt(c.getCampos().get("clave_contacto").getValor());
            }

            if (c.getCampos().get("clave_grupo").getValor() != null && !c.getAccion().equals("delete") && !c.getCampos().get("clave_grupo").getValor().equals("")) {
                this.claveGrupo = Integer.parseInt(c.getCampos().get("clave_grupo").getValor());
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
    public String insert() throws Fallo {

        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        String resultadoInsert="";
        StringBuilder q = new StringBuilder();
        String notificacion="";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(),super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType()); 
        Forma frmActividad = new Forma();
        Forma frmCategoriaActividad = new Forma();
        Integer claveCategoria= 0;
        
        Mail mail = new Mail(super.getUsuario());
        Actividad a = null;
        CategoriaActividad ca = null;
        
        Consulta cnsNota=new Consulta(273, "insert", "0", "", null,super.getUsuario());
        cnsNota.setRegistros();
        
        try {

            //1. Inserta nueva actividad del contacto con quien se comparte
            frmActividad = new Forma(new Consulta(101,"insert", "0", "", null,super.getUsuario()), false);
            //frmActividad.setClaveForma(101, usuario.getClavePerfil(), usuario.getCx());
            //frmActividad.setConsulta("insert", "0", "", usuario.getReglasDeReemplazo(), usuario.getCx());
            frmActividad.setRegistros();   
            a = new Actividad(this.claveActividad, super.getUsuario());
            ca = new CategoriaActividad(a.getClaveCategoria(), super.getUsuario());                
            
            //2. Inserta nueva relación archivo contacto
            q.append(super.insert(true));
            resultadoXML.append(q.toString());
            
            //frmArchivo.getConsulta().setPk(preguntaAuditor.getClavePreguntaAuditor().toString());
            frmActividad.getCampos().get("actividad").setValor(a.getActividad());
            frmActividad.getCampos().get("fecha_inicial").setValor(dateFormatter.format(a.getFechaInicial()));
            
            if (a.getFechaFinal()!=null)
                frmActividad.getCampos().get("fecha_final").setValor(dateFormatter.format(a.getFechaFinal()));
            else 
                frmActividad.getCampos().get("fecha_final").setValor(null);
            
            frmActividad.getCampos().get("lugar").setValor(a.getLugar());
            
            if (frmActividad.getCampos().get("clave_flujo_forma")!=null)
                frmActividad.getCampos().get("clave_flujo_forma").setValor(a.getClaveFlujoForma().toString());
            
            if (frmActividad.getCampos().get("clave_proyecto")!=null)
                frmActividad.getCampos().get("clave_proyecto").setValor(a.getClaveProyecto().toString());
            
            frmActividad.getCampos().get("clave_estatus").setValor(a.getClaveEstatus().toString());
            frmActividad.getCampos().get("clave_empleado_solicitante").setValor(a.getClaveEmpleadoSolicitante().toString());
            
            if (frmActividad.getCampos().get("alertar_con_anticipacion")!=null)
                frmActividad.getCampos().get("alertar_con_anticipacion").setValor(a.getAlertarConAnticipacion().toString());
            
            if (frmActividad.getCampos().get("observacion")!=null)
                frmActividad.getCampos().get("observacion").setValor(a.getObservacion());
            
            if (frmActividad.getCampos().get("clave_forma")!=null)
                frmActividad.getCampos().get("clave_forma").setValor("101");
            
            if (frmActividad.getCampos().get("clave_registro")!=null)
                frmActividad.getCampos().get("clave_registro").setValor(this.claveActividad.toString());
            
            if (frmActividad.getCampos().get("clave_actividad_origen")!=null)
                frmActividad.getCampos().get("clave_actividad_origen").setValor(this.claveActividad.toString());
            //2. envía notificación con liga
            if (this.claveContacto != null) {
                Contacto c = new Contacto(this.claveContacto, super.getUsuario().getCx());
                //Verifica si el contacto a quien se le está compartiendo tiene una categoría de actividad similar
                rs = oDb.getRs("SELECT clave_categoria,categoria FROM be_categoria_actividad WHERE categoria='".concat(ca.getCategoria().replaceAll("'", "''")).concat("' AND clave_empleado=").concat(c.getClave().toString()));

                //Si no hay una categoría similar para el usuario al que se le está asignando se le debe de crear
                if (!rs.next()) {
                    frmCategoriaActividad = new Forma(new Consulta(309, "insert", "0", "", null, super.getUsuario()), false);
                    /*frmCategoriaActividad.setClaveForma(309, usuario.getClavePerfil(), usuario.getCx());
                    frmCategoriaActividad.setConsulta("insert", "0", "", usuario.getReglasDeReemplazo(), usuario.getCx());*/
                    frmCategoriaActividad.setRegistros(); 
                    frmCategoriaActividad.getCampos().get("categoria").setValor(ca.getCategoria());
                    frmCategoriaActividad.getCampos().get("clave_empleado").setValor(c.getClave().toString());
                    resultadoInsert = frmCategoriaActividad.insert(true);
                    String PK= resultadoInsert.substring(resultadoInsert.lastIndexOf("<pk>")+4, resultadoInsert.lastIndexOf("</pk>"));
                    resultadoXML.append("<insert_categoria_actividad>".concat(resultadoInsert).concat("</insert_categoria_actividad>")); 
                    claveCategoria =  Integer.parseInt(PK);
                } else {
                    claveCategoria = rs.getInt("clave_categoria");
                } 
                
                frmActividad.getCampos().get("clave_categoria").setValor(claveCategoria.toString());
                frmActividad.getCampos().get("clave_empleado_asignado").setValor(c.getClave().toString());
                resultadoInsert =frmActividad.insert(true);
                
                String PK= resultadoInsert.substring(resultadoInsert.lastIndexOf("<pk>")+4, resultadoInsert.lastIndexOf("</pk>"));
                resultadoXML.append("<insert_actividad>".concat(resultadoInsert).concat("</insert_actividad>") );
                
                if (a.getClaveEstatus()==0) {
                    notificacion =  "Estimado(a) ".concat(c.getNombre()).concat(":<br /><br />")
                            .concat("Te he agendado la actividad : <strong>").concat(a.getActividad()).concat("</strong>;<br />")
                            .concat("Hora de inicio: ").concat(dateFormatter.format(a.getFechaInicial())).concat("<br />");
                    if (a.getLugar()!=null) {
                          if (!a.getLugar().equals("")) {
                               notificacion = notificacion.concat("Lugar: ").concat(a.getLugar()).concat("<br />");
                           }
                    }        
                    
                    notificacion = notificacion.concat("<a href='http://siap.ilce.edu.mx/intranet/confirm.jsp?$ca=").concat(PK).concat("&$r=2").concat("'>Si asistiré</a><br />")
                                    .concat("<a href='http://siap.ilce.edu.mx/intranet/confirm.jsp?$ca=").concat(PK).concat("&$r=3").concat("'>No asistiré</a><br />")
                                    .concat("Para consultar la agenda accede a http://siap.ilce.edu.mx/intranet/.<br /><br /> ")
                                    .concat(super.getUsuario().getNombre());

                }
                                                                                      
                if (c.getEmail()!= null) {
                    mail.sendEmail("intranet@ilce.edu.mx", c.getEmail(),
                            super.getUsuario().getNombre().concat(" ").concat(super.getUsuario().getApellido_paterno()).concat(" te ha agendado la actividad ").concat(a.getActividad()),
                            notificacion.concat(super.getUsuario().getNombre()), null);
                } else {
                    q.append("<warning><![CDATA[El usuario ").append(c.getNombre()).append(" ").append(c.getApellido_paterno()).append(" no tiene un correo registrado y no fue posible notificarsele.").append("]]></warning>");
                }
                
                //Ahora se retoman las notas de la actividad para incorporarlas
                rs = oDb.getRs("select * from be_nota_forma where clave_forma=101 and clave_registro=".concat(a.getClaveActividad().toString()));
                while (rs.next()) {
                    Usuario remitente = new Usuario();
                    remitente.setCx(super.getUsuario().getCx());
                    remitente.setClave(rs.getInt("clave_empleado"));
                            
                    cnsNota.getCampos().get("clave_forma").setValor("101");
                    cnsNota.getCampos().get("clave_registro").setValor(PK);
                    cnsNota.getCampos().get("clave_empleado").setValor(rs.getString("clave_empleado"));
                    cnsNota.getCampos().get("titulo").setValor(remitente.getNombre().concat(" ").concat(remitente.getApellido_paterno()));
                    cnsNota.getCampos().get("mensaje").setValor(rs.getString("mensaje"));
                    cnsNota.getCampos().get("fecha_nota").setValor(dateFormatter.format(rs.getDate("fecha_nota")));
                    Nota nota = new Nota(cnsNota);
                    resultadoXML.append("<insert_nota_forma>".concat(nota.insert(remitente, "la actividad ".concat(a.getActividad()), false)).concat("</insert_nota_forma>") );
                }
            } else if (this.claveGrupo != null) {

                for (Contacto c : new Grupo(this.claveGrupo, super.getUsuario().getCx()).getContactos(super.getUsuario().getCx())) {
                    frmActividad.getCampos().get("clave_empleado_asignado").setValor(c.getClave().toString());
                    resultadoInsert = frmActividad.insert(true); 

                    String PK= resultadoInsert.substring(resultadoInsert.lastIndexOf("<pk>")+4, resultadoInsert.lastIndexOf("</pk>"));
                    
                    resultadoXML.append("<insert_actividad>".concat(resultadoInsert).concat("</insert_actividad>") );
                        
                    if (a.getClaveEstatus()==0) {
                    notificacion =  "Estimado(a) ".concat(c.getNombre()).concat(":<br /><br />")
                            .concat("Te he agendado la actividad : <strong>").concat(a.getActividad()).concat("</strong>;<br />")
                            .concat("Hora de inicio: ").concat(dateFormatter.format(a.getFechaInicial())).concat("< br/>");
                    if (a.getLugar()!=null) {
                          if (!a.getLugar().equals("")) {
                               notificacion = notificacion.concat("Lugar: ").concat(a.getLugar()).concat("<br />");
                           }
                    }
                    
                    notificacion = notificacion.concat("<a href='http://siap.ilce.edu.mx/intranet/confirm.jsp?$ca=").concat(PK).concat("&$r=2'>Si asistiré</a><br />")
                                    .concat("<a href='http://siap.ilce.edu.mx/intranet/confirm.jsp?$ca=").concat(PK).concat("&$r=3'>No asistiré</a><br />")
                                    .concat("Para consultar la agenda accede a http://siap.ilce.edu.mx/intranet/.<br /><br /> ");

                    }
                                    
                    if (c.getEmail()!= null) {
                        /*Contacto c = new Contacto (this.claveContacto, cx); */
                        mail.sendEmail("intranet@ilce.edu.mx", c.getEmail(),
                            super.getUsuario().getNombre().concat(" ").concat(super.getUsuario().getApellido_paterno()).concat(" te ha agendado la actividad ").concat(a.getActividad()),
                            notificacion.concat(super.getUsuario().getNombre()), null);
                    } else {
                        q.append("<warning><![CDATA[El usuario ").append(c.getNombre()).append(" ").append(c.getApellido_paterno()).append(" no tiene un correo registrado y no fue posible notificarsele.").append("]]></warning>");
                    }
                    
                    //Ahora se retoman las notas de la actividad para incorporarlas
                    rs = oDb.getRs("select * from be_nota_forma where clave_forma=101 and clave_registro=".concat(a.getClaveActividad().toString()));
                    while (rs.next()) {
                        Usuario remitente = new Usuario();
                        remitente.setCx(super.getUsuario().getCx());
                        remitente.setClave(rs.getInt("clave_empleado"));
                    
                        cnsNota.getCampos().get("clave_forma").setValor("101");
                        cnsNota.getCampos().get("clave_registro").setValor(PK);
                        cnsNota.getCampos().get("clave_empleado").setValor(rs.getString("clave_empleado"));
                        cnsNota.getCampos().get("titulo").setValor(remitente.getNombre().concat(" ").concat(remitente.getApellido_paterno()));
                        cnsNota.getCampos().get("mensaje").setValor(rs.getString("mensaje"));
                        cnsNota.getCampos().get("fecha_nota").setValor(rs.getDate("fecha_nota").toString());
                        Nota nota = new Nota(cnsNota);
                        resultadoXML.append("<insert_nota_forma>".concat(nota.insert(remitente, "la actividad ".concat(a.getActividad()), false)).concat("</insert_nota_forma>") );                        
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

