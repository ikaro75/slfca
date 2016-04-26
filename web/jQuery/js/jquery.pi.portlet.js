( function($) {
    
    $.fn.portlets = function(opc){

        $.fn.portlets.settings = {
            app:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
           $.fn.portlets.options = $.extend($.fn.portlets.settings, opc);
           //$(this).css("height","auto");
           
           if ($(this).attr("class").indexOf("1")==0) {
                    //Oculta botones para cerrar
                    $(".closeLnkFiltro").hide();
                    
                    //Hace bind del liga del búsqueda
                    $(".linkSearch").click(function(){
                        nAplicacion=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                        sW=$(this).attr("data");
                        data=$(this).attr("data");
                        //$.fn.appmenu.setGridFilter(nAplicacion+"_"+sForma+"_0",nAplicacion,nForma,data);
                        sGridIdSuffix=nAplicacion + "_" + nForma + "_0";
                        if (data==undefined){
                            data="";
                            $("#lnkRemoveFilter_grid_" + sGridIdSuffix).remove();
                        }
                        else {
                            //Si no existe el link para quitar filtro, lo establece
                            if ($("#lnkRemoveFilter_grid_" + sGridIdSuffix).length==0) {                                        
                                //oGridHeader=$("#grid_").parent().parent().parent()[0].childNodes[0].childNodes[1]
                                oGridHeader=$("#gview_grid_" +sGridIdSuffix).find("span.ui-jqgrid-title");
                                $(oGridHeader[0]).append("<a style='margin-left:10px' href='#' id='lnkRemoveFilter_grid_" + sGridIdSuffix +"'>(Quitar filtro)</a>");

                                //Verifica URL anterior del grid filtrado
                                var previousWhere="";
                                var previusUrl=$("#grid_" + sGridIdSuffix)[0].p.url;
                                if (previusUrl>-1);
                                previousWhere=previusUrl.split("&$w=")[1];

                                //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                                $("#lnkRemoveFilter_grid_" + sGridIdSuffix).click(function() {
                                        $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
                                            url:previusUrl
                                        }).trigger("reloadGrid");

                                    $(this).remove();

                                });
                            }
                        }

                        $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
                            url:"control?$cmd=grid&$cf=" + nForma + "&$ta=select&$dp=body&$w=" +data
                        }).trigger("reloadGrid");
                    });                   

                    //Hace bind con los divs padres del link en el evento hover
                    $(".portletFiltroSuper").hover(
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
           } else if ($(this).attr("class").indexOf("2")==0) {
                // Quita los eventos asignados anteriormente
                $(".lnkReporte").unbind("click");
                
                //Le asigna el evento clic a todos los links de la bitacora
                $(".lnkReporte").click(function(){
                    
                    if ($(this).attr("parametros")==0) {
                        //lanzalo 
                        var caracteristicas = "height=800,width=800,scrollTo,resizable=1,scrollbars=1,location=0";
                        window.open('control?$cmd=report&$cr='+$(this).attr("reporte")+"&$pk=", "_blank", caracteristicas); 
                       
                    } else {
                        $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                        });
                    
                        $("#top").reportParameter ({
                            titulo:"Parámetros del reporte '" + $(this).text() + "'",
                            app:"1",
                            forma:$(this).attr("forma"),
                            reporte: $(this).attr("reporte"),
                            filtroForaneo: "",
                            top: 122,
                            height:500,
                            width:510,
                            events:[],
                            error:""
                        }
                        );                     
                    }
                    

                });               
           } else if ($(this).attr("class").indexOf("3")==0) {
               $("abbr.timeago").timeago();
               //Le asigna el evento clic a todos los links de la bitacora
                $(".lnkBitacora").click(function(){
                    $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                        });
                    
                    nAplicacion=this.id.split("_")[1];
                    nForma=this.id.split("_")[2];
                    nRegistro=this.id.split("_")[3];    
                    
                    $("body").form2({
                        app: nAplicacion,
                        forma: nForma,
                        pk:nRegistro,
                        datestamp:$(this).attr("datestamp"),
                        modo:"update",
                        columnas:1,
                        filtroForaneo:"2=clave_aplicacion=" + nAplicacion,
                        height:"500",
                        width:"80%",
                        originatingObject:"#lnkBitacora_" + nAplicacion + "_" + nForma + "_" + nRegistro
                    });
                }); 
                
           } else if ($(this).attr("class").indexOf("4")==0) {
                $("abbr.timeago").timeago();
                // Quita los eventos asignados anteriormente
                $(".lnkComentario").unbind("click");
                
                //Le asigna el evento clic a todos los links de la bitacora
                $(".lnkComentario").click(function(){            
                        $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                        });
                        
                        nAplicacion=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                        nRegistro=this.id.split("_")[3];    

                        $("body").form2({
                        app: this.id.split("_")[1],
                        forma:this.id.split("_")[2],
                        pk:this.id.split("_")[3],
                        datestamp:$(this).attr("datestamp"),
                        modo:"update",
                        columnas:1,
                        filtroForaneo:"2=clave_aplicacion=" + nAplicacion,
                        height:"500",
                        width:"80%",
                        originatingObject:"#lnkComentario_" + nAplicacion + "_" + nForma + "_" + nRegistro
                    });
                });                
           }
       });

    };

})(jQuery);