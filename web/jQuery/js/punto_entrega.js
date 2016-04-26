function almacenes_init() {
    claveAplicacion = $(xml).find("configuracion_forma").find("clave_aplicacion")[0].childNodes[0].data;
    claveForma = $(xml).find("configuracion_forma").find("clave_forma")[0].childNodes[0].data;
    claveRegistro = $(xml).find("configuracion_forma").find("pk")[0].childNodes[0].data;
    cerrado = $(xml).find("cerrado")[0].childNodes[0].data;
    claveEstado = $(xml).find("clave_estado")[0].childNodes[0].data;
    
    $("#clave_estado").change(function() {
       if ($("#clave_estado").val()!="") {
           setXMLInSelect4("#form_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro + " #clave_municipio",611,"foreign",'',"fide_municipio.clave_estado=" +$("#clave_estado").val());           
       }
    });    

    if (cerrado=="1") {
        $("#td_fecha_cierre_operacion").parent().show();
    } else {
        $("#td_fecha_cierre_operacion").parent().hide();
    }
    
    $("#cerrado").change(function(){
        if ( $("input[@id=cerrado]:checked").length>0){
            $("#td_fecha_cierre_operacion").parent().show();
        } else {
             $("#td_fecha_cierre_operacion").parent().hide();
        } 
    });    
    
    if (claveEstado!="") {
        setXMLInSelect4("#form_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro + " #clave_municipio",611,"foreign",'',"clave_estado=" +claveEstado);           
        $("#clave_municipio option[value='" + $(xml).find("clave_municipio")[0].childNodes[0].data + "']").attr("selected", "selected");
    }
}

function tiendas_init() {
    claveAplicacion = $(xml).find("configuracion_forma").find("clave_aplicacion")[0].childNodes[0].data;
    claveForma = $(xml).find("configuracion_forma").find("clave_forma")[0].childNodes[0].data;
    claveRegistro = $(xml).find("configuracion_forma").find("pk")[0].childNodes[0].data;
    
    $("#clave_estado").change(function() {
       if ($("#clave_estado").val()!="") {
           setXMLInSelect4("#form_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro + " #clave_municipio",611,"foreign",'',"fide_municipio.clave_estado=" +$("#clave_estado").val());           
           setXMLInSelect4("#form_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro + " #clave_punto_padre",608,"foreign",'',"clave_estado=" +$("#clave_estado").val());
       }
    }); 
    
    if (cerrado=="1") {
        $("#td_fecha_cierre_operacion").parent().show();
    } else {
        $("#td_fecha_cierre_operacion").parent().hide();
    }
    
    $("#cerrado").change(function(){
        if ( $("input[@id=cerrado]:checked").length>0){
            $("#td_fecha_cierre_operacion").parent().show();
        } else {
             $("#td_fecha_cierre_operacion").parent().hide();
        } 
    });
}

function distribucion_init () {
    claveAplicacion = $(xml).find("configuracion_forma").find("clave_aplicacion")[0].childNodes[0].data;
    claveForma = $(xml).find("configuracion_forma").find("clave_forma")[0].childNodes[0].data;
    claveRegistro = $(xml).find("configuracion_forma").find("pk")[0].childNodes[0].data;
    
    $("#tienda").prop('disabled', true);
    
    $("#clave_proveedor").change(function() {
       if ($("#clave_proveedor").val()!="") {
           setXMLInSelect4("#form_" + claveAplicacion + "_" + claveForma + "_" + claveRegistro + " #clave_producto",644,"foreign",'',"clave_proveedor=" +$("#clave_proveedor").val());
       }
    });
}

function punto_entregas_grid_init() {

    $(".startsurvey").click(function() {
        nCuestionario=this.id.split("_")[1];
        nProspecto=this.id.split("_")[2];
        sModo = this.id.split("_")[3];
        nClaveFormaEntidadParticipante=this.id.split("_")[4];
        IdTienda =this.id.split("_")[5];
       $("#_cache_").val(nCuestionario + "_" + IdTienda);
        
        if (sModo=='insert') {
            $("#divwait")
            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando cuestionario...</p>")
            .attr('title','Espere un momento por favor') 
            .dialog({
                height: 140,
                modal: true,
                autoOpen: true,
                closeOnEscape:false
            });
            //Inserta cuestionario de participante en blanco
            $.ajax({
            url: "control?$cmd=register&$cf=633&$pk=0&$ta=insert&clave_punto="+nProspecto+"&clave_cuestionario="+nCuestionario+"&clave_empleado="+$("#_ce_").val() + "&clave_estatus=1&fecha_inicio=%ahora",
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlCuestionario = new ActiveXObject("Microsoft.XMLDOM");
                    xmlCuestionario.async = false;
                    xmlCuestionario.validateOnParse = "true";
                    xmlCuestionario.loadXML(data);

                    if (xmlCuestionario.parseError.errorCode > 0) {
                        alert("Error de compilación xml:" + xmlCuestionario.parseError.errorCode + "\nParse reason:" + xmlCuestionario.parseError.reason + "\nLinea:" + xmlCuestionario.parseError.line);
                    }
                }
                else {
                    xmlCuestionario = data;
                }
                
                nCuestionario = $(xmlCuestionario).find("pk")[0].childNodes[0].data;
                
                $("#top").survey({
                    pk: nCuestionario,
                    modo: "open",
                    claveProspecto: nProspecto,
                    claveFormaEntidadParticipante: nClaveFormaEntidadParticipante
                });
                
            },
            error: function(xhr, err) {
                alert("Error al recuperar los datos del cuestionario: \n" + +xhr.responseText);
            }
        })
        } else {
            $("#divwait")
            .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Abriendo cuestionario...</p>")
            .attr('title','Espere un momento por favor') 
            .dialog({
                height: 140,
                modal: true,
                autoOpen: true,
                closeOnEscape:false
            });
            
            $("#top").survey({
                pk: nCuestionario,
                modo: sModo,
                claveProspecto: nProspecto,
                claveFormaEntidadParticipante: nClaveFormaEntidadParticipante
            });
        }
    });
    
}

function parametro_acuse_recibo_init() {
    $("#clave_estado").change(function() {
       if ($("#clave_estado").val()!="") {
           setXMLInSelect4("#id_almacen",704,"foreign",'',"clave_estado=" +$("#clave_estado").val());           
       } else {
           $("#id_almacen").empty();
       }
    });   
}

function calendario_cierre_punto_ongridComplete() {
    $(".startsurvey").click(function() {
        idControl = this.id.split("_")[1];
        window.open("ireport.jsp?$cr=","_blank");
    });
}

function carpeta_init() {
     clavePunto = $(xml).find("clave_punto")[0].childNodes[0].data;
     
     if (clavePunto!="") {
         setXMLInSelect4("#clave_punto",681,"foreign",'',"fide_punto_entrega.clave_punto=" +clavePunto);    
         $("#clave_punto option[value='" + clavePunto + "']").attr("selected", "selected");
     }
}

function cuestionario_supervision_init() {
    //1. Toma del cache 
    claveCuestionarioParticipante ="";
    IdTienda = "";
    oculto = false;
    
    if ($("#_cache_").val().split("_").length>0)
        claveCuestionarioParticipante=$("#_cache_").val().split("_")[0];
    if ($("#_cache_").val().split("_").length>1)
        IdTienda=$("#_cache_").val().split("_")[1];
    forma = $("#surveyform_" + claveCuestionarioParticipante);
    
    //2. Si viene vacío  
    $(forma.find("input")[0]).val(IdTienda).unbind("change").change(function() {
        if (!oculto) 
            ocultaPreguntas(forma);
        oculto = true;
    });
    
    $(forma.find("input")[0]).parent().append("<input type='button' value='Buscar tienda' id='buscaTienda'/>")
    $("#buscaTienda").button().unbind("click").click(function() {
        //Vuelve visibles las preguntas y bloquea la tienda
        alert("En desarrollo");
    });
    
    //
    if ($(forma.find("input")[0]).val()=="") {
        ocultaPreguntas(forma);
        oculto = true;
    }    
}

function ocultaPreguntas(forma) {
    for (i=1; forma.find("input").length; i++) {
        forma.find("input").parent().hide();
        forma.find("input").hide();
    }
}