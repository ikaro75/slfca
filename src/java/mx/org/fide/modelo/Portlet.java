package mx.org.fide.modelo;

import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Portlet {
    private Integer clave_portlet;
    private Integer clave_forma;
    private Integer clave_perfil;
    private Integer clave_tipo;
    private String titulo;
    private String codigo;
    private Integer orden;
    
    public Integer getClave_portlet() {
        return clave_portlet;
    }

    public void setClave_portlet(Integer clave_portlet) {
        this.clave_portlet = clave_portlet;
    }

    public Integer getClave_forma() {
        return clave_forma;
    }

    public void setClave_forma(Integer clave_forma) {
        this.clave_forma = clave_forma;
    }

    public Integer getClave_perfil() {
        return clave_perfil;
    }

    public void setClave_perfil(Integer clave_perfil) {
        this.clave_perfil = clave_perfil;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Portlet(Integer clave_portlet, Integer clave_forma, Integer clave_perfil, Integer clave_tipo, String titulo, String codigo, Integer orden) {
        this.clave_portlet = clave_portlet;
        this.clave_forma = clave_forma;
        this.clave_perfil = clave_perfil;
        this.clave_tipo = clave_tipo;
        this.titulo = titulo;
        this.codigo = codigo;
        this.orden = orden;
    }
    
    public String getHtml(Integer claveForma, Usuario usuario) throws Fallo {
        StringBuilder html = new StringBuilder();
   
        try {
            Conexion oDb  = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
            if (this.clave_portlet==1) {                
                ResultSet rs = oDb.getRs("SELECT * FROM be_filtro WHERE clave_forma=".concat(claveForma.toString()).concat(" AND clave_empleado=").concat(usuario.getClave().toString()));
                while (rs.next()) {
                    html.append("<div class=\"portletFiltroSuper\"><div class=\"portletFiltro\"><a class=\"linkSearch\" href=\"#\" id=\"lnkBusqueda_")
                        .append(rs.getInt("clave_aplicacion")).append("_")
                        .append(rs.getInt("clave_forma")).append("_")
                        .append(rs.getInt("clave_filtro")).append("\" data=\"").append(URLEncoder.encode(rs.getString("consulta"),"UTF-8")).append("\"")
                        .append(" forma=\"").append(rs.getInt("clave_forma")).append("\" pk=\"").append(rs.getInt("clave_filtro")).append("\">")
                        .append(rs.getString("filtro")).append("</a></div><div style=\"float:right\"><div title=\"Eliminar filtro\" style=\"cursor: pointer; float: right; display: none;\" class=\"closeLnkFiltro ui-icon ui-icon-close\" pk=\"")
                        .append(rs.getString("filtro")).append("\" forma=\"").append(rs.getInt("clave_forma")).append("\"></div></div></div>");
                }
            } else if (this.clave_portlet==2) {
                ResultSet rs = oDb.getRs("SELECT clave_reporte,reporte,clave_forma,clave_perfil,clave_tipo_reporte,jsp,jrxml,consulta,etiqueta_tick,color_series,generar_en_insercion,generar_en_actualizacion, (select count(clave_parametro) from be_parametro_reporte where clave_reporte=reporte.clave_reporte) as parametros FROM be_reporte WHERE clave_forma=".concat(claveForma.toString()).concat(" AND clave_perfil=").concat(usuario.getClavePerfil().toString()));
                while (rs.next()) {
                    html.append("<div class='portletReporte'><a href='#' id='lnkReporte_")
                        .append(rs.getInt("clave_reporte")).append("' class='lnkReporte' reporte='")
                            .append(rs.getInt("clave_reporte")).append("' ").append(" perfil='")
                            .append(usuario.getClavePerfil()).append("' jsp='").append(rs.getString("jsp"))
                            .append("' parametros='").append(rs.getInt("parametros"))
                            .append("' tipo_reporte='").append(rs.getInt("clave_tipo_reporte"))
                            .append("' forma='").append(rs.getInt("clave_forma")).append("'>").append(rs.getString("reporte")).append("</a></div>");    
                }            
            } else if (this.clave_portlet==3) {
                FormaX forma = new FormaX();
                forma.setConsulta(new Consulta(claveForma, "select", "0", "", null, usuario));
                forma.setClaveForma(claveForma, usuario);
                ResultSet rs = oDb.getRs("SELECT TOP 10 ba.clave_bitacora, ba.fecha, "
                                  .concat("CASE ISNULL(foto,'0') WHEN '0' THEN '<img src=''img/alguien.jpg'' class=''bitacora'' />' ")
                                  .concat("ELSE '<img src=''uploads/' + CONVERT(varchar,e.clave_empleado) + '/' + foto + ''' class=''bitacora'' />' END as foto,")
                                  .concat("e.nombre + ' ' + e.apellido_paterno as nombre,")
                                  .concat("te.tipo_evento AS clave_tipo_evento, lower(f.alias_tab) as entidad,").concat("x.").concat(forma.getConsulta().getCampos().get(1).nombre).concat(" AS descripcion_entidad,")
                                  .concat("ba.clave_forma, ba.clave_registro ")
                                  .concat("FROM be_bitacora ba, be_empleado e, be_tipo_evento te, be_forma f,").concat(forma.getTabla()).concat(" x ")
                                  .concat("WHERE  ba.clave_empleado=e.clave_empleado AND ")
                                  .concat("ba.clave_tipo_evento=te.clave_tipo_evento AND ")
                                  .concat("ba.clave_forma=f.clave_forma AND ")
                                  .concat("ba.clave_tipo_evento IN (1,2,3) AND ")
                                  .concat("x.").concat(forma.getLlavePrimaria()).concat("= ba.clave_registro AND ba.clave_forma=").concat(claveForma.toString())
                                  .concat(" ORDER BY FECHA DESC"));
                
                while (rs.next()) {
                    html.append("<div class='bitacora'>").append(rs.getString("foto"))
                        .append(rs.getString("nombre")).append(" ").append(rs.getString("clave_tipo_evento")).append(" ").append(rs.getString("entidad"))
                        .append(" <a href='#' id='lnkBitacora_").append(forma.getClaveAplicacion()).append("_").append(claveForma).append("_")
                        .append(rs.getString("clave_registro")).append("' class='lnkBitacora'>").append(rs.getString("descripcion_entidad"))
                        .append("</a>&nbsp;<abbr class='timeago' title='").append(rs.getDate("fecha")).append("'>") .append(rs.getDate("fecha")).append("</abbr></div>");
                }                
            } else if (this.clave_portlet==4) {
                FormaX forma = new FormaX();
                forma.setConsulta(new Consulta(claveForma, "select", "0", "", null, usuario));
                forma.setClaveForma(claveForma, usuario);
                ResultSet rs = oDb.getRs("SELECT TOP 10 clave_nota,forma.clave_forma,clave_registro,clave_empleado,titulo,mensaje,"
                               .concat("fecha_nota, lower(forma.alias_tab) as entidad,")
                               .concat("(SELECT CASE WHEN foto IS NULL THEN 'img/sin_foto.jpg' ELSE 'uploads/' + convert(varchar,clave_empleado) + '/' + foto end from empleado where clave_empleado=nota_forma.clave_empleado) as foto,")
                               .concat("(SELECT ").concat(forma.getConsulta().getCampos().get(1).nombre).concat(" FROM ").concat(forma.getTabla()).concat(" WHERE ").concat(forma.getLlavePrimaria()).concat("=nota_forma.clave_registro) as titulo_entidad")
                               .concat(" FROM be_nota_forma, be_forma ")
                               .concat("WHERE nota_forma.clave_forma=forma.clave_forma AND nota_forma.clave_forma=").concat(claveForma.toString()).concat(" ORDER BY fecha_nota DESC"));
                
                while (rs.next()) {                    
                    html.append("<div class='bitacora'>")
                        .append("<img src='").append(rs.getString("foto")).append("' class='bitacora' border='1'>")
                        .append("<strong>").append(rs.getString("titulo")).append("</strong><abbr class='timeago' title='").append(rs.getDate("fecha_nota"))
                        .append("'>").append(rs.getDate("fecha_nota")).append("</abbr>")
                        .append("<div>Comentó en ").append(rs.getString("entidad")).append(" <em><a href='#' id='lnkComentario_")
                        .append(forma.getClaveAplicacion()).append("_").append(forma.getClaveForma()).append("_").append(rs.getInt("clave_registro"))
                        .append("' class='lnkComentario'>").append(rs.getString("titulo_entidad")).append("</a></em>:</div>")
                        .append("<div style='font-style: normal; font: lighter'>").append(rs.getString("mensaje")).append("</div></div>");
                }
            }
        } catch (Exception ex)  {
           html.append("Error al generar portlet:").append(ex.getMessage());
        }    
        
        return html.toString();
    }
    
    /*public String getJs(Integer claveForma, Usuario usuario) throws Fallo {
        StringBuilder js = new StringBuilder();
   
        try {
            Conexion oDb  = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
            ResultSet rs = oDb.getRs("SELECT * FROM filtro WHERE clave_forma=".concat(claveForma.toString()).concat(" AND clave_empleado=").concat(usuario.getClave().toString()));
            while (rs.next()) {
                if (this.clave_portlet==1) {
                    html.append("<div class='link_toolbar'><div class='linkSearch'><a class='linkSearch' href='#' id='lnkBusqueda_")
                        .append(rs.getInt("clave_aplicacion")).append("_")
                        .append(rs.getInt("clave_forma")).append("_")
                        .append(rs.getInt("clave_filtro")).append("' data='").append(rs.getString("consulta")).append("'")
                        .append(" forma='").append(rs.getInt("clave_forma")).append(" pk='").append(rs.getInt("clave_filtro")).append("'>")
                        .append(rs.getString("filtro")).append("</a></div><div style='float:right'><div title='Eliminar filtro' style'cursor: pointer; float: right; display: none;' class='closeLnkFiltro ui-icon ui-icon-close' pk='")
                        .append(rs.getString("filtro")).append("' forma='").append(rs.getInt("clave_forma")).append("'></div></div></div>");                
                    
                    //Oculta botones para cerrar
                    $(".ui-icon-close", ".linkSearch").hide();
                    
                    //Hace bind del liga del búsqueda
                    $("#lnkBusqueda_" + sSuffix).click(function(){
                        nAplicacion=this.id.split("_")[1];
                        sForma=this.id.split("_")[2];
                        sW=$(this).attr("data");
                       data=$(this).attr("data");
                        if ($("#showEntity_" + nAplicacion + "_" + sForma).length>0)
                            $("#showEntity_" + nAplicacion + "_" + sForma).trigger("click",data);
                        else {
                            $.fn.appmenu.setGridFilter(nAplicacion+"_"+sForma+"_0",nAplicacion,nForma,data);
                        }
                    });                    
                });

                //Hace bind con los divs padres del link en el evento hover
                $(".link_toolbar").hover(
                    function () {
                        //$(this).addClass('active_filter');
                        $(".closeLnkFiltro",this).show();
                    },
                    function () {
                        //$(this).removeClass('active_filter');
                        $(".closeLnkFiltro",this).hide();
                    }
                    );
                
                //Hace bind con los botones de cerrar en el evento hover
                $(".closeLnkFiltro").hover(
                    function () {
                        $(this).parent().addClass('ui-state-default');
                        $(this).parent().addClass('ui-corner-all');
                    },
                    function () {
                        $(this).parent().removeClass('ui-state-default');
                        $(this).parent().removeClass('ui-corner-all');
                    }
                    );
                
                //Hace un unbind
                $(".closeLnkFiltro").unbind("click");
                //Hace bind del botón de búsqueda
                $(".closeLnkFiltro").click(function(){
                    if (!confirm('¿Desea borrar el filtro seleccionado?')) return false;
                    $.post("control","$cmd=register&$ta=delete&$cf=93&$pk=" + $(this).attr("pk"));
                    $(this).parent().parent().remove();
                });
                                    
                }
            }
        } catch (Exception ex)  {
            throw new Fallo(ex.getMessage());
        }    
        
        return html.toString();
    }
    
    public String getJ(Integer claveForma) {
        StringBuilder js = new StringBuilder();
        
        if (this.clave_portlet==1) {
            js.append("//Oculta botones para cerrar\n" +
"                    $(\".ui-icon-close\", \"#filtros_\"+sDivSuffix).hide();\n" +
"                    \n" +
"                    //Hace bind del liga del búsqueda\n" +
"                    $(\"#lnkBusqueda_\" + sSuffix).click(function(){\n" +
"                        nAplicacion=this.id.split(\"_\")[1];\n" +
"                        sForma=this.id.split(\"_\")[2];\n" +
"                        sW=$(this).attr(\"data\");\n" +
"                        data=$(this).attr(\"data\");\n" +
"                        if ($(\"#showEntity_\" + nAplicacion + \"_\" + sForma).length>0)\n" +
"                            $(\"#showEntity_\" + nAplicacion + \"_\" + sForma).trigger(\"click\",data);\n" +
"                        else {\n" +
"                            $.fn.appmenu.setGridFilter(nAplicacion+\"_\"+sForma+\"_0\",nAplicacion,nForma,data);\n" +
"                        }\n" +
"                    });                    \n" +
"                });\n" +
"\n" +
"                //Hace bind con los divs padres del link en el evento hover\n" +
"                $(\".link_toolbar\").hover(\n" +
"                    function () {\n" +
"                        //$(this).addClass('active_filter');\n" +
"                        $(\".closeLnkFiltro\",this).show();\n" +
"                    },\n" +
"                    function () {\n" +
"                        //$(this).removeClass('active_filter');\n" +
"                        $(\".closeLnkFiltro\",this).hide();\n" +
"                    }\n" +
"                    );\n" +
"                \n" +
"                //Hace bind con los botones de cerrar en el evento hover\n" +
"                $(\".closeLnkFiltro\").hover(\n" +
"                    function () {\n" +
"                        $(this).parent().addClass('ui-state-default');\n" +
"                        $(this).parent().addClass('ui-corner-all');\n" +
"                    },\n" +
"                    function () {\n" +
"                        $(this).parent().removeClass('ui-state-default');\n" +
"                        $(this).parent().removeClass('ui-corner-all');\n" +
"                    }\n" +
"                    );\n" +
"                \n" +
"                //Hace un unbind\n" +
"                $(\".closeLnkFiltro\").unbind(\"click\");\n" +
"                //Hace bind del botón de búsqueda\n" +
"                $(\".closeLnkFiltro\").click(function(){\n" +
"                    if (!confirm('¿Desea borrar el filtro seleccionado?')) return false;\n" +
"                    $.post(\"control\",\"$cmd=register&$ta=delete&$cf=93&$pk=\" + $(this).attr(\"pk\"));\n" +
"                    $(this).parent().parent().remove();\n" +
"                });");
        }
        
        return js.toString();
    } */
    
}
