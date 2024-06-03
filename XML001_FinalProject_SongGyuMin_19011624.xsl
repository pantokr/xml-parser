<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="uri:xsl">

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="epl">
        <html>
            <head>
                <title> EPL Players' Status </title>
            </head>
            <body>
                <div>
                    <xsl:apply-templates />
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="body">
        <div align="center">
            <xsl:apply-templates />
        </div>
    </xsl:template>

    <xsl:template match="h2">
        <div>
            <h2>
                <xsl:value-of />
            </h2>
        </div>
    </xsl:template>

    <xsl:template match="p">
        <div>
            <xsl:value-of />
        </div>
    </xsl:template>

    <xsl:template match="picture">
        <div>
            <img>
                <xsl:attribute name="src">
                    <xsl:value-of select="@src" />
                </xsl:attribute>
                <xsl:attribute name="width">
                    <xsl:value-of select="@width" />
                </xsl:attribute>
                <xsl:attribute name="height">
                    <xsl:value-of select="@height" />
                </xsl:attribute>
                <xsl:attribute name="alt">
                    <xsl:value-of select="@alt" />
                </xsl:attribute>
            </img>
        </div>
    </xsl:template>

    <xsl:template match="list">

        <xsl:for-each select="caption">
            <caption>
                <xsl:value-of />
            </caption>
        </xsl:for-each>

        <table style="margin-top:1em">

            <xsl:attribute name="border">
                <xsl:value-of select="@border" />
            </xsl:attribute>
            <xsl:attribute name="width">
                <xsl:value-of select="@width" />
            </xsl:attribute>
            <xsl:attribute name="height">
                <xsl:value-of select="@height" />
            </xsl:attribute>

            <tr>
                <th>Number</th>
                <th>Name</th>
                <th>Club</th>
                <th>Position</th>
                <th>Age</th>
                <th>Height</th>
                <th>Weight</th>
                <th>GK</th>
            </tr>

            <xsl:for-each select="player">
                <tr>
                    <td style="text-align:center">
                        <xsl:eval>order_function()</xsl:eval>
                    </td>
                    <td>
                        <xsl:value-of select="name" />
                    </td>
                    <td>
                        <xsl:value-of select="club" />
                    </td>
                    <td>
                        <xsl:value-of select="position" />
                    </td>
                    <td>
                        <xsl:value-of select="age" />
                    </td>
                    <td>
                        <xsl:value-of select="height" />
                    </td>
                    <td>
                        <xsl:value-of select="weight" />
                    </td>
                    <td>
                        <xsl:if test="position[.$eq$'GK']">
                            GK 
                        </xsl:if>
                        <xsl:if test="position[.$ne$'GK']"></xsl:if>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>

    <xsl:script>
        <![CDATA[
            order = 1 
            function order_function(){
                return order++;
            }
        ]]>
    </xsl:script>

</xsl:stylesheet>