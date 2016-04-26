<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : menu_app.xsl
    Created on : 26 de octubre de 2011, 10:45
    Author     : Daniel
    Description:
        crear el html necesario para el widget menu
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <html>
            <head>
                <title>menu_app.xsl</title>
            </head>
            <body>
              <xsl:for-each select="qry/registro">
              <a href="#" id="showEntity_{@clave_aplicacion}_{@clave_forma}" class='menu' nueva_entidad="{@alias_menu_nueva_entidad}" edita_entidad="{@alias_menu_mostrar_entidad}" >"><xsl:value-of select="qry/registro/forma"/></a>
              </xsl:for-each>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
