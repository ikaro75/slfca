( function($) {
    $.fn.wall = function(opc){
        
        $.fn.wall.settings = {
            titulo:"",
            app:"",
            forma:"",
            pk:"",
            pk_name:"",
            pagina: "1",
            numeroDeRegistros:"20",
            w:"",
            xmlUrl : "control?$cmd=post" , // "srvControl" "xml_tests/forma.app.xml"
            formaComentario: ""     
        };
        
        $("#_status_").val("Inicializando actividades recientes"); 
        $.fn.wall.options = $.extend($.fn.wall.settings, opc);
        $.fn.wall.options.formSuffix = $("#_ce_").val();
        $.fn.wall.options.html_nuevo_comentario = 
            '<form class="forma" id="commentForm_' + $.fn.wall.options.formSuffix + 
            '" name="commentForm_' + $.fn.wall.options.formSuffix + '" method="POST" >'+
            '<table class="forma" style="margin-left: 0px;" >'+
            '<tr><td style="vertical-align: top; width:60px;"  ><img src="'+ $("#avatar")[0].src + '" style="width:50px; height:58px !important" border="1" ></td><td>'+ 
            $("#_un_").html().replace("Bienvenid@ ","") +
            '<br />'+
            '<textarea class="comment" id="publicacion" name="publicacion" style="width: 98%; height: 20px;"></textarea>' +
            '</td></tr>'+
            '<tr style="display:none;"><td>&nbsp;</td><td><input type="hidden" id="clave_empleado" name="clave_empleado" value="' +
            $("#_ce_").val() + '"><input type="hidden" id="clave_estatus" name="clave_estatus" value="1"><input type="hidden" id="fecha" name="fecha" value="%ahora"><input type="hidden" id="clave_publicacion_padre" name="clave_publicacion_padre" value=""></td></tr></table></form>';

        $.fn.wall.options.html_comentario = 
            '<table class="forma">'+
            '<tr><td style="vertical-align:top; width:60px;" ><img src="%foto" style="width:50px; height:58px !important" border="1"></td>'+
            '<td style="vertical-align:top;"><strong><a href="#" id="contactProfile_%clave_empleado" class="lnkContactProfile">%nombre</a></strong> <abbr class="timeago" title="%fechaISO8601">%fecha</abbr> <br />'+
            '<div class="post">%publicacion</div></td>'+
            '<td style="vertical-align:top; width:30px" ><div style="float:right"><div title="Eliminar comentario" style="cursor: pointer; float: right" class="closeLnkFiltro ui-icon ui-icon-close" pk="%pk"></div></div></td></tr>' +
            '<tr style="margin-bottom:5px;"><td>&nbsp;</td><td><a class="comentar_link" id="lnkcomentar_%clave_publicacion_padre" href="#">Comentar</a>&nbsp;|&nbsp;<a class="compartir_link" id="lnkcompartir_%clave_publicacion_padre" href="#">Compartir</a></td><td>&nbsp;</td></tr>' +
            '<tr id="trrespuesta_%clave_publicacion_padre" class="trrespuesta"><td>&nbsp;</td><td style="padding: 0px;">' + 
            $.fn.wall.options.html_nuevo_comentario.replace('<form class="forma" id="commentForm_'+ $.fn.wall.options.formSuffix + '" name="commentForm_' + $.fn.wall.options.formSuffix + '"','<form class="forma" id="replyForm_'+ $.fn.wall.options.formSuffix + '_%clave_publicacion_padre" name="replyForm_'+ $.fn.wall.options.formSuffix + '_%clave_publicacion_padre"' )
            .replace('id="publicacion" name="publicacion"','id="respuesta_%clave_publicacion_padre" name="respuesta_%clave_publicacion_padre"') 
            .replace('<table class="forma"','<table class="forma ui-state-highlight"') +
            '</td></tr>' +              
            '</table>';     
        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            wall=this;            
            
            $("#_status_").val("Cargando ultimas noticias"); 
            $.ajax(
            {   
            url: $.fn.wall.options.xmlUrl + "&$cf="+ $.fn.wall.options.forma + "&$ta=select&page=" +$.fn.wall.options.pagina +"&rows="+$.fn.wall.options.numeroDeRegistros+ "&sidx=fecha&sord=desc&$w="+$.fn.wall.options.w,
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
                
                $("#_status_").val("Construyendo panel de actividades recientes"); 
                /*Verifica el estatus de error*/
                var oError=$(xml).find("error");
                if (oError.length>0) {
                    var sDescripcionError=oError.text();
                }
                
                //Se activa o se desactiva el textarea con preview de links de acuerdo a los permisos del perfil del usuario
                /* Procesamiento de permisos */
                var sPermiso = "";
                var oPermisos = $(xml).find("clave_permiso");
                oPermisos.each(function() {
                            sPermiso += $(this).text() + ",";
                })
                
                sPermiso = sPermiso.substr(0, sPermiso.length - 1);

                if (sPermiso.indexOf("2") > -1 ) {
                     $(wall).html($.fn.wall.options.html_nuevo_comentario.replace("ui-state-highlight","").replace('<input type="hidden" id="clave_publicacion_padre" name="clave_publicacion_padre" value="">',''));
                     $(wall).append("<div id='wallWait' style='width:100%; margin:0 auto 0 auto; clear: both; text-align: center;'><br /><br /><br /><br />Cargando...<br /><img src='img/loading.gif' /></div>");
                     //Se agrega el preview al textarea del formulario
                     $(wall).find("#publicacion").linkpreview();
                     //Se agrega la barra de herramientas al text area
                     $(wall).find(".sharetoolbar").fieldtoolbar({
                        app: "29"
                     });

                     //El botón compartir se agregó como parte de el toolbar
                     $(wall).find("#btnCompartir_" + $("#_ce_").val()).button().click(function() {
                        $.fn.wall.options.formaComentario = "commentForm_" + $.fn.wall.options.formSuffix //Es importante definir esta variable pues define el compartamiento de cómo procesar el resultado final
                        $("#btnCompartir_" + $("#_ce_")).disabled=true; 

                        var commentForm =$("#commentForm_" + $.fn.wall.options.formSuffix);

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
                        return false;                 

                       });

                       function verifyLinkPreview(jqForm, options) {
                            replyID=jqForm[0].id.split("_")[1];

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
                            replyID=jqForm[0].id.split("_")[1];

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
                                    alert("Ocurri&oacute; un problema al guardar el comentario (" + 
                                    error.text() + ".)");

                                    return false;    
                                }

                                sResultado=$(xmlComment).find("pk").text();                     
                                //Se convierte el text en div
                                aFechayhora=sDateTimeToString(new Date());
                                fecha=aFechayhora.split(" ")[0];
                                hora=aFechayhora.split(" ")[1];
                                fecha = fecha.split("/")[2]+"-"+fecha.split("/")[1]+"-"+fecha.split("/")[0];

                                if ($.fn.wall.options.formaComentario.indexOf("replyForm")>-1) {

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
                                                        .replace("%publicacion",$("#" + $.fn.wall.options.formaComentario + " #publicacion").val())
                                                        .replace(/%fecha/g,fecha + " " + hora)
                                                        .replace(/%clave_publicacion_padre/g,$.fn.wall.options.formaComentario.split("_")[2]));
                                } else {
                                    oNuevoComentario=$("<div id='comentario_"+sResultado+"' class='post'><div>").html($.fn.wall.options.
                                        html_comentario.
                                        replace("%foto",$("#avatar")[0].src).
                                        replace("%nombre",$("#_un_").html().replace("Bienvenid@ ","")).
                                        replace("%publicacion",$("#publicacion").val()).
                                        replace(/%fecha/g,fecha + " " + hora).
                                        replace(/%clave_publicacion_padre/g,sResultado).
                                        replace("%pk",sResultado).
                                        replace('name="clave_publicacion_padre" value=""','name="clave_publicacion_padre" value="' + sResultado + '"')
                                    );            
                                }

                                if ($.fn.wall.options.formaComentario.indexOf("replyForm")==-1) {
                                    $("#" + $.fn.wall.options.formaComentario + " #publicacion").val("").next().val("").next().html("").hide();
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
                               $(".closeLnkFiltro",oNuevoComentario).hover(
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
                                    $.post("control","$cmd=register&$ta=delete&$cf=299&$pk="+$(this).attr("pk"));
                                    $("#comentario_"+$(this).attr("pk")).remove();
                               });

                               if ($.fn.wall.options.formaComentario.indexOf("replyForm")>-1) {
                                     //sHtml=$("#trrespuesta_" + $.fn.post.options.formaComentario.split("_")[2]).html();
                                     //sRespuesta=$("#trrespuesta_" + $.fn.post.options.formaComentario.split("_")[2]).find("#publicacion").val();
                                     //$("#trrespuesta_" + $.fn.post.options.formaComentario.split("_")[2]).find("#publicacion").parent().html("<div>" + sRespuesta + "</div>");
                                    $(oNuevoComentario).insertBefore($("#trrespuesta_" + $.fn.wall.options.formaComentario.split("_")[2])); 
                                    $("#trrespuesta_" + $.fn.wall.options.formaComentario.split("_")[2]).find("#publicacion").val("")
                                    $("#trrespuesta_" + $.fn.wall.options.formaComentario.split("_")[2]).find("#publicacion")[0].id="respuesta_" + $.fn.wall.options.formaComentario.split("_")[2];
                                    $("#trrespuesta_" + $.fn.wall.options.formaComentario.split("_")[2]).find("#respuesta_" + $.fn.wall.options.formaComentario.split("_")[2])[0].name="respuesta_" + $.fn.wall.options.formaComentario.split("_")[2];
                               } else {
                                    $(oNuevoComentario).insertAfter($(wall).children()[0]);
                               }   

                               $("#lnkcomentar_" + sResultado).click(function(e){
                                    $("#trrespuesta_"+this.id.split("_")[1]).toggle();                
                               });

                               if ($.fn.wall.options.formaComentario.indexOf("replyForm")==-1) {
                                    $("#respuesta_" + sResultado).autosize().keydown(function(event) {
                                         if (event.which == 13) {
                                            event.preventDefault();
                                            replyID=this.id.split("_")[1];
                                            this.id="publicacion";
                                            this.name="publicacion"
                                            var commentForm =$("#replyForm_195_" + replyID);
                                            $.fn.wall.options.formaComentario = "replyForm_195_" + replyID;

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
                               }

                               if ($.fn.wall.options.formaComentario.indexOf("replyForm")>-1) {
                                    oNuevoComentario.find("#trrespuesta_" +$.fn.wall.options.formaComentario.split("_")[2]).hide(); }
                               else {
                                    oNuevoComentario.find("#trrespuesta_" + sResultado).hide(); 
                               }

                               oNuevoComentario.find("abbr.timeago").timeago();                    
                    }
                } else {
                    $(wall).html("");
                }
            
                posts=$(xml).find("html");
                htmlForPosts="";
                $.each(posts, function(){
                    htmlForPosts+=this.childNodes[0].data;                    
                });
               
               $("#wallWait").remove();
               
               if (sPermiso.indexOf("2") == -1 ) {
                   htmlForPosts=htmlForPosts.replace(new RegExp( '<td><a class="comentar_link" id="lnkcomentar_[0-9]+" href="#">Comentar</a>&nbsp;\\|&nbsp;<a class="compartir_link" id="lnkcompartir_[0-9]+" href="#">Compartir</a></td>','g'),'');
               }
               $(wall).append(replaceAll(htmlForPosts, "<html><![CDATA[", ""));
               $(wall).find(".trrespuesta").hide();
               $(wall).find("abbr.timeago").timeago();

                //Muestra el div de comentar si está oculto y establece el focus en el input
               $(".comentar_link").click(function(e){
                    $(".trrespuesta_"+this.id.split("_")[1]).toggle();
                    e.stopImmediatePropagation();
                });

                //Muestra el div de comentar si está oculto y establece el focus en el input
                $(".compartir_link").click(function(){
                    alert("Falta implementar");
                });

                $(".lnkContactProfile").click(function(e){
                    nPK= this.id.split("_")[1];
                    theTab="#tab_2_29_299_"+nPK;
                    if ($(theTab).length) {
                          //Selecciona el tab correspondiente
                          $("#tab_2_0").tabs("select", theTab);
                      }
                      else {
                          $("#tab_2_0").tabs( "add", theTab, this.text);
                          $("#tab_2_0").tabs( "select", theTab);
                          $(theTab).html("<div id='divCompartir_" + nPK + "' style='float: left;width: 70%;'>" +
                                         "<div id='divMuro_" + nPK + "' style='clear:both'>" + 
                                         "<br /><br /><br /><br /><br /><br />" + 
                                         "<div style='margin:0 auto 0 auto; width:100%; text-align: center'>Cargando<br /> <img src='img/loading.gif' /></div>" +
                                         "</div></div>" +
                                         "<div class='portlet' id='profile_"+ nPK + "' style='float: right;width: 20%;'>"+  
                                         "<div class='portlet-header' >" + this.text + "</div>" +
                                         "<div class='portlet-content'></div>" + 
                                         "</div>" 
                            );

                         $("#divMuro_" + nPK).wall({forma:349, pk:nPK, w:"clave_empleado=" + nPK });
                         $("#profile_"+ nPK).profiler({pk:nPK});
                    }
                    e.stopImmediatePropagation();
                });
            
               /* aqui empieza copia */ 
               $(".ui-icon-close", wall).hide();
               
               $(wall).find(".closeLnkFiltro").hover(
                function () {
                    $(this).parent().addClass('ui-state-default');
                    $(this).parent().addClass('ui-corner-all');
                    },
                function () {
                    $(this).parent().removeClass('ui-state-default');
                    $(this).parent().removeClass('ui-corner-all');
                    }
                );            
             
                $(wall).find(".closeLnkFiltro").click(function(){
                    if (!confirm('¿Desea borrar el comentario seleccionado?')) return false;
                    $.post("control","$cmd=register&$ta=delete&$cf=299&$pk=" + $(this).attr("pk"));
                    $($(this).parent().parent().parent().parent().parent().parent()[0]).remove();
                });
                
                $(wall).find(".replycomment").autosize().keydown(function(event) {
                    if (event.which == 13) {
                       event.preventDefault();
                       replyID=this.id.split("_")[1];
                       this.id="publicacion";
                       this.name="publicacion"
                       var commentForm =$("#replyForm_195_" + replyID);
                       $.fn.wall.options.formaComentario = "replyForm_195_" + replyID;

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
                
               /* aqui acaba copia */
               //Se crea la función anónima
               var paging =(function (e, oDiv){
                        //Borra el botón y lo susitutye por un mensaje de "Cargando..."
                        $.fn.wall.options.forma=$(oDiv).attr("forma");
                        $.fn.wall.options.pagina =$(oDiv).attr("pagina");
                        $.fn.wall.options.numeroDeRegistros = 20;
                        wall = $(oDiv).parent().parent();
                        waitingDiv = $(oDiv).parent();
                        $(oDiv).parent().html("Cargando...<br /><img src='img/loading.gif' />");
                        $.ajax(
                            {   
                            url: $.fn.wall.options.xmlUrl + "&$cf="+ $.fn.wall.options.forma + "&$ta=select&page=" +$.fn.wall.options.pagina +"&rows="+$.fn.wall.options.numeroDeRegistros+ "&sidx=fecha&sord=desc&$w="+$.fn.wall.options.w,
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
                                }

                                posts=$(xml).find("html");
                                htmlForPosts="";
                                $.each(posts, function(){
                                    htmlForPosts+=this.childNodes[0].textContent;                    
                                });
                               
                               $(waitingDiv).remove();
                               $(wall).append(replaceAll(htmlForPosts, "<html><![CDATA[", ""));               
                               
                               $(wall).find(".trrespuesta").hide();
                               $(wall).find("abbr.timeago").timeago();
                                //Muestra el div de comentar si está oculto y establece el focus en el input
                               $(wall).find(".comentar_link").click(function(e){
                                    $(".trrespuesta_"+this.id.split("_")[1]).toggle();
                                    e.stopImmediatePropagation();
                                });

                                //Muestra el div de comentar si está oculto y establece el focus en el input
                                $(wall).find(".compartir_link").click(function(){
                                    alert("Falta implementar");
                                });

                                $(wall).find(".lnkContactProfile").click(function(e){
                                    nPK= this.id.split("_")[1];
                                    theTab="#tab_2_29_299_"+nPK;
                                    if ($(theTab).length) {
                                          //Selecciona el tab correspondiente
                                          $("#tab_2_0").tabs("select", theTab);
                                      }
                                      else {
                                          $("#tab_2_0").tabs( "add", theTab, this.text);
                                          $("#tab_2_0").tabs( "select", theTab);
                                          $(theTab).html("<div id='divCompartir_" + nPK + "'  style='float: left;width: 70%;'>" +
                                                        "<div id='divMuro_" + nPK + "' style='clear:both'>" + 
                                                        "<br /><br /><br /><br /><br /><br />" + 
                                                        "  <div style='margin:0 auto 0 auto; width:100%; text-align: center'>Cargando<br /> <img src='img/loading.gif' /></div>" +
                                                        "  </div>" +
                                                        "</div>" + 
                                                        "<div class='portlet' id='profile_"+ nPK + "'  style='float: right; width: 20%;'>"+  
                                                        "<div class='portlet-header' >" + this.text + "</div>" +
                                                        "<div class='portlet-content'></div>" + 
                                                        "</div>"
                                                         
                                            );

                                            $("#divMuro_" + nPK).wall({forma:349, pk:nPK, w:"clave_empleado=" + nPK });
                                            $("#profile_"+ nPK).profiler({pk:nPK});
                                    }
                                    e.stopImmediatePropagation();
                                });

                                $(wall).find(".replycomment").autosize().keydown(function(event) {
                                    if (event.which == 13) {
                                       event.preventDefault();
                                       replyID=this.id.split("_")[1];
                                       this.id="publicacion";
                                       this.name="publicacion"
                                       var commentForm =$("#replyForm_195_" + replyID);
                                       $.fn.wall.options.formaComentario = "replyForm_195_" + replyID;

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
                                
                               //Se habilita el botón paginador 
                               $(wall).find(".btnPaginador").button().click(
                                    function (e) { 
                                        paging(e, this); 
                                    }
                                );
                               
                               e.stopImmediatePropagation();
                            },
                            error:function(xhr,err){
                                alert("Error al recuperar la forma");
                            }
                            });
                            e.stopImmediatePropagation();
                });
                
               //Se habilita el botón paginador 
               $(wall).find(".btnPaginador").button().click(function(e){
                   paging(e, this);
               });
               
               $("#_status_").val(""); 
            },
            error:function(xhr,err){
                alert("Error al recuperar las publicaciones");
                $("#_status_").val(""); 
            }
            });
        });
 
    };
}) (jQuery);
   
