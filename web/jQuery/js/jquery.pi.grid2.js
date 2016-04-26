/*
 * Plugin de jQuery para cargar grid a partir de una paginota
 *
 */
( function($) {
    $.fn.appgrid2 = function(opc){
        $.fn.appgrid2.settings = {
            xmlUrl : "control", // "control", "control?$cmd=grid" "xml_tests/widget.grid.xml"
            wsParameters:"",
            app:"",
            entidad:"",
            pk:"0",
            suffix:"",
            leyendas:[],
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
            eventoGrid: "",
            error:"",
            claveTipoGrid:"",
            agrupar:false
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $.fn.appgrid2.options = $.extend($.fn.appgrid2.settings, opc);
            var nApp=$.fn.appgrid2.options.app;
            var nEntidad=$.fn.appgrid2.options.entidad;
            var nPK=$.fn.appgrid2.options.pk;
            var suffix =  "_" + nApp + "_" + nEntidad + "_" + nPK; //+ "_" + $.fn.appgrid2.options.datestamp;

            obj = $(this);
            obj.attr("ready", "");
            //Verifica si el objeto padre es un tabEntity
            //Si así es toma de su id el sufijo app + entidad principal + entidad foranea            
            $(this).html("<table width='100%' id='grid"+ suffix +
                "' titulo='" + $.fn.appgrid2.options.titulo +
                "' wsParameters='"+ $.fn.appgrid2.options.wsParameters +
                "' openkardex='" + $.fn.appgrid2.options.openKardex +
                "' editingApp='" + $.fn.appgrid2.options.editingApp +
                "' datestamp='" + $.fn.appgrid2.options.datestamp +
                "' originatingObject='"+ $.fn.appgrid2.options.originatingObject +
                "' callFormWithRelationships='"+$.fn.appgrid2.options.callFormWithRelationships+
                "' updateTreeAfterPost='" + $.fn.appgrid2.options.updateTreeAfterPost +
                "' requeriesFilter='" + $.fn.appgrid2.options.requeriesFilter  +
                "'>" +
                "</table><div id='pager" + suffix +"' security=''><div align='center' id='loader" + suffix +"'><br/><br/><br/><br/><br/><br/><br /><br/><br/><br/><br/><br/><br/><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br /></div></div>");

            $.fn.appgrid2.getGridDefinition();
        });

    };

    $.fn.appgrid2.getGridDefinition = function(){
        $.ajax(
        {
            url: $.fn.appgrid2.options.xmlUrl + "?$cmd=grid&$cf=" + $.fn.appgrid2.options.entidad + "&$ta=select&$dp=header&$w=" + $.fn.appgrid2.options.wsParameters,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  processGridDefinition, 
            error:function(xhr,err){
                sTipoError='Problemas al recuperar definición de grid.\n';
                if (xhr.responseText.indexOf('NullPointerException')>-1)
                    sTipoError+='Problemas de conexión a la base de datos, verifique la conexión a la red.';
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
        
        $.fn.appgrid2.handleGridDefinition(xml);

        if ($.fn.appgrid2.options.error!="") {       
            obj.html("<div class='ui-widget'>"+
                "<br><br><br><br><br><br><br><br><br><br><br><div style='padding: 0 .7em; width: 80%; margin-left: auto; margin-right: auto;text-align: center;' class='ui-state-error ui-corner-all'>"+
                "<p class='app_error'><span style='float: left; margin-right: .3em;' class='ui-icon ui-icon-alert'></span>"+
                $.fn.appgrid2.options.error+"</p>"+
                "</div></div>"+obj.html());
                   
            //Remueve del dom el men saje de espera
            $("#loader_"+ $.fn.appgrid2.options.app + "_" + $.fn.appgrid2.options.entidad +"_0" /*+ "_" + $.fn.appgrid2.options.datestamp*/).html("");
                    
            /* Captura el click del link */
            $(".editLink").click(function(){
                sCmd=this.id.split("-")[0];
                nQuery=this.id.split("_")[0].split("-")[1];
                nApp=this.id.split("_")[1];
                nForma=this.id.split("_")[2];
                        
                if (sCmd=="lnkEditQuery")  {                           
                    $("#top").form2({
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
                    //Borra el error y coloca el mensaje de espera"
                    obj.find(".ui-widget").remove();
                    $("#loader_"+ $.fn.appgrid2.options.app + "_" + $.fn.appgrid2.options.entidad + "_0" /* + "_" + $.fn.appgrid2.options.datestamp */).html("<br/><br/><br/><br/><br/><br/><br /><br/><br/><br/><br/><br/><br/><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br />");
                    $.fn.appgrid2.options.error="";
                    $.fn.appgrid2.getGridDefinition();
                /*setTimeout("$('.queued_grids:first').gridqueue()",500);*/
                }
                        
            });

            return true;
        }

        var suffix =  "_" + $.fn.appgrid2.options.app + "_" + $.fn.appgrid2.options.entidad + "_" + $.fn.appgrid2.options.pk; //+ "_" + $.fn.appgrid2.options.datestamp;

        /* Inicia implementación del grid */
        var nApp=$.fn.appgrid2.options.app;
        var nEntidad=$.fn.appgrid2.options.entidad;

        /* Agrega la liga para quitar filtro desde el contructor    */
        if ($.fn.appgrid2.options.wsParameters!="" && $.fn.appgrid2.options.showFilterLink)
            $.fn.appgrid2.options.titulo+="&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid" + suffix+"'>(Quitar filtro)</a>";

        if ($("#grid"+suffix).attr("requeriesFilter")==0 ) //Si no está configurado para filtrar registros, trae el cuerpo
            xmlURL=$.fn.appgrid2.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$w=" + $.fn.appgrid2.options.wsParameters;
        else if ($("#lnkRemoveFilter_grid"+suffix).length==0)  //Si está configurado y no tiene un filtro solo trae la cabecera
            xmlURL=$.fn.appgrid2.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$w=" + $.fn.appgrid2.options.wsParameters;
        else
            xmlURL=$.fn.appgrid2.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$w=" + $.fn.appgrid2.options.wsParameters;
        
        if ($.fn.appgrid2.options.claveTipoGrid=="2") {
            xmlURL= xmlURL.replace("control?$cmd=grid","control?$cmd=tree");
        }
        
        if (nEntidad==285)  {
            nRowNum = 5;
            aRowList=[5,10,20]
        } else {
            nRowNum = 25;
            aRowList = [25,50,100];
        }
        
        //Se reemplaza el contenido del toppager_right con
        // el combo para agrupar
        var sOptions="<option value='clear'>Quitar agrupamiento</option>";
        var i=0; 
        $.fn.appgrid2.options.agrupar=false;
        $(xml).find("column_definition").children().each(function(){
                if ($(this).find("usado_para_agrupar").text()=="1")  {
                  sOptions+="<option value='" + $.fn.appgrid2.options.colModel[i].name + "'>" + $(this).find("alias_campo").text() + "</option>";
                  $.fn.appgrid2.options.agrupar=true;
                }
                i++;
            }
        );
        
       oGridConfig= {
            url:xmlURL,
            datatype: "xml",
            colNames:$.fn.appgrid2.options.colNames,
            colModel:$.fn.appgrid2.options.colModel,
            rowNum:nRowNum,      
            width:$.fn.appgrid2.options.width,
            shrinkToFit: false,
            height:$.fn.appgrid2.options.height,
            rowList:aRowList,
            pager: jQuery('#pager' + suffix),
            toppager:true,
            sortname: $.fn.appgrid2.options.colModel[0],//$.fn.appgrid2.options.sortname,//+suffix,
            viewrecords: true,
            sortorder: "desc",
            //loadonce: true,
            caption:$.fn.appgrid2.options.titulo, 
            /*onSelectRow: function(id){
                if(id && id!==lastSel){ 
                   $("#grid" + suffix).restoreRow(lastSel); 
                   lastSel=id; 
                }
                
                $("#grid" + suffix).editRow(id, true); 
              },*/        
            gridComplete:function(){
                if ($.fn.appgrid2.options.eventoGrid!=undefined && $.fn.appgrid2.options.eventoGrid !=null && $.fn.appgrid2.options.eventoGrid!="") {
                    $.globalEval($.fn.appgrid2.options.eventoGrid);
                }
                /* Quita eventos anteriores */
                $(".gridlink").unbind("click");
                        
                /* Establece eventos a los link del interior del grid*/ 
                $(".gridlink").click(function(e, data) {
                    var cmdWS=this.id.split("#")[0];
                    var relaciones=this.id.split("#")[1];
                    var nApp=cmdWS.split("-")[1];
                    var nEntidad=cmdWS.split("-")[2];
                    var nPK=cmdWS.split("-")[3];
                    aCmdWS=cmdWS.split("-");
                    var sW="";
                    if (aCmdWS.length>4) {
                        for (i=4; i<aCmdWS.length;i++)
                            sW+=aCmdWS[i]+"&";
                    }
                            
                    var actividad=$(this).attr("actividad");
                            
                    if (sW=="")
                        sW="";
                            
                    if (relaciones==undefined)
                        sShowRelationships="false";
                    else
                        sShowRelationships="true";
                            
                    var sModo="";
                            
                    if (nPK=="0") 
                        sModo="insert";
                    else
                        sModo="update";

                    $("#divwait")
                    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                    .attr('title','Espere un momento por favor') 
                    .dialog({
                        height: 140,
                        modal: true,
                        autoOpen: true,
                        closeOnEscape:false
                    });
                            
                    if ($("#_gado_").val()=="true")
                        sUpdateControl="grid" + suffix;
                    else
                        sUpdateControl="";
                                
                    $("#top").form2({
                        app: nApp,
                        forma:nEntidad,
                        datestamp:oGrid.attr("datestamp"),
                        modo:sModo,
                        columnas:1,
                        pk:nPK,
                        filtroForaneo:sW,
                        height:"500",
                        width:"80%",
                        originatingObject:oGrid[0].id + "_"+ actividad,
                        updateControl:sUpdateControl
                    });
                });
                
                $(".reportlink").unbind("click");
                $(".reportlink").click(function(e, data) {
                    //Aqui se hace un envío con un post
                    postConfig="clave_estatus=1";
                    $.post("control?$cmd=register&$cf=159&$ta=update&$pk=" + $(this).attr("actividad") ,postConfig);
                });
                
                //En el caso de mensajeria instantanea se elimina el titulo    
                if (nEntidad==285)  {
                    $("#gview_grid_106_285_0").find(".ui-jqgrid-titlebar").hide();
                    $("#grid_106_285_0_toppager_right").find(".ui-paging-info").html("");
                    $("#grid_106_285_0_toppager").addClass("ui-state-highlight");
                    $("#grid_106_285_0_clave_empleado_contacto_106_285_0").addClass("ui-state-highlight");
                    $("#pager_106_285_0_center").addClass("ui-state-highlight");
                    $($("#gview_grid_106_285_0").children()[2]).hide();
                    
                    $(".divChatContact").unbind("click"); 
                    $(".divChatContact").click(function() { 
                        nEmpleado=$(this).attr("clave");
                        sEstatus=$(this).attr("estatus");
                        
                        if (sEstatus=="offline") {
                            alert($(this).html().substr(29, $(this).html().length)+ ' no está en linea, el sistema enviará su mensaje a su buzón');
                            $("#divwait")
                            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                            .attr('title','Espere un momento por favor') 
                            .dialog({
                                height: 140,
                                modal: true,
                                autoOpen: true,
                                closeOnEscape:false
                            });
                    
                            $("#top").form2({
                                app: 106,
                                forma:286,
                                datestamp:obj.attr("datestamp"),
                                modo:"insert",
                                titulo: "Consulta ",
                                columnas:1,
                                pk:0,
                                filtroForaneo:"10=clave_empleado="+nEmpleado + "&11=clave_empleado="+$("#_ce_").val(),
                                height:"500",
                                width:"80%",
                                originatingObject: "",
                                updateControl:""
                            });

                        } else {
                            if ($("#_imContacto_"+nEmpleado).length) {
                                //Selecciona el tab correspondiente
                                $("#_imTab").tabs("select", "#_imContacto_"+nEmpleado);
                            }
                            else {

                                if ($(this).html().length>50)
                                    sTitulo=$(this).html().substr(0, 45)+"...";
                                else 
                                    sTitulo=$(this).html();

                                $("#_imTab").tabs( "add", "#_imContacto_"+nEmpleado,sTitulo );
                                
                                $("#_imContacto_"+nEmpleado).html("<textarea readonly='readonly' style='width:" + ($("#_imTab").width()-10) + "px; height:100px;' id='_imTextArea_" + nEmpleado +"' ></textarea> <input type='text' id='_imInput_" + nEmpleado + "' value='Escribe aqui' style='width:" + ($("#_imTab").width()-10) + "px;'/>").attr("style","padding: 2px;");
                                
                                //Se programa el evento 
                                $("#_imInput_" + nEmpleado).keydown(function(event) {
                                  nEmpleado=this.id.split("_")[2];  
                                  if (event.which == 13) {
                                    event.preventDefault();
                                    //Aqui se hace un envío con un post
                                    postConfig="clave_empleado_remitente=" + $("#_ce_").val() + 
                                               "&clave_empleado_destinatario=" + nEmpleado + 
                                               "&mensaje=" + $(this).val() + 
                                               "&fecha=" + sDateTimeToString(new Date()) +
                                               "&prioridad=1"+
                                               "&clave_estatus=1";
                                    $("#_imTextArea_" + nEmpleado).val($("#_imTextArea_" + nEmpleado).val()+"\nYo: "+$(this).val());
                                    $(this).val("");
                                    
                                    $.post("control?$cmd=register&$cf=286&$ta=insert&$pk=0",postConfig);
                                    
                                  }
                                });
                                
                                $("#_imTab").tabs( "select", "#_imContacto_"+nEmpleado);
                            }                            
                        }
 
                    }); 
                //$("#grid_106_285_0_toppager").next().remove();
                }
                
                $(".progressbar").each( function(){
                    $(this).progressbar({
                        value: $(this).attr("avance")
                    });
                });
                    
                // Presenta la forma de búsqueda si el parametro es verdero y no hay un filtro
                if ($("#grid"+suffix).attr("requeriesFilter")==1 && $("#lnkRemoveFilter_grid"+suffix).length==0) {
                    $("#top").form2({
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
                }
                    
                $("#divwait").dialog( "close" );

            },
            ondblClickRow: function(rowid) {

                    nRow=rowid;
                    if (nRow) {
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
                                            
                        $("#top").form2({
                            app: nApp,
                            forma:nEntidad,
                            datestamp:$(this).attr("datestamp"),
                            modo:"update",
                            titulo: $.fn.appgrid2.options.leyendas[1],
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
                
                    return false;
            }
        };
        
        if ($.fn.appgrid2.options.agrupar && $.fn.appgrid2.options.claveTipoGrid=="1") {
            oGridConfig.grouping=false;
            oGridConfig.groupingView = {
                groupField : $.fn.appgrid2.options.groupFields[0],
                            groupColumnShow : [true],
                            groupText : ['<b>{0}</b>'],
                            groupCollapse : false,
                            groupSummary : [true],
                            groupOrder: ['asc']/*,
                            groupDataSorted : true*/
                            };
            //oGridConfig.footerrow=true;
            //oGridConfig.userDataOnFooter=true;
        }
        
        if ($.fn.appgrid2.options.claveTipoGrid=="2") {
            oGridConfig.treeGrid=true;
            oGridConfig.treeGridModel = 'adjacency';
            oGridConfig.ExpandColumn=$.fn.appgrid2.options.colModel[1].name;
            oGridConfig.mtype="POST";
        }
        
        var oGrid=$("#grid" + suffix).jqGrid(oGridConfig);
        
        //Función para editar en línea
        /*$("#grid" + suffix).jqGrid('editRow',rowid, 
        { 
            keys : true, 
            oneditfunc: function() {
                alert ("edited"); 
            }
        });*/

        
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
                                
                                
                    $("#top").form2({
                        app: nApp,
                        forma:nEntidad,
                        datestamp:$(this).attr("datestamp"),
                        modo:"insert",
                        titulo: $.fn.appgrid2.options.leyendas[0],
                        columnas:1,
                        pk:0,
                        filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3="+$(this).attr("wsParameters"),
                        height:"500",
                        width:"80%",
                        originatingObject:oGrid.id,
                        showRelationships:$(this).attr("callFormWithRelationships"),
                        updateControl:sUpdateControl
                    });
                
                    return false;
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
                                            
                        $("#top").form2({
                            app: nApp,
                            forma:nEntidad,
                            datestamp:$(this).attr("datestamp"),
                            modo:"update",
                            titulo: $.fn.appgrid2.options.leyendas[1],
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
                
                    return false;
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
                               dataType: ($.browser.msie) ? "text" : "xml",
                                success:  function(data){
                                    if (typeof data == "string") {
                                        xmlDelete = new ActiveXObject("Microsoft.XMLDOM");
                                        xmlDelete.async = false;
                                        xmlDelete.validateOnParse="true";
                                        xmlDelete.loadXML(favorito);
                                        if (xmlDelete.parseError.errorCode>0) {
                                            alert("Error de compilación xml:" + xmlDelete.parseError.errorCode +"\nParse reason:" + xmlDelete.parseError.reason + "\nLinea:" + xmlDelete.parseError.line);
                                        }
                                    }
                                    else {
                                        xmlDelete= data;
                                    }
                                    
                                    if ($(xmlDelete).find("error").length>0) {
                                        alert("Error al eliminar registro: " + $(xmlDelete).find("error").text());
                                        return;
                                    }
                                    oGrid.jqGrid('delRowData',nRow);
                                    //Actualiza árbol
                                    if ($.fn.appgrid2.options.updateTreeAfterPost) {
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
                
                    return false;
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
        .navSeparatorAdd('#grid'+ suffix+'_toppager',{
            sepclass : "ui-separator",
            sepcontent: ""
        })
        .navButtonAdd('#grid'+ suffix+'_toppager',{
            caption:"",
            buttonicon:"ui-icon-search",
            onClickButton:  function() {

                $("#divwait")
                .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                .attr('title','Espere un momento por favor') 
                .dialog({
                    height: 140,
                    modal: true,
                    autoOpen: true,
                    closeOnEscape:false
                });


                $("#top").form2({
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
        }).navSeparatorAdd('#grid'+ suffix+'_toppager',{
                sepclass : "ui-separator",
                sepcontent: ""
            }).navButtonAdd('#grid'+ suffix+'_toppager',{
                caption:"",
                buttonicon:"ui-icon-calculator",
                onClickButton:  function() {
                    nApp=this.id.split("_")[1];
                    nForma=this.id.split("_")[2];
                    window.open("control?$cmd=download&$cf="+nForma+"&$ta=select&$w="+$.fn.appgrid2.options.wsParameters, "_blank");
                    return false;            
                },
                position: "last",
                title:"Descargar catálogo en formato XLS",
                cursor: "pointer"
       });
    
        //Remueve del dom el loader
        $("#loader"+ suffix).remove();
        //if ($.fn.appgrid2.options.insertInDesktopEnabled=="1")

            oGrid.navGrid('#grid'+ suffix+'_toppager',{
                edit:false,
                add:false,
                del:false,
                search:false,
                view:false
            })
            .navSeparatorAdd('#grid'+ suffix+'_toppager',{
                sepclass : "ui-separator",
                sepcontent: ""
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
                        ",openKardex:" + oGrid.attr("openKardex") +
                        ",editingApp:" + oGrid.attr("editingApp") +
                        ",inDesktop:true"
                        );
                            
                    $.ajax(
                    {
                        url: "control?"+postConfig,
                        dataType: ($.browser.msie) ? "text" : "xml",
                        success:  function(favorito){
                            if (typeof favorito == "string") {
                                xmlFav = new ActiveXObject("Microsoft.XMLDOM");
                                xmlFav.async = false;
                                xmlFav.validateOnParse="true";
                                xmlFav.loadXML(favorito);
                                if (xmlFav.parseError.errorCode>0) {
                                    alert("Error de compilación xml:" + xmlFav.parseError.errorCode +"\nParse reason:" + xmlFav.parseError.reason + "\nLinea:" + xmlFav.parseError.line);
                                }
                            }
                            else {
                                xmlFav= favorito;
                            }
                                            
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

                            setTimeout("$('.queued_grids:first').gridqueue()",2000);
                            alert("Se agregó el grid a tus favoritos");  
                        },
                        error:function(xhr,err){
                            alert("Error al eliminar registro");
                        }
                    }); 
                    return false;            
                },
                position: "last",
                title:"Insertar en favoritos",
                cursor: "pointer"
            });
            
            
        
        //Aparece un botón para editar la forma si se cuenta con el perfil de admnistrador
        if ($("#_cp_").val()=="1") {
            oGrid.navGrid('#grid'+ suffix+'_toppager',{
                edit:false,
                add:false,
                del:false,
                search:false
            })
            .navSeparatorAdd('#grid'+ suffix+'_toppager',{
                sepclass : "ui-separator",
                sepcontent: ""
            })
            .navButtonAdd('#grid'+ suffix+'_toppager',{
                caption:"",
                buttonicon:"ui-icon-gear",
                onClickButton:function() {
                       
                        nPK= this.id.split("_")[2];
                        nEditingApp=$(this).attr("editingApp");
                                
                        //Verifica si se actualiza árbol
                        sUpdateControl="";
                        if ($(this).attr("updateTreeAfterPost")=="true")
                            sUpdateControl=oGrid.attr("originatingObject");
                        else
                            sUpdateControl=oGrid[0].id;
                    
                        $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                        });
                                            
                        $("body").form2({
                            app: 1,
                            forma:3,
                            datestamp:$(this).attr("datestamp"),
                            modo:"update",
                            titulo: $.fn.appgrid2.options.leyendas[1],
                            columnas:1,
                            pk:nPK,
                            filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3=clave_aplicacion="+ $.fn.appgrid2.options.app + "&4=clave_aplicacion=" + $.fn.appgrid2.options.app,
                            height:"500",
                            width:"80%",
                            originatingObject: $(this).id,
                            showRelationships:$(this).attr("callFormWithRelationships"),
                            updateControl:sUpdateControl
                        });
                
                },
                position: "last",
                title:"Editar forma",
                cursor: "pointer"
            });
        }
        
        //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
        if ($.fn.appgrid2.options.wsParameters!="" && $.fn.appgrid2.options.showFilterLink) {
            $("#lnkRemoveFilter_grid" + suffix).click(function() {
                nApp=this.id.split("_")[2];
                nForma=this.id.split("_")[3];
                sDS=this.id.split("_")[4];
                var sGridId="#grid_" + nApp + "_" + nForma+ "_"+ sDS;
                $(this).remove();
                        
                if ($("#grid"+suffix).attr("requeriesFilter")=="1")
                    $("#top").form2({
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
                        url:"control?$cmd=grid&$cf=" + nForma + "&$ta=select&$dp=body"
                    }).trigger("reloadGrid")
            
                return false;    
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

        if ($.fn.appgrid2.options.agrupar)
            $("#grid" + suffix + "_toppager_right").attr("id","gridAgrupar" + suffix + "_toppager_right").html("<div id='divAgrupar_"+ suffix +"'><i>Agrupar por: </i><select id='cbGroups"+suffix+"'>"+sOptions+"</select></div>");

        //Establece evento para select
        $("#cbGroups"+suffix).change(function() {
            var suffix=this.id.split("_")[1] + "_" +
            this.id.split("_")[2] + "_" +
            this.id.split("_")[3];
            var sVal=$(this).val();
            if(sVal=='clear')
                oGrid.jqGrid('groupingRemove',true);
            else
                oGrid.jqGrid('groupingGroupBy',sVal);
        });
    
        //Verifica si el grid está en una cola
        if ($.fn.appgrid2.options.inQueue)
            setTimeout("$('.queued_grids:first').gridqueue({height: $('#_gq_').val()+'%'})",3000);

        if ($.fn.appgrid2.options.removeGridTitle)
            $('.ui-jqgrid-titlebar',oGrid).remove();

    /* Finaliza implementación de grid */

    }
         
    $.fn.appgrid2.handleGridDefinition = function(xml){
        var iCol=0;
        
        //Manejo de errores
        var error=$(xml).find("error");
        
        if (error.length>0) {
            if (error.text().indexOf("SQLServerException")>0 ) {
                nConsulta= error.find("clave_consulta").text();
                $.fn.appgrid2.options.error+="Hay un error en la consulta (" + 
                error.find("general").text() + ". " +  
                error.find("descripcion").text() +")";
                if ($("#_cp_").val()=="1") 
                    $.fn.appgrid2.options.error+=", haga click <a href='#' id='lnkEditQuery-" + nConsulta  + "_" + 
                    $.fn.appgrid2.options.app +"_" +  $.fn.appgrid2.options.entidad +"' class='editLink'>aqui</a> para editarla";
                return true;    
            }  
            
            if (error.text().indexOf("No se pudo realizar la conexión TCP/IP al host")>0) {
                $.fn.appgrid2.options.error="Sin conexión. Haga clic <a href='#' class='editLink' id='lnkReloadGrid-0_" + $.fn.appgrid2.options.app +"_" +  $.fn.appgrid2.options.entidad +"'>aqui</a> para volver a intentarlo";
                return true; 
            }
            
            if (error.find("tipo").text()=="Exception" && $("#_cp_").val()=="1") {
                $.fn.appgrid2.options.error+="No hay una consulta asociada a esta acción" + 
                error.find("general").text() + ". " +  
                error.find("descripcion").text() + "), haga click <a href='#' id='lnkEditQuery-0" +   + "_" + 
                $.fn.appgrid2.options.app +"_" +  $.fn.appgrid2.options.entidad +"' class='editLink'>aqui</a> para crearla";
                return true;    
            }    
        }      
       
        //Titulo del grid
        $.fn.appgrid2.options.titulo=$(xml).find("configuracion_grid").find("forma").text();
       
        //Titulo de forma de alta y de edición
        if ($(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[0]=="el")
            nuevo="Nuevo ";
        else
            nuevo="Nueva ";
       
        //Parametro de prefiltro
        $.fn.appgrid2.options.claveTipoGrid=$(xml).find("configuracion_grid").find("clave_tipo_grid").text();
        $.fn.appgrid2.options.requeriesFilter=$(xml).find("configuracion_grid").find("prefiltro").text();
        $.fn.appgrid2.options.eventoGrid=$(xml).find("configuracion_grid").find("evento_grid").text(); 
       
        $.fn.appgrid2.options.leyendas[0]=nuevo+$(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[1];
        $.fn.appgrid2.options.leyendas[1]="Edición de "+$(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[1];
        $.fn.appgrid2.options.logPhrase=$(xml).find("configuracion_grid").find("alias_tab").text();
       
        var oColumnas=$(xml).find("column_definition");
        $.fn.appgrid2.options.sortname=oColumnas.children()[0];

        var sPermiso="";
        var oPermisos=$(xml).find("clave_permiso");
        
        if (oPermisos.length==0) {
            $.fn.appgrid2.options.error="<p>Permisos insuficientes para consultar este cat&aacute;logo, consulte con el administrador del sistema</p>";
            return;
        }
                 
        oPermisos.each( function() {
            sPermiso+=$(this).text()+",";
        });
        sPermiso=sPermiso.substr(0,sPermiso.length-1);
    
        //No se tiene permiso de visualizar el grid
        if (sPermiso.indexOf("1")==-1) {
            $.fn.appgrid2.options.colModel=null;
            $.fn.appgrid2.options.error="<p>Permisos insuficientes para consultar este cat&aacute;logo, consulte con el administrador del sistema</p>";
            return;
        }

        oTamano = oColumnas.find('tamano');
        oAlias = oColumnas.find('alias_campo');
        var suffix =  "_" + $.fn.appgrid2.options.app + "_" + $.fn.appgrid2.options.entidad + "_" + $.fn.appgrid2.options.pk; //+ "_"+ $.fn.appgrid2.options.datestamp;
        $("#grid"+suffix).attr("requeriesFilter",$.fn.appgrid2.options.requeriesFilter);
        //var suffix = "-" + sDateTime(new Date());

        oAlias.each( function() {
            
            var sParent=$(this).parent()[0].tagName;
            if (sPermiso.indexOf("5")==-1 && $($(this).parent()).find("dato_sensible").text()=="1")
                return true;
            if (sParent=='column_definition') return true;
            oColumna=null;
            if ($(this).parent().attr("tipo_dato")=="money") {
                oColumna={
                    name:sParent+suffix,
                    index:sParent+suffix,
                    width:oTamano[iCol].attributes.length==0?$(oTamano[iCol]).text():$(oTamano[iCol+1]).text(),
                    sortable:true,
                    formatter: "currency",
                    summaryType: "sum"
                };                                
            } else if ($(this).parent().index()==0) {
                oColumna={
                    name:sParent+suffix,
                    index:sParent+suffix,
                    width:oTamano[iCol].attributes.length==0?$(oTamano[iCol]).text():$(oTamano[iCol+1]).text(),
                    sortable:true,
                    summaryType: "count",
                    summaryTpl : "Total ({0})"
                };                                
           } else if ($(this).parent().index()!=0) {
                oColumna={
                    name:sParent+suffix,
                    index:sParent+suffix,
                    width:oTamano[iCol].attributes.length==0?$(oTamano[iCol]).text():$(oTamano[iCol+1]).text(),
                    sortable:true  
                };                
            }
            
            if ($.fn.appgrid2.options.claveTipoGrid==2 && iCol==0 ) {
                oColumna.hidden=true;
                oColumna.key=true;
            }
            
            $.fn.appgrid2.options.colNames[iCol]=$(this).text();
            $.fn.appgrid2.options.colModel[iCol]=oColumna;
            $.fn.appgrid2.options.groupFields[iCol]=$(this).text();
            iCol++;
        });
    
        $("#pager"+ suffix).attr("security", sPermiso);
    
    }
})(jQuery);