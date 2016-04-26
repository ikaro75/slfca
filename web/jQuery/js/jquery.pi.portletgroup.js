/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
       
( function($) {
    $.fn.portletgroup = function(opc){

        $.fn.portletgroup.settings = {
            xmlGrupoUrl : "control?$cmd=plain&$cf=305&$ta=select" // /ProyILCE/srvControl?$cmd=sesion
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.portletgroup.options = $.extend($.fn.portletgroup.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html("<div align='center' class='cargando' ><br /><br />Cargando informaci&oacute;n...  <br /><img src='img/loading.gif' /></div>")
             $.fn.portletgroup.ajax(obj);
             
        });

    };

    $.fn.portletgroup.ajax = function(obj){
        $("#_status_").val("Cargando portlet de grupos de la agenda");     
         $.ajax(
            {
            url: $.fn.portletgroup.options.xmlGrupoUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xmlGrupo = new ActiveXObject("Microsoft.XMLDOM");
                 xmlGrupo.async = false;
                 xmlGrupo.validateOnParse="true";
                 xmlGrupo.loadXML(data);
                 if (xmlGrupo.parseError.errorCode>0) {
                        alert("Error de compilación xmlGrupo:" + xmlGrupo.parseError.errorCode +"\nParse reason:" + xmlGrupo.parseError.reason + "\nLinea:" + xmlGrupo.parseError.line);}
                }
                 else {
                    xmlGrupo = data;}
                
                $("#_status_").val("Construyendo portlet de grupos de la agenda"); 
                oRegistro=$(xmlGrupo).find("registro");
                obj.html("<input id='txtBuscaGrupo' type='text' style='width: 80%;' /><button id='bntNuevoGrupo' style='width:25px; height:25px;float: right;'><span style='margin-left: -8px;' class='ui-icon ui-icon-plus'></span></button>");
                oRegistro.each( function() {
                    sID=$(this).find("clave_grupo").text();
                    sDiv="<div id='grpPrt_" + sID + "' class='portletGroupDiv'><a href='#' id='lnkEditGroup_" + sID + "' class='edit_group'>" + $(this).find("grupo").text() + "</a></div>";
                    obj.append(sDiv);
                });
                
                $("#txtBuscaGrupo").keyup(function() {
                    var filter=$(this).val().toLowerCase();
                    
                    if (filter=="") {
                        $(".portletGroupDiv").show();
                    } else {
                        $(".portletGroupDiv").each( function() {
                           if ($(this).text().toLowerCase().indexOf(filter)!=0) {
                               $(this).hide();
                           }                           
                        });
                    }    
                });
                
                $("#txtBuscaGrupo").click(function() {
                    $("#txtBuscaGrupo").focus();
                });
                
                $("#bntNuevoGrupo").button().click(function() {
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
                            app: 32,
                            forma:305,
                            datestamp:"",
                            modo:"insert",
                            titulo: "Nuevo grupo",
                            columnas:1,
                            pk:0,
                            filtroForaneo:"",
                            height:"500",
                            width:"80%",
                            originatingObject: "",
                            showRelationships: true,
                            updateControl: "portletGrupo"
                    });
                    
                }
                );
                    
                $(".edit_group").click(function(event) {
                    $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando la forma...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                     });
                     ID=this.id.split("_")[1];
                     $("#top").form({
                            app: 32,
                            forma:305,
                            datestamp:"",
                            modo:"update",
                            titulo: "Edición de grupo",
                            columnas:1,
                            pk:ID,
                            filtroForaneo:"2=clave_aplicacion=32&3=",
                            height:"500",
                            width:"80%",
                            originatingObject: "",
                            showRelationships: true,
                            updateControl: ""
                    });
                    event.stopImmediatePropagation();
                });
                
                $(".portletGroupDiv").draggable( { cursor: 'move',
                                              helper: function ( event ) {
                                                return '<div id="draggableHelper" style="z-index:10000 ">' + $(event.currentTarget).html() + '</div>';
                                    }}).droppable(
                                        {  activeClass: "ui-state-highlight",
                                           accept: ".portletContactDiv", 
                                           drop: function( event, ui ) {
                                            var grupo=$(this).text();
                                            var draggable = ui.draggable;
                                            $( this ).addClass( "ui-state-highlight" );
                                            
                                            $.ajax(
                                            {
                                                url: "control?$cmd=register&$ta=insert&$cf=306&$pk=0&$ta=insert&clave_empleado_contacto=" +  draggable[0].id.split("_")[1] + "&clave_grupo=" + this.id.split("_")[1],
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

                                                     var oError=$(xmlCnctGrupo).find("error");

                                                     if (oError.length>0) {
                                                        alert(oError.text());
                                                     } else {
                                                        alert("Se agregó al contacto " + draggable.text() + " en el grupo " + grupo);
                                                     }
                                                    },
                                                error: function(xhr, err) {
                                                    alert("Error al compartir la actividad");
                                                }
                                            });                                             
                                            
                                        event.stopImmediatePropagation();
                                   }
                                }
                            );


                $(".fc-event-inner").droppable(
                    {  activeClass: "ui-state-highlight",
                       accept: ".portletdiv",
                       drop: function( event, ui ) {
                        /*$( this ).addClass( "ui-state-highlight" );*/
                        alert("muy bien");
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
