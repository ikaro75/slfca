package mx.org.fide.encuesta;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Participante extends Consulta {

    private Integer claveParticipante;
    private String nombre;
    private String apellidos;
    private String email;
    private String iniciales;
    private String numeroSitioDeInvestigacion;
    private String numeroDeParticipante;
    private Integer numeroDeAleatorizacion;
    private Integer claveEmpleado;
    private Integer claveEstatus;

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(Integer claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    public Integer getClaveEstatus() {
        return claveEstatus;
    }

    public void setClaveEstatus(Integer claveEstatus) {
        this.claveEstatus = claveEstatus;
    }

    public Integer getClaveParticipante() {
        return claveParticipante;
    }

    public void setClaveParticipante(Integer claveParticipante) {
        this.claveParticipante = claveParticipante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIniciales() {
        return iniciales;
    }

    public void setIniciales(String iniciales) {
        this.iniciales = iniciales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumeroDeAleatorizacion() {
        return numeroDeAleatorizacion;
    }

    public void setNumeroDeAleatorizacion(Integer numeroDeAleatorizacion) {
        this.numeroDeAleatorizacion = numeroDeAleatorizacion;
    }

    public String getNumeroDeParticipante() {
        return numeroDeParticipante;
    }

    public void setNumeroDeParticipante(String numeroDeParticipante) {
        this.numeroDeParticipante = numeroDeParticipante;
    }

    public String getNumeroSitioDeInvestigacion() {
        return numeroSitioDeInvestigacion;
    }

    public void setNumeroSitioDeInvestigacion(String numeroSitioDeInvestigacion) {
        this.numeroSitioDeInvestigacion = numeroSitioDeInvestigacion;
    }

    public Participante(Consulta c) throws Fallo {
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
                if (c.getCampos().get(field.getName()) != null) {
                    if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("varchar")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), String.class);
                    } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("int")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Integer.class);
                    } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("float") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("decimal")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Float.class);
                    } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                        setter = clazz.getMethod("set".concat(field.getName().substring(0, 1).toUpperCase()).concat(field.getName().substring(1)), Date.class);
                    }

                    if (c.getCampos().get(field.getName()).getValor() != null && !c.getAccion().equals("delete")) {

                        //Valida si el valor del campo es una cadena vacío 
                        if (c.getCampos().get(field.getName()).getValor().equals("") && !c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("varchar")) {
                            if (c.getCampos().get(field.getName()).getObligatorio() == 1 && !c.getCampos().get(field.getName()).isAutoIncrement()) {
                                throw new Fallo("El valor del campo ".concat(c.getCampos().get(field.getName()).getAlias()).concat(" no es válido, verifique"));
                            }
                        } else {
                            if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("varchar")) {
                                setter.invoke(this, c.getCampos().get(field.getName()).getValor());
                            } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("int")) {
                                setter.invoke(this, Integer.parseInt(c.getCampos().get(field.getName()).getValor()));
                            } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("float") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("decimal")) {
                                setter.invoke(this, Float.parseFloat(c.getCampos().get(field.getName()).getValor()));
                            } else if (c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("date") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldate") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("smalldatetime") || c.getCampos().get(field.getName()).getTipoDato().toLowerCase().equals("datetime")) {
                                setter.invoke(this, formatter.parse(c.getCampos().get(field.getName()).getValor()));
                            }
                        }
                    } else if (c.getCampos().get(field.getName()).isAutoIncrement()) {
                        setter.invoke(this, Integer.parseInt(c.getPk()));
                    } else if (c.getCampos().get(field.getName()).getObligatorio() == 1) {
                        throw new Fallo("No se especificó ".concat(c.getCampos().get(field.getName()).getAlias()).concat(", verifique"));
                    }
                }
            }

            //Aquí debe ir el código para cargar desde el constructor los detalles del pagare


        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public Participante(Integer claveParticipante, Usuario usuario) throws Fallo {
        super.setTabla("fide_participante");
        super.setLlavePrimaria("claveParticipante");
        super.setPk(String.valueOf(claveParticipante));
        super.setUsuario(usuario);

        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_participante where claveParticipante=".concat(String.valueOf(claveParticipante)));

            if (rs.next()) {
                this.claveParticipante = rs.getInt("claveParticipante");
                this.nombre = rs.getString("nombre");
                this.apellidos = rs.getString("apellidos");
                this.email = rs.getString("email");
                this.iniciales = rs.getString("iniciales");
                this.numeroSitioDeInvestigacion = rs.getString("numeroSitioDeInvestigacion");
                this.numeroDeParticipante = rs.getString("numeroDeParticipante");
                this.numeroDeAleatorizacion = rs.getInt("numeroDeAleatorizacion");
                this.claveEmpleado = rs.getInt("claveEmpleado");
                this.claveEstatus = rs.getInt("claveEstatus");
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;

            } else {
                rs.close();
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("No se encontró el cuestionario especificado");
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar el cuestionario: ".concat(e.getMessage()));
        }
    }

    public String insert() throws Fallo {
        Consulta c = null;
        ResultSet rs;
        StringBuilder q = new StringBuilder();
        StringBuilder resultadoXML = new StringBuilder();
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Boolean startedTransaction = false;
        Integer claveBitacora;

        try {

            //1. Abre transaccion
            startedTransaction = true;
            if (oDb.getDbType() == Conexion.DbType.MYSQL) {
                oDb.execute("START TRANSACTION");
            } else if (oDb.getDbType() == Conexion.DbType.MSSQL) {
                oDb.execute("BEGIN TRANSACTION");
                oDb.execute("SET DATEFORMAT YMD");
            }

            //2. Inserta participante
            q = new StringBuilder();
            resultadoXML.append("<insert_participante>".concat(super.insert(oDb)).concat("</insert_participante>"));

            c = new Consulta(368, "insert", "0", "", null, super.getUsuario());
            c.getCampos().get("claveestudio").setValor("1");
            c.getCampos().get("claveparticipante").setValor(this.getPk());
            c.getCampos().get("claveestatus").setValor("1");

            //3. Inserta estudio del participante
            resultadoXML.append(new EstudioParticipante(c).insert(oDb));

            oDb.execute("COMMIT");
            oDb.cierraConexion();
            oDb = null;
            return resultadoXML.toString();
        } catch (Exception e) {

            if (startedTransaction) {
                oDb.execute("ROLLBACK");
            }

            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(e.getMessage());
        }
    }
}
