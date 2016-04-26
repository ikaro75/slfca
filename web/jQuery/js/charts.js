function pieEntregasPorEstado(claveFormaDetalle, valorDeReemplazo, titulo, divId) {
    $.jqplot.config.enablePlugins = true;
    $.getJSON("pie.jsp?$cf="+ claveFormaDetalle + "&$ta=children", function (data) {
        var plot1 = jQuery.jqplot(divId, [data],
                {
                    title: titulo,
                    seriesDefaults: {
                        renderer: $.jqplot.PieRenderer,
                        rendererOptions: { padding: 20, sliceMargin: 2, showDataLabels: true }
                     },
                     legend:{
                        show:true , 
                        location:'e',
                        marginTop: '15px'
                    },
                    seriesColors:['#21DD1F','#FFB200']
                }
        );

        $("#loader_" + divId).remove();
    }).fail(function (jqxhr, textStatus, error) {
        var err = textStatus + ", " + error;
        console.log("Request Failed: " + err);
    });
}

function barrasAvancesX() {
var plot2 = $.jqplot('grafica_avances_por_estado', [
        [[2, 1], [4, 2], [6, 3], [3, 4]],
        [[5, 1], [1, 2], [3, 3], [4, 4]],
        [[4, 1], [7, 2], [1, 3], [2, 4]]], {
        seriesDefaults: {
            renderer: $.jqplot.BarRenderer,
            // Show point labels to the right ('e'ast) of each bar.
            // edgeTolerance of -15 allows labels flow outside the grid
            // up to 15 pixels.  If they flow out more than that, they 
            // will be hidden.
            pointLabels: {show: true, location: 'e', edgeTolerance: -15},
            // Rotate the bar shadow as if bar is lit from top right.
            shadowAngle: 135,
            // Here's where we tell the chart it is oriented horizontally.
            rendererOptions: {
                barDirection: 'horizontal'
            }
        },
        axes: {
            yaxis: {
                renderer: $.jqplot.CategoryAxisRenderer
            }
        }
    });    
}


function barrasAvancesXX(claveFormaDetalle, valorDeReemplazo, titulo, divId) {
    $.jqplot.config.enablePlugins = true;
    $.getJSON( "chart.jsp?$cf=" + claveFormaDetalle + "&$ta=foreign&$vr="+encodeURIComponent(valorDeReemplazo)+"&$ts=2", function(data) {
            
            //El primer valor son las etiquetas, el segundo los valores de las barras
             for (var i=0, len=data.length; i < len; i++) {
               console.log(data[i]);
            }
            
            plot2 = $.jqplot(divId, [data[1]], {
                title:titulo,
                animate: !$.jqplot.use_excanvas,
                series:[{color: '#ff4466'}],
                seriesDefaults: {
                    /*show: true,     */
                    renderer: $.jqplot.BarRenderer ,/*
                    shadowAngle: 135,
                    rendererOptions: { barDirection: 'vertical' }, */
                    pointLabels: { show: true,  /*location: 'e', edgeTolerance: -15*/ }
                },
                axes: {
                    xaxis:{renderer:$.jqplot.CategoryAxisRenderer,
                           ticks:data[0]}
                      },
                highlighter: { show: false }      
            });

            }).fail(function( jqxhr, textStatus, error ) {
                var err = textStatus + ", " + error;
                console.log( "Request Failed: " + err );
            });
            
}

function graficaAvanceAcumulado(claveFormaDetalle, valorDeReemplazo, titulo, divId) {
    $.getJSON( "chart.jsp?$cf=" + claveFormaDetalle + "&$ta=foreign&$vr="+encodeURIComponent(valorDeReemplazo)+"&$ts=2", function(data) {
            
            //El primer valor son las etiquetas, el segundo los valores de las barras
             for (var i=0, len=data.length; i < len; i++) {
               console.log(data[i]);
            }
            
            plot2 = $.jqplot(divId, [data[1]], {
            title:titulo,
            animate: !$.jqplot.use_excanvas,
            series:[{color: '#21DD1F'}],
            seriesDefaults: {
                show: true,     
                renderer: $.jqplot.BarRenderer,
                shadowAngle: 135,
                rendererOptions: { barDirection: 'horizontal' },
                pointLabels: { show: true, location: 'e', edgeTolerance: -15 }
            },
            axes: {
                yaxis:{renderer:$.jqplot.CategoryAxisRenderer,
                       ticks:data[0]}
                  }
            });

            }).fail(function( jqxhr, textStatus, error ) {
                var err = textStatus + ", " + error;
                console.log( "Request Failed: " + err );
            });
            
}

function graficaAvanceUltimaCarga(claveFormaDetalle, valorDeReemplazo, titulo, divId) {
    $.getJSON( "chart.jsp?$cf=" + claveFormaDetalle + "&$ta=log&$vr="+encodeURIComponent(valorDeReemplazo)+"&$ts=2", function(data) {
            
            //El primer valor son las etiquetas, el segundo los valores de las barras
             for (var i=0, len=data.length; i < len; i++) {
               console.log(data[i]);
            }
            
            plot2 = $.jqplot(divId, [data[1]], {
            title:titulo,
            animate: !$.jqplot.use_excanvas,
            series:[{color: '#21DD1F'}],
            seriesDefaults: {
                show: true,     
                renderer: $.jqplot.BarRenderer,
                shadowAngle: 135,
                rendererOptions: { barDirection: 'horizontal' },
                pointLabels: { show: true, location: 'e', edgeTolerance: -15 }
            },
            axes: {
                yaxis:{renderer:$.jqplot.CategoryAxisRenderer,
                       ticks:data[0]}
                  }
            });
            
            $("#loader_" + divId).remove();
            
            }).fail(function( jqxhr, textStatus, error ) {
                var err = textStatus + ", " + error;
                console.log( "Request Failed: " + err );
            });
            
            
}