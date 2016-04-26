/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {

    /*if (($("#msjLogin").html().toLowerCase()=='<span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span>')||
        ($("#msjLogin").html().toLowerCase()=='<span style="float: left; margin-right: 0.3em" class="ui-icon ui-icon-alert"></span>'))   {
    $("#divMsgLogin").hide();
    }*/
    
    $("#divMsjRecuperaPW").hide();
    $("#iniciarsesion").button();
    $("#btnRecuperarPw").button();
    $("#divCarousel").agile_carousel({
                carousel_data: [{
                                "content": $("#divLogin").html(),
                                "content_button": ""
                                }, {
                                "content": $("#divLostPw").html(),
                                "content_button": ""
                                }],
                carousel_outer_height: 228,
                carousel_height: 228,
                slide_height: 230,
                carousel_outer_width: 550,
                slide_width: 550,
                transition_time: 300,
                continuous_scrolling: true,
                control_set_1: "numbered_buttons",
                no_control_set: "hover_previous_button,hover_next_button"
    });
    
    //Hack a carrousel
    $(".slide_number_1").html("Inicio de sesi&oacute;n");
    $(".slide_number_2").html("Olvid&eacute; mi contrase&ntilde;a");
    //$("#divLostPw").hide();

    /*$("#iniciarsesion").click(function(){
        s=$("#texto_mensaje").html;
        if ($("#lgn").val()=="") {
            $("#msjLogin").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>Se requiere clave de usuario, verifique</strong>");
            $("#divMsgLogin").show();
            return false;
        }
        else {
            $("#divMsgLogin").hide();
        }

        if ($("#psw").val()=="") {
            $("#msjLogin").html("<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span><strong>Se requiere contrase&ntilde;a, verifique</strong>");
            $("#divMsgLogin").show();
            return false;
        }

        $("#frmLogin").submit();
        return true;
    });*/

    $("#btnRecuperarPw").click(function(){
        if ($("#rc").val()=="") {
            $($("#msjRecuperaPW").children()[1]).html("Se requiere clave de usuario, verifique");
            $("#divMsjRecuperaPW").show();
            return;
        }
        else {
            $.ajax(
            {
                url: "control?$cmd=recoverpw&email="+$("#rc").val(),
                dataType: ($.browser.msie) ? "text" : "xml",
                success:  function(data){
                if (typeof data == "string") {
                    xmlLPResponse = new ActiveXObject("Microsoft.XMLDOM");
                    xmlLPResponse.async = false;
                    xmlLPResponse.validateOnParse="true";
                    xmlLPResponse.loadXML(data);
                    if (xmlLPResponse.parseError.errorCode>0)
                        alert("Error de compilación xml:" + xmlLPResponse.parseError.errorCode +"\nParse reason:" + xmlLPResponse.parseError.reason + "\nLinea:" + xmlLPResponse.parseError.line);
                }                
                else 
                    xmlLPResponse = data;            
                $($("#msjRecuperaPW").html('<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span><strong>'+$(xmlLPResponse).find("respuesta").text()+'</strong>'));
                $("#divMsjRecuperaPW").show();
            }}
            );
        }

    });
});
