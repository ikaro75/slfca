/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
       
( function($) {
    $.fn.portletdocument = function(opc){

        $.fn.portletdocument.settings = {
            xmlDocUrl : "control?$cmd=plain&$cf=311&$ta=select" // /ProyILCE/srvControl?$cmd=sesion
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.portletdocument.options = $.extend($.fn.portletdocument.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html("<div align='center' class='cargando' ><br /><br />Cargando informaci&oacute;n...  <br /><img src='img/loading.gif' /></div>")
             $.fn.portletdocument.ajax(obj);
             
        });

    };

    $.fn.portletdocument.ajax = function(obj){
         $("#_status_").val("Cargando datos de portlet de documentos en la agenda");
         
         $.ajax(
            {
            url: $.fn.portletdocument.options.xmlDocUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                 xmlDoc.async = false;
                 xmlDoc.validateOnParse="true";
                 xmlDoc.loadXML(data);
                 if (xmlDoc.parseError.errorCode>0) {
                        alert("Error de compilación xmlDoc:" + xmlDoc.parseError.errorCode +"\nParse reason:" + xmlDoc.parseError.reason + "\nLinea:" + xmlDoc.parseError.line);}
                }
                 else {
                    xmlDoc = data;}
                
                $("#_status_").val("Construyendo portlet de documentos en la agenda");
                oRegistro=$(xmlDoc).find("registro");
                                obj.html("<input id='txtBuscaDocumento' type='text' style='width: 80%;' /><button id='bntNuevoDocumento' style='width:25px; height:25px;float: right;'><span style='margin-left: -8px;' class='ui-icon ui-icon-plus'></span></button>");
                oRegistro.each( function() {
                    sID=$(this).find("clave_archivo").text();
                    sDiv="<div id='docPrt_" + sID + "' class='portletdivDoc'>" + $(this).find("archivo").text().substring(0,$(this).find("archivo").text().indexOf("</a>")-1) + "</div>";
                    obj.append(sDiv);
                });
                
               $("#txtBuscaDocumento").keyup(function() {
                    var filter=$(this).val().toLowerCase();
                    
                    if (filter=="") {
                        $(".portletdivDoc").show();
                    } else {
                        $(".portletdivDoc").each( function() {
                           if ($(this).text().toLowerCase().indexOf(filter)!=0) {
                               $(this).hide();
                           }                           
                        });
                    }    
                });
                
                $("#txtBuscaDocumento").click(function() {
                    $("#txtBuscaDocumento").focus();
                });
                
                $("#bntNuevoDocumento").button().click(function() {
                     $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando la forma...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                     });

                     $("#top").form({
                            app: 31,
                            forma:311,
                            datestamp:"",
                            modo:"insert",
                            titulo: "Nuevo documento",
                            columnas:1,
                            pk:0,
                            filtroForaneo:"",
                            height:"500",
                            width:"80%",
                            originatingObject: "",
                            showRelationships: true,
                            updateControl: "portletDocumento"
                    });
                    
                }
                );
                    
                $(".portletdivDoc").droppable(
                        {  activeClass: "ui-state-highlight",
                           accept: ".portletContactDiv, .portletGroupDiv", 
                           drop: function( event, ui ) {
                            var draggable = ui.draggable;
                            $( this ).addClass( "ui-state-highlight" );
                            
                            $("#divwait")
                             .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Compartiendo el documento...</p>")
                             .attr('title','Espere un momento por favor') 
                             .dialog({
                                 height: 140,
                                 modal: true,
                                 autoOpen: true,
                                 closeOnEscape:false
                           });
                     
                            if (draggable[0].className.indexOf("portletContactDiv")>-1) {
                                mensaje="Se compartió el documento '" + $(this).text() + "' con " + draggable.text();
                                urlPost="control?$cmd=register&$ta=insert&clave_archivo=" + this.id.split("_")[1] + "&$cf=304&$pk=0&clave_contacto="+draggable[0].id.split("_")[1] + "&fecha=" + escape("%ahora");
                            } else {
                                mensaje="Se compartió el documento '" + $(this).text() + "' con el grupo " + draggable.text();
                                urlPost="control?$cmd=register&$ta=insert&clave_archivo=" + this.id.split("_")[1] + "&$cf=304&$pk=0&clave_grupo="+draggable[0].id.split("_")[1] + "&fecha=" + escape("%ahora");
                                
                            } 
                   
                            $.ajax(
                                {
                                    url: urlPost,
                                    dataType: ($.browser.msie) ? "text" : "xml",
                                    success:  function(data){
                                         if (typeof data == "string") {
                                            xmlCnctGrupo = new ActiveXObject("Microsoft.XMLDOM");
                                            xmlCnctGrupo.async = false;
                                            xmlCnctGrupo.validateOnParse="true";
                                            xmlCnctGrupo.loadXML(data);
                                            if (xmlCnctGrupo.parseError.errorCode>0) {
                                                   alert("Error de compilación xml:" + xmlCnctGrupo.parseError.errorCode +"\nParse reason:" + xmlCnctGrupo.parseError.reason + "\nLinea:" + xmlCnctGrupo.parseError.line);}
                                            }
                                         else {
                                            xmlCnctGrupo = data;}
                                         
                                         $("#divwait").dialog("close")
                                         $("#divwait").dialog("destroy"); 
                                         
                                         var oError=$(xmlCnctGrupo).find("error");

                                         if (oError.length>0) {
                                            alert(oError.text());
                                         } else {
                                            alert(mensaje);
                                         }
                                        },
                                    error: function(xhr, err) {
                                        $("#divwait").dialog("close");
                                        $("#divwait").dialog("destroy"); 
                                        alert("Error al compartir documento");
                                    }
                            })                            
	
                            event.stopImmediatePropagation();
                           }
                        }
                    );
                        
                   $("#_status_").val("");     
            },
            error:function(xhr,err){
                if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1) {
                    alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                    window.location='login.jsp';
                }else{
                    alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                    alert("responseText: "+xhr.responseText);}
                $("#_status_").val("");
            }
            });
    };

})(jQuery);
