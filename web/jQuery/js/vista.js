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
 * and open the template in the editor.
 */
var jsonConfig= {gridTimers: []};
$(document).ready(function() {

    //Despliega dialogo modal para evitar acciones del usuario mientras se cargan primeros grids
    //puesto que causa conflictos
    $("#_status_").val("Inicializando");
    $("#divwait").dialog({
            height: 140,
            modal: true,
            autoOpen: true,
            closeOnEscape:false
    });
        
    /*$('#switcher').themeswitcher({
    			imgpath: "images/"
    		}); */
    $("#tabs").tabs();
    //Crea menú de aplicaciones de acuerdo al perfil
    $(".appmenu").appmenu();
    //barrasAvances(745, "", "Avances acumulados por Estado ", "grafica_avances_por_estado");
    graficaAvanceAcumulado(745, "", "<h3>Porcentajes de avance acumulado por Estado</h3>", "avance_acumulado");
    graficaAvanceUltimaCarga(745, "", "<h3>L&aacute;mparas entregadas al 22 de marzo por Estado</h3>", "avance_diferencial");
    pieEntregasPorEstado(745, "", "<h3>Porcentaje total de entregas</h3>", "porcentaje_avance")
    $("#sessionMenu").sessionmenu();
    
      
            
}); //close $(