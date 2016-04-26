/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function fx_cuestionario_participante_grid_init() {
                    
    $(".startsurvey").click(function() {
        nEncuesta=this.id.split("_")[1];
        nProspecto=this.id.split("_")[2];
        nModo = this.id.split("_")[3];
        
        if (nModo==6) {
            sModo="show-authentication-form";
        } else {
            sModo="open";
        }
        
        $("#divwait")
        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
        .attr('title','Espere un momento por favor') 
        .dialog({
            height: 140,
            modal: true,
            autoOpen: true,
            closeOnEscape:false
        });
    
        $("#top").survey( {
            pk: nEncuesta,
            modo:"open",
            claveProspecto: nProspecto,
            modo: sModo
        });
    });
    
    $(".openpendingquestions").click(function() {
        nEncuesta=this.id.split("_")[1];
        nProspecto=this.id.split("_")[2];
        nModo = this.id.split("_")[3];
        
        if (nModo==6) {
            sModo="show-authentication-form";
        } else {
            sModo="open-pending-questions";
        }
        
        $("#divwait")
        .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
        .attr('title','Espere un momento por favor') 
        .dialog({
            height: 140,
            modal: true,
            autoOpen: true,
            closeOnEscape:false
        });
    
        $("#top").survey( {
            pk: nEncuesta,
            modo:"open",
            claveProspecto: nProspecto,
            modo: sModo
        });
    });    
}

function fx_duplica_cuestionario(nCuestionario) {
    //Llamada ajax a capa controladora
    $("#divwait")
    .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Duplicando formulario...</p>")
    .attr('title','Espere un momento por favor') 
    .dialog({
        height: 140,
        modal: true,
        autoOpen: true,
        closeOnEscape:false
    });

    var options = { 
            beforeSubmit:  validateForm,  // pre-submit callback 
            success:       processXml,  // post-submit callback 
            dataType:  ($.browser.msie) ? "text" : "xml",
            url: "control?$cmd=register&$ta="+$("#formTab_" + formSuffix).attr("modo"),       // override for form's 'action' attribute 
            error:function(xhr,err){
                $("#grid_"+gridSuffix+"_toppager_right").children(0).html("Error al guardar registro");
                $("#dlgModal_"+ formSuffix).remove();
                alert("Error al guardar registro: "+xhr.readyState+"\nstatus: "+xhr.status + "\nResponseText:"+ xhr.responseText);          
            }
    }; 

    oForm.ajaxSubmit(options); 
    
    $.ajax(
    {
        url: "control?$cmd=register&$ta=duplicate&$cf=268&$pk="+ nCuestionario,
        dataType: ($.browser.msie) ? "text" : "xml",
        success:  function(data){
            if (typeof data == "string") {
                xmlCuestionario = new ActiveXObject("Microsoft.XMLDOM");
                xmlCuestionario.async = false;
                xmlCuestionario.validateOnParse="true";
                xmlCuestionario.loadXML(data);
                if (xmlCuestionario.parseError.errorCode>0) {
                    alert("Error de compilación xml:" + xmlCuestionario.parseError.errorCode +"\nParse reason:" + xmlCuestionario.parseError.reason + "\nLinea:" + xmlCuestionario.parseError.line);
                }
            }
            else {
                xmlCuestionario = data;
            }        
            
            error=$(xmlCuestionario).find("error");
            if (error.length>0) {
                $("#divwait").dialog( "close" )
                $("#divwait").dialog("destroy");             
                alert("No fue posible duplicar el cuestionario:" + error.text());
                return;
            }
            
            //Se debe reabrir el registro duplicado
            $("#dlgModal_27_268_" + nCuestionario).remove();
            
             $("body").form({
                app: 27,
                forma:268,
                datestamp:$(this).attr("datestamp"),
                modo:"update",
                columnas:1,
                pk:nPK,
                height:"500",
                width:"80%",
                originatingObject: $(this).id,
                updateControl:""
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
    //Si es exitosa la duplicación entonces vuelve a abrir forma
}