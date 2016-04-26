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
 * and open the template in the editor.
 */
function cuestionario_supervision_init() {
    claveAplicacion = "149";
    claveForma = $(xml).find("configuracion_forma").find("clave_forma")[0].childNodes[0].data;
    claveRegistro = $(xml).find("configuracion_forma").find("pk")[0].childNodes[0].data;
    if ($.fn.form.options.modo != "lookup") {
        $("#btnGuardar_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro).attr("disabled", "disabled");
    } else {
        $("#clave_empleado").remove();
        $("<tr><td id='td_clave_empleado' class='etiqueta_forma1'><a href='#' class='edit_field'>Captur&oacute;</a></td>"+
          "<td class='etiqueta_forma_control1'><select tipo_dato='int' tabindex='2' class='singleInput' id='clave_empleado' name='clave_empleado'><option></option></select></td></tr>").insertAfter($("#td_clave_estado").parent());
        setXMLInSelect3("form_149_729_0 #clave_empleado", 6, "foreign","");
    }
    
    $("#id_tienda").unbind("blur").blur(function () {
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
            url: "control?$cmd=plain&$cf=681&$ta=select&$w=id_control=" + $(this).val(),
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
}

function cuestionario_supervision_grid_complete(){
     $(".startsurvey").click(function() {
        nCuestionario=this.id.split("_")[1];
        nProspecto=this.id.split("_")[2];
        sModo = this.id.split("_")[3];
        nClaveFormaEntidadParticipante=this.id.split("_")[4];
        IdTienda =this.id.split("_")[5];
       $("#_cache_").val(nCuestionario + "_" + IdTienda);
        
        if (sModo=='insert') {
            $("#divwait")
            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando cuestionario...</p>")
            .attr('title','Espere un momento por favor') 
            .dialog({
                height: 140,
                modal: true,
                autoOpen: true,
                closeOnEscape:false
            });
            //Inserta cuestionario de participante en blanco
            $.ajax({
            url: "control?$cmd=register&$cf=633&$pk=0&$ta=insert&clave_punto="+nProspecto+"&clave_cuestionario="+nCuestionario+"&clave_empleado="+$("#_ce_").val() + "&clave_estatus=1&fecha_inicio=%ahora",
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
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
                    claveFormaEntidadParticipante: nClaveFormaEntidadParticipante,
                    gridPorActualizar: "grid_149_729_0"
                });
                
                $("#_cache_").val("");
                
            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos del cuestionario: \n" + +xhr.responseText);
            }
        })
        } else {
            $("#divwait")
            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Abriendo cuestionario...</p>")
            .attr('title','Espere un momento por favor') 
            .dialog({
                height: 140,
                modal: true,
                autoOpen: true,
                closeOnEscape:false
            });
            
            $("#top").survey({
                pk: nCuestionario,
                modo: sModo,
                claveProspecto: nProspecto,
                claveFormaEntidadParticipante: nClaveFormaEntidadParticipante,
                gridPorActualizar: "grid_149_729_0"
            });
            
            $("#_cache_").val("");
        }
    });
    
    if ($("#_cache_").val().indexOf("_")>0) {
        $("#divwait")
            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Abriendo cuestionario...</p>")
            .attr('title','Espere un momento por favor') 
            .dialog({
                height: 140,
                modal: true,
                autoOpen: true,
                closeOnEscape:false
            });
            
        $("#top").survey({
            pk: $("#_cache_").val().split("_")[0],
            modo: "open",
            claveProspecto: $("#_cache_").val().split("_")[1],
            claveFormaEntidadParticipante: 681,
            gridPorActualizar: "grid_149_729_0"
        }); 
        
        $("#_cache_").val("");
    }
}

function eventos_cuestionario_supervision_init() {
    
}