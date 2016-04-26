package mx.org.fide.interfaz;

import java.sql.ResultSet;
import java.util.ArrayList;
import mx.org.fide.modelo.Aplicacion;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Usuario;

public class Interfaz {
    private ArrayList <Aplicacion> aplicaciones = new ArrayList <Aplicacion>();
    private Usuario usuario = null;
    
    public Interfaz(Usuario usuario) {
        this.usuario=usuario;
        this.getAplicaciones();
    }
    
    public ArrayList <Aplicacion> getAplicaciones() {
        this.aplicaciones=new ArrayList <Aplicacion>();
        Conexion oDb = new Conexion(usuario.getCx().getServer(),usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.clave_aplicacion, a.aplicacion, a.clave_forma_principal, a.clave_aplicacion_padre, f.forma, a.orden")
           .append(" FROM be_perfil_aplicacion pa, be_aplicacion a, be_forma f ")
           .append(" WHERE pa.clave_aplicacion=a.clave_aplicacion AND pa.activo=1 AND a.clave_forma_principal=f.clave_forma AND pa.clave_perfil=")
           .append(usuario.getClavePerfil()).append(" ORDER BY a.clave_aplicacion_padre, a.orden");
        try {
            ResultSet rs = oDb.getRs(sql.toString());
            while (rs.next()) {
                Aplicacion aplicacion = new Aplicacion();
                aplicacion.setClaveAplicacion(rs.getInt("clave_aplicacion"));
                aplicacion.setAplicacion(rs.getString("aplicacion"));
                aplicacion.setClaveAplicacionPadre(rs.getInt("clave_aplicacion_padre"));
                aplicacion.setClaveFormaPrincipal(rs.getInt("clave_forma_principal"));
                aplicacion.setFormaPrincipal(rs.getString("forma"));
                aplicacion.setOrden(rs.getInt("orden"));
                this.aplicaciones.add(aplicacion);
            }
            
        } catch (Exception e) {
            
        } finally {
            oDb.cierraConexion();
            oDb = null;
        }
        return this.aplicaciones;    
    }
    
    public String getAppMenu(Integer appPadre, Integer appActual) {
        StringBuilder html = new StringBuilder();
        StringBuilder htmlSubApps = new StringBuilder();

        if (appPadre==null) {
            appPadre=0;
        }

        if (appActual==0){
            appActual=aplicaciones.get(0).getClaveAplicacion();
        }

        try {                     
            for (Aplicacion aplicacion: aplicaciones) {

                //Las aplicaciones vienen en orden por aplicación padre y por orden
                //De entrada se imprimen en este nivel solamennte cuyos padres cumplen con el criterio
                if (aplicacion.getClaveAplicacionPadre().intValue() ==appPadre.intValue()){
                    html.append("<li ");

                    if (aplicacion.getClaveAplicacion().intValue()==appActual.intValue()) 
                         html.append("class='Selected'>");
                    else 
                        html.append(">");

                    html.append("<a href='vista.html?$cmd=plainbypage&$ta=select&page=1&rows=20&sidx=&sord=desc&$w=&$om=form&$cf=").append(aplicacion.getClaveFormaPrincipal()).append("&app=").append(aplicacion.getClaveAplicacion()).append("' data-ajax='false'>").append(aplicacion.getAplicacion()).append("</a>");
                    htmlSubApps = new StringBuilder().append(getAppMenu(aplicacion.getClaveAplicacion(), appActual));
                    html.append(htmlSubApps).append("</li>");
                }
            }    
        } catch(Exception e) {
            html.append(e.getMessage());
        } finally  {
            return html.length()>0? "<ul>".concat(html.append("</ul>").toString()):"";
        }
    }
    public void setAplicaciones(ArrayList<Aplicacion> aplicaciones) {
        this.aplicaciones = aplicaciones;
    }

    
}