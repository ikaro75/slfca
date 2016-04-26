/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function be_forma_init() {
    clave_aplicacion = $("#_cache_").val();  //Debe ser el valor de la segunda columna
    $("#_cache_").val("");
    clave_forma = $(xml).find("registro").find("clave_forma")[0].firstChild.data;
    
    if (clave_forma == "") {
        clave_forma = "0";
    }

    if ($("#form_" + clave_aplicacion + "_3_" + clave_forma + " #tabla").val() != "") {
        setXMLInSelect3("form_" + clave_aplicacion + "_3_" + clave_forma + " #llave_primaria", -2, "update", $("#tabla").val());
        llave = $(xml).find("registro").find("llave_primaria")[0].childNodes[0].data;
        $("#form_" + clave_aplicacion + "_3_" + clave_forma + " #llave_primaria option[value=" + llave + "]").attr("selected", true);
    }

    $("#form_" + clave_aplicacion + "_3_" + clave_forma + " #tabla").change(function() {
        if ($("#form_" + clave_aplicacion + "_3_" + clave_forma + " #tabla").val() != "")
            setXMLInSelect3("form_" + clave_aplicacion + "_3_" + clave_forma + " #llave_primaria", -2, "update", $(this).val());
    });

    /*if (!$("#form_" + clave_aplicacion + "_3_" + clave_forma + " #muestra_formas_foraneas").is(':checked')) {
     $("#form_" + clave_aplicacion + "_3_" + clave_forma + " #td_clave_tipo_presentacion_forma_foranea a").hide();
     $("#form_" + clave_aplicacion + "_3_" + clave_forma + " #clave_tipo_presentacion_forma_foranea").hide();
     }*/

}

function be_aplicacion_grid_init() {

    $("a.export_link").click(function() {
        aplicacion = (this).id.split("-")[0];
        $("#_cache_").val(aplicacion);
        $("body").form({
            app: aplicacion,
            forma: 292,
            modo: "insert",
            pk: 0,
            width: "600"
        });
    });


}

function importa_init() {
    $("#clave_aplicacion").val($("#_cache_").val());
    $("#_cache_").val("");
    aplicacion = $("#clave_aplicacion").val();

    $("#btnGuardar_" + aplicacion + "_601_0").parent().html("<input type='button' style='float: right;' class='formButton' id='btnImporta' value='Importar'>");
    $("#btnImporta").button().click(function() {
        //  mandar a llamar con la función de jquery ajax el jsp

        $.ajax(
                {
                    url: "control?$cmd=import&$app=" + $("#clave_aplicacion").val() + "&$prefijo_tablas=" + escape($("#tabla").val()),
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
                            $("#tdEstatus_28_292_0").html($(xmlResultImport).find("error").text());
                        } else {
                            warnings = "";
                            $(xmlResultImport).find("warning").each(function() {
                                warnings += $(this).text() + "\n";
                            })

                            alert($(xmlResultImport).find("resumen").text() + "\n" + warnings);
                            $("#dlgModal_28_292_0").remove();
                        }
                    },
                    error: function(xhr, err) {
                    }
                });
    });
}

function campos_forma_init() {
    var tipo_control = $("#tipo_control").val();
    if (tipo_control == "5") {
        $("#td_obligatorio").children().hide();
        $("#obligatorio").hide();
        $("#td_clave_forma_foranea").children().hide();
        $("#clave_forma_foranea").hide().siblings().hide();
        $("#td_tamano").children().hide();
        $("#tamano").hide();
        $("#td_usado_para_agrupar").children().hide();
        $("#usado_para_agrupar").hide();
    }
    else {
        $("#td_obligatorio").children().show();
        $("#obligatorio").show();
        $("#td_clave_forma_foranea").children().show();
        $("#clave_forma_foranea").show().siblings().show();
        $("#td_tamano").children().show();
        $("#tamano").show();
        $("#td_usado_para_agrupar").children().show();
        $("#usado_para_agrupar").show();
    }

    /*if ($("#clave_forma_foranea").val()!="") {
     $("#td_filtro_foraneo").parent().show();
     $("#td_edita_forma_foranea").parent().show();
     $("#td_carga_dato_foraneos_retrasada").parent().show();
     $("#td_no_permitir_valor_foraneo_nulo").parent().show();
     }
     else {
     $("#td_filtro_foraneo").parent().hide();
     $("#td_edita_forma_foranea").parent().hide();
     $("#td_carga_dato_foraneos_retrasada").parent().hide();
     $("#td_no_permitir_valor_foraneo_nulo").parent().hide();
     }*/

    $('#tipo_control').click(function() {
        if (this.value == "5") {
            $("#td_obligatorio").children().hide();
            $("#obligatorio").hide();
            $("#td_clave_forma_foranea").children().hide();
            $("#clave_forma_foranea").hide().siblings().hide();
            $("#td_tamano").children().hide();
            $("#tamano").hide();
            $("#td_usado_para_agrupar").children().hide();
            $("#usado_para_agrupar").hide();
        }
        else {
            $("#td_obligatorio").children().show();
            $("#obligatorio").show();
            $("#td_clave_forma_foranea").children().show();
            $("#clave_forma_foranea").show().siblings().show();
            $("#td_tamano").children().show();
            $("#tamano").show();
            $("#td_usado_para_agrupar").children().show();
            $("#usado_para_agrupar").show();
        }
    });
}

function be_aplicacion_grid_init() {

    $("a.export_link").click(function() {
        aplicacion = (this).id.split("-")[0];
        $("#_cache_").val(aplicacion);
        $("body").form({
            app: aplicacion,
            forma: 601,
            modo: "insert",
            pk: 0,
            width: "600"
        });
    });
}

function be_forma_grid_init() {
    $("a.update_form_link").click(function() {
        //formaYTabla = $(this).id.split("-")[1];
        $("#_cache_").val(this.id.split("-")[1] + "-" + this.id.split("-")[2]);
        $("body").form({
            app: 1,
            forma: 627,
            modo: "insert",
            pk: 0,
            width: "600",
            columnas: 1
        });
    });
}

function be_equivalencia_init() {
    //Se llama al webservice, el cual debe detectar si:
    //1. Existe la tabla de la forma
    //2. Contiene los mismos campos que están en el diccionario de datos
    var claveEquivalencia = $(xml).find("clave_equivalencia")[0].childNodes[0].data;
    var tablaNueva = $(xml).find("tabla_nueva")[0].childNodes[0].data;
    var tablaAnterior = $(xml).find("tabla_anterior")[0].childNodes[0].data;
    var claveForma = $(xml).find("clave_forma")[0].childNodes[0].data;
    
    if (tablaAnterior=="")
        tablaAnterior=escape($("#_cache_").val().split("-")[1]);
    
    if (claveForma=="")
        claveForma = escape($("#_cache_").val().split("-")[0]);

    if (claveEquivalencia == "")
        claveEquivalencia = "0";
    
    $("#form_1_627_" + claveEquivalencia + " #clave_forma").val(claveForma);
    $("#form_1_627_" + claveEquivalencia + " #tabla_anterior").val(tablaAnterior);

    $("#td_tabla_nueva").parent().hide();
    $("#tabla_nueva").removeClass("obligatorio");

    if (claveEquivalencia == "0") {
        $.ajax({
            url: "control?$cmd=exists_table&tabla=" + tablaAnterior,
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlResultFix = new ActiveXObject("Microsoft.XMLDOM");
                    xmlResultFix.async = false;
                    xmlResultFix.validateOnParse = "true";
                    xmlResultFix.loadXML(data);
                    if (xmlResultFix.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlResultFix.parseError.errorCode + "\nParse reason:" + xmlResultFix.parseError.reason + "\nLinea:" + xmlResultFix.parseError.line);
                    }
                }
                else {
                    xmlResultFix = data;
                }

                if ($(xmlResultFix).find("error").length > 0) {
                    alert($(xmlResultFix).find("error").text());
                    $("#tdEstatus_1_627_0").html($(xmlResultFix).find("error").text());
                }

                if ($(xmlResultFix).find("respuesta").text() != "si") {
                    alert("No se encontró la tabla, seleccione cuál la sustituirá")
                    $("#td_tabla_nueva").parent().show();
                    $("#tabla_nueva").addClass("obligatorio");
                } else {
                    $("#td_tabla_nueva").parent().hide();
                    $("#tabla_nueva").removeClass("obligatorio");
                    
                    //Inserta registro de la defición de la equivalencia 
                    $("#btnGuardar_1_627_0").click();
                }
            },
            error: function(xhr, err) {
            }
        });
    }
    
    if (tablaNueva=="") {
        $("#td_tabla_nueva").parent().hide();
    }
}

function be_equivalencia_campo_init() {
    var claveEquivalencia = $(xml).find("clave_equivalencia")[0].childNodes[0].data;
    var tablaAnterior = $("#form_1_627_" + claveEquivalencia + " #tabla_anterior").val();
    setXMLInSelect4("#campo_nuevo",-2,"update",tablaAnterior,"");
    $("#campo_nuevo").append('<option value="[Borrar de consultas y diccionario]">[Borrar de consultas y diccionario]</option>');
}

function be_equivalencia_campo_grid_init() {
    claveAplicacion=$(xml).find("configuracion_grid").find("clave_aplicacion")[0].childNodes[0].data;
    claveForma=$(xml).find("configuracion_grid").find("clave_forma")[0].childNodes[0].data;
    claveRegistro="0";
    registros=parseInt($("#grid_"+ claveAplicacion + "_" + claveForma + "_0").getGridParam("reccount"));
    
    if (registros==0){
         $($("#formGrid_" + claveAplicacion + "_" + claveForma).parent().parent().parent()).dialog("destroy").remove();
         alert("Consultas y diccionario de datos actualizados");
    }
    

}