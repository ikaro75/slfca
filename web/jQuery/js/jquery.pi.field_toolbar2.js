/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
( function($) {
    
    $.fn.fieldtoolbar2 = function(opc){

        $.fn.fieldtoolbar2.settings = {
            app:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.fieldtoolbar2.options = $.extend($.fn.fieldtoolbar2.settings, opc);
             obj = $(this);
             /*$.fn.fieldtoolbar2.options.form=$(this).attr("forma");
             $.fn.fieldtoolbar2.options.control=$(this).attr("control");
             $.fn.fieldtoolbar2.options.titulo_agregar=$(this).attr("titulo_agregar");
             $.fn.fieldtoolbar2.options.titulo_editar=$(this).attr("titulo_editar");*/

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

                    if ($(this).attr("tipo_accion")=="update") {
                        nPK=$("#" + $(this).attr("control")+ " :selected").val();
                        if (nPK=="") {
                            alert('Seleccione un elemento de la lista para poder editarlo');
                            return;
                        }                    
                    } else
                        nPK="0";
                    
                    $("body").form2({
                        app:  $.fn.fieldtoolbar2.options.app,
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

                    $("body").form2({app:  $.fn.fieldtoolbar2.options.app,
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