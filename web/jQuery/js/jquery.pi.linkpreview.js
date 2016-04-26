( function($) {
    $.fn.linkpreview = function(opc){

        $.fn.linkpreview.settings = {
            xmlUrl : "preview_xml.jsp", // /ProyILCE/srvControl?$cmd=sesion
            xmlPreviewDefaultText: "¿Qué deseas compartir con la comunidad?",
            timestamp :""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.linkpreview.options = $.extend($.fn.linkpreview.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             $.fn.linkpreview.options.timestamp = sDateTime(new Date());
             $(this).val($.fn.linkpreview.options.xmlPreviewDefaultText);
             $("<input type='hidden' style='width: 400px' id='linkpreview_" + $.fn.linkpreview.options.timestamp + "'/><div id='fd_" + $.fn.linkpreview.options.timestamp +"' style='clear: both; width: 99%; height:150px;' /><div class='sharetoolbar' tipo='share_toolbar' control='" + this.id + "' style='background-color:  #CFCECE; clear:both; width: 99%;'/>").insertAfter(this);
    
            $(this).attr("style","heigth:20px").addClass("sugerencia").css({
                "width":"98%"
            });
            
            $("#fd_"+ $.fn.linkpreview.options.timestamp).hide();
            $(this)
              .focus( function() {
                $(this).animate({
                        height: '40px'
                    }, "slow");
                
                if ($(this).val()==$.fn.linkpreview.options.xmlPreviewDefaultText && $(this).attr("class")=='comment sugerencia' ) {
                    $(this).val("");
                    $(this).removeClass("sugerencia");
                }
              })
              .keyup(function() {
                var url = $(this).val().match(/(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/);
                if (url==null) {
                     //$("#fd_" + + $.fn.linkpreview.options.timestamp).html("");
                } else {
                    
                    if (url.length > 0) {

                        var txt=url[0];
                        youtubeLink=false;
                        if (txt.indexOf("http://www.youtube.com/watch?v=")>-1) youtubeLink = true;
                        
                         $("#linkpreview_" + $.fn.linkpreview.options.timestamp).val(url[0]);
                         $("#fd_"+ $.fn.linkpreview.options.timestamp).show("fast").html("<br /><br /><br /><br /><div style='margin:0 auto 0 auto; width:100%; text-align: center'>Cargando <img src='img/loading.gif' /></div>");
                         var getLink = $("#linkpreview_" + $.fn.linkpreview.options.timestamp).attr('value');
                        
                         $.ajax({   
                             url: 'preview_xml.jsp?link='+ getLink +'&tip=x' ,
                             dataType: ($.browser.msie) ? "text" : "xml",
                             success:  function(data){
                                 if (typeof data == "string") {
                                     xmlLinkPreview = new ActiveXObject("Microsoft.XMLDOM");
                                     xmlLinkPreview.async = false;
                                     xmlLinkPreview.validateOnParse="true";
                                     xmlLinkPreview.loadXML(data);
                                     if (xmlLinkPreview.parseError.errorCode>0) {
                                         alert("Error de compilación xml:" + xmlLinkPreview.parseError.errorCode +"\nParse reason:" + xmlLinkPreview.parseError.reason + "\nLinea:" + xmlLinkPreview.parseError.line);
                                     }
                                 }
                                 else {
                                     xmlLinkPreview = data;
                                 }
                                 
                                 var sHtml="<div class='jq_content_inner'>%contenido</div>"
                                 /*Verifica el estatus de error*/
                                 var oError=$(xmlLinkPreview).find("error");
                                 if (oError.length>0) {
                                     sHtml=sHtml.replace("%contenido",oError.text());
                                 } else {
                                     sImagesHtml=""
                                     
                                     if (youtubeLink) {
                                        imagenes=$(xmlLinkPreview).find("ogimage")
                                        
                                        if (imagenes.length>0) 
                                            sImagesHtml = "<a target='_blank' href='" + getLink + "'><img style='margin: 5px;vertical-align: middle; width:210px; heigth: 110px' alt='Imagen' id='jqImage' src='" + imagenes.text() + "'></a>";
                                        
                                     } else {
                                        imagenes=$(xmlLinkPreview).find("src");
                                        i = 0;
                                        $.each(imagenes, function(){
                                            if (i==0) {
                                               sImagesHtml = "<img style='margin: 5px;vertical-align: middle; width=200px' alt='Imagen' id='jqImage' src='" + $(this).text() + "'><div id='jq_allImages'>";
                                            } else {
                                               sImagesHtml += "<img alt='Imagen' style='display: none' src='" + $(this).text() + "' id='Image" + i + "'>";
                                            }
                                            i++;

                                        });

                                        sImagesHtml += "</div>";
                                     }
                                     
                                     sHtml=sHtml.replace('%contenido','<div style="display: inline-block; overflow: hidden;background-color: white;vertical-align: middle; margin-left: 5px; float: left;" id="jqImageDIV">' + sImagesHtml + '</div>' + 
                                             '<div style="overflow: hidden; margin-left: 10px;"><b><a target="_blank" style="font-size: 12px;color: gray;text-decoration: none; margin-bottom: 5px" href="' + getLink + '">' +
                                             $(xmlLinkPreview).find("titulo").text() + '</a></b></div>'+
                                             '<div style="display: inline-block;width: 280px; vertical-align: top; margin-right: 10px;">' + $(xmlLinkPreview).find("descripcion").text() + '<br />' +
                                             '<div id="jq_console" max="' + $(xmlLinkPreview).find("src").length + '">' +
                                                '<div id="jq_back-next"><br /><input type="text" disabled="true" style="border: none;background-color: transparent;width: 50px" value="1 de ' + $(xmlLinkPreview).find("src").length + '" id="count">' +
                                                '<input type="image" name="backButton_' +  $.fn.linkpreview.options.timestamp + '" id="backButton_' +  $.fn.linkpreview.options.timestamp + '" src="img/back.png" value="0">'+
                                                '<input type="image" name="nextButton_' +  $.fn.linkpreview.options.timestamp + '" id="nextButton_' +  $.fn.linkpreview.options.timestamp + '" src="img/next.png" value="' + ($(xmlLinkPreview).find("src").length>1?2:1)  + '">'+
                                                '</div>' +
                                                '<input type="checkbox" id="jq_attach_'  + $.fn.linkpreview.options.timestamp + '"/>Sin imagen en miniatura<br /><br />' +
                                             '</div></div>');            
                                    
                                 }
                                 
                                 $("#fd_"+ $.fn.linkpreview.options.timestamp).html(sHtml);
                                 
                                 $("#jq_attach_"+ $.fn.linkpreview.options.timestamp).click(
                                    function() {
                                       if ($(this).attr("checked")=="checked") {
                                           $("#jqImage").hide();
                                           $("#jq_back-next").hide();
                                       } else {
                                           $("#jqImage").show();
                                           $("#jq_back-next").show();
                                       }
                                    }
                                 );
                                     
                                 $("#backButton_"+ $.fn.linkpreview.options.timestamp).click(
                                    function() {
                                       timestamp=this.id.split("_")[1];
                                       
                                       var backValue = $(this).val();
                                       var decNext;
                                       var decBack;
                                       var max = $("#jq_console").attr("max");
                                       
                                       if (backValue==0) {
                                           backValue=max;
                                           decNext=1;
                                       } else {
                                           decNext=backValue+2;
                                       }    
                                               
                                      $("#count").val(backValue+" de "+max);
                                       decBack = backValue - 1;
                                       var getImage = $('#Image'+ decBack).attr("src");                       
                                       
                                       $("#backButton_" + timestamp).val(decBack);
                                       $("#nextButton_" + timestamp).val(decNext);
                                       $("#jqImage").attr("src",getImage);
                                       return false;
                                    }
                                 );
                                 
                                 $("#nextButton_"+ $.fn.linkpreview.options.timestamp).click(
                                    function() {
                                       timestamp=this.id.split("_")[1];
                                       
                                       var nextValue = $(this).val();
                                       var decNext;
                                       var decBack;
                                       var max = $("#jq_console").attr("max");
                                       
                                       if (nextValue>max) {
                                           decNext=1;
                                           decBack=max;
                                       } else {
                                           decNext=nextValue;
                                           decBack = nextValue-1;
                                       }  
                                               
                                      $("#count").val(decNext+" de "+max);
                                       var getImage = $('#Image'+ decNext).attr("src");
                                       decNext++;                        
                                       
                                       $("#backButton_" + timestamp).val(decBack);
                                       $("#nextButton_" + timestamp).val(decNext);
                                       $("#jqImage").attr("src",getImage);
                                       return false;
                                    }
                                 );
                                                        
                             },
                             error:function(xhr,err){
                                 alert("Error al recuperar la forma");
                             }
                             });
                            //$("#fd_"+ $.fn.linkpreview.options.timestamp).load('preview.jsp?link=' + getLink +'&'+'tip=x');
                        }
                    }
            });
           
        });

    };

})(jQuery);
