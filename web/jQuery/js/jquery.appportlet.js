/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
( function($) {
    $.fn.appportlet = function(opc){

        $.fn.appportlet.settings = {
            category: "0",
            app: "0",
            form: "0",
            register: "0",
            gridData: "",
            isKardex: 0,
            logPortlet: 1, //portlet de actividad reciente
            filterPortlet: 1, //portlets de filtros
            kardexPortlet: 1, //portlet de kardex
            templatePortlet:1, //portlet de plantillas
            reportPortlet:1, //portlet de reportes
            requierePrefiltro:0,
            tabTitle: ""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.appportlet.options = $.extend($.fn.appportlet.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            //Se debe crear el menú de subaplicaciones
                     
            var portlets="";
            $("#_status_").val("Creando interfaz de la aplicacion")
            if ($.fn.appportlet.options.isKardex!=1) {    
                portlets='<div id="appMenu_' + $.fn.appportlet.options.category + "_" + $.fn.appportlet.options.app + '" aplicacion="'+$.fn.appportlet.options.app+'"></div><br />'+
                '<div id="tab_' + + $.fn.appportlet.options.category + "_" + $.fn.appportlet.options.app + '" class="appTab" >' + 
                '<ul>'+
                ' <li><a href="#appTab_'+  $.fn.appportlet.options.category + "_" + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + '">'+$.fn.appportlet.options.tabTitle+'</a></li>'+
                '</ul>'+
                '<div id="appTab_'+ $.fn.appportlet.options.category + "_" + $.fn.appportlet.options.app + "_" +$.fn.appportlet.options.form +'" class="appTab">' //La primer pestaña corresponde a la forma principal de la aplicación padre 
            }
            
            portlets+='<div class="column ui-sortable">';
            if ($.fn.appportlet.options.isKardex==1)    {
                portlets+='<div class="portlet" id="kardexPortlet_'+ $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register + '" type="kardex">' + 
                '<div class="portlet-header">Catálogos relacionados</div>' + 
                '<div class="portlet-content" style="word-wrap: break-word; overflow: auto; break-word; height: 87%;">' + 
                '<div id="tvApp_' + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + "_" + $.fn.appportlet.options.register + '">'+
                '</div>' +
                '</div>' +
                '</div>';
            } 
            
            if ($.fn.appportlet.options.reportPortlet==1)    {
                portlets+='<div class="portlet" id="reportPortlet_'+ $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register + '" type="template">' + 
                '<div class="portlet-header">Reportes</div>' + 
                '<div class="portlet-content" style="word-wrap: break-word; overflow: auto; height: 87%;">' + 
                '<p></p>' + 
                '</div>' +
                '</div>';
            } 
            
            if ($.fn.appportlet.options.logPortlet==1)    {
                portlets+='<div class="portlet" id="logPortlet_' + $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register + '" type="log" >'+
                '<div class="portlet-header">Actividad reciente</div>'+
                '<div class="portlet-content" style="word-wrap: break-word; overflow: auto; height: 87%;">'+
                '<p></p>' + 
                '</div>'+
                '</div>'
            }

            if ($.fn.appportlet.options.filterPortlet==1)    {
                portlets+='<div class="portlet" id="filterPortlet_'+ $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form  + '_' + $.fn.appportlet.options.register + '" type="filter">' + 
                '<div class="portlet-header">Mis filtros</div>' + 
                '<div class="portlet-content" style="word-wrap: break-word; overflow: auto; height: 87%;">' + 
                '<p></p>' + 
                '</div>' +
                '</div>';
            }         
          
            if ($.fn.appportlet.options.templatePortlet==1)    {
                portlets+='<div class="portlet" id="templatePortlet_'+ $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register + '" type="template">' + 
                '<div class="portlet-header">Plantillas</div>' + 
                '<div class="portlet-content" style="word-wrap: break-word; overflow: auto; height: 87%;">' + 
                '<p></p>' + 
                '</div>' +
                '</div>';
            } 
            
            estilo='style="width:82%"'; 
            
            if ($.fn.appportlet.options.isKardex!=1 &&
                $.fn.appportlet.options.reportPortlet!=1 &&
                $.fn.appportlet.options.logPortlet!=1 &&
                $.fn.appportlet.options.filterPortlet!=1 &&
                $.fn.appportlet.options.templatePortlet!=1)
                 estilo='';  

            portlets+='</div>' + //<div class="column ui-sortable">
            '<div id="entityCaruosel_' + $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register + '" class="entityCarrousel" ' + estilo + ' >'+
            ($.fn.appportlet.options.requierePrefiltro==1?'<form id="prefiltro_'+ $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form + '"><input type="button" id="busquedaRapida_'+ $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form +'" value="Buscar" />' + ($("#_cp_").val()!=10?'&nbsp; <a href="#" id="busquedaAvanzada_'+ $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form +'"">B&uacute;squeda avanzada</a></form>':'') :'') + 
            '<div class="gridCarrousel">' +
            '<div id="divgrid_' + $.fn.appportlet.options.app + '_' + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register + '" app="' + $.fn.appportlet.options.app + '" form="' + $.fn.appportlet.options.form + '" inDesktop="true" openKardex="false" ></div>'+
            '</div>' + 
            '<div class="newRegisterCarrousel">' + 
            '</div>' +
            '<div class="editRegisterCarrousel">' + 
            '</div>' + 
            '</div>' + 
            '</div>';  //formTab
            
            if ($.fn.appportlet.options.isKardex!=1)
                portlets+='</div>'; //appTab
                        
            $(this).html(portlets);
            
            //Crea tabs
            if ($.fn.appportlet.options.isKardex!=1) {
                $("#_status_").val("Construyendo interfaz de la aplicacion")
                $("#appTab_" + $.fn.appportlet.options.category + "_" +$.fn.appportlet.options.app).tabs();
                
                //Crea el control tab aqui, puesto que desde este control se va a manipular
                var $tabSubApps = $("#appTab_" + $.fn.appportlet.options.category + "_" + $.fn.appportlet.options.app).tabs({
                    tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
                });

                $( "#appTab_" + $.fn.appportlet.options.category + "_" + $.fn.appportlet.options.app+ " span.ui-icon-close" ).live( "click", function() {
                    //var index = $( "li", $tabSubApps ).index( $( this ).parent() );
                    var index =  $("#appTab_"+ $.fn.appportlet.options.category + "_" + $.fn.appportlet.options.app).find("li").index( $( this ).parent() );
                    $("#appTab_"+ $.fn.appportlet.options.category + "_" + $.fn.appportlet.options.app) .tabs( "remove", index );
                });            
 
                //Crear menú de aplicaciones
                $("#appMenu_" + $.fn.appportlet.options.category + "_" +$.fn.appportlet.options.app).appmenu({
                    aplicacion_padre:$.fn.appportlet.options.app /*,
                    clave_categoria: */
                });
            }    
            //Hace falta crear portlets
            $( ".column" ).sortable({
                connectWith: ".column"
            });

            $("#logPortlet_"+$.fn.appportlet.options.app+ "_"+$.fn.appportlet.options.form+ '_' + $.fn.appportlet.options.register+
              ",#filterPortlet_"+$.fn.appportlet.options.app+"_"+$.fn.appportlet.options.form+ '_' + $.fn.appportlet.options.register+
              ",#kardexPortlet_"+ $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form+ '_' + $.fn.appportlet.options.register+
              ",#templatePortlet_"+ $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register+
              ",#reportPortlet_"+ $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register)
            .addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
            .find( ".portlet-header" )
            .addClass( "ui-widget-header ui-corner-all" )
            .prepend( "<span class='ui-icon ui-icon-minusthick'></span>")
            .end()
            .find( ".portlet-content" );            


            $( ".portlet-header .ui-icon" ).click(function() {
                $( this ).toggleClass( "ui-icon-minusthick" ).toggleClass( "ui-icon-plusthick" );
                $( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
            });

            $( ".column" ).disableSelection();
                                
            //Hace falta llamar al widget que llene el portlet de la bitacora
            if ($.fn.appportlet.options.logPortlet==1 && $.fn.appportlet.options.isKardex!=1)  
                $("#logPortlet_" + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register)
                .portletLog({
                    "app":$.fn.appportlet.options.app,
                    "form": $.fn.appportlet.options.form
            });
            
            
            $("#prefiltro_"+$.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form).prefilter(
            {   app:$.fn.appportlet.options.app,
                form:$.fn.appportlet.options.form
            });
            
            $("#filterPortlet_"+$.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + "_" + $.fn.appportlet.options.register).portletFilter(
            {
                app:$.fn.appportlet.options.app,
                form:$.fn.appportlet.options.form,
                register:$.fn.appportlet.options.register
            });
            
            //Hace falta llamar al widget que llene el portlet del kardex
            // Crea árbol
            if ($.fn.appportlet.options.isKardex==1)    {
                $("#tvApp_" + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + "_" + $.fn.appportlet.options.register ).treeMenu({
                    app:$.fn.appportlet.options.app,
                    entidad: $.fn.appportlet.options.form ,
                    pk:$.fn.appportlet.options.register
                });
            }
            //Hace falta llamar al widget que llene el portlet de plantillas
                                                                
            //Hace falta crear carrusel
            /*$("#entityCaruosel_" + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form).agile_carousel({
                                    carousel_data: [{
                                                    "content": "<br /><br /><div id='grid_" + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + "' class='queued_grids' app='" + $.fn.appportlet.options.app + "' form='" + $.fn.appportlet.options.form + "' inDesktop='true' openKardex='false' >Aquí va el grid</div>",
                                                    "content_button": ""
                                                    }, {
                                                    "content": "<div id='newRegisterCarrousel" +$.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form+"'>Aqui va la ventana de nuevo registro</div>",
                                                    "content_button": ""       
                                                    }, {
                                                    "content": "<div id='editRegisterCarrousel" +$.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form+"'>Aqui va la ventana de edición de registro</div>",
                                                    "content_button": ""       
                                                    }],
                                    carousel_outer_height:  $($("#entityCaruosel_" + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form).parent()).height(),
                                    carousel_height: $($("#entityCaruosel_" + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form).parent()).height(),
                                    slide_height: $($("#entityCaruosel_" + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form).parent()).height()+2,
                                    carousel_outer_width: $("#grid_"+$.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form).width(),
                                    slide_width: $("#grid_"+$.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form).width(), 
                                    transition_time: 300,
                                    continuous_scrolling: false,
                                    control_set_1: "previous_button,next_button",
                                    control_set_2: "numbered_buttons"
                                }); */


            if ($.fn.appportlet.options.reportPortlet==1)  
                $("#reportPortlet_" + $.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form + '_' + $.fn.appportlet.options.register)
                .portletReport({
                    "app":$.fn.appportlet.options.app,
                    "form": $.fn.appportlet.options.form
            });
            
            if ($.fn.appportlet.options.isKardex==0) {
                $("#divgrid_"+$.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form+ '_' + $.fn.appportlet.options.register).appgrid({
                    app: $.fn.appportlet.options.app,
                    entidad: $.fn.appportlet.options.form,
                    pk:0,
                    editingApp:$.fn.appportlet.options.app,
                    wsParameters:$.fn.appportlet.options.gridData,
                    height:"400px",
                    openKardex:true,
                    originatingObject:obj[0].id,
                    getLog:true,
                    getFilters:true
                });
            }
            else {
                $("#divgrid_"+$.fn.appportlet.options.app + "_" + $.fn.appportlet.options.form+ '_' + $.fn.appportlet.options.register)
                .html("<div class='ui-widget mensaje-info' style='margin: auto; width: 100%'; >" +
                    "<div class='ui-state-highlight ui-corner-all' style='padding: 0 .7em;'>" +
                    "<p ><span class='ui-icon ui-icon-info' style='float: left; margin-right: .3em;'></span><strong>En este espacio se muestran los cat&aacute;logos relacionados a " + $.fn.appgrid.options.logPhrase + " '" + oGrid.getCell(nRow,1) + "', seleccione una carpeta del men&uacute; de la izquierda</strong></p>"+
                    "</div>" + 
                    "</div>");
            }
            
            $("#_status_").val(""); 
        });

    };

})(jQuery);