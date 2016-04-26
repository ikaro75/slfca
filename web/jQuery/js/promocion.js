function prmcn_entregables_init() {
    $("#cantidad,#precio_unitario,#costo_directo,#tipo_cambio,#factor_riesgo,#factor_costo_operacion,#factor_recuperacion").change(function() {
        /*Quita formato a numeros para efectuar operaciones */
        $("#costo_directo").val($("#costo_directo").val().replace(/,/g, "").replace(/\$/g, ""));
        $("#tipo_cambio").val($("#tipo_cambio").val().replace(/,/g, "").replace(/\$/g, ""));

        var costo_unitario = ((((parseFloat($("#costo_directo").val())) * (1 + parseInt($("#factor_recuperacion").val()) / 100)) * (1 + parseInt($("#factor_costo_operacion").val()) / 100)) * (1 + parseInt($("#factor_riesgo").val()) / 100)) / parseFloat($("#tipo_cambio").val());
        $("#precio_unitario").val(costo_unitario);
        var importe = costo_unitario * parseInt($("#cantidad").val());
        $("#importe").val(importe);

        /* Aplica formato */
        $("#costo_directo").val(formatCurrency($("#costo_directo").val()));
        $("#tipo_cambio").val(formatCurrency($("#tipo_cambio").val()));
        $("#precio_unitario").val(formatCurrency($("#precio_unitario").val()));
        $("#importe").val(formatCurrency($("#importe").val()));
    });
}

function prmcn_recursos_init() {
    $("#horas_desarrollo").val($("#horas_desarrollo").val().replace(/,/g, "").replace(/\$/g, "")).unbind("focus").unbind("blur").removeClass("money");

    claveTipoRecurso = $(xml).find("clave_tipo_recurso")[0].childNodes[0].data;
    claveRecurso = $(xml).find("clave_recurso")[0].childNodes[0].data;

    if (claveTipoRecurso == 1) {
        /*Llena el select de insumos con los perfiles del cátalogo */
        setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + claveTipoRecurso);
        $("#td_horas_desarrollo").parent().show();
        $("#td_horas_desarrollo").html("Horas de desarrollo");
        $("#td_fecha_inicial").parent().show();
        $("#td_fecha_final").parent().show();
    }
    else if (claveTipoRecurso == 2) {
        /*Llena el select de insumos con los bienes del cátalogo */
        setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + claveTipoRecurso);
        $("#td_horas_desarrollo").parent().hide();
        $("#td_fecha_inicial").parent().hide();
        $("#td_fecha_final").parent().hide();
    }
    else if (claveTipoRecurso == 3) {
        /*Llena el select de insumos con los servicios externos del cátalogo */
        setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + claveTipoRecurso);
        $("#td_horas_desarrollo").parent().hide();
        $("#td_fecha_inicial").parent().hide();
        $("#td_fecha_final").parent().hide();
    }
    else if (claveTipoRecurso == 4) {
        /*Llena el select de insumos con los servicios internos del cátalogo */
        setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + claveTipoRecurso);
        $("#td_horas_desarrollo").parent().hide();
        $("#td_fecha_inicial").parent().show();
        $("#td_fecha_final").parent().show();
    }
    else if (claveTipoRecurso == 5) {
        /*Llena el select de insumos con los viáticos del cátalogo */
        setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + claveTipoRecurso);
        $("#td_horas_desarrollo").html("Días");
        $("#td_horas_desarrollo").parent().show();
        $("#td_fecha_inicial").parent().hide();
        $("#td_fecha_final").parent().hide();
    }
    else if (claveTipoRecurso == 6) {
        /*Llena el select de insumos con los viáticos del cátalogo */
        setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + claveTipoRecurso);
        $("#td_horas_desarrollo").parent().show();
        $("#td_horas_desarrollo").html("Horas de desarrollo");
        $("#td_fecha_inicial").parent().show();
        $("#td_fecha_final").parent().show();
    }

    $("#clave_recurso option[value=" + claveRecurso + "]").attr("selected", true);

    $('#clave_tipo_recurso').change(function() {
        if ($(this).val() == "") {
            /*Llena el select de insumos con los perfiles del cátalogo */
            $("#clave_recurso").find('option').remove();
        }
        if ($(this).val() == 1) {
            /*Llena el select de insumos con los perfiles del cátalogo */
            setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + $(this).val());
            $("#td_horas_desarrollo").html("Días");
            $("#td_horas_desarrollo").parent().show();
            $("#td_fecha_inicial").parent().show();
            $("#td_fecha_final").parent().show();
        } else if ($(this).val() == 2) {
            /*Llena el select de insumos con los bienes del cátalogo */
            setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + $(this).val());
            $("#td_horas_desarrollo").parent().hide();
            $("#td_fecha_inicial").parent().hide();
            $("#td_fecha_final").parent().hide();
        } else if ($(this).val() == 3) {
            /*Llena el select de insumos con los servicios externos del cátalogo */
            setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + $(this).val());
            $("#td_horas_desarrollo").parent().hide();
            $("#td_fecha_inicial").parent().hide();
            $("#td_fecha_final").parent().hide();
        } else if ($(this).val() == 4) {
            /*Llena el select de insumos con los servicios internos del cátalogo */
            setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + $(this).val());
            $("#td_horas_desarrollo").parent().hide();
            $("#td_fecha_inicial").parent().show();
            $("#td_fecha_final").parent().show();
        } else if ($(this).val() == 5) {
            /*Llena el select de insumos con los viáticos del cátalogo */
            setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + $(this).val());
            $("#td_horas_desarrollo").html("Días");
            $("#td_horas_desarrollo").parent().show();
            $("#td_fecha_inicial").parent().hide();
            $("#td_fecha_final").parent().hide();
        } else if ($(this).val() == 6) {
            /*Llena el select de insumos con los perfiles del cátalogo */
            setXMLInSelect3("clave_recurso", 354, "foreign", null, "clave_tipo_recurso=" + $(this).val());
            $("#td_horas_desarrollo").html("Días");
            $("#td_horas_desarrollo").parent().show();
            $("#td_fecha_inicial").parent().show();
            $("#td_fecha_final").parent().show();
        }
    });

    $("#clave_recurso").change(function() {
        $.ajax({
            url: "control?$cmd=plain&$cf=354&$pk=" + $(this).val() + "&$ta=select&$w=clave_recurso=" + $(this).val(),
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlInsumo = new ActiveXObject("Microsoft.XMLDOM");
                    xmlInsumo.async = false;
                    xmlInsumo.validateOnParse = "true";
                    xmlInsumo.loadXML(data);

                    if (xmlInsumo.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlInsumo.parseError.errorCode + "\nParse reason:" + xmlInsumo.parseError.reason + "\nLinea:" + xmlInsumo.parseError.line);
                    }
                }
                else {
                    xmlInsumo = data;
                }

                $("#costo_unitario").val($(xmlInsumo).find("importe").text());
                $("#clave_impuesto option[value=" + $(xmlInsumo).find("clave_impuesto").text() + "]").attr("selected", true);
                $("#costo_total").val($(xmlInsumo).find("importe_total").text());
                var IVA = parseFloat($("#clave_impuesto option:selected").html().split("-")[1]) / 100;

                sForma = $($("#costo_unitario").parent().parent().parent().parent().parent())[0].id;
                $("#" + sForma + " #costo_unitario").val($("#" + sForma + " #costo_unitario").val().replace(/,/g, "").replace(/\$/g, ""));
                costoTotal = parseFloat($("#" + sForma + " #costo_unitario").val()) * parseFloat($("#" + sForma + " #cantidad").val()) * (1 + IVA) * parseFloat($("#" + sForma + " #horas_desarrollo").val());
                $("#" + sForma + " #costo_total").val(formatCurrency(costoTotal));
                $("#" + sForma + " #costo_unitario").val(formatCurrency($("#" + sForma + " #costo_unitario").val()));
            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos del insumo: \n" + +xhr.responseText);
            }
        })
    });

    var sForma = $($("#costo_unitario").parent().parent().parent().parent().parent())[0].id;

    $("#" + sForma + " #costo_unitario, #" + sForma + " #cantidad,#" + sForma + " #horas_desarrollo,#" + sForma + " #clave_impuesto").change(function() {
        //Quita formato al importe
        $("#" + sForma + " #costo_unitario").val($("#" + sForma + " #costo_unitario").val().replace(/,/g, "").replace(/\$/g, ""));
        var IVA = parseFloat($("#" + sForma + " #clave_impuesto option:selected").html().split("-")[1]) / 100;
        costoTotal = parseFloat($("#" + sForma + " #costo_unitario").val()) * parseFloat($("#" + sForma + " #cantidad").val()) * parseFloat($("#" + sForma + " #horas_desarrollo").val() * (1 + IVA));
        $("#" + sForma + " #costo_total").val(formatCurrency(costoTotal));
        $("#" + sForma + " #costo_unitario").val(formatCurrency($("#" + sForma + " #costo_unitario").val()));
    });
}