/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.utilerias;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;


/**
 *  Utilierías diversas que utiliza el sistema
 */
public class Utilerias {
    /**
     * Decodifica URIs 
     * @param encodedURI Cadena codificada
     * @return           Cadena decodificada
     */
    public static String decodeURIComponent(String encodedURI) {
  char actualChar;
 
  StringBuffer buffer = new StringBuffer();
 
  int bytePattern, sumb = 0;
 
  for (int i = 0, more = -1; i < encodedURI.length(); i++) {
   actualChar = encodedURI.charAt(i);
 
   switch (actualChar) {
    case '%': {
     actualChar = encodedURI.charAt(++i);
     int hb = (Character.isDigit(actualChar) ? actualChar - '0'
       : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
     actualChar = encodedURI.charAt(++i);
     int lb = (Character.isDigit(actualChar) ? actualChar - '0'
       : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
     bytePattern = (hb << 4) | lb;
     break;
    }
    case '+': {
     bytePattern = ' ';
     break;
    }
    default: {
     bytePattern = actualChar;
    }
   }
 
   if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
    sumb = (sumb << 6) | (bytePattern & 0x3f);
    if (--more == 0)
     buffer.append((char) sumb);
   } else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
    buffer.append((char) bytePattern);
   } else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
    sumb = bytePattern & 0x1f;
    more = 1;
   } else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
    sumb = bytePattern & 0x0f;
    more = 2;
   } else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
    sumb = bytePattern & 0x07;
    more = 3;
   } else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
    sumb = bytePattern & 0x03;
    more = 4;
   } else { // 1111110x
    sumb = bytePattern & 0x01;
    more = 5;
   }
  }
  return buffer.toString();
 }
   
    /**
     * Decodifica URIs
     * @param s Cadena codificada
     * @return  Cadena decodificada
     */
    public static String decodeURIComponentX(String s)
  {
    if (s == null)
    {
      return null;
    }

    String result = null;

    try
    {
      result = URLDecoder.decode(s, "UTF-8");
    }

    // This exception should never occur.
    catch (UnsupportedEncodingException e)
    {
      result = s;   
    }

    return result;
  }

    /**
     * Decodifica  URI
     * @param s Cadena codificada
     * @return  Cadena decodificada
     */
    public static String decodeURIComponentXX(String s)
  { 
    if (s == null)
    {
      return null;
    }
    return s.replace("Ã¡", "á").replace("Ã©", "é").replace("Ã­", "í").replace("Ã³", "ó").replace("Ãº", "ú").replace("Ã±", "ñ").replace("Ã", "Á").replace("Ã", "É").replace("Ã", "Í").replace("Ã", "Ó").replace("Ã", "Ú").replace("Ã", "Ñ").replace("Â´", "´").replace("Ã¼", "ü").replace("Ã","Ü");
  }
   
    /**
     *  Verifica si una cadena es numérica
     * @param cadena    Cadena a verificar
     * @return          Verdadero si la cadena es numérica, falso de otro modo
     */
    public static boolean isNumeric(String cadena){
	try {
		Integer.parseInt(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
	}
    }
   
    /**
     * Rectifica cadenas con ' para evitar inyección de código SQL
     * @param cadena    Cadena a rectificar
     * @return          Cadena rectificada
     */
    public static String c34(String cadena){
        if (cadena!=null) {
          return "'".concat(cadena.replaceAll("'", "''")).concat("'");
        } else {
          return "null";  
        }
        
    }
   
    /**
     * Rectifica StringBuilder con ' para evitar inyección de código SQL
     * @param cadena
     * @return
     */
    public static String c34(StringBuilder cadena){
          return cadena.toString().replaceAll("'", "''");
    }
    

     /**
     * Recupera la fecha actual del servidor
     * @return Objeto de tipo Date con la fecha actual del servidor
     */
    public static Date getDateServidor() {
        ResultSet oRs = null;
        Date fechaServidor = null;
        Conexion cx = null;
        try{
            cx = new Conexion();
            String q="";
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         
            if (cx.getDbType()==mx.org.fide.modelo.Conexion.DbType.MYSQL ) {
                q="select now()";
            }
            try {
                oRs= cx.getRs(q);
                oRs.next();
                fechaServidor = formatoFecha.parse(oRs.getString(1));
                oRs.close();
            } catch (Exception e) {
                e.getMessage();
            }     
        } catch(Fallo ex) {
            System.out.println("Error: ".concat(ex.getMessage()));   
        }
        finally{
            cx.cierraConexion();
            return fechaServidor;
        }
    }
    
    public static Boolean existeLaTabla(String tabla, Usuario usuario) {
        ResultSet rs = null;
        Conexion cx = null;
        StringBuilder q = new StringBuilder();
        try{
            cx = usuario.getCx();
            if (cx.getDbType()==mx.org.fide.modelo.Conexion.DbType.MSSQL){
                  q = new StringBuilder("SELECT so.name as table_name FROM SYSOBJECTS as so ")
                              .append("WHERE so.XTYPE='u' AND so.name = '").append(tabla).append("'");
          }

          rs = cx.getRs(q.toString());
          return rs.next();
          
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }
}
