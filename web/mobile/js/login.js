$(document).on("pagecreate", "#login", function() {
      $.ajax(
            {
                url: "../sesion_mobile.jsp",
                dataType: "text",
                success: function(data) {        
                    e = document.getElementById("$config");
                    if (data!="") { 
                        $(e).html(data);
                        $(e).prop("selectedIndex", 0).selectmenu('refresh');
                    } else 
                        $("#submitButton").attr("disabled","disabled");
                },
                error: function(xhr, err) {
                }
            }); 
            
    $("#submitButton").click(function() {
        if ($("#email").val()=="" ) {
            alert("Escriba su usuario");
            return false;
        }

        if ($("#password").val()=="" ) {
            alert("Escriba su contraseña");
            return false;
        }
        
        var e = document.getElementById("$config");
        var selectedIndex = $(e)[0].selectedIndex;
        $.ajax({
                url: "../control?$cmd=login&$app=mobile&email="+$("#email").val()+"&password="+$("#password").val()+"&$config="+$(e).val(),
                dataType: ($.browser.msie) ? "text" : "xml",
                success: function(data) {
                        if (typeof data == "string") {
                            xmlLogin = new ActiveXObject("Microsoft.XMLDOM");
                            xmlLogin.async = false;
                            xmlLogin.validateOnParse = "true";
                            xmlLogin.loadXML(data);
                            if (xmlLogin.parseError.errorCode > 0) {
                                alert("Error de compilación xml:" + xmlLogin.parseError.errorCode + "\nParse reason:" + xmlLogin.parseError.reason + "\nLinea:" + xmlLogin.parseError.line);
                            }
                        }
                        else {
                            xmlLogin = data;
                        }
                        
                        if ($(xmlLogin).find("error").length > 0) {
                            $("#footer").html("<div data-icon='alert' style='margin: 10px;text-align:center;'>"+ $(xmlLogin).find("error").text()+"</div>");
                        } else {
                            //Crea cookie y redirecciona a la capa vista
                            if ($(xmlLogin).find("mensaje").length > 0) {
                                $("#footer").html("<div data-icon='alert' style='margin: 10px;text-align:center;'>"+ $(xmlLogin).find("mensaje").text()+"</div>");
                            } else {
                            //Crea cookie y redirecciona a la capa vista
                            
                            }
                            
                        }
   
                        $(e).prop("selectedIndex",selectedIndex).selectmenu('refresh');
                    },
                error: function(xhr, err) {
                }
            }); 
            
           
    })
});