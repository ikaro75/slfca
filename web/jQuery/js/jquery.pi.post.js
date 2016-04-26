/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
( function($) {
    $.fn.post = function(opc){

        $.fn.post.settings = {
            xmlUrl : "control?$cmd=insert", // /ProyILCE/srvControl?$cmd=sesion
            formSuffix:"",
            claveNota:"",
            claveForma:"",
            claveRegistro:"",
            claveEmpleado:"",
            titulo:"",
            mensaje:"",
            formaComentario: ""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.post.options = $.extend($.fn.post.settings, opc);
        $.fn.post.options.formSuffix = $("#_ce_").val();
        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            obj = $(this);
            $.fn.post.options.html_nuevo_comentario = 
            '<form class="forma" id="commentForm_' + $.fn.post.options.formSuffix + 
            '" name="commentForm_' + $.fn.post.options.formSuffix + '" method="POST" >'+
            '<table class="forma" style="margin-left: 0px;" >'+
            '<tr><td style="vertical-align: top; width:60px;"  ><img src="'+ $("#avatar")[0].src + '" style="width:50px; height:58px !important" border="1" ></td><td>'+ 
            $("#_un_").html().replace("Bienvenid@ ","") +
            '<br />'+
            '<textarea class="comment" id="publicacion" name="publicacion" style="width: 98%; height: 20px;"></textarea>' +
            '</td></tr>'+
            '<tr style="display:none;"><td>&nbsp;</td><td><input type="hidden" id="clave_empleado" name="clave_empleado" value="' +
            $("#_ce_").val() + '"><input type="hidden" id="clave_estatus" name="clave_estatus" value="1"><input type="hidden" id="fecha" name="fecha" value="%ahora"><input type="hidden" id="clave_publicacion_padre" name="clave_publicacion_padre" value=""></td></tr></table></form>';

            $.fn.post.options.html_comentario = 
            '<table class="forma">'+
            '<tr><td style="vertical-align:top; width:60px;" ><img src="%foto" style="width:50px; height:58px !important" border="1"></td>'+
            '<td style="vertical-align:top;"><strong><a href="#" id="contactProfile_%clave_empleado" class="lnkContactProfile">%nombre</a></strong> <abbr class="timeago" title="%fechaISO8601">%fecha</abbr> <br />'+
            '<div class="post">%publicacion</div></td>'+
            '<td style="vertical-align:top; width:30px" ><div style="float:right"><div title="Eliminar comentario" style="cursor: pointer; float: right" class="closeLnkFiltro ui-icon ui-icon-close" pk="' + obj[0].id.split("-")[1] +'"></div></div></td></tr>' +
            '<tr style="margin-bottom:5px;"><td>&nbsp;</td><td><a class="comentar_link" id="lnkcomentar_%clave_publicacion_padre" href="#">Comentar</a>&nbsp;|&nbsp;<a class="compartir_link" id="lnkcompartir_%clave_publicacion_padre" href="#">Compartir</a></td><td>&nbsp;</td></tr>' +
            '<tr id="trrespuesta_%clave_publicacion_padre" class="trrespuesta"><td>&nbsp;</td><td style="padding: 0px;">' + 
            $.fn.post.options.html_nuevo_comentario.replace('<form class="forma" id="commentForm_'+ $.fn.post.options.formSuffix + '" name="commentForm_' + $.fn.post.options.formSuffix + '"','<form class="forma" id="replyForm_'+ $.fn.post.options.formSuffix + '_%clave_publicacion_padre" name="replyForm_'+ $.fn.post.options.formSuffix + '_%clave_publicacion_padre"' )
            .replace('id="publicacion" name="publicacion"','id="respuesta_%clave_publicacion_padre" name="respuesta_%clave_publicacion_padre"') 
            .replace('<table class="forma"','<table class="forma ui-state-highlight"') +
            '</td></tr>' +              
            '</table>';
            
            if (obj[0].id=="") {
                obj[0].id="_nuevo_comentario";
                obj.html($.fn.post.options.html_nuevo_comentario.replace("ui-state-highlight","").replace('<input type="hidden" id="clave_publicacion_padre" name="clave_publicacion_padre" value="">',''));                
            } 
            
            /*
            $(".ui-icon-close", "#"+this.id).hide();  
            //Establecer el evento hover
            $(this).hover(
                function () {
                    $(this).find(".ui-icon-close").show();
                    $(this).find("closeLnkFiltro").addClass('ui-state-default');
                    $(this).find("closeLnkFiltro").addClass('ui-corner-all');
                },
                function () {
                    $(this).find(".ui-icon-close").hide();
                /*$(this).find("closeLnkFiltro").removeClass('ui-state-default');
                    $(this).find("closeLnkFiltro").removeClass('ui-corner-all');
           
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
             
            $(this).find(".closeLnkFiltro").click(function(){
                if (!confirm('¿Desea borrar el comentario seleccionado?')) return false;
                $.post("control","$cmd=register&$ta=delete&$cf=299&$pk=" + $(this).attr("pk"));
                $($(this).parent().parent().parent().parent().parent().parent()[0]).remove();
            }); 
            
            $(this).find(".replycomment").autosize().keydown(function(event) {
                    if (event.which == 13) {
                       event.preventDefault();
                       replyID=this.id.split("_")[1];
                       this.id="publicacion";
                       this.name="publicacion"
                       var commentForm =$("#replyForm_195_" + replyID);
                       $.fn.post.options.formaComentario = "replyForm_195_" + replyID;

                       var options = { 
                            beforeSerialize: verifyLinkPreview,
                            beforeSubmit:  validateCommentForm,  // pre-submit callback 
                            success:       processCommentXml,  // post-submit callback 
                            dataType:  ($.browser.msie) ? "text" : "xml",
                            url: "control?$cmd=register&$cf=299&$ta=insert",       // override for form's 'action' attribute 
                            error:function(xhr,err){
                                alert("Error al guardar comentario: "+xhr.readyState+"\nstatus: "+xhr.status + "\nResponseText:"+ xhr.responseText);          
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
                        event.stopImmediatePropagation();
                        return false;
                    }
                }); */
        });
    };

    function verifyLinkPreview(jqForm, options) {
        replyID=jqForm[0].id.split("_")[2];

        var link = jqForm.find("#publicacion").val().match(/(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/);
        if (link!=null && link.length>0) {
           jqForm.find("#publicacion").val(jqForm.find("#publicacion").val().replace(link[0], '<a href="' + link[0] + '" target="_blank">' + link[0] + "</a>"));
        }
        
        if (jqForm.find("#publicacion").next().next().find(".jq_content_inner").length>0) {
            sHtmlToPublish = '<div class="linkpreview">' + jqForm.find("#publicacion").next().next().find(".jq_content_inner").html().replace(jqForm.find("#publicacion").next().next().find("#jq_console").html(),"") + '</div>';
            jqForm.find("#publicacion").val(jqForm.find("#publicacion").val() + sHtmlToPublish);
        }        
    }
    
    function validateCommentForm(formData, jqForm, options) { 
        replyID=jqForm[0].id.split("_")[2];
        
        commentField="#publicacion";
        if (jqForm.find(commentField).length==0) {
            commentField = "#respuesta_" + replyID;
        }
        
        if (jqForm.find(commentField).val()=="") {
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

            return false;    
        }

        sResultado=$(xmlComment).find("pk").text();                     
        //Se convierte el text en div
        aFechayhora=sDateTimeToString(new Date());
        fecha=aFechayhora.split(" ")[0];
        hora=aFechayhora.split(" ")[1];
        fecha = fecha.split("/")[2]+"-"+fecha.split("/")[1]+"-"+fecha.split("/")[0];
        
        if ($.fn.post.options.formaComentario.indexOf("replyForm")>-1) {
            
           comentarioPublicado= "<tr class='trrespuesta_%clave_publicacion_padre trrespuesta' style='display: table-row;'>"+
                                "<td>&nbsp;</td>" + 
                                "<td>" +
                                "<table style='margin-left: 0px;' class='forma ui-state-highlight'>"+
                                "<tbody>" + 
                                "<tr>" + 
                                "<td style='vertical-align: top; width:60px;'><img border='1' style='width:50px; height:58px !important' src='%foto'></td>"+
                                "<td><strong>%nombre</strong> <abbr title='%fecha' class='timeago'>%fecha</abbr><br><div style='width: 98%; height: 20px; overflow: hidden; word-wrap: break-word; resize: horizontal;'>%publicacion</div></td>"+
                                "</tr>" + 
                                "</tbody>" + 
                                "</table>" + 
                                "</td>" + 
                                "</tr>";
                                
    
            oNuevoComentario=$(comentarioPublicado.replace("%foto",$("#avatar")[0].src)
                                .replace("%nombre",$("#_un_").html().replace("Bienvenid@ ",""))
                                .replace("%publicacion",$("#" + $.fn.post.options.formaComentario + " #publicacion").val())
                                .replace(/%fecha/g,fecha + " " + hora)
                                .replace(/%clave_publicacion_padre/g,$.fn.post.options.formaComentario.split("_")[2]));
        } else {
            oNuevoComentario=$("<div id='comentario_"+sResultado+"' class='post'><div>").html($.fn.post.options.
                html_comentario.
                replace("%foto",$("#avatar")[0].src).
                replace("%nombre",$("#_un_").html().replace("Bienvenid@ ","")).
                replace("%publicacion",$("#publicacion").val()).
                replace(/%fecha/g,fecha + " " + hora).
                replace(/%clave_publicacion_padre/g,sResultado)
            );            
        }
        
        if ($.fn.post.options.formaComentario.indexOf("replyForm")==-1) {
            $("#" + $.fn.post.options.formaComentario + " #publicacion").val("").next().val("").next().html("").hide();
        }    
        
        $(".ui-icon-close", oNuevoComentario).hide();

        //Establecer el evento hover
        $(oNuevoComentario).hover(
            function () {
                $(this).find(".ui-icon-close").show();
            /*$(this).find("closeLnkFiltro").addClass('ui-state-default');
                    $(this).find("closeLnkFiltro").addClass('ui-corner-all');*/
            },
            function () {
                $(this).find(".ui-icon-close").hide();
            /*$(this).find("closeLnkFiltro").removeClass('ui-state-default');
                    $(this).find("closeLnkFiltro").removeClass('ui-corner-all');*/
        });

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
                
        $(".closeLnkFiltro", oNuevoComentario).click(function(){
            if (!confirm('¿Desea borrar el comentario seleccionado?')) return false;
            $.post("control","$cmd=register&$ta=delete&$cf=273&$pk="+$(this).attr("pk"));
            $("#comentario_"+commentId).remove();
        });
        
         if ($.fn.post.options.formaComentario.indexOf("replyForm")>-1) {
             //sHtml=$("#trrespuesta_" + $.fn.post.options.formaComentario.split("_")[2]).html();
             //sRespuesta=$("#trrespuesta_" + $.fn.post.options.formaComentario.split("_")[2]).find("#publicacion").val();
             //$("#trrespuesta_" + $.fn.post.options.formaComentario.split("_")[2]).find("#publicacion").parent().html("<div>" + sRespuesta + "</div>");
            $(oNuevoComentario).insertBefore($("#trrespuesta_" + $.fn.post.options.formaComentario.split("_")[2])); 
            $("#trrespuesta_" + $.fn.post.options.formaComentario.split("_")[2]).find("#publicacion").val("")
         } else {
            $(oNuevoComentario).insertBefore($("#divMuro").children()[0]);
         }   
        
        $("#lnkcomentar_" + sResultado).click(function(e){
            $(".trrespuesta_"+this.id.split("_")[1]).toggle();                
         });
        
        $("#respuesta_" + sResultado).autosize().keydown(function(event) {
                if (event.which == 13) {
                   event.preventDefault();
                   replyID=this.id.split("_")[1];
                   this.id="publicacion";
                   this.name="publicacion"
                   var commentForm =$("#replyForm_195_" + replyID);
                    
                   var options = { 
                        beforeSerialize: verifyLinkPreview,
                        beforeSubmit:  validateCommentForm,  // pre-submit callback 
                        success:       processCommentXml,  // post-submit callback 
                        dataType:  ($.browser.msie) ? "text" : "xml",
                        url: "control?$cmd=register&$cf=299&$ta=insert",       // override for form's 'action' attribute 
                        error:function(xhr,err){
                            alert("Error al guardar comentario: "+xhr.readyState+"\nstatus: "+xhr.status + "\nResponseText:"+ xhr.responseText);          
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
                    event.stopImmediatePropagation();
                    return false;
                }
       });
            
        oNuevoComentario.find(".trrespuesta_" +sResultado).hide();
        
        oNuevoComentario.find("abbr.timeago").timeago();
        
        /*$("#btnAgregaComentario_"+ $.fn.post.options.formSuffix).button().click(function() {
            $("#btnAgregaComentario_"+$.fn.post.options.formSuffix).disabled=true; 

            var commentForm =$("#commentForm_" + $.fn.post.options.formSuffix);

            var options = { 
                beforeSubmit:  validateCommentForm,  // pre-submit callback 
                success:       processCommentXml,  // post-submit callback 
                dataType:  ($.browser.msie) ? "text" : "xml",
                url: "control?$cmd=register&$cf=273&$ta=insert",       // override for form's 'action' attribute 
                error:function(xhr,err){
                    alert("Error al guardar comentario: "+xhr.readyState+"\nstatus: "+xhr.status + "\nResponseText:"+ xhr.responseText);          
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

        }); */
    }
})(jQuery);