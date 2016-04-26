/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.foreign_grid = function(opc){

        $.fn.foreign_grid.settings = {
            height:"100%"
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.foreign_grid.options = $.extend($.fn.foreign_grid.settings, opc);
             obj = $(this);
             obj.html("<div align='center' class='cargando'><br /><br />Cargando informaci&oacute;n...<br /><img src='img/loading.gif' /></div>")
             obj.appgrid({app: $(this).attr("app"),
                             entidad: $(this).attr("form"),
                             wsParameters: $(this).attr("rel"),
                             titulo:$(this).attr("titulo"),
                             leyendas:["Nuevo registro", "Edición de registro"],
                             height:$.fn.foreign_grid.options.height
                             });

        });

    };

})(jQuery);