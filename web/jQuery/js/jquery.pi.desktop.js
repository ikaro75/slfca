/* 
 * Plugin de jQuery para cargar menÃº de sesiÃ³n a travÃ©s de un plugin
 * 
 */
( function($) {
    $.fn.desktop = function(opc){

        $.fn.desktop.settings = {
            xmlUrl : "control?$cmd=plain&$cf=1&$ta=select&$w=" + escape("clave_empleado=" +$("#_ce_").val()+ " AND parametro like 'escritorio.%'")  //"srvControl?$cmd=form&$cf=1&$ta=select&$w=" + escape("c.clave_empleado=" +$("#_ce_").val()+ " AND c.parametro like 'escritorio.%'")
        };

        // Ponemos la variable de opciones antes de la iteraciÃ³n (each) para ahorrar recursos
        $.fn.desktop.options = $.extend($.fn.desktop.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            obj = $(this);

            $( ".column" ).sortable({
			connectWith: ".column"
            });
            
            $( ".portlet" ).addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
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

            /*$("#divCarouselMisFavoritos").agile_carousel({
                carousel_data: [{
                                "content": $("#ayudaComoAgregarAFavoritos").html(),
                                "content_button": ""
                                }, {
                                "content": $("#ayudaComoEliminarFavoritos").html(),
                                "content_button": ""
                                }],
                carousel_outer_height: $("#divCarouselMisFavoritos").height(),
                carousel_height: $("#divCarouselMisFavoritos").height(),
                slide_height: $("#divCarouselMisFavoritos").height()+2,
                carousel_outer_width: $("#divCarouselMisFavoritos").width(),
                slide_width: $("#divCarouselMisFavoritos").width(), 
                transition_time: 300,
                continuous_scrolling: false,
                control_set_1: "previous_button,next_button",
                control_set_2: "numbered_buttons"
            });*/
            
             //$('#tabUser').tabs( "select", "#tabPendientes" );
             
             $("#tabs").tabs( "select", "#tabAplicaciones" );
             
             
             //Activa el carrousel de ayuda de favoritos
             $("#divCarouselMisAplicaciones").agile_carousel({
                carousel_data: [{
                                "content": $("#ayudaComoUsarMisAplicaciones").html(),
                                "content_button": ""
                                }, {
                                "content": $("#ayudaComoAgregarUnRegistro").html(),
                                "content_button": ""
                                }, {
                                "content": $("#ayudaComoEditarUnRegistro").html(),
                                "content_button": ""
                                },{
                                "content": $("#ayudaComoEliminarUnRegistro").html(),
                                "content_button": ""
                                },{
                                "content": $("#ayudaComoFiltrarRegistros").html(),
                                "content_button": ""
                                }/*,{
                                "content": $("#ayudaComoAgregarCatalogoAFavoritos").html(),
                                "content_button": ""
                                }*/
                                
                            ],
                carousel_outer_height: $("#divCarouselMisAplicaciones").height(),
                carousel_height: $("#divCarouselMisAplicaciones").height(),
                slide_height: $("#divCarouselMisAplicaciones").height()+2,
                carousel_outer_width: $("#divCarouselMisAplicaciones").width(),
                slide_width: $("#divCarouselMisAplicaciones").width(), 
                transition_time: 300,
                continuous_scrolling: false,
                number_slides_visible: 1,
                control_set_1: "previous_button,next_button",
                control_set_2: "numbered_buttons"
            });   
            
            $("#tabs").tabs( "select", "#tabAyuda" );

            $("#divCarouselAyuda").agile_carousel({
                carousel_data: [{
                                "content": $("#ayudaIndice").html(),
                                "content_button": ""
                                }, {
                                "content": $("#ayudaVistazo").html(),
                                "content_button": ""
                                }, {
                                "content": $("#ayudaInicio").html(),
                                "content_button": ""
                                }/*,{
                                "content": $($($($("#divCarouselMisFavoritos").children()[0]).children()[0]).children()[0]).html(),
                                "content_button": ""
                                }, {
                                "content": $($($($("#divCarouselMisFavoritos").children()[0]).children()[0]).children()[1]).html(),
                                "content_button": ""
                                }*/,{
                                "content": $("#ayudaAplicacion").html(),
                                "content_button": ""
                                },{ //ayudaComoUsarMisAplicacionesBis
                                "content": $($($($("#divCarouselMisAplicaciones").children()[0]).children()[0]).children()[0]).html(),
                                "content_button": ""
                                }, { //ayudaComoAgregarUnRegistro
                                "content": $($($($("#divCarouselMisAplicaciones").children()[0]).children()[0]).children()[1]).html(),
                                "content_button": ""
                                }, { //ayudaComoEditarUnRegistro
                                "content": $($($($("#divCarouselMisAplicaciones").children()[0]).children()[0]).children()[2]).html(),
                                "content_button": ""
                                },{ //ayudaComoEliminarUnRegistro
                                "content": $($($($("#divCarouselMisAplicaciones").children()[0]).children()[0]).children()[3]).html(),
                                "content_button": ""
                                },{ //ayudaComoFiltrarRegistros
                                "content": $($($($("#divCarouselMisAplicaciones").children()[0]).children()[0]).children()[4]).html(),
                                "content_button": ""
                                }/*,{ //ayudaComoAgregarCatalogoAFavoritos
                                "content":  $($($($("#divCarouselMisAplicaciones").children()[0]).children()[0]).children()[5]).html(),
                                "content_button": ""
                                },{ //ayudaComoEliminarCatalogoDeFavoritos
                                "content":  $($($($("#divCarouselMisFavoritos").children()[0]).children()[0]).children()[1]).html(),
                                "content_button": ""
                                }*/,{
                                "content": $("#ayudaMapa").html(),
                                "content_button": ""
                                }
                                
                            ],
                carousel_outer_height: $("#divCarouselAyuda").height(),
                carousel_height: $("#divCarouselAyuda").height(),
                slide_height: $("#divCarouselAyuda").height()+2,
                carousel_outer_width: $("#divCarouselAyuda").width(),
                slide_width: $("#divCarouselAyuda").width(), 
                transition_time: 300,
                continuous_scrolling: false,
                number_slides_visible: 1,
                control_set_1: "previous_button,next_button",
                control_set_2: "numbered_buttons"
            });   
            
            //Activa los links de la ayuda
            $(".lnkAyuda").click(function() { 
                nSlide=this.id.split("-")[1];
                $($("#divCarouselAyuda").find(".slide_number_"+nSlide)).trigger( $.Event('click') );
            });
            
            //Activa los tooltips de los links con clase tooltipLink
            $(".tooltipLink").tooltip({
                    bodyHandler: function() {
                            return $("<img/>").attr("src", this.id);
                    },
                    showURL: false
            });
            
            $("#tabs").tabs( "select", "#tabInicio" );
            //$("#tabMisFavoritos").tabs( "remove", 0);
            
            //Para el perfil DICONSA elimina las pestañas que no se requieren
            if ($("#_cp_").val()=="10") {
                $("#tabInicio").remove();
                $( "a[href='#tabInicio']").closest("li").remove();
                
                $("#tabMapaDelSitio").remove();
                $( "a[href='#tabMapaDelSitio']").closest("li").remove();
                
                $("#tabAyuda").remove();
                $( "a[href='#tabAyuda']").closest("li").remove();
                
                $("#tabContacto").remove();
                $( "a[href='#tabContacto']").closest("li").remove();
                
                $("#tabs").tabs( "refresh" );

                $("#tabs").tabs( "select", "#tabAplicaciones" );
                $("#divwait").dialog( "close" );
                $("#divwait").dialog("destroy"); 
                
            }
            
            $.fn.desktop.ajax(obj);


        });

    };

    $.fn.desktop.ajax = function(obj){
         $.ajax(
            {
            url: $.fn.desktop.options.xmlUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xmlConfig = new ActiveXObject("Microsoft.XMLDOM");
                 xmlConfig.async = false;
                 xmlConfig.validateOnParse="true";
                 xmlConfig.loadXML(data);
                 if (xmlConfig.parseError.errorCode>0) {
                        alert("Error de compilaciÃ³n xml:" + xmlConfig.parseError.errorCode +"\nParse reason:" + xmlConfig.parseError.reason + "\nLinea:" + xmlConfig.parseError.line);}
                }
                 else {
                    xmlConfig= data;}

                $.fn.desktop.handleSession(xmlConfig);
                
                //Activa las ligas del mapa de sitio
                $('.maplink').click(function() {
                    aTabsSecuence=this.id.split("-");
                    for (i=1;i<aTabsSecuence.length;i++) {
                        if (i==1)
                            $("#tabs").tabs( "select", "#"+aTabsSecuence[i] );
                        else
                            $("#"+aTabsSecuence[i-1]).tabs( "select", "#"+aTabsSecuence[i] );
                    }
                });
                
                $('.queued_grids:first').gridqueue({height:"800px;", width:"700px"});
                
            },
            error:function(xhr,err){
                alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
    };

    $.fn.desktop.handleSession = function(xml){
        var sFondo="";
        $(xml).find("registro").each(function(){
            //Carga los datos del xml en la variable de configuraciÃ³n
            if ($(this).find("parametro").length>0) {
            sParametro=$(this).find("parametro")[0].firstChild.nodeValue;

            if (sParametro=='escritorio.imagen de fondo') {
               sFondo=$(this).find("valor")[0].firstChild.nodeValue;
               if (sFondo!='')
                   obj.css('background-image', 'url('+sFondo+')');
            }

            if(sParametro==='escritorio.grid') {
                nClave=$(this).find("clave_parametro").text().split("\n")[0]
                sValor=$(this).find("valor").text().split("\n")[0];
                nApp=sValor.split(",")[0].split(":")[1];
                nForm=sValor.split(",")[1].split(":")[1];
                wsParameters=sValor.split(",")[2].split(":")[1];
                titulo=sValor.split(",")[3].split(":")[1];
                leyendas=sValor.split(",")[4].split(":")[1].replace("/",",");
                openKardex=sValor.split(",")[5].split(":")[1];
                inDesktop=sValor.split(",")[6].split(":")[1];
   
            }

            if (sParametro=='grid.actualizaDespuesDeOperacion') {
                 sValor=$(this).find("valor").text().split("\n")[0];
                 $("#_gado_").val(sValor);
            }
          }
        });
                
    }
})(jQuery);