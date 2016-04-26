/*
 * Plugin de jQuery para cargar grid a partir de una paginota
 *
 */
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
 \u00bf -> ¿
 * and open the template in the editor.
 */
function beneficiario_init(ampliado) {
    claveAplicacion = $(xml).find("configuracion_forma").find("clave_aplicacion")[0].childNodes[0].data; //"139";
    claveForma =  $(xml).find("configuracion_forma").find("clave_forma")[0].childNodes[0].data; //"613";
    claveRegistro = ($(xml).find("configuracion_forma").find("pk")[0].childNodes[0].data == "" ? "0" : $(xml).find("configuracion_forma").find("pk")[0].childNodes[0].data);

    if (claveRegistro == "") {
        claveRegistro = "0";
    }

    //Se oculta la fecha de registro, solo se debe mostrar si se trata de una búsqueda
    if ($.fn.form.options.modo != "lookup") {
        $("#td_fecha_registro").parent().hide();
    }

    $("#tienda").attr("disabled", "disabled");
    $("#encargado").attr("disabled", "disabled");
    $("#direccion_tienda").attr("disabled", "disabled");

    $("#form_" + claveAplicacion + "_" + claveForma + "_0").keypress(function (e) {//Para deshabilitar el uso de la tecla "Enter"
        if (e.which == 13) {
            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).button().click();
            return false;
        }

    });
    $("#tienda").attr("disabled", "disabled"); //Desabilitar el campo de la tienda
    $("#encargado").attr("disabled", "disabled");
    $("#form_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro + " #rpu").keyup(function (event) {
        if (event.which == 13) {
            event.preventDefault();
            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).button().click();
        }
    }).blur(function () {

                if ($.fn.form.options.modo == "lookup" || $(this).val() == "")
                    return;

                if (!check_number(this)) {
                    alert("El RPU debe contener solo n\u00fameros, verifique");
                    $(this).val("");
                    $("#form_129_613_0").find("input[type=text], textarea, select").val("");
                    $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled");
                    return;
                }

                if ($(this).val().length != 12) {
                    alert("El RPU consultado no es v\u00e1lido, favor de verificar");
                    $(this).val("");
                    $("#form_129_613_0").find("input[type=text], textarea, select").val("");
                    $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled");
                    return;
                }

                $("#_status_").val("Validando beneficiario");
                $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Validando beneficiario...</p>")
                        .attr('title', 'Espere un momento por favor')
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape: false
                        });

                $("#divwait").parent().parent().css("zIndex", 9999);

                $.ajax({
                    url: "control?$cmd=validabeneficiario&rpu=" + $(this).val(),
                    dataType: ($.browser.msie) ? "text" : "xml",
                    type: "POST",
                    success: function (data) {
                        if (typeof data == "string") {
                            xmlRPU = new ActiveXObject("Microsoft.XMLDOM");
                            xmlRPU.async = false;
                            xmlRPU.validateOnParse = "true";
                            xmlRPU.loadXML(data);

                            if (xmlRPU.parseError.errorCode > 0) {
                                alert("Error de compilación xml:" + xmlRPU.parseError.errorCode + "\nParse reason:" + xmlRPU.parseError.reason + "\nLinea:" + xmlRPU.parseError.line);
                            }
                        }
                        else {
                            xmlRPU = data;
                        }

                        $("#_status_").val("");
                        //Compruba que no venga con errores
                        if ($(xmlRPU).find("error").length > 0) {
                            $("#divwait").dialog("close");
                            $("#form_129_613_0").find("input[type=text], textarea,select").val("");
                            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled");

                            $("#tarifa").val("");
                            $("#nombre").val("");
                            $("#direccion").val("");

                            //Validar  la zona
                            //zona = $(xmlRPU).find("zona")[0].childNodes[0].data; // 01=urbana
                            $("#poblacion").val("");
                            $("#clave_estado")[0].selectedIndex = -1
                            $("#clave_municipio")[0].selectedIndex = -1
                            
                            if ($("#_cp_").val() != 4)
                                ("#clave_municipio_entrega")[0].selectedIndex = -1
                            
                            var mensaje = $(xmlRPU).find("error").text().split(",");
                            if (mensaje[1]) {
                                if (confirm(mensaje[0] + ". Haga clic en 'Aceptar' para ver el registro.")) {
                                    $("#divwait")
                                            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                                            .attr('title', 'Espere un momento por favor')
                                            .dialog({
                                                height: 140,
                                                modal: true,
                                                autoOpen: true,
                                                closeOnEscape: false
                                            });

                                    $("#dlgModal_129_613_0").remove();

                                    $("body").form({
                                        app: "139",
                                        forma: 613,
                                        modo: "update",
                                        columnas: 1,
                                        pk: mensaje[1],
                                        filtroForaneo: "2=clave_aplicacion=142&3=",
                                        height: "90%",
                                        width: "80%",
                                        originatingObject: "",
                                        showRelationships: "false",
                                        updateControl: "",
                                        secondFieldText: "" //Puesto que se trata de un registro nuevo, 
                                    });


                                    if ($.fn.form.options.modo == "update" && $("#_cp_").val() == 4) {
                                        $("#btnGuardar_" + "139" + "_" + "613" + "_" + mensaje[1]).attr("disabled", "disabled");
                                        $("#clave_estado").css("pointer-events", "none");
                                        $("#clave_municipio").css("pointer-events", "none");
                                        $("#clave_estado_entrega").css("pointer-events", "none");
                                        $("#clave_municipio_entrega").css("pointer-events", "none");
                                        $("#clave_localidad_entrega").css("pointer-events", "none");
                                        $("#clave_punto").css("pointer-events", "none");
                                        $("#clave_carpeta").css("pointer-events", "none");
                                        $("#clave_tipo_padron").css("pointer-events", "none");

                                        //$("#formTab_129_613_"+claveRegistro).find('select').attr('disabled',true);
                                        if ($.fn.form.options.modo == "update") {
                                            $("#rpu").attr("readonly", "readonly");
                                        }

                                        $("#nombre").attr("readonly", "readonly");
                                        $("#poblacion").attr("readonly", "readonly");
                                        $("#colonia").attr("readonly", "readonly");
                                        $("#direccion").attr("readonly", "readonly");
                                        $("#cp").attr("readonly", "readonly");
                                        $("#telefono").attr("readonly", "readonly");
                                        $("#pagina").attr("readonly", "readonly");
                                        $("#fecha_entrega").attr("readonly", "readonly");
                                    }

                                }

                            }
                            else {
                                alert(mensaje[0]);
                            }
                        } else {
                            rpu = $(xmlRPU).find("rpu")[0].childNodes[0].data.substring(3, 15);
                            $("#fecha_registro").val("%ahora");
                            //Validar  el tipo de facturacion
                            //$("#tarifa").val($(xmlRPU).find("tipo_facturacion")[0].childNodes[0].data);
                            $("#nombre").val($(xmlRPU).find("nombre_beneficiario")[0].childNodes[0].data);
                            $("#direccion").val($(xmlRPU).find("direccion")[0].childNodes[0].data);

                            //Validar  la zona
                            //zona = $(xmlRPU).find("zona")[0].childNodes[0].data; // 01=urbana
                            $("#poblacion").val($(xmlRPU).find("poblacion")[0].childNodes[0].data);
                            claveEstado = $(xmlRPU).find("clave_estado")[0].childNodes[0].data;

                            $("#clave_estado option[value='" + claveEstado + "']").attr("selected", "selected");

                            if ($.fn.form.options.modo == "insert") {
                                if ($("#_cp_").val() != 4)
                                    $("#clave_estado_entrega option[value='" + claveEstado + "']").attr("selected", "selected");
                                
                                if ($("#_cp_").val() != 4)
                                setXMLInSelect4("#clave_municipio_entrega", 611, "foreign", '', "fide_municipio.clave_estado=" + claveEstado);
                            }

                            //Llena la lista de municipios de acuerdo al estado
                            setXMLInSelect4("#clave_municipio", 611, "select", '', "clave_estado=" + claveEstado);
                            $("#clave_municipio option[value='" + $(xmlRPU).find("clave_municipio")[0].childNodes[0].data + "']").attr("selected", "selected");

                            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).button("enable");

                            $("#divwait").dialog("close");
                        }

                    },
                    error: function (xhr, err) {
                        alert("Error al buscar RPU: \n" + +xhr.responseText);
                    }
                });

            });

    if ($.fn.form.options.modo == "lookup") {
        b = document.getElementById("$b");
        $(b).parent().parent().hide();
        $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).click(function () {
            $("#_cache_").val("buscandoBeneficiario=" + $("#rpu").val());
        });

    }

    if ($(xml).find("clave_estado").length > 0)
        claveEstado = $(xml).find("clave_estado")[0].childNodes[0].data;
    else
        claveEstado = "";

    if ($(xml).find("clave_estado_entrega").length > 0)
        claveEstadoEntrega = $(xml).find("clave_estado_entrega")[0].childNodes[0].data;
    else
        claveEstadoEntrega = ""

    if ($(xml).find("clave_municipio_entrega").length > 0)
        claveMunicipioEntrega = $(xml).find("clave_municipio_entrega")[0].childNodes[0].data;
    else
        claveMunicipioEntrega = ""

    if ($(xml).find("clave_localidad_entrega").length > 0)
        claveLocalidadEntrega = $(xml).find("clave_localidad_entrega")[0].childNodes[0].data;
    else {
        claveLocalidadEntrega = "";
    }

    if ($(xml).find("clave_punto").length>0)
        clavePuntoEntrega = $(xml).find("clave_punto")[0].childNodes[0].data;
    else
        clavePuntoEntrega = "";
    
    if ($.fn.form.options.modo == "insert") {
        if ($("#_cache_").val().indexOf("agregaBeneficiario") == 0) {
            $("#form_" + claveAplicacion + "_" + claveForma + "_0 #rpu").val($("#_cache_").val().split("=")[1]).change();
            $("#_cache_").val("");
        }

        if ($("#_cp_").val() == 4) {
            $("#clave_tipo_padron option[value='1']").remove();
        }
    }

    if (claveEstado != "") {
        //Llena la lista de municipios de acuerdo al estado
        if ($("#clave_municipio").length > 0) {
            setXMLInSelect4("#clave_municipio", 611, "select", '', "clave_estado=" + claveEstado);
            $("#clave_municipio option[value='" + $(xml).find("clave_municipio")[0].childNodes[0].data + "']").attr("selected", "selected");
        }
    }

    if (claveEstadoEntrega != "") {
        //Llena la lista de municipios de acuerdo al estado
        setXMLInSelect4("#clave_municipio_entrega", 611, "foreign", '', "fide_municipio.clave_estado=" + claveEstadoEntrega);
        $("#clave_municipio_entrega option[value='" + claveMunicipioEntrega + "']").attr("selected", "selected");

        //Filtra las tiendas cercanas al estado 
        if (claveMunicipioEntrega != "") {
            //Llena la lista de localidades de acuerdo al municipio
            setXMLInSelect4("#clave_localidad_entrega", 696, "select", '', "clave_municipio=" + claveMunicipioEntrega);
            $("#clave_localidad_entrega option[value='" + claveLocalidadEntrega + "']").attr("selected", "selected");

            if (claveLocalidadEntrega != "") {
                setXMLInSelect4($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto", 681, "foreign", '', "(clave_localidad=" + claveLocalidadEntrega + ($.fn.form.options.modo == "update" ? " OR fide_punto_entrega.clave_punto=" + clavePuntoEntrega + ")" : ""));
                $(($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto")+" option[value='" + clavePuntoEntrega + "']").attr("selected", "selected");

                //Filtra las carpetas de solo el punto de entrega
                if (clavePuntoEntrega != "") {
                    setXMLInSelect4("#clave_carpeta", 682, "foreign", '', "clave_punto=" + clavePuntoEntrega);
                    $("#clave_carpeta option[value='" + $(xml).find("clave_carpeta")[0].childNodes[0].data + "']").attr("selected", "selected");
                }
            }

        }

    }

    $("#clave_estado, #fide_beneficiario\\.clave_estado").change(function () {
        //Llena la lista de municipios de acuerdo al estado
        if ($(this).val() != "") {
            if ($("#clave_municipio").length>0)
                setXMLInSelect4("#clave_municipio", 611, "select", '', "clave_estado=" + $(this).val());
            else 
                setXMLInSelect4("#fide_beneficiario\\.clave_municipio", 611, "select", '', "clave_estado=" + $(this).val());
        } else {
            $("#clave_municipio, #fide_beneficiario\\.clave_municipio").empty();
            $("#tienda,#encargado").val("");
        }
    });

    $("#clave_estado_entrega").change(function () {
        //Llena la lista de municipios de acuerdo al estado
        if ($(this).val() != "") {
            setXMLInSelect4("#clave_municipio_entrega", 611, "foreign", '', "fide_municipio.clave_estado=" + $(this).val());
            $("#clave_localidad_entrega," + ($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto") + ",#clave_carpeta").empty();
            $("#tienda,#encargado").val("");
        } else {
            $("#clave_municipio_entrega,#clave_localidad_entrega," + ($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto") + ",#clave_carpeta").empty();
            $("#tienda,#encargado").val("");
        }
    });


    /*$("#clave_municipio").change(function () {
        //Llena la lista de municipios de acuerdo al estado
        if ($(this).val() != "") {
            //Filtra las tiendas cercanas al municipio 
            setXMLInSelect4("#clave_localidad", 696, "select", '', "clave_municipio=" + $(this).val());
        } else {
            $("#poblacion").empty();
            $("#tienda,#encargado").val("");
        }

    }); */

    $("#clave_municipio_entrega").change(function () {
        //Llena la lista de municipios de acuerdo al estado
        if ($(this).val() != "") {
            //Filtra las tiendas cercanas al municipio 
            setXMLInSelect4("#clave_localidad_entrega", 696, "foreign", '', "fide_localidad.clave_municipio=" + $(this).val());
            $( ($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto") + ",#clave_carpeta").empty();
        } else {
            $("#clave_localidad_entrega," + ($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto") + ",#clave_carpeta").empty();
            $("#tienda,#encargado").val("");
        }

    });

    $("#clave_localidad_entrega").change(function () {
        //Llena la lista de municipios de acuerdo al estado
        if ($(this).val() != "") {
            //Filtra las tiendas cercanas al municipio 
            setXMLInSelect4(($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto"), 681, "foreign", '', "clave_localidad=" + $(this).val() + (ampliado ? "AND cerrado=0" : ""));
            $("#clave_carpeta").empty();
        } else {
            $(($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto") + ",#clave_carpeta").empty();
            $("#tienda,#encargado").val("");
        }
    });

    $(($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto")).change(function () {
        //Llena la lista de municipios de acuerdo al estado

        if ($(this).val() != "") {
            //Filtra las tiendas cercanas al municipio 
            setXMLInSelect4("#clave_carpeta", 682, "foreign", '', "clave_punto=" + $(this).val() + " AND clave_tipo_carpeta=2");
            //Se llama el web service para jalar la fecha de entrega 
            // solo si no es una búsqueda
            if ($.fn.form.options.modo === "lookup")
                return;
            
            
            $.ajax({
                url: "control?$cmd=plain&$cf=683&$ta=foreign&$w=clave_punto=" + $(($.fn.form.options.modo == "lookup"?"#fide_beneficiario\\.clave_punto":"#clave_punto")).val(),
                dataType: ($.browser.msie) ? "text" : "xml",
                type: "POST",
                success: function (data) {
                    if (typeof data == "string") {
                        xmlFecha = new ActiveXObject("Microsoft.XMLDOM");
                        xmlFecha.async = false;
                        xmlFecha.validateOnParse = "true";
                        xmlFecha.loadXML(data);

                        if (xmlFecha.parseError.errorCode > 0) {
                            alert("Error de compilación xml:" + xmlFecha.parseError.errorCode + "\nParse reason:" + xmlFecha.parseError.reason + "\nLinea:" + xmlFecha.parseError.line);
                        }
                    }
                    else {
                        xmlFecha = data;
                    }

                    $("#_status_").val("");
                    //Compruba que no venga con errores
                    if ($(xmlFecha).find("error").length > 0) {
                        alert($(xmlFecha).find("error").text());
                    } else {

                        sFecha = $(xmlFecha).find("ano_fecha_programada")[0].childNodes[0].data + "-" + $(xmlFecha).find("mes_fecha_programada")[0].childNodes[0].data + "-28";
                        dfechaEntrega = new Date(parseInt(sFecha.split("-")[0]), parseInt(sFecha.split("-")[1]) - 1, parseInt(sFecha.split("-")[2]), 0, 0, 0);

                        sFecha = $(xmlFecha).find("hoy")[0].childNodes[0].data;
                        dHoy = new Date(parseInt(sFecha.split("-")[0]), parseInt(sFecha.split("-")[1]) - 1, parseInt(sFecha.split("-")[2]), 0, 0, 0);

                        if (dfechaEntrega > dHoy) {
                            dfechaEntrega = new Date(dfechaEntrega.getTime() + 30 * 24 * 60 * 60 * 1000);
                        } else {
                            dfechaEntrega = new Date(dHoy.getTime() + 30 * 24 * 60 * 60 * 1000);
                        }

                        $("#fecha_entrega").val(sDateToString(dfechaEntrega));
                        $("#tienda").val($(xmlFecha).find("tienda")[0].childNodes[0].data);
                        $("#direccion_tienda").val($(xmlFecha).find("direccion")[0].childNodes[0].data);
                        $("#encargado").val($(xmlFecha).find("encargado")[0].childNodes[0].data);
                        $("#divwait").dialog("close");
                    }

                },
                error: function (xhr, err) {
                    alert("Error al buscar fecha de entrega: \n" + +xhr.responseText);
                }
            });
        }
    });

    //Si el perfil es el de call center deshabilita los controles de la forma
    if ($.fn.form.options.modo == "insert" && $("#_cp_").val() == 4) {    
            $("#btnGuardar_129_613_0").hide();
    }
    
    if ($.fn.form.options.modo == "update" && $("#_cp_").val() == 4) {
        $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled");
        $("#clave_estado").css("pointer-events", "none");
        $("#clave_municipio").css("pointer-events", "none");
        $("#clave_estado_entrega").css("pointer-events", "none");
        $("#clave_municipio_entrega").css("pointer-events", "none");
        $("#clave_punto").css("pointer-events", "none");
        $("#clave_carpeta").css("pointer-events", "none");
        $("#clave_tipo_padron").css("pointer-events", "none");

        //$("#formTab_129_613_"+claveRegistro).find('select').attr('disabled',true);
        if ($.fn.form.options.modo == "update") {
            $("#rpu").attr("readonly", "readonly");
        }

        $("#nombre").attr("readonly", "readonly");
        $("#poblacion").attr("readonly", "readonly");
        $("#colonia").attr("readonly", "readonly");
        $("#direccion").attr("readonly", "readonly");
        $("#cp").attr("readonly", "readonly");
        $("#telefono").attr("readonly", "readonly");
        $("#pagina").attr("readonly", "readonly");
        $("#fecha_entrega").attr("readonly", "readonly");
    }
}

function beneficiario_grid_init(ampliado) {
    //Verifica si viene vacía la búsqueda
    registros = parseInt($("#grid_129_613_0").getGridParam("reccount"));
    //Toma el where del la consulta
    url = $("#grid_129_613_0").jqGrid('getGridParam', 'url').split("$w=")[1];
    rpu = "";
    if (url != "") {
        aUrl = url.split("&");
        for (i = 0; i < aUrl.length; i++) {
            if (aUrl[i].indexOf("rpu") == 0) {
                rpu = aUrl[i].replace("rpu%20like%20'", "").replace("%25'", "");
                break;
            }
        }
    }

    if (registros == 0 && $("#_cp_").val() == "4" && ($("#lnkRemoveFilter_grid_129_613_0").length > 0)) {
        if (confirm("El RPU no se encuentra registrado, \u00bfDesea validar si puede ser beneficiario?")) {
            if ($("#_cache_").val().indexOf("buscandoBeneficiario") == 0) {
                $("#_cache_").val("agregaBeneficiario=" + $("#_cache_").val().split("=")[1]);
            }
        }
    } else if (registros == 0 /* && $("#_cp_").val() == "10" */&& !isNaN(rpu) && rpu.length == 12) {
        //if (confirm("El RPU no se encuentra registrado, \u00bfDesea validar si puede ser beneficiario?")) {
            if ($("#_cache_").val().indexOf("buscandoBeneficiario") == 0) {
               $("#divwait")
                .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;B\u00fasqueda en padr\u00f3n de programas anteriores en progreso...</p>")
                .attr('title', 'Espere un momento por favor')
                .dialog({
                    height: 140,
                    modal: true,
                    autoOpen: true,
                    closeOnEscape: false
                });
                
                // Hace validacion
                $.ajax({
                url: "control?$cmd=plain&$cf=725&$pk=0&$ta=select&$w=rpu='" + $("#_cache_").val().split("=")[1] + "'",
                dataType: ($.browser.msie) ? "text" : "xml",
                success: function (data) {
                    if (typeof data == "string") {
                        xmlPadronAnterior = new ActiveXObject("Microsoft.XMLDOM");
                        xmlPadronAnterior.async = false;
                        xmlPadronAnterior.validateOnParse = "true";
                        xmlPadronAnterior.loadXML(data);

                        if (xmlPadronAnterior.parseError.errorCode > 0) {
                            alert("Error de compilación xml:" + xmlPadronAnterior.parseError.errorCode + "\nParse reason:" + xmlPadronAnterior.parseError.reason + "\nLinea:" + xmlPadronAnterior.parseError.line);
                        }
                    }
                    else {
                        xmlPadronAnterior = data;
                    }
                    
                    
                    if ($(xmlPadronAnterior).find("programa").length==0) {
                        alert("El servicio de luz especificado no se encontr\u00fa");
                    } else {
                        if ($(xmlPadronAnterior).find("programa").text()!="") {
                            alert("El servicio de energ\u00eda indicado particip\u00f3 en el Programa 'Ah\u00f3rrate una luz " + $($(xmlPadronAnterior).find("programa")[0]).text() + "'");
                        }
                    }
                    
                    $("#_cache_").val("");
                    $("#divwait").dialog("close");

                },
                error: function (xhr, err) {
                    alert("Error al recuperar los datos del padr\u00f3n anterior: \n" + +xhr.responseText);
                }
            });
                
            }
        //}
    } 

    if ($("#_cache_").val().indexOf("agregaBeneficiario") == 0) {
        $("#divwait")
                .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                .attr('title', 'Espere un momento por favor')
                .dialog({
                    height: 140,
                    modal: true,
                    autoOpen: true,
                    closeOnEscape: false
                });

        $("body").form({
            app: "139",
            forma: 613,
            modo: "insert",
            columnas: 1,
            pk: 0,
            filtroForaneo: "2=clave_aplicacion=142&3=",
            height: "90%",
            width: "80%",
            originatingObject: "",
            showRelationships: "false",
            updateControl: "",
            secondFieldText: "" //Puesto que se trata de un registro nuevo, 
        });

    }

    $(".startsurvey").click(function () {
        nCuestionario = this.id.split("_")[1];
        nProspecto = this.id.split("_")[2];
        sModo = this.id.split("_")[3];
        nFormaEntidadParticipante = this.id.split("_")[4];

        if (sModo == 'insert') {
            $("#divwait")
                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando cuestionario...</p>")
                    .attr('title', 'Espere un momento por favor')
                    .dialog({
                        height: 140,
                        modal: true,
                        autoOpen: true,
                        closeOnEscape: false
                    });
            //Inserta cuestionario de participante en blanco
            $.ajax({
                url: "control?$cmd=register&$cf=633&$pk=0&$ta=insert&clave_beneficiario=" + nProspecto + "&clave_cuestionario=" + nCuestionario + "&clave_empleado=" + $("#_ce_").val() + "&clave_estatus=1&fecha_inicio=%ahora",
                dataType: ($.browser.msie) ? "text" : "xml",
                success: function (data) {
                    if (typeof data == "string") {
                        xmlCuestionario = new ActiveXObject("Microsoft.XMLDOM");
                        xmlCuestionario.async = false;
                        xmlCuestionario.validateOnParse = "true";
                        xmlCuestionario.loadXML(data);

                        if (xmlCuestionario.parseError.errorCode > 0) {
                            alert("Error de compilación xml:" + xmlCuestionario.parseError.errorCode + "\nParse reason:" + xmlCuestionario.parseError.reason + "\nLinea:" + xmlCuestionario.parseError.line);
                        }
                    }
                    else {
                        xmlCuestionario = data;
                    }

                    nCuestionario = $(xmlCuestionario).find("pk")[0].childNodes[0].data;

                    $("#top").survey({
                        pk: nCuestionario,
                        modo: "open",
                        claveProspecto: nProspecto,
                        claveFormaEntidadParticipante: nFormaEntidadParticipante,
                        gridPorActualizar: "grid_150_733_0"
                    });

                },
                error: function (xhr, err) {
                    alert("Error al recuperar los datos del cuestionario: \n" + +xhr.responseText);
                }
            });
        } else {
            $("#divwait")
                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Abriendo cuestionario...</p>")
                    .attr('title', 'Espere un momento por favor')
                    .dialog({
                        height: 140,
                        modal: true,
                        autoOpen: true,
                        closeOnEscape: false
                    });

            $("#top").survey({
                pk: nCuestionario,
                modo: sModo,
                claveProspecto: nProspecto,
                claveFormaEntidadParticipante: nFormaEntidadParticipante,
                gridPorActualizar: "grid_150_733_0"
            });
        }
    });

}

function garantia_init() {
    claveGarantia = $(xml).find("clave_garantia")[0].childNodes[0].data;
    claveEstado = $(xml).find("clave_estado")[0].childNodes[0].data;
    claveAplicacion = $(xml).find("clave_aplicacion")[0].childNodes[0].data;
    claveForma = $(xml).find("clave_forma")[0].childNodes[0].data;
    claveBeneficiario = $("#clave_beneficiario").val();

    //Se verifica la tienda de los detalles del beneficiario
    if ($("#form_129_613_" + claveBeneficiario + " #clave_punto").length > 0)
        claveTienda = $("#form_129_613_" + claveBeneficiario + " #clave_punto").val();
    else
        claveTienda = $("#form_139_677_" + claveBeneficiario + " #clave_punto").val();

    if (claveGarantia == "")
        claveGarantia = "0";

    //Se llena el combo de clave_punto 
    if (claveTienda != "") {
        setXMLInSelect4("#form_" + claveAplicacion + "_" + claveForma + "_" + claveGarantia + " #clave_punto", 673, "foreign", '', "clave_punto in (SELECT clave_punto_padre FROM fide_punto_entrega WHERE clave_punto=" + claveTienda + " AND cerrado=0)");
        //En este punto se debe verificar si la tienda
        $("#form_" + claveAplicacion + "_" + claveForma + "_" + claveGarantia + " #clave_punto option[value='" + $(xml).find("clave_punto")[0].childNodes[0].data + "']").attr("selected", "selected");

    }
}

function encuesta_opinion_init() {
    //Oculta la pregunta ¿cuál? y la repuesta al iniciar el cuestionario
    //Esta es la pregunta
    //Obtenemos el id de la forma para poder ocultar o mostrar las respuestas
    id = "#" + $("form").attr("id");
    // obtener el número de la pregunta 
    preguntauno = $("input:radio").attr("id").substring(15, 18);
    preguntatres = parseInt(preguntauno) + 2;
    preguntacinco = parseInt(preguntauno) + 4;
    preguntanueve = parseInt(preguntauno) + 8;
    preguntaonce = parseInt(preguntauno) + 10;

    //   alert($("input[name='claveRespuesta_"+preguntauno+"']:radio").is(':checked'));
    //   verificamos el estatus de la pregunta para el inicio del programa
    if ($("input:radio[name='claveRespuesta_" + preguntauno + "']:checked").attr("value") == "76" || $("input[name='claveRespuesta_" + preguntauno + "']:radio").is(':checked') == false)
    {
        $($(".trPregunta")[1]).hide();
        //Estas son las respuestas
        $($(".trPregunta")[1]).next().hide();
        //$('input[type="checkbox"]').removeAttr('checked');           

        // alert($("input[name='trPregunta']:checked").val() );    

        //$(this).prop('checked',false);
        //$($(".trPregunta")[1]).next().removeAttr('checked');
    }
    else {

        $($(".trPregunta")[1]).show();
        //Estas son las respuestas
        $($(".trPregunta")[1]).next().show();

    }

    if ($("input:radio[name='claveRespuesta_" + preguntatres + "']:checked").attr("value") == "84" || $("input[name='claveRespuesta_" + preguntatres + "']:radio").is(':checked') == false)
    {
        $($(".trPregunta")[3]).hide();
        $("#chkStatus").prop("checked", false)
        //Estas son las respuestas
        $($(".trPregunta")[3]).next().hide();
    }
    else {

        $($(".trPregunta")[3]).show();
        //Estas son las respuestas
        $($(".trPregunta")[3]).next().show();

    }
    if ($("input:radio[name='claveRespuesta_" + preguntacinco + "']:checked").attr("value") == "91" || $("input[name='claveRespuesta_" + preguntacinco + "']:radio").is(':checked') == false)
    {
        $($(".trPregunta")[5]).hide();
        //Estas son las respuestas
        $($(".trPregunta")[5]).next().hide();
    }
    else {

        $($(".trPregunta")[5]).show();
        //Estas son las respuestas
        $($(".trPregunta")[5]).next().show();

    }
    if ($("input:radio[name='claveRespuesta_" + preguntanueve + "']:checked").attr("value") == "104" || $("input[name='claveRespuesta_" + preguntanueve + "']:radio").is(':checked') == false)
    {
        $($(".trPregunta")[9]).hide();
        //Estas son las respuestas
        $($(".trPregunta")[9]).next().hide();
    }
    else {

        $($(".trPregunta")[9]).show();
        //Estas son las respuestas
        $($(".trPregunta")[9]).next().show();

    }

    if ($("input:radio[name='claveRespuesta_" + preguntaonce + "']:checked").attr("value") == "112" || $("input[name='claveRespuesta_" + preguntaonce + "']:radio").is(':checked') == false)
    {
        $($(".trPregunta")[11]).hide();
        //Estas son las respuestas
        $($(".trPregunta")[11]).next().hide();
    }
    else {

        $($(".trPregunta")[11]).show();
        //Estas son las respuestas
        $($(".trPregunta")[11]).next().show();

    }
    //Condiciona el despliegue
    $(id).click(function () {

        if ($("input:radio[name='claveRespuesta_" + preguntauno + "']:checked").attr("value") == "77") {
            $($(".trPregunta")[1]).show();
            $($(".trPregunta")[1]).next().show();
        } else {
            $($(".trPregunta")[1]).hide();
            $($(".trPregunta")[1]).next().hide();
        }
    });
    $(id).click(function () {

        if ($("input:radio[name='claveRespuesta_" + preguntatres + "']:checked").attr("value") == "85") {
            $($(".trPregunta")[3]).show();
            $($(".trPregunta")[3]).next().show();
        } else {
            $($(".trPregunta")[3]).hide();
            $($(".trPregunta")[3]).next().hide();
        }
    });


    $(id).click(function () {

        if ($("input:radio[name='claveRespuesta_" + preguntacinco + "']:checked").attr("value") == "92") {
            $($(".trPregunta")[5]).show();
            $($(".trPregunta")[5]).next().show();
        } else {
            $($(".trPregunta")[5]).hide();
            $($(".trPregunta")[5]).next().hide();
        }
    });

    $(id).click(function () {

        if ($("input:radio[name='claveRespuesta_" + preguntanueve + "']:checked").attr("value") == "105") {
            $($(".trPregunta")[9]).show();
            $($(".trPregunta")[9]).next().show();
        } else {
            $($(".trPregunta")[9]).hide();
            $($(".trPregunta")[9]).next().hide();
        }
    });

    $(id).click(function () {

        if ($("input:radio[name='claveRespuesta_" + preguntaonce + "']:checked").attr("value") == "113") {
            $($(".trPregunta")[11]).show();
            $($(".trPregunta")[11]).next().show();
        } else {
            $($(".trPregunta")[11]).hide();
            $($(".trPregunta")[11]).next().hide();
        }
    });
}

function encuesta_opinion_beneficiario_init() {
    //Oculta la pregunta ¿cuál? y la repuesta al iniciar el cuestionario
    //Obtenemos el id de la forma para poder ocultar o mostrar las respuestas
    id = "#" + $("form").attr("id");
    // obtener el número de la pregunta 
    preguntauno = $("input:radio").attr("id").substring(15, 22);
    preguntatres = parseInt(preguntauno) + 2;
    preguntacinco = parseInt(preguntauno) + 4;
    preguntanueve = parseInt(preguntauno) + 8;
    preguntaonce = parseInt(preguntauno) + 10;

    if ($("input:radio[name='claveRespuesta_" + preguntauno + "']:checked").attr("value") == "147" || $("input[name='claveRespuesta_" + preguntauno + "']:radio").is(':checked') == false)
    {
        // $($(".trPregunta")[1]).hide();
        //Estas son las respuestas
        // $($(".trPregunta")[1]).next().hide();
        //$('input[type="checkbox"]').removeAttr('checked');           
        alert("Es una prueba");
        //  alert($("input:radio[name='claveRespuesta_"+preguntauno+"']:checked").attr("value")=="147");    

        //$(this).prop('checked',false);
        //$($(".trPregunta")[1]).next().removeAttr('checked');
    }
    else {

        $($(".trPregunta")[1]).show();
        //Estas son las respuestas
        $($(".trPregunta")[1]).next().show();

    }
}//cierre encuesta opinion
