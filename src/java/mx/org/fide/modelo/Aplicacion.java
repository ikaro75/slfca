/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.modelo;
import java.util.ArrayList;


/**
 *
 * @author Daniel
 */
public class Aplicacion {
    private Integer claveAplicacion;
    private String aplicacion;
    private String aliasMenuNuevaEntidad;
    private String aliasMenuMuestraEntidad;
    private Integer claveFormaPrincipal;
    private String formaPrincipal;
    private Integer claveAplicacionPadre;
    private Integer claveCategoria;
    private Integer orden;
    
    private ArrayList<FormaX> formas = new  ArrayList<FormaX>();

    public String getAliasMenuMuestraEntidad() {
        return aliasMenuMuestraEntidad;
    }

    public Integer getClaveFormaPrincipal() {
        return claveFormaPrincipal;
    }

    public void setClaveFormaPrincipal(Integer claveFormaPrincipal) {
        this.claveFormaPrincipal = claveFormaPrincipal;
    }

    public void setAliasMenuMuestraEntidad(String aliasMenuMuestraEntidad) {
        this.aliasMenuMuestraEntidad = aliasMenuMuestraEntidad;
    }

    public String getAliasMenuNuevaEntidad() {
        return aliasMenuNuevaEntidad;
    }

    public void setAliasMenuNuevaEntidad(String aliasMenuNuevaEntidad) {
        this.aliasMenuNuevaEntidad = aliasMenuNuevaEntidad;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public Integer getClaveAplicacion() {
        return claveAplicacion;
    }

    public void setClaveAplicacion(Integer claveAplicacion) {
        this.claveAplicacion = claveAplicacion;
    }
        public ArrayList<FormaX> getFormas() {
        return formas;
    }

    public void setFormas(ArrayList<FormaX> formas) {
        this.formas = formas;
    }

    public void pushForma(FormaX forma) {
        this.formas.add(forma);
    }
    
    public void popForma(int i) {
        this.formas.remove(i);
    }

    public String getFormaPrincipal() {
        return formaPrincipal;
    }

    public void setFormaPrincipal(String formaPrincipal) {
        this.formaPrincipal = formaPrincipal;
    }

    public Integer getClaveAplicacionPadre() {
        return claveAplicacionPadre;
    }

    public void setClaveAplicacionPadre(Integer claveAplicacionPadre) {
        this.claveAplicacionPadre = claveAplicacionPadre;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) { 
        this.orden = orden;
    }

    public Integer getClaveCategoria() {
        return claveCategoria;
    }

    public void setClaveCategoria(Integer claveCategoria) {
        this.claveCategoria = claveCategoria;
    }
    
    @Override
    public String toString() {
        return "Aplicacion{" + "claveAplicacion=" + claveAplicacion + ", aplicacion=" + aplicacion + ", aliasMenuNuevaEntidad=" + aliasMenuNuevaEntidad + ", aliasMenuMuestraEntidad=" + aliasMenuMuestraEntidad + ", claveFormaPrincipal=" + claveFormaPrincipal + ", formas=" + formas + '}';
    }
    
}
