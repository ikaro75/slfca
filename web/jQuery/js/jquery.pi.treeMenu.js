/*
 * Plugin de jQuery para cargar arbol a partir de un webservice
 *
 */

( function($) {
    $.fn.treeMenu = function(opc){

        $.fn.treeMenu.settings = {
            xmlUrl : "control?$cmd=plain",
            app: "",
            entidad:"",
            pk:"",
            datestamp: sDateTime(new Date()),
            error:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.treeMenu.options = $.extend($.fn.treeMenu.settings, opc);
            obj=$(this);
            obj.attr("app", $.fn.treeMenu.options.app);
            obj.attr("entidad", $.fn.treeMenu.options.entidad);
            obj.attr("pk", $.fn.treeMenu.options.pk);
            obj.attr("datestamp", $.fn.treeMenu.options.datestamp);
            $.fn.treeMenu.getTreeDefinition(this);
        });
    }

    $.fn.treeMenu.getTreeDefinition=function(o) {
        var nEntidad=$(o).attr("entidad");
        var nPk=$(o).attr("pk");
        $.ajax(
        {
            url: $.fn.treeMenu.options.xmlUrl + "&$cf=" + nEntidad + "&$pk=" + nPk + "&$ta=children",
            dataType: ($.browser.msie) ? "text" : "xml",
            contentType: "application/x-www-form-urlencoded",
            success:  function(xmlTree){
                if (typeof xmlTree == "string") {
                    xmlGT = new ActiveXObject("Microsoft.XMLDOM");
                    xmlGT.async = false;
                    xmlGT.validateOnParse="true";
                    xmlGT.loadXML(xmlTree);
                    if (xmlGT.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xmlGT.parseError.errorCode +"\nParse reason:" + xmlGT.parseError.reason + "\nLinea:" + xmlGT.parseError.line);
                }
                else
                    xmlGT = xmlTree;

                var error=$(xmlTree).find("error");

                if (error.length>0) {
                    if (error.find("tipo").text()=="SQLServerException" && $("#_cp_").val()=="1") {                
                         $.fn.appgrid.options.error+="Hay un error en la consulta (" + 
                             error.find("general").text() + ". " +  
                             error.find("descripcion").text() + "), haga click <a href='#' id='lnkEditQuery_" + 
                            $.fn.appgrid.options.app +"_" +  $.fn.appgrid.options.entidad +"' class='editLink'>aqui</a> para editarla "
                        obj.html($.fn.appgrid.options.error);
                        return true;    
                        }
              }
              
              var oTypes = {
                    "max_depth" : -2,
                    "max_children" : -2,
                    "types":{}
                };

                //Destruye los nodos existentes para recargarlos
                $("ul",obj).remove();
                oRegistros = $(xmlGT).find("registro")
                
                var sTypes="";
                //Se define la estructura del árbol de acuerdo a la aplicación
                var sXML="";

                //Se reemplaza el parámetro de la aplicación
                
                oRegistros.each( function() {
                   sDateStamp=$(o).attr("datestamp");
                   sTypes+= '"'+$.trim($(this).find('rel').text().replace('\n','')) + '":{"icon":{"image":"' + $.trim($(this).find('icono').text().split('\n')[0]) + '"}},';
                   nClaveNodo= $.trim($(this).find('clave_nodo').text().replace('\n','')) + "-" + sDateStamp;
                   sRel=$.trim($(this).find('rel').text().replace('\n',''));
                   sTextoNodo=$.trim($(this).find('texto_nodo').text().replace('\n','')).replace("&aacute;","á").replace("&eacute;","é").replace("&iacute;","í").replace("&oacute;","ó").replace("&uacute;","ú").replace("&Aacute;","Á").replace("&Eacute;","É").replace("&Iacute;","Í").replace("&Oacute;","Ó").replace("&Uacute;","Ú");;
                   nClaveNodoPadre=$.trim($(this).find('clave_nodo_padre').text().replace('\n',''))+"-"+sDateStamp;
                   sState=$.trim($(this).find('state').text().replace('\n',''));
                   sEvento=$.trim($(this).find('onclick').text().replace('\n',''));
                   nRefrescaArbol=$.trim($(this).find('refresca_arbol').text().replace('\n',''));
                   sXML+="<item id='" + nClaveNodo + "' parent_id='" + nClaveNodoPadre + "' rel='" + sRel +"' state='" + sState + "' evento='"+sEvento +"' refresca_arbol='" +nRefrescaArbol + "'><content><name><![CDATA[" + sTextoNodo + "]]></name></content></item>";
                });

                sXML="<root>" + sXML + "</root>";
                aPlugins="themes,contextmenu,xml_data,types,ui".split(",");
                sTypes="{"+sTypes.substring(0,sTypes.length-1)+"}";
                oTypes.types = $.parseJSON(sTypes);
              
                $(o).jstree({
                    "plugins" : aPlugins,
                    "xml_data" : {
                        "data" : sXML
                    },
                    "themes" : {
                        "theme" : "default",
                        "dots" : true,
                        "icons": true
                    },
                    "types": oTypes
                    }
                );

                //Primero elimina la asociación del evento si ya habia sido declarada anteriormente
                $("#" + o.id+ " a").unbind("click");

                $("#" + o.id+ " a").live("click", function(e) {
                      var oTheNode=this.parentNode;
                      var sNodeId=oTheNode.id;
                      var sTitulo=$.trim(this.text);
                      var sTipoNodo=$(oTheNode).attr("evento");
                      var nApp=sNodeId.split("-")[1];
                      var nForma=sNodeId.split("-")[2];
                      var nRefrescaArbol=$(oTheNode).attr("refresca_arbol");
                      var sW="";

                      for (i=3; i<sNodeId.split("-").length-1;i++) {
                          if (i==3)
                            sW+=sNodeId.split("-")[i];
                          else
                            sW+="&"+sNodeId.split("-")[i];
                      }

                      if (sTipoNodo=='') return false;

                      //Llama grids
                      var oId=o.id;
                       $("#divgrid_"+oId.split("_")[1]+"_"+oId.split("_")[2]+"_"+oId.split("_")[3]).appgrid({
                           app: nApp,
                          entidad: nForma,
                          editingApp: oId.split("_")[3],
                          pk: nPK,
                          wsParameters: sW,
                          titulo:sTitulo,
                          showFilterLink:false,
                          inQueue:false,
                          leyendas:["Nuevo registro", "Edición de registro"],
                          height:"82%",
                          originatingObject:oId,
                          callFormWithRelationships: sTipoNodo=="showGridAndRelationships"?true:false,
                          updateTreeAfterPost:nRefrescaArbol=="1"?true:false
                       });
                       
                       //Se carga el contenido del la bitacora 
                        $("#logPortlet_" + oId.split("_")[1] + "_" + oId.split("_")[2] + '_' + oId.split("_")[3])
                        .portletLog({
                            "app":nApp,
                            "form": nForma
                        });
                
                 });
                
                //Se abrió el dialogo de espera en la función openKardex del grid
                $("#divwait").dialog( "close" );
            },
            error:function(xhr,err){
                alert("Error al recuperar definición de arbol\nreadyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                alert("responseText: "+xhr.responseText);}
            });
 };
})(jQuery);
