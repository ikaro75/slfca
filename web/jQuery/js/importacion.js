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
 \u00BF -> ¿
 * and open the template in the editor.
 */
function fide_definicion_importacion_detalle_init() {
    claveTipoArchivo = $(xml).find("clave_tipo_archivo")[0].firstChild.data;

    if (claveTipoArchivo == "1") {
        $("#td_caracter_separador").parent().show();
        $("#td_caracter_nueva_linea").parent().show();
    } else {
        $("#td_caracter_separador").parent().hide();
        $("#td_caracter_nueva_linea").parent().hide();
    }
    
    if (claveTipoArchivo == "4") {
       $("#td_clave_forma").parent().show();
    } else {
       $("#td_clave_forma").parent().hide();
    }
    
    $("#clave_tipo_archivo").change(function () {
        if ($(this).val() == "1") {
            $("#td_caracter_separador").parent().show();
            $("#td_caracter_nueva_linea").parent().show();
        } else {
            $("#td_caracter_separador").parent().hide();
            $("#td_caracter_nueva_linea").parent().hide();
        }
        
        if ($(this).val() == "4") {
            $("#td_clave_forma").parent().show();
        } else {
           $("#td_clave_forma").parent().hide();
        }
    });
    /*claveAplicacion = $(xml).find("configuracion_forma").find("clave_aplicacion")[0].firstChild.data;
     claveForma = $(xml).find("configuracion_forma").find("clave_forma")[0].firstChild.data;
     claveRegistro = $(xml).find("configuracion_forma").find("pk")[0].firstChild.data;
     tabla = $(xml).find("registro").find("tabla")[0].firstChild.data;
     campo = $(xml).find("registro").find("campo")[0].firstChild.data;
     
     $("#form_" + claveAplicacion + "_" + claveForma + "_0 #tabla").change(function() {
     if ($("#form_" + claveAplicacion + "_" + claveForma + "_0 #tabla").val() != "")
     setXMLInSelect4("#form_" + claveAplicacion + "_" + claveForma + "_0 #campo", -2, "update", $(this).val());
     });
     
     if (campo!="") {
     setXMLInSelect4("#form_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro + " #campo", -2, "update", tabla);
     $("#form_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro +" #campo option[value=" + campo + "]").attr("selected", true);
     }*/
}


function importacion_init() {
    var claveAplicacion = $(xml).find("configuracion_forma").find("clave_aplicacion")[0].childNodes[0].data;
    var claveForma = $(xml).find("configuracion_forma").find("clave_forma")[0].childNodes[0].data;
    var claveRegistro = $(xml).find("configuracion_forma").find("pk")[0].childNodes[0].data;
    var consulta=$(xml).find("consulta")[0].childNodes[0].data;
    
    var claveImportacion = $(xml).find("clave_importacion")[0].firstChild.data;
    var claveDefinicion = $(xml).find("clave_definicion")[0].firstChild.data; 
    var claveImportacion=(claveImportacion==""?"0":claveImportacion); 
    
    $("#clave_punto").removeClass("obligatorio");
    $("#td_clave_punto").parent().hide();
    $("#td_id_control").parent().hide();
    $("#td_nombre_almacen").parent().hide();
    $("#id_control").prop("disabled",true);
    $("#nombre_almacen").prop("disabled","disabled");
    $("#td_clave_tipo_ordenamiento").parent().hide();
    
    if (claveDefinicion==5) /* Nuevos beneficiarios del padrón extendido desde XLS */{    
        $("#td_archivo").parent().show();
        $("#archivo").addClass("obligatorio");
        $("#lnkFormTab_144_735").hide(); //.tabs( "option", "hide" ); 
        $("#lnkFormTab_144_736").hide();
        $("#lnkFormTab_144_740").hide();        
        $("#formGrid_152_737").hide();
        $("#td_id_control").parent().hide();
        $("#id_control").removeClass("obligatorio");
        $("#id_control").prop("disabled",true);
        $("#td_nombre_almacen").parent().hide();
        $("#td_clave_tipo_ordenamiento").parent().hide();
    } else if (claveDefinicion==6) /* Avances del padrón entregado por DICONSA desde TXT*/{
        $("#td_archivo").parent().show();
        $("#archivo").addClass("obligatorio");
        $("#lnkFormTab_144_714" ).hide();  //.tabs( "option", "hide" ); 
        $("#lnkFormTab_144_735").hide(); 
        $("#lnkFormTab_144_740").hide();
        $("#formGrid_152_737").hide();
        $("#td_id_control").parent().hide();
        $("#id_control").removeClass("obligatorio");
        $("#id_control").prop("disabled",true);
        $("#td_nombre_almacen").parent().hide();        
        $("#td_clave_tipo_ordenamiento").parent().hide();
        $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).removeAttr("disabled").button( "refresh" );
    } else if (claveDefinicion==7) /* Lecturas de medidores MRV desde CSV */ {
        $("#td_archivo").parent().show();
        $("#archivo").addClass("obligatorio");
        $("#lnkFormTab_144_714").hide(); 
        $("#lnkFormTab_144_736").hide();
        $("#lnkFormTab_144_740").hide();
        $("#formGrid_152_737").hide();
        $("#td_id_control").parent().hide();
        $("#id_control").prop("disabled",true);
        $("#id_control").removeClass("obligatorio");
        $("#td_nombre_almacen").parent().hide();
        $("#td_clave_tipo_ordenamiento").parent().hide();
        $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).removeAttr("disabled").button( "refresh" );
    } else if (claveDefinicion==8) /* nuevos beneficiarios de padrón potencial */{
        $("#td_archivo").parent().hide();
        $("#archivo").removeClass("obligatorio");
        $("#lnkFormTab_144_714").hide(); 
        $("#lnkFormTab_144_735").hide()
        $("#lnkFormTab_144_736").hide();
        $("#lnkFormTab_144_740").show();
        $("#td_id_control").parent().show();
        $("#id_control").prop("disabled", false);
        $("#td_nombre_almacen").parent().show();
        $("#id_control").addClass("obligatorio");
        $("#td_clave_tipo_ordenamiento").parent().show();
        $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled").button( "refresh" );
        
        //Se inyecta el grid del padrón de cfe
        if ($("#formGrid_152_737").length==0) {
            $("<div style='display: inline-block;' id='formGrid_152_737" +
            "' app='152' form='737' titulo='Seleccione el criterio para filtrar el padr&oacute;n elegible' leyendas='' align='center'>" +
            "<br /><br />Cargando informaci&oacute;n... <br /> <br />" +
            "<img src='img/loading.gif' />" +
            "</div>").insertAfter("#form_144_712_" + claveImportacion);
         
            setTimeout(function(){ 
                $("#formGrid_152_737").appgrid(
                   {app: "152",
                    entidad: "737",
                    pk: "0",
                    editingApp: "1",
                    wsParameters: encodeURIComponent(consulta),
                    titulo: "Seleccione el criterio para filtrar el padr&oacute;n elegible",
                    height: "350",
                    requeriesFilter:0,
                    openKardex: false,
                    originatingObject: "",
                    showFilterLink: true,
                    insertInDesktopEnabled: "0"})
                .removeClass('queued_grids')
                .addClass('gridForeignContainer');
                },2000);
        } else {
            $("#formGrid_152_737").show();
        }
    }
    
    $("#clave_definicion").change(function () {
        if ($(this).val()==5) /* Nuevos beneficiarios del padrón extendido desde XLS */{    
            $("#td_archivo").parent().show();
            $("#archivo").addClass("obligatorio");
            $("#lnkFormTab_144_735").hide(); //.tabs( "option", "hide" ); 
            $("#lnkFormTab_144_736").hide();
            $("#lnkFormTab_144_740").hide();
            $("#formGrid_152_737").hide();
            $("#td_id_control").parent().hide();
            $("#id_control").prop("disabled",true);
            $("#id_control").removeClass("obligatorio");
            $("#td_nombre_almacen").parent().hide();
            $("#td_clave_tipo_ordenamiento").parent().hide();
            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).removeAttr("disabled").button( "refresh" );
        } else if ($(this).val()==6) /* Avances del padrón entregado por DICONSA desde TXT*/{
            $("#td_archivo").parent().show();
            $("#archivo").addClass("obligatorio");
            $("#lnkFormTab_144_714" ).hide();  //.tabs( "option", "hide" ); 
            $("#lnkFormTab_144_735").hide(); 
            $("#formGrid_152_737").hide();
            $("#lnkFormTab_144_740").hide();
            $("#td_id_control").parent().hide();
            $("#id_control").prop("disabled",true);
            $("#id_control").removeClass("obligatorio");
            $("#td_nombre_almacen").parent().hide();
            $("#td_clave_tipo_ordenamiento").parent().hide();
            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).removeAttr("disabled").button( "refresh" );
        } else if ($(this).val()==7) /* Lecturas de medidores MRV desde CSV */ {
            $("#td_archivo").parent().show();
            $("#archivo").addClass("obligatorio");
            $("#lnkFormTab_144_714").hide(); 
            $("#lnkFormTab_144_736").hide();
            $("#formGrid_152_737").hide();
            $("#lnkFormTab_144_740").hide();
            $("#td_id_control").parent().hide();
            $("#id_control").removeClass("obligatorio");
            $("#id_control").prop("disabled",true);
            $("#td_nombre_almacen").parent().hide();          
            $("#td_clave_tipo_ordenamiento").parent().hide();
            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).removeAttr("disabled").button( "refresh" );
        } else if ($(this).val()==8) /* nuevos beneficiarios de padrón potencial */{
            $("#td_archivo").parent().hide();
            $("#archivo").removeClass("obligatorio");
            $("#lnkFormTab_144_714").hide(); 
            $("#lnkFormTab_144_735").hide()
            $("#lnkFormTab_144_736").hide();
            $("#lnkFormTab_144_740").show();
            $("#id_control").prop("disabled",false);
            $("#td_id_control").parent().show();
            $("#td_nombre_almacen").parent().show();
            $("#id_control").addClass("obligatorio");
            $("#td_clave_tipo_ordenamiento").parent().show();
            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled").button( "refresh" );
            
            //Se inyecta el grid del padrón de cfe
            if ($("#formGrid_152_737").length==0) {
            $("<div style='display: inline-block;' id='formGrid_152_737" +
                "' app='152' form='737' titulo='Seleccione el criterio para filtrar el padr&oacute;n elegible' leyendas='' align='center' >" +
                "<br /><br />Cargando informaci&oacute;n... <br /> <br />" +
                "<img src='img/loading.gif' />" +
                "</div>").insertAfter($("#form_144_712_" + claveImportacion));

             $("#formGrid_152_737").appgrid(
                {app: "152",
                    entidad: "737",
                    pk: "0",
                    editingApp: "1",
                    wsParameters: "",
                    titulo: "Seleccione el criterio para filtrar el padr&oacute;n elegible",
                    height: "350",
                    requeriesFilter:0,
                    openKardex: false,
                    originatingObject: "",
                    showFilterLink: true,
                    insertInDesktopEnabled: "0"})
                .removeClass('queued_grids')
                .addClass('gridForeignContainer');
            } else {
                $("#formGrid_152_737").show();
            }
        }        
    });
    
    $("#id_control").unbind("blur").blur(function () {
        if ($.fn.form.options.modo == "lookup" || $(this).val() == "")
            return;

        if (!check_number(this)) {
            alert("El ID Tienda debe contener solo n\u00fameros, verifique");
            $(this).val("");
            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled");
            return;
        }

        if ($(this).val().length > 10) {
            alert("El ID Tienda consultado no es v\u00e1lido, favor de verificar");
            $(this).val("");
            $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled");
            return;
        }

        $("#_status_").val("Validando tienda");
        $("#divwait")
                .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Validando clave de tienda...</p>")
                .attr('title', 'Espere un momento por favor')
                .dialog({
                    height: 140,
                    modal: true,
                    autoOpen: true,
                    closeOnEscape: false
                });

        $("#divwait").parent().parent().css("zIndex", 9999);

        $.ajax({
            url: "control?$cmd=plain&$cf=738&$ta=select&$w=id_control=" + $(this).val(),
            dataType: ($.browser.msie) ? "text" : "xml",
            type: "POST",
            success: function (data) {
                if (typeof data == "string") {
                    xmlIDTienda = new ActiveXObject("Microsoft.XMLDOM");
                    xmlIDTienda.async = false;
                    xmlIDTienda.validateOnParse = "true";
                    xmlIDTienda.loadXML(data);

                    if (xmlIDTienda.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlIDTienda.parseError.errorCode + "\nParse reason:" + xmlIDTienda.parseError.reason + "\nLinea:" + xmlIDTienda.parseError.line);
                    }
                }
                else {
                    xmlIDTienda = data;
                }

                $("#_status_").val("");
                //Comprueba que no venga con errores
                if ($(xmlIDTienda).find("error").length > 0) {
                    $("#divwait").dialog("close");

                    $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled");

                    var mensaje = $(xmlIDTienda).find("error").text().split(",");
                    alert(mensaje[0]);

                } else {
                    $("#divwait").dialog("close");
                    if ($(xmlIDTienda).find("clave_punto")[0].childNodes[0].data == "") {
                        alert("No se encontr\u00f3 la tienda especificada, verifique");
                        $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled").button( "refresh" );
                    } else {
                        $("#nombre_almacen").val($(xmlIDTienda).find("punto_entrega")[0].childNodes[0].data);
                        $("#clave_punto").val($(xmlIDTienda).find("clave_punto")[0].childNodes[0].data);
                        $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).removeAttr("disabled").button( "refresh" );
                    }

                }

            },
            error: function (xhr, err) {
                alert("Error al buscar RPU: \n" + +xhr.responseText);
            }
        });
    });
    
    $("#clave_estatus").change(function () {
        if ($(this).val() == "1") {
            
            $("#divwait")
                .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Eliminaci\u00f3n de registros importados en progreso...</p>")
                .attr('title', 'Espere un momento por favor')
                .dialog({
                    height: 140,
                    modal: true,
                    autoOpen: true,
                    closeOnEscape: false
                });
            if (confirm("Para volver a cargar los registros se eliminar\u00e1n los anteriores, \u00BFEst\u00e1 seguro que desea continuar?")) {
                $.ajax({
                    url: "control?$cmd=register&$ta=delete&$cf=714&$w=clave_importacion=" + claveImportacion,
                    dataType: ($.browser.msie) ? "text" : "xml",
                    success: function (data) {
                        if (typeof data == "string") {
                            xmlEliminacionDeDetalle = new ActiveXObject("Microsoft.XMLDOM");
                            xmlEliminacionDeDetalle.async = false;
                            xmlEliminacionDeDetalle.validateOnParse = "true";
                            xmlEliminacionDeDetalle.loadXML(data);

                            if (xmlEliminacionDeDetalle.parseError.errorCode > 0) {
                                alert("Error de compilaci\u00f3n xml:" + xmlImport.parseError.errorCode + "\nParse reason:" + xmlEliminacionDeDetalle.parseError.reason + "\nLinea:" + xmlImport.parseError.line);
                            }
                        }
                        else {
                            xmlEliminacionDeDetalle = data;
                        }

                        error = $(xmlEliminacionDeDetalle).find("error").text();

                        if (error == "" || error == undefined) {
                            alert("Se eliminaron los registros de la importaci\u00f3n exitosamente");
                            $("#grid_144_714_0").jqGrid().trigger("reloadGrid");
                        } else {
                            alert(error);
                        }
                        $("#divwait").dialog("close");
                    },
                    error: function (xhr, err) {
                        alert("Error al recuperar los datos de proceso: \n" + +xhr.responseText);
                    }
                });
            } 
        }
    })

    $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).click(function(){ 
        $("#id_control").prop("disabled",true);   
    });
    
    $("#_sc_").val(claveAplicacion+ "_" + claveForma + "_" + claveRegistro);
}

function importacion_grid_init() {
    $(".progressbar_importa").each(function () {

        if ($(this).attr("value") == undefined) {
            $(this).progressbar(
                    {"value": false});
        } else {
            $(this).progressbar(
                    {"value": parseInt($(this).attr("value")),
                        "max": parseInt($(this).attr("max")),
                        change: function () {
                            $("#progresslabel_" + this.id.split("_")[1]).text($(this).progressbar("value") + " registros procesados");
                        }}
            );
        }
    });

    $(".export_link").click(function () {
        accion=this.id.split("|")[0];
        if (accion=="importa") {
            accion= "Importaci\u00f3n";
            claveEstatus=4;
        } else  if (accion=="valida") {
            accion="Validaci\u00f3n";
            claveEstatus=2;
        } else  if (accion=="sicom") {
            accion="Consulta";
            claveEstatus=5;
        }
        
        //Manda post
        $("#divwait")
                .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;"+ accion + " en progreso...</p>")
                .attr('title', 'Espere un momento por favor')
                .dialog({
                    height: 140,
                    modal: true,
                    autoOpen: true,
                    closeOnEscape: false
                });
                
        
        claveImportacion = this.id.split("|")[1];
        claveDefinicion = this.id.split("|")[2];
        archivo = this.id.split("|")[3];
        consulta = this.id.split("|")[4];
        clavePunto = this.id.split("|")[5];
        claveTipoOrdenamiento = this.id.split("|")[6];
        
        $.ajax({
            url: "control?$cmd=register&$ta=update&$cf=712&$pk=" + claveImportacion + "&clave_definicion=" + claveDefinicion + "&archivo=" + decodeURIComponent(archivo) + "&clave_estatus=" + claveEstatus + "&consulta=" + encodeURIComponent(consulta) + "&clave_punto=" + clavePunto + "&clave_tipo_ordenamiento=" + claveTipoOrdenamiento,
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function (data) {
                if (typeof data == "string") {
                    xmlImport = new ActiveXObject("Microsoft.XMLDOM");
                    xmlImport.async = false;
                    xmlImport.validateOnParse = "true";
                    xmlImport.loadXML(data);

                    if (xmlImport.parseError.errorCode > 0) {
                        alert("Error de compilaci\u00f3n xml:" + xmlImport.parseError.errorCode + "\nParse reason:" + xmlImport.parseError.reason + "\nLinea:" + xmlImport.parseError.line);
                    }
                }
                else {
                    xmlImport = data;
                }

                error = $(xmlImport).find("error").text();

                if (error == "" || error == undefined) {
                    //Hace el conteo 
                    $.ajax({
                        url: "control?$cmd=plain&$cf=714&$ta=foreign&$w=clave_importacion=" + claveImportacion,
                        dataType: ($.browser.msie) ? "text" : "xml",
                        success: function (data) {
                            if (typeof data == "string") {
                                xmlResultadoImport = new ActiveXObject("Microsoft.XMLDOM");
                                xmlResultadoImport.async = false;
                                xmlImportTimer.validateOnParse = "true";
                                xmlResultadoImport.loadXML(data);

                                if (xmlResultadoImport.parseError.errorCode > 0) {
                                    alert("Error de compilación xml:" + xmlResultadoImport.parseError.errorCode + "\nParse reason:" + xmlResultadoImport.parseError.reason + "\nLinea:" + xmlResultadoImport.parseError.line);
                                }
                            }
                            else {
                                xmlResultadoImport = data;
                            }

                            error = $(xmlResultadoImport).find("error").text();

                            if (error == "" || error == undefined) {                               
                                alert("Importaci\u00f3n finalizada exitosamente\n\nResumen:\n\nRegistros con RPUs incorrectos: "+$(xmlResultadoImport).find("rpus_incorrectos").text() + "\n" +
                                      "Registros con id_control incorrectos: "+$(xmlResultadoImport).find("id_control_incorrectos").text());

                            }
                        },
                        error: function (xhr, err) {
                            alert("Error al recuperar los datos de proceso: \n" + +xhr.responseText);
                        }
                    });
                    
                    //Recarga del grid
                    $("#grid_144_712_0").jqGrid().trigger("reloadGrid");
                } else {
                    alert(error);
                }
                $("#divwait").dialog("close");
            },
            error: function (xhr, err) {
                alert("Error al recuperar los datos de proceso: \n" + +xhr.responseText);
            }
        });
    });
}

function importacion_grid_detalle_init() {

}

function padron_ampliado_potencial_init() {
    $("#clave_estado").change(function(){
         //Llena la lista de municipios de acuerdo al estado
        if ($(this).val() != "") {
            setXMLInSelect4("#clave_municipio", 611, "select", '', "clave_estado=" + $(this).val());
        } else {
            $("#clave_municipio").empty();
            $("#poblacioncfe").empty();
            $("#tienda,#encargado").val("");
        }
    }); 
    
    $("#clave_municipio").change(function(){
        if ($(this).val() != "") {
            setXMLInSelect4("#poblacioncfe", 741, "foreign", "", "clave_municipio=" + $(this).val());
        } else {
            $("#poblacioncfe").empty();
            $("#tienda,#encargado").val("");
        }
    });
}

function padron_ampliado_potencial_grid_init(){
    url =$("#grid_152_737_0").jqGrid("getGridParam","url");
    aWhere = url.split("&"); 
    where = "";
    for (i=0;i<aWhere.length;i++) {
        if (aWhere[i].indexOf("$w=")==0) {
            where = decodeURIComponent(aWhere[i].replace("$w=",""));
            //where = aWhere[i].replace("$w=","");
            break;
        }
    }
    
    //Solo si se estableció un filtro permite continuar 
    if (where!="") {
        $("#consulta").val(where);
        $("#btnGuardar_" + $("#_sc_").val()).removeAttr("disabled").button( "refresh" );
    } else {
        $("#consulta").val("");
        $("#btnGuardar_" + $("#_sc_").val()).attr("disabled","disabled").button( "refresh" );
    }
}

function activaTimer(claveImportacion) {
    var timer;
    $.ajax({
        url: "control?$cmd=importfile&$ci=" + claveImportacion,
        dataType: ($.browser.msie) ? "text" : "xml",
        success: function (data) {
            if (typeof data == "string") {
                xmlImportTimer = new ActiveXObject("Microsoft.XMLDOM");
                xmlImportTimer.async = false;
                xmlImportTimer.validateOnParse = "true";
                xmlImportTimer.loadXML(data);

                if (xmlImportTimer.parseError.errorCode > 0) {
                    alert("Error de compilación xml:" + xmlImportTimer.parseError.errorCode + "\nParse reason:" + xmlImportTimer.parseError.reason + "\nLinea:" + xmlImportTimer.parseError.line);
                }
            }
            else {
                xmlImportTimer = data;
            }

            error = $(xmlImportTimer).find("error").text();

            if (error == "" || error == undefined) {
                avance = $(xmlImportTimer).find("avance").text();

                if (($(this).attr("value")) == undefined) {
                    $("#progressbar_" + claveImportacion).progressbar("option", "value", false);
                } else {
                    $("#progressbar_" + claveImportacion).progressbar("value", parseInt(avance));
                }

                //hace timeout 
                if (avance != "Concluido")
                    timer = setTimeout(activaTimer(claveImportacion), 2000);
                else
                    clearTimeout(timer);

            } else {
                $("#importa_" + claveImportacion).text("Continuar");
                clearTimeout(timer);
                alert(error);
            }
        },
        error: function (xhr, err) {
            alert("Error al recuperar los datos de proceso: \n" + +xhr.responseText);
        }
    });
} 

