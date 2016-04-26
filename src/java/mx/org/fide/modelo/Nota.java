/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.modelo;

/**
 *
 * @author Daniel
 */
public class Nota {
    private int claveNota;
    private int claveForma;
    private int claveRegistro;
    private int claveEmpleado;
    private String titulo;
    private String mensaje;
    private String fechaNota;
    private String foto;
    private String nombre;

    public int getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(int claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    public int getClaveForma() {
        return claveForma;
    }

    public void setClaveForma(int claveForma) {
        this.claveForma = claveForma;
    }

    public int getClaveNota() {
        return claveNota;
    }

    public void setClaveNota(int claveNota) {
        this.claveNota = claveNota;
    }

    public int getClaveRegistro() {
        return claveRegistro;
    }

    public void setClaveRegistro(int claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

    public String getFechaNota() {
        return fechaNota;
    }

    public void setFechaNota(String fechaNota) {
        this.fechaNota = fechaNota;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Nota(int claveNota, int claveForma, int claveRegistro, int claveEmpleado, String titulo, String mensaje, String fechaNota, String foto, String nombre) {
        this.claveNota = claveNota;
        this.claveForma = claveForma;
        this.claveRegistro = claveRegistro;
        this.claveEmpleado = claveEmpleado;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaNota = fechaNota;
        this.foto = foto;
        this.nombre = nombre; 
    }
    
    
    
}
