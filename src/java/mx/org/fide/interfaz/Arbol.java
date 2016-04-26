package mx.org.fide.interfaz;

import mx.org.fide.backend.Aplicacion;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Usuario;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
public class Arbol {
    Tree <Aplicacion> menu = null;
    
    public Arbol(Usuario usuario) {
        Tree <Aplicacion> menu = new Tree<Aplicacion>(new Aplicacion());
        HashMap <Integer, Aplicacion> hmAplicaciones= new HashMap <Integer, Aplicacion>();
        Aplicacion aplicacion = null;
        
        Conexion oDb = new Conexion(usuario.getCx().getServer(),usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.clave_aplicacion, a.aplicacion, a.clave_forma_principal, a.clave_aplicacion_padre, f.forma, a.orden")
           .append(" FROM perfil_aplicacion pa, aplicacion a, forma f ")
           .append(" WHERE pa.clave_aplicacion=a.clave_aplicacion AND pa.activo=1 AND a.clave_forma_principal=f.clave_forma AND pa.clave_perfil=")
           .append(usuario.getClavePerfil()).append(" ORDER BY a.clave_aplicacion_padre, a.orden");
        try {
            ResultSet rs = oDb.getRs(sql.toString());
            while (rs.next()) {
                aplicacion = new Aplicacion();
                aplicacion.setClaveAplicacion(rs.getInt("clave_aplicacion"));
                aplicacion.setAplicacion(rs.getString("aplicacion"));
                aplicacion.setClaveAplicacionPadre(rs.getInt("clave_aplicacion_padre"));
                aplicacion.setClaveFormaPrincipal(rs.getInt("clave_forma_principal"));
                aplicacion.setFormaPrincipal(rs.getString("forma"));
                aplicacion.setOrden(rs.getInt("orden"));
                
                hmAplicaciones.put(aplicacion.getClaveAplicacion(), aplicacion);
                
            }
            
            //Ahora se barre el hashmap creando el árbol
            for (int i=0; i<hmAplicaciones.size(); i++) {
                aplicacion=hmAplicaciones.get(i);
                
                if (aplicacion.getClaveAplicacionPadre()==null) {
                    menu.addLeaf(aplicacion);    
                } else {
                    menu.addLeaf(hmAplicaciones.get(aplicacion.getClaveAplicacionPadre()), aplicacion);
                }
            }
        } catch (Exception e) {
            
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }        
    }

    public Tree<Aplicacion> getMenu() {
        return menu;
    }

    public void setMenu(Tree<Aplicacion> menu) {
        this.menu = menu;
    }
    
    public String getHTMLMenu() {
       StringBuffer s = new StringBuffer(); 
       for (Tree<Aplicacion> child : this.getMenu().getSubTrees()) {
          s.append(child.getHead());
       }
       return s.toString();
    }
    
}