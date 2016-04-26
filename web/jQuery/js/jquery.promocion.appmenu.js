/* 
 * Plugin de jQuery para cargar accordeón a través de un plugin
 * 
 */
( function($) {
    $.fn.appmenu = function(opc){

        $.fn.appmenu.settings = {
            xmlUrl : "control?$cmd=app", //"/ProyILCE/srvControl?$cmd=appmenu" "/ProyILCE/resource/jsp/xmlMenu.jsp"  //"$cf=1&$ta=XML acordeon", //"/ProyILCE/xml_tests/widget.accordion.xml",
            aplicacion_padre:"",
        };
        
        if ($("#_refri_").val()=="1") return;
        
        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.appmenu.options = $.extend($.fn.appmenu.settings, opc);
        // 1. Se hace una sola llamada al servicio
        
        s=$.fn.appmenu.options.xmlUrl //+"&$c="+obj[0].id.split("_")[1];
        
        if ($.fn.appmenu.options.aplicacion_padre!="")
            s=s+"&$ap="+$.fn.appmenu.options.aplicacion_padre;
        
        hTags = this;
        
        $("#_status_").val("Cargando información de menús"); 
        
        $.ajax(
        {
            url: s,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                if (typeof data == "string") {
                    xmlApps = new ActiveXObject("Microsoft.XMLDOM");
                    xmlApps.async = false;
                    xmlApps.validateOnParse="true";
                    xmlApps.loadXML(data);
                    if (xmlApps.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xmlApps.parseError.errorCode +"\nParse reason:" + xmlApps.parseError.reason + "\nLinea:" + xmlApps.parseError.line);
                    }
                }
                else {
                    xmlApps = data;
                }
                
                $("#_status_").val("Contruyendo menús");
                // Devuelvo la lista de objetos jQuery
                return hTags.each( function(){
                    obj=$(this);
                    if ($.fn.appmenu.options.xmlUrl!="") {
                         obj.html( $.fn.appmenu.handleMenu(xmlApps, obj[0].id.split("_")[1]));
                    }
                    
                    //Carga las aplicaciones en el mapa del sitio
                    if ($.fn.appmenu.options.aplicacion_padre=="")
                        sTab="tab_" + obj[0].id.split("_")[1] + "_0";
                    else
                        sTab="tab_" + obj[0].id.split("_")[1] +  "_" + obj.attr("aplicacion");

                    $("#"+sTab).tabs();
                    //Crea el control tab aqui, puesto que desde este control se va a manipular
                    $("#"+ sTab).tabs({
                        tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
                    });

                    $( "#"+sTab+" span.ui-icon-close" ).live( "click", function() {
                        if ($( this ).parent().parent().parent()[0]==undefined) return;
                        var TabId=$( this ).parent().parent().parent()[0].id
                        var index =  $("#"+TabId).find("li").index( $( this ).parent() );
                        if (index < 0 ) return;
                       $("#"+TabId) .tabs( "remove", index );
                    });

                     /*Captura el evento clic para los links*/
                    $(this).find(".menu")
                        .button()
                        .click(function(e, data) {
                            $("#_status_").val("Construyendo interfaz de la aplicación");
                            link_id=this.name;

                            //Verifica si existe
                            var nAplicacionPadre = $(this).attr("aplicacion_padre")
                            var nCategoria = link_id.split("_")[1];
                            var nAplicacion=link_id.split("_")[2];
                            var nEntidad=link_id.split("_")[3];
                            var nPK=0;
                            var sTitulo=$(this).text();
                            var sForma = $(this).attr("forma");

                            if (nAplicacionPadre=="0")
                                sTab="tab_" + nCategoria + "_0";
                            else
                                sTab="tab_" + nCategoria +  "_" + nAplicacionPadre;

                            if ($("#tab_"+nCategoria+"_"+nAplicacion+"_"+nEntidad+"_"+nPK).length) {
                                //Selecciona el tab correspondiente
                                $("#"+ sTab).tabs("select", "#tab_"+nCategoria+"_"+nAplicacion+"_"+nEntidad+"_"+nPK);
                            }
                            else {
                                    $("#"+ sTab).tabs( "add", "#tab_"+nCategoria+"_"+nAplicacion+"_"+nEntidad+"_"+nPK, sTitulo);
                                    $("#"+ sTab).tabs( "select", "#tab_"+nCategoria+"_"+nAplicacion+"_"+nEntidad+"_"+nPK);
                                    oTabPanel=$("#tab_"+nCategoria+"_"+nAplicacion+"_"+nEntidad+"_"+nPK);
                                    // Aqui va a ir la barra de avisos 
                                    oTabPanel.addClass("appTab");
                                    oTabPanel.html(""); 
                                    //Se inserta el div para el grid
                                    $(oTabPanel).appportlet({
                                            category: nCategoria,
                                            app: nAplicacion,
                                            form: nEntidad,
                                            register: "0",
                                            gridData: "",
                                            logPortlet: 1, //portlet de actividad reciente
                                            filterPortlet: 1, //portlets de filtros
                                            kardexPortlet: 1, //portlet de kardex
                                            templatePortlet: 1, //portlet de plantillas 
                                            tabTitle: sForma
                                    });                                    

                            }    
                             $("#_status_").val(""); 
                        })
                        .parent()
                        .buttonset();                     
                    });                   

            },
            error:function(xhr,err){
                $("#_status_").val("");
                if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1 || err=="error") {
                    $("#_refri_").val("1");
                    alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                    window.location='login.jsp';
                    xhr.abort();
                }else{
                    alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                    alert("responseText: "+xhr.responseText);
                }
                $("#_status_").val("");
            }
        });

    };
    
   
    /*Crea el HTML que conforma los botones */
    $.fn.appmenu.handleMenu = function(xml, categoria){   
        sHtml="";

        $(xml).find("registro").each(function(){
            nCategoria =$(this).find("clave_categoria").text();
            
            if (nCategoria!=categoria) return true;
            
            nAplicacion=$(this).find("clave_aplicacion").text();
            
            if ($("#_cp_").val()!=1 && (nAplicacion==1 || nAplicacion==2))
                return true;
                    
            nAplicacionPadre=$(this).find("clave_aplicacion_padre").text();
            sTituloAplicacion=$(this).find("aplicacion").text()
            nEntidad=$(this).find("clave_forma").text();
            sForma =  $(this).find("forma").text();
            sHtml+="<a href='#' class='menu' id='showEntity_" + nCategoria + "_" + nAplicacion + "_" + nEntidad +"' name='showEntity_" + nCategoria + "_" + nAplicacion + "_" + nEntidad +"' aplicacion_padre='" + nAplicacionPadre + "' aplicacion='"+nAplicacion+ "' forma='" + sForma +"' >" + sTituloAplicacion + "</a>";
            
            //Agrega los menús al mapa del sitio
            $("#app_menu_in_map").append("<dt><a id='mapLink-tabAplicaciones-tabshowEntity" +nAplicacion + "_" + "_" + nEntidad + "' class='maplink' href='#'>"+sTituloAplicacion+"</dt>");
        })

        return sHtml;
    }
    
})(jQuery);