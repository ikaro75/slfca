package mx.org.fide.controlador;

import java.io.File;
import java.io.FileInputStream;
import mx.org.fide.configuracion.Configuracion;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Usuario;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.mail.Mail;
import mx.org.fide.reporte.Reporte;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.poi.util.IOUtils;

/**
 * Clase encargada de procesar solicitudes de los usuarios a través del
 * protocolo HTTP Administra lo relacionado a las sesiones y de redireccionar
 * hacia los diferentes recursos del sistema Esta clase es la capa controladora
 */
public class Controlador extends HttpServlet implements HttpSessionListener {

    String sNextAction = "";
    Integer usuariosConectados = new Integer(0);
    //Importacion importacion= null;
    private static final int BYTES_DOWNLOAD = 1024;
    
    /*public void init() {
      ImportacionEnBackground ieb = new ImportacionEnBackground();
      ieb.start();
      getServletContext().setAttribute("ieb", ieb);
    }*/

    /**
     * Procesa y valida las peticiones de los usuarios hechas a través de los
     * métodos <code>GET</code> y <code>POST</code> del protocolo HTTP, gestiona
     * el manejo de sesión de usuarios y redirecciona a los recursos del sistema
     *
     * @param request request del servlet
     * @param response response del servlet
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de E/S
     * @throws Fallo Un fallo en la respuesta del controlador
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Fallo {
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0);
        //response.setContentType("text/xml;charset=ISO-8859-1");
        request.setCharacterEncoding("UTF8");

        //Carga de configuración
        Sesion sesion = (Sesion) request.getSession().getServletContext().getAttribute("sesion");

        if (sesion == null) {
            sesion = new Sesion();
            request.getSession().getServletContext().setAttribute("sesion", sesion);
        }

        if (request.getParameter("$config") != null) {
            sesion.getConfiguracion().setConfiguracionActual(request.getParameter("$config"));
        } else {
            if (sesion.getConfiguracion().getConfiguracionActual() == null) {
                Object[] aParametros = sesion.getConfiguracion().getParametros().keySet().toArray();
                if (aParametros.length == 0) {
                    sNextAction = "<error><![CDATA[No hay parámetros de configuracón definidos, verifique]]></error>";
                } else {
                    sesion.getConfiguracion().setConfiguracionActual(aParametros[0].toString());
                }
            }
        }

        String cmd = request.getParameter("$cmd");
        try {
            if (cmd == null) {
                sNextAction = "<error><![CDATA[Falta comando, verifique]]></error>";
            } else {

                if (cmd.equals("testconnection")) {
                    Configuracion configuracion = new Configuracion();
                    Object[] aDb = configuracion.getParametros().keySet().toArray();
                    for (int i = 0; i < aDb.length; i++) {
                        LinkedHashMap db = (LinkedHashMap) configuracion.getParametros().get(aDb[i]);
                        Conexion cx = new Conexion(db.get("db_server").toString(), db.get("db_name").toString(), db.get("db_user").toString(), db.get("db_pw").toString(), mx.org.fide.modelo.Conexion.DbType.valueOf(db.get("db_type").toString()));
                        if (cx.getDbType() == mx.org.fide.modelo.Conexion.DbType.MYSQL) {
                            //sNextAction+="<mensaje><![CDATA[connection string = jdbc:mysql://".concat(cx.getServer()).concat("/").concat(cx.getDb()).concat(",").concat(cx.getUser()).concat(",").concat(cx.getPw()).concat("]]></mensaje>");
                            cx.getRs("select 'Hola mundo' as mensaje");
                        } else if (cx.getDbType() == mx.org.fide.modelo.Conexion.DbType.MSSQL) {
                            //sNextAction+="<mensaje><![CDATA[connection string = jdbc:sqlserver://".concat(cx.getServer()).concat(";databaseName=").concat(cx.getDb()).concat(";selectMethod=cursor;").concat(cx.getUser()).concat(",").concat(cx.getPw()).concat("]]></mensaje>");
                            cx.getRs("select Hola mundo' as mensaje");
                        } else if (cx.getDbType() == mx.org.fide.modelo.Conexion.DbType.ORACLE) {
                            //sNextAction+="<mensaje><![CDATA[connection string = jdbc:oracle:thin:".concat(cx.getUser()).concat("/").concat(cx.getPw()).concat("@").concat(cx.getServer()).concat(":").concat(cx.getDb()).concat("]]></mensaje>");
                            cx.getRs("select 'Hola mundo' as mensaje from dual");
                        }
                        sNextAction += "<mensaje><![CDATA[Conexión exitosa con configuración '".concat(aDb[i].toString()).concat("']]></mensaje>");
                    }

                } else if (cmd.equals("login")) /* inicia comando login */ {
                    Usuario usuario = new Usuario();
                    usuario.setIp(request.getRemoteAddr());
                    usuario.setNavegador(request.getHeader("user-agent"));
                    String aParametros[] = {"email", "password"};
                    sNextAction = valida_parametros(request, aParametros);
                    if (sNextAction.equals("")) {
                        Conexion cx = new Conexion(sesion);
                        int nUsuario = cx.getLogin(request.getParameter("email"), request.getParameter("password"));

                        if (nUsuario != 0) {
                            usuario.setCx(cx);
                            usuario.setConfiguracion(sesion.getConfiguracion());
                            usuario.setClave(nUsuario);
                            usuario.setSesion(request.getSession().getId());
                            request.getSession().setAttribute("usuario", usuario);

                            if (sesion == null) {
                                sesion = new Sesion();
                            }

                            if (sesion.usuarios.get(usuario.getClave()) == null) {
                                sesion.usuarios.put(usuario.getClave(), usuario);
                                sesion.usuariosConectados++;

                                synchronized (request.getSession().getServletContext()) {
                                    request.getSession().getServletContext().setAttribute("sesion", sesion);
                                }
                            }

                            if (request.getParameter("$app") == null) {
                                sNextAction = "<mensaje><![CDATA[Usuario validado]]></mensaje>";

                                if (request.getParameter("url") != null) {
                                    request.getRequestDispatcher(request.getParameter("url")).forward(request, response);
                                } else {
                                    response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                                    response.setHeader("Location", "/slfca/vista.jsp");
                                    response.setContentType("text/html");
                                    response.sendRedirect("/slfca/vista.jsp");
                                    return;
                                    //request.getRequestDispatcher("/promocion.jsp").forward(request, response);
                                }

                            } else if (request.getParameter("$app").equals("interfaz")) {
                                sNextAction = "<mensaje><![CDATA[Usuario validado]]></mensaje>";
                                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                                response.setHeader("Location", "/slfca/interfaz.jsp");
                                response.setContentType("text/html");
                                response.sendRedirect("/administrax/interfaz.jsp");
                            } else if (request.getParameter("$app").equals("timeclock")) {
                                sNextAction = "<mensaje><![CDATA[Usuario validado]]></mensaje>";
                                request.getRequestDispatcher("/timeclock.jsp").forward(request, response);
                            } else if (request.getParameter("$app").equals("mobile")) {
                                sNextAction = "<mensaje><![CDATA[Usuario validado]]></mensaje>";
                                //request.getRequestDispatcher("vista.html").forward(request, response);
                            } else if (request.getParameter("$app").equals("none")) {
                                sNextAction = "<mensaje><![CDATA[Usuario validado]]></mensaje><clave_oficina>".concat(usuario.getClaveOficina().toString()).concat("</clave_oficina>");
                            }

                        } else {
                            sNextAction = "<mensaje><![CDATA[Usuario y/o password incorrecto, verifique]]></mensaje>";
                            request.getSession().setAttribute("mensaje_login", "Usuario y/o password incorrecto, verifique");
                            if (request.getParameter("$app") == null) {
                                request.getRequestDispatcher("/login.jsp").forward(request, response);
                            } else if (request.getParameter("$app").equals("none") || request.getParameter("$app").equals("mobile")) {
                                //No hace nada
                            } else {
                                request.getRequestDispatcher("/login.jsp").forward(request, response);
                            }
                        }
                    }
                } else if (cmd.equals("recoverpw")) {
                    Usuario usuario = new Usuario();
                    usuario.setConfiguracion(sesion.getConfiguracion());
                    Conexion cx = new Conexion(sesion);
                    usuario.setCx(cx);
                    usuario.setIp(request.getRemoteAddr());
                    usuario.setNavegador(request.getHeader("user-agent"));
                    String aParametros[] = {"email"};
                    sNextAction = valida_parametros(request, aParametros);
                    if (sNextAction.equals("")) {
                        String p = usuario.recuperaPassword(request.getParameter("email"));
                        if (p.equals("")) {
                            sNextAction = "El correo especificado no está registrado, verifique";
                        } else {
                            try {
                                Mail userMail = new Mail(usuario);
                                userMail.sendEmail("noresponder@fide.org.mx",
                                        request.getParameter("email"),
                                        "Contraseña recuperada",
                                        "<img src='http://ahorrateunaluz.org.mx:8080/slfca/img/fide_banner.png'><br><br><div style='font-size:12px; font-family:arial'>Estimado usuario(a)<br><br>Se envió el siguiente correo como respuesta a su solicitud de recuperación de contraseña.<br><br>Usuario:".concat(request.getParameter("email")).concat("<br>Contraseña:").concat(p).concat("<br><br>Sistema del Programa Ahórrate una luz</div>"),
                                        "");
                                sNextAction = "Su contraseña fue enviada a su correo registrado, verifique";
                            } catch (Exception e) {
                                sNextAction = "<error_mail>".concat(e.getMessage()).concat("</error_mail>");
                            }
                        }
                    }

                } else if (cmd.equals("sms")) {
                    
                    if (request.getSession().getAttribute("usuario") == null) {
                       Usuario usuario = new Usuario();
                       usuario.setIp(request.getRemoteAddr());
                       usuario.setNavegador(request.getHeader("user-agent"));
                       Conexion cx = new Conexion(sesion);
                       int nUsuario = cx.getLogin("sms@fide.org.mx","MercaCel");

                       if (nUsuario != 0) {
                            usuario.setCx(cx);
                            usuario.setConfiguracion(sesion.getConfiguracion());
                            usuario.setClave(nUsuario);
                            usuario.setSesion(request.getSession().getId());
                            request.getSession().setAttribute("usuario", usuario);
                       } else {
                            sNextAction = "<mensaje><![CDATA[Usuario y/o password incorrecto, verifique]]></mensaje>";
                            request.getSession().setAttribute("mensaje_login", "Usuario y/o password incorrecto, verifique");
                            if (request.getParameter("$app") == null) {
                                request.getRequestDispatcher("/login.jsp").forward(request, response);
                            } else if (request.getParameter("$app").equals("none") || request.getParameter("$app").equals("mobile")) {
                                //No hace nada
                            } else {
                                request.getRequestDispatcher("/login.jsp").forward(request, response);
                            }
                        }
                       
                    }
                    
                    request.getRequestDispatcher("/sms.jsp").forward(request, response);
                    
                } else if (cmd.equals("logout")) {
                    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                    sesion = (Sesion) request.getSession().getServletContext().getAttribute("sesion");

                    if (sesion.usuarios.get(usuario.getClave()) != null) {
                        sesion.usuarios.remove(usuario.getClave());
                        sesion.usuariosConectados--;
                    }

                    usuario.logOut(usuario.getCx());
                    usuario = null;
                    request.getSession().removeAttribute("usuario");
                    sNextAction = "Sesión finalizada";
                    request.getSession().setAttribute("mensaje_login", "Sesión finalizada");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                } else {
                    //Verifica que la clave de usuario esté activa
                    if (request.getSession().getAttribute("usuario") == null) {
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                    } else {
                        /* comandos para presentar informacion */
                        if (cmd.equals("vista")) { //Carga jsp indicado
                            request.getRequestDispatcher("/vista.jsp").forward(request, response);
                        } else if (cmd.equals("form")) { //Carga jsp indicado
                            request.getRequestDispatcher("/form.jsp").forward(request, response);
                        } else if (cmd.equals("parameter")) { //xml sin diccionario de datos
                            request.getRequestDispatcher("/parameter.jsp").forward(request, response);
                        } else if (cmd.equals("plain")) { //xml sin diccionario de datos
                            request.getRequestDispatcher("/plain.jsp").forward(request, response);
                        } else if (cmd.equals("plainbypage")) { //xml sin diccionario de datos
                            request.getRequestDispatcher("/plainbypage.jsp").forward(request, response);
                        } else if (cmd.equals("grid")) { //xml para contruir el grid
                            request.getRequestDispatcher("/grid.jsp").forward(request, response);
                        } else if (cmd.equals("tree")) { //xml para contruir el grid
                            request.getRequestDispatcher("/tree.jsp").forward(request, response);
                        } else if (cmd.equals("sesion")) { //xml para menú de sesión
                            request.getRequestDispatcher("/sesion.jsp").forward(request, response);
                        } else if (cmd.equals("app")) { // xml para menú de aplicaciones
                            request.getRequestDispatcher("/app.jsp").forward(request, response);
                        } else if (cmd.equals("menu")) { // xml para menú de aplicaciones
                            request.getRequestDispatcher("/menu.jsp").forward(request, response);
                        } else if (cmd.equals("post")) { // xml para publicar en muro
                            request.getRequestDispatcher("/new_post.jsp").forward(request, response);
                        } else if (cmd.equals("register")) {/* gestión de inserciones y actualizaciones */

                            if (request.getContentType() != null) {
                                if (request.getContentType().contains("multipart/form-data;")) {
                                    request.getRequestDispatcher("/upload.jsp").forward(request, response);

                                } else {
                                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                                }
                            } else {
                                request.getRequestDispatcher("/register.jsp").forward(request, response);
                            }

                        } else if (cmd.equals("download") && request.getParameter("$ta")!=null) { /* generacion de archivos en excel */
                            if (request.getParameter("$ta").equals("excel"))
                                request.getRequestDispatcher("/excel.jsp").forward(request, response);
                        } else if (cmd.equals("download") && request.getParameter("$file")!=null) { /* generacion de archivos en excel */
                            	response.setContentType("text/xml; charset=UTF-8");                                
                                File archivoADescargar = new File(request.getParameter("$file"));
                                response.setHeader("Content-Disposition","attachment;filename=".concat(archivoADescargar.getName()));                                                         
                                InputStream in = null;
                                OutputStream out = null;
                                try {
                                    in = new FileInputStream(archivoADescargar);
                                    out = response.getOutputStream();
                                    IOUtils.copy(in, out);
                                } finally {
                                  IOUtils.closeQuietly(in); //checks for null
                                  IOUtils.closeQuietly(out); //checks for null
                                }                                
                                
                        }else if (cmd.equals("scheduled_download")) {/* descarga programada */
                            request.getRequestDispatcher("/scheduled_download.jsp").forward(request, response);
                        } else if (cmd.equals("report")) {/* controlador de reportes html */

                            String aParametros[] = {"$cr"};
                            sNextAction = valida_parametros(request, aParametros);

                            //El parámetro $tg define a qué jsp se debe redireccionar
                            if (sNextAction.equals("")) {
                                //Si se indica que $tg es el primero, no se sabe qué tipo de reporte se trata;
                                //en este caso el controlador detecta el tipo del primer reporte
                                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                                Reporte r = new Reporte();
                                r.setClaveReporte(Integer.parseInt(request.getParameter("$cr")), usuario.getClavePerfil(), usuario.getCx());
                                //Se redirecciona al jsp correcto de acuerdo al tipo de gráfico
                                request.getRequestDispatcher("/".concat(r.getJsp()).concat(".jsp")).forward(request, response);
                            }

                        } else if (cmd.equals("validate")) { /* Comandos para validar consultas */

                            request.getRequestDispatcher("/validate.jsp").forward(request, response);
                        } else if (cmd.equals("import")) { /* Comando para importar tablas en bulk */

                            request.getRequestDispatcher("/import.jsp").forward(request, response);
                            
                        /*} else if (cmd.equals("importfile")) {

                            Integer claveImportacion =Integer.parseInt(request.getParameter("$ci"));
                            ImportacionEnBackground ieb = (ImportacionEnBackground)getServletContext().getAttribute("ieb");
                            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                            Integer registros=0;
                            
                            if (ieb.getImportacion()==null) {
                                importacion = new Importacion(claveImportacion,usuario);
                                if (importacion.getClaveEstatus()==3) {
                                    sNextAction = "<avance>Importacion finalizada</avance>";
                                } else {
                                    if (!ieb.getError().equals(""))  {
                                        sNextAction =ieb.getError();
                                        ieb.setImportacion(null);
                                    } else {
                                       sNextAction = "<avance>".concat(importacion.getRegistrosProcesados().toString()).concat("</avance><registros>").concat(importacion.getRegistrosPorProcesar().toString()).concat("</registros>");     
                                       ieb.setImportacion(importacion);
                                       ieb.setError("");
                                       sNextAction = "<avance>".concat(importacion.getRegistrosProcesados().toString()).concat("</avance><registros>").concat(importacion.getRegistrosPorProcesar().toString()).concat("</registros>");                                    
                                    }
                                }    
                            } else {                                  
                                if (ieb.getError()==null) {
                                    if (ieb.getError().equals("")) {
                                        if (importacion.getTxt()!=null)
                                         sNextAction = "<avance>".concat(importacion.getTxt().getRegistroActual().toString()).concat("</avance>");

                                        if (importacion.getClaveEstatus()==3) {
                                            request.getSession().removeAttribute("Importacion");
                                            sNextAction = "<avance>Concluido</avance>";
                                        }
                                    } else {
                                        sNextAction="<error>".concat(ieb.getError()).concat("</error>");
                                        ieb.setImportacion(null);
                                    }    
                                } else {
                                    if (ieb.getError().equals("")) {
                                        if (importacion.getTxt()!=null)
                                         sNextAction = "<avance>".concat(importacion.getRegistrosProcesados().toString()).concat("</avance>");

                                        if (importacion.getClaveEstatus()==3) {
                                            request.getSession().removeAttribute("Importacion");
                                            sNextAction = "<avance>Concluido</avance>";
                                        }
                                    } else {
                                        sNextAction="<error>".concat(ieb.getError()).concat("</error>");
                                        ieb.setImportacion(null);
                                    }
                                }
                                    
                            }
                            request.getSession().setAttribute("ieb", ieb);*/
                        } else if (cmd.equals("exists_table")) {
                            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                            if (mx.org.fide.utilerias.Utilerias.existeLaTabla(request.getParameter("tabla"), usuario)) {
                                sNextAction = "<existe_tabla>si</existe_tabla>";
                            } else {
                                sNextAction = "<existe_tabla>no</existe_tabla>";
                            }
                        } else if (cmd.equals("survey")) {  /* Comando para llamar al jsp de encuestas */

                            if (request.getContentType() != null) {
                                if (request.getContentType().contains("multipart/form-data;")) {
                                    request.getRequestDispatcher("/survey_with_upload.jsp").forward(request, response);

                                } else {
                                    request.getRequestDispatcher("/survey.jsp").forward(request, response);
                                }
                            } else {
                                request.getRequestDispatcher("/survey.jsp").forward(request, response);
                            }

                        } else if (cmd.equals("validabeneficiario")) { /* Comando para importar tablas en bulk */

                            request.getRequestDispatcher("/validate_beneficiary.jsp").forward(request, response);
                        } else {
                            sNextAction = "<error><![CDATA[Comando no válido]]></error>";
                        }
                    }  //else de  if (request.getSession().getAttribute("usuario")==null)  
                } //else de comandos que requieren de sesión
            } //if (cmd==null) ... else
        } catch (Fallo f) {
            if (f.getMessage().equals("Usuario no encontrado")) {
                request.getSession().setAttribute("mensaje_login", "Usuario y/o password incorrecto, verifique");
            } else {
                sNextAction += "<error><![CDATA[".concat(f.getMessage()).concat("]]></error>");
                request.getSession().setAttribute("mensaje_login", f.getMessage());
            }

            if (!cmd.equals("testconnection") && !request.getParameter("$app").equals("mobile") && !request.getParameter("$app").equals("none")) {
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            sNextAction = "<error>".concat(e.getMessage()).concat("</error>");
        } finally {
            PrintWriter out = response.getWriter();
            /* Termina comando login */
            out.print("<?xml version='1.0' encoding='UTF-8'?>");
            out.print("<respuesta>");
            out.print("<cmd type='" + cmd + "' />");
            out.print(sNextAction);
            out.print("</respuesta>");
            out.close();
        }
    }

    String valida_parametros(HttpServletRequest request, String args[]) {
        String s = "";
        int i;
        for (i = 0; i < args.length; i++) {
            if (request.getParameter(args[i]) == null) {
                s = "<error><![CDATA[Falta el parámetro " + args[i] + ", verifique]]></error>";
                break;
            }
        }
        return s;
    }

    public void sessionCreated(HttpSessionEvent request) {
        System.out.println("Sesión creada");
        ServletContext apSession = request.getSession().getServletContext();
        Sesion sesion = (Sesion) apSession.getAttribute("sesion");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        /* Si el usuario es nulo quiere decir que no se ha hecho login */
        if (usuario == null) {
            return;
        }

        if (sesion == null) {
            try {
                sesion = new Sesion();
            } catch (Fallo ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (sesion.usuarios.get(usuario.getClave()) == null) {
            sesion.usuarios.put(usuario.getClave(), usuario);
            sesion.usuariosConectados++;
        }

        synchronized (apSession) {
            apSession.setAttribute("sesion", sesion);
        }
    }

    public void sessionDestroyed(HttpSessionEvent request) {
        System.out.println("Session destruida");
        ServletContext contexto = request.getSession().getServletContext();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        synchronized (contexto) {
            Sesion sesion = (Sesion) contexto.getAttribute("sesion");
            if (sesion.getUsuarios().get(usuario.getClave()) != null) {
                sesion.getUsuarios().remove(usuario.getClave());
                sesion.setUsuariosConectados(sesion.getUsuariosConectados() - 1);
            }

            contexto.setAttribute("sesion", sesion);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Capa controladora de AdministraX";
    }// </editor-fold>

}
