/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
       
( function($) {
    $.fn.sessionmenu = function(opc){

        $.fn.sessionmenu.settings = {
            xmlUrl : "control?$cmd=sesion", // /ProyILCE/srvControl?$cmd=sesion
            empleado:"",
            nombre:"",
            apellidos:"",
            email:"",
            perfil:"",
            foto:"",
            ultima_app:"",
            cambia_password: "0",
            area:""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.sessionmenu.options = $.extend($.fn.sessionmenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             obj.html("<div align='center' class='cargando' ><br /><br />Cargando informaci&oacute;n...  <br /><img src='img/loading.gif' /></div>")
             $.fn.sessionmenu.ajax(obj);
             
        });

    };

    $.fn.sessionmenu.ajax = function(obj){
         $("#_status_").val("Recuperando datos de la sesión");
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
                
                $("#_status_").val("Contruyendo menú de sesión");
                obj.html($.fn.sessionmenu.handleSession(xml));

                /* $("#lnkConfiguracion").click(function() {

                    //Crea el control del tab para la colección principal
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
                    
                }); */
                
                //Abre dialogo de edición de imagen
                $("#avatar").click(function() {
                    $("#divwait")
                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                    .attr('title','Espere un momento por favor') 
                    .dialog({
                        height: 140,
                        modal: true,
                        autoOpen: true,
                        closeOnEscape:false
                    });
                    
                    $("body").form({
                        app: 1,
                        forma:6,
                        datestamp:obj.attr("datestamp"),
                        modo:"update",
                        titulo: "Consulta ",
                        columnas:1,
                        pk:$("#_ce_").val(),
                        filtroForaneo:"2=clave_aplicacion=1",
                        height:"500",
                        width:"80%",
                        originatingObject: "",
                        updateControl:"avatar"
                    });                    
                    /*if ($(this).attr("snow")=="0") {
                         $("#divwait")
                        .html("<br /><h1>!!El equipo de desarrollo les desea felices fiestas!!</h1><p style='text-align: center'><img src='img/feliz_navidad.jpg' /</p>")
                        .attr('title','Feliz navidad') 
                        .dialog({
                                height: 500,
                                modal: true,
                                autoOpen: true,
                                closeOnEscape:false
                        });
                        $(document).snowfall({round : true, minSize: 5, maxSize:8}); // add rounded
                        $(this).attr("snow","1");
                    }
                    else {
                        $(document).snowfall('clear'); 
                        $(this).attr("snow","0");
                    }*/
                });
                
                //Comprueba si se tiene el tab de inicio
                if ($("#tabInicio").length>0) { 
                    //Está comentado puesto que no se está detectando si ya se cargó previamente el desktop; hace falta encontrar una forma más confiable de verifiación
                    $("#_status_").val("Inicializando escritorio");
                    $("#tabInicio").desktop();
                }
                
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

    $.fn.sessionmenu.handleSession = function(xml){
        oRegistro=$(xml).find("registro");
            //Carga los datos del xml en la variable de coniguración
        $.fn.sessionmenu.options.empleado=oRegistro.find("clave_empleado").text();
        $.fn.sessionmenu.options.nombre=oRegistro.find("nombre").text();
        $.fn.sessionmenu.options.apellidos=oRegistro.find("apellidos").text();
        $.fn.sessionmenu.options.email=oRegistro.find("email").text();
        $.fn.sessionmenu.options.perfil=oRegistro.find("clave_perfil").text();
        $.fn.sessionmenu.options.foto=oRegistro.find("foto").text();
        $.fn.sessionmenu.options.area=oRegistro.find("clave_area").text();
        $.fn.sessionmenu.options.cambia_password=oRegistro.find("cambia_password").text();
        
        //Construye html de acuerdo a configuración recuperada
        if ($.fn.sessionmenu.options.foto=="")
            $.fn.sessionmenu.options.foto='img/sin_foto.jpg'
        var sHtml='<table border="0" cellspacing="0" cellpadding="0">'+
                  '<tr>' +
                  '<td valign="top">' +
                  '<table border="0" align="center" cellpadding="5" cellspacing="5">' +
                  '<tr>'+
                  '<td class="session_menu">'+
                  '<div align="right"  class="ui-widget">'+
                  '<span id="_un_" class="ui-state-default session_menu" cp="'+ $.fn.sessionmenu.options.cambia_password +'"><strong>Bienvenid@ '+ $.fn.sessionmenu.options.nombre +'</strong></span><br/>'+
                  '<a class="ui-state-default session_menu" href="control?$cmd=logout" id="lnkCerrarSesion">Cerrar sesi&oacute;n </a>'+
                  '</div></td>' +
                  '</tr>'+
                  '</table>'+
                  '</td>' +
                  '<td><img src="' + $.fn.sessionmenu.options.foto + '" width="75" height="86" border="1" id="avatar" snow="0"/></td>' +
                  '</tr>'+
                '</table>';
        return sHtml;
    }

})(jQuery);
