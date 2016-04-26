
( function($) {
    $.fn.portletFilter = function(opc){
        $.fn.portletFilter.settings = {
            app:0,
            form:0,
            register:0
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.portletFilter.options = $.extend($.fn.portletFilter.settings, opc);
            $.fn.portletFilter.getContent($(this));
        });
    };      
    
    $.fn.portletFilter.getContent = function(obj){    
        $("#_status_").val("Cargando filtros de la aplicación");
        sWS="control?$cmd=plain&$ta=select&$cf=93&$w=clave_forma=" + $.fn.portletFilter.options.form;
        $.ajax({
            url: sWS,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlPF = new ActiveXObject("Microsoft.XMLDOM");
                    xmlPF.async = false;
                    xmlPF.validateOnParse="true";
                    xmlPF.loadXML(data);
                    if (xmlPF.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xmlPF.parseError.errorCode +"\nParse reason:" + xmlPF.parseError.reason + "\nLinea:" + xmlPF.parseError.line);
                }                
                else 
                    xmlPF = data;
                $("#_status_").val("Construyento portlet de filtros");
                sHtml="";
                if ($(xmlPF).find("error").length)
                    sHtml = $(xmlPF).find("error").text();
                    
                var nAplicacion="";
                
                $(xmlPF).find("registro").each( function(){
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

                    //$(obj[0].childNodes[1]).html(sHtml);
                    $(obj[0].childNodes[1]).append(sBusquedas);

                    //Oculta botones para cerrar
                    $(".ui-icon-close", obj[0].childNodes[1]).hide();
                    
                    //Hace bind de la liga de búsqueda
                    $("#lnkBusqueda_" + sSuffix).click(function(){
                        nAplicacion=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                        sW=$(this).attr("data");
                        /*var newE = $.Event('click');
                        newE.gridFilter=$(this).attr("data");*/
                        data=$(this).attr("data");
                        $.fn.portletFilter.setGridFilter(nAplicacion+"_"+nForma+"_0",nAplicacion,nForma,data);
                        
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
               $("#_status_").val("");
            },
            error:function(xhr,err){
                if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1) {
                    //alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                    window.location='login.jsp';
                }else{        
                    alert("Error al recuperar registros: "+xhr.readyState+"\nstatus: "+xhr.status + "\responseText:"+ xhr.responseText);
                }
                $("#_status_").val("");
            }            
        });
    }

    $.fn.portletFilter.setGridFilter = function (sGridIdSuffix,nApp, nEntidad,data) {
        //Recarga el grid por si tiene algún filtro
        if (data==undefined){
            data="";
            $("#lnkRemoveFilter_grid_" +sGridIdSuffix ).remove();
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
                        });
                    }
                    else 
                        $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
                            url:previusUrl
                        }).trigger("reloadGrid");
                    
                    $(this).remove();
                        
                });
            }
        }
                            
        $("#grid_"+sGridIdSuffix).jqGrid('setGridParam',{
            url:"control?$cmd=grid&$cf=" + nEntidad + "&$ta=select&$dp=body&$w=" +data
        }).trigger("reloadGrid");
    };

})(jQuery);