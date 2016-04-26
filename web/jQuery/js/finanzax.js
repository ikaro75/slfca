function fx_transaction_init() {
    
    //Se sale en caso de que la forma esté en modo de búsqueda
    if ($("#formTab_29_264_0").attr("modo")=="lookup")
        return;   
    
    claveTransaccion="";
    clavePoliza="";
    /* Cambio de look al campo cuenta */
    /*
    $("#clave_cuenta_origen").attr("style","width:90%");
    $("#importe").attr("style","width:70%");
    $("#clave_moneda").attr("style","width:60%");
    $("#tipo_cambio").attr("style","width:50%"); 
    $("#td_clave_cuenta_origen").next().attr("style","padding: 0px;");
    sHTML="<table style='width:100%; -webkit-border-horizontal-spacing: 0px; border-width: 0px; margin: 0px auto; padding: 0px;'><tr><td style='width:50%; padding: 0px; margin: 0px auto;'>" + $("#td_clave_cuenta_origen").next().html() + "</td><td style='width:15%; padding: 0px; margin: 0px auto;'> " + $("#td_importe").next().html() + "</td><td style='width:10%; padding: 0px; margin: 0px auto;'>" + $("#td_clave_moneda").next().html() + "</td><td style='width:10%; padding: 0px; margin: 0px auto;'>" + $("#td_tipo_cambio").next().html() + "</td></tr></table>";
    $("#td_clave_cuenta_origen").next().html(sHTML);
    $("#clave_cuenta_origen").next().show();
    $("#td_importe").parent().remove();
    $("#td_clave_moneda").parent().remove();
    $("#td_tipo_cambio").parent().remove();
    $("#msgvalida_clave_cuenta_origen").hide();
    
    // Cambio de look al campo categoría
    $("#clave_cuenta_destino").attr("style","width:90%");
    sHTML="<table style='width:100%; -webkit-border-horizontal-spacing: 0px; border-width: 0px; margin: 0px auto; padding: 0px;'><tr><td style='width:63%; padding: 0px; margin: 0px auto;'>" + $("#td_clave_cuenta_destino").next().html() + "</td><td style='width:15%; padding: 0px; margin: 0px auto;'> " + $("#importe").parent().html() + "</td><td style='width:10%; padding: 0px; margin: 0px auto;'><input type='button' id='dividirTransaccion' value='Dividir transacción' /></td></tr></table>";
    $("#td_clave_cuenta_destino").next().html(sHTML);
    
    
    $("#clave_cuenta_origen").next().show();
   */
   
    if ($(xml).find("clave_poliza").length>0) {
        clavePoliza=$(xml).find("clave_poliza")[0].firstChild.data;
        
        if (clavePoliza =="") {
            clavePoliza =0;
        }
    } else {
        clavePoliza = 0;
    }
        
    clave_tipo_transaccion=$(xml).find("clave_tipo_transaccion")[0].firstChild.data;
    importe=$(xml).find("importe")[0].firstChild.data.replace("$","").replace(",","");
    
    if ($(xml).find("dividir_transaccion").length>0) {
        tieneSubtransacciones=$(xml).find("dividir_transaccion")[0].firstChild.data==""?0:$(xml).find("dividir_transaccion")[0].firstChild.data;
    } else {
        tieneSubtransacciones=0;
    }
    
    $("#importe").val(formatCurrency(Math.abs(importe)));
    $("#td_dividir_transaccion").html("").next().html("<table style='width:90%; margin: 0px auto; padding: 0px; border: 0px;'><tr><td><input type='hidden' value='" + tieneSubtransacciones + "' id='dividir_transaccion' name='dividir_transaccion'/><input type='button' id='dividirTransaccion' value='Dividir transacci\u00f3n' /></td></tr></table>");
    
    if (clave_tipo_transaccion=="") {
    } else if (clave_tipo_transaccion==1) {
        //Si no se tienen subtransacciones
        if (tieneSubtransacciones==0) {
            $("#td_dividir_transaccion").parent().show();
            //$("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
            $("#td_clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)"); 
            //setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"");
            //setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
            $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data+"]").attr("selected",true);
            $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data +"]").attr("selected",true); 
        } else {
            $("#tiene_subtransacciones").val("1");
            $("#clave_cuenta_destino").html("");
            $("#td_clave_cuenta_destino").next().html("<div id='divGrid_9_251_"+ clavePoliza +"' class='gridContainer' style='width:95%; margin: 0px;'></div>");
            $('#divGrid_9_251_'+ clavePoliza).appgrid({
                app: '9',
                entidad: '251',
                pk:'0',
                editingApp: '1',
                wsParameters:'cargo=0 AND clave_poliza=' + clavePoliza + ' or (fx_poliza_detalle.clave_cuenta= fx_cuenta.clave_cuenta and cargo=0 AND clave_poliza=-1)',
                height:'65%',
                openKardex:false,
                insertInDesktopEnabled:'0',
                showFilterLink: false,
                originatingObject:'',
                getLog:false,
                getFilters:false,
                gridComplete: '$.ajax( ' +
                '{'+
                'url: "control?$cmd=plain&$ta=select&$cf=253&$w=clave_poliza in (-1,'+clavePoliza+')",' +
                'dataType: ($.browser.msie) ? "text" : "xml",'+
                'success:  function(data) {'+
                '	if (typeof data == "string") {'+
                '		xmlTotal = new ActiveXObject("Microsoft.XMLDOM");'+
                '		xmlTotal.async = false;'+
                '		xmlTotal.validateOnParse="true";'+
                '		xmlTotal.loadXML(data);'+
                '		if (xmlTotal.parseError.errorCode>0) {'+
                '			alert("Error de compilación xml total de subtransacciones");'+
                '		}'+
                '	}'+
                '	else {'+
                '		xmlTotal= data;'+
                '	}'+
                '	$("#form_29_264_' + clavePoliza +' #importe").attr("readonly", true); '+
                '	$("#form_29_264_' + clavePoliza +' #importe").val($(xmlTotal).find("importe").text());'+
                '	  }, '+
                'error:function(xhr,err){'+
                '	alert("Problemas al recuperar importe de subtransacciones");'+
                '}' +
                '});'  
            });    
        }
    }
    else if (clave_tipo_transaccion==2) {
        
        if (tieneSubtransacciones==0) {
            $("#td_dividir_transaccion").parent().show();
            //$("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
            $("#td_clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)"); 
            //setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"");
            setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
            //$("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
            $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data +"]").attr("selected",true); 
        } else {
            $("#tiene_subtransacciones").val("1");
            $("#clave_cuenta_destino").html("");
            $("#td_clave_cuenta_destino").next().html("<div id='divGrid_9_251_"+ clavePoliza +"' class='gridContainer' style='width:95%; margin: 0px;'></div>");
            $('#divGrid_9_251_'+ clavePoliza).appgrid({
                app: '9',
                entidad: '251',
                pk:'0',
                editingApp: '1',
                wsParameters:'((clave_poliza=' + clavePoliza + ' AND cargo=1) OR (clave_poliza=-' + $('#_ce_').val() + '))',
                height:'65%',
                openKardex:false,
                insertInDesktopEnabled:'0',
                showFilterLink: false,
                originatingObject:'',
                getLog:false,
                getFilters:false,
                gridComplete: '$.ajax( ' +
                '{'+
                'url: "control?$cmd=plain&$ta=select&$cf=253&$w=((clave_poliza=' + clavePoliza + ' AND cargo=1) or (clave_poliza=-1))",' +
                'dataType: ($.browser.msie) ? "text" : "xml",'+
                'success:  function(data) {'+
                '	if (typeof data == "string") {'+
                '		xmlTotal = new ActiveXObject("Microsoft.XMLDOM");'+
                '		xmlTotal.async = false;'+
                '		xmlTotal.validateOnParse="true";'+
                '		xmlTotal.loadXML(data);'+
                '		if (xmlTotal.parseError.errorCode>0) {'+
                '			alert("Error de compilación xml total de subtransacciones");'+
                '		}'+
                '	}'+
                '	else {'+
                '		xmlTotal= data;'+
                '	}'+
                '	$("#form_29_264_' + clavePoliza +' #importe").attr("readonly", true); '+
                '	$("#form_29_264_' + clavePoliza +' #importe").val(formatCurrency($(xmlTotal).find("importe").text()));'+
                '	  }, '+
                'error:function(xhr,err){'+
                '	alert("Problemas al recuperar importe de subtransacciones");'+
                '}' +
                '});'  
            });            
        }
    } else if (clave_tipo_transaccion==3) {
        $("#td_dividir_transaccion").parent().hide();
        $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
        $("#td_clave_cuenta_origen").next().html('<select name="clave_cuenta_origen" id="clave_cuenta_origen" class="inputWidgeted2 obligatorio" tabindex="7" tipo_dato="int"><option selected="selected"></option><option value="1">Efectivo</option></select>');
        $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"");
        setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"");
        $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
        $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);        
    } else if ( clave_tipo_transaccion==4) {
        $("#td_dividir_transaccion").parent().hide();
        $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta in (1,3)");
        $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"clave_tipo_cuenta=2");
        $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
        $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","")+"]").attr("selected",true);
    } else if (clave_tipo_transaccion==5) {
        $("#td_dividir_transaccion").parent().show();
        $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta=3");
        $("#td_clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)"); 
        setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
        $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
        $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);         
    } else if (clave_tipo_transaccion==6) {
        $("#td_dividir_transaccion").parent().show();
        $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta=2");
        $("#td_clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)"); 
        setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
        $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
        $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);                 
    } else if (clave_tipo_transaccion==7) {
        $("#td_dividir_transaccion").parent().show();
        $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta=1");
        $("#td_clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)"); 
        setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
        $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
        $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);                 
    } else if (clave_tipo_transaccion==8) {
        $("#td_dividir_transaccion").parent().show();
        $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta in (1,3)");
        $("#td_clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)"); 
        setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
        $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
        $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);                 
    }  else if (clave_tipo_transaccion==9) {
        //Cómo todavia no hay varo se trata de una transferencia entre activos 
        $("#td_dividir_transaccion").parent().hide();
        $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
        $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_cuenta = 38");
        setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"clave_cuenta = 7 ");
        $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
        $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);        
        
    } else if (clave_tipo_transaccion==10) {    
        $("#td_dividir_transaccion").parent().hide();
        $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
        $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta in (1,2,3)");
        setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"clave_cuenta in (38)");
        
        $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
        $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);        
        
    } else if (clave_tipo_transaccion==11) {    
        $("#td_dividir_transaccion").parent().hide();
        $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
        $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)");
        setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_cuenta in (7)");
        setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"clave_tipo_cuenta in (1,2,3)");
        
        $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
        $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);        
        
    } else if (clave_tipo_transaccion==12) {
        if (tieneSubtransacciones==0) {
            $("#td_dividir_transaccion").parent().show();
            setXMLInSelect3("clave_cuenta_origen",247,"foreign",null," OR clave_tipo_cuenta IN (4) AND clave_cuenta=7");
            //$("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
            $("#clave_cuenta_origen").html("Categoria (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)"); 
            setXMLInSelect3("clave_cuenta_destino",242,"foreign",null," OR clave_tipo_cuenta IN (6) AND clave_cuenta=39");

            $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
            $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data +"]").attr("selected",true); 
        } else {
            $("#tiene_subtransacciones").val("1");
            setXMLInSelect3("clave_cuenta_origen",247,"foreign",null,"clave_cuenta=39");
            $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);            
            $("#clave_cuenta_destino").html("");
            $("#td_clave_cuenta_destino").next().html("<div id='divGrid_9_251_"+ clavePoliza +"' class='gridContainer' style='width:95%; margin: 0px;'></div>");
            $('#divGrid_9_251_'+ clavePoliza).appgrid({
                app: '9',
                entidad: '251',
                pk:'0',
                editingApp: '1',
                wsParameters:'((clave_poliza=' + clavePoliza + ' AND cargo=1) OR (clave_poliza=-' + $('#_ce_').val() + '))',
                height:'65%',
                openKardex:false,
                insertInDesktopEnabled:'0',
                showFilterLink: false,
                originatingObject:'',
                getLog:false,
                getFilters:false,
                gridComplete: '$.ajax( ' +
                '{'+
                'url: "control?$cmd=plain&$ta=select&$cf=253&$w=((clave_poliza=' + clavePoliza + ' AND cargo=1) or (clave_poliza=-1))",' +
                'dataType: ($.browser.msie) ? "text" : "xml",'+
                'success:  function(data) {'+
                '	if (typeof data == "string") {'+
                '		xmlTotal = new ActiveXObject("Microsoft.XMLDOM");'+
                '		xmlTotal.async = false;'+
                '		xmlTotal.validateOnParse="true";'+
                '		xmlTotal.loadXML(data);'+
                '		if (xmlTotal.parseError.errorCode>0) {'+
                '			alert("Error de compilación xml total de subtransacciones");'+
                '		}'+
                '	}'+
                '	else {'+
                '		xmlTotal= data;'+
                '	}'+
                '	$("#form_29_264_' + clavePoliza +' #importe").attr("readonly", true); '+
                '	$("#form_29_264_' + clavePoliza +' #importe").val(formatCurrency($(xmlTotal).find("importe").text()));'+
                '	  }, '+
                'error:function(xhr,err){'+
                '	alert("Problemas al recuperar importe de subtransacciones");'+
                '}' +
                '});'  
            });            
        }    
    }

    /* Se activan los controles de acuerdo a la transacción abierta para edición */

    $("#dividirTransaccion").unbind("click");
    $("#dividirTransaccion").button().click (function() { 
        $("#dividir_transaccion").val("1");
        $("#clave_cuenta_destino").hide();
        $("#clave_cuenta_destino").removeClass("obligatorio");
        $("#td_clave_cuenta_destino").next().html("<div id='divGrid_9_251_"+clavePoliza+"' class='gridContainer' style='width:95%; margin: 0px;'></div>");
        $('#divGrid_9_251_'+clavePoliza).appgrid({
            app: '9',
            entidad: '251',
            pk:'0',
            editingApp: '1',
            wsParameters:'(clave_poliza=-'+$('#_ce_').val() + ((clavePoliza!='')?' OR (clave_poliza='+clavePoliza+  ' AND cargo=1':'')+')',
            height:'65%',
            openKardex:false,
            insertInDesktopEnabled:'0',
            showFilterLink: false,
            originatingObject:'',
            getLog:false,
            getFilters:false,
            gridComplete: '$.ajax( ' +
            '{'+ 
            'url: "control?$cmd=plain&$ta=select&$cf=253&$w=' + (clavePoliza!=''?' clave_poliza=' + clavePoliza + ' OR ':'') + ' clave_poliza=-' + $("#_ce_").val() + '",' +
            'dataType: ($.browser.msie) ? "text" : "xml",'+
            'success:  function(data) {'+
            '	if (typeof data == "string") {'+
            '		xmlTotal = new ActiveXObject("Microsoft.XMLDOM");'+
            '		xmlTotal.async = false;'+
            '		xmlTotal.validateOnParse="true";'+
            '		xmlTotal.loadXML(data);'+
            '		if (xmlTotal.parseError.errorCode>0) {'+
            '			alert("Error de compilación xml total de subtransacciones");'+
            '		}'+
            '	}'+
            '	else {'+
            '		xmlTotal= data;'+
            '	}'+
            '	$("#form_29_264_0 #importe").attr("readonly", true); '+
            '	$("#form_29_264_0 #importe").val(formatCurrency($(xmlTotal).find("importe").text()));'+
            '	  }, '+
            'error:function(xhr,err){'+
            '	alert("Problemas al recuperar importe de subtransacciones");'+
            '}' +
            '});'  
        });
 
    });

    $("#clave_tipo_transaccion").unbind("change");
    $("#clave_tipo_transaccion").change ( function() {
        /* depósito (1), 
         * retiro (2), 
         * transferencia (3),
         * retiro por cajero automático (4),
         * compra con tarjeta (5), 
         * compra con efectivo (6), 
         * depósito con cheque (7), 
         * compra en línea (8) */     
        if ($("#clave_tipo_transaccion").val()==1)  {
            $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            $("#clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"");
            setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");             
        } else if ($("#clave_tipo_transaccion").val()==2) { 
            $("#td_dividir_transaccion").parent().show();
            $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            $("#clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"");
            setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
        } else if ($("#clave_tipo_transaccion").val()==3) {
            $("#td_dividir_transaccion").parent().hide();
            $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            $("#td_clave_cuenta_origen").next().html('<select name="clave_cuenta_origen" id="clave_cuenta_origen" class="inputWidgeted2 obligatorio" tabindex="7" tipo_dato="int"><option selected="selected"></option><option value="1">Efectivo</option></select>');
            $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"");
            setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"");
        } else if ( $("#clave_tipo_transaccion").val()==4) {
            $("#td_dividir_transaccion").parent().hide();
            $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta in (1,3)");
            $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"clave_tipo_cuenta=2");
        } else if ( $("#clave_tipo_transaccion").val()==5) {
            $("#td_dividir_transaccion").parent().show();
            $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta=3");
            $("td_#clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)"); 
            setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
        } else if ( $("#clave_tipo_transaccion").val()==6) {
            $("#td_dividir_transaccion").parent().show();
            $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta=2");
            $("#td_clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)"); 
            setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
        } else if ( $("#clave_tipo_transaccion").val()==7) {
            $("#td_dividir_transaccion").parent().show();
            $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta=1");
            $("#td_clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)"); 
            setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
        } else if ( $("#clave_tipo_transaccion").val()==8) {
            $("#td_dividir_transaccion").parent().show();
            $("#td_clave_cuenta_origen").html("Cuenta (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta in (1,3)");
            $("#td_clave_cuenta_destino").html("Categoria (<span id='msgvalida_clave_cuenta' style='display: none; '>Obligatorio</span>*)"); 
            setXMLInSelect3("clave_cuenta_destino",247,"foreign",null,"");
        } else if ($("#clave_tipo_transaccion").val()==9) {
            //Cómo todavia no hay varo se trata de una transferencia entre activos 
            $("#td_dividir_transaccion").parent().hide();
            $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
            $("#td_clave_cuenta_origen").next().html('<select name="clave_cuenta_origen" id="clave_cuenta_origen" class="inputWidgeted2 obligatorio" tabindex="7" tipo_dato="int"><option selected="selected"></option><option value="1">Efectivo</option></select>');
            $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_cuenta=38");
            setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"clave_cuenta=7");
            $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
            $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);        
        } else if ($("#clave_tipo_transaccion").val()==10) {    
            $("#td_dividir_transaccion").parent().hide();
            $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
            $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_tipo_cuenta in (1,2,3)");
            setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"clave_cuenta in (38)");

            $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
            $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);            
        } else if ($("#clave_tipo_transaccion").val()==11) {          
            $("#td_dividir_transaccion").parent().hide();
            $("#td_clave_cuenta_origen").html("De (<span id='msgvalida_clave_cuenta_origen' style='display: none; '>Obligatorio</span>*)");
            $("#td_clave_cuenta_destino").html("A (<span id='msgvalida_clave_cuenta_destino' style='display: none; '>Obligatorio</span>*)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_cuenta in (7)");
            setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"clave_tipo_cuenta in (1,2,3)");

            $("#clave_cuenta_origen option[value="+$(xml).find("clave_cuenta_origen")[0].firstChild.data+"]").attr("selected",true);
            $("#clave_cuenta_destino option[value="+$(xml).find("clave_cuenta_destino")[0].firstChild.data.replace("-","") +"]").attr("selected",true);        
        } else if ($("#clave_tipo_transaccion").val()==12) {   
            $("#td_dividir_transaccion").parent().hide();
            setXMLInSelect3("clave_cuenta_destino",242,"foreign",null,"clave_cuenta in (7)");
            setXMLInSelect3("clave_cuenta_origen",242,"foreign",null,"clave_cuenta in (39)");   
            $("#clave_cuenta_destino option[value=7]").attr("selected", "selected");
            $("#clave_cuenta_origen option[value=39]").attr("selected", "selected");
            
            $.ajax(
                {
                    url: "intereses.jsp?folio=" + $("#form_29_264_"+ clavePoliza + " #folio_pagare").val() + "&fecha=" + $("#form_29_264_"+ clavePoliza + " #fecha").val(),
                    dataType: ($.browser.msie) ? "text" : "xml",
                    success: function(data) {
                        if (typeof data == "string") {
                            xmlIntereses = new ActiveXObject("Microsoft.XMLDOM");
                            xmlIntereses.async = false;
                            xmlIntereses.validateOnParse = "true";
                            xmlIntereses.loadXML(data);
                            if (xmlIntereses.parseError.errorCode > 0) {
                                alert("Error de compilación xml:" + xmlCategoriaRecurso.parseError.errorCode + "\nParse reason:" + xmlCategoriaRecurso.parseError.reason + "\nLinea:" + xmlCategoriaRecurso.parseError.line);
                            }
                        }
                        else {
                            xmlIntereses = data;
                        }
                        
                        if ($(xmlIntereses).find("error").length>0) {
                            alert($(xmlIntereses).find("error").text());
                            return;
                        }

                        $("#form_29_264_"+ clavePoliza + " #importe").val(formatCurrency($(xmlIntereses).find("intereses").text()));

                    },
                    error: function(xhr, err) {
                        alert("Error al calcular intereses " + xhr.readyState + "\nstatus: " + xhr.status + "\responseText:" + xhr.responseText);
                    }
                });
        }
        
    /* Se activan los controles de acuerdo a la transacción abierta para edición */
    /*
        $("#dividirTransaccion").unbind("click");
        $("#dividirTransaccion").button().click (function() { 
            $("#dividir_transaccion").val("1");
            $("#clave_cuenta_destino").parent().hide();
            $("#clave_cuenta_destino").removeClass("obligatorio");
            $("#td_clave_cuenta_destino").next().html("<div id='divGrid_9_251_0' class='gridContainer' ></div>");
            $('#divGrid_9_251_0').appgrid({
                app: '9',
                entidad: '251',
                pk:'0',
                editingApp: '1',
                wsParameters:'clave_poliza=-'+$('#_ce_').val(),
                height:'70%',
                openKardex:false,
                insertInDesktopEnabled:'0',
                showFilterLink: false,
                originatingObject:'',
                getLog:false,
                getFilters:false,
                gridComplete: '$.ajax( ' +
                '{'+
                'url: "control?$cmd=plain&$ta=select&$cf=253&$w=clave_poliza=-'+$('#_ce_').val() +'",'+
                'dataType: ($.browser.msie) ? "text" : "xml",'+
                'success:  function(data) {'+
                '	if (typeof data == "string") {'+
                '		xmlTotal = new ActiveXObject("Microsoft.XMLDOM");'+
                '		xmlTotal.async = false;'+
                '		xmlTotal.validateOnParse="true";'+
                '		xmlTotal.loadXML(data);'+
                '		if (xmlTotal.parseError.errorCode>0) {'+
                '			alert("Error de compilación xml total de subtransacciones");'+
                '		}'+
                '	}'+
                '	else {'+
                '		xmlTotal= data;'+
                '	}'+
                '	$("#form_9_234_0 #importe").attr("readonly", true); '+
                '	$("#form_9_234_0 #importe").val($(xmlTotal).find("importe").text());'+
                '	  }, '+
                'error:function(xhr,err){'+
                '	alert("Problemas al recuperar importe de subtransacciones");'+
                '}' +
                '});'  
            })
 
        }); */
    }
    )
        
    
}

function fx_sub_transaction_init() {
    /* Si se trata de una edición */
    if ($(xml).find("clave_poliza").length>0) {
        clavePoliza=$(xml).find("clave_poliza")[0].firstChild.data.replace("$","").replace(",","");
        importe=$(xml).find("importe")[0].firstChild.data.replace("$","").replace(",","");
        $("#form_9_251_" + clavePoliza +" #importe").val(formatCurrency(Math.abs(importe)));
    }
}

function fx_categoria_init() {
    claveCategoria="";
    claveTipoPresupuesto="1";
    usarElMismoPresupuesto="0";
    
    if ($(xml).find("clave_cuenta").length>0) {
        claveCategoria=$(xml).find("clave_cuenta")[0].firstChild.data;
    }

    if ($(xml).find("clave_tipo_presupuesto").length>0) {
        claveTipoPresupuesto=$(xml).find("clave_tipo_presupuesto")[0].firstChild.data;
    }

    if ($(xml).find("usar_mismo_presupuesto_cada_mes").length>0) {
        usarElMismoPresupuesto=$(xml).find("usar_mismo_presupuesto_cada_mes")[0].firstChild.data;
    }

    if (claveTipoPresupuesto=="") {
        
        $("#td_usar_mismo_presupuesto_cada_mes").parent().hide();
        $("#td_presupuesto_mensual").next().removeClass("obligatorio").parent().hide();
        $("#td_pasar_presupuesto_excedente_al_siguiente_mes").parent().hide();   

        if ($("#formTab_12_247_"+claveCategoria+" li").length>=2){
            $($("#formTab_12_247_"+claveCategoria+" li")[1]).hide();
            $("#formTab_12_254").hide();
        }  
    }
    else if (claveTipoPresupuesto=="1" ){
        
        $("#td_usar_mismo_presupuesto_cada_mes").parent().show();
        
        if (usarElMismoPresupuesto=="0" || usarElMismoPresupuesto=="") 
            $("#td_presupuesto_mensual").next().removeClass("obligatorio").parent().hide();
        else    
            $("#td_presupuesto_mensual").next().addClass("obligatorio").parent().show();
        

        $("#td_pasar_presupuesto_excedente_al_siguiente_mes").parent().hide();
        
        if (claveCategoria=="") {
            $($("#formTab_12_247_0 li")[1]).hide();
            $("#formTab_12_254").hide();
        } else {
            $($("#formTab_12_247_"+claveCategoria+" li")[1]).hide();
            $("#formTab_12_254").hide();            
        }
            
    } else {

        if (usarElMismoPresupuesto=="0" || usarElMismoPresupuesto=="") 
            $("#td_presupuesto_mensual").parent().hide();
        else    
            $("#td_presupuesto_mensual").parent().show();
        
        $("#td_usar_mismo_presupuesto_cada_mes").parent().show();
        $("#td_pasar_presupuesto_excedente_al_siguiente_mes").parent().show();
    }
    
    $("#usar_mismo_presupuesto_cada_mes").click(function() {
        if ($(this)[0].checked) {
            $("#td_presupuesto_mensual").next().addClass("obligatorio").parent().show();
            if ($("#formTab_12_247_"+claveCategoria+" li").length>=2){
                $($("#formTab_12_247_"+claveCategoria+" li")[1]).hide();
                $("#formTab_12_254").hide();
            }                
        } else {
            $("#td_presupuesto_mensual").next().removeClass("obligatorio").parent().hide();
            if ($("#formTab_12_247_"+claveCategoria+" li").length>=2){
                $($("#formTab_12_247_"+claveCategoria+" li")[1]).show();
                $("#formTab_12_254").show();
            }              
        }
    });
    
    $("#clave_tipo_presupuesto").change(function() {
        if ($(this).val()==1) {
            $("#td_usar_mismo_presupuesto_cada_mes").parent().hide();
            $("#td_pasar_presupuesto_excedente_al_siguiente_mes").parent().hide();
            $($("#formTab_12_247_"+claveCategoria+" li")[1]).hide();
            $("#formTab_12_254").hide();
        } else {
            $("#td_usar_mismo_presupuesto_cada_mes").parent().show();
            $("#td_pasar_presupuesto_excedente_al_siguiente_mes").parent().show();
            if ($("#formTab_12_247_"+claveCategoria+" li").length>=2){
                $($("#formTab_12_247_"+claveCategoria+" li")[1]).show();
                $("#formTab_12_254").show();
            }    
        }
    });       
}

function fx_duplica_sub_transacciones(clavePoliza) {
    //Se deben duplicar las subtransacciones cuyo clave transaccion padre = clavePoliza
    //barriendo el grid haciendo posts de insert 
    $("#divwait")
    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Duplicando subtransacciones...</p>")
    .attr('title','Espere un momento por favor') 
    .dialog({
        height: 140,
        modal: true,
        autoOpen: true,
        closeOnEscape:false
    });


    $.ajax(
    {
        url: "control?$cmd=register&$ta=duplicate&$cf=251&$pk="+ clavePoliza,
        dataType: ($.browser.msie) ? "text" : "xml",
        success:  function(data){
            if (typeof data == "string") {
                xmlTransaccion = new ActiveXObject("Microsoft.XMLDOM");
                xmlTransaccion.async = false;
                xmlTransaccion.validateOnParse="true";
                xmlTransaccion.loadXML(data);
                if (xmlTransaccion.parseError.errorCode>0) {
                    alert("Error de compilación xml:" + xmlTransaccion.parseError.errorCode +"\nParse reason:" + xmlTransaccion.parseError.reason + "\nLinea:" + xmlTransaccion.parseError.line);
                }
            }
            else {
                xmlTransaccion = data;
            }        
            //Se deben recargar el grid de manera que puedan editarse estar subtransacciones
            error=$(xmlTransaccion).find("error");
            if (error.length>0) {
                $("#divwait").dialog( "close" )
                $("#divwait").dialog("destroy");             
                alert("No fue posible duplicar subtransacciones:" + error.text());
                return;
            }
        
            $("#clave_cuenta_destino").next().html("<div id='divGrid_9_251_0' class='gridContainer' style='width:95%; margin: 0px;'></div>");
            $('#divGrid_9_251_0').appgrid({
                app: '9',
                entidad: '251',
                pk:'0',
                editingApp: '1',
                wsParameters:'clave_poliza=-'+$('#_ce_').val(),
                height:'65%',
                openKardex:false,
                insertInDesktopEnabled:'0',
                showFilterLink: false,
                originatingObject:'',
                getLog:false,
                getFilters:false,
                gridComplete: '$.ajax( ' +
                '{'+
                'url: "control?$cmd=plain&$ta=select&$cf=253&$w=clave_poliza=-' + $('#_ce_').val()+ '",' +
                'dataType: ($.browser.msie) ? "text" : "xml",'+
                'success:  function(data) {'+
                '	if (typeof data == "string") {'+
                '		xmlTotal = new ActiveXObject("Microsoft.XMLDOM");'+
                '		xmlTotal.async = false;'+
                '		xmlTotal.validateOnParse="true";'+
                '		xmlTotal.loadXML(data);'+
                '		if (xmlTotal.parseError.errorCode>0) {'+
                '			alert("Error de compilación xml total de subtransacciones");'+
                '		}'+
                '	}'+
                '	else {'+
                '		xmlTotal= data;'+
                '	}'+
                '	$("#form_9_264_0 #importe").attr("readonly", true); '+
                '	$("#form_9_264_0 #importe").val(formatCurrency($(xmlTotal).find("importe").text()));'+
                '	  }, '+
                'error:function(xhr,err){'+
                '	alert("Problemas al recuperar importe de subtransacciones");'+
                '}' +
                '});'  
            });
        
            $("#divwait").dialog( "close" )
            $("#divwait").dialog("destroy");     
        },
        error:function(xhr,err){
            alert("Error al duplicar subtransacciones");
            $("#divwait").dialog( "close" )
            $("#divwait").dialog("destroy");                            

        }
    
    
    });

    $("#cantidad,#precio_unitario,#costo_directo,#tipo_cambio,#costo_indirecto").change( function() { 
    
        /*Quita formato a numeros para efectuar operaciones */
        $("#costo_directo").val($("#costo_directo").val().replace(/,/g,"").replace(/\$/g,""));
        $("#costo_indirecto").val($("#costo_indirecto").val().replace(/,/g,"").replace(/\$/g,""));
        $("#tipo_cambio").val($("#tipo_cambio").val().replace(/,/g,"").replace(/\$/g,""));

        var costo_unitario=((((parseFloat($("#costo_directo").val())+parseFloat($("#costo_indirecto").val()))*(1+parseInt($("#factor_recuperacion").val())/100))*(1+parseInt($("#factor_costo_operacion").val())/100))*(1+parseInt($("#factor_riesgo").val())/100))/parseFloat($("#tipo_cambio").val());
        $("#precio_unitario").val(costo_unitario);
        var importe= costo_unitario * parseInt($("#cantidad").val());
        $("#importe").val(importe);  

        /* Aplica formato */
        $("#costo_directo").val(formatCurrency($("#costo_directo").val()));
        $("#costo_indirecto").val(formatCurrency($("#costo_indirecto").val()));
        $("#tipo_cambio").val(formatCurrency($("#tipo_cambio").val()));
        $("#precio_unitario").val(formatCurrency($("#precio_unitario").val()));
        $("#importe").val(formatCurrency($("#importe").val()));

    });



}
