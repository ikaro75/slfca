/* 
 * Plugin de jQuery para cargar forma a través de un plugin
 * 
 */
(function($) {
    $.fn.form_mobile = function(opc) {

        $.fn.form_mobile.settings = {
            titulo: "",
            app: "",
            forma: "",
            pk: "",
            pk_name: "",
            xmlUrl: "control?$cmd=form", // "srvControl" "xml_tests/forma.app.xml",
            filtroForaneo: "",
            columnas: 2,
            modo: "",
            top: 122,
            height: 500,
            width: 510,
            datestamp: "",
            updateControl: "",
            updateForeignForm: "",
            originatingObject: "",
            showRelationships: "false",
            permiteDuplicarRegistro: "false",
            permiteInsertarComentarios: "false",
            permiteGuardarComoPlantilla: "false",
            events: [],
            error: ""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each(function() {
            $.fn.form_mobile.options = $.extend($.fn.form_mobile.settings, opc);
            var obj = $(this);
            $.fn.form_mobile.getGUI(obj);
        });

    };


    $.fn.form_mobile.getGUI = function(obj) {
        var host = ""; //"http://siap.ilce.edu.mx/intranet-movil/";
        
        setTimeout(function(){$.mobile.loading('show');},1);
        $("#_status_").val("Iniciando forma");
        $("#titulo_registro_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk).remove();
        $("#subtitulo_registro_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk).next().remove()
        $("#subtitulo_registro_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk).remove();
        $("#divLink_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk).remove();
        //Crea clave unica para forma
        var formSuffix = $.fn.form_mobile.options.app + "_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk;
        var sDialogo = "";
        var sMainDivTabs = "";
        var sDivTabs = "";
        var sUlTabs = "";
        var sBotonera = "";
        
        //1. Primero crear el HTML necesario para contruir la interfaz de las relaciones

        sMainDivTabs = "<div id='formTab_" + formSuffix + "' security='" +
                "' datestamp='" + $.fn.form_mobile.options.datestamp +
                "' app='" + $.fn.form_mobile.options.app +
                "' forma='" + $.fn.form_mobile.options.forma +
                "' pk='" + $.fn.form_mobile.options.pk +
                "' pk_name='" + $.fn.form_mobile.options.pk_name +
                "' modo='" + $.fn.form_mobile.options.modo +
                "' originatingObject='" + $.fn.form_mobile.options.originatingObject +
                "' updateControl='" + $.fn.form_mobile.options.updateControl +
                "' updateForeignForm='" + $.fn.form_mobile.options.updateForeignForm +
                "' permiteDuplicarRegistro='" + $.fn.form_mobile.options.permiteDuplicarRegistro +
                "' permiteInsertarComentarios='" + $.fn.form_mobile.options.permiteInsertarComentarios +
                "' filtroForaneo='" + $.fn.form_mobile.options.filtroForaneo + 
                "' alias_log='" +
                "' >";

        sMainDivTabs += "</div>";
        sDialogo += "<div id='dlgModal_" + formSuffix + "' title='" + $.fn.form_mobile.options.titulo + "'>" + sMainDivTabs + "</div>";
        obj.append(sDialogo);

        $("#_status_").val("Cargando datos de la forma");

        $.ajax({
            url: $.fn.form_mobile.options.xmlUrl + "&$cf=" + $("#formTab_" + formSuffix).attr("forma") + "&$pk=" + $("#formTab_" + formSuffix).attr("pk") + "&$ta=" + $("#formTab_" + formSuffix).attr("modo") + "&1=clave_aplicacion=" + $("#formTab_" + formSuffix).attr("pk") + "&" + $("#formTab_" + formSuffix).attr("filtroForaneo"),
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xml = new ActiveXObject("Microsoft.XMLDOM");
                    xml.async = false;
                    xml.validateOnParse = "true";
                    xml.loadXML(data);
                    if (xml.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xml.parseError.errorCode + "\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                    }
                }
                else {
                    xml = data;
                }

                /*Verifica el estatus de error*/
                var formSuffix = $.fn.form_mobile.options.app + "_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk;
                var oError = $(xml).find("error");
                if (oError.length > 0) {
                    var sDescripcionError = oError.text();
                    $("#registro_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk).remove();
                    alert(sDescripcionError);
                    $("#_status_").val("");
                    return false;
                }

                var sCatalogosForaneos = "";
                var sWSParameters = "";
                $.fn.form_mobile.options.showRelationships = $(xml).find("configuracion_forma").find("muestra_formas_foraneas").text();
                if ($.fn.form_mobile.options.showRelationships == "true" && $("#formTab_" + formSuffix).attr("modo") == 'update') {
                    if ($(xml).find("configuracion_forma").find("clave_tipo_presentacion_forma_foranea").text() == "1") {
                        oTabsForaneos = $(xml).find("clave_forma_foranea");

                        if (oTabsForaneos.length > 0) {
                            
                            sWSParameters = "&" + $(xml).find("llave_primaria").text() + "=" + $.fn.form_mobile.options.pk + "&4=" + $(xml).find("llave_primaria").text() + "=" + $.fn.form_mobile.options.pk + "&";
                            sCatalogosForaneos = '<h4>Cat&aacute;logos relacionados</h4>' +
                                    '<div data-role="collapsible-set" data-theme="a" data-content-theme="a" id="foraneos_' + $.fn.form_mobile.options.forma + '_' + $.fn.form_mobile.options.pk + '" data-collapsed-icon="carat-r" data-expanded-icon="carat-d">';
                                    
                            oTabsForaneos.each(function() {
                                sCatalogosForaneos += '<div class="colapsableforaneo" data-role="collapsible" data-collapsed="true" id="foraneo_' + $(this).text() + '">' + 
                                                      '<h3>' + $(this).next().text() + '<div style="float:right"><input type="button" data-icon-pos="top" data-icon="plus" data-inline="true" data-mini="true" data-theme="a" class="newRecord ui-btn-right" id="lnkNuevo_'+ $(this).text() + '" value="Nuevo"/></div></h3>' + 
                                                      '<ul data-role="listview" data-inset="true" id="lvForaneo_' + $(this).text() + '" data-filter="true" data-filter-placeholder="Buscar..." style="box-shadow:none;" data-theme="h" class="lvforeign"></ul></div>';
                            });

                        }

                        sCatalogosForaneos += '</div>';

                        //Se debe crear un listview con las ligas para los catálogos relacionados
                    }
                }

                $.fn.form_mobile.options.permiteDuplicarRegistro = $(xml).find("permite_duplicar_registro").text();
                $.fn.form_mobile.options.permiteInsertarComentarios = $(xml).find("permite_insertar_comentarios").text();
                $.fn.form_mobile.options.permiteGuardarComoPlantilla = $(xml).find("permite_guardar_como_plantilla").text();
                /* Procesamiento de permisos */
                var sPermiso = "";
                var oPermisos = $(xml).find("clave_permiso");
                oPermisos.each(function() {
                    sPermiso += $(this).text() + ",";
                });
                
                sPermiso = sPermiso.substr(0, sPermiso.length - 1);

                if (sPermiso.indexOf("2") == -1 && $("#formTab_" + formSuffix).attr("modo") == 'insert') {
                    alert("Su perfil no cuenta con permisos para insertar registros de esta forma, consulte al administrador del sistema");
                    //Cierra el dialogo de espera
                    $("#_status_").val("");
                    return false;
                }

                if (sPermiso.indexOf("3") == -1 && $("#formTab_" + formSuffix).attr("modo") == 'update') {
                    alert("Su perfil no cuenta con permisos para actualizar registros de esta forma, consulte al administrador del sistema");
                    //Cierra el dialogo de espera
                    $("#_status_").val("");
                    return false;
                }

                //Se extraen datos generales de la forma
                sAliasLog = $(xml).find("configuracion_forma").find("alias_tab").text()
                $("#formTab_" + formSuffix).attr("alias_log", sAliasLog);
                $("#formTab_" + formSuffix).attr("pk_name", $(xml).find("configuracion_forma").find("llave_primaria").text());

                sTitulo = "";
                if (sAliasLog.split(' ').length > 1) {
                    if (sAliasLog.split(' ')[0] == 'la' && $("#formTab_" + formSuffix).attr("modo") == 'insert')
                        sTitulo = "Nueva " + sAliasLog.split(' ')[1];

                    if (sAliasLog.split(' ')[0] == 'el' && $("#formTab_" + formSuffix).attr("modo") == 'insert')
                        sTitulo = "Nuevo " + sAliasLog.split(' ')[1];

                    if ($("#formTab_" + formSuffix).attr("modo") == 'update')
                        sTitulo = "Edici&oacute;n de " + sAliasLog.split(' ')[1];
                }

                $("#titulo_header_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk).html(sTitulo);
                //Si se permite duplicar el registro actual
                if ($.fn.form_mobile.options.modo == "update" && $.fn.form_mobile.options.permiteDuplicarRegistro == "true") {
                    sHTML = $("#formTab_" + formSuffix).html();
                    $("#formTab_" + formSuffix).html(sHTML.replace('<!-- InicioOpciones -->', '<div><input type="button" class="formButton" id="opciones_' + formSuffix + '" value="Opciones" /><input type="button" class="formButton" id="btnMenuOpciones_' + formSuffix + '" value="" /></div><ul>'));
                    sHTML = $("#formTab_" + formSuffix).html();
                    $("#formTab_" + formSuffix).html(sHTML.replace('<!-- Duplicar -->', '<li><a id="btnDuplicar_' + formSuffix + '">Duplicar</a></li>'));
                }

                //Si se puede guardar como plantilla y aplicarlas
                if ($.fn.form_mobile.options.modo == "update" && $.fn.form_mobile.options.permiteGuardarComoPlantilla == "true") {
                    sHTML = $("#formTab_" + formSuffix).html();
                    $("#formTab_" + formSuffix).html(sHTML.replace('<!-- InicioOpciones -->', '<div><input type="button" class="formButton" id="opciones_' + formSuffix + '" value="Opciones" /><input type="button" class="formButton" id="btnMenuOpciones_' + formSuffix + '" value="" /></div><ul>'));
                    sHTML = $("#formTab_" + formSuffix).html();
                    $("#formTab_" + formSuffix).html(sHTML.replace('<!-- GuardarComoPlantilla -->', '<li><a id="btnGuardarComoPlantilla_' + formSuffix + '" >Guardar como plantilla...</a></li>'));
                    sHTML = $("#formTab_" + formSuffix).html();
                    $("#formTab_" + formSuffix).html(sHTML.replace('<!-- AplicarPlantilla -->', '<li><a id="btnAplicarPlantilla_' + formSuffix + '" >Aplicar plantilla...</a></li>'));
                }

                if ($.fn.form_mobile.options.modo == "insert" && $.fn.form_mobile.options.permiteGuardarComoPlantilla == "true") {
                    sHTML = $("#formTab_" + formSuffix).html();
                    $("#formTab_" + formSuffix).html(sHTML.replace('<!-- InicioOpciones -->', '<div><input type="button" class="formButton" id="opciones_' + formSuffix + '" value="Opciones" /><input type="button" class="formButton" id="btnMenuOpciones_' + formSuffix + '" value="" /></div><ul>'));
                    sHTML = $("#formTab_" + formSuffix).html();
                    $("#formTab_" + formSuffix).html(sHTML.replace('<!-- AplicarPlantilla -->', '<li><a id="btnAplicarPlantilla_' + formSuffix + '" >Aplicar plantilla...</a></li>'));
                }
                
                var htmlForNotes = "";
                
                if ($.fn.form_mobile.options.modo == "update" && $.fn.form_mobile.options.permiteInsertarComentarios == "true") {
                    notas = $(xml).find("notas_forma").find("nota");
                    $.each(notas, function() {
                        if ($(this).find('foto').text() == "") {
                            foto = "img/sin_foto.jpg"
                        }
                        else {
                            foto = $(this).find('foto').text()
                        }

                        htmlForNotes +=
                                '<div id="comentario-' + $(this).attr("id") +
                                '" foto="' + foto +
                                '" nombre="' + $(this).find('nombre').text() +
                                '" titulo="' + $(this).find('titulo').text() +
                                '" mensaje="' + $(this).find('mensaje').text() +
                                '" fecha_nota="' + $(this).find('fecha_nota').text() +
                                '" class="comentario"></div>';
                    });
                }
                //Verifica si tiene permisos para eliminar
                /*if ($.fn.form_mobile.options.modo == "update" && sPermiso.indexOf("3") > -1) {
                 sHTML = $("#formTab_" + formSuffix).html();
                 $("#formTab_" + formSuffix).html(sHTML.replace('<!-- InicioOpciones -->', '<div><input type="button" class="formButton" id="opciones_' + formSuffix + '" value="Opciones" /><input type="button" class="formButton" id="btnMenuOpciones_' + formSuffix + '" value="" /></div><ul>'));
                 sHTML = $("#formTab_" + formSuffix).html();
                 $("#formTab_" + formSuffix).html(sHTML.replace('<!-- Eliminar -->', '<li><a id="btnEliminar_' + formSuffix + '" >Eliminar</a></li>'));
                 }*/

                //Inicia generación de HTML de la forma general 
                var sRenglon = '';
                var nApp = $.fn.form_mobile.options.app;
                var oCampos = $(xml).find("registro").children();
                var tabIndex = 1;
                bVDS = $("#formTab_" + formSuffix).attr("security").indexOf("5") != -1 ? true : false;
                sInvisibleInputs = "";
                var bAutoIncrement = false;

                oCampos.each(function() {
                    sValorPredeterminado = "";
                    oCampo = $(this);
                    sTipoCampo = oCampo.attr("tipo_dato").toLowerCase();
                    if (oCampo.find('evento').text() != "")
                        $.fn.form_mobile.options.events[tabIndex - 1] = oCampo.find('evento').text();

                    bAutoIncrement = (oCampo.attr("autoincrement") != undefined) ? true : false;
                    if (bAutoIncrement)
                        $.fn.form_mobile.options.pk_name = oCampo[0].nodeName;
                    //Genera etiqueta
                    nClave_campo = (oCampo.find('clave_campo').text() == undefined) ? 0 : oCampo.find('clave_campo').text();
                    sAlias = oCampo.find('alias_campo').text();
                    bActivo = oCampo.find('activo').text();
                    sValorPredeterminado = oCampo.find('valor_predeterminado').text();
                    bVisible = oCampo.find('visible').text();
                    bNoPermitirValorForaneoNulo = oCampo.find('no_permitir_valor_foraneo_nulo').text();
                    sAyuda = oCampo.find('ayuda').text();
                    nObligatorio = oCampo.find('obligatorio').text();
                    sTipoControl = oCampo.find('clave_tipo_control').text();
                    //Genera liga para forma foranea
                    var nFormaForanea = $(this).find('foraneo').attr("clave_forma");
                    var nEditaForaneos = $(this).find('foraneo').attr("agrega_registro");
                    
                    /*if (bAutoIncrement)
                        return true;*/
                    if (bVisible == '0' || bVisible == '') {
                        sInvisibleInputs += '<input type="hidden" tipo_dato="' + sTipoCampo + '" id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" value="'
                        if ($.fn.form_mobile.options.modo == 'insert')
                            sInvisibleInputs += (sValorPredeterminado != "") ? eval(sValorPredeterminado) : "";
                        else
                            sInvisibleInputs += oCampo[0].childNodes[0].data;

                        sInvisibleInputs += '" />';
                        return true;
                    }

                    sRenglon += '<div class="ui-field-contain" id="div_' + oCampo[0].nodeName + '" ';

                    if (bVisible == '0')
                        sRenglon += 'class="invisible"';

                    /*if (nEditaForaneos=="true") {
                        sRenglon +='><fieldset data-role="controlgroup" data-type="horizontal" ';
                    }*/
                    
                    sRenglon += '><label for="' + oCampo[0].nodeName + '">' + sAlias;

                    //Verifica si el campo es obligatorio para incluir la leyenda en el alias
                    if ($.fn.form_mobile.options.modo != "lookup" && nObligatorio == "1") {
                        sRenglon += ' (<span class="mensajeobligatorio" id="msgvalida_' + oCampo[0].nodeName + '">Obligatorio</span>*)</label>';
                    } else {
                        sRenglon += '</label>';
                    }
                    
                    if (nEditaForaneos=="true")
                       sRenglon +='<div data-role="controlgroup" data-type="horizontal">';
                   
                    if (nFormaForanea != undefined) {
                        sRenglon += '<select tipo_dato="' + sTipoCampo + '" tabindex="' + tabIndex + '" ';

                        if (bActivo != "1")
                            sRenglon += ' disabled="disabled" ';

                        /*if ($.fn.form_mobile.options.modo != "lookup" && nEditaForaneos == "true") {
                            sRenglon += 'class="inputWidgeted1'
                        }
                        else {
                            sRenglon += 'class="singleInput'
                        }*/

                        // Establece el Tipo de control autocomplete
                        if (sTipoControl == "4") {
                            sRenglon += " autocompleteselect ";
                        }

                        //Establece seudoclase a select
                        if ($.fn.form_mobile.options.modo != "lookup" && oCampo.find('obligatorio').text() == "1") {
                            sRenglon += ' class="obligatorio" ';
                        }/*
                        else {
                            sRenglon += '" ';
                        }*/

                        //sRenglon+='id="' + oCampo[0].nodeName + formSuffix + '" name="' + oCampo[0].nodeName + formSuffix + '" >';
                        sRenglon += 'id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" >';
                        if ($.fn.form_mobile.options.modo == "lookup" || bNoPermitirValorForaneoNulo != "1") {
                            sRenglon += "<option ";
                            if ($.fn.form_mobile.options.modo != 'update' && sValorPredeterminado == "")
                                sRenglon += "selected='selected' ";
                            sRenglon += "></option>";
                        }

                        oCamposForaneos = oCampo.find('registro_' + oCampo[0].nodeName)

                        oCamposForaneos.each(
                                function() {
                                    oCampoForaneo = $(this);
                                    sRenglon += "<option ";

                                    if ($.fn.form_mobile.options.modo == 'insert' && sValorPredeterminado != "") {
                                        if (eval(sValorPredeterminado) == oCampoForaneo.children()[0].childNodes[0].data)
                                            sRenglon += "selected='selected'";
                                    }

                                    if ($.fn.form_mobile.options.modo == 'update' && oCampo[0].childNodes[0].data == oCampoForaneo.children()[0].childNodes[0].data)
                                        sRenglon += "selected='selected'";
                                    sRenglon += " value='" + oCampoForaneo.children()[0].childNodes[0].data + "' >" + oCampoForaneo.children()[1].childNodes[0].data + "</option>";
                                }
                        )

                        sRenglon += '</select>';

                        if ($.fn.form_mobile.options.modo != "lookup" && nEditaForaneos == "true") {
                            //sRenglon += "<div class='widgetbutton' tipo_accion='" + $.fn.form_mobile.options.modo + "' tipo='foreign_toolbar' control='form" + formSuffix + " #" + oCampo[0].nodeName + "' forma='" + nFormaForanea + "' titulo_agregar='Nuevo " + sAlias.toLowerCase() + "' titulo_editar='Editar " + sAlias.toLowerCase() + "' ></div>";
                            sRenglon += '<a href="#" class="ui-btn ui-btn-inline ui-icon-plus ui-btn-icon-notext ui-corner-all" >No text</a><a href="#" class="ui-btn ui-btn-inline ui-icon-edit ui-btn-icon-notext ui-corner-all" >No text</a></div>';
                        }

                        sRenglon += '</div>';
                        
                        if (nEditaForaneos=="true")
                           sRenglon += '</div>';
                    }
                    else {
                        if (oCampo.find('clave_tipo_control').text() == "textarea" || sTipoCampo == "text") {
                            sRenglon += '<textarea tabindex="' + tabIndex + '" rows="10" ';

                            if (bActivo != "1")
                                sRenglon += ' readonly="readonly" ';

                            sWidgetButton = "";

                            if ((sTipoCampo == 'money' || sTipoCampo == 'decimal') && bActivo == "1") {
                                sRenglon += ' type="number" data-clear-btn="true" ';
                                sWidgetButton = '<div class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset widgetbutton" tipo="calculator_buton" control="form' + formSuffix + ' #' + oCampo[0].nodeName + '"></div>';
                                //sRenglon +="<div class='widgetbutton' tipo='foreign_toolbar' control='" + oCampo[0].nodeName + "' forma='" + nFormaForanea + "' titulo_agregar='Nuevo " + sAlias.toLowerCase() + "' titulo_editar='Editar " + sAlias.toLowerCase() + "' ></div>";
                            } else if (sTipoCampo == 'datetime' && bActivo == "1") {
                                sRenglon += ' type="date" data-clear-btn="true" ';
                            }
                            else if (oCampo.find('clave_tipo_control').text() == "5") {
                                sRenglon += 'class="webeditor';
                            }
                            else {
                                sRenglon += 'class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset singleInput';
                            }

                            //Establece la marca de obligatorio con la seudoclase obligatorio
                            if ($.fn.form_mobile.options.modo != "lookup" && oCampo.find('obligatorio').text() == "1")
                                sRenglon += ' obligatorio"';
                            else
                                sRenglon += '"';

                            sRenglon += ' id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" >';

                            if ($.fn.form_mobile.options.modo == 'insert')
                                sRenglon += (sValorPredeterminado != "") ? eval(sValorPredeterminado) : "";
                            else
                                sRenglon += oCampo[0].childNodes[0].data;

                            sRenglon += '</textarea></div>';
                            
                            //Agrega mecanismo para validar código javascript :-)
                            if (($.fn.form_mobile.options.forma == 13 || $.fn.form_mobile.options.forma == 3) && oCampo[0].nodeName == "evento") {
                                sRenglon += '<div class="ui-field-contain"><label for="btnValidaCodigo" /><input id="btnValidaCodigo" type="button" value="Valida c&oacute;digo" onclick="validateCode(document.getElementById(\'evento\').value)"></div>';
                            }

                            if ($.fn.form_mobile.options.forma == 220 && oCampo[0].nodeName == "notificacion") {
                                sRenglon += '<div class="ui-field-contain"><label for="btnValidaCodigo" /><input type="button" id="btnValidaCodigo" value="Valida c&oacute;digo" onclick="validateCode(document.getElementById(\'notificacion\').value)"></div>';
                            }

                            //Agrega mecanismo para validar sentencias de sql :-)
                            if (($.fn.form_mobile.options.forma == 8 || $.fn.form_mobile.options.forma == 257) && oCampo[0].nodeName == "consulta") {
                                sRenglon += '<div class="ui-field-contain"><label for="btnValidaCodigo" /><input type="button" value="Valida consulta" onclick="validateSQL(document.getElementById(\'consulta\').value);"></div>';
                            }

                            
                        }
                        else if ($(this).find('clave_tipo_control').text() == "checkbox" || (sTipoCampo == "bit" || sTipoCampo == "tinyint")) {
                            sRenglon += '<input data-role="flipswitch" data-on-text="Si" data-off-text="No" type="checkbox" value="1" tabindex="' + tabIndex + '" id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" ';
                            
                            if (bActivo != "1")
                                sRenglon += ' readonly="readonly" ';

                            // Establece la marca de obligatorio con la seudoclase obligatorio
                            if ($.fn.form_mobile.options.modo != "lookup" && oCampo.find('obligatorio').text() == "1") {
                                sRenglon += 'class="singleInput obligatorio" ';
                            }
                            else {
                                sRenglon += 'class="singleInput" ';
                            }

                            if ($.fn.form_mobile.options.modo == 'insert')
                                sRenglon += (sValorPredeterminado == '1') ? 'checked="checked" ' : '';
                            else
                                sRenglon += (oCampo[0].childNodes[0].data == '1' || oCampo[0].childNodes[0].data == 'true') ? 'checked="checked" ' : '';

                            sRenglon += ' /></div>';
                        } else if ($.fn.form_mobile.options.modo != "lookup" && $(this).find('clave_tipo_control').text() == "3" && oCampo[0].childNodes[0].data != "") {
                            sOnChangeEvent = "if (!this.checked){$('#divCheckbox').html('<input type=file tipo_dato=varchar id=" + oCampo[0].nodeName + " name=" + oCampo[0].nodeName + " class=obligatorio />'); $('#" + oCampo[0].nodeName + "').textinput().textinput('refresh');}";
                            sRenglon += '<div id="divCheckbox" style="text-align:left">' +
                                    '<input type="checkbox" value="1" checked="checked" onChange="javascript:' + sOnChangeEvent + '" />';

                            if (oCampo[0].childNodes[0].data.indexOf("Error", 0) > -1) {
                                sRenglon += oCampo[0].childNodes[0].data.substring(oCampo[0].childNodes[0].data.lastIndexOf("/") + 1) + '</div>';
                            } else {
                                sRenglon += '<a href="' + oCampo[0].childNodes[0].data + '" target="_blank">' + oCampo[0].childNodes[0].data + '</a><input type="hidden" name="' + oCampo[0].nodeName + '" id="' + oCampo[0].nodeName + '" value="' + oCampo[0].childNodes[0].data + '" /></div></div>';
                            }
                        }
                        else {
                            sRenglon += '<input tipo_dato="' + sTipoCampo + '" id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" ' +
                                    'tabindex="' + tabIndex + '" ';

                            if (bActivo != "1" || bAutoIncrement )
                                sRenglon += ' readonly="readonly" ';
                            else {
                                sRenglon += ' data-clear-btn="true" ';
                            }

                            sWidgetButton = "";

                            if ((sTipoCampo == 'money' || sTipoCampo == 'decimal') && bActivo == "1") {
                                sRenglon += ' type="number" ';
                            } else if (sTipoCampo == 'datetime' && bActivo == "1") {
                                sRenglon += ' type="datetime-local" ';
                            } else if (sTipoCampo == 'smalldatetime' && bActivo == "1") {
                                sRenglon += ' type="date" ';
                            }

                            if ($.fn.form_mobile.options.modo != "lookup" && oCampo.find('obligatorio').text() == "1")
                                sRenglon += ' class="obligatorio';

                            if (bActivo == "1") {
                                if (sTipoCampo == "datetime")
                                    sRenglon += ' fechayhora';
                                else if (sTipoCampo == "smalldatetime" || sTipoCampo == "date")
                                    sRenglon += ' fecha';
                                else if (sTipoCampo == "money" || sTipoCampo == 'decimal')
                                    sRenglon += ' money';
                            }
                            
                            if ($(this).find('clave_tipo_control').text() == "2" && $.fn.form_mobile.options.modo != "lookup")
                                 sRenglon += '" type="password" value="';
                            if ($(this).find('clave_tipo_control').text() == "3" && $.fn.form_mobile.options.modo != "lookup")
                                sRenglon += ' file" type="file" value="';
                            else {
                                sRenglon += '" type="text" value="';
                            }
                            
                            if ($.fn.form_mobile.options.modo == 'insert')
                                sRenglon += (sValorPredeterminado != "") ? (eval(sValorPredeterminado)) : "";
                            else
                                sRenglon += oCampo[0].childNodes[0].data;

                            sRenglon += '" />';                            

                            //Validación para inputs estandar de acuerdo al tipo de datos del campo
                            /* if (sTipoCampo == "int" /*|| sTipoCampo=="money") {
                                sRenglon += " onBlur='javascript:check_number(this)'";
                            } else if (sTipoCampo == "date") {
                                sRenglon += " onBlur='javascript:check_date(this)' "
                            } */

                            sRenglon +=  sWidgetButton + '</div>';
                        }
                    }
                    tabIndex++;
                }) //oCampos.each

                var sForm = "";

                //Llena la primer pestaña con la forma de la entidad principal
                instrucciones = $(xml).find("configuracion_forma").find("instrucciones").text();
                if (instrucciones!="") {
                    instrucciones = "<div style='clear:both'>" + instrucciones + "<div>";
                }
                
                if (sCatalogosForaneos!="") {
                   sCatalogosForaneos = "<br />" + sCatalogosForaneos;
                }
                
                if ($.fn.form_mobile.options.modo == "update" && $.fn.form_mobile.options.permiteInsertarComentarios == "true") {
                    htmlForNotes = '<h4 style="text-align: left">Comentarios</h4>' + htmlForNotes + '<div class="comentario"></div>';               
                }
                
                sForm = instrucciones + "<form class='forma' id='form_" + formSuffix + "' name='form_" + formSuffix + "' method='POST' >" + sRenglon +
                        sInvisibleInputs +
                        sCatalogosForaneos + 
                        "<input type='hidden' id='_e' name='_e' value='' />" +
                        "<input type='hidden' id='_r' name='_r' value='' />" +
                        "<input type='hidden' id='_tcc' name='_tcc' value='' />" +
                        "<input type='hidden' id='$ta' name='$ta' value='" + $.fn.form_mobile.options.modo + "' />" +
                        "<input type='hidden' id='$ca' name='$ca' value='" + $.fn.form_mobile.options.app + "' />" +
                        "<input type='hidden' id='$cf' name='$cf' value='" + $.fn.form_mobile.options.forma + "' />" +
                        "<input type='hidden' id='$pk' name='$pk' value='" + $.fn.form_mobile.options.pk + "' />" +
                "</form>"; /*+
                "<div data-role='popup' id='divPopup_" + formSuffix + "' class='ui-content' data-theme='a' style='max-width:350px;'><p id='pPopup_" + formSuffix + "'></p></div>";*/
                
                if ($.fn.form_mobile.options.modo == 'insert') {
                    $("#formTab_" + formSuffix).html(sForm + htmlForNotes + "<input type='button' style='float: right;' class='formButton' id='btnGuardar_" + formSuffix + "' value='Guardar' data-theme='a'/>");
                } else if ($.fn.form_mobile.options.modo == 'update'){
                    $("#formTab_" + formSuffix).html(sForm + htmlForNotes + "<div class='ui-field-contain'><input type='button' data-inline='true' class='formaButton' id='btnEliminar_" + formSuffix + "' value='Eliminar' data-theme='a'/><input type='button' data-inline='true' class='formButton' id='btnGuardar_" + formSuffix + "' value='Guardar' data-theme='a'/></div>");
                } else if ($.fn.form_mobile.options.modo == 'lookup'){
                    $("#formTab_" + formSuffix).html(sForm + htmlForNotes + "<input type='button' style='float: right;' class='formButton' id='btnBuscar_" + formSuffix + "' value='Buscar' data-theme='a'/>");                
                }
                // Se construye el collapsible
                 
                // $("#foraneos_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk).trigger('create'); //.collapsibleset({ collapsedIcon: "arrow-r" });
                //$(".colapsableforaneo").trigger('create');
                //Se extrae los parámetros de los filtros foráneos
                
                var sWSParameters= "&3=" + $.fn.form_mobile.options.pk_name+'='+ $.fn.form_mobile.options.pk+'&4=' +  $.fn.form_mobile.options.pk_name+'='+ $.fn.form_mobile.options.pk;
                var aWSParameters=$("#form_" + formSuffix).serialize().split("&");

                for (k=0; k<aWSParameters.length;k++) {
                     if (aWSParameters[k].substr(0,2)!="_e") {
                        if (aWSParameters[k].substr(0,3)=="%24")
                            sWSParameters+="&"+(k+5)+"="+aWSParameters[k].replace("_"+formSuffix,"");
                        else {
                            if ($("#"+aWSParameters[k].split("=")[0])[0].type!="textarea") 
                                sWSParameters+="&"+(k+5)+"="+aWSParameters[k].replace("_"+formSuffix,"");
                            }         
                     } 
                }
                
                // Se agregan los parámetos a cada botón nuevo 
                $(".newRecord").each(function() {
                    if ($(this).attr("parametros-foraneos")==undefined) {
                        $(this).attr("parametros-foraneos",sWSParameters);
                    }    
                }); 
                
                //Crea el collapsible
                //$("#foraneos_" + $.fn.form_mobile.options.forma + "_" + $.fn.form_mobile.options.pk).collapsibleset({ iconpos: "right" });
                //Función para llenar el listview dentro del collapsible
                function loadXmlInListView(forma) {
                    //Se extrae el número de la forma foranea a partir del id
                    //var forma = this.id.split("_")[1]; 
                    var parametros = $("#lnkNuevo_" + forma).attr("parametros-foraneos");
                    
                    if ($("#formTab_" + formSuffix).attr("pk")=="0") return;
                    
                    $.ajax({
                        url: "plain.jsp?&$cf=" + forma + "&$ta=select&$pk=0&$w=" + $("#formTab_" + formSuffix).attr("pk_name") + "=" + $("#formTab_" + formSuffix).attr("pk"),
                        dataType: ($.browser.msie) ? "text" : "xml",
                        success:  function(data){
                             if (typeof data == "string") {
                                xmlForaneo = new ActiveXObject("Microsoft.XMLDOM");
                                xmlForaneo.async = false;
                                xmlForaneo.validateOnParse="true";
                                xmlForaneo.loadXML(data);
                                if (xmlForaneo.parseError.errorCode>0) {
                                       alert("Error de compilación xml:" + xmlForaneo.parseError.errorCode +"\nParse reason:" + xmlForaneo.parseError.reason + "\nLinea:" + xmlForaneo.parseError.line);}
                                }
                                else {
                                    xmlForaneo = data;}
                            
                             registros = $(xmlForaneo).find("registro");
                             if (registros.length > 0) {
                                html = "";
                                $.each(registros, function() {
                                    if ($(this).children()[0].textContent == "") {
                                        setTimeout(function(){$.mobile.loading('hide');},300);
                                        return;
                                    }
                                    //htmlForNoticias+='<div class="item" id="noticia_' + $(this).find("clave_publicacion").text() + '"><a href="#" id="noticia_' + $(this).find("clave_publicacion").text() + '"><img src="/intranet-movil/noticias/' + $(this).find("imagen").text()  + '"><p class="example-title">' +  $(this).find("titulo").text() + '</p></a></div>';                     
                                    //html += '<li><a class="lnkRegister" id="lnkRegister_' + forma + '_' + $(this).children()[0].textContent + '" href="#" data-rel="dialog" data-transition="pop" parametros-foraneos="' + sWSParameters + '"><img src="' + $(this).children()[5].textContent + '"><h2>' + $(this).children()[2].textContent + '</h2><p>' + $(this).children()[1].textContent + '</p><p class="ui-li-aside">' + $(this).children()[3].textContent + '</p></a></li>';
                                    if ($("#lnkRegister_" + +$("#_cf_").val() + "_" + $(this).children()[0].textContent).length == 0)
                                        html += '<li><a class="lnkRegister" id="lnkRegister_' + forma + "_" + $(this).children()[0].textContent + '" href="#" data-rel="page" data-transition="flip"><h2 style="margin-left:1em">' + $(this).children()[1].textContent + '</h2><p style="margin-left:1em">' + ($(this).children().length>3?$(this).children()[2].textContent:"") + '</p><p class="ui-li-aside">' + ($(this).children().length>4?$(this).children()[3].textContent:"") + '</p></a></li>';                                       
                                });
                               }
                            
                            $("#lvForaneo_" + forma ).html(html).listview().listview('refresh');
                            
                            //Se agrega el evento al los botones del listview 
                            $('.lnkRegister').unbind('click');
                            $(".lnkRegister").click(function(e) {
                                var cf = this.id.split("_")[1];
                                var pk = this.id.split("_")[2];
                                var parametros = $(this).attr("parametros-foraneos");
                                if ($("#registro_" + cf + "_" + pk).length==0) {
                                    $("body").append('<div data-role="page" id="registro_' + cf + '_' + pk + '">' +
                                                     '<div data-role="header" class="header" data-theme="a">'+
                                                     '<h1 id="titulo_header_' + cf + '_' + pk + '">Registro</h1><button class="ui-btn ui-icon-back ui-btn-icon-right" id ="btnRegresar">Regresar</button></div>' +  
                                                     '<div data-role="main"  class="ui-content">'+
                                                     '<h2 id="titulo_registro_' + cf + '_' + pk + '"></h2>'+
                                                     '<h3 id="subtitulo_registro_' + cf + '_' + pk + '"></h3>'+
                                                     '<div style="margin: 0px auto; text-align: center;"><img class="ima_tam" id="imagen_' + cf + '_' + pk + '" src="" /></div>'+
                                                     '<div id="divContenido_' + cf + '_' + pk + '"></div>'+
                                                     '<div id="divLink_' + cf + '_' + pk + '"></div>'+
                                                     '</div>');  
                                }

                                 $.ajax({
                                        url: host + "control?$cmd=plain&$cf=" + cf + "&$ta=update&$pk=" + pk,
                                        dataType: ($.browser.msie) ? "text" : "xml",
                                        success: function(data) {
                                            if (typeof data == "string") {
                                                xmlRegistro = new ActiveXObject("Microsoft.XMLDOM");
                                                xmlRegistro.async = false;
                                                xmlRegistro.validateOnParse = "true";
                                                xmlRegistro.loadXML(data);
                                                if (xmlRegistro.parseError.errorCode > 0)
                                                    alert("Error de compilación xml:" + xmlRegistro.parseError.errorCode + "\nParse reason:" + xmlRegistro.parseError.reason + "\nLinea:" + xmlRegistro.parseError.line);
                                            }
                                            else
                                                xmlRegistro = data;

                                            $("#titulo_header_"+ cf + "_" + pk).text($(xmlRegistro).find("configuracion").find("forma").text());
                                            $("#divContenido_" + cf + "_" + pk).form_mobile_queue({app: 1,
                                                forma: cf,
                                                modo: "update",
                                                columnas: 1,
                                                pk: pk,
                                                filtroForaneo: "2=clave_aplicacion=1" + parametros,
                                                height: "500",
                                                width: "800px",
                                                originatingObject: "",
                                                updateControl: ""
                                            });

                                            $.mobile.changePage('#registro_' + cf + '_' + pk, {transition: "flip",role: "page"});

                                        }
                                    });                    
                            });                            
                            setTimeout(function(){$.mobile.loading('hide');},300);
   
                        },
                        error:function(xhr,err){    
                            setTimeout(function(){$.mobile.loading('hide');},300);
                        } 
                    });

                }
                
                $( ".colapsableforaneo" ).on( "collapsibleexpand", function( event, ui ) {
                    loadXmlInListView(this.id.split("_")[1]);}
                );
        
                //$( ".colapsableforaneo" ).bind('expand', function() { alert("Hola mundo");/*loadXmlInListView();*/});
                
                //Se agrega el evento del botón nuevo 
                $(".newRecord").button().button( "refresh" ).click(function(e) {
                     e.preventDefault(); 
                     e.stopPropagation(); 
                     var cf = this.id.split("_")[1];
                     var parametros = $(this).attr("parametros-foraneos");  
                     if ($("#registro_" + cf + "_0").length==0) {
                        $("body").append('<div data-role="page"  style="width:100%;" id="registro_' + cf + '_0">' +
                                          '<div data-role="header" data-theme="a" ><h1 id="titulo_header_' + cf + '_0">Registro</h1><button class="ui-btn ui-icon-back ui-btn-icon-right" id ="btnRegresar">Regresar</button></div>'+
                                          '<div data-role="content" >'+
                                          '<h2 id="titulo_registro_' + cf + '_0"></h2>'+
                                          '<h3 id="subtitulo_registro_' + cf + '_0"></h2>'+
                                          '<div style="margin: 0px auto; text-align: center;"><img class="ima_tam" id="imagen_' + cf + '_0" src="" /></div>'+
                                          '<div id="divContenido_' + cf + '_0"></div>'+
                                          '<div id="divLink_' + cf + '_0"></div>'+
                                          '</div>');  
                     }           
                     
                     $.ajax({
                            url: host + "control?$cmd=plain&$cf=" + cf + "&$ta=update&$pk=0",
                            dataType: ($.browser.msie) ? "text" : "xml",
                            success: function(data) {
                                if (typeof data == "string") {
                                    xmlRegistro = new ActiveXObject("Microsoft.XMLDOM");
                                    xmlRegistro.async = false;
                                    xmlRegistro.validateOnParse = "true";
                                    xmlRegistro.loadXML(data);
                                    if (xmlRegistro.parseError.errorCode > 0)
                                        alert("Error de compilación xml:" + xmlRegistro.parseError.errorCode + "\nParse reason:" + xmlRegistro.parseError.reason + "\nLinea:" + xmlRegistro.parseError.line);
                                }
                                else
                                    xmlRegistro = data;

                                $("#titulo_header_"+ cf + "_0").text($(xmlRegistro).find("configuracion").find("forma").text());
                                $("#divContenido_" + cf + "_0").form_mobile_queue({app: 1,
                                    forma: cf,
                                    modo: "insert",
                                    columnas: 1,
                                    pk: 0,
                                    filtroForaneo: "2=clave_aplicacion=1"+parametros,
                                    height: "500",
                                    width: "800px",
                                    originatingObject: "",
                                    updateControl: "lvForaneo_" + cf
                                });

                                $.mobile.changePage('#registro_' + cf + '_0', {transition: "flip",role: "dialog"});
                                
                            }
                        });                  
                }); 
                
                if (instrucciones != "") {
                    $("#dlgModal_" + formSuffix).html($("#dlgModal_" + formSuffix).html().replace("<!-- Instrucciones -->", "<p><strong>" + instrucciones + "</strong></p>"));
                }

                //Se extrae posibles escenarios que se podrían disparar al guardar la forma, 
                //dependiendo del valor del campo de seguimiento
                // Esto sólo aplica cuando el modo de la forma es Insert o Update
                if ($("#formTab_" + formSuffix).attr("modo") != "lookup") {

                    var sReportes = "";
                    reportes = $(xml).find("reportes");
                    $.each(reportes, function() {
                        sReportes += $(this).find('reporte').text() +
                                '|' + $(this).find('reporte').attr('id') + "||";
                    });

                    $("#_r").val(sReportes);
                }

                //Aplica el codigo proveniente del XML y que aplica en la forma
                evento = $(xml).find('configuracion_forma').find('evento_forma').text();
                if (evento != "" && evento != "null")
                    try { $.globalEval(evento);} 
                    catch(err) { /*$("#pPopup_" + formSuffix).html(err.message).parent().popup( "open" );*/}

                //Ahora carga los eventos relacionados con los campos
                for (i = 0; i < $.fn.form_mobile.options.events.length; i++) {
                    if ($.fn.form_mobile.options.events[i] != undefined && $.fn.form_mobile.options.events[i] != "")
                        try { $.globalEval($.fn.form_mobile.options.events[i]); } 
                        catch(err) { /*$("#pPopup_" + formSuffix).html(err.message).parent().popup( "open" );*/}
                }

                //Establece atributo de seguridad
                $("#formTab_" + formSuffix).attr("security", sPermiso);

                //Inicializa los select
                //$("select").selectmenu().selectmenu("refresh", true);
                //Borra la leyenda del grid                
                var oForm = $("#form_" + formSuffix);

                //Se asigna evento al botón de guardar
                $("#btnEliminar_" + formSuffix).button().click(function() {
                    nApp = this.id.split("_")[1];
                    nForma = this.id.split("_")[2];
                    nPK = this.id.split("_")[3];

                    if (confirm("¿Desea eliminar el actual registro?")) {
                         $.post("control","$cmd=register&$ta=delete&$cf=" + nForma + "&$pk="+nPK);
                         $('#registro_' + nForma + '_' + nPK).dialog("close");
                         
                         if (nForma=="382") {
                             $("#evento_" + nForma + "_" + nPK).remove();
                         } else {
                             $($("#lnkRegister_" + nForma + "_" + nPK).parent().parent().parent()).remove();
                         }
                         
                    }
                });
                
                $("#btnGuardar_" + formSuffix).button().click(function() {
                    setTimeout(function(){$.mobile.loading('show');},1);
                    nApp = this.id.split("_")[1];
                    nForma = this.id.split("_")[2];
                    nPK = this.id.split("_")[3];
                    formSuffix = this.id.split("_")[1] + "_" + this.id.split("_")[2] + "_" + this.id.split("_")[3];

                    $("#btnGuardar_" + formSuffix).disabled = true;
                    //Actualiza el estatus bar
                    $("#tdEstatus_" + formSuffix).html("<img src='img/throbber.gif'>&nbsp;Validando informacion...");

                    // inside event callbacks 'this' is the DOM element so we first 
                    // wrap it in a jQuery object and then invoke ajaxSubmit 
                    //Le quita el formato a los campos tipo money
                    oForm.find('input').each(function() {
                        if ($(this).attr("tipo_dato") == "money" || $(this).attr("tipo_dato") == "decimal") {
                            $(this).val($(this).val().replace(/,/g, "").replace(/\$/g, ""));
                        }
                    });

                    var options = {
                        beforeSubmit: validateForm, // pre-submit callback 
                        success: processXml, // post-submit callback 
                        dataType: ($.browser.msie) ? "text" : "xml",
                        url: "control?$cmd=register&$ta=" + $("#formTab_" + formSuffix).attr("modo"), // override for form's 'action' attribute 
                        error: function(xhr, err) {
                            setTimeout(function(){$.mobile.loading('hide');},300);
                            if (xhr.responseText.indexOf("Iniciar sesi&oacute;n") > -1) {
                                alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                                window.location = 'index.html';
                            } else {
                                $("#dlgModal_" + formSuffix).remove();
                                alert("Error al guardar registro: " + xhr.readyState + "\nstatus: " + xhr.status + "\nResponseText:" + xhr.responseText);
                            }
                        }
                    };

                    oForm.ajaxSubmit(options);
                    setTimeout(function(){$.mobile.loading('hide');},300);
                    // !!! Important !!! 
                    // always return false to prevent standard browser submit and page navigation 
                    return false;
                });

                $("#btnBuscar_" + formSuffix).button().click(function() {
                    setTimeout(function(){$.mobile.loading('show');},1);
                    nApp = this.id.split("_")[1];
                    nForma = this.id.split("_")[2];
                    nPK = this.id.split("_")[3];
                    formSuffix = this.id.split("_")[1] + "_" + this.id.split("_")[2] + "_" + this.id.split("_")[3];

                    $("#btnBuscar_" + formSuffix).disabled = true;
                    //Actualiza el estatus bar
                    $("#tdEstatus_" + formSuffix).html("<img src='img/throbber.gif'>&nbsp;Validando informacion...");

                    // inside event callbacks 'this' is the DOM element so we first 
                    // wrap it in a jQuery object and then invoke ajaxSubmit 
                    //Le quita el formato a los campos tipo money
                    oForm.find('input').each(function() {
                        if ($(this).attr("tipo_dato") == "money" || $(this).attr("tipo_dato") == "decimal") {
                            $(this).val($(this).val().replace(/,/g, "").replace(/\$/g, ""));
                        }
                    });

                    var options = {
                        beforeSubmit: validateForm, // pre-submit callback 
                        success: processXml, // post-submit callback 
                        dataType: ($.browser.msie) ? "text" : "xml",
                        url: "control?$cmd=register&$ta=" + $("#formTab_" + formSuffix).attr("modo"), // override for form's 'action' attribute 
                        error: function(xhr, err) {
                            setTimeout(function(){$.mobile.loading('hide');},300);
                            if (xhr.responseText.indexOf("Iniciar sesi&oacute;n") > -1) {
                                alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                                window.location = 'index.html';
                            } else {
                                $("#dlgModal_" + formSuffix).remove();
                                alert("Error al guardar registro: " + xhr.readyState + "\nstatus: " + xhr.status + "\nResponseText:" + xhr.responseText);
                            }
                        }
                    };

                    oForm.ajaxSubmit(options);
                    setTimeout(function(){$.mobile.loading('hide');},300);
                    // !!! Important !!! 
                    // always return false to prevent standard browser submit and page navigation 
                    return false;
                });
                
                // Se ocultan los mensajes de validación
                $(".mensajeobligatorio").hide();

                /*$(".money").calculator({
                    useThemeRoller: true,
                    prompt: 'Calculadora',
                    showOn: 'operator',
                    isOperator: mathsOnly
                }).focus(function() {
                    $(this).val($(this).val().replace(/,/g, "").replace(/\$/g, ""));

                    if ($(this).val() === "0" || $(this).val() === "0.0" || $(this).val() === "0.00") {
                        $(this).val("");
                    }
                }).blur(function() {
                    $(this).val(formatCurrency($(this).val()));
                });*/

                function mathsOnly(ch, event, value, base, decimalChar) {
                    return '+-*/'.indexOf(ch) > -1 && !(ch == '-' && value == '');
                }

                //Se activa el foreign toolbar para editar registros foraneos
                oForm.find('.widgetbutton').fieldtoolbar({
                    app: $.fn.form_mobile.options.app
                });
                
               //$('#registro_' + $.fn.form_mobile.options.forma + '_' + $.fn.form_mobile.options.pk).page('destroy').page();
                
                function validateForm(formData, jqForm, options) {
                    var bCompleto = true;

                    $(jqForm[0]).find('.obligatorio').each(function() {

                        if (this.id != "" && this.id != undefined) {

                            if ($.trim(this.value) == "" && $(this).attr("type") != "checkbox") {
                                $("#div_" + this.name).addClass("errorencampo")
                                $(this).addClass("errorencampo");
                                $("#msgvalida_" + this.name).show();
                                bCompleto = false;
                            }
                            else if ($(this).attr("type") == "checkbox" && !this.checked) {
                                $("#div_" + this.name).addClass("errorencampo")
                                $(this).addClass("errorencampo");
                                $("#msgvalida_" + this.name).show();
                                bCompleto = false;
                            }
                            else {
                                $("#td_" + this.name).removeClass("errorencampo")
                                $("#msgvalida_" + this.name).hide();
                                $(this).removeClass("errorencampo");
                            }
                        }
                    });

                    if (!bCompleto) {
                       alert("Falta dato obligatorio, verifique");
                        return false;
                    }
                    else {
                        $("#tdEstatus_" + formSuffix).html("<img src='img/throbber.gif'>&nbsp;Enviando informaci&oacute;n...");
                        return true;
                    }
                }

                function processXml(data) {
                    // 'responseXML' is the XML document returned by the server; we use 
                    // jQuery to extract the content of the message node from the XML doc 
                    if (typeof data == "string") {
                        xmlResult = new ActiveXObject("Microsoft.XMLDOM");
                        xmlResult.async = false;
                        xmlResult.validateOnParse = "true";
                        xmlResult.loadXML(data);
                        if (xmlResult.parseError.errorCode > 0) {
                            alert("Error de compilaci&oacute;n xml:" + xmlResult.parseError.errorCode + "\nParse reason:" + xmlResult.parseError.reason + "\nLinea:" + xmlResult.parseError.line);
                        }
                    }
                    else {
                        xmlResult = data;
                    }

                    var error = $(xmlResult).find("error");

                    if (error.length > 0) {
                        setTimeout(function(){$.mobile.loading('hide');},300);
                        $.fn.form_mobile.options.error = "Ocurri&oacute; un problema al guardar el registro: " +
                                error.text() + ".";


                        alert($.fn.form_mobile.options.error);

                        return false;
                    }

                    var warning = $(xmlResult).find("warning");
                    if (warning.length > 0) {
                        for (i = 0; i < warning.length; i++) {
                            alert($($(warning)[i]).text());
                        }

                    }

                    var nApp = $("#formTab_" + formSuffix).attr("app")
                    var nForma = $("#formTab_" + formSuffix).attr("forma");
                    var nPK = $("#formTab_" + formSuffix).attr("pk")
                    sResultado = $($(xmlResult).find("pk")[0]).text();

                    //Se transforma un campo en comentario
                    if ($("#_tcc").val() != "" && $("#_tcc").val() != undefined) {
                        postConfig = "titulo=" + $("#commentForm_" + formSuffix).find("#titulo").val() + " - " + $("#" + $("#_tcc").val()).parent().prev().html().replace(" (*)", "") + "&mensaje=" + $("#" + $("#_tcc").val()).val() + "&clave_forma=" + $("#commentForm_" + formSuffix).find("#clave_forma").val() + "&clave_registro=" + $("#commentForm_" + formSuffix).find("#clave_registro").val() + "&clave_empleado=" + $("#commentForm_" + formSuffix).find("#clave_empleado").val();
                        $.post("control?$cmd=register&$cf=273&$ta=insert", postConfig);
                    }

                    //Genera reportes
                    if ($("#_r").val() != "") {
                        var caracteristicas = "height=800,width=800,scrollTo,resizable=1,scrollbars=1,location=0";
                        aReportes = $("#_r").val().split("||");
                        for (var i = 0; i < aReportes.length; i++) {
                            if (aReportes[i] != "")
                                window.open('control?$cmd=report&$cr=' + aReportes[i].split("|")[1] + "&$pk=" + sResultado, "_blank", caracteristicas);
                        }
                    }

                    //Verifica el tipo de control por actualizar
                    sControl = $("#formTab_" + formSuffix).attr("updateControl");

                    if (sControl != "") {
                        oControl = $("#" + sControl);
                        if (oControl.length > 0) {
                            if (sControl == "lv") {
                               loadXML();
                            } else if (sControl == "calendar") {
                                /*var source = {
                                            url: 'events.jsp',
                                            type: 'GET'
                                      }
                                $('#calendar').fullCalendar('removeEvents');
                                $('#calendar').fullCalendar('addEventSource', source );
                                $('#calendar').fullCalendar('rerenderEvents');
                                $('#calendar').fullCalendar('removeEventSource', source );*/
                                setTimeout(function(){
                                    $('#calendar').fullCalendar('rerenderEvents');
                                    $('#calendar').fullCalendar('refetchEvents'); }
                                    ,1000);
                                
                            } else if (oControl[0].className.indexOf("ui-listview")>-1) {
                                //loadXMLinAnyListview(sControl, "", nForma, "");
                                $('#foraneo_' + sControl.split("_")[1]).trigger('expand');
                                //$("#listview").trigger('create') //.listview('refresh') */
                            } else if (sControl == "portletGrupo") {
                                $("#portletGrupo").portletgroup();
                            } else if (sControl == "portletDocumento") {
                                $("#portletDocumento").portletdocument();
                            } else if (oControl[0].nodeName == "DIV") {
                                $("#grid_" + gridSuffix).jqGrid().trigger("reloadGrid");
                            } else if (oControl[0].nodeName == "TABLE") {
                                // Refresca el grid de pendientes
                                oControl.jqGrid().trigger("reloadGrid");
                            }
                            /* en caso de que no lo sea actualiza un combo*/
                            else
                            if ($("#" + sControl).length > 0) {
                                setXMLInSelect3(sControl, $("#formTab_" + formSuffix).attr("updateForeignForm"), 'foreign', null)
                            }
                        }
                    }

                    //Cierra el dialogo
                    //window.history.go(-1);
                    $('#registro_' + $.fn.form_mobile.options.forma + '_' + $.fn.form_mobile.options.pk).dialog("close");
                    //;
                    setTimeout(function(){$.mobile.loading('hide');},300);
                }

                $(".comentario").comments_mobile({
                    formSuffix: formSuffix,
                    claveForma: $.fn.form_mobile.options.forma,
                    claveRegistro: $.fn.form_mobile.options.pk,
                    titulo: "",
                    mensaje: ""
                }).removeClass("comentario");
                
                $.mobile.changePage('#registro_' + $.fn.form_mobile.options.forma + '_' + $.fn.form_mobile.options.pk, {transition: "flip", role: "page"});
                //Cierra el dialogo de espera
                setTimeout(function(){$.mobile.loading('hide');},300);
                $("#_status_").val("");

            }, // Termina llamada del ajax
            error: function(xhr, err) {
                if (xhr.responseText.indexOf("Iniciar sesi&oacute;n") > -1) {
                    alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                    window.location = 'index.html';
                } else {
                    $("#dlgModal_" + formSuffix).remove();

                    //Cierra el dialogo de espera
                    setTimeout(function(){$.mobile.loading('hide');},300);

                    alert("Error al recuperar forma: " + xhr.readyState + "\nstatus: " + xhr.status + "\responseText:" + xhr.responseText);
                }

                $("#_status_").val("");
            }
        });

    };

})(jQuery);