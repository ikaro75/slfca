$(document).ready(function() {
    
    $("#btnlogin").button().click(function() {
        window.location="login.jsp";
    });
    
     $("#btnsignup").button().click(function() {
        window.location="#contacto";
    });
    
    $("#btnEnviar").button().click(function() {
        window.location="#lnkcontacto";
    });
    
    $("#divCarouselAdministrax").agile_carousel({
        carousel_data: [{
            "content": $("#carrusel1").html(),
            "content_button": ""
        }, {
            "content": $("#carrusel2").html(),
            "content_button": ""
        }, {
            "content": $("#carrusel3").html(),
            "content_button": ""
        }, {
            "content": $("#carrusel4").html(),
            "content_button": ""
        }, {
            "content": $("#carrusel5").html(),
            "content_button": ""
        }, {
            "content": $("#carrusel6").html(),
            "content_button": ""
        }, {
            "content": $("#carrusel7").html(),
            "content_button": ""
        }                    
        ],
        carousel_outer_height: $("#divCarouselAdministrax").height(),
        carousel_height: $("#divCarouselAdministrax").height(),
        slide_height: $("#divCarouselAdministrax").height()+2,
        carousel_outer_width: $("#divCarouselAdministrax").width(),
        slide_width: $("#divCarouselAdministrax").width(), 
        transition_time: 300,
        timer: 4000,
        continuous_scrolling: true,
        number_slides_visible: 1,
        control_set_1: "previous_button,next_button",
        control_set_2: "numbered_buttons"
    }); 
    
    $("#carruselApps").agile_carousel({
        carousel_data: [{
            "content": $("#apps0").html(),
            "content_button": ""
        }, {
            "content": $("#apps1").html(),
            "content_button": ""
        }, {
            "content": $("#apps2").html(),
            "content_button": ""
        }, {
            "content": $("#apps3").html(),
            "content_button": ""
        }, {
            "content": $("#apps4").html(),
            "content_button": ""
        }, {
            "content": $("#apps5").html(),
            "content_button": ""
        }, {
            "content": $("#apps6").html(),
            "content_button": ""
        }                    
        ],
        carousel_outer_height: $("#apps2").height(),
        carousel_height:$("#apps2").height(),
        slide_height: $("#apps2").height(),
        carousel_outer_width: $("#carruselApps").width(),
        slide_width: $("#carruselApps").width(), 
        transition_time: 300,
        continuous_scrolling: true,
        number_slides_visible: 1,
        control_set_1: "previous_button,next_button",
        control_set_2: "numbered_buttons"
    }); 
    
    //$("#carruselApps").find(".agile_carousel")
}); //close $(