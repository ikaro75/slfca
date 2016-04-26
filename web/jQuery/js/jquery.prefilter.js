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
 \u00bf -> ¿
 * and open the template in the editor.
 */
( function($) {
    $.fn.prefilter = function(opc){
        $.fn.prefilter.settings = {
            app:0,
            form:0
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.prefilter.options = $.extend($.fn.prefilter.settings, opc);
            $.fn.prefilter.getContent($(this));
        });
    };      
    
    $.fn.prefilter.getContent = function(obj){    
        $("#_status_").val("Cargando prefiltro");
        sWS="control?$cmd=form&$ta=prefilter&$cf=" + $.fn.prefilter.options.form + "&$pk=0";
        $.ajax({
            url: sWS,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlPrefiltro = new ActiveXObject("Microsoft.XMLDOM");
                    xmlPrefiltro.async = false;
                    xmlPrefiltro.validateOnParse="true";
                    xmlPrefiltro.loadXML(data);
                    if (xmlPrefiltro.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xmlPrefiltro.parseError.errorCode +"\nParse reason:" + xmlPrefiltro.parseError.reason + "\nLinea:" + xmlPrefiltro.parseError.line);
                }                
                else 
                    xmlPrefiltro = data;
                
                $("#_status_").val("Construyento controles de prefiltro");
                
                sHtml="";
                if ($(xmlPrefiltro).find("error").length) {
                    $(obj).html('<div class="ui-widget"><div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">'+
                                                 '<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>'+$(xmlPrefiltro).find("error").text()+
                                                 '</p></div></div>');
                    return;                                
                                                 
                }
                var html='';
                
                $(xmlPrefiltro).find("registro").children().each( function(){
                    sTipoCampo= $(this).attr("tipo_dato").toLowerCase();
                    html+='<label for="br_' + $(this)[0].nodeName + '" class="label_busqueda_rapida">' + $(this).find("alias_campo").text() + '</label>&nbsp;<input tipo_dato="' + sTipoCampo + '" id="' + $(this)[0].nodeName + '" style="font-size: 14px" ';
                    
                    if (sTipoCampo == 'money' || sTipoCampo == 'decimal') {
                      //debe hacerse una validación para el campo  
                      html+=' type="text" class="inputWidgeted money" tipo="calculator_buton" /> <div class="widgetbutton" tipo="calculator_buton" control="br_' +  $.fn.prefilter.options.app + '_' + $.fn.prefilter.options.form + '_' + oCampo[0].nodeName + '"></div>';
                    } else if ($(this).find('tipo_control').text() == "checkbox" || (sTipoCampo == "bit" || sTipoCampo == "tinyint")) {
                      html+=' type="checkbox" />';
                    } else if (sTipoCampo == "datetime") {
                        html+=' type="text" class="inputWidgeted fecha" /><div class="widgetbutton" tipo="calculator_button" control="br_' +  $.fn.prefilter.options.app + '_' + $.fn.prefilter.options.form + '_' + oCampo[0].nodeName + '"></div>';
                    } else if (sTipoCampo == "smalldatetime" || sTipoCampo == "date") {
                        html+=' type="text" class="inputWidgeted fechayhora" /><div class="widgetbutton" tipo="calculator_button" control="br_' +  $.fn.prefilter.options.app + '_' + $.fn.prefilter.options.form + '_' + oCampo[0].nodeName + '"></div>';
                    } else if(sTipoCampo =="varchar") {
                         html+=' type="text" />';
                    } else {
                         html+=' type="text" />';
                    }
                    html+="&nbsp;";
                });
                
                $(obj).html(html + "&nbsp;" + $(obj).html());
                
                //nuevamente se hace el barrido del xml para asignar eventos a los controles creados
                
                $(xmlPrefiltro).find("registro").children().each( function(){
                     $('#prefiltro_' + $.fn.prefilter.options.app + '_' + $.fn.prefilter.options.form+ ' #' + $(this)[0].nodeName).unbind("keyup" ).keyup(function(event){
                         switch(event.keyCode) {
                            case 16:
                            case 17:
                            case 18:
                            case 20:
                            case 27:
                            case 32: 
                            case 33: 
                            case 34: 
                            case 35: 
                            case 36: 
                            case 37:
                            case 38:
                            case 39:
                            case 40:
                            case 45:  
                                return;
                                break;    
                            case 13:
                                event.preventDefault();
                         }
                         //Solo falta validar la búsqueda por tipo de dato
                         if ($(this).val().length>=6) {
                             //alert("hola mundo");
                             suffix = $(this).parent()[0].id.replace("prefiltro","");
                             forma = suffix.split("_")[2];
                             
                             if (this.id=="rpu")
                                $("#_cache_").val("buscandoBeneficiario="+$(this).val());
                            
                             $("#grid"+ suffix +"_0").jqGrid('setGridParam',{
                                    url:"control?$cmd=grid&$cf=" + forma+ "&$ta=select&$dp=body&$w=" + encodeURIComponent(this.id + " like '" + $(this).val()+ "%'")}).trigger("reloadGrid")
                                    
                         }
                     });
                });
                
                //Evento del botón 
                $("#busquedaRapida_"+$.fn.prefilter.options.app + '_' + $.fn.prefilter.options.form).button().click(function(){
                    app=this.id.split("_")[1];
                    form=this.id.split("_")[2];
                    
                    estaVacio=true;
                    controles=$(this).parent().children();
                    w = "";
                    i=0;
                    while (i<controles.length) {
                        if (controles[i].tagName.toLowerCase()=="input" && controles[i].type=="text") {
                            if ($("#"+controles[i].id).val()!="") {
                                estaVacio=false;
                                if ($("#"+controles[i].id).attr("tipo_dato")=="varchar" || $("#"+controles[i].id).attr("tipo_dato")=="datetime" || $("#"+controles[i].id).attr("tipo_dato")=="smalldatetime")
                                    w += (w==""?"":" AND ")+controles[i].id + " like '" + $("#"+controles[i].id).val() + "%'";
                                else {
                                    w += (w==""?"":" AND ")+controles[i].id + "=" + $("#"+controles[i].id).val();
                                }
                            } 
                            
                            if (controles[i].id=="rpu") {
                                if (isNaN($("#" + controles[i].id).val()) || $("#" + controles[i].id).val().length!=12) {
                                    alert("El RPU debe ser un valor num\u00e9rico de 12 d\u00edgitos, verifique");  
                                    return;
                                }
                                $("#_cache_").val("buscandoBeneficiario="+$("#"+controles[i].id).val());
                            }    
                        }
                        
                        i++;
                    }
                    
                    if (estaVacio) {
                        alert("Es necesario definir un campo");
                    } else {
                        $("#grid_"+ app + "_" + form + "_0").jqGrid('setGridParam',{
                                    url:"control?$cmd=grid&$cf=" + form + "&$ta=select&$dp=body&$w=" + encodeURIComponent(w)}).trigger("reloadGrid");
                    }
                });
                
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
                
                $("#prefiltro_"+  $.fn.prefilter.options.app + "_" + $.fn.prefilter.options.form).find('.widgetbutton').fieldtoolbar({
                    app: $.fn.prefilter.options.app
                });
                        
                
               $("#busquedaAvanzada_"+ $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form).click(function() {
                    $("#grid"+$.fn.prefilter.options.app+"_"+ $.fn.appportlet.options.form + "_0_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Generando forma...");

                    $("#divwait")
                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                    .attr('title','Espere un momento por favor') 
                    .dialog({
                        height: 140,
                        modal: true,
                        autoOpen: true,
                        closeOnEscape:false
                    });

                    //Copia al cache la app que se está usando al editar
                    $("#_cache_").val($(this).attr("editingApp"));

                    $("body").form({
                        app: $.fn.prefilter.options.app,
                        forma:$.fn.appportlet.options.form,
                        datestamp:$(this).attr("datestamp"),
                        modo:"lookup",
                        titulo: "Filtrado de registros",
                        columnas:1,
                        height:"500",
                        width:"80%",
                        pk:0,
                        originatingObject: "grid_"+$.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + "_0"
                    });                    
               });
           
               function mathsOnly(ch, event, value, base, decimalChar) {
                            return '+-*/'.indexOf(ch) > -1 && !(ch == '-' && value == '');
               }
               
               $("#_status_").val("");
            },
            error:function(xhr,err){
                if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1) {
                    //alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                    window.location='login.jsp';
                }else{        
                    alert("Error al recuperar registros: "+xhr.readyState+"\nstatus: "+xhr.status + "\responseText:"+ xhr.responseText);
                }
                $("#_status_").val("");
            }            
        });
    }

    $.fn.prefilter.setGridFilter = function (sGridIdSuffix,nApp, nEntidad,data) {
        //Recarga el grid por si tiene algún filtro
        if (data==undefined){
            data="";
            $("#lnkRemoveFilter_grid_" +sGridIdSuffix ).remove();
        }
        else {
            //Si no existe el link para quitar filtro, lo establece
            if ($("#lnkRemoveFilter_grid_" + sGridIdSuffix).length==0) {                                        
                //oGridHeader=$("#grid_").parent().parent().parent()[0].childNodes[0].childNodes[1]
                oGridHeader=$("#gview_grid_" +sGridIdSuffix).find("span.ui-jqgrid-title");
                $(oGridHeader[0]).append("<a style='margin-left:10px' href='#' id='lnkRemoveFilter_grid_" + sGridIdSuffix +"'>(Quitar filtro)</a>");
                
                //Verifica URL anterior del grid filtrado
                var previousWhere="";
                var previusUrl=$("#grid_" + sGridIdSuffix)[0].p.url;
                if (previusUrl>-1);
                previousWhere=previusUrl.split("&$w=")[1];
                                
                //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                $("#lnkRemoveFilter_grid_" + sGridIdSuffix).click(function() {
                    if ($("grid_" + sGridIdSuffix).attr("requeriesFilter")=="1") {
                        $("body").form({
                            app: nApp,
                            forma:nEntidad,
                            datestamp:$(this).attr("datestamp"),
                            modo:"lookup",
                            titulo: "Filtrado de registros",
                            columnas:1,
                            height:"500",
                            width:"80%",
                            pk:0,
                            originatingObject: oGrid.id
                        });
                    }
                    else 
                        $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
                            url:previusUrl
                        }).trigger("reloadGrid");
                    
                    $(this).remove();
                        
                });
            }
        }
                            
        $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
            url:"control?$cmd=grid&$cf=" + nEntidad + "&$ta=select&$dp=body&$w=" +data
        }).trigger("reloadGrid");
    };

})(jQuery);