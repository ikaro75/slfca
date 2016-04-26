/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function compartir_init() {
    //Se sustituye el campo clave_tipo_entidad de tipo text por un checkbox
    $("#clave_tipo_entidad").parent().html("<input type='checkbox' id='clave_tipo_entidad' value='1' style='float: left;' />");
    $("#td_clave_grupo").parent().hide();
    $("#clave_grupo").removeClass("obligatorio");  
    
    $("#clave_tipo_entidad").click(function() { 
        if ($(this).attr("checked")) {
            $("#td_clave_contacto").parent().hide();
            $("#td_clave_grupo").parent().show();
            $("#clave_contacto").removeClass("obligatorio");
            $("#clave_grupo").addClass("obligatorio");
            $("#clave_contacto option:selected").removeAttr("selected");
        } else {
            $("#td_clave_contacto").parent().show();
            $("#td_clave_grupo").parent().hide();
            $("#clave_contacto").addClass("obligatorio");
            $("#clave_grupo").removeClass("obligatorio");
            $("#clave_grupo option:selected").removeAttr("selected");
        }
    });
    
}
