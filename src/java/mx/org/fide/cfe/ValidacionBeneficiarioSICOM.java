package mx.org.fide.cfe;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 *
 * @author daniel.martinez
 */
public class ValidacionBeneficiarioSICOM {
    
    public String estatusSolicitud="";
    private String rpu;
    private String nombre;
    private Integer claveEstado;
    private Integer claveMunicipio;
    private String poblacion;
    private String direccion;
    private Integer clavePunto;
    private Integer claveTipoPadron;
    private Integer claveEstadoEntrega;
    private Integer claveMunicipioEntrega;
    private Integer claveLocalidadEntrega;
    private String fechaEntrega;
    private String fechaRegistro;
    private String mensajeValidacion;
    private Integer claveEstatusImportacionDetalle;
    
    public String valida(String rpu, String idControl, Integer origenValidacion, Usuario usuario) throws Fallo {
        ResultSet rs;
        ResultSet rsPuntoEntrega;
        ResultSet rsFechaProgramada;
        ResultSet rsBeneficiario;
        StringBuilder resultado = new StringBuilder();
        StringBuilder q = new StringBuilder();
        Conexion cx = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
        String mensajeValidacion ="";
        try {
            //1. Verifica si ya está en el padrón actual
            q.append("SELECT clave_beneficiario, nombre,")
             .append("(SELECT id_control FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto) as id_control, ")
             .append("(SELECT punto_entrega FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto) as punto_entrega, ")
             .append("(SELECT direccion FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto) as direccion, ")
            .append("(SELECT localidad FROM fide_localidad WHERE clave_localidad=(SELECT clave_localidad FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto)) as localidad, ")                    
             .append("(SELECT municipio FROM fide_municipio WHERE clave_municipio=(SELECT clave_municipio FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto)) as municipio, ")
             .append("(SELECT estado FROM fide_estado WHERE clave_estado=(SELECT clave_estado FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto)) as estado, ") 
             .append("clave_tipo_padron, pagina")       
             .append(" FROM fide_beneficiario WHERE rpu='").append(rpu).append("'");
            rs = cx.getRs(q.toString());

            if (rs.next()) {
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El RPU consultado se encuentra registrado en la lista de beneficiarios del programa \"Ahórrate una luz\","+rs.getString("clave_beneficiario");
                } else if (origenValidacion==2 || origenValidacion==3) {
                    if (idControl.equals(rs.getString("id_control"))) {
                        this.mensajeValidacion = "El usuario ya se encuentra registrado en la misma Tienda DICONSA, en la hoja ".concat(rs.getString("pagina"));
                        if (rs.getInt("clave_tipo_padron")==1) {
                            this.claveEstatusImportacionDetalle = 11;
                        }    
                        else {
                             this.claveEstatusImportacionDetalle = 13;
                        }     
                    } else {
                        
                        if (rs.getInt("clave_tipo_padron")==1) {
                            this.claveEstatusImportacionDetalle = 12;
                        }    
                        else {
                             this.claveEstatusImportacionDetalle = 14;
                        }
                        
                       this.mensajeValidacion = "Ya está registrado, pase por sus lámparas a la tienda ".concat(rs.getString("punto_entrega")).concat(" ubicada en ")
                                            .concat(rs.getString("direccion")).concat(", ").concat(rs.getString("localidad"))
                                            .concat(", ").concat(rs.getString("municipio")).concat(", ").concat(rs.getString("estado"))
                                            .concat(", en la hoja ").concat(rs.getString("pagina")); 
                    } 
                }
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }
                        
            //2. Verifica si fue dado de alta en padrones anteriores
            q = new StringBuilder().append("SELECT programa FROM fide_padron_anterior WHERE rpu='").append(rpu).append("'");
            rs = cx.getRs(q.toString());

            if (rs.next()) {
                String sPrograma="";
                if (rs.getInt("programa")==1) {
                    sPrograma ="Luz sustentable I";
                    this.claveEstatusImportacionDetalle = 4;
                } else if (rs.getInt("programa")==2) {
                    sPrograma ="Luz sustentable II";
                    this.claveEstatusImportacionDetalle = 5;
                } else if (rs.getInt("programa")==3) {
                    sPrograma ="Piloto en el Estado de Michoacán";
                    this.claveEstatusImportacionDetalle = 6;
                } else if (rs.getInt("programa")==4) {
                    sPrograma ="Piloto en el Estado de Sonora";
                    this.claveEstatusImportacionDetalle = 7;
                } 
                  else if (rs.getInt("programa")==5) {
                    sPrograma ="Piloto en el Estado de Chihuahua";
                    this.claveEstatusImportacionDetalle = 7;
                } 
                  else if (rs.getInt("programa")==6) {
                    sPrograma ="Piloto en el Estado de Guerrero";
                    this.claveEstatusImportacionDetalle = 7;
                } 
                
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El RPU consultado participó en el Programa '".concat(sPrograma).concat("' y por lo tanto no puede ser beneficiario del Programa \"Ahórrate una luz\"");
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "El usuario participó en programas anteriores";
                }
                
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }            
            
            // 3. verifica si existe el RPU en SICOM -- Se elimina esta validación a solicitud de Subdirección de programas
            Trama t = new Trama(rpu); 
            
            // 3. Verifica que el RPU se encuentra en la tabla FIDE_PADRON_CFE
            /*/q = new StringBuilder().append("SELECT * FROM fide_padron_cfe WHERE rpu='").append(rpu).append("'");
            rsBeneficiario = cx.getRs(q.toString());

            if (!rsBeneficiario.next()) {
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El RPU consultado no existe; favor de verificar";
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "El número de servicio del usuario no se encuentra en la base de datos";
                    this.claveEstatusImportacionDetalle = 9;
                }
                
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            } */                 
            
            if (t.getError()!=null){
                throw new Fallo(t.getError());
            }
            
            if (t.getEstatus().equals("S1")) {
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El RPU consultado no existe; favor de verificar";
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "El número de servicio del usuario no se encuentra en la base de datos";
                    this.claveEstatusImportacionDetalle = 9;
                }
                
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }           
            
            if (t.getEstatus().equals("S6")) {
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El RPU proporcionado no se encuentra activo y por lo tanto no puede ser beneficiario del programa \"Ahórrate una luz\"";
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "El servicio de energía no se encuentra activo";
                }
                this.claveEstatusImportacionDetalle = 3;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }
            
            //Valida que la tarifa sea de 1A a 1F
            if (t.getTarifa().equals("01") || t.getTarifa().equals("1A") || t.getTarifa().equals("1B") || t.getTarifa().equals("1C") || 
                t.getTarifa().equals("1D") || t.getTarifa().equals("1E") || t.getTarifa().equals("1F")) {}
            else {
                 if (origenValidacion==1) {
                    this.mensajeValidacion = "El RPU consultado no se encuentra dentro de las tarifas participantes en el Programa \"Ahórrate una luz\"";
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "La tarifa del usuario no participa en el Programa";
                }
                this.claveEstatusImportacionDetalle = 8;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }    
            
            //Valida que la tarifa no sea DAC
            if (t.getDAC().equals("1")) {
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El RPU consultado no se encuentra dentro de las tarifas participantes en el Programa \"Ahórrate una luz\"";
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "La tarifa del usuario no participa en el Programa";
                }
                
                this.claveEstatusImportacionDetalle = 8;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            } 
            
            //Valida que el estado y municipio del beneficiario está dentro del catálogo 
            
            /*String claveEstadoCFE = rs.getString("clavedomun").substring(0, 0);
            String claveMunicipioCFE = rs.getString("clavedomun").substring(1, 3);
            
            //4. Extrae la equivalencia de la clave de estado 
            q = new StringBuilder().append("SELECT clave_estado FROM fide_estado WHERE codigo_estado_cfe='").append(claveEstadoCFE).append("'");
            rs = cx.getRs(q.toString());
            if (rs.next()) {
                this.claveEstado= rs.getInt("clave_estado");
            } else {
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El Estado del beneficiario no está en el Programa \"Ahórrate una luz\"";
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "La localidad no es participante en el programa";
                }
                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }
            */
            
            int claveEstado =0;
            //4. Extrae la equivalencia de la clave de estado 
            q = new StringBuilder().append("SELECT clave_estado FROM fide_estado WHERE codigo_estado_cfe='").append(t.getClaveEstado()).append("'");
            rs = cx.getRs(q.toString());
            if (rs.next()) {
                claveEstado= rs.getInt("clave_estado");
            } else {
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El Estado del beneficiario no está en el Programa \"Ahórrate una luz\"";
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "La localidad no es participante en el programa";
                }
                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            } 
            
            
            //4. Extrae la equivalencia de la clave de estado 
            q = new StringBuilder().append("SELECT clave_municipio FROM fide_municipio WHERE clave_estado=").append(claveEstado).append(" AND clave_municipio_cfe='").append(/*claveMunicipioCFE*/t.getClaveMunicipio()).append("'");
            rs = cx.getRs(q.toString());
            if (rs.next()) {
                claveMunicipio= rs.getInt("clave_municipio");
            } else {
                 
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El municipio del beneficiario no está en el Programa \"Ahórrate una luz\"";
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "La localidad no es participante en el programa";
                }
                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }
                    
            int claveMunicipio =0;
            //4. Extrae la equivalencia de la clave de estado 
            q = new StringBuilder().append("SELECT clave_municipio FROM fide_municipio WHERE clave_estado=").append(claveEstado).append(" AND clave_municipio_cfe='").append(/*claveMunicipioCFE*/t.getClaveMunicipio()).append("'");
            rs = cx.getRs(q.toString());
            if (rs.next()) {
                claveMunicipio= rs.getInt("clave_municipio");
            } else {
                 
                if (origenValidacion==1) {
                    this.mensajeValidacion = "El municipio del beneficiario no está en el Programa \"Ahórrate una luz\"";
                } else if (origenValidacion==2 || origenValidacion==3) {
                    this.mensajeValidacion = "La localidad no es participante en el programa";
                }
                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }
            
            this.rpu =rpu;
            this.nombre= /*rsBeneficiario.getString("nombre").toUpperCase();*/ t.getNombreBeneficiario();
            this.claveEstado=claveEstado;
            this.claveMunicipio =claveMunicipio;
            this.poblacion="";
            

            if (origenValidacion==1) {
                this.mensajeValidacion=resultado.append("<rpu>").append(/*rsBeneficiario.getString("rpu")*/getRpu()).append("</rpu><nombre_beneficiario><![CDATA[")
                         .append(/*rsBeneficiario.getString("nombre").toUpperCase()*/ t.getNombreBeneficiario()).append("]]></nombre_beneficiario><clave_estado>")
                         .append(this.claveEstado).append("</clave_estado><clave_municipio>")
                         .append(this.claveMunicipio).append("</clave_municipio><direccion>")
                         .append(/*rsBeneficiario.getString("direccion")*/t.getDireccion()).append("</direccion><zona>")
                         .append(t.getZona()).append("</zona><poblacion>")
                         .append(/*rsBeneficiario.getString("poblacioncfe")*/t.getPoblacion()).append("</poblacion><tipo_facturacion>")
                         .append(/*""*/t.getTarifa()).append("</tipo_facturacion>").toString();
            } else  if (origenValidacion==2 || origenValidacion==3) { 
                q = new StringBuilder().append("select * from fide_punto_entrega where cerrado=0 AND id_control=").append(idControl);
                rsPuntoEntrega = cx.getRs(q.toString());
                if (!rsPuntoEntrega.next()) {
                    this.mensajeValidacion="Favor de verificar el número de tienda DICONSA de 10 dígitos";
                    this.estatusSolicitud = "rechazado";
                    throw new Fallo(this.mensajeValidacion);
                } 
                
                cx.execute("SET DATEFORMAT ymd");
                
                q = new StringBuilder().append("SELECT CASE WHEN ")
                    .append(" CONVERT(smalldatetime,CONVERT(varchar,ano_fecha_programada) + '/' +  CONVERT(varchar,mes_fecha_programada) + '/28') > getdate() THEN ")
                    .append(" DATEADD(d,30,CONVERT(smalldatetime, CONVERT(varchar,ano_fecha_programada) + '/' +  CONVERT(varchar,mes_fecha_programada) + '/28')) ")
                    .append("ELSE  ")
                    .append(" DATEADD(d,30,GETDATE())	")
                    .append("END as fecha_programada ")
                    .append("FROM fide_distribucion WHERE clave_punto in ")
                    .append("(SELECT clave_punto FROM fide_punto_entrega almacen WHERE clave_punto in (SELECT clave_punto_padre FROM fide_punto_entrega WHERE id_control=").append(idControl).append("))");
                
                this.claveEstatusImportacionDetalle=15;
                
                rsFechaProgramada = cx.getRs(q.toString());
                if (!rsFechaProgramada.next()) {
                    this.mensajeValidacion="Fecha de entrega no disponible";
                    this.estatusSolicitud = "rechazado";
                    throw new Fallo(this.mensajeValidacion);
                }
                
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                //Se inserta el nuevo beneficiario en el padrón ampliado
                this.direccion=/*rsBeneficiario.getString("direccion")*/ t.getDireccion();
                this.clavePunto=rsPuntoEntrega.getInt("clave_punto");
                this.claveTipoPadron = 2;
                this.claveEstadoEntrega=rsPuntoEntrega.getInt("clave_estado");
                this.claveMunicipioEntrega=rsPuntoEntrega.getInt("clave_municipio");
                this.claveLocalidadEntrega=rsPuntoEntrega.getInt("clave_localidad");
                this.fechaEntrega=rsFechaProgramada.getString("fecha_programada");
                this.fechaRegistro="%ahora";
            
                if (origenValidacion==2) {    
                    Consulta consulta = new Consulta(613,"insert","0","",null,usuario);
                    consulta.getCampos().get("rpu").setValor(rpu);
                    consulta.getCampos().get("nombre").setValor(/*rsBeneficiario.getString("nombre").toUpperCase()*/ t.getNombreBeneficiario());
                    consulta.getCampos().get("clave_estado").setValor(String.valueOf(this.claveEstado));
                    consulta.getCampos().get("clave_municipio").setValor(String.valueOf(this.claveMunicipio));
                    consulta.getCampos().get("poblacion").setValor("");
                    consulta.getCampos().get("direccion").setValor(/*rsBeneficiario.getString("direccion")*/t.getDireccion());
                    consulta.getCampos().get("clave_punto").setValor(rsPuntoEntrega.getString("clave_punto"));
                    consulta.getCampos().get("clave_tipo_padron").setValor("2");
                    consulta.getCampos().get("clave_estado_entrega").setValor(rsPuntoEntrega.getString("clave_estado"));
                    consulta.getCampos().get("clave_municipio_entrega").setValor(rsPuntoEntrega.getString("clave_municipio"));
                    consulta.getCampos().get("clave_localidad_entrega").setValor(rsPuntoEntrega.getString("clave_localidad"));
                    consulta.getCampos().get("fecha_entrega").setValor(df2.format(rsFechaProgramada.getDate("fecha_programada")));
                    consulta.getCampos().get("fecha_registro").setValor("%ahora");
                    resultado.append(consulta.insert(true));                
                
                    if (resultado.toString().contains("error")) {
                        this.estatusSolicitud = "rechazado";
                        throw new Fallo(resultado.toString());
                    }
                }
                
                this.estatusSolicitud = "aceptado";
                this.mensajeValidacion = "Solicitud de ingreso exitosa. El usuario se incluye en el padrón ampliado con fecha estimada de entrega del ".concat(df2.format(rsFechaProgramada.getDate("fecha_programada")));
                rsPuntoEntrega.close();
                rsFechaProgramada.close();
            }
             
        } catch(Fallo e) {
            cx.cierraConexion();
            cx= null;
            throw new Fallo(e.getMessage());
        } catch (SQLException e) {
            cx.cierraConexion();
            cx= null;
            throw new Fallo(e.getMessage());
        } catch(Exception e) {
            cx.cierraConexion();
            cx= null;
            throw new Fallo(e.getMessage());            
        }       
        
        return this.mensajeValidacion;
    }
    
    
    public String getEstatusSolicitud() {
        return estatusSolicitud;
    }

    public  void setEstatusSolicitud(String estatusSolicitud) {
        this.estatusSolicitud = estatusSolicitud;
    }

    public String getRpu() {
        return rpu;
    }

    public void setRpu(String rpu) {
        this.rpu = rpu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getClaveEstado() {
        return claveEstado;
    }

    public void setClaveEstado(Integer claveEstado) {
        this.claveEstado = claveEstado;
    }

    public Integer getClaveMunicipio() {
        return claveMunicipio;
    }

    public void setClaveMunicipio(Integer claveMunicipio) {
        this.claveMunicipio = claveMunicipio;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getClavePunto() {
        return clavePunto;
    }

    public void setClavePunto(Integer clavePunto) {
        this.clavePunto = clavePunto;
    }

    public Integer getClaveTipoPadron() {
        return claveTipoPadron;
    }

    public void setClaveTipoPadron(Integer claveTipoPadron) {
        this.claveTipoPadron = claveTipoPadron;
    }

    public Integer getClaveEstadoEntrega() {
        return claveEstadoEntrega;
    }

    public void setClaveEstadoEntrega(Integer claveEstadoEntrega) {
        this.claveEstadoEntrega = claveEstadoEntrega;
    }

    public Integer getClaveMunicipioEntrega() {
        return claveMunicipioEntrega;
    }

    public void setClaveMunicipioEntrega(Integer claveMunicipioEntrega) {
        this.claveMunicipioEntrega = claveMunicipioEntrega;
    }

    public Integer getClaveLocalidadEntrega() {
        return claveLocalidadEntrega;
    }

    public void setClaveLocalidadEntrega(Integer claveLocalidadEntrega) {
        this.claveLocalidadEntrega = claveLocalidadEntrega;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Integer getClaveEstatusImportacionDetalle() {
        return claveEstatusImportacionDetalle;
    }

    public void setClaveEstatusImportacionDetalle(Integer claveEstatusImportacionDetalle) {
        this.claveEstatusImportacionDetalle = claveEstatusImportacionDetalle;
    }
    
    
}

