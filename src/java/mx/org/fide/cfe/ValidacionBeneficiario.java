package mx.org.fide.cfe;

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
public class ValidacionBeneficiario {

    public String estatusSolicitud = "";
    private String rpu;
    private String nombre;
    private Integer claveEstado;
    private Integer claveMunicipio;
    private Integer claveLocalidad;
    private String poblacion;
    private String direccion;
    private Integer clavePunto;
    private Integer claveTipoPadron;
    private Integer claveEstadoEntrega;
    private Integer claveMunicipioEntrega;
    private Integer claveLocalidadEntrega;
    private String tarifa;
    private String fechaEntrega;
    private String fechaRegistro;
    private String mensajeValidacion;
    private Integer claveEstatusImportacionDetalle;
    private Boolean tomaInfoDeLaTienda = false;

    public String valida(String rpu, String idControl, Integer origenValidacion, Usuario usuario) throws Fallo {
        ResultSet rs = null;
        ResultSet rsPuntoEntrega = null;
        ResultSet rsBeneficiario = null;
        ResultSet rsFechaProgramada = null;
        StringBuilder resultado = new StringBuilder();
        StringBuilder q = new StringBuilder();
        Conexion cx = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
        String mensajeValidacion = "";
        String claveEstadoCFE = "";
        String claveMunicipioCFE = "";
        String poblacionBeneficiario = "";
        String direccionBeneficiario = "";
        String nombreBeneficiario = "";
        this.rpu = rpu;
        this.estatusSolicitud = "";
        this.nombre = null;
        this.claveEstado = 0;
        this.claveMunicipio = null;
        this.claveLocalidad = null;
        this.poblacion = null;
        this.direccion = null;
        this.clavePunto = null;
        this.claveTipoPadron = null;
        this.claveEstadoEntrega = null;
        this.claveMunicipioEntrega = null;
        this.claveLocalidadEntrega = null;
        this.tarifa = null;
        this.fechaEntrega = null;
        this.fechaRegistro = null;
        this.mensajeValidacion = null;
        this.claveEstatusImportacionDetalle = null;
        this.tomaInfoDeLaTienda = false;

        try {
            //0. Verifica longitud de RPU 
            if (rpu.length() != 12) {
                this.mensajeValidacion = "El número de servicio no tiene 12 dígitos";
                this.claveEstatusImportacionDetalle = 9;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }

            //1. Verifica si ya está en el padrón actual
            q.append("SELECT clave_beneficiario, nombre,")
                    .append("(SELECT id_control FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto) as id_control, ")
                    .append("(SELECT punto_entrega FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto) as punto_entrega, ")
                    .append("(SELECT direccion FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto) as direccion, ")
                    .append("(SELECT localidad FROM fide_localidad WHERE clave_localidad=(SELECT clave_localidad FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto)) as localidad, ")
                    .append("(SELECT municipio FROM fide_municipio WHERE clave_municipio=(SELECT clave_municipio FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto)) as municipio, ")
                    .append("(SELECT estado FROM fide_estado WHERE clave_estado=(SELECT clave_estado FROM fide_punto_entrega WHERE clave_punto=fide_beneficiario.clave_punto)) as estado, ")
                    .append("clave_tipo_padron, pagina")
                    .append(" FROM fide_beneficiario WHERE rpu='").append(rpu).append("' AND clave_tipo_padron <>4");
            rs = cx.getRs(q.toString());

            if (rs.next()) {
                if (origenValidacion == 1) {
                    this.mensajeValidacion = "El RPU consultado se encuentra registrado en la lista de beneficiarios del programa \"Ahórrate una luz\"," + rs.getString("clave_beneficiario");
                } else if (origenValidacion == 2 || origenValidacion == 3 || origenValidacion == 4) {
                    this.nombre = rs.getString("nombre");
                    if (Integer.parseInt(idControl) == rs.getInt("id_control")) {
                        this.mensajeValidacion = "El usuario ya se encuentra registrado en la misma Tienda DICONSA, en la hoja ".concat(rs.getString("pagina") == null ? " <no establecida>" : rs.getString("pagina"));
                        if (rs.getInt("clave_tipo_padron") == 1) {
                            this.claveEstatusImportacionDetalle = 11;
                        } else {
                            this.claveEstatusImportacionDetalle = 13;
                        }
                    } else {

                        if (rs.getInt("clave_tipo_padron") == 1) {
                            this.claveEstatusImportacionDetalle = 12;
                        } else {
                            this.claveEstatusImportacionDetalle = 14;
                        }

                        this.mensajeValidacion = "Ya está registrado, pase por sus lámparas a la tienda ".concat(rs.getString("punto_entrega")).concat(" ubicada en ")
                                .concat(rs.getString("direccion")).concat(", ").concat(rs.getString("localidad"))
                                .concat(", ").concat(rs.getString("municipio")).concat(", ").concat(rs.getString("estado"))
                                .concat(rs.getString("pagina") == null ? "" : ", en la hoja ".concat(rs.getString("pagina")));
                    }
                }
                this.estatusSolicitud = "rechazado";

                if (origenValidacion == 4) {
                    this.claveEstatusImportacionDetalle = 14;
                    //Se retoman los datos del padron de beneficiarios
                    q = new StringBuilder().append("SELECT clave_beneficiario, nombre, clave_estado, clave_municipio, poblacion, direccion, tarifa FROM fide_beneficiario WHERE rpu='").append(rpu).append("' AND clave_tipo_padron<>4");
                    rs = cx.getRs(q.toString());
                    if (rs.next()) {
                        this.tomaInfoDeLaTienda = false;
                        this.nombre = rs.getString("nombre");
                        this.claveEstado = rs.getInt("clave_estado");
                        this.claveMunicipio = rs.getInt("clave_municipio");
                        this.poblacion = rs.getString("poblacion");
                        this.direccion = rs.getString("direccion");
                        this.tarifa = rs.getString("tarifa");
                        this.claveTipoPadron = 4;
                    }
                    
                    //Verificar dentro de la misma importación duplicados del padrón 4
                    q = new StringBuilder().append("UPDATE fide_importacion_detalle SET clave_estatus=4 WHERE rpu='").append(rpu).append("' AND clave_estatus=0");
                    rs = cx.execute(q.toString());
                    
                }

                throw new Fallo(this.mensajeValidacion);
            }

            //2. Verifica si fue dado de alta en padrones anteriores
            q = new StringBuilder().append("SELECT programa FROM fide_padron_anterior WHERE rpu='").append(rpu).append("'");
            rs = cx.getRs(q.toString());

            if (rs.next()) {

                String sPrograma = "";
                if (rs.getInt("programa") == 1) {
                    sPrograma = "Luz sustentable I";
                    this.claveEstatusImportacionDetalle = 4;
                } else if (rs.getInt("programa") == 2) {
                    sPrograma = "Luz sustentable II";
                    this.claveEstatusImportacionDetalle = 5;
                } else if (rs.getInt("programa") == 3) {
                    sPrograma = "Piloto en el Estado de Michoacán";
                    this.claveEstatusImportacionDetalle = 6;
                } else if (rs.getInt("programa") == 4) {
                    sPrograma = "Piloto en el Estado de Sonora";
                    this.claveEstatusImportacionDetalle = 7;
                } else if (rs.getInt("programa") == 5) {
                    sPrograma = "Piloto en el Estado de Chihuahua";
                    this.claveEstatusImportacionDetalle = 7;
                } else if (rs.getInt("programa") == 6) {
                    sPrograma = "Piloto en el Estado de Guerrero";
                    this.claveEstatusImportacionDetalle = 7;
                }

                if (origenValidacion == 1) {
                    this.mensajeValidacion = "El RPU consultado participó en el Programa '".concat(sPrograma).concat("' y por lo tanto no puede ser beneficiario del Programa \"Ahórrate una luz\"");
                } else if (origenValidacion == 2 || origenValidacion == 3 || origenValidacion == 4) {
                    this.mensajeValidacion = "El usuario participó en programas anteriores";
                }

                this.estatusSolicitud = "rechazado";

                if (origenValidacion == 4) {
                    this.tomaInfoDeLaTienda = true;
                }
                
                throw new Fallo(this.mensajeValidacion);
            }

            //3. Verifica que el RPU se encuentra en la tabla FIDE_PADRON_CFE
            q = new StringBuilder().append("SELECT * FROM fide_padron_cfe WHERE rpu='").append(rpu).append("'");
            rsBeneficiario = cx.getRs(q.toString());

            if (!rsBeneficiario.next()) {
                //Si no se encuentra en el padrón de cfe, se hace la validación en SICOM
                if (origenValidacion == 4) {
                    //Aquí debe salir 
                    this.tomaInfoDeLaTienda = false;
                    this.claveEstatusImportacionDetalle = 18;
                    this.estatusSolicitud = "aceptado";
                    this.mensajeValidacion = "Beneficiario pendiente de ser validado en SICOM";
                    throw new Fallo(this.mensajeValidacion);
                }

                Trama t = new Trama(rpu);

                if (t.getError() != null) {
                    throw new Fallo(t.getError());
                }

                if (t.getEstatus().equals("S1")) {
                    if (origenValidacion == 1) {
                        this.mensajeValidacion = "El RPU consultado no existe; favor de verificar";
                    } else if (origenValidacion == 2 || origenValidacion == 3 || origenValidacion == 4) {
                        this.mensajeValidacion = "El número de servicio del usuario no se encuentra en la base de datos";
                        this.claveEstatusImportacionDetalle = 9;
                    }

                    this.estatusSolicitud = "rechazado";
                    if (origenValidacion == 4) {
                        this.tomaInfoDeLaTienda = true;
                        throw new Fallo(this.mensajeValidacion);
                    }
                }

                if (t.getEstatus().equals("S6")) {
                    if (origenValidacion == 1) {
                        this.mensajeValidacion = "El RPU proporcionado no se encuentra activo y por lo tanto no puede ser beneficiario del programa \"Ahórrate una luz\"";
                    } else if (origenValidacion == 2 || origenValidacion == 3 || origenValidacion == 4) {
                        this.mensajeValidacion = "El servicio de energía no se encuentra activo";

                        if (origenValidacion == 4) {
                            this.tomaInfoDeLaTienda = true;
                        }
                    }
                    this.claveEstatusImportacionDetalle = 3;
                    this.estatusSolicitud = "rechazado";
                    throw new Fallo(this.mensajeValidacion);
                }

                this.tarifa = t.getTarifa();
                //Valida que la tarifa sea de 1A a 1F
                if (t.getTarifa().equals("01") || t.getTarifa().equals("1A") || t.getTarifa().equals("1B") || t.getTarifa().equals("1C")
                        || t.getTarifa().equals("1D") || t.getTarifa().equals("1E") || t.getTarifa().equals("1F")) {
                } else {
                    if (origenValidacion == 1) {
                        this.mensajeValidacion = "El RPU consultado no se encuentra dentro de las tarifas participantes en el Programa \"Ahórrate una luz\"";
                    } else if (origenValidacion == 2 || origenValidacion == 3) {
                        this.mensajeValidacion = "La tarifa del usuario no participa en el Programa";
                    } else if (origenValidacion == 4) {
                        this.tomaInfoDeLaTienda = true;
                    }

                    this.claveEstatusImportacionDetalle = 8;
                    this.estatusSolicitud = "rechazado";
                    throw new Fallo(this.mensajeValidacion);
                }

                //Valida que la tarifa no sea DAC
                if (t.getDAC().equals("1")) {
                    if (origenValidacion == 1) {
                        this.mensajeValidacion = "El RPU consultado no se encuentra dentro de las tarifas participantes en el Programa \"Ahórrate una luz\"";
                    } else if (origenValidacion == 2 || origenValidacion == 3) {
                        this.mensajeValidacion = "La tarifa del usuario no participa en el Programa";
                    } else if (origenValidacion == 4) {
                        this.tomaInfoDeLaTienda = true;
                    }

                    this.claveEstatusImportacionDetalle = 8;
                    this.estatusSolicitud = "rechazado";
                    throw new Fallo(this.mensajeValidacion);
                }
                claveEstadoCFE = t.getClaveEstado();
                claveMunicipioCFE = t.getClaveMunicipio();
                poblacionBeneficiario = t.getPoblacion();
                direccionBeneficiario = t.getDireccion();
                nombreBeneficiario = t.getNombreBeneficiario();

            } else {
                //Valida que el estado y municipio del beneficiario está dentro del catálogo 
                claveEstadoCFE = rsBeneficiario.getString("claveedomun").substring(0, 1);
                claveMunicipioCFE = rsBeneficiario.getString("claveedomun").substring(1, 4);
                poblacionBeneficiario = rsBeneficiario.getString("poblacioncfe").toUpperCase();
                direccionBeneficiario = rsBeneficiario.getString("direccion");
                nombreBeneficiario = rsBeneficiario.getString("nombre").toUpperCase();
            }

            //4. Extrae la equivalencia de la clave de estado 
            q = new StringBuilder().append("SELECT clave_estado FROM fide_estado WHERE codigo_estado_cfe='").append(claveEstadoCFE).append("'");
            rs = cx.getRs(q.toString());
            if (rs.next()) {
                this.claveEstado = rs.getInt("clave_estado");
            } else {
                if (origenValidacion == 1) {
                    this.mensajeValidacion = "El Estado del beneficiario no está en el Programa \"Ahórrate una luz\"";
                } else if (origenValidacion == 2 || origenValidacion == 3) {
                    this.mensajeValidacion = "La localidad no es participante en el programa";
                } else if (origenValidacion == 4) {
                    this.mensajeValidacion = "La localidad no es participante en el programa";
                    this.tomaInfoDeLaTienda = true;
                }

                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }

            //4. Extrae la equivalencia de la clave de municipios 
            q = new StringBuilder().append("SELECT clave_municipio FROM fide_municipio WHERE clave_estado=").append(this.claveEstado).append(" AND clave_municipio_cfe='").append(claveMunicipioCFE).append("'");
            rs = cx.getRs(q.toString());
            if (rs.next()) {
                claveMunicipio = rs.getInt("clave_municipio");
            } else {

                if (origenValidacion == 1) {
                    this.mensajeValidacion = "El municipio del beneficiario no está en el Programa \"Ahórrate una luz\"";
                } else if (origenValidacion == 2 || origenValidacion == 3) {
                    this.mensajeValidacion = "El municipio no es participante en el programa";
                } else if (origenValidacion == 4) {
                    this.mensajeValidacion = "El municipio no es participante en el programa";
                    this.tomaInfoDeLaTienda = true;
                }
                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }

            //5. Verifica que la población reportada en SICOM esté dentro del catálogo de localidades
            q = new StringBuilder().append("SELECT clave_localidad FROM fide_localidad, fide_municipio WHERE fide_localidad.clave_municipio=fide_municipio.clave_municipio and fide_localidad.clave_municipio=").append(claveMunicipio).append(" AND fide_localidad.localidad like '").append(poblacionBeneficiario.trim().replaceAll("'","''")).append("%'");
            rs = cx.getRs(q.toString());
            if (rs.next()) {
                claveLocalidad = rs.getInt("clave_localidad");
            } else {

                if (origenValidacion == 1) {
                    this.mensajeValidacion = "La localidad del beneficiario no está en el Programa \"Ahórrate una luz\"";
                } else if (origenValidacion == 2 || origenValidacion == 3) {
                    this.mensajeValidacion = "La localidad no es participante en el programa";
                } else if (origenValidacion == 4) {
                    this.mensajeValidacion = "La localidad no es participante en el programa";
                    this.tomaInfoDeLaTienda = true;
                }
                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }

            this.nombre = nombreBeneficiario;
            this.claveEstado = claveEstado;
            this.claveMunicipio = claveMunicipio;
            this.poblacion = poblacionBeneficiario;

            if (origenValidacion == 1) {
                this.mensajeValidacion = resultado.append("<rpu>").append(this.rpu).append("</rpu><nombre_beneficiario><![CDATA[")
                        .append(this.nombre.toUpperCase()).append("]]></nombre_beneficiario><clave_estado>")
                        .append(this.claveEstado).append("</clave_estado><clave_municipio>")
                        .append(this.claveMunicipio).append("</clave_municipio><direccion>")
                        .append(direccionBeneficiario).append("</direccion><zona>")
                        .append("").append("</zona><poblacion>")
                        .append(this.poblacion).append("</poblacion><tipo_facturacion>")
                        .append("").append("</tipo_facturacion>").toString();
            } else if (origenValidacion == 2 || origenValidacion == 3 || origenValidacion == 4) {

                //Se quita la condición puesto que 
                q = new StringBuilder().append("select * from fide_punto_entrega where /*cerrado in (0,2) AND */ id_control=").append(idControl);
                rsPuntoEntrega = cx.getRs(q.toString());
                if (!rsPuntoEntrega.next()) {
                    this.mensajeValidacion = "Favor de verificar el número de tienda DICONSA de 10 dígitos, no existe o está cerrado";
                    this.estatusSolicitud = "rechazado";
                    this.claveEstatusImportacionDetalle = 10;
                    throw new Fallo(this.mensajeValidacion);
                }

                //Debido a que el padron 4 no requiere fecha programada se puede omitir este código
                if (origenValidacion == 2 || origenValidacion == 3) {
                    cx.execute("SET DATEFORMAT ymd");

                    q = new StringBuilder().append("SELECT CASE WHEN ")
                            .append(" CONVERT(smalldatetime,CONVERT(varchar,ano_fecha_programada) + '/' +  CONVERT(varchar,mes_fecha_programada) + '/28') > getdate() THEN ")
                            .append(" DATEADD(d,30,CONVERT(smalldatetime, CONVERT(varchar,ano_fecha_programada) + '/' +  CONVERT(varchar,mes_fecha_programada) + '/28')) ")
                            .append("ELSE  ")
                            .append(" DATEADD(d,30,GETDATE())	")
                            .append("END as fecha_programada ")
                            .append("FROM fide_distribucion WHERE clave_punto in ")
                            .append("(SELECT clave_punto FROM fide_punto_entrega almacen WHERE clave_punto in (SELECT clave_punto_padre FROM fide_punto_entrega WHERE id_control=").append(idControl).append("))");

                    this.claveEstatusImportacionDetalle = 15;

                    rsFechaProgramada = cx.getRs(q.toString());
                    if (!rsFechaProgramada.next()) {
                        q = new StringBuilder().append("SELECT DATEADD(d,30,GETDATE()) as fecha_programada");
                        rsFechaProgramada = cx.getRs(q.toString());
                        if (rsFechaProgramada.next()) {
                            this.mensajeValidacion = "Fecha de entrega no disponible";
                            this.estatusSolicitud = "rechazado";
                            this.claveEstatusImportacionDetalle = 17;
                            throw new Fallo(this.mensajeValidacion);
                        }
                    }
                    this.fechaEntrega = rsFechaProgramada.getString("fecha_programada");
                }

                //Se inserta el nuevo beneficiario en el padrón ampliado
                this.direccion = direccionBeneficiario;
                this.clavePunto = rsPuntoEntrega.getInt("clave_punto");
                this.claveTipoPadron = 2;
                this.claveEstadoEntrega = rsPuntoEntrega.getInt("clave_estado");
                this.claveMunicipioEntrega = rsPuntoEntrega.getInt("clave_municipio");
                this.claveLocalidadEntrega = rsPuntoEntrega.getInt("clave_localidad");
                this.fechaRegistro = "%ahora";

                if (this.tomaInfoDeLaTienda) {
                    this.claveEstado = rsPuntoEntrega.getInt("clave_estado");
                    this.claveMunicipio = rsPuntoEntrega.getInt("clave_municipio");
                    this.poblacion = rsPuntoEntrega.getString("localidad");
                }

                if (origenValidacion == 2) {
                    SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                    Consulta consulta = new Consulta(613, "insert", "0", "", null, usuario);
                    consulta.getCampos().get("rpu").setValor(rpu);
                    consulta.getCampos().get("nombre").setValor(this.nombre.toUpperCase());
                    consulta.getCampos().get("clave_estado").setValor(String.valueOf(this.claveEstado));
                    consulta.getCampos().get("clave_municipio").setValor(String.valueOf(this.claveMunicipio));
                    consulta.getCampos().get("poblacion").setValor(this.poblacion);
                    consulta.getCampos().get("direccion").setValor(this.direccion);
                    consulta.getCampos().get("clave_punto").setValor(rsPuntoEntrega.getString("clave_punto"));
                    consulta.getCampos().get("clave_tipo_padron").setValor("2");
                    consulta.getCampos().get("clave_estado_entrega").setValor(rsPuntoEntrega.getString("clave_estado"));
                    consulta.getCampos().get("clave_municipio_entrega").setValor(rsPuntoEntrega.getString("clave_municipio"));
                    consulta.getCampos().get("clave_localidad_entrega").setValor(rsPuntoEntrega.getString("clave_localidad"));
                    //consulta.getCampos().get("fecha_entrega").setValor("01/01/2015");
                    consulta.getCampos().get("fecha_entrega").setValor(df2.format(rsFechaProgramada.getDate("fecha_programada")));
                    consulta.getCampos().get("fecha_registro").setValor("%ahora");
                    resultado.append(consulta.insert(true));

                    if (resultado.toString().contains("error")) {
                        this.estatusSolicitud = "rechazado";
                        throw new Fallo(resultado.toString());
                    }
                }

                this.claveEstatusImportacionDetalle = 16;
                this.estatusSolicitud = "aceptado";
                this.mensajeValidacion = "Solicitud de ingreso exitosa. El usuario se incluye en el padrón ampliado con fecha estimada de entrega del 01/01/2015";
                rsPuntoEntrega.close();
                if (rsFechaProgramada != null) {
                    rsFechaProgramada.close();
                }
            }

        } catch (Fallo e) {
            cx.cierraConexion();
            cx = null;
            throw new Fallo(e.getMessage());
        } catch (SQLException e) {
            cx.cierraConexion();
            cx = null;
            throw new Fallo(e.getMessage());
        } catch (Exception e) {
            cx.cierraConexion();
            cx = null;
            throw new Fallo(e.getMessage());
        }

        return this.mensajeValidacion;
    }

    public String getDataFromSICOM(String rpu, Usuario usuario) throws Fallo {
        ResultSet rs = null;
        Trama t = new Trama(rpu);
        Conexion cx = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
        StringBuilder q = new StringBuilder();

        if (t.getError() != null) {
            throw new Fallo(t.getError());
        }

        if (t.getEstatus().equals("S1")) {
            this.mensajeValidacion = "El número de servicio del usuario no se encuentra en SICOM";
            this.claveEstatusImportacionDetalle = 9;
            this.estatusSolicitud = "rechazado";
            this.tomaInfoDeLaTienda = true;
            throw new Fallo(this.mensajeValidacion);
        }

        if (t.getEstatus().equals("S6")) {
            this.mensajeValidacion = "El servicio de energía no se encuentra activo";
            this.tomaInfoDeLaTienda = true;
            this.claveEstatusImportacionDetalle = 3;
            this.estatusSolicitud = "rechazado";
            throw new Fallo(this.mensajeValidacion);
        }

        this.tarifa = t.getTarifa();
        //Valida que la tarifa sea de 1A a 1F
        if (t.getTarifa().equals("01") || t.getTarifa().equals("1A") || t.getTarifa().equals("1B") || t.getTarifa().equals("1C")
                || t.getTarifa().equals("1D") || t.getTarifa().equals("1E") || t.getTarifa().equals("1F")) {
        } else {
            this.mensajeValidacion = "La tarifa del usuario no participa en el Programa";
            this.tomaInfoDeLaTienda = true;
            this.claveEstatusImportacionDetalle = 8;
            this.estatusSolicitud = "rechazado";
            throw new Fallo(this.mensajeValidacion);
        }

        //Valida que la tarifa no sea DAC
        if (t.getDAC().equals("1")) {
            this.mensajeValidacion = "La tarifa del usuario no participa en el Programa";
            this.tomaInfoDeLaTienda = true;
            this.claveEstatusImportacionDetalle = 8;
            this.estatusSolicitud = "rechazado";
            throw new Fallo(this.mensajeValidacion);            
        }
       
        //4. Extrae la equivalencia de la clave de estado 
        try {
            q = new StringBuilder().append("SELECT clave_estado FROM fide_estado WHERE codigo_estado_cfe='").append(t.getClaveEstado()).append("'");
            rs = cx.getRs(q.toString());
            if (rs.next()) {
                this.claveEstado = rs.getInt("clave_estado");
            } else {
                this.mensajeValidacion = "La localidad no es participante en el programa";
                this.tomaInfoDeLaTienda = true;
                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);                
            }

            //4. Extrae la equivalencia de la clave de municipios 
            q  = new StringBuilder().append("SELECT clave_municipio FROM fide_municipio WHERE clave_estado=").append(this.claveEstado).append(" AND clave_municipio_cfe='").append(t.getClaveMunicipio()).append("'");
            rs  = cx.getRs(q.toString());

            if (rs.next ()) {
                this.claveMunicipio = rs.getInt("clave_municipio");
            } else {
                this.mensajeValidacion = "El municipio no es participante en el programa";
                this.tomaInfoDeLaTienda = true;
                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }

            //5. Verifica que la población reportada en SICOM esté dentro del catálogo de localidades
            q  = new StringBuilder().append("SELECT clave_localidad, localidad FROM fide_localidad, fide_municipio WHERE fide_localidad.clave_municipio=fide_municipio.clave_municipio and fide_localidad.clave_municipio=").append(claveMunicipio).append(" AND fide_localidad.localidad like '").append(t.getPoblacion().trim()).append("%'");
            rs  = cx.getRs(q.toString());

            if (rs.next()) {
                this.claveLocalidad = rs.getInt("clave_localidad");
                this.poblacion =  rs.getString("localidad");
            } else {
                this.mensajeValidacion = "La localidad no es participante en el programa";
                this.tomaInfoDeLaTienda = true;
                this.claveEstatusImportacionDetalle = 2;
                this.estatusSolicitud = "rechazado";
                throw new Fallo(this.mensajeValidacion);
            }
            
            this.claveEstatusImportacionDetalle = 16;
            this.estatusSolicitud = "aceptado";
            this.mensajeValidacion = "Beneficiario validado en SICOM.";
        } catch (Exception e ) {
             throw new Fallo(e.getMessage());
        }
        
        return  this.mensajeValidacion;
}

public String getEstatusSolicitud() {
        return estatusSolicitud;
    }

    public void setEstatusSolicitud(String estatusSolicitud) {
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

    public Integer getClaveLocalidad() {
        return claveLocalidad;
    }

    public void setClaveLocalidad(Integer claveLocalidad) {
        this.claveLocalidad = claveLocalidad;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public Boolean getTomaInfoDeLaTienda() {
        return tomaInfoDeLaTienda;
    }

    public void setTomaInfoDeLaTienda(Boolean tomaInfoDeLaTienda) {
        this.tomaInfoDeLaTienda = tomaInfoDeLaTienda;
    }

    public static void main(String args[]) {
        try {
            StringBuilder q = new StringBuilder();
            //Validación de padrón 4 en SICOM
            Conexion cx = new Conexion("10.55.210.5:1433", "SLFCA", "dmartinez", "dmartinez", Conexion.DbType.valueOf("MSSQL"));
            //"SELECT clave_importacion_detalle,rpu, id_control FROM fide_importacion_detalle WHERE clave_tipo_padron=4 AND rpu='000012054123'"
            //q = new StringBuilder("SELECT clave_importacion_detalle, rpu, id_control FROM fide_importacion_detalle WHERE  clave_importacion=4283 ORDER BY clave_importacion_detalle");
            q = new StringBuilder("select * from fide_importacion_detalle where clave_importacion in (4283,4284) and clave_tipo_padron=4 and rpu in (select rpu from temp_dmm_20160315)");
            
            /*q =  new StringBuilder("select clave_importacion_detalle, rpu, id_control from fide_importacion_detalle where clave_importacion_detalle in (")
             .append("select clave_importacion_detalle ")
             .append(" from fide_beneficiario, fide_punto_entrega")
             .append(" where fide_beneficiario.clave_punto= fide_punto_entrega.clave_punto")
             .append(" and (fide_beneficiario.clave_estado_entrega!=fide_punto_entrega.clave_estado")
             .append(" or fide_beneficiario.clave_municipio_entrega!=fide_punto_entrega.clave_municipio")
             .append(" or fide_beneficiario.clave_localidad_entrega!=fide_punto_entrega.clave_localidad)")
             .append(" and fide_beneficiario.clave_tipo_padron=4")
             .append(") and clave_importacion in (select clave_importacion from fide_importacion where clave_definicion=6) ORDER BY clave_importacion_detalle");*/
            ResultSet rs = cx.getRs(q.toString());
            ValidacionBeneficiario v = new ValidacionBeneficiario();
            String mensaje = "";
            Usuario usuario = new Usuario();
            usuario.setCx(cx);

            Integer clavePunto = 0;
            Integer claveEstadoEntrega = 0;
            Integer claveMunicipioEntrega = 0;
            Integer claveLocalidadEntrega = 0;
            String poblacion = "";
            String tarifa = "";
            Integer i = 0;
            while (rs.next()) {
                  //if (rs.getInt("clave_importacion_detalle")==101031009 || rs.getInt("clave_importacion_detalle")==101031213 || rs.getInt("clave_importacion_detalle")==101031214) {
                //    System.out.println("Stop");                  }

                try {
                    mensaje = v.valida(rs.getString("rpu"), rs.getString("id_control").replace("\r", ""), 4, usuario);
                    //mensaje = v.getDataFromSICOM(rs.getString("rpu"), usuario);
                } catch (Exception e) {
                    mensaje = e.getMessage();
                }

                if (v.getClavePunto()==null) {
                    ResultSet rsTemp = cx.getRs("SELECT clave_punto, clave_estado, clave_municipio, clave_localidad, (SELECT localidad FROM fide_localidad WHERE clave_localidad=fide_punto_entrega.clave_localidad) as poblacion FROM fide_punto_entrega WHERE id_control=".concat(rs.getString("id_control")));
                           
                    if (rsTemp.next()) {
                       clavePunto = rsTemp.getInt("clave_punto");
                       claveEstadoEntrega = rsTemp.getInt("clave_estado");
                       claveMunicipioEntrega= rsTemp.getInt("clave_municipio");
                       claveLocalidadEntrega = rsTemp.getInt("clave_localidad");
                       poblacion =rsTemp.getString("poblacion");
                       tarifa = v.getTarifa()==null?"":v.getTarifa();
                    } else {
                       mensaje = mensaje.concat("; no está definida la tienda ").concat(rs.getString("id_control"));
                    }
                 } else {
                    clavePunto = v.getClavePunto();
                    claveEstadoEntrega = v.getClaveEstadoEntrega();
                    claveMunicipioEntrega= v.getClaveMunicipioEntrega();
                    claveLocalidadEntrega = v.getClaveLocalidadEntrega();
                    poblacion = v.getPoblacion();
                    tarifa = v.getTarifa()==null?"":v.getTarifa();
                }
                
                try {
                    if (v.getTomaInfoDeLaTienda()) {
                     q = new StringBuilder().append("UPDATE fide_beneficiario SET tarifa='").append(tarifa).append("',")
                     .append("clave_estado=").append(claveEstadoEntrega)
                     .append(",clave_municipio=").append(claveMunicipioEntrega)
                     .append(",poblacion='").append(poblacion)
                     .append("', clave_estado_entrega=").append(claveEstadoEntrega)
                     .append(",clave_municipio_entrega=").append(claveMunicipioEntrega)                                        
                     .append(",clave_localidad_entrega=").append(claveLocalidadEntrega)
                     .append(" WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));
                                
                     } else {
                     q = new StringBuilder().append("UPDATE fide_beneficiario SET tarifa='").append(tarifa).append("',")
                     .append("clave_estado=").append(v.getClaveEstado())
                     .append(",clave_municipio=").append(v.getClaveMunicipio())
                     .append(",poblacion='").append(v.getPoblacion())
                     .append("', clave_estado_entrega=").append(claveEstadoEntrega)
                     .append(", clave_municipio_entrega=").append(claveMunicipioEntrega)                                        
                     .append(", clave_localidad_entrega=").append(claveLocalidadEntrega)
                     .append(" WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));                               
                     }    
                           
                     cx.execute(q.toString());

                    /*q = new StringBuilder().append("UPDATE fide_importacion_detalle SET mensaje_error='").append(v.getMensajeValidacion()).append("',")
                            .append("clave_estatus=").append(v.getClaveEstatusImportacionDetalle())
                            .append(" WHERE clave_importacion_detalle=").append(rs.getString("clave_importacion_detalle"));

                    cx.execute(q.toString());*/

                    System.out.println(new StringBuilder("--->").append(rs.getString("clave_importacion_detalle")).append(" actualizado").append("Registro ").append(i).append("").toString());
                    i++;

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Error :".concat(e.getMessage()));
        }
    }
}
