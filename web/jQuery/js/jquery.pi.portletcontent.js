/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
( function($) {
    $.fn.portletContent = function(opc){
        $.fn.portletContent.settings = {
            app:0,
            form:0,
            register:0,
            contentType: ""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.portletContent.options = $.extend($.fn.portletContent.settings, opc);
            $.fn.portletContent.options.contentType=$(this).attr("type");
            $(this).removeClass("process");
            $.fn.portletContent.getContent($(this));
        });
    };      
    
    $.fn.portletContent.getContent = function(obj){
        if ($.fn.portletContent.options.contentType=="log")
            sWS="control?$cmd=plain&$ta=log&$cf=" + $.fn.portletContent.options.form;
        
        if ($.fn.portletContent.options.contentType=="filter")
            sWS="control?$cmd=plain&$cf=93&$ta=select&$w=" + escape("clave_empleado=" +$("#_ce_").val()+ " AND clave_forma="+$.fn.portletContent.options.form);
        
        $.ajax({
            url: sWS,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlPC = new ActiveXObject("Microsoft.XMLDOM");
                    xmlPC.async = false;
                    xmlPC.validateOnParse="true";
                    xmlPC.loadXML(data);
                    if (xmlPC.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xmlPC.parseError.errorCode +"\nParse reason:" + xmlPC.parseError.reason + "\nLinea:" + xmlPC.parseError.line);
                }                
                else 
                    xmlPC = data;
                
                sHtml="";
                if ($(xmlPC).find("error").length)
                    sHtml = $(xmlPC).find("error").text();
                    
                if ($.fn.portletContent.options.contentType=="log") {
                    $(xmlPC).find("registro").each( function(){

                        dFecha=$(this).find("fecha")[0].firstChild.data;
                        sFoto=$(this).find("foto")[0].firstChild.data.toLowerCase();
                        sNombre=$(this).find("nombre")[0].firstChild.data;
                        sTipoEvento=$(this).find("clave_tipo_evento")[0].firstChild.data;
                        sForma=$(this).find("entidad")[0].firstChild.data;
                        sBitacora=$(this).find("descripcion_entidad")[0].firstChild.data
                        nForma=$(this).find("clave_forma")[0].firstChild.data;
                        nRegistro=$(this).find("clave_registro")[0].firstChild.data;
                   
                        sHtml+="<div class='bitacora'>" + sFoto +
                        sNombre + " " + sTipoEvento + " " + sForma + " " + 
                        "<a href='#' id='lnkBitacora_" + nAplicacion + "_" + nForma + "_" + nRegistro + "' class='lnkBitacora'>"+
                        sBitacora  + "</a> - <abbr class='timeago' title='"+dFecha.replace(".0","")+"'>" + dFecha.replace(".0","") + "</abbr>" +
                        "</div>";
                 
                    });
                                       
                }
                    
                if ($.fn.portletContent.options.contentType=="filter") {
                    sHtml="";
                    $(xmlPC).find("registro").each( function(){ 
                        nClave=$(this).find("clave_filtro")[0].firstChild.data;
                        if (nClave=="") return false
                        sFiltro=$(this).find("filtro")[0].firstChild.data
                        sW=escape($(this).find("consulta")[0].firstChild.data);
                        sSuffix =$.fn.portletContent.options.app + "_" + $.fn.portletContent.options.form + "_" + $.fn.portletContent.options.register;
                        sHtml+="<div class='link_toolbar'>"+
                        "<div class='linkSearch'><a class='linkSearch' href='#' id='lnkBusqueda_" + sSuffix  + "' data='" +sW+ "' forma='" + $.fn.portletContent.options.form + "' pk='" + nClave + "' >" + sFiltro + "</a></div>"+
                        "<div style='float:right'><div title='Eliminar filtro' style='cursor: pointer; float: right' class='closeLnkFiltro ui-icon ui-icon-close' pk='" + nClave + "' forma='" + $.fn.portletContent.options.form  + "'></div></div>" +
                        "</div>";                        
                    });                       
                }
                
                $(obj[0].childNodes[1]).html(sHtml);
                
                //Crea eventos de acuerdo al contenido entregado
                if ($.fn.portletContent.options.contentType=="log") {
                    $("abbr.timeago").timeago();                
                    // Quita los eventos asignados anteriormente
                    $(".lnkBitacora").unbind("click");
                
                    //Le asigna el evento clic a todos los links de la bitacora
                    $(".lnkBitacora").click(function(){
                        nApp=this.id.split("_")[1];
                        nForm=this.id.split("_")[2];
                        nRegister=this.id.split("_")[3];
                        $("body").form({
                            app: nApp,
                            forma:nForm,
                            pk:this.id.split("_")[3],
                            datestamp:$(this).attr("datestamp"),
                            modo:"update",
                            columnas:1,
                            filtroForaneo:"2=clave_aplicacion=" + nApp,
                            height:"500",
                            width:"80%",
                            originatingObject:"#lnkBitacora_" + nApp + "_" + nForm + "_" + nRegister
                        });
                    }); 
                }
                
                if ($.fn.portletContent.options.contentType=="filter") {
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
                        if (!confirm('¿Desea borrar el filtro seleccionado?')) return false;
                        $.post("control?$cmd=register$ta=delete","$cf=93&$pk=" + $(this).attr("pk"));
                        $(this).parent().parent().remove();
                    });
                }
            },
            error:function(xhr,err){
                if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1) {
                    //alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                    window.location='login.jsp';
                }else{        
                    alert("Error al recuperar registros: "+xhr.readyState+"\nstatus: "+xhr.status + "\responseText:"+ xhr.responseText);
                }
            }            
        });
    }
    

})(jQuery);