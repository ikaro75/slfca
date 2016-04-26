/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.formqueue = function(opc){

        $.fn.formqueue.settings = {
            titulo:"",
            app:"", 
            forma:"",
            pk:"",
            pk_name:"",
            xmlUrl : "control?$cmd=form", //"xml_tests/forma.app.xml",
            filtroForaneo: "",
            columnas: 1,
            modo:"",
            top: 122,
            height:500,
            width:510,
            datestamp:"",
            updateControl:"",
            updateForeignForm:"",
            originatingObject:"",
            showRelationships:"false"
        };
           
        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
          $.fn.formqueue.options = $.extend($.fn.formqueue.settings, opc);
          
          $("body").form({
            app:  $.fn.formqueue.options.app,
            forma: $.fn.formqueue.options.forma,
            datestamp: $.fn.formqueue.options.datestamp,
            modo: $.fn.formqueue.options.modo,
            titulo:  $.fn.formqueue.options.leyenda,
            columnas: $.fn.formqueue.options.columnas,
            pk: $.fn.formqueue.options.pk,
            filtroForaneo:$.fn.formqueue.options.filtroForaneo,
            height:$.fn.formqueue.options.height,
            width:$.fn.formqueue.options.width,
            originatingObject: $.fn.formqueue.options.originatingObject,
            showRelationships:$.fn.formqueue.options.showRelationships,
            updateControl: $.fn.formqueue.options.updateControl
        });    
        });

 };


})(jQuery);