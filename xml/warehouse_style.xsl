<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:tns="http://www.example.org/warehouse">

    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Warehouse Management System</title>
                <style>
                    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f4f9; color: #333; margin: 20px; }
                    h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
                    h2 { color: #2980b9; margin-top: 30px; }
                    table { width: 100%; border-collapse: collapse; margin-top: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); background: white; }
                    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
                    th { background-color: #34495e; color: white; font-weight: bold; }
                    tr:nth-child(even) { background-color: #f2f2f2; }
                    tr:hover { background-color: #e8f6f3; }
                    .status-free { color: green; font-weight: bold; }
                    .status-occupied { color: red; font-weight: bold; }
                    .status-maintenance { color: orange; font-weight: bold; }
                    .money { font-family: 'Courier New', monospace; font-weight: bold; }
                    .contact { font-style: italic; color: #555; }
                    .section-badge { background: #95a5a6; color: white; padding: 2px 6px; border-radius: 4px; font-size: 0.8em; }

                    /* List styling */
                    ul { list-style-type: square; margin: 0; padding-left: 20px; }
                </style>
            </head>
            <body>
                <h1>Warehouse System Dashboard</h1>

                <!-- USERS TABLE -->
                <h2>Users</h2>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Login</th>
                        <th>Contact</th>
                    </tr>
                    <xsl:for-each select="tns:WarehouseSystem/tns:Users/tns:User">
                        <tr>
                            <td><xsl:value-of select="@userId"/></td>
                            <td>
                                <span style="font-weight: bold;">
                                    <xsl:value-of select="tns:LastName"/>
                                </span>,
                                <xsl:value-of select="tns:FirstName"/>
                            </td>
                            <td><xsl:value-of select="tns:Login"/></td>
                            <td class="contact">
                                <xsl:choose>
                                    <xsl:when test="tns:ContactInfo/tns:Phone">
                                        Phone: <xsl:value-of select="tns:ContactInfo/tns:Phone"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        Email: <xsl:value-of select="tns:ContactInfo/tns:Email"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>

                <!-- RACKS TABLE (With Filtering for FREE vs OCCUPIED) -->
                <h2>Racks Storage</h2>
                <p>Showing detailed rack information sorted by price.</p>
                <table>
                    <tr>
                        <th>ID / Section</th>
                        <th>Type</th>
                        <th>Status</th>
                        <th>Price/Day</th>
                        <th>Dimensions (WxHxD)</th>
                        <th>Tenant</th>
                    </tr>
                    <xsl:for-each select="tns:WarehouseSystem/tns:Racks/tns:Rack">
                        <xsl:sort select="tns:PricePerDay" data-type="number" order="ascending"/>
                        <tr>
                            <td>
                                <b>#<xsl:value-of select="tns:Number"/></b>
                                <br/>
                                <span class="section-badge">
                                    <xsl:choose>
                                        <xsl:when test="@sectionCode">
                                            Sec: <xsl:value-of select="@sectionCode"/>
                                        </xsl:when>
                                        <xsl:otherwise>No Section</xsl:otherwise>
                                    </xsl:choose>
                                </span>
                            </td>
                            <td><xsl:value-of select="tns:Type"/></td>
                            <td>
                                <xsl:attribute name="class">
                                    <xsl:choose>
                                        <xsl:when test="tns:Status='FREE'">status-free</xsl:when>
                                        <xsl:when test="tns:Status='OCCUPIED'">status-occupied</xsl:when>
                                        <xsl:otherwise>status-maintenance</xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>
                                <xsl:value-of select="tns:Status"/>
                            </td>
                            <td class="money"><xsl:value-of select="tns:PricePerDay"/> UAH</td>
                            <td>
                                <xsl:value-of select="tns:Dimensions/tns:Width"/>x
                                <xsl:value-of select="tns:Dimensions/tns:Height"/>x
                                <xsl:value-of select="tns:Dimensions/tns:Depth"/>
                            </td>
                            <td>
                                <xsl:if test="tns:TenantId">
                                    User: <xsl:value-of select="tns:TenantId"/>
                                </xsl:if>
                                <xsl:if test="not(tns:TenantId)">-</xsl:if>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>

                <!-- ORDERS TABLE (Filtered: Show only ACTIVE orders) -->
                <h2>Active Orders</h2>
                <table>
                    <tr>
                        <th>Order ID</th>
                        <th>User</th>
                        <th>Dates</th>
                        <th>Type</th>
                        <th>Assigned Racks</th>
                    </tr>
                    <!-- FILTER: Only show ACTIVE orders -->
                    <xsl:for-each select="tns:WarehouseSystem/tns:Orders/tns:Order[tns:Status='ACTIVE']">
                        <tr>
                            <td><xsl:value-of select="@orderId"/></td>
                            <td><xsl:value-of select="tns:UserId"/></td>
                            <td>
                                From: <xsl:value-of select="tns:StartDate"/> <br/>
                                To: <xsl:value-of select="tns:EndDate"/>
                            </td>
                            <td><xsl:value-of select="tns:DesiredType"/></td>
                            <td>
                                <ul>
                                    <xsl:for-each select="tns:AssignedRacks/tns:RackRef">
                                        <li><xsl:value-of select="."/></li>
                                    </xsl:for-each>
                                </ul>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>