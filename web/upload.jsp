<?xml version="1.0" encoding="UTF-8"?>
<%@ page import="java.util.ArrayList"
         %><%@ page import="mx.org.fide.modelo.*"
         %><%@ page import="mx.org.fide.utilerias.*"
         %><%@page import="java.util.HashMap"
         %><%@page import="org.apache.tomcat.util.http.fileupload.FileItem" 
         %><%@page import="java.util.List"
         %><%@page import="org.apache.tomcat.util.http.fileupload.DiskFileUpload"
         %><%@page import="java.io.File"
         %><%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.nio.channels.FileChannel"%>
<%@page import="java.io.IOException"%>
<%@page import="mx.org.fide.backend.Forma"%><%
//request.setCharacterEncoding("UTF-8");
    response.setContentType("text/xml;charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");

    String error = "";
    int forma = 0;;
    String tipoAccion = "";
    String w = "";
    String source = "";
    String result = "";
    int pk = 0;
    Forma frmTemp = new Forma();
    response.setContentType("text/xml");
    Usuario user = (Usuario) request.getSession().getAttribute("usuario");

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }

    HashMap requestParameters = new HashMap();

    DiskFileUpload requestWU = new DiskFileUpload();
    requestWU.setSizeMax(-1); // 2MB
    requestWU.setSizeThreshold(20480); // tamaño por encima del cual los ficheros son escritos directamente en disco
    File folder = new File("C:\\slfca\\".concat(String.valueOf(user.getClave())));

    if (!folder.exists()) {
        folder.mkdir();
    }

    try {
        requestWU.setRepositoryPath("C://slfca//".concat(String.valueOf(user.getClave())));
        List requestFields = requestWU.parseRequest(request);

        for (int i = 0; i < requestFields.size(); i++) {
            FileItem fiField = (FileItem) requestFields.get(i);
            if (fiField.getName() != null && !fiField.getName().equals("")) {
                String fileName = Utilerias.decodeURIComponentX(fiField.getName());
                String extension = fileName.substring(fileName.indexOf(".") + 1);
                if (extension.equalsIgnoreCase("jsp")
                        || extension.equalsIgnoreCase("php")
                        || extension.equalsIgnoreCase("js")
                        || extension.equalsIgnoreCase("com")
                        || extension.equalsIgnoreCase("dll")
                        || extension.equalsIgnoreCase("pl")
                        || extension.equalsIgnoreCase("py")
                        || extension.equalsIgnoreCase("exe")
                        || extension.equalsIgnoreCase("vb")
                        || extension.equalsIgnoreCase("reg")
                        || extension.equalsIgnoreCase("perl")
                        || extension.equalsIgnoreCase("java")
                        || extension.equalsIgnoreCase("bat")
                        || extension.equalsIgnoreCase("asp")) {
                    //Se borra el archivo
                    fiField.delete();
                    throw new Fallo("Tipo de archivo no permitido");
                }

                try {
                    // construimos un objeto file para recuperar el trayecto completo
                    File archivo = new File(Utilerias.decodeURIComponent((fileName)));

                    //Se busca en el directorio si no hay un archivo del mismo nombre 
                    File DirectorioDeCargas = new File("c:\\slfca\\");
                    File[] listaDeArchivos = DirectorioDeCargas.listFiles();
                    if (listaDeArchivos != null) {
                        for (File archivoDeLaLista : listaDeArchivos) {
                            if (archivoDeLaLista.isDirectory()) {
                                File[] listaDeArchivosSubCarpeta = archivoDeLaLista.listFiles();
                                for (File archivoDeLaListaSubcarpeta : listaDeArchivosSubCarpeta) {
                                    if (fileName.equalsIgnoreCase(archivoDeLaListaSubcarpeta.getName()) && archivoDeLaListaSubcarpeta.isFile()) {
                                        throw new Fallo("El archivo especificado ya existe en la ruta ".concat(archivoDeLaListaSubcarpeta.getAbsolutePath()));
                                    }
                                }
                            } else if (fileName.equalsIgnoreCase(archivoDeLaLista.getName()) && archivoDeLaLista.isFile()) {
                                //Se revisa adentro de los directorios contenidos 

                                throw new Fallo("El archivo especificado ya existe en la ruta ".concat(archivoDeLaLista.getAbsolutePath()));
                            }
                        }
                    }

                    //Se guarda el archivo en la librería
                    archivo = new File("c:\\slfca\\".concat(String.valueOf(user.getClave())).concat("\\").concat(archivo.getName()));
                    // escribimos el fichero colgando del nuevo path
                    fiField.write(archivo);

                    //Se deja una copia en el caché
                    String sAppPath = application.getRealPath("/").replace("\\build", "");

                    File forderDestino = new File(sAppPath.concat("\\temp\\").concat(String.valueOf(user.getClave())).concat("\\"));
                    if (!forderDestino.exists()) {
                        forderDestino.mkdir();
                    }

                    File archivoDestino = new File(forderDestino.getAbsolutePath().concat("\\").concat(archivo.getName()));

                    if (!archivoDestino.exists()) {
                        archivoDestino.createNewFile();
                    }

                    try {
                        FileChannel origen = new FileInputStream(archivo).getChannel();
                        FileChannel destino = new FileOutputStream(archivoDestino).getChannel();

                        if (destino != null && origen != null) {
                            destino.transferFrom(origen, 0, origen.size());
                        }
                        if (origen != null) {
                            origen.close();
                        }
                        if (destino != null) {
                            destino.close();
                        }
                    } catch (Exception e) {
                        error = "Error - No fue posible escribir el archivo en la carpeta temporal: ".concat(e.getMessage());
                    }

                    requestParameters.put(fiField.getFieldName(), Utilerias.decodeURIComponent(archivo.getAbsolutePath()));
                } catch (IOException ioe) {
                    System.out.printf(ioe.getMessage());
                    error = ioe.getMessage();
                    //throw new Fallo(ioe.getMessage() );
                } catch (Exception e) {
                    System.out.printf(e.getMessage());
                    error = e.getMessage();
                    //throw new Fallo(e.getMessage() );
                }
            } else {
                //Va construyendo el queryParameter para llamar por get a register.jsp
                requestParameters.put(fiField.getFieldName(), Utilerias.decodeURIComponentXX(fiField.getString()));
            }

        }
    } catch (Exception e) {
        System.out.printf(e.getMessage());
        error = e.getMessage();
    }

    try {
        if (requestParameters.get("$cf") == null) {
            throw new Fallo("Falta parámetro $cf");
        } else {
            forma = Integer.parseInt(requestParameters.get("$cf").toString());
        }

        if (requestParameters.get("$ta") == null) {
            throw new Fallo("Falta parámetro $ta");
        } else {
            tipoAccion = requestParameters.get("$ta").toString();
        }

        if (requestParameters.get("$pk") != null) {
            pk = Integer.parseInt(requestParameters.get("$pk").toString());
        }

        if (requestParameters.get("$w") != null) {
            w = requestParameters.get("$w").toString();
        }

    } catch (Fallo f) {
        error = f.getMessage();
    } catch (Exception e) {
        error = e.getMessage();
    } finally {%>
<qry><%
    if (!error.equals("")) {%>
    <error><![CDATA[<%=error%>]]></error>
</qry><%
        return;
    }%>
<%
    }

    try {
        //Si no está abierto el objeto forma
        frmTemp = new Forma(new Consulta(forma, tipoAccion, String.valueOf(pk), w, null, user), false);
        source = frmTemp.getSQL();

        if (!tipoAccion.equals("delete")) {
            String valor;
            for (Campo campo : frmTemp.getCampos().values()) {
                if (!campo.isAutoIncrement()) {
                    if (requestParameters.get(campo.getNombre()) != null || (campo.getTipoDato().equals("bit") || campo.getTipoDato().equals("TINYINT"))) {
                        //valor=Utilerias.decodeURIComponentXX(request.getParameter(campo.getNombre()));
                        if ((campo.getTipoDato().equals("bit") || campo.getTipoDato().equals("TINYINT")) && requestParameters.get(campo.getNombre()) == null) {
                            valor = "0";
                        } else {
                            valor = requestParameters.get(campo.getNombre()).toString();
                        }

                        campo.setValor(valor);
                    }
                }
            }
        }
    } catch (Fallo f) {
        error = f.getMessage();
    } catch (Exception e) {
        error = e.getMessage();
    } finally {%>
<sql><![CDATA[<%=source%>]]></sql>
    <% if (!error.equals("")) {%>
<error><![CDATA[<%=error%>]]></error>
</qry>
<%
            return;
        }
    }

%>  <clave_consulta><%=frmTemp.getClaveConsulta()%></clave_consulta><%
    try {
        Desencadenador d = new Desencadenador(frmTemp);

        if (tipoAccion.equals("insert")) {
            result = d.insert();
        } else if (tipoAccion.equals("update")) {
            result = d.update();
        } else if (tipoAccion.equals("delete")) {
            result = d.delete();
        } else if (tipoAccion.equals("duplicate")) {
            result = d.duplicate();
        }

    } catch (Fallo f) {
        error = f.getMessage();
    } catch (Exception e) {
        if ((e.getMessage() != null)) {
            error = e.getMessage();
        } else {
            error = "Error no identificado";
        }
    } finally {
        if (!error.equals("")) {%>
<error><![CDATA[<%=error%>]]></error>
</qry>
<%
        return;
    }%>
<result><%=result%></result>
</qry><%
        /* guarda el objeto usuario en la sesión para aprovechar los objetos que se tiene abiertos */
        request.getSession().setAttribute("usuario", user);

        //Recarga  el objeto usuario si es que se actualizó
        if (frmTemp.getTabla().equals("be_empleado") && requestParameters.get("$pk").equals(String.valueOf(user.getClave()))) {
            user.setClave(user.getClave());
        }

    }%>