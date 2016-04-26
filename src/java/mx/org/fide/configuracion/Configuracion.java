/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.configuracion;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mx.org.fide.archivo.Archivo;
import mx.org.fide.modelo.Fallo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Daniel
 */
public class Configuracion {
    private LinkedHashMap parametros = new LinkedHashMap();
    private String configuracionActual;
    
    public Configuracion() throws Fallo{
        try {
            /*ClassLoader classLoader = Thread.currentThread().getContextClassgetClass().getClassLoader().getResource(".").getPath()Loader();
            URL url = classLoader.getResource("com/administrax/configuracion"); 
            * String path = ;
            * */
            File xml = new File(Configuracion.class.getResource("/mx/org/fide/configuracion/configuracion.xml").toURI());

            //Eliminar esta linea cuando entre a producción
            //xml = new File("configuracion.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList empresas = doc.getElementsByTagName("empresa");
            for (int i = 0; i < empresas.getLength(); i++) {
                Node nodeEmpresa = empresas.item(i);

                if (nodeEmpresa.getNodeType() != Node.ELEMENT_NODE) {
                    throw new Fallo("Tipo de nodo no válido <empresa>");
                }
                
                LinkedHashMap temp = new LinkedHashMap();
                Element empresa = (Element) nodeEmpresa;
                String sEmpresa = getValue("razon_social", empresa);
                temp.put("enterprise_name", sEmpresa);
                temp.put("enterprise_login_logo", getValue("imagen_login", empresa));
                temp.put("enterprise_banner_logo", getValue("imagen_banner", empresa));
                
                NodeList nodeDb = empresa.getElementsByTagName("db");
               
                if (nodeDb.getLength() == 0) {
                    throw new Fallo("Falta nodo db, verifique");
                }
                
                Element db = (Element) nodeDb.item(0);

                temp.put("db_server", getValue("servidor", db));
                temp.put("db_name", getValue("nombre", db));
                temp.put("db_user", getValue("usuario", db));
                temp.put("db_pw", getValue("password", db));
                temp.put("db_type", getValue("tipo", db));                
                
               NodeList nodeEmail = empresa.getElementsByTagName("email");
               
                if (nodeEmail.getLength() == 0) {
                    throw new Fallo("Falta nodo email, verifique");
                }
            
                Element email = (Element) nodeEmail.item(0);

                temp.put("smtp_user", getValue("usuario_smtp", email) );
                temp.put("starttls_enabled", getValue("iniciotls_habilitado", email));
                temp.put("smtp_host", getValue("servidor_smtp", email));
                temp.put("smtp_port", getValue("puerto_smtp", email));
                temp.put("smtp_pw", getValue("password_smtp", email));
                
                //Se agrega al final a la tabla de parametros
                parametros.put(sEmpresa, temp);
            }            
        } catch (FileNotFoundException  fe) {
            throw new Fallo(fe.getMessage());
        } catch (Exception ex) {
            throw new Fallo(ex.getMessage());
        } 
    }
    
    private static String getValue(String tag, Element element) throws Fallo {
        if (element.getElementsByTagName(tag).item(0)==null) {
            throw new Fallo("No se encontró la etiqueta ".concat(tag.toUpperCase()).concat(" en el documento XML"));
        }
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        
        if (node==null)
            return null;
        else            
            return node.getNodeValue();
    }
    
    /*public Configuracion() throws Fallo{
        Archivo archivo = new Archivo();
        try {
            archivo.lee("/com/administrax/archivo/configuracion.properties");
            parametros.put("db_server", archivo.getPropiedades().getProperty("db_server"));
            parametros.put("db_name", archivo.getPropiedades().getProperty("db_name"));
            parametros.put("db_user", archivo.getPropiedades().getProperty("db_user"));
            parametros.put("db_pw", archivo.getPropiedades().getProperty("db_pw"));
            parametros.put("db_type", archivo.getPropiedades().getProperty("db_type"));
            parametros.put("enterprise_logo", archivo.getPropiedades().getProperty("logo"));
            parametros.put("enterprise_name", archivo.getPropiedades().getProperty("logo"));
            parametros.put("smtp_user", archivo.getPropiedades().getProperty("smtp_user"));
            parametros.put("starttls_enabled", archivo.getPropiedades().getProperty("starttls_enabled"));
            parametros.put("smtp_host", archivo.getPropiedades().getProperty("smtp_host"));
            parametros.put("smtp_auth", archivo.getPropiedades().getProperty("smtp_auth"));
            parametros.put("smtp_pw", archivo.getPropiedades().getProperty("smtp_pw"));
            
        } catch(Exception e) {
            System.out.println(e.getStackTrace());
            throw new Fallo("Error al leer el archivo de configuracion:".concat(e.getMessage()) );
        }    
            
        
    }*/

    public LinkedHashMap getParametros() {
        return parametros;
    }

    public void setParametros(LinkedHashMap parametros) {
        this.parametros = parametros;
    }

    public String getConfiguracionActual() {
        return configuracionActual;
    }

    public void setConfiguracionActual(String configuracionActual) {
        this.configuracionActual = configuracionActual;
    }
    
}
