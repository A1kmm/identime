<%@ page language="java" contentType="application/xrds+xml"
    pageEncoding="UTF-8"%><?xml version="1.0" encoding="UTF-8"?>
<xrds:XRDS
xmlns:xrds="xri://$xrds"
xmlns:openid="http://openid.net/xmlns/1.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="xri://$xrds https://openid4java.googlecode.com/svn-history/r2/trunk/lib/xrds.xsd
                    xri://$xrd*($v*2.0) https://openid4java.googlecode.com/svn-history/r2/trunk/lib/xrd.xsd"
xmlns="xri://$xrd*($v*2.0)">
  <XRD>
      <Service priority="1">
       <Type>http://specs.openid.net/auth/2.0/signon</Type>
       <URI>${baseURL}/provider</URI>
    </Service>
    <Service priority="0">
       <Type>http://openid.net/signon/1.0</Type>
       <URI>${baseURL}/provider</URI>
    </Service>
  </XRD>
</xrds:XRDS>