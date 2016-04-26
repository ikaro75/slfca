
package mx.org.fide.comunidad;

import java.sql.ResultSet; 
import java.text.SimpleDateFormat;
import java.util.Date;
import mx.org.fide.backend.Forma;
import mx.org.fide.mail.Mail;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 * Clase que contiene las especificaciones del contacto al que se comparte el archivo.
 * Se extiende de la clase consulta.
 * @author Daniel
 */
public class ArchivoContacto extends Consulta {

    private Integer claveArchivoContacto;
    private Integer claveArchivo;
    private Integer claveContacto;
    private Integer claveGrupo;

    /**
     * Recupera el archivo que se comparte.
     * @return Clave de archivo.
     */
    public Integer getClaveArchivoContacto() {
        return claveArchivoContacto;
    }

    /**
     * Establece el archivo que se compartirá.
     * @param claveArchivoContacto  Clave de archivo.
     */
    public void setClaveArchivoContacto(Integer claveArchivoContacto) {
        this.claveArchivoContacto = claveArchivoContacto;
    }

    /**
     * Recupera archivo.
     * @return Clave del archivo.
     */
    public Integer getClaveArchivo() {
        return claveArchivo;
    }

    /**
     * Establece archivo. 
     * @param claveArchivo Clave del archivo. 
     */
    public void setClaveArchivo(Integer claveArchivo) {
        this.claveArchivo = claveArchivo;
    }

    /**
     * Recupera al contacto para compartir archivo.
     * @return Clave del contacto.
     */
    public Integer getClaveContacto() {
        return claveContacto;
    }

    /**
     * Establece contacto para compartir archivo.
     * @param claveContacto Clave del contacto.
     */
    public void setClaveContacto(Integer claveContacto) {
        this.claveContacto = claveContacto;
    }

    /**
     * Recupera el grupo al que se compartió el archivo.
     * @return Calave del grupo.
     */
    public Integer getClaveGrupo() {
        return claveGrupo;
    }

    /**
     * Establece la clave del grupo.
     * @param claveGrupo Clave del grupo.
     */
    public void setClaveGrupo(Integer claveGrupo) {
        this.claveGrupo = claveGrupo;
    }

    /**
     * Constructor de la clase ArchivoContacto.
     * @param c Consulta en SQL.
     * @param cx Conexión a la base de datos.
     * @throws Fallo 
     */
    public ArchivoContacto(Consulta c) throws Fallo {
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
            if (c.getCampos().get("clave_archivo_contacto").getValor() != null) {
                this.claveArchivoContacto = Integer.parseInt(c.getCampos().get("clave_archivo_contacto").getValor());
            }

            if (c.getCampos().get("clave_archivo").getValor() != null && !c.getAccion().equals("delete")) {
                this.claveArchivo = Integer.parseInt(c.getCampos().get("clave_archivo").getValor());
            } else {
                throw new Fallo("No se especificó la clave del archivo, verifique");
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
        String mensaje = "";
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Conexion oDb = new Conexion(this.getUsuario().getCx().getServer(), this.getUsuario().getCx().getDb(), this.getUsuario().getCx().getUser(), this.getUsuario().getCx().getPw(), this.getUsuario().getCx().getDbType());
        Forma frmArchivo = new Forma();
        Mail mail = new Mail(super.getUsuario());
        Documento d = null;

        try {

            //2. Inserta nueva relación archivo contacto
            if (super.getCampos().get("mensaje")!=null) {
                mensaje=super.getCampos().get("mensaje").getValor();
                if (mensaje==null)  mensaje="";
            }
            
            frmArchivo = new Forma (new Consulta(311,"insert", "0", "", null, this.getUsuario()),false);
            /*frmArchivo.setClaveForma(311, usuario.getClavePerfil(), usuario.getCx());
            frmArchivo.setConsulta("insert", "0", "", usuario.getReglasDeReemplazo(), usuario.getCx());
            frmArchivo.getConsulta().setRegistros(usuario.getCx());   */
            frmArchivo.setRegistros();
            d = new Documento(this.claveArchivo, this.getUsuario().getCx());
            
            q.append(super.insert(true));
            resultadoXML.append(q.toString());
            
            //frmArchivo.getConsulta().setPk(preguntaAuditor.getClavePreguntaAuditor().toString());
            frmArchivo.getCampos().get("descripcion").setValor(d.getDescripcion());
            frmArchivo.getCampos().get("archivo").setValor(d.getArchivo());
            frmArchivo.getCampos().get("clave_categoria").setValor(d.getClaveCategoria().toString());
            frmArchivo.getCampos().get("palabras_clave").setValor(d.getPalabrasClave());
            frmArchivo.getCampos().get("fecha").setValor(dateFormatter.format(new Date()));
            frmArchivo.getCampos().get("clave_forma").setValor(d.getClaveForma().toString());
            frmArchivo.getCampos().get("clave_registro").setValor(d.getClaveRegistro().toString());

            //2. envía notificación con liga
            if (this.claveContacto != null) {
                Contacto c = new Contacto(this.claveContacto, this.getUsuario().getCx());
                frmArchivo.getCampos().get("clave_empleado").setValor(c.getClave().toString());
                resultadoXML.append("<insert_archivocontacto>").append(frmArchivo.insert(true)).append("</insert_archivocontacto>");
                
                if (c.getEmail()!= null) {
                    mail.sendEmail("intranet@ilce.edu.mx", c.getEmail(),
                            this.getUsuario().getNombre().concat(" ").concat(this.getUsuario().getApellido_paterno()).concat(" quiere compartir ").concat(d.getArchivo().replaceAll("/intranet/temp/".concat(this.getUsuario().getClave().toString()).concat("/"), "")).concat(" contigo"),
                            "Estimado(a) ".concat(c.getNombre()).concat(":<br /><br />")
                            .concat("Te he compartido un archivo llamado : <strong>").concat(d.getArchivo().replaceAll("/intranet/temp/".concat(this.getUsuario().getClave().toString()).concat("/"), "")).concat("</strong><br /><br />")
                            .concat("y lo puedes descargar desde <a href='http://siap.ilce.edu.mx").concat(d.getArchivo()).concat("'>http://siap.ilce.edu.mx").concat(d.getArchivo()).concat("</a>; o bien puedes acceder al archivo desde la intranet, en la sección 'Comunidad / Documentos' en http://siap.ilce.edu.mx/intranet/.<br /><br />")
                            .concat("<i>").concat(mensaje).concat("</i>").concat((!mensaje.equals("")?"<br /><br />":""))
                            .concat(this.getUsuario().getNombre()), null);
                } else {
                    q.append("<warning><![CDATA[El usuario ").append(c.getNombre()).append(" ").append(c.getApellido_paterno()).append(" no tiene un correo registrado y no fue posible notificarsele.").append("]]></warning>");
                }
            } else if (this.claveGrupo != null) {

                for (Contacto c : new Grupo(this.claveGrupo, this.getUsuario().getCx()).getContactos(this.getUsuario().getCx())) {
                    frmArchivo.getCampos().get("clave_empleado").setValor(c.getClave().toString());
                    resultadoXML.append(frmArchivo.insert(true));
                    
                    if (c.getEmail()!= null) {
                        /*Contacto c = new Contacto (this.claveContacto, cx); */
                        mail.sendEmail("intranet@ilce.edu.mx", c.getEmail(),
                                this.getUsuario().getNombre().concat(" ").concat(this.getUsuario().getApellido_paterno()).concat(" quiere compartir ").concat(d.getArchivo().replaceAll("/intranet/temp/".concat(this.getUsuario().getClave().toString()).concat("/"), "")).concat(" contigo"),
                                "Estimado(a) ".concat(c.getNombre()).concat(":<br /><br />")
                                .concat("Te he compartido un archivo llamado : ").concat(d.getArchivo().replaceAll("/intranet/temp/".concat(this.getUsuario().getClave().toString()).concat("/"), "")).concat("<br /><br />")
                                .concat("y lo puedes descargar desde <a href='http://siap.ilce.edu.mx").concat(d.getArchivo()).concat("'>http://siap.ilce.edu.mx").concat(d.getArchivo()).concat("</a>; o bien puedes acceder al archivo desde la intranet, en la sección 'Comunidad / Documentos' en http://siap.ilce.edu.mx/intranet/.<br /><br /> ")
                                .concat("<i>").concat(mensaje).concat("</i>").concat((!mensaje.equals("")?"<br /><br />":""))
                                .concat(this.getUsuario().getNombre()), null);
                    } else {
                        q.append("<warning><![CDATA[El usuario ").append(c.getNombre()).append(" ").append(c.getApellido_paterno()).append(" no tiene un correo registrado y no fue posible notificarsele.").append("]]></warning>");
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
