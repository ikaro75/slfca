/* 
 \u00e1 -> á 
 \u00e9 -> é 
 \u00ed -> í 
 \u00f3 -> ó
 \u00fa -> ú 
 
 \u00c1 -> Á 
 \u00c9 -> É 
 \u00cd -> Í 
 \u00d3 -> Ó 
 \u00da -> Ú 
 
 \u00f1 -> ñ 
 \u00d1 -> Ñ
 * and open the template in the editor.
 */
(function ($) {
    $.fn.portletReport = function (opc) {
        $.fn.portletReport.settings = {
            app: 0,
            form: 0,
            register: 0
        };

        // Devuelvo la lista de objetos jQuery
        return this.each(function () {
            $.fn.portletReport.options = $.extend($.fn.portletReport.settings, opc);
            $.fn.portletReport.getContent($(this));
        });
    };

    $.fn.portletReport.getContent = function (obj) {

        $("#_status_").val("Cargando reportes de la aplicacion...");
        sWS = "control?$cmd=plain&$cf=257&$ta=select&$w=clave_forma=" + $.fn.portletReport.options.form + " AND clave_perfil=" + $("#_cp_").val();

        $.ajax({
            url: sWS,
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function (data) {
                if (typeof data == "string") {
                    xmlReport = new ActiveXObject("Microsoft.XMLDOM");
                    xmlReport.async = false;
                    xmlReport.validateOnParse = "true";
                    xmlReport.loadXML(data);
                    if (xmlReport.parseError.errorCode > 0)
                        alert("Error de compilaci�n xml:" + xmlReport.parseError.errorCode + "\nParse reason:" + xmlReport.parseError.reason + "\nLinea:" + xmlReport.parseError.line);
                }
                else
                    xmlReport = data;

                $("#_status_").val("Contruyendo portlet de reportes");
                sHtml = "";
                if ($(xmlReport).find("error").length)
                    sHtml = $(xmlReport).find("error").text();

                sHtml = "";
                $(xmlReport).find("registro").each(function () {

                    nClaveReporte = $(this).find("clave_reporte")[0].firstChild.data;
                    nClaveForma = $(this).find("clave_forma")[0].firstChild.data;
                    sReporte = $(this).find("reporte")[0].firstChild.data;
                    nClavePerfil = $(this).find("clave_perfil")[0].firstChild.data;
                    sJSP = $(this).find("jsp")[0].firstChild.data;
                    nParametros = $(this).find("parametros")[0].firstChild.data;
                    nClaveTipoReporte = $(this).find("clave_tipo_reporte")[0].firstChild.data;

                    sHtml += "<div class='reporte'>" +
                            "<a href='#' id='lnkReporte_" + nClaveReporte + "' class='lnkReporte' reporte='" + nClaveReporte + "' perfil='" + nClavePerfil + "' jsp='" + sJSP + "' parametros='" + nParametros + "' tipo_reporte='" + nClaveTipoReporte + "' forma='" + nClaveForma + "'>" +
                            sReporte + "</a></div>";

                });

                $(obj[0].childNodes[1]).html(sHtml);

                //Le asigna el evento clic a todos los links de la bitacora
                $(".lnkReporte").unbind("click").click(function () {
                    if ($(this).attr("tipo_reporte") == 3) {
                        //Solo descarga el zip
                        window.open($(this).attr("jsp"));
                    } else {
                       /* if ($(this).attr("parametros") == 0) {
                            //lanzalo 
                            var caracteristicas = "height=800,width=800,scrollTo,resizable=1,scrollbars=1,location=0";
                            window.open('control?$cmd=report&$cr=' + $(this).attr("reporte") + "&$pk=", "_blank", caracteristicas);

                        } else {*/
                            $("#divwait")
                                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                                    .attr('title', 'Espere un momento por favor')
                                    .dialog({
                                        height: 140,
                                        modal: true,
                                        autoOpen: true,
                                        closeOnEscape: false
                                    });

                            $("#top").reportParameter({
                                titulo: "Par\u00e1metros del reporte '" + $(this).text() + "'",
                                app: "1",
                                forma: $(this).attr("forma"),
                                reporte: $(this).attr("reporte"),
                                filtroForaneo: "",
                                top: 122,
                                height: 500,
                                width: 510,
                                events: [],
                                error: ""
                            }
                            );
                        /*}*/
                    }
                });
                $("#_status_").val("");

            },
            error: function (xhr, err) {
                if (xhr.responseText.indexOf("Iniciar sesi&oacute;n") > -1) {
                    //alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                    window.location = 'login.jsp';
                } else {
                    alert("Error al recuperar registros: " + xhr.readyState + "\nstatus: " + xhr.status + "\responseText:" + xhr.responseText);
                }
                $("#_status_").val("");
            }
        });
    }


})(jQuery);