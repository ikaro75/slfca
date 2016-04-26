<%-- 
    Document   : report
    Created on : 07-may-2012, 11:17:28
    Author     : Daniel
--%>
<%@page import="java.util.ArrayList" %><%@page import="mx.org.fide.modelo.*"
                                               %><%@page import="mx.org.fide.reporte.*"
                                               %><%@page import="java.text.SimpleDateFormat" 
                                               %><%@page import="java.text.NumberFormat"
%><%@page import="java.text.DecimalFormat"%><%

    response.setContentType("text/html; charset=ISO-8859-1");
    request.setCharacterEncoding("UTF8");

    String error = "";
    int forma = 0;
    String tipoAccion = "";
    String w = "";
    String source = "";
    int reporte = 0;
    Reporte r = new Reporte();
    ArrayList<Reporte> reportes = new ArrayList<Reporte>();

    Usuario user = (Usuario) request.getSession().getAttribute("usuario");

    if (user == null) {
        request.getRequestDispatcher("/index.jsp");
    }

    try {

        if (request.getParameter("$cr") == null) {
            error = "Falta parámetro $cr";
        } else {
            try {
                reporte = Integer.parseInt(request.getParameter("$cr"));
            } catch (Exception e) {
                throw new Fallo("El parámetro $cr no es válido, verifique");
            }
        }

        //Recupera los datos del reporte con el setter
        r.setClaveReporte(reporte, user.getClavePerfil(), user.getCx());
        r.ejecutaConsulta(user.getCx());

        forma = r.getClaveForma();
        user.setForma(forma, user.getClavePerfil());

    } catch (Fallo f) {
        error = f.getMessage();

    } catch (Exception e) {
        error = e.getMessage();
    }
%><!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" media="screen" href="css/vista.css"/>
        <title><%=user.getForma(forma).getForma()%> / Reportes</title>
        <script type="text/javascript" src="jQuery/jqPlot/jquery.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/jquery.jqplot.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.pointLabels.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.dateAxisRenderer.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.canvasTextRenderer.min.js"></script>
        <script type="text/javascript" src="jQuery/jqPlot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
        <link rel="stylesheet" type="text/css" hrf="jQuery/jqPlot/jquery.jqplot.min.css" />    
        <style>
            .jqplot-table-legend td div div {
                border-style: solid;
            }
            .jqplot-table-legend-label {
                font-family:sans-serif; font-size: 9px;
            }
            table.reporte {
                width: 90%;
                margin: auto;
            }

            td.reporte {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
            }

            #clave_reporte {
                font-weight:300 ; font-family:Verdana, Arial, Helvetica, sans-serif; font-size: 20px; width: 100%; border-bottom-color: #FFFFFF; border-bottom-style:none;
            }
        </style>
        <script>
            $(document).ready(function(){ <%
                int i = 0;
                StringBuilder listaDeSeries = new StringBuilder();
                StringBuilder listaDeEtiquetas = new StringBuilder();
                StringBuilder listaDeVariables = new StringBuilder();
                StringBuilder listaDeColores = new StringBuilder("");
                StringBuilder tabla = new StringBuilder();

                tabla.append("<table class='reporte'><tr><td class='reporte'><strong>Símbolo</strong></td><td class='reporte'><strong>").append(r.getTituloTick()).append("</strong></td>");
                for (Serie s : r.getSeries()) {
                    tabla.append("<td class='reporte'><strong>").append(s.getEtiqueta()).append("</strong></td>");
                }
                tabla.append("</tr>");

                i = 0;
                for (Tick t : r.getTicks()) {
                    tabla.append("<tr><td class='reporte'>").append(t.getSimbolo()).append("</td><td class='reporte'>").append(t.getTick()).append("</td>");
                    for (Serie s : r.getSeries()) {
                        if (r.getFormatoDatosNumericos() == null) {
                            tabla.append("<td class='reporte'>").append(s.getDatos().get(i)).append("</td>");
                        } else {
                            if (r.getFormatoDatosNumericos().equals("")) {
                                tabla.append("<td class='reporte'>").append(s.getDatos().get(i)).append("</td>");
                            } else {
                                NumberFormat formater = new DecimalFormat(r.getFormatoDatosNumericos());
                                tabla.append("<td class='reporte'>").append(formater.format(s.getDatos().get(i))).append("</td>");
                            }
                        }

                    }
                    tabla.append("</tr>");

                    i++;
                }


                tabla.append("</table>");
                listaDeColores.append("seriesColors: [");

                i = 0;
                for (Serie s : r.getSeries()) {
                    listaDeSeries.append("s").append(i).append(",");
                    listaDeEtiquetas.append("{label:'").append(s.getEtiqueta().replaceAll("'", "")).append("'},");
                    if (s.getColor() != null) {
                        listaDeColores.append("'").append(s.getColor()).append("',");
                    } else {
                        listaDeColores = new StringBuilder("");;
                    }

                    listaDeVariables.append("var s").append(i).append("=").append("[");
                    for (Float f : s.getDatos()) {
                        listaDeVariables.append(f).append(",");
                    }

                    listaDeVariables.deleteCharAt(listaDeVariables.lastIndexOf(",")).append("];");
                    i++;
                }

                listaDeSeries.deleteCharAt(listaDeSeries.lastIndexOf(","));
                listaDeEtiquetas.deleteCharAt(listaDeEtiquetas.lastIndexOf(","));
                if (!listaDeColores.toString().equals("")) {
                    listaDeColores.deleteCharAt(listaDeColores.lastIndexOf(",")).append("],");
                }
            %>
            <%=listaDeVariables%>
                    // Can specify a custom tick Array.
                    // Ticks should match up one for each y value (category) in the series.
                    var ticks = [<%=r.imprimeSimbolos()%>];

                    var plot1 = $.jqplot('chart1', [<%=listaDeSeries%>], {
                        // The "seriesDefaults" option is an options object that will
                        // be applied to all series in the chart.
            <% if (!listaDeColores.equals("")) {
                               out.print(listaDeColores);
                           }%>
                                       seriesDefaults:{
                                           renderer:$.jqplot.BarRenderer,
                                           rendererOptions: {fillToZero: true}
                                       },
                                       stackSeries: true,
                                       // Custom labels for the series are specified with the "label"
                                       // option on the series option.  Here a series option object
                                       // is specified for each series.
                                       series:[<%=listaDeEtiquetas%>
                                       ],
                                       // Show the legend and put it outside the grid, but inside the
                                       // plot container, shrinking the grid to accomodate the legend.
                                       // A value of "outside" would not shrink the grid and allow
                                       // the legend to overflow the container.
                                       legend: {
                                           show: true,
                                           location: 'w', 
                                           placement: 'outsideGrid',
                                           showSwatches : true
                                       },
                                       axesDefaults: {
                                           tickRenderer: $.jqplot.CanvasAxisTickRenderer
                                       },                   
                                       axes: {
                                           // Use a category axis on the x axis and use our custom ticks.
                                           xaxis: {
                                               renderer: $.jqplot.CategoryAxisRenderer,
                                               ticks: ticks,
                                               tickOptions: {
                                                   angle: -30,
                                                   fontSize: '9pt'
                                               }               
                                           },
                                           // Pad the y axis just a little so bars can get close to, but
                                           // not touch, the grid boundaries.  1.2 is the default padding.
                                           yaxis: {
                                               pad: 1.2,
                                               autoscale:true,
                                               tickOptions: {formatString: '%d'}
                                           }
                                       }
                                   });
                               });        
        </script>
    </head>
    <body>
        <%
            i = 0;
            for (Reporte re : user.getForma(forma).getReportes(user.getClavePerfil(), user.getCx())) {
                if (i == 0) {%>
        <select id="clave_reporte" onchange="javascript:document.location.href=this.value" >
            <%  }%>
            <option value="control?$cmd=report&$cr=<%=re.getClaveReporte()%>" <% if (Integer.parseInt(request.getParameter("$cr")) == re.getClaveReporte()) {%>selected="selected"<% }%> ><%=re.getReporte()%> </option>
            <% i++;
                }%>
        </select>
        <br />
        <div style="float:right; clear:both; height: 20px;" ><input type="button" style="font-size: 10px" onClick="window.print();" value="Imprimir" /></div>
            <% if (!error.equals("")) {%>
        <p id="msjLogin"><span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span><%=error%></p>
        <% }%>
        <br />
        <div id='chart1' style="width:80%; height: 80%; margin:auto; "></div>
        <div>
            <%=tabla%>          
        </div>
        <br />
    </body>
</html>