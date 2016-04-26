function proveedor_init() {    
    claveEstado= $(xml).find("clave_estado")[0].childNodes[0].data; 
        
    if (claveEstado!="") {
        //Llena la lista de municipios de acuerdo al estado
        setXMLInSelect4("#clave_municipio",611,"select",'',"clave_estado="+claveEstado);
        $("#clave_municipio option[value='" + $(xml).find("clave_municipio")[0].childNodes[0].data + "']").attr("selected", "selected");
    }
    
    $("#clave_estado").change(function() {
    //Llena la lista de municipios de acuerdo al estado
    if ($(this).val()!="") {
        setXMLInSelect4("#clave_municipio",611,"select",'',"clave_estado="+$(this).val());
    }
    
    $("#clave_estado").click(function() {
        //Llena la lista de municipios de acuerdo al estado
        if ($(this).val()!="") {
            setXMLInSelect4("#clave_municipio",611,"select",'',"clave_estado="+$(this).val());
        }
    });
   
});

}

function embarques_init() {
    claveProveedor= $(xml).find("clave_proveedor")[0].childNodes[0].data; 
    if (claveProveedor!="") {
        //Llena la lista de municipios de acuerdo al estado
        setXMLInSelect4("#clave_producto",644,"select",'',"clave_proveedor="+claveProveedor);
        $("#clave_producto option[value='" + $(xml).find("clave_producto")[0].childNodes[0].data + "']").attr("selected", "selected");
    } else {
        //Toma el valor por default
        claveProveedor = $("#clave_proveedor").val();
        setXMLInSelect4("#clave_producto",644,"select",'',"clave_proveedor="+claveProveedor);
    }
    
    $("#clave_proveedor").click(function() {
        if ($(this).val()!="") {
            setXMLInSelect4("#clave_producto",644,"select",'',"clave_proveedor="+$(this).val());
        }
    });
     
    
}

function compras_init() {
    $("#clave_tipo_documento option[value='1']").remove();
    $("#clave_tipo_documento option[value='2']").remove();
    $("#clave_tipo_documento option[value='3']").remove();
    $("#clave_tipo_documento option[value='4']").remove();
    $("#clave_tipo_documento option[value='5']").remove();
    $("#clave_tipo_documento option[value='7']").remove();
    $("#clave_tipo_documento option[value='8']").remove();
    $("#clave_tipo_documento option[value='9']").remove();
    $("#clave_tipo_documento option[value='10']").remove();
    
    $("#fecha_pago").hide();
    $("#td_fecha_pago").html("");
    $("#td_clave_moneda_enganche").parent().hide();
    $("#td_tipo_cambio_enganche").parent().hide();
    $("#td_numero_pagos").parent().hide();
    $("#td_tipo_cambio_pagos").parent().hide();
    $("#td_dias_plazo").parent().hide();
    $("#td_tasa_interes_pago").parent().hide();
            
    $("#form_31_601_0 #pago_a_plazo").change(function(){
        if ($(this).attr("checked")=="checked")  { // Pago en plazos 
            $("#td_fecha_pago").html("Fecha de pago (a 30 d&iacute;as)");
            $("#fecha_pago").show();
            $("#td_clave_moneda_enganche").parent().show();
            $("#td_tipo_cambio_enganche").parent().show();
            $("#td_numero_pagos").parent().show();
            $("#td_tipo_cambio_pagos").parent().show();
            $("#td_dias_plazo").parent().show();
            $("#td_tasa_interes_pago").parent().show();
            
            $("#fecha_pago").addClass("obligatorio");
            $("#clave_moneda_enganche").addClass("obligatorio");
            $("#tipo_cambio_enganche").addClass("obligatorio");
            $("#importe_enganche").addClass("obligatorio");
            $("#numero_pagos").addClass("obligatorio");
            $("#clave_moneda_pagos").addClass("obligatorio");
            $("#tipo_cambio_pagos").addClass("obligatorio");
            $("#importe_pagos").addClass("obligatorio");
            $("#dias_plazo").addClass("obligatorio");
            $("#fecha_primer_pago").addClass("obligatorio");
            $("#tasa_interes_pago").addClass("obligatorio");
            
            
        } else  {
            $("#td_fecha_pago").html("");
            $("#fecha_pago").hide();
            $("#td_clave_moneda_enganche").parent().hide();
            $("#td_tipo_cambio_enganche").parent().hide();
            $("#td_numero_pagos").parent().hide();
            $("#td_tipo_cambio_pagos").parent().hide();
            $("#td_dias_plazo").parent().hide();
            $("#td_tasa_interes_pago").parent().hide();
            
            $("#fecha_pago").removeClass("obligatorio");
            $("#clave_moneda_enganche").removeClass("obligatorio");
            $("#tipo_cambio_enganche").removeClass("obligatorio");
            $("#importe_enganche").removeClass("obligatorio");
            $("#numero_pagos").removeClass("obligatorio");
            $("#clave_moneda_pagos").removeClass("obligatorio");
            $("#tipo_cambio_pagos").removeClass("obligatorio");
            $("#importe_pagos").removeClass("obligatorio");
            $("#dias_plazo").removeClass("obligatorio");
            $("#fecha_primer_pago").removeClass("obligatorio");
            $("#tasa_interes_pago").removeClass("obligatorio");
        }    
        
    });
}

function compras_detalle_init() {
    $("#cantidad,#importe_unitario,#clave_impuesto").change(function() {
        importe=parseFloat($("#importe_unitario").val().replace(/,/g,"").replace(/\$/g,""));
        porcentaje_iva=parseFloat($("#clave_impuesto option:selected").html().split("%")[0]);
        cantidad=parseFloat($("#cantidad").val());
        importe_iva= cantidad * (parseFloat($("#importe_unitario").val().replace(/,/g,"").replace(/\$/g,"")) * porcentaje_iva /100);

        $("#importe_total").val(formatCurrency(cantidad * importe + importe_iva));
        $("#importe_unitario").val(formatCurrency($("#importe_unitario").val()));
        $("#importe_impuesto").val(formatCurrency(importe_iva));
    });
    
    $("#clave_producto").change(function () {
        $.ajax({
            url: "control?$cmd=plain&$cf=569&$pk=" + $(this).val() + "&$ta=foreign&$w=clave_producto=" + $(this).val(),
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlProducto = new ActiveXObject("Microsoft.XMLDOM");
                    xmlProducto.async = false;
                    xmlProducto.validateOnParse = "true";
                    xmlProducto.loadXML(data);

                    if (xmlProducto.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlProducto.parseError.errorCode + "\nParse reason:" + xmlInsumo.parseError.reason + "\nLinea:" + xmlInsumo.parseError.line);
                    }
                }
                else {
                    xmlProducto = data;
                }

                $("#importe_unitario").val(formatCurrency($(xmlProducto).find("precio").text()));
                $("#clave_unidad option[value=" + $(xmlProducto).find("clave_unidad_entrada").text()+ "]").attr("selected", true);
                $("#clave_impuesto option[value=" + $(xmlProducto).find("clave_impuesto_predeterminado").text().replace("%","")+ "]").attr("selected", true);
                 
            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos del insumo: \n" + +xhr.responseText);
            }
        });
    });    
}

function distribucion_init() {
    var claveEstado ="";
    if ($.fn.form.options.modo=="lookup") {
        $("#clave_punto").attr("name","fide_distribucion.clave_punto");
    }

    var claveAplicacion=$(xml).find("clave_aplicacion")[0].childNodes[0].data; 
    var claveForma=$(xml).find("clave_forma")[0].childNodes[0].data; 
    var claveRegistro=($(xml).find("clave_distribucion")[0].childNodes[0].data==""?"0":$(xml).find("clave_distribucion")[0].childNodes[0].data); 
    
    if($(xml).find("clave_estado").length>0)
       claveEstado=$(xml).find("clave_estado")[0].childNodes[0].data; 
    
   
    if (claveEstado!="") {
        setXMLInSelect4("#formTab_105_" + claveForma + "_" + claveRegistro + " #clave_punto",608,"select",'',"clave_estado="+claveEstado); 
        $("#clave_punto option[value=" + $(xml).find("clave_punto")[0].childNodes[0].data + "]").attr("selected", true);
    }
    
    $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).focus(function () {
        //$("#clave_estado").attr('disabled',true); 
        if ($.fn.form.options.modo!="lookup")
            $("#formTab_105_670_"+claveRegistro).find("#clave_estado").attr('disabled',true);
        
        $("#id_control").prop("disabled", true); 
        $("#lfca_programadas").prop("disabled", true);
        //$(this).click();
    });
    
    $("#tienda").prop('disabled', true);
    $("#lfca_programadas").prop('disabled', true);
      
    $("#formTab_105_" + claveForma + "_" + claveRegistro + " #clave_estado").change(function () {
        setXMLInSelect4("#formTab_105_" + claveForma + "_" + claveRegistro + " #clave_punto",608,"select",'',"clave_estado="+$(this).val()); 
        $("#id_control").val("");
    });
    
    $("#paquetes_programados").change(function () {
        if ($(this).val()!="" && !isNaN($(this).val())) {
            parteDecimal= parseInt($(this).val())/1000*2.5 -  Math.floor(parseInt($(this).val())/1000*2.5);
            $("#stock_garantia_programado").val(((parteDecimal>=0.5)?Math.floor(parseInt($(this).val())/1000*2.5)+1:Math.floor(parseInt($(this).val())/1000*2.5))); 
            $("#lfca_programadas").val(parseInt($(this).val())*5);
        }
    });
    
    $("#clave_punto").change(function () {
        $.ajax({
            url: "control?$cmd=plain&$cf=608&$pk=" + $(this).val() + "&$ta=select&$w=clave_punto=" + $(this).val(),
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlPunto = new ActiveXObject("Microsoft.XMLDOM");
                    xmlPunto.async = false;
                    xmlPunto.validateOnParse = "true";
                    xmlPunto.loadXML(data);

                    if (xmlPunto.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlPunto.parseError.errorCode + "\nParse reason:" + xmlPunto.parseError.reason + "\nLinea:" + xmlPunto.parseError.line);
                    }
                }
                else {
                    xmlPunto = data;
                }

                $("#tienda").val($(xmlPunto).find("punto_entrega")[0].childNodes[0].data + "/" + $(xmlPunto).find("direccion")[0].childNodes[0].data);
            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos del punto de entrega: \n" + +xhr.responseText);
            }
        });        
    });
    
}

function documento_init() {
    $("#importe_calculado").prop("disabled",true);
    $("#importe_a_pagar").prop("disabled",true);
    $("#cantidad_productos_entregados").prop("disabled",true);
    $("#td_clave_documento_padre").parent().hide();
    $("#cantidad").focus(function(){
        $(this).val(($(this).val().replace(/,/g,"")));
    }).blur(function(){
        $(this).val(formatInt($(this).val()));
    }).val(formatInt($("#cantidad").val()));
    
     $("#cantidad_productos_entregados").val(formatInt($("#cantidad").val()));
    
    /*$("#cantidad,#clave_producto").change(function () {
        $.ajax({
            url: "control?$cmd=plain&$cf=644&$pk=" + $(this).val() + "&$ta=foreign&$w=clave_producto=" + $("#clave_producto").val(),
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlProducto = new ActiveXObject("Microsoft.XMLDOM");
                    xmlProducto.async = false;
                    xmlProducto.validateOnParse = "true";
                    xmlProducto.loadXML(data);

                    if (xmlProducto.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlProducto.parseError.errorCode + "\nParse reason:" + xmlInsumo.parseError.reason + "\nLinea:" + xmlInsumo.parseError.line);
                    }
                }
                else {
                    xmlProducto = data;
                }
                
                var precio=parseFloat($(xmlProducto).find("precio").text());
                var cantidad = parseFloat($("#cantidad").val());
                $("#importe").val(formatCurrency(precio*cantidad));
            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos del insumo: \n" + +xhr.responseText);
            }
        });        
    });*/
    
    $("#clave_tipo_documento").change(function () {
       if ($(this).val()=="1")  {
           $("#td_clave_documento_padre").parent().hide();
           $("#td_cantidad_productos_entregados").parent().show();
           $("#td_importe_calculado").parent().show();
           $("#td_importe_a_pagar").parent().show();           
       } else {
           $("#td_clave_documento_padre").parent().show();
           $("#td_cantidad_productos_entregados").parent().hide();
           $("#td_importe_calculado").parent().hide();
           $("#td_importe_a_pagar").parent().hide();
       }
    });
    
}

function documento_partida_init() {
    $("#td_clave_documento").parent().hide();
    $("#td_clave_impuesto").parent().hide();
    $("#td_clave_unidad").parent().hide();
    $("#td_importe_impuesto").parent().hide();
    $("#td_tipo_cambio").parent().hide();
    $("#td_clave_moneda").parent().hide();
    
    $("#clave_producto").change(function() {
        if ($(this).val()=="")  return;
    
        $.ajax({
            url: "control?$cmd=plain&$cf=644&$pk=" + $(this).val() + "&$ta=foreign&$w=clave_producto=" + $(this).val(),
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlProducto = new ActiveXObject("Microsoft.XMLDOM");
                    xmlProducto.async = false;
                    xmlProducto.validateOnParse = "true";
                    xmlProducto.loadXML(data);

                    if (xmlProducto.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlProducto.parseError.errorCode + "\nParse reason:" + xmlInsumo.parseError.reason + "\nLinea:" + xmlInsumo.parseError.line);
                    }
                }
                else {
                    xmlProducto = data;
                }
                
                $("#importe_unitario").val(formatCurrency($(xmlProducto).find("precio").text()));
                $("#clave_unidad option[value=" + $(xmlProducto).find("clave_unidad").text()+ "]").attr("selected", true);
                //$("#clave_impuesto option[value=" + $(xmlProducto).find("clave_impuesto").text().replace("%","")+ "]").attr("selected", true);
                
                //Cálculo del importe final
                
                //importeIVA=parseInt($(xmlProducto).find("precio").text()) * parseInt($(xmlProducto).find("porcentaje_impuesto").text())/100;
                
                $("#importe_total").val(formatCurrency(parseInt($(xmlProducto).find("precio").text())* parseInt($("#cantidad").val())));
            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos del insumo: \n" + +xhr.responseText);
            }
        });
    });
    
    $("#cantidad,#importe_unitario").change(function() {
        $("#importe_total").val(formatCurrency(parseFloat($("#importe_unitario").val().replace(/,/g,"").replace(/\$/g,""))* parseFloat($("#cantidad").val().replace(/,/g,"").replace(/\$/g,""))));
    });
}