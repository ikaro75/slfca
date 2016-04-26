package mx.org.fide.encuesta;

import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import mx.org.fide.utilerias.Utilerias;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Cuestionario extends Consulta {

    private Integer claveCuestionario;
    private String cuestionario;
    private String descripcion;
    private Boolean mostrarNumeracion;
    private Boolean extraePreguntasAleatorias;
    private Integer numeroDePreguntas;
    private String encabezado;
    private String pieDePagina;
    private Integer orden;
    private String evento;
    private Integer claveEstatus;
    private ArrayList<Seccion> secciones;

    public Integer getClaveCuestionario() {
        return claveCuestionario;
    }

    public void setClaveCuestionario(Integer claveCuestionario) {
        this.claveCuestionario = claveCuestionario;
    }

    public Integer getClaveEstatus() {
        return claveEstatus;
    }

    public void setClaveEstatus(Integer claveEstatus) {
        this.claveEstatus = claveEstatus;
    }

    public String getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(String cuestionario) {
        this.cuestionario = cuestionario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public Boolean getExtraePreguntasAleatorias() {
        return extraePreguntasAleatorias;
    }

    public void setExtraePreguntasAleatorias(Boolean extraePreguntasAleatorias) {
        this.extraePreguntasAleatorias = extraePreguntasAleatorias;
    }

    public Boolean getMostrarNumeracion() {
        return mostrarNumeracion;
    }

    public void setMostrarNumeracion(Boolean mostrarNumeracion) {
        this.mostrarNumeracion = mostrarNumeracion;
    }

    public Integer getNumeroDePreguntas() {
        return numeroDePreguntas;
    }

    public void setNumeroDePreguntas(Integer numeroDePreguntas) {
        this.numeroDePreguntas = numeroDePreguntas;
    }

    public String getPieDePagina() {
        return pieDePagina;
    }

    public void setPieDePagina(String pieDePagina) {
        this.pieDePagina = pieDePagina;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
    
    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public ArrayList<Seccion> getSecciones() throws Fallo {
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Seccion seccion = null;
        this.secciones = new ArrayList<Seccion>();

        try {
            rs = oDb.getRs("select clave_seccion from fide_seccion_pregunta where clave_cuestionario=".concat(String.valueOf(this.claveCuestionario)).concat(" ORDER BY orden"));

            while (rs.next()) {
                seccion = new Seccion(rs.getInt("clave_seccion"), super.getUsuario());
                this.secciones.add(seccion);
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar las secciones del cuestionario: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        }

        return secciones;
    }

    public void setSecciones(ArrayList<Seccion> secciones) {
        this.secciones = secciones;
    }

    public Integer getPrimeraSeccion() throws Fallo {
        Integer primeraSeccion;
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select top 1 orden, clave_seccion from fide_seccion_pregunta where clave_cuestionario=".concat(this.claveCuestionario.toString()).concat(" order by orden asc"));
            if (!rs.next()) {
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("Error al recuperar la primera sección del cuestionario.");
            } else {
                primeraSeccion = rs.getInt("clave_seccion");
                rs.close();
                oDb.cierraConexion();
                return primeraSeccion;
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la primera sección del cuestionario: ".concat(e.getMessage()));
        }

    }

    public Integer getUltimaSeccion() throws Fallo {
        Integer ultimaSeccion;
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select top 1 orden, clave_seccion from fide_seccion_pregunta where clave_cuestionario=".concat(this.claveCuestionario.toString()).concat(" order by orden desc"));
            if (!rs.next()) {
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("Error al recuperar la última sección del cuestionario.");
            } else {
                ultimaSeccion = rs.getInt("clave_seccion");
                rs.close();
                oDb.cierraConexion();
                return ultimaSeccion;
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la última sección del cuestionario: ".concat(e.getMessage()));
        }

    }

    public Integer getPrimeraPregunta() throws Fallo {
        Integer primeraPregunta;
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            if (oDb.getDbType()==mx.org.fide.modelo.Conexion.DbType.MYSQL)
                rs = oDb.getRs("select orden, clave_pregunta from fide_pregunta where clave_seccion=".concat(this.getPrimeraSeccion().toString()).concat(" order by orden asc limit 0,1"));
            else if (oDb.getDbType()==mx.org.fide.modelo.Conexion.DbType.MSSQL) 
                rs = oDb.getRs("select top 1 orden, clave_pregunta from fide_pregunta where clave_seccion=".concat(this.getPrimeraSeccion().toString()).concat(" order by orden asc"));
            
            if (!rs.next()) {
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("Error al recuperar la primera pregunta del cuestionario.");
            } else {
                primeraPregunta = rs.getInt("clave_pregunta");
                rs.close();
                oDb.cierraConexion();
                return primeraPregunta;
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la primera pregunta del cuestionario: ".concat(e.getMessage()));
        }

    }
        
    public Integer getUltimaPregunta() throws Fallo {
        Integer ultimaPregunta;
        StringBuilder q = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        try {
            rs = oDb.getRs("select orden, clave_pregunta from fide_pregunta where clave_seccion=".concat(this.getUltimaSeccion().toString()).concat(" order by orden desc limit 0,1"));
            if (!rs.next()) {
                rs = null;
                oDb.cierraConexion();
                oDb = null;
                throw new Fallo("Error al recuperar la última pregunta del cuestionario.");
            } else {
                ultimaPregunta = rs.getInt("clave_pregunta");
                rs.close();
                oDb.cierraConexion();
                return ultimaPregunta;
            }
        } catch (Exception e) {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo("Error al recuperar la última pregunta del cuestionario: ".concat(e.getMessage()));
        }

    }
    
    public Cuestionario(Integer claveCuestionario, Usuario usuario) throws Fallo {   
        super.setTabla("fide_cuestionario");
        super.setLlavePrimaria("clave_cuestionario");
        super.setPk(String.valueOf(claveCuestionario));
        super.setUsuario(usuario);
        
        StringBuilder q = new StringBuilder();
        Conexion oDb = null;
        ResultSet rs = null;
        
        try {
            oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
            rs = oDb.getRs("select * from fide_cuestionario where clave_cuestionario=".concat(String.valueOf(claveCuestionario)));

            if (rs.next()) {
                this.claveCuestionario = rs.getInt("clave_cuestionario");
                this.cuestionario = rs.getString("cuestionario");
                this.descripcion = rs.getString("descripcion");
                this.encabezado = rs.getString("encabezado");
                this.pieDePagina = rs.getString("pie_de_pagina");
                this.evento = rs.getString("evento");
                this.claveEstatus = rs.getInt("clave_estatus");
                this.orden = rs.getInt("orden");
            } else {
                throw new Fallo("No se encontró el cuestionario especificado");
            }
        } catch (Exception e) {
            throw new Fallo("Error al recuperar el cuestionario: ".concat(e.getMessage()));
        } finally {
            rs = null;
            oDb.cierraConexion();
            oDb = null;
        }
    } 

    public Cuestionario(Consulta c) throws Fallo {
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
            if (c.getCampos().get("clave_cuestionario").getValor() != null) {
                this.claveCuestionario = Integer.parseInt(c.getCampos().get("clave_cuestionario").getValor());
            }

            if (c.getCampos().get("cuestionario").getValor() != null) {
                this.cuestionario =c.getCampos().get("cuestionario").getValor();
            } else {
                throw new Fallo("No se especificó el nombre del cuestionario, verifique");
            }

            if (c.getCampos().get("descripcion").getValor() != null) {
                this.descripcion =c.getCampos().get("descripcion").getValor();
            } 
           
            if (c.getCampos().get("evento").getValor() != null) {
                this.evento =c.getCampos().get("evento").getValor();
            }
            
            if (c.getCampos().get("mostrar_numeracion").getValor() != null) {
                this.mostrarNumeracion =  Boolean.parseBoolean(c.getCampos().get("mostrar_numeracion").getValor());
            } 

            if (c.getCampos().get("extrae_preguntas_aleatorias") != null) {
                if (c.getCampos().get("extrae_preguntas_aleatorias").getValor() != null) {
                    this.extraePreguntasAleatorias =  Boolean.parseBoolean(c.getCampos().get("extrae_preguntas_aleatorias").getValor());
                }     
            } 
            
            if (c.getCampos().get("numero_de_preguntas").getValor() != null) {
                    this.numeroDePreguntas = Integer.parseInt(c.getCampos().get("numero_de_preguntas").getValor());
            }
            
            
            if (c.getCampos().get("encabezado").getValor() != null) {
                this.encabezado =c.getCampos().get("encabezado").getValor();
            }
           
            if (c.getCampos().get("pie_de_pagina").getValor() != null) {
                this.encabezado =c.getCampos().get("pie_de_pagina").getValor();
            }

            if (c.getCampos().get("orden").getValor() != null) {
                this.orden = Integer.parseInt(c.getCampos().get("orden").getValor());
            } else {
                throw new Fallo("No se especificó el orden, verifique");
            }
                       
            if (c.getCampos().get("clave_estatus").getValor() != null) {
                this.claveEstatus = Integer.parseInt(c.getCampos().get("clave_estatus").getValor());
            } else {
                throw new Fallo("No se especificó el estatus del cuestionario, verifique");
            }
            
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }
        
    public String insert() throws Fallo {
        StringBuilder resultado = new StringBuilder();
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());

        resultado.append(super.insert(true));

        if (resultado.toString().split("<error>").length > 1) {
            oDb.cierraConexion();
            oDb = null;
            throw new Fallo(resultado.toString().split("<error><!\\[")[1].replaceAll("CDATA\\[", "").replaceAll("]]></error>", ""));
        }

        //Se debe recuperar la llave primaria de la aplicación
        this.claveCuestionario = Integer.parseInt(resultado.toString().split("<pk>")[1].replace("</pk>", ""));
        return resultado.toString();
    }

    public String update() throws Fallo {
        StringBuilder resultado = new StringBuilder();
        return resultado.append(super.update(true)).toString();
    }

    public String delete() throws Fallo {
        //Pseudocodigo:
        //1. Verificar que no tiene secciones
        StringBuilder resultado = new StringBuilder();
        ResultSet rs = null;
        Conexion oDb = new Conexion(super.getUsuario().getCx().getServer(), super.getUsuario().getCx().getDb(), super.getUsuario().getCx().getUser(), super.getUsuario().getCx().getPw(), super.getUsuario().getCx().getDbType());
        Integer secciones;
        try {
            rs = oDb.getRs("select count(clave_seccion) as secciones from fide_seccion_pregunta where clave_cuestionario=".concat(this.claveCuestionario.toString()));

            if (rs.next()) {
                if (rs.getInt("secciones") > 0) {
                    throw new Fallo("Se requiere eliminar las secciones del cuestionario antes, verifique");
                } else {
                    resultado.append(super.delete(true,super.getUsuario()));
                }
            } else {
                throw new Fallo("Error al recuperar las secciones del cuestionario");
            }

            return resultado.toString();
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }

    }

    /*@Override
    public String duplicate(int claveEmpleado, String ip, String browser, int forma, Conexion cx) throws Fallo {
        StringBuilder resultadoXML = new StringBuilder();
        StringBuilder q = new StringBuilder();
        Conexion oDb = new Conexion(cx.getServer(), cx.getDb(), cx.getUser(), cx.getPw(), cx.getDbType());
        ResultSet rs = null;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Integer claveCuestionarioOrigen=Integer.parseInt(super.getCampos().get("claveCuestionario").getValorOriginal());
        //1. Abre transaccion
        try {
            if (oDb.getDbType() == Conexion.DbType.MYSQL) {
                oDb.execute("START TRANSACTION");
            } else if (oDb.getDbType() == Conexion.DbType.MSSQL) {
                oDb.execute("BEGIN TRANSACTION");
                oDb.execute("SET DATEFORMAT YMD");
            }

            //2. Duplica el cuestionario
            q = new StringBuilder();
            q.append(super.insert(claveEmpleado, ip, browser, forma, cx, false));

            resultadoXML.append("<duplicate_cuestionario><![CDATA[").append(q.toString()).append("]]></duplicate_cuestionario>");
            rs = oDb.execute(q.toString());

            if (rs != null) {
                if (rs.next()) {
                    this.claveCuestionario = rs.getInt(1);
                    resultadoXML.append("<pk>").append(this.claveCuestionario).append("</pk>");

                    //Inserta en bitácora
                    oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,".concat("clave_forma,clave_registro) ").concat("VALUES(").concat(String.valueOf(claveEmpleado)).concat(",'").concat(dateFormatter.format(new Date())).concat("',2,'").concat(ip).concat("','").concat(browser).concat("',").concat(String.valueOf(forma)).concat(",").concat(String.valueOf(this.claveCuestionario)).concat(")"));

                } else {
                    throw new Fallo("Error al recuperar clave de bitacora");
                }
            }

            //3. Duplica secciones
            
            for (Seccion seccion : new Cuestionario(claveCuestionarioOrigen, cx).getSecciones(cx)) {
                q = new StringBuilder();
                q.append("INSERT INTO fide_seccion_pregunta (seccion, claveCuestionario, instruccion, orden, numeroDePreguntasAlAzarDeEstaSeccion)").append("VALUES(").append(Utilerias.c34(seccion.getSeccion())).append(",").append(this.claveCuestionario.toString()).append(",").append(Utilerias.c34(seccion.getInstruccion())).append(",").append(seccion.getOrden().toString()).append(",").append(seccion.getNumeroDePreguntasAlAzarDeEstaSeccion().toString()).append(")");

                resultadoXML.append("<duplicate_cuestionario><![CDATA[").append(q.toString()).append("]]></duplicate_cuestionario>");
                rs = oDb.execute(q.toString());

                if (rs != null) {
                    if (rs.next()) {
                        Integer clave_seccion = rs.getInt(1);
                        resultadoXML.append("<pk>").append(clave_seccion).append("</pk>");

                        //Inserta en bitácora
                        oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,".concat("clave_forma,clave_registro) ").concat("VALUES(").concat(String.valueOf(claveEmpleado)).concat(",'").concat(dateFormatter.format(new Date())).concat("',2,'").concat(ip).concat("','").concat(browser).concat("',").concat(String.valueOf(forma)).concat(",").concat(String.valueOf(clave_seccion)).concat(")"));

                        for (Pregunta pregunta : seccion.getPreguntas(cx)) {
                            q = new StringBuilder();
                            q.append("INSERT INTO fide_pregunta (clave_seccion, instruccion, pregunta, claveTipoPregunta, orden, claveTipoDatoRespuesta, obligatoria, textoFinal)").append("VALUES(").append(clave_seccion.toString()).append(",").append(Utilerias.c34(pregunta.getInstruccion())).append(",").append(Utilerias.c34(pregunta.getPregunta())).append(",").append(pregunta.getClaveTipoPregunta().toString()).append(",").append(pregunta.getOrden().toString()).append(",").append(pregunta.getClaveTipoDatoRespuesta().toString()).append(",").append(pregunta.getObligatoria().toString()).append(",").append(Utilerias.c34(pregunta.getTextoFinal())).append(")");

                            resultadoXML.append("<duplicate_cuestionario><![CDATA[").append(q.toString()).append("]]></duplicate_cuestionario>");
                            rs = oDb.execute(q.toString());

                            if (rs != null) {
                                if (rs.next()) {
                                    Integer clavePregunta = rs.getInt(1);
                                    resultadoXML.append("<pk>").append(clavePregunta).append("</pk>");

                                    //Inserta en bitácora
                                    oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,".concat("clave_forma,clave_registro) ").concat("VALUES(").concat(String.valueOf(claveEmpleado)).concat(",'").concat(dateFormatter.format(new Date())).concat("',2,'").concat(ip).concat("','").concat(browser).concat("',").concat(String.valueOf(forma)).concat(",").concat(String.valueOf(clavePregunta)).concat(")"));

                                    for (Respuesta respuesta : pregunta.getRespuestas(cx)) {
                                        q = new StringBuilder();
                                        q.append("INSERT INTO fide_respuesta (clavePregunta, codigoRespuesta, respuesta, siEstaEsLaRespuesta, accion, respuestaCorrecta, otro)").append("VALUES(").append(clavePregunta.toString()).append(",").append(Utilerias.c34(respuesta.getCodigoRespuesta())).append(",").append(Utilerias.c34(respuesta.getRespuesta())).append(",").append(respuesta.getSiEstaEsLaRespuesta()).append(",").append(respuesta.getAccion()).append(",").append(respuesta.getRespuestaCorrecta()).append(",").append(respuesta.getOtro()).append(")");

                                        resultadoXML.append("<duplicate_cuestionario><![CDATA[").append(q.toString()).append("]]></duplicate_cuestionario>");
                                        rs = oDb.execute(q.toString());

                                        if (rs != null) {
                                            if (rs.next()) {
                                                Integer claveRespuesta = rs.getInt(1);
                                                resultadoXML.append("<pk>").append(claveRespuesta).append("</pk>");

                                                //Inserta en bitácora
                                                oDb.execute("INSERT INTO be_bitacora (clave_empleado,fecha,clave_tipo_evento,ip,navegador,".concat("clave_forma,clave_registro) ").concat("VALUES(").concat(String.valueOf(claveEmpleado)).concat(",'").concat(dateFormatter.format(new Date())).concat("',2,'").concat(ip).concat("','").concat(browser).concat("',").concat(String.valueOf(forma)).concat(",").concat(String.valueOf(claveRespuesta)).concat(")"));
                                            }

                                        } else {
                                            throw new Fallo("Error al recuperar clave de bitacora");
                                        }
                                    }

                                }

                            } else {
                                throw new Fallo("Error al recuperar clave de bitacora");
                            }
                        }
                    } else {
                        throw new Fallo("Error al recuperar clave de bitacora");
                    }

                } else {
                    throw new Fallo("Error al recuperar clave de bitacora");
                }
            }

            oDb.execute("COMMIT");

        } catch (Exception e) {
            oDb.execute("ROLLBACK");
            throw new Fallo(resultadoXML.toString().concat("<detalles>").concat(e.getMessage()).concat("</detalles>"));
        }

        return resultadoXML.toString();
    } */
}
