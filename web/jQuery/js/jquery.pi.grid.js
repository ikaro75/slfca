/*
 * Plugin de jQuery para cargar grid a partir de una paginota
 *
 */
/* 
\u00e1 -> á 
\u00e9 -> é 
\u00ed -> í 
\u00f3 -> ó
\u00fa -> ú 

\u00c1 -> Á 
\u00c9 -> É 
\u00cd -> Í 
\u00d3 -> Ó 
\u00da -> Ú 

\u00f1 -> ñ 
\u00d1 -> Ñ
\u00BF -> ¿
 * and open the template in the editor.
 */
( function($) {
    $.fn.appgrid = function(opc){
        $.fn.appgrid.settings = {
            xmlUrl : "control" , // "control", "control?$cmd=grid" "xml_tests/widget.grid.xml"
            wsParameters:"",
            titulo:"",
            app:"",
            entidad:"",
            pk:"0",
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
            gridComplete: "",
            error:"",
            claveTipoGrid:"",
            agrupar:false
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $("#_status_").val("Inicializando cat\u00e1logo");
            $.fn.appgrid.options = $.extend($.fn.appgrid.settings, opc);
            var nApp=$.fn.appgrid.options.app;
            var nEntidad=$.fn.appgrid.options.entidad;
            var nPK=$.fn.appgrid.options.pk;
            var suffix =  "_" + nApp + "_" + nEntidad + "_" + nPK; //+ $.fn.appgrid.options.datestamp;

            obj = $(this);
            //Verifica si el objeto padre es un tabEntity
            //Si así es toma de su id el sufijo app + entidad principal + entidad foranea            
            $(this).html("<table width='100%' id='grid"+ suffix +
                "' titulo='" + $.fn.appgrid.options.titulo +
                "' wsParameters='"+ $.fn.appgrid.options.wsParameters +
                "' openkardex='" + $.fn.appgrid.options.openKardex +
                "' editingApp='" + $.fn.appgrid.options.editingApp +
                "' datestamp='" + $.fn.appgrid.options.datestamp +
                "' originatingObject='"+ $.fn.appgrid.options.originatingObject +
                "' callFormWithRelationships='"+$.fn.appgrid.options.callFormWithRelationships+
                "' updateTreeAfterPost='" + $.fn.appgrid.options.updateTreeAfterPost +
                "' requeriesFilter='" + $.fn.appgrid.options.requeriesFilter  +
                "' frecuenciaActualizacion='" + $.fn.appgrid.options.frecuenciaActualizacion  +
                "'>" +
                "</table><input type='hidden' value='' id='grid_complete_code_" + suffix + "'><div id='pager" + suffix +"' security=''><div align='center' id='loader" + suffix +"'><br/><br/><br/><br/><br/><br/><br /><br/><br/><br/><br/><br/><br/><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br /></div></div>");

            $.fn.appgrid.getGridDefinition();
        });

    };

    $.fn.appgrid.getGridDefinition = function(){
        $("#_status_").val("Cargando definici\u00f3n del cat\u00e1logo");
        $.ajax(
        {
            url: $.fn.appgrid.options.xmlUrl + "?$cmd=grid&$cf=" + $.fn.appgrid.options.entidad.split('-')[0] + "&$ta=select&$dp=header&$w=" + $.fn.appgrid.options.wsParameters,
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  processGridDefinition, 
            error:function(xhr,err){
                sTipoError='Problemas al recuperar definici\u00f3n de cat\u00e1logo.\n';
                if (xhr.responseText.indexOf('NullPointerException')>-1)
                    sTipoError+='Problemas de conexi\u00f3n a la base de datos, verifique la conexi\u00f3n a la red.'
                else
                    sTipoError+=xhr.responseText;

                suffix=obj.children()[1].id.replace("pager","");
                $("#loader"+suffix).html("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>"+
                    "<div class='ui-widget'>"+
                    "<div style='padding: 0 .7em; width: 80%' class='ui-state-error ui-corner-all'>"+
                    "<p class='app_error'><span style='float: left; margin-right: .3em;' class='ui-icon ui-icon-alert'></span>"+
                    sTipoError+"</p>"+
                    "</div></div>");
                $("#_status_").val("");
            }
        });
    };

    function processGridDefinition (data){
        $("#_status_").val("Procesando definici\u00f3n del cat\u00e1logo");
        
        if (typeof data == "string") {
            xml = new ActiveXObject("Microsoft.XMLDOM");
            xml.async = false;
            xml.validateOnParse="true";
            xml.loadXML(data);
            if (xml.parseError.errorCode>0) {
                alert("Error de compilaci\u00f3n xml:" + xml.parseError.errorCode +"\nParse reason:" + xml.parseError.reason + "\nLinea:" + xml.parseError.line);
                $("#_status_").val("");
            }
        }
        else {
            xml = data;
        }
                
        var iCol=0;
        //Manejo de errores
        var error=$(xml).find("error");
        
        if (error.length>0) {
            if (error.text().indexOf("SQLServerException")>0 ) {
                nConsulta= error.find("clave_consulta").text();
                $.fn.appgrid.options.error+="Hay un error en la consulta de la forma (" + error.text() + ")";
                if ($("#_cp_").val()=="1") 
                    $.fn.appgrid.options.error+=", haga click <a href='#' id='lnkEditQuery-" + $.fn.appgrid.options.entidad.split('-')[0]  + "_" + 
                    $.fn.appgrid.options.app +"_" +  $.fn.appgrid.options.entidad +"' class='editLink'>aqui</a> para editarla";
                $("#_status_").val("");
            }  
            
            if (error.text().indexOf("No se pudo realizar la conexión TCP/IP al servidor")>0) {
                $.fn.appgrid.options.error="Sin conexi\u00f3n. Haga clic <a href='#' class='editLink' id='lnkReloadGrid-0_" + $.fn.appgrid.options.app +"_" +  $.fn.appgrid.options.entidad +"'>aqui</a> para volver a intentarlo";
                $("#_status_").val("");
                return true; 
            }
            
            
            if (error.find("tipo").text()=="Exception" && $("#_cp_").val()=="1") {
                $.fn.appgrid.options.error+="No hay una consulta asociada a esta acci\u00f3n" + 
                error.find("general").text() + ". " +  
                error.find("descripcion").text() + "), haga click <a href='#' id='lnkEditQuery-1" +   + "_" + 
                $.fn.appgrid.options.app +"_" +  $.fn.appgrid.options.entidad +"' class='editLink'>aqui</a> para crearla"
                $("#_status_").val("");
                return true;    
            }  
            
            if (error.text().indexOf("Error al ejecutar la consulta")>-1 && $("#_cp_").val()=="1") {
                $.fn.appgrid.options.error+=error.text()+ ", haga click <a href='#' id='lnkEditQuery-1_3_" +  
                $.fn.appgrid.options.entidad.split('-')[0] + "' class='editLink'>aqui</a> para editar la forma";
        
               $("#loader_"+ $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad + "_" + $.fn.appgrid.options.pk /*+ $.fn.appgrid.options.datestamp*/).html("<br><br><br>"+
                    "<div class='ui-widget'>"+
                    "<div style='padding: 0 .7em; width: 80%' class='ui-state-error ui-corner-all'>"+
                    "<span style='float: left; margin-right: .3em;' class='ui-icon ui-icon-alert'></span>"+ $.fn.appgrid.options.error + "<br><br><br></div></div>");            
                    
                /* Captura el click del link */
                $(".editLink").click(function(){
                    sCmd=this.id.split("-")[0];
                    nQuery=this.id.split("_")[2];
                    $("#_cache_").val("1");
                    $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Por favor espere, construcci\u00f3n de interfaz en progreso...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                    });
                    
                    if (sCmd=="lnkEditQuery")  {                           
                        $("body").form({
                            app: 1,
                            forma:3,
                            datestamp:obj.attr("datestamp"),
                            modo:"update",
                            titulo: "Edici\u00f3n de forma",
                            columnas:1,
                            pk:nQuery,
                            filtroForaneo:"2=clave_aplicacion=1&3=",
                            height:"90%",
                            width:"90%",
                            originatingObject: obj.id,
                            updateControl:obj.id,
                            secondFieldText: "<Consulta>"
                        });
                    }

                    if (sCmd=="lnkReloadGrid") {
                        //Borra el error y coloca el mensaje de espera"
                        obj.find(".ui-widget").remove();
                        $("#loader_"+ $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad + "_" + $.fn.appgrid.options.pk).html("<br/><br/><br/><br/><br/><br/><br /><br/><br/><br/><br/><br/><br/><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br />");
                        $.fn.appgrid.options.error="";
                        $.fn.appgrid.getGridDefinition();
                    /*setTimeout("$('.queued_grids:first').gridqueue()",500);*/
                    }

                });
                $("#_status_").val("");
                return true;    
            } 
          
        }      
       
        //Titulo del grid
       $.fn.appgrid.options.titulo=$(xml).find("configuracion_grid").find("forma").text();
       $("#divgrid_"+ $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad + "_" + $.fn.appgrid.options.pk).attr("titulo",$.fn.appgrid.options.titulo)
       
        //Titulo de forma de alta y de edición
        if ($(xml).find("configuracion_grid").find("alias_tab").text().split(" ")[0]=="el")
            nuevo="Nuevo ";
        else
            nuevo="Nueva ";
       
        //Parametro de prefiltro
        $.fn.appgrid.options.claveTipoGrid=$(xml).find("configuracion_grid").find("clave_tipo_grid").text();
        $.fn.appgrid.options.requeriesFilter=$(xml).find("configuracion_grid").find("prefiltro").text();
        $.fn.appgrid.options.logPhrase=$(xml).find("configuracion_grid").find("alias_tab").text();
        $.fn.appgrid.options.gridComplete =$(xml).find('configuracion_grid').find('evento_grid').text();
        $.fn.appgrid.options.frecuenciaActualizacion  =$(xml).find('configuracion_grid').find('frecuencia_actualizacion').text();
        $.fn.appgrid.options.multiselect=$(xml).find('configuracion_grid').find('permite_multiseleccion').text()=="true"?true:false;

        var oColumnas=$(xml).find("column_definition");
        $.fn.appgrid.options.sortname=oColumnas.children()[0];

        var sPermiso="";
        var oPermisos=$(xml).find("clave_permiso");
        
        if (oPermisos.length==0) {
            $.fn.appgrid.options.error="<p>Permisos insuficientes para consultar este cat&aacute;logo, consulte con el administrador del sistema</p>";
            $("#_status_").val("");
            return;
        }
                 
        oPermisos.each( function() {
            sPermiso+=$(this).text()+",";
        })
        sPermiso=sPermiso.substr(0,sPermiso.length-1);
    
        //No se tiene permiso de visualizar el grid
        if (sPermiso.indexOf("1")==-1) {
            $.fn.appgrid.options.colModel=null;
            $.fn.appgrid.options.error="<p>Permisos insuficientes para consultar este cat&aacute;logo, consulte con el administrador del sistema</p>";
            $("#_status_").val("");
            return;
        }

        oTamano=oColumnas.find('tamano');
        oAlias= oColumnas.find('alias_campo');
        oFormato = oColumnas.find('formato');
        
        var suffix =  "_" + $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad+ "_"+ $.fn.appgrid.options.pk; // + $.fn.appgrid.options.datestamp;
        $("#grid"+suffix).attr("requeriesFilter",$.fn.appgrid.options.requeriesFilter);
        $("#grid_complete_code_" + suffix).val($(xml).find('configuracion_grid').find('evento_grid').text());
        //var suffix = "-" + sDateTime(new Date());
        $("#grid"+ suffix).attr("frecuencia_actualizacion", $.fn.appgrid.options.frecuenciaActualizacion);

        oAlias.each( function() {
             
            var sParent=$(this).parent()[0].tagName;
            if (sParent=='column_definition') return true;
            oColumna=null;
            if ($(this).parent().attr("tipo_dato")=="money" || oFormato[iCol].textContent.indexOf("$")>-1) {
                oColumna={
                    name:sParent+suffix,
                    index:sParent+suffix,
                    width:oTamano[iCol].attributes.length==0?$(oTamano[iCol]).text():$(oTamano[iCol+1]).text(),
                    sortable:true,
                    formatter: "currency",
                    formatoptions: {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 4, prefix:"$ ", suffix:""},
                    summaryType: "sum"
                };                                
            } else if ($(this).parent().attr("tipo_dato")=="float" || oFormato[iCol].textContent.indexOf("$")>-1) {
                oColumna={
                    name:sParent+suffix,
                    index:sParent+suffix,
                    width:oTamano[iCol].attributes.length==0?$(oTamano[iCol]).text():$(oTamano[iCol+1]).text(),
                    sortable:true,
                    formatter: "currency",
                    formatoptions: {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, prefix:"", suffix:""},
                    summaryType: "sum"
                };                                
            } else if ($(this).parent().attr("tipo_dato")=="decimal" && sParent.indexOf("clave")!=0) {
                oColumna={
                    name:sParent+suffix,
                    index:sParent+suffix,
                    width:oTamano[iCol].attributes.length==0?$(oTamano[iCol]).text():$(oTamano[iCol+1]).text(),
                    sortable:true,
                    formatter: "integer",
                    formatoptions: {thousandsSeparator: ","},
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
            
            if ($.fn.appgrid.options.claveTipoGrid==2 && iCol==0 ) {
                oColumna.hidden=true;
                oColumna.key=true;
            }

            $.fn.appgrid.options.colNames[iCol]=$(this).text();
            $.fn.appgrid.options.colModel[iCol]=oColumna;
            $.fn.appgrid.options.groupFields[iCol]=$(this).text();
            iCol++;
        });
    
        $("#pager"+ suffix).attr("security", sPermiso);

        if ($.fn.appgrid.options.error!="") {       
            obj.html("<div class='ui-widget'>"+
                "<br><br><br><br><br><br><br><br><br><br><br><div style='padding: 0 .7em; width: 80%; margin-left: auto; margin-right: auto;text-align: center;' class='ui-state-error ui-corner-all'>"+
                "<p class='app_error'><span style='float: left; margin-right: .3em;' class='ui-icon ui-icon-alert'></span>"+
                $.fn.appgrid.options.error+"</p>"+
                "</div></div>"+obj.html());
                   
            //Remueve del dom el mensaje de espera
            $("#loader_"+ $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad + "_" + $.fn.appgrid.options.pk /*+ $.fn.appgrid.options.datestamp*/).html("");
                    
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
                        titulo: sTitulo,
                        columnas:1,
                        pk:nQuery,
                        filtroForaneo:"2=clave_aplicacion=1&3="+obj.attr("wsParameters"),
                        height:"90%",
                        width:"90%",
                        originatingObject: obj.id,
                        updateControl:obj.id,
                        secondFieldText: oGrid.getCell(nRow,1)
                    });
                }
                        
                if (sCmd=="lnkReloadGrid") {
                    //Borra el error y coloca el mensaje de espera"
                    obj.find(".ui-widget").remove();
                    $("#loader_"+ $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad + "_" + $.fn.appgrid.options.pk).html("<br/><br/><br/><br/><br/><br/><br /><br/><br/><br/><br/><br/><br/><br />Cargando informaci&oacute;n... <br><img src='img/loading.gif' /><br /><br />");
                    $.fn.appgrid.options.error="";
                    $.fn.appgrid.getGridDefinition();
                /*setTimeout("$('.queued_grids:first').gridqueue()",500);*/
                }
                        
            });
            
            $("#_status_").val("");
            return true;
        }

        var suffix =  "_" + $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad + "_" + $.fn.appgrid.options.pk; /*+ $.fn.appgrid.options.datestamp;*/

        /* Inicia implementación del grid */
        var nApp=$.fn.appgrid.options.app;
        var nEntidad=$.fn.appgrid.options.entidad.split('-')[0];

        /* Agrega la liga para quitar filtro desde el contructor    */
        if ($.fn.appgrid.options.wsParameters!="" && $.fn.appgrid.options.showFilterLink)
            $.fn.appgrid.options.titulo+="&nbsp;&nbsp;&nbsp;<a href='#' id='lnkRemoveFilter_grid" + suffix+"'>(Quitar filtro) [" + $.fn.appgrid.options.wsParameters + "])</a>";

        if ($("#grid"+suffix).attr("requeriesFilter")==0 ) //Si no está configurado para filtrar registros, trae el cuerpo
            xmlURL=$.fn.appgrid.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$w=" + $.fn.appgrid.options.wsParameters;
        else if ($("#lnkRemoveFilter_grid"+suffix).length==0)  //Si está configurado y no tiene un filtro solo trae la cabecera
            xmlURL=$.fn.appgrid.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$w=" + $.fn.appgrid.options.wsParameters;
        else
            xmlURL=$.fn.appgrid.options.xmlUrl + "?$cmd=grid&$cf="+ nEntidad + "&$ta=select&$dp=body&$w=" + $.fn.appgrid.options.wsParameters;
        
        if ($.fn.appgrid.options.claveTipoGrid=="2") {
            xmlURL= xmlURL.replace("control?$cmd=grid","control?$cmd=tree");
        }
        
        if ($("#divgrid_"+ $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad + "_" + $.fn.appgrid.options.pk).length>0)
           $("#divgrid_"+ $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad + "_" + $.fn.appgrid.options.pk).attr("original_url",xmlURL)
        else 
           $("#formGrid_"+ $.fn.appgrid.options.app + "_" + $.fn.appgrid.options.entidad).attr("original_url",xmlURL)
       
        $("#_status_").val("Cargando datos del cat\u00e1logo");
        
        //Se reemplaza el contenido del toppager_right con
        // el combo para agrupar
        var sOptions="<option value='clear'>Quitar agrupamiento</option>";
        var i=0; 
        $.fn.appgrid.options.agrupar=false;
        $(xml).find("column_definition").children().each(function(){
                if ($(this).find("usado_para_agrupar").text()=="1")  {
                  sOptions+="<option value='" + $.fn.appgrid.options.colModel[i].name + "'>" + $(this).find("alias_campo").text() + "</option>";
                  $.fn.appgrid.options.agrupar=true;
                }
                i++;
            }
        );
        
         oGridConfig= {
            url:xmlURL,
            datatype: "xml",
            colNames:$.fn.appgrid.options.colNames,
            colModel:$.fn.appgrid.options.colModel,
            rowNum:100,
            /*autowidth: true,*/
            width:$.fn.appgrid.options.width,
            shrinkToFit: false,
            height:$.fn.appgrid.options.height,
            rowList:[100,200,300],
            pager: jQuery('#pager' + suffix),
            toppager:true,
            sortname: $.fn.appgrid.options.colModel[0],//$.fn.appgrid.options.sortname,//+suffix,
            viewrecords: true,
            sortorder: "desc",
            multiselect: $.fn.appgrid.options.multiselect,
            //loadonce: true,
            caption:$.fn.appgrid.options.titulo,
            
            ondblClickRow: function(rowid) {
                    
                    if ($("#_cp_").val()==10) {
                        return false;
                    }
                    
                    nRow=rowid;
                    if (nRow) {
                        nPK= $(this).getCell(nRow,0);
                        $.fn.appgrid.options.secondFieldText= $(this).getCell(nRow,1);
                        
                        if (nPK.indexOf("checkbox")>-1) {
                             nPK= $(this).getCell(nRow,1);
                            $.fn.appgrid.options.secondFieldText= $(this).getCell(nRow,2);
                        }
                        
                        nEditingApp=$(this).attr("editingApp");
                                
                        //Verifica si se actualiza árbol
                        sUpdateControl="";
                        if ($(this).attr("updateTreeAfterPost")=="true")
                            sUpdateControl=oGrid.attr("originatingObject");

                        $("#divwait")
                        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Por favor espere, construcci\u00f3n de interfaz en progreso...</p>")
                        .attr('title','Espere un momento por favor') 
                        .dialog({
                            height: 140,
                            modal: true,
                            autoOpen: true,
                            closeOnEscape:false
                        });
                        
                        //Copia al cache la app que se está usando al editar
                        $("#_cache_").val($(this).attr("editingApp"));
                
                        $("#top").form({
                            app: nApp,
                            forma:nEntidad,
                            datestamp:$(this).attr("datestamp"),
                            modo:"update",
                            titulo: $.fn.appgrid.options.secondFieldText,
                            columnas:1,
                            pk:nPK,
                            filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3="+$(this).attr("wsParameters"),
                            height:"90%",
                            width:"80%",
                            originatingObject: $(this).id,
                            showRelationships:$(this).attr("callFormWithRelationships"),
                            updateControl:sUpdateControl,
                            secondFieldText: oGrid.getCell(nRow,1)
                        });
                    }
                    else {
                        alert('Seleccione el registro a editar');
                    }
                
                    return false;
            },            
            gridComplete:function(){
                
                /*function filterCells(index) {
                   return $(this).text().replace("$","").replace(",","")<0;
                }*/
                
                if ($("#grid"+ suffix).attr("frecuencia_actualizacion")!="" && $("#grid"+ suffix).attr("frecuencia_actualizacion")!="0") {
                   //Verifica si ya existe el timer asociado al grid
                   timerActivo=false;
                   for (e=0; e<jsonConfig.gridTimers.length; e++) {
                       if (jsonConfig.gridTimers[e].gridSuffix==suffix) {
                           jsonConfig.gridTimers[e].timerID = setTimeout("$('#grid"+ suffix+"').trigger('reloadGrid')",$("#grid"+ suffix).attr("frecuencia_actualizacion"));  
                           timerActivo=true;
                       }
                   }
                   
                   if (!timerActivo) 
                       jsonConfig.gridTimers.push({gridSuffix:suffix,timerID:setTimeout("$('#grid"+ suffix+"').trigger('reloadGrid')",$("#grid"+ suffix).attr("frecuencia_actualizacion"))});                   
                }
                
                //Pasa negativos a color rojo
                //$("#grid" + suffix + " tbody tr td[aria-describedby='grid" + suffix + "_Freight']").filter(filterCells).parent("tr").addClass("rowchanged");
                //$("#grid" + suffix + " tbody tr td").filter(filterCells).parent("tr").addClass("negativo");
   
               if ($("#grid_complete_code_" + suffix).val()!="" && $("#grid_complete_code_" + suffix).val()!=null && $("#grid_complete_code_" + suffix).val()!="null") {
                   $.globalEval($("#grid_complete_code_" + suffix).val());
               }
               
                $("#_status_").val("");
              
            },loadError : function(xhr,st,err) { 
                $("#grid_" + suffix + "_toppager_right").children(0).html("Error cargar catálogo:" + xhr.status + " "+xhr.statusText);
            }
        };
        
        if ($.fn.appgrid.options.agrupar && $.fn.appgrid.options.claveTipoGrid=="1") {
            oGridConfig.grouping=false;
            oGridConfig.groupingView = {
                groupField : $.fn.appgrid.options.groupFields[0],
                            groupColumnShow : [true],
                            groupText : ['<b>{0}</b>'],
                            groupCollapse : false,
                            groupSummary : [true],
                            groupOrder: ['asc']/*,
                            groupDataSorted : true*/
                            };
            oGridConfig.footerrow=true;
            oGridConfig.userDataOnFooter=true;
        }
        
        if ($.fn.appgrid.options.claveTipoGrid=="2") {
            oGridConfig.treeGrid=true;
            oGridConfig.treeGridModel = 'adjacency';
            oGridConfig.ExpandColumn=$.fn.appgrid.options.colModel[1].name;
            oGridConfig.mtype="POST";
        }
        
        var oGrid=$("#grid" + suffix).jqGrid(oGridConfig);

        //Verifica si ya existen los botones para
        //evitar duplicarlos
        
        $("#_status_").val("Estableciendo eventos al cat\u00e1logo " + $.fn.appgrid.options.titulo);

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
                                
                    //Copia al cache la app que se está usando al editar
                    $("#_cache_").val($(this).attr("editingApp"));     
                    
                    $("body").form({
                        app: nApp,
                        forma:nEntidad,
                        datestamp:$(this).attr("datestamp"),
                        modo:"insert",
                        titulo: $.fn.appgrid.options.secondFieldText,
                        columnas:1,
                        pk:0,
                        filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3="+$(this).attr("wsParameters"),
                        height:"90%",
                        width:"80%",
                        originatingObject:oGrid.id,
                        showRelationships:$(this).attr("callFormWithRelationships"),
                        updateControl:sUpdateControl,
                        secondFieldText: "" //Puesto que se trata de un registro nuevo, 
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
                        nPK= $(this).getCell(nRow,0).trim();
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
                        
                        //Copia al cache la app que se está usando al editar
                        $("#_cache_").val($(this).attr("editingApp"));
                        
                        $("body").form({
                            app: nApp,
                            forma:nEntidad,
                            datestamp:$(this).attr("datestamp"),
                            modo:"update",
                            titulo: $.fn.appgrid.options.secondFieldText,
                            columnas:1,
                            pk:nPK,
                            filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3="+$(this).attr("wsParameters"),
                            height:"500",
                            width:"80%",
                            originatingObject: $(this).id,
                            showRelationships:$(this).attr("callFormWithRelationships"),
                            updateControl:sUpdateControl,
                            secondFieldText: oGrid.getCell($(this).getGridParam('selrow'),1)
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
                        if (confirm("\xBFEst\u00e1 seguro que desea eliminar el registro? No es posible deshacer esta acci\u00f3n.")){
                            $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Eliminando registro...");
                            $.ajax(
                            {
                                url: "control?$cmd=register&$ta=delete&$cf="+ nEntidad + "&$pk="+ nPK + "&w=" + nEntidad,
                                dataType: "text",
                                success:  function(data){
                                    oGrid.jqGrid('delRowData',nRow);
                                    //Actualiza árbol
                                    if ($.fn.appgrid.options.updateTreeAfterPost) {
                                        sTvId=oGrid.attr("originatingObject");
                                        $("#"+sTvId).treeMenu.getTreeDefinition($("#"+sTvId));
                                    }
                                },
                                error:function(xhr,err){
                                    if (xhr.responseText.indexOf("Iniciar sesi&oacute;n")>-1) {
                                        alert("Su sesi\u00f3n ha expirado, por seguridad es necesario volverse a registrar");
                                        window.location='login.jsp';
                                    }   
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

        if (sP.indexOf("6")>-1) {
            oGrid.navGrid('#grid'+ suffix+'_toppager',{
                edit:false,
                add:false,
                del:false,
                search:false
            })
            .navSeparatorAdd('#grid'+ suffix+'_toppager',{
                sepclass : "ui-separator",
                sepcontent: ""
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
                
                $("#grid"+suffix+"_toppager_right").children(0).html("<img src='img/throbber.gif'>&nbsp;Generando forma...");

                $("#divwait")
                .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                .attr('title','Espere un momento por favor') 
                .dialog({
                    height: 140,
                    modal: true,
                    autoOpen: true,
                    closeOnEscape:false
                });

                //Copia al cache la app que se está usando al editar
                $("#_cache_").val($(this).attr("editingApp"));
                
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
        }).navSeparatorAdd('#grid'+ suffix+'_toppager',{
                sepclass : "ui-separator",
                sepcontent: ""
        }).navButtonAdd('#grid'+ suffix+'_toppager',{
                caption:"",
                buttonicon:"ui-icon-calculator",
                onClickButton:  function() {
                    nApp=this.id.split("_")[1];
                    nForma=this.id.split("_")[2];
                    sUrl=$("#grid"+suffix).jqGrid('getGridParam', 'url');
                    
                    $("body").append("<div id='dialog-confirm' title='\u00BFProblemas al descargar este excel?'>" +
                                     "<p style='font-size:12px'><span class='ui-icon ui-icon-info' style='float:left; margin:0 7px 20px 0;'></span>Si experimenta problemas al descargar el archivo de excel seleccione la opci&oacute;n 'Descarga programada'</p></div>");
                    $("#dialog-confirm").dialog({
                            resizable: false,
                            height:160,
                            width:450,
                            modal: true,
                            buttons: {
                              "Descarga programada": function() {
                                $.post( sUrl.replace("cmd=grid","cmd=scheduled_download").replace("ta=select","ta=excel&$file="+$.fn.appgrid.options.titulo) );
                                alert("Su descarga ya ha sido programada, recibir\u00e1 una notificaci\u00f3n con la liga del archivo a su correo cuando est\u00e9  lista");
                                $( this ).dialog( "close" );
                                $("#dialog-confirm").remove();
                              },
                              "Descargar ahora": function() {
                                if ($("#_cp_").val() != "10") {
                                    window.open(sUrl.replace("cmd=grid","cmd=download").replace("ta=select","ta=excel"), "_blank");
                                } else {
                                    alert("Su perfil no cuenta con permisos para exportar catálogos");
                                }                                   
                                $( this ).dialog( "close" );
                                $("#dialog-confirm").remove();
                              }
                            }
                    });

                    return false;
                },
                position: "last",
                title:"Descargar cat\u00e1logo en formato XLS",
                cursor: "pointer"
       });
                
        oGrid.navGrid('#grid'+ suffix+'_toppager',{
            edit:false,
            add:false,
            del:false,
            search:false
        })
        .navSeparatorAdd('#grid'+ suffix+'_toppager',{
            sepclass : "ui-separator",
            sepcontent: ""
        });

        //Aparece un botón para editar la forma si se cuenta con el perfil de admnistrador
        if ($("#_cp_").val()=="1") {
            oGrid.navGrid('#grid'+ suffix+'_toppager',{
                edit:false,
                add:false,
                del:false,
                search:false
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

                        //Copia al cache la app que se está usando al editar
                        $("#_cache_").val($(this).attr("editingApp"));
                                            
                        $("body").form({
                            app: $(this).attr("editingApp"),
                            forma:3,
                            datestamp:$(this).attr("datestamp"),
                            modo:"update",
                            titulo: $.fn.appgrid.options.titulo, 
                            columnas:1,
                            pk:nPK,
                            filtroForaneo:"2=clave_aplicacion=" + nEditingApp + "&3=clave_aplicacion="+ $.fn.appgrid.options.app + "&4=clave_aplicacion=" + $.fn.appgrid.options.app,
                            height:"500",
                            width:"80%",
                            originatingObject: $(this).id,
                            showRelationships:$(this).attr("callFormWithRelationships"),
                            updateControl:sUpdateControl,
                            secondFieldText: $.fn.appgrid.options.titulo
                        });
                
                },
                position: "last",
                title:"Editar forma",
                cursor: "pointer"
            });
        }
        
        //Remueve del dom el loader
        $("#loader"+ suffix).remove();

        //Establece la función para la liga lnkRemoveFilter_grid_ que remueve el filtro del grid
        if ($.fn.appgrid.options.wsParameters!="" && $.fn.appgrid.options.showFilterLink) {
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
                        url:"control?$cmd=grid&$cf=" + nForma + "&$ta=select&$dp=body"
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

        if ($.fn.appgrid.options.agrupar)
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
        if ($.fn.appgrid.options.inQueue)
            setTimeout("$('.queued_grids:first').gridqueue({height: $('#_gq_').val()+'%'})",3000);

        if ($.fn.appgrid.options.removeGridTitle)
            $('.ui-jqgrid-titlebar',oGrid).remove();
        
        
    /* Finaliza implementación de grid */

    }
})(jQuery);