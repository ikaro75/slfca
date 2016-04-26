/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
       
( function($) {
    $.fn.portletcontact = function(opc){

        $.fn.portletcontact.settings = {
            xmlContactoUrl : "control?$cmd=plain&$cf=302&$ta=select" // /ProyILCE/srvControl?$cmd=sesion
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.portletcontact.options = $.extend($.fn.portletcontact.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html("<div align='center' class='cargando' ><br /><br />Cargando informaci&oacute;n...  <br /><img src='img/loading.gif' /></div>")
             $.fn.portletcontact.ajax(obj);
             
        });

    };

    $.fn.portletcontact.ajax = function(obj){
        $("#_status_").val("Cargando portlet de contactos en la agenda");
         $.ajax(
            {
            url: $.fn.portletcontact.options.xmlContactoUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xmlContacto = new ActiveXObject("Microsoft.XMLDOM");
                 xmlContacto.async = false;
                 xmlContacto.validateOnParse="true";
                 xmlContacto.loadXML(data);
                 if (xmlContacto.parseError.errorCode>0) {
                        alert("Error de compilación xmlContacto:" + xmlContacto.parseError.errorCode +"\nParse reason:" + xmlContacto.parseError.reason + "\nLinea:" + xmlContacto.parseError.line);}
                }
                 else {
                    xmlContacto = data;}
                
                $("#_status_").val("Construyendo portlet de contactos en la agenda");
                oRegistro=$(xmlContacto).find("registro");
                obj.html("<input id='txtBuscaContacto' type='text' style='width: 80%;' /><button id='bntNuevoContacto' style='width:25px; height:25px;float: right;'><span style='margin-left: -8px;' class='ui-icon ui-icon-plus'></span></button>");
                oRegistro.each( function() {
                    sID=$(this).find("clave_empleado_contacto").text();
                    sDiv=$(this).find("clave_contacto").text().replace('class="griddiv"', 'id="cntPrt_' + sID + '" class="portletContactDiv"').replace('class="gridpic"', 'class="portletpic"');
                    obj.append(sDiv);
                });
                
                $("#txtBuscaContacto").keyup(function() {
                    var filter=$(this).val().toLowerCase();;
                    
                    if (filter=="") {
                        $(".portletContactDiv").show();
                    } else {
                        $(".portletContactDiv").each( function() {
                           if ($(this).text().toLowerCase().indexOf(filter)!=0) {
                               $(this).hide();
                           }                           
                        });
                    }    
                });
                
                $("#txtBuscaContacto").click(function() {
                    $("#txtBuscaContacto").focus();
                });
                
                $("#bntNuevoContacto").button().click(function() {
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
                            app: 30,
                            forma:302,
                            datestamp:"",
                            modo:"insert",
                            titulo: "Nuevo contacto",
                            columnas:1,
                            pk:0,
                            filtroForaneo:"",
                            height:"500",
                            width:"80%",
                            originatingObject: "",
                            showRelationships: true,
                            updateControl: "portletContacto"
                    });
                    
                }
                );
                
                $(".portletContactDiv").draggable( { cursor: 'move',
                                              helper: function ( event ) {
                                                return '<div id="draggableHelper" style="z-index:10000 ">' + $(event.currentTarget).html() + '</div>';
                                              } /*,
                                              stop: function handleDragStop( event, ui ) {
                                                var offsetXPos = parseInt( ui.offset.left );
                                                var offsetYPos = parseInt( ui.offset.top );
                                                alert( "Se compartió la actividad con " + $(event.target).html().substring($(event.target).html().indexOf(">")+1, $(event.target).html().length) );
                                              }*/ });
                $("#_status_").val("");
               
                
            },
            error:function(xhr,err){
                
                if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1) {
                    alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                    window.location='login.jsp';
                }else{
                    alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                    alert("responseText: "+xhr.responseText); 
                }
                
                $("#_status_").val("");
             }   
            });
    };

})(jQuery);
