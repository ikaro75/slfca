package mx.org.fide.backend;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;


public class EquivalenciaCampo extends Consulta {
    private Integer claveEquivalenciaDetalle;
    private int claveEquivalencia;
    private String campoAnterior;
    private String campoNuevo;

    public EquivalenciaCampo() {
    }

    public EquivalenciaCampo(Integer claveEquivalenciaDetalle, Usuario usuario) {
        this.claveEquivalenciaDetalle = claveEquivalenciaDetalle;
    }

    public EquivalenciaCampo(Integer claveEquivalenciaDetalle, int claveEquivalencia, String campoAnterior) {
        this.claveEquivalenciaDetalle = claveEquivalenciaDetalle;
        this.claveEquivalencia = claveEquivalencia;
        this.campoAnterior = campoAnterior;
    }
    
    public EquivalenciaCampo(Consulta c) throws Fallo {
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder q = new StringBuilder();
        try {
            Object o = this;
            Class<?> clazz = o.getClass();
            Method setter = null;

            for (Field field : clazz.getDeclaredFields()) {
                StringBuilder dbField = new StringBuilder();
                String[] aTemp = field.getName().split("(?=\\p{Upper})");
                for (Integer k=0; k<aTemp.length; k++) {
                    if (k+1<aTemp.length) {
                        dbField.append(aTemp[k].toLowerCase()).append("_");
                    } else {
                        dbField.append(aTemp[k].toLowerCase());
                    }
                }

                if (c.getCampos().get(dbField.toString()) != null) {
                    if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("varchar")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), String.class);
                    } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("int")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Integer.class);
                    } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("float"))  {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Float.class);
                    } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("decimal")) { 
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Double.class);
                    } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), java.util.Date.class);
                    }
                    
                    if (c.getCampos().get(dbField.toString()).getValor() != null && !c.getAccion().equals("delete")) {

                        //Valida si el valor del campo es una cadena vacío 
                        if (c.getCampos().get(dbField.toString()).getValor().equals("") && !c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("varchar")) {
                            if (c.getCampos().get(dbField.toString()).getObligatorio() == 1 && !c.getCampos().get(field.getName()).isAutoIncrement()) {
                                throw new Fallo("El valor del campo ".concat(c.getCampos().get(dbField.toString()).getAlias()).concat(" no es válido, verifique"));
                            }
                        } else {
                            if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("varchar")) {
                                setter.invoke(this, c.getCampos().get(dbField.toString()).getValor());
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("int")) {
                                setter.invoke(this, Integer.parseInt(c.getCampos().get(dbField.toString()).getValor()));
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("float")) {
                                setter.invoke(this, Float.parseFloat(c.getCampos().get(dbField.toString()).getValor()));
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("decimal")) {
                                setter.invoke(this, Double.parseDouble(c.getCampos().get(dbField.toString()).getValor()));    
                            } else if (c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(dbField.toString()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                                setter.invoke(this, formatter.parse(c.getCampos().get(dbField.toString()).getValor()));
                            }
                        }
                    } else if (c.getCampos().get(dbField.toString()).isAutoIncrement()) {
                        setter.invoke(this, Integer.parseInt(c.getPk()));
                    } else if (c.getCampos().get(dbField).getObligatorio() == 1) {
                        throw new Fallo("No se especificó ".concat(c.getCampos().get(dbField.toString()).getAlias()).concat(", verifique"));
                    }
                } 
            }

            //Aquí debe ir el código para cargar desde el constructor los detalles del pagare
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }
    
    public Integer getClaveEquivalenciaDetalle() {
        return claveEquivalenciaDetalle;
    }

    public void setClaveEquivalenciaDetalle(Integer claveEquivalenciaDetalle) {
        this.claveEquivalenciaDetalle = claveEquivalenciaDetalle;
    }

    public int getClaveEquivalencia() {
        return claveEquivalencia;
    }

    public void setClaveEquivalencia(int claveEquivalencia) {
        this.claveEquivalencia = claveEquivalencia;
    }

    public String getCampoAnterior() {
        return campoAnterior;
    }

    public void setCampoAnterior(String campoAnterior) {
        this.campoAnterior = campoAnterior;
    }

    public String getCampoNuevo() {
        return campoNuevo;
    }

    public void setCampoNuevo(String campoNuevo) {
        this.campoNuevo = campoNuevo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (claveEquivalenciaDetalle != null ? claveEquivalenciaDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquivalenciaCampo)) {
            return false;
        }
        EquivalenciaCampo other = (EquivalenciaCampo) object;
        if ((this.claveEquivalenciaDetalle == null && other.claveEquivalenciaDetalle != null) || (this.claveEquivalenciaDetalle != null && !this.claveEquivalenciaDetalle.equals(other.claveEquivalenciaDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.org.fide.backend.BeEquivalenciaCampo[ claveEquivalenciaDetalle=" + claveEquivalenciaDetalle + " ]";
    }
    
}
