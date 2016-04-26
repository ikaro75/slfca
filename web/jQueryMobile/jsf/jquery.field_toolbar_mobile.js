/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    
    $.fn.fieldtoolbar = function(opc){

        $.fn.fieldtoolbar.settings = {
            app:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.fieldtoolbar.options = $.extend($.fn.fieldtoolbar.settings, opc);
            obj = $(this);
            /*$.fn.fieldtoolbar.options.form=$(this).attr("forma");
             $.fn.fieldtoolbar.options.control=$(this).attr("control");
             $.fn.fieldtoolbar.options.titulo_agregar=$(this).attr("titulo_agregar");
             $.fn.fieldtoolbar.options.titulo_editar=$(this).attr("titulo_editar");*/

            var suffix=obj[0].previousSibling.id;
            sTipoBoton=obj.attr("tipo")

            if (sTipoBoton=="foreign_toolbar") {
                sHtml="<table class='ui-pg-table navtable' cellspacing='0' cellpadding='0' border=0' style='float: left; table-layout: auto;'><tr>" +
                "<td class='ui-pg-button ui-corner-all' title='Insertar registro' style='border-width: 1px;' >" +
                "<div class='ui-pg-div'>" +
                "<span id='spnInsrt" + suffix + "' class='ui-icon ui-icon-plus' control='" + obj.attr("control") + "' tipo_accion='insert' forma='" + obj.attr("forma") + "'></span></div></td>"  + //Botón de insertar
                "<td class='ui-pg-button ui-corner-all' title='Editar registro' style='border-width: 1px;' >" +
                "<div class='ui-pg-div' >" +
                "<span id='spnUpdt" + suffix + "' control='" + obj.attr("control") + "' tipo_accion='update' forma='" + obj.attr("forma") + "' class='ui-icon ui-icon-pencil'></span></div></td>" + // Botón de editar
                "</tr></table>";
            }

            if (sTipoBoton=="calendar_button") {
                sHtml="<table class='ui-pg-table navtable' cellspacing='0' cellpadding='0' border=0' style='float: left; table-layout: auto;'><tr>" +
                "<td class='ui-pg-button ui-corner-all' title='Muestra calendario'  style='border-width: 1px;' ><div class='ui-pg-div'>" +
                "<span id='spnCalendar_" + obj.attr("control").replace(/ #/g,"_") + "' forma='" + obj.attr("forma") + "' titulo_agregar='" + obj.attr("titulo_agregar") + "' control='" + obj.attr("control")+ "' class='ui-icon ui-icon-calendar'></span></div></td>"  + //Botón de calendario
                "</tr></table>";
            }

            if (sTipoBoton=="calculator_button") {
                sHtml="<div id='div_spnCalculator" + suffix + "' >"+
                "<table class='ui-pg-table navtable' cellspacing='0' cellpadding='0' border=0' style='float: left; table-layout: auto;'><tr>" +
                "<td class='ui-pg-button ui-corner-all' title='Muestra calculadora' style='border-width: 1px;'><div class='ui-pg-div'>" +
                "<span id='spnCalculator_" + obj.attr("control").replace(/ #/g,"_") + "' forma='" + obj.attr("forma") + "' titulo_agregar='" + obj.attr("titulo_agregar") + "' control='" + obj.attr("control")+ "' class='ui-icon ui-icon-calculator calculator-trigger'></span></div></td>"  + //Botón de insertar
                "</tr></table>"
            }

           if (sTipoBoton=="share_toolbar") { 
               sHtml=
                "<table style='width:99%;' class='ui-pg-table navtable' cellspacing='0' cellpadding='0' border=0' style='float: left; table-layout: auto;'><tr>" +
                "<td style='width:20px;' class='ui-pg-button ui-corner-all' title='Compartir un archivo de tu computadora' style='border-width: 1px;' >" +
                "<div class='ui-pg-div'>" +
                "<span id='spnShareFile_" + $.fn.linkpreview.options.timestamp  + "' class='ui-icon ui-icon-arrowthick-1-n' control='" + obj.id + "' tipo_accion='updload' forma=''></span></div></td>"  + //Botón de upload
                "<td style='width:20px;' class='ui-pg-button ui-corner-all' title='Compartir un reporte' style='border-width: 1px;' >" +
                "<div class='ui-pg-div' >" +
                "<span id='spnShareReport_" + $.fn.linkpreview.options.timestamp  + "' class='ui-icon ui-icon-note' control='" + obj.id + "' tipo_accion='reporte' forma='" + obj.attr("forma") + "' ></span></div></td>" + // Botón de reporte
                "<td style='width:20px;' class='ui-pg-button ui-corner-all' title='Compartir un reporte' style='border-width: 1px;' >" +
                "<div class='ui-pg-div' >" +
                "<span id='spnShareEvent_" + $.fn.linkpreview.options.timestamp  + "' class='ui-icon ui-icon-calendar' control='" + obj.id + "' tipo_accion='evento' forma='" + obj.attr("forma") + "' ></span></div></td>" + // Botón de evento        
                "<td>&nbsp;</td>" +
                "<td >" +
                "<input style='float:right;' type='button' value='Compartir' id='btnCompartir_"+ $("#_ce_").val() +"'></td>" + // Botón de compartir        
                "</tr></table>"+
                "<div style='width:79%' id='wallz_" + $.fn.linkpreview.options.timestamp + "' ><ul id='post_" + $.fn.linkpreview.options.timestamp + "' /></div>" +
                "</div>"
            }
            
            obj.html(sHtml);

            $(".ui-pg-button").hover(
                function () {
                    $(this).addClass('ui-state-hover');
                },
                function () {
                    $(this).removeClass('ui-state-hover');
                }
            );

            if (sTipoBoton=="foreign_toolbar") {
                
                //Quita eventos anteriores
                $("#spnInsrt" + suffix +", #spnUpdt" + suffix).unbind("click");
                $("#spnInsrt" + suffix +", #spnUpdt" + suffix).click(function(){
                    
                    $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                    });
                        
                    if ($(this).attr("tipo_accion")=="update") {
                        nPK=$("#" + $(this).attr("control")+ " :selected").val();
                        if (nPK=="") {
                            alert('Seleccione un elemento de la lista para poder editarlo');
                            return;
                        }                    
                    } else
                        nPK="0";
                    
                    $("body").form({
                        app:  $.fn.fieldtoolbar.options.app,
                        forma: $(this).attr("forma"),
                        modo:$(this).attr("tipo_accion"),
                        titulo: $(this).attr("titulo_agregar"),
                        columnas:1,
                        pk: nPK,
                        height:400,
                        width:"80%",
                        updateControl:$(this).attr("control"),
                        updateForeignForm:$(this).attr("forma"),
                        originatingObject: "#" +$(this).attr("control")

                    });
                })

            /*$("#spnUpdt" + suffix).click(function(){

                    nPK=$("#" + $(this).attr("control")+ " :selected").val();
                    if (nPK=="") {
                        alert('Seleccione un elemento de la lista para poder editarlo');
                        return;
                    }

                    $("body").form({app:  $.fn.fieldtoolbar.options.app,
                                forma:$(this).attr("forma") ,
                                modo:"update",
                                titulo: $(this).attr("titulo_editar"),
                                columnas:1,
                                pk: nPK,
                                height:400,
                                width:"80%",
                                updateControl:suffix,
                                updateForeignForm:$(this).attr("forma"),
                                originatingObject:$(this).id
                            });

             })*/
            }

            if (sTipoBoton=="calendar_button") {
                //Se activa el datepicker para los campos con seudoclase fecha
                $("#spnCalendar_" + obj.attr("control").replace(/ #/g,"_")).click(function(){
                    $("#"+$(this).attr("control")).datepicker('show');
                });
            }

            if (sTipoBoton=="calculator_button") {
                //Se activa el datepicker para los campos con seudoclase fecha
                $("#spnCalculator_" + obj.attr("control").replace(/ #/g,"_")).click(function(){
                    $("#"+$(this).attr("control")).calculator('show');
                });
            }
        });

    };

})(jQuery);