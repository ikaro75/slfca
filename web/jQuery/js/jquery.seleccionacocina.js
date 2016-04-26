(function($) {
    $.fn.seleccionacocina = function(opc){

        $.fn.seleccionacocina.settings = {
            height:"300"
        };

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            setGUI($(this));
        });
        
        function setGUI (obj) {
                  $.ajax({
                  url: "kitchen_picker.jsp?$empresa=" + $("#_enterprise_").val() + "&$empleado=" + $("#_ce_").val() + "&$ta=open",
                  dataType: ($.browser.msie) ? "text" : "xml",
                  success: function(data) {
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

                      /*Verifica el estatus de error*/
                      var oError=$(xml).find("error");
                      if (oError.length>0) {
                          var sDescripcionError=oError.text();
                          alert(sDescripcionError);
                          $("#divwait").dialog( "close" );                 
                          return false;
                      }
                      
                      obj.append("<div id='tablePickerDialog'>" + $(xml).find("html").html() + "<div style='text-align:center'><button id='seleccionacocina'>Selecciona cocina</button></div></div>");
                      
                      $("#seleccionacocina").button().click(function(){
                          //Aqui va el ajax que manda a llamar al webservice y recibe la respuesta
                          $.ajax({
                            url: "kitchen_picker.jsp?$empresa=" + $("#_enterprise_").val() + "&$empleado=" + $("#_ce_").val() + "&" + $("#object_picker").serialize() + "&$ta=save",
                            dataType: ($.browser.msie) ? "text" : "xml",
                            success: function(data) {
                                if (typeof data == "string") {
                                    xmlGuardar = new ActiveXObject("Microsoft.XMLDOM");
                                    xmlGuardar.async = false;
                                    xmlGuardar.validateOnParse="true";
                                    xmlGuardar.loadXML(data);
                                    if (xmlGuardar.parseError.errorCode>0) {
                                        alert("Error de compilación xml:" + xmlGuardar.parseError.errorCode +"\nParse reason:" + xmlGuardar.parseError.reason + "\nLinea:" + xmlGuardar.parseError.line);
                                    }
                                }
                                else {
                                    xmlGuardar = data;
                                }

                                /*Verifica el estatus de error*/
                                var oError=$(xmlGuardar).find("error");
                                if (oError.length>0) {
                                    var sDescripcionError=oError.text();
                                    alert(sDescripcionError);
                                    $("#divwait").dialog( "close" );                 
                                    return false;
                                }
                                
                                $("#divwait").dialog( "close" );       
                            },
                            error: function(xhr, err) {
                                alert("Error al recuperar los datos de la cocina: \n" + +xhr.responseText);
                            }
                        })
                          $("#tablePickerDialog").dialog("close");
                          $("#tablePickerDialog").remove();
                      });
                      
                      $("#tablePickerDialog").dialog({
                                modal: true,
                                autoOpen: true,
                                closeOnEscape: false,
                                width: 500
                            });
                      $("#divwait").dialog( "close" );       
                  },
                  error: function(xhr, err) {
                      alert("Error al recuperar los datos de la cocina: \n" + +xhr.responseText);
                  }
              })  
            }
    }
})(jQuery);
             