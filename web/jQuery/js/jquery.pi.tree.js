( function($) {
    $.fn.treeMenu = function(opc){

        $.fn.treeMenu.settings = {
            xmlUrl : "xml_tests/widget.tree.xml?app=",
            app: "",
            pk:""
        };

        // Ponemos la variable de opciones antes de la iteración (each) para ahorrar recursos
        $.fn.treeMenu.options = $.extend($.fn.treeMenu.settings, opc);

        // Devuelvo la lista de objetos jQuery
        return this.each( function(){
            $(this).jstree({
                "plugins" : [ "themes", "contextmenu", "xml_data","types" ],
                "xml_data" : {
                    "ajax" : {
                        "url" : $.fn.treeMenu.options.xmlUrl + $.fn.treeMenu.options.app
                    },
                    "xsl" : "nest"
                },
                "themes" : {
                    "theme" : "default",
                    "dots" : true,
                    "icons": true
                },
                "types" : {
                    "max_depth" : -2,
                    "max_children" : -2,
                    "valid_children" : [ "app" ],
                    "types" : {
                        "app" : {
                            "valid_children" : [ "entidades", "perfiles","usuarios" ],
                            "icon" : {
                                "image" : "http://localhost:8088/ProyILCE/img/app7.png"
                            },
                            "start_drag" : false,
                            "move_node" : false,
                            "delete_node" : false,
                            "remove" : false
                        },
                        "entidades" : {
                            "valid_children" : "entidad",
                            "icon" : {
                                "image" : "http://localhost:8088/ProyILCE/img/forms9.png"
                            }
                        },
                        "entidad" : {
                            "valid_children" : ["consultas", "entidad"],
                            "icon" : {
                                "image" : "http://localhost:8088/ProyILCE/img/form5.png"
                            }
                        },
                        "consultas" : {
                            "valid_children" : ["consulta"],
                            "icon" : {
                                "image" : "http://localhost:8088/ProyILCE/img/consultas4.png"
                            }
                        },
                        "consulta" : {
                            "valid_children" : ["consulta"],
                            "icon" : {
                                "image" : "http://localhost:8088/ProyILCE/img/consulta6.png"
                            }
                        },

                        "campo" : {
                            "valid_children" : "none",
                            "icon" : {
                                "image" : "http://localhost:8088/ProyILCE/img/campo4.png"
                            }
                        },
                        "perfiles" : {
                            "valid_children" : [ "campo", "folder" ],
                            "icon" : {
                                "image" : "http://localhost:8088/ProyILCE/img/perfiles10.png"
                            }
                        },
                        "usuarios" : {
                            "valid_children" : [ "campo", "folder" ],
                            "icon" : {
                                "image" : "http://localhost:8088/ProyILCE/img/usuarios2.png"
                            }
                        }
                    }
                }
            });

        });

    };

})(jQuery);
