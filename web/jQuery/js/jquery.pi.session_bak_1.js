/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.sessionmenu = function(opc){

        $.fn.sessionmenu.settings = {
            xmlUrl : "sesion.jsp", //"/ProyILCE/resource/jsp/xmlSession.jsp" 
            empleado:"",
            nombre:"",
            apellido_paterno:"",
            apellido_materno:"",
            email:"",
            perfil:"",
            foto:"",
            ultima_app:""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.sessionmenu.options = $.extend($.fn.sessionmenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             $.fn.sessionmenu.ajax(obj);
             
        });

    };

    $.fn.sessionmenu.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.sessionmenu.options.xmlUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xml = new ActiveXObject("Microsoft.XMLDOM");
                 xml.async = false;
                 xml.validateOnParse="true";
                 xml.loadXML(data);
                 if (xml.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);}
                }
                 else {
                    xml = data;}
                
                obj.html($.fn.sessionmenu.handleSession(xml));
                
                /* Crea el widget botón */   
                $("#lnkSesion").button()
		.next()
			.button( {
				text: false,
				icons: {
					primary: "ui-icon-triangle-1-s"
				}
                }).click( function() {
				var menu = $(this).parent().next().show().position({
					my: "left top",
					at: "left bottom",
					of: this
				});
                                //Cuando se hace click en cualquier parte del documento se
                                //oculta el menú
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
                             
                
                $("#lnkConfiguracion").click(function() {

                    //Crea el control del tab
                    var $tabs = $('#tabs').tabs({
                    tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
                    });

                    if ($("#tabConfiguracion").length) {
                         //Selecciona el tab correspondiente
                         $tabs.tabs( "select", "#tabConfiguracion");    }
                    else {
                         $tabs.tabs( "add", "#tabConfiguracion", "Configuraci&oacute;n");
                         $tabs.tabs( "select", "#tabConfiguracion");

                         $("#tabConfiguracion").appgrid({app: "1",
                                              entidad: "1",
                                              pk:"0",
                                              wsParameters:"e.clave_empleado=" + $.fn.sessionmenu.options.empleado,
                                              titulo:"Par&aacute;metros de configuraci&oacute;n",
                                              inQueue:false,
                                              height:"70%",
                                              showFilterLink:false,
                                              editingApp:"1",
                                              openKardex:false,
                                              leyendas:["Nuevo par&aacute;metro", "Edición de par&aacute;metro"]});
                    }
                    
                    
                });
                
                $("#lnkLogout").click(function() {
                    window.location.href="srvLogout";
                }
                );
                    
                
                //Inicializa el escritorio
                $("#tabUser").desktop();
               
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.sessionmenu.handleSession = function(xml){
        var oReg=$(xml).find("registro:first");
        //Carga los datos del xml en la variable de coniguración
        $.fn.sessionmenu.options.empleado=oReg.find("clave_empleado").text();
        $.fn.sessionmenu.options.nombre=oReg.find("nombre").text();
        $.fn.sessionmenu.options.apellido_paterno=oReg.find("apellido_paterno").text();
        $.fn.sessionmenu.options.apellido_materno=oReg.find("apellido_materno").text();
        $.fn.sessionmenu.options.email=oReg.find("email").text();
        $.fn.sessionmenu.options.perfil=oReg.find("clave_perfil").text();
        $.fn.sessionmenu.options.foto=oReg.find("foto").text();

        //Construye html de acuerdo a configuración recuperada
        if ($.fn.sessionmenu.options.foto=="")
            $.fn.sessionmenu.options.foto='img/sin_foto.jpg'

        var sHtml='<a href="#" id="lnkSesion">Bienvenid@ ' + $.fn.sessionmenu.options.nombre + ' ' + $.fn.sessionmenu.options.apellido_paterno + '</a>'+
                  '<a href="#" >Seleccione una opcion</a>'+
                  '<div id="switcher"></div>';
        return sHtml;
    }

})(jQuery);