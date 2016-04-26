$(document).bind('pageinit', function() {
    var host = "";

    $("#submitLogin").click(function() {
        if ($("#email").val() == "") {
            $("#dfnMessageLogin").html("Se requiere correo electr&oacute;nico, verifique.");
            $( "#msjLogin" ).popup( "open" );
            $("#email").focus();
            return false;
        }

        if ($("#password").val() == "") {
            $("#dfnMessageLogin").html("Se requiere contrase&ntilde;a, verifique.");
            $( "#msjLogin" ).popup( "open" );
            $("#password").focus();
            return false;
        }

        $.ajax({
            type: "post",
            url: host + "control?$cmd=login&$app=mobile&email=" + $("#email").val() + "&password=" + $("#password").val(),
            cache: false,
            dataType: ($.browser.msie) ? "text" : "xml",
            success: function(data) {
                if (typeof data == "string") {
                    xmlLogin = new ActiveXObject("Microsoft.XMLDOM");
                    xmlLogin.async = false;
                    xmlLogin.validateOnParse = "true";
                    xmlLogin.loadXML(data);
                    if (xmlLogin.parseError.errorCode > 0)
                        alert("Error de compilación xml:" + xmlLogin.parseError.errorCode + "\nParse reason:" + xmlLogin.parseError.reason + "\nLinea:" + xmlLogin.parseError.line);
                }
                else
                    xmlLogin = data;

                if ($(xmlLogin).find("mensaje").text() == "Usuario validado") {
                    // Guarda en localStorage los datos de la sesión
                    localStorage.setItem("clave_usuario", $(xmlLogin).find("clave_usuario").text());
                    localStorage.setItem("nombre", $(xmlLogin).find("nombre").text());
                    localStorage.setItem("foto", $(xmlLogin).find("foto").text());
                    location.href = "vista.html";
                } else {
                    $.mobile.activePage;
                    $("#msjLogin").removeClass("ui-body-e");
                    $("#msjLogin").addClass("ui-body-g");
                    $("#dfnMessageLogin").html($(xmlLogin).find("mensaje").text());
                    $("#msjLogin").popup( "open" );
                }
            },
            error: function(xhr, err) {
                $("#dfnMessageLogin").html("Servidor no disponible");
                $( "#msjLogin" ).popup( "open" );
            }
        });
        return false;

    });

    $("#btnRecuperarPw").click(function() {
        if ($("#rc").val() == "") {
            $("#dfnMessageRP").html("Se requiere el correo electr&oacute;nico del usuario, verifique");
            $( "#msjRecuperaPW" ).popup( "open" );
            $("#rc").focus();
            return false;
        } else {
            $.ajax({
                type: "post",
                url: host + "control?$cmd=recoverpw",
                data: "email=" + $("#rc").val(),
                cache: false,
                dataType: ($.browser.msie) ? "text" : "xml",
                success: function(data) {
                    if (typeof data == "string") {
                        xmlLPResponse = new ActiveXObject("Microsoft.XMLDOM");
                        xmlLPResponse.async = false;
                        xmlLPResponse.validateOnParse = "true";
                        xmlLPResponse.loadXML(data);
                        if (xmlLPResponse.parseError.errorCode > 0)
                            alert("Error de compilación xml:" + xmlLPResponse.parseError.errorCode + "\nParse reason:" + xmlLPResponse.parseError.reason + "\nLinea:" + xmlLPResponse.parseError.line);
                    }
                    else
                        xmlLPResponse = data;

                    $.mobile.hidePageLoadingMsg();
                    $($("#dfnMessageRP").html($(xmlLPResponse).find("respuesta").text()));
                    $( "#msjRecuperaPW" ).popup( "open" );
                },
                error: function(xhr, err) {
                    $("#dfnMessageRP").html("Servidor no disponible");
                    $( "#msjRecuperaPW" ).popup( "open" );
                }
            });
            return false;
        }
    });
});
