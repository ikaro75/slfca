/* 
 * Plugin de jQuery para cargar forma a través de un plugin
 * 
 */
(function($) {
    $.fn.form = function(opc) {

        $.fn.form.settings = {
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
            $.fn.form.options = $.extend($.fn.form.settings, opc);
            obj = $(this);
            $.fn.form.getGUI(obj);
        });

    };


    $.fn.form.getGUI = function(obj) {
        $("#_status_").val("Iniciando forma");
        //Crea clave unica para forma
        var formSuffix = $.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        var sDialogo = "";
        var sMainDivTabs = "";
        var sDivTabs = "";
        var sUlTabs = "";
        var sBotonera = "";
        //1. Primero crear el HTML necesario para contruir la interfaz de las relaciones

        sMainDivTabs = "<div id='formTab_" + formSuffix + "' security='" +
                "' datestamp='" + $.fn.form.options.datestamp +
                "' app='" + $.fn.form.options.app +
                "' forma='" + $.fn.form.options.forma +
                "' pk='" + $.fn.form.options.pk +
                "' pk_name='" + $.fn.form.options.pk_name +
                "' modo='" + $.fn.form.options.modo +
                "' originatingObject='" + $.fn.form.options.originatingObject +
                "' updateControl='" + $.fn.form.options.updateControl +
                "' updateForeignForm='" + $.fn.form.options.updateForeignForm +
                "' permiteDuplicarRegistro='" + $.fn.form.options.permiteDuplicarRegistro +
                "' permiteInsertarComentarios='" + $.fn.form.options.permiteInsertarComentarios +
                "' alias_log='" +
                "' >";

        var sBusqueda = "";
        if ($.fn.form.options.modo != 'lookup') {
            sTituloTab = "General";
            sButtonCaption = 'Guardar';
        }
        else {
            sTituloTab = "Seleccione los criterios de b&uacute;queda";
            sButtonCaption = 'Buscar'
            sBusqueda = "<tr><td class='etiqueta_forma1' style='width:50%'>Guardar filtro como: </td><td class='etiqueta_forma1'><input name='$b' id='$b' value='' class='singleInput' /></td></tr>";
        }

        sUlTabs += "<ul><li><a href='#divFormGeneral_" + formSuffix + "'>" + sTituloTab + "</a></li><!-- UlTabsForaneos --></ul>";
        sDivTabs += "<div id='divFormGeneral_" + formSuffix + "' >" +
                "<div align='center'><br /><br />Cargando informaci&oacute;n... <br /> <br />" +
                "<img src='img/loading.gif' />" +
                "</div><!-- DivTagForaneos -->" + 
                "</div><!-- DivTabsForaneos -->";

        sBotonera += "<div align='right' style='clear:left'><!-- Comentarios --><table style='width:100%'>" + sBusqueda + "<tr><td align='left' id='tdEstatus_" + formSuffix + "' class='estatus_bar'>&nbsp;</td><td align='right'>";
        /*if ($.fn.form.options.modo=="update" && $.fn.form.options.permiteDuplicarRegistro=="true")        
         sBotonera+="<input type='button' class='formButton' id='btnDuplicar_" + formSuffix +"' value='Duplicar' /></td></tr></table></div>";*/

        sBotonera += "<div><input type='button' style='float: right;' class='formButton' id='btnGuardar_" + formSuffix + "' value='" + sButtonCaption + "' /><!-- InicioOpciones --><!-- GuardarYReiniciar --><!-- Duplicar --><!-- GuardarComoPlantilla --><!-- AplicarPlantilla --><!-- InsertarComentario --><!-- Eliminar --><!-- FinOpciones --></div></td></tr></table></div>";

        sMainDivTabs += sUlTabs + sDivTabs + sBotonera + "</div>";
        sDialogo += "<div id='dlgModal_" + formSuffix + "' title='" + $.fn.form.options.titulo + "'>" + sMainDivTabs + "</div>";
        obj.append(sDialogo);
        $.fn.form.setFormObjects();

    };

    $.fn.form.setFormObjects = function() {

        var formSuffix = $.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        var gridSuffix = $.fn.form.options.app + "_" + $.fn.form.options.forma + "_0";// + $.fn.form.options.datestamp;
        $("#_status_").val("Cargando datos de la forma");
        
        $.ajax(
                {
                    url: $.fn.form.options.xmlUrl + "&$cf=" + $.fn.form.options.forma + "&$pk=" + $.fn.form.options.pk + "&$ta=" + $.fn.form.options.modo + "&1=clave_aplicacion=" + $.fn.form.options.pk + "&" + $.fn.form.options.filtroForaneo,
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
                        var oError = $(xml).find("error");
                        if (oError.length > 0) {
                            var sDescripcionError = oError.text();
                            $("#grid_" + gridSuffix + "_toppager_right").children(0).html(sDescripcionError);
                            $("#dlgModal_" + formSuffix).remove();
                            if (sDescripcionError.indexOf("No hay consulta definida para la forma", 0) > -1 && $("#_cp_").val() == "1") {
                                if (confirm("No hay una consulta establecida para dicha función, ¿desea configurarla?"))
                                    $("body").formqueue({
                                        app: 1,
                                        forma: 8,
                                        datestamp: $.fn.form.options.datestamp,
                                        modo: "insert",
                                        columnas: 1,
                                        pk: 0,
                                        filtroForaneo: "2=clave_aplicacion=1&3=clave_forma=" + $.fn.form.options.forma + "&4=clave_perfil=1&5=clave_forma=" + $.fn.form.options.forma + "&6=tipo_accion='lookup'",
                                        height: "500",
                                        width: "500",
                                        originatingObject: "",
                                        showRelationships: false
                                    });
                            }
                            else
                                alert(sDescripcionError);

                            $("#divwait").dialog("close");
                            $("#_status_").val("");
                            return false;
                        }


                        $.fn.form.options.showRelationships = $(xml).find("configuracion_forma").find("muestra_formas_foraneas").text();
                        if ($.fn.form.options.showRelationships == "true" && $("#formTab_" + formSuffix).attr("modo") == 'update') {
                            if ($(xml).find("configuracion_forma").find("clave_tipo_presentacion_forma_foranea").text() == "1") {
                                oTabsForaneos = $(xml).find("clave_forma_foranea");
                                sUlTabs = "";
                                sDivTabs = "";
                                oTabsForaneos.each(function() {
                                    sUlTabs += "<li><a id='lnkFormTab_" +  $.fn.form.options.app + "_" + $(this).text() + "' href='#formTab_" + $.fn.form.options.app + "_" + $(this).text() + "'>" + $(this).next().text() + "</a></li>";
                                    sDivTabs += "<div id='formTab_" + $.fn.form.options.app + "_" + $(this).text() + "'>" +
                                            "<div id='formGrid_" + $.fn.form.options.app + "_" + $(this).text() +
                                            "' app='" + $.fn.form.options.app +
                                            "' forma='" + $(this).text() +
                                            "' titulo='" + $(this).next().text() +
                                            "' leyendas='" +
                                            "' align='center' class='queued_grids'>" +
                                            "<br /><br />Cargando informaci&oacute;n... <br /> <br />" +
                                            "<img src='img/loading.gif' />" +
                                            "</div>" +
                                            "</div>";

                                });
                                $("#dlgModal_" + formSuffix)
                                $("#dlgModal_" + formSuffix).html($("#dlgModal_" + formSuffix).html().replace("<!-- UlTabsForaneos -->", sUlTabs).replace('<!-- DivTabsForaneos -->', sDivTabs));
                            } 
                        }

                        formSuffix = $.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
                        $.fn.form.options.permiteDuplicarRegistro = $(xml).find("permite_duplicar_registro").text();
                        $.fn.form.options.permiteInsertarComentarios = $(xml).find("permite_insertar_comentarios").text();
                        $.fn.form.options.permiteGuardarComoPlantilla = $(xml).find("permite_guardar_como_plantilla").text();
                        /* Procesamiento de permisos */
                        var sPermiso = "";
                        var oPermisos = $(xml).find("clave_permiso");
                        oPermisos.each(function() {
                            sPermiso += $(this).text() + ",";
                        })
                        sPermiso = sPermiso.substr(0, sPermiso.length - 1);

                        if (sPermiso.indexOf("2") == -1 && $("#formTab_" + formSuffix).attr("modo") == 'insert') {
                            alert("Su perfil no cuenta con permisos para insertar registros de esta forma, consulte al administrador del sistema");
                            $("#grid_" + gridSuffix + "_toppager_right").children(0).html("");
                            //Cierra el dialogo de espera
                            $("#divwait").dialog("close");
                            $("#_status_").val("");
                            return false;
                        }

                        if (sPermiso.indexOf("3") == -1 && $("#formTab_" + formSuffix).attr("modo") == 'update') {
                            alert("Su perfil no cuenta con permisos para actualizar registros de esta forma, consulte al administrador del sistema");
                            $("#grid_" + gridSuffix + "_toppager_right").children(0).html("");
                            //Cierra el dialogo de espera
                            $("#divwait").dialog("close");
                            $("#_status_").val("");
                            return false;
                        }

                        //Se extraen datos generales de la forma
                        sAliasLog = $(xml).find("configuracion_forma").find("alias_tab").text()
                        $("#formTab_" + formSuffix).attr("alias_log", sAliasLog);
                        $("#formTab_" + formSuffix).attr("pk_name", $(xml).find("configuracion_forma").find("llave_primaria").text());

                        sTitulo = "";
                        if (sAliasLog.split(' ').length > 1) {
                            if (sAliasLog.split(' ')[0] == 'la' &&
                                    $("#formTab_" + formSuffix).attr("modo") == 'insert')
                                sTitulo = "Nueva " + sAliasLog.split(' ')[1];

                            if (sAliasLog.split(' ')[0] == 'el' &&
                                    $("#formTab_" + formSuffix).attr("modo") == 'insert')
                                sTitulo = "Nuevo " + sAliasLog.split(' ')[1];

                            if ($("#formTab_" + formSuffix).attr("modo") == 'update')
                                sTitulo = "Edici&oacute;n de " + sAliasLog.split(' ')[1];
                        }


                        //Si se permite duplicar el registro actual
                        if ($.fn.form.options.modo == "update" && $.fn.form.options.permiteDuplicarRegistro == "true") {
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- InicioOpciones -->', '<div><input type="button" class="formButton" id="opciones_' + formSuffix + '" value="Opciones" /><input type="button" class="formButton" id="btnMenuOpciones_' + formSuffix + '" value="" /></div><ul>'));
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- Duplicar -->', '<li><a id="btnDuplicar_' + formSuffix + '">Duplicar</a></li>'));
                        }

                        //Si se puede guardar como plantilla y aplicarlas
                        if ($.fn.form.options.modo == "update" && $.fn.form.options.permiteGuardarComoPlantilla == "true") {
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- InicioOpciones -->', '<div><input type="button" class="formButton" id="opciones_' + formSuffix + '" value="Opciones" /><input type="button" class="formButton" id="btnMenuOpciones_' + formSuffix + '" value="" /></div><ul>'));
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- GuardarComoPlantilla -->', '<li><a id="btnGuardarComoPlantilla_' + formSuffix + '" >Guardar como plantilla...</a></li>'));
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- AplicarPlantilla -->', '<li><a id="btnAplicarPlantilla_' + formSuffix + '" >Aplicar plantilla...</a></li>'));
                        }

                        if ($.fn.form.options.modo == "insert" && $.fn.form.options.permiteGuardarComoPlantilla == "true") {
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- InicioOpciones -->', '<div><input type="button" class="formButton" id="opciones_' + formSuffix + '" value="Opciones" /><input type="button" class="formButton" id="btnMenuOpciones_' + formSuffix + '" value="" /></div><ul>'));
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- AplicarPlantilla -->', '<li><a id="btnAplicarPlantilla_' + formSuffix + '" >Aplicar plantilla...</a></li>'));
                        }

                        //Verifica si tiene permisos para eliminar
                        if ($.fn.form.options.modo == "update" && sPermiso.indexOf("3") > -1) {
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- InicioOpciones -->', '<div><input type="button" class="formButton" id="opciones_' + formSuffix + '" value="Opciones" /><input type="button" class="formButton" id="btnMenuOpciones_' + formSuffix + '" value="" /></div><ul>'));
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- Eliminar -->', '<li><a id="btnEliminar_' + formSuffix + '" >Eliminar</a></li>'));
                        }

                        //Verifica si se construyó menu de opciones
                        if ($("#formTab_" + formSuffix).html().indexOf("<!-- InicioOpciones -->") == -1) {
                            sHTML = $("#formTab_" + formSuffix).html();
                            $("#formTab_" + formSuffix).html(sHTML.replace('<!-- FinOpciones -->', '</ul>'));
                        }

                        $("#formTab_" + formSuffix).html($("#formTab_" + formSuffix).html() + '<input type="button" class"formButton"');

                        if ($.fn.form.options.modo == "update" && $.fn.form.options.permiteInsertarComentarios == "true") {
                            notas = $(xml).find("notas_forma").find("nota");
                            htmlForNotes = "";
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

                            $("#formTab_" + formSuffix).html($("#formTab_" + formSuffix).html().replace('<!-- Comentarios -->', '<h2 style="margin:15px; text-align: left">Comentarios</h2>' + htmlForNotes + '<div class="comentario"></div>'));
                            $(".comentario").comments({
                                formSuffix: formSuffix,
                                claveForma: $.fn.form.options.forma,
                                claveRegistro: $.fn.form.options.pk,
                                titulo: "",
                                mensaje: ""
                            });
                        }

                        //Se genera el HTML de la forma general 
                        $("#divFormGeneral_" + formSuffix).html($.fn.form.handleForm(xml));

                        instrucciones = $(xml).find("configuracion_forma").find("instrucciones").text();

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
                            $.globalEval(evento);

                        //Ahora carga los eventos relacionados con los campos
                        for (i = 0; i < $.fn.form.options.events.length; i++) {
                            if ($.fn.form.options.events[i] != undefined && $.fn.form.options.events[i] != "")
                                $.globalEval($.fn.form.options.events[i]);
                        }

                        //Establece atributo de seguridad
                        $("#formTab_" + formSuffix).attr("security", sPermiso);

                        //Borra la leyenda del grid                
                        $("#grid_" + gridSuffix + "_toppager_right").children(0).html("");
                        var oForm = $("#form_" + formSuffix);

                        //Activa los tooltips para ayuda 
                        $(".tooltipField").tooltip({
                            bodyHandler: function() {
                                return $(this).attr("ayuda");
                            },
                            showURL: false,
                            extraClass: "pretty",
                            fixPNG: true
                        });

                        //Se crea control
                        $("#opciones_" + formSuffix)
                                .button()
                                .click(function() {
                            alert("Seleccione una opción del menú");
                        })
                                .next()
                                .button({
                            text: false,
                            icons: {
                                primary: 'ui-icon-triangle-1-s'
                            }
                        })
                                .click(function() {
                            var menu = $(this).parent().next().show().position({
                                my: "left bottom",
                                at: "left top",
                                of: this
                            });

                            $(document).one("click", function() {
                                menu.hide();
                            });
                            return false;
                        })
                                .parent()
                                .buttonset()
                                .next()
                                .hide()
                                .menu();

                        //Se asigna evento al botón de guardar
                        $("#btnGuardar_" + formSuffix).button().click(function() {
                            nApp = this.id.split("_")[1];
                            nForma = this.id.split("_")[2];
                            nPK = this.id.split("_")[3];
                            formSuffix = this.id.split("_")[1] + "_" + this.id.split("_")[2] + "_" + this.id.split("_")[3];

                            $("#btnGuardar_" + formSuffix).disabled = true;
                            //Actualiza el estatus bar
                            $("#tdEstatus_" + formSuffix).html("<img src='img/throbber.gif'>&nbsp;Validando informacion...");

                            // inside event callbacks 'this' is the DOM element so we first 
                            // wrap it in a jQuery object and then invoke ajaxSubmit 
                            if ($("#formTab_" + formSuffix).attr("modo") != "lookup") {

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
                                        if (xhr.responseText.indexOf("Iniciar sesi&oacute;n") > -1) {
                                            alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                                            window.location = 'login.jsp';
                                        } else {
                                            $("#grid_" + gridSuffix + "_toppager_right").children(0).html("Error al guardar registro");
                                            $("#dlgModal_" + formSuffix).remove();
                                            alert("Error al guardar registro: " + xhr.readyState + "\nstatus: " + xhr.status + "\nResponseText:" + xhr.responseText);
                                        }
                                    }
                                    //type:      type        // 'get' or 'post', override for form's 'method' attribute 
                                    //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
                                    //clearForm: true        // clear all form fields after successful submit 
                                    //resetForm: true        // reset the form after successful submit 

                                    // $.ajax options can be used here too, for example: 
                                    //timeout:   3000 
                                };

                                oForm.ajaxSubmit(options);

                                // !!! Important !!! 
                                // always return false to prevent standard browser submit and page navigation 
                                return false;
                            }
                            else {
                                //Valida que traiga al menos un dato:
                                sData = "";
                                oCampos = oForm.serializeArray();
                                $.each(oCampos, function(i, oCampo) {
                                    sTipoDato = $(document.getElementById(oCampos[i].name)).attr("tipo_dato");
                                    sNombreCampo = oCampo.name.replace("_" + formSuffix, "");
                                    if ($.trim(oCampo.value) != "" &&
                                            sNombreCampo != "$ta" &&
                                            sNombreCampo != "$ca" &&
                                            sNombreCampo != "$cf" &&
                                            sNombreCampo != "$pk")
                                        if (sTipoDato == "varchar" || sTipoDato == "text")
                                            sData += sNombreCampo + " like '" + oCampo.value + "%'&";
                                        else if (sTipoDato == 'smalldatetime' || sTipoDato == 'datetime' || sTipoDato == 'date') {
                                            aFecha = oCampo.value.split("/");
                                            sData += sNombreCampo + "='" + aFecha[2] + "/" + aFecha[1] + "/" + aFecha[0] + "'&";
                                        }
                                        else
                                            sData += sNombreCampo + "=" + oCampo.value + "&";
                                });


                                if (sData == "") {
                                    alert("Es necesario especificar al menos un criterio de búsqueda, verifique");
                                    $("#tdEstatus_" + formSuffix).html(" Es necesario especificar al menos un criterio de b&uacute;squeda, verifique");
                                }
                                else {
                                    sData = sData.substring(0, sData.length - 1).replace("&", " AND ");
                                    oGridHeader = $("#grid_" + gridSuffix).parent().parent().parent().find("span.ui-jqgrid-title");
                                    //oMenuAccordion=$("#grid_"+gridSuffix).parent().parent().parent().parent().parent().parent().parent().parent().prev().prev().children().children();
                                    sId = $("#grid_" + gridSuffix).parent().parent().parent().parent().parent()[0].id;
                                    if (sId.split("_")[4] != undefined) {
                                        filterPortletId = "#filterPortlet_" + sId.split("_")[1] + "_" + sId.split("_")[2] + "_" + sId.split("_")[3] + "_" + sId.split("_")[4];
                                    }
                                    else {
                                        filterPortletId = "#filterPortlet_" + sId.split("_")[1] + "_" + sId.split("_")[2] + "_" + sId.split("_")[3];
                                    }

                                    oFilterPortlet = $(filterPortletId).children()[1];

                                    nAplicacion = oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[2];
                                    nForma = oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[3];
                                    sDateStamp = oGridHeader[0].parentNode.parentNode.parentNode.id.split("_")[4];
                                    $(oGridHeader[0]).append("&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid_" + nAplicacion + "_" + nForma + "_" + sDateStamp + "'>(Quitar filtro)</a>");

                                    //Verifica URL anterior del grid filtrado
                                    var previousWhere = "";
                                    var previusUrl = $("#grid_" + gridSuffix)[0].p.url;
                                    if (previusUrl > -1)
                                        ;
                                    previousWhere = previusUrl.split("&$w=")[1];


                                    //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                                    $("#lnkRemoveFilter_grid_" + gridSuffix).click(function() {
                                        var sGridId = "#grid_" + gridSuffix;
                                        if ($(sGridId).attr("requeriesFilter") == "1") {
                                            $("body").form({
                                                app: nApp,
                                                forma: nEntidad,
                                                datestamp: gridSuffix.split("_")[2],
                                                modo: "lookup",
                                                titulo: "Filtrado de registros",
                                                columnas: 1,
                                                height: "500",
                                                width: "80%",
                                                pk: 0,
                                                originatingObject: sGridId
                                            });
                                        }
                                        else
                                            $(sGridId).jqGrid('setGridParam', {
                                                url: previusUrl
                                            }).trigger("reloadGrid");

                                        $(this).remove();
                                    });

                                    // Si el usuario le dió un nombre a la consulta
                                    // Significa que la desea guardar
                                    //sData=escape(sData.substring(0,sData.length-1).replace("&"," AND "));
                                    if (previousWhere == "") {
                                        sData = escape(sData);
                                    }
                                    else {
                                        sData = escape(previousWhere + " AND " + sData);
                                    }

                                    if (document.getElementById("$b").value != "") {
                                        sBusqueda = document.getElementById("$b").value;
                                        postConfig = "$cf=93&$ta=insert&$pk=0" +
                                                "&clave_aplicacion=" + $.fn.form.options.app +
                                                "&clave_forma=" + $.fn.form.options.forma +
                                                "&clave_empleado=" + $("#_ce_").val() +
                                                "&filtro=" + escape(sBusqueda) +
                                                "&consulta=" + sData;
                                        $.post("control?$cmd=register&$ta=insert", postConfig);

                                        // Aqui va método del filtro para actualizarlo
                                        $("filterPortlet_" + gridSuffix).portletFilter(
                                                {
                                                    app: nApp,
                                                    form: nForma,
                                                    register: nPK
                                                })
                                    }

                                    $("#grid_" + gridSuffix).jqGrid('setGridParam', {
                                        url: "control?$cmd=grid&$cf=" + $.fn.form.options.forma + "&$ta=select&$w=" + sData + "&$dp=body&page=1"
                                    }).trigger("reloadGrid")
                                    $("#dlgModal_" + formSuffix).dialog("destroy");
                                    $("#dlgModal_" + formSuffix).remove();
                                    return false;
                                }
                            }

                        });

                        //Se asigna el evento al botón Duplicar 
                        $("#btnDuplicar_" + formSuffix).click(function() {
                            $.fn.form.options.modo = "duplicate";
                            $("#formTab_" + formSuffix).attr("modo", "duplicate");

                            oForm.find('input').each(function() {
                                if ($(this).attr("tipo_dato") == "money" || $(this).attr("tipo_dato") == "decimal") {
                                    $(this).val($(this).val().replace(/,/g, "").replace(/\$/g, ""));
                                }
                            });

                            $("#divwait")
                                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Duplicando registro...</p>")
                                    .attr('title', 'Espere un momento por favor')
                                    .dialog({
                                height: 140,
                                modal: true,
                                autoOpen: true,
                                closeOnEscape: false
                            });

                            var options = {
                                beforeSubmit: validateForm, // pre-submit callback 
                                success: processXml, // post-submit callback 
                                dataType: ($.browser.msie) ? "text" : "xml",
                                url: "control?$cmd=register&$ta=duplicate", // override for form's 'action' attribute 
                                error: function(xhr, err) {

                                    if (xhr.responseText.indexOf("Iniciar sesi&oacute;n") > -1) {
                                        alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                                        window.location = 'login.jsp';
                                    }

                                    $("#grid_" + gridSuffix + "_toppager_right").children(0).html("Error al duplicar registro");
                                    $("#dlgModal_" + formSuffix).remove();
                                    alert("Error al duplicar registro: " + xhr.readyState + "\nstatus: " + xhr.status + "\nResponseText:" + xhr.responseText);
                                }
                                //type:      type        // 'get' or 'post', override for form's 'method' attribute 
                                //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
                                //clearForm: true        // clear all form fields after successful submit 
                                //resetForm: true        // reset the form after successful submit 

                                // $.ajax options can be used here too, for example: 
                                //timeout:   3000 
                            };

                            oForm.ajaxSubmit(options);

                            // !!! Important !!! 
                            // always return false to prevent standard browser submit and page navigation 
                            return false;
                            //Se verifican permisos para insertar el proyecto
                            /*if ($("#formTab_" + formSuffix ).attr("security").indexOf("2")<0) {
                             alert('Su perfil no cuenta con permisos para insertar registros, verifique');
                             return;
                             }
                             
                             $.fn.form.options.modo="insert";
                             $("#formTab_" + formSuffix).attr("modo","insert");
                             // Se restauran valores predeterminados  
                             var oCampos= $(xml).find("registro").children();
                             oCampos.each(function(){
                             oCampo=$(this);
                             sValorPredeterminado=oCampo.find('valor_predeterminado').text();
                             if (sValorPredeterminado!="") {
                             $("#"+oCampo[0].nodeName).val((sValorPredeterminado!="")?eval(sValorPredeterminado):"");
                             }     
                             });      
                             
                             $("#ui-dialog-title-dlgModal_"+ formSuffix).text($("#ui-dialog-title-dlgModal_"+ formSuffix).text().replace("Edición de","Nuev@"));
                             //Se modifican el botón opciones para que queden de acuerdo al modo "insert"           
                             $("#opciones_"+ formSuffix).parent().next().find("li").each( function(){
                             if ($(this).text()=="Duplicar")
                             $(this).remove();
                             else if ($(this).text()=="Guardar como plantilla...")
                             $(this).remove();
                             else if ($(this).text()=="Eliminar") 
                             $(this).remove();
                             });
                             
                             ///Reglas especiales para aplicaciones específicas
                             if (formSuffix.split("_")[1]==234)
                             fx_duplica_sub_transacciones(formSuffix.split("_")[2]);
                             
                             if (formSuffix.split("_")[1]==268)
                             fx_duplica_cuestionario(formSuffix.split("_")[2]); */
                            //$("#btnGuardar_"+ formSuffix).click(); 
                        });

                        //Fuerza a que se haga scroll a la página
                        //location.href=location.href.replace(location.hash,"") +"#"+sDateTime(new Date());
                        $("html, body").animate({
                            scrollTop: $("#top").offset().top + "px"
                        }, {
                            duration: 0,
                            easing: "swing"
                        });
                        //$("html").scrollTop(-100);

                        //Se crea el diálogo con el HTML completo
                        $("#dlgModal_" + formSuffix).dialog({
                            modal: true,
                            title: sTitulo,
                            /*height:$.fn.form.options.height, */
                            top: document.body.scrollTop + 350,
                            width: $.fn.form.options.width,
                            open: function(event, ui) {
                                $(this).dialog("option", "position", "center");
                            },
                            close: function(event, ui) {
                                $(this).dialog("destroy");
                                $(this).remove();
                            }

                        });

                        //Se crean los tabs
                        $("#formTab_" + formSuffix).tabs();
                        $("#formTab_" + formSuffix).bind('tabsselect', function(e, ui) {

                            if (ui.index == 0)
                                return;

                            app = ui.panel.id.split("_")[1];
                            form = ui.panel.id.split("_")[2];
                            formSuffix = ui.panel.id.split("_")[1] + "_" + ui.panel.id.split("_")[2];
                            sGridId = "#formGrid_" + formSuffix;

                            /* Si ya se cargó el grid ya no es necesario volverlo a cargar :-) */

                            if ($(sGridId)[0].className == "gridForeignContainer")
                                return;

                            sWSParameters = $("#formTab_" + formSuffix).parent().attr("pk_name") + "=" + $("#formTab_" + formSuffix).parent().attr("pk") + "&4=" + $("#formTab_" + formSuffix).parent().attr("pk_name") + "=" + $("#formTab_" + formSuffix).parent().attr("pk") + "&";
                            aWSParameters = $($(ui.panel).parent().children()[1].children[0]).serialize().split("&");

                            for (k = 0; k < aWSParameters.length; k++) {
                                if (aWSParameters[k].substr(0, 2) != "_e") {
                                    if (aWSParameters[k].substr(0, 3) == "%24")
                                        sWSParameters += (k + 5) + "=" + aWSParameters[k].replace("_" + formSuffix, "") + "&";
                                    else {
                                        if ($("#" + aWSParameters[k].split("=")[0])[0].type != "textarea")
                                            if (aWSParameters[k].split("=")[1] != "")
                                                sWSParameters += (k + 5) + "=" + aWSParameters[k].replace("_" + formSuffix, "") + "&";
                                    }

                                }
                            }

                            $(sGridId).appgrid(
                                    {app: app,
                                        entidad: form,
                                        pk: "0",
                                        editingApp: "1",
                                        wsParameters: sWSParameters,
                                        titulo: sTitulo,
                                        height: "250",
                                        leyendas: ["Nuev@ " + sTitulo.substring(0, sTitulo.length - 1).toLowerCase(),
                                            "Edici&oacute;n de " + sTitulo.substring(0, sTitulo.length - 1).toLowerCase()],
                                        openKardex: false,
                                        originatingObject: "",
                                        showFilterLink: false,
                                        insertInDesktopEnabled: "0"})
                                    .removeClass('queued_grids')
                                    .addClass('gridForeignContainer');

                        });

                        //Se llama a cola de grids
                        /*for (i=0; i<$(".queued_grids").length;i++) {
                         
                         //Entrega los valores del formulario
                         //para establecer posible relación de forma y grid
                         sWSParameters=$.fn.form.options.pk_name+'='+$.fn.form.options.pk+'&4=' + $.fn.form.options.pk_name+'='+$.fn.form.options.pk;
                         aWSParameters=$("#form_" + formSuffix).serialize().split("&");
                         for (k=0; k<aWSParameters.length;k++) {
                         if (aWSParameters[k].substr(0,2)!="_e") {
                         if (aWSParameters[k].substr(0,3)=="%24")
                         sWSParameters+=(k+5)+"="+aWSParameters[k].replace("_"+formSuffix,"")+"&";
                         else {
                         if ($("#"+aWSParameters[k].split("=")[0])[0].type!="textarea") 
                         sWSParameters+=(k+5)+"="+aWSParameters[k].replace("_"+formSuffix,"")+"&";
                         }
                         
                         } 
                         
                         }
                         
                         $("#formTab_" + formSuffix).tabs('select',i+1);
                         
                         //Se agrega la leyenda en la etiqueta del grid                   
                         oGrid=$(".queued_grids:first")[0]; 
                         
                         $(oGrid).attr("leyendas","Nuev@ " + sTitulo.substring(0,sTitulo.length-1).toLowerCase()+","+
                         "Edici&oacute;n de " + sTitulo.substring(0,sTitulo.length-1).toLowerCase());
                         
                         $(oGrid).removeClass('queued_grids').addClass('gridForeignContainer');
                         sTitulo=$(oGrid).attr("titulo");
                         gridId=oGrid.id;
                         sGridDef='$("#'+gridId+'").appgrid({'+
                         'app: "'+$.fn.form.options.app+'",'+
                         'entidad:"'+ gridId.split("_")[2]+'",'+
                         'pk:"0",'+
                         'editingApp:"1",'+
                         'wsParameters:"' +$.fn.form.options.pk_name+"="+$.fn.form.options.pk+'&'+sWSParameters+'",'+
                         'titulo:"'+sTitulo+'",'+
                         'height:"250",'+
                         'openKardex:false,'+
                         'originatingObject:"",'+
                         'showFilterLink:false,'+
                         'insertInDesktopEnabled:"0"});';
                         setTimeout(sGridDef,1500);
                         }*/
                        //nWidth=$("#divFormProfiles_" + suffix).width();

                        //Reestablece la pestaña general
                        $("#formTab_" + formSuffix).tabs('select', 0);
                        // Se ocultan los mensajes de validación
                        $(".mensajeobligatorio").hide();

                        $(".fecha").datepicker({
                            dateFormat: 'dd/mm/yy',
                            dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                            monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre']
                        });

                        $(".fechayhora").datetimepicker({
                            dateFormat: 'dd/mm/yy',
                            dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                            monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
                            timeOnlyTitle: 'Seleccione hora',
                            timeText: 'Hora seleccionada',
                            hourText: 'Hora',
                            minuteText: 'Minutos',
                            secondText: 'Segundos',
                            currentText: 'Ahora',
                            closeText: 'Cerrar'
                        });


                        $(".money").calculator({
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
                        });

                        $(".autocompleteselect").combobox();

                        $(".webeditor").wysiwyg();

                        function mathsOnly(ch, event, value, base, decimalChar) {
                            return '+-*/'.indexOf(ch) > -1 && !(ch == '-' && value == '');
                        }

                        //Se activa el foreign toolbar para editar registros foraneos
                        oForm.find('.widgetbutton').fieldtoolbar({
                            app: $.fn.form.options.app
                        });

                        //Se activan el click de las liga orientadas a editar definiciones de campo
                        if ($("#_cp_").val() == "1") {
                            $(".edit_field").die("click", edita_diccionario)
                            $(".edit_field").live("click", edita_diccionario)
                        }
                        //Función para editar diccionario desde la liga del alias
                        function edita_diccionario() {
                            aId = this.id.split("-");
                            nApp = aId[1];
                            nForma = aId[2];
                            nPk = aId[3];

                            //Si la forma ya está presente aborta llamado
                            if ($("#dlgModal_" + nApp + "_" + nForma + "_" + nPk).length > 0)
                                return false;

                            sModo = "update";
                            if (nPk == 0)
                                sModo = "insert"

                            $("body").form({
                                app: nApp,
                                forma: 13,
                                datestamp: obj.attr("datestamp"),
                                modo: sModo,
                                titulo: "Diccionario de datos ",
                                columnas: 1,
                                pk: nPk,
                                filtroForaneo: aId[4],
                                height: "500",
                                width: "80%",
                                originatingObject: obj.id,
                                updateControl: obj.id
                            });

                            return true;
                        }

                        function validateForm(formData, jqForm, options) {
                            var bCompleto = true;

                            $(jqForm[0]).find('.obligatorio').each(function() {

                                if (this.id != "" && this.id != undefined) {

                                    if ($.trim(this.value) == "" && $(this).attr("type") != "checkbox") {
                                        $("#td_" + this.name).addClass("errorencampo")
                                        $(this).addClass("errorencampo");
                                        $($($(this).parent().parent().children()[0]).children()[1]).show();
                                        bCompleto = false;
                                    }
                                    else if ($(this).attr("type") == "checkbox" && !this.checked) {
                                        $("#td_" + this.name).addClass("errorencampo")
                                        $(this).addClass("errorencampo");
                                        $($($(this).parent().parent().children()[0]).children()[1]).show();
                                        bCompleto = false;
                                    }
                                    else {
                                        $("#td_" + this.name).removeClass("errorencampo")
                                        $($($(this).parent().parent().children()[0]).children()[1]).hide();
                                        $(this).removeClass("errorencampo");
                                    }
                                }
                            });

                            if (!bCompleto) {
                                $("#tdEstatus_" + formSuffix).html("Falta dato obligatorio, verifique");
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
                                $("#divwait").dialog("close");
                                $.fn.form.options.error = "Ocurri&oacute; un problema al guardar el registro: " +
                                        error.text() + ".";

                                if ($("#_cp_").val() == "1")
                                    $.fn.form.options.error += ", haga click <a href='#' id='lnkEditQuery_" +
                                            $.fn.form.options.app + "_" + $.fn.form.options.entidad + "' class='editLink'>aqui</a> para editarla ";

                                $("#tdEstatus_" + formSuffix).html($.fn.form.options.error);
                                $("#grid_" + gridSuffix + "_toppager_right").children(0).html($.fn.form.options.error);

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

                            if (sControl == "") {
                                /*Si no fue definido el control, por default se actualiza el grid*/
                                $("#grid_" + gridSuffix).jqGrid().trigger("reloadGrid");
                            } else {

                                if (sControl == "avatar") {
                                    setTimeout('$("#sessionMenu").sessionmenu()', 2000);
                                } else {

                                    /*Verifica si en realidad existe el control ...*/
                                    /* Verifica la posicion correcta del nodo */

                                    //Mecanismo para validar posción correcta del control a actualizar
                                    //a partir de la posición del widget
                                    /*fieldOnOpenedForms=$(document.getElementsByName(sControl));
                                     
                                     for (i=0; i<=fieldOnOpenedForms.length; i++) {
                                     if ($(document.getElementsByName(sControl))[i].form.id == "form_" + formSuffix){
                                     oControl=$(document.getElementsByName(sControl))[i];
                                     break;  
                                     }    
                                     }*/

                                    oControl = $("#" + sControl);
                                    if (oControl.length > 0) {
                                        if (sControl == "portletContacto") {
                                            $("#portletContacto").portletcontact();
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


                            }

                            //Cierra el dialogo
                            $("#dlgModal_" + formSuffix).dialog("destroy");
                            $("#dlgModal_" + formSuffix).remove();

                            //La forma se vuelve a abrir cuando se solicita
                            //que muestre sus relaciones
                            //y es una alta 
                            if (($.fn.form.options.showRelationships == 'true' &&
                                    $.fn.form.options.modo == "insert") || ($.fn.form.options.modo == "duplicate")) {

                                $("#divwait")
                                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Regenerando forma...</p>")
                                        .attr('title', 'Espere un momento por favor')
                                        .dialog({
                                    height: 140,
                                    modal: true,
                                    autoOpen: true,
                                    closeOnEscape: false
                                });

                                $("body").formqueue({
                                    app: $.fn.form.options.app,
                                    forma: $.fn.form.options.forma,
                                    datestamp: $.fn.form.options.datestamp,
                                    modo: "update",
                                    titulo: $.fn.form.options.titulo,
                                    columnas: 1,
                                    pk: sResultado,
                                    filtroForaneo: $.fn.form.options.filtroForaneo,
                                    height: "500",
                                    width: "80%",
                                    originatingObject: $.fn.form.options.originatingObject,
                                    showRelationships: $.fn.form.options.showRelationships,
                                    updateControl: sControl
                                });
                            }


                        }

                        //Cierra el dialogo de espera
                        $("#divwait").dialog("close");
                        $("#_status_").val("");

                    },
                    error: function(xhr, err) {
                        if (xhr.responseText.indexOf("Iniciar sesi&oacute;n") > -1) {
                            alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                            window.location = 'login.jsp';
                        } else {
                            $("#grid_" + gridSuffix + "_toppager_right").children(0).html("Error al recuperar la forma");
                            $("#dlgModal_" + formSuffix).remove();

                            //Cierra el dialogo de espera
                            $("#divwait").dialog("close");

                            alert("Error al recuperar forma: " + xhr.readyState + "\nstatus: " + xhr.status + "\responseText:" + xhr.responseText);
                        }
                        
                        $("#_status_").val("");
                    }
                });
    }


    $.fn.form.handleForm = function(xml) {
        var sRenglon = '';
        var nFormaForanea = 0;
        var nApp = $.fn.form.options.app;
        var sSuffix = '_' + $.fn.form.options.app + '_' + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        var oCampos = $(xml).find("registro").children();
        var tabIndex = 1;
        bVDS = $("#formTab" + sSuffix).attr("security").indexOf("5") != -1 ? true : false;
        sInvisibleInputs = "";
        var bAutoIncrement = false;

        oCampos.each(function() {
            sValorPredeterminado = "";
            oCampo = $(this);
            sTipoCampo = oCampo.attr("tipo_dato").toLowerCase();
            if (oCampo.find('evento').text() != "")
                $.fn.form.options.events[tabIndex - 1] = oCampo.find('evento').text();

            bAutoIncrement = (oCampo.attr("autoincrement") != undefined) ? true : false;
            if (bAutoIncrement)
                $.fn.form.options.pk_name = oCampo[0].nodeName;
            //Genera etiqueta
            nClave_campo = (oCampo.find('clave_campo').text() == undefined) ? 0 : oCampo.find('clave_campo').text();
            sAlias = oCampo.find('alias_campo').text();
            bDatoSensible = oCampo.find('dato_sensible').text();
            bActivo = oCampo.find('activo').text();
            sValorPredeterminado = oCampo.find('valor_predeterminado').text();
            bVisible = oCampo.find('visible').text();
            bNoPermitirValorForaneoNulo = oCampo.find('no_permitir_valor_foraneo_nulo').text();
            sAyuda = oCampo.find('ayuda').text();
            nObligatorio = oCampo.find('obligatorio').text();
            sTipoControl = oCampo.find('tipo_control').text();

            if (bAutoIncrement)
                return true;
            if (bDatoSensible == "1" && !bVDS)
                return true;
            if (bVisible == '0' || bVisible == '') {
                sInvisibleInputs += '<input type="hidden" tipo_dato="' + sTipoCampo + '" id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" value="'
                if ($.fn.form.options.modo == 'insert')
                    sInvisibleInputs += (sValorPredeterminado != "") ? eval(sValorPredeterminado) : "";
                else
                    sInvisibleInputs += oCampo[0].childNodes[0].data;

                sInvisibleInputs += '" />';
                return true;
            }

            sRenglon += '<td id="td_' + oCampo[0].nodeName + '" ';
            sRenglon += ' class="etiqueta_forma1'
            if (bVisible == '0')
                sRenglon += ' invisible';

            sRenglon += '">';

            if (sAlias != '') {
                if ($("#_cp_").val() == "1")
                    sRenglon += "<a id='lnkEditFieldDef-1-13-" + nClave_campo + "-2=clave_aplicacion=" + $.fn.form.options.app + "3=clave_forma=" + $.fn.form.options.forma + "' href='#' class='edit_field' title='Haga clic aqui para abrir su definici&oacute;n en el diccionario de datos'>" + sAlias + "</a>"
                else {
                    //Establece la seudoclase para mostrar la ayuda
                    if (sAyuda != "")
                        sRenglon += "<a class='tooltipField' ayuda='" + sAyuda + "' href='#'>" + sAlias + "</a>";
                    else
                        sRenglon += sAlias;
                }
            }
            else {
                sRenglon += "<a id='lnkEditFieldDef-1-13-" + nClave_campo + "-2=clave_aplicacion=" + $.fn.form.options.app + "3=clave_forma=" + $.fn.form.options.forma + "' href='#' class='edit_field' title='El campo no cuenta con alias, haga clic aqui para abrir su definici&oacute;n en el diccionario de datos'>" + oCampo[0].nodeName + "</a>";
            }

            //Verifica si el campo es obligatorio para incluir la leyenda en el alias
            if ($.fn.form.options.modo != "lookup" && nObligatorio == "1") {
                sRenglon += ' (<span class="mensajeobligatorio" id="msgvalida_' + oCampo[0].nodeName + '">Obligatorio</span>*)</td>'
            }
            else {
                sRenglon += '</td>'
            }

            //Genera liga para forma foranea
            var nFormaForanea = $(this).find('foraneo').attr("clave_forma");
            var nEditaForaneos = $(this).find('foraneo').attr("agrega_registro");
            if (nFormaForanea != undefined) {

                sRenglon += '<td class="etiqueta_forma_control1"><select tipo_dato="' + sTipoCampo + '" tabindex="' + tabIndex + '" ';

                if (bActivo != "1")
                    sRenglon += ' disabled="disabled" ';

                if ($.fn.form.options.modo != "lookup" && nEditaForaneos == "true") {
                    sRenglon += 'class="inputWidgeted1'
                }
                else {
                    sRenglon += 'class="singleInput'
                }

                // Establece el Tipo de control autocomplete
                if (sTipoControl == "autocomplete") {
                    sRenglon += " autocompleteselect ";
                }

                //Establece seudoclase a select

                if ($.fn.form.options.modo != "lookup" && oCampo.find('obligatorio').text() == "1") {
                    sRenglon += ' obligatorio" '
                }
                else {
                    sRenglon += '" '
                }

                //sRenglon+='id="' + oCampo[0].nodeName + sSuffix + '" name="' + oCampo[0].nodeName + sSuffix + '" >';
                sRenglon += 'id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" >';
                if ($.fn.form.options.modo == "lookup" || bNoPermitirValorForaneoNulo != "1") {
                    sRenglon += "<option ";
                    if ($.fn.form.options.modo != 'update' && sValorPredeterminado == "")
                        sRenglon += "selected='selected' ";
                    sRenglon += "></option>";
                }

                oCamposForaneos = oCampo.find('registro_' + oCampo[0].nodeName)

                oCamposForaneos.each(
                        function() {
                            oCampoForaneo = $(this);
                            sRenglon += "<option ";

                            if ($.fn.form.options.modo == 'insert' && sValorPredeterminado != "") {
                                if (eval(sValorPredeterminado) == oCampoForaneo.children()[0].childNodes[0].data)
                                    sRenglon += "selected='selected'";
                            }

                            if ($.fn.form.options.modo == 'update' && oCampo[0].childNodes[0].data == oCampoForaneo.children()[0].childNodes[0].data)
                                sRenglon += "selected='selected'";
                            sRenglon += " value='" + oCampoForaneo.children()[0].childNodes[0].data + "' >" + oCampoForaneo.children()[1].childNodes[0].data + "</option>";
                        }
                )

                sRenglon += '</select>';

                if ($.fn.form.options.modo != "lookup" && nEditaForaneos == "true") {
                    sRenglon += "<div class='widgetbutton' tipo_accion='" + $.fn.form.options.modo + "' tipo='foreign_toolbar' control='form" + sSuffix + " #" + oCampo[0].nodeName + "' forma='" + nFormaForanea + "' titulo_agregar='Nuevo " + sAlias.toLowerCase() + "' titulo_editar='Editar " + sAlias.toLowerCase() + "' ></div>";
                }

                sRenglon += '</td>§';
            }
            else {
                if (oCampo.find('tipo_control').text() == "textarea" || sTipoCampo == "text") {
                    sRenglon += '<td class="etiqueta_forma_control1">' +
                            '<textarea tabindex="' + tabIndex + '" rows="10" ';

                    if (bActivo != "1")
                        sRenglon += ' readonly="readonly" ';

                    sWidgetButton = "";

                    if ((sTipoCampo == 'money' || sTipoCampo == 'decimal') && bActivo == "1") {
                        sRenglon += 'class="inputWidgeted1';
                        sWidgetButton = '<div class="widgetbutton" tipo="calculator_buton" control="form' + sSuffix + ' #' + oCampo[0].nodeName + '"></div>';
                        //sRenglon +="<div class='widgetbutton' tipo='foreign_toolbar' control='" + oCampo[0].nodeName + "' forma='" + nFormaForanea + "' titulo_agregar='Nuevo " + sAlias.toLowerCase() + "' titulo_editar='Editar " + sAlias.toLowerCase() + "' ></div>";
                    } else if (sTipoCampo == 'datetime' && bActivo == "1") {
                        sRenglon += 'class="inputWidgeted1';
                        sWidgetButton = '<div class="widgetbutton" tipo="calendar_buton" control="form' + sSuffix + ' #' + oCampo[0].nodeName + sSuffix + '"></div>';
                    }
                    else if (oCampo.find('tipo_control').text() == "webeditor") {
                        sRenglon += 'class="webeditor';
                    }
                    else {
                        sRenglon += 'class="singleInput';
                    }

                    //Establece la marca de obligatorio con la seudoclase obligatorio
                    if ($.fn.form.options.modo != "lookup" && oCampo.find('obligatorio').text() == "1")
                        sRenglon += ' obligatorio"';
                    else
                        sRenglon += '"';

                    //sRenglon += ' id="' + oCampo[0].nodeName + sSuffix + '" name="' +  oCampo[0].nodeName + sSuffix + '" ' +
                    sRenglon += ' id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" >';

                    if ($.fn.form.options.modo == 'insert')
                        sRenglon += (sValorPredeterminado != "") ? eval(sValorPredeterminado) : "";
                    else
                        sRenglon += oCampo[0].childNodes[0].data;

                    sRenglon += '</textarea>';

                    //Agrega mecanismo para validar código javascript :-)
                    if (($.fn.form.options.forma == 13 || $.fn.form.options.forma == 3) && oCampo[0].nodeName == "evento") {
                        sRenglon += '<br><input type="button" value="Valida c&oacute;digo" onclick="validateCode(document.getElementById(\'evento\').value)">';
                    }

                    if ($.fn.form.options.forma == 220 && oCampo[0].nodeName == "notificacion") {
                        sRenglon += '<br><input type="button" value="Valida c&oacute;digo" onclick="validateCode(document.getElementById(\'notificacion\').value)">';
                    }

                    //Agrega mecanismo para validar sentencias de sql :-)
                    if (($.fn.form.options.forma == 8 || $.fn.form.options.forma == 257) && oCampo[0].nodeName == "consulta") {
                        sRenglon += '<br><input type="button" value="Valida consulta" onclick="validateSQL(document.getElementById(\'consulta\').value);">';
                    }

                    sRenglon += '</td>§';
                }
                else if ($(this).find('tipo_control').text() == "checkbox" || (sTipoCampo == "bit" || sTipoCampo == "tinyint")) {
                    sRenglon += '<td class="etiqueta_forma_control1">' +
                            '<div style="width:10px; margin: 0px; padding: 0px"><input type="checkbox" value="1" tabindex="' + tabIndex +
                            '" id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" ';

                    if (bActivo != "1")
                        sRenglon += ' readonly="readonly" ';

                    // Establece la marca de obligatorio con la seudoclase obligatorio
                    if ($.fn.form.options.modo != "lookup" && oCampo.find('obligatorio').text() == "1") {
                        sRenglon += 'class="singleInput obligatorio" ';
                    }
                    else {
                        sRenglon += 'class="singleInput" ';
                    }

                    if ($.fn.form.options.modo == 'insert')
                        sRenglon += (sValorPredeterminado == '1') ? 'checked="checked" ' : '';
                    else
                        sRenglon += (oCampo[0].childNodes[0].data == '1' || oCampo[0].childNodes[0].data == 'true') ? 'checked="checked" ' : '';

                    sRenglon += ' /></div></td>§';
                } else if ($.fn.form.options.modo != "lookup" && $(this).find('tipo_control').text() == "file" && oCampo[0].childNodes[0].data != "") {
                    //sOnChangeEvent="if (this.checked) $('#td_"+ oCampo[0].nodeName +"').html('<input tipo_dato=varchar id="+oCampo[0].nodeName+" name="+oCampo[0].nodeName+" class=singleInput type=file >');";
                    sOnChangeEvent = "if (!this.checked){$('#td_" + oCampo[0].nodeName + "').next().html('<input tipo_dato=varchar id=" + oCampo[0].nodeName + " name=" + oCampo[0].nodeName + " class=singleInput type=file >');}";
                    sRenglon += '<td class="etiqueta_forma_control1" style="text-align:left">' +
                            '<input type="checkbox" value="1" checked="checked" onChange="javascript:' + sOnChangeEvent + '" />';

                    if (oCampo[0].childNodes[0].data.indexOf("Error", 0) > -1) {
                        sRenglon += oCampo[0].childNodes[0].data.substring(oCampo[0].childNodes[0].data.lastIndexOf("/") + 1) + '</td>§';
                    } else {
                        sRenglon += '<a href="' + oCampo[0].childNodes[0].data + '" target="_blank">' + oCampo[0].childNodes[0].data + '</a><input type="hidden" name="' + oCampo[0].nodeName + '" id="' + oCampo[0].nodeName + '" value="' + oCampo[0].childNodes[0].data + '" /></td>§';
                    }


                }
                else {
                    sRenglon += '<td class="etiqueta_forma_control1">' +
                            '<input tipo_dato="' + sTipoCampo + '" id="' + oCampo[0].nodeName + '" name="' + oCampo[0].nodeName + '" ' +
                            'tabindex="' + tabIndex + '" ';

                    if (bActivo != "1")
                        sRenglon += ' readonly="readonly" ';

                    sWidgetButton = "";

                    if ((sTipoCampo == 'money' || sTipoCampo == 'decimal') && bActivo == "1") {
                        sRenglon += 'class="inputWidgeted1';
                        sWidgetButton = '<div class="widgetbutton" tipo="calculator_button" control="form' + sSuffix + " #" + oCampo[0].nodeName + '"></div>';
                    } else if (sTipoCampo == 'datetime' && bActivo == "1") {
                        sRenglon += 'class="inputWidgeted1';
                        sWidgetButton = '<div class="widgetbutton" tipo="calendar_button" control="form' + sSuffix + " #" + oCampo[0].nodeName + '"></div>';
                    }
                    else
                        sRenglon += 'class="singleInput';

                    if ($.fn.form.options.modo != "lookup" && oCampo.find('obligatorio').text() == "1")
                        sRenglon += ' obligatorio';

                    if (bActivo == "1") {
                        if (sTipoCampo == "datetime")
                            sRenglon += ' fechayhora';
                        else if (sTipoCampo == "smalldatetime" || sTipoCampo == "date")
                            sRenglon += ' fecha';
                        else if (sTipoCampo == "money" || sTipoCampo == 'decimal')
                            sRenglon += ' money';
                    }

                    if (oCampo.find('tipo_control').text() == "file" && $.fn.form.options.modo != "lookup")
                        sRenglon += ' file" type="' + oCampo.find('tipo_control').text() + '" value="';
                    else if (oCampo.find('tipo_control').text() != "file" && $.fn.form.options.modo != "lookup") {
                        sRenglon += '" type="' + oCampo.find('tipo_control').text() + '" value="';
                    } else {
                        sRenglon += '" type="text" value="';
                    }

                    if ($.fn.form.options.modo == 'insert')
                        sRenglon += (sValorPredeterminado != "") ? (eval(sValorPredeterminado)) : "";
                    else
                        sRenglon += oCampo[0].childNodes[0].data;

                    sRenglon += '" ';

                    //Validación para inputs estandar de acuerdo al tipo de datos del campo
                    if (sTipoCampo == "int" /*|| sTipoCampo=="money"*/) {
                        sRenglon += " onBlur='javascript:check_number(this)'";
                    }
                    else if (sTipoCampo == "date") {
                        sRenglon += " onBlur='javascript:check_date(this)' "
                    }

                    sRenglon += ' />' + sWidgetButton + ' </td>§';
                }
            }
            tabIndex++;
        }) //oCampos.each

        //Distribución en columnas
        sRenglon = sRenglon.substring(0, sRenglon.length - 1);
        var aRows = sRenglon.split('§');
        var nCols = $.fn.form.options.columnas;
        if (aRows.length > 18) {
            nCols = 2;
            //Vacía los textareas en otro arreglo
            /*var indexOfRowWithTextAreas=0;
             for (i=0; i<aRows.length;i++) {
             if (aRows[i].indexOf("textarea")>-1) {
             aRowsWithTextAreas[indexOfRowWithTextAreas]=aRows[i];
             aRows.splice(i,1);
             i--;
             indexOfRowWithTextAreas++;
             }
             
             }*/
        }
        var nRows = Math.round(aRows.length / nCols);
        var sForm = "";
        var i;
        for (i = 0; i < aRows.length; i++) {
            if (aRows[i].indexOf("textarea") > -1 && nCols > 1) {
                sForm += "<tr >" + aRows[i].replace(/class="etiqueta_forma_control1"/g, 'class="etiqueta_forma_control' + nCols + '" colspan="4"').replace(/etiqueta_forma1/g, "etiqueta_forma" + nCols).replace(/inputWidgeted1/g, "inputWidgeted" + nCols) + "</tr>";
                if (aRows.length > i + 1) {
                    i++;
                    if (aRows[i].indexOf("textarea") > -1) {
                        sForm += "<tr>" + aRows[i].replace(/class="etiqueta_forma_control1"/g, 'class="etiqueta_forma_control' + nCols + '" colspan="4"').replace(/etiqueta_forma1/g, "etiqueta_forma" + nCols).replace(/inputWidgeted1/g, "inputWidgeted" + nCols) + "</tr>";
                    } else {
                        sForm += "<tr>" + aRows[i].replace(/etiqueta_forma_control1/g, "etiqueta_forma_control" + nCols).replace(/etiqueta_forma1/g, "etiqueta_forma" + nCols).replace(/inputWidgeted1/g, "inputWidgeted" + nCols) + "<td>&nbsp;</td></tr>";
                    }
                }
            }
            else {
                sForm += "<tr>" + aRows[i].replace(/etiqueta_forma_control1/g, "etiqueta_forma_control" + nCols).replace(/etiqueta_forma1/g, "etiqueta_forma" + nCols).replace(/inputWidgeted1/g, "inputWidgeted" + nCols);
                if (aRows.length > i + 1 && nCols > 1) {
                    i++;
                    if (aRows[i].indexOf("textarea") > -1) {
                        sForm += "<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr><tr>" + aRows[i].replace(/class="etiqueta_forma_control1"/g, 'class="etiqueta_forma_control' + nCols + '" colspan="4"').replace(/etiqueta_forma1/g, "etiqueta_forma" + nCols).replace(/inputWidgeted1/g, "inputWidgeted" + nCols);
                    } else {
                        sForm += "<td>&nbsp;</td>" + aRows[i].replace(/etiqueta_forma_control1/g, "etiqueta_forma_control" + nCols).replace(/etiqueta_forma1/g, "etiqueta_forma" + nCols).replace(/inputWidgeted1/g, "inputWidgeted" + nCols);
                    }
                }
                sForm += "</tr>";
            }
        }


        //Llena la primer pestaña con la forma de la entidad principal
        var formSuffix = $.fn.form.options.app + "_" + $.fn.form.options.forma + "_" + $.fn.form.options.pk;
        sForm = "<!-- Instrucciones --><form class='forma' id='form_" + formSuffix + "' name='form_" + formSuffix + "' method='POST' ><table class='forma'>" + sForm + "</table>" +
                sInvisibleInputs +
                "<input type='hidden' id='_e' name='_e' value='' />" +
                "<input type='hidden' id='_r' name='_r' value='' />" +
                "<input type='hidden' id='_tcc' name='_tcc' value='' />" +
                "<input type='hidden' id='$ta' name='$ta' value='" + $.fn.form.options.modo + "' />" +
                "<input type='hidden' id='$ca' name='$ca' value='" + $.fn.form.options.app + "' />" +
                "<input type='hidden' id='$cf' name='$cf' value='" + $.fn.form.options.forma + "' />" +
                "<input type='hidden' id='$pk' name='$pk' value='" + $.fn.form.options.pk + "' /></form>"

        return sForm;
    }


})(jQuery);