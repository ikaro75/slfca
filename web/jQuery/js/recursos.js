function rcrss_recursos_int() {

    //Código para crear campos extras al cargar
    clave_recurso = $(xml).find("clave_recurso")[0].childNodes[0].data; 
    clave_categoria_recurso = $(xml).find("clave_categoria_recurso")[0].childNodes[0].data; 
    descripcion = $(xml).find("descripcion")[0].childNodes[0].data;
    clave_tipo_recurso = $(xml).find("clave_tipo_recurso")[0].childNodes[0].data;

    if (clave_recurso == "") {
        clave_recurso = "0";
    }

    if (clave_categoria_recurso != "") {
        $.ajax(
                {
                    url: "control?$cmd=plain&$cf=360&$pk=&$ta=select&$w=clave_categoria_recurso=" + clave_categoria_recurso,
                    dataType: ($.browser.msie) ? "text" : "xml",
                    success: function(data) {
                        if (typeof data == "string") {
                            xmlCategoriaRecurso = new ActiveXObject("Microsoft.XMLDOM");
                            xmlCategoriaRecurso.async = false;
                            xmlCategoriaRecurso.validateOnParse = "true";
                            xmlCategoriaRecurso.loadXML(data);
                            if (xmlCategoriaRecurso.parseError.errorCode > 0) {
                                alert("Error de compilación xml:" + xmlCategoriaRecurso.parseError.errorCode + "\nParse reason:" + xmlCategoriaRecurso.parseError.reason + "\nLinea:" + xmlCategoriaRecurso.parseError.line);
                            }
                        }
                        else {
                            xmlCategoriaRecurso = data;
                        }

                        oComponentes = $(xmlCategoriaRecurso).find("clave_categoria_recurso_componente");
                        sHtml = "";
                        i = 0;
                        aDescripcion = descripcion.split("\n");
                        $("#descripcion").val(aDescripcion[0]);
                        $(oComponentes).each(function() {
                            if ($(oComponentes[i]).text() != "") {
                                sHtml += "<tr class='componente'><td class='etiqueta_forma1' id='td_descripcion" + i + "'><a class='edit_field' href='#' >" + $(oComponentes[i]).text() + "</a>(<span id='msgvalida_descripcion" + i + "' style='display: none;'>Obligatorio</span>*)</td><td class='etiqueta_forma_control1'><input type='' value='" + aDescripcion[i + 1] + "' class='singleInput obligatorio' name='descripcion' id='descripcion' tipo_dato='varchar'></td></tr>"
                            }
                            i++;
                        });

                        //Se eliminan los trs con clase 'componente''
                        $("#td_descripcion").parent().parent().parent().find(".componente").remove();

                        //Se agregan los controles a la descripción
                        $("#td_descripcion").parent().after(sHtml);


                        /*xmlCategoriaRecurso.                
                         5. Si se trata de una categoria sin subcategorias deben de recuperarse los componentes asociados al insumo
                         y crear los controles para su captura*/

                    },
                    error: function(xhr, err) {
                        alert("Error al recuperar subcategoría. " + xhr.readyState + "\nstatus: " + xhr.status + "\responseText:" + xhr.responseText);
                    }
                });
    }

    if (clave_tipo_recurso != "") {
        if (clave_tipo_recurso == "1") { //Si se trata de recursos humanos...
            setXMLInSelect3("clave_categoria_recurso", 352, "foreign", "", "categoria_recurso%20LIKE%20'Perfil%25'");
            $("#td_clave_unidad").parent().hide();
            $("#clave_unidad").removeClass("obligatorio");
        } else if (clave_tipo_recurso == "2") { //Si se trata de recursos materiales...
            setXMLInSelect3("clave_categoria_recurso", 352, "foreign", "", "categoria_recurso%20LIKE%20'Bien%20%25'");
            $("#td_clave_unidad").parent().show();
            $("#clave_unidad").addClass("obligatorio");
        } else if (clave_tipo_recurso == "3") { //Si se trata de servicios externos...
            setXMLInSelect3("clave_categoria_recurso", 352, "foreign", "", "categoria_recurso%20LIKE%20'Servicio%20%25'");
            $("#td_clave_unidad").parent().hide();
            $("#clave_unidad").removeClass("obligatorio");
        } else if (clave_tipo_recurso == "5") { //Si se trata de viáticos...
            setXMLInSelect3("clave_categoria_recurso", 352, "foreign", "", "categoria_recurso%20LIKE%20'Viáticos%25'");
            $("#td_clave_unidad").parent().show();
            $("#clave_unidad").addClass("obligatorio");
        }
        $("#clave_categoria_recurso option[value=" + clave_categoria_recurso + "]").attr("selected", true)
    }

    $("#form_34_354_" + clave_recurso + " #clave_tipo_recurso").change(function() {
        if (this.value == "1") { //Si se trata de recursos humanos...
            setXMLInSelect3("clave_categoria_recurso", 352, "foreign", "", "categoria_recurso%20LIKE%20'Perfil%25'");
            $("#td_clave_unidad").parent().hide();
            $("#form_34_354_" + clave_recurso + "#clave_unidad").removeClass("obligatorio");
        } else if (this.value == "2") { //Si se trata de recursos materiales...
            setXMLInSelect3("clave_categoria_recurso", 352, "foreign", "", "categoria_recurso%20LIKE%20'Bien%20%25'");
            $("#td_clave_unidad").parent().show();
            $("#form_34_354_" + clave_recurso + "#clave_unidad").addClass("obligatorio");
        } else if (this.value == "3") { //Si se trata de servicios externos...
            setXMLInSelect3("clave_categoria_recurso", 352, "foreign", "", "categoria_recurso%20LIKE%20'Servicio externo%20%25'");
            $("#td_clave_unidad").parent().hide();
            $("#form_34_354_" + clave_recurso + "#clave_unidad").removeClass("obligatorio");
        } else if (this.value == "5") { //Si se trata de viáticos...
            setXMLInSelect3("clave_categoria_recurso", 352, "foreign", "", "categoria_recurso%20LIKE%20'Viáticos%20%25'");
            $("#td_clave_unidad").parent().hide();
            $("#form_34_354_" + clave_recurso + "#clave_unidad").removeClass("obligatorio");
        }
    });

    $("#clave_categoria_recurso").change(function() {
        sForma = $(this).parent().parent().parent().parent().parent()[0].id;
        if ($(this).val() == "") {
            $("#" + sForma + " #td_descripcion").parent().parent().parent().find(".componente").remove();
            return;
        }

        $.ajax(
                {
                    url: "control?$cmd=plain&$cf=360&$pk=&$ta=select&$w=clave_categoria_recurso=" + $(this).val(),
                    dataType: ($.browser.msie) ? "text" : "xml",
                    success: function(data) {
                        if (typeof data == "string") {
                            xmlCategoriaRecurso = new ActiveXObject("Microsoft.XMLDOM");
                            xmlCategoriaRecurso.async = false;
                            xmlCategoriaRecurso.validateOnParse = "true";
                            xmlCategoriaRecurso.loadXML(data);
                            if (xmlCategoriaRecurso.parseError.errorCode > 0) {
                                alert("Error de compilación xml:" + xmlCategoriaRecurso.parseError.errorCode + "\nParse reason:" + xmlCategoriaRecurso.parseError.reason + "\nLinea:" + xmlCategoriaRecurso.parseError.line);
                            }
                        }
                        else {
                            xmlCategoriaRecurso = data;
                        }

                        oComponentes = $(xmlCategoriaRecurso).find("clave_categoria_recurso_componente");
                        sHtml = "";
                        i = 0;
                        $(oComponentes).each(function() {
                            if ($(oComponentes[i]).text() != "") {
                                sHtml += "<tr class='componente'><td class='etiqueta_forma1' id='td_descripcion" + i + "'><a class='edit_field' href='#' >" + $(oComponentes[i]).text() + "</a>(<span id='msgvalida_descripcion" + i + "' style='display: none;'>Obligatorio</span>*)</td><td class='etiqueta_forma_control1'><input type='' value='' class='singleInput obligatorio' name='descripcion' id='descripcion' tipo_dato='varchar'></td></tr>"
                            }
                            i++;
                        });

                        //Se eliminan los trs con clase 'componente''
                        $("#" + sForma + " #td_descripcion").parent().parent().parent().find(".componente").remove();

                        //Se agregan los controles a la descripción
                        $("#" + sForma + " #td_descripcion").parent().after(sHtml);


                        /*xmlCategoriaRecurso.                
                         5. Si se trata de una categoria sin subcategorias deben de recuperarse los componentes asociados al insumo
                         y crear los controles para su captura*/

                    },
                    error: function(xhr, err) {
                        alert("Error al recuperar subcategoría. " + xhr.readyState + "\nstatus: " + xhr.status + "\responseText:" + xhr.responseText);
                    }
                });
    });

    $("#" + sForma + " #clave_tipo_recurso").change(function() {
        $("#" + sForma + " #clave_categoria_recurso option").attr("selected", "");
        $("#" + sForma + " #td_descripcion").parent().parent().parent().find(".componente").remove();
    });

    $("#importe,#clave_impuesto").change(function() {
        sForma = $(this).parent().parent().parent().parent().parent()[0].id;
        $("#" + sForma + " #importe").val($("#" + sForma + " #importe").val().replace(/,/g, "").replace(/\$/g, ""));
        var IVA = parseFloat($("#" + sForma + " #clave_impuesto option:selected").html().split("-")[1]) / 100;
        var importe_total = Math.round((parseFloat($("#" + sForma + " #importe").val()) * (1 + IVA)) * 100) / 100;
        $("#" + sForma + " #importe_total").val(formatCurrency(importe_total));
        $("#" + sForma + " #importe").val(formatCurrency($("#" + sForma + " #importe").val()));

    });
}