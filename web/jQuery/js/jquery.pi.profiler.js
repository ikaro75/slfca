( function($) {
    $.fn.profiler = function(opc){

        $.fn.profiler.settings = {
            pk:""
        };
           
        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
          $.fn.profiler.options = $.extend($.fn.profiler.settings, opc);

            $(this).addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .prepend("<span class='ui-icon ui-icon-minusthick'></span>")
                    .end()
                    .find(".portlet-content");

           $(this).find(".portlet-header .ui-icon").click(function() {
                $(this).toggleClass("ui-icon-minusthick").toggleClass("ui-icon-plusthick");
                $(this).parents(".portlet:first").find(".portlet-content").toggle();
            });
            
            $(this).find(".portlet-content").html("<div id='profilerWait' style='width:100%; margin:0 auto 0 auto; clear: both; text-align: center;'><br /><br /><br /><br />Cargando...<br /><img src='img/loading.gif' /></div>");
            
            $.ajax(
            {
                url: "control?$cmd=form&$cf=6&$pk=" + this.id.split("_")[1] + "&$ta=update&$w=clave_empleado=" + this.id.split("_")[1],
                dataType: ($.browser.msie) ? "text" : "xml",
                success:  function(data){
                     if (typeof data == "string") {
                        xmlProfile = new ActiveXObject("Microsoft.XMLDOM");
                        xmlProfile.async = false;
                        xmlProfile.validateOnParse="true";
                        xmlProfile.loadXML(data);
                        if (xmlProfile.parseError.errorCode>0) {
                               alert("Error de compilación xml:" + xmlProfile.parseError.errorCode +"\nParse reason:" + xmlProfile.parseError.reason + "\nLinea:" + xmlProfile.parseError.line);}
                        }
                     else {
                        xmlProfile = data;}

                     var oError=$(xmlProfile).find("error");

                     if (oError.length>0) {
                        $(this).find(".portlet-content").html(oError.text());
                     } 
                     
                     sHTML="<img src='" + $(xmlProfile).find("foto")[0].childNodes[0].textContent + "' style='float: left;margin-right: 10px;'/>" +
                             "<h3>" + $(xmlProfile).find("nombre")[0].childNodes[0].textContent + " " + $(xmlProfile).find("apellido_paterno")[0].childNodes[0].textContent + " " +  $(xmlProfile).find("apellido_materno")[0].childNodes[0].textContent + "</h3>" +
                             $(xmlProfile).find("email")[0].childNodes[0].textContent;

                     $("#profilerWait").parent().html(sHTML);
                     
                    },
                error: function(xhr, err) {
                    alert("Error al compartir recuperar información de usuario");
                }
            });
 
        });

 };


})(jQuery);
