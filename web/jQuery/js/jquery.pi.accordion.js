/* 
 * Plugin de jQuery para cargar accordeón a través de un plugin
 * 
 */
( function($) {
    $.fn.appmenu = function(opc){

        $.fn.appmenu.settings = {
            xmlUrl : "", //"/ProyILCE/xml_tests/widget.accordion.xml",
            usuario:"",
            menu: [/*{aplicacion:"", elementos_menu:[{etiqueta:"", entidad:"", funcion:""}]}*/],
            ts:""

        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.appmenu.options = $.extend($.fn.appmenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             obj = $(this);
             if ($.fn.appmenu.options.xmlUrl!="") {
                 obj.html("<div align='center' class='cargando'><br /><br />Cargando informaci&oacute;n...<br /><img src='img/loading.gif' /></div>"); $.fn.appmenu.options.ts="U2FsdGVkX1+K/UZ+8JLyZRxlM2+sjv0subeoJS4mtaQ=";
                 $.fn.appmenu.ajax(obj);
             }
        });

    };

    $.fn.appmenu.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.appmenu.options.xmlUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xmlAppMenu = new ActiveXObject("Microsoft.XMLDOM");
                 xmlAppMenu.async = false;
                 xmlAppMenu.validateOnParse="true";
                 xmlAppMenu.loadXML(data);
                 if (xmlAppMenu.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xmlAppMenu.parseError.errorCode +"\nParse reason:" + xmlAppMenu.parseError.reason + "\nLinea:" + xmlAppMenu.parseError.line);}
                }
                 else {
                    xmlAppMenu = data;}
                obj.html($.fn.appmenu.handleAccordion(xmlAppMenu));
                $(obj).accordion({
                active: false,
                autoHeight: false,
                collapsible: true,
                change: function() {
                  $(this).find('h3').blur();
                 }
                });

                //Crea el control tab aqui, puesto que desde este control se va a manipular
                var $tabs = $('#tabs').tabs({
                    tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
                });

                $( "#tabs span.ui-icon-close" ).live( "click", function() {
			var index = $( "li", $tabs ).index( $( this ).parent() );
			$tabs .tabs( "remove", index );
                });
                
                //Hace el binding de las ligas con sus eventos
                //seleccionando todas las ligas
                $("a.appMenu").each( function(){
                    link_id="#"+this.id;

                    $(link_id).click(function(e, data) {
                            //Verifica si existe
                            var nAplicacion=this.id.split("_")[1];
                            var nEntidad=this.id.split("_")[2];
                            var sTitulo=this.childNodes[0].data;

                            if ($("#tab"+this.id).length) {
                                //Selecciona el tab correspondiente
                                $tabs.tabs("select", "#tab"+this.id);

                                //Recupera el id del grid del tab
                                sGridIdSuffix=$("#tab" + this.id).children()[0].id.replace("gbox_grid_","");

                                //Recarga el grid por si tiene algún filtro
                                if (data==undefined){
                                    data="";
                                   $("#lnkRemoveFilter_grid_" +sGridIdSuffix ).remove();
                                }
                                else {
                                    //Si no existe el link para quitar filtro, lo establece
                                    if ($("#lnkRemoveFilter_grid_" + sGridIdSuffix).length==0) {                                        
                                        oGridHeader=$("#gview_grid_" +sGridIdSuffix).find("span.ui-jqgrid-title");
                                        $(oGridHeader[0]).append("<a style='margin-left:10px' href='#' id='lnkRemoveFilter_grid_" + sGridIdSuffix +"'>(Quitar filtro)</a>");

                                        //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                                        $("#lnkRemoveFilter_grid_" + sGridIdSuffix).click(function() {
                                            if ($("grid_" + sGridIdSuffix).attr("requeriesFilter")=="1") {
                                                $("body").form({
                                                    app: nApp,
                                                    forma:nEntidad,
                                                    datestamp:$(this).attr("datestamp"),
                                                    modo:"lookup",
                                                    titulo: "Filtrado de registros",
                                                    columnas:1,
                                                    height:"500",
                                                    width:"80%",
                                                    pk:0,
                                                    originatingObject: oGrid.id
                                                }); }
                                            else     
                                                $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{url:"control?$cmd=grid&$cf=" + nEntidad + "&$dp=body&page=1"}).trigger("reloadGrid")
                                            
                                            $(this).remove();
                                        });
                                    }
                                }

                               $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{url:"control?$cmd=grid&$cf=" + nEntidad + "&$dp=body&$w=" +data}).trigger("reloadGrid");

                            }
                            else {

                                if (this.id.split("_")[0]=="newEntity") {
                                    $("body").form({app: nAplicacion,
                                                    forma:nEntidad,
                                                    modo:"insert",
                                                    pk:0,
                                                    titulo: sTitulo,
                                                    columnas:1,
                                                    height:400,
                                                    width:500,
                                                    originatingObject:obj.id
                                               });
                                }
                                else {
                                    $tabs.tabs( "add", "#tab"+this.id, this.childNodes[0].data);
                                    $tabs.tabs( "select", "#tab"+this.id);
                                    oTabPanel=$("#tab"+this.id);
                                    var sLeyendaNuevoRegistro=$("#newEntity_" + nAplicacion + "_" + nEntidad ).text();
                                    var sLeyendaEditaRegistro="Edita " + sLeyendaNuevoRegistro.split(" ")[1];

                                    $("#tab"+this.id).appgrid({app: nAplicacion,
                                                               entidad: nEntidad,
                                                               pk:0,
                                                               editingApp:nAplicacion,
                                                               wsParameters:data,
                                                               titulo:sTitulo,
                                                               height:"70%",
                                                               leyendas:[sLeyendaNuevoRegistro, sLeyendaEditaRegistro],
                                                               openKardex:true,
                                                               originatingObject:obj[0].id
                                                           });
                                }
                            }
                        });
                });



                //Mecanismo para forzar a que el DOM no se cargue del cache
                // ya que esto hace que se dupliquen los ids de los grid en cola
                if ($("#_ts_").val()!="") {
                    $.post("control?$cmd=plain&$cf=1&$ta=select&$w=parametro='cache-pragma'", function(data) {
                         if (typeof data == "string") {
                             xmlCache = new ActiveXObject("Microsoft.XMLDOM");
                             xmlCache.async = false;
                             xmlCache.validateOnParse="true";
                             xmlCache.loadXML(data);
                             if (xmlCache.parseError.errorCode>0) {
                                    alert("Error de compilación xml:" + xmlCache.parseError.errorCode +"\nParse reason:" + xmlCache.parseError.reason + "\nLinea:" + xmlCache.parseError.line);}
                            }
                             else {
                                xmlCache = data;}

                            h=$(xmlCache).find("valor").text();
                            if (h!=undefined)
                                $("#_ts_").val(h);
                            else
                                $("#_ts_").val($.fn.appmenu.options.ts);
                    });
                 }
                 //Incluye los queries almacenados de cada aplicacion
                 $.fn.appmenu.getSearchs();
                 
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

   $.fn.appmenu.getSearchs = function() {
       $.ajax(
            {
            url: "control?$cmd=plain&$cf=1&$ta=select&$w=" + escape("c.claveempleado=" +$("#_ce_").val()+ " AND c.parametro like 'menu.busqueda.%'"),
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xmlGs = new ActiveXObject("Microsoft.XMLDOM");
                 xmlGs.async = false;
                 xmlGs.validateOnParse="true";
                 xmlGs.loadXML(data);
                 if (xmlGs.parseError.errorCode>0)
                    alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);}                
                 else 
                    xmlGs = data;
                
                var sBusquedas="<br>";
                var nAplicacion="";
                
                $(xmlGs).find("registro").each( function(){
                    nClave=$(this).find("clave_parametro")[0].firstChild.data;
                    sParametro=$(this).find("parametro")[0].firstChild.data
                    nForma=sParametro.split(".")[2];
                    sEtiqueta=sParametro.split(".")[3];
                    nAplicacion =$(this).find("clave_aplicacion")[0].firstChild.data;
                    sValor=escape($(this).find("valor")[0].firstChild.data);
                    sSuffix =nAplicacion + "_" + nForma + "_" + nClave;
                    sBusquedas="<div class='link_toolbar'>"+
                               "<a class='appMenu' href='#' id='lnkBusqueda_" + sSuffix  + "' data='" +sValor+ "' forma='" + nForma + "' pk='" + nClave + "' >" + sEtiqueta + "</a>"+
                               "<div style='float:right'><div title='Eliminar reporte' style='cursor: pointer; float: right' class='closeLnkFiltro ui-icon ui-icon-close' pk='" + nClave + "' forma='" + nForma + "'></div></div>"   +
                               "</div>";

                    $("#appQries_"+nAplicacion).append(sBusquedas);

                    //Oculta botones
                    $(".ui-icon-close", "#appQries_"+nAplicacion).hide();
                    //Hace bind del liga del búsqueda
                    $("#lnkBusqueda_" + sSuffix).click(function(){
                        nAplicacion=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                        sValor=this.id.split("_")[4];
                        /*var newE = $.Event('click');
                        newE.gridFilter=$(this).attr("data");*/
                        data=$(this).attr("data");
                        $("#showEntity_" + nAplicacion + "_" + nForma).trigger("click",data);
                    });                    
                });

                //Hace bind con los divs padres del link en el evento hover
                $(".link_toolbar").hover(
                    function () {
                    //$(this).addClass('active_filter');
                    $(".closeLnkFiltro",this).show();
                    },
                    function () {
                    //$(this).removeClass('active_filter');
                    $(".closeLnkFiltro",this).hide();
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

                //Hace bind del botón de búsqueda
                $(".closeLnkFiltro").click(function(){
                    if (!confirm('¿Desea borrar el reporte seleccionado?')) return false;
                      $.post("control","$cmd=register&$ta=delete&$cf=1&$pk=" + $(this).attr("pk"));
                      $(this).parent().parent().remove();
                });


            }
       });
   }

    $.fn.appmenu.handleAccordion = function(xml){
        sHtml="";

        $(xml).find("registro").each(function(){
            nAplicacion=$(this).find("clave_aplicacion").text();
            sTituloAplicacion=$(this).find("aplicacion").text()
            nEntidad=$(this).find("clave_forma").text();
            sAliasNuevaEntidad=$(this).find("alias_menu_nueva_entidad").text();
            sAliasMostrarEntidad=$(this).find("alias_menu_mostrar_entidad").text();
            nInsertar = $(this).find("insertar").text();
            nMostrar = $(this).find("mostrar").text();

            sHtml+="<h3><a href='#' class='appMenuTitle' >" + sTituloAplicacion + "</a></h3>" +
                "<div id='mnuApp_" + nAplicacion + "' >";

            if (nInsertar=="1")
                   sHtml+="<div class='appMenu'><a href='#' class='appMenu' id='newEntity_" + nAplicacion + "_" + nEntidad + "' >"+sAliasNuevaEntidad+"</a></div>";

            //if (nMostrar=="1")
                   sHtml+="<div class='appMenu'><a href='#' class='appMenu' id='showEntity_" + nAplicacion + "_" + nEntidad + "' >"+sAliasMostrarEntidad+"</a></div>";

            sHtml+="<div id='appQries_" + nAplicacion + "'><br><span class='app_search_title'>Mis reportes>></span><br /></div></div>";

        })

        return sHtml;
    }
    
})(jQuery);
