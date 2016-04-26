$(document).ready(function() {

    $("#btnLoginCandidato").button().click(function() {
        window.location = "login.jsp";
    });

    $("#btnRegisterCandidato").button().click(function() {
        window.location = "#contacto";
    });


    $("#btnLoginEmpleador").button().click(function() {
        window.location = "login.jsp";
    });

    $("#btnRegisterEmpleador").button().click(function() {
        window.location = "#contacto";
    });

    $("#btnEnviar").button().click(function() {
        window.location = "#lnkcontacto";
    });

    $("#divCarouselEmpleox").agile_carousel({
        carousel_data: [{
                "content": $("#carruselCandidato").html(),
                "content_button": ""
            }, {
                "content": $("#carruselEmpleador").html(),
                "content_button": ""
            }
        ],
        carousel_outer_height: $("#divCarouselEmpleox").height(),
        carousel_height: $("#divCarouselEmpleox").height(),
        slide_height: $("#divCarouselEmpleox").height() + 2,
        carousel_outer_width: $("#divCarouselEmpleox").width(),
        slide_width: $("#divCarouselEmpleox").width(),
        transition_time: 300,
        continuous_scrolling: false,
        number_slides_visible: 1,
        control_set_1: "previous_button,next_button",
        control_set_2: "numbered_buttons"
    });

    $(".control_set_1").hide();

    $(".slide_number_1").html("<a href='#' class='entitymenu'>Postulantes</a>").append("&nbsp;&nbsp;|&nbsp;");
    $(".slide_number_2").html("<a href='#' class='entitymenu'>Empleadores</a>");
    var estilo = $(".agile_carousel").attr("style");
    estilo = estilo + ";top:-30px;";
    $(".agile_carousel").attr("style", estilo);
    $("#btnBuscarEmpleos").button();

    //$("#carruselApps").find(".agile_carousel")

    $("#formaBusquedaEmpleo #tr_clave_estado").toggle();
    $("#formaBusquedaEmpleo #tr_clave_ciudad").toggle();
    $("#formaBusquedaEmpleo #tr_clave_categoria").toggle();
    $("#formaBusquedaEmpleo #tr_clave_area").toggle();
    $("#formaBusquedaEmpleo #tr_clave_sector").toggle();
    $("#formaBusquedaEmpleo #tr_w1").toggle();
    $("#formaBusquedaEmpleo #tr_w2").toggle();

    $("#lnkBusquedaAvanzadaEmpleos").click(function() {

        if ($(this).text() == 'M\u00e1s opciones') {
            $(this).text('Menos opciones');
        } else {
            $(this).text('M\u00e1s opciones');
        }

        $("#formaBusquedaEmpleo #tr_clave_estado").toggle();
        $("#formaBusquedaEmpleo #tr_clave_ciudad").toggle();
        $("#formaBusquedaEmpleo #tr_clave_categoria").toggle();
        $("#formaBusquedaEmpleo #tr_clave_area").toggle();
        $("#formaBusquedaEmpleo #tr_clave_sector").toggle();
        $("#formaBusquedaEmpleo #tr_w1").toggle();
        $("#formaBusquedaEmpleo #tr_w2").toggle();
    });

    $("#tabsCandidato").tabs();
    $("#tabInicioCandidato").tabs();
    $("#tabCurriculumCandidato").tabs();

    $("#tabsEmpleador").tabs();
    $("#tabInicioEmpleador").tabs();
    $("#tabPerfilEmpresa").tabs();

    $("#formaBusquedaCvs #tr_titulo_cv").toggle();
    $("#formaBusquedaCvs #tr_w1").toggle();
    $("#formaBusquedaCvs #tr_clave_genero").toggle();
    $("#formaBusquedaCvs #tr_clave_estado").toggle();
    $("#formaBusquedaCvs #tr_clave_nivel_estudio").toggle();
    $("#formaBusquedaCvs #tr_institucion").toggle();
    $("#formaBusquedaCvs #tr_clave_idioma").toggle();
    $("#formaBusquedaCvs #tr_clave_area_experiencia").toggle();
    $("#formaBusquedaCvs #tr_w2").toggle();
    $("#formaBusquedaCvs #tr_w3").toggle();

    $("#btnBuscarCVs").button();
    $("#lnkBusquedaAvanzadaCVs").click(function() {
        if ($(this).text() == 'M\u00e1s opciones') {
            $(this).text('Menos opciones');
        } else {
            $(this).text('M\u00e1s opciones');
        }

        $("#formaBusquedaCvs #tr_titulo_cv").toggle();
        $("#formaBusquedaCvs #tr_w1").toggle();
        $("#formaBusquedaCvs #tr_clave_genero").toggle();
        $("#formaBusquedaCvs #tr_clave_estado").toggle();
        $("#formaBusquedaCvs #tr_clave_nivel_estudio").toggle();
        $("#formaBusquedaCvs #tr_institucion").toggle();
        $("#formaBusquedaCvs #tr_clave_idioma").toggle();
        $("#formaBusquedaCvs #tr_clave_area_experiencia").toggle();
        $("#formaBusquedaCvs #tr_w2").toggle();
        $("#formaBusquedaCvs #tr_w3").toggle();

    });
    
    $("#btnRegisterCandidato").click(function() {
            $("#divwait")
                .html("<br /><p style='text-align: center'><img src='img/throbber.gif' />&nbsp;Generando forma...</p>")
                .attr('title','Espere un momento por favor') 
                .dialog({
                    height: 140,
                    modal: true,
                    autoOpen: true,
                    closeOnEscape:false
                });
                                            
                $("#top").form({
                    app: 28,
                    forma:304,
                    datestamp:"",
                    modo:"insert",
                    columnas:1,
                    pk:0,
                    filtroForaneo:"",
                    height:"500",
                    width:"80%",
                    originatingObject: "",
                    showRelationships:"false",
                    updateControl:""
                });
    });
    

}); //close $(

function candidato_init() {
    $("#td_ciudad").html('Confirme su contraseña (<span id="msgvalida_ciudad" class="mensajeobligatorio" style="display: none;">Obligatorio</span>*)');
}