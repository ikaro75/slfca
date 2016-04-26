function cambia_password_init() {
    $("#opciones_1_258_" + $("#_ce_").val()).remove();
    $("#btnMenuOpciones_1_258_" + $("#_ce_").val()).remove();
    $("#btnEliminar_1_258_" + $("#_ce_").val()).parent().parent().remove();
    
    $("#btnGuardar_1_258_" + $("#_ce_").val()).click(function() {
       if ($("#password").val() != $("#nombre").val()) {
            alert('El password y la confirmación no coinciden, verifique');
            $('#nombre').val("");
            $('#password').val("");
            $('#password').focus();
            return;
        } else {
            if ($("#password").val()=="" || $("#nombre").val()==""){
                alert('Los campos están vacios, verifique');
                $('#password').focus();
                return;  
            }
            //$("#password,#nombre").change(function() {});
            $("#apellido_paterno").attr("disabled","disabled");
            $("#nombre").attr("disabled","disabled");
        }    
    });
    timere=setInterval(function(){$("#password").focus();
    clearTimeout(timere)},1000);
}
