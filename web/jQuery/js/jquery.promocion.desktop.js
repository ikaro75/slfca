/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
(function($) {
    $.fn.desktop = function(opc) {

        $.fn.desktop.settings = {
            xmlUrl: "control?$cmd=plain&$cf=1&$ta=select&$w=" + escape("clave_empleado=" + $("#_ce_").val() + " AND parametro like 'escritorio.%'")  //"srvControl?$cmd=form&$cf=1&$ta=select&$w=" + escape("c.claveempleado=" +$("#_ce_").val()+ " AND c.parametro like 'escritorio.%'")
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.desktop.options = $.extend($.fn.desktop.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each(function() {
            
            $("#_status_").val("Creando calendario");
            obj = $(this);
            var date = new Date();
            var d = date.getDate();
            var m = date.getMonth();
            var y = date.getFullYear();

            /* Declaración de  objeto MyCalendar */
            $("#tab_0_0").tabs();
            $("#tab_1_34").tabs();
            $('#tabs').tabs("select", "#tabComunidad");
           
           $("#_status_").val("Creando portlets");
           
            $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .prepend("<span class='ui-icon ui-icon-minusthick'></span>")
                    .end()
                    .find(".portlet-content");

            $(".portlet-header .ui-icon").click(function() {
                $(this).toggleClass("ui-icon-minusthick").toggleClass("ui-icon-plusthick");
                $(this).parents(".portlet:first").find(".portlet-content").toggle();
            });

            $(".column").disableSelection();
            
            $("#_status_").val("Creando interfaz de usuario");
            

            //Se crea la interfaz de comunidades
            $("#_status_").val("Creando interfaz de comunidad");
            $("#tabComunidad").tabs();
            $(".menu_comunidad").button().parent().buttonset();

            //Se agrega el botón de nueva actividad a la agenda
            Calendar = {};
            Calendar.timePickerMinHour = 5;
            Calendar.timePickerMaxHour = 23;
            Calendar.timePickerMinuteInterval = 5;
            Calendar.useCalendarColorForEvent = 1;
            Calendar.defaultCalendarUserColor = '#0000FF';
            Calendar.currentCalendar = '1';
            Calendar.currentCalendars = Calendar.currentCalendar;
            //Calendar.activeCalendarsString		= '';
            Calendar.initialCalendar = '1';
            Calendar.currentCalendarType = 'normal';
            Calendar.currentCalendarColor = Calendar.defaultCalendarUserColor;
            Calendar.datepickerMinDate = '-12m';
            Calendar.datepickerMaxDate = '+12m';
            Calendar.FULLCAL_URL = 'http://localhost:8080/intranet/jQuery/js/';
            Calendar.hideMonthViewButton = '';
            Calendar.hideWeekViewButton = '';
            Calendar.hideDayViewButton = '';
            Calendar.showAgendaViewButton = '1';
            Calendar.weekViewType = 'agendaWeek';
            Calendar.dayViewType = 'agendaDay';
            Calendar.defaultView = 'month';
            
            var calEvent = {
                    url: 'events.jsp',
                    type: 'GET',
                    error: function() {
                        alert('Ocurrió un error al recuperar eventos');
                    }
            };
            
            $('#agenda').fullCalendar({
                theme: true,
                events:    calEvent,
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                showAgendaButton: Calendar.showAgendaViewButton,
                buttonText: {
                    today: 'Hoy',
                    month: 'mes',
                    week: 'semana',
                    day: 'día'
                },
                monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio',
                    'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
                monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
                dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
                dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
                firstDay: 1,
                editable: true,
                allDayText: "Todo el día" /*,
                eventSources: [
                    'events.jsp' ] */, 
                eventRender: function(event, element) {
                   element.attr("id","evento_"+event._id).find('span.fc-event-title').html(element.find('span.fc-event-title').text());
                   element.droppable(
                        {  activeClass: "ui-state-highlight",
                           accept: ".portletContactDiv, .portletGroupDiv", 
                           drop: function( event, ui ) {
                            var draggable = ui.draggable;
                            $( this ).addClass( "ui-state-highlight" );
                            var mensaje="";
                            var urlPost="";
                            if (draggable[0].className.indexOf("portletContactDiv")>-1) {
                                mensaje="Se compartió la actividad con " + draggable.text();
                                urlPost="control?$cmd=register&$ta=insert&clave_actividad=" + this.id.split("_")[1] + "&$cf=313&$pk=0&clave_contacto="+draggable[0].id.split("_")[1];
                            } else {
                                mensaje="Se compartió la actividad con el grupo " + draggable.text();
                                urlPost="control?$cmd=register&$ta=insert&clave_actividad=" + this.id.split("_")[1] + "&$cf=313&$pk=0&clave_grupo="+draggable[0].id.split("_")[1];
                                
                            }    
                            
                            $("#divwait")
                            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Asignando actividad...</p>")
                            .attr('title','Espere un momento por favor') 
                            .dialog({
                                height: 140,
                                modal: true,
                                autoOpen: true,
                                closeOnEscape:false
                            });
                           
                            $.ajax(
                                {
                                    url: urlPost,
                                    dataType: ($.browser.msie) ? "text" : "xml",
                                    success:  function(data){
                                         if (typeof data == "string") {
                                            xmlComparte = new ActiveXObject("Microsoft.XMLDOM");
                                            xmlComparte.async = false;
                                            xmlComparte.validateOnParse="true";
                                            xmlComparte.loadXML(data);
                                            if (xmlComparte.parseError.errorCode>0) {
                                                   alert("Error de compilación xml:" + xmlComparte.parseError.errorCode +"\nParse reason:" + xmlComparte.parseError.reason + "\nLinea:" + xmlComparte.parseError.line);}
                                            }
                                         else {
                                            xmlComparte = data;}
                                         
                                         var oError=$(xmlComparte).find("error");
                                         $("#divwait").dialog("close")
                                         $("#divwait").dialog("destroy"); 
                                        
                                         if (oError.length>0) {
                                            alert(oError.text());
                                         } else {
                                            alert(mensaje);
                                         }
                                        },
                                    error: function(xhr, err) {
                                        $("#divwait").dialog("close")
                                        $("#divwait").dialog("destroy"); 
                                        alert("Error al compartir la actividad");
                                    }
                            }); 
                            
                            event.stopImmediatePropagation();
                           }
                        }
                    );
                        
                }, 
                eventClick: function(event) {
                    $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                      });
                                            
                      $("#top").form({
                            app: "1",
                            forma:"101",
                            datestamp:$(this).attr("datestamp"),
                            modo:"update",
                            titulo: $.fn.appgrid.options.leyendas[1],
                            columnas:1,
                            pk: event.id,
                            filtroForaneo:"2=clave_aplicacion=2",
                            height:"500",
                            width:"80%",
                            originatingObject: "",
                            showRelationships:"true",
                            updateControl:""
                    });
                    return false;
                },
                eventDrop: function(event,dayDelta,minuteDelta,allDay,revertFunc) {

                    /*if (allDay) {
                        alert("El evento ahora aplica para todo el día");
                    }else{
                        alert("El evento tiene una hora específica");
                    }*/

                    if (!confirm(event.title + " fue movido " +
                        dayDelta + " días y " +
                        minuteDelta + " minutos. ¿Estás seguro de este cambio?")) {
                        revertFunc();
                    } else {
                        postConfig = "&$cf=101&$ta=update&$pk="+ event.id + 
                                                       "&fecha_inicial=" + sDateTimeToString(event._start) +
                                                       (event._end==null?"":"&fecha_final=" + sDateTimeToString(event._end));
                        $.post("control?$cmd=register",postConfig);                       
                    }

               },
               eventResize: function(event,dayDelta,minuteDelta,revertFunc) {
                if (!confirm(event.title + " fue movido " +
                        dayDelta + " días y " +
                        minuteDelta + " minutos. ¿Estás seguro de este cambio?")) {
                        revertFunc();
                    } else {
                        postConfig = "&$cf=101&$ta=update&$pk="+ event.id + 
                                                       "&fecha_inicial=" + sDateTimeToString(event._start) +
                                                       (event._end==null?"":"&fecha_final=" + sDateTimeToString(event._end));
                        $.post("control?$cmd=register",postConfig);                       
                    }
            },droppable: true
            });
            
            
            $("#_status_").val("Incorporando eventos a la agenda");
            $("table.fc-header").find("tr").append("<td class='fc-header-right'><button id='btnNuevaActividad' style='height: 26px'>nueva actividad</button></td>");
            $("#agenda").fullCalendar("render");
            
            $("#btnNuevaActividad").button().click(function() {
                $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                        });
                        
                $("body").form({
                        app: "1",
                        forma:"101",
                        datestamp:"111",
                        modo:"insert",
                        titulo: "Nueva actividad",
                        columnas:"1",
                        pk:"0",
                        filtroForaneo:"2=clave_aplicacion=2",
                        height:"500",
                        width:"80%",
                        originatingObject:"divgrid_1_101_0",
                        updateControl:"divgrid_1_101_0"
                    });
                    
            });
            
            $("#btnNuevoContacto").button().click(function() {
                
            $("body").form({
                        app: "1",
                        forma:"302",
                        datestamp:"111",
                        modo:"insert",
                        titulo: "Nueva contacto",
                        columnas:"1",
                        pk:"0",
                        filtroForaneo:"2=clave_aplicacion=2",
                        height:"500",
                        width:"80%",
                        originatingObject:"divgrid_1_302_0",
                        updateControl:"divgrid_1_302_0"
                    });
            });
            
            $("#btnNuevoGrupo").button().click(function() {
                $("body").form({
                        app: "1",
                        forma:"305",
                        datestamp:"111",
                        modo:"insert",
                        titulo: "Nuevo grupo",
                        columnas:"1",
                        pk:"0",
                        filtroForaneo:"2=clave_aplicacion=2",
                        height:"500",
                        width:"80%",
                        originatingObject:"divgrid_1_305_0",
                        updateControl:"divgrid_1_305_0"
                    });
            });
            
            $("#btnNuevoDocumento").button().click(function() {
                $("body").form({
                        app: "1",
                        forma:"311",
                        datestamp:"311",
                        modo:"insert",
                        titulo: "Nuevo documento",
                        columnas:"1",
                        pk:"0",
                        filtroForaneo:"2=clave_aplicacion=2",
                        height:"500",
                        width:"80%",
                        originatingObject:"divgrid_1_311_0",
                        updateControl:"divgrid_1_311_0"
                    });
            });
            
            //Activa los tooltips de los links con clase tooltipLink
            $(".tooltipLink").tooltip({
                bodyHandler: function() {
                    return $("<img/>").attr("src", this.id);
                },
                showURL: false
            });
            
            //Crear el portlet de contactos en el desktop
            $("#portletContacto").portletcontact();
            $("#portletGrupo").portletgroup();
            $("#portletDocumento").portletdocument();
            
            $("#tabs").tabs("select", "#tabInicio");
            
            //Se recuperan los ultimos posts colocándoles en el muro
            if ($("#_status_").val()!="") {
                $("#divwait")
                            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;" + $("#_status_").val() + ".</p>")
                            .attr('title','Espere un momento por favor') 
                            .dialog({
                                height: 140,
                                modal: true,
                                autoOpen: true,
                                closeOnEscape:false
               });
               setTimeout('$("#divMuro").wall({forma:299})',2000);
            } else {
              $("#divMuro").wall({forma:299});
            }
            
            $.fn.desktop.ajax(obj);

        });

    };

    $.fn.desktop.ajax = function(obj) {
        $("#_status_").val("Recuperando configuración del usuario");
        $.ajax(
                {
                    url: $.fn.desktop.options.xmlUrl,
                    dataType: ($.browser.msie) ? "text" : "xml",
                    success: function(data) {
                        if (typeof data == "string") {
                            xmlConfig = new ActiveXObject("Microsoft.XMLDOM");
                            xmlConfig.async = false;
                            xmlConfig.validateOnParse = "true";
                            xmlConfig.loadXML(data);
                            if (xmlConfig.parseError.errorCode > 0) {
                                alert("Error de compilación xml:" + xmlConfig.parseError.errorCode + "\nParse reason:" + xmlConfig.parseError.reason + "\nLinea:" + xmlConfig.parseError.line);
                            }
                        }
                        else {
                            xmlConfig = data;
                        }
                        
                        $.fn.desktop.handleSession(xmlConfig);

                        //Activa las ligas del mapa de sitio
                        $('.maplink').click(function() {
                            aTabsSecuence = this.id.split("-");
                            for (i = 1; i < aTabsSecuence.length; i++) {
                                if (i == 1)
                                    $("#tabs").tabs("select", "#" + aTabsSecuence[i]);
                                else
                                    $("#" + aTabsSecuence[i - 1]).tabs("select", "#" + aTabsSecuence[i]);
                            }
                        });
                        
                        //Selecciona el tab de promoción para el correcto despliegue del grid
                        $("#tabs").tabs("select", "#tabPromocion");
                        
                        $("#reportPortlet_34_314_0").portletReport(
                        {
                            app:34,
                            form:314
                        });
                        
                        $("#filterPortlet_34_314_0").portletFilter(
                        {
                            app:34,
                            form:314,
                            register:0
                        });
                
                        $('.queued_grids:first').gridqueue({height: "300"});
                        
                        $(".fc-content-paul").hide();
                        //Ejecuta los comandos definidos en el query string

                         $("#_status_").cqs();
                    },
                    error: function(xhr, err) {
                        alert("readyState: " + xhr.readyState + "\nstatus: " + xhr.status);
                        alert("responseText: " + xhr.responseText);
                    }
                });
    };

    $.fn.desktop.handleSession = function(xml) {
        var sFondo = "";
        $(xml).find("registro").each(function() {
            
            if ($(this).find("parametro").length>0) {
                //Carga los datos del xml en la variable de configuración
                sParametro = $(this).find("parametro")[0].firstChild.nodeValue;

                if (sParametro == 'escritorio.imagen de fondo') {
                    sFondo = $(this).find("valor")[0].firstChild.nodeValue;
                    if (sFondo != '')
                        obj.css('background-image', 'url(' + sFondo + ')');
                }

                if (sParametro === 'escritorio.grid') {
                    nClave = $(this).find("clave_parametro").text().split("\n")[0]
                    sValor = $(this).find("valor").text().split("\n")[0];
                    nApp = sValor.split(",")[0].split(":")[1];
                    nForm = sValor.split(",")[1].split(":")[1];
                    wsParameters = sValor.split(",")[2].split(":")[1];
                    titulo = sValor.split(",")[3].split(":")[1];
                    leyendas = sValor.split(",")[4].split(":")[1].replace("/", ",");
                    openKardex = sValor.split(",")[5].split(":")[1];
                    inDesktop = sValor.split(",")[6].split(":")[1];


                    $('#tabMisFavoritos').tabs("add", "#tabMisFavoritos_" + nClave, titulo);

                    $("#tabMisFavoritos_" + nClave).append("<div class='queued_grids'" +
                            " id='divDesktopGrid_" + nApp + "_" + nForm + "' " +
                            " app='" + nApp + "' " +
                            " form='" + nForm + "' " +
                            " wsParameters='" + wsParameters + "' " +
                            " titulo='" + titulo + "' " +
                            " leyendas='" + leyendas + "' " +
                            " openKardex='" + openKardex + "' " +
                            " inDesktop='" + inDesktop + "' " +
                            " class='queued_grids' " +
                            " editingApp='1' " +
                            " insertInDesktopEnabled='0'></div>");

                    //Agrega el favorito al mapa del sitio
                    $("#tabMisFavoritos_in_map").append("<dt><a id='mapLink-tabInicio-tabUser-tabFavoritos-tabMisFavoritos-tabMisFavoritos_" + nClave + "' class='maplink' href='#'>" + titulo + "</a></dt>");
                }

                if (sParametro == 'grid.actualizaDespuesDeOperacion') {
                    sValor = $(this).find("valor").text().split("\n")[0];
                    $("#_gado_").val(sValor);
                }
            }

        });

    }
    
})(jQuery);
