/* 
 * Plugin de jQuery para cargar forma a través de un plugin
 * 
 */
(function($) {
    $.fn.form_mobile_queue = function(opc) {

        $.fn.form_mobile_queue.settings = {
            titulo: "",
            app: "",
            forma: "",
            pk: "",
            pk_name: "",
            xmlUrl: "control?$cmd=form", // "srvControl" "xml_tests/forma.app.xml",
            filtroForaneo: "",
            columnas: 2,
            modo: "",
            top: 122,
            height: 500,
            width: 510,
            datestamp: "",
            updateControl: "",
            updateForeignForm: "",
            originatingObject: "",
            showRelationships: "false",
            permiteDuplicarRegistro: "false",
            permiteInsertarComentarios: "false",
            permiteGuardarComoPlantilla: "false",
            events: [],
            error: ""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each(function() {
            $.fn.form_mobile_queue.options = $.extend($.fn.form_mobile_queue.settings, opc);
            $(this).form_mobile({app: 1,
               forma: $.fn.form_mobile_queue.options.forma,
               modo: $.fn.form_mobile_queue.options.modo,
               columnas: $.fn.form_mobile_queue.options.columnas,
               pk: $.fn.form_mobile_queue.options.pk,
               filtroForaneo: $.fn.form_mobile_queue.options.filtroForaneo,
               height: $.fn.form_mobile_queue.options.height,
               width: $.fn.form_mobile_queue.options.width,
               originatingObject: "",
               updateControl: $.fn.form_mobile_queue.options.updateControl
           });
        });

    };

})(jQuery);