/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.importacion;

/**
 *
 * @author daniel.martinez
 */
public class DefinicionImportacionDetalle  {
    private Integer claveDefinicionDetalle;
    private Integer claveDefinicion;
    private Integer columna;
    private Integer claveCampoDefinicion;
    private String valorPredefinido;
    private String campo;
    private String tipoDato;
    private Boolean opcional;
    
    public DefinicionImportacionDetalle() {
    }

    public DefinicionImportacionDetalle(Integer claveDefinicionDetalle) {
        this.claveDefinicionDetalle = claveDefinicionDetalle;
    }

    public DefinicionImportacionDetalle(Integer claveDefinicionDetalle, Integer claveDefinicion, Integer columna, Integer claveCampoDefinicion) {
        this.claveDefinicionDetalle = claveDefinicionDetalle;
        this.claveDefinicion = claveDefinicion;
        this.columna = columna;
        this.claveCampoDefinicion = claveCampoDefinicion;
    }

    public Integer getClaveDefinicionDetalle() {
        return claveDefinicionDetalle;
    }

    public void setClaveDefinicionDetalle(Integer claveDefinicionDetalle) {
        this.claveDefinicionDetalle = claveDefinicionDetalle;
    }

    public Integer getClaveDefinicion() {
        return claveDefinicion;
    }

    public void setClaveDefinicion(Integer claveDefinicion) {
        this.claveDefinicion = claveDefinicion;
    }

    public Integer getColumna() {
        return columna;
    }

    public void setColumna(Integer columna) {
        this.columna = columna;
    }

    public Integer getClaveCampoDefinicion() {
        return claveCampoDefinicion;
    }

    public void setClaveCampoDefinicion(Integer claveCampoDefinicion) {
        this.claveCampoDefinicion = claveCampoDefinicion;
    }

    public String getValorPredefinido() {
        return valorPredefinido;
    }

    public void setValorPredefinido(String valorPredefinido) {
        this.valorPredefinido = valorPredefinido;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }
    
    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Boolean getOpcional() {
        return opcional;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DefinicionImportacionDetalle)) {
            return false;
        }
        DefinicionImportacionDetalle other = (DefinicionImportacionDetalle) object;
        if ((this.claveDefinicionDetalle == null && other.claveDefinicionDetalle != null) || (this.claveDefinicionDetalle != null && !this.claveDefinicionDetalle.equals(other.claveDefinicionDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.org.fide.importacion.DefinicionImportacionDetalle[ claveDefinicionDetalle=" + claveDefinicionDetalle + " ]";
    }
    
}
