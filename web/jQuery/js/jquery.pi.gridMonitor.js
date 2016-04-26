/*
 * Plugin de jQuery para cargar grid a partir de una paginota
 *
 */
( function($) {
    $.fn.monitorgrid = function(opc){
        $.fn.monitorgrid.settings = {
            xmlUrl : "control" , // "control", "control?$cmd=grid" "xml_tests/widget.grid.xml"
            wsParameters:"",
            titulo:"",
            leyendas:[],
            app:"",
            entidad:"",
            pk:"",
            suffix:"",
            colNames: [],
            colModel: [{}],
            groupFields:[],
            sortname:"",
            tab:"",
            insertInDesktopEnabled:"1",
            width:"",
            height:"",
            openKardex:false,
            loadMode:"",
            removeGridTitle:false,
            showFilterLink:true,
            inQueue:false,
            inDesktop:false,
            editingApp:"",
            datestamp: sDateTime(new Date()),
            originatingObject:"",
            callFormWithRelationships:false,
            updateTreeAfterPost:false,
            logPhrase:"",
            requeriesFilter:0,
            timerId:0,
            error:""
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.monitorgrid.options = $.extend($.fn.monitorgrid.settings, opc);
            var nApp=$.fn.monitorgrid.options.app;
            var nEntidad=$.fn.monitorgrid.options.entidad;
            var nPK=$.fn.monitorgrid.options.pk;
            var suffix =  "_" + nApp + "_" + nEntidad + "_" + $.fn.monitorgrid.options.datestamp;
            
            var t; 
            obj = $(this);
            //Verifica si el objeto padre es un tabEntity
            //Si así es toma de su id el sufijo app + entidad principal + entidad foranea            
            $(this).html("<table width='100%' id='grid"+ suffix +
                "' titulo='" + $.fn.monitorgrid.options.titulo +
                "' wsParameters='"+ $.fn.monitorgrid.options.wsParameters +
                "' openkardex='" + $.fn.monitorgrid.options.openKardex +
                "' editingApp='" + $.fn.monitorgrid.options.editingApp +
                "' leyendas='" + $.fn.monitorgrid.options.leyendas[0] + "," + $.fn.monitorgrid.options.leyendas[1]+
                "' datestamp='" + $.fn.monitorgrid.options.datestamp +
                "' originatingObject='"+ $.fn.monitorgrid.options.originatingObject +
                "' callFormWithRelationships='"+$.fn.monitorgrid.options.callFormWithRelationships+
                "' updateTreeAfterPost='" + $.fn.monitorgrid.options.updateTreeAfterPost +
                "' requeriesFilter='" + $.fn.monitorgrid.options.requeriesFilter  +
                "'>" +
                "</table><div id='pager" + suffix +"' security=''><div align='center' id='loader" + suffix +"'><br/><br/><br/><br/><br/><br/><br /><br/><br/><br/><br/><br/><br/><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br /></div></div>");

            $.fn.monitorgrid.getGridDefinition();
        });

    };

    $.fn.monitorgrid.getGridDefinition = function(){
        $.ajax(
        {
            url: $.fn.monitorgrid.options.xmlUrl + "?$cmd=grid&$cf=" + $.fn.monitorgrid.options.entidad.split('-')[0] + "&$ta=select&$dp=header&$w=" + $.fn.monitorgrid.options.wsParameters,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  processGridDefinition, 
            error:function(xhr,err){
                sTipoError='Problemas al recuperar definición de grid.\n';
                if (xhr.responseText.indexOf('NullPointerException')>-1)
                    sTipoError+='Problemas de conexión a la base de datos, verifique la conexión a la red.'
                else
                    sTipoError+=xhr.responseText;

                suffix=obj.children()[1].id.replace("pager","");
                $("#loader"+suffix).html("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>"+
                    "<div class='ui-widget'>"+
                    "<div style='padding: 0 .7em; width: 80%' class='ui-state-error ui-corner-all'>"+
                    "<p class='app_error'><span style='float: left; margin-right: .3em;' class='ui-icon ui-icon-alert'></span>"+
                    sTipoError+"</p>"+
                    "</div></div>");
            }
        });
    };

    function processGridDefinition (data){
                if (typeof data == "string") {
                    xml = new ActiveXObject("Microsoft.XMLDOM");
                    xml.async = false;
                    xml.validateOnParse="true";
                    xml.loadXML(data);
                    if (xml.parseError.errorCode>0) {
                        alert("Error de compilación xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                    }
                }
                else {
                    xml = data;
                }
                
                $.fn.monitorgrid.handleGridDefinition(xml);

                if ($.fn.monitorgrid.options.error!="") {       
                    obj.html("<div class='ui-widget'>"+
                    "<br><br><br><br><br><br><br><br><br><br><br><div style='padding: 0 .7em; width: 80%; margin-left: auto; margin-right: auto;text-align: center;' class='ui-state-error ui-corner-all'>"+
                    "<p class='app_error'><span style='float: left; margin-right: .3em;' class='ui-icon ui-icon-alert'></span>"+
                    $.fn.monitorgrid.options.error+"</p>"+
                    "</div></div>"+obj.html());
                   
                   //Remueve del dom el mensaje de espera
                   $("#loader_"+ $.fn.monitorgrid.options.app + "_" + $.fn.monitorgrid.options.entidad + "_" + $.fn.monitorgrid.options.datestamp).html("");
                    
                    /* Captura el click del link */
                    $(".editLink").click(function(){
                        sCmd=this.id.split("-")[0];
                        nQuery=this.id.split("_")[0].split("-")[1];
                        nApp=this.id.split("_")[1];
                        nForma=this.id.split("_")[2];
                        
                        if (sCmd=="lnkEditQuery")  {                           
                            $("body").form({
                                app: nApp,
                                forma:8,
                                datestamp:obj.attr("datestamp"),
                                modo:"update",
                                titulo: "Consulta ",
                                columnas:1,
                                pk:nQuery,
                                filtroForaneo:"2=clave_aplicacion=1&3="+obj.attr("wsParameters"),
                                height:"500",
                                width:"80%",
                                originatingObject: obj.id,
                                updateControl:obj.id
                            });
                        }
                        
                        if (sCmd=="lnkReloadGrid") {
                           //Borra el error y coloca el mensaje de espera
                           obj.find(".ui-widget").remove();
                           $("#loader_"+ $.fn.monitorgrid.options.app + "_" + $.fn.monitorgrid.options.entidad + "_" + $.fn.monitorgrid.options.datestamp).html("<br/><br/><br/><br/><br/><br/><br /><br/><br/><br/><br/><br/><br/><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br />");
                           $.fn.monitorgrid.options.error="";
                           $.fn.monitorgrid.getGridDefinition();
                           /*setTimeout("$('.queued_grids:first').gridqueue()",500);*/
                        }
                        
                    });

                    return true;
                }

                var suffix =  "_" + $.fn.monitorgrid.options.app + "_" + $.fn.monitorgrid.options.entidad + "_" + $.fn.monitorgrid.options.datestamp;

                /* Inicia implementación del grid */
                var nApp=$.fn.monitorgrid.options.app;
                var nEntidad=$.fn.monitorgrid.options.entidad.split('-')[0];

                /* Agrega la liga para quitar filtro desde el contructor    */
                if ($.fn.monitorgrid.options.wsParameters!="" && $.fn.monitorgrid.options.showFilterLink)
                    $.fn.monitorgrid.options.titulo+="&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid" + suffix+"'>(Quitar filtro)</a>";

                /*Crea cadena a partir de objeto xml*/
                /*var sXML="";
                if (window.ActiveXObject)
                    sXML = xml;
                // code for Mozilla, Firefox, Opera, etc.
                else
                   sXML = (new XMLSerializer()).serializeToString(xml);*/
                if ($.fn.monitorgrid.options.loadMode=='delayed')
                    sDataType="local";
                else
                    sDataType="xml";

                if ($("#grid"+suffix).attr("requeriesFilter")==0 ) //Si no está configurado para filtrar registros, trae el cuerpo
                        xmlURL=$.fn.monitorgrid.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$removeTags=true&$w=" + $.fn.monitorgrid.options.wsParameters;
                    else if ($("#lnkRemoveFilter_grid"+suffix).length==0)  //Si está configurado y no tiene un filtro solo trae la cabecera
                        xmlURL=$.fn.monitorgrid.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$removeTags=true&$w=" + $.fn.monitorgrid.options.wsParameters;
                        else
                            xmlURL=$.fn.monitorgrid.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$removeTags=true&$w=" + $.fn.monitorgrid.options.wsParameters;
               
                var oGrid=$("#grid" + suffix).jqGrid(
                {//datatype: "xmlstring",
                    //datastr: sXML,
                    url:xmlURL,
                    datatype: sDataType,
                    colNames:$.fn.monitorgrid.options.colNames,
                    colModel:$.fn.monitorgrid.options.colModel,
                    rowNum:50,
                    /*autowidth: true,*/
                    width:$.fn.monitorgrid.options.width,
                    shrinkToFit: false,
                    height:$.fn.monitorgrid.options.height,
                    rowList:[50,100,200],
                    pager: jQuery('#pager' + suffix),
                    toppager:true,
                    sortname: $.fn.monitorgrid.options.colModel[0],//$.fn.monitorgrid.options.sortname,//+suffix,
                    viewrecords: true,
                    sortorder: "desc",
                    //loadonce: true,
                    caption:$.fn.monitorgrid.options.titulo
                /*grouping: true,
                    groupingView : {
                            groupField : $.fn.monitorgrid.options.groupFields[0],
                            groupColumnShow : [true],
                            groupText : ['<b>{0}</b>'],
                            groupCollapse : false,
                            groupOrder: ['asc'],
                            groupSummary : [false],
                            groupDataSorted : true
                    },
                    footerrow: true,
                    userDataOnFooter: true */,
                    gridComplete:function(){    
                            //Se debe buscar en el grid cuales son los que están en rojo
                            //si están en rojo se debe enviar el correo y se debe marcar el registro para evitar que se
                            //vuelva a enviar la notificación
                            nRows=oGrid.jqGrid('getGridParam','reccount');
                            for (a=1; a<= nRows; a++) {
                                /*dHoy=new Date();
                                dFechaVigencia= new Date(oGrid.getCell(a,6)); //Fecha de la vigencia*/
                                nEstatusNotificacion= oGrid.getCell(a,6) //Estatus de notificación
                                semaforoAmarillo=oGrid.getCell(a,1).indexOf("amarillo.png");
                                semaforoRojo=oGrid.getCell(a,1).indexOf("rojo.png");
                                
                                nActividad=oGrid.getCell(a,0);
                                //Cuando es amarillo se envía la notificación de que solo quedan poco dias
                                if (semaforoAmarillo>=0 && nEstatusNotificacion!="1") {
                                    //Hace el envío 
                                    to=oGrid.getCell(a,3).split("|")[1];
                                    asignado=oGrid.getCell(a,3).split("|")[0];
                                    notificacion=oGrid.getCell(a,9);
                                    enviarNotificacion=notificacion.split("|")[0];
                                    asunto=notificacion.split("|")[1];
                                    notificacion=notificacion.split("|")[2];
                                    dias = oGrid.getCell(a,1).split("-")[1].toLowerCase();
                                    
                                    if (enviarNotificacion="1") {
                                        postConfig="from=siap@ilce.edu.mx&to=" + to + "&subject=" + asunto +
                                        "&message=Estimad@ " + asignado + '\n\n'+ notificacion+'\n\nSólo '+dias;
                                        $.post("control?$cmd=mail",postConfig);
                                        // la clave_estatus_notificacion es 1 con el semáforo amarillo
                                        $.post("control?$cmd=register&$ta=update&$cf=256&$pk="+nActividad+"&clave_estatus_notificacion=1");
                                        //Hace la actualización de la fecha de envío
                                        //alert("Este está por atrasarse: " + oGrid.getCell(a,2).split("-")[0] + "\npostConfig="+postConfig);

                                    }
                                }
                                
                                //Cuando es rojo se envía la notificación de que ya caducó la vigencia de la actividad
                                if (semaforoRojo>=0 && nEstatusNotificacion!="2") {
                                    //Hace el envío 
                                    to=oGrid.getCell(a,3).split("|")[1];
                                    asignado=oGrid.getCell(a,3).split("|")[0];
                                    notificacion=oGrid.getCell(a,9);
                                    enviarNotificacion=notificacion.split("|")[0];
                                    asunto=notificacion.split("|")[1];
                                    notificacion=notificacion.split("|")[2];
                                    dias = oGrid.getCell(a,1).split("-")[1].toLowerCase();
                                    
                                    if (enviarNotificacion="1") {
                                        postConfig="from=siap@ilce.edu.mx&to=" + to + "&subject=" + asunto +
                                        "&message=Estimad@ " + asignado + '\n\n'+ notificacion+'\n\nLa solicitud '+dias;
                                        // el clave_estatus_notificacion es 2 con el semáforo rojo
                                        $.post("control?$cmd=register&$ta=update&$cf=256&$pk="+nActividad+"&clave_estatus_notificacion=2");
                                    
                                        $.post("control?$cmd=mail",postConfig);
                                        //Hace la actualización del estatus de envío
                                        //alert("Ya se atrasó : " + oGrid.getCell(a,2).split("-")[0] + "\npostConfig="+postConfig);

                                    }
                                }
                                
                                t=setTimeout("$('#grid"+suffix+"').jqGrid().trigger('reloadGrid')",2000);
                                alert(t)
                                        
                            }

                    }
                });


                //Quita agrupamiento
                //oGrid.jqGrid('groupingRemove',true);

                //Verifica si ya existen los botones para
                //evitar duplicarlos
                if ($('#grid'+ suffix+'_toppager_left').html()!="")
                    return true;

                //Va estableciendo botones de acuerdo a permisos
                sP=$("#pager"+suffix).attr("security");

                if (sP.indexOf("2")>-1) {
                    oGrid.navGrid('#grid'+ suffix+'_toppager',{
                        edit:false,
                        add:false,
                        del:false,
                        search:false
                    })
                    .navButtonAdd('#grid'+ suffix+'_toppager',{
                        caption:"",
                        buttonicon:"ui-icon-plus",
                        onClickButton:function() {
                            $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Generando forma...");
                            nEditingApp=$(this).attr("editingApp");
                            
                            //Verifica si se actualiza árbol
                            sUpdateControl="";
                            if ($(this).attr("updateTreeAfterPost")=="true") 
                                    sUpdateControl=oGrid.attr("originatingObject");

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
                                app: nApp,
                                forma:nEntidad,
                                datestamp:$(this).attr("datestamp"),
                                modo:"insert",
                                titulo: $.fn.monitorgrid.options.leyendas[0],
                                columnas:1,
                                pk:0,
                                filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3="+$(this).attr("wsParameters"),
                                height:"500",
                                width:"80%",
                                originatingObject:oGrid.id,
                                showRelationships:$(this).attr("callFormWithRelationships"),
                                updateControl:sUpdateControl
                            });
                        },
                        position: "last",
                        title:"Nuevo registro",
                        cursor: "pointer"
                    });
                }

                if (sP.indexOf("3")>-1) {
                    oGrid.navGrid('#grid'+ suffix+'_toppager',{
                        edit:false,
                        add:false,
                        del:false,
                        search:false
                    })
                    .navButtonAdd('#grid'+ suffix+'_toppager',{
                        caption:"",
                        buttonicon:"ui-icon-pencil",
                        onClickButton:function() {
                            nRow=$(this).getGridParam('selrow');
                            if (nRow) {
                                $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Generando forma...");
                                nPK= $(this).getCell(nRow,0);
                                nEditingApp=$(this).attr("editingApp");
                                
                                //Verifica si se actualiza árbol
                                sUpdateControl="";
                                if ($(this).attr("updateTreeAfterPost")=="true")
                                    sUpdateControl=oGrid.attr("originatingObject");

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
                                    app: nApp,
                                    forma:nEntidad,
                                    datestamp:$(this).attr("datestamp"),
                                    modo:"update",
                                    titulo: $.fn.monitorgrid.options.leyendas[1],
                                    columnas:1,
                                    pk:nPK,
                                    filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3="+$(this).attr("wsParameters"),
                                    height:"500",
                                    width:"80%",
                                    originatingObject: $(this).id,
                                    showRelationships:$(this).attr("callFormWithRelationships"),
                                    updateControl:sUpdateControl
                                });
                            }
                            else {
                                alert('Seleccione el registro a editar');
                            }
                        },
                        position: "last",
                        title:"Editar registro",
                        cursor: "pointer"
                    });
                }

                if (sP.indexOf("4")>-1) {
                    oGrid.navGrid('#grid'+ suffix+'_toppager',{
                        edit:false,
                        add:false,
                        del:false,
                        search:false
                    })
                    .navButtonAdd('#grid'+ suffix+'_toppager',{
                        caption:"",
                        buttonicon:"ui-icon-trash",
                        onClickButton:function() {
                            nRow=$(this).getGridParam('selrow');
                            if (nRow) {
                                nPK= $(this).getCell(nRow,0);
                                if (confirm("¿Está seguro que desea eliminar el registro? No es posible deshacer esta acción.")){
                                    $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Eliminando registro...");
                                    $.ajax(
                                    {
                                        url: "control?$cmd=register&$ta=delete&$cf="+ nEntidad + "&$pk="+ nPK,
                                        dataType: "text",
                                        success:  function(data){
                                            oGrid.jqGrid('delRowData',nRow);
                                            //Actualiza árbol
                                            if ($.fn.monitorgrid.options.updateTreeAfterPost) {
                                                sTvId=oGrid.attr("originatingObject");
                                                $("#"+sTvId).treeMenu.getTreeDefinition($("#"+sTvId));
                                            }
                                        },
                                        error:function(xhr,err){
                                            alert("Error al eliminar registro");
                                        }
                                    });
                                    $("#grid"+suffix+"_toppager_right").children(0).html("");
                                }
                            }
                            else {
                                alert('Seleccione el registro a eliminar');
                            }
                        },
                        position: "last",
                        title:"Eliminar registro",
                        cursor: "pointer"
                    });
                }

                oGrid.navGrid('#grid'+ suffix+'_toppager',{
                    edit:false,
                    add:false,
                    del:false,
                    search:false
                })
                .navButtonAdd('#grid'+ suffix+'_toppager',{
                    caption:"",
                    buttonicon:"ui-icon-search",
                    onClickButton:  function() {
                        $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Generando forma...");
                        $("body").form({
                            app: nApp,
                            forma:nEntidad,
                            datestamp:$(this).attr("datestamp"),
                            modo:"lookup",
                            titulo: "Filtrado de registros",
                            columnas:1,
                            height:"500",
                            width:"80%",
                            pk:0,
                            originatingObject: oGrid.id
                        });
                        
                    },
                    position: "last",
                    title:"Filtrar",
                    cursor: "pointer"
                });
                
                oGrid.navGrid('#grid'+ suffix+'_toppager',{
                    edit:false,
                    add:false,
                    del:false,
                    search:false
                })
                .navButtonAdd('#grid'+ suffix+'_toppager',{
                    caption:"",
                    buttonicon:"ui-icon-newwin",
                    onClickButton:  function() {
                        var nApp=this.id.split("_")[1];
                        var nForm=this.id.split("_")[2];
                        var sDateStamp=this.id.split("_")[3];
                        nRow=$(this).getGridParam('selrow');
                        if (nRow) {
                            $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Abriendo kardex...");
                            nPK= $(this).getCell(nRow,0);
                            $.fn.monitorgrid.openKardex(nApp,nForm,sDateStamp,nPK);
                        }
                        else
                            alert('Seleccione un registro');
                    },
                    position: "last",
                    title:"Abrir kardex",
                    cursor: "pointer"
                });
                
                //Crea el botón para detener y volver a iniciar el timeout
                oGrid.navGrid('#grid'+ suffix+'_toppager',{
                    edit:false,
                    add:false,
                    del:false,
                    search:false
                })
                .navButtonAdd('#grid'+ suffix+'_toppager',{
                    caption:"",
                    buttonicon:"ui-icon-pause",
                    onClickButton:  function() {
                        oBoton=$('#grid'+ suffix+'_toppager_left').find(".ui-icon-pause");
                        //Verifica qué icono se tiene
                        if (oBoton.length>0 ) {
                            oBoton.removeClass('ui-icon-pause');
                            oBoton.addClass('ui-icon-play');
                            clearTimeout(t);
                            alert('Monitoreo detenido');
                        } else {
                            oBoton=$('#grid'+ suffix+'_toppager_left').find(".ui-icon-play");
                            alert('Monitoreo reanudado');
                            oBoton.removeClass('ui-icon-play');
                            oBoton.addClass('ui-icon-pause'); 
                            t=setTimeout("$('#grid"+suffix+"').jqGrid().trigger('reloadGrid')",2000);
                        }    
                           
                           
                    },
                    position: "last",
                    title:"Detener monitoreo",
                    cursor: "pointer"
                });                
                
                //Remueve del dom el loader
                $("#loader"+ suffix).remove();
                if ($.fn.monitorgrid.options.insertInDesktopEnabled=="1")

                    oGrid.navGrid('#grid'+ suffix+'_toppager',{
                        edit:false,
                        add:false,
                        del:false,
                        search:false,
                        view:false
                    })
                    .navButtonAdd('#grid'+ suffix+'_toppager',{
                        caption:"",
                        buttonicon:"ui-icon-star",
                        onClickButton:  function() {
                            nApp=this.id.split("_")[1];
                            nForma=this.id.split("_")[2];
                            postConfig = "$cmd=register&$cf=1&$ta=insert&$pk=0"+
                            "&clave_aplicacion=" + nApp +
                            "&clave_empleado="+ $("#_ce_").val() +
                            "&parametro=escritorio.grid"+
                            "&valor=" +escape("app:"+nApp+
                                ",entidad:" + nForma +
                                ",wsParameters:" + oGrid.attr("wsParameters") +
                                ",titulo:" + oGrid.attr("titulo") +
                                ",leyendas:" + oGrid.attr("leyendas").replace(",", "/") +
                                ",openKardex:" + oGrid.attr("openKardex") +
                                ",editingApp:" + oGrid.attr("editingApp") +
                                ",inDesktop:true"
                                );
                            
                            $.ajax(
                                    {
                                        url: "control?$cmd=register&$ta=insert?"+postConfig,
                                        dataType: ($.browser.msie) ? "text" : "xml",
                                        success:  function(favorito){
                                             if (typeof favorito == "string") {
                                             xmlFav = new ActiveXObject("Microsoft.XMLDOM");
                                             xmlFav.async = false;
                                             xmlFav.validateOnParse="true";
                                             xmlFav.loadXML(favorito);
                                             if (xmlFav.parseError.errorCode>0) {
                                                    alert("Error de compilación xml:" + xmlFav.parseError.errorCode +"\nParse reason:" + xmlFav.parseError.reason + "\nLinea:" + xmlFav.parseError.line);}
                                            }
                                             else {
                                                xmlFav= favorito;}
                                            
                                            if ($(xmlFav).find("error").length>0) {
                                                alert("Error al insertar grid en favoritos");
                                                return;
                                            }
                                                
                                            nClave=$(xmlFav).find("pk").text();
                                            //Inserta el html para agregar el grid en el escritorio
                                            $('#tabMisFavoritos').tabs( "add", "#tabMisFavoritos_"+nClave, oGrid.attr("titulo"));

                                            $("#tabMisFavoritos_"+nClave).append("<div class='queued_grids'" +
                                                " id='divDesktopGrid_" + nApp + "_" + nForma + "' " +
                                                " app='" + nApp + "' " +
                                                " form='" + nForma + "' " +
                                                " wsParameters='" + oGrid.attr("wsParameters") + "' " +
                                                " titulo='" + oGrid.attr("titulo") + "' " +
                                                " leyendas='" +oGrid.attr("leyendas")+ "' "  +
                                                " openKardex='" + oGrid.attr("openKardex") + "' " +
                                                " inDesktop='true'" +
                                                " class='queued_grids'," +
                                                " insertInDesktopEnabled='0'></div>"+
                                                "<div class='desktopGridContainer' ><br>&nbsp;&nbsp;&nbsp;&nbsp;<br><br></div><br>"
                                            );

                                            setTimeout("$('.queued_grids:first').gridqueueMonitor()",2000);
                                            alert("Se agregó el grid a tus favoritos");  
                                        },
                                        error:function(xhr,err){
                                            alert("Error al eliminar registro");
                                        }
                            }); 
                            
                        },
                        position: "last",
                        title:"Insertar en favoritos",
                        cursor: "pointer"
                    });

                //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
                if ($.fn.monitorgrid.options.wsParameters!="" && $.fn.monitorgrid.options.showFilterLink) {
                    $("#lnkRemoveFilter_grid" + suffix).click(function() {
                        nApp=this.id.split("_")[2];
                        nForma=this.id.split("_")[3];
                        sDS=this.id.split("_")[4];
                        var sGridId="#grid_" + nApp + "_" + nForma+ "_"+ sDS;
                        $(this).remove();
                        
                        if ($("#grid"+suffix).attr("requeriesFilter")=="1")
                            $("body").form({
                                app: nApp,
                                forma:nEntidad,
                                datestamp:$(this).attr("datestamp"),
                                modo:"lookup",
                                titulo: "Filtrado de registros",
                                columnas:1,
                                height:"500",
                                width:"80%",
                                pk:0,
                                originatingObject: "#grid"+suffix
                            });                            
                        else    
                            $(sGridId).jqGrid('setGridParam',{
                                url:"control?$cmd=grid&$cf=" + nForma + "&ta=select&$dp=body"
                            }).trigger("reloadGrid")
                    });
                }


                //remueve los botones refresh agregados por default
                $("table","#grid"+ suffix+"_toppager_left").each( function(){
                    if($(this).index()>0)
                        $(this).remove();
                });
                
                $("#grid"+ suffix+"_toppager_left")[0].style.width="";
                //Quita el paginador del la barra de herramientas superior
                $("#grid"+ suffix+"_toppager_center").remove();

                //Se reemplaza el contenido del toppager_right con
                // el combo para agrupar
                /*var sOptions="<option value='clear'>Quitar agrupamiento</option>";
                        for (i = 0; i < $.fn.monitorgrid.options.groupFields.length; i++) {
                            sOptions+="<option value='" + $.fn.monitorgrid.options.groupFields[i]+ "'>" +
                                $.fn.monitorgrid.options.groupFields[i] +
                                "</option>"
                        }

                        $("#grid"+ suffix+"_toppager_right").html("<select id='cbGroups"+suffix+"'>"+sOptions+"</select>"); */

                //Establece evento para select
                /*$("#cbGroups"+suffix).change(function() {
                    var suffix=this.id.split("_")[1] + "_" +
                    this.id.split("_")[2] + "_" +
                    this.id.split("_")[3];
                    var sVal=$(this).val();
                    if(sVal=='clear')
                        oGrid.jqGrid('groupingRemove',true);
                    else
                        oGrid.jqGrid('groupingGroupBy',sVal);
                });*/

                //Remueve el botón de kardex si no está especificado en el constructor
                if (oGrid.attr("openKardex")!="true")
                    $(".ui-icon-newwin", $("#grid"+ suffix+"_toppager_left")).remove()

                //Verifica si el grid está en una cola
                if ($.fn.monitorgrid.options.inQueue)
                    setTimeout("$('.queued_grids:first').gridqueueMonitor({height: $('#_gq_').val()+'%'})",3000);

                if ($.fn.monitorgrid.options.removeGridTitle)
                    $('.ui-jqgrid-titlebar',oGrid).remove();

            /* Finaliza implementación de grid */

    }
            
    $.fn.monitorgrid.handleGridDefinition = function(xml){
        var iCol=0;
        
        //Manejo de errores
        var error=$(xml).find("error");
        
        if (error.length>0) {
            if (error.find("tipo").text()=="SQLServerException"  ) {
                 nConsulta= error.find("clave_consulta").text();
                 $.fn.monitorgrid.options.error+="Hay un error en la consulta (" + 
                     error.find("general").text() + ". " +  
                     error.find("descripcion").text() +")";
                    if ($("#_cp_").val()=="1") 
                        $.fn.monitorgrid.options.error+=", haga click <a href='#' id='lnkEditQuery-" + nConsulta  + "_" + 
                        $.fn.monitorgrid.options.app +"_" +  $.fn.monitorgrid.options.entidad +"' class='editLink'>aqui</a> para editarla";
                return true;    
            }  
            
            if (error.find("tipo").text()=="NullPointerException") {
               $.fn.monitorgrid.options.error="Sin conexión. Haga clic <a href='#' class='editLink' id='lnkReloadGrid-0_" + $.fn.monitorgrid.options.app +"_" +  $.fn.monitorgrid.options.entidad +"'>aqui</a> para volver a intentarlo";
               return true; 
            }
            
            if (error.find("tipo").text()=="Exception" && $("#_cp_").val()=="1") {
             $.fn.monitorgrid.options.error+="No hay una consulta asociada a esta acción" + 
                 error.find("general").text() + ". " +  
                 error.find("descripcion").text() + "), haga click <a href='#' id='lnkEditQuery-0" +   + "_" + 
                $.fn.monitorgrid.options.app +"_" +  $.fn.monitorgrid.options.entidad +"' class='editLink'>aqui</a> para crearla"
            return true;    
            }    
          
       }      
       
       //Titulo del grid
       $.fn.monitorgrid.options.titulo=$(xml).find("configuracion_grid").find("forma").text();
       
       //Titulo de forma de alta y de edición
       if ($(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[0]=="el")
           nuevo="Nuevo ";
       else
           nuevo="Nueva ";
       
       //Parametro de prefiltro
       $.fn.monitorgrid.options.requeriesFilter=$(xml).find("configuracion_grid").find("prefiltro").text();
       
       $.fn.monitorgrid.options.leyendas[0]=nuevo+$(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[1];
       $.fn.monitorgrid.options.leyendas[1]="Edición de "+$(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[1];
       $.fn.monitorgrid.options.logPhrase=$(xml).find("configuracion_grid").find("alias_tab").text();
       
       var oColumnas=$(xml).find("column_definition");
        $.fn.monitorgrid.options.sortname=oColumnas.children()[0];

        var sPermiso="";
        var oPermisos=$(xml).find("clave_permiso");
        
        if (oPermisos.length==0) {
            $.fn.monitorgrid.options.error="<p>Permisos insuficientes para consultar este catálogo, consulte con el administrador del sistema</p>";
            return;
        }
                 
        oPermisos.each( function() {
            sPermiso+=$(this).text()+",";
        })
        sPermiso=sPermiso.substr(0,sPermiso.length-1);

        if (sPermiso.indexOf("1")==-1) {
            $.fn.monitorgrid.options.colModel=null;
            return true;
        }

        oTamano=oColumnas.find('tamano');
        oAlias= oColumnas.find('alias_campo');
        var suffix =  "_" + $.fn.monitorgrid.options.app + "_" + $.fn.monitorgrid.options.entidad+ "_"+ $.fn.monitorgrid.options.datestamp;
        $("#grid"+suffix).attr("requeriesFilter",$.fn.monitorgrid.options.requeriesFilter);
        //var suffix = "-" + sDateTime(new Date());
        oAlias.each( function() {
             
            var sParent=$(this).parent()[0].tagName;
            if (sPermiso.indexOf("5")==-1 && $($(this).parent()).find("dato_sensible").text()=="1")
                return true;
            if (sParent=='column_definition') return true;
            oColumna={
                name:sParent+suffix,
                index:sParent+suffix,
                width:$(oTamano[iCol]).text(),
                sortable:true
            };

            $.fn.monitorgrid.options.colNames[iCol]=$(this).text();
            $.fn.monitorgrid.options.colModel[iCol]=oColumna;
            $.fn.monitorgrid.options.groupFields[iCol]=$(this).text();
            iCol++;
        });

        $("#pager"+ suffix).attr("security", sPermiso);
    
    }
    
    $.fn.monitorgrid.openKardex = function(nApp, nEntidad,sDateStamp, id) {
        var suffix =  "_" + nApp + "_" + nEntidad +"_" + sDateStamp;

        var $tabs = $('#tabMisApps').tabs({
            tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close'>Cerrar tab</span></li>"
        });
        
                
        //Verifica si ya está abierto el tab en modo de edición
        if ($("#tabEditEntity"+suffix + "_" + id).length) {
            $tabs.tabs( "select", "#tabEditEntity"+suffix+"_"+id);
        }
        else {
            $("#divwait")
            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Construyendo el kardex...</p>")
            .attr('title','Espere un momento por favor') 
            .dialog({
                    height: 140,
                    modal: true,
                    autoOpen: true,
                    closeOnEscape:false
            });
            
            oGrid=$('#grid'+ suffix);
            var nRow=oGrid.getGridParam('selrow');
            sTabTitulo=oGrid.jqGrid()[0].p.colNames[1] + ' ' + oGrid.getCell(nRow,1);
            sEntidad=$('#grid'+ suffix).jqGrid()[0].id.split("_")[2];
            $tabs.tabs( "add", "#tabEditEntity"+suffix+"_"+id, sTabTitulo);
            $tabs.tabs( "select", "#tabEditEntity"+suffix+"_"+id);
            oTabPanel =$("#tabEditEntity"+suffix+"_"+id);
            //Crea la interfaz de la aplicación abierta
            oTabPanel.html(
                "<div id='splitterContainer_"+ nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp +"' class='splitterContainer'>"+
                "   <div id='leftPane_"+ nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp +"' class='leftPane'>"+
                "       <div id='accordion_"+nApp + "_" + nEntidad+"_" + id + "_" + sDateStamp + "' class='accordionContainer'>"+
                "               <h3>&nbsp;Catálogos relacionados</h3>" +
                "                   <div id='tvApp_" + nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp + "' class='treeContainer' behaviour='kardex'></div>" +
                "               <h3>&nbsp;Actividad reciente</h3>" +
                "                   <div id='bitacora_"+nApp + "_" + nEntidad+"_" + id + "_" + sDateStamp + "'></div>"+
                "               <h3>&nbsp;Mis reportes</h3>" +
                "                   <div id='filtros_"+nApp + "_" + nEntidad+"_" + id + "_" + sDateStamp + "'></div>"+
                "       </div>"+                
                "   </div>"+
                "   <div id='rigthPane_"+ nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp+"' class='rigthPane'>"+
                "       <div id='divForeignGrids_" + nApp + "_" + nEntidad + "_" + id + "_" + sDateStamp +"' class='gridKardexContainer'>" +
                "               <br><br><br><br><br><br><br><br><br><br><br>"+
                "               <div class='ui-widget mensaje-info' >" +
                "                  <div class='ui-state-highlight ui-corner-all' style='padding: 0 .7em;'>" +
                "                        <p ><span class='ui-icon ui-icon-info' style='float: left; margin-right: .3em;'></span><strong>En este espacio se muestran los catálogos relacionados a " + $.fn.monitorgrid.options.logPhrase + " '" + oGrid.getCell(nRow,1) + "', seleccione una carpeta del menú de la izquierda</strong></p>"+
                "                  </div>" + 
                "              </div>"  + 
                "        </div>" +
                "   </div>"+
                "</div>");            
        }
        $("#grid"+suffix+"_toppager_right").children(0).html("");
    }
})(jQuery);