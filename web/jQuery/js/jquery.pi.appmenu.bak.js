/* 
 * Plugin de jQuery para cargar accordeón a través de un plugin
 * 
 */
( function($) {
    $.fn.appmenu = function(opc){

        $.fn.appmenu.settings = {
            xmlUrl : "control?$cmd=app", //"/ProyILCE/srvControl?$cmd=appmenu" "/ProyILCE/resource/jsp/xmlMenu.jsp"  //"$cf=1&$ta=XML acordeon", //"/ProyILCE/xml_tests/widget.accordion.xml",
            usuario:"",
            ts:""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.appmenu.options = $.extend($.fn.appmenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            obj = $(this);
            if ($.fn.appmenu.options.xmlUrl!="") {
                $.fn.appmenu.options.ts="U2FsdGVkX1+K/UZ+8JLyZRxlM2+sjv0subeoJS4mtaQ=";
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
                    xmlApps = new ActiveXObject("Microsoft.XMLDOM");
                    xmlApps.async = false;
                    xmlApps.validateOnParse="true";
                    xmlApps.loadXML(data);
                    if (xmlApps.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xmlApps.parseError.errorCode +"\nParse reason:" + xmlApps.parseError.reason + "\nLinea:" + xmlApps.parseError.line);
                    }
                }
                else {
                    xmlApps = data;
                }
                
                /* Inserta el html en el elemento apps */
                obj.html( $.fn.appmenu.handleMenu(xmlApps));
                //Carga las aplicaciones en el mapa del sitio
                
                $("#tabs").tabs();

                /*$( "#tabs span.ui-icon-triangle-1-s" ).live( "click", function() {
                        var menu = $("#apps").parent().show().position({
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
                });       */     
                
                /*$("#menu_inicio")
                    .button()
                    .click(function() {
                        alert("Por implementar");
                    })
                    .next() //menu_aplicaciones 
                        .button()
                        .click(function() {
                            alert("Por implementar");
                        })                        
                        .next() // menu_splitter 
                            .button( {
                                    text: false,
                                    icons: {
                                            primary: "ui-icon-triangle-1-s"
                                    }
                            })
                            .click( function() {
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
                            .next() // menu_mapa
                                .button()
                                .click(function() {
                                    alert("Por implementar");
                                })       
                                .next() // menu_ayuda 
                                    .button()
                                    .click(function() {
                                        alert("Por implementar");
                                    })
                                    .next() // menu_contacto 
                                    .button()
                                    .click(function() {
                                        alert("Por implementar");
                                    })
                                    .parent()
				    .buttonset()
                                    .next().
                                        hide().
                                        menu(); */
                
                //Crea el control tab aqui, puesto que desde este control se va a manipular
                var $tabMisApps = $('#tabMisApps').tabs({
                    tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
                });

                $( "#tabMisApps span.ui-icon-close" ).live( "click", function() {
                    var index = $( "li", $tabMisApps ).index( $( this ).parent() );
                    $tabMisApps .tabs( "remove", index );
                });
                
                 /*Captura el evento clic para los links*/
                $(".menu")
                    .button()
                    .click(function(e, data) {
                        link_id=this.name;

                        //Verifica si existe
                        var nAplicacion=link_id.split("_")[1];
                        var nEntidad=link_id.split("_")[2];
                        var sTitulo=$(this).text();

                        if ($("#tab"+link_id).length) {
                            //Selecciona el tab correspondiente
                            $tabMisApps.tabs("select", "#tab"+link_id);

                            //Recupera el id del grid del tab       
                            sGridIdSuffix=$($($($("#tab" + link_id).children()[0]).children()[2]).children()[0]).children()[0].id.replace("gbox_grid_","");
                            $.fn.appmenu.setGridFilter(sGridIdSuffix,nAplicacion,nEntidad,data);

                        }
                        else {

                            if (link_id.split("_")[0]=="newEntity") {
                                $("body").form({
                                    app: nAplicacion,
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
                                $tabMisApps.tabs( "add", "#tab"+link_id, sTitulo);
                                $tabMisApps.tabs( "select", "#tab"+link_id);
                                oTabPanel=$("#tab"+link_id);
                                // Aqui va a ir la barra de avisos 
                                oTabPanel.addClass("appTab");
                                oTabPanel.html(""); 
                                //Se inserta el div para el grid
                                oTabPanel.html(
                                    "<div id='splitterContainer_"+ nAplicacion + "_" + nEntidad + "_0' class='splitterContainer'>"+
                                    "   <div id='leftPane_"+ nAplicacion + "_" + nEntidad + "_0' class='leftPane' style='float:left; width:20%'>"+           
                                    "       <div id='accordion_"+nAplicacion + "_" + nEntidad+"_0' class='accordionContainer'>"+
                                    "           <h3>&nbsp;Actividad reciente</h3>" +
                                    "           <div id='bitacora_"+nAplicacion + "_" + nEntidad+"_0'></div>"+
                                    "           <h3>&nbsp;Mis reportes</h3>" +
                                    "           <div id='filtros_"+nAplicacion + "_" + nEntidad+"_0'></div>"+
                                    "       </div>"+
                                    "   </div>"+
                                    "   <div id='rigthPane_"+ nAplicacion + "_" + nEntidad + "_0' class='rigthPane' style='width: 80%; float: right;'>"+
                                    "       <div id='entityCarrousel_" + nAplicacion+ "_" + nEntidad+"_0' class='entityCarrousel' style='width:102.5%; margin-left:15px'>" +
                                    "           <div id='gridCarrousel_" + nAplicacion+"_" + nEntidad+"_0' >" +
                                    "               <div id='grid_"+nAplicacion + "_" + nEntidad+"_0' class='gridContainer'/>"+
                                    "           </div>" + 
                                    "           <div id='chartCarrousel_" + nAplicacion + "_" + nEntidad + "_0'>" +
                                    "               <div id='chartIngresosProyectos_" + nAplicacion + "_" + nEntidad + "'></div>"+
                                    "               <div id='chartEngresosProyectos_" + nAplicacion + "_" + nEntidad + "></div>"+
                                    "               <div id='chartNetoProyecto_" + nAplicacion + "_" + nEntidad + "'></div>"+
                                    "           </div>" +
                                    "   </div>"+
                                    "</div>");

                               /* $("#splitterContainer_"+ nAplicacion + "_" + nEntidad + "_0").splitter({
                                    type: "v",
                                    outline: true,
                                    minLeft: 100, 
                                    sizeLeft: 200, 
                                    minRight: 500,
                                    resizeToWidth: true,
                                    cookie: "vsplitter",
                                    accessKey: 'I'
                                });*/

                                var sLeyendaNuevoRegistro=$(this).attr("nueva_entidad");
                                var sLeyendaEditaRegistro="Edita " + sLeyendaNuevoRegistro.split(" ")[1];
                                
                                //Se crea el carrusel
                                /*
                                $("#entityCarrousel_" + nAplicacion + "_" + nEntidad+ "_0").agile_carousel({
                                    carousel_data: [{
                                                    "content": "<div id='grid_"+nAplicacion + "_" + nEntidad+"_0' class='gridContainer' />",
                                                    "content_button": ""
                                                    }, {
                                                    "content": "<div id='chartCarrousel_" + nAplicacion + "_" + nEntidad + "_0'>" +
                                                                    "<div id='chartIngresosProyectos_" + nAplicacion + "_" + nEntidad + "'></div>"+
                                                                    "<div id='chartEngresosProyectos_" + nAplicacion + "_" + nEntidad + "'></div>"+
                                                                    "<div id='chartNetoProyecto_" + nAplicacion + "_" + nEntidad + "'></div>"+
                                                               "</div>",
                                                    "content_button": ""       
                                                    }],
                                    carousel_outer_height: $("#entityCarrousel_" + nAplicacion + "_" + nEntidad+ "_0").height(),
                                    carousel_height: $("#entityCarrousel_" + nAplicacion + "_" + nEntidad+ "_0").height(),
                                    slide_height: $("#entityCarrousel_" + nAplicacion + "_" + nEntidad+ "_0").height()+2,
                                    carousel_outer_width: $("#entityCarrousel_" + nAplicacion + "_" + nEntidad+ "_0").width(),
                                    slide_width: $("#entityCarrousel_" + nAplicacion + "_" + nEntidad+ "_0").width(), 
                                    transition_time: 300,
                                    continuous_scrolling: false,
                                    control_set_1: "previous_button,next_button",
                                    control_set_2: "numbered_buttons"
                                });
                                */
                               
                                $("#grid_"+nAplicacion + "_" + nEntidad+"_0").appgrid({
                                    app: nAplicacion,
                                    entidad: nEntidad,
                                    pk:0,
                                    editingApp:nAplicacion,
                                    wsParameters:data,
                                    titulo:sTitulo,
                                    height:"70%",
                                    leyendas:[sLeyendaNuevoRegistro, sLeyendaEditaRegistro],
                                    openKardex:true,
                                    originatingObject:obj[0].id,
                                    getLog:true,
                                    getFilters:true
                                });
                                setTimeout("$.fn.appmenu.getFullMenu('"+nAplicacion + "_" + nEntidad+"_0',"+nAplicacion+","+nEntidad+",1)",500)
                            
                            }
                        }    
                    })
                    .parent()
                    .buttonset(); 
                 
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);
            }
        });
    };
    
    $.fn.appmenu.setGridFilter = function (sGridIdSuffix,nApp, nEntidad,data) {
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
                        $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
                            url:"control?$cmd=grid&$cf=" + nEntidad + "&$dp=body&page=1"
                        }).trigger("reloadGrid")
                        $(this).remove();
                });
            }
        }

        $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
            url:"control?$cmd=grid&$cf=" + nEntidad + "&$dp=body&$w=" +data
        }).trigger("reloadGrid");
    };

    $.fn.appmenu.getFullMenu = function (sDivSuffix, nApp, nForma) {
        $.fn.appmenu.getLog(sDivSuffix,nApp,nForma,1);
    };
    
    $.fn.appmenu.getSearchs = function(sDivSuffix, nApp, nForma,bGetAccordion) {
        $("#filtros_"+sDivSuffix).html("");
        $.ajax(
        {
            url: "control?$cmd=plain&$cf=93&$ta=select&$w=" + escape("clave_empleado=" +$("#_ce_").val()+ " AND clave_forma="+nForma),
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlGs = new ActiveXObject("Microsoft.XMLDOM");
                    xmlGs.async = false;
                    xmlGs.validateOnParse="true";
                    xmlGs.loadXML(data);
                    if (xmlGs.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xmlGs.parseError.errorCode +"\nParse reason:" + xmlGs.parseError.reason + "\nLinea:" + xmlGs.parseError.line);
                }                
                else 
                    xmlGs = data;
                
                var sBusquedas="<br>";
                var nAplicacion="";
                
                $(xmlGs).find("registro").each( function(){
                    nClave=$(this).find("clave_filtro")[0].firstChild.data;
                    if (nClave=="") return false
                    sFiltro=$(this).find("filtro")[0].firstChild.data
                    nForma=$(this).find("clave_forma")[0].firstChild.data;
                    nAplicacion =$(this).find("clave_aplicacion")[0].firstChild.data;
                    sW=escape($(this).find("consulta")[0].firstChild.data);
                    sSuffix =nAplicacion + "_" + nForma + "_" + nClave;
                    sBusquedas="<div class='link_toolbar'>"+
                    "<div class='linkSearch'><a class='linkSearch' href='#' id='lnkBusqueda_" + sSuffix  + "' data='" +sW+ "' forma='" + nForma + "' pk='" + nClave + "' >" + sFiltro + "</a></div>"+
                    "<div style='float:right'><div title='Eliminar filtro' style='cursor: pointer; float: right' class='closeLnkFiltro ui-icon ui-icon-close' pk='" + nClave + "' forma='" + nForma + "'></div></div>" +
                    "</div>";

                    $("#filtros_"+sDivSuffix).append(sBusquedas);

                    //Oculta botones para cerrar
                    $(".ui-icon-close", "#filtros_"+sDivSuffix).hide();
                    
                    //Hace bind del liga del búsqueda
                    $("#lnkBusqueda_" + sSuffix).click(function(){
                        nAplicacion=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                        sW=$(this).attr("data");
                        /*var newE = $.Event('click');
                        newE.gridFilter=$(this).attr("data");*/
                        data=$(this).attr("data");
                        if ($("#showEntity_" + nAplicacion + "_" + nForma).length>0)
                            $("#showEntity_" + nAplicacion + "_" + nForma).trigger("click",data);
                        else {
                            //hace la búsqueda del grid de acuerdo a la posicion relativa del objeto actual
                            aGridIdSuffix=$(this).parent().parent().parent().parent().parent().next().next().children()[0].children[0].id.split("_");
                            $.fn.appmenu.setGridFilter(aGridIdSuffix[2]+"_"+aGridIdSuffix[3]+"_"+aGridIdSuffix[4],nAplicacion,nForma,data);
                        }
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
                
                //Hace un unbind
                $(".closeLnkFiltro").unbind("click");
                //Hace bind del botón de búsqueda
                $(".closeLnkFiltro").click(function(){
                    if (!confirm('¿Desea borrar el filtro seleccionado?')) return false;
                    $.post("control","$cmd=register&$ta=delete&$cf=93&$pk=" + $(this).attr("pk"));
                    $(this).parent().parent().remove();
                });
                
                //if (bGetLog==1)
               //     $.fn.appmenu.getLog(sDivSuffix,nApp,nForma,bGetLog);
                
                if (bGetAccordion==1 && $("#accordion_"+sDivSuffix).attr("role")!='tablist') 
                    $("#accordion_"+sDivSuffix).accordion({
                        active: false,
                        /*fillSpace:true, */
                        autoHeight: false,
                        collapsible: true,
                        change: function() {
                            $(this).find('h3').blur();
                        }
                    });

            },
            error:function(xhr,err){
                alert("Error al recuperar filtros: "+xhr.readyState+"\nstatus: "+xhr.status + "\responseText:"+ xhr.responseText);            }            
        });
    }
    
    $.fn.appmenu.getLog = function(sDivSuffix,nApp,nForma, bGetSearchs) {
        $("#bitacora_"+sDivSuffix).html("");
        $.ajax(
        {
            url: "control?$cmd=plain&$cf="+nForma+"&$ta=log",
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlLog = new ActiveXObject("Microsoft.XMLDOM");
                    xmlLog.async = false;
                    xmlLog.validateOnParse="true";
                    xmlLog.loadXML(data);
                    if (xmlLog.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                }                
                else 
                    xmlLog = data;
                
                $(xmlLog).find("registro").each( function(){
                    sHtml="";
                    dFecha=$(this).find("fecha_bitacora")[0].firstChild.data;
                    sFoto=$(this).find("foto")[0].firstChild.data.toLowerCase();
                    sNombre=$(this).find("nombre")[0].firstChild.data;
                    sTipoEvento=$(this).find("clave_tipo_evento")[0].firstChild.data;
                    sForma=$(this).find("entidad")[0].firstChild.data;
                    sBitacora=$(this).find("descripcion_entidad")[0].firstChild.data
                    nForma=$(this).find("clave_forma")[0].firstChild.data;
                    nRegistro=$(this).find("clave_registro")[0].firstChild.data;
                   
                    sHtml="<div class='bitacora'>" +
                    sFoto +
                    sNombre + " " + sTipoEvento + " " + sForma + " " + 
                    "<a href='#' id='lnkBitacora_" + nAplicacion + "_" + nForma + "_" + nRegistro + "' class='lnkBitacora'>"+
                    sBitacora  + "</a> - " + dFecha +
                    "</div>";
                
                    $("#bitacora_"+sDivSuffix).append(sHtml);
                    //Hace bind del liga del búsqueda
 
                });
                
                // Quita los eventos asignados anteriormente
                $(".lnkBitacora").unbind("click");
                
                //Le asigna el evento clic a todos los links de la bitacora
                $(".lnkBitacora").click(function(){
                    $("body").form({
                        app: this.id.split("_")[1],
                        forma:this.id.split("_")[2],
                        pk:this.id.split("_")[3],
                        datestamp:$(this).attr("datestamp"),
                        modo:"update",
                        titulo: "Edita " + sForma.split(" ")[1],
                        columnas:1,
                        filtroForaneo:"2=clave_aplicacion=" + nApp,
                        height:"500",
                        width:"80%",
                        originatingObject:"#lnkBitacora_" + nApp + "_" + nForma + "_" + nRegistro
                    });
                }); 
               
               if (bGetSearchs==1)
                    $.fn.appmenu.getSearchs(sDivSuffix, nApp, nForma,1);
                
            },
            error:function(xhr,err){
                alert("Error al recuperar bitácora: "+xhr.readyState+"\nstatus: "+xhr.status + "\responseText:"+ xhr.responseText);
            }
        });
    }
   
    /*Crea el HTML que conforma los botones */
    $.fn.appmenu.handleMenu = function(xml){   
        sHtml="";

        $(xml).find("registro").each(function(){
            nAplicacion=$(this).find("clave_aplicacion").text();
            
            if ($("#_cp_").val()!=1 && (nAplicacion==1 || nAplicacion==2))
                return true;
                    
            sTituloAplicacion=$(this).find("aplicacion").text()
            nEntidad=$(this).find("clave_forma").text();
            sAliasNuevaEntidad=$(this).find("alias_menu_nueva_entidad").text();
            sAliasMostrarEntidad=$(this).find("alias_menu_mostrar_entidad").text();
            nInsertar = $(this).find("insertar").text();
            nMostrar = $(this).find("mostrar").text();

            sHtml+="<a href='#' class='menu' name='showEntity_" + nAplicacion + "_" + nEntidad +"'  nueva_entidad='" + sAliasNuevaEntidad +"' edita_entidad='"+ sAliasMostrarEntidad + "'>" + sTituloAplicacion + "</a>";
            
            //Agrega los menús al mapa del sitio
            $("#app_menu_in_map").append("<dt><a id='mapLink-tabAplicaciones-tabshowEntity" +nAplicacion + "_" + "_" + nEntidad + "' class='maplink' href='#'>"+sTituloAplicacion+"</dt>");
        })

        return sHtml;
    }
    
})(jQuery);