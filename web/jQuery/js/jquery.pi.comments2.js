/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
( function($) {
    $.fn.comments2 = function(opc){

        $.fn.comments2.settings = {
            xmlUrl : "control?$cmd=insert", // /ProyILCE/srvControl?$cmd=sesion
            formSuffix:"",
            claveNota:"",
            claveForma:"",
            claveRegistro:"",
            claveEmpleado:"",
            titulo:"",
            mensaje:""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.comments2.options = $.extend($.fn.comments2.settings, opc);
        
        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            obj = $(this);
            $.fn.comments2.options.html_nuevo_comentario = 
                '<form class="forma" id="commentForm_' + $.fn.comments2.options.formSuffix + 
                '" name="commentForm_' + $.fn.comments2.options.formSuffix + '" method="POST" >'+
                '<table class="comentariono">'+
                '<tr><td style="text-align:center; width:60px" ><img src="'+ $("#avatar")[0].src + '" style="width:60; height:69 !important" border="1" width="60" height="69"></td><td>'+ 
                $("#_un_").html() +
                '<input type="hidden" id="titulo" name="titulo" value="'+ 
                $("#_un_").html().replace("<strong>","").replace("</strong>","")+'"><br />'+
                '<textarea rows="4" class="comment" id="mensaje" name="mensaje"></textarea></td></tr>'+
                '<tr><td>&nbsp;</td><td><input type="hidden" id="clave_forma" name="clave_forma" value="' + 
                $.fn.comments2.options.claveForma + '"><input type="hidden" id="clave_registro" name="clave_registro" value="' +
                $.fn.comments2.options.claveRegistro + '"><input type="hidden" id="clave_empleado" name="clave_empleado" value="' +
                $("#_ce_").val() + '"><input type="button" id="btnAgregaComentario_'+ $.fn.comments2.options.formSuffix + 
                '" value="Agregar comentario"></td></tr></table></form>';

            $.fn.comments2.options.html_comentario = 
                '<table class="comentariono">'+
                '<tr><td style="text-align:center; width:60px" ><img src="%foto" style="width:60; height:69 !important" border="1" width="60" height="69"></td>'+
                '<td style="vertical-align:top;"><strong>%titulo</strong> <abbr class="timeago" title="%fecha_nota">%fecha_nota</abbr> <br />'+
                '<div class="comment">%mensaje</div></td>'+
                '<td style="vertical-align:top; width:30px" ><div style="float:right"><div title="Eliminar comentario" style="cursor: pointer; float: right" class="closeLnkFiltro ui-icon ui-icon-close" pk="' + obj[0].id.split("-")[1] +'"></div></div></td></tr></table>';
            
            if (obj[0].id=="") {
                obj[0].id="_nuevo_comentario"+ $.fn.comments2.options.formSuffix;
                obj.html($.fn.comments2.options.html_nuevo_comentario);
                
                $("#btnAgregaComentario_"+ $.fn.comments2.options.formSuffix).button().click(function() {
                    $("#tdEstatus_" +$.fn.comments2.options.formSuffix).html("<img src='img/throbber.gif'>&nbsp;Publicando comentario...");
                    $("#btnAgregaComentario_"+$.fn.comments2.options.formSuffix).disabled=true; 

                    var commentForm =$("#commentForm_" + $.fn.comments2.options.formSuffix);

                    var options = { 
                        beforeSubmit:  validateCommentForm,  // pre-submit callback 
                        success:       processCommentXml,  // post-submit callback 
                        dataType:  ($.browser.msie) ? "text" : "xml",
                        url: "control?$cmd=register&$cf=273&$ta=insert",       // override for form's 'action' attribute 
                        error:function(xhr,err){
                            alert("Error al guardar comentario: "+xhr.readyState+"\nstatus: "+xhr.status + "\nResponseText:"+ xhr.responseText);          
                            $("#tdEstatus_" +$.fn.comments2.options.formSuffix).html("Falló el envío del comentario:" + xhr.responseText);
                        }
                        //type:      type        // 'get' or 'post', override for form's 'method' attribute 
                        //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
                        //clearForm: true        // clear all form fields after successful submit 
                        //resetForm: true        // reset the form after successful submit 

                        // $.ajax options can be used here too, for example: 
                        //timeout:   3000 
                    };
                    commentForm.ajaxSubmit(options); 

                    // !!! Important !!! 
                    // always return false to prevent standard browser submit and page navigation 
                    return false;                 

                });                
            } else {
                obj.html($.fn.comments2.options.
                    html_comentario.
                    replace("%foto",obj.attr("foto")).
                    replace("%titulo",obj.attr("titulo")).
                    replace("%nombre",obj.attr("nombre")).
                    replace("%mensaje",obj.find("mensaje").html()).
                    replace(/%fecha_nota/g,obj.attr("fecha_nota")).
                    replace("%fecha_nota",obj.attr("fecha_nota"))
            );  
                
                if (obj.attr("clave_empleado")!=$("#_ce_").val() && $("#_cp_").val()>1){
                    obj.html(obj.html().replace('<div style="float:right"><div title="Eliminar comentario" style="cursor: pointer; float: right" class="closeLnkFiltro ui-icon ui-icon-close" pk="' + obj[0].id.split("-")[1] +'"></div>',''));
                } 
                
                $(".ui-icon-close", "#"+obj[0].id).hide();          
            }
            
            $("abbr.timeago").timeago();
            //Establecer el evento hover
            obj.hover(
            function () {
                $(this).find(".ui-icon-close").show();
                /*$(this).find("closeLnkFiltro").addClass('ui-state-default');
                    $(this).find("closeLnkFiltro").addClass('ui-corner-all');*/
            },
            function () {
                $(this).find(".ui-icon-close").hide();
                /*$(this).find("closeLnkFiltro").removeClass('ui-state-default');
                    $(this).find("closeLnkFiltro").removeClass('ui-corner-all');*/
            }
        );

            //Hace bind con los botones de cerrar en el evento hover
            $(".closeLnkFiltro").hover(
            function () {
                $(this).parent().addClass('ui-state-default');
                $(this).parent().addClass('ui-corner-all');
            },
            function () {
                $(this).parent().removeClass('ui-state-default');
                $(this).parent().removeClass('ui-corner-all');
            }
        );
            
             
            obj.find(".closeLnkFiltro").click(function(){
                if (!confirm('¿Desea borrar el comentario seleccionado?')) return false;
                $.post("control","$cmd=register&$ta=delete&$cf=273&$pk=" + $(this).parent().parent().parent().parent().parent().parent()[0].id.split("-")[1]);
                $($(this).parent().parent().parent().parent().parent().parent()[0]).remove();
            });
        });

    };

    function validateCommentForm(formData, jqForm, options) { 
        if (jqForm.find("#mensaje").val()=="") {
            alert('Escriba un comentario');
            return false;
        }

    }

    function processCommentXml(data) { 
        // 'responseXML' is the XML document returned by the server; we use 
        // jQuery to extract the content of the message node from the XML doc 
        if (typeof data == "string") {
            xmlComment = new ActiveXObject("Microsoft.XMLDOM"); 
            xmlComment.async = false;
            xmlComment.validateOnParse="true";
            xmlComment.loadXML(data);
            if (xmlComment.parseError.errorCode>0) {
                alert("Error de compilaci&oacute;n xml:" + xmlComment.parseError.errorCode +"\nParse reason:" + xmlComment.parseError.reason + "\nLinea:" + xmlComment.parseError.line);
            }
        }
        else {
            xmlComment = data;
        }

        var error = $(xmlComment).find("error");

        if (error.length>0) {
            $.fn.form.options.error="Ocurri&oacute; un problema al guardar el comentario (" + 
                error.text() + ".)";

            if ($("#_cp_").val()=="1")
                $.fn.form.options.error+=", haga click <a href='#' id='lnkEditQuery_" + 
                $.fn.form.options.app +"_" +  $.fn.form.options.entidad +"' class='editLink'>aqui</a> para editarla ";

            $("#tdEstatus_" + $.fn.comments2.options.formSuffix).html($.fn.form.options.error);

            return false;    
        }      

        sResultado=$(xmlComment).find("pk").text(); 
        sMensaje=$("#commentForm_" + $.fn.comments2.options.formSuffix + " #mensaje").val();
        //Se convierte el text en div
        $("#_nuevo_comentario"+ $.fn.comments2.options.formSuffix).html($.fn.comments2.options.
            html_comentario.
            replace("%foto",$("#avatar")[0].src).
            replace("%titulo",$("#_un_").html()).
            replace("%nombre",$("#_un_").html()).
            replace("%mensaje",sMensaje).
            replace(/%fecha_nota/g,sDateTimeToString(new Date())).
            replace("%fecha_nota",sDateTimeToString(new Date()))
        );

        $("#_nuevo_comentario"+ $.fn.comments2.options.formSuffix)[0].id="comentario_"+sResultado;
        $(".ui-icon-close", "#comentario_"+sResultado).hide();
                    
        //Establecer el evento hover
        $("#comentario_"+sResultado).hover(
        function () {
            $(this).find(".ui-icon-close").show();
            /*$(this).find("closeLnkFiltro").addClass('ui-state-default');
                        $(this).find("closeLnkFiltro").addClass('ui-corner-all');*/
        },
        function () {
            $(this).find(".ui-icon-close").hide();
            /*$(this).find("closeLnkFiltro").removeClass('ui-state-default');
                        $(this).find("closeLnkFiltro").removeClass('ui-corner-all');*/
        }
        );

        //Hace bind con los botones de cerrar en el evento hover
        $(".closeLnkFiltro").hover(
        function () {
            $(this).parent().addClass('ui-state-default');
            $(this).parent().addClass('ui-corner-all');
        },
        function () {
            $(this).parent().removeClass('ui-state-default');
            $(this).parent().removeClass('ui-corner-all');
        }
        );
                
        $(".closeLnkFiltro", "#comentario_"+sResultado).click(function(){
            if (!confirm('¿Desea borrar el comentario seleccionado?')) return false;
            commentId=$(this).parent().parent().parent().parent().parent().parent()[0].id.split("_")[1];
            $.post("control","$cmd=register&$ta=delete&$cf=273&$pk="+commentId);
            $("#comentario_"+commentId).remove();
        });
        
    $('<div class="comentariono" id="_nuevo_comentario' + $.fn.comments2.options.formSuffix + '">'+$.fn.comments2.options.html_nuevo_comentario+'</div>').insertAfter("#comentario_"+sResultado);
    $("abbr.timeago").timeago();
    $("#btnAgregaComentario_"+ $.fn.comments2.options.formSuffix).button().click(function() {
        $("#tdEstatus_" +$.fn.comments2.options.formSuffix).html("<img src='img/throbber.gif'>&nbsp;Publicando comentario...");
        $("#btnAgregaComentario_"+$.fn.comments2.options.formSuffix).disabled=true; 
        var commentForm =$("#commentForm_" + $.fn.comments2.options.formSuffix);
        var options = { 
            beforeSubmit:  validateCommentForm,  // pre-submit callback 
            success:       processCommentXml,  // post-submit callback 
            dataType:  ($.browser.msie) ? "text" : "xml",
            url: "control?$cmd=register&$cf=273&$ta=insert",       // override for form's 'action' attribute 
            error:function(xhr,err){
                alert("Error al guardar comentario: "+xhr.readyState+"\nstatus: "+xhr.status + "\nResponseText:"+ xhr.responseText);          
                $("#tdEstatus_" +$.fn.comments2.options.formSuffix).html("Falló el envío del comentario:" + xhr.responseText);
            }
            //type:      type        // 'get' or 'post', override for form's 'method' attribute 
            //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
            //clearForm: true        // clear all form fields after successful submit 
            //resetForm: true        // reset the form after successful submit 

            // $.ajax options can be used here too, for example: 
            //timeout:   3000 
        };
        commentForm.ajaxSubmit(options); 

        // !!! Important !!! 
        // always return false to prevent standard browser submit and page navigation 
        return false;                 

    });          
    $("#tdEstatus_" +$.fn.comments2.options.formSuffix).html("");
}
})(jQuery);