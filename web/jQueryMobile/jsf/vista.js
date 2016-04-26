var host = ""; 
var cmd = "plainbypage";
var cf = 2;
var ta = "select";
var pagina = 1;
var rows = 20;
var sidx = "";
var sord = "desc";
var w = "";
var om = "";
var app = 1;
var busquedaRapida="";
var encabezadoLv="";

$(document).bind('pageinit', function() {
    /*Tokeniza los parámetros de la dirección para procesar la solicitud */
    var aHash = window.location.search.substr(1, window.location.search.lenght).split("&");

    for (i = 0; i < aHash.length; i++) {
        
        if (aHash[i].split("=")[0] == "app") {
            app = aHash[i].split("=")[1];
        }
        if (aHash[i].split("=")[0] == "$cmd") {
            cmd = aHash[i].split("=")[1];
        }

        if (aHash[i].split("=")[0] == "$cf") {
            cf = aHash[i].split("=")[1];
        }

        if (aHash[i].split("=")[0] == "page") {
            pagina = aHash[i].split("=")[1];
        }

        if (aHash[i].split("=")[0] == "rows") {
            rows = aHash[i].split("=")[1];
        }

        if (aHash[i].split("=")[0] == "sidx") {
            sidx = aHash[i].split("=")[1];
        }

        if (aHash[i].split("=")[0] == "sord") {
            sord = aHash[i].split("=")[1];
        }

        if (aHash[i].split("=")[0] == "w") {
            w = aHash[i].split("=")[1];
        }

        if (aHash[i].split("=")[0] == "$om") {
            om = aHash[i].split("=")[1];
        }
    }

    $("#_host_").val(host);
    $("#_cmd_").val(cmd);
    $("#_cf_").val(cf);
    $("#_pagina_").val(pagina);
    $("#_rows_").val(rows);
    $("#_sidx_").val(sidx);
    $("#_sord_").val(sord);
    $("#_w_").val(w);
    $("#_om_").val(om);    
    /* Recupera las aplicaciones a las que tiene acceso el usuario */
    cargaMenu();
    /*Recupera el catálogo solicitado */
    cargaCatalogo();
});

function cargaMenu() {

$.ajax(
    {
    url: "control?$cmd=menu&app="+app,
    dataType: ($.browser.msie) ? "text" : "xml",
    success:  function(data){
         if (typeof data == "string") {
         xmlMenu = new ActiveXObject("Microsoft.XMLDOM");
         xmlMenu.async = false;
         xmlMenu.validateOnParse="true";
         xmlMenu.loadXML(data);
         if (xmlMenu.parseError.errorCode>0) {
                alert("Error de compilación xml:" + xmlMenu.parseError.errorCode +"\nParse reason:" + xmlMenu.parseError.reason + "\nLinea:" + xmlMenu.parseError.line);}
        }
         else {
            xmlMenu = data;}

        html=$(xmlMenu).find("html")[0].childNodes[0].data;

        $('nav#menu').html(html).mmenu({
            extensions: ['effect-slide-menu', 'pageshadow'],
            searchfield: {add:true , placeholder:"Escriba su b&uacute;squeda", noResults:"No se encontraron resultados"},
            counters: true,
            navbar: {
                title: 'Men&uacute;'
            }, 
            navbars : {
                height 	: 3,
			content : [ 
				'<img id="userAvatar" src="img/sin_foto.jpg" style="width: 60px;height: 60px;padding: 2px; border:1px solid;display: block; margin-left: auto;margin-right: auto" />',
                                '<a id="userName" style="margin-top:0px; padding: 0px;">Demo</a>',
				'<a href="#/" id="lnkCerrarSesion" style="margin-top:0px; padding: 0px;">Cerrar sesi&oacute;n</a>'
			]
            }
                
        });
        
        $("#lnkCerrarSesion").click(function() {
            $.post("control?$cmd=logout");
            location.href = "login.html";
        });

    },
    error:function(xhr,err){
        if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1) {
            alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
            window.location='login.jsp';
        }else{
            alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
            alert("responseText: "+xhr.responseText);
        }
    }
    });
    
}

function cargaCatalogo() {
    
    $("#btnNuevoRegistro").click(function() {
        if ($("#registro_" + $("#_cf_").val() + "_0").length == 0) {
            $("body").append('<div data-role="page"  style="width:100%;" id="registro_' + $("#_cf_").val() + '_0">' +
                    '<div data-role="header" data-theme="a" ><h1 id="titulo_header_' + $("#_cf_").val() + '_0">Registro</h1></div>' +
                    '<div data-role="content" >' +
                    '<h2 id="titulo_registro_' + $("#_cf_").val() + '_0"></h2>' +
                    '<h3 id="subtitulo_registro_' + $("#_cf_").val() + '_0"></h2>' +
                    '<div style="margin: 0px auto; text-align: center;"><img class="ima_tam" id="imagen_' + $("#_cf_").val() + '_0" src="" /></div>' +
                    '<div id="divContenido_' + $("#_cf_").val() + '_0"></div>' +
                    '<div id="divLink_' + $("#_cf_").val() + '_0"></div>' +
                    '</div>');
        }

        $("#divContenido_" + $("#_cf_").val() + "_0").form_mobile({app: 1,
            forma: $("#_cf_").val(),
            modo: "insert",
            columnas: 1,
            pk: 0,
            filtroForaneo: "2=clave_aplicacion=1",
            height: "500",
            width: "800px",
            originatingObject: "",
            updateControl: "lvCatalogo"
        });

        //$.mobile.changePage('#registro_' + $("#_cf_").val() + '_0', {transition: "flip", role: "page"});
    });
    
    $("#btnBusqueda").click(function() {
        if ($("#registro_" + $("#_cf_").val() + "_0").length == 0) {
            $("body").append('<div data-role="page"  style="width:100%;" id="registro_' + $("#_cf_").val() + '_0">' +
                    '<div data-role="header" data-theme="a" ><h1 id="titulo_header_' + $("#_cf_").val() + '_0">Registro</h1></div>' +
                    '<div data-role="content" >' +
                    '<h2 id="titulo_registro_' + $("#_cf_").val() + '_0"></h2>' +
                    '<h3 id="subtitulo_registro_' + $("#_cf_").val() + '_0"></h2>' +
                    '<div style="margin: 0px auto; text-align: center;"><img class="ima_tam" id="imagen_' + $("#_cf_").val() + '_0" src="" /></div>' +
                    '<div id="divContenido_' + $("#_cf_").val() + '_0"></div>' +
                    '<div id="divLink_' + $("#_cf_").val() + '_0"></div>' +
                    '</div>');
        }

        $("#divContenido_" + $("#_cf_").val() + "_0").form_mobile({app: 1,
            forma: $("#_cf_").val(),
            modo: "lookup",
            columnas: 1,
            pk: 0,
            filtroForaneo: "2=clave_aplicacion=1",
            height: "500",
            width: "800px",
            originatingObject: "",
            updateControl: "lvCatalogo"
        });

        //$.mobile.changePage('#registro_' + $("#_cf_").val() + '_0', {transition: "flip", role: "page"});        
    }); 
    
    $("#txtBusca").keyup(function() {
        filtroPerron();
    });

    cargaXML();
}
    
function cargaXML() {
        var nocache = new Date().getTime();
        
        setTimeout(function(){$.mobile.loading('show');},1);
        
        $.ajax({
        url: $("#_host_").val() + "control?$cmd=" + $("#_cmd_").val() + "&$cf=" + $("#_cf_").val() + "&$ta=select&page=" + $("#_pagina_").val() + "&rows=" + $("#_rows_").val() + "&sidx=" + $("#_sidx_").val() + "&sord=" + $("#_sord_").val() + "&$w=" + escape($("#_w_").val()) + "&cache=" + nocache,
        dataType: ($.browser.msie) ? "text" : "xml",
        cache: false,
        success: function(data) {

            if (typeof data == "string") {
                xmlCatalogo = new ActiveXObject("Microsoft.XMLDOM");
                xmlCatalogo.async = false;
                xmlCatalogo.validateOnParse = "true";
                xmlCatalogo.loadXML(data);
                if (xmlCatalogo.parseError.errorCode > 0)
                    alert("Error de compilación xml:" + xmlCatalogo.parseError.errorCode + "\nParse reason:" + xmlCatalogo.parseError.reason + "\nLinea:" + xmlCatalogo.parseError.line);
            }
            else
                xmlCatalogo = data;

            //Trae los permisos
            sPermiso = "";
            oPermisos = $(xmlCatalogo).find("clave_permiso");
            oPermisos.each(function() {
                sPermiso += $(this).text() + ",";
            })
            sPermiso = sPermiso.substr(0, sPermiso.length - 1)
            $("#_security_").val(sPermiso);

            if (sPermiso.indexOf("2") == -1) {
                $("#btnNuevoRegistro").hide();
            }
            total_registros = Math.floor($(xmlCatalogo).find("configuracion_forma").find("registros").text());
            var html = $("#lvCatalogo").children().length==0?'<li data-role="list-divider" id="liEncabezado">' + $(xmlCatalogo).find("forma").text() + '<span id="spnContador" class="ui-li-count">' + total_registros + '</span></li>':'';
            registros = $(xmlCatalogo).find("registro");
            $("#_busquedarapida_").val($(xmlCatalogo).find("configuracion_forma").find("busqueda_rapida").text());
            if (registros.length > 0) {    
                $.each(registros, function() {
                    if ($(this).children()[0].textContent == "") {
                         $.mobile.loading( "hide" );
                        return;
                    }
                    
                    if ($("#lnkRegister_" + +$("#_cf_").val() + "_" + $(this).children()[0].textContent).length == 0)
                        html += '<li><a class="lnkRegister" id="lnkRegister_' + $("#_cf_").val() + "_" + $(this).children()[0].textContent + '" href="#" data-rel="page" data-transition="flip"><h2 style="margin-left:1em">' + $(this).children()[1].textContent + '</h2><p style="margin-left:1em">' + $(this).children()[2].textContent + '</p><p class="ui-li-aside">' + ($(this).children().length>4?$(this).children()[3].textContent:"") + '</p></a></li>';                   
                    
                });

                $("#lvCatalogo").append(html).listview('refresh');

                //Verifica si hay páginas pendientes 
                total_paginas = Math.floor($(xmlCatalogo).find("configuracion_forma").find("total_paginas").text());
                if (Math.floor($("#_pagina_").val()) < total_paginas) {
                    if (!$("#divPaginador").length) {
                        $("#catalogo").append("<div id='divPaginador'><button id='btnPaginador' class='ui-btn'>Mostrar m&aacute;s registros</button><div>");
                    }
                } else {
                    $("#divPaginador").hide();
                }

                $("#btnPaginador").click(function() {
                    $("#_pagina_").val(Math.floor($("#_pagina_").val()) + 1);
                    cargaXML();
                });

                $(".lnkRegister").unbind('click').click(function() {
                    var cf = this.id.split("_")[1];
                    var id = this.id.split("_")[2];
                    if ($("#registro_" + cf + "_" + id).length == 0) {
                        $("body").append('<div data-role="page"  style="width:100%;" id="registro_' + cf + '_' + id + '">' +
                                '<div data-role="header" data-theme="a" class="header">' +
                                '<h1 id="titulo_header_' + cf + '_' + id + '">Registro</h1><button class="ui-btn ui-icon-back ui-btn-icon-right" id ="btnRegresar">Regresar</button></div>' +
                                '<div role="main" class="ui-content" >' +
                                '<h2 id="titulo_registro_' + cf + '_' + id + '"></h2>' +
                                '<h3 id="subtitulo_registro_' + cf + '_' + id + '"></h2>' +
                                '<div style="margin: 0px auto; text-align: center;"><img class="ima_tam" id="imagen_' + cf + '_' + id + '" src="" /></div>' +
                                '<div id="divContenido_' + cf + '_' + id + '"></div>' +
                                '<div id="divLink_' + cf + '_' + id + '"></div>' +
                                '</div>');
                    }

                    $.ajax({
                        url: $("#_host_").val() + "control?$cmd=" + $("#_cmd_").val() + "&$cf=" + cf + "&$ta=update&$pk=" + id,
                        dataType: ($.browser.msie) ? "text" : "xml",
                        success: function(data) {
                            if (typeof data == "string") {
                                xmlRegistro = new ActiveXObject("Microsoft.XMLDOM");
                                xmlRegistro.async = false;
                                xmlRegistro.validateOnParse = "true";
                                xmlRegistro.loadXML(data);
                                if (xmlRegistro.parseError.errorCode > 0)
                                    alert("Error de compilación xml:" + xmlRegistro.parseError.errorCode + "\nParse reason:" + xmlRegistro.parseError.reason + "\nLinea:" + xmlRegistro.parseError.line);
                            }
                            else
                                xmlRegistro = data;

                            $("#titulo_header_" + $("#_cf_").val() + "_" + id).text($(xmlRegistro).find("configuracion").find("forma").text());

                            if ($("#_om_").val() == "dialog") {
                                $("#titulo_registro_" + cf + "_" + id).html($(xmlRegistro).find("registro").children()[1].textContent);
                                $("#subtitulo_registro_" + cf + "_" + id).html($(xmlRegistro).find("registro").children()[2].textContent);
                                $("#imagen_" + cf + "_" + id).attr("src", $(xmlRegistro).find("registro").children()[4].textContent).addClass("ima_tam");
                                $("#divContenido_" + cf + "_" + id).html($(xmlRegistro).find("registro").children()[3].textContent);
                                $("#divLink_" + cf + "_" + id).html("<br>" + $(xmlRegistro).find("registro").children()[5].textContent);
                            } else {
                                $("#divContenido_" + cf + "_" + id).form_mobile({app: 1,
                                    forma: cf,
                                    modo: "update",
                                    columnas: 1,
                                    pk: id,
                                    filtroForaneo: "2=clave_aplicacion=1",
                                    height: "500",
                                    width: "800px",
                                    originatingObject: "",
                                    updateControl: "listview"
                                });
                            }

                            //$.mobile.changePage('#registro_' + cf + '_' + id, {transition: "flip", role: "page"});
                        }
                    });

                }).live('vmouseover', function() {
                    ;
                    $($(this).find(".shareLnkFiltro")[0]).show();
                }).live('vmouseout', function() {
                    ;
                    $($(this).find(".shareLnkFiltro")[0]).hide();
                });

                    $(".shareLnkFiltro").unbind('click').click(function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                         $("#compartir_forma").val($("#_cf_").val());
                         $("#compartir_registro").val($(this).attr("pk"));
                        $.mobile.changePage("#compartirConContactos", {transition: "flip", role: "dialog"});
                    });
                } else {
                    html += '<li><p>No se encontraron resultados</p></li>';
                     $("#lvCatalogo").append(html).listview('refresh');    
                }
                
                setTimeout(function(){$.mobile.loading('hide');},300);
            }
    });
}
    
    function filtroPerron() {
        value = $("#txtBusca").val();
            
        if ($("#_busquedarapida_").val()=="null") {
            $("#pPopup").html("No est&aacute; definida la b&uacute;squeda r&aacute;pida").parent().popup( "open" );
            return;
        }

        $ul = $("#lvCatalogo");
        $ul.html("");
        pagina=1;
        $("#_pagina_").val(pagina);
        $("#divPaginador").remove();
        $("#_w_").val($("#_busquedarapida_").val().replace(/\$kw/g,value));
        //De acuerdo a la clave de la forma se establece el where
        cargaXML();
    }    
