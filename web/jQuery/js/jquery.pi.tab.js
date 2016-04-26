/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
( function($) {
    $.fn.apptab = function(opc){

        $.fn.apptab.settings = {
            xmlUrl : "xml_tests/widget.tabs.xml?entidad=",
            app:"",
            entidad:"",
            pk:"",
            tabs:[{id:"",
                  label:"",
                  grids:[]}]
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
            $.fn.apptab.options = $.extend($.fn.apptab.settings, opc);
            obj = $(this);
            obj.html("<div align='center'><br />Cargando informaci&oacute;n... <br /> <br /><img src='img/loading.gif' /></div>")
            $.fn.apptab.getForeignTabs(obj);
        });

    };

     $.fn.apptab.getForeignTabs = function(obj){
         $.ajax(
            {
            url: $.fn.apptab.options.xmlUrl,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xml = new ActiveXObject("Microsoft.XMLDOM");
                 xml.async = false;
                 xml.validateOnParse="true";
                 xml.loadXML(data);
                 if (xml.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);}
                }
                 else {
                    xml = data;}
                obj.html($.fn.apptab.handleTab(xml));
                var suffix = "_" + $.fn.apptab.options.app + "_" + $.fn.apptab.options.entidad + "_" + $.fn.apptab.options.pk;
                var $entityTab = $("#tabEntity" + suffix).tabs({
                        select: function(event, ui) {
                            /*Aqui se debe carga el grid
                             *Primero se verifica si existe el elemento HTML
                             *Si existe quiere decir que ya está cargado el grid
                             *con sus grids foraneos
                             */
                            if (ui.index==0) return true;
                            
                            var nForeignEntity=ui.tab.hash.split("_")[ui.tab.hash.split("_").length-1];
                            if ($("#tree_" + $.fn.apptab.options.pk).length==0) {
                               //$.fn.apptab.getTreeStructure($.fn.apptab.options.app, ui.index);
                               // Construye grids foraneos
                               //$.fn.apptab.getForeignGrids(nForeignEntity,ui.index);
                            }
                        }
                });

                //Inicializa forma de edición en la primera página
                firstTab=$entityTab.find(".ui-tabs-panel:first")[0].id;
                $("#" + firstTab).form({
                        app: $.fn.apptab.options.app,
                        forma:$.fn.apptab.options.entidad,
                        pk:$.fn.apptab.options.pk,
                        modo:"update",
                        titulo: "Datos generales"
                });

            },
            error:function(xhr,err){
                if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1) {
                    alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                    window.location='login.jsp';
                }else{
                    alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                    alert("responseText: "+xhr.responseText);}
            }
            });
    };

    $.fn.apptab.handleTab = function(xml){
        var sUl='<ul>';
        var sDivs='';
        var i=0;
        suffix=$.fn.apptab.options.app + '_' + $.fn.apptab.options.entidad + "_" + $.fn.apptab.options.pk;
        oForaneos=$(xml).find("clave_forma");
        $(xml).find("alias_tab").each(function(){
            //Carga los datos del xml en la variable de configuración
            tabStructure={id:"tabEntity_"  + suffix + "_" + $(oForaneos[i]).text(),
                          label:this.childNodes[0].data,
                          grids:[]};
            $.fn.apptab.options.tabs[i]=tabStructure;
            sUl+='<li><a href="#' + tabStructure.id + '">' + tabStructure.label + '</a></li>';
            sDivs+='<div id="' + tabStructure.id + '"></div>';
            i++;
        });

        //Construye html de acuerdo a configuración recuperada
        var sHtml =
         '<div id="tabEntity_' + suffix + '">' +
         sUl + sDivs + '</div>';

        return sHtml
    }

    $.fn.apptab.getForeignGrids=function (nForm, nTab) {
        $.ajax(
            {
            url: "xml_tests/foreign_grids.xml?entidad=", //$.fn.apptab.options.xmlUrl + "?nForm=" + nForm,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
                 if (typeof data == "string") {
                 xml = new ActiveXObject("Microsoft.XMLDOM");
                 xml.async = false;
                 xml.validateOnParse="true";
                 xml.loadXML(data);
                 if (xml.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);}
                }
                 else {
                    xml = data;}

                var nGrid=0;
                var suffix=$.fn.apptab.options.app + '_' + $.fn.apptab.options.entidad + "_" + $.fn.apptab.options.pk + "_" + nForm;
                $(xml).find("clave_forma").each(function() {
                    sDivGridId=$.fn.apptab.options.app + "_" + $(this).text();
                    grids={id:"#divForeingGrid2_" + sDivGridId,
                           app:$.fn.apptab.options.app,
                           entidad:$(this).text()
                       };

                   //Guarda el ID del grid en la definición del tab correspondiente
                   $.fn.apptab.options.tabs[nTab].grids[nGrid]=grids;

                    if (nGrid==0) {
                        $("#tabEntity_"+suffix).append("<div id='divForeingGrid1_" + sDivGridId + "' name='divForeingGrid1_" + sDivGridId + "' class='gridContainer'>" +
                                                          "<div id='divForeingGrid2_" + sDivGridId + "' name='divForeingGrid2_" + sDivGridId + "' ></div>" +
                                                       "</div>");
                        sElementIdDiv="#divForeingGrid1_" + sDivGridId;
                        sElementIdGrid="#divForeingGrid2_" + sDivGridId}
                    else {
                        //Si es el primero de crea un marco que indique que los otros son registros relacionados
                        if (nGrid==1) sLeyenda='<h4>Registros relacionados</h4>';
                        else sLeyenda='';

                        $(sElementIdDiv).append(sLeyenda + "<div id='divForeingGrid1_" + sDivGridId +"' name='divForeingGrid1_" + sDivGridId +"' class='gridContainer'>" +
                                             "<div id='divForeingGrid2_" + sDivGridId + "' name='divForeingGrid2_" + sDivGridId + "' ></div>" +
                                             "</div>")
                        sElementIdGrid="#divForeingGrid2_" + sDivGridId;
                    }

                    nGrid++;
                    });

                    $.fn.apptab.initForeingGrids(nTab);
                    },
                    error:function(xhr,err){
                        if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1) {
                             alert("Su sesión ha expirado, por seguridad es necesario volverse a registrar");
                            window.location='login.jsp';
                        }else{
                            alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
                            alert("responseText: "+xhr.responseText);}
                        }
                    });
    }
    
    $.fn.apptab.initForeingGrids=function(nTab) {
        oCurrentTab=$.fn.apptab.options.tabs[nTab];
        for (i=0; i<oCurrentTab.grids.length; i++)
            $(oCurrentTab.grids[i].id).appgrid({app:$.fn.apptab.options.app,
                                                entidad: oCurrentTab.grids[i].entidad,
                                                pk:$.fn.apptab.options.pk});
    }

    $.fn.apptab.getTreeStructure=function(nApp, nTab) {
        $("#tv1").jstree({
            "plugins" : [ "themes", "contextmenu", "xml_data","types" ],
            "xml_data" : {
			"ajax" : {
				"url" : "xml_tests/widget.tree.xml?app=   " + nApp
                                },
			"xsl" : "nest"
                        },
            "types" : {
                        "max_depth" : -2,
                        "max_children" : -2,
                        "valid_children" : [ "drive" ],
                        "types" : {
                                   "app" : {
                                              "valid_children" : [ "entidad", "perfil","usuario" ],
                                               "icon" : {
                                                          "image" : "/static/v.1.0rc2/_demo/root.png"
                                                        },
                                            "start_drag" : false,
                                            "move_node" : false,
                                            "delete_node" : false,
                                            "remove" : false
                                    },                            
                                   "entidad" : {
                                                "valid_children" : ["consulta", "entidad"],
                                                "icon" : {
                                                           "image" : "/static/v.1.0rc2/_demo/folder.png"
                                                         }
                                   },
                                   "consulta" : {
                                                "valid_children" : "campo",
                                                "icon" : {
                                                            "image" : "/static/v.1.0rc2/_demo/file.png"
                                                }
                                    },
                                   "campo" : {
                                                "valid_children" : "none",
                                                "icon" : {
                                                            "image" : "/static/v.1.0rc2/_demo/file.png"
                                                }
                                    },
                                    "perfil" : {
                                                "valid_children" : [ "campo", "folder" ],
                                                "icon" : {
                                                           "image" : "/static/v.1.0rc2/_demo/folder.png"
                                                         }
                                    },
                                    "usuario" : {
                                                "valid_children" : [ "campo", "folder" ],
                                                "icon" : {
                                                           "image" : "/static/v.1.0rc2/_demo/folder.png"
                                                         }
                                    }
                        }
                    }
        });
    }

})(jQuery);
