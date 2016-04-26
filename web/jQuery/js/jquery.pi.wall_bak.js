( function($) {
    $.fn.wall = function(opc){
        
        $.fn.wall.settings = {
            titulo:"",
            app:"",
            forma:"",
            pk:"",
            pk_name:"",
            pagina: "1",
            numeroDeRegistros:"20",
            
            xmlUrl : "control?$cmd=post" , // "srvControl" "xml_tests/forma.app.xml"
                 
        };
        
        $.fn.wall.options = $.extend($.fn.wall.settings, opc);
        
        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            wall=this;
            $.ajax(
            {   
            url: $.fn.wall.options.xmlUrl + "&$cf=299&$ta=select&page=" +$.fn.wall.options.pagina +"&rows="+$.fn.wall.options.numeroDeRegistros+ "&sidx=fecha&sord=desc",
            dataType: ($.browser.msie) ? "text" : "xml",
            success:  function(data){
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
                }
                
                posts=$(xml).find("registro");
                htmlForPosts="";
                $.each(posts, function(){
                    if ($(this).find("clave_empleado").find('foto').text()=="") {
                        foto="img/sin_foto.jpg"
                    }
                    else {
                        foto=$(this).find("clave_empleado").find('foto').text()
                    } 

                    htmlForPosts+=  
                    "<div id='comentario-"+ $(this).find("clave_publicacion").text() +
                    "' foto='"+ foto + 
                    "' clave_empleado='" + $(this).find("clave_empleado")[0].childNodes[0].data + 
                    "' nombre='"+ $(this).find("nombre_empleado").text() + 
                    "' publicacion='"+ $(this).find("publicacion").text() + 
                    "' fecha='"+ $(this).find("fecha").text() +                             
                    "' class='post'></div>";                    
                });
                    
               $(wall).html(htmlForPosts);
               $(".post").post();
            },
            error:function(xhr,err){
                alert("Error al recuperar la forma");
            }
            });
        });
 
    };
}) (jQuery);
   
