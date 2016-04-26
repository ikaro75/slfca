function restaurantex_reservacion_init() {
    $("#td_clave_mesa").parent().hide();
    $("#clave_mesa").removeClass("obligatorio");
    
    $("#clave_estatus").change(function() {
       if ($(this).val()==2) {
           $("#td_clave_mesa").parent().show();
           $("#clave_mesa").addClass("obligatorio");
       } else {
           $("#td_clave_mesa").parent().hide();
           $("#clave_mesa").removeClass("obligatorio");
       }
    });
}

function restaurantex_cuenta_init() {
    var claveCuenta=$(xml).find("clave_cuenta")[0].childNodes[0].data==""?"0":$(xml).find("clave_cuenta")[0].childNodes[0].data;
    var importePagado=0;
    var importeTotal=0;
    
    if ($(xml).find("importe_pagos").length>0)
        importePagado=$(xml).find("importe_pagos")[0].childNodes[0].data==""?"0":$(xml).find("importe_pagos")[0].childNodes[0].data.replace(/,/g,"").replace(/\$/g,"");
   
    if ($(xml).find("importe_total").length>0)
        importeTotal=$(xml).find("importe_total")[0].childNodes[0].data==""?"0":$(xml).find("importe_total")[0].childNodes[0].data.replace(/,/g,"").replace(/\$/g,"");
    
    $("#importe_subtotal").attr("disabled", "disabled");
    $("#importe_iva").attr("disabled", "disabled");
    $("#importe_total").attr("disabled", "disabled");
    $("#importe_pagos").attr("disabled", "disabled");
    $("#importe_por_pagar").attr("disabled", "disabled").val(formatCurrency(importeTotal-importePagado));
    
    $("#porcentaje_descuento").change(function () {
        porcentajeDescuento=parseFloat($(this).val());
        if (porcentajeDescuento<0 || porcentajeDescuento>100) {
            alert("Porcentaje de descuento no válido, verifique");
            $(this).val("0.0").focus();
        } else {
            importeSubtotal=parseFloat($("#importe_subtotal").val().replace(/,/g,"").replace(/\$/g,""));
            importeIVA=parseFloat($("#importe_iva").val().replace(/,/g,"").replace(/\$/g,""));
            
            if (porcentajeDescuento>0)
                importeDescuento=(importeSubtotal+importeIVA)*porcentajeDescuento/100;
            else 
                importeDescuento=0;
            
            $("#importe_descuento").val(formatCurrency(importeDescuento));
            
            porcentajePropina=parseFloat($("#porcentaje_propina").val());
            if (porcentajePropina>0)
                importePropina=(importeSubtotal+importeIVA-importeDescuento)*porcentajePropina/100;
            else 
                importePropina=0;
            
            importeTotal=importeSubtotal+importeIVA-importeDescuento+importePropina;
            importePagado=parseFloat($("#importe_pagos").val().replace(/,/g,"").replace(/\$/g,""));
            $("#importe_total").val(formatCurrency(importeSubtotal+importeIVA-importeDescuento+importePropina));
            $("#importe_por_pagar").val(formatCurrency(importeTotal-importePagado));
            
        }
     });
     
     $("#porcentaje_propina").change(function () {
        porcentajePropina=parseFloat($(this).val());
        if (porcentajePropina<0 || porcentajePropina>15) {
            alert("Porcentaje de propina no válido, verifique");
            $(this).val("0.0").focus();
        } else {
            importeSubtotal=parseFloat($("#importe_subtotal").val().replace(/,/g,"").replace(/\$/g,""));
            importeIVA=parseFloat($("#importe_iva").val().replace(/,/g,"").replace(/\$/g,""));
            
            porcentajeDescuento=parseInt($("#porcentaje_descuento").val());
            if (porcentajeDescuento>0)
                importeDescuento=(importeSubtotal+importeIVA)*porcentajeDescuento/100;
            else 
                importeDescuento=0;
            
            if (porcentajePropina>0)
                importePropina=(importeSubtotal+importeIVA-importeDescuento)*porcentajeDescuento/100;
            else 
                importePropina=0;
            
            importeTotal=importeSubtotal+importeIVA-importeDescuento+importePropina;
            importePagado=parseFloat($("#importe_pagos").val().replace(/,/g,"").replace(/\$/g,""));

            $("#importe_propina").val(formatCurrency(importePropina));
            $("#importe_total").val(formatCurrency(importeSubtotal+importeIVA-importeDescuento+importePropina));
            $("#importe_por_pagar").val(formatCurrency(importeTotal-importePagado));
            
        }
     });
  
     if ($("#btnEliminar_53_578_"+ claveCuenta).length>0) {
         $("#btnEliminar_53_578_"+ claveCuenta).parent().parent().remove();
         $("#btnMenuOpciones_53_578_"+ claveCuenta).remove();
         $("#opciones_53_578_" + claveCuenta).attr("id","pagar_cuenta").val("Pagar");
     }
     
     if ($("#btnEliminar_50_578_"+ claveCuenta).length>0) {
         $("#btnEliminar_50_578_"+ claveCuenta).parent().parent().remove();
         $("#btnMenuOpciones_50_578_"+ claveCuenta).remove();
         $("#opciones_50_578_" + claveCuenta).attr("id","pagar_cuenta").val("Pagar");
     }
     
     $("#pagar_cuenta").button().click(function() {
         
         //Se debe de hacer un update de la cuenta 
         
         //Todos los input de tipo comanda
         var porcentajeDescuento=0;
         var porcentajePropina=0;
         var claveCuentaDetalle="";
         var clavesCuentaDetalle="";
         
         if ($("#porcentaje_descuento").val()!=="" && parseFloat($("#porcentaje_descuento").val().replace(/,/g,"").replace(/\$/g,""))>0)
             porcentajeDescuento= parseFloat($("#porcentaje_descuento").val().replace(/,/g,"").replace(/\$/g,""))/100;
         
         if ($("#porcentaje_propina").val()!=="" && parseFloat($("#porcentaje_propina").val().replace(/,/g,"").replace(/\$/g,""))>0)
             porcentajePropina=parseFloat($("#porcentaje_propina").val().replace(/,/g,"").replace(/\$/g,""))/100;;
         
         var importeCuenta=0;
         var importeComandasSeleccionadas=0;
         var nRow=0;
         
         $(".cbox").each(function(){
             if (nRow>0) {
                if ($("#grid_50_579_0").length>0) {
                    importeProducto = parseFloat($("#grid_50_579_0").getCell(nRow,7).replace(/,/g,"").replace(/\$/g,"")) + parseFloat($("#grid_50_579_0").getCell(nRow,8).replace(/,/g,"").replace(/\$/g,""));
                    claveCuentaDetalle=$("#grid_50_579_0").getCell(nRow,0);
                }

                if ($("#grid_53_579_0").length>0) {
                    importeProducto = parseFloat($("#grid_53_579_0").getCell(nRow,7).replace(/,/g,"").replace(/\$/g,"")) + parseFloat($("#grid_50_579_0").getCell(nRow,8).replace(/,/g,"").replace(/\$/g,""));
                    claveCuentaDetalle=$("#grid_53_579_0").getCell(nRow,0);
                }

                if ($(this).attr("checked")=="checked" && this.id!="cb_grid_50_579_0" && this.id!="cb_grid_53_579_0") { 
                    importeComandasSeleccionadas+= (importeProducto - (importeProducto * porcentajeDescuento)) + ((importeProducto - (importeProducto * porcentajeDescuento))*porcentajePropina);
                    if (clavesCuentaDetalle!="") {
                        clavesCuentaDetalle+="," + claveCuentaDetalle + ",";
                    } else {
                        clavesCuentaDetalle = claveCuentaDetalle;
                    }
                    importeCuenta +=(importeProducto - (importeProducto * porcentajeDescuento)) + ((importeProducto - (importeProducto * porcentajeDescuento))*porcentajePropina);
                }
              }
              nRow++;
          });
          
          if (importeComandasSeleccionadas==0) {
              $("#_cache_").val(clavesCuentaDetalle + "@" + formatCurrency(importeCuenta));
          } else {
              $("#_cache_").val(clavesCuentaDetalle + "@" + formatCurrency(importeComandasSeleccionadas));
          }
          
         $("#top").form({
                    app: 53,
                    forma: 600,
                    datestamp:$(this).attr("datestamp"),
                    modo:"insert",
                    columnas:1,
                    pk: 0,
                    filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3=clave_cuenta="+claveCuenta,
                    height:"90%",
                    width:"80%",
                    originatingObject: $(this).id,
                    showRelationships:false,
                    updateControl:$("#grid_50_600_0").length>0?"grid_50_600_0":"grid_53_600_0"
                });
          
     });

}

function restaurantex_cuenta_cerrada_init() {
    var claveCuenta=$(xml).find("clave_cuenta")[0].childNodes[0].data==""?"0":$(xml).find("clave_cuenta")[0].childNodes[0].data;
    var importePagado=0;
    if ($(xml).find("importe_pagos").length>0)
        importePagado=$(xml).find("importe_pagos")[0].childNodes[0].data==""?"0":$(xml).find("importe_pagos")[0].childNodes[0].data.replace(/,/g,"").replace(/\$/g,"");
   
    var importeTotal=$(xml).find("importe_total")[0].childNodes[0].data==""?"0":$(xml).find("importe_total")[0].childNodes[0].data.replace(/,/g,"").replace(/\$/g,"");
    
    $("#importe_subtotal").attr("disabled", "disabled");
    $("#importe_iva").attr("disabled", "disabled");
    $("#importe_total").attr("disabled", "disabled");
    $("#importe_pagos").attr("disabled", "disabled");
    $("#importe_por_pagar").attr("disabled", "disabled").val(formatCurrency(importeTotal-importePagado));
    
    $("#porcentaje_descuento").change(function () {
        porcentajeDescuento=parseFloat($(this).val());
        if (porcentajeDescuento<0 || porcentajeDescuento>100) {
            alert("Porcentaje de descuento no válido, verifique");
            $(this).val("0.0").focus();
        } else {
            importeSubtotal=parseFloat($("#importe_subtotal").val().replace(/,/g,"").replace(/\$/g,""));
            importeIVA=parseFloat($("#importe_iva").val().replace(/,/g,"").replace(/\$/g,""));
            
            if (porcentajeDescuento>0)
                importeDescuento=(importeSubtotal+importeIVA)*porcentajeDescuento/100;
            else 
                importeDescuento=0;
            
            $("#importe_descuento").val(formatCurrency(importeDescuento));
            
            porcentajePropina=parseFloat($("#porcentaje_propina").val());
            if (porcentajePropina>0)
                importePropina=(importeSubtotal+importeIVA-importeDescuento)*porcentajePropina/100;
            else 
                importePropina=0;
            
            importeTotal=importeSubtotal+importeIVA-importeDescuento+importePropina;
            importePagado=parseFloat($("#importe_pagos").val().replace(/,/g,"").replace(/\$/g,""));
            $("#importe_total").val(formatCurrency(importeSubtotal+importeIVA-importeDescuento+importePropina));
            $("#importe_por_pagar").val(formatCurrency(importeTotal-importePagado));
            
        }
     });
     
     $("#porcentaje_propina").change(function () {
        porcentajePropina=parseFloat($(this).val());
        if (porcentajePropina<0 || porcentajePropina>15) {
            alert("Porcentaje de propina no válido, verifique");
            $(this).val("0.0").focus();
        } else {
            importeSubtotal=parseFloat($("#importe_subtotal").val().replace(/,/g,"").replace(/\$/g,""));
            importeIVA=parseFloat($("#importe_iva").val().replace(/,/g,"").replace(/\$/g,""));
            
            porcentajeDescuento=parseInt($("#porcentaje_descuento").val());
            if (porcentajeDescuento>0)
                importeDescuento=(importeSubtotal+importeIVA)*porcentajeDescuento/100;
            else 
                importeDescuento=0;
            
            if (porcentajePropina>0)
                importePropina=(importeSubtotal+importeIVA-importeDescuento)*porcentajeDescuento/100;
            else 
                importePropina=0;
            
            importeTotal=importeSubtotal+importeIVA-importeDescuento+importePropina;
            importePagado=parseFloat($("#importe_pagos").val().replace(/,/g,"").replace(/\$/g,""));

            $("#importe_propina").val(formatCurrency(importePropina));
            $("#importe_total").val(formatCurrency(importeSubtotal+importeIVA-importeDescuento+importePropina));
            $("#importe_por_pagar").val(formatCurrency(importeTotal-importePagado));
            
        }
     });
     
     if ($("#btnEliminar_53_628_"+ claveCuenta).length>0) {
        $("#btnEliminar_53_628_"+ claveCuenta).parent().parent().remove();
        $("#btnMenuOpciones_53_628_"+ claveCuenta).remove();
        $("#opciones_53_628_" + claveCuenta).attr("id","pagar_cuenta").val("Pagar");
    }

    if ($("#btnEliminar_50_628_"+ claveCuenta).length>0) {
        $("#btnEliminar_50_628_"+ claveCuenta).parent().parent().remove();
        $("#btnMenuOpciones_50_628_"+ claveCuenta).remove();
        $("#opciones_50_628_" + claveCuenta).attr("id","pagar_cuenta").val("Pagar");
    }

    $("#pagar_cuenta").button().click(function() {
         //Todos los input de tipo comanda
         var porcentajeDescuento=0;
         var porcentajePropina=0;
         
         if ($("#porcentaje_descuento").val()!=="" && parseFloat($("#porcentaje_descuento").val().replace(/,/g,"").replace(/\$/g,""))>0)
             porcentajeDescuento= parseFloat($("#porcentaje_descuento").val().replace(/,/g,"").replace(/\$/g,""))/100;
         
         if ($("#porcentaje_propina").val()!=="" && parseFloat($("#porcentaje_propina").val().replace(/,/g,"").replace(/\$/g,""))>0)
             porcentajePropina=parseFloat($("#porcentaje_propina").val().replace(/,/g,"").replace(/\$/g,""))/100;;
         
         var importeCuenta=0;
         var importeComandasSeleccionadas=0;
         var claveCuentaDetalle="";
         var clavesCuentaDetalle = "";
         var nRow=0;
         $(".cbox").each(function(){
             if (nRow>0) {
                importeProducto = parseFloat($("#grid_53_630_0").getCell(nRow,7).replace(/,/g,"").replace(/\$/g,"")) + parseFloat($("#grid_53_630_0").getCell(nRow,8).replace(/,/g,"").replace(/\$/g,""));
                claveCuentaDetalle=$("#grid_53_579_0").getCell(nRow,0);
                if ($(this).attr("checked")=="checked" && this.id!="cb_grid_50_579_0" && this.id!="cb_grid_53_579_0") { 
                    importeComandasSeleccionadas+= (importeProducto - (importeProducto * porcentajeDescuento)) + ((importeProducto - (importeProducto * porcentajeDescuento))*porcentajePropina);
                    if (clavesCuentaDetalle!="") {
                        clavesCuentaDetalle+="," + claveCuentaDetalle;
                    } else {
                        clavesCuentaDetalle = claveCuentaDetalle;
                    }
                    importeCuenta +=(importeProducto - (importeProducto * porcentajeDescuento)) + ((importeProducto - (importeProducto * porcentajeDescuento))*porcentajePropina);;
                }
             }   
             nRow++;
          });
          
          if (importeComandasSeleccionadas==0) {
              $("#_cache_").val("@" + formatCurrency(importeCuenta));
          } else {
              $("#_cache_").val(clavesCuentaDetalle + "@" + formatCurrency(importeComandasSeleccionadas));
          }
          
         $("#top").form({
                    app: 53,
                    forma: 600,
                    datestamp:$(this).attr("datestamp"),
                    modo:"insert",
                    columnas:1,
                    pk: 0,
                    filtroForaneo:"2=clave_aplicacion=53&3=clave_cuenta="+claveCuenta,
                    height:"90%",
                    width:"80%",
                    originatingObject: $(this).id,
                    showRelationships:false,
                    updateControl:$("#grid_53_600_0").length>0?"grid_53_600_0":"grid_50_600_0"
                });
          
     });

}

function restaurantex_cuenta_detalle_init() {
    var claveCuentaDetalle=$(xml).find("clave_cuenta_detalle")[0].childNodes[0].data==""?"0":$(xml).find("clave_cuenta_detalle")[0].childNodes[0].data;
    //Llena el select  de los asientos de acuerdo al número de personas
    var asiento = $(xml).find("asiento")[0].childNodes[0].data
    
    $('#asiento').append('<option value="0">Para compartir</option>');
    
    for (i=1; i<=parseInt($(xml).find("numero_personas")[0].childNodes[0].data);i++) {
        $('#asiento').append('<option value="' + i + '" >' + i + '</option>');
    }
    
    $("#asiento option[value=" + asiento +"]").attr("selected", "selected");    
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
                $("#clave_impuesto option[value="+ $(xmlProducto).find("clave_impuesto_predeterminado")[0].firstChild.data+"]").attr("selected",true);
                importeUnitario=parseFloat($(xmlProducto).find("precio").text());
                porcentajeIVA=parseFloat($("#clave_impuesto option:selected").text().split("%")[0]);
                cantidad=parseFloat($("#cantidad").val());
                
                if (porcentajeIVA!=0)  {
                    porcentajeIVA = porcentajeIVA/100;
                }
                
                importeIVA=importeUnitario*cantidad*porcentajeIVA;
                $("#importe_impuesto").val(formatCurrency(importeIVA));
                
                if ($("#form_29_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
                    $("#form_29_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));
                } 
                
                if ($("#form_43_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
                    $("#form_43_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));
                }
                
                if ($("#form_50_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
                    $("#form_50_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));
                }
                
                if ($("#form_53_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
                    $("#form_53_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));
                }
                
            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos del insumo: \n" + +xhr.responseText);
            }
        });        
    });
    
    $("#cantidad, #clave_impuesto, #importe_unitario").change(function(){
        importeUnitario=parseFloat($("#importe_unitario").val().replace(/,/g,"").replace(/\$/g,""));
        
        if ($("#form_29_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
            porcentajeIVA=parseFloat($("#form_29_579_" + claveCuentaDetalle + " #clave_impuesto option:selected").text().split("%")[0]);
        } 
       
        if ($("#form_49_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
            porcentajeIVA=parseFloat($("#form_49_579_" + claveCuentaDetalle + " #clave_impuesto option:selected").text().split("%")[0]);
        } 
        
        if ($("#form_50_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
            porcentajeIVA=parseFloat($("#form_50_579_" + claveCuentaDetalle + " #clave_impuesto option:selected").text().split("%")[0]);
        } 
        
        if ($("#form_53_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
            porcentajeIVA=parseFloat($("#form_53_579_" + claveCuentaDetalle + " #clave_impuesto option:selected").text().split("%")[0]);
        } 
        cantidad=parseFloat($("#cantidad").val().replace(/,/g,"").replace(/\$/g,""));

        if (porcentajeIVA!=0)  {
            porcentajeIVA = porcentajeIVA/100;
        }

        importeIVA=importeUnitario*cantidad*porcentajeIVA;
        
        if  ($("#form_29_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) { 
            $("#form_29_579_" + claveCuentaDetalle + " #importe_impuesto").val(formatCurrency(importeIVA));
            $("#form_29_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));                    
        }

        if ($("#form_49_579_" + claveCuentaDetalle + " #importe_impuesto").length>0) { 
            $("#form_49_579_" + claveCuentaDetalle + " #importe_impuesto").val(formatCurrency(importeIVA));
            $("#form_49_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));        
        }

        if ($("#form_50_579_" + claveCuentaDetalle + " #importe_impuesto").length>0) {
            $("#form_50_579_" + claveCuentaDetalle + " #importe_impuesto").val(formatCurrency(importeIVA));
            $("#form_50_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));        
        }
        
        if ($("#form_53_579_" + claveCuentaDetalle + " #importe_impuesto").length>0) {
            $("#form_53_579_" + claveCuentaDetalle + " #importe_impuesto").val(formatCurrency(importeIVA));
            $("#form_53_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));        
        }        
    })
    
}

function restaurantex_cuenta_cerrada_detalle_init() {
    var claveCuentaDetalle=$(xml).find("clave_cuenta_detalle")[0].childNodes[0].data==""?"0":$(xml).find("clave_cuenta_detalle")[0].childNodes[0].data;
    //Llena el select  de los asientos de acuerdo al número de personas
    var asiento = $(xml).find("asiento")[0].childNodes[0].data
    
    $('#asiento').append('<option value="0">Para compartir</option>');
    
    for (i=1; i<=parseInt($(xml).find("numero_personas")[0].childNodes[0].data);i++) {
        $('#asiento').append('<option value="' + i + '" >' + i + '</option>');
    }
    
    $("#asiento option[value=" + asiento +"]").attr("selected", "selected");    
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
                $("#clave_impuesto option[value="+ $(xmlProducto).find("clave_impuesto_predeterminado")[0].firstChild.data+"]").attr("selected",true);
                importeUnitario=parseFloat($(xmlProducto).find("precio").text());
                porcentajeIVA=parseFloat($("#clave_impuesto option:selected").text().split("%")[0]);
                cantidad=parseFloat($("#cantidad").val());
                
                if (porcentajeIVA!=0)  {
                    porcentajeIVA = porcentajeIVA/100;
                }
                
                importeIVA=importeUnitario*cantidad*porcentajeIVA;
                $("#importe_impuesto").val(formatCurrency(importeIVA));
                
                if ($("#form_50_628_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
                    $("#form_50_628_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));
                }
                
                if ($("#form_53_628_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
                    $("#form_53_628_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));
                }
                
            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos del insumo: \n" + +xhr.responseText);
            }
        });        
    });
    
    $("#cantidad, #clave_impuesto, #importe_unitario").change(function(){
        importeUnitario=parseFloat($("#importe_unitario").val().replace(/,/g,"").replace(/\$/g,""));
        
        if ($("#form_29_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
            porcentajeIVA=parseFloat($("#form_29_579_" + claveCuentaDetalle + " #clave_impuesto option:selected").text().split("%")[0]);
        } 
       
        if ($("#form_49_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
            porcentajeIVA=parseFloat($("#form_49_579_" + claveCuentaDetalle + " #clave_impuesto option:selected").text().split("%")[0]);
        } 
        
        if ($("#form_50_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
            porcentajeIVA=parseFloat($("#form_50_579_" + claveCuentaDetalle + " #clave_impuesto option:selected").text().split("%")[0]);
        } 
        
        if ($("#form_53_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) {
            porcentajeIVA=parseFloat($("#form_53_579_" + claveCuentaDetalle + " #clave_impuesto option:selected").text().split("%")[0]);
        } 
        cantidad=parseFloat($("#cantidad").val().replace(/,/g,"").replace(/\$/g,""));

        if (porcentajeIVA!=0)  {
            porcentajeIVA = porcentajeIVA/100;
        }

        importeIVA=importeUnitario*cantidad*porcentajeIVA;
        
        if  ($("#form_29_579_" + claveCuentaDetalle + " #clave_impuesto").length>0) { 
            $("#form_29_579_" + claveCuentaDetalle + " #importe_impuesto").val(formatCurrency(importeIVA));
            $("#form_29_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));                    
        }

        if ($("#form_49_579_" + claveCuentaDetalle + " #importe_impuesto").length>0) { 
            $("#form_49_579_" + claveCuentaDetalle + " #importe_impuesto").val(formatCurrency(importeIVA));
            $("#form_49_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));        
        }

        if ($("#form_50_579_" + claveCuentaDetalle + " #importe_impuesto").length>0) {
            $("#form_50_579_" + claveCuentaDetalle + " #importe_impuesto").val(formatCurrency(importeIVA));
            $("#form_50_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));        
        }
        
        if ($("#form_53_579_" + claveCuentaDetalle + " #importe_impuesto").length>0) {
            $("#form_53_579_" + claveCuentaDetalle + " #importe_impuesto").val(formatCurrency(importeIVA));
            $("#form_53_579_" + claveCuentaDetalle + " #importe_total").val(formatCurrency((importeUnitario*cantidad) + importeIVA));        
        }        
    });
    
}


// Actualiza los datos económicos de la cuenta
function restaurantex_cuenta_detalle_ongridComplete() {
    var claveCuenta=document.getElementById("$pk").value;
    var importePagado=0;
    $.ajax({
            url: "control?$cmd=plain&$cf=578&$pk=" + claveCuenta + "&$ta=select&$w=clave_cuenta="+claveCuenta,
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlCuenta = new ActiveXObject("Microsoft.XMLDOM");
                    xmlCuenta.async = false;
                    xmlCuenta.validateOnParse = "true";
                    xmlCuenta.loadXML(data); 

                    if (xmlCuenta.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlCuenta.parseError.errorCode + "\nParse reason:" + xmlCuenta.parseError.reason + "\nLinea:" + xmlCuenta.parseError.line);
                    }
                }
                else {
                    xmlCuenta = data;
                }
                
                $("#importe_subtotal").val(formatCurrency($(xmlCuenta).find("importe_subtotal").text()));
                $("#importe_iva").val(formatCurrency($(xmlCuenta).find("importe_iva").text()));
                
                //Se recalcula el descuento
                porcentajeDescuento=parseFloat($("#porcentaje_descuento").val());
                
                if ($(xml).find("importe_pagos").length>0)
                    importePagado= $("#importe_pagos").val().replace(/,/g,"").replace(/\$/g,"");
                importeSubtotal=parseFloat($("#importe_subtotal").val().replace(/,/g,"").replace(/\$/g,""));
                importeIVA=parseFloat($("#importe_iva").val().replace(/,/g,"").replace(/\$/g,""));
            
                if (porcentajeDescuento>0)
                    importeDescuento=(importeSubtotal+importeIVA)*porcentajeDescuento/100;
                else 
                    importeDescuento=0;
                
                porcentajePropina=parseFloat($("#porcentaje_propina").val());
                
                if (porcentajePropina>0)
                    importePropina=(importeSubtotal+importeIVA-importeDescuento)*porcentajePropina/100;
                else 
                    importePropina=0;
            
                $("#importe_descuento").val(formatCurrency(importeDescuento));
                importeTotal=importeSubtotal+importeIVA-importeDescuento+importePropina;
                $("#importe_total").val(formatCurrency(importeTotal));
                $("#importe_por_pagar").val(formatCurrency(importeTotal-importePagado));

            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos de la cuenta: \n" + +xhr.responseText);
            }
        });
}

// Actualiza los datos económicos de la cuenta
function restaurantex_cuenta_cerrada_detalle_ongridComplete() {
    var claveCuenta=document.getElementById("$pk").value;
    var importePagado=0;
    $.ajax({
            url: "control?$cmd=plain&$cf=628&$pk=" + claveCuenta + "&$ta=select&$w=clave_cuenta="+claveCuenta,
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlCuenta = new ActiveXObject("Microsoft.XMLDOM");
                    xmlCuenta.async = false;
                    xmlCuenta.validateOnParse = "true";
                    xmlCuenta.loadXML(data); 

                    if (xmlCuenta.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlCuenta.parseError.errorCode + "\nParse reason:" + xmlCuenta.parseError.reason + "\nLinea:" + xmlCuenta.parseError.line);
                    }
                }
                else {
                    xmlCuenta = data;
                }
                
                $("#importe_subtotal").val(formatCurrency($(xmlCuenta).find("importe_subtotal").text()));
                $("#importe_iva").val(formatCurrency($(xmlCuenta).find("importe_iva").text()));
                
                //Se recalcula el descuento
                porcentajeDescuento=parseFloat($("#porcentaje_descuento").val());
                
                if ($(xml).find("importe_pagos").length>0)
                    importePagado= $("#importe_pagos").val().replace(/,/g,"").replace(/\$/g,"");
                importeSubtotal=parseFloat($("#importe_subtotal").val().replace(/,/g,"").replace(/\$/g,""));
                importeIVA=parseFloat($("#importe_iva").val().replace(/,/g,"").replace(/\$/g,""));
            
                if (porcentajeDescuento>0)
                    importeDescuento=(importeSubtotal+importeIVA)*porcentajeDescuento/100;
                else 
                    importeDescuento=0;
                
                porcentajePropina=parseFloat($("#porcentaje_propina").val());
                
                if (porcentajePropina>0)
                    importePropina=(importeSubtotal+importeIVA-importeDescuento)*porcentajePropina/100;
                else 
                    importePropina=0;
            
                $("#importe_descuento").val(formatCurrency(importeDescuento));
                importeTotal=importeSubtotal+importeIVA-importeDescuento+importePropina;
                $("#importe_total").val(formatCurrency(importeTotal));
                $("#importe_por_pagar").val(formatCurrency(importeTotal-importePagado));

            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos de la cuenta: \n" + +xhr.responseText);
            }
        });
}

function restaurantex_pago_init() {
    if ($(xml).find("clave_forma_pago")[0].firstChild.data==1) {
        $("#td_referencia_documento").parent().hide();
        $("#td_clave_banco_origen").parent().hide();
        $("#td_cuenta_origen").parent().hide();
        $("#td_cuenta_origen").parent().hide();
        $("#td_clave_banco_destino").parent().hide();    
    } else {
        $("#td_referencia_documento").parent().show();
        $("#td_clave_banco_origen").parent().show();
        $("#td_cuenta_origen").parent().show();
        $("#td_cuenta_origen").parent().show();
        $("#td_clave_banco_destino").parent().show();                  
    }
    
    $("#clave_forma_pago").change(function(){
        if ($(this).val()==1) {
            $("#td_referencia_documento").parent().hide();
            $("#td_clave_banco_origen").parent().hide();
            $("#td_cuenta_origen").parent().hide();
            $("#td_cuenta_origen").parent().hide();
            $("#td_clave_banco_destino").parent().hide();
        } else {
            $("#td_referencia_documento").parent().show();
            $("#td_clave_banco_origen").parent().show();
            $("#td_cuenta_origen").parent().show();
            $("#td_cuenta_origen").parent().show();
            $("#td_clave_banco_destino").parent().show();            
        }
    });
    
    if ($("#_cache_").val()!="") {
        $("#importe_pago").val($("#_cache_").val().split("@")[1]);
        $("#restaurantex_clave_cuentas").val($("#_cache_").val().split("@")[0]);
    } else {
        $("#importe_pago").val($("#importe_total").val());
    }
    
}

function restaurantex_pago_ongridComplete() {
    

}

function restaurantex_sala_ongridComplete() {
    $("a.bulk_tables_link").click(function() {
        clave_sala = (this).id.split("-")[1];
        $("#_cache_").val(clave_sala);
        $("body").form({
            app: 29,
            forma: 633,
            modo: "insert",
            pk: 0,
            width: "600"
        });
    });
}

function restaurantex_define_mesas_init() {
    $("#clave_sala").val($("#_cache_").val());
    $("#_cache_").val("");
    clave_sala = $("#clave_sala").val();

    $("#btnGuardar_29_633_0").parent().html("<input type='button' style='float: right;' class='formButton' id='btnInsertaMesas' value='Inserta mesas'>");
    $("#btnInsertaMesas").button().click(function() {
        //  mandar a llamar con la función de jquery ajax el jsp

        $.ajax(
                {
                    url: "inserta_mesas.jsp?$clave_sala=" + $("#clave_sala").val() + "&$numero_mesas=" + escape($("#numero").val()),
                    dataType: ($.browser.msie) ? "text" : "xml",
                    success: function(data) {
                        if (typeof data == "string") {
                            xmlResultImport = new ActiveXObject("Microsoft.XMLDOM");
                            xmlResultImport.async = false;
                            xmlResultImport.validateOnParse = "true";
                            xmlResultImport.loadXML(data);
                            if (xmlResultImport.parseError.errorCode > 0) {
                                alert("Error de compilación xml:" + xmlResultImport.parseError.errorCode + "\nParse reason:" + xmlResultImport.parseError.reason + "\nLinea:" + xmlResultImport.parseError.line);
                            }
                        }
                        else {
                            xmlResultImport = data;
                        }

                        if ($(xmlResultImport).find("error").length > 0) {
                            alert($(xmlResultImport).find("error").text());
                            $("#tdEstatus_29_633_0").html($(xmlResultImport).find("error").text());
                        } else {
                            warnings = "";
                            $(xmlResultImport).find("warning").each(function() {
                                warnings += $(this).text() + "\n";
                            }) 
                            
                            alert("Inserción de mesas finalizada.\n"+warnings);
                            $("#dlgModal_29_633_0").remove();
                        }
                    },
                    error: function(xhr, err) {
                    }
                });
    });
}
