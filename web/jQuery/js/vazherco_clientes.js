function vazherco_clientes_grid_init() {

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
    
}
