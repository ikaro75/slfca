/* 
 * Plugin de jQuery para cargar menú de sesión a través de un plugin
 * 
 */
       
( function($) {
    $.fn.cqs = function(opc){

        $.fn.cqs.settings = {
            obj: null,
            cleanUp: true
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.cqs.options = $.extend($.fn.cqs.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
             $.fn.cqs.options.obj = this.id;
             
             //Genera retraso en evento
             $.fn.cqs.queryStringBoot($.fn.cqs.options.obj, "");
             
        });

    };

    $.fn.cqs.queryStringBoot = function(obj, next) {
          
        //Verifica el estatus de la aplicación
        if ($("#" + obj).val()!="") {
             $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;" + $("#" + obj).val() + "</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                    }); 
             setTimeout("$.fn.cqs.queryStringBoot('" + obj + "','')",2000);
             return; 
        }
        
        if ($("#_refri_").val()!="") {
            aComandosQS=$("#_refri_").val().split(";");            
            categoria=0;
            aplicacion=0;
            grid=0;
            forma=0;
            updateform="";
            
            for (i=0; aComandosQS.length-1;i++ ) {

                if (aComandosQS[i]=="") break;
                
                if (aComandosQS[i].split("=")[0]=="error") {
                    alert(aComandosQS[i].split("=")[1]);
                    break;
                }
                
                if (aComandosQS[i].split("=")[0]=="category") {
                    categoria=aComandosQS[i].split("=")[1];                  
                    $("#tabs").tabs("select",  parseInt(categoria));
                }

                if (aComandosQS[i].split("=")[0]=="app") {
                    aplicacion=aComandosQS[i].split("=")[1];
                }

                if (aComandosQS[i].split("=")[0]=="grid" ) {
                    
                    grid = aComandosQS[i].split("=")[1];
                    
                    if (next=="grid" || next=="") {
                        $("#divwait")
                            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Abriendo aplicación...</p>")
                            .attr('title','Espere un momento por favor') 
                            .dialog({
                                height: 140,
                                modal: true,
                                autoOpen: true,
                                closeOnEscape:false
                            });
                        $("#showEntity_"+ categoria + "_" + aplicacion + "_" + grid).click();
                        
                        if (aComandosQS[i+1]=="") {
                          $.fn.cqs.cleanUp =true;
                          break;
                        }
                        
                        if (i<aComandosQS.length-1) {
                            $.fn.cqs.cleanUp = false;
                            setTimeout("$.fn.cqs.queryStringBoot('" + obj + "', '" + aComandosQS[i+1].split("=")[0] + "')",2000);
                            break;                        
                        }
                    }
                }
                
                if (aComandosQS[i].split("=")[0]=="insertForm" && (next=="insertForm" || next=="")) {
                    if ($("#" + obj).val()!="") {
                        $.fn.cqs.cleanUp = false;
                        setTimeout("$.fn.cqs.queryStringBoot('" + obj + "','insertForm')",2000);
                        return;
                    };
                
                    $("#divwait")
                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                    .attr('title','Espere un momento por favor') 
                    .dialog({
                        height: 140,
                        modal: true,
                        autoOpen: true,
                        closeOnEscape:false
                    });
                    
                    $("#top").form(
                       {app: aplicacion,
                        forma: grid,
                        datestamp:"",
                        modo:"insert",
                        columnas:1,
                        pk:0,
                        filtroForaneo:"",
                        height:"500",
                        width:"80%",
                        originatingObject:"divgrid_" + aplicacion + "_" + grid + "_0",
                        updateControl:"divgrid_" + aplicacion + "_" + grid + "_0"
                    });
                }
                
                if (aComandosQS[i].split("=")[0]=="updateForm" && (next=="updateForm" || next=="")) {
                    if ($("#" + obj).val()!="") {
                        $.fn.cqs.cleanUp = false;
                        setTimeout("$.fn.cqs.queryStringBoot('" + obj + "','updateForm')",2000);
                        return;
                    };
                    
                    if ($("#dlgModal_" + aplicacion + "_" + grid + "_" + aComandosQS[i].split("=")[1]).length==0) {
                        
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
                            app: aplicacion,
                            forma:grid,
                            datestamp:"",
                            modo:"update",
                            columnas:1,
                            pk:aComandosQS[i].split("=")[1],
                            filtroForaneo:"",
                            height:"500",
                            width:"80%",
                            originatingObject:"divgrid_" + aplicacion + "_" + grid + "_0",
                            updateControl:"divgrid_" + aplicacion + "_" + grid + "_0",
                          });
                    }
                }                
                
                if (aComandosQS[i].split("=")[0]=="foreignform") {
                    next=aComandosQS[i+1].split("=")[0];
                    grid = aComandosQS[i].split("=")[1];
                    if ($("#" + obj).val()!="") {
                        $.fn.cqs.cleanUp = false;
                        setTimeout("$.fn.cqs.queryStringBoot('" + obj + "','foreignform')",1000);
                        return;
                    } 
                    
                    $("#lnkFormTab_" + aplicacion + "_" + aComandosQS[i].split("=")[1] ).trigger("click");
                }
            }
            
       }
       
       if ($("#_refri_").val()=="") {
            $("#divwait").dialog( "close" );
       } else {
           if ($.fn.cqs.cleanUp) {
                $("#_refri_").val("");
                $("#divwait").dialog( "close" );
           }      
       }
    }

})(jQuery);
