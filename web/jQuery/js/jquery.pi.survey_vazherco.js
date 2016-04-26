/* 
 * Plugin de jQuery para cargar forma a través de un plugin
 * 
 */
( function($) {
    $.fn.survey = function(opc){

        $.fn.survey.settings = { 
            titulo:"",
            pk:"",
            claveProspecto:"",
            xmlUrl : "control?$cmd=survey", 
            seccionActual:"",
            primeraSeccion:"",
            ultimaSeccion:"",
            estatus:"",
            modo:"",
            respuestaCondicional_5_siEstaEsLaRespuesta:"",
            top: 122,
            height:500,
            width:1200,
            events:[],
            error:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.survey.options = $.extend($.fn.survey.settings, opc);
            obj = $(this);
            $.fn.survey.getGUI(obj);          
        });
 
    };
   
    
    $.fn.survey.getGUI = function(obj){
        //Crea clave unica para forma
        var surveySuffix =$.fn.survey.options.pk;
        var sDialogo="";
        var sMainDivTabs="";
        var sDivTabs="";
        var sUlTabs="";
        var sBotonera="";
        //1. Primero crear el HTML necesario para contruir la interfaz de las relaciones
       
        sMainDivTabs="<div id='surveyTab_" + surveySuffix +  
        "' pk='" + $.fn.survey.options.pk +
        "' >";
    
        sTituloTab="General";
        
        if ($.fn.survey.options.modo=="show-authentication-form") {
            sTituloTab = "Ingrese código de autorización"
        }
        
        sUlTabs+="<ul><li><a href='#divSurveyGeneral_" + surveySuffix +"'>"+ sTituloTab + "</a></li><!-- UlTabsForaneos --></ul>";
        sDivTabs+="<div id='divSurveyGeneral_" + surveySuffix +"' >" +
        "<div align='center'><br /><br />Cargando informaci&oacute;n... <br /> <br />"+
        "<img src='img/loading.gif' />"+
        "</div>"+
        "</div><!-- DivTabsForaneos -->";
    
        sBotonera+="<div align='right' style='clear:left'><table style='width:100%'><tr><td align='left' id='tdEstatus_" +surveySuffix+"' class='estatus_bar'>&nbsp;</td><td align='right'>" +
                   "<div style='width: 100%;margin-right: 10px;'>";
        
        
        if ($.fn.survey.options.modo=="open") {
            sBotonera+="<input type='button' style='float: right;' class='formButton' id='btnSiguiente_" + surveySuffix +"' value='Siguiente' /><input type='button' style='float: right;' class='formButton' id='btnAnterior_" + surveySuffix +"' value='Anterior' /></div></td></tr></table></div>";
        } else if ($.fn.survey.options.modo=="show-authentication-form") {                     
            sBotonera+="<input type='button' style='float: right;' class='formButton' id='btnAutentifica_" + surveySuffix +"' value='Autentifica' /><input type='button' class='formButton' id='btn_ReenviaCodigoAutorizacion" + surveySuffix +"' value='Reenvía código de autorización' /></div></td></tr></table></div>";
        }
        
        sMainDivTabs+=sUlTabs+sDivTabs+sBotonera+"</div>";
        sDialogo+="<div id='dlgModal_"+ surveySuffix + "' title='" + $.fn.survey.options.titulo +"'>" + sMainDivTabs + "</div>";
        obj.append(sDialogo);
        $.fn.survey.setFormObjects();
        
    };
    
    $.fn.survey.setFormObjects = function(){  
   
        var surveySuffix =$.fn.survey.options.pk;
        
        $.ajax(
        {   
            url: $.fn.survey.options.xmlUrl + "&$ccp=" + $.fn.survey.options.pk + "&$pk=" + $.fn.survey.options.pk + "&$cp="+ $.fn.survey.options.claveProspecto+"&$ta=" + $.fn.survey.options.modo,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xml = new ActiveXObject("Microsoft.XMLDOM");
                    xml.async = false;
                    xml.validateOnParse="true";
                    xml.loadXML(data);
                    if (xml.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                    }
                }
                else {
                    xml = data;
                }
            
                /*Verifica el estatus de error*/
                var oError=$(xml).find("error");
                if (oError.length>0) {
                    var sDescripcionError=oError.text();
                    $("#dlgModal_"+ surveySuffix).remove();
                    alert(sDescripcionError);
                    $("#divwait").dialog( "close" );                 
                    return false;
                }
               
                surveySuffix = $.fn.survey.options.pk;                     
            
                //Se extraen datos generales de la forma
                $("#surveyTab_" + surveySuffix).html($("#surveyTab_" + surveySuffix).html()+'<input type="button" class"formButton"');
                
                //Se genera el HTML de la forma general 
                $("#divSurveyGeneral_" + surveySuffix).html($(xml).find("html").text());
                
                //Aplica el codigo proveniente del XML y que aplica en la forma
                //$.fn.survey.options.pk=$(xml).find('configuracion_survey').find('seccion_actual').text();
                $.fn.survey.options.seccionActual=$(xml).find('configuracion_survey').find('seccion_actual').text();
                $.fn.survey.options.primeraSeccion=$(xml).find('configuracion_survey').find('primera_seccion').text();
                $.fn.survey.options.ultimaSeccion=$(xml).find('configuracion_survey').find('ultima_seccion').text();
                $.fn.survey.options.titulo = $(xml).find('configuracion_survey').find('cuestionario').text();
                $.fn.survey.options.estatus = $(xml).find('configuracion_survey').find('estatus').text();
                $.fn.survey.options.respuestaCondicional_5_siEstaEsLaRespuesta  = $(xml).find('configuracion_survey').find('respuestaCondicional_5_siEstaEsLaRespuesta').text();
                evento=$(xml).find('configuracion_survey').find('evento').text();
                if (evento!="" && evento!="null")
                    $.globalEval(evento);
                
                //Borra la leyenda del grid                
                var oForm=$("#surveyform_" + surveySuffix);

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
                $("#opciones_"+surveySuffix)
                .button()
                .click(function() {
                    alert( "Seleccione una opción del menú" );
                })
                .next()
                .button( {
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
                
                $("#btnAnterior_"+ surveySuffix+", #btnSiguiente_"+ surveySuffix).button().click(function() {
                    
                    /*
                     *1. Validar preguntas marcadas como obligatorias 
                     *2. Si falta alguna obligatoria notificar y salir
                     *3. Actualizar ex_respuesta_participante a través de la llamada a survey_vazherco.jsp 
                     *   con el parametro $ta=save; survey_vazherco.jsp debe proporcionar en su respuesta 
                     *   la indicación si se presentó error al guardar, y si se guardaron bien
                     *   la indicación en función a la acción configurada en las respuestas seleccionadas
                     */
                    
                    surveySuffix =$.fn.survey.options.pk;
                    if (this.id=='btnAnterior_'+ surveySuffix)
                        document.getElementById('$hacia').value='atras';
                    else
                        document.getElementById('$hacia').value='adelante';
                        
                    $("#btnSiguiente_"+ surveySuffix).disabled=true;
                    $("#btnAnterior_"+ surveySuffix).disabled=true;
                    //Actualiza el estatus bar
                    $("#tdEstatus_" + $.fn.survey.options.pk).html("<img src='img/throbber.gif'>&nbsp;Validando informacion...");
                    
                    // inside event callbacks 'this' is the DOM element so we first 
                    // wrap it in a jQuery object and then invoke ajaxSubmit 
                    var options = { 
                            beforeSubmit:  validateForm,  // pre-submit callback 
                            success:       processXml,  // post-submit callback 
                            dataType:  ($.browser.msie) ? "text" : "xml",
                            url: "control?$cmd=survey&$ccp=" + $.fn.survey.options.pk + "&$ta=save",       // override for form's 'action' attribute 
                            error:function(xhr,err){
                                $("#dlgModal_"+ surveySuffix).remove();
                                alert("Error al guardar respuestas: "+xhr.readyState+"\nstatus: "+xhr.status + "\nResponseText:"+ xhr.responseText);          
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
                    
                        
                });
                
                
               $("#btnAutentifica_"+ surveySuffix).button().click(function() {
                    $.ajax(
                    {
                        url: "survey_vazherco.jsp?$ccp="+ $.fn.survey.options.pk +"&$ta=valid-authentication&auth_code=" + $("#auth_code").val(), 
                        dataType: ($.browser.msie) ? "text" : "xml",
                        success:  function(data){
                              if (typeof data == "string") {
                                xmlmlx = new ActiveXObject("Microsoft.XMLDOM");
                                xmlmlx.async = false;
                                xmlmlx.validateOnParse="true";
                                xmlmlx.loadXML(data);
                                if (xmlmlx.parseError.errorCode>0) {
                                    alert("Error de compilación xml:" + xmlmlx.parseError.errorCode +"\nParse reason:" + xmlmlx.parseError.reason + "\nLinea:" + xmlmlx.parseError.line);
                                }
                            }
                            else {
                                xmlmlx = data;
                            }
                          alert($(xmlmlx).find('alert').text());  
                          siguienteAccion= $(xmlmlx).find("siguiente_accion").text();
                          
                          if (siguienteAccion=="Finaliza cuestionario" || siguienteAccion=="Excluye estudio") {
                                $("#dlgModal_"+ surveySuffix).dialog("destroy");
                                $("#dlgModal_"+ surveySuffix).remove();
                          }   
                        },
                        error:function(xhr,err){
                            alert("Error al autenticar autorización");       

                        }
                    });
               });
               
               $("#btn_ReenviaCodigoAutorizacion"+ surveySuffix).button().click(function() {
                    $.ajax(
                    {
                                                
                        url: "survey_vazherco.jsp?$ccp=" + surveySuffix + "&$ta=send-validation-code&" + $("#auth_code").val(), 
                        dataType: ($.browser.msie) ? "text" : "xml",
                        success:  function(data){
                              if (typeof data == "string") {
                                xmlmlx = new ActiveXObject("Microsoft.XMLDOM");
                                xmlmlx.async = false;
                                xmlmlx.validateOnParse="true";
                                xmlmlx.loadXML(data);
                                if (xmlmlx.parseError.errorCode>0) {
                                    alert("Error de compilación xml:" + xmlmlx.parseError.errorCode +"\nParse reason:" + xmlmlx.parseError.reason + "\nLinea:" + xmlmlx.parseError.line);
                                }
                            }
                            else {
                                xml = data;
                            }
                          alert($(xml).find('alert').text());  
                        },
                        error:function(xhr,err){
                            alert("Error al enviar código de autorización");       

                        }
                    });
               });
               
                if ($.fn.survey.options.seccionActual==$.fn.survey.options.primeraSeccion) {
                    $("#btnAnterior_"+ surveySuffix).hide();
                }
                
                //Se debe presentar el botón finaliza si la seccionActual = a la última seccion y el perfil es el 2
                if (($.fn.survey.options.seccionActual==$.fn.survey.options.ultimaSeccion /*&& $("#_cp_").val()== "2"*/)
                    || ( // o si el estatus de cuestionario=5 y el perfil es el de un auditor y
                         // hay una pregunta en la sección actual cuyo atributo siEstaEsLaRespuesta=5 esta marcada ?  
                        $.fn.survey.options.respuestaCondicional_5_siEstaEsLaRespuesta=="true"  && $("#_cp_").val()== "3")) {
                    $("#btnSiguiente_"+ surveySuffix).val("Finalizar");
                }
                
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
                $("#dlgModal_"+ surveySuffix).dialog({
                    title: $.fn.survey.options.titulo,
                    modal: true,
                    /*height:$.fn.survey.options.height, */
                    top:document.body.scrollTop+350,
                    width:$.fn.survey.options.width,
                    open: function(event, ui) { 
                        $(this).dialog( "option", "position","center" ); 
                    },
                    close: function(event, ui) {
                        $(this).dialog("destroy");
                        $(this).remove();
                    }
                });
            
                //Se crean los tabs
                $("#surveyTab_" + surveySuffix).tabs();
                
                //Reestablece la pestaña general
                $("#surveyTab_" + surveySuffix).tabs('select',0);
                //Se ocultan los mensajes de validación
                $(".mensajeobligatorio").hide();

                //Se ocultan los campos con clase invisible
                //$(".invisible").hide().next().hide();
             
                $(".fecha").datepicker({
                    dateFormat: 'dd/mm/yy',
                    dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                    monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']
                });
                
                $(".fechayhora").datetimepicker({
                    dateFormat: 'dd/mm/yy',
                    dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
                    monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
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
                    $(this).val($(this).val().replace(/,/g,"").replace(/\$/g,""));
                }).blur(function() {
                    $(this).val(formatCurrency($(this).val()));
                });
                
                $(".webeditor").wysiwyg();
               
                function mathsOnly(ch, event, value, base, decimalChar) { 
                    return '+-*/'.indexOf(ch) > -1 && !(ch == '-' && value == ''); 
                }  
                
                function validateForm(formData, jqForm, options) { 
                    var bCompleto=true;
                    
                    $(jqForm[0]).find('.obligatorio').each(function() {
                        
                        if (this.id!="" && this.id!=undefined)  {
                                
                            if ($(this).attr("type")=="radio")  {
                                if (!$("input[name='" + this.id + "']:radio").is(':checked')) {
                                    $(this).addClass("errorencampo");
                                    $($(this).parent().parent().prev().children()[0]).addClass("errorencampo");
                                    $(this).parent().parent().prev().find(".mensajeobligatorio").show();
                                    bCompleto=false;                                    
                                } else {
                                    $(this).removeClass("errorencampo");
                                    $($(this).parent().parent().prev().children()[0]).removeClass("errorencampo");
                                    $(this).parent().parent().prev().find(".mensajeobligatorio").hide();                                                                       
                                }
                            } else if ($.trim(this.value)=="" && $(this).attr("type")!="checkbox") {
                                $("#td_" + this.name).addClass("errorencampo")
                                $(this).addClass("errorencampo");
                                $($($(this).parent().parent().children()[0]).children()[1]).show();
                                bCompleto=false;
                            }
                            else if ($(this).attr("type")=="checkbox" /*&& !this.checked*/)  {
                                //1. Se extrae el estilo preguntaX
                                pregunta=$(this)[0].classList[1];
                                //2. Se verifica si otros checkboxes del mismo estilo están marcados
                                if ($(jqForm[0]).find("." + pregunta + ":checked").length==0) {
                                    $("#td_" + this.name).addClass("errorencampo");
                                    $(this).addClass("errorencampo");
                                    //$($($(this).parent().parent().children()[0]).children()[1]).show();
                                    bCompleto=false;                                                                
                                }
                                
                            }
                            else {
                                $("#td_" + this.name).removeClass("errorencampo")
                                $($($(this).parent().parent().children()[0]).children()[1]).hide();
                                $(this).removeClass("errorencampo");
                            }
                        }
                    });
                    
                    if (!bCompleto){
                        alert("Falta dato obligatorio, verifique");
                        $("#tdEstatus_" + $.fn.survey.options.pk).html("Falta dato obligatorio, verifique");
                        return false;
                    }
                    else {
                        $("#tdEstatus_" + $.fn.survey.options.pk).html("<img src='img/throbber.gif'>&nbsp;Enviando informaci&oacute;n...");
                        return true;
                    }                        
                }
                
                function processXml(data) { 
                    // 'responseXML' is the XML document returned by the server; we use 
                    // jQuery to extract the content of the message node from the XML doc 
                    if (typeof data == "string") {
                        xmlResult = new ActiveXObject("Microsoft.XMLDOM");
                        xmlResult.async = false;
                        xmlResult.validateOnParse="true";
                        xmlResult.loadXML(data);
                        if (xmlResult.parseError.errorCode>0) {
                            alert("Error de compilaci&oacute;n xml:" + xmlResult.parseError.errorCode +"\nParse reason:" + xmlResult.parseError.reason + "\nLinea:" + xmlResult.parseError.line);
                        }
                    }
                    else {
                        xmlResult = data;
                    }

                    var error = $(xmlResult).find("error");
                    
                    if (error.length>0) {
                        $.fn.survey.options.error= error.text();
                                                                
                        $("#tdEstatus_" + $.fn.survey.options.pk).html($.fn.survey.options.error);
                                
                        return false;    
                    }      
                    
                    var warning= $(xmlResult).find("warning");
                    if (warning.length>0) {
                        for (i=0; i<warning.length; i++){
                            alert($($(warning)[i]).text());
                        }
                        
                    }
                    
                    var siguienteAccion= $(xmlResult).find("siguiente_accion").text();
                    
                    //Cierra el dialogo
                    if (siguienteAccion=="continua") {
                        $("#dlgModal_"+ surveySuffix).dialog("destroy");
                        $("#dlgModal_"+ surveySuffix).remove();
                        $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando siguiente sección de preguntas...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                        });
                    
                        $("#top").survey( {
                            pk: $.fn.survey.options.pk,
                            claveProspecto: $("#_ce_").val(),
                            modo:"open"
                        });
                    }  else if (siguienteAccion=="Excluye estudio") {
                        $("#dlgModal_"+ surveySuffix).dialog("destroy");
                        $("#dlgModal_"+ surveySuffix).remove();
                        alert('El paciente ha sido excluido del estudio, por favor conteste el formulario de finalización');
                    } else if(siguienteAccion=="Solicita autenticacion") {
                        $("#dlgModal_"+ surveySuffix).dialog("destroy");
                        $("#dlgModal_"+ surveySuffix).remove();
                        $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma de autenticanción...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                        });                        
                        $("#top").survey( {
                            pk: $.fn.survey.options.pk,
                            claveProspecto: $("#_ce_").val(),
                            modo:"show-authentication-form"
                        });
                        
                    }else if (siguienteAccion=="Finaliza cuestionario") {
                        $("#dlgModal_"+ surveySuffix).dialog("destroy");
                        $("#dlgModal_"+ surveySuffix).remove();
                        alert('Formato finalizado exitosamente');
                    }          
       
                }
                
                //Cierra el dialogo de espera
                $("#divwait").dialog( "close" );                

                
            },
            error:function(xhr,err){
                $("#dlgModal_"+ surveySuffix).remove();
                //Cierra el dialogo de espera
                $("#divwait").dialog( "close" );                
                alert("Error al recuperar forma: "+xhr.readyState+"\nstatus: "+xhr.status + "\responseText:"+ xhr.responseText);          
            }
        });
    }

})(jQuery);