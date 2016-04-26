function pagarex_pagare_init() {
    folioPagare = $(xml).find("folio_pagare")[0].childNodes[0].data;
    claveEstatus = $(xml).find("clave_estatus")[0].childNodes[0].data;
    claveContrato  = $(xml).find("clave_contrato")[0].childNodes[0].data;
    
    //Si se trata de un nuevo pagaré y no es una forma de búsqueda
    if ($("#form_29_342_0").length>0) {
       if (folioPagare == "" && $("#form_29_342_0")[0][19].value!="lookup") {
            $("#clave_estatus option[value='2']").remove();
            $("#clave_estatus option[value='3']").remove();

            //Agrega opción en blanco en el campo de contratos y habilita la función para
            //extraer al cliente en función al contrato
            $('#clave_cliente').find('option').remove().end();
            $("#clave_contrato option:first").before("<option value='' selected='selected'></option>");
            $("#clave_contrato").unbind("change").change(function() {
                if ($(this).val()!="") {
                   setXMLInSelect3("form_29_342_0 #clave_cliente", 333, "foreign", null,"clave_cliente IN (SELECT clave_cliente FROM pagarex_contrato WHERE clave_contrato="+ $(this).val() +")");
                }       
             });        
        }
    }


    $("#td_clave_cuenta").parent().hide();
    $("#td_numero_transferencia").parent().hide();
    $("#td_fecha_a_transferir").parent().hide();

    $("#clave_estatus").change(function() {
        if ($(this).val() == "2") {
            $("#td_clave_cuenta").parent().show();
            $("#td_numero_transferencia").parent().show();
            $("#td_fecha_a_transferir").parent().show();
            $("#clave_cuenta").addClass("obligatorio");
            //$("#numero_transferencia").addClass("obligatorio");
            $("#fecha_a_transferir").addClass("obligatorio");
        } else {
            $("#td_clave_cuenta").parent().hide();
            $("#td_numero_transferencia").parent().hide();
            $("#td_fecha_a_transferir").parent().hide();
            $("#clave_cuenta").removeClass("obligatorio");
            //$("#numero_transferencia").removeClass("obligatorio");
            $("#fecha_a_transferir").removeClass("obligatorio");
        }
    });

    
    if (claveEstatus == "2") {
        $("#td_clave_cuenta").parent().show();
        $("#td_numero_transferencia").parent().show();
        $("#td_fecha_a_transferir").parent().show();
        $("#clave_cuenta").addClass("obligatorio");
        $("#numero_transferencia").addClass("obligatorio");
        $("#fecha_a_transferir").addClass("obligatorio");
    }
}

function calendario_cierres_init() {
    var clave_estatus_inicial = $(xml).find("clave_estatus")[0].childNodes[0].data;

    if (clave_estatus_inicial == "1") {
        $("#clave_estatus option[value='3']").remove();
    } else if (clave_estatus_inicial=="3" || clave_estatus_inicial=="4") {
        $("#td_clave_estatus").next().html("<strong style='float:left'>Procesado</strong>");
        $("#fecha_cierre").attr("disabled","disabled");
        $("#clave_poliza").attr("disabled","disabled");
    }

    $("#clave_estatus").change(function() {
        if ($(this).val() == 2) {
            if (!confirm('Una vez cerrado el periodo del calendario de pagos no será posible agregar pólizas con las fechas anteriores al cierre, está seguro que desea continuar?') == 1) {
                $(this).val(clave_estatus_inicial);
            }
        } else if ($(this).val() == 3) {
            if (!confirm('Esta operación generará los intereses del periodo y no es posible deshacerla, ¿está seguro que desea continuar?') == 1) {
                $(this).val(clave_estatus_inicial);
            }

        }
    })

}

function calendario_cierres_grid_init() {
    
    $("a.closing_link").click(function() {
	tasa_tie = (this).id.split("_")[3];
        fecha_cierre = (this).id.split("_")[2];
        id_cierre = (this).id.split("_")[1];
        $("#_cache_").val(fecha_cierre);
        if (confirm('Esta operación generará los intereses del periodo y no es posible deshacerla, ¿está seguro que desea continuar?') == 1) {
            $("#divwait")
                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Procesando cierre...</p>")
                    .attr('title', 'Espere un momento por favor')
                    .dialog({
                height: 140,
                modal: true,
                autoOpen: true,
                closeOnEscape: false
            });

            $.ajax(
                    {
                        url: "control?$cmd=register&$ta=update&$cf=349&$pk=" + id_cierre + "&clave_estatus=3&fecha_cierre="+fecha_cierre+ "&tasa_tie="+tasa_tie,
                        dataType: ($.browser.msie) ? "text" : "xml",
                        success: function(data) {
                            if (typeof data == "string") {
                                xmlCalendario = new ActiveXObject("Microsoft.XMLDOM");
                                xmlCalendario.async = false;
                                xmlCalendario.validateOnParse = "true";
                                xmlCalendario.loadXML(data);
                                if (xmlCalendario.parseError.errorCode > 0) {
                                    alert("Error de compilación xml:" + xmlCalendario.parseError.errorCode + "\nParse reason:" + xmlCalendario.parseError.reason + "\nLinea:" + xmlCalendario.parseError.line);
                                }
                            }
                            else {
                                xmlCalendario = data;
                            }
                                
                            if ($(xmlCalendario).find("error").length > 0) {
                                alert($(xmlCalendario).find("error").text());
                            } else {
                                alert('Cierre procesado exitosamente');

                            }
                            

                            $("#grid_30_349_0").jqGrid().trigger("reloadGrid");
                                
                            $("#divwait").dialog("close");

                        },
                        error: function(xhr, err) {
                        }
                    });
        }
    });
}
