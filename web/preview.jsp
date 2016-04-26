<%@page import="java.util.ArrayList"%>
<%@page import="mx.org.fide.utilerias.LinkPreview"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Vista preliminar de link</title>
        <link rel="stylesheet" type="text/css" media="screen" href="css/vista.css"/>
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.6.4.min.js"></script>
        <script language="javascript">
        function back(backid,max){
            var backValue = backid;
            var decNext;
            document.getElementById("count").value = backValue+" de "+max;
            var getImage = document.getElementById('Image'+backValue).src;
            var nextValue = document.getElementsByName('nextButton')[0].id;
            decNext = nextValue - 1;
            if(decNext == 0){
                decNext = backValue;
            }
            var decBack = backValue - 1;
            if(decBack<=0)
            {
                decBack = max;
            }
            document.getElementById(backid).id = decBack;
            document.getElementsByName("nextButton")[0].id = decNext;
            document.getElementById("jqImage").src=getImage;
        }
        function next(nextid,max){
            var nextValue = nextid;
            var incBack;
            document.getElementById("count").value = nextValue+" de "+max;
            var getImage = document.getElementById('Image'+nextValue).src;
            var backValue = document.getElementsByName("backButton")[0].id;
            incBack = parseInt(backValue)+ 1;
            if(incBack > max){
                incBack = parseInt(nextValue) - 1 ;
                if(incBack == 0){
                    incBack = 1;
                }
            }
            var incNext = parseInt(nextValue) + 1;
            if(incNext > max){
                incNext = 2;
            }
            document.getElementById(nextid).id = incNext;
            document.getElementsByName("backButton")[0].id = incBack;
            document.getElementById("jqImage").src=getImage;
        }

            </script>          
    </head>  
    <body>
        <div class="jq_content_inner">
                <%
                    String link = request.getParameter("link");
                    if (!link.startsWith("http://") && !link.startsWith("https://")) {
                        link = "http://" + link;
                    }
                    LinkPreview _LP = new LinkPreview();                            
                    String status = _LP.getWebPage(link);
                    String titulo = new String (_LP.title.getBytes(), "utf-8");
                %>
                <div style="overflow: hidden; margin-left: 10px;">
                    <b>
                        <a href="<%=link%>" style="font-size: 20px;color: gray;text-decoration: none; margin-bottom: 5px" target="_blank" ><%=titulo%></a>
                    </b>
                </div>
                <% 
                    String tip = request.getParameter("tip");
                    String linky = request.getParameter("idy");
                    if(tip.contentEquals("y")){ //Si contiene Y es decir que el link es de youtube 
                        String desctube = new String (_LP.description.getBytes(), "utf-8");            
                %>
                <div id="todo">
                    <div id="video" style="float: left;width:200px;height:100px;border:1px solid #000;">
                        <!-- Aquí va el video -->
                        <iframe width="200" height="100"  src="http://www.youtube.com/embed/<%=linky%>" frameborder="0" allowfullscreen></iframe>
                    </div>
                    <div style="float: left;display: inline-block;width: 300px;vertical-align: top; margin-left: 5px""><%=desctube%></div>
                </div>
                <input type="hidden" id="LinkContent" name="LinkContent"/>
                <br /><br /><br><br /><br /><br /><br />
                <%}else{%>
                <%
                    ArrayList<String> Image = _LP.images;
                    if (Image.size()>0) {
                %>
                <div  id="jqImageDIV" style="display: inline-block;width: 210px; height: 110px; overflow: hidden;background-color: white;vertical-align: middle; margin-left: 5px;">
                    <img src="<%=Image.get(1)%>" id="jqImage" alt="Image" *width="200px" style="margin: 5px;vertical-align: middle"/>
                    <div id="jq_allImages">
                        <%
                            for (int i = 1; i < Image.size(); i++) {
                        %>
                        <img id="Image<%=i%>" src="<%=Image.get(i)%>" style="display: none" alt="Images"/>
                        <%   
                            }
                        %>
                    </div>
                </div>
                <%
                    }
                    String descripcion = new String (_LP.description.getBytes(), "utf-8");
                    if (Image.size() > 1) {
                %>
                    <div style="display: inline-block;width: 280px;vertical-align: top; margin-left: 5px;">
                        <%
                    } else {
                        %>
                        <div style="display: inline-block;width: auto;vertical-align: top">
                        <%
                    }
                        %>
                        <%=descripcion%>
                        <br/>
                        <div id="jq_console">
                            <%
                                if (Image.size() > 1) {
                            %>
                            <div id="jq_back-next">
                                <%--<input type="checkbox" id="jq_removeImage"/> Remover Imagen <font style="font-size: 10px">(*Large image will take time to load)</font>--%>
                                <br/>
                                <input type="text" id="count" value="1 de <%=Image.size()%>" style="border: none;background-color: transparent;width: 50px" disabled="true"/>
                                <input type="image" src="img/back.png" id="<%=Image.size()%>" name="backButton" onclick="back(this.id, <%=Image.size()%>);"/> <input type="image" src="img/next.png" id="2" name="nextButton" onclick="next(this.id, <%=Image.size()%>);">
                            </div>
                            <%
                                }
                            %>
                            <!--<input type="button" value="Quitar" id="jq_attach"/>-->
                            <br/>
                            <font style="font-size: 12px" id="status"><%=status%></font>
                            <br/>
                            <%--<%=result%>--%>
                        </div>
                    </div>
                </div>
            </div>
        <input type="hidden" id="LinkContent" name="LinkContent"/>
        
        <%}%>
    </body>
</html>
