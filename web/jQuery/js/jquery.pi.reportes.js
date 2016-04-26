/* 
 * Plugin de jQuery para cargar forma a través de un plugin
 * 
 */
( function($) {
    $.fn.reportes = function(opc){

        $.fn.reportes.settings = {
            titulo:"Reportes / ",
            app:"1",
            forma:"",
            pk:"0",
            pk_name:"clave_reporte",
            perfil: "",
            xmlUrl : "control?$cmd=form" , // "srvControl" "xml_tests/forma.app.xml",
            filtroForaneo: "",
            columnas: 2,
            modo:"",
            top: 122,
            height:500,
            width:510,
            error:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.reportes.options = $.extend($.fn.reportes.settings, opc);
            obj = $(this);
            $.fn.reportes.getGUI(obj);          
        });
 
    };
   
    
    $.fn.reportes.getGUI = function(obj){
        //Crea clave unica para forma
        var formSuffix =$.fn.reportes.options.app + "_" + $.fn.reportes.options.forma + "_" + $.fn.reportes.options.pk;
        var gridSuffix=$.fn.reportes.options.app + "_" + $.fn.reportes.options.forma + "_0";// + $.fn.reportes.options.datestamp;
        
        var sDialogo="";
        var sMainDivTabs="";
        var sDivTabs="";
        var sUlTabs="";
        var sBotonera="";
        //1. Primero crear el HTML necesario para contruir la interfaz de las relaciones
       
        sMainDivTabs="<div id='formTab_" + formSuffix +"'"+ 
            "' app='" + $.fn.reportes.options.app +
            "' forma='" + $.fn.reportes.options.forma +
            "' pk='" + $.fn.reportes.options.pk +
            "' pk_name='" + $.fn.reportes.options.pk_name + 
            "' perfil='" + $.fn.reportes.options.perfil +         
            "' >";
    
        sTituloTab="General";

        sBotonera+="<div align='right' style='clear:left'><table style='width:100%'><tr><td align='left' id='tdEstatus_" +formSuffix+"' class='estatus_bar'>&nbsp;</td><td align='right'>&nbsp;</td></tr></table></div>";
        
        sUlTabs="<ul><li><a href='#formTab_" + $.fn.reportes.options.app +"_"+  $.fn.reportes.options.forma +"'>Reportes</a></li></ul>";
        sDivTabs="<div id='formTab_" + $.fn.reportes.options.app +"_"+  $.fn.reportes.options.forma  +"'>"+
            "<div id='formGrid_"+ $.fn.reportes.options.app+"_"+  $.fn.reportes.options.forma + "_" + $.fn.reportes.options.pk + 
            "' app='" + $.fn.reportes.options.app +
            "' form='" +  $.fn.reportes.options.forma +
            "' titulo='Reportes' leyendas='" + 
            "' wsParameters='' align='center' class='queued_grids'>"+
            "<br /><br />Cargando informaci&oacute;n... <br /> <br />"+
            "<img src='img/loading.gif' />"+
            "</div>"+
            "</div>";  
            
        sMainDivTabs+=sUlTabs+sDivTabs+sBotonera+"</div>";
        sDialogo+="<div id='dlgModal_"+ formSuffix + "' title='Reporte'>" + sMainDivTabs + "</div>";
        obj.append(sDialogo);

        //Fuerza a que se haga scroll a la página
        //location.href=location.href.replace(location.hash,"") +"#"+sDateTime(new Date());
        $("html, body").animate({
            scrollTop: $("#top").offset().top + "px"
        }, {
            duration: 0,
            easing: "swing"
        });

        //Se crea el diálogo con el HTML completo
        $("#dlgModal_"+ formSuffix).dialog({
            modal: true,
            title: $.fn.reportes.options.titulo,    
            /*height:$.fn.reportes.options.height, */
            top:document.body.scrollTop+350,
            width:$.fn.reportes.options.width,
            open: function(event, ui) { 
                $(this).dialog( "option", "position","center" ); 
            },
            close: function(event, ui) {
                $(this).dialog("destroy");
                $(this).remove();
            }

        });

        //Se crean los tabs
        sGridId="#formGrid_"+formSuffix;
        $("#formTab_" + formSuffix).tabs();
        $(sGridId).appgrid(
        {
            app: "1",
            entidad:"257",
            pk:"0",
            editingApp:"1",
            wsParameters:"clave_forma="+$.fn.reportes.options.forma+ " AND clave_perfil="+ $("#_cp_").val(),
            titulo:$.fn.reportes.options.titulo,
            height:"250",
            openKardex:false,
            originatingObject:"",
            showFilterLink:false,
            insertInDesktopEnabled:"0",
            executeReports:true
        })
        .removeClass('queued_grids')
        .addClass('gridForeignContainer');         
        
        //Cierra el dialogo de espera
        $("#divwait").dialog( "close" ); 
    };
    
 
})(jQuery);