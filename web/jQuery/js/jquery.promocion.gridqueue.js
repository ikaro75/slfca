/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    $.fn.gridqueue = function(opc){

        $.fn.gridqueue.settings = {
            height:"300"
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $("#_status_").val("Inicializando cola de grids");
            $.fn.gridqueue.options = $.extend($.fn.gridqueue.settings, opc);
             obj = $(this);
             obj.html("<div align='center' class='cargando'><br /><br />Cargando informaci&oacute;n...<br /><img src='img/loading.gif' /></div>")
             $.fn.gridqueue.getGridConfig(obj);
             obj.removeClass("queued_grids");
             obj.addClass("desktopGridContainer");
             
             //Verifica si hay grids pendientes en la cola y destruye el dialogo de espera
             //si este es el caso
             if ($(".queued_grids").length==0) {
                 //Verifica si se reseteó el password
                 if ($("#_un_").attr("cp")=="1") {
                    $("#_status_").val("Solicitando cambio de contraseña");
                    $("body").form({
                            app: 1,
                            forma:258,
                            datestamp:obj.attr("datestamp"),
                            modo:"update",
                            titulo: "Cambio de password ",
                            columnas:1,
                            pk:$("#_ce_").val(),
                            filtroForaneo:"",
                            height:"500",
                            width:"80%",
                            originatingObject: "",
                            updateControl:""
                        });                    
                 }
                 else {
                    //$("#divwait").dialog( "close" );
                    //$("#_status_").val("");
                 }
                 
                 //$("#divwait").dialog("destroy");
             }
        });

 };

$("#password,#nombre").change( function() {  
	if ($("#password").val()!=$("#nombre").val()) {
		alert('El password y la confirmación no coinciden, verifique');
		$("#password").val('');
		$("#nombre").val('');
	}
 });

$.fn.gridqueue.getGridConfig= function(obj){

//Si está adentro de un tab, establece el tab actual
//Pasa el control al tab del grid para cálculo de ancho
//Devuelve el control al tab anterior

/*
 *                $('#tabUser').tabs( "select", "#tabFavoritos");  
                
 **/
    //$("#divwait").dialog("close");
    var restaura=false;
    var grandParentId= obj.parent().parent()[0].id;
    var parentId=obj.parent()[0].id;
    var nWidth=0;
    //Esta línea es para considerar el grid construido en contacto
    if (grandParentId=="") {
        grandParentId=obj.parent().parent().parent().parent().parent().parent()[0].id;
    } else {
        nWidth=obj.parent().width();
    }
    
    var tabIndex = $("#tabs").tabs('option', 'selected');
    //Las siguientes lineas comprueban si se va a agregar en favoritos
    
    if (grandParentId=='appTab_1_34_314') {
             restaura=true;
             $("#tabs").tabs('select','#tabPromocion'); 
             nWidth=obj.parent().width();
    } 
    if (nWidth==0) { 
         if (grandParentId=='agenda') {
             restaura=true;
             $("#tabs").tabs('select','#tabComunidad');
             nWidth=obj.parent().width()
         }
    } 
    
    obj.appgrid({app: obj.attr("app"),
          entidad: obj.attr("form"),
          wsParameters: obj.attr("wsParameters"),
          titulo:obj.attr("titulo"),
          leyendas:obj.attr("leyendas").split(","),
          inDesktop:obj.attr("indesktop"),
          height:$.fn.gridqueue.options.height,
          openKardex:obj.attr("openKardex"),
          removeGridTitle:true,
          showFilterLink:false,
          inQueue:true,
          insertInDesktopEnabled:0,
          editingApp:"1",
          width:nWidth
     });
     
     if (restaura) {
         $("#tabs").tabs('select',"tabInicio"); 
     }
     
    //$("#divwait").dialog('open');    

}

})(jQuery);