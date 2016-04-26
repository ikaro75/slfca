/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.encuesta;

import mx.org.fide.mail.Mail;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EstudioParticipante extends Consulta{
    private Integer claveEstudioParticipante;
    private Integer claveEstudio;
    private Integer claveParticipante;
    private Integer claveCuestionarioActual;
    private Date fecha;
    private Integer claveEstatus;

    public Integer getClaveCuestionarioActual() {
        return claveCuestionarioActual;
    }

    public void setClaveCuestionarioActual(Integer claveCuestionarioActual) { 
        this.claveCuestionarioActual = claveCuestionarioActual;
    }

    public Integer getClaveEstudio() {
        return claveEstudio;
    }

    public void setClaveEstudio(Integer claveEstudio) {
        this.claveEstudio = claveEstudio;
    }

    public Integer getClaveEstudioParticipante() {
        return claveEstudioParticipante;
    }

    public void setClaveEstudioParticipante(Integer claveEstudioParticipante) {
        this.claveEstudioParticipante = claveEstudioParticipante;
    }

    public Integer getClaveParticipante() {
        return claveParticipante;
    }

    public void setClaveParticipante(Integer claveParticipante) {
        this.claveParticipante = claveParticipante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getClaveEstatus() {
        return claveEstatus;
    }

    public void setClaveEstatus(Integer claveEstatus) {
        this.claveEstatus = claveEstatus;
    }

    public Integer getPrimerCuestionario() throws Fallo {
        Integer primerCuestionario;
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select orden, claveCuestionario from fide_cuestionario where claveEstudio=".concat(this.claveEstudio.toString()).concat(" order by orden asc limit 0,1"));
            if (!rs.next()) {
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("Error al recuperar el primer cuestionario del estudio.");
            } else {
                primerCuestionario = rs.getInt("claveCuestionario");
                rs.close();
                oDb.cierraConexion();
                return primerCuestionario;
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar el primer cuestionario del estudio: ".concat(e.getMessage()));
        }

    }
        
    public Integer getUltimoCuestionario() throws Fallo {
        Integer ultimoCuestionario;
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select orden, claveCuestionario from fide_cuestionario where claveEstudio=".concat(this.claveEstudio.toString()).concat(" order by orden desc limit 0,1"));
            if (!rs.next()) {
                throw new Fallo("Error al recuperar el última cuestionario del estudio.");
            } else {
                ultimoCuestionario = rs.getInt("claveCuestionario");
                return ultimoCuestionario;
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar el último cuestionario del estudio: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        }

    }

    public Integer getCuestionarioAnterior() throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer cuestionarioAnterior;
        Cuestionario cuestionario = new Cuestionario(this.claveCuestionarioActual, super.getUsuario());
        try {            
            rs = oDb.getRs("select claveCuestionario from fide_cuestionario where orden<".concat(cuestionario.getOrden().toString()).concat(" and claveEstudio=".concat(this.claveEstudio.toString()).concat(" order by orden limit 0,1")));
            if (rs.next()) {
                cuestionarioAnterior = rs.getInt("claveCuestionario");
                rs.close();
                oDb.cierraConexion();
                return  cuestionarioAnterior;
            } else {
                 throw new Fallo("No hay cuestionario anterior");
            }
        } catch (Exception e) {
                 throw new Fallo(e.getMessage());
        }
    }

    public Integer getCuestionarioSiguiente() throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer cuestionarioSiguiente;
        Cuestionario cuestionario = new Cuestionario(this.claveCuestionarioActual, super.getUsuario());
        try {            
            rs = oDb.getRs("select claveCuestionario from fide_cuestionario where orden>".concat(cuestionario.getOrden().toString()).concat(" and claveEstudio=".concat(this.claveEstudio.toString()).concat(" order by orden limit 0,1")));

            if (rs.next()) {
                cuestionarioSiguiente = rs.getInt("claveCuestionario");
                rs.close();
                oDb.cierraConexion();
                return cuestionarioSiguiente;
            } else {
                 throw new Fallo("No hay cuestionario posterior");
            }
        } catch (Exception e) {
                 throw new Fallo(e.getMessage());
        }
    }
    
    public EstudioParticipante(Integer claveEstudio, Usuario usuario) throws Fallo {
        super.setTabla("fide_estudio_participante");
        super.setLlavePrimaria("claveEstudioParticipante");
        super.setPk(String.valueOf(claveEstudio));
        super.setUsuario(usuario);

        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select * from fide_estudio_participante where claveEstudio=".concat(String.valueOf(claveEstudio)).concat(" AND claveParticipante=").concat(claveParticipante.toString()));

            if (rs.next()) {      
                this.claveEstudioParticipante= rs.getInt("claveEstudioParticipante");
                this.claveEstudio=rs.getInt("claveEstudio");
                this.claveParticipante=rs.getInt("claveParticipante");
                this.claveCuestionarioActual=rs.getInt("claveCuestionarioActual");
                this.fecha=rs.getDate("fecha");
                this.claveEstatus=rs.getInt("claveEstatus");
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

   public EstudioParticipante(Consulta c) throws Fallo {
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
        StringBuilder q = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            if (c.getCampos().get("claveestudioparticipante").getValor() != null) {
                this.claveEstudioParticipante = Integer.parseInt(c.getCampos().get("claveestudioparticipante").getValor());
            }

            if (c.getCampos().get("claveestudio").getValor() != null) {
                this.claveEstudio = Integer.parseInt(c.getCampos().get("claveestudio").getValor());
            } else {
                throw new Fallo("No se especificó la clave del estudio, verifique");
            }

            if (c.getCampos().get("claveparticipante").getValor() != null) {
                this.claveParticipante = Integer.parseInt(c.getCampos().get("claveparticipante").getValor());
            } else {
                throw new Fallo("No se especificó la clave del participante, verifique");
            }

            if (c.getCampos().get("fecha") == null) {
                c.getCampos().get("fecha").setValor(formatter.format(new Date()));
                this.fecha = new Date();
            } else if (c.getCampos().get("fecha").getValor() == null) {
                c.getCampos().get("fecha").setValor(formatter.format(new Date()));
                this.fecha = new Date();
            } else if (c.getCampos().get("fecha").getValor().equals("")) {
                c.getCampos().get("fecha").setValor(formatter.format(new Date()));
                this.fecha = new Date();
            } else {
                this.fecha = formatter.parse(c.getCampos().get("fecha").getValor());
            }

            if (c.getCampos().get("clavecuestionarioactual") == null) {
                Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
                ResultSet rs;
                q = new StringBuilder();
                q.append("SELECT claveCuestionario FROM fide_cuestionario WHERE claveEstudio=").append(String.valueOf(this.claveEstudio)).append(" ORDER BY orden ASC LIMIT 0,1");
                rs = oDb.getRs(q.toString());
                if (!rs.next()) {
                    throw new Fallo("No fue posible identificar el primer cuestionario del estudio seleccionado, verifique");
                } else {
                    this.claveCuestionarioActual = rs.getInt("claveCuestionario");
                    c.getCampos().get("clavecuestionarioActual").setValor(String.valueOf(this.claveCuestionarioActual));
                }

            } else if (c.getCampos().get("clavecuestionarioactual").getValor() == null || c.getCampos().get("clavecuestionarioactual").getValor().equals("") ) {
                Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
                ResultSet rs;
                q = new StringBuilder();
                q.append("SELECT claveCuestionario FROM fide_cuestionario WHERE claveEstudio=").append(String.valueOf(this.claveEstudio)).append(" ORDER BY orden ASC LIMIT 0,1");
                rs = oDb.getRs(q.toString());
                if (!rs.next()) {
                    throw new Fallo("No fue posible identificar el primer cuestionario del estudio seleccionado, verifique");
                } else {
                    this.claveCuestionarioActual = rs.getInt("clavecuestionario");
                    c.getCampos().get("clavecuestionarioactual").setValor(String.valueOf(this.claveCuestionarioActual));
                }
            } else {
                this.claveCuestionarioActual = Integer.parseInt(c.getCampos().get("clavecuestionarioactual").getValor());
            }
                        
            if (c.getCampos().get("claveestatus").getValor() != null) {
                this.claveEstatus = Integer.parseInt(c.getCampos().get("claveestatus").getValor());
            } else {
                throw new Fallo("No se especificó el estatus del estudio, verifique");
            }
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }
   
    public String insert() throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer claveBitacora;
        CuestionarioParticipante primerCuestionario;

        try {
            //Verifica si no ha sido de dada de alta la relación 
            q.append("SELECT claveEstudio FROM fide_estudio_participante WHERE claveEstudio=").append(this.claveEstudio).append(" AND claveParticipante=").append(this.claveParticipante);
            rs = oDb.getRs(q.toString());
            if (rs.next()) {
                rs.close();
                throw new Fallo("Este estudio ya ha sido asignado a este usuario anteriormente, verifique");
            }

            //1. Abre transaccion
            if (oDb.getDbType() == Conexion.DbType.MYSQL) {
                oDb.execute("START TRANSACTION");
            } else if (oDb.getDbType() == Conexion.DbType.MSSQL) {
                oDb.execute("BEGIN TRANSACTION");
                oDb.execute("SET DATEFORMAT YMD");
            }

            //2. Inserta nuevo fide_estudio_participante

            resultadoXML.append("<insert_estudioParticipante>").append(super.insert(oDb)).append("</insert_estudioParticipante>");
            
            //3. Inserta primer cuestionario del estudio   
            Consulta consulta= new Consulta(368, "insert", "0", "", null,super.getUsuario());
            consulta.getCampos().get("claveEstudioParticipante").setValor(this.claveEstudioParticipante.toString());
            consulta.getCampos().get("claveParticipante").setValor(this.claveParticipante.toString());
            consulta.getCampos().get("claveCuestionario").setValor(this.claveCuestionarioActual.toString()); 
            consulta.getCampos().get("claveEstatus").setValor("1"); 
            primerCuestionario = new CuestionarioParticipante(consulta); 
            //hace falta llenar los valores del alta de CuestionarioParticipante
            primerCuestionario.insert(oDb);

            oDb.execute("COMMIT");

        } catch (Exception e) {
            oDb.execute("ROLLBACK");
            throw new Fallo(resultadoXML.toString().concat("<detalles>").concat(e.getMessage()).concat("</detalles>"));
        } finally {
            oDb.cierraConexion();
            oDb=null;
        }
        return resultadoXML.toString();
    }

    public String insert(Conexion oDb) throws Fallo {
        //Validación de acuerdo a tipo de transaccion
        //Reglas de inserción
        ResultSet rs;
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Integer claveBitacora;
        CuestionarioParticipante primerCuestionario;

        try {
            //Verifica si no ha sido de dada de alta la relación 
            q.append("SELECT claveEstudio FROM fide_estudio_participante WHERE claveEstudio=").append(this.claveEstudio).append(" AND claveParticipante=").append(this.claveParticipante);
            rs = oDb.getRs(q.toString());
            if (rs.next()) {
                rs.close();
                throw new Fallo("Este estudio ya ha sido asignado a este usuario anteriormente, verifique");
            }

            //2. Inserta nuevo fide_estudio_participante
            resultadoXML.append("<insert_estudioParticipante>").append(super.insert(oDb)).append("</insert_estudioParticipante>");
            
            //3. Inserta primer cuestionario del estudio   
            Consulta consulta= new Consulta(367, "insert", "0", "", null,super.getUsuario());
            consulta.getCampos().get("claveestudioparticipante").setValor(super.getPk());
            consulta.getCampos().get("claveparticipante").setValor(this.claveParticipante.toString());
            consulta.getCampos().get("clavecuestionario").setValor(this.claveCuestionarioActual.toString()); 
            consulta.getCampos().get("claveestatus").setValor("1"); 
            primerCuestionario = new CuestionarioParticipante(consulta); 
            //hace falta llenar los valores del alta de CuestionarioParticipante
            resultadoXML.append(primerCuestionario.insert(oDb));

        } catch (Exception e) {
            throw new Fallo(resultadoXML.toString().concat("<detalles>").concat(e.getMessage()).concat("</detalles>"));
        } 
        return resultadoXML.toString();
    }
}
