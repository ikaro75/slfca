( function($) {
    $.fn.portletLog = function(opc){
        $.fn.portletLog.settings = {
            app:0,
            form:0,
            register:0
        };
        
        if ($("#_refri_").val()=="1") return;
        
        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.portletLog.options = $.extend($.fn.portletLog.settings, opc);
            $.fn.portletLog.getContent($(this));
        });
    };      
    
    $.fn.portletLog.getContent = function(obj){
        sWS="control?$cmd=plain&$ta=log&$cf=" + $.fn.portletLog.options.form;
        $("#_status_").val("Cargando portlet de actividad reciente");
        $.ajax({
            url: sWS,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlPL = new ActiveXObject("Microsoft.XMLDOM");
                    xmlPL.async = false;
                    xmlPL.validateOnParse="true";
                    xmlPL.loadXML(data);
                    if (xmlPL.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xmlPL.parseError.errorCode +"\nParse reason:" + xmlPL.parseError.reason + "\nLinea:" + xmlPL.parseError.line);
                }                
                else 
                    xmlPL = data;
                
                $("#_status_").val("Construyendo portlet de actividad reciente");
                sHtml="";
                if ($(xmlPL).find("error").length)
                    sHtml = $(xmlPL).find("error").text();
                    
                $(xmlPL).find("registro").each( function(){
                    dFecha=$(this).find("fecha")[0].firstChild.data;
                    sFoto= $(this).find("foto")[0].firstChild.data;
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
                                       
                $(obj[0].childNodes[1]).html(sHtml);

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

               $("#_status_").val("");
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